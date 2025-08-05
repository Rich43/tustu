package com.sun.glass.ui;

import java.security.AllPermission;
import java.security.Permission;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Robot.class */
public abstract class Robot {
    private static final Permission allPermission = new AllPermission();
    public static final int MOUSE_LEFT_BTN = 1;
    public static final int MOUSE_RIGHT_BTN = 2;
    public static final int MOUSE_MIDDLE_BTN = 4;

    protected abstract void _create();

    protected abstract void _destroy();

    protected abstract void _keyPress(int i2);

    protected abstract void _keyRelease(int i2);

    protected abstract void _mouseMove(int i2, int i3);

    protected abstract void _mousePress(int i2);

    protected abstract void _mouseRelease(int i2);

    protected abstract void _mouseWheel(int i2);

    protected abstract int _getMouseX();

    protected abstract int _getMouseY();

    protected abstract int _getPixelColor(int i2, int i3);

    protected abstract Pixels _getScreenCapture(int i2, int i3, int i4, int i5, boolean z2);

    protected Robot() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(allPermission);
        }
        Application.checkEventThread();
        _create();
    }

    public void destroy() {
        Application.checkEventThread();
        _destroy();
    }

    public void keyPress(int code) {
        Application.checkEventThread();
        _keyPress(code);
    }

    public void keyRelease(int code) {
        Application.checkEventThread();
        _keyRelease(code);
    }

    public void mouseMove(int x2, int y2) {
        Application.checkEventThread();
        _mouseMove(x2, y2);
    }

    public void mousePress(int buttons) {
        Application.checkEventThread();
        _mousePress(buttons);
    }

    public void mouseRelease(int buttons) {
        Application.checkEventThread();
        _mouseRelease(buttons);
    }

    public void mouseWheel(int wheelAmt) {
        Application.checkEventThread();
        _mouseWheel(wheelAmt);
    }

    public int getMouseX() {
        Application.checkEventThread();
        return _getMouseX();
    }

    public int getMouseY() {
        Application.checkEventThread();
        return _getMouseY();
    }

    public int getPixelColor(int x2, int y2) {
        Application.checkEventThread();
        return _getPixelColor(x2, y2);
    }

    public Pixels getScreenCapture(int x2, int y2, int width, int height, boolean isHiDPI) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be > 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height must be > 0");
        }
        if (width >= Integer.MAX_VALUE / height) {
            throw new IllegalArgumentException("invalid capture size");
        }
        Application.checkEventThread();
        return _getScreenCapture(x2, y2, width, height, isHiDPI);
    }

    public Pixels getScreenCapture(int x2, int y2, int width, int height) {
        return getScreenCapture(x2, y2, width, height, false);
    }
}
