package uielements;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class CustomSliderUI extends BasicSliderUI {
    public CustomSliderUI(JSlider slider) {
        super(slider);
    }

    @Override
    public void paintTrack(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradientPaint = new GradientPaint(trackRect.x, 0 ,Color.BLUE,
                trackRect.x + trackRect.width, 0, Color.ORANGE);
        g2d.setPaint(gradientPaint);
        g2d.fillRoundRect(
                trackRect.x,
                trackRect.y + 10,
                trackRect.width,
                15,
                20,
                20);
        g2d.dispose();
    }

    @Override
    public void paintThumb(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arrowWidth = 12;
        int arrowHeight = 10;
        int defaultThumbCentreX = thumbRect.x + thumbRect.width / 2;
        int defaultThumbCentreY = thumbRect.y + thumbRect.height / 2;
        int arrowPointOffset = defaultThumbCentreY - arrowHeight * 2;

        Polygon triangle = new Polygon();
        triangle.addPoint(defaultThumbCentreX, arrowPointOffset + arrowHeight);
        triangle.addPoint(defaultThumbCentreX - arrowWidth/2, arrowPointOffset);
        triangle.addPoint(defaultThumbCentreX + arrowWidth/2, arrowPointOffset);
        g2d.setColor(Color.black);
        g2d.fill(triangle);
        g2d.draw(triangle);
        g2d.dispose();
    }
    protected Dimension getThumbSize() {
        return new Dimension(40,40);
    }
    @Override
    public void paintFocus(Graphics g){
        return;
    }
}
