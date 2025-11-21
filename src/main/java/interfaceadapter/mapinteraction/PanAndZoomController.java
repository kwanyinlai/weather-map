package interfaceadapter.mapinteraction;

import usecase.mapinteraction.PanAndZoomInputBoundary;
import usecase.mapinteraction.PanAndZoomInputData;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import java.awt.event.*;


public class PanAndZoomController extends MouseAdapter implements MouseWheelListener {
    private final PanAndZoomInputBoundary useCase;
    private final JMapViewer mapViewer;
    private int lastMouseX;
    private int lastMouseY;

    public PanAndZoomController(PanAndZoomInputBoundary useCase, JMapViewer mapViewer) {
        this.useCase = useCase;
        this.mapViewer = mapViewer;
        bindEvents();
    }

    private void bindEvents() {
        mapViewer.addMouseWheelListener(this);
        mapViewer.addMouseListener(this);
        mapViewer.addMouseMotionListener(this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int zoomIncrement = e.getWheelRotation() < 0 ? 1 : -1;
        PanAndZoomInputData input = new PanAndZoomInputData(zoomIncrement);
        useCase.handleZoom(input);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        double dx = (double)lastMouseX - e.getX();
        double dy = (double)lastMouseY - e.getY();
        lastMouseX = e.getX();
        lastMouseY = e.getY();
        PanAndZoomInputData input = new PanAndZoomInputData(dx, dy);
        useCase.handlePan(input);
    }
}