package org.icepdf.ri.common.views;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import org.icepdf.core.SecurityCallback;
import org.icepdf.core.pobjects.Destination;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.search.DocumentSearchController;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.PropertyConstants;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.PopupAnnotationComponent;
import org.icepdf.ri.images.Images;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/DocumentViewControllerImpl.class */
public class DocumentViewControllerImpl implements DocumentViewController, ComponentListener {
    private static final Logger logger = Logger.getLogger(DocumentViewControllerImpl.class.toString());
    public static final int ONE_PAGE_VIEW = 1;
    public static final int ONE_COLUMN_VIEW = 2;
    public static final int TWO_PAGE_LEFT_VIEW = 3;
    public static final int TWO_COLUMN_LEFT_VIEW = 4;
    public static final int TWO_PAGE_RIGHT_VIEW = 5;
    public static final int TWO_COLUMN_RIGHT_VIEW = 6;
    public static final int USE_ATTACHMENTS_VIEW = 7;
    public static final float ZOOM_FACTOR = 1.2f;
    public static final float ROTATION_FACTOR = 90.0f;
    public static Color backgroundColor;
    private float[] zoomLevels;
    private Document document;
    private DocumentViewModelImpl documentViewModel;
    private AbstractDocumentView documentView;
    protected int viewportWidth;
    protected int oldViewportWidth;
    protected int viewportHeight;
    protected int oldViewportHeight;
    protected int viewType;
    protected int oldViewType;
    protected int viewportFitMode;
    protected int oldViewportFitMode;
    protected int cursorType;
    protected SwingController viewerController;
    protected AnnotationCallback annotationCallback;
    protected SecurityCallback securityCallback;
    protected PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private JScrollPane documentViewScrollPane = new JScrollPane();

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.background.color", "#808080");
            int colorValue = ColorUtil.convertColor(color);
            backgroundColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("808080", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading page shadow colour");
            }
        }
    }

    public DocumentViewControllerImpl(final SwingController viewerController) {
        this.viewerController = viewerController;
        this.documentViewScrollPane.getViewport().setBackground(backgroundColor);
        this.documentViewScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        this.documentViewScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        Action deleteAnnotation = new AbstractAction() { // from class: org.icepdf.ri.common.views.DocumentViewControllerImpl.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent e2) {
                if (DocumentViewControllerImpl.this.documentViewModel != null) {
                    DocumentViewControllerImpl.this.deleteCurrentAnnotation();
                    viewerController.reflectUndoCommands();
                }
            }
        };
        InputMap inputMap = this.documentViewScrollPane.getInputMap(2);
        inputMap.put(KeyStroke.getKeyStroke("DELETE"), "removeSelecteAnnotation");
        this.documentViewScrollPane.getActionMap().put("removeSelecteAnnotation", deleteAnnotation);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public Document getDocument() {
        return this.document;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void setDocument(Document newDocument) {
        if (this.document != null) {
            this.document.dispose();
            this.document = null;
        }
        this.document = newDocument;
        if (this.documentViewModel != null) {
            this.documentViewModel.dispose();
            this.documentViewModel = null;
        }
        this.documentViewModel = new DocumentViewModelImpl(this.document, this.documentViewScrollPane);
        setViewType();
        this.documentViewScrollPane.addComponentListener(this);
        this.documentViewScrollPane.validate();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void closeDocument() throws HeadlessException {
        this.documentViewScrollPane.removeComponentListener(this);
        if (this.documentView != null) {
            this.documentViewScrollPane.remove(this.documentView);
            this.documentView.dispose();
            this.documentView = null;
        }
        if (this.documentViewModel != null) {
            this.documentViewModel.dispose();
            this.documentViewModel = null;
        }
        setCurrentPageIndex(0);
        setZoom(1.0f);
        setRotation(0.0f);
        setViewCursor(8);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public Adjustable getHorizontalScrollBar() {
        return this.documentViewScrollPane.getHorizontalScrollBar();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public Adjustable getVerticalScrollBar() {
        return this.documentViewScrollPane.getVerticalScrollBar();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public JViewport getViewPort() {
        return this.documentViewScrollPane.getViewport();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void setAnnotationCallback(AnnotationCallback annotationCallback) {
        this.annotationCallback = annotationCallback;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void setSecurityCallback(SecurityCallback securityCallback) {
        this.securityCallback = securityCallback;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void clearSelectedAnnotations() {
        if (this.documentViewModel.getCurrentAnnotation() != null) {
            this.documentViewModel.getCurrentAnnotation().setSelected(false);
            firePropertyChange(PropertyConstants.ANNOTATION_DESELECTED, this.documentViewModel.getCurrentAnnotation(), (Object) null);
            this.documentViewModel.setCurrentAnnotation(null);
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void assignSelectedAnnotation(AnnotationComponent annotationComponent) {
        firePropertyChange(PropertyConstants.ANNOTATION_SELECTED, this.documentViewModel.getCurrentAnnotation(), annotationComponent);
        this.documentViewModel.setCurrentAnnotation(annotationComponent);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void clearSelectedText() {
        ArrayList<WeakReference<AbstractPageViewComponent>> selectedPages = this.documentViewModel.getSelectedPageText();
        this.documentViewModel.setSelectAll(false);
        if (selectedPages != null && selectedPages.size() > 0) {
            Iterator i$ = selectedPages.iterator();
            while (i$.hasNext()) {
                WeakReference<AbstractPageViewComponent> page = i$.next();
                PageViewComponent pageComp = page.get();
                if (pageComp != null) {
                    pageComp.clearSelectedText();
                }
            }
            selectedPages.clear();
            this.documentView.repaint();
        }
        firePropertyChange(PropertyConstants.TEXT_DESELECTED, (Object) null, (Object) null);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void clearHighlightedText() {
        DocumentSearchController searchController = this.viewerController.getDocumentSearchController();
        searchController.clearAllSearchHighlight();
        this.documentView.repaint();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void selectAllText() {
        this.documentViewModel.setSelectAll(true);
        this.documentView.repaint();
        firePropertyChange(PropertyConstants.TEXT_SELECT_ALL, (Object) null, (Object) null);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public String getSelectedText() {
        StringBuilder selectedText = new StringBuilder();
        try {
            if (!this.documentViewModel.isSelectAll()) {
                ArrayList<WeakReference<AbstractPageViewComponent>> selectedPages = this.documentViewModel.getSelectedPageText();
                if (selectedPages != null && selectedPages.size() > 0) {
                    Iterator i$ = selectedPages.iterator();
                    while (i$.hasNext()) {
                        WeakReference<AbstractPageViewComponent> page = i$.next();
                        AbstractPageViewComponent pageComp = page.get();
                        if (pageComp != null) {
                            int pageIndex = pageComp.getPageIndex();
                            selectedText.append((CharSequence) this.document.getPageText(pageIndex).getSelected());
                        }
                    }
                }
            } else {
                Document document = this.documentViewModel.getDocument();
                for (int i2 = 0; i2 < document.getNumberOfPages(); i2++) {
                    selectedText.append((Object) this.viewerController.getDocument().getPageText(i2));
                }
            }
        } catch (InterruptedException e2) {
            logger.log(Level.SEVERE, "Page text extraction thread interrupted.", (Throwable) e2);
        }
        return selectedText.toString();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public AnnotationCallback getAnnotationCallback() {
        return this.annotationCallback;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public SecurityCallback getSecurityCallback() {
        return this.securityCallback;
    }

    public DocumentView getDocumentView() {
        return this.documentView;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public synchronized void setViewKeyListener(KeyListener l2) {
        if (this.documentView != null) {
            this.documentView.addKeyListener(l2);
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void setDestinationTarget(Destination destination) {
        int pageNumber;
        if (this.documentView == null || this.documentViewModel == null || destination == null || destination.getPageReference() == null || (pageNumber = getPageTree().getPageNumber(destination.getPageReference())) < 0) {
            return;
        }
        JViewport documentViewport = this.documentViewScrollPane != null ? this.documentViewScrollPane.getViewport() : null;
        if (documentViewport != null) {
            Rectangle pageBounds = this.documentViewModel.getPageBounds(pageNumber);
            if (this.documentViewModel.getViewRotation() == 0.0f && pageBounds != null) {
                setCurrentPageIndex(pageNumber);
                if (destination.getZoom() != null && destination.getZoom().floatValue() > 0.0f) {
                    setZoomCentered(destination.getZoom().floatValue(), null, false);
                }
                Point newViewPosition = new Point(pageBounds.getLocation());
                float zoom = getZoom();
                Rectangle viewportBounds = this.documentView.getBounds();
                Rectangle viewportRect = documentViewport.getViewRect();
                if (destination.getTop() != null && destination.getTop().floatValue() != 0.0f) {
                    newViewPosition.f12371y = (pageBounds.f12373y + pageBounds.height) - ((int) (destination.getTop().floatValue() * zoom));
                }
                if (newViewPosition.f12371y + viewportRect.height > viewportBounds.height) {
                    newViewPosition.f12371y = viewportBounds.height - viewportRect.height;
                }
                if (destination.getLeft() != null && destination.getLeft().floatValue() != 0.0f) {
                    newViewPosition.f12370x = pageBounds.f12372x + ((int) (destination.getLeft().floatValue() * zoom));
                }
                if (newViewPosition.f12370x + viewportRect.width > viewportBounds.width) {
                    newViewPosition.f12370x = viewportBounds.width - viewportRect.width;
                }
                if (newViewPosition.f12370x < 0) {
                    newViewPosition.f12370x = 0;
                }
                if (newViewPosition.f12371y < 0) {
                    newViewPosition.f12371y = 0;
                }
                documentViewport.setViewPosition(newViewPosition);
                int oldPageIndex = this.documentViewModel.getViewCurrentPageIndex();
                this.documentViewModel.setViewCurrentPageIndex(pageNumber);
                firePropertyChange(PropertyConstants.DOCUMENT_CURRENT_PAGE, oldPageIndex, pageNumber);
            } else {
                setCurrentPageIndex(pageNumber);
            }
            this.viewerController.updateDocumentView();
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void dispose() {
        if (this.documentView != null) {
            this.documentView.dispose();
            this.documentView = null;
        }
        if (this.documentViewModel != null) {
            this.documentViewModel.dispose();
            this.documentViewModel = null;
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public Container getViewContainer() {
        return this.documentViewScrollPane;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public Controller getParentController() {
        return this.viewerController;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public int getViewMode() {
        return this.viewType;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void setViewType(int documentViewType) {
        this.oldViewType = this.viewType;
        this.viewType = documentViewType;
        if (this.documentView != null) {
            this.documentView.uninstallCurrentTool();
        }
        setViewType();
    }

    public void revertViewType() {
        this.viewType = this.oldViewType;
        setViewType(this.viewType);
    }

    private void setViewType() {
        if (this.documentView != null) {
            this.documentViewScrollPane.remove(this.documentView);
            this.documentViewScrollPane.validate();
            this.documentView.dispose();
        }
        if (this.documentViewModel == null) {
            return;
        }
        if (this.viewType == 2) {
            this.documentView = new OneColumnPageView(this, this.documentViewScrollPane, this.documentViewModel);
        } else if (this.viewType == 1) {
            this.documentView = new OnePageView(this, this.documentViewScrollPane, this.documentViewModel);
        } else if (this.viewType == 4) {
            this.documentView = new TwoColumnPageView(this, this.documentViewScrollPane, this.documentViewModel, 0);
        } else if (this.viewType == 3) {
            this.documentView = new TwoPageView(this, this.documentViewScrollPane, this.documentViewModel, 0);
        } else if (this.viewType == 6) {
            this.documentView = new TwoColumnPageView(this, this.documentViewScrollPane, this.documentViewModel, 1);
        } else if (this.viewType == 5) {
            this.documentView = new TwoPageView(this, this.documentViewScrollPane, this.documentViewModel, 1);
        } else if (this.viewType == 7) {
            this.documentView = new CollectionDocumentView(this, this.documentViewScrollPane, this.documentViewModel);
        } else {
            this.documentView = new OneColumnPageView(this, this.documentViewScrollPane, this.documentViewModel);
        }
        this.documentView.setToolMode(this.documentViewModel.getViewToolMode());
        this.documentViewScrollPane.setViewportView(this.documentView);
        this.documentViewScrollPane.validate();
        this.viewerController.setPageFitMode(this.viewportFitMode, true);
        setCurrentPageIndex(this.documentViewModel.getViewCurrentPageIndex());
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setFitMode(int fitMode) {
        if (this.documentViewModel == null || this.viewType == 7) {
            return false;
        }
        boolean changed = fitMode != this.viewportFitMode;
        this.viewportFitMode = fitMode;
        if (this.document != null) {
            float newZoom = this.documentViewModel.getViewZoom();
            if (this.viewportFitMode == 2) {
                newZoom = 1.0f;
            } else if (this.viewportFitMode == 3) {
                if (this.documentView != null && this.documentViewScrollPane != null) {
                    float viewportHeight = this.documentViewScrollPane.getViewport().getViewRect().height;
                    float pageViewHeight = this.documentView.getDocumentSize().height;
                    newZoom = viewportHeight > 0.0f ? viewportHeight / (pageViewHeight + (AbstractDocumentView.layoutInserts * 2)) : 1.0f;
                }
            } else if (this.viewportFitMode == 4 && this.documentView != null && this.documentViewScrollPane != null) {
                float viewportWidth = this.documentViewScrollPane.getViewport().getViewRect().width;
                float pageViewWidth = this.documentView.getDocumentSize().width;
                if (!this.documentViewScrollPane.getVerticalScrollBar().isVisible()) {
                    viewportWidth -= this.documentViewScrollPane.getVerticalScrollBar().getWidth();
                }
                newZoom = viewportWidth > 0.0f ? viewportWidth / (pageViewWidth + (AbstractDocumentView.layoutInserts * 2)) : 1.0f;
            }
            if (getVerticalScrollBar().getValue() == 0) {
                setZoomCentered(newZoom, new Point(0, 0), true);
            } else {
                setZoomCentered(newZoom, null, true);
            }
        }
        return changed;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public int getFitMode() {
        return this.viewportFitMode;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void setDocumentViewType(int documentView, int fitMode) {
        setViewType(documentView);
        setFitMode(fitMode);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setCurrentPageIndex(int pageIndex) {
        if (this.documentViewModel == null) {
            return false;
        }
        if (pageIndex < 0) {
            pageIndex = 0;
        } else if (pageIndex > this.document.getNumberOfPages() - 1) {
            pageIndex = this.document.getNumberOfPages() - 1;
        }
        int oldPageIndex = this.documentViewModel.getViewCurrentPageIndex();
        boolean changed = this.documentViewModel.setViewCurrentPageIndex(pageIndex);
        if (this.documentView != null) {
            this.documentView.updateDocumentView();
        }
        Rectangle perferedPageOffset = this.documentViewModel.getPageBounds(getCurrentPageIndex());
        if (perferedPageOffset != null) {
            Rectangle currentViewSize = this.documentView.getBounds();
            if (perferedPageOffset.f12372x + perferedPageOffset.width > currentViewSize.width) {
                perferedPageOffset.f12372x = currentViewSize.width - perferedPageOffset.width;
            }
            if (perferedPageOffset.f12373y + perferedPageOffset.height > currentViewSize.height) {
                perferedPageOffset.f12373y = currentViewSize.height - perferedPageOffset.height;
            }
            this.documentViewScrollPane.getViewport().setViewPosition(perferedPageOffset.getLocation());
            this.documentViewScrollPane.revalidate();
        }
        firePropertyChange(PropertyConstants.DOCUMENT_CURRENT_PAGE, oldPageIndex, pageIndex);
        return changed;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public int setCurrentPageNext() {
        int increment = 0;
        if (this.documentViewModel != null) {
            increment = this.documentView.getNextPageIncrement();
            int current = this.documentViewModel.getViewCurrentPageIndex();
            if (current + increment < this.document.getNumberOfPages()) {
                this.documentViewModel.setViewCurrentPageIndex(current + increment);
            } else {
                this.documentViewModel.setViewCurrentPageIndex(this.document.getNumberOfPages() - 1);
            }
        }
        return increment;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public int setCurrentPagePrevious() {
        int decrement = 0;
        if (this.documentViewModel != null) {
            decrement = this.documentView.getPreviousPageIncrement();
            int current = this.documentViewModel.getViewCurrentPageIndex();
            if (current - decrement >= 0) {
                this.documentViewModel.setViewCurrentPageIndex(current - decrement);
            } else {
                this.documentViewModel.setViewCurrentPageIndex(0);
            }
        }
        return decrement;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public int getCurrentPageIndex() {
        if (this.documentViewModel == null) {
            return -1;
        }
        return this.documentViewModel.getViewCurrentPageIndex();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public int getCurrentPageDisplayValue() {
        if (this.documentViewModel == null) {
            return -1;
        }
        return this.documentViewModel.getViewCurrentPageIndex() + 1;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public float[] getZoomLevels() {
        return this.zoomLevels;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void setZoomLevels(float[] zoomLevels) {
        this.zoomLevels = zoomLevels;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setZoom(float viewZoom) {
        return setZoomCentered(viewZoom, null, false);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setZoomIn() {
        return setZoomIn(null);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setZoomOut() {
        return setZoomOut(null);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public float getZoom() {
        if (this.documentViewModel != null) {
            return this.documentViewModel.getViewZoom();
        }
        return 0.0f;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public float getRotation() {
        if (this.documentViewModel == null) {
            return -1.0f;
        }
        return this.documentViewModel.getViewRotation();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public float setRotateRight() {
        if (this.documentViewModel == null) {
            return -1.0f;
        }
        float viewRotation = this.documentViewModel.getViewRotation() - 90.0f;
        if (viewRotation < 0.0f) {
            viewRotation += 360.0f;
        }
        this.documentViewModel.setViewRotation(viewRotation);
        this.documentViewScrollPane.revalidate();
        return viewRotation;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public float setRotateLeft() {
        if (this.documentViewModel == null) {
            return -1.0f;
        }
        float viewRotation = (this.documentViewModel.getViewRotation() + 90.0f) % 360.0f;
        this.documentViewModel.setViewRotation(viewRotation);
        this.documentViewScrollPane.revalidate();
        return viewRotation;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setRotation(float viewRotation) {
        if (this.documentViewModel == null) {
            return false;
        }
        boolean changed = this.documentViewModel.setViewRotation(viewRotation);
        this.documentViewModel.setViewRotation(viewRotation);
        this.documentViewScrollPane.revalidate();
        return changed;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setToolMode(int viewToolMode) {
        if (this.documentViewModel != null) {
            boolean changed = this.documentViewModel.setViewToolMode(viewToolMode);
            if (changed) {
                if (this.documentView != null) {
                    this.documentView.setToolMode(viewToolMode);
                }
                List<AbstractPageViewComponent> pageComponents = this.documentViewModel.getPageComponents();
                for (AbstractPageViewComponent page : pageComponents) {
                    page.setToolMode(viewToolMode);
                }
            }
            return changed;
        }
        return false;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean isToolModeSelected(int viewToolMode) {
        return getToolMode() == viewToolMode;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public int getToolMode() {
        if (this.documentViewModel == null) {
            return 50;
        }
        return this.documentViewModel.getViewToolMode();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void setViewCursor(int cursorType) throws HeadlessException {
        this.cursorType = cursorType;
        Cursor cursor = getViewCursor(cursorType);
        if (this.documentViewScrollPane != null && this.documentViewScrollPane.getViewport() != null) {
            this.documentViewScrollPane.getViewport().setCursor(cursor);
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public int getViewCursor() {
        return this.cursorType;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public Cursor getViewCursor(int currsorType) throws HeadlessException {
        String imageName;
        Cursor c2;
        if (currsorType == 8) {
            return Cursor.getPredefinedCursor(0);
        }
        if (currsorType == 6) {
            return Cursor.getPredefinedCursor(3);
        }
        if (currsorType == 7) {
            return Cursor.getPredefinedCursor(0);
        }
        if (currsorType == 1) {
            imageName = "hand_open.gif";
        } else if (currsorType == 2) {
            imageName = "hand_closed.gif";
        } else if (currsorType == 3) {
            imageName = "zoom_in.gif";
        } else if (currsorType == 4) {
            imageName = "zoom_out.gif";
        } else if (currsorType == 12) {
            imageName = "zoom.gif";
        } else {
            if (currsorType == 9) {
                return Cursor.getPredefinedCursor(12);
            }
            if (currsorType == 10) {
                return Cursor.getPredefinedCursor(2);
            }
            if (currsorType == 11) {
                return Cursor.getPredefinedCursor(1);
            }
            return Cursor.getPredefinedCursor(0);
        }
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension bestsize = tk.getBestCursorSize(24, 24);
        if (bestsize.width != 0) {
            Point cursorHotSpot = new Point(12, 12);
            try {
                ImageIcon cursorImage = new ImageIcon(Images.get(imageName));
                c2 = tk.createCustomCursor(cursorImage.getImage(), cursorHotSpot, imageName);
            } catch (RuntimeException ex) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "Trying to load image: " + imageName, (Throwable) ex);
                }
                throw ex;
            }
        } else {
            c2 = Cursor.getDefaultCursor();
            logger.warning("System does not support custom cursors");
        }
        return c2;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void requestViewFocusInWindow() {
        if (this.documentViewScrollPane != null) {
            this.documentViewScrollPane.requestFocus();
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setZoomIn(Point p2) {
        float zoom = getZoom() * 1.2f;
        return setZoomCentered(zoom, p2, false);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setZoomOut(Point p2) {
        float zoom = getZoom() / 1.2f;
        return setZoomCentered(zoom, p2, false);
    }

    private void zoomCenter(Point centeringPoint) {
        if (centeringPoint == null) {
            centeringPoint = getCenteringPoint();
        }
        if (centeringPoint == null || this.documentViewScrollPane == null) {
            return;
        }
        int viewPortWidth = this.documentViewScrollPane.getViewport().getWidth();
        int viewPortHeight = this.documentViewScrollPane.getViewport().getHeight();
        int scrollPaneX = this.documentViewScrollPane.getViewport().getViewPosition().f12370x;
        int scrollPaneY = this.documentViewScrollPane.getViewport().getViewPosition().f12371y;
        Dimension pageViewSize = this.documentView.getPreferredSize();
        int pageViewWidth = pageViewSize.width;
        int pageViewHeight = pageViewSize.height;
        centeringPoint.setLocation(centeringPoint.f12370x - (viewPortWidth / 2), centeringPoint.f12371y - (viewPortHeight / 2));
        if (pageViewWidth < viewPortWidth || pageViewHeight < viewPortHeight) {
            if (centeringPoint.f12370x >= pageViewWidth - viewPortWidth || centeringPoint.f12370x < 0) {
                centeringPoint.f12370x = scrollPaneX;
            }
            if (centeringPoint.f12371y >= pageViewHeight - viewPortHeight || centeringPoint.f12371y < 0) {
                centeringPoint.f12371y = scrollPaneY;
            }
        } else {
            if (centeringPoint.f12370x + viewPortWidth > pageViewWidth) {
                centeringPoint.f12370x = pageViewWidth - viewPortWidth;
            } else if (centeringPoint.f12370x < 0) {
                centeringPoint.f12370x = 0;
            }
            if (centeringPoint.f12371y + viewPortHeight > pageViewHeight) {
                centeringPoint.f12371y = pageViewHeight - viewPortHeight;
            } else if (centeringPoint.f12371y < 0) {
                centeringPoint.f12371y = 0;
            }
        }
        this.documentViewScrollPane.getViewport().setViewPosition(centeringPoint);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setZoomCentered(float zoom, Point centeringPoint, boolean becauseOfValidFitMode) {
        if (this.documentViewModel == null) {
            return false;
        }
        if (this.zoomLevels != null) {
            if (zoom < this.zoomLevels[0]) {
                zoom = this.zoomLevels[0];
            } else if (zoom > this.zoomLevels[this.zoomLevels.length - 1]) {
                zoom = this.zoomLevels[this.zoomLevels.length - 1];
            }
        }
        if (centeringPoint == null) {
            centeringPoint = getCenteringPoint();
        }
        float previousZoom = getZoom();
        boolean changed = this.documentViewModel.setViewZoom(zoom);
        this.documentViewScrollPane.validate();
        if (changed && centeringPoint != null) {
            centeringPoint.setLocation((centeringPoint.f12370x / previousZoom) * zoom, (centeringPoint.f12371y / previousZoom) * zoom);
        }
        zoomCenter(centeringPoint);
        if (this.viewerController != null) {
            this.viewerController.doCommonZoomUIUpdates(becauseOfValidFitMode);
        }
        return changed;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public boolean setZoomToViewPort(float zoom, Point zoomPointDelta, int pageIndex, boolean becauseOfValidFitMode) {
        if (this.documentViewModel == null) {
            return false;
        }
        if (this.zoomLevels != null) {
            if (zoom < this.zoomLevels[0]) {
                zoom = this.zoomLevels[0];
            } else if (zoom > this.zoomLevels[this.zoomLevels.length - 1]) {
                zoom = this.zoomLevels[this.zoomLevels.length - 1];
            }
        }
        if (zoomPointDelta == null) {
            zoomPointDelta = new Point();
        }
        float previousZoom = getZoom();
        boolean changed = this.documentViewModel.setViewZoom(zoom);
        this.documentViewScrollPane.validate();
        if (changed) {
            Rectangle bounds = this.documentViewModel.getPageBounds(pageIndex);
            zoomPointDelta.setLocation((zoomPointDelta.f12370x / previousZoom) * zoom, (zoomPointDelta.f12371y / previousZoom) * zoom);
            zoomPointDelta.setLocation(bounds.f12372x + zoomPointDelta.f12370x, bounds.f12373y + zoomPointDelta.f12371y);
            getViewPort().setViewPosition(zoomPointDelta);
        }
        if (this.viewerController != null) {
            this.viewerController.doCommonZoomUIUpdates(becauseOfValidFitMode);
        }
        return changed;
    }

    private Point getCenteringPoint() {
        Point centeringPoint = null;
        if (this.documentViewScrollPane != null) {
            int x2 = this.documentViewScrollPane.getViewport().getViewPosition().f12370x + (this.documentViewScrollPane.getViewport().getWidth() / 2);
            int y2 = this.documentViewScrollPane.getViewport().getViewPosition().f12371y + (this.documentViewScrollPane.getViewport().getHeight() / 2);
            centeringPoint = new Point(x2, y2);
        }
        return centeringPoint;
    }

    private PageTree getPageTree() {
        if (this.document == null) {
            return null;
        }
        return this.document.getPageTree();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public DocumentViewModel getDocumentViewModel() {
        return this.documentViewModel;
    }

    @Override // java.awt.event.ComponentListener
    public void componentHidden(ComponentEvent e2) {
    }

    @Override // java.awt.event.ComponentListener
    public void componentMoved(ComponentEvent e2) {
    }

    @Override // java.awt.event.ComponentListener
    public void componentResized(ComponentEvent e2) {
        Object src = e2.getSource();
        if (src != null && src == this.documentViewScrollPane) {
            setFitMode(getFitMode());
        }
    }

    @Override // java.awt.event.ComponentListener
    public void componentShown(ComponentEvent e2) {
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void firePropertyChange(String event, int oldValue, int newValue) {
        this.changes.firePropertyChange(event, oldValue, newValue);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void firePropertyChange(String event, Object oldValue, Object newValue) {
        this.changes.firePropertyChange(event, oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener l2) {
        this.changes.addPropertyChangeListener(l2);
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void deleteCurrentAnnotation() {
        AbstractAnnotationComponent annotationComponent = (AbstractAnnotationComponent) this.documentViewModel.getCurrentAnnotation();
        if (!(annotationComponent instanceof PopupAnnotationComponent)) {
            deleteAnnotation(annotationComponent);
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void deleteAnnotation(AnnotationComponent annotationComponent) {
        if (this.documentViewModel != null && annotationComponent != null) {
            PageViewComponent pageComponent = annotationComponent.getPageViewComponent();
            if (this.annotationCallback != null) {
                this.annotationCallback.removeAnnotation(pageComponent, annotationComponent);
            }
            firePropertyChange(PropertyConstants.ANNOTATION_DELETED, this.documentViewModel.getCurrentAnnotation(), (Object) null);
            assignSelectedAnnotation(null);
            this.documentView.repaint();
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void undo() {
        this.documentViewModel.getAnnotationCareTaker().undo();
        this.documentView.repaint();
    }

    @Override // org.icepdf.ri.common.views.DocumentViewController
    public void redo() {
        this.documentViewModel.getAnnotationCareTaker().redo();
        this.documentView.repaint();
    }

    public void removePropertyChangeListener(PropertyChangeListener l2) {
        this.changes.removePropertyChangeListener(l2);
    }
}
