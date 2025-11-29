package view;

import static constants.Constants.*;
import static interfaceadapter.infopanel.InfoPanelViewModel.*;

import java.awt.*;
import java.awt.event.*;
import java.time.Instant;
import java.util.List;

import javax.swing.*;

import interfaceadapter.infopanel.InfoPanelController;
import interfaceadapter.infopanel.InfoPanelPresenter;
import interfaceadapter.infopanel.InfoPanelViewModel;

public final class InfoPanelView extends JPanel {

    private static final int WIDTH = INFO_PANEL_WIDTH;
    private static final int HEIGHT = INFO_PANEL_HEIGHT;

    private final InfoPanelViewModel vm;
    private InfoPanelController controller;

    private boolean belowZoomThreshold;

    private final Rectangle closeRect = new Rectangle();
    private boolean hoverClose;

    private ComponentListener parentResizeListener;

    public InfoPanelView(InfoPanelViewModel infoPanelViewModel) {
        this.vm = infoPanelViewModel;
        setOpaque(false);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                final boolean h = closeRect.contains(e.getPoint());
                if (h != hoverClose) {
                    hoverClose = h;
                    repaint(closeRect);
                }
                if (h) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (closeRect.contains(e.getPoint()) && controller != null) {
                    controller.onCloseRequested();
                    SwingUtilities.invokeLater(InfoPanelView.this::repaint);
                }
            }
        });
    }

    /**
     * Binds this view to the given presenter.
     * Registers a change listener on the presenter so that whenever the presenter
     * updates, this view is repainted on the Swing event dispatch thread.
     *
     * @param presenter the presenter to bind to this view
     */
    public void bind(InfoPanelPresenter presenter) {
        presenter.addChangeListener(
                event -> SwingUtilities.invokeLater(InfoPanelView.this::repaint)
        );
    }

    public void setController(InfoPanelController controller) {
        this.controller = controller;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        relayoutToBottomLeft();
        final Container p = getParent();
        if (p != null && parentResizeListener == null) {
            parentResizeListener = new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    relayoutToBottomLeft();
                }
            };
            p.addComponentListener(parentResizeListener);
        }
    }

    @Override
    public void removeNotify() {
        final Container p = getParent();
        if (p != null && parentResizeListener != null) {
            p.removeComponentListener(parentResizeListener);
        }
        parentResizeListener = null;
        super.removeNotify();
    }

    private void relayoutToBottomLeft() {
        final Container p = getParent();
        final int ch = p.getHeight();
        final int y = Math.max(MARGIN, ch - HEIGHT - MARGIN);
        setBounds(MARGIN, y, WIDTH, HEIGHT);
        revalidate();
        repaint();
    }

    /**
     * Handles updates to the map viewport (center and zoom level).
     * If no controller is attached, this method does nothing. When the zoom
     * level is below {@code ZOOM_THRESHOLD}, the info panel is hidden and no
     * update is forwarded to the controller. Once the zoom level goes back
     * above the threshold, the panel is made visible again and the change is
     * forwarded to the controller. For subsequent changes above the threshold,
     * the viewport change is forwarded directly to the controller.
     *
     * @param centerLat latitude of the viewport center, in degrees
     * @param centerLon longitude of the viewport center, in degrees
     * @param zoom      current zoom level of the map
     */
    public void onViewportChanged(double centerLat, double centerLon, int zoom) {
        final boolean zoomBelowThreshold = zoom < ZOOM_THRESHOLD;
        final boolean hasController = controller != null;

        if (zoomBelowThreshold) {
            if (!belowZoomThreshold) {
                belowZoomThreshold = true;
                vm.setVisible(false);
                setVisible(false);
                repaint();
            }
        }
        else {
            if (belowZoomThreshold) {
                belowZoomThreshold = false;
                vm.setVisible(true);
                if (!isVisible()) {
                    setVisible(true);
                }
                if (hasController) {
                    controller.onViewportChanged(centerLat, centerLon, zoom);
                }
                repaint();
            }
            else {
                if (hasController) {
                    controller.onViewportChanged(centerLat, centerLon, zoom);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g0) {
        final boolean shouldPaint =
                vm != null && vm.getVisible() && !vm.isLoading();
        if (shouldPaint) {
            super.paintComponent(g0);

            final Graphics2D g = (Graphics2D) g0.create();
            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                final int width = getWidth();
                final int height = getHeight();

                paintBackground(g, width, height);
                updateCloseRect(width);
                paintCloseButton(g);

                int y = PAD;
                y = paintHeaderSection(g, width, y);
                y = paintNowSection(g, width, y);
                paintHourlySection(g, height, y);
            }
            finally {
                g.dispose();
            }
        }
    }

    private static String fallback(String fallback, String str) {
        String result = fallback;
        if (str != null) {
            final String trimmed = str.trim();
            if (!trimmed.isEmpty()) {
                result = str;
            }
        }

        return result;
    }

    private static int drawLeft(Graphics2D graph, String str, int xaxis, int yaxis, Font font, Color color) {
        graph.setFont(font);
        graph.setColor(color);
        final FontMetrics fontm = graph.getFontMetrics();
        graph.drawString(str, xaxis, yaxis + fontm.getAscent());
        return fontm.getHeight();
    }

    private static int drawRight(Graphics2D graph, String str, int rightx, int yaxis, Font font, Color color) {
        graph.setFont(font);
        graph.setColor(color);
        final FontMetrics fontm = graph.getFontMetrics();
        final int tw = fontm.stringWidth(str);
        graph.drawString(str, rightx - tw, yaxis + fontm.getAscent());
        return fontm.getHeight();
    }

    private static Instant addHours(Instant base, int integ) {
        return base.plusSeconds(INFO_PANEL_SECONDS_TO_ADD * integ);
    }

    // Helpers for painting.
    private void paintBackground(Graphics2D graph, int width, int height) {
        graph.setColor(BG);
        graph.fillRoundRect(0, 0, width, height, RADIUS, RADIUS);
        graph.setColor(STROKE);
        graph.drawRoundRect(0, 0, width - 1, height - 1, RADIUS, RADIUS);
    }

    private void updateCloseRect(int width) {
        final int buttonSize = CLOSE_BUTTON_SIZE;
        closeRect.setBounds(width - PAD - buttonSize, PAD + CLOSE_BUTTON_TOP_OFFSET,
                buttonSize, buttonSize);
    }

    private void paintCloseButton(Graphics2D graph) {
        if (hoverClose) {
            graph.setColor(CLOSE_BG_HOVER);
        }
        else {
            graph.setColor(CLOSE_BG);
        }

        graph.fillRoundRect(closeRect.x, closeRect.y,
                closeRect.width, closeRect.height, ARC, ARC);

        graph.setColor(STROKE);
        graph.drawRoundRect(closeRect.x, closeRect.y,
                closeRect.width, closeRect.height, ARC, ARC);

        graph.setFont(F_BIG);
        final FontMetrics fmClose = graph.getFontMetrics();
        final String closeChar = "✕";
        final int cx = closeRect.x
                + (closeRect.width - fmClose.stringWidth(closeChar)) / 2;
        final int cy = closeRect.y
                + (closeRect.height + fmClose.getAscent()
                - fmClose.getDescent()) / 2;
        graph.drawString(closeChar, cx, cy);
    }

    private int paintHeaderSection(Graphics2D graph, int width, int startY) {
        int y = startY;
        final int headerTop = y;
        final int rightColX = width - PAD;

        final String city = fallback("(City Name)", vm.getPlaceName());
        final int leftHeight = drawLeft(graph, city, PAD, y, F_CITY, STROKE);

        final String tempStr;
        final Double tempC = vm.getTempC();
        if (tempC == null) {
            tempStr = "(Temperature)";
        }
        else {
            tempStr = String.format("%.1f °C", tempC);
        }

        final String condStr = fallback("(Weather)", vm.getCondition());

        int rightY = headerTop;
        rightY += drawRight(graph, tempStr, rightColX, rightY, F_BIG, STROKE);
        rightY += drawRight(graph, condStr, rightColX, rightY, F_BODY, SUBTLE);

        y += Math.max(leftHeight, rightY - headerTop) + ARC;
        return y;
    }

    private int paintNowSection(Graphics2D graph, int width, int startY) {
        final int timeCol = PAD;
        int y = startY;

        graph.setColor(SUBTLE);
        graph.drawLine(PAD, y, width - PAD, y);
        y += INFO_PANEL_DIVIDER_GAP;

        y += drawLeft(graph, "Now", timeCol, y, F_HEADER, STROKE) + INFO_PANEL_LABEL_GAP;

        final String ts;
        final Instant fetchedAt = vm.getFetchedAt();
        if (fetchedAt == null) {
            ts = "(time unknown)";
        }
        else {
            ts = HOUR_FMT.format(fetchedAt) + " • local";
        }
        y += drawLeft(graph, ts, timeCol, y, F_BODY, SUBTLE) + INFO_PANEL_TIME_GAP;

        y += drawLeft(graph, "Hourly Forecast", timeCol, y, F_HEADER, STROKE) + INFO_PANEL_HOURLY_HEADER_GAP;
        return y;
    }

    private void paintHourlySection(Graphics2D graph, int panelHeight, int startY) {
        final int timeCol = PAD;
        final int valueCol = timeCol + HOURLY_VALUE_COL_OFFSET;

        final List<Double> temps = vm.getHourlyTemps();
        final int rowHeight = graph.getFontMetrics(F_BODY).getHeight() + HOURLY_ROW_EXTRA_PADDING;
        int y = startY;

        if (temps != null && !temps.isEmpty()) {
            final int shown = Math.min(HOURLY_MAX_ROWS, temps.size());

            for (int i = 0; i < shown && y + rowHeight < panelHeight - PAD; i++) {
                final Instant time = addHours(vm.getFetchedAt(), i);
                final String left;
                if (time == null) {
                    left = "(time)";
                }
                else {
                    left = HOUR_FMT.format(time);
                }

                final Double t = temps.get(i);
                final String right;
                if (t == null) {
                    right = "(temperature)";
                }
                else {
                    right = String.format("%.0f°C", t);
                }

                drawLeft(graph, left, timeCol, y, F_BODY, STROKE);
                drawLeft(graph, right, valueCol, y, F_BODY, STROKE);
                y += rowHeight;
            }
        }
        else {
            for (int i = 0; i < HOURLY_MAX_ROWS && y + rowHeight < panelHeight - PAD; i++) {
                drawLeft(graph, "(time)", timeCol, y, F_BODY, SUBTLE);
                drawLeft(graph, "(temperature)", valueCol, y, F_BODY, SUBTLE);
                y += rowHeight;
            }
        }
    }
}

