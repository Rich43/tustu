package javafx.animation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/RotateTransition.class */
public final class RotateTransition extends Transition {
    private static final double EPSILON = 1.0E-12d;
    private double start;
    private double delta;
    private ObjectProperty<Node> node;
    private Node cachedNode;
    private ObjectProperty<Duration> duration;
    private ObjectProperty<Point3D> axis;
    private DoubleProperty fromAngle;
    private static final double DEFAULT_FROM_ANGLE = Double.NaN;
    private DoubleProperty toAngle;
    private static final double DEFAULT_TO_ANGLE = Double.NaN;
    private DoubleProperty byAngle;
    private static final double DEFAULT_BY_ANGLE = 0.0d;
    private static final Node DEFAULT_NODE = null;
    private static final Duration DEFAULT_DURATION = Duration.millis(400.0d);
    private static final Point3D DEFAULT_AXIS = null;

    public final void setNode(Node value) {
        if (this.node != null || value != null) {
            nodeProperty().set(value);
        }
    }

    public final Node getNode() {
        return this.node == null ? DEFAULT_NODE : this.node.get();
    }

    public final ObjectProperty<Node> nodeProperty() {
        if (this.node == null) {
            this.node = new SimpleObjectProperty(this, "node", DEFAULT_NODE);
        }
        return this.node;
    }

    public final void setDuration(Duration value) {
        if (this.duration != null || !DEFAULT_DURATION.equals(value)) {
            durationProperty().set(value);
        }
    }

    public final Duration getDuration() {
        return this.duration == null ? DEFAULT_DURATION : this.duration.get();
    }

    public final ObjectProperty<Duration> durationProperty() {
        if (this.duration == null) {
            this.duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION) { // from class: javafx.animation.RotateTransition.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    try {
                        RotateTransition.this.setCycleDuration(RotateTransition.this.getDuration());
                    } catch (IllegalArgumentException e2) {
                        if (isBound()) {
                            unbind();
                        }
                        set(RotateTransition.this.getCycleDuration());
                        throw e2;
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RotateTransition.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "duration";
                }
            };
        }
        return this.duration;
    }

    public final void setAxis(Point3D value) {
        if (this.axis != null || value != null) {
            axisProperty().set(value);
        }
    }

    public final Point3D getAxis() {
        return this.axis == null ? DEFAULT_AXIS : this.axis.get();
    }

    public final ObjectProperty<Point3D> axisProperty() {
        if (this.axis == null) {
            this.axis = new SimpleObjectProperty(this, "axis", DEFAULT_AXIS);
        }
        return this.axis;
    }

    public final void setFromAngle(double value) {
        if (this.fromAngle != null || !Double.isNaN(value)) {
            fromAngleProperty().set(value);
        }
    }

    public final double getFromAngle() {
        if (this.fromAngle == null) {
            return Double.NaN;
        }
        return this.fromAngle.get();
    }

    public final DoubleProperty fromAngleProperty() {
        if (this.fromAngle == null) {
            this.fromAngle = new SimpleDoubleProperty(this, "fromAngle", Double.NaN);
        }
        return this.fromAngle;
    }

    public final void setToAngle(double value) {
        if (this.toAngle != null || !Double.isNaN(value)) {
            toAngleProperty().set(value);
        }
    }

    public final double getToAngle() {
        if (this.toAngle == null) {
            return Double.NaN;
        }
        return this.toAngle.get();
    }

    public final DoubleProperty toAngleProperty() {
        if (this.toAngle == null) {
            this.toAngle = new SimpleDoubleProperty(this, "toAngle", Double.NaN);
        }
        return this.toAngle;
    }

    public final void setByAngle(double value) {
        if (this.byAngle != null || Math.abs(value - 0.0d) > 1.0E-12d) {
            byAngleProperty().set(value);
        }
    }

    public final double getByAngle() {
        if (this.byAngle == null) {
            return 0.0d;
        }
        return this.byAngle.get();
    }

    public final DoubleProperty byAngleProperty() {
        if (this.byAngle == null) {
            this.byAngle = new SimpleDoubleProperty(this, "byAngle", 0.0d);
        }
        return this.byAngle;
    }

    public RotateTransition(Duration duration, Node node) {
        setDuration(duration);
        setNode(node);
        setCycleDuration(duration);
    }

    public RotateTransition(Duration duration) {
        this(duration, null);
    }

    public RotateTransition() {
        this(DEFAULT_DURATION, null);
    }

    @Override // javafx.animation.Transition
    protected void interpolate(double frac) {
        this.cachedNode.setRotate(this.start + (frac * this.delta));
    }

    private Node getTargetNode() {
        Node node = getNode();
        return node != null ? node : getParentTargetNode();
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    boolean impl_startable(boolean forceSync) {
        return super.impl_startable(forceSync) && !(getTargetNode() == null && (forceSync || this.cachedNode == null));
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    void impl_sync(boolean forceSync) {
        super.impl_sync(forceSync);
        if (forceSync || this.cachedNode == null) {
            this.cachedNode = getTargetNode();
            double _fromAngle = getFromAngle();
            double _toAngle = getToAngle();
            this.start = !Double.isNaN(_fromAngle) ? _fromAngle : this.cachedNode.getRotate();
            this.delta = !Double.isNaN(_toAngle) ? _toAngle - this.start : getByAngle();
            Point3D _axis = getAxis();
            if (_axis != null) {
                this.node.get().setRotationAxis(_axis);
            }
        }
    }
}
