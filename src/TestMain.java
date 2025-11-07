import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class TestMain extends JFrame implements JMapViewerEventListener {

    private final JMapViewer map;

    public TestMain() {
        super("JMapViewer test");
        setSize(800, 800);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane mapLayers = new JLayeredPane();

        map = new JMapViewer();
        map.addJMVListener(this);
        map.setBounds(0,0,800,800);
        map.setZoomContolsVisible(false);

        //Overlay
        BufferedImage resizedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        try {
            //load image and set opacity. Will need to save image in some entity and have a seprate method
            // for setting the opacity.
            Image image = ImageIO.read(new File("img/0 (1).png"));
            float opacity = 0.5f;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g.drawImage(image, 0, 0, 510, 510, null);
        } catch(Exception e){ e.printStackTrace(); }
        //add image to label
        ImageIcon tileAlp = new ImageIcon(resizedImage);
        JLabel labelAlp = new JLabel();
        labelAlp.setIcon(tileAlp);
        labelAlp.setBounds(0,0,800,800);


        // new Integer needed to make layering work. Simply having a number will not work.
        mapLayers.add(map, new Integer(1));
        mapLayers.add(labelAlp, new Integer(2));

        Panel menu = new Panel();
        Button reset = new Button("Reset Map");
        menu.add(reset);

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.setZoom(1);
                map.setCenter( new Point(map.getWidth() / 2 ,map.getHeight() / 2));
                map.moveMap(0,0);
            }
        });


        add(mapLayers, BorderLayout.CENTER);
        add(menu, BorderLayout.EAST);
    }


    public static void main(String[] args) {
        new TestMain().setVisible(true);
    }

    public void processCommand(JMVCommandEvent command) {

    }
}