package usecase.bookmark.visitbookmark;

import entity.Viewport;

public final class VisitBookmarkOutputData {

    private final Viewport viewport;
    private final boolean success;

    public VisitBookmarkOutputData(Viewport viewport, boolean success) {
        this.viewport = viewport;
        this.success = success;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public boolean isSuccess() {
        return success;
    }
}
