package view;

import javax.swing.*;

public class MapOverlayStructureView extends JPanel {
    JLayeredPane mapLayers = new JLayeredPane();

    public MapOverlayStructureView(){
        this.add(mapLayers);
    }

    public void addComponent(JPanel component, int layer){
        mapLayers.add(component, new Integer(layer));
        //new Integer object required as specified in docs
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/JLayeredPane.html
    }

}
