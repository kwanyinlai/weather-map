package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class MapOverlayStructureView extends JPanel{
    JLayeredPane mapLayers = new JLayeredPane();
    private PropertyChangeSupport support;
    private Dimension size;

    public MapOverlayStructureView(){
        //Components in JLayeredPane do not automatically resize, and requires a observer method
        mapLayers.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newSize = mapLayers.getSize();
                support.firePropertyChange("size", size, newSize);
                size = newSize;
            }
        });

        this.add(mapLayers);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addComponent(JPanel component, int layer){
        mapLayers.add(component, new Integer(layer));
        //new Integer object required as specified in docs
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/JLayeredPane.html
    }

}
