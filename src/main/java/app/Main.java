package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame application = new AppBuilder()
                .addUpdateOverlayUseCase()
                .createOverlayView()
                .addInfoPanelView()
                .addPanZoomView()
                .addLegendView()
                .addChangeOpacityView()
                .addWeatherLayersUseCase()
                .addProgramTimeView()
                .addUpdateMapTimeUseCase()
                // .addBookmarkView()
                .addMapOverlayView()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
