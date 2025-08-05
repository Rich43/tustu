package org.icepdf.ri.common.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/ZoomInPageHandler.class */
public class ZoomInPageHandler extends SelectionBoxHandler implements ToolHandler {
    private static final Logger logger = Logger.getLogger(ZoomInPageHandler.class.toString());
    private Point initialPoint;

    public ZoomInPageHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
        this.initialPoint = new Point();
        selectionBoxColour = Color.DARK_GRAY;
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
        if (this.documentViewController != null) {
            updateSelectionSize(e2, this.pageViewComponent);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if ((e2.getModifiers() & 501) != 0 && (e2.getModifiers() & 16) != 0) {
            Point pageOffset = this.documentViewModel.getPageBounds(this.pageViewComponent.getPageIndex()).getLocation();
            Point mouse = e2.getPoint();
            mouse.setLocation(pageOffset.f12370x + mouse.f12370x, pageOffset.f12371y + mouse.f12371y);
            this.documentViewController.setZoomIn(mouse);
        }
        if (this.pageViewComponent != null) {
            this.pageViewComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        if (this.documentViewController != null) {
            resetRectangle(e2.getX(), e2.getY());
            this.initialPoint.setLocation(e2.getPoint());
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        if (this.documentViewController != null) {
            updateSelectionSize(e2, this.pageViewComponent);
            Point pageOffset = this.documentViewModel.getPageBounds(this.pageViewComponent.getPageIndex()).getLocation();
            Rectangle absoluteRectToDraw = new Rectangle(pageOffset.f12370x + this.rectToDraw.f12372x, pageOffset.f12371y + this.rectToDraw.f12373y, this.rectToDraw.width, this.rectToDraw.height);
            if (this.documentViewController.getViewPort() != null && absoluteRectToDraw.getWidth() > 0.0d && absoluteRectToDraw.getHeight() > 0.0d) {
                float zoom = calculateZoom(this.documentViewController, absoluteRectToDraw, this.documentViewModel);
                int pageIndex = this.pageViewComponent.getPageIndex();
                Rectangle location = this.documentViewModel.getPageBounds(pageIndex);
                Point delta = new Point(absoluteRectToDraw.f12372x - location.f12372x, absoluteRectToDraw.f12373y - location.f12373y);
                this.documentViewController.setZoomToViewPort(zoom, delta, pageIndex, true);
            }
            clearRectangle(this.pageViewComponent);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent e2) {
    }

    @Override // org.icepdf.ri.common.tools.SelectionBoxHandler
    public void setSelectionRectangle(Point cursorLocation, Rectangle selection) {
        setSelectionSize(selection, this.pageViewComponent);
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
        paintSelectionBox(g2, this.rectToDraw);
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void installTool() {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void uninstallTool() {
    }

    public static float calculateZoom(DocumentViewController documentViewController, Rectangle rectToDraw, DocumentViewModel documentViewModel) {
        Dimension viewport = documentViewController.getViewPort().getParent().getSize();
        int selectionMax = rectToDraw.width;
        int screenMax = viewport.width;
        if (screenMax < viewport.getHeight()) {
            screenMax = viewport.height;
        }
        if (selectionMax < rectToDraw.getHeight()) {
            selectionMax = rectToDraw.height;
        }
        return (screenMax / selectionMax) * documentViewModel.getViewZoom();
    }
}
