package com.sun.webkit;

import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCRectangle;

/* loaded from: jfxrt.jar:com/sun/webkit/UIClient.class */
public interface UIClient {
    WebPage createPage(boolean z2, boolean z3, boolean z4, boolean z5);

    void closePage();

    void showView();

    WCRectangle getViewBounds();

    void setViewBounds(WCRectangle wCRectangle);

    void setStatusbarText(String str);

    void alert(String str);

    boolean confirm(String str);

    String prompt(String str, String str2);

    boolean canRunBeforeUnloadConfirmPanel();

    boolean runBeforeUnloadConfirmPanel(String str);

    String[] chooseFile(String str, boolean z2, String str2);

    void print();

    void startDrag(WCImage wCImage, int i2, int i3, int i4, int i5, String[] strArr, Object[] objArr, boolean z2);

    void confirmStartDrag();

    boolean isDragConfirmed();
}
