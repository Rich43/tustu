package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:javafx/scene/effect/DropShadow.class */
public class DropShadow extends Effect {
    private boolean changeIsLocal;
    private ObjectProperty<Effect> input;
    private DoubleProperty radius;
    private DoubleProperty width;
    private DoubleProperty height;
    private ObjectProperty<BlurType> blurType;
    private DoubleProperty spread;
    private ObjectProperty<Color> color;
    private DoubleProperty offsetX;
    private DoubleProperty offsetY;

    public DropShadow() {
    }

    public DropShadow(double radius, Color color) {
        setRadius(radius);
        setColor(color);
    }

    public DropShadow(double radius, double offsetX, double offsetY, Color color) {
        setRadius(radius);
        setOffsetX(offsetX);
        setOffsetY(offsetY);
        setColor(color);
    }

    public DropShadow(BlurType blurType, Color color, double radius, double spread, double offsetX, double offsetY) {
        setBlurType(blurType);
        setColor(color);
        setRadius(radius);
        setSpread(spread);
        setOffsetX(offsetX);
        setOffsetY(offsetY);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.DropShadow impl_createImpl() {
        return new com.sun.scenario.effect.DropShadow();
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
            this.radius = new DoublePropertyBase(10.0d) { // from class: javafx.scene.effect.DropShadow.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    double localRadius = DropShadow.this.getRadius();
                    if (!DropShadow.this.changeIsLocal) {
                        DropShadow.this.changeIsLocal = true;
                        DropShadow.this.updateRadius(localRadius);
                        DropShadow.this.changeIsLocal = false;
                        DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                        DropShadow.this.effectBoundsChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DropShadow.this;
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
            this.width = new DoublePropertyBase(21.0d) { // from class: javafx.scene.effect.DropShadow.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    double localWidth = DropShadow.this.getWidth();
                    if (!DropShadow.this.changeIsLocal) {
                        DropShadow.this.changeIsLocal = true;
                        DropShadow.this.updateWidth(localWidth);
                        DropShadow.this.changeIsLocal = false;
                        DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                        DropShadow.this.effectBoundsChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DropShadow.this;
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
            this.height = new DoublePropertyBase(21.0d) { // from class: javafx.scene.effect.DropShadow.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    double localHeight = DropShadow.this.getHeight();
                    if (!DropShadow.this.changeIsLocal) {
                        DropShadow.this.changeIsLocal = true;
                        DropShadow.this.updateHeight(localHeight);
                        DropShadow.this.changeIsLocal = false;
                        DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                        DropShadow.this.effectBoundsChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DropShadow.this;
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
            this.blurType = new ObjectPropertyBase<BlurType>(BlurType.THREE_PASS_BOX) { // from class: javafx.scene.effect.DropShadow.4
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    DropShadow.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "blurType";
                }
            };
        }
        return this.blurType;
    }

    public final void setSpread(double value) {
        spreadProperty().set(value);
    }

    public final double getSpread() {
        if (this.spread == null) {
            return 0.0d;
        }
        return this.spread.get();
    }

    public final DoubleProperty spreadProperty() {
        if (this.spread == null) {
            this.spread = new DoublePropertyBase() { // from class: javafx.scene.effect.DropShadow.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "spread";
                }
            };
        }
        return this.spread;
    }

    public final void setColor(Color value) {
        colorProperty().set(value);
    }

    public final Color getColor() {
        return this.color == null ? Color.BLACK : this.color.get();
    }

    public final ObjectProperty<Color> colorProperty() {
        if (this.color == null) {
            this.color = new ObjectPropertyBase<Color>(Color.BLACK) { // from class: javafx.scene.effect.DropShadow.6
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "color";
                }
            };
        }
        return this.color;
    }

    public final void setOffsetX(double value) {
        offsetXProperty().set(value);
    }

    public final double getOffsetX() {
        if (this.offsetX == null) {
            return 0.0d;
        }
        return this.offsetX.get();
    }

    public final DoubleProperty offsetXProperty() {
        if (this.offsetX == null) {
            this.offsetX = new DoublePropertyBase() { // from class: javafx.scene.effect.DropShadow.7
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    DropShadow.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "offsetX";
                }
            };
        }
        return this.offsetX;
    }

    public final void setOffsetY(double value) {
        offsetYProperty().set(value);
    }

    public final double getOffsetY() {
        if (this.offsetY == null) {
            return 0.0d;
        }
        return this.offsetY.get();
    }

    public final DoubleProperty offsetYProperty() {
        if (this.offsetY == null) {
            this.offsetY = new DoublePropertyBase() { // from class: javafx.scene.effect.DropShadow.8
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    DropShadow.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    DropShadow.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DropShadow.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "offsetY";
                }
            };
        }
        return this.offsetY;
    }

    private float getClampedWidth() {
        return (float) Utils.clamp(0.0d, getWidth(), 255.0d);
    }

    private float getClampedHeight() {
        return (float) Utils.clamp(0.0d, getHeight(), 255.0d);
    }

    private float getClampedSpread() {
        return (float) Utils.clamp(0.0d, getSpread(), 1.0d);
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
        com.sun.scenario.effect.DropShadow peer = (com.sun.scenario.effect.DropShadow) impl_getImpl();
        peer.setShadowSourceInput(localInput == null ? null : localInput.impl_getImpl());
        peer.setContentInput(localInput == null ? null : localInput.impl_getImpl());
        peer.setGaussianWidth(getClampedWidth());
        peer.setGaussianHeight(getClampedHeight());
        peer.setSpread(getClampedSpread());
        peer.setShadowMode(Toolkit.getToolkit().toShadowMode(getBlurTypeInternal()));
        peer.setColor(Toolkit.getToolkit().toColor4f(getColorInternal()));
        peer.setOffsetX((int) getOffsetX());
        peer.setOffsetY((int) getOffsetY());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        BaseBounds bounds2 = getInputBounds(bounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, getInput());
        int shadowX = (int) getOffsetX();
        int shadowY = (int) getOffsetY();
        BaseBounds shadowBounds = BaseBounds.getInstance(bounds2.getMinX() + shadowX, bounds2.getMinY() + shadowY, bounds2.getMinZ(), bounds2.getMaxX() + shadowX, bounds2.getMaxY() + shadowY, bounds2.getMaxZ());
        BaseBounds shadowBounds2 = getShadowBounds(shadowBounds, tx, getClampedWidth(), getClampedHeight(), getBlurTypeInternal());
        BaseBounds contentBounds = transformBounds(tx, bounds2);
        BaseBounds ret = contentBounds.deriveWithUnion(shadowBounds2);
        return ret;
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        DropShadow d2 = new DropShadow(getBlurType(), getColor(), getRadius(), getSpread(), getOffsetX(), getOffsetY());
        d2.setInput(getInput());
        d2.setWidth(getWidth());
        d2.setHeight(getHeight());
        return d2;
    }
}
