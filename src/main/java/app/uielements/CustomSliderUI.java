package uielements;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class CustomSliderUI extends BasicSliderUI {
    public CustomSliderUI(JSlider slider) {
        super(slider);
    }

//    @Override
//    public void paintTrack(Graphics g){
//
//    }

    @Override
    public void paintThumb(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arrowWidth = 12;
        int arrowHeight = 10;
        int defaultThumbCentreX = thumbRect.x + thumbRect.width / 2;
        int defaultThumbCentreY = thumbRect.y + thumbRect.height / 2;
        int arrowPointOffset = defaultThumbCentreY - arrowHeight / 2;

        Polygon triangle = new Polygon();
        triangle.addPoint(defaultThumbCentreX, defaultThumbCentreY + arrowHeight);
        triangle.addPoint(defaultThumbCentreX-arrowWidth/2, arrowPointOffset);
        triangle.addPoint(defaultThumbCentreX + arrowWidth/2, arrowPointOffset);
        g2d.setColor(Color.black);
        g2d.fill(triangle);
        g2d.draw(triangle);
        g2d.dispose();
    }
}
