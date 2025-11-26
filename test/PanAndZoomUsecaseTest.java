import entity.Viewport;
import usecase.mapinteraction.*;
import org.junit.jupiter.api.Test;
import java.awt.Dimension;
import static org.junit.jupiter.api.Assertions.*;

class PanAndZoomUsecaseTest {
    private static class TestPresenter implements PanAndZoomOutputBoundary {
        private PanAndZoomOutputData capturedOutputData;

        @Override
        public void present(PanAndZoomOutputData outputData) {
            this.capturedOutputData = outputData;
        }

        public PanAndZoomOutputData getCapturedOutputData() {
            return capturedOutputData;
        }
    }

    @Test
    void testUpdateViewportUpdatesAllProperties() {
        Viewport realViewport = new Viewport(
                300.0, 300.0,
                800,
                5,
                10,
                0,
                600
        );
        TestPresenter testPresenter = new TestPresenter();
        PanAndZoomUseCase useCase = new PanAndZoomUseCase(realViewport, testPresenter);


        int testZoom = 8;
        int testCenterX = 500;
        int testCenterY = 400;
        int testMapWidth = 1000;
        int testMapHeight = 700;
        PanAndZoomInputData inputData = new PanAndZoomInputData(
                testZoom, testCenterX, testCenterY, testMapWidth, testMapHeight
        );
        useCase.updateViewport(inputData);


        assertEquals(testZoom, realViewport.getZoomLevel());
        assertEquals(testCenterX, realViewport.getPixelCenterX());
        assertEquals(testCenterY, realViewport.getPixelCenterY());
        assertEquals(testMapWidth, realViewport.getViewWidth());
        assertEquals(testMapHeight, realViewport.getViewHeight());
        Dimension updatedSize = new Dimension(realViewport.getViewWidth(), realViewport.getViewHeight());
        assertEquals(new Dimension(testMapWidth, testMapHeight), updatedSize);
    }


    @Test
    void testUpdateViewportPassesCorrectOutputToPresenter() {

        PanAndZoomOutputData capturedOutput = getPanAndZoomOutputData();
        assertNotNull(capturedOutput, "Presenter did not accept OutputData");


        Viewport outputViewport = capturedOutput.getViewport();
        assertEquals(6, outputViewport.getZoomLevel());
        assertEquals(450.0, outputViewport.getPixelCenterX());
        assertEquals(350.0, outputViewport.getPixelCenterY());
        assertEquals(800, outputViewport.getViewWidth());
        assertEquals(600, outputViewport.getViewHeight());
    }

    private static PanAndZoomOutputData getPanAndZoomOutputData() {
        Viewport realViewport = new Viewport(200.0, 200.0, 600, 3, 15, 0, 500);
        TestPresenter testPresenter = new TestPresenter();
        PanAndZoomUseCase useCase = new PanAndZoomUseCase(realViewport, testPresenter);

        PanAndZoomInputData inputData = new PanAndZoomInputData(6, 450, 350, 800, 600);

        useCase.updateViewport(inputData);

        return testPresenter.getCapturedOutputData();
    }

    @Test
    void testUpdateViewportHandlesBoundaryValues() {
        Viewport realViewport = new Viewport(300.0, 300.0, 800, 5,
                10, 0, 600);
        TestPresenter testPresenter = new TestPresenter();
        PanAndZoomUseCase useCase = new PanAndZoomUseCase(realViewport, testPresenter);

        PanAndZoomInputData boundaryInput = new PanAndZoomInputData(11, -50, 1000,
                800, 600);
        useCase.updateViewport(boundaryInput);


        assertEquals(11, realViewport.getZoomLevel());

        assertEquals(-50.0, realViewport.getPixelCenterX());
        assertEquals(1000.0, realViewport.getPixelCenterY());
        assertTrue(realViewport.getViewWidth() > 0 && realViewport.getViewHeight() > 0);
    }

    @Test
    void testUpdateViewportWithBoundedZoom() {

        Viewport realViewport = new Viewport(300.0, 300.0, 800, 5,
                10, 0, 600);
        TestPresenter testPresenter = new TestPresenter();
        PanAndZoomUseCase useCase = new PanAndZoomUseCase(realViewport, testPresenter);


        int offset = 8;
        int boundedZoom = realViewport.getBounedZoom(offset);


        PanAndZoomInputData inputData = new PanAndZoomInputData(boundedZoom, 500, 400,
                800, 600);
        useCase.updateViewport(inputData);

        assertEquals(10, realViewport.getZoomLevel());
    }
}
