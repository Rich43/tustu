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

/* loaded from: jfxrt.jar:javafx/scene/effect/ColorAdjust.class */
public class ColorAdjust extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty hue;
    private DoubleProperty saturation;
    private DoubleProperty brightness;
    private DoubleProperty contrast;

    public ColorAdjust() {
    }

    public ColorAdjust(double hue, double saturation, double brightness, double contrast) {
        setBrightness(brightness);
        setContrast(contrast);
        setHue(hue);
        setSaturation(saturation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.ColorAdjust impl_createImpl() {
        return new com.sun.scenario.effect.ColorAdjust();
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

    public final void setHue(double value) {
        hueProperty().set(value);
    }

    public final double getHue() {
        if (this.hue == null) {
            return 0.0d;
        }
        return this.hue.get();
    }

    public final DoubleProperty hueProperty() {
        if (this.hue == null) {
            this.hue = new DoublePropertyBase() { // from class: javafx.scene.effect.ColorAdjust.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorAdjust.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColorAdjust.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "hue";
                }
            };
        }
        return this.hue;
    }

    public final void setSaturation(double value) {
        saturationProperty().set(value);
    }

    public final double getSaturation() {
        if (this.saturation == null) {
            return 0.0d;
        }
        return this.saturation.get();
    }

    public final DoubleProperty saturationProperty() {
        if (this.saturation == null) {
            this.saturation = new DoublePropertyBase() { // from class: javafx.scene.effect.ColorAdjust.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorAdjust.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColorAdjust.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "saturation";
                }
            };
        }
        return this.saturation;
    }

    public final void setBrightness(double value) {
        brightnessProperty().set(value);
    }

    public final double getBrightness() {
        if (this.brightness == null) {
            return 0.0d;
        }
        return this.brightness.get();
    }

    public final DoubleProperty brightnessProperty() {
        if (this.brightness == null) {
            this.brightness = new DoublePropertyBase() { // from class: javafx.scene.effect.ColorAdjust.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorAdjust.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColorAdjust.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "brightness";
                }
            };
        }
        return this.brightness;
    }

    public final void setContrast(double value) {
        contrastProperty().set(value);
    }

    public final double getContrast() {
        if (this.contrast == null) {
            return 0.0d;
        }
        return this.contrast.get();
    }

    public final DoubleProperty contrastProperty() {
        if (this.contrast == null) {
            this.contrast = new DoublePropertyBase() { // from class: javafx.scene.effect.ColorAdjust.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ColorAdjust.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorAdjust.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColorAdjust.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "contrast";
                }
            };
        }
        return this.contrast;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localInput = getInput();
        if (localInput != null) {
            localInput.impl_sync();
        }
        com.sun.scenario.effect.ColorAdjust peer = (com.sun.scenario.effect.ColorAdjust) impl_getImpl();
        peer.setInput(localInput == null ? null : localInput.impl_getImpl());
        peer.setHue((float) Utils.clamp(-1.0d, getHue(), 1.0d));
        peer.setSaturation((float) Utils.clamp(-1.0d, getSaturation(), 1.0d));
        peer.setBrightness((float) Utils.clamp(-1.0d, getBrightness(), 1.0d));
        peer.setContrast((float) Utils.clamp(-1.0d, getContrast(), 1.0d));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        return getInputBounds(bounds, tx, node, boundsAccessor, getInput());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        ColorAdjust ca = new ColorAdjust(getHue(), getSaturation(), getBrightness(), getContrast());
        ca.setInput(ca.getInput());
        return ca;
    }
}
