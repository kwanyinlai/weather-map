//package interfaceadapter.maptime.timeanimation;
//
//import usecase.maptime.UpdateMapTimeOutputData;
//
//
///** Presenter class for UpdateMapTime interactor
// *
// */
//public class TimeAnimationPresenter implements TimeAnimationOutputBoundary {
//
//    @Override
//    public void forceUpdateSlider(UpdateMapTimeOutputData newSliderValue) {
//
//        ProgramTimeState programTimeState = programTimeViewModel.getState();
//        programTimeState.setTime(formatTimeInstant(newTime.getStamp()));
//        programTimeViewModel.firePropertyChange("time slider");
//    }
//    private String formatTimeInstant(java.time.Instant instant) {
//        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//                .withZone(ZoneId.systemDefault())
//                .format(instant);
//    }
//}
