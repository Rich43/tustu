package com.sun.glass.ui.win;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Size;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinCursor.class */
final class WinCursor extends Cursor {
    private static native void _initIDs();

    @Override // com.sun.glass.ui.Cursor
    protected native long _createCursor(int i2, int i3, Pixels pixels);

    private static native void _setVisible(boolean z2);

    private static native Size _getBestSize(int i2, int i3);

    static {
        _initIDs();
    }

    protected WinCursor(int type) {
        super(type);
    }

    protected WinCursor(int x2, int y2, Pixels pixels) {
        super(x2, y2, pixels);
    }

    static void setVisible_impl(boolean visible) {
        _setVisible(visible);
    }

    static Size getBestSize_impl(int width, int height) {
        return _getBestSize(width, height);
    }
}
