package com.sun.javafx.tk;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/TKStageListener.class */
public interface TKStageListener {
    void changedLocation(float f2, float f3);

    void changedSize(float f2, float f3);

    void changedFocused(boolean z2, FocusCause focusCause);

    void changedIconified(boolean z2);

    void changedMaximized(boolean z2);

    void changedAlwaysOnTop(boolean z2);

    void changedResizable(boolean z2);

    void changedFullscreen(boolean z2);

    void changedScreen(Object obj, Object obj2);

    void closing();

    void closed();

    void focusUngrab();
}
