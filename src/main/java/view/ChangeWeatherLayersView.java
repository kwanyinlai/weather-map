package view;

import entity.OverlayManager;
import entity.WeatherType;

import javax.swing.*;
import java.awt.*;

public class ChangeWeatherLayersView extends JPanel{
    private final JComboBox<WeatherType> dropdown;
    private final JSlider slider;

    public ChangeWeatherLayersView(){
        this.setPreferredSize(new Dimension(200,200));
        dropdown = new JComboBox<>(WeatherType.values());
        dropdown.setPreferredSize(new Dimension(200, 100));
        slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
        slider.setPreferredSize(new Dimension(100, 50));
        slider.addChangeListener(
                evt -> {
                    JSlider source = (JSlider) evt.getSource();
                    if (source.getValueIsAdjusting()) {
                        //this.programTimeController.execute(source.getValue());
                    }
                }
        );
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(dropdown);
        this.add(slider);
    }

    //TODO add action listener to update OM
}
