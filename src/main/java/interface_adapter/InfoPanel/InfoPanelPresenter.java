package interface_adapter.InfoPanel;

import usecase.infopanel.InfoPanelError;
import usecase.infopanel.InfoPanelOutputBoundary;
import usecase.infopanel.InfoPanelOutputData;

public class InfoPanelPresenter implements InfoPanelOutputBoundary {
    private final InfoPanelViewModel vm;

    public InfoPanelPresenter(InfoPanelViewModel vm) {
        this.vm = vm;
    }

    @Override public void presentLoading() {
        vm.loading = true;
        vm.error = null;
    }

    @Override public void present(InfoPanelOutputData data) {
        vm.loading = false;
        vm.error = null;
        vm.placeName = data.placeName;
        vm.tempC = data.tempC;
        vm.condition = data.condition;
        vm.hourlyTemps = data.hourlyTemps;
        vm.fetchedAt = data.fetchedAt;
    }

    @Override public void presentError(InfoPanelError error) {
        vm.loading = false;
        vm.error = error;
    }
}
