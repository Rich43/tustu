package org.icepdf.ri.common.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.icepdf.ri.common.CurrentPageChanger;
import org.icepdf.ri.common.KeyListenerPageColumnChanger;
import org.icepdf.ri.common.SwingController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/TwoColumnPageView.class */
public class TwoColumnPageView extends AbstractDocumentView {
    protected JScrollPane documentScrollpane;
    protected boolean disposing;
    protected JPanel pagesPanel;
    protected int viewAlignment;
    protected CurrentPageChanger currentPageChanger;
    protected KeyListenerPageColumnChanger keyListenerPageChanger;

    public TwoColumnPageView(DocumentViewController documentDocumentViewController, JScrollPane documentScrollpane, DocumentViewModelImpl documentViewModel, int viewAlignment) {
        super(documentDocumentViewController, documentScrollpane, documentViewModel);
        this.documentScrollpane = documentScrollpane;
        this.viewAlignment = viewAlignment;
        buildGUI();
        this.currentPageChanger = new CurrentPageChanger(documentScrollpane, this, documentViewModel.getPageComponents());
        if (this.documentViewController.getParentController() instanceof SwingController) {
            this.keyListenerPageChanger = KeyListenerPageColumnChanger.install((SwingController) this.documentViewController.getParentController(), this.documentScrollpane, this, this.currentPageChanger);
        }
    }

    private void buildGUI() {
        this.pagesPanel = new JPanel();
        this.pagesPanel.setBackground(backgroundColor);
        GridLayout gridLayout = new GridLayout(0, 2, horizontalSpace, verticalSpace);
        this.pagesPanel.setLayout(gridLayout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1.0d;
        gbc.weightx = 1.0d;
        gbc.insets = new Insets(layoutInserts, layoutInserts, layoutInserts, layoutInserts);
        gbc.gridwidth = 0;
        setLayout(new GridBagLayout());
        add(this.pagesPanel, gbc);
        List<AbstractPageViewComponent> pageComponents = this.documentViewModel.getPageComponents();
        if (pageComponents != null) {
            int max = pageComponents.size();
            int max2 = pageComponents.size();
            for (int i2 = 0; i2 < max && i2 < max2; i2++) {
                if (i2 == 0 && max2 > 2 && this.viewAlignment == 1) {
                    this.pagesPanel.add(new JLabel());
                }
                PageViewComponent pageViewComponent = pageComponents.get(i2);
                if (pageViewComponent != null) {
                    pageViewComponent.setDocumentViewCallback(this);
                    this.pagesPanel.add(new PageViewDecorator((AbstractPageViewComponent) pageViewComponent));
                }
            }
        }
    }

    @Override // org.icepdf.ri.common.views.AbstractDocumentView, org.icepdf.ri.common.views.DocumentView
    public void updateDocumentView() {
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public int getNextPageIncrement() {
        return 2;
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public int getPreviousPageIncrement() {
        return 2;
    }

    @Override // org.icepdf.ri.common.views.AbstractDocumentView, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        this.currentPageChanger.mouseReleased(e2);
    }

    @Override // org.icepdf.ri.common.views.AbstractDocumentView, org.icepdf.ri.common.views.DocumentView
    public void dispose() {
        this.disposing = true;
        if (this.currentPageChanger != null) {
            this.currentPageChanger.dispose();
        }
        if (this.keyListenerPageChanger != null) {
            this.keyListenerPageChanger.uninstall();
        }
        this.pagesPanel.removeAll();
        this.pagesPanel.invalidate();
        super.dispose();
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public Dimension getDocumentSize() {
        float pageViewWidth = 0.0f;
        float pageViewHeight = 0.0f;
        if (this.pagesPanel != null) {
            int currPageIndex = this.documentViewController.getCurrentPageIndex();
            int currCompIndex = currPageIndex;
            int numComponents = this.pagesPanel.getComponentCount();
            boolean foundCurrent = false;
            while (true) {
                if (currCompIndex < 0 || currCompIndex >= numComponents) {
                    break;
                }
                Component comp = this.pagesPanel.getComponent(currCompIndex);
                if (comp instanceof PageViewDecorator) {
                    PageViewDecorator pvd = (PageViewDecorator) comp;
                    PageViewComponent pvc = pvd.getPageViewComponent();
                    if (pvc.getPageIndex() == currPageIndex) {
                        Dimension dim = pvd.getPreferredSize();
                        pageViewWidth = dim.width;
                        pageViewHeight = dim.height;
                        foundCurrent = true;
                        break;
                    }
                }
                currCompIndex++;
            }
            if (foundCurrent) {
                boolean evenPageIndex = (currPageIndex & 1) == 0;
                boolean bumpedIndex = currCompIndex != currPageIndex;
                boolean onLeft = evenPageIndex ^ bumpedIndex;
                int otherCompIndex = onLeft ? currCompIndex + 1 : currCompIndex - 1;
                if (otherCompIndex >= 0 && otherCompIndex < numComponents) {
                    Component comp2 = this.pagesPanel.getComponent(otherCompIndex);
                    if (comp2 instanceof PageViewDecorator) {
                        Dimension dim2 = ((PageViewDecorator) comp2).getPreferredSize();
                        pageViewWidth = dim2.width;
                        pageViewHeight = dim2.height;
                    }
                }
            }
        }
        float currentZoom = this.documentViewModel.getViewZoom();
        return new Dimension((int) ((Math.abs(pageViewWidth / currentZoom) * 2.0f) + (AbstractDocumentView.horizontalSpace * 4)), (int) (Math.abs(pageViewHeight / currentZoom) + (AbstractDocumentView.verticalSpace * 2)));
    }

    @Override // org.icepdf.ri.common.views.AbstractDocumentView, javax.swing.JComponent
    public void paintComponent(Graphics g2) {
        Rectangle clipBounds = g2.getClipBounds();
        g2.setColor(backgroundColor);
        g2.fillRect(clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height);
        super.paintComponent(g2);
    }
}
