package com.sun.javafx.robot.impl;

import com.sun.javafx.robot.FXRobotImage;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/* loaded from: jfxrt.jar:com/sun/javafx/robot/impl/FXRobotHelper.class */
public class FXRobotHelper {
    static FXRobotInputAccessor inputAccessor;
    static FXRobotSceneAccessor sceneAccessor;
    static FXRobotStageAccessor stageAccessor;
    static FXRobotImageConvertor imageConvertor;

    /* loaded from: jfxrt.jar:com/sun/javafx/robot/impl/FXRobotHelper$FXRobotImageConvertor.class */
    public static abstract class FXRobotImageConvertor {
        public abstract FXRobotImage convertToFXRobotImage(Object obj);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/robot/impl/FXRobotHelper$FXRobotInputAccessor.class */
    public static abstract class FXRobotInputAccessor {
        public abstract int getCodeForKeyCode(KeyCode keyCode);

        public abstract KeyCode getKeyCodeForCode(int i2);

        public abstract KeyEvent createKeyEvent(EventType<? extends KeyEvent> eventType, KeyCode keyCode, String str, String str2, boolean z2, boolean z3, boolean z4, boolean z5);

        public abstract MouseEvent createMouseEvent(EventType<? extends MouseEvent> eventType, int i2, int i3, int i4, int i5, MouseButton mouseButton, int i6, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9);

        public abstract ScrollEvent createScrollEvent(EventType<? extends ScrollEvent> eventType, int i2, int i3, ScrollEvent.HorizontalTextScrollUnits horizontalTextScrollUnits, int i4, ScrollEvent.VerticalTextScrollUnits verticalTextScrollUnits, int i5, int i6, int i7, int i8, int i9, boolean z2, boolean z3, boolean z4, boolean z5);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/robot/impl/FXRobotHelper$FXRobotSceneAccessor.class */
    public static abstract class FXRobotSceneAccessor {
        public abstract void processKeyEvent(Scene scene, KeyEvent keyEvent);

        public abstract void processMouseEvent(Scene scene, MouseEvent mouseEvent);

        public abstract void processScrollEvent(Scene scene, ScrollEvent scrollEvent);

        public abstract ObservableList<Node> getChildren(Parent parent);

        public abstract Object renderToImage(Scene scene, Object obj);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/robot/impl/FXRobotHelper$FXRobotStageAccessor.class */
    public static abstract class FXRobotStageAccessor {
        public abstract ObservableList<Stage> getStages();
    }

    public static ObservableList<Node> getChildren(Parent p2) {
        if (sceneAccessor == null) {
        }
        return sceneAccessor.getChildren(p2);
    }

    public static ObservableList<Stage> getStages() {
        if (stageAccessor == null) {
        }
        return stageAccessor.getStages();
    }

    public static Color argbToColor(int argb) {
        int a2 = argb >> 24;
        float aa2 = (a2 & 255) / 255.0f;
        int r2 = argb >> 16;
        int r3 = r2 & 255;
        int g2 = argb >> 8;
        int g3 = g2 & 255;
        int b2 = argb & 255;
        return Color.rgb(r3, g3, b2, aa2);
    }

    public static void setInputAccessor(FXRobotInputAccessor a2) {
        if (inputAccessor != null) {
            System.out.println("Warning: Input accessor is already set: " + ((Object) inputAccessor));
            Thread.dumpStack();
        }
        inputAccessor = a2;
    }

    public static void setSceneAccessor(FXRobotSceneAccessor a2) {
        if (sceneAccessor != null) {
            System.out.println("Warning: Scene accessor is already set: " + ((Object) sceneAccessor));
            Thread.dumpStack();
        }
        sceneAccessor = a2;
    }

    public static void setImageConvertor(FXRobotImageConvertor ic) {
        if (imageConvertor != null) {
            System.out.println("Warning: Image convertor is already set: " + ((Object) imageConvertor));
            Thread.dumpStack();
        }
        imageConvertor = ic;
    }

    public static void setStageAccessor(FXRobotStageAccessor a2) {
        if (stageAccessor != null) {
            System.out.println("Warning: Stage accessor already set: " + ((Object) stageAccessor));
            Thread.dumpStack();
        }
        stageAccessor = a2;
    }
}
