package org.icepdf.ri.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JScrollPane;
import org.icepdf.core.util.PropertyConstants;
import org.icepdf.ri.common.views.AbstractDocumentView;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.PageViewComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/CurrentPageChanger.class */
public class CurrentPageChanger extends MouseAdapter implements AdjustmentListener {
    private static final Logger logger = Logger.getLogger(CurrentPageChanger.class.toString());
    private boolean isScrolled;
    private List<AbstractPageViewComponent> pageComponents;
    private JScrollPane scrollpane;
    private Object mouseWheelCurrentPageListener;
    private AbstractDocumentView documentView;
    private DocumentViewModel documentViewModel;

    public CurrentPageChanger(JScrollPane scrollpane, AbstractDocumentView documentView, List<AbstractPageViewComponent> pageComponents) {
        this(scrollpane, documentView, pageComponents, true);
    }

    public CurrentPageChanger(JScrollPane scrollpane, AbstractDocumentView documentView, List<AbstractPageViewComponent> pageComponents, boolean addWheelMouseListener) {
        this.isScrolled = false;
        this.pageComponents = pageComponents;
        this.scrollpane = scrollpane;
        this.documentView = documentView;
        this.documentViewModel = documentView.getViewModel();
        this.documentView.addMouseListener(this);
        this.scrollpane.getHorizontalScrollBar().addAdjustmentListener(this);
        this.scrollpane.getHorizontalScrollBar().addMouseListener(this);
        this.scrollpane.getVerticalScrollBar().addAdjustmentListener(this);
        this.scrollpane.getVerticalScrollBar().addMouseListener(this);
        this.mouseWheelCurrentPageListener = MouseWheelCurrentPageListener.install(scrollpane, this);
    }

    private void addMouseListenerToAnyButtonsIn(Component comp) {
        int children = comp instanceof Container ? ((Container) comp).getComponentCount() : -1;
        for (int i2 = 0; i2 < children; i2++) {
            Component kid = ((Container) comp).getComponent(i2);
            if (kid instanceof AbstractButton) {
                kid.addMouseListener(this);
            }
            addMouseListenerToAnyButtonsIn(kid);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        if (this.isScrolled) {
            calculateCurrentPage();
            this.isScrolled = false;
        }
    }

    @Override // java.awt.event.AdjustmentListener
    public void adjustmentValueChanged(AdjustmentEvent e2) {
        this.isScrolled = true;
    }

    public void calculateCurrentPage() {
        if (this.pageComponents != null) {
            Rectangle viewport = this.scrollpane.getViewport().getViewRect();
            ArrayList<PageViewComponent> visiblePages = new ArrayList<>(10);
            int pageCount = 0;
            for (PageViewComponent pageComponent : this.pageComponents) {
                if (pageComponent != null && this.documentViewModel.getPageBounds(pageCount) != null && pageComponent.isShowing()) {
                    visiblePages.add(pageComponent);
                }
                pageCount++;
            }
            int x2 = viewport.f12372x + (viewport.width / 2);
            int y2 = viewport.f12373y + (viewport.height / 2);
            Point centerView = new Point(x2, y2);
            double minLength = Double.MAX_VALUE;
            int minPage = -1;
            Iterator i$ = visiblePages.iterator();
            while (i$.hasNext()) {
                PageViewComponent pageComponent2 = i$.next();
                if (pageComponent2 != null) {
                    Rectangle pageBounds = this.documentViewModel.getPageBounds(pageComponent2.getPageIndex());
                    int x3 = pageBounds.f12372x + (pageBounds.width / 2);
                    int y3 = pageBounds.f12373y + (pageBounds.height / 2);
                    double tmpDistance = centerView.distance(x3, y3);
                    if (tmpDistance < minLength) {
                        minLength = tmpDistance;
                        minPage = pageComponent2.getPageIndex();
                    }
                }
            }
            visiblePages.clear();
            visiblePages.trimToSize();
            int oldCurrentPage = this.documentViewModel.getViewCurrentPageIndex();
            this.documentViewModel.setViewCurrentPageIndex(minPage);
            DocumentViewControllerImpl documentViewController = (DocumentViewControllerImpl) this.documentView.getParentViewController();
            documentViewController.firePropertyChange(PropertyConstants.DOCUMENT_CURRENT_PAGE, oldCurrentPage, minPage);
        }
    }

    public void dispose() {
        this.documentView.removeMouseListener(this);
        this.scrollpane.getHorizontalScrollBar().removeAdjustmentListener(this);
        this.scrollpane.getHorizontalScrollBar().removeMouseListener(this);
        this.scrollpane.getVerticalScrollBar().removeAdjustmentListener(this);
        this.scrollpane.getVerticalScrollBar().removeMouseListener(this);
        MouseWheelCurrentPageListener.uninstall(this.scrollpane, this.mouseWheelCurrentPageListener);
    }
}
