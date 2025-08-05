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
import jdk.jfr.Threshold;

/* loaded from: jfxrt.jar:javafx/scene/effect/Bloom.class */
public class Bloom extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty threshold;

    public Bloom() {
    }

    public Bloom(double threshold) {
        setThreshold(threshold);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.Bloom impl_createImpl() {
        return new com.sun.scenario.effect.Bloom();
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

    public final void setThreshold(double value) {
        thresholdProperty().set(value);
    }

    public final double getThreshold() {
        if (this.threshold == null) {
            return 0.3d;
        }
        return this.threshold.get();
    }

    public final DoubleProperty thresholdProperty() {
        if (this.threshold == null) {
            this.threshold = new DoublePropertyBase(0.3d) { // from class: javafx.scene.effect.Bloom.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Bloom.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Bloom.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return Threshold.NAME;
                }
            };
        }
        return this.threshold;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localInput = getInput();
        if (localInput != null) {
            localInput.impl_sync();
        }
        com.sun.scenario.effect.Bloom peer = (com.sun.scenario.effect.Bloom) impl_getImpl();
        peer.setInput(localInput == null ? null : localInput.impl_getImpl());
        peer.setThreshold((float) Utils.clamp(0.0d, getThreshold(), 1.0d));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        return getInputBounds(bounds, tx, node, boundsAccessor, getInput());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        Bloom b2 = new Bloom(getThreshold());
        b2.setInput(getInput());
        return b2;
    }
}
