package com.sun.glass.ui.win;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinRobot.class */
final class WinRobot extends Robot {
    @Override // com.sun.glass.ui.Robot
    protected native void _keyPress(int i2);

    @Override // com.sun.glass.ui.Robot
    protected native void _keyRelease(int i2);

    @Override // com.sun.glass.ui.Robot
    protected native void _mouseMove(int i2, int i3);

    @Override // com.sun.glass.ui.Robot
    protected native void _mousePress(int i2);

    @Override // com.sun.glass.ui.Robot
    protected native void _mouseRelease(int i2);

    @Override // com.sun.glass.ui.Robot
    protected native void _mouseWheel(int i2);

    @Override // com.sun.glass.ui.Robot
    protected native int _getMouseX();

    @Override // com.sun.glass.ui.Robot
    protected native int _getMouseY();

    @Override // com.sun.glass.ui.Robot
    protected native int _getPixelColor(int i2, int i3);

    private native void _getScreenCapture(int i2, int i3, int i4, int i5, int[] iArr);

    WinRobot() {
    }

    @Override // com.sun.glass.ui.Robot
    protected void _create() {
    }

    @Override // com.sun.glass.ui.Robot
    protected void _destroy() {
    }

    @Override // com.sun.glass.ui.Robot
    protected Pixels _getScreenCapture(int x2, int y2, int width, int height, boolean isHiDPI) {
        int[] data = new int[width * height];
        _getScreenCapture(x2, y2, width, height, data);
        return Application.GetApplication().createPixels(width, height, IntBuffer.wrap(data));
    }
}
