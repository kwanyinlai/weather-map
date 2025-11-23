package view;
import interfaceadapter.mapinteraction.MapViewModel;
import interfaceadapter.mapinteraction.PanAndZoomController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.*;

// PanAndZoomView.java
public class PanAndZoomView extends JPanel {
    private final JMapViewer mapViewer;
    private final MapViewModel  viewModel;
    private final JLabel errorLabel;
    public PanAndZoomView(MapViewModel mapViewModel, JMapViewer mapViewer) {
        this.viewModel = mapViewModel;
        this.mapViewer = mapViewer;
        this.errorLabel = new JLabel(" ");
        mapViewer.setZoomContolsVisible(true);
        mapViewer.setPreferredSize(new Dimension(600, 600));
        setLayout(new BorderLayout());
        add(mapViewer, BorderLayout.CENTER);
        add(errorLabel, BorderLayout.SOUTH);
        errorLabel.setForeground(Color.RED);
        //this.viewModel.addPropertyChangeListener(this::onViewModelChanged);
        mapViewer.setFocusable(true);
        mapViewer.requestFocusInWindow();
        mapViewer.setRequestFocusEnabled(true);
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