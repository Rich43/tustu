package org.icepdf.ri.common.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import org.icepdf.ri.common.views.DocumentViewController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/DynamicZoomHandler.class */
public class DynamicZoomHandler implements ToolHandler, MouseWheelListener {
    private static final Logger logger = Logger.getLogger(ZoomOutPageHandler.class.toString());
    private DocumentViewController documentViewController;
    protected JScrollPane documentScrollPane;

    public DynamicZoomHandler(DocumentViewController documentViewController, JScrollPane documentScrollPane) {
        this.documentViewController = documentViewController;
        this.documentScrollPane = documentScrollPane;
    }

    @Override // java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent e2) {
        int rotation = e2.getWheelRotation();
        this.documentScrollPane.setWheelScrollingEnabled(false);
        Point offset = this.documentScrollPane.getViewport().getViewPosition();
        int viewWidth = this.documentScrollPane.getViewport().getWidth() / 2;
        int viewHeight = this.documentScrollPane.getViewport().getHeight() / 2;
        offset.setLocation(offset.f12370x + viewWidth, offset.f12371y + viewHeight);
        if (rotation > 0) {
            this.documentViewController.setZoomOut(offset);
        } else {
            this.documentViewController.setZoomIn(offset);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void installTool() {
        this.documentScrollPane.setWheelScrollingEnabled(false);
        this.documentScrollPane.addMouseWheelListener(this);
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void uninstallTool() {
        this.documentScrollPane.setWheelScrollingEnabled(true);
        this.documentScrollPane.removeMouseWheelListener(this);
    }
}
