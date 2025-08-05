package com.sun.javafx.tk;

import com.sun.glass.ui.Accessible;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.input.ZoomEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/TKSceneListener.class */
public interface TKSceneListener {
    void changedLocation(float f2, float f3);

    void changedSize(float f2, float f3);

    void mouseEvent(EventType<MouseEvent> eventType, double d2, double d3, double d4, double d5, MouseButton mouseButton, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, boolean z10);

    void keyEvent(KeyEvent keyEvent);

    void inputMethodEvent(EventType<InputMethodEvent> eventType, ObservableList<InputMethodTextRun> observableList, String str, int i2);

    void scrollEvent(EventType<ScrollEvent> eventType, double d2, double d3, double d4, double d5, double d6, double d7, int i2, int i3, int i4, int i5, int i6, double d8, double d9, double d10, double d11, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7);

    void menuEvent(double d2, double d3, double d4, double d5, boolean z2);

    void zoomEvent(EventType<ZoomEvent> eventType, double d2, double d3, double d4, double d5, double d6, double d7, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7);

    void rotateEvent(EventType<RotateEvent> eventType, double d2, double d3, double d4, double d5, double d6, double d7, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7);

    void swipeEvent(EventType<SwipeEvent> eventType, int i2, double d2, double d3, double d4, double d5, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6);

    void touchEventBegin(long j2, int i2, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6);

    void touchEventNext(TouchPoint.State state, long j2, double d2, double d3, double d4, double d5);

    void touchEventEnd();

    Accessible getSceneAccessible();
}
