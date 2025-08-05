package org.icepdf.ri.common.utility.thumbs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.ri.common.PageThumbnailComponent;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.ModifiedFlowLayout;
import org.icepdf.ri.util.PropertiesManager;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/thumbs/ThumbnailsPanel.class */
public class ThumbnailsPanel extends JPanel {
    protected DocumentViewController documentViewController;
    protected Document currentDocument;
    protected PropertiesManager propertiesManager;
    protected DocumentViewModel documentViewModel;
    protected float thumbNailZoom;
    protected static final int MAX_PAGE_SIZE_READ_AHEAD = 10;
    private SwingController controller;

    public ThumbnailsPanel(SwingController controller, PropertiesManager propertiesManager) {
        this.thumbNailZoom = 0.1f;
        this.controller = controller;
        this.propertiesManager = propertiesManager;
        if (propertiesManager != null) {
            this.thumbNailZoom = propertiesManager.getFloat(PropertiesManager.PROPERTY_UTILITYPANE_THUMBNAILS_ZOOM);
        }
    }

    public void setDocument(Document document) {
        this.currentDocument = document;
        this.documentViewController = this.controller.getDocumentViewController();
        if (document != null) {
            buildUI();
        } else {
            removeAll();
        }
    }

    public void dispose() {
        removeAll();
    }

    private void buildUI() {
        final ModifiedFlowLayout layout = new ModifiedFlowLayout();
        final JPanel pageThumbsPanel = new JPanel(layout);
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(pageThumbsPanel, 22, 30);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().addChangeListener(new ChangeListener() { // from class: org.icepdf.ri.common.utility.thumbs.ThumbnailsPanel.1
            @Override // javax.swing.event.ChangeListener
            public void stateChanged(ChangeEvent e2) {
                JViewport tmp = (JViewport) e2.getSource();
                Dimension dim = layout.computeSize(tmp.getWidth(), pageThumbsPanel);
                pageThumbsPanel.setPreferredSize(dim);
            }
        });
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() { // from class: org.icepdf.ri.common.utility.thumbs.ThumbnailsPanel.2
            @Override // java.awt.event.AdjustmentListener
            public void adjustmentValueChanged(AdjustmentEvent e2) {
                if (!e2.getValueIsAdjusting()) {
                    ThumbnailsPanel.this.repaint();
                }
            }
        });
        PageThumbnailComponent pageThumbnailComponent = null;
        PageTree pageTree = this.currentDocument.getPageTree();
        int numberOfPages = this.currentDocument.getNumberOfPages();
        int avgPageWidth = 0;
        int avgPageHeight = 0;
        for (int i2 = 0; i2 < numberOfPages; i2++) {
            if (i2 < 10) {
                pageThumbnailComponent = new PageThumbnailComponent(this.controller, scrollPane, pageTree, i2, this.thumbNailZoom);
                avgPageWidth += pageThumbnailComponent.getPreferredSize().width;
                avgPageHeight += pageThumbnailComponent.getPreferredSize().height;
            } else if (i2 > 10) {
                pageThumbnailComponent = new PageThumbnailComponent(this.controller, scrollPane, pageTree, i2, avgPageWidth, avgPageHeight, this.thumbNailZoom);
            } else if (i2 == 10) {
                avgPageWidth /= 10;
                avgPageHeight /= 10;
                pageThumbnailComponent = new PageThumbnailComponent(this.controller, scrollPane, pageTree, i2, avgPageWidth, avgPageHeight, this.thumbNailZoom);
            }
            pageThumbsPanel.add(pageThumbnailComponent);
        }
        pageThumbsPanel.revalidate();
        scrollPane.validate();
    }
}
