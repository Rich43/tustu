package com.sun.javafx.scene.input;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SubSceneHelper;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.input.PickResult;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/input/PickResultChooser.class */
public class PickResultChooser {
    private Node node;
    private Point3D point;
    private Point3D normal;
    private Point2D texCoord;
    private double distance = Double.POSITIVE_INFINITY;
    private int face = -1;
    private boolean empty = true;
    private boolean closed = false;

    public static Point3D computePoint(PickRay ray, double distance) {
        Vec3d origin = ray.getOriginNoClone();
        Vec3d dir = ray.getDirectionNoClone();
        return new Point3D(origin.f11930x + (dir.f11930x * distance), origin.f11931y + (dir.f11931y * distance), origin.f11932z + (dir.f11932z * distance));
    }

    public PickResult toPickResult() {
        if (this.empty) {
            return null;
        }
        return new PickResult(this.node, this.point, this.distance, this.face, this.normal, this.texCoord);
    }

    public boolean isCloser(double distance) {
        return distance < this.distance || this.empty;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean offer(Node node, double distance, int face, Point3D point, Point2D texCoord) {
        return processOffer(node, node, distance, point, face, this.normal, texCoord);
    }

    public boolean offer(Node node, double distance, Point3D point) {
        return processOffer(node, node, distance, point, -1, null, null);
    }

    public boolean offerSubScenePickResult(SubScene subScene, PickResult pickResult, double distance) {
        if (pickResult == null) {
            return false;
        }
        return processOffer(pickResult.getIntersectedNode(), subScene, distance, pickResult.getIntersectedPoint(), pickResult.getIntersectedFace(), pickResult.getIntersectedNormal(), pickResult.getIntersectedTexCoord());
    }

    private boolean processOffer(Node node, Node depthTestNode, double distance, Point3D point, int face, Point3D normal, Point2D texCoord) {
        boolean zIsDepthBuffer;
        SubScene subScene = NodeHelper.getSubScene(depthTestNode);
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            zIsDepthBuffer = false;
        } else if (subScene != null) {
            zIsDepthBuffer = SubSceneHelper.isDepthBuffer(subScene);
        } else {
            zIsDepthBuffer = depthTestNode.getScene().isDepthBuffer();
        }
        boolean hasDepthBuffer = zIsDepthBuffer;
        boolean hasDepthTest = hasDepthBuffer && NodeHelper.isDerivedDepthTest(depthTestNode);
        boolean accepted = false;
        if ((this.empty || (hasDepthTest && distance < this.distance)) && !this.closed) {
            this.node = node;
            this.distance = distance;
            this.face = face;
            this.point = point;
            this.normal = normal;
            this.texCoord = texCoord;
            this.empty = false;
            accepted = true;
        }
        if (!hasDepthTest) {
            this.closed = true;
        }
        return accepted;
    }

    public final Node getIntersectedNode() {
        return this.node;
    }

    public final double getIntersectedDistance() {
        return this.distance;
    }

    public final int getIntersectedFace() {
        return this.face;
    }

    public final Point3D getIntersectedPoint() {
        return this.point;
    }

    public final Point3D getIntersectedNormal() {
        return this.normal;
    }

    public final Point2D getIntersectedTexCoord() {
        return this.texCoord;
    }
}
