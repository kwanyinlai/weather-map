package usecase.mapinteraction;

import entity.Viewport;

public class PanAndZoomOutputData {
    private final Viewport newViewport;
    private final String feedback;
    private final boolean isSuccess;
    public PanAndZoomOutputData(Viewport newViewport, String feedback, boolean isSuccess) {
        this.newViewport = newViewport;
        this.feedback = feedback;
        this.isSuccess = isSuccess;
    }
    public Viewport getNewViewport() { return newViewport; }
    public String getFeedback() { return feedback; }
    public boolean isSuccess() { return isSuccess; }
}
