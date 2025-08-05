package org.icepdf.ri.common.tools;

import java.awt.Adjustable;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import org.icepdf.ri.common.views.AbstractDocumentView;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/PanningHandler.class */
public class PanningHandler implements ToolHandler {
    private Point lastMousePosition = new Point();
    private DocumentViewController documentViewController;
    private DocumentViewModel documentViewModel;
    private AbstractDocumentView parentComponent;

    public PanningHandler(DocumentViewController documentViewController, DocumentViewModel documentViewModel, AbstractDocumentView parentComponent) {
        this.documentViewController = documentViewController;
        this.documentViewModel = documentViewModel;
        this.parentComponent = parentComponent;
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
        if (this.documentViewController != null) {
            Adjustable verticalScrollbar = this.documentViewController.getVerticalScrollBar();
            Adjustable horizontalScrollbar = this.documentViewController.getHorizontalScrollBar();
            if (verticalScrollbar != null && horizontalScrollbar != null) {
                Point p2 = new Point(((int) e2.getPoint().getX()) - horizontalScrollbar.getValue(), ((int) e2.getPoint().getY()) - verticalScrollbar.getValue());
                int x2 = (int) (horizontalScrollbar.getValue() - (p2.getX() - this.lastMousePosition.getX()));
                int y2 = (int) (verticalScrollbar.getValue() - (p2.getY() - this.lastMousePosition.getY()));
                horizontalScrollbar.setValue(x2);
                verticalScrollbar.setValue(y2);
                this.lastMousePosition.setLocation(p2);
            }
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent e2) {
        if (this.documentViewController != null) {
            Adjustable verticalScrollbar = this.documentViewController.getVerticalScrollBar();
            Adjustable horizontalScrollbar = this.documentViewController.getHorizontalScrollBar();
            this.lastMousePosition.setLocation(e2.getPoint().getX() - horizontalScrollbar.getValue(), e2.getPoint().getY() - verticalScrollbar.getValue());
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        this.documentViewController.clearSelectedAnnotations();
        if (this.parentComponent != null) {
            this.parentComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        if (this.documentViewController != null && this.documentViewController.getDocumentViewModel().isViewToolModeSelected(1)) {
            this.documentViewController.setViewCursor(2);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        if (this.documentViewController != null && this.documentViewController.getDocumentViewModel().getViewToolMode() == 1) {
            this.documentViewController.setViewCursor(1);
        }
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
