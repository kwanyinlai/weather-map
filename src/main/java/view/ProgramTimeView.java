package view;

import interfaceadapter.maptime.ProgramTimeController;
import interfaceadapter.maptime.ProgramTimeState;
import interfaceadapter.maptime.ProgramTimeViewModel;
import uielements.CustomSliderUI;
import uielements.DefaultThemes;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Program Time slider
 */
public class ProgramTimeView extends JPanel implements PropertyChangeListener {

    private final String viewName = "program time";
    private final ProgramTimeViewModel programTimeViewModel;
    private ProgramTimeController programTimeController;
    private final JSlider timeSlider;
    private final JLabel currentTimeTitleLabel;
    private final JLabel currentTime;

    public ProgramTimeView(ProgramTimeViewModel programTimeViewModel) {
        this.programTimeViewModel = programTimeViewModel;
        programTimeViewModel.addPropertyChangeListener(this);

        /** Adding JSlider
         *
         */
        timeSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);
        timeSlider.setUI(new CustomSliderUI(timeSlider));
        timeSlider.setPreferredSize(new Dimension(400, 50));
        timeSlider.addChangeListener(
                evt -> {
                    JSlider source = (JSlider) evt.getSource();
                    if (source.getValueIsAdjusting()) {
                        this.programTimeController.execute(source.getValue());
                    }
                }
        );

        /** Adding current time label
         *
         */
        currentTimeTitleLabel = new JLabel(ProgramTimeViewModel.CURRENT_TIME_LABEL);
        currentTimeTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentTime = new JLabel(ProgramTimeViewModel.getCurrentTimeFormatted());
        currentTime.setFont(DefaultThemes.NORMAL_BODY_FONT);
        currentTimeTitleLabel.setFont(DefaultThemes.BOLD_BODY_FONT);

        this.add(timeSlider);
        this.add(currentTimeTitleLabel);
        this.add(currentTime);
    }

    public String getViewName() {
        return viewName;
    }
    public void setProgramTimeController(ProgramTimeController controller) {
        this.programTimeController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("time slider")) {
            final ProgramTimeState state = (ProgramTimeState) evt.getNewValue();
            currentTime.setText(state.getTime());
        }
        else{
            System.out.println(evt.getPropertyName());
        }
    }
}
