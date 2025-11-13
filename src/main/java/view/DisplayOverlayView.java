package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class DisplayOverlayView extends JPanel implements PropertyChangeListener {


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(Objects.equals(evt.getPropertyName(), "size")) {
            this.setBounds(new Rectangle((Dimension) evt.getNewValue()));
        } else {
            //overlay change
        }
    }


}
