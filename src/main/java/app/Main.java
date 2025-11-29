package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addUpdateOverlayUseCase()
                .createOverlayView()
                .addInfoPanelView()
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
                .build();
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
