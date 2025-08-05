package com.sun.javafx.webkit;

import com.sun.webkit.WebPage;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/Accessor.class */
public abstract class Accessor {
    private static PageAccessor pageAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/Accessor$PageAccessor.class */
    public interface PageAccessor {
        WebPage getPage(WebEngine webEngine);
    }

    public abstract WebEngine getEngine();

    public abstract WebView getView();

    public abstract WebPage getPage();

    public abstract void addChild(Node node);

    public abstract void removeChild(Node node);

    public abstract void addViewListener(InvalidationListener invalidationListener);

    public static void setPageAccessor(PageAccessor instance) {
        pageAccessor = instance;
    }

    public static WebPage getPageFor(WebEngine w2) {
        return pageAccessor.getPage(w2);
    }
}
