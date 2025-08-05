package javafx.animation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/StrokeTransition.class */
public final class StrokeTransition extends Transition {
    private Color start;
    private Color end;
    private ObjectProperty<Shape> shape;
    private Shape cachedShape;
    private ObjectProperty<Duration> duration;
    private ObjectProperty<Color> fromValue;
    private ObjectProperty<Color> toValue;
    private static final Shape DEFAULT_SHAPE = null;
    private static final Duration DEFAULT_DURATION = Duration.millis(400.0d);
    private static final Color DEFAULT_FROM_VALUE = null;
    private static final Color DEFAULT_TO_VALUE = null;

    public final void setShape(Shape value) {
        if (this.shape != null || value != null) {
            shapeProperty().set(value);
        }
    }

    public final Shape getShape() {
        return this.shape == null ? DEFAULT_SHAPE : this.shape.get();
    }

    public final ObjectProperty<Shape> shapeProperty() {
        if (this.shape == null) {
            this.shape = new SimpleObjectProperty(this, "shape", DEFAULT_SHAPE);
        }
        return this.shape;
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
            this.duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION) { // from class: javafx.animation.StrokeTransition.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    try {
                        StrokeTransition.this.setCycleDuration(StrokeTransition.this.getDuration());
                    } catch (IllegalArgumentException e2) {
                        if (isBound()) {
                            unbind();
                        }
                        set(StrokeTransition.this.getCycleDuration());
                        throw e2;
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return StrokeTransition.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "duration";
                }
            };
        }
        return this.duration;
    }

    public final void setFromValue(Color value) {
        if (this.fromValue != null || value != null) {
            fromValueProperty().set(value);
        }
    }

    public final Color getFromValue() {
        return this.fromValue == null ? DEFAULT_FROM_VALUE : this.fromValue.get();
    }

    public final ObjectProperty<Color> fromValueProperty() {
        if (this.fromValue == null) {
            this.fromValue = new SimpleObjectProperty(this, "fromValue", DEFAULT_FROM_VALUE);
        }
        return this.fromValue;
    }

    public final void setToValue(Color value) {
        if (this.toValue != null || value != null) {
            toValueProperty().set(value);
        }
    }

    public final Color getToValue() {
        return this.toValue == null ? DEFAULT_TO_VALUE : this.toValue.get();
    }

    public final ObjectProperty<Color> toValueProperty() {
        if (this.toValue == null) {
            this.toValue = new SimpleObjectProperty(this, "toValue", DEFAULT_TO_VALUE);
        }
        return this.toValue;
    }

    public StrokeTransition(Duration duration, Shape shape, Color fromValue, Color toValue) {
        setDuration(duration);
        setShape(shape);
        setFromValue(fromValue);
        setToValue(toValue);
        setCycleDuration(duration);
    }

    public StrokeTransition(Duration duration, Color fromValue, Color toValue) {
        this(duration, null, fromValue, toValue);
    }

    public StrokeTransition(Duration duration, Shape shape) {
        this(duration, shape, null, null);
    }

    public StrokeTransition(Duration duration) {
        this(duration, null);
    }

    public StrokeTransition() {
        this(DEFAULT_DURATION, null);
    }

    @Override // javafx.animation.Transition
    protected void interpolate(double frac) {
        Color newColor = this.start.interpolate(this.end, frac);
        this.cachedShape.setStroke(newColor);
    }

    private Shape getTargetShape() {
        Shape shape = getShape();
        if (shape == null) {
            Node node = getParentTargetNode();
            if (node instanceof Shape) {
                shape = (Shape) node;
            }
        }
        return shape;
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    boolean impl_startable(boolean forceSync) {
        if (!super.impl_startable(forceSync)) {
            return false;
        }
        if (!forceSync && this.cachedShape != null) {
            return true;
        }
        Shape shape = getTargetShape();
        return shape != null && (getFromValue() != null || (shape.getStroke() instanceof Color)) && getToValue() != null;
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    void impl_sync(boolean forceSync) {
        super.impl_sync(forceSync);
        if (forceSync || this.cachedShape == null) {
            this.cachedShape = getTargetShape();
            Color _fromValue = getFromValue();
            this.start = _fromValue != null ? _fromValue : (Color) this.cachedShape.getStroke();
            this.end = getToValue();
        }
    }
}
