package usecase.bookmark;

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
import usecase.weatherlayers.update.UpdateOverlayInputBoundary;

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

        public void reset() {
            successData = null;
            failureMessage = null;
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

        public void reset() {
            successData = null;
            failureMessage = null;
        }
    }

    /**
     * Test double for VisitBookmarkOutputBoundary that records calls.
     */
    private static class TestVisitBookmarkPresenter implements VisitBookmarkOutputBoundary {
        private VisitBookmarkOutputData successData;
        private String failureMessage;

        @Override
        public void present(VisitBookmarkOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void presentVisitBookmarkFailure(String errorMessage) {
            this.failureMessage = errorMessage;
        }

        public VisitBookmarkOutputData getSuccessData() {
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
            assertNotEquals(b1, b3);
            assertNotEquals(b1, b4);
            assertNotEquals(b1, null);
            assertNotEquals(b1, "not a bookmark");
        }

        @Test
        void testHashCode() {
            BookmarkedLocation b1 = new BookmarkedLocation("Home", 45.5, -73.5);
            BookmarkedLocation b2 = new BookmarkedLocation("Home", 45.5, -73.5);
            assertEquals(b1.hashCode(), b2.hashCode());
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
            storage.addBookmarkedLocation(bookmark); // Duplicate

            boolean removed = storage.removeBookmarkedLocation(bookmark);
            assertTrue(removed);

            List<BookmarkedLocation> result = storage.getBookmarkedLocations();
            assertTrue(result.isEmpty());
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
        void testAddBookmarkWithNaNCoordinates() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", Double.NaN, -73.5);
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
        void testAddBookmarkDuplicate() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestAddBookmarkPresenter presenter = new TestAddBookmarkPresenter();
            AddBookmarkUseCase useCase = new AddBookmarkUseCase(storage, presenter);

            AddBookmarkInputData input = new AddBookmarkInputData("Home", 45.5, -73.5);
            useCase.addBookmark(input);
            presenter.reset();

            // Try to add the same bookmark again
            useCase.addBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("A bookmark with the same name and location already exists.",
                    presenter.getFailureMessage());
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

            // Test valid boundary values
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

            List<BookmarkedLocation> bookmarks = presenter.getSuccessData().getBookmarks();
            assertThrows(UnsupportedOperationException.class, () -> bookmarks.add(
                    new BookmarkedLocation("Work", 46.0, -74.0)));
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
        void testRemoveBookmarkNotFound() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", 45.5, -73.5);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmarks not found.", presenter.getFailureMessage());
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
        void testRemoveBookmarkWithNaNCoordinates() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", Double.NaN, -73.5);
            useCase.removeBookmark(input);

            assertNull(presenter.getSuccessData());
            assertEquals("Bookmark coordinates are invalid.", presenter.getFailureMessage());
        }

        @Test
        void testRemoveBookmarkWithOutOfRangeCoordinates() {
            InMemoryBookmarkStorage storage = new InMemoryBookmarkStorage();
            TestRemoveBookmarkPresenter presenter = new TestRemoveBookmarkPresenter();
            RemoveBookmarkUseCase useCase = new RemoveBookmarkUseCase(storage, presenter);

            RemoveBookmarkInputData input = new RemoveBookmarkInputData("Home", 91.0, -73.5);
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
                    viewport, updateOverlay, panZoomPresenter, errorPresenter);

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
                    viewport, updateOverlay, panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(Double.NaN, -73.5);
            useCase.visitBookmark(input);

            assertNull(panZoomPresenter.getCapturedData());
            assertEquals(0, updateOverlay.getUpdateCallCount());
            assertEquals("Invalid bookmark coordinates.", errorPresenter.getFailureMessage());
        }

        @Test
        void testVisitBookmarkWithOutOfRangeLatitude() {
            entity.Viewport viewport = new entity.Viewport(0.0, 0.0, 800, 5, 15, 0, 600);
            TestUpdateOverlayUseCase updateOverlay = new TestUpdateOverlayUseCase();
            TestPanAndZoomPresenter panZoomPresenter = new TestPanAndZoomPresenter();
            TestVisitBookmarkPresenter errorPresenter = new TestVisitBookmarkPresenter();
            VisitBookmarkUseCase useCase = new VisitBookmarkUseCase(
                    viewport, updateOverlay, panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(91.0, -73.5);
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
                    viewport, updateOverlay, panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(45.5, 181.0);
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
                    viewport, updateOverlay, panZoomPresenter, errorPresenter);

            // Test valid boundary values
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
                    viewport, updateOverlay, panZoomPresenter, errorPresenter);

            VisitBookmarkInputData input = new VisitBookmarkInputData(45.5, -73.5);
            useCase.visitBookmark(input);

            // Viewport should have been updated with pixel coordinates
            assertNotNull(panZoomPresenter.getCapturedData());
            entity.Viewport updatedViewport = panZoomPresenter.getCapturedData().getViewport();
            assertEquals(viewport, updatedViewport);
        }
    }
}

