package com.sun.webkit;

/* loaded from: jfxrt.jar:com/sun/webkit/WCFrameView.class */
final class WCFrameView extends WCWidget {
    WCFrameView(WebPage page) {
        super(page);
    }

    @Override // com.sun.webkit.WCWidget
    protected void requestFocus() {
        WebPageClient pageClient = getPage().getPageClient();
        if (pageClient != null) {
            pageClient.setFocus(true);
        }
    }

    @Override // com.sun.webkit.WCWidget
    protected void setCursor(long cursorID) {
        WebPageClient pageClient = getPage().getPageClient();
        if (pageClient != null) {
            pageClient.setCursor(cursorID);
        }
    }
}
