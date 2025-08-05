package com.sun.javafx.scene;

import javafx.scene.Camera;
import javafx.scene.SubScene;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/SubSceneHelper.class */
public class SubSceneHelper {
    private static SubSceneAccessor subSceneAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/SubSceneHelper$SubSceneAccessor.class */
    public interface SubSceneAccessor {
        boolean isDepthBuffer(SubScene subScene);

        Camera getEffectiveCamera(SubScene subScene);
    }

    static {
        forceInit(SubScene.class);
    }

    private SubSceneHelper() {
    }

    public static boolean isDepthBuffer(SubScene subScene) {
        return subSceneAccessor.isDepthBuffer(subScene);
    }

    public static Camera getEffectiveCamera(SubScene subScene) {
        return subSceneAccessor.getEffectiveCamera(subScene);
    }

    public static void setSubSceneAccessor(SubSceneAccessor newAccessor) {
        if (subSceneAccessor != null) {
            throw new IllegalStateException();
        }
        subSceneAccessor = newAccessor;
    }

    private static void forceInit(Class<?> classToInit) {
        try {
            Class.forName(classToInit.getName(), true, classToInit.getClassLoader());
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }
}
