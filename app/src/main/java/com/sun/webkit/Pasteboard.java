package com.sun.webkit;

import com.sun.webkit.graphics.WCImageFrame;

/* loaded from: jfxrt.jar:com/sun/webkit/Pasteboard.class */
public interface Pasteboard {
    String getPlainText();

    String getHtml();

    void writePlainText(String str);

    void writeSelection(boolean z2, String str, String str2);

    void writeImage(WCImageFrame wCImageFrame);

    void writeUrl(String str, String str2);
}
