package app.uielements;

import javax.swing.*;
import java.awt.*;

public class CircleButton extends JButton {

    public CircleButton(Icon icon) {
        super(icon);
    }
    @Override
    public void paintComponent(Graphics g) {
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 5, 5);
        g.setColor(Color.BLACK);
    }

}
