package view;
import interfaceadapter.mapinteraction.MapViewModel;
import interfaceadapter.mapinteraction.PanAndZoomController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import entity.Location;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;

public class PanAndZoomView extends JPanel {
    private final JMapViewer mapViewer;
    private final MapViewModel  viewModel;
    private final JLabel errorLabel;
    public PanAndZoomView(MapViewModel mapViewModel) {
        this.viewModel = mapViewModel;
        this.mapViewer = new JMapViewer();
        this.errorLabel = new JLabel(" ");
        mapViewer.setZoomContolsVisible(true);
        mapViewer.setPreferredSize(new Dimension(600, 600));
        setLayout(new BorderLayout());
        add(mapViewer, BorderLayout.CENTER);
        add(errorLabel, BorderLayout.SOUTH);
        errorLabel.setForeground(Color.RED);
        this.viewModel.addPropertyChangeListener(this::onViewModelChanged);
        mapViewer.setFocusable(true);
        mapViewer.requestFocusInWindow();
    }
    public void setController(PanAndZoomController controller) {
        MouseListener[] mouseListeners = mapViewer.getMouseListeners();
        for (MouseListener listener : mouseListeners) {
            mapViewer.removeMouseListener(listener);
        }
        MouseMotionListener[] motionListeners = mapViewer.getMouseMotionListeners();
        for (MouseMotionListener listener : motionListeners) {
            mapViewer.removeMouseMotionListener(listener);
        }
        //Remove all the default mouse listener to avoid the conflict
        mapViewer.removeMouseWheelListener(null);

        mapViewer.addMouseWheelListener(controller);
        mapViewer.addMouseListener(controller);
        mapViewer.addMouseMotionListener(controller);
    }
    private void onViewModelChanged(PropertyChangeEvent e) {
        syncWithViewModel();
    }
    public void syncWithViewModel() {
        Location centre = viewModel.getCenter();
        int zoom = viewModel.getZoomLevel();
        String error = viewModel.getError();

        if (centre != null) {
            mapViewer.setDisplayPosition(
                    new Coordinate(centre.getLatitude(), centre.getLongitude()),
                    zoom
            );
        }
        errorLabel.setText(error != null ? error : " ");
    }

    public JMapViewer getMapViewer() {
        return mapViewer;
    }


}
