package interfaceadapter.mapinteraction;

import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import usecase.mapinteraction.PanAndZoomInputBoundary;
import usecase.mapinteraction.PanAndZoomInputData;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import java.awt.*;
import java.awt.event.*;
public class PanAndZoomController implements JMapViewerEventListener {
    private final PanAndZoomInputBoundary useCase;
    private final JMapViewer mapViewer;

    public PanAndZoomController(PanAndZoomInputBoundary useCase, JMapViewer mapViewer) {
        this.useCase = useCase;
        this.mapViewer = mapViewer;
        setupListeners();
    }

    private void setupListeners() {
        mapViewer.addJMVListener(this);
        mapViewer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                syncViewport();
            }
        });
        syncViewport();
    }

    private void syncViewport() {
        int zoom = mapViewer.getZoom();
        Point center = mapViewer.getCenter();
        int width = mapViewer.getWidth();
        int height = mapViewer.getHeight();
        PanAndZoomInputData input = new PanAndZoomInputData(zoom, center.x, center.y, width, height);
        useCase.updateViewport(input);
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        if (command.getCommand() == JMVCommandEvent.COMMAND.MOVE ||
                command.getCommand() == JMVCommandEvent.COMMAND.ZOOM) {
            syncViewport();
        }
    }

}

