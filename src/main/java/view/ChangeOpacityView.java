package view;

import entity.OverlayManager;
import entity.WeatherType;

import javax.swing.*;

public class ChangeOpacityView extends JPanel{
    private final OverlayManager overlayManager;
    private final JComboBox<WeatherType> dropdown;

    public ChangeOpacityView(OverlayManager OM){
        overlayManager = OM;
        dropdown = new JComboBox<>(WeatherType.values());
        this.add(dropdown);
    }

    //TODO add action listener to update OM
}
