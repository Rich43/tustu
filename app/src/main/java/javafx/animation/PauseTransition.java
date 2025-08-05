package javafx.animation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/PauseTransition.class */
public final class PauseTransition extends Transition {
    private ObjectProperty<Duration> duration;
    private static final Duration DEFAULT_DURATION = Duration.millis(400.0d);

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
            this.duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION) { // from class: javafx.animation.PauseTransition.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    try {
                        PauseTransition.this.setCycleDuration(PauseTransition.this.getDuration());
                    } catch (IllegalArgumentException e2) {
                        if (isBound()) {
                            unbind();
                        }
                        set(PauseTransition.this.getCycleDuration());
                        throw e2;
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PauseTransition.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "duration";
                }
            };
        }
        return this.duration;
    }

    public PauseTransition(Duration duration) {
        setDuration(duration);
        setCycleDuration(duration);
    }

    public PauseTransition() {
        this(DEFAULT_DURATION);
    }

    @Override // javafx.animation.Transition
    public void interpolate(double frac) {
    }
}
