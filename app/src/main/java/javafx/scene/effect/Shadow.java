package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.scenario.effect.GeneralShadow;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:javafx/scene/effect/Shadow.class */
public class Shadow extends Effect {
    private boolean changeIsLocal;
    private ObjectProperty<Effect> input;
    private DoubleProperty radius;
    private DoubleProperty width;
    private DoubleProperty height;
    private ObjectProperty<BlurType> blurType;
    private ObjectProperty<Color> color;

    public Shadow() {
    }

    public Shadow(double radius, Color color) {
        setRadius(radius);
        setColor(color);
    }

    public Shadow(BlurType blurType, Color color, double radius) {
        setBlurType(blurType);
        setColor(color);
        setRadius(radius);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public GeneralShadow impl_createImpl() {
        return new GeneralShadow();
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
            this.radius = new DoublePropertyBase(10.0d) { // from class: javafx.scene.effect.Shadow.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    double localRadius = Shadow.this.getRadius();
                    if (!Shadow.this.changeIsLocal) {
                        Shadow.this.changeIsLocal = true;
                        Shadow.this.updateRadius(localRadius);
                        Shadow.this.changeIsLocal = false;
                        Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                        Shadow.this.effectBoundsChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "radius";
                }
            };
        }
        return this.radius;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRadius(double value) {
        double newdim = (value * 2.0d) + 1.0d;
        if (this.width != null && this.width.isBound()) {
            if (this.height == null || !this.height.isBound()) {
                setHeight((newdim * 2.0d) - getWidth());
                return;
            }
            return;
        }
        if (this.height != null && this.height.isBound()) {
            setWidth((newdim * 2.0d) - getHeight());
        } else {
            setWidth(newdim);
            setHeight(newdim);
        }
    }

    public final void setWidth(double value) {
        widthProperty().set(value);
    }

    public final double getWidth() {
        if (this.width == null) {
            return 21.0d;
        }
        return this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new DoublePropertyBase(21.0d) { // from class: javafx.scene.effect.Shadow.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    double localWidth = Shadow.this.getWidth();
                    if (!Shadow.this.changeIsLocal) {
                        Shadow.this.changeIsLocal = true;
                        Shadow.this.updateWidth(localWidth);
                        Shadow.this.changeIsLocal = false;
                        Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                        Shadow.this.effectBoundsChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.WIDTH_TAG_NAME;
                }
            };
        }
        return this.width;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWidth(double value) {
        if (this.radius == null || !this.radius.isBound()) {
            double newrad = (((value + getHeight()) / 2.0d) - 1.0d) / 2.0d;
            if (newrad < 0.0d) {
                newrad = 0.0d;
            }
            setRadius(newrad);
            return;
        }
        if (this.height == null || !this.height.isBound()) {
            double newdim = (getRadius() * 2.0d) + 1.0d;
            setHeight((newdim * 2.0d) - value);
        }
    }

    public final void setHeight(double value) {
        heightProperty().set(value);
    }

    public final double getHeight() {
        if (this.height == null) {
            return 21.0d;
        }
        return this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new DoublePropertyBase(21.0d) { // from class: javafx.scene.effect.Shadow.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    double localHeight = Shadow.this.getHeight();
                    if (!Shadow.this.changeIsLocal) {
                        Shadow.this.changeIsLocal = true;
                        Shadow.this.updateHeight(localHeight);
                        Shadow.this.changeIsLocal = false;
                        Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                        Shadow.this.effectBoundsChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.HEIGHT_TAG_NAME;
                }
            };
        }
        return this.height;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeight(double value) {
        if (this.radius == null || !this.radius.isBound()) {
            double newrad = (((getWidth() + value) / 2.0d) - 1.0d) / 2.0d;
            if (newrad < 0.0d) {
                newrad = 0.0d;
            }
            setRadius(newrad);
            return;
        }
        if (this.width == null || !this.width.isBound()) {
            double newdim = (getRadius() * 2.0d) + 1.0d;
            setWidth((newdim * 2.0d) - value);
        }
    }

    public final void setBlurType(BlurType value) {
        blurTypeProperty().set(value);
    }

    public final BlurType getBlurType() {
        return this.blurType == null ? BlurType.THREE_PASS_BOX : this.blurType.get();
    }

    public final ObjectProperty<BlurType> blurTypeProperty() {
        if (this.blurType == null) {
            this.blurType = new ObjectPropertyBase<BlurType>(BlurType.THREE_PASS_BOX) { // from class: javafx.scene.effect.Shadow.4
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    Shadow.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "blurType";
                }
            };
        }
        return this.blurType;
    }

    public final void setColor(Color value) {
        colorProperty().set(value);
    }

    public final Color getColor() {
        return this.color == null ? Color.BLACK : this.color.get();
    }

    public final ObjectProperty<Color> colorProperty() {
        if (this.color == null) {
            this.color = new ObjectPropertyBase<Color>(Color.BLACK) { // from class: javafx.scene.effect.Shadow.5
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Shadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "color";
                }
            };
        }
        return this.color;
    }

    private float getClampedWidth() {
        return (float) Utils.clamp(0.0d, getWidth(), 255.0d);
    }

    private float getClampedHeight() {
        return (float) Utils.clamp(0.0d, getHeight(), 255.0d);
    }

    private Color getColorInternal() {
        Color c2 = getColor();
        return c2 == null ? Color.BLACK : c2;
    }

    private BlurType getBlurTypeInternal() {
        BlurType bt2 = getBlurType();
        return bt2 == null ? BlurType.THREE_PASS_BOX : bt2;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localInput = getInput();
        if (localInput != null) {
            localInput.impl_sync();
        }
        GeneralShadow peer = (GeneralShadow) impl_getImpl();
        peer.setInput(localInput == null ? null : localInput.impl_getImpl());
        peer.setGaussianWidth(getClampedWidth());
        peer.setGaussianHeight(getClampedHeight());
        peer.setShadowMode(Toolkit.getToolkit().toShadowMode(getBlurTypeInternal()));
        peer.setColor(Toolkit.getToolkit().toColor4f(getColorInternal()));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        return getShadowBounds(getInputBounds(bounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, getInput()), tx, getClampedWidth(), getClampedHeight(), getBlurTypeInternal());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        Shadow shadow = new Shadow(getBlurType(), getColor(), getRadius());
        shadow.setInput(getInput());
        shadow.setHeight(getHeight());
        shadow.setWidth(getWidth());
        return shadow;
    }
}
