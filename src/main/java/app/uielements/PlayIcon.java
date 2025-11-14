package app.uielements;

import javax.swing.*;
import java.awt.*;

public class PlayIcon implements Icon {
    private final int width;
    private final int height;
    private final Color color;

    public PlayIcon(int height, int width, Color color) {
        this.height = height;
        this.width = width;
        this.color = color;
    }
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Polygon triangle = new Polygon();
        triangle.addPoint(x, y);
        triangle.addPoint(x, y + height);
        triangle.addPoint(x + width, y + height/2);
        g2d.fillPolygon(triangle);
        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return heigt;
    }
}
