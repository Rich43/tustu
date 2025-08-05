package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import sun.security.x509.IssuingDistributionPointExtension;

/* loaded from: jfxrt.jar:javafx/scene/input/PickResult.class */
public class PickResult {
    public static final int FACE_UNDEFINED = -1;
    private Node node;
    private Point3D point;
    private double distance;
    private int face;
    private Point3D normal;
    private Point2D texCoord;

    public PickResult(@NamedArg("node") Node node, @NamedArg(IssuingDistributionPointExtension.POINT) Point3D point, @NamedArg("distance") double distance, @NamedArg("face") int face, @NamedArg("texCoord") Point2D texCoord) {
        this.distance = Double.POSITIVE_INFINITY;
        this.face = -1;
        this.node = node;
        this.point = point;
        this.distance = distance;
        this.face = face;
        this.normal = null;
        this.texCoord = texCoord;
    }

    public PickResult(@NamedArg("node") Node node, @NamedArg(IssuingDistributionPointExtension.POINT) Point3D point, @NamedArg("distance") double distance, @NamedArg("face") int face, @NamedArg("normal") Point3D normal, @NamedArg("texCoord") Point2D texCoord) {
        this.distance = Double.POSITIVE_INFINITY;
        this.face = -1;
        this.node = node;
        this.point = point;
        this.distance = distance;
        this.face = face;
        this.normal = normal;
        this.texCoord = texCoord;
    }

    public PickResult(@NamedArg("node") Node node, @NamedArg(IssuingDistributionPointExtension.POINT) Point3D point, @NamedArg("distance") double distance) {
        this.distance = Double.POSITIVE_INFINITY;
        this.face = -1;
        this.node = node;
        this.point = point;
        this.distance = distance;
        this.face = -1;
        this.normal = null;
        this.texCoord = null;
    }

    public PickResult(@NamedArg("target") EventTarget target, @NamedArg("sceneX") double sceneX, @NamedArg("sceneY") double sceneY) {
        this(target instanceof Node ? (Node) target : null, target instanceof Node ? ((Node) target).sceneToLocal(sceneX, sceneY, 0.0d) : new Point3D(sceneX, sceneY, 0.0d), 1.0d);
    }

    public final Node getIntersectedNode() {
        return this.node;
    }

    public final Point3D getIntersectedPoint() {
        return this.point;
    }

    public final double getIntersectedDistance() {
        return this.distance;
    }

    public final int getIntersectedFace() {
        return this.face;
    }

    public final Point3D getIntersectedNormal() {
        return this.normal;
    }

    public final Point2D getIntersectedTexCoord() {
        return this.texCoord;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PickResult [");
        sb.append("node = ").append((Object) getIntersectedNode()).append(", point = ").append((Object) getIntersectedPoint()).append(", distance = ").append(getIntersectedDistance());
        if (getIntersectedFace() != -1) {
            sb.append(", face = ").append(getIntersectedFace());
        }
        if (getIntersectedNormal() != null) {
            sb.append(", normal = ").append((Object) getIntersectedNormal());
        }
        if (getIntersectedTexCoord() != null) {
            sb.append(", texCoord = ").append((Object) getIntersectedTexCoord());
        }
        return sb.toString();
    }
}
