package app;

import app.AppBuilder;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addUpdateOverlayUseCase()
                .createOverlayView()
                .addMapOverlayView()
                .addLegendView()
                .addChangeOpacityView()
                .addWeatherLayersUseCase()
                .addProgramTimeView()
                .addUpdateMapTimeUseCase()
                .build();
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
