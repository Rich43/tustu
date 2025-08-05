package org.icepdf.ri.common;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.ref.SoftReference;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.pobjects.Thumbnail;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/PageThumbnailComponent.class */
public class PageThumbnailComponent extends JComponent implements MouseListener {
    private static final Logger logger = Logger.getLogger(PageThumbnailComponent.class.toString());
    private float thumbNailZoom;
    private JScrollPane parentScrollPane;
    private PageTree pageTree;
    private int pageIndex;
    private boolean initiatedThumbnailGeneration;
    private Rectangle pageSize;
    private boolean isPageSizeCalculated;
    private SwingController controller;
    private SoftReference<Image> bufferedPageImageReference;
    private boolean disposing;
    private boolean inited;

    public PageThumbnailComponent(SwingController controller, JScrollPane parentScrollPane, PageTree pageTree, int pageNumber, float thumbNailZoom) {
        this(controller, parentScrollPane, pageTree, pageNumber, 0, 0, thumbNailZoom);
    }

    public PageThumbnailComponent(SwingController controller, JScrollPane parentScrollPane, PageTree pageTree, int pageNumber, int width, int height, float thumbNailZoom) {
        this.pageSize = new Rectangle();
        this.isPageSizeCalculated = false;
        this.disposing = false;
        this.parentScrollPane = parentScrollPane;
        this.pageTree = pageTree;
        this.pageIndex = pageNumber;
        this.controller = controller;
        this.thumbNailZoom = thumbNailZoom;
        addMouseListener(this);
        setCursor(Cursor.getPredefinedCursor(12));
        this.bufferedPageImageReference = new SoftReference<>(null);
        if (width == 0 && height == 0) {
            calculatePageSize(this.pageSize);
            this.isPageSizeCalculated = true;
        } else {
            this.pageSize.setSize(width, height);
        }
    }

    public void init() {
        if (this.inited) {
            return;
        }
        this.inited = true;
    }

    public void dispose() {
        Image pageBufferImage;
        this.disposing = true;
        removeMouseListener(this);
        if (this.bufferedPageImageReference != null && (pageBufferImage = this.bufferedPageImageReference.get()) != null) {
            pageBufferImage.flush();
        }
        this.inited = false;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return this.pageSize.getSize();
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics gg) {
        if (!this.inited) {
            init();
        }
        if (!this.isPageSizeCalculated) {
            calculatePageSize(this.pageSize);
            invalidate();
        }
        Graphics2D g2 = (Graphics2D) gg.create(0, 0, this.pageSize.width, this.pageSize.height);
        g2.setColor(Color.white);
        g2.fillRect(0, 0, this.pageSize.width, this.pageSize.height);
        Page page = this.pageTree.getPage(this.pageIndex);
        if (this.bufferedPageImageReference.get() != null) {
            g2.drawImage(this.bufferedPageImageReference.get(), 0, 0, (ImageObserver) null);
        } else if (page != null && page.getThumbnail() != null) {
            Thumbnail thumbNail = page.getThumbnail();
            this.bufferedPageImageReference = new SoftReference<>(thumbNail.getImage());
            g2.drawImage(thumbNail.getImage(), 0, 0, (ImageObserver) null);
        }
        if (this.bufferedPageImageReference.get() == null && !this.initiatedThumbnailGeneration) {
            if (this.parentScrollPane != null && this.parentScrollPane.getVerticalScrollBar().getValueIsAdjusting()) {
                return;
            }
            this.initiatedThumbnailGeneration = true;
            Library.executePainter(new PagePainter());
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if (this.controller != null) {
            this.controller.showPage(this.pageIndex);
        }
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

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/PageThumbnailComponent$PagePainter.class */
    class PagePainter implements Runnable {
        PagePainter() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (PageThumbnailComponent.this.pageTree != null) {
                BufferedImage image = new BufferedImage(PageThumbnailComponent.this.pageSize.width, PageThumbnailComponent.this.pageSize.height, 2);
                Graphics2D imageGraphics = image.createGraphics();
                Page page = PageThumbnailComponent.this.pageTree.getPage(PageThumbnailComponent.this.pageIndex);
                page.init();
                if (page != null) {
                    page.paint(imageGraphics, 1, 2, 0.0f, PageThumbnailComponent.this.thumbNailZoom, true, false);
                }
                PageThumbnailComponent.this.bufferedPageImageReference = new SoftReference(image);
                PageThumbnailComponent.this.initiatedThumbnailGeneration = false;
                SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.PageThumbnailComponent.PagePainter.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PageThumbnailComponent.this.parentScrollPane.repaint();
                    }
                });
            }
        }
    }

    private void calculatePageSize(Rectangle pageSize) {
        Page currentPage;
        if (this.pageTree != null && (currentPage = this.pageTree.getPage(this.pageIndex)) != null) {
            if (currentPage.getThumbnail() != null) {
                pageSize.setSize(currentPage.getThumbnail().getDimension());
            } else {
                pageSize.setSize(currentPage.getSize(2, 0.0f, this.thumbNailZoom).toDimension());
            }
        }
    }
}
