package javafx.animation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/TranslateTransition.class */
public final class TranslateTransition extends Transition {
    private static final double EPSILON = 1.0E-12d;
    private double startX;
    private double startY;
    private double startZ;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private ObjectProperty<Node> node;
    private Node cachedNode;
    private ObjectProperty<Duration> duration;
    private DoubleProperty fromX;
    private static final double DEFAULT_FROM_X = Double.NaN;
    private DoubleProperty fromY;
    private static final double DEFAULT_FROM_Y = Double.NaN;
    private DoubleProperty fromZ;
    private static final double DEFAULT_FROM_Z = Double.NaN;
    private DoubleProperty toX;
    private static final double DEFAULT_TO_X = Double.NaN;
    private DoubleProperty toY;
    private static final double DEFAULT_TO_Y = Double.NaN;
    private DoubleProperty toZ;
    private static final double DEFAULT_TO_Z = Double.NaN;
    private DoubleProperty byX;
    private static final double DEFAULT_BY_X = 0.0d;
    private DoubleProperty byY;
    private static final double DEFAULT_BY_Y = 0.0d;
    private DoubleProperty byZ;
    private static final double DEFAULT_BY_Z = 0.0d;
    private static final Node DEFAULT_NODE = null;
    private static final Duration DEFAULT_DURATION = Duration.millis(400.0d);

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
            this.duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION) { // from class: javafx.animation.TranslateTransition.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    try {
                        TranslateTransition.this.setCycleDuration(TranslateTransition.this.getDuration());
                    } catch (IllegalArgumentException e2) {
                        if (isBound()) {
                            unbind();
                        }
                        set(TranslateTransition.this.getCycleDuration());
                        throw e2;
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TranslateTransition.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "duration";
                }
            };
        }
        return this.duration;
    }

    public final void setFromX(double value) {
        if (this.fromX != null || !Double.isNaN(value)) {
            fromXProperty().set(value);
        }
    }

    public final double getFromX() {
        if (this.fromX == null) {
            return Double.NaN;
        }
        return this.fromX.get();
    }

    public final DoubleProperty fromXProperty() {
        if (this.fromX == null) {
            this.fromX = new SimpleDoubleProperty(this, "fromX", Double.NaN);
        }
        return this.fromX;
    }

    public final void setFromY(double value) {
        if (this.fromY != null || !Double.isNaN(value)) {
            fromYProperty().set(value);
        }
    }

    public final double getFromY() {
        if (this.fromY == null) {
            return Double.NaN;
        }
        return this.fromY.get();
    }

    public final DoubleProperty fromYProperty() {
        if (this.fromY == null) {
            this.fromY = new SimpleDoubleProperty(this, "fromY", Double.NaN);
        }
        return this.fromY;
    }

    public final void setFromZ(double value) {
        if (this.fromZ != null || !Double.isNaN(value)) {
            fromZProperty().set(value);
        }
    }

    public final double getFromZ() {
        if (this.fromZ == null) {
            return Double.NaN;
        }
        return this.fromZ.get();
    }

    public final DoubleProperty fromZProperty() {
        if (this.fromZ == null) {
            this.fromZ = new SimpleDoubleProperty(this, "fromZ", Double.NaN);
        }
        return this.fromZ;
    }

    public final void setToX(double value) {
        if (this.toX != null || !Double.isNaN(value)) {
            toXProperty().set(value);
        }
    }

    public final double getToX() {
        if (this.toX == null) {
            return Double.NaN;
        }
        return this.toX.get();
    }

    public final DoubleProperty toXProperty() {
        if (this.toX == null) {
            this.toX = new SimpleDoubleProperty(this, "toX", Double.NaN);
        }
        return this.toX;
    }

    public final void setToY(double value) {
        if (this.toY != null || !Double.isNaN(value)) {
            toYProperty().set(value);
        }
    }

    public final double getToY() {
        if (this.toY == null) {
            return Double.NaN;
        }
        return this.toY.get();
    }

    public final DoubleProperty toYProperty() {
        if (this.toY == null) {
            this.toY = new SimpleDoubleProperty(this, "toY", Double.NaN);
        }
        return this.toY;
    }

    public final void setToZ(double value) {
        if (this.toZ != null || !Double.isNaN(value)) {
            toZProperty().set(value);
        }
    }

    public final double getToZ() {
        if (this.toZ == null) {
            return Double.NaN;
        }
        return this.toZ.get();
    }

    public final DoubleProperty toZProperty() {
        if (this.toZ == null) {
            this.toZ = new SimpleDoubleProperty(this, "toZ", Double.NaN);
        }
        return this.toZ;
    }

    public final void setByX(double value) {
        if (this.byX != null || Math.abs(value - 0.0d) > 1.0E-12d) {
            byXProperty().set(value);
        }
    }

    public final double getByX() {
        if (this.byX == null) {
            return 0.0d;
        }
        return this.byX.get();
    }

    public final DoubleProperty byXProperty() {
        if (this.byX == null) {
            this.byX = new SimpleDoubleProperty(this, "byX", 0.0d);
        }
        return this.byX;
    }

    public final void setByY(double value) {
        if (this.byY != null || Math.abs(value - 0.0d) > 1.0E-12d) {
            byYProperty().set(value);
        }
    }

    public final double getByY() {
        if (this.byY == null) {
            return 0.0d;
        }
        return this.byY.get();
    }

    public final DoubleProperty byYProperty() {
        if (this.byY == null) {
            this.byY = new SimpleDoubleProperty(this, "byY", 0.0d);
        }
        return this.byY;
    }

    public final void setByZ(double value) {
        if (this.byZ != null || Math.abs(value - 0.0d) > 1.0E-12d) {
            byZProperty().set(value);
        }
    }

    public final double getByZ() {
        if (this.byZ == null) {
            return 0.0d;
        }
        return this.byZ.get();
    }

    public final DoubleProperty byZProperty() {
        if (this.byZ == null) {
            this.byZ = new SimpleDoubleProperty(this, "byZ", 0.0d);
        }
        return this.byZ;
    }

    public TranslateTransition(Duration duration, Node node) {
        setDuration(duration);
        setNode(node);
        setCycleDuration(duration);
    }

    public TranslateTransition(Duration duration) {
        this(duration, null);
    }

    public TranslateTransition() {
        this(DEFAULT_DURATION, null);
    }

    @Override // javafx.animation.Transition
    public void interpolate(double frac) {
        if (!Double.isNaN(this.startX)) {
            this.cachedNode.setTranslateX(this.startX + (frac * this.deltaX));
        }
        if (!Double.isNaN(this.startY)) {
            this.cachedNode.setTranslateY(this.startY + (frac * this.deltaY));
        }
        if (!Double.isNaN(this.startZ)) {
            this.cachedNode.setTranslateZ(this.startZ + (frac * this.deltaZ));
        }
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
            double _fromX = getFromX();
            double _fromY = getFromY();
            double _fromZ = getFromZ();
            double _toX = getToX();
            double _toY = getToY();
            double _toZ = getToZ();
            double _byX = getByX();
            double _byY = getByY();
            double _byZ = getByZ();
            if (Double.isNaN(_fromX) && Double.isNaN(_toX) && Math.abs(_byX) < 1.0E-12d) {
                this.startX = Double.NaN;
            } else {
                this.startX = !Double.isNaN(_fromX) ? _fromX : this.cachedNode.getTranslateX();
                this.deltaX = !Double.isNaN(_toX) ? _toX - this.startX : _byX;
            }
            if (Double.isNaN(_fromY) && Double.isNaN(_toY) && Math.abs(_byY) < 1.0E-12d) {
                this.startY = Double.NaN;
            } else {
                this.startY = !Double.isNaN(_fromY) ? _fromY : this.cachedNode.getTranslateY();
                this.deltaY = !Double.isNaN(_toY) ? _toY - this.startY : getByY();
            }
            if (Double.isNaN(_fromZ) && Double.isNaN(_toZ) && Math.abs(_byZ) < 1.0E-12d) {
                this.startZ = Double.NaN;
            } else {
                this.startZ = !Double.isNaN(_fromZ) ? _fromZ : this.cachedNode.getTranslateZ();
                this.deltaZ = !Double.isNaN(_toZ) ? _toZ - this.startZ : getByZ();
            }
        }
    }
}
