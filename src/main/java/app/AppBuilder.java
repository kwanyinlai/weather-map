package app;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;

import constants.Constants;
import entity.ProgramTime;
import entity.Viewport;
import interfaceadapter.maptime.programtime.ProgramTimeController;
import interfaceadapter.maptime.programtime.ProgramTimePresenter;
import interfaceadapter.maptime.timeanimation.TimeAnimationController;
import interfaceadapter.weatherLayers.*;
import usecase.maptime.UpdateMapTimeInputBoundary;
import usecase.weatherLayers.layers.*;
import usecase.weatherLayers.update.UpdateOverlayOutputBoundary;
import usecase.weatherLayers.update.UpdateOverlaySizeUseCase;
import usecase.weatherLayers.update.UpdateOverlayUseCase;
import usecase.maptime.UpdateMapTimeOutputBoundary;
import usecase.maptime.UpdateMapTimeUseCase;
import view.*;
import interfaceadapter.maptime.programtime.ProgramTimeViewModel;
import dataaccessinterface.TileRepository;
import dataaccessobjects.CachedTileRepository;
import entity.OverlayManager;
import interfaceadapter.mapinteraction.MapViewModel;
import interfaceadapter.mapinteraction.PanAndZoomController;
import interfaceadapter.mapinteraction.PanAndZoomPresenter;
import usecase.mapinteraction.PanAndZoomUseCase;
import usecase.mapinteraction.PanAndZoomInputBoundary;

public class AppBuilder {
    private final JPanel borderPanel = new JPanel();
    private final BorderLayout borderLayout = new BorderLayout();

    private DisplayOverlayView weatherOverlayView;

    private ProgramTimeView programTimeView;
    private ProgramTimeViewModel programTimeViewModel;

    private UpdateOverlayViewModel overlayViewModel;

    private LegendsView legendsView;
    private LegendViewModel legendViewModel;

    private UpdateOverlayUseCase updateOverlayUseCase;

    private WeatherLayersViewModel weatherLayersViewModel;
    private ChangeWeatherLayersView changeWeatherView;
    private ChangeOpacityUseCase changeOpacityUseCase;
    private ChangeLayerUseCase changeLayerUseCase;
    private UpdateOverlaySizeUseCase updateOverlaySizeUseCase;


    private MapOverlayStructureView mapOverlayStructure;

    // initialising core entities
    private final ProgramTime programTime = new ProgramTime(Instant.now());
    private final OverlayManager overlayManager = new OverlayManager(Constants.DEFAULT_MAP_WIDTH,
            Constants.DEFAULT_MAP_HEIGHT);
    private final Viewport viewport = new Viewport(300,300,Constants.DEFAULT_MAP_WIDTH,
            0, 6, 0, Constants.DEFAULT_MAP_HEIGHT);
    private PanAndZoomView panAndZoomView;
    private MapViewModel mapViewModel;
    private PanAndZoomPresenter panAndZoomPresenter;
    private PanAndZoomInputBoundary panAndZoomUseCase;
    private PanAndZoomController panAndZoomController;

    public AppBuilder() {
        borderPanel.setLayout(borderLayout);
        borderPanel.setPreferredSize(new Dimension(Constants.DEFAULT_PROGRAM_WIDTH, Constants.DEFAULT_PROGRAM_HEIGHT));
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

    public AppBuilder createOverlayView(){
        updateOverlaySizeUseCase = new UpdateOverlaySizeUseCase(overlayManager, viewport);
        UpdateOverlaySizeController sizeController = new UpdateOverlaySizeController(updateOverlaySizeUseCase, updateOverlayUseCase);
        weatherOverlayView = new DisplayOverlayView(sizeController, overlayViewModel);
        return this;
    }
    /**
     * Overlays the weather overlay component and JMV component. Should be called after both layers have been
     * initiazlized.
     * @return this
     */
    public AppBuilder addMapOverlayView(){
        
        mapOverlayStructure = new MapOverlayStructureView();
        mapOverlayStructure.addPropertyChangeListener(weatherOverlayView);
        mapOverlayStructure.addComponent(panAndZoomView, 1);
        mapOverlayStructure.addComponent(weatherOverlayView, 2);
        //...
        borderPanel.add(mapOverlayStructure, BorderLayout.CENTER);
        return this;
    }

    public AppBuilder addLegendView(){
        legendViewModel = new LegendViewModel();
        legendsView = new LegendsView(legendViewModel);
        borderPanel.add(legendsView, BorderLayout.NORTH);
        return this;
    }

    public AppBuilder addWeatherLayersUseCase(){
        ChangeLayerOutputBoundary layerOutputBoundary = new WeatherLayersPresenter(weatherLayersViewModel);
        UpdateLegendOutputBoundary legendOutputBoundary = new LegendPresenter(legendViewModel);
        changeLayerUseCase = new ChangeLayerUseCase(overlayManager, layerOutputBoundary, legendOutputBoundary);
        changeOpacityUseCase = new ChangeOpacityUseCase(overlayManager);
        WeatherLayersController layersController = new WeatherLayersController(changeLayerUseCase, changeOpacityUseCase);
        changeWeatherView.addLayerController(layersController);
        UpdateOverlayController updateCont = new UpdateOverlayController(updateOverlayUseCase);
        changeWeatherView.addUpdateController(updateCont);
        return this;
    }

    public AppBuilder addUpdateOverlayUseCase(){
        overlayViewModel = new UpdateOverlayViewModel();
        final UpdateOverlayOutputBoundary output = new UpdateOverlayPresenter(overlayViewModel);
         updateOverlayUseCase = new UpdateOverlayUseCase(
                overlayManager,
                CachedTileRepository.getInstance(),
                programTime,
                 viewport,
                 output
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
        ProgramTimeController programTimeController = new ProgramTimeController(updateMapTimeInputBoundary, java.time.Duration.ofDays(3));
        // TODO: move the ofDays(3) into entities as a business rule
        TimeAnimationController timeAnimationController = new TimeAnimationController(updateMapTimeInputBoundary, 500);
        programTimeView.setProgramTimeController(programTimeController);
        programTimeView.setTimeAnimationController(timeAnimationController);
        return this;
    }
    public AppBuilder addPanZoomView() {

        mapViewModel = new MapViewModel();
        panAndZoomView = new PanAndZoomView(mapViewModel);
        panAndZoomPresenter = new PanAndZoomPresenter(
                panAndZoomView.getMapViewer(),
                mapViewModel
        );
        panAndZoomUseCase = new PanAndZoomUseCase(viewport, panAndZoomPresenter);
        panAndZoomController = new PanAndZoomController(
                panAndZoomUseCase,
                panAndZoomView.getMapViewer()
        );
        panAndZoomView.setController(panAndZoomController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Weather Map");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(borderPanel);
        updateOverlayUseCase.update();

        return application;
    }


}
