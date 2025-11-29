package app;

import constants.Constants;
import dataaccessinterface.BookmarkedLocationStorage;
import dataaccessinterface.OkHttpsPointWeatherGatewayXml;
import dataaccessobjects.CachedTileRepository;
import dataaccessobjects.InDiskBookmarkStorage;
import entity.OverlayManager;
import entity.ProgramTime;
import entity.Viewport;
import interfaceadapter.bookmark.BookmarksViewModel;
import interfaceadapter.bookmark.addbookmark.AddBookmarkController;
import interfaceadapter.bookmark.addbookmark.AddBookmarkPresenter;
import interfaceadapter.bookmark.listbookmark.ListBookmarksController;
import interfaceadapter.bookmark.listbookmark.ListBookmarksPresenter;
import interfaceadapter.bookmark.removebookmark.RemoveBookmarkController;
import interfaceadapter.bookmark.removebookmark.RemoveBookmarkPresenter;
import interfaceadapter.mapinteraction.MapViewModel;
import interfaceadapter.mapinteraction.PanAndZoomController;
import interfaceadapter.mapinteraction.PanAndZoomPresenter;
import interfaceadapter.maptime.programtime.ProgramTimeController;
import interfaceadapter.maptime.programtime.ProgramTimePresenter;
import interfaceadapter.maptime.programtime.ProgramTimeViewModel;
import interfaceadapter.maptime.timeanimation.TimeAnimationController;
import interfaceadapter.weatherLayers.*;
import interfaceadapter.infopanel.*;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import usecase.bookmark.addbookmark.AddBookmarkInputBoundary;
import usecase.bookmark.addbookmark.AddBookmarkOutputBoundary;
import usecase.bookmark.addbookmark.AddBookmarkUseCase;
import usecase.bookmark.listbookmark.ListBookmarksInputBoundary;
import usecase.bookmark.listbookmark.ListBookmarksUseCase;
import usecase.bookmark.removebookmark.RemoveBookmarkInputBoundary;
import usecase.bookmark.removebookmark.RemoveBookmarkOutputBoundary;
import usecase.bookmark.removebookmark.RemoveBookmarkUseCase;
import usecase.mapinteraction.PanAndZoomInputBoundary;
import usecase.mapinteraction.PanAndZoomUseCase;
import usecase.maptime.UpdateMapTimeInputBoundary;
import usecase.maptime.UpdateMapTimeOutputBoundary;
import usecase.maptime.UpdateMapTimeUseCase;
import usecase.weatherLayers.layers.ChangeLayerOutputBoundary;
import usecase.weatherLayers.layers.ChangeLayerUseCase;
import usecase.weatherLayers.layers.ChangeOpacityUseCase;
import usecase.weatherLayers.layers.UpdateLegendOutputBoundary;
import usecase.weatherLayers.update.UpdateOverlayOutputBoundary;
import usecase.weatherLayers.update.UpdateOverlaySizeUseCase;
import usecase.weatherLayers.update.UpdateOverlayUseCase;
import usecase.infopanel.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;

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
    private JMapViewer mapViewer = new JMapViewer();

    // initialising core entities
    private final ProgramTime programTime = new ProgramTime(Instant.now());
    private final OverlayManager overlayManager = new OverlayManager(Constants.DEFAULT_MAP_WIDTH,
            Constants.DEFAULT_MAP_HEIGHT);
    private final Viewport viewport = new Viewport(000,000,Constants.DEFAULT_MAP_WIDTH,
            0, 6, 0, 584);
    private final BookmarkedLocationStorage bookmarkStorage = new InDiskBookmarkStorage(Constants.BOOKMARK_DATA_PATH);
    private PanAndZoomView panAndZoomView;
    private MapViewModel mapViewModel;
    private PanAndZoomPresenter panAndZoomPresenter;
    private PanAndZoomInputBoundary panAndZoomUseCase;
    private PanAndZoomController panAndZoomController;
    private BookmarksViewModel bookmarksViewModel;
    private BookmarksView bookmarksView;
    private AddBookmarkInputBoundary addBookmarkUseCase;
    private RemoveBookmarkInputBoundary removeBookmarkUseCase;
    private ListBookmarksInputBoundary listBookmarksUseCase;
    private AddBookmarkOutputBoundary addBookmarkPresenter;
    private RemoveBookmarkOutputBoundary removeBookmarkPresenter;
    private ListBookmarksPresenter listBookmarksPresenter;
    private interfaceadapter.infopanel.InfoPanelViewModel infoPanelViewModel;
    private interfaceadapter.infopanel.InfoPanelController infoPanelController;
    private usecase.infopanel.InfoPanelInteractor infoPanelUseCase;
    private view.InfoPanelView infoPanelView;
    private AddBookmarkController addBookmarkController;
    private RemoveBookmarkController removeBookmarkController;
    private ListBookmarksController listBookmarksController;



    public AppBuilder() {
        borderPanel.setLayout(borderLayout);
        borderPanel.setPreferredSize(new Dimension(Constants.DEFAULT_PROGRAM_WIDTH, Constants.DEFAULT_PROGRAM_HEIGHT));
    }

    public AppBuilder addInfoPanelView() {
        infoPanelViewModel = new InfoPanelViewModel();
        InfoPanelPresenter presenter = new InfoPanelPresenter(infoPanelViewModel);

        PointWeatherFetcher fetcher = new OkHttpsPointWeatherGatewayXml();
        InfoPanelInteractor useCase = new InfoPanelInteractor(fetcher, presenter);

        infoPanelController = new InfoPanelController(useCase, presenter);

        infoPanelView = new InfoPanelView(infoPanelViewModel);
        infoPanelView.setController(infoPanelController);
        infoPanelView.bind(presenter);

        return this;
    }

    public AppBuilder addBookmarkView(){

        bookmarksViewModel = new BookmarksViewModel();
        removeBookmarkPresenter = new RemoveBookmarkPresenter(bookmarksViewModel);
        listBookmarksPresenter = new ListBookmarksPresenter(bookmarksViewModel);
        addBookmarkPresenter = new AddBookmarkPresenter(bookmarksViewModel);
        addBookmarkUseCase = new AddBookmarkUseCase(bookmarkStorage, addBookmarkPresenter);
        removeBookmarkUseCase = new RemoveBookmarkUseCase(bookmarkStorage, removeBookmarkPresenter);
        listBookmarksUseCase = new ListBookmarksUseCase(bookmarkStorage, listBookmarksPresenter);

        ;
        addBookmarkController = new AddBookmarkController(addBookmarkUseCase);
        removeBookmarkController = new RemoveBookmarkController(removeBookmarkUseCase);
        listBookmarksController = new ListBookmarksController(listBookmarksUseCase);
        bookmarksView = new BookmarksView(bookmarksViewModel, addBookmarkController, removeBookmarkController,
                listBookmarksController);
        borderPanel.add(bookmarksView, BorderLayout.EAST);

        return this;
    }

    public AppBuilder addProgramTimeView() {
        programTimeViewModel = new ProgramTimeViewModel();
        programTimeView = new ProgramTimeView(programTimeViewModel);
        borderPanel.add(programTimeView, BorderLayout.SOUTH);
        return this;
    }

    public AppBuilder addChangeOpacityView(){
        weatherLayersViewModel = new WeatherLayersViewModel(0.5);
        changeWeatherView = new ChangeWeatherLayersView(weatherLayersViewModel, mapViewer);
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
    public AppBuilder addMapOverlayView() {

        mapOverlayStructure = new MapOverlayStructureView();
        mapOverlayStructure.addPropertyChangeListener(weatherOverlayView);
        mapOverlayStructure.addComponent(panAndZoomView, 1);
        mapOverlayStructure.addComponent(weatherOverlayView, 2);
        //...
        if (infoPanelView != null) {
            mapOverlayStructure.addComponent(infoPanelView, 99);
        }
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
        viewport.addListener(updateCont);
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
        panAndZoomView = new PanAndZoomView(mapViewModel, mapViewer);
        panAndZoomPresenter = new PanAndZoomPresenter(
                viewport,
                panAndZoomView.getMapViewer(),
                mapViewModel
        );
        panAndZoomUseCase = new PanAndZoomUseCase(viewport);
        panAndZoomController = new PanAndZoomController(
                panAndZoomUseCase,
                panAndZoomView.getMapViewer()
        );
        panAndZoomView.setController(panAndZoomController);
        viewport.getSupport().addPropertyChangeListener(evt -> {
            if (updateOverlayUseCase != null) {
                updateOverlayUseCase.update();
            }
            if (infoPanelView != null) {
                final var c = viewport.getCentre();
                final int z = viewport.getZoomLevel();
                infoPanelView.onViewportChanged(c.getLatitude(), c.getLongitude(), z);
            }
        });
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
