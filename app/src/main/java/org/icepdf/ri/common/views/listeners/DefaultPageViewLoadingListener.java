package org.icepdf.ri.common.views.listeners;

import javax.swing.JComponent;
import org.icepdf.core.events.PageInitializingEvent;
import org.icepdf.core.events.PageLoadingEvent;
import org.icepdf.core.events.PagePaintingEvent;
import org.icepdf.ri.common.views.DocumentViewController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/listeners/DefaultPageViewLoadingListener.class */
public class DefaultPageViewLoadingListener extends PageViewLoadingListener {
    private JComponent pageComponent;
    private DocumentViewController documentViewController;

    public DefaultPageViewLoadingListener(JComponent pageComponent, DocumentViewController documentViewController) {
        this.pageComponent = pageComponent;
        this.documentViewController = documentViewController;
    }

    @Override // org.icepdf.ri.common.views.listeners.PageViewLoadingListener
    public void setDocumentViewController(DocumentViewController documentViewController) {
        this.documentViewController = documentViewController;
    }

    @Override // org.icepdf.core.events.PageLoadingAdapter, org.icepdf.core.events.PageLoadingListener
    public void pageLoadingStarted(PageLoadingEvent event) {
        this.pageComponent.setCursor(this.documentViewController.getViewCursor(6));
    }

    @Override // org.icepdf.core.events.PageLoadingAdapter, org.icepdf.core.events.PageLoadingListener
    public void pageInitializationStarted(PageInitializingEvent event) {
        this.pageComponent.setCursor(this.documentViewController.getViewCursor(6));
    }

    @Override // org.icepdf.core.events.PageLoadingAdapter, org.icepdf.core.events.PageLoadingListener
    public void pagePaintingStarted(PagePaintingEvent event) {
        this.pageComponent.setCursor(this.documentViewController.getViewCursor(6));
    }

    @Override // org.icepdf.core.events.PageLoadingAdapter, org.icepdf.core.events.PageLoadingListener
    public void pageLoadingEnded(PageLoadingEvent event) {
        this.pageComponent.setCursor(null);
    }
}
