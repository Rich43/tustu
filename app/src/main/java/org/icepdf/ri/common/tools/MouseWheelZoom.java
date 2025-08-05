package org.icepdf.ri.common.tools;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollPane;
import org.icepdf.ri.common.views.DocumentViewController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/MouseWheelZoom.class */
public class MouseWheelZoom implements MouseWheelListener {
    protected DocumentViewController documentViewController;
    protected JScrollPane documentScrollPane;

    public MouseWheelZoom(DocumentViewController documentViewController, JScrollPane documentScrollPane) {
        this.documentScrollPane = documentScrollPane;
        this.documentViewController = documentViewController;
    }

    @Override // java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent e2) {
        int rotation = e2.getWheelRotation();
        if ((e2.getModifiers() & 2) == 2 || (e2.getModifiers() & 4) == 4) {
            this.documentScrollPane.setWheelScrollingEnabled(false);
            Point offset = this.documentScrollPane.getViewport().getViewPosition();
            int viewWidth = this.documentScrollPane.getViewport().getWidth() / 2;
            int viewHeight = this.documentScrollPane.getViewport().getHeight() / 2;
            offset.setLocation(offset.f12370x + viewWidth, offset.f12371y + viewHeight);
            if (rotation > 0) {
                this.documentViewController.setZoomOut(offset);
                return;
            } else {
                this.documentViewController.setZoomIn(offset);
                return;
            }
        }
        this.documentScrollPane.setWheelScrollingEnabled(true);
    }
}
