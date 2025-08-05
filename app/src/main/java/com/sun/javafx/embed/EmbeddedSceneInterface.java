package com.sun.javafx.embed;

import com.sun.javafx.scene.traversal.Direction;
import java.nio.IntBuffer;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.InputMethodTextRun;

/* loaded from: jfxrt.jar:com/sun/javafx/embed/EmbeddedSceneInterface.class */
public interface EmbeddedSceneInterface {
    void setSize(int i2, int i3);

    void setPixelScaleFactor(float f2);

    boolean getPixels(IntBuffer intBuffer, int i2, int i3);

    void mouseEvent(int i2, int i3, boolean z2, boolean z3, boolean z4, int i4, int i5, int i6, int i7, boolean z5, boolean z6, boolean z7, boolean z8, int i8, boolean z9);

    void keyEvent(int i2, int i3, char[] cArr, int i4);

    void menuEvent(int i2, int i3, int i4, int i5, boolean z2);

    boolean traverseOut(Direction direction);

    void setDragStartListener(HostDragStartListener hostDragStartListener);

    EmbeddedSceneDTInterface createDropTarget();

    void inputMethodEvent(EventType<InputMethodEvent> eventType, ObservableList<InputMethodTextRun> observableList, String str, int i2);

    InputMethodRequests getInputMethodRequests();
}
