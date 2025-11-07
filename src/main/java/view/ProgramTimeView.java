package main.java.view;

import main.java.interfaceadapter.maptime.ProgramTimeController;
import main.java.interfaceadapter.maptime.ProgramTimeState;
import main.java.interfaceadapter.maptime.ProgramTimeViewModel;
import main.java.interfaceadapter.maptime.ProgramTimeController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Program Time slider
 */
public class ProgramTimeView extends JPanel {

    private final String viewName = "program time";
    private final ProgramTimeViewModel programTimeViewModel;
    private ProgramTimeController programTimeController;
    private final JSlider timeSlider;

    public ProgramTimeView(ProgramTimeViewModel programTimeViewModel) {
        this.programTimeViewModel = programTimeViewModel;


        timeSlider = new JSlider(SwingConstants.HORIZONTAL);
        timeSlider.setMajorTickSpacing(1);
        timeSlider.addChangeListener(
                evt -> {
                    JSlider source = (JSlider) evt.getSource();
                    if (source.getValueIsAdjusting()) {
                        final ProgramTimeState currentState = programTimeViewModel.getState();
                        this.programTimeController.execute(currentState);
                    }
                }
        );

        this.add(timeSlider);
    }

    public String getViewName() {
        return viewName;
    }

}
