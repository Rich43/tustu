package org.icepdf.ri.common.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.icepdf.core.util.PropertyConstants;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/TextSelectionViewHandler.class */
public class TextSelectionViewHandler extends SelectionBoxHandler implements ToolHandler {
    private static final Logger logger = Logger.getLogger(TextSelectionViewHandler.class.toString());
    protected JComponent parentComponent;

    public TextSelectionViewHandler(DocumentViewController documentViewController, DocumentViewModel documentViewModel, JComponent parentComponent) {
        super(documentViewController, null, documentViewModel);
        this.parentComponent = parentComponent;
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if (this.parentComponent != null) {
            this.parentComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        this.documentViewController.clearSelectedText();
        resetRectangle(e2.getX(), e2.getY());
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        updateSelectionSize(e2, this.parentComponent);
        ArrayList<WeakReference<AbstractPageViewComponent>> selectedPages = this.documentViewModel.getSelectedPageText();
        AbstractPageViewComponent pageComponent = isOverPageComponent(this.parentComponent, e2);
        if (pageComponent != null) {
            MouseEvent modeEvent = SwingUtilities.convertMouseEvent(this.parentComponent, e2, pageComponent);
            if (selectedPages != null && selectedPages.size() > 0) {
                Iterator i$ = selectedPages.iterator();
                while (i$.hasNext()) {
                    WeakReference<AbstractPageViewComponent> page = i$.next();
                    AbstractPageViewComponent pageComp = page.get();
                    if (pageComp != null) {
                        pageComp.dispatchEvent(modeEvent);
                    }
                }
            }
        }
        if (selectedPages != null && selectedPages.size() > 0) {
            this.documentViewController.firePropertyChange(PropertyConstants.TEXT_SELECTED, (Object) null, (Object) null);
        }
        if (selectedPages != null && selectedPages.size() > 0) {
            Iterator i$2 = selectedPages.iterator();
            while (i$2.hasNext()) {
                WeakReference<AbstractPageViewComponent> page2 = i$2.next();
                AbstractPageViewComponent pageComp2 = page2.get();
                if (pageComp2 != null) {
                    pageComp2.clearSelectionRectangle();
                }
            }
        }
        clearRectangle(this.parentComponent);
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
        if (this.documentViewController != null) {
            updateSelectionSize(e2, this.parentComponent);
            this.documentViewModel.clearSelectedPageText();
            if (this.documentViewModel != null) {
                List<AbstractPageViewComponent> pages = this.documentViewModel.getPageComponents();
                for (AbstractPageViewComponent page : pages) {
                    Rectangle tmp = SwingUtilities.convertRectangle(this.parentComponent, getRectToDraw(), page);
                    if (page.getBounds().intersects(tmp)) {
                        this.documentViewModel.addSelectedPageText(page);
                        Rectangle selectRec = SwingUtilities.convertRectangle(this.parentComponent, this.rectToDraw, page);
                        page.setSelectionRectangle(SwingUtilities.convertPoint(this.parentComponent, e2.getPoint(), page), selectRec);
                    }
                }
            }
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent e2) {
        AbstractPageViewComponent pageComponent = isOverPageComponent(this.parentComponent, e2);
        if (pageComponent != null) {
            pageComponent.dispatchEvent(SwingUtilities.convertMouseEvent(this.parentComponent, e2, pageComponent));
        }
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
    }

    @Override // org.icepdf.ri.common.tools.SelectionBoxHandler
    public void setSelectionRectangle(Point cursorLocation, Rectangle selection) {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void installTool() {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void uninstallTool() {
    }
}
