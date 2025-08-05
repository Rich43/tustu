package org.icepdf.ri.common.views.listeners;

import org.icepdf.core.events.PageLoadingAdapter;
import org.icepdf.ri.common.views.DocumentViewController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/listeners/PageViewLoadingListener.class */
public abstract class PageViewLoadingListener extends PageLoadingAdapter {
    public abstract void setDocumentViewController(DocumentViewController documentViewController);
}
