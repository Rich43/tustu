package com.sun.webkit.graphics;

import com.sun.prism.paint.Color;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCGradient.class */
public abstract class WCGradient<G> {
    public static final int PAD = 1;
    public static final int REFLECT = 2;
    public static final int REPEAT = 3;
    private int spreadMethod = 1;
    private boolean proportional;

    protected abstract void addStop(Color color, float f2);

    public abstract G getPlatformGradient();

    void setSpreadMethod(int spreadMethod) {
        if (spreadMethod != 2 && spreadMethod != 3) {
            spreadMethod = 1;
        }
        this.spreadMethod = spreadMethod;
    }

    public int getSpreadMethod() {
        return this.spreadMethod;
    }

    void setProportional(boolean proportional) {
        this.proportional = proportional;
    }

    public boolean isProportional() {
        return this.proportional;
    }
}
