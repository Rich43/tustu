package org.icepdf.ri.common.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.icepdf.core.events.PaintPageEvent;
import org.icepdf.core.events.PaintPageListener;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.pobjects.annotations.FreeTextAnnotation;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.search.DocumentSearchController;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PropertyConstants;
import org.icepdf.ri.common.tools.SelectionBoxHandler;
import org.icepdf.ri.common.tools.TextSelectionPageHandler;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.FreeTextAnnotationComponent;
import org.icepdf.ri.common.views.annotations.PopupAnnotationComponent;
import org.icepdf.ri.common.views.listeners.DefaultPageViewLoadingListener;
import org.icepdf.ri.common.views.listeners.PageViewLoadingListener;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/PageViewComponentImpl.class */
public class PageViewComponentImpl extends AbstractPageViewComponent implements PaintPageListener, FocusListener, ComponentListener {
    private static final Logger logger = Logger.getLogger(PageViewComponentImpl.class.toString());
    private static Color pageColor;
    private static boolean enablePageLoadingProxy;
    private PageTree pageTree;
    private JScrollPane parentScrollPane;
    private int previousScrollValue;
    private int pageIndex;
    private Rectangle pageSize;
    private Rectangle defaultPageSize;
    private boolean isPageSizeCalculated;
    private float currentZoom;
    private float currentRotation;
    private SoftReference<Image> bufferedPageImageReference;
    private Rectangle bufferedPageImageBounds;
    private static Timer isDirtyTimer;
    private DirtyTimerAction dirtyTimerAction;
    private boolean isActionListenerRegistered;
    private PageInitializer pageInitializer;
    private PagePainter pagePainter;
    private final Object paintCopyAreaLock;
    private boolean disposing;
    private PageViewLoadingListener pageLoadingListener;
    private Rectangle clipBounds;
    private Rectangle oldClipBounds;
    private boolean inited;
    private static double verticalScaleFactor;
    private static double horizontalScaleFactor;
    private static int dirtyTimerInterval;
    private static int scrollInitThreshold;
    private static GraphicsConfiguration gc;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.paper.color", "#FFFFFF");
            int colorValue = ColorUtil.convertColor(color);
            pageColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("FFFFFF", 16));
        } catch (NumberFormatException e2) {
            logger.warning("Error reading page paper color.");
        }
        enablePageLoadingProxy = Defs.booleanProperty("org.icepdf.core.views.page.proxy", true);
        dirtyTimerInterval = 5;
        scrollInitThreshold = 250;
        try {
            verticalScaleFactor = Double.parseDouble(Defs.sysProperty("org.icepdf.core.views.buffersize.vertical", "1.25"));
            horizontalScaleFactor = Double.parseDouble(Defs.sysProperty("org.icepdf.core.views.buffersize.horizontal", "1.25"));
        } catch (NumberFormatException e3) {
            logger.warning("Error reading buffered scale factor");
        }
        try {
            dirtyTimerInterval = Defs.intProperty("org.icepdf.core.views.dirtytimer.interval", dirtyTimerInterval);
        } catch (NumberFormatException e4) {
            logger.log(Level.FINE, "Error reading dirty timer interval");
        }
        try {
            scrollInitThreshold = Defs.intProperty("org.icepdf.core.views.scroll.initThreshold", scrollInitThreshold);
        } catch (NumberFormatException e5) {
            logger.log(Level.FINE, "Error reading init threshold timer interval");
        }
    }

    public PageViewComponentImpl(DocumentViewModel documentViewModel, PageTree pageTree, int pageNumber, JScrollPane parentScrollPane) {
        this(documentViewModel, pageTree, pageNumber, parentScrollPane, 0, 0);
    }

    public PageViewComponentImpl(DocumentViewModel documentViewModel, PageTree pageTree, int pageNumber, JScrollPane parentScrollPane, int width, int height) {
        this.pageSize = new Rectangle();
        this.defaultPageSize = new Rectangle();
        this.isPageSizeCalculated = false;
        this.bufferedPageImageBounds = new Rectangle();
        this.paintCopyAreaLock = new Object();
        this.disposing = false;
        setFocusable(true);
        addFocusListener(this);
        addComponentListener(this);
        this.pageLoadingListener = new DefaultPageViewLoadingListener(this, this.documentViewController);
        this.documentViewModel = documentViewModel;
        this.parentScrollPane = parentScrollPane;
        this.currentRotation = documentViewModel.getViewRotation();
        this.currentZoom = documentViewModel.getViewRotation();
        this.pageTree = pageTree;
        this.pageIndex = pageNumber;
        this.clipBounds = new Rectangle();
        this.oldClipBounds = new Rectangle();
        this.bufferedPageImageReference = new SoftReference<>(null);
        if (width == 0 && height == 0) {
            calculatePageSize(this.pageSize);
            this.isPageSizeCalculated = true;
        } else {
            this.pageSize.setSize(width, height);
            this.defaultPageSize.setSize(width, height);
        }
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void addAnnotation(AnnotationComponent annotation) {
        if (this.annotationComponents == null) {
            this.annotationComponents = new ArrayList<>();
        }
        this.annotationComponents.add(annotation);
        if (annotation instanceof PopupAnnotationComponent) {
            add((AbstractAnnotationComponent) annotation, JLayeredPane.POPUP_LAYER);
        } else {
            add((AbstractAnnotationComponent) annotation, JLayeredPane.DEFAULT_LAYER);
        }
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void removeAnnotation(AnnotationComponent annotationComp) {
        this.annotationComponents.remove(annotationComp);
        remove((AbstractAnnotationComponent) annotationComp);
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void init() {
        if (this.inited) {
            return;
        }
        this.inited = true;
        this.dirtyTimerAction = new DirtyTimerAction();
        if (isDirtyTimer == null) {
            isDirtyTimer = new Timer(dirtyTimerInterval, this.dirtyTimerAction);
            isDirtyTimer.setInitialDelay(0);
            isDirtyTimer.start();
        } else {
            isDirtyTimer.addActionListener(this.dirtyTimerAction);
            isDirtyTimer.setInitialDelay(0);
        }
        this.isActionListenerRegistered = true;
        this.pageInitializer = new PageInitializer();
        this.pagePainter = new PagePainter();
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void invalidatePage() {
        Page page = getPage();
        page.getLibrary().disposeFontResources();
        page.resetInitializedState();
        this.currentZoom = -1.0f;
        this.pagePainter.setIsBufferDirty(true);
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void invalidatePageBuffer() {
        this.currentZoom = -1.0f;
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void dispose() {
        Image pageBufferImage;
        this.disposing = true;
        if (isDirtyTimer != null) {
            isDirtyTimer.removeActionListener(this.dirtyTimerAction);
            this.isActionListenerRegistered = false;
        }
        removeComponentListener(this);
        removeMouseMotionListener(this.currentToolHandler);
        removeMouseListener(this.currentToolHandler);
        removeFocusListener(this);
        removeComponentListener(this);
        removePageRepaintListener();
        if (this.bufferedPageImageReference != null && (pageBufferImage = this.bufferedPageImageReference.get()) != null) {
            pageBufferImage.flush();
        }
        if (this.annotationComponents != null) {
            int max = this.annotationComponents.size();
            for (int i2 = 0; i2 < max; i2++) {
                this.annotationComponents.get(i2).dispose();
            }
        }
        this.inited = false;
    }

    @Override // org.icepdf.ri.common.views.AbstractPageViewComponent
    public Page getPage() {
        Page page = this.pageTree.getPage(this.pageIndex);
        if (page != null) {
            page.addPaintPageListener(this);
            page.addPageProcessingListener(this.pageLoadingListener);
        }
        return page;
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void setDocumentViewCallback(DocumentView parentDocumentView) {
        this.parentDocumentView = parentDocumentView;
        this.documentViewController = this.parentDocumentView.getParentViewController();
        this.pageLoadingListener.setDocumentViewController(this.documentViewController);
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public int getPageIndex() {
        return this.pageIndex;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return this.pageSize.getSize();
    }

    @Override // java.awt.Container, java.awt.Component
    public void invalidate() {
        calculateRoughPageSize(this.pageSize);
        if (this.pagePainter != null) {
            this.pagePainter.setIsBufferDirty(true);
        }
        super.invalidate();
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics gg) {
        PageText pageText;
        if (!this.inited) {
            init();
        }
        if (!this.isActionListenerRegistered) {
            isDirtyTimer.addActionListener(this.dirtyTimerAction);
        }
        if (!this.isPageSizeCalculated) {
            calculatePageSize(this.pageSize);
            this.pagePainter.setIsBufferDirty(true);
        } else if (isPageStateDirty()) {
            if (this.pagePainter.isRunning()) {
                this.pagePainter.stopPaintingPage();
            }
            this.pagePainter.setIsBufferDirty(true);
            calculatePageSize(this.pageSize);
        }
        Graphics2D g2 = (Graphics2D) gg.create(0, 0, this.pageSize.width, this.pageSize.height);
        g2.setColor(pageColor);
        g2.fillRect(0, 0, this.pageSize.width, this.pageSize.height);
        if (enablePageLoadingProxy && isPageIntersectViewport() && !isDirtyTimer.isRunning()) {
            isDirtyTimer.addActionListener(this.dirtyTimerAction);
            this.isActionListenerRegistered = true;
        } else if (!enablePageLoadingProxy && isPageIntersectViewport() && (isPageStateDirty() || isBufferDirty())) {
            this.pageInitializer.run();
            this.pagePainter.run();
        }
        if (this.parentScrollPane == null) {
            this.oldClipBounds.setBounds(this.clipBounds);
            this.clipBounds.setBounds(g2.getClipBounds());
            if (this.oldClipBounds.width == 0 && this.oldClipBounds.height == 0) {
                this.oldClipBounds.setBounds(this.clipBounds);
            }
        }
        if (this.bufferedPageImageReference != null) {
            Image pageBufferImage = this.bufferedPageImageReference.get();
            if (pageBufferImage != null && !isPageStateDirty()) {
                g2.drawImage(pageBufferImage, this.bufferedPageImageBounds.f12372x, this.bufferedPageImageBounds.f12373y, this);
            } else if (!isDirtyTimer.isRunning()) {
                this.currentZoom = -1.0f;
                isDirtyTimer.addActionListener(this.dirtyTimerAction);
                this.isActionListenerRegistered = true;
            }
            paintAnnotations(g2);
            Page currentPage = getPage();
            DocumentSearchController searchController = this.documentViewController.getParentController().getDocumentSearchController();
            if (currentPage != null && currentPage.isInitiated() && ((searchController.isSearchHighlightRefreshNeeded(this.pageIndex, null) || this.documentViewModel.isViewToolModeSelected(5) || this.documentViewModel.isViewToolModeSelected(8)) && (pageText = currentPage.getViewText()) != null)) {
                if (searchController.isSearchHighlightRefreshNeeded(this.pageIndex, pageText)) {
                    searchController.searchHighlightPage(this.pageIndex);
                }
                if (this.documentViewModel.isSelectAll()) {
                    this.documentViewModel.addSelectedPageText(this);
                    pageText.selectAll();
                }
                TextSelectionPageHandler.paintSelectedText(g2, this, this.documentViewModel);
            }
            if (this.currentToolHandler != null) {
                this.currentToolHandler.paintTool(g2);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void paintAnnotations(Graphics g2) {
        Page currentPage = getPage();
        if (currentPage != null && currentPage.isInitiated() && this.annotationComponents != null) {
            Graphics2D gg2 = (Graphics2D) g2;
            AffineTransform prePaintTransform = gg2.getTransform();
            Color oldColor = gg2.getColor();
            Stroke oldStroke = gg2.getStroke();
            AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
            gg2.transform(at2);
            boolean notSelectTool = this.documentViewModel.getViewToolMode() != 6;
            int max = this.annotationComponents.size();
            for (int i2 = 0; i2 < max; i2++) {
                AnnotationComponent annotationComponent = this.annotationComponents.get(i2);
                if (((Component) annotationComponent).isVisible() && (!(annotationComponent.getAnnotation() instanceof FreeTextAnnotation) || !((FreeTextAnnotationComponent) annotationComponent).isActive())) {
                    annotationComponent.getAnnotation().render(gg2, 1, this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom(), annotationComponent.hasFocus() && notSelectTool);
                }
            }
            gg2.setColor(oldColor);
            gg2.setStroke(oldStroke);
            gg2.setTransform(prePaintTransform);
        }
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void clearSelectedText() {
        Page currentPage = getPage();
        if (currentPage.getViewText() != null) {
            currentPage.getViewText().clearSelected();
        }
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void setSelectionRectangle(Point cursorLocation, Rectangle selection) {
        if (this.currentToolHandler instanceof SelectionBoxHandler) {
            ((SelectionBoxHandler) this.currentToolHandler).setSelectionRectangle(cursorLocation, selection);
        }
    }

    @Override // org.icepdf.ri.common.views.PageViewComponent
    public void clearSelectionRectangle() {
        if (this.currentToolHandler instanceof SelectionBoxHandler) {
            ((SelectionBoxHandler) this.currentToolHandler).clearRectangle(this);
        }
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent e2) {
        int oldCurrentPage = this.documentViewModel.getViewCurrentPageIndex();
        this.documentViewModel.setViewCurrentPageIndex(this.pageIndex);
        this.documentViewController.firePropertyChange(PropertyConstants.DOCUMENT_CURRENT_PAGE, oldCurrentPage, this.pageIndex);
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent e2) {
    }

    @Override // java.awt.event.ComponentListener
    public void componentHidden(ComponentEvent e2) {
        if (isDirtyTimer != null && isDirtyTimer.isRunning()) {
            isDirtyTimer.removeActionListener(this.dirtyTimerAction);
            this.isActionListenerRegistered = false;
        }
    }

    @Override // java.awt.event.ComponentListener
    public void componentMoved(ComponentEvent e2) {
    }

    @Override // java.awt.event.ComponentListener
    public void componentResized(ComponentEvent e2) {
    }

    @Override // java.awt.event.ComponentListener
    public void componentShown(ComponentEvent e2) {
        if (isDirtyTimer != null && !isDirtyTimer.isRunning()) {
            isDirtyTimer.addActionListener(this.dirtyTimerAction);
            this.isActionListenerRegistered = true;
        }
    }

    private void calculateRoughPageSize(Rectangle pageSize) {
        float width = this.defaultPageSize.width;
        float height = this.defaultPageSize.height;
        float totalRotation = this.documentViewModel.getViewRotation();
        if (totalRotation != 0.0f && totalRotation != 180.0f) {
            if (totalRotation == 90.0f || totalRotation == 270.0f) {
                width = height;
                height = width;
            } else {
                AffineTransform at2 = new AffineTransform();
                double radians = Math.toRadians(totalRotation);
                at2.rotate(radians);
                Rectangle2D.Double boundingBox = new Rectangle2D.Double(0.0d, 0.0d, 0.0d, 0.0d);
                Point2D.Double src = new Point2D.Double();
                Point2D.Double dst = new Point2D.Double();
                src.setLocation(0.0d, height);
                at2.transform(src, dst);
                boundingBox.add(dst);
                src.setLocation(width, height);
                at2.transform(src, dst);
                boundingBox.add(dst);
                src.setLocation(0.0d, 0.0d);
                at2.transform(src, dst);
                boundingBox.add(dst);
                src.setLocation(width, 0.0d);
                at2.transform(src, dst);
                boundingBox.add(dst);
                width = (float) boundingBox.getWidth();
                height = (float) boundingBox.getHeight();
            }
        }
        pageSize.setSize((int) (width * this.documentViewModel.getViewZoom()), (int) (height * this.documentViewModel.getViewZoom()));
    }

    private void calculatePageSize(Rectangle pageSize) {
        if (this.pageTree != null) {
            Page currentPage = getPage();
            if (currentPage != null) {
                pageSize.setSize(currentPage.getSize(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom()).toDimension());
                this.defaultPageSize.setSize(currentPage.getSize(this.documentViewModel.getPageBoundary(), 0.0f, 1.0f).toDimension());
            }
            this.isPageSizeCalculated = true;
        }
    }

    private Rectangle calculatePageSize() {
        Page currentPage;
        if (this.pageTree != null && (currentPage = getPage()) != null) {
            return new Rectangle(currentPage.getSize(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom()).toDimension());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBufferDirty() {
        Rectangle tempClipBounds;
        if (this.disposing) {
            return false;
        }
        if (this.pageSize.height <= this.clipBounds.height && this.pageSize.width <= this.clipBounds.width) {
            return false;
        }
        if (this.parentScrollPane != null) {
            tempClipBounds = new Rectangle(this.parentScrollPane.getViewport().getViewRect());
        } else {
            tempClipBounds = new Rectangle(this.clipBounds);
        }
        Rectangle pageBounds = this.documentViewModel.getPageBounds(this.pageIndex);
        Rectangle normalizedBounds = new Rectangle(this.bufferedPageImageBounds);
        normalizedBounds.f12372x += pageBounds.f12372x;
        normalizedBounds.f12373y += pageBounds.f12373y;
        return !normalizedBounds.contains(pageBounds.intersection(tempClipBounds));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createBufferedPageImage(Page page, PagePainter pagePainter) {
        if (this.disposing) {
            return;
        }
        Graphics2D imageGraphics = null;
        synchronized (this.paintCopyAreaLock) {
            boolean isPageStateDirty = isPageStateDirty();
            this.currentRotation = this.documentViewModel.getViewRotation();
            this.currentZoom = this.documentViewModel.getViewZoom();
            if (this.parentScrollPane != null) {
                Rectangle rect = this.parentScrollPane.getViewport().getViewRect();
                if (this.clipBounds.width < rect.width || this.clipBounds.height < rect.height) {
                    isPageStateDirty = true;
                }
                this.clipBounds.setBounds(rect);
                if (this.oldClipBounds.width == 0 && this.oldClipBounds.height == 0) {
                    this.oldClipBounds.setBounds(this.clipBounds);
                }
            }
            Image pageBufferImage = this.bufferedPageImageReference.get();
            Rectangle oldBufferedPageImageBounds = new Rectangle(this.bufferedPageImageBounds);
            Rectangle pageBounds = this.documentViewModel.getPageBounds(this.pageIndex);
            this.bufferedPageImageBounds.setBounds(pageBounds.intersection(this.clipBounds));
            if (this.pageSize.width <= this.clipBounds.width) {
                this.bufferedPageImageBounds.f12372x = 0;
                this.bufferedPageImageBounds.width = this.pageSize.width;
            } else {
                if (horizontalScaleFactor > 1.0d) {
                    double width = (this.clipBounds.width * horizontalScaleFactor) - this.clipBounds.width;
                    this.bufferedPageImageBounds.f12372x = (int) (this.bufferedPageImageBounds.f12372x - width);
                    this.bufferedPageImageBounds.width = (int) (this.bufferedPageImageBounds.width + (width * 2.0d));
                    if (this.bufferedPageImageBounds.width > this.pageSize.width) {
                        this.bufferedPageImageBounds.width = this.pageSize.width;
                    }
                } else {
                    this.bufferedPageImageBounds.width = this.clipBounds.width;
                }
                this.bufferedPageImageBounds.f12372x -= pageBounds.f12372x;
            }
            if (this.pageSize.height <= this.clipBounds.height) {
                this.bufferedPageImageBounds.f12373y = 0;
                this.bufferedPageImageBounds.height = this.pageSize.height;
            } else {
                if (verticalScaleFactor > 1.0d) {
                    double height = (this.clipBounds.height * verticalScaleFactor) - this.clipBounds.height;
                    this.bufferedPageImageBounds.f12373y = (int) (this.bufferedPageImageBounds.f12373y - height);
                    this.bufferedPageImageBounds.height = (int) (this.bufferedPageImageBounds.height + (height * 2.0d));
                    if (this.bufferedPageImageBounds.height > this.pageSize.height) {
                        this.bufferedPageImageBounds.height = this.pageSize.height;
                    }
                } else {
                    this.bufferedPageImageBounds.height = this.clipBounds.height;
                }
                this.bufferedPageImageBounds.f12373y -= pageBounds.f12373y;
            }
            if (this.bufferedPageImageBounds.width > oldBufferedPageImageBounds.width || this.bufferedPageImageBounds.height > oldBufferedPageImageBounds.height) {
                isPageStateDirty = true;
            }
            if (this.bufferedPageImageBounds.f12372x < 0) {
                this.bufferedPageImageBounds.f12372x = 0;
            }
            if (this.bufferedPageImageBounds.f12372x + this.bufferedPageImageBounds.width > this.pageSize.width) {
                this.bufferedPageImageBounds.width = this.pageSize.width - this.bufferedPageImageBounds.f12372x;
            }
            if (this.bufferedPageImageBounds.f12373y < 0) {
                this.bufferedPageImageBounds.f12373y = 0;
            }
            if (this.bufferedPageImageBounds.f12373y + this.bufferedPageImageBounds.height > this.pageSize.height) {
                this.bufferedPageImageBounds.height = this.pageSize.height - this.bufferedPageImageBounds.f12373y;
            }
            if (this.bufferedPageImageBounds.width < 1 || this.bufferedPageImageBounds.height < 1) {
                this.bufferedPageImageBounds.setBounds(pageBounds);
            }
            if (isPageStateDirty || pageBufferImage == null) {
                if (gc == null) {
                    gc = getGraphicsConfiguration();
                }
                if (gc != null && isShowing()) {
                    if (pageBufferImage != null) {
                        pageBufferImage.flush();
                    }
                    pageBufferImage = gc.createCompatibleImage(this.bufferedPageImageBounds.width, this.bufferedPageImageBounds.height);
                    Graphics g2 = pageBufferImage.getGraphics();
                    g2.setColor(pageColor);
                    g2.fillRect(0, 0, this.pageSize.width, this.pageSize.height);
                }
                this.bufferedPageImageReference = new SoftReference<>(pageBufferImage);
                pagePainter.setIsBufferDirty(true);
            }
            if (pageBufferImage != null) {
                imageGraphics = (Graphics2D) pageBufferImage.getGraphics();
                imageGraphics.setClip(0, 0, this.bufferedPageImageBounds.width, this.bufferedPageImageBounds.height);
                int xTrans = 0 - this.bufferedPageImageBounds.f12372x;
                int yTrans = 0 - this.bufferedPageImageBounds.f12373y;
                Rectangle normalizedClipBounds = new Rectangle(this.clipBounds);
                normalizedClipBounds.f12372x -= pageBounds.f12372x;
                normalizedClipBounds.f12373y -= pageBounds.f12373y;
                if (!isPageStateDirty && !pagePainter.isLastPaintDirty() && pagePainter.isBufferDirty() && this.bufferedPageImageBounds.intersects(oldBufferedPageImageBounds)) {
                    Rectangle copyRect = this.bufferedPageImageBounds.intersection(oldBufferedPageImageBounds).intersection(normalizedClipBounds);
                    int xTransOld = 0 - oldBufferedPageImageBounds.f12372x;
                    int yTransOld = 0 - oldBufferedPageImageBounds.f12373y;
                    int dx = oldBufferedPageImageBounds.f12372x - this.bufferedPageImageBounds.f12372x;
                    int dy = oldBufferedPageImageBounds.f12373y - this.bufferedPageImageBounds.f12373y;
                    imageGraphics.copyArea(copyRect.f12372x + xTransOld, copyRect.f12373y + yTransOld, copyRect.width, copyRect.height, dx, dy);
                    Area copyArea = new Area(copyRect);
                    Area bufferArea = new Area(this.bufferedPageImageBounds);
                    bufferArea.subtract(copyArea);
                    imageGraphics.translate(xTrans, yTrans);
                    imageGraphics.setClip(bufferArea);
                    imageGraphics.translate(-xTrans, -yTrans);
                } else {
                    imageGraphics.translate(xTrans, yTrans);
                    imageGraphics.setClip(this.bufferedPageImageBounds);
                    imageGraphics.translate(-xTrans, -yTrans);
                }
                imageGraphics.translate(xTrans, yTrans);
                imageGraphics.setColor(pageColor);
                imageGraphics.fillRect(this.bufferedPageImageBounds.f12372x, this.bufferedPageImageBounds.f12373y, this.bufferedPageImageBounds.width, this.bufferedPageImageBounds.height);
            }
            this.oldClipBounds.setBounds(this.clipBounds);
        }
        if (imageGraphics != null) {
            if (page != null) {
                page.paint(imageGraphics, 1, this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom(), false, false);
                if (pagePainter.isStopPaintingRequested()) {
                    pagePainter.setIsLastPaintDirty(true);
                    pagePainter.setIsBufferDirty(true);
                } else {
                    pagePainter.setIsLastPaintDirty(false);
                    pagePainter.setIsBufferDirty(false);
                }
            }
            imageGraphics.dispose();
        }
    }

    private void removePageRepaintListener() {
        Page currentPage;
        if (this.inited && (currentPage = getPage()) != null) {
            currentPage.removePaintPageListener(this);
            currentPage.removePageProcessingListener(this.pageLoadingListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPageStateDirty() {
        return (this.currentZoom == this.documentViewModel.getViewZoom() && this.currentRotation == this.documentViewModel.getViewRotation() && this.oldClipBounds.width == this.clipBounds.width && this.oldClipBounds.height == this.clipBounds.height) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPageIntersectViewport() {
        Rectangle pageBounds = this.documentViewModel.getPageBounds(this.pageIndex);
        return pageBounds != null && isShowing() && pageBounds.intersects(this.parentScrollPane.getViewport().getViewRect());
    }

    @Override // org.icepdf.core.events.PaintPageListener
    public void paintPage(PaintPageEvent event) {
        Object source = event.getSource();
        Page page = getPage();
        if (page.equals(source)) {
            Runnable doSwingWork = new Runnable() { // from class: org.icepdf.ri.common.views.PageViewComponentImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!PageViewComponentImpl.this.disposing) {
                        PageViewComponentImpl.this.repaint();
                    }
                }
            };
            SwingUtilities.invokeLater(doSwingWork);
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/PageViewComponentImpl$PagePainter.class */
    public class PagePainter implements Runnable {
        private boolean isRunning;
        private boolean isLastPaintDirty;
        private boolean isBufferyDirty;
        private boolean isStopRequested;
        private Page page;
        private final Object isRunningLock = new Object();
        private boolean hasBeenQueued;

        public PagePainter() {
        }

        public synchronized boolean isLastPaintDirty() {
            boolean z2;
            synchronized (this.isRunningLock) {
                z2 = this.isLastPaintDirty;
            }
            return z2;
        }

        public void setIsLastPaintDirty(boolean isDirty) {
            synchronized (this.isRunningLock) {
                this.isLastPaintDirty = isDirty;
            }
        }

        public void setIsBufferDirty(boolean isDirty) {
            synchronized (this.isRunningLock) {
                this.isBufferyDirty = isDirty;
            }
        }

        public boolean isBufferDirty() {
            boolean z2;
            synchronized (this.isRunningLock) {
                z2 = this.isBufferyDirty;
            }
            return z2;
        }

        public boolean isStopPaintingRequested() {
            boolean z2;
            synchronized (this.isRunningLock) {
                z2 = this.isStopRequested;
            }
            return z2;
        }

        public synchronized void stopPaintingPage() {
            synchronized (this.isRunningLock) {
                PageViewComponentImpl.this.getPage().requestInterrupt();
                this.isStopRequested = true;
                this.isLastPaintDirty = true;
                this.isBufferyDirty = true;
            }
        }

        public void setPage(Page page) {
            this.page = page;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this.isRunningLock) {
                this.isRunning = true;
                this.hasBeenQueued = false;
            }
            try {
                boolean isPageStateDirty = PageViewComponentImpl.this.isPageStateDirty();
                PageViewComponentImpl.this.createBufferedPageImage(this.page, this);
                PageViewComponentImpl.this.refreshAnnotationComponents(this.page);
                if (isPageStateDirty && this.page != null && this.page.getAnnotations() != null) {
                    Runnable doSwingWork = new Runnable() { // from class: org.icepdf.ri.common.views.PageViewComponentImpl.PagePainter.1
                        @Override // java.lang.Runnable
                        public void run() {
                            PageViewComponentImpl.this.invalidate();
                            PageViewComponentImpl.this.validate();
                        }
                    };
                    SwingUtilities.invokeLater(doSwingWork);
                    Runnable doSwingWork2 = new Runnable() { // from class: org.icepdf.ri.common.views.PageViewComponentImpl.PagePainter.2
                        @Override // java.lang.Runnable
                        public void run() {
                            PageViewComponentImpl.this.repaint();
                        }
                    };
                    SwingUtilities.invokeLater(doSwingWork2);
                }
                this.page = null;
            } catch (Throwable e2) {
                PageViewComponentImpl.logger.log(Level.WARNING, "Error creating buffer, page: " + PageViewComponentImpl.this.pageIndex, e2);
                PageViewComponentImpl.this.currentZoom = -1.0f;
            }
            synchronized (this.isRunningLock) {
                this.isRunning = false;
            }
        }

        public void setStopRequested(boolean isStopRequested) {
            synchronized (this.isRunningLock) {
                this.isStopRequested = isStopRequested;
            }
        }

        public boolean hasBeenQueued() {
            boolean z2;
            synchronized (this.isRunningLock) {
                z2 = this.hasBeenQueued;
            }
            return z2;
        }

        public void setHasBeenQueued(boolean hasBeenQueued) {
            synchronized (this.isRunningLock) {
                this.hasBeenQueued = hasBeenQueued;
            }
        }

        public boolean isRunning() {
            boolean z2;
            synchronized (this.isRunningLock) {
                z2 = this.isRunning;
            }
            return z2;
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/PageViewComponentImpl$PageInitializer.class */
    private class PageInitializer implements Runnable {
        private boolean isRunning;
        private final Object isRunningLock;
        private boolean hasBeenQueued;
        private Page page;

        private PageInitializer() {
            this.isRunningLock = new Object();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPage(Page page) {
            this.page = page;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this.isRunningLock) {
                this.isRunning = true;
            }
            try {
                this.page = PageViewComponentImpl.this.getPage();
                this.page.init();
                SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.views.PageViewComponentImpl.PageInitializer.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PageViewComponentImpl.this.refreshAnnotationComponents(PageInitializer.this.page);
                        PageInitializer.this.page = null;
                    }
                });
                if (PageViewComponentImpl.this.documentViewController.getAnnotationCallback() != null) {
                    PageViewComponentImpl.this.documentViewController.getAnnotationCallback().pageAnnotationsInitialized(this.page);
                }
                synchronized (this.isRunningLock) {
                    PageViewComponentImpl.this.pageInitializer.setHasBeenQueued(false);
                    this.isRunning = false;
                }
            } catch (Throwable e2) {
                PageViewComponentImpl.logger.log(Level.WARNING, "Error initiating page: " + PageViewComponentImpl.this.pageIndex, e2);
                this.hasBeenQueued = true;
            }
        }

        public boolean hasBeenQueued() {
            boolean z2;
            synchronized (this.isRunningLock) {
                z2 = this.hasBeenQueued;
            }
            return z2;
        }

        public void setHasBeenQueued(boolean hasBeenQueued) {
            synchronized (this.isRunningLock) {
                this.hasBeenQueued = hasBeenQueued;
            }
        }

        public boolean isRunning() {
            boolean z2;
            synchronized (this.isRunningLock) {
                z2 = this.isRunning;
            }
            return z2;
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/PageViewComponentImpl$DirtyTimerAction.class */
    private class DirtyTimerAction implements ActionListener {
        private Page page;

        private DirtyTimerAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent e2) {
            boolean interestsView = PageViewComponentImpl.this.isPageIntersectViewport();
            if (PageViewComponentImpl.this.disposing || !interestsView) {
                PageViewComponentImpl.isDirtyTimer.removeActionListener(PageViewComponentImpl.this.dirtyTimerAction);
                PageViewComponentImpl.this.isActionListenerRegistered = false;
                this.page = null;
                if (PageViewComponentImpl.this.pagePainter.isRunning()) {
                    PageViewComponentImpl.this.pagePainter.stopPaintingPage();
                    PageViewComponentImpl.this.pagePainter.setIsLastPaintDirty(true);
                    return;
                }
                return;
            }
            boolean isBufferDirty = PageViewComponentImpl.this.pagePainter.isBufferDirty() || PageViewComponentImpl.this.isBufferDirty();
            if (isBufferDirty || PageViewComponentImpl.this.pagePainter.isStopPaintingRequested()) {
                int diff = Math.abs(PageViewComponentImpl.this.previousScrollValue - PageViewComponentImpl.this.parentScrollPane.getVerticalScrollBar().getValue());
                PageViewComponentImpl.this.previousScrollValue = PageViewComponentImpl.this.parentScrollPane.getVerticalScrollBar().getValue();
                if (PageViewComponentImpl.this.parentScrollPane != null && diff > PageViewComponentImpl.scrollInitThreshold) {
                    return;
                }
                this.page = PageViewComponentImpl.this.getPage();
                PageViewComponentImpl.this.pagePainter.setStopRequested(false);
                if (isBufferDirty && this.page != null && !this.page.isInitiated() && !PageViewComponentImpl.this.pageInitializer.isRunning() && !PageViewComponentImpl.this.pageInitializer.hasBeenQueued()) {
                    PageViewComponentImpl.this.pageInitializer.setHasBeenQueued(true);
                    PageViewComponentImpl.this.pageInitializer.setPage(this.page);
                    Library.execute(PageViewComponentImpl.this.pageInitializer);
                }
                boolean tmp = (PageViewComponentImpl.this.pageInitializer.isRunning() || this.page == null || !this.page.isInitiated() || PageViewComponentImpl.this.pagePainter.isRunning() || PageViewComponentImpl.this.pagePainter.hasBeenQueued()) ? false : true;
                Rectangle rect = PageViewComponentImpl.this.parentScrollPane.getViewport().getViewRect();
                if (rect.width != PageViewComponentImpl.this.clipBounds.width || rect.height != PageViewComponentImpl.this.clipBounds.height) {
                    isBufferDirty = true;
                }
                if (this.page != null && tmp && isBufferDirty) {
                    PageViewComponentImpl.this.pagePainter.setPage(this.page);
                    PageViewComponentImpl.this.pagePainter.setHasBeenQueued(true);
                    PageViewComponentImpl.this.pagePainter.setIsBufferDirty(isBufferDirty);
                    Library.executePainter(PageViewComponentImpl.this.pagePainter);
                }
            }
            if (!PageViewComponentImpl.this.pagePainter.hasBeenQueued() && !PageViewComponentImpl.this.pagePainter.isRunning() && PageViewComponentImpl.this.bufferedPageImageReference != null && PageViewComponentImpl.this.bufferedPageImageReference.get() == null) {
                PageViewComponentImpl.this.pagePainter.setIsBufferDirty(true);
            }
        }
    }
}
