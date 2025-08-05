package com.sun.awt;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.geom.Point2D;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:com/sun/awt/SecurityWarning.class */
public final class SecurityWarning {
    private SecurityWarning() {
    }

    public static Dimension getSize(Window window) {
        if (window == null) {
            throw new NullPointerException("The window argument should not be null.");
        }
        if (window.getWarningString() == null) {
            throw new IllegalArgumentException("The window must have a non-null warning string.");
        }
        return AWTAccessor.getWindowAccessor().getSecurityWarningSize(window);
    }

    public static void setPosition(Window window, Point2D point2D, float f2, float f3) {
        if (window == null) {
            throw new NullPointerException("The window argument should not be null.");
        }
        if (window.getWarningString() == null) {
            throw new IllegalArgumentException("The window must have a non-null warning string.");
        }
        if (point2D == null) {
            throw new NullPointerException("The point argument must not be null");
        }
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("alignmentX must be in the range [0.0f ... 1.0f].");
        }
        if (f3 < 0.0f || f3 > 1.0f) {
            throw new IllegalArgumentException("alignmentY must be in the range [0.0f ... 1.0f].");
        }
        AWTAccessor.getWindowAccessor().setSecurityWarningPosition(window, point2D, f2, f3);
    }
}
