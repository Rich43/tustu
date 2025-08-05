package com.sun.javafx.tk;

import java.security.AccessControlContext;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/TKStage.class */
public interface TKStage {
    public static final KeyCodeCombination defaultFullScreenExitKeycombo = new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP, KeyCombination.ModifierValue.UP);

    void setTKStageListener(TKStageListener tKStageListener);

    TKScene createTKScene(boolean z2, boolean z3, AccessControlContext accessControlContext);

    void setScene(TKScene tKScene);

    void setBounds(float f2, float f3, boolean z2, boolean z3, float f4, float f5, float f6, float f7, float f8, float f9);

    float getUIScale();

    float getRenderScale();

    void setIcons(List list);

    void setTitle(String str);

    void setVisible(boolean z2);

    void setOpacity(float f2);

    void setIconified(boolean z2);

    void setMaximized(boolean z2);

    void setAlwaysOnTop(boolean z2);

    void setResizable(boolean z2);

    void setImportant(boolean z2);

    void setMinimumSize(int i2, int i3);

    void setMaximumSize(int i2, int i3);

    void setFullScreen(boolean z2);

    void requestFocus();

    void toBack();

    void toFront();

    void close();

    void requestFocus(FocusCause focusCause);

    boolean grabFocus();

    void ungrabFocus();

    void requestInput(String str, int i2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15);

    void releaseInput();

    void setRTL(boolean z2);

    void setEnabled(boolean z2);

    long getRawHandle();

    default void postponeClose() {
    }

    default void closePostponed() {
    }
}
