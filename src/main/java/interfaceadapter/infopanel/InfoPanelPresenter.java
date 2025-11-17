package interfaceadapter.infopanel;

import usecase.infopanel.InfoPanelError;
import usecase.infopanel.InfoPanelOutputBoundary;
import usecase.infopanel.InfoPanelOutputData;

import javax.swing.SwingUtilities;
import view.InfoPanelView;

public class InfoPanelPresenter implements InfoPanelOutputBoundary {
    private final InfoPanelViewModel vm;
    private final InfoPanelView view;

    public InfoPanelPresenter(InfoPanelViewModel vm, InfoPanelView view) {
        this.vm = vm;
        this.view = view;
    }

    @Override public void presentLoading() {
        vm.loading = true;
        vm.error = null;
        repaintOnEDT();
    }

    @Override public void present(InfoPanelOutputData data) {
        vm.loading = false;
        vm.error = null;
        vm.placeName   = data.placeName;
        vm.tempC       = data.tempC;
        vm.condition   = data.condition;
        vm.hourlyTemps = data.hourlyTemps;
        vm.fetchedAt   = data.fetchedAt;
        repaintOnEDT();
    }

    @Override public void presentError(InfoPanelError error) {
        vm.loading = false;
        vm.error = error;
        vm.placeName = null;
        vm.hourlyTemps = java.util.Collections.emptyList(); vm.loading = true;

        repaintOnEDT();
    }

    private void repaintOnEDT() {
        if (view == null) return;
        if (SwingUtilities.isEventDispatchThread()) {
            view.repaint();
        } else {
            SwingUtilities.invokeLater(view::repaint);
        }
    }
}
