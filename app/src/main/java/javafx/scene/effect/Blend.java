package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.util.Utils;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.scenario.effect.Blend;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

/* loaded from: jfxrt.jar:javafx/scene/effect/Blend.class */
public class Blend extends Effect {
    private ObjectProperty<BlendMode> mode;
    private DoubleProperty opacity;
    private ObjectProperty<Effect> bottomInput;
    private ObjectProperty<Effect> topInput;

    private static Blend.Mode toPGMode(BlendMode mode) {
        if (mode == null) {
            return Blend.Mode.SRC_OVER;
        }
        if (mode == BlendMode.SRC_OVER) {
            return Blend.Mode.SRC_OVER;
        }
        if (mode == BlendMode.SRC_ATOP) {
            return Blend.Mode.SRC_ATOP;
        }
        if (mode == BlendMode.ADD) {
            return Blend.Mode.ADD;
        }
        if (mode == BlendMode.MULTIPLY) {
            return Blend.Mode.MULTIPLY;
        }
        if (mode == BlendMode.SCREEN) {
            return Blend.Mode.SCREEN;
        }
        if (mode == BlendMode.OVERLAY) {
            return Blend.Mode.OVERLAY;
        }
        if (mode == BlendMode.DARKEN) {
            return Blend.Mode.DARKEN;
        }
        if (mode == BlendMode.LIGHTEN) {
            return Blend.Mode.LIGHTEN;
        }
        if (mode == BlendMode.COLOR_DODGE) {
            return Blend.Mode.COLOR_DODGE;
        }
        if (mode == BlendMode.COLOR_BURN) {
            return Blend.Mode.COLOR_BURN;
        }
        if (mode == BlendMode.HARD_LIGHT) {
            return Blend.Mode.HARD_LIGHT;
        }
        if (mode == BlendMode.SOFT_LIGHT) {
            return Blend.Mode.SOFT_LIGHT;
        }
        if (mode == BlendMode.DIFFERENCE) {
            return Blend.Mode.DIFFERENCE;
        }
        if (mode == BlendMode.EXCLUSION) {
            return Blend.Mode.EXCLUSION;
        }
        if (mode == BlendMode.RED) {
            return Blend.Mode.RED;
        }
        if (mode == BlendMode.GREEN) {
            return Blend.Mode.GREEN;
        }
        if (mode == BlendMode.BLUE) {
            return Blend.Mode.BLUE;
        }
        throw new AssertionError((Object) "Unrecognized blend mode: {mode}");
    }

    @Deprecated
    public static Blend.Mode impl_getToolkitMode(BlendMode mode) {
        return toPGMode(mode);
    }

    public Blend() {
    }

    public Blend(BlendMode mode) {
        setMode(mode);
    }

    public Blend(BlendMode mode, Effect bottomInput, Effect topInput) {
        setMode(mode);
        setBottomInput(bottomInput);
        setTopInput(topInput);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.Blend impl_createImpl() {
        return new com.sun.scenario.effect.Blend(toPGMode(BlendMode.SRC_OVER), com.sun.scenario.effect.Effect.DefaultInput, com.sun.scenario.effect.Effect.DefaultInput);
    }

    public final void setMode(BlendMode value) {
        modeProperty().set(value);
    }

    public final BlendMode getMode() {
        return this.mode == null ? BlendMode.SRC_OVER : this.mode.get();
    }

    public final ObjectProperty<BlendMode> modeProperty() {
        if (this.mode == null) {
            this.mode = new ObjectPropertyBase<BlendMode>(BlendMode.SRC_OVER) { // from class: javafx.scene.effect.Blend.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Blend.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Blend.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return Constants.ATTRNAME_MODE;
                }
            };
        }
        return this.mode;
    }

    public final void setOpacity(double value) {
        opacityProperty().set(value);
    }

    public final double getOpacity() {
        if (this.opacity == null) {
            return 1.0d;
        }
        return this.opacity.get();
    }

    public final DoubleProperty opacityProperty() {
        if (this.opacity == null) {
            this.opacity = new DoublePropertyBase(1.0d) { // from class: javafx.scene.effect.Blend.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Blend.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Blend.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "opacity";
                }
            };
        }
        return this.opacity;
    }

    public final void setBottomInput(Effect value) {
        bottomInputProperty().set(value);
    }

    public final Effect getBottomInput() {
        if (this.bottomInput == null) {
            return null;
        }
        return this.bottomInput.get();
    }

    public final ObjectProperty<Effect> bottomInputProperty() {
        if (this.bottomInput == null) {
            this.bottomInput = new Effect.EffectInputProperty("bottomInput");
        }
        return this.bottomInput;
    }

    public final void setTopInput(Effect value) {
        topInputProperty().set(value);
    }

    public final Effect getTopInput() {
        if (this.topInput == null) {
            return null;
        }
        return this.topInput.get();
    }

    public final ObjectProperty<Effect> topInputProperty() {
        if (this.topInput == null) {
            this.topInput = new Effect.EffectInputProperty("topInput");
        }
        return this.topInput;
    }

    @Override // javafx.scene.effect.Effect
    boolean impl_checkChainContains(Effect e2) {
        Effect localTopInput = getTopInput();
        Effect localBottomInput = getBottomInput();
        if (localTopInput == e2 || localBottomInput == e2) {
            return true;
        }
        if (localTopInput != null && localTopInput.impl_checkChainContains(e2)) {
            return true;
        }
        if (localBottomInput != null && localBottomInput.impl_checkChainContains(e2)) {
            return true;
        }
        return false;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localBottomInput = getBottomInput();
        Effect localTopInput = getTopInput();
        if (localTopInput != null) {
            localTopInput.impl_sync();
        }
        if (localBottomInput != null) {
            localBottomInput.impl_sync();
        }
        com.sun.scenario.effect.Blend peer = (com.sun.scenario.effect.Blend) impl_getImpl();
        peer.setTopInput(localTopInput == null ? null : localTopInput.impl_getImpl());
        peer.setBottomInput(localBottomInput == null ? null : localBottomInput.impl_getImpl());
        peer.setOpacity((float) Utils.clamp(0.0d, getOpacity(), 1.0d));
        peer.setMode(toPGMode(getMode()));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        BaseBounds topBounds = new RectBounds();
        BaseBounds bottomBounds = new RectBounds();
        BaseBounds ret = getInputBounds(topBounds, tx, node, boundsAccessor, getTopInput()).deriveWithUnion(getInputBounds(bottomBounds, tx, node, boundsAccessor, getBottomInput()));
        return ret;
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        return new Blend(getMode(), getBottomInput(), getTopInput());
    }
}
