package interfaceadapter.bookmark.visitbookmark;

import interfaceadapter.bookmark.BookmarksViewModel;
import usecase.bookmark.visitbookmark.VisitBookmarkOutputBoundary;
import usecase.bookmark.visitbookmark.VisitBookmarkOutputData;

/**
 * Presenter for the "visit bookmark" use case.
 * For now used to surface errors.
 */
public final class VisitBookmarkPresenter implements VisitBookmarkOutputBoundary {

    private final BookmarksViewModel viewModel;

    public VisitBookmarkPresenter(BookmarksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(VisitBookmarkOutputData outputData) {
        // On success, clear any error message.
        viewModel.setErrorMessage(null);
    }

    @Override
    public void presentVisitBookmarkFailure(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}
