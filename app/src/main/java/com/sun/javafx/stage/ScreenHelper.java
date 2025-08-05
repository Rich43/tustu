package com.sun.javafx.stage;

import javafx.stage.Screen;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/ScreenHelper.class */
public final class ScreenHelper {
    private static ScreenAccessor screenAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/stage/ScreenHelper$ScreenAccessor.class */
    public interface ScreenAccessor {
        float getRenderScale(Screen screen);
    }

    public static void setScreenAccessor(ScreenAccessor a2) {
        if (screenAccessor != null) {
            throw new IllegalStateException();
        }
        screenAccessor = a2;
    }

    public static ScreenAccessor getScreenAccessor() {
        return screenAccessor;
    }
}
