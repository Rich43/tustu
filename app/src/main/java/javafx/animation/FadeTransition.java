package javafx.animation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/FadeTransition.class */
public final class FadeTransition extends Transition {
    private static final double EPSILON = 1.0E-12d;
    private double start;
    private double delta;
    private ObjectProperty<Node> node;
    private Node cachedNode;
    private ObjectProperty<Duration> duration;
    private DoubleProperty fromValue;
    private static final double DEFAULT_FROM_VALUE = Double.NaN;
    private DoubleProperty toValue;
    private static final double DEFAULT_TO_VALUE = Double.NaN;
    private DoubleProperty byValue;
    private static final double DEFAULT_BY_VALUE = 0.0d;
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
            this.duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION) { // from class: javafx.animation.FadeTransition.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    try {
                        FadeTransition.this.setCycleDuration(FadeTransition.this.getDuration());
                    } catch (IllegalArgumentException e2) {
                        if (isBound()) {
                            unbind();
                        }
                        set(FadeTransition.this.getCycleDuration());
                        throw e2;
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FadeTransition.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "duration";
                }
            };
        }
        return this.duration;
    }

    public final void setFromValue(double value) {
        if (this.fromValue != null || !Double.isNaN(value)) {
            fromValueProperty().set(value);
        }
    }

    public final double getFromValue() {
        if (this.fromValue == null) {
            return Double.NaN;
        }
        return this.fromValue.get();
    }

    public final DoubleProperty fromValueProperty() {
        if (this.fromValue == null) {
            this.fromValue = new SimpleDoubleProperty(this, "fromValue", Double.NaN);
        }
        return this.fromValue;
    }

    public final void setToValue(double value) {
        if (this.toValue != null || !Double.isNaN(value)) {
            toValueProperty().set(value);
        }
    }

    public final double getToValue() {
        if (this.toValue == null) {
            return Double.NaN;
        }
        return this.toValue.get();
    }

    public final DoubleProperty toValueProperty() {
        if (this.toValue == null) {
            this.toValue = new SimpleDoubleProperty(this, "toValue", Double.NaN);
        }
        return this.toValue;
    }

    public final void setByValue(double value) {
        if (this.byValue != null || Math.abs(value - 0.0d) > 1.0E-12d) {
            byValueProperty().set(value);
        }
    }

    public final double getByValue() {
        if (this.byValue == null) {
            return 0.0d;
        }
        return this.byValue.get();
    }

    public final DoubleProperty byValueProperty() {
        if (this.byValue == null) {
            this.byValue = new SimpleDoubleProperty(this, "byValue", 0.0d);
        }
        return this.byValue;
    }

    public FadeTransition(Duration duration, Node node) {
        setDuration(duration);
        setNode(node);
        setCycleDuration(duration);
    }

    public FadeTransition(Duration duration) {
        this(duration, null);
    }

    public FadeTransition() {
        this(DEFAULT_DURATION, null);
    }

    @Override // javafx.animation.Transition
    protected void interpolate(double frac) {
        double newOpacity = Math.max(0.0d, Math.min(this.start + (frac * this.delta), 1.0d));
        this.cachedNode.setOpacity(newOpacity);
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
        double opacity;
        super.impl_sync(forceSync);
        if (forceSync || this.cachedNode == null) {
            this.cachedNode = getTargetNode();
            double _fromValue = getFromValue();
            double _toValue = getToValue();
            if (!Double.isNaN(_fromValue)) {
                opacity = Math.max(0.0d, Math.min(_fromValue, 1.0d));
            } else {
                opacity = this.cachedNode.getOpacity();
            }
            this.start = opacity;
            this.delta = !Double.isNaN(_toValue) ? _toValue - this.start : getByValue();
            if (this.start + this.delta > 1.0d) {
                this.delta = 1.0d - this.start;
            } else if (this.start + this.delta < 0.0d) {
                this.delta = -this.start;
            }
        }
    }
}
