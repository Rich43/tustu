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

/* loaded from: jfxrt.jar:javafx/scene/effect/GaussianBlur.class */
public class GaussianBlur extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty radius;

    public GaussianBlur() {
    }

    public GaussianBlur(double radius) {
        setRadius(radius);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.GaussianBlur impl_createImpl() {
        return new com.sun.scenario.effect.GaussianBlur();
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
            this.input = new Effect.EffectInputProperty(this, "input");
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
            this.radius = new DoublePropertyBase(10.0d) { // from class: javafx.scene.effect.GaussianBlur.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    GaussianBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    GaussianBlur.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return GaussianBlur.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "radius";
                }
            };
        }
        return this.radius;
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
        com.sun.scenario.effect.GaussianBlur peer = (com.sun.scenario.effect.GaussianBlur) impl_getImpl();
        peer.setRadius(getClampedRadius());
        peer.setInput(localInput == null ? null : localInput.impl_getImpl());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        BaseBounds bounds2 = getInputBounds(bounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, getInput());
        float r2 = getClampedRadius();
        return transformBounds(tx, bounds2.deriveWithPadding(r2, r2, 0.0f));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        return new GaussianBlur(getRadius());
    }
}
