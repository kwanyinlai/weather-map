package app.uielements;

import javax.swing.*;
import java.awt.*;

public class PauseIcon implements Icon {
    private final int height;
    private final int width;
    private final Color color;

    public PauseIcon(int width, int height, Color color) {
        this.height = height;
        this.width = width;
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int pauseRectWidth = width/3;
        g2d.fillRect(x, y, pauseRectWidth, height);
        g2d.fillRect(x+pauseRectWidth * 2, y, pauseRectWidth, height);
        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
