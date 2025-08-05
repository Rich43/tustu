package com.sun.webkit;

import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;

/* loaded from: jfxrt.jar:com/sun/webkit/WebPageClient.class */
public interface WebPageClient<T> {
    void setCursor(long j2);

    void setFocus(boolean z2);

    void transferFocus(boolean z2);

    void setTooltip(String str);

    WCRectangle getScreenBounds(boolean z2);

    int getScreenDepth();

    T getContainer();

    WCPoint screenToWindow(WCPoint wCPoint);

    WCPoint windowToScreen(WCPoint wCPoint);

    WCPageBackBuffer createBackBuffer();

    boolean isBackBufferSupported();

    void addMessageToConsole(String str, int i2, String str2);

    void didClearWindowObject(long j2, long j3);
}
