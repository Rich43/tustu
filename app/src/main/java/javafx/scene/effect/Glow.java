package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.util.Utils;
import com.sun.org.apache.xalan.internal.templates.Constants;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

/* loaded from: jfxrt.jar:javafx/scene/effect/Glow.class */
public class Glow extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty level;

    public Glow() {
    }

    public Glow(double level) {
        setLevel(level);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.Glow impl_createImpl() {
        return new com.sun.scenario.effect.Glow();
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

    public final void setLevel(double value) {
        levelProperty().set(value);
    }

    public final double getLevel() {
        if (this.level == null) {
            return 0.3d;
        }
        return this.level.get();
    }

    public final DoubleProperty levelProperty() {
        if (this.level == null) {
            this.level = new DoublePropertyBase(0.3d) { // from class: javafx.scene.effect.Glow.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Glow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Glow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return Constants.ATTRNAME_LEVEL;
                }
            };
        }
        return this.level;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localInput = getInput();
        if (localInput != null) {
            localInput.impl_sync();
        }
        com.sun.scenario.effect.Glow peer = (com.sun.scenario.effect.Glow) impl_getImpl();
        peer.setInput(localInput == null ? null : localInput.impl_getImpl());
        peer.setLevel((float) Utils.clamp(0.0d, getLevel(), 1.0d));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        return getInputBounds(bounds, tx, node, boundsAccessor, getInput());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        return new Glow(getLevel());
    }
}
