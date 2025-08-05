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
import org.icepdf.ri.common.KeyListenerPageChanger;
import org.icepdf.ri.common.MouseWheelListenerPageChanger;
import org.icepdf.ri.common.SwingController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/OnePageView.class */
public class OnePageView extends AbstractDocumentView {
    protected JScrollPane documentScrollpane;
    protected boolean disposing;
    protected JPanel pagesPanel;
    protected Object pageChangerListener;
    protected KeyListenerPageChanger keyListenerPageChanger;

    public OnePageView(DocumentViewController documentDocumentViewController, JScrollPane documentScrollpane, DocumentViewModelImpl documentViewModel) {
        super(documentDocumentViewController, documentScrollpane, documentViewModel);
        this.documentScrollpane = documentScrollpane;
        buildGUI();
        if (this.documentViewController.getParentController() instanceof SwingController) {
            this.pageChangerListener = MouseWheelListenerPageChanger.install((SwingController) this.documentViewController.getParentController(), this.documentScrollpane, this);
            this.keyListenerPageChanger = KeyListenerPageChanger.install((SwingController) this.documentViewController.getParentController(), this.documentScrollpane, this);
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
        updateDocumentView();
        setLayout(new GridBagLayout());
        add(this.pagesPanel, gbc);
    }

    @Override // org.icepdf.ri.common.views.AbstractDocumentView, org.icepdf.ri.common.views.DocumentView
    public void updateDocumentView() {
        List<AbstractPageViewComponent> pageComponents = this.documentViewModel.getPageComponents();
        if (pageComponents != null) {
            PageViewComponent pageViewComponent = pageComponents.get(this.documentViewModel.getViewCurrentPageIndex());
            if (pageViewComponent != null) {
                this.pagesPanel.removeAll();
                pageViewComponent.setDocumentViewCallback(this);
                this.pagesPanel.add(new PageViewDecorator((AbstractPageViewComponent) pageViewComponent));
                pageViewComponent.invalidate();
            }
            this.documentScrollpane.revalidate();
            for (AbstractPageViewComponent pageViewCom : pageComponents) {
                if (pageViewCom != null) {
                    pageViewCom.setDocumentViewCallback(this);
                }
            }
        }
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
        if (this.pageChangerListener != null) {
            MouseWheelListenerPageChanger.uninstall(this.documentScrollpane, this.pageChangerListener);
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
            int count = this.pagesPanel.getComponentCount();
            int i2 = 0;
            while (true) {
                if (i2 >= count) {
                    break;
                }
                Component comp = this.pagesPanel.getComponent(i2);
                if (!(comp instanceof PageViewDecorator)) {
                    i2++;
                } else {
                    PageViewDecorator pvd = (PageViewDecorator) comp;
                    Dimension dim = pvd.getPreferredSize();
                    pageViewWidth = dim.width;
                    pageViewHeight = dim.height;
                    break;
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
