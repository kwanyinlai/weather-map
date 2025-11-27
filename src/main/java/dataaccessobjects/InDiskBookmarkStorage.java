package dataaccessobjects;

import dataaccessinterface.BookmarkedLocationStorage;
import entity.BookmarkedLocation;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Disk-backed implementation of {@link BookmarkedLocationStorage} that persists
 * bookmarks as a JSON array in a single file. Public methods are synchronized,
 * reads are tolerant of missing/empty files, and writes are performed atomically
 * (temp file + move).
 */
public final class InDiskBookmarkStorage implements BookmarkedLocationStorage {

    private static final String JSON_FIELD_NAME = "name";
    private static final String JSON_FIELD_LATITUDE = "latitude";
    private static final String JSON_FIELD_LONGITUDE = "longitude";

    private final Path filePath;

    /**
     * Creates a storage instance that persists bookmarks to the given file.
     *
     * @param filePath Path to the JSON file used for persistence.
     */
    public InDiskBookmarkStorage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all bookmarked locations from disk.
     *
     * @return An immutable list of {@link BookmarkedLocation} reconstructed from
     * the JSON file; returns an empty list if the file is missing or empty.
     * @throws org.json.JSONException If the on-disk JSON is malformed.
     */
    @Override
    public synchronized List<BookmarkedLocation> getBookmarkedLocations() {
        JSONArray arr = readArray();
        List<BookmarkedLocation> out = new ArrayList<>(arr.length());

        for (int i = 0; i < arr.length(); i++) {
            out.add(convertJsonEntryToBookmark(arr.getJSONObject(i)));
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Appends a new bookmark and persists the updated collection to disk.
     *
     * @param b The {@link BookmarkedLocation} to add.
     * @throws RuntimeException If writing to disk fails.
     */
    @Override
    public synchronized void addBookmarkedLocation(BookmarkedLocation b) {
        JSONArray arr = readArray();
        arr.put(convertBookmarkToJsonEntry(b));
        writeArray(arr);
    }

    /**
     * Removes all persisted bookmarks that match the given entity by name and
     * latitude/longitude, then persists the updated collection.
     *
     * @param b The bookmark to remove.
     * @return {@code true} if at least one matching bookmark was found and removed; {@code false} otherwise.
     * @throws dataaccessobjects.BookmarkPersistenceException If writing to disk fails.
     */
    @Override
    public synchronized boolean removeBookmarkedLocation(BookmarkedLocation b) {
        JSONArray arr = readArray();
        boolean removed = false;

        // Iterate backwards to avoid index issues when removing elements
        for (int i = arr.length() - 1; i >= 0; i--) {
            JSONObject obj = arr.getJSONObject(i);

            if (jsonMatchesBookmark(obj, b)) {
                arr.remove(i);
                removed = true;
            }
        }

        // Only write if something was removed
        if (removed) {
            writeArray(arr);
        }

        return removed;
    }

    // ---------- Helper Methods ----------

    /**
     * Reads the JSON array of bookmarks from the persistence file.
     *
     * @return A {@link JSONArray} of bookmarks, or an empty array if the file
     * does not exist or contains no data.
     * @throws org.json.JSONException If the file contains malformed JSON.
     */
    private JSONArray readArray() {
        try {
            if (!Files.exists(filePath)) {
                return new JSONArray();
            }

            String json = Files.readString(filePath, StandardCharsets.UTF_8).trim();

            if (json.isEmpty()) {
                return new JSONArray();
            }

            return new JSONArray(json);

        } catch (IOException e) {
            // Fail-safe on read: treat as empty array
            return new JSONArray();
        }
    }

    /**
     * Persists the provided JSON array to disk atomically. Creates parent
     * directories as needed.
     *
     * @param arr The {@link JSONArray} representing all bookmarks to persist.
     * @throws dataaccessobjects.BookmarkPersistenceException if saving fails.
     */
    private void writeArray(JSONArray arr) {
        try {
            Files.createDirectories(filePath.getParent());
            // atomic write: write to temp and move over the target
            Path tmp = Files.createTempFile(filePath.getParent(), "bookmarks", ".tmp");
            Files.writeString(tmp, arr.toString(2), StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(tmp, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

        } catch (IOException e) {
            throw new BookmarkPersistenceException("Failed to persist bookmarks to " + filePath, e);
        }
    }

    /**
     * Serializes a {@link BookmarkedLocation} to a JSON object compatible with
     * the on-disk schema.
     *
     * @param b The bookmark to serialize.
     * @return A {@link JSONObject} with fields: {@code name}, {@code latitude},
     * {@code longitude}, and {@code savedTime}.
     */
    private JSONObject convertBookmarkToJsonEntry(BookmarkedLocation b) {
        JSONObject obj = new JSONObject();

        obj.put(JSON_FIELD_NAME, b.getName());
        obj.put(JSON_FIELD_LATITUDE, b.getLatitude());
        obj.put(JSON_FIELD_LONGITUDE, b.getLongitude());

        return obj;
    }

    /**
     * Deserializes a JSON object from the on-disk schema into a
     * {@link BookmarkedLocation}.
     *
     * @param obj A {@link JSONObject} containing fields: {@code name},
     *            {@code latitude}, {@code longitude}, and {@code savedTime}.
     * @return A reconstructed {@link BookmarkedLocation}.
     * @throws org.json.JSONException                  If required fields are missing or of the wrong type.
     * @throws java.time.format.DateTimeParseException If {@code savedTime} cannot be parsed.
     */
    private BookmarkedLocation convertJsonEntryToBookmark(JSONObject obj) {
        double lat = obj.getDouble(JSON_FIELD_LATITUDE);
        double lon = obj.getDouble(JSON_FIELD_LONGITUDE);
        String name = obj.getString(JSON_FIELD_NAME);

        return new BookmarkedLocation(name, lat, lon);
    }

    /**
     * Checks whether a persisted JSON object corresponds to the given entity by
     * comparing name and coordinates.
     *
     * @param obj The JSON object from storage.
     * @param b   The in-memory bookmark to match.
     * @return {@code true} if both name and coordinates match; {@code false} otherwise.
     */
    private boolean jsonMatchesBookmark(JSONObject obj, BookmarkedLocation b) {
        String name = obj.optString(JSON_FIELD_NAME, "");
        double lat = obj.optDouble(JSON_FIELD_LATITUDE, Double.NaN);
        double lon = obj.optDouble(JSON_FIELD_LONGITUDE, Double.NaN);

        return name.equals(b.getName())
                && Double.compare(lat, b.getLatitude()) == 0
                && Double.compare(lon, b.getLongitude()) == 0;
    }
}