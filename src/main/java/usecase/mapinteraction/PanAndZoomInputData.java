package usecase.mapinteraction;

public class PanAndZoomInputData {
    private final int zoomLevel;
    private final int pixelCenterX;
    private final int pixelCenterY;
    private final int viewWidth;
    private final int viewHeight;

    public PanAndZoomInputData(int zoomLevel, int pixelCenterX, int pixelCenterY, int viewWidth, int viewHeight) {
        this.zoomLevel = zoomLevel;
        this.pixelCenterX = pixelCenterX;
        this.pixelCenterY = pixelCenterY;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public int getPixelCenterX() {
        return pixelCenterX;
    }

    public int getPixelCenterY() {
        return pixelCenterY;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }
}
