package com.sun.javafx.robot.impl;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotImage;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/robot/impl/BaseFXRobot.class */
public class BaseFXRobot extends FXRobot {
    private static final boolean debugOut;
    private static Map<KeyCode, String> keyTextMap;
    private Scene target;
    private boolean isShiftDown = false;
    private boolean isControlDown = false;
    private boolean isAltDown = false;
    private boolean isMetaDown = false;
    private boolean isButton1Pressed = false;
    private boolean isButton2Pressed = false;
    private boolean isButton3Pressed = false;
    private MouseButton lastButtonPressed = null;
    private double sceneMouseX;
    private double sceneMouseY;
    private double screenMouseX;
    private double screenMouseY;
    private Object lastImage;
    private FXRobotImage lastConvertedImage;

    static {
        String str = KeyEvent.CHAR_UNDEFINED;
        debugOut = computeDebugOut();
    }

    private static boolean computeDebugOut() {
        boolean debug = false;
        try {
            debug = "true".equals(System.getProperty("fxrobot.verbose", "false"));
        } catch (Throwable th) {
        }
        return debug;
    }

    private static void out(String s2) {
        if (debugOut) {
            System.out.println("[FXRobot] " + s2);
        }
    }

    private static String getKeyText(KeyCode keyCode) {
        return keyCode.getName();
    }

    public BaseFXRobot(Scene target) {
        this.target = target;
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void waitForIdle() {
        CountDownLatch latch = new CountDownLatch(1);
        PlatformImpl.runLater(() -> {
            latch.countDown();
        });
        while (true) {
            try {
                latch.await();
                return;
            } catch (InterruptedException e2) {
            }
        }
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void keyPress(KeyCode keyCode) {
        doKeyEvent(KeyEvent.KEY_PRESSED, keyCode, "");
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void keyRelease(KeyCode keyCode) {
        doKeyEvent(KeyEvent.KEY_RELEASED, keyCode, "");
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void keyType(KeyCode keyCode, String keyChar) {
        doKeyEvent(KeyEvent.KEY_TYPED, keyCode, keyChar);
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void mouseMove(int x2, int y2) {
        doMouseEvent(x2, y2, this.lastButtonPressed, 0, MouseEvent.MOUSE_MOVED);
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void mousePress(MouseButton button, int clickCount) {
        doMouseEvent(this.sceneMouseX, this.sceneMouseY, button, clickCount, MouseEvent.MOUSE_PRESSED);
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void mouseRelease(MouseButton button, int clickCount) {
        doMouseEvent(this.sceneMouseX, this.sceneMouseY, button, clickCount, MouseEvent.MOUSE_RELEASED);
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void mouseClick(MouseButton button, int clickCount) {
        doMouseEvent(this.sceneMouseX, this.sceneMouseY, button, clickCount, MouseEvent.MOUSE_CLICKED);
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void mouseDrag(MouseButton button) {
        doMouseEvent(this.sceneMouseX, this.sceneMouseY, button, 0, MouseEvent.MOUSE_DRAGGED);
    }

    @Override // com.sun.javafx.robot.FXRobot
    public void mouseWheel(int wheelAmt) {
        doScrollEvent(this.sceneMouseX, this.sceneMouseY, wheelAmt, ScrollEvent.SCROLL);
    }

    @Override // com.sun.javafx.robot.FXRobot
    public int getPixelColor(int x2, int y2) {
        FXRobotImage image = getSceneCapture(0, 0, 100, 100);
        if (image != null) {
            return image.getArgb(x2, y2);
        }
        return 0;
    }

    @Override // com.sun.javafx.robot.FXRobot
    public FXRobotImage getSceneCapture(int x2, int y2, int w2, int h2) {
        Object ret = FXRobotHelper.sceneAccessor.renderToImage(this.target, this.lastImage);
        if (ret != null) {
            this.lastImage = ret;
            this.lastConvertedImage = FXRobotHelper.imageConvertor.convertToFXRobotImage(ret);
        }
        return this.lastConvertedImage;
    }

    private void doKeyEvent(EventType<KeyEvent> eventType, KeyCode keyCode, String character) {
        boolean pressed = eventType == KeyEvent.KEY_PRESSED;
        boolean typed = eventType == KeyEvent.KEY_TYPED;
        if (keyCode == KeyCode.SHIFT) {
            this.isShiftDown = pressed;
        }
        if (keyCode == KeyCode.CONTROL) {
            this.isControlDown = pressed;
        }
        if (keyCode == KeyCode.ALT) {
            this.isAltDown = pressed;
        }
        if (keyCode == KeyCode.META) {
            this.isMetaDown = pressed;
        }
        String keyText = typed ? "" : getKeyText(keyCode);
        String keyChar = typed ? character : KeyEvent.CHAR_UNDEFINED;
        KeyEvent e2 = FXRobotHelper.inputAccessor.createKeyEvent(eventType, keyCode, keyChar, keyText, this.isShiftDown, this.isControlDown, this.isAltDown, this.isMetaDown);
        PlatformImpl.runLater(() -> {
            out("doKeyEvent: injecting: {e}");
            FXRobotHelper.sceneAccessor.processKeyEvent(this.target, e2);
        });
        if (this.autoWait) {
            waitForIdle();
        }
    }

    private void doMouseEvent(double x2, double y2, MouseButton passedButton, int clickCount, EventType<MouseEvent> passedType) {
        this.screenMouseX = this.target.getWindow().getX() + this.target.getX() + x2;
        this.screenMouseY = this.target.getWindow().getY() + this.target.getY() + y2;
        this.sceneMouseX = x2;
        this.sceneMouseY = y2;
        MouseButton button = passedButton;
        EventType<MouseEvent> type = passedType;
        if (type == MouseEvent.MOUSE_PRESSED || type == MouseEvent.MOUSE_RELEASED) {
            boolean pressed = type == MouseEvent.MOUSE_PRESSED;
            if (button == MouseButton.PRIMARY) {
                this.isButton1Pressed = pressed;
            } else if (button == MouseButton.MIDDLE) {
                this.isButton2Pressed = pressed;
            } else if (button == MouseButton.SECONDARY) {
                this.isButton3Pressed = pressed;
            }
            if (pressed) {
                this.lastButtonPressed = button;
            } else if (!this.isButton1Pressed && !this.isButton2Pressed && !this.isButton3Pressed) {
                this.lastButtonPressed = MouseButton.NONE;
            }
        } else if (type == MouseEvent.MOUSE_MOVED) {
            boolean someButtonPressed = this.isButton1Pressed || this.isButton2Pressed || this.isButton3Pressed;
            if (someButtonPressed) {
                type = MouseEvent.MOUSE_DRAGGED;
                button = MouseButton.NONE;
            }
        }
        MouseEvent e2 = FXRobotHelper.inputAccessor.createMouseEvent(type, (int) this.sceneMouseX, (int) this.sceneMouseY, (int) this.screenMouseX, (int) this.screenMouseY, button, clickCount, this.isShiftDown, this.isControlDown, this.isAltDown, this.isMetaDown, button == MouseButton.SECONDARY, this.isButton1Pressed, this.isButton2Pressed, this.isButton3Pressed);
        PlatformImpl.runLater(() -> {
            out("doMouseEvent: injecting: " + ((Object) e2));
            FXRobotHelper.sceneAccessor.processMouseEvent(this.target, e2);
        });
        if (this.autoWait) {
            waitForIdle();
        }
    }

    private void doScrollEvent(double x2, double y2, double rotation, EventType<ScrollEvent> type) {
        this.screenMouseX = this.target.getWindow().getX() + this.target.getX() + x2;
        this.screenMouseY = this.target.getWindow().getY() + this.target.getY() + y2;
        this.sceneMouseX = x2;
        this.sceneMouseY = y2;
        ScrollEvent e2 = FXRobotHelper.inputAccessor.createScrollEvent(type, 0, ((int) rotation) * 40, ScrollEvent.HorizontalTextScrollUnits.NONE, 0, ScrollEvent.VerticalTextScrollUnits.NONE, 0, (int) this.sceneMouseX, (int) this.sceneMouseY, (int) this.screenMouseX, (int) this.screenMouseY, this.isShiftDown, this.isControlDown, this.isAltDown, this.isMetaDown);
        PlatformImpl.runLater(() -> {
            out("doScrollEvent: injecting: " + ((Object) e2));
            FXRobotHelper.sceneAccessor.processScrollEvent(this.target, e2);
        });
        if (this.autoWait) {
            waitForIdle();
        }
    }
}
