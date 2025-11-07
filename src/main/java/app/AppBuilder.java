package app;

import javax.swing.*;
import java.awt.*;
import main.java.view.ProgramTimeView;
import main.java.interfaceadapter.maptime.ProgramTimeViewModel;

public class AppBuilder {
    private final JPanel borderPanel = new JPanel();
    private final BorderLayout borderLayout = new BorderLayout();

    private ProgramTimeView programTimeView;
    private ProgramTimeViewModel programTimeViewModel;

    public AppBuilder() {
        borderPanel.setLayout(borderLayout);
        borderPanel.setPreferredSize(new Dimension(800, 600));
    }

//    public AppBuilder addProgramTimeView() {
//        programTimeViewModel = new SignupViewModel();
//        programTimeView = new SignupView(signupViewModel);
//        borderPanel.add(programTimeView, programTimeView.getViewName());
//        return this;
//    }
//
//    public AppBuilder addUpdateMapTimeUseCase() {
//        final UpdateMapTimeOutputBoundary updateMapTimeOutputBoundary = new UpdateMapTimePresenter(viewManagerModel,
//                signupViewModel, loginViewModel);
//        final UpdateMapTimeInputBoundary updateMapTimeUseCase = new UpdateMapTimeUseCase(
//                userDataAccessObject, signupOutputBoundary);
//
//        ProgramTimeController controller = new ProgramTimeController(userSignupInteractor);
//        programTimeView.setProgramTimeController(controller);
//        return this;
//    }

    public JFrame build() {
        final JFrame application = new JFrame("Weather Map");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(borderPanel);

        return application;
    }


}
