package usecase.infopanel;

public interface InfoPanelOutputBoundary {
        void present(InfoPanelResponseModel res);
        void presentLoading();
        void presentError(String message);
    }