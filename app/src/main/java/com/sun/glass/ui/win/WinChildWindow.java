package com.sun.glass.ui.win;

import com.sun.glass.ui.Pixels;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinChildWindow.class */
final class WinChildWindow extends WinWindow {
    protected WinChildWindow(long parent) {
        super(parent);
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected long _createWindow(long ownerPtr, long screenPtr, int mask) {
        return 0L;
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected boolean _setMenubar(long ptr, long menubarPtr) {
        return false;
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected boolean _minimize(long ptr, boolean minimize) {
        return false;
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected boolean _maximize(long ptr, boolean maximize, boolean wasMaximized) {
        return false;
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected boolean _setResizable(long ptr, boolean resizable) {
        return false;
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected boolean _setTitle(long ptr, String title) {
        return false;
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected void _setLevel(long ptr, int level) {
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected void _setAlpha(long ptr, float alpha) {
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected boolean _setMinimumSize(long ptr, int width, int height) {
        return false;
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected boolean _setMaximumSize(long ptr, int width, int height) {
        return false;
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected void _setIcon(long ptr, Pixels pixels) {
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected void _enterModal(long ptr) {
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected void _enterModalWithWindow(long dialog, long window) {
    }

    @Override // com.sun.glass.ui.win.WinWindow, com.sun.glass.ui.Window
    protected void _exitModal(long ptr) {
    }
}
