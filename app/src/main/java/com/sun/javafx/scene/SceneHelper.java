package com.sun.javafx.scene;

import com.sun.glass.ui.Accessible;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/SceneHelper.class */
public final class SceneHelper {
    private static SceneAccessor sceneAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/SceneHelper$SceneAccessor.class */
    public interface SceneAccessor {
        void setPaused(boolean z2);

        void parentEffectiveOrientationInvalidated(Scene scene);

        Camera getEffectiveCamera(Scene scene);

        Scene createPopupScene(Parent parent);

        void setTransientFocusContainer(Scene scene, Node node);

        Accessible getAccessible(Scene scene);
    }

    static {
        forceInit(Scene.class);
    }

    private SceneHelper() {
    }

    public static void setPaused(boolean paused) {
        sceneAccessor.setPaused(paused);
    }

    public static void parentEffectiveOrientationInvalidated(Scene scene) {
        sceneAccessor.parentEffectiveOrientationInvalidated(scene);
    }

    public static Camera getEffectiveCamera(Scene scene) {
        return sceneAccessor.getEffectiveCamera(scene);
    }

    public static Scene createPopupScene(Parent root) {
        return sceneAccessor.createPopupScene(root);
    }

    public static Accessible getAccessible(Scene scene) {
        return sceneAccessor.getAccessible(scene);
    }

    public static void setSceneAccessor(SceneAccessor newAccessor) {
        if (sceneAccessor != null) {
            throw new IllegalStateException();
        }
        sceneAccessor = newAccessor;
    }

    public static SceneAccessor getSceneAccessor() {
        if (sceneAccessor == null) {
            throw new IllegalStateException();
        }
        return sceneAccessor;
    }

    private static void forceInit(Class<?> classToInit) {
        try {
            Class.forName(classToInit.getName(), true, classToInit.getClassLoader());
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }
}
