package com.sun.javafx.robot;

import com.sun.javafx.robot.impl.BaseFXRobot;
import javafx.scene.Scene;

/* loaded from: jfxrt.jar:com/sun/javafx/robot/FXRobotFactory.class */
public class FXRobotFactory {
    public static FXRobot createRobot(Scene scene) {
        return new BaseFXRobot(scene);
    }
}
