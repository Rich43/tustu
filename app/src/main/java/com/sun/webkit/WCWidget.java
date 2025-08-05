package com.sun.webkit;

import com.sun.webkit.graphics.WCRectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/WCWidget.class */
class WCWidget {
    private static final Logger log = Logger.getLogger(WCWidget.class.getName());

    /* renamed from: x, reason: collision with root package name */
    private int f12050x;

    /* renamed from: y, reason: collision with root package name */
    private int f12051y;
    private int width;
    private int height;
    private final WebPage page;

    private static native void initIDs();

    static {
        initIDs();
    }

    WCWidget(WebPage page) {
        this.page = page;
    }

    WebPage getPage() {
        return this.page;
    }

    WCRectangle getBounds() {
        return new WCRectangle(this.f12050x, this.f12051y, this.width, this.height);
    }

    void setBounds(int x2, int y2, int width, int height) {
        this.f12050x = x2;
        this.f12051y = y2;
        this.width = width;
        this.height = height;
    }

    protected void destroy() {
    }

    protected void requestFocus() {
    }

    protected void setCursor(long cursorID) {
    }

    protected void setVisible(boolean visible) {
    }

    private void fwkDestroy() {
        log.log(Level.FINER, "destroy");
        destroy();
    }

    private void fwkSetBounds(int x2, int y2, int w2, int h2) {
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "setBounds({0}, {1}, {2}, {3})", new Object[]{Integer.valueOf(x2), Integer.valueOf(y2), Integer.valueOf(w2), Integer.valueOf(h2)});
        }
        setBounds(x2, y2, w2, h2);
    }

    private void fwkRequestFocus() {
        log.log(Level.FINER, "requestFocus");
        requestFocus();
    }

    private void fwkSetCursor(long cursorID) {
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "setCursor({0})", Long.valueOf(cursorID));
        }
        setCursor(cursorID);
    }

    private void fwkSetVisible(boolean visible) {
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "setVisible({0})", Boolean.valueOf(visible));
        }
        setVisible(visible);
    }

    protected int fwkGetScreenDepth() {
        log.log(Level.FINER, "getScreenDepth");
        WebPageClient pageClient = this.page.getPageClient();
        if (pageClient != null) {
            return pageClient.getScreenDepth();
        }
        return 24;
    }

    protected WCRectangle fwkGetScreenRect(boolean available) {
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getScreenRect({0})", Boolean.valueOf(available));
        }
        WebPageClient pageClient = this.page.getPageClient();
        if (pageClient != null) {
            return pageClient.getScreenBounds(available);
        }
        return null;
    }
}
