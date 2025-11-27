package view;
import entity.LocationWithName;
import interfaceadapter.searchbar.SearchBarViewModel;
import interfaceadapter.searchbar.SearchBarController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;

import static constants.Constants.*;

public class SearchBarView extends JPanel {
    private final JMapViewer mapViewer;
    private final JTextField searchField = new JTextField(10);
    private final JList<LocationWithName> resultList = new JList<>();
    private final transient SearchBarController controller;
    private final transient SearchBarViewModel viewModel;

    public SearchBarView(SearchBarViewModel viewModel, SearchBarController controller, JMapViewer mapViewer) {
        this.mapViewer = mapViewer;
        this.viewModel = viewModel;
        this.controller = controller;
        bindEvents();
        bindViewModel();
    }
    private void bindViewModel() {
        viewModel.addPropertyChangeListener(e -> {
            if (SearchBarViewModel.SEARCH_RESULTS_PROPERTY.equals(e.getPropertyName())) {
                List<LocationWithName> newResults = viewModel.getSearchResults();
                SwingUtilities.invokeLater(() ->
                        resultList.setListData(newResults.toArray(new LocationWithName[0]))
                );
            }
        });
    }
    private void bindEvents() {
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(this::onSearchClick);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.add(new JLabel("Search Location："));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.setPreferredSize(new Dimension(SEARCH_PANEL_WIDTH, SEARCH_PANEL_HEIGHT));

        JScrollPane resultScroll = new JScrollPane(resultList);
        resultScroll.setPreferredSize(new Dimension(RESULTSCROLL_SIZE_WIDTH, RESULTSCROLL_SIZE_HEIGHT));
        resultScroll.setMaximumSize(new Dimension(RESULTSCROLL_SIZE_WIDTH, RESULTSCROLL_SIZE_HEIGHT));
        resultList.setVisibleRowCount(NUM_VISIBLE_SEARCH_RESULTS);
        resultList.setCellRenderer((list, value,
                                    index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.toString());
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
                label.setOpaque(true);
            }
            return label;
        });

        setLayout(new BorderLayout(10, 10));
        add(searchPanel, BorderLayout.NORTH);
        add(resultScroll, BorderLayout.CENTER);
        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                LocationWithName selected = resultList.getSelectedValue();
                if (selected != null && selected.getCoordinate() != null) {
                    mapViewer.setDisplayPosition(selected.getCoordinate(), AFTER_SEARCHING_ZOOM_LEVEL);
                }
            }
        });
    }

    private void onSearchClick(ActionEvent e) {
        String query = searchField.getText().trim();
        controller.handleSearch(query);
    }
}
