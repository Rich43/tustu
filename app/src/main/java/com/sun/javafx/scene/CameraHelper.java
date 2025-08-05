package com.sun.javafx.scene;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/CameraHelper.class */
public class CameraHelper {
    private static CameraAccessor cameraAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/CameraHelper$CameraAccessor.class */
    public interface CameraAccessor {
        Point2D project(Camera camera, Point3D point3D);

        Point2D pickNodeXYPlane(Camera camera, Node node, double d2, double d3);

        Point3D pickProjectPlane(Camera camera, double d2, double d3);
    }

    static {
        forceInit(Camera.class);
    }

    private CameraHelper() {
    }

    public static Point2D project(Camera camera, Point3D p2) {
        return cameraAccessor.project(camera, p2);
    }

    public static Point2D pickNodeXYPlane(Camera camera, Node node, double x2, double y2) {
        return cameraAccessor.pickNodeXYPlane(camera, node, x2, y2);
    }

    public static Point3D pickProjectPlane(Camera camera, double x2, double y2) {
        return cameraAccessor.pickProjectPlane(camera, x2, y2);
    }

    public static void setCameraAccessor(CameraAccessor newAccessor) {
        if (cameraAccessor != null) {
            throw new IllegalStateException();
        }
        cameraAccessor = newAccessor;
    }

    private static void forceInit(Class<?> classToInit) {
        try {
            Class.forName(classToInit.getName(), true, classToInit.getClassLoader());
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }
}
