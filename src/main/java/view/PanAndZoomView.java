package view;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

// PanAndZoomView.java
public class PanAndZoomView extends JPanel implements PropertyChangeListener {
    private final JMapViewer mapViewer;

    public PanAndZoomView(JMapViewer mapViewer) {
        this.mapViewer = mapViewer;
        JLabel errorLabel = new JLabel(" ");
        mapViewer.setZoomContolsVisible(true);
        mapViewer.setPreferredSize(new Dimension(600, 600));
        setLayout(new BorderLayout());
        add(mapViewer, BorderLayout.CENTER);
        add(errorLabel, BorderLayout.SOUTH);
        errorLabel.setForeground(Color.RED);
        mapViewer.setFocusable(true);
        mapViewer.requestFocusInWindow();
        mapViewer.setRequestFocusEnabled(true);
        this.setBounds(0, 0, 600, 600);
        mapViewer.setBounds(0, 0, 600, 600);
    }

    public JMapViewer getMapViewer() {
        return mapViewer;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //Listener listening to size change in MapOverlayStructureView and listening to viewmodel's change.
        if(Objects.equals(evt.getPropertyName(), "size")) {
            this.setBounds(new Rectangle((Dimension)evt.getNewValue()));
            mapViewer.setBounds(new Rectangle((Dimension)evt.getNewValue()));
            mapViewer.setSize((Dimension) evt.getNewValue());
        }
    }

    /**
     * set the initial location of the map on startup
     */
    public void setMapLocation(int zoom, int x, int y){
        mapViewer.setZoom(zoom);
        mapViewer.setCenter(new Point(x, y));
    }

}