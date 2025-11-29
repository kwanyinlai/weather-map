package interfaceadapter.infopanel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import usecase.infopanel.InfoPanelError;
import usecase.infopanel.InfoPanelOutputBoundary;
import usecase.infopanel.InfoPanelOutputData;

public class InfoPanelPresenter implements InfoPanelOutputBoundary {
    private final InfoPanelViewModel vm;
    private final List<ChangeListener> listeners = new ArrayList<>();

    public InfoPanelPresenter(InfoPanelViewModel vmodel) {
        this.vm = vmodel;
    }

    /**
     * Adds a non-null change listener to this model.
     *
     * @param listen the listener to add; ignored if {@code null}
     */
    public void addChangeListener(ChangeListener listen) {
        if (listen != null) {
            listeners.add(listen);
        }
    }

    private void notifyChanged() {
        final ChangeEvent ev = new ChangeEvent(this);
        for (ChangeListener l : listeners) {
            l.stateChanged(ev);
        }
    }

    @Override
    public void presentLoading() {
        vm.setLoading(true);
        vm.setError(null);
        vm.setVisible(true);
        notifyChanged();
    }

    @Override
    public void present(InfoPanelOutputData data) {
        vm.setLoading(false);
        vm.setError(null);
        vm.setVisible(true);

        vm.setPlaceName(data.getPlaceName());
        vm.setTempC(data.getTempC());
        vm.setCondition(data.getCondition());
        vm.setHourlyTemps(data.getHourlyTemps());
        vm.setFetchedAt(data.getFetchedAt());

        notifyChanged();
    }

    @Override public void presentError(InfoPanelError error) {
        vm.setLoading(false);
        vm.setError(error);

        vm.setVisible(!(error == InfoPanelError.HIDDEN_BY_ZOOM
                || error == InfoPanelError.USER_CLOSED));

        if (!vm.getVisible()) {
            vm.setPlaceName(null);
            vm.setTempC(null);
            vm.setCondition(null);
            vm.setHourlyTemps(java.util.Collections.emptyList());
            vm.setFetchedAt(null);
        }

        notifyChanged();
    }
}
