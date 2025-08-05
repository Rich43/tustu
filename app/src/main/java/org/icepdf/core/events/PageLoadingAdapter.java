package org.icepdf.core.events;

/* loaded from: icepdf-core.jar:org/icepdf/core/events/PageLoadingAdapter.class */
public abstract class PageLoadingAdapter implements PageLoadingListener {
    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageLoadingStarted(PageLoadingEvent event) {
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageInitializationStarted(PageInitializingEvent event) {
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageInitializationEnded(PageInitializingEvent event) {
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageImageLoaded(PageImageEvent event) {
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pagePaintingStarted(PagePaintingEvent event) {
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pagePaintingEnded(PagePaintingEvent event) {
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageLoadingEnded(PageLoadingEvent event) {
    }
}
