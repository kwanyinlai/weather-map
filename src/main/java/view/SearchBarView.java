package view;
import entity.LocationWithName;
import interfaceadapter.searchbar.SearchBarViewModel;
import interfaceadapter.searchbar.SearchBarController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;

public class SearchBarView extends JPanel {
    private JMapViewer mapViewer;
    private final JTextField searchField = new JTextField(10);
    private final JList<LocationWithName> resultList = new JList<>();
    private final SearchBarController controller;
    private final SearchBarViewModel viewModel;

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
        searchPanel.setPreferredSize(new Dimension(250, 60));

        JScrollPane resultScroll = new JScrollPane(resultList);
        resultScroll.setPreferredSize(new Dimension(250, 100));
        resultScroll.setMaximumSize(new Dimension(250, 100));
        resultList.setVisibleRowCount(3);
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
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        setPreferredSize(new Dimension(260, 180));
        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                LocationWithName selected = resultList.getSelectedValue();
                if (selected != null && selected.getCoordinate() != null) {
                    mapViewer.setDisplayPosition(selected.getCoordinate(), 10);
                }
            }
        });
    }

    private void onSearchClick(ActionEvent e) {
        String query = searchField.getText().trim();
        controller.handleSearch(query);
    }
}
