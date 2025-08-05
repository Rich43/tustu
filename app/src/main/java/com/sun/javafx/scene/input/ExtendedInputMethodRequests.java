package com.sun.javafx.scene.input;

import javafx.scene.input.InputMethodRequests;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/input/ExtendedInputMethodRequests.class */
public interface ExtendedInputMethodRequests extends InputMethodRequests {
    int getInsertPositionOffset();

    String getCommittedText(int i2, int i3);

    int getCommittedTextLength();
}
