package view;

import entity.LayerNotFoundException;
import entity.WeatherType;
import interfaceadapter.weatherLayers.UpdateOverlayController;
import interfaceadapter.weatherLayers.WeatherLayersController;
import interfaceadapter.weatherLayers.WeatherLayersViewModel;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ChangeWeatherLayersView extends JPanel{
    private final JComboBox<WeatherType> weatherDropdown;
    private final JSlider slider;
    private final WeatherLayersViewModel vm;
    private final JMapViewer mapViewer;

    private WeatherLayersController layersController;
    private UpdateOverlayController updateController;

    public ChangeWeatherLayersView(WeatherLayersViewModel vm, JMapViewer mapViewer){
        this.vm = vm;
        this.mapViewer = mapViewer;
        this.setPreferredSize(new Dimension(200,200));


        slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
        slider.setPreferredSize(new Dimension(100, 50));
        slider.addChangeListener(evt -> {
                    JSlider source = (JSlider) evt.getSource();
                    if(!source.getValueIsAdjusting()) {
                        layersController.executeChangeOpacity(source.getValue());
                        updateController.update();
                    }
                }
        );

        //drop down menu for basemap options
        if (System.getenv("THUNDERFOREST_KEY") != null) {
           JComboBox<TileSource> basemapDropdown = new JComboBox<>(new TileSource[]{
                    new OsmTileSource.Mapnik(),
                    new OsmTileSource.CycleMap(),
                    new OsmTileSource.TransportMap(),
                    new OsmTileSource.LandscapeMap(),
                    new OsmTileSource.OutdoorsMap(),
                    new OsmTileSource.MobileAtlas()
            });
            basemapDropdown.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    mapViewer.setTileSource((TileSource) e.getItem());
                }
            });
            this.add(basemapDropdown);
        }
        else{
            //https://www.thunderforest.com/docs/apikeys/
            //Set env variable THUNDERFOREST_KEY={YOUR KEY}
            JLabel warning = new JLabel("<html>Missing Thunderforest API key.<br>Basemap cannot be switched.</html>");
            warning.setSize(200,200);
            this.add(warning);
        }

        weatherDropdown = new JComboBox<>(WeatherType.values());
        weatherDropdown.setPreferredSize(new Dimension(200, 100));
        weatherDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    layersController.executeChangeLayer((WeatherType) weatherDropdown.getSelectedItem());
                    slider.setValue((int)(vm.getCurrentOpacity()*100));
                    slider.setEnabled(true);
                    updateController.update();
                } catch (LayerNotFoundException ex) {
                    slider.setEnabled(false);
                }
            }
        });



        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(weatherDropdown);
        this.add(slider);
    }

    public void addLayerController (WeatherLayersController cont){
        this.layersController = cont;
    }

    public void addUpdateController (UpdateOverlayController cont){
        this.updateController = cont;
    }
}
