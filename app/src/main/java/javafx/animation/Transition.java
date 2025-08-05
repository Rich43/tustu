package javafx.animation;

import com.sun.scenario.animation.AbstractMasterTimer;
import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/animation/Transition.class */
public abstract class Transition extends Animation {
    private ObjectProperty<Interpolator> interpolator;
    private static final Interpolator DEFAULT_INTERPOLATOR = Interpolator.EASE_BOTH;
    private Interpolator cachedInterpolator;

    protected abstract void interpolate(double d2);

    public final void setInterpolator(Interpolator value) {
        if (this.interpolator != null || !DEFAULT_INTERPOLATOR.equals(value)) {
            interpolatorProperty().set(value);
        }
    }

    public final Interpolator getInterpolator() {
        return this.interpolator == null ? DEFAULT_INTERPOLATOR : this.interpolator.get();
    }

    public final ObjectProperty<Interpolator> interpolatorProperty() {
        if (this.interpolator == null) {
            this.interpolator = new SimpleObjectProperty(this, "interpolator", DEFAULT_INTERPOLATOR);
        }
        return this.interpolator;
    }

    protected Interpolator getCachedInterpolator() {
        return this.cachedInterpolator;
    }

    public Transition(double targetFramerate) {
        super(targetFramerate);
    }

    public Transition() {
    }

    Transition(AbstractMasterTimer timer) {
        super(timer);
    }

    protected Node getParentTargetNode() {
        if (this.parent == null || !(this.parent instanceof Transition)) {
            return null;
        }
        return ((Transition) this.parent).getParentTargetNode();
    }

    private double calculateFraction(long currentTicks, long cycleTicks) {
        double frac = cycleTicks <= 0 ? 1.0d : currentTicks / cycleTicks;
        return this.cachedInterpolator.interpolate(0.0d, 1.0d, frac);
    }

    @Override // javafx.animation.Animation
    boolean impl_startable(boolean forceSync) {
        return super.impl_startable(forceSync) && !(getInterpolator() == null && (forceSync || this.cachedInterpolator == null));
    }

    @Override // javafx.animation.Animation
    void impl_sync(boolean forceSync) {
        super.impl_sync(forceSync);
        if (forceSync || this.cachedInterpolator == null) {
            this.cachedInterpolator = getInterpolator();
        }
    }

    @Override // javafx.animation.Animation
    void impl_playTo(long currentTicks, long cycleTicks) {
        impl_setCurrentTicks(currentTicks);
        interpolate(calculateFraction(currentTicks, cycleTicks));
    }

    @Override // javafx.animation.Animation
    void impl_jumpTo(long currentTicks, long cycleTicks, boolean forceJump) {
        impl_setCurrentTicks(currentTicks);
        if (getStatus() != Animation.Status.STOPPED || forceJump) {
            impl_sync(false);
            interpolate(calculateFraction(currentTicks, cycleTicks));
        }
    }
}
