package view;

import constants.Constants;
import interfaceadapter.weatherlayers.LegendViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LegendsView extends JPanel implements PropertyChangeListener {
    private final ImageIcon imageIcon;
    private final LegendViewModel legendView;
    private final JLabel label;
    public LegendsView(LegendViewModel legendView){
        this.legendView = legendView;
        legendView.addPropertyChangeListener(this);

        imageIcon = new ImageIcon();
        label = new JLabel();
        label.setIcon(imageIcon);
        this.add(label);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        imageIcon.setImage(legendView.getState().getImage().getScaledInstance(Constants.DEFAULT_PROGRAM_WIDTH ,-1,
                Image.SCALE_REPLICATE));
        this.repaint();
    }
}
