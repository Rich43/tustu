package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.util.Utils;
import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

/* loaded from: jfxrt.jar:javafx/scene/effect/BoxBlur.class */
public class BoxBlur extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty width;
    private DoubleProperty height;
    private IntegerProperty iterations;

    public BoxBlur() {
    }

    public BoxBlur(double width, double height, int iterations) {
        setWidth(width);
        setHeight(height);
        setIterations(iterations);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.BoxBlur impl_createImpl() {
        return new com.sun.scenario.effect.BoxBlur();
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

    public final void setWidth(double value) {
        widthProperty().set(value);
    }

    public final double getWidth() {
        if (this.width == null) {
            return 5.0d;
        }
        return this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new DoublePropertyBase(5.0d) { // from class: javafx.scene.effect.BoxBlur.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    BoxBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    BoxBlur.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return BoxBlur.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.WIDTH_TAG_NAME;
                }
            };
        }
        return this.width;
    }

    public final void setHeight(double value) {
        heightProperty().set(value);
    }

    public final double getHeight() {
        if (this.height == null) {
            return 5.0d;
        }
        return this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new DoublePropertyBase(5.0d) { // from class: javafx.scene.effect.BoxBlur.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    BoxBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    BoxBlur.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return BoxBlur.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.HEIGHT_TAG_NAME;
                }
            };
        }
        return this.height;
    }

    public final void setIterations(int value) {
        iterationsProperty().set(value);
    }

    public final int getIterations() {
        if (this.iterations == null) {
            return 1;
        }
        return this.iterations.get();
    }

    public final IntegerProperty iterationsProperty() {
        if (this.iterations == null) {
            this.iterations = new IntegerPropertyBase(1) { // from class: javafx.scene.effect.BoxBlur.3
                @Override // javafx.beans.property.IntegerPropertyBase
                public void invalidated() {
                    BoxBlur.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    BoxBlur.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return BoxBlur.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "iterations";
                }
            };
        }
        return this.iterations;
    }

    private int getClampedWidth() {
        return Utils.clamp(0, (int) getWidth(), 255);
    }

    private int getClampedHeight() {
        return Utils.clamp(0, (int) getHeight(), 255);
    }

    private int getClampedIterations() {
        return Utils.clamp(0, getIterations(), 3);
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localInput = getInput();
        if (localInput != null) {
            localInput.impl_sync();
        }
        com.sun.scenario.effect.BoxBlur peer = (com.sun.scenario.effect.BoxBlur) impl_getImpl();
        peer.setInput(localInput == null ? null : localInput.impl_getImpl());
        peer.setHorizontalSize(getClampedWidth());
        peer.setVerticalSize(getClampedHeight());
        peer.setPasses(getClampedIterations());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        BaseBounds bounds2 = getInputBounds(bounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, getInput());
        int localIterations = getClampedIterations();
        int hgrow = getKernelSize(getClampedWidth(), localIterations);
        int vgrow = getKernelSize(getClampedHeight(), localIterations);
        return transformBounds(tx, bounds2.deriveWithPadding(hgrow, vgrow, 0.0f));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        BoxBlur bb2 = new BoxBlur(getWidth(), getHeight(), getIterations());
        bb2.setInput(getInput());
        return bb2;
    }
}
