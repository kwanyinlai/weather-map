package app;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import entity.ProgramTime;
import entity.Viewport;
import interfaceadapter.maptime.ProgramTimeController;
import interfaceadapter.maptime.ProgramTimePresenter;
import interfaceadapter.weatherLayers.WeatherLayersController;
import interfaceadapter.weatherLayers.WeatherLayersPresenter;
import interfaceadapter.weatherLayers.WeatherLayersViewModel;
import usecase.maptime.UpdateMapTimeInputBoundary;
import usecase.weatherLayers.ChangeLayerOutputBoundary;
import usecase.weatherLayers.ChangeLayerUseCase;
import usecase.weatherLayers.ChangeOpacityUseCase;
import usecase.weatherLayers.UpdateOverlayUseCase;
import usecase.maptime.UpdateMapTimeOutputBoundary;
import usecase.maptime.UpdateMapTimeUseCase;
import view.ChangeWeatherLayersView;
import view.MapOverlayStructureView;
import view.ProgramTimeView;
import interfaceadapter.maptime.ProgramTimeViewModel;
import dataaccessinterface.TileRepository;
import dataaccessobjects.CachedTileRepository;
import entity.OverlayManager;

public class AppBuilder {
    private final JPanel borderPanel = new JPanel();
    private final BorderLayout borderLayout = new BorderLayout();


    private ProgramTimeView programTimeView;
    private ProgramTimeViewModel programTimeViewModel;

    private UpdateOverlayUseCase updateOverlayUseCase;

    private WeatherLayersViewModel weatherLayersViewModel;
    private ChangeWeatherLayersView changeWeatherView;
    private ChangeOpacityUseCase changeOpacityUseCase;
    private ChangeLayerUseCase changeLayerUseCase;

    private MapOverlayStructureView mapOverlayStructure;

    // initialising core entities
    private final ProgramTime programTime = new ProgramTime(Instant.now());
    private final TileRepository tileRepository = new CachedTileRepository(10); // TODO: change cache size
    private final OverlayManager overlayManager = new OverlayManager(10,10);
    private final Viewport viewport = new Viewport(0,0,600,
            0, 6, 0, 600);

    public AppBuilder() {
        borderPanel.setLayout(borderLayout);
        borderPanel.setPreferredSize(new Dimension(800, 600));
    }

    public AppBuilder addProgramTimeView() {
        programTimeViewModel = new ProgramTimeViewModel();
        programTimeView = new ProgramTimeView(programTimeViewModel);
        borderPanel.add(programTimeView, BorderLayout.SOUTH);
        return this;
    }

    public AppBuilder addChangeOpacityView(){
        weatherLayersViewModel = new WeatherLayersViewModel(0.5);
        changeWeatherView = new ChangeWeatherLayersView(weatherLayersViewModel);
        borderPanel.add(changeWeatherView, BorderLayout.EAST);
        return this;
    }

    public AppBuilder addMapOverlayView(){
        //TODO implement overlayComponent, MapComponent and other panels to be layered onto the map (temp names only)
        //overlayComponent = new OverlayView(..., ...);
        //mapComponent = new MapComponent(..., ...);
        //infoPanel = ...
        mapOverlayStructure = new MapOverlayStructureView();
        //mapOverlayStructure.addComponent(mapComponent, 1);
        //mapOverlayStructure.addComponent(overlayComponent, 2);
        //...
        return this;
    }

    public AppBuilder addWeatherLayersUseCase(){
        ChangeLayerOutputBoundary layerOutputBoundary = new WeatherLayersPresenter(weatherLayersViewModel);
        changeLayerUseCase = new ChangeLayerUseCase(overlayManager, layerOutputBoundary);
        changeOpacityUseCase = new ChangeOpacityUseCase(overlayManager);
        WeatherLayersController cont = new WeatherLayersController(changeLayerUseCase, changeOpacityUseCase);
        changeWeatherView.addController(cont);
        return this;
    }

    public AppBuilder addUpdateOverlayUseCase(){
         updateOverlayUseCase = new UpdateOverlayUseCase(
                overlayManager,
                tileRepository,
                programTime,
                 viewport
        );
        return this;
    }

    public AppBuilder addUpdateMapTimeUseCase() {
        final UpdateMapTimeOutputBoundary updateMapTimeOutputBoundary = new ProgramTimePresenter(programTimeViewModel);
        final UpdateMapTimeInputBoundary updateMapTimeInputBoundary =
                new UpdateMapTimeUseCase(
                        programTime,
                        updateOverlayUseCase,
                        updateMapTimeOutputBoundary
                    );
        ProgramTimeController controller = new ProgramTimeController(updateMapTimeInputBoundary, java.time.Duration.ofDays(3));
        programTimeView.setProgramTimeController(controller);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Weather Map");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(borderPanel);

        return application;
    }


}
