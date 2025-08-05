package com.sun.javafx.robot;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/* loaded from: jfxrt.jar:com/sun/javafx/robot/FXRobot.class */
public abstract class FXRobot {
    protected boolean autoWait = false;

    public abstract void waitForIdle();

    public abstract void keyPress(KeyCode keyCode);

    public abstract void keyRelease(KeyCode keyCode);

    public abstract void keyType(KeyCode keyCode, String str);

    public abstract void mouseMove(int i2, int i3);

    public abstract void mousePress(MouseButton mouseButton, int i2);

    public abstract void mouseRelease(MouseButton mouseButton, int i2);

    public abstract void mouseClick(MouseButton mouseButton, int i2);

    public abstract void mouseDrag(MouseButton mouseButton);

    public abstract void mouseWheel(int i2);

    public abstract int getPixelColor(int i2, int i3);

    public abstract FXRobotImage getSceneCapture(int i2, int i3, int i4, int i5);

    public void setAutoWaitForIdle(boolean wait) {
        this.autoWait = wait;
    }

    public void mousePress(MouseButton button) {
        mousePress(button, 1);
    }

    public void mouseRelease(MouseButton button) {
        mouseRelease(button, 1);
    }

    public void mouseClick(MouseButton button) {
        mouseClick(button, 1);
    }
}
