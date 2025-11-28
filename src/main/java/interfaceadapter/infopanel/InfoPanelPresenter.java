package interfaceadapter.infopanel;

import usecase.infopanel.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections.*;

public class InfoPanelPresenter implements InfoPanelOutputBoundary {
    private final InfoPanelViewModel vm;
    private final List<ChangeListener> listeners = new ArrayList<>();

    public InfoPanelPresenter(InfoPanelViewModel vm) {
        this.vm = vm;
    }

    public void addChangeListener(ChangeListener l) {
        if (l != null){
            listeners.add(l);
        }
    }
    private void notifyChanged() {
        ChangeEvent ev = new ChangeEvent(this);
        for (ChangeListener l : listeners){
            l.stateChanged(ev);
        }
    }

    @Override
    public void presentLoading() {
        vm.loading = true;
        vm.error = null;
        vm.visible = true;
        notifyChanged();
    }

    @Override
    public void present(InfoPanelOutputData data) {
        vm.loading = false;
        vm.error = null;
        vm.visible = true;

        vm.placeName   = data.placeName;
        vm.tempC       = data.tempC;
        vm.condition   = data.condition;
        vm.hourlyTemps = data.hourlyTemps;
        vm.fetchedAt   = data.fetchedAt;

        notifyChanged();
    }

    @Override public void presentError(InfoPanelError error) {
        vm.loading = false;
        vm.error   = error;

        vm.visible = !(error == InfoPanelError.HIDDEN_BY_ZOOM
                || error == InfoPanelError.USER_CLOSED);

        if (!vm.visible) {
            vm.placeName = null;
            vm.tempC     = null;
            vm.condition = null;
            vm.hourlyTemps = java.util.Collections.emptyList();
            vm.fetchedAt   = null;
        }

        notifyChanged();
    }
}
