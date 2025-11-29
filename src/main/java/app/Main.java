package app;

import javax.swing.*;

public final class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addUpdateOverlayUseCase()
                .createOverlayView()
                .addPanZoomView()
                .addMapOverlayView()
                .addLegendView()
                .addChangeOpacityView()
                .addWeatherLayersUseCase()
                .addMapSettingsPersistence()
                .addProgramTimeView()
                .addUpdateMapTimeUseCase()
                .addBookmarkView()
                .addSearchBarView()
                .addSettingsAndBookmarkSidePanel()
//                .addInfoPanelView()
                .build();
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }

    private Main() {
        // hiding constructor
    }
}
