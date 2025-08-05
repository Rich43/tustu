package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/effect/Effect.class */
public abstract class Effect {
    private com.sun.scenario.effect.Effect peer;
    private IntegerProperty effectDirty = new SimpleIntegerProperty(this, "effectDirty");

    abstract com.sun.scenario.effect.Effect impl_createImpl();

    abstract void impl_update();

    abstract boolean impl_checkChainContains(Effect effect);

    @Deprecated
    public abstract BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor);

    @Deprecated
    public abstract Effect impl_copy();

    protected Effect() {
        markDirty(EffectDirtyBits.EFFECT_DIRTY);
    }

    void effectBoundsChanged() {
        toggleDirty(EffectDirtyBits.BOUNDS_CHANGED);
    }

    @Deprecated
    public com.sun.scenario.effect.Effect impl_getImpl() {
        if (this.peer == null) {
            this.peer = impl_createImpl();
        }
        return this.peer;
    }

    private void setEffectDirty(int value) {
        impl_effectDirtyProperty().set(value);
    }

    @Deprecated
    public final IntegerProperty impl_effectDirtyProperty() {
        return this.effectDirty;
    }

    @Deprecated
    public final boolean impl_isEffectDirty() {
        return isEffectDirty(EffectDirtyBits.EFFECT_DIRTY);
    }

    final void markDirty(EffectDirtyBits dirtyBit) {
        setEffectDirty(this.effectDirty.get() | dirtyBit.getMask());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleDirty(EffectDirtyBits dirtyBit) {
        setEffectDirty(this.effectDirty.get() ^ dirtyBit.getMask());
    }

    private boolean isEffectDirty(EffectDirtyBits dirtyBit) {
        return (this.effectDirty.get() & dirtyBit.getMask()) != 0;
    }

    private void clearEffectDirty(EffectDirtyBits dirtyBit) {
        setEffectDirty(this.effectDirty.get() & (dirtyBit.getMask() ^ (-1)));
    }

    @Deprecated
    public final void impl_sync() {
        if (isEffectDirty(EffectDirtyBits.EFFECT_DIRTY)) {
            impl_update();
            clearEffectDirty(EffectDirtyBits.EFFECT_DIRTY);
        }
    }

    boolean impl_containsCycles(Effect value) {
        if (value != null) {
            if (value == this || value.impl_checkChainContains(this)) {
                return true;
            }
            return false;
        }
        return false;
    }

    /* loaded from: jfxrt.jar:javafx/scene/effect/Effect$EffectInputChangeListener.class */
    class EffectInputChangeListener extends EffectChangeListener {
        private int oldBits;

        EffectInputChangeListener() {
        }

        public void register(Effect value) {
            super.register(value == null ? null : value.impl_effectDirtyProperty());
            if (value != null) {
                this.oldBits = value.impl_effectDirtyProperty().get();
            }
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            int newBits = ((IntegerProperty) valueModel).get();
            int dirtyBits = newBits ^ this.oldBits;
            this.oldBits = newBits;
            if (EffectDirtyBits.isSet(dirtyBits, EffectDirtyBits.EFFECT_DIRTY) && EffectDirtyBits.isSet(newBits, EffectDirtyBits.EFFECT_DIRTY)) {
                Effect.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            }
            if (EffectDirtyBits.isSet(dirtyBits, EffectDirtyBits.BOUNDS_CHANGED)) {
                Effect.this.toggleDirty(EffectDirtyBits.BOUNDS_CHANGED);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/effect/Effect$EffectInputProperty.class */
    class EffectInputProperty extends ObjectPropertyBase<Effect> {
        private final String propertyName;
        private Effect validInput = null;
        private final EffectInputChangeListener effectChangeListener;

        public EffectInputProperty(String propertyName) {
            this.effectChangeListener = Effect.this.new EffectInputChangeListener();
            this.propertyName = propertyName;
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        public void invalidated() {
            Effect newInput = (Effect) super.get();
            if (Effect.this.impl_containsCycles(newInput)) {
                if (isBound()) {
                    unbind();
                    set(this.validInput);
                    throw new IllegalArgumentException("Cycle in effect chain detected, binding was set to incorrect value, unbinding the input property");
                }
                set(this.validInput);
                throw new IllegalArgumentException("Cycle in effect chain detected");
            }
            this.validInput = newInput;
            this.effectChangeListener.register(newInput);
            Effect.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            Effect.this.effectBoundsChanged();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Effect.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return this.propertyName;
        }
    }

    static BaseBounds transformBounds(BaseTransform tx, BaseBounds r2) {
        if (tx == null || tx.isIdentity()) {
            return r2;
        }
        BaseBounds ret = new RectBounds();
        return tx.transform(r2, ret);
    }

    static int getKernelSize(float fsize, int iterations) {
        int ksize = (int) Math.ceil(fsize);
        if (ksize < 1) {
            ksize = 1;
        }
        return ((((ksize - 1) * iterations) + 1) | 1) / 2;
    }

    static BaseBounds getShadowBounds(BaseBounds bounds, BaseTransform tx, float width, float height, BlurType blurType) {
        int hgrow = 0;
        int vgrow = 0;
        switch (blurType) {
            case GAUSSIAN:
                float hradius = width < 1.0f ? 0.0f : (width - 1.0f) / 2.0f;
                float vradius = height < 1.0f ? 0.0f : (height - 1.0f) / 2.0f;
                hgrow = (int) Math.ceil(hradius);
                vgrow = (int) Math.ceil(vradius);
                break;
            case ONE_PASS_BOX:
                hgrow = getKernelSize(Math.round(width / 3.0f), 1);
                vgrow = getKernelSize(Math.round(height / 3.0f), 1);
                break;
            case TWO_PASS_BOX:
                hgrow = getKernelSize(Math.round(width / 3.0f), 2);
                vgrow = getKernelSize(Math.round(height / 3.0f), 2);
                break;
            case THREE_PASS_BOX:
                hgrow = getKernelSize(Math.round(width / 3.0f), 3);
                vgrow = getKernelSize(Math.round(height / 3.0f), 3);
                break;
        }
        return transformBounds(tx, bounds.deriveWithPadding(hgrow, vgrow, 0.0f));
    }

    static BaseBounds getInputBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor, Effect input) {
        BaseBounds bounds2;
        if (input != null) {
            bounds2 = input.impl_getBounds(bounds, tx, node, boundsAccessor);
        } else {
            bounds2 = boundsAccessor.getGeomBounds(bounds, tx, node);
        }
        return bounds2;
    }
}
