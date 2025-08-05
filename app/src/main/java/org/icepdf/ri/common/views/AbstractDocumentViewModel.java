package org.icepdf.ri.common.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.icepdf.core.Memento;
import org.icepdf.core.pobjects.Document;
import org.icepdf.ri.common.UndoCaretaker;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/AbstractDocumentViewModel.class */
public abstract class AbstractDocumentViewModel implements DocumentViewModel {
    private static final Logger log = Logger.getLogger(AbstractDocumentViewModel.class.toString());
    protected Document currentDocument;
    private ArrayList<WeakReference<AbstractPageViewComponent>> selectedPageText;
    private boolean selectAll;
    protected List<AbstractPageViewComponent> pageComponents;
    protected AnnotationComponent currentAnnotation;
    protected float userRotation;
    protected float oldUserRotation;
    protected int currentPageIndex;
    protected int oldPageIndex;
    protected int userToolModeFlag;
    protected int oldUserToolModeFlag;
    protected static final int MAX_PAGE_SIZE_READ_AHEAD = 10;
    protected float userZoom = 1.0f;
    protected float oldUserZoom = 1.0f;
    protected int pageBoundary = 2;
    protected UndoCaretaker undoCaretaker = new UndoCaretaker();

    public AbstractDocumentViewModel(Document currentDocument) {
        this.currentDocument = currentDocument;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public Document getDocument() {
        return this.currentDocument;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public List<AbstractPageViewComponent> getPageComponents() {
        return this.pageComponents;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public boolean setViewCurrentPageIndex(int pageIndex) {
        boolean changed = pageIndex != this.currentPageIndex;
        this.oldPageIndex = this.currentPageIndex;
        this.currentPageIndex = pageIndex;
        return changed;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public int getViewCurrentPageIndex() {
        return this.currentPageIndex;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public ArrayList<WeakReference<AbstractPageViewComponent>> getSelectedPageText() {
        return this.selectedPageText;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public boolean isSelectAll() {
        return this.selectAll;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public void addSelectedPageText(AbstractPageViewComponent pageViewComponent) {
        if (this.selectedPageText == null) {
            this.selectedPageText = new ArrayList<>();
        }
        this.selectedPageText.add(new WeakReference<>(pageViewComponent));
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public void clearSelectedPageText() {
        if (this.selectedPageText != null) {
            this.selectedPageText.clear();
        }
        this.selectAll = false;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public boolean setViewZoom(float viewZoom) {
        boolean changed = this.userZoom != viewZoom;
        if (changed) {
            this.oldUserZoom = this.userZoom;
            this.userZoom = viewZoom;
            for (AbstractPageViewComponent pageViewComponent : this.pageComponents) {
                if (pageViewComponent != null) {
                    pageViewComponent.invalidate();
                }
            }
        }
        return changed;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public void invalidate() {
        for (AbstractPageViewComponent pageViewComponent : this.pageComponents) {
            pageViewComponent.invalidatePage();
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public float getViewZoom() {
        return this.userZoom;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public boolean setViewRotation(float viewRotation) {
        boolean changed = this.userRotation != viewRotation;
        if (changed) {
            this.oldUserRotation = this.userRotation;
            this.userRotation = viewRotation;
            for (AbstractPageViewComponent pageViewComponent : this.pageComponents) {
                if (pageViewComponent != null) {
                    pageViewComponent.invalidate();
                }
            }
        }
        return changed;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public float getViewRotation() {
        return this.userRotation;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public boolean setViewToolMode(int viewToolMode) {
        boolean changed = viewToolMode != this.userToolModeFlag;
        if (changed) {
            this.oldUserToolModeFlag = this.userToolModeFlag;
            this.userToolModeFlag = viewToolMode;
        }
        return changed;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public int getViewToolMode() {
        return this.userToolModeFlag;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public boolean isViewToolModeSelected(int viewToolMode) {
        return this.userToolModeFlag == viewToolMode;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public void setPageBoundary(int pageBoundary) {
        this.pageBoundary = pageBoundary;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public int getPageBoundary() {
        return this.pageBoundary;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public Rectangle getPageBounds(int pageIndex) {
        Component pageViewComponentImpl;
        Rectangle pageBounds = new Rectangle();
        if (this.pageComponents != null && pageIndex < this.pageComponents.size() && (pageViewComponentImpl = this.pageComponents.get(pageIndex)) != null) {
            Dimension size = pageViewComponentImpl.getPreferredSize();
            pageBounds.setSize(size.width, size.height);
            for (Component parentComponent = pageViewComponentImpl; parentComponent != null && !(parentComponent instanceof DocumentView); parentComponent = parentComponent.getParent()) {
                pageBounds.f12372x += parentComponent.getBounds().f12372x;
                pageBounds.f12373y += parentComponent.getBounds().f12373y;
            }
        }
        return pageBounds;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public void dispose() {
        if (this.pageComponents != null) {
            for (AbstractPageViewComponent pageViewComponent : this.pageComponents) {
                if (pageViewComponent != null) {
                    pageViewComponent.dispose();
                }
            }
            this.pageComponents.clear();
        }
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public AnnotationComponent getCurrentAnnotation() {
        return this.currentAnnotation;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public void setCurrentAnnotation(AnnotationComponent currentAnnotation) {
        if (this.currentAnnotation != null) {
            this.currentAnnotation.setSelected(false);
            this.currentAnnotation.repaint();
        }
        this.currentAnnotation = currentAnnotation;
        if (this.currentAnnotation != null) {
            this.currentAnnotation.setSelected(true);
        }
    }

    public UndoCaretaker getAnnotationCareTaker() {
        return this.undoCaretaker;
    }

    @Override // org.icepdf.ri.common.views.DocumentViewModel
    public void addMemento(Memento oldMementoState, Memento newMementoState) {
        this.undoCaretaker.addState(oldMementoState, newMementoState);
    }
}
