package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.util.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

/* loaded from: jfxrt.jar:javafx/scene/effect/MotionBlur.class */
public class MotionBlur extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty radius;
    private DoubleProperty angle;

    public MotionBlur() {
    }

    public MotionBlur(double angle, double radius) {
        setAngle(angle);
        setRadius(radius);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.MotionBlur impl_createImpl() {
        return new com.sun.scenario.effect.MotionBlur();
    }

    public final void setInput(Effect value) {
        inputProperty().set(value);
    }

    public final Effect getInput() {
        if (this.input == null) {
            return null;
        }
        return this.input.get();
    }

    public final ObjectProperty<Effect> inputProperty() {
        if (this.input == null) {
            this.input = new Effect.EffectInputProperty("input");
        }
        return this.input;
    }

    @Override // javafx.scene.effect.Effect
    boolean impl_checkChainContains(Effect e2) {
        Effect localInput = getInput();
        if (localInput == null) {
            return false;
        }
        if (localInput == e2) {
            return true;
        }
        return localInput.impl_checkChainContains(e2);
    }

    public final void setRadius(double value) {
        radiusProperty().set(value);
    }

    public final double getRadius() {
        if (this.radius == null) {
            return 10.0d;
        }
        return this.radius.get();
    }

    public final DoubleProperty radiusProperty() {
        if (this.radius == null) {
            this.radius = new DoublePropertyBase(10.0d) { // from class: javafx.scene.effect.MotionBlur.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    MotionBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    MotionBlur.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MotionBlur.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "radius";
                }
            };
        }
        return this.radius;
    }

    public final void setAngle(double value) {
        angleProperty().set(value);
    }

    public final double getAngle() {
        if (this.angle == null) {
            return 0.0d;
        }
        return this.angle.get();
    }

    public final DoubleProperty angleProperty() {
        if (this.angle == null) {
            this.angle = new DoublePropertyBase() { // from class: javafx.scene.effect.MotionBlur.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    MotionBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    MotionBlur.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MotionBlur.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "angle";
                }
            };
        }
        return this.angle;
    }

    private float getClampedRadius() {
        return (float) Utils.clamp(0.0d, getRadius(), 63.0d);
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localInput = getInput();
        if (localInput != null) {
            localInput.impl_sync();
        }
        com.sun.scenario.effect.MotionBlur peer = (com.sun.scenario.effect.MotionBlur) impl_getImpl();
        peer.setInput(localInput == null ? null : localInput.impl_getImpl());
        peer.setRadius(getClampedRadius());
        peer.setAngle((float) Math.toRadians(getAngle()));
    }

    private int getHPad() {
        return (int) Math.ceil(Math.abs(Math.cos(Math.toRadians(getAngle()))) * getClampedRadius());
    }

    private int getVPad() {
        return (int) Math.ceil(Math.abs(Math.sin(Math.toRadians(getAngle()))) * getClampedRadius());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        BaseBounds bounds2 = getInputBounds(bounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, getInput());
        int hpad = getHPad();
        int vpad = getVPad();
        return transformBounds(tx, bounds2.deriveWithPadding(hpad, vpad, 0.0f));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        MotionBlur mb = new MotionBlur(getAngle(), getRadius());
        mb.setInput(mb.getInput());
        return mb;
    }
}
