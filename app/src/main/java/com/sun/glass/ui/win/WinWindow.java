package com.sun.glass.ui.win;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinWindow.class */
class WinWindow extends Window {
    private boolean deferredClosing;
    private boolean closingRequested;

    private static native void _initIDs();

    @Override // com.sun.glass.ui.Window
    protected native long _createWindow(long j2, long j3, int i2);

    @Override // com.sun.glass.ui.Window
    protected native long _createChildWindow(long j2);

    @Override // com.sun.glass.ui.Window
    protected native boolean _close(long j2);

    @Override // com.sun.glass.ui.Window
    protected native boolean _setView(long j2, View view);

    @Override // com.sun.glass.ui.Window
    protected native boolean _setMenubar(long j2, long j3);

    @Override // com.sun.glass.ui.Window
    protected native boolean _minimize(long j2, boolean z2);

    @Override // com.sun.glass.ui.Window
    protected native boolean _maximize(long j2, boolean z2, boolean z3);

    @Override // com.sun.glass.ui.Window
    protected native void _setBounds(long j2, int i2, int i3, boolean z2, boolean z3, int i4, int i5, int i6, int i7, float f2, float f3);

    @Override // com.sun.glass.ui.Window
    protected native boolean _setVisible(long j2, boolean z2);

    @Override // com.sun.glass.ui.Window
    protected native boolean _setResizable(long j2, boolean z2);

    @Override // com.sun.glass.ui.Window
    protected native boolean _requestFocus(long j2, int i2);

    @Override // com.sun.glass.ui.Window
    protected native void _setFocusable(long j2, boolean z2);

    @Override // com.sun.glass.ui.Window
    protected native boolean _setTitle(long j2, String str);

    @Override // com.sun.glass.ui.Window
    protected native void _setLevel(long j2, int i2);

    @Override // com.sun.glass.ui.Window
    protected native void _setAlpha(long j2, float f2);

    @Override // com.sun.glass.ui.Window
    protected native boolean _setBackground(long j2, float f2, float f3, float f4);

    @Override // com.sun.glass.ui.Window
    protected native void _setEnabled(long j2, boolean z2);

    @Override // com.sun.glass.ui.Window
    protected native boolean _setMinimumSize(long j2, int i2, int i3);

    @Override // com.sun.glass.ui.Window
    protected native boolean _setMaximumSize(long j2, int i2, int i3);

    @Override // com.sun.glass.ui.Window
    protected native void _setIcon(long j2, Pixels pixels);

    @Override // com.sun.glass.ui.Window
    protected native void _toFront(long j2);

    @Override // com.sun.glass.ui.Window
    protected native void _toBack(long j2);

    @Override // com.sun.glass.ui.Window
    protected native void _enterModal(long j2);

    @Override // com.sun.glass.ui.Window
    protected native void _enterModalWithWindow(long j2, long j3);

    @Override // com.sun.glass.ui.Window
    protected native void _exitModal(long j2);

    @Override // com.sun.glass.ui.Window
    protected native boolean _grabFocus(long j2);

    @Override // com.sun.glass.ui.Window
    protected native void _ungrabFocus(long j2);

    @Override // com.sun.glass.ui.Window
    protected native int _getEmbeddedX(long j2);

    @Override // com.sun.glass.ui.Window
    protected native int _getEmbeddedY(long j2);

    @Override // com.sun.glass.ui.Window
    protected native void _setCursor(long j2, Cursor cursor);

    static {
        _initIDs();
    }

    protected WinWindow(Window owner, Screen screen, int styleMask) {
        super(owner, screen, styleMask);
        this.deferredClosing = false;
        this.closingRequested = false;
        setPlatformScale(screen.getUIScale());
        setRenderScale(screen.getRenderScale());
    }

    protected WinWindow(long parent) {
        super(parent);
        this.deferredClosing = false;
        this.closingRequested = false;
        setPlatformScale(getScreen().getUIScale());
        setRenderScale(getScreen().getRenderScale());
    }

    protected void notifyScaleChanged(float newUIScale, float newRenderScale) {
        setPlatformScale(newUIScale);
        setRenderScale(newRenderScale);
    }

    @Override // com.sun.glass.ui.Window
    protected void _requestInput(long ptr, String text, int type, double width, double height, double Mxx, double Mxy, double Mxz, double Mxt, double Myx, double Myy, double Myz, double Myt, double Mzx, double Mzy, double Mzz, double Mzt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.glass.ui.Window
    protected void _releaseInput(long ptr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void setDeferredClosing(boolean dc) {
        this.deferredClosing = dc;
        if (!this.deferredClosing && this.closingRequested) {
            close();
        }
    }

    @Override // com.sun.glass.ui.Window
    public void close() {
        if (!this.deferredClosing) {
            super.close();
        } else {
            this.closingRequested = true;
            setVisible(false);
        }
    }
}
