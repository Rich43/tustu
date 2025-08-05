package org.icepdf.core.events;

/* loaded from: icepdf-core.jar:org/icepdf/core/events/PageLoadingListener.class */
public interface PageLoadingListener {
    void pageLoadingStarted(PageLoadingEvent pageLoadingEvent);

    void pageInitializationStarted(PageInitializingEvent pageInitializingEvent);

    void pageInitializationEnded(PageInitializingEvent pageInitializingEvent);

    void pageImageLoaded(PageImageEvent pageImageEvent);

    void pagePaintingStarted(PagePaintingEvent pagePaintingEvent);

    void pagePaintingEnded(PagePaintingEvent pagePaintingEvent);

    void pageLoadingEnded(PageLoadingEvent pageLoadingEvent);
}
