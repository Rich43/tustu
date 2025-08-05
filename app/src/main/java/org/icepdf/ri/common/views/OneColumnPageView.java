package org.icepdf.ri.common.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.icepdf.ri.common.CurrentPageChanger;
import org.icepdf.ri.common.KeyListenerPageColumnChanger;
import org.icepdf.ri.common.SwingController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/OneColumnPageView.class */
public class OneColumnPageView extends AbstractDocumentView {
    protected JScrollPane documentScrollpane;
    protected boolean disposing;
    protected JPanel pagesPanel;
    protected CurrentPageChanger currentPageChanger;
    protected KeyListenerPageColumnChanger keyListenerPageChanger;

    public OneColumnPageView(DocumentViewController documentDocumentViewController, JScrollPane documentScrollpane, DocumentViewModelImpl documentViewModel) {
        super(documentDocumentViewController, documentScrollpane, documentViewModel);
        this.documentScrollpane = documentScrollpane;
        buildGUI();
        this.currentPageChanger = new CurrentPageChanger(documentScrollpane, this, documentViewModel.getPageComponents());
        if (this.documentViewController.getParentController() instanceof SwingController) {
            this.keyListenerPageChanger = KeyListenerPageColumnChanger.install((SwingController) this.documentViewController.getParentController(), this.documentScrollpane, this, this.currentPageChanger);
        }
    }

    private void buildGUI() {
        this.pagesPanel = new JPanel();
        this.pagesPanel.setBackground(backgroundColor);
        GridLayout gridLayout = new GridLayout(0, 1, horizontalSpace, verticalSpace);
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
            for (AbstractPageViewComponent pageViewComponent : pageComponents) {
                if (pageViewComponent != null) {
                    pageViewComponent.setDocumentViewCallback(this);
                    this.pagesPanel.add(new PageViewDecorator(pageViewComponent));
                }
            }
        }
    }

    @Override // org.icepdf.ri.common.views.AbstractDocumentView, org.icepdf.ri.common.views.DocumentView
    public void updateDocumentView() {
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public int getNextPageIncrement() {
        return 1;
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public int getPreviousPageIncrement() {
        return 1;
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
            int currCompIndex = this.documentViewController.getCurrentPageIndex();
            int numComponents = this.pagesPanel.getComponentCount();
            if (currCompIndex >= 0 && currCompIndex < numComponents) {
                Component comp = this.pagesPanel.getComponent(currCompIndex);
                if (comp instanceof PageViewDecorator) {
                    PageViewDecorator pvd = (PageViewDecorator) comp;
                    Dimension dim = pvd.getPreferredSize();
                    pageViewWidth = dim.width;
                    pageViewHeight = dim.height;
                }
            }
        }
        float currentZoom = this.documentViewModel.getViewZoom();
        return new Dimension((int) (Math.abs(pageViewWidth / currentZoom) + (AbstractDocumentView.horizontalSpace * 2)), (int) (Math.abs(pageViewHeight / currentZoom) + (AbstractDocumentView.verticalSpace * 2)));
    }

    @Override // org.icepdf.ri.common.views.AbstractDocumentView, javax.swing.JComponent
    public void paintComponent(Graphics g2) {
        Rectangle clipBounds = g2.getClipBounds();
        g2.setColor(backgroundColor);
        g2.fillRect(clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height);
        super.paintComponent(g2);
    }
}
