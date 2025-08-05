package com.sun.javafx.webkit;

import javafx.scene.web.WebView;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/WebConsoleListener.class */
public interface WebConsoleListener {
    void messageAdded(WebView webView, String str, int i2, String str2);

    static void setDefaultListener(WebConsoleListener l2) {
        WebPageClientImpl.setConsoleListener(l2);
    }
}
