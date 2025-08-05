package com.sun.webkit;

import com.sun.webkit.graphics.WCFont;

/* loaded from: jfxrt.jar:com/sun/webkit/PopupMenu.class */
public abstract class PopupMenu {
    private long pdata;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract void show(WebPage webPage, int i2, int i3, int i4);

    protected abstract void hide();

    protected abstract void setSelectedItem(int i2);

    protected abstract void appendItem(String str, boolean z2, boolean z3, boolean z4, int i2, int i3, WCFont wCFont);

    private native void twkSelectionCommited(long j2, int i2);

    private native void twkPopupClosed(long j2);

    static {
        $assertionsDisabled = !PopupMenu.class.desiredAssertionStatus();
    }

    protected void notifySelectionCommited(int index) {
        twkSelectionCommited(this.pdata, index);
    }

    protected void notifyPopupClosed() {
        twkPopupClosed(this.pdata);
    }

    private static PopupMenu fwkCreatePopupMenu(long pData) {
        PopupMenu popupMenu = Utilities.getUtilities().createPopupMenu();
        popupMenu.pdata = pData;
        return popupMenu;
    }

    private void fwkShow(WebPage page, int x2, int y2, int width) {
        if (!$assertionsDisabled && page == null) {
            throw new AssertionError();
        }
        show(page, x2, y2, width);
    }

    private void fwkHide() {
        hide();
    }

    private void fwkSetSelectedItem(int index) {
        setSelectedItem(index);
    }

    private void fwkAppendItem(String itemText, boolean isLabel, boolean isSeparator, boolean isEnabled, int bgColor, int fgColor, WCFont font) {
        appendItem(itemText, isLabel, isSeparator, isEnabled, bgColor, fgColor, font);
    }

    private void fwkDestroy() {
        this.pdata = 0L;
    }
}
