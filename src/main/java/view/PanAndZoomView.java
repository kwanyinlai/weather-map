package view;
import interfaceadapter.mapinteraction.MapViewModel;
import interfaceadapter.mapinteraction.PanAndZoomController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.*;

// PanAndZoomView.java
public class PanAndZoomView extends JPanel {
    private final JMapViewer mapViewer;
    private final MapViewModel mapViewModel;

    public PanAndZoomView(MapViewModel mapViewModel) {
        this.mapViewModel = mapViewModel;
        this.mapViewer = new JMapViewer();
        initMap();
        this.add(mapViewer);
        this.setBounds(0, 0, 600, 600);
        mapViewer.setBounds(0, 0, 600, 600);
    }

    private void initMap() {
        mapViewer.setZoomContolsVisible(true);
        mapViewer.setPreferredSize(new Dimension(600, 600));
    }

    public JMapViewer getMapViewer() {
        return mapViewer;
    }

    public void setController(PanAndZoomController controller) {
    }
}