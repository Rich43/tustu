package org.icepdf.ri.common.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/ZoomOutPageHandler.class */
public class ZoomOutPageHandler implements ToolHandler {
    private static final Logger logger = Logger.getLogger(ZoomOutPageHandler.class.toString());
    private AbstractPageViewComponent pageViewComponent;
    private DocumentViewController documentViewController;
    private DocumentViewModel documentViewModel;

    public ZoomOutPageHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        this.documentViewController = documentViewController;
        this.pageViewComponent = pageViewComponent;
        this.documentViewModel = documentViewModel;
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if ((e2.getModifiers() & 501) != 0 && (e2.getModifiers() & 16) != 0) {
            Point pageOffset = this.documentViewModel.getPageBounds(this.pageViewComponent.getPageIndex()).getLocation();
            Point mouse = e2.getPoint();
            mouse.setLocation(pageOffset.f12370x + mouse.f12370x, pageOffset.f12371y + mouse.f12371y);
            this.documentViewController.setZoomOut(mouse);
        }
        if (this.pageViewComponent != null) {
            this.pageViewComponent.requestFocus();
        }
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
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void uninstallTool() {
    }
}
