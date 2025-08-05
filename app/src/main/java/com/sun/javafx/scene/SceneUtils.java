package com.sun.javafx.scene;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/SceneUtils.class */
public class SceneUtils {
    public static Point3D subSceneToScene(SubScene subScene, Point3D point) {
        Node subScene2 = subScene;
        while (true) {
            Node n2 = subScene2;
            if (n2 != null) {
                Point2D projection = CameraHelper.project(SubSceneHelper.getEffectiveCamera(subScene), point);
                point = n2.localToScene(projection.getX(), projection.getY(), 0.0d);
                subScene2 = NodeHelper.getSubScene(n2);
            } else {
                return point;
            }
        }
    }

    public static Point2D sceneToSubScenePlane(SubScene subScene, Point2D point) {
        return computeSubSceneCoordinates(point.getX(), point.getY(), subScene);
    }

    private static Point2D computeSubSceneCoordinates(double x2, double y2, SubScene subScene) {
        SubScene outer = NodeHelper.getSubScene(subScene);
        if (outer == null) {
            return CameraHelper.pickNodeXYPlane(SceneHelper.getEffectiveCamera(subScene.getScene()), subScene, x2, y2);
        }
        Point2D coords = computeSubSceneCoordinates(x2, y2, outer);
        if (coords != null) {
            coords = CameraHelper.pickNodeXYPlane(SubSceneHelper.getEffectiveCamera(outer), subScene, coords.getX(), coords.getY());
        }
        return coords;
    }
}
