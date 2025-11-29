import dataaccessinterface.BookmarkedLocationStorage;
import dataaccessobjects.BookmarkPersistenceException;
import dataaccessobjects.InDiskBookmarkStorage;
import entity.BookmarkedLocation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import usecase.bookmark.addbookmark.*;
import usecase.bookmark.listbookmark.*;
import usecase.bookmark.removebookmark.*;
import usecase.bookmark.visitbookmark.*;
import usecase.weatherlayers.updateoverlay.UpdateOverlayUseCase;
import usecase.mapnavigation.PanAndZoomOutputBoundary;
import usecase.mapnavigation.PanAndZoomOutputData;
import interfaceadapter.bookmark.addbookmark.AddBookmarkController;
import interfaceadapter.bookmark.addbookmark.AddBookmarkPresenter;
import interfaceadapter.bookmark.removebookmark.RemoveBookmarkController;
import interfaceadapter.bookmark.removebookmark.RemoveBookmarkPresenter;
import interfaceadapter.bookmark.listbookmark.ListBookmarksController;
import interfaceadapter.bookmark.listbookmark.ListBookmarksPresenter;
import interfaceadapter.bookmark.visitbookmark.VisitBookmarkController;
import interfaceadapter.bookmark.visitbookmark.VisitBookmarkPresenter;
import interfaceadapter.bookmark.BookmarksViewModel;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for bookmark-related use cases, entities, and storage.
 */
class BookmarksUseCaseTest {

    /**
     * In-memory implementation of BookmarkedLocationStorage for testing.
     */
    private static class InMemoryBookmarkStorage implements BookmarkedLocationStorage {
        private final List<BookmarkedLocation> bookmarks = new ArrayList<>();

        @Override
        public List<BookmarkedLocation> getBookmarkedLocations() {
            return List.copyOf(bookmarks);
        }

        @Override
        public void addBookmarkedLocation(BookmarkedLocation bookmarkedLocation) {
            bookmarks.add(bookmarkedLocation);
        }

        @Override
        public boolean removeBookmarkedLocation(BookmarkedLocation bookmarkedLocation) {
            boolean removed = false;
            for (int i = bookmarks.size() - 1; i >= 0; i--) {
                BookmarkedLocation b = bookmarks.get(i);
                if (b.getName().equals(bookmarkedLocation.getName())
                        && Double.compare(b.getLatitude(), bookmarkedLocation.getLatitude()) == 0
                        && Double.compare(b.getLongitude(), bookmarkedLocation.getLongitude()) == 0) {
                    bookmarks.remove(i);
                    removed = true;
                }
            }
            return removed;
        }
    }

    /**
     * Failing storage implementation that throws exceptions.
     */
    private static class FailingBookmarkStorage implements BookmarkedLocationStorage {
        @Override
        public List<BookmarkedLocation> getBookmarkedLocations() {
            throw new RuntimeException("Storage read failure");
        }

        @Override
        public void addBookmarkedLocation(BookmarkedLocation bookmarkedLocation) {
            throw new RuntimeException("Storage write failure");
        }

        @Override
        public boolean removeBookmarkedLocation(BookmarkedLocation bookmarkedLocation) {
            throw new BookmarkPersistenceException("Storage remove failure", new RuntimeException());
        }
    }

    /**
     * Test double for AddBookmarkOutputBoundary that records calls.
     */
    private static class TestAddBookmarkPresenter implements AddBookmarkOutputBoundary {
        private AddBookmarkOutputData successData;
        private String failureMessage;

        @Override
        public void presentAddedBookmark(AddBookmarkOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void presentAddBookmarkFailure(String errorMessage) {
            this.failureMessage = errorMessage;
        }

        public AddBookmarkOutputData getSuccessData() {
            return successData;
        }

        public String getFailureMessage() {
            return failureMessage;
        }

        public void reset() {
            successData = null;
            failureMessage = null;
        }
    }

    /**
     * Test double for ListBookmarksOutputBoundary that records calls.
     */
    private static class TestListBookmarksPresenter implements ListBookmarksOutputBoundary {
        private ListBookmarksOutputData successData;
        private String failureMessage;

        @Override
        public void presentBookmarks(ListBookmarksOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void presentListBookmarksFailure(String errorMessage) {
            this.failureMessage = errorMessage;
        }

        public ListBookmarksOutputData getSuccessData() {
            return successData;
        }

        public String getFailureMessage() {
            return failureMessage;
        }
    }

    /**
     * Test double for RemoveBookmarkOutputBoundary that records calls.
     */
    private static class TestRemoveBookmarkPresenter implements RemoveBookmarkOutputBoundary {
        private RemoveBookmarkOutputData successData;
        private String failureMessage;

        @Override
        public void presentRemovedBookmark(RemoveBookmarkOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void presentRemoveBookmarkFailure(String errorMessage) {
            this.failureMessage = errorMessage;
        }

        public RemoveBookmarkOutputData getSuccessData() {
            return successData;
        }

        public String getFailureMessage() {
            return failureMessage;
        }
    }

    /**
     * Test double for VisitBookmarkOutputBoundary that records calls.
     */
    private static class TestVisitBookmarkPresenter implements VisitBookmarkOutputBoundary {
        private String failureMessage;

        @Override
        public void present(VisitBookmarkOutputData outputData) {
       
        }

        @Override
        public void presentVisitBookmarkFailure(String errorMessage) {
            this.failureMessage = errorMessage;
        }

        public String getFailureMessage() {
            return failureMessage;
        }
    }

    private static class TestUpdateOverlayUseCase {
        private int updateCallCount = 0;
        private final UpdateOverlayUseCase mockInstance;

        public TestUpdateOverlayUseCase() {
            mockInstance = Mockito.mock(UpdateOverlayUseCase.class);
            Mockito.doAnswer(invocation -> {
                updateCallCount++;
                return null;
            }).when(mockInstance).update();
        }

        public UpdateOverlayUseCase getInstance() {
            return mockInstance;
        }

        public int getUpdateCallCount() {
            return updateCallCount;
        }
    }

    /**
     * Test double for PanAndZoomOutputBoundary that records calls.
     */
    private static class TestPanAndZoomPresenter implements PanAndZoomOutputBoundary {
        private PanAndZoomOutputData capturedData;

        @Override
        public void present(PanAndZoomOutputData outputData) {
            this.capturedData = outputData;
        }

        public PanAndZoomOutputData getCapturedData() {
            return capturedData;
        }
    }

    // ========== Entity Tests ==========

    @Nested
    class BookmarkedLocationTests {
        @Test
        void testConstructorAndGetters() {
            BookmarkedLocation bookmark = new BookmarkedLocation("Home", 45.5, -73.5);
            assertEquals("Home", bookmark.getName());
            assertEquals(45.5, bookmark.getLatitude());
            assertEquals(-73.5, bookmark.getLongitude());
        }

        @Test
        void testEquals() {
            BookmarkedLocation b1 = new BookmarkedLocation("Home", 45.5, -73.5);
            BookmarkedLocation b2 = new BookmarkedLocation("Home", 45.5, -73.5);
            BookmarkedLocation b3 = new BookmarkedLocation("Work", 45.5, -73.5);
            BookmarkedLocation b4 = new BookmarkedLocation("Home", 46.0, -73.5);

            assertEquals(b1, b2);
            assertNotEquals(b3, b1);
            assertEquals(b4, b1);
            assertNotEquals(null, b1);

            @SuppressWarnings("EqualsBetweenInconvertibleTypes")
            boolean equalsResult = b1.equals("not a bookmark");
            assertFalse(equalsResult);
        }

        @Test
        void testHashCode() {
            BookmarkedLocation b1 = new BookmarkedLocation("Home", 45.5, -73.5);
            BookmarkedLocation b2 = new BookmarkedLocation("Home", 45.5, -73.5);
            assertEquals(b1.hashCode(), b2.hashCode());
        }

        @Test
        void testEqualsWithNull() {
            BookmarkedLocation b1 = new BookmarkedLocation("Home", 45.5, -73.5);
            assertNotEquals(null, b1);
        }

        @Test
        void testEqualsWithDifferentClass() {
            BookmarkedLocation b1 = new BookmarkedLocation("Home", 45.5, -73.5);
            @SuppressWarnings("EqualsBetweenInconvertibleTypes")
            boolean equalsString = b1.equals("not a bookmark");
            assertFalse(equalsString);
            assertNotEquals(new Object(), b1);
        }
    }

    // ========== Storage Tests ==========

    @Nested
    class InDiskBookmarkStorageTests {
        @Test
        void testGetBookmarkedLocationsWhenFileDoesNotExist(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("bookmarks.json");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void testAddAndGetBookmarkedLocation(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("bookmarks.json");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            BookmarkedLocation bookmark = new BookmarkedLocation("Home", 45.5, -73.5);
            storage.addBookmarkedLocation(bookmark);

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertEquals(1, result.size());
            assertEquals(bookmark.getName(), result.get(0).getName());
            assertEquals(bookmark.getLatitude(), result.get(0).getLatitude());
            assertEquals(bookmark.getLongitude(), result.get(0).getLongitude());
        }

        @Test
        void testAddMultipleBookmarks(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("bookmarks.json");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            storage.addBookmarkedLocation(new BookmarkedLocation("Home", 45.5, -73.5));
            storage.addBookmarkedLocation(new BookmarkedLocation("Work", 46.0, -74.0));

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertEquals(2, result.size());
        }

        @Test
        void testRemoveBookmarkedLocation(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("bookmarks.json");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            BookmarkedLocation bookmark = new BookmarkedLocation("Home", 45.5, -73.5);
            storage.addBookmarkedLocation(bookmark);

            boolean removed = storage.removeBookmarkedLocation(bookmark);
            assertTrue(removed);

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertTrue(result.isEmpty());
        }

        @Test
        void testRemoveNonExistentBookmark(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("bookmarks.json");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            BookmarkedLocation bookmark = new BookmarkedLocation("Home", 45.5, -73.5);
            boolean removed = storage.removeBookmarkedLocation(bookmark);
            assertFalse(removed);
        }

        @Test
        void testRemoveAllMatchingBookmarks(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("bookmarks.json");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            BookmarkedLocation bookmark = new BookmarkedLocation("Home", 45.5, -73.5);
            storage.addBookmarkedLocation(bookmark);
            storage.addBookmarkedLocation(bookmark); 

            boolean removed = storage.removeBookmarkedLocation(bookmark);
            assertTrue(removed);

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertTrue(result.isEmpty());
        }

        @Test
        void testGetBookmarkedLocationsWithEmptyFile(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("bookmarks.json");
            Files.writeString(filePath, "");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertTrue(result.isEmpty());
        }

        @Test
        void testGetBookmarkedLocationsHandlesIOException(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("bookmarks.json");
        
            Files.createDirectory(filePath);
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertTrue(result.isEmpty());
        }

        @Test
        void testAddBookmarkWithNullParentDirectory(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("bookmarks.json");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            BookmarkedLocation bookmark = new BookmarkedLocation("Home", 45.5, -73.5);

            storage.addBookmarkedLocation(bookmark);

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertEquals(1, result.size());
        }

        @Test
        void testRemoveBookmarkWithPartialMatch(@TempDir Path tempDir) {
            Path filePath = tempDir.resolve("bookmarks.json");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            storage.addBookmarkedLocation(new BookmarkedLocation("Home", 45.5, -73.5));
            storage.addBookmarkedLocation(new BookmarkedLocation("Home", 46.0, -74.0)); 
            storage.addBookmarkedLocation(new BookmarkedLocation("Work", 45.5, -73.5)); 

            BookmarkedLocation toRemove = new BookmarkedLocation("Home", 45.5, -73.5);
            boolean removed = storage.removeBookmarkedLocation(toRemove);
            assertTrue(removed);

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertEquals(2, result.size());
        }

        @Test
        void testGetBookmarkedLocationsWithMalformedJSON(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("bookmarks.json");
            Files.writeString(filePath, "invalid json {");
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            assertThrows(org.json.JSONException.class, storage::getBookmarkedLocations);
        }

        @Test
        void testGetBookmarkedLocationsWithMissingFields(@TempDir Path tempDir) throws Exception {
            Path filePath = tempDir.resolve("bookmarks.json");
            Files.writeString(filePath, "[{\"name\": \"Home\"}]"); 
            InDiskBookmarkStorage storage = new InDiskBookmarkStorage(filePath);

            assertThrows(org.json.JSONException.class, storage::getBookmarkedLocations);
        }
    }

    // ========== Add Bookmark Use Case Tests ==========

    @Nested
    class AddBookmarkTests {
        @Test
        void testAddBookmarkSuccess() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", 45.5, -73.5);
            useCase.addBookmark(input);

            assertNotNull(presenter.getSuccessData());
            assertEquals("Home", presenter.getSuccessData().getName());
            assertEquals(45.5, presenter.getSuccessData().getLatitude());
            assertEquals(-73.5, presenter.getSuccessData().getLongitude());
            assertNull(presenter.getFailureMessage());

            List<BookmarkedLocation> stored = storage.getBookmarkedLocations();
            assertEquals(1, stored.size());
        }

        @Test
        void testAddBookmarkWithEmptyName() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("", 45.5, -73.5);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertNotNull(presenter.getFailureMessage());
            assertEquals("Bookmark name must not be empty.", presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithBlankName() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("   ", 45.5, -73.5);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertNotNull(presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithNullName() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData(null, 45.5, -73.5);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertNotNull(presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithNaNLatitude() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", Double.NaN, -73.5);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are invalid.", presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithNaNLongitude() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", 45.5, Double.NaN);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are invalid.", presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithOutOfRangeLatitude() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", 91.0, -73.5);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are out of range.", presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithLatitudeLessThanMinus90() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", -91.0, -73.5);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are out of range.", presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithOutOfRangeLongitude() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", 45.5, 181.0);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are out of range.", presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithLongitudeLessThanMinus180() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", 45.5, -181.0);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are out of range.", presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkDuplicate() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", 45.5, -73.5);
            useCase.addBookmark(input);
            presenter.reset();

            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("A bookmark with the same name and location already exists.",
                    presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithSameNameButDifferentCoordinates() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input1 = new AddBookmarkInputData("Home", 45.5, -73.5);
            useCase.addBookmark(input1);
            presenter.reset();

            AddBookmarkInputData input2 = new AddBookmarkInputData("Home", 46.0, -74.0);
            useCase.addBookmark(input2);

            assertNotNull(presenter.getSuccessData());
            assertEquals(2, storage.getBookmarkedLocations().size());
        }

        @Test
        void testAddBookmarkWithSameCoordinatesButDifferentName() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input1 = new AddBookmarkInputData("Home", 45.5, -73.5);
            useCase.addBookmark(input1);
            presenter.reset();

            AddBookmarkInputData input2 = new AddBookmarkInputData("Work", 45.5, -73.5);
            useCase.addBookmark(input2);

            assertNotNull(presenter.getSuccessData());
            assertEquals(2, storage.getBookmarkedLocations().size());
        }

        @Test
        void testAddBookmarkWhenStorageReadFails() {
            FailingBookmarkStorage storage = new FailingBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", 45.5, -73.5);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Failed to read existing bookmarks.", presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWhenStorageWriteFails() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage() {
                @Override
                public void addBookmarkedLocation(BookmarkedLocation bookmarkedLocation) {
                    throw new RuntimeException("Write failure");
                }
            };
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", 45.5, -73.5);
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Failed to save bookmark.", presenter.getFailureMessage());
        }

        @Test
        void testAddBookmarkWithBoundaryCoordinates() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input1 = new AddBookmarkInputData("North", 90.0, 0.0);
            useCase.addBookmark(input1);
            assertNotNull(presenter.getSuccessData());
            presenter.reset();

            AddBookmarkInputData input2 = new AddBookmarkInputData("South", -90.0, 0.0);
            useCase.addBookmark(input2);
            assertNotNull(presenter.getSuccessData());
            presenter.reset();

            AddBookmarkInputData input3 = new AddBookmarkInputData("East", 0.0, 180.0);
            useCase.addBookmark(input3);
            assertNotNull(presenter.getSuccessData());
            presenter.reset();

            AddBookmarkInputData input4 = new AddBookmarkInputData("West", 0.0, -180.0);
            useCase.addBookmark(input4);
            assertNotNull(presenter.getSuccessData());
        }
    }

    // ========== List Bookmarks Use Case Tests ==========

    @Nested
    class ListBookmarksTests {
        @Test
        void testListBookmarksSuccess() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            storage.addBookmarkedLocation(new BookmarkedLocation("Home", 45.5, -73.5));
            storage.addBookmarkedLocation(new BookmarkedLocation("Work", 46.0, -74.0));

            TestListBookmarksPresenter presenter = new TestListBookmarksPresenter();
            ListBookmarksUseCase useCase = new ListBookmarksUseCase(storage, presenter);

            ListBookmarksInputData input = new ListBookmarksInputData();
            useCase.listBookmarks(input);

            assertNotNull(presenter.getSuccessData());
            List<BookmarkedLocation> bookmarks = presenter.getSuccessData().getBookmarks();
            assertEquals(2, bookmarks.size());
            assertNull(presenter.getFailureMessage());
        }

        @Test
        void testListBookmarksWhenEmpty() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestListBookmarksPresenter presenter = new TestListBookmarksPresenter();
            ListBookmarksUseCase useCase = new ListBookmarksUseCase(storage, presenter);

            ListBookmarksInputData input = new ListBookmarksInputData();
            useCase.listBookmarks(input);

            assertNotNull(presenter.getSuccessData());
            List<BookmarkedLocation> bookmarks = presenter.getSuccessData().getBookmarks();
            assertTrue(bookmarks.isEmpty());
            assertNull(presenter.getFailureMessage());
        }

        @Test
        void testListBookmarksWhenStorageFails() {
            FailingBookmarkStorage storage = new FailingBookmarkStorage();
            TestListBookmarksPresenter presenter = new TestListBookmarksPresenter();
            ListBookmarksUseCase useCase = new ListBookmarksUseCase(storage, presenter);

            ListBookmarksInputData input = new ListBookmarksInputData();
            useCase.listBookmarks(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Failed to load bookmarks.", presenter.getFailureMessage());
        }

        @Test
        void testListBookmarksOutputDataIsImmutable() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            storage.addBookmarkedLocation(new BookmarkedLocation("Home", 45.5, -73.5));

            TestListBookmarksPresenter presenter = new TestListBookmarksPresenter();
            ListBookmarksUseCase useCase = new ListBookmarksUseCase(storage, presenter);

            ListBookmarksInputData input = new ListBookmarksInputData();
            useCase.listBookmarks(input);

            final List<BookmarkedLocation> finalBookmarks = presenter.getSuccessData().getBookmarks();
            BookmarkedLocation newBookmark = new BookmarkedLocation("Work", 46.0, -74.0);

            assertThrows(UnsupportedOperationException.class,
                    () -> finalBookmarks.add(newBookmark));
        }
    }

    // ========== Remove Bookmark Use Case Tests ==========

    @Nested
    class RemoveBookmarkTests {
        @Test
        void testRemoveBookmarkSuccess() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            BookmarkedLocation bookmark = new BookmarkedLocation("Home", 45.5, -73.5);
            storage.addBookmarkedLocation(bookmark);

            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", 45.5, -73.5);
            useCase.removeBookmark(input);

            assertNotNull(presenter.getSuccessData());
            assertEquals("Home", presenter.getSuccessData().getName());
            assertTrue(presenter.getSuccessData().isRemoved());
            assertNull(presenter.getFailureMessage());

            List<BookmarkedLocation> stored = storage.getBookmarkedLocations();
            assertTrue(stored.isEmpty());
        }

        @Test
        void testRemoveBookmarkWithNullInput() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            useCase.removeBookmark(null);

            assertNull(presenter.getSuccessData());
            assertEquals("No bookmark data was provided.", presenter.getFailureMessage());
        }

        @Test
        void testRemoveBookmarkWithEmptyName() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("", 45.5, -73.5);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark name must not be empty.", presenter.getFailureMessage());
        }

        @Test
        void testRemoveBookmarkWithNaNLatitude() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", Double.NaN, -73.5);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are invalid.", presenter.getFailureMessage());
        }

        @Test
        void testRemoveBookmarkWithNaNLongitude() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", 45.5, Double.NaN);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are invalid.", presenter.getFailureMessage());
        }

        @Test
        void testRemoveBookmarkWithOutOfRangeLatitude() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", 91.0, -73.5);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are out of range.", presenter.getFailureMessage());
        }

        @Test
        void testRemoveBookmarkWithLatitudeLessThanMinus90() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", -91.0, -73.5);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are out of range.", presenter.getFailureMessage());
        }

        @Test
        void testRemoveBookmarkWithOutOfRangeLongitude() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", 45.5, 181.0);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are out of range.", presenter.getFailureMessage());
        }

        @Test
        void testRemoveBookmarkWithLongitudeLessThanMinus180() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", 45.5, -181.0);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are out of range.", presenter.getFailureMessage());
        }


        @Test
        void testRemoveBookmarkWhenStorageFails() {
            FailingBookmarkStorage storage = new FailingBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", 45.5, -73.5);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Failed to update bookmarks.", presenter.getFailureMessage());
        }
    }

    // ========== Visit Bookmark Use Case Tests ==========

    @Nested
    class VisitBookmarkTests {
        @Test
        void testVisitBookmarkSuccess() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(45.5, -73.5);
            useCase.visitBookmark(input);

            assertNotNull(panZoomPresenter.getCapturedData());
            assertTrue(panZoomPresenter.getCapturedData().isUpdated());
            assertEquals(1, updateOverlay.getUpdateCallCount());
            assertNull(errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkWithNaNLatitude() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(Double.NaN, -73.5);
            useCase.visitBookmark(input);

            assertNull(panZoomPresenter.getCapturedData());
            assertEquals(0, updateOverlay.getUpdateCallCount());
            assertEquals("Invalid bookmark coordinates.", errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkWithNaNLongitude() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(45.5, Double.NaN);
            useCase.visitBookmark(input);

            assertNull(panZoomPresenter.getCapturedData());
            assertEquals("Invalid bookmark coordinates.", errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkWithOutOfRangeLatitude() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(91.0, -73.5);
            useCase.visitBookmark(input);

            assertNull(panZoomPresenter.getCapturedData());
            assertEquals("Invalid bookmark coordinates.", errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkWithLatitudeLessThanMinus90() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(-91.0, -73.5);
            useCase.visitBookmark(input);

            assertNull(panZoomPresenter.getCapturedData());
            assertEquals("Invalid bookmark coordinates.", errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkWithOutOfRangeLongitude() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(45.5, 181.0);
            useCase.visitBookmark(input);

            assertNull(panZoomPresenter.getCapturedData());
            assertEquals("Invalid bookmark coordinates.", errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkWithLongitudeLessThanMinus180() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(45.5, -181.0);
            useCase.visitBookmark(input);

            assertNull(panZoomPresenter.getCapturedData());
            assertEquals("Invalid bookmark coordinates.", errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkWithBoundaryCoordinates() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(90.0, 180.0);
            useCase.visitBookmark(input);

            assertNotNull(panZoomPresenter.getCapturedData());
            assertNull(errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkUpdatesViewport() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(45.5, -73.5);
            useCase.visitBookmark(input);

            assertNotNull(panZoomPresenter.getCapturedData());
            entity.Viewport updatedViewport = panZoomPresenter.getCapturedData().getViewport();
            assertEquals(viewport, updatedViewport);
        }

        @Test
        void testVisitBookmarkWithNullUpdateOverlay() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, null, panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(45.5, -73.5);
            useCase.visitBookmark(input);

            assertNotNull(panZoomPresenter.getCapturedData());
            assertNull(errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkWithNegativeBoundaryValues() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay.getInstance(), panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(-90.0, -180.0);
            useCase.visitBookmark(input);

            assertNotNull(panZoomPresenter.getCapturedData());
            assertNull(errorPresenter.getFailureMessage());
        }
    }

    // ========== Controller Tests ==========

    @Nested
    class ControllerTests {
        @Test
        void testAddBookmarkController() {
            AddBookmarkInputBoundary mockUseCase = Mockito.mock(AddBookmarkInputBoundary.class);
            AddBookmarkController controller = new AddBookmarkController(mockUseCase);

            controller.addBookmark("Home", 45.5, -73.5);

            Mockito.verify(mockUseCase).addBookmark(Mockito.argThat(input ->
                    input.getName().equals("Home") &&
                    input.getLatitude() == 45.5 &&
                    input.getLongitude() == -73.5
            ));
        }

        @Test
        void testRemoveBookmarkController() {
            RemoveBookmarkInputBoundary mockUseCase = Mockito.mock(RemoveBookmarkInputBoundary.class);
            RemoveBookmarkController controller = new RemoveBookmarkController(mockUseCase);

            controller.removeBookmark("Home", 45.5, -73.5);

            Mockito.verify(mockUseCase).removeBookmark(Mockito.argThat(input ->
                    input.getName().equals("Home") &&
                    input.getLatitude() == 45.5 &&
                    input.getLongitude() == -73.5
            ));
        }

        @Test
        void testListBookmarksController() {
            ListBookmarksInputBoundary mockUseCase = Mockito.mock(ListBookmarksInputBoundary.class);
            ListBookmarksController controller = new ListBookmarksController(mockUseCase);

            controller.listBookmarks();

            Mockito.verify(mockUseCase).listBookmarks(Mockito.any(ListBookmarksInputData.class));
        }

        @Test
        void testVisitBookmarkController() {
            VisitBookmarkInputBoundary mockUseCase = Mockito.mock(VisitBookmarkInputBoundary.class);
            VisitBookmarkController controller = new VisitBookmarkController(mockUseCase);

            controller.visitBookmark(45.5, -73.5);

            Mockito.verify(mockUseCase).visitBookmark(Mockito.argThat(input ->
                    input.getLatitude() == 45.5 &&
                    input.getLongitude() == -73.5
            ));
        }
    }

    // ========== Presenter Tests ==========

    @Nested
    class PresenterTests {
        @Test
        void testAddBookmarkPresenterSuccess() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            AddBookmarkPresenter presenter = new AddBookmarkPresenter(viewModel);

            AddBookmarkOutputData outputData = new AddBookmarkOutputData("Home", 45.5, -73.5);
            presenter.presentAddedBookmark(outputData);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertNotNull(state);
            assertEquals(1, state.getBookmarks().size());
            assertEquals("Home", state.getBookmarks().get(0).getName());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testAddBookmarkPresenterWithExistingBookmarks() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            viewModel.setBookmarks(List.of(new BookmarkedLocation("Work", 46.0, -74.0)));
            AddBookmarkPresenter presenter = new AddBookmarkPresenter(viewModel);

            AddBookmarkOutputData outputData = new AddBookmarkOutputData("Home", 45.5, -73.5);
            presenter.presentAddedBookmark(outputData);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals(2, state.getBookmarks().size());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testAddBookmarkPresenterFailure() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            AddBookmarkPresenter presenter = new AddBookmarkPresenter(viewModel);

            presenter.presentAddBookmarkFailure("Error message");

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals("Error message", state.getErrorMessage());
        }

        @Test
        void testRemoveBookmarkPresenterSuccess() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            viewModel.setBookmarks(List.of(
                    new BookmarkedLocation("Home", 45.5, -73.5),
                    new BookmarkedLocation("Work", 46.0, -74.0)
            ));
            RemoveBookmarkPresenter presenter = new RemoveBookmarkPresenter(viewModel);

            RemoveBookmarkOutputData outputData = new RemoveBookmarkOutputData("Home", 45.5, -73.5, true);
            presenter.presentRemovedBookmark(outputData);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals(1, state.getBookmarks().size());
            assertEquals("Work", state.getBookmarks().get(0).getName());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testRemoveBookmarkPresenterWithNoMatchingBookmarks() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            viewModel.setBookmarks(List.of(
                    new BookmarkedLocation("Work", 46.0, -74.0)
            ));
            RemoveBookmarkPresenter presenter = new RemoveBookmarkPresenter(viewModel);

            RemoveBookmarkOutputData outputData = new RemoveBookmarkOutputData("Home", 45.5, -73.5, true);
            presenter.presentRemovedBookmark(outputData);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals(1, state.getBookmarks().size());
            assertEquals("Work", state.getBookmarks().get(0).getName());
        }

        @Test
        void testRemoveBookmarkPresenterWithNullState() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            RemoveBookmarkPresenter presenter = new RemoveBookmarkPresenter(viewModel);

            RemoveBookmarkOutputData outputData = new RemoveBookmarkOutputData("Home", 45.5, -73.5, true);
            presenter.presentRemovedBookmark(outputData);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertTrue(state.getBookmarks().isEmpty());
        }

        @Test
        void testAddBookmarkPresenterWithNullState() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            AddBookmarkPresenter presenter = new AddBookmarkPresenter(viewModel);

            AddBookmarkOutputData outputData = new AddBookmarkOutputData("Home", 45.5, -73.5);
            presenter.presentAddedBookmark(outputData);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals(1, state.getBookmarks().size());
        }

        @Test
        void testRemoveBookmarkPresenterNotRemoved() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            RemoveBookmarkPresenter presenter = new RemoveBookmarkPresenter(viewModel);

            RemoveBookmarkOutputData outputData = new RemoveBookmarkOutputData("Home", 45.5, -73.5, false);
            presenter.presentRemovedBookmark(outputData);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals("The bookmark could not be removed.", state.getErrorMessage());
        }

        @Test
        void testRemoveBookmarkPresenterFailure() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            RemoveBookmarkPresenter presenter = new RemoveBookmarkPresenter(viewModel);

            presenter.presentRemoveBookmarkFailure("Error message");

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals("Error message", state.getErrorMessage());
        }

        @Test
        void testListBookmarksPresenterSuccess() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            ListBookmarksPresenter presenter = new ListBookmarksPresenter(viewModel);

            List<BookmarkedLocation> bookmarks = List.of(
                    new BookmarkedLocation("Home", 45.5, -73.5),
                    new BookmarkedLocation("Work", 46.0, -74.0)
            );
            ListBookmarksOutputData outputData = new ListBookmarksOutputData(bookmarks);
            presenter.presentBookmarks(outputData);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals(2, state.getBookmarks().size());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testListBookmarksPresenterFailure() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            ListBookmarksPresenter presenter = new ListBookmarksPresenter(viewModel);

            presenter.presentListBookmarksFailure("Error message");

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals("Error message", state.getErrorMessage());
        }

        @Test
        void testVisitBookmarkPresenterSuccess() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            viewModel.setErrorMessage("Previous error");
            VisitBookmarkPresenter presenter = new VisitBookmarkPresenter(viewModel);

            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            VisitBookmarkOutputData outputData = new VisitBookmarkOutputData(viewport, true);
            presenter.present(outputData);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertNull(state.getErrorMessage());
        }

        @Test
        void testVisitBookmarkPresenterFailure() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            VisitBookmarkPresenter presenter = new VisitBookmarkPresenter(viewModel);

            presenter.presentVisitBookmarkFailure("Error message");

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals("Error message", state.getErrorMessage());
        }
    }

    // ========== ViewModel Tests ==========

    @Nested
    class ViewModelTests {
        @Test
        void testBookmarksViewModelInitialization() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertNotNull(state);
            assertTrue(state.getBookmarks().isEmpty());
            assertNull(state.getErrorMessage());
        }

        @Test
        void testBookmarksViewModelSetBookmarks() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            List<BookmarkedLocation> bookmarks = List.of(
                    new BookmarkedLocation("Home", 45.5, -73.5)
            );
            viewModel.setBookmarks(bookmarks);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals(1, state.getBookmarks().size());
        }

        @Test
        void testBookmarksViewModelSetBookmarksWithNull() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            viewModel.setBookmarks(null);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertTrue(state.getBookmarks().isEmpty());
        }

        @Test
        void testBookmarksViewModelSetErrorMessage() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            viewModel.setErrorMessage("Error message");

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals("Error message", state.getErrorMessage());
        }

        @Test
        void testBookmarksViewModelSetErrorMessagePreservesBookmarks() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            viewModel.setBookmarks(List.of(new BookmarkedLocation("Home", 45.5, -73.5)));
            viewModel.setErrorMessage("Error message");

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals(1, state.getBookmarks().size());
            assertEquals("Error message", state.getErrorMessage());
        }

        @Test
        void testBookmarksViewModelSetState() {
            BookmarksViewModel viewModel = new BookmarksViewModel();
            BookmarksViewModel.BookmarksState newState = new BookmarksViewModel.BookmarksState(
                    List.of(new BookmarkedLocation("Home", 45.5, -73.5)),
                    null
            );
            viewModel.setState(newState);

            BookmarksViewModel.BookmarksState state = viewModel.getState();
            assertEquals(1, state.getBookmarks().size());
        }

        @Test
        void testBookmarksStateGetters() {
            List<BookmarkedLocation> bookmarks = List.of(new BookmarkedLocation("Home", 45.5, -73.5));
            BookmarksViewModel.BookmarksState state = new BookmarksViewModel.BookmarksState(bookmarks, "Error");

            assertEquals(1, state.getBookmarks().size());
            assertEquals("Error", state.getErrorMessage());
        }

        @Test
        void testBookmarksStateWithNullBookmarks() {
            BookmarksViewModel.BookmarksState state = new BookmarksViewModel.BookmarksState(null, null);
            assertTrue(state.getBookmarks().isEmpty());
            assertNull(state.getErrorMessage());
        }
    }
}

