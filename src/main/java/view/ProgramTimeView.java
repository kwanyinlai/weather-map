package view;

import app.uielements.PlayPausePauseIcon;
import constants.Constants;
import interfaceadapter.maptime.programtime.ProgramTimeController;
import interfaceadapter.maptime.programtime.ProgramTimeState;
import interfaceadapter.maptime.programtime.ProgramTimeViewModel;
import interfaceadapter.maptime.timeanimation.TimeAnimationController;
import uielements.CustomSliderUI;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Program Time slider.
 */
public class ProgramTimeView extends JPanel implements PropertyChangeListener {

    private transient ProgramTimeController programTimeController;
    private transient TimeAnimationController timeAnimationController;
    private final JSlider timeSlider;
    private final JButton playPauseButton;
    private final JLabel currentTime;

    public ProgramTimeView(ProgramTimeViewModel programTimeViewModel) {
        programTimeViewModel.addPropertyChangeListener(this);
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        timeSlider = new JSlider(SwingConstants.HORIZONTAL, 0, Constants.PERCENT_MULTIPLIER, 0);
        timeSlider.setUI(new CustomSliderUI(timeSlider));
        timeSlider.setPaintTicks(false);
        timeSlider.addChangeListener(
                evt -> {
                    JSlider source = (JSlider) evt.getSource();
                    if (source.getValueIsAdjusting()) {
                        this.programTimeController.updateTime(source.getValue());
                    }
                }
        );
        timeSlider.setOpaque(false);
        timeSlider.setPaintTicks(false);
        timeSlider.setPreferredSize(new Dimension(Constants.SLIDER_WIDTH, Constants.SLIDER_HEIGHT));

        final PlayPausePauseIcon playIcon = new PlayPausePauseIcon(
                Constants.PLAY_PAUSE_BUTTON_WIDTH,
                Constants.PLAY_PAUSE_BUTTON_HEIGHT,
                Constants.PLAY_BUTTON_FILE_PATH);
        playPauseButton = new JButton(playIcon);
        final PlayPausePauseIcon pauseIcon = new PlayPausePauseIcon(
                Constants.PLAY_PAUSE_BUTTON_WIDTH,
                Constants.PLAY_PAUSE_BUTTON_HEIGHT,
                Constants.PAUSE_BUTTON_FILE_PATH);
        playPauseButton.addActionListener((ActionEvent e1) -> {
            Icon current = playPauseButton.getIcon();
            if (current.equals(playIcon)) {
                playPauseButton.setIcon(pauseIcon);
                timeAnimationController.play();
            }
            else{
                playPauseButton.setIcon(playIcon);
                timeAnimationController.pause();
            }
        });

        JLabel currentTimeTitleLabel = new JLabel(ProgramTimeViewModel.CURRENT_TIME_LABEL);
        currentTimeTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentTime = new JLabel(ProgramTimeViewModel.getCurrentTimeFormatted());
        this.add(playPauseButton);
        this.add(timeSlider);
        this.add(currentTimeTitleLabel);
        this.add(currentTime);
    }

    public String getViewName() {
        return "program time";
    }
    public void setProgramTimeController(ProgramTimeController controller) {
        this.programTimeController = controller;
    }

    public void setTimeAnimationController(TimeAnimationController controller) {
        this.timeAnimationController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("time slider")) {
            final ProgramTimeState state = (ProgramTimeState) evt.getNewValue();
            currentTime.setText(state.getTime());
        }
        else if (evt.getPropertyName().equals("animator")) {
            final ProgramTimeState state = (ProgramTimeState) evt.getNewValue();
            timeSlider.setValue(state.getSliderValue());
            currentTime.setText(state.getTime());
        }
    }
}
