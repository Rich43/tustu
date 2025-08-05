package com.sun.glass.ui.win;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinView.class */
final class WinView extends View {
    private static final long multiClickTime;
    private static final int multiClickMaxX;
    private static final int multiClickMaxY;

    private static native void _initIDs();

    private static native long _getMultiClickTime_impl();

    private static native int _getMultiClickMaxX_impl();

    private static native int _getMultiClickMaxY_impl();

    @Override // com.sun.glass.ui.View
    protected native void _enableInputMethodEvents(long j2, boolean z2);

    @Override // com.sun.glass.ui.View
    protected native void _finishInputMethodComposition(long j2);

    @Override // com.sun.glass.ui.View
    protected native long _create(Map map);

    @Override // com.sun.glass.ui.View
    protected native long _getNativeView(long j2);

    @Override // com.sun.glass.ui.View
    protected native int _getX(long j2);

    @Override // com.sun.glass.ui.View
    protected native int _getY(long j2);

    @Override // com.sun.glass.ui.View
    protected native void _setParent(long j2, long j3);

    @Override // com.sun.glass.ui.View
    protected native boolean _close(long j2);

    @Override // com.sun.glass.ui.View
    protected native void _scheduleRepaint(long j2);

    @Override // com.sun.glass.ui.View
    protected native void _begin(long j2);

    @Override // com.sun.glass.ui.View
    protected native void _end(long j2);

    @Override // com.sun.glass.ui.View
    protected native void _uploadPixels(long j2, Pixels pixels);

    @Override // com.sun.glass.ui.View
    protected native boolean _enterFullscreen(long j2, boolean z2, boolean z3, boolean z4);

    @Override // com.sun.glass.ui.View
    protected native void _exitFullscreen(long j2, boolean z2);

    static {
        _initIDs();
        multiClickTime = _getMultiClickTime_impl();
        multiClickMaxX = _getMultiClickMaxX_impl();
        multiClickMaxY = _getMultiClickMaxY_impl();
    }

    protected WinView() {
    }

    static long getMultiClickTime_impl() {
        return multiClickTime;
    }

    static int getMultiClickMaxX_impl() {
        return multiClickMaxX;
    }

    static int getMultiClickMaxY_impl() {
        return multiClickMaxY;
    }

    @Override // com.sun.glass.ui.View
    protected int _getNativeFrameBuffer(long ptr) {
        return 0;
    }
}
