package view;

import javax.swing.*;

public class MapOverlayStructureView extends JPanel {
    JLayeredPane mapLayers = new JLayeredPane();

    public MapOverlayStructureView(){
        this.add(mapLayers);
    }

    public addMap(JPanel map){
        mapLayers.add(map, new Integer(1));
    }

    public addOverlay(JPanel Overlay){
        mapLayers.add(Overlay, new Integer(2));
    }
}
