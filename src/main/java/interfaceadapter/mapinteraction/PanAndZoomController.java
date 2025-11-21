package interfaceadapter.mapinteraction;

import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import usecase.mapinteraction.PanAndZoomInputBoundary;
import usecase.mapinteraction.PanAndZoomInputData;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import java.awt.*;
import java.awt.event.*;
public class PanAndZoomController implements JMapViewerEventListener {
    private final PanAndZoomInputBoundary useCase;
    private final JMapViewer mapViewer;

    public PanAndZoomController(PanAndZoomInputBoundary useCase, JMapViewer mapViewer) {
        this.useCase = useCase;
        this.mapViewer = mapViewer;
        setupListeners();
    }

    private void setupListeners() {
        // 添加JMapViewer事件监听器
        mapViewer.addJMVListener(this);

        // 确保保留JMapViewer原有的鼠标监听器
        mapViewer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                syncViewport();
            }
        });

        // 初始同步一次
        syncViewport();
    }

    private void syncViewport() {
        // 从JMapViewer直接获取所有必要数据
        int zoom = mapViewer.getZoom();
        Point center = mapViewer.getCenter();
        int width = mapViewer.getWidth();
        int height = mapViewer.getHeight();

        // 创建输入数据并调用用例
        PanAndZoomInputData input = new PanAndZoomInputData(zoom, center.x, center.y, width, height);
        useCase.updateViewport(input);
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        // 当地图发生移动或缩放时同步Viewport
        if (command.getCommand() == JMVCommandEvent.COMMAND.MOVE ||
                command.getCommand() == JMVCommandEvent.COMMAND.ZOOM) {
            syncViewport();
        }
    }

}

