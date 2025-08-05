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

/* loaded from: jfxrt.jar:javafx/scene/effect/Reflection.class */
public class Reflection extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty topOffset;
    private DoubleProperty topOpacity;
    private DoubleProperty bottomOpacity;
    private DoubleProperty fraction;

    public Reflection() {
    }

    public Reflection(double topOffset, double fraction, double topOpacity, double bottomOpacity) {
        setBottomOpacity(bottomOpacity);
        setTopOffset(topOffset);
        setTopOpacity(topOpacity);
        setFraction(fraction);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.Reflection impl_createImpl() {
        return new com.sun.scenario.effect.Reflection();
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

    public final void setTopOffset(double value) {
        topOffsetProperty().set(value);
    }

    public final double getTopOffset() {
        if (this.topOffset == null) {
            return 0.0d;
        }
        return this.topOffset.get();
    }

    public final DoubleProperty topOffsetProperty() {
        if (this.topOffset == null) {
            this.topOffset = new DoublePropertyBase() { // from class: javafx.scene.effect.Reflection.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    Reflection.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Reflection.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "topOffset";
                }
            };
        }
        return this.topOffset;
    }

    public final void setTopOpacity(double value) {
        topOpacityProperty().set(value);
    }

    public final double getTopOpacity() {
        if (this.topOpacity == null) {
            return 0.5d;
        }
        return this.topOpacity.get();
    }

    public final DoubleProperty topOpacityProperty() {
        if (this.topOpacity == null) {
            this.topOpacity = new DoublePropertyBase(0.5d) { // from class: javafx.scene.effect.Reflection.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Reflection.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "topOpacity";
                }
            };
        }
        return this.topOpacity;
    }

    public final void setBottomOpacity(double value) {
        bottomOpacityProperty().set(value);
    }

    public final double getBottomOpacity() {
        if (this.bottomOpacity == null) {
            return 0.0d;
        }
        return this.bottomOpacity.get();
    }

    public final DoubleProperty bottomOpacityProperty() {
        if (this.bottomOpacity == null) {
            this.bottomOpacity = new DoublePropertyBase() { // from class: javafx.scene.effect.Reflection.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Reflection.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "bottomOpacity";
                }
            };
        }
        return this.bottomOpacity;
    }

    public final void setFraction(double value) {
        fractionProperty().set(value);
    }

    public final double getFraction() {
        if (this.fraction == null) {
            return 0.75d;
        }
        return this.fraction.get();
    }

    public final DoubleProperty fractionProperty() {
        if (this.fraction == null) {
            this.fraction = new DoublePropertyBase(0.75d) { // from class: javafx.scene.effect.Reflection.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Reflection.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    Reflection.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Reflection.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fraction";
                }
            };
        }
        return this.fraction;
    }

    private float getClampedFraction() {
        return (float) Utils.clamp(0.0d, getFraction(), 1.0d);
    }

    private float getClampedBottomOpacity() {
        return (float) Utils.clamp(0.0d, getBottomOpacity(), 1.0d);
    }

    private float getClampedTopOpacity() {
        return (float) Utils.clamp(0.0d, getTopOpacity(), 1.0d);
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localInput = getInput();
        if (localInput != null) {
            localInput.impl_sync();
        }
        com.sun.scenario.effect.Reflection peer = (com.sun.scenario.effect.Reflection) impl_getImpl();
        peer.setInput(localInput == null ? null : localInput.impl_getImpl());
        peer.setFraction(getClampedFraction());
        peer.setTopOffset((float) getTopOffset());
        peer.setBottomOpacity(getClampedBottomOpacity());
        peer.setTopOpacity(getClampedTopOpacity());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        BaseBounds bounds2 = getInputBounds(bounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, getInput());
        bounds2.roundOut();
        float x1 = bounds2.getMinX();
        float y1 = bounds2.getMaxY() + ((float) getTopOffset());
        float z1 = bounds2.getMinZ();
        float x2 = bounds2.getMaxX();
        float y2 = y1 + (getClampedFraction() * bounds2.getHeight());
        float z2 = bounds2.getMaxZ();
        BaseBounds ret = BaseBounds.getInstance(x1, y1, z1, x2, y2, z2);
        return transformBounds(tx, ret.deriveWithUnion(bounds2));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        Reflection ref = new Reflection(getTopOffset(), getFraction(), getTopOpacity(), getBottomOpacity());
        ref.setInput(ref.getInput());
        return ref;
    }
}
