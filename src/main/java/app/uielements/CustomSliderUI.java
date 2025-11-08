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
        g2d.translate(thumbRect.x, thumbRect.y);
        int arrowWidth = thumbRect.width - thumbRect.x;
        int arrowHeight = thumbRect.height - thumbRect.y;
        Polygon triangle = new Polygon();
        triangle.addPoint(thumbRect.x, thumbRect.y);
        triangle.addPoint(thumbRect.x - arrowWidth/2, thumbRect.y + arrowHeight);
        triangle.addPoint(thumbRect.x + arrowWidth/2, thumbRect.y + arrowHeight);
        g2d.setColor(Color.black);
        g2d.draw(triangle);

        g2d.dispose();
    }
}
