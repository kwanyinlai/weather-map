package app.uielements;

import dataaccessobjects.SimpleImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PauseIcon implements Icon {

    private final Image image;

    public PauseIcon(int width, int height) {
        ImageIcon icon;

        try {
            icon = new ImageIcon(
                    new SimpleImageLoader().getImage("img/pause.png")
            );
        } catch (Exception e) {
            icon = new ImageIcon(new BufferedImage(
                    width, height, BufferedImage.TYPE_INT_ARGB)
            );
        }
        this.image = icon.getImage().getScaledInstance(
                width, height, Image.SCALE_SMOOTH
        );
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }

    @Override
    public int getIconWidth() {
        return image.getWidth(null);
    }

    @Override
    public int getIconHeight() {
        return image.getHeight(null);
    }
}
