package usecase.infopanel;

public interface InfoPanelOutputBoundary {
    void presentLoading();
    void present(InfoPanelOutputData data);
    void presentError(InfoPanelError error);
}