package com.sun.javafx.scene.input;

import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.SceneUtils;
import com.sun.javafx.scene.SubSceneHelper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.input.PickResult;
import javafx.scene.input.TransferMode;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/input/InputEventUtils.class */
public class InputEventUtils {
    private static final List<TransferMode> TM_ANY = Collections.unmodifiableList(Arrays.asList(TransferMode.COPY, TransferMode.MOVE, TransferMode.LINK));
    private static final List<TransferMode> TM_COPY_OR_MOVE = Collections.unmodifiableList(Arrays.asList(TransferMode.COPY, TransferMode.MOVE));

    public static Point3D recomputeCoordinates(PickResult result, Object newSource) {
        Point3D coordinates = result.getIntersectedPoint();
        if (coordinates == null) {
            return new Point3D(Double.NaN, Double.NaN, Double.NaN);
        }
        Node oldSourceNode = result.getIntersectedNode();
        Node newSourceNode = newSource instanceof Node ? (Node) newSource : null;
        SubScene oldSubScene = oldSourceNode == null ? null : NodeHelper.getSubScene(oldSourceNode);
        SubScene newSubScene = newSourceNode == null ? null : NodeHelper.getSubScene(newSourceNode);
        boolean subScenesDiffer = oldSubScene != newSubScene;
        if (oldSourceNode != null) {
            coordinates = oldSourceNode.localToScene(coordinates);
            if (subScenesDiffer && oldSubScene != null) {
                coordinates = SceneUtils.subSceneToScene(oldSubScene, coordinates);
            }
        }
        if (newSourceNode != null) {
            if (subScenesDiffer && newSubScene != null) {
                Point2D planeCoords = SceneUtils.sceneToSubScenePlane(newSubScene, CameraHelper.project(SceneHelper.getEffectiveCamera(newSourceNode.getScene()), coordinates));
                if (planeCoords == null) {
                    coordinates = null;
                } else {
                    coordinates = CameraHelper.pickProjectPlane(SubSceneHelper.getEffectiveCamera(newSubScene), planeCoords.getX(), planeCoords.getY());
                }
            }
            if (coordinates != null) {
                coordinates = newSourceNode.sceneToLocal(coordinates);
            }
            if (coordinates == null) {
                coordinates = new Point3D(Double.NaN, Double.NaN, Double.NaN);
            }
        }
        return coordinates;
    }

    public static List<TransferMode> safeTransferModes(TransferMode[] modes) {
        if (modes == TransferMode.ANY) {
            return TM_ANY;
        }
        if (modes == TransferMode.COPY_OR_MOVE) {
            return TM_COPY_OR_MOVE;
        }
        return Arrays.asList(modes);
    }
}
