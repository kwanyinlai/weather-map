package view;

import app.uielements.PauseIcon;
import app.uielements.PlayIcon;
import interfaceadapter.maptime.programtime.ProgramTimeController;
import interfaceadapter.maptime.programtime.ProgramTimeState;
import interfaceadapter.maptime.programtime.ProgramTimeViewModel;
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
    private final JButton playPauseButton;
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
                        this.programTimeController.updateTime(source.getValue());
                    }
                }
        );
        timeSlider.setOpaque(false);
        timeSlider.setMajorTickSpacing(5);
        timeSlider.setPaintTicks(true);


        /** Pause play button
         *
         */
        playPauseButton = new JButton(new PlayIcon(20,20, Color.ORANGE));
        playPauseButton.addActionListener((ActionEvent e) -> {
            Icon current = playPauseButton.getIcon();
            if (current instanceof PlayIcon ) {
                button.setIcon(new PauseIcon(20,20, Color.ORANGE));
            }
            else{
                button.setIcon(new PlayIcon(20,20, Color.ORANGE));
            }
        });



        /** Adding current time label
         *
         */
        currentTimeTitleLabel = new JLabel(ProgramTimeViewModel.CURRENT_TIME_LABEL);
        currentTimeTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentTime = new JLabel(ProgramTimeViewModel.getCurrentTimeFormatted());
        currentTime.setFont(DefaultThemes.NORMAL_BODY_FONT);
        currentTimeTitleLabel.setFont(DefaultThemes.BOLD_BODY_FONT);
        this.add(playPauseButton);
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
