package view;

import interfaceadapter.infopanel.InfoPanelController;
import interfaceadapter.infopanel.InfoPanelViewModel;
import usecase.infopanel.InfoPanelError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class InfoPanelView extends JPanel {

    private InfoPanelViewModel vm;

    private InfoPanelController controller;

    private Rectangle closeRect = new Rectangle();
    private boolean hoverClose = false;

    private static final int PAD = 18, RADIUS = 20;
    private static final Color BG = new Color(255,255,255,235);
    private static final Color STROKE = new Color(30,30,30);
    private static final Color SUBTLE = new Color(0,0,0,110);
    private static final Color CLOSE_BG = new Color(0,0,0,30);
    private static final Color CLOSE_BG_HOVER = new Color(0,0,0,60);
    private static final Font F_HEADER = new Font("SansSerif", Font.BOLD, 20);
    private static final Font F_CITY   = new Font("SansSerif", Font.BOLD, 26);
    private static final Font F_BIG    = new Font("SansSerif", Font.PLAIN, 18);
    private static final Font F_BODY   = new Font("SansSerif", Font.PLAIN, 14);
    private static final DateTimeFormatter HOUR_FMT =
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());

    public InfoPanelView(InfoPanelViewModel infoPanelViewModel) {
        this.vm = infoPanelViewModel;
        setOpaque(false);
        setPreferredSize(new Dimension(440, 560));

        addMouseMotionListener(new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                boolean h = closeRect.contains(e.getPoint());
                if (h != hoverClose) { hoverClose = h; repaint(closeRect); }
                setCursor(h ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor());
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (closeRect.contains(e.getPoint()) && controller != null) {
                    controller.onCloseRequested();
                    setVisible(false);
                }
            }
        });
    }

    public void setController(InfoPanelController controller) { this.controller = controller; }

    public void setViewModel(InfoPanelViewModel vm) { this.vm = vm; repaint(); }

    public void refresh() { repaint(); }


    @Override
    protected void paintComponent(Graphics g0) {
        if (vm == null || !vm.visible) {
            return;
        }

        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        g.setColor(BG);
        g.fillRoundRect(0,0,w,h,RADIUS,RADIUS);
        g.setColor(STROKE);
        g.drawRoundRect(0,0,w-1,h-1,RADIUS,RADIUS);

        int btnSize = 28;
        closeRect.setBounds(w - PAD - btnSize, PAD - 4, btnSize, btnSize);
        g.setColor(hoverClose ? CLOSE_BG_HOVER : CLOSE_BG);
        g.fillRoundRect(closeRect.x, closeRect.y, closeRect.width, closeRect.height, 10, 10);
        g.setColor(STROKE);
        g.drawRoundRect(closeRect.x, closeRect.y, closeRect.width, closeRect.height, 10, 10);
        g.setFont(F_BIG);
        FontMetrics fmClose = g.getFontMetrics();
        String closeChar = "\u2715";
        int cx = closeRect.x + (closeRect.width - fmClose.stringWidth(closeChar)) / 2;
        int cy = closeRect.y + (closeRect.height + fmClose.getAscent() - fmClose.getDescent()) / 2;
        g.drawString(closeChar, cx, cy);

        int x = PAD, y = PAD;

        if (vm == null || vm.loading) {
            y += drawLeft(g, "Loading weather…", x, y, F_BIG, STROKE);
            g.dispose();
            return;
        }

        // Header
        int headerTop = y;
        int rightColX = w - PAD;

        int lh = drawLeft(g, fallback("(City Name)", vm.placeName), x, y, F_CITY, STROKE);

        String tempStr = (vm.tempC == null) ? "(Temperature)" : String.format("%.1f °C", vm.tempC);
        String condStr = fallback("(Weather)", vm.condition);

        int ry = headerTop;
        ry += drawRight(g, tempStr, rightColX, ry, F_BIG, STROKE);
        ry += drawRight(g, condStr, rightColX, ry, F_BODY, SUBTLE);

        y += Math.max(lh, ry - headerTop) + 10;

        g.setColor(SUBTLE);
        g.drawLine(PAD, y, w - PAD, y);
        y += 16;

        // NOW
        y += drawLeft(g, "Now", x, y, F_HEADER, STROKE) + 6;
        String ts = (vm.fetchedAt == null) ? "(time unknown)" : HOUR_FMT.format(vm.fetchedAt) + " • local";
        y += drawLeft(g, ts, x, y, F_BODY, SUBTLE) + 12;

        // Hourly Forecast
        y += drawLeft(g, "Hourly Forecast", x, y, F_HEADER, STROKE) + 8;
        int timeCol = x, valCol = x + 120;
        List<Double> temps = vm.hourlyTemps;
        int rowH = g.getFontMetrics(F_BODY).getHeight() + 4;

        if (temps != null && !temps.isEmpty()) {
            int shown = Math.min(10, temps.size());
            for (int i = 0; i < shown && y + rowH < h - PAD; i++) {
                Instant t = addHours(vm.fetchedAt, i);
                String left  = (t == null) ? "(time)" : HOUR_FMT.format(t);
                Double tc = temps.get(i);
                String right = (tc == null) ? "(temperature)" : String.format("%.0f°C", tc);

                drawLeft(g, left,  timeCol, y, F_BODY, STROKE);
                drawLeft(g, right, valCol, y, F_BODY, STROKE);
                y += rowH;
            }
        } else {
            for (int i = 0; i < 10 && y + rowH < h - PAD; i++) {
                drawLeft(g, "(time)", timeCol, y, F_BODY, SUBTLE);
                drawLeft(g, "(temperature)", valCol, y, F_BODY, SUBTLE);
                y += rowH;
            }
        }

        g.dispose();
    }

    // helpers
    private static String fallback(String fb, String s){ return (s == null || s.trim().isEmpty()) ? fb : s; }

    private static int drawLeft(Graphics2D g, String t, int x, int y, Font f, Color c){
        g.setFont(f);
        g.setColor(c);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(t, x, y + fm.getAscent());
        return fm.getHeight();
    }

    private static int drawRight(Graphics2D g, String t, int rx, int y, Font f, Color c){
        g.setFont(f);
        g.setColor(c);
        FontMetrics fm = g.getFontMetrics();
        int tw = fm.stringWidth(t);
        g.drawString(t, rx - tw, y + fm.getAscent());
        return fm.getHeight();
    }

    private static Instant addHours(Instant base, int i){
        if (base == null) return null;
        return base.plusSeconds(3600L * i);
    }
}
