package view;

import entity.LayerNotFoundException;
import entity.OverlayManager;
import entity.WeatherType;
import interfaceadapter.weatherLayers.WeatherLayersController;
import interfaceadapter.weatherLayers.WeatherLayersViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeWeatherLayersView extends JPanel{
    private final JComboBox<WeatherType> dropdown;
    private final JSlider slider;
    private WeatherLayersController controller;
    private final WeatherLayersViewModel vm;

    public ChangeWeatherLayersView(WeatherLayersViewModel vm){
        this.vm = vm;
        this.setPreferredSize(new Dimension(200,200));


        slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
        slider.setPreferredSize(new Dimension(100, 50));
        slider.addChangeListener(evt -> {
                    JSlider source = (JSlider) evt.getSource();
                    if(!source.getValueIsAdjusting()) {
                        controller.executeChangeOpacity(source.getValue());
                    }
                }
        );


        dropdown = new JComboBox<>(WeatherType.values());
        dropdown.setPreferredSize(new Dimension(200, 100));
        dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.executeChangeLayer((WeatherType) dropdown.getSelectedItem());
                    slider.setValue((int)(vm.getCurrentOpacity()*100));
                    slider.setEnabled(true);
                } catch (LayerNotFoundException ex) {
                    slider.setEnabled(false);
                }
            }
        });



        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(dropdown);
        this.add(slider);
    }

    public void addController (WeatherLayersController cont){
        this.controller = cont;
    }
}
