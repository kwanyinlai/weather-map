package app;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import entity.ProgramTime;
import interfaceadapter.maptime.ProgramTimeController;
import interfaceadapter.maptime.ProgramTimePresenter;
import usecase.maptime.UpdateMapTimeInputBoundary;
import usecase.UpdateOverlayUseCase;
import usecase.maptime.UpdateMapTimeOutputBoundary;
import usecase.maptime.UpdateMapTimeUseCase;
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

    // initialising core entities
    private final ProgramTime programTime = new ProgramTime(Instant.now());
    private final TileRepository tileRepository = new CachedTileRepository(10); // TODO: change cache size
    private final OverlayManager overlayManager = new OverlayManager(10,10);

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
        return this;
    }

    public AppBuilder addUpdateOverlayUseCase(){
         updateOverlayUseCase = new UpdateOverlayUseCase(
                overlayManager,
                tileRepository,
                programTime
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
