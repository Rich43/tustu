package org.icepdf.ri.common.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.icepdf.ri.common.views.AbstractDocumentView;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/ZoomInViewHandler.class */
public class ZoomInViewHandler extends SelectionBoxHandler implements ToolHandler {
    private static final Logger logger = Logger.getLogger(ZoomInPageHandler.class.toString());
    private AbstractDocumentView parentComponent;

    public ZoomInViewHandler(DocumentViewController documentViewController, DocumentViewModel documentViewModel, AbstractDocumentView parentComponent) {
        super(documentViewController, null, documentViewModel);
        this.parentComponent = parentComponent;
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
        if (this.documentViewController != null) {
            updateSelectionSize(e2, this.parentComponent);
            if (this.documentViewModel != null) {
                List<AbstractPageViewComponent> pages = this.documentViewModel.getPageComponents();
                for (AbstractPageViewComponent page : pages) {
                    Rectangle tmp = SwingUtilities.convertRectangle(this.parentComponent, getRectToDraw(), page);
                    if (page.getBounds().intersects(tmp)) {
                        Rectangle selectRec = SwingUtilities.convertRectangle(this.parentComponent, this.rectToDraw, page);
                        page.setSelectionRectangle(SwingUtilities.convertPoint(this.parentComponent, e2.getPoint(), page), selectRec);
                    }
                }
            }
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if ((e2.getModifiers() & 501) != 0 && (e2.getModifiers() & 16) != 0) {
            this.documentViewController.setZoomIn(e2.getPoint());
        }
        if (this.parentComponent != null) {
            this.parentComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        if (this.documentViewController != null) {
            resetRectangle(e2.getX(), e2.getY());
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        if (this.documentViewController != null) {
            updateSelectionSize(e2, this.parentComponent);
            if (this.documentViewController.getViewPort() != null && this.rectToDraw.getWidth() > 0.0d && this.rectToDraw.getHeight() > 0.0d) {
                float zoom = ZoomInPageHandler.calculateZoom(this.documentViewController, this.rectToDraw, this.documentViewModel);
                Point center = new Point((int) this.rectToDraw.getCenterX(), (int) this.rectToDraw.getCenterY());
                this.documentViewController.setZoomCentered(zoom, center, true);
            }
            clearRectangle(this.parentComponent);
            List<AbstractPageViewComponent> selectedPages = this.documentViewModel.getPageComponents();
            if (selectedPages != null && selectedPages.size() > 0) {
                for (AbstractPageViewComponent pageComp : selectedPages) {
                    if (pageComp != null && pageComp.isVisible()) {
                        pageComp.clearSelectionRectangle();
                    }
                }
            }
        }
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void installTool() {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void uninstallTool() {
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
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
        paintSelectionBox(g2, this.rectToDraw);
    }
}
