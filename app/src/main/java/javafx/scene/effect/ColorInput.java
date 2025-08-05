package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.scenario.effect.Flood;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/effect/ColorInput.class */
public class ColorInput extends Effect {
    private ObjectProperty<Paint> paint;

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12649x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12650y;
    private DoubleProperty width;
    private DoubleProperty height;

    public ColorInput() {
    }

    public ColorInput(double x2, double y2, double width, double height, Paint paint) {
        setX(x2);
        setY(y2);
        setWidth(width);
        setHeight(height);
        setPaint(paint);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public Flood impl_createImpl() {
        return new Flood(Toolkit.getPaintAccessor().getPlatformPaint(Color.RED));
    }

    public final void setPaint(Paint value) {
        paintProperty().set(value);
    }

    public final Paint getPaint() {
        return this.paint == null ? Color.RED : this.paint.get();
    }

    public final ObjectProperty<Paint> paintProperty() {
        if (this.paint == null) {
            this.paint = new ObjectPropertyBase<Paint>(Color.RED) { // from class: javafx.scene.effect.ColorInput.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColorInput.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "paint";
                }
            };
        }
        return this.paint;
    }

    public final void setX(double value) {
        xProperty().set(value);
    }

    public final double getX() {
        if (this.f12649x == null) {
            return 0.0d;
        }
        return this.f12649x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12649x == null) {
            this.f12649x = new DoublePropertyBase() { // from class: javafx.scene.effect.ColorInput.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorInput.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColorInput.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12649x;
    }

    public final void setY(double value) {
        yProperty().set(value);
    }

    public final double getY() {
        if (this.f12650y == null) {
            return 0.0d;
        }
        return this.f12650y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12650y == null) {
            this.f12650y = new DoublePropertyBase() { // from class: javafx.scene.effect.ColorInput.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorInput.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColorInput.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12650y;
    }

    public final void setWidth(double value) {
        widthProperty().set(value);
    }

    public final double getWidth() {
        if (this.width == null) {
            return 0.0d;
        }
        return this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new DoublePropertyBase() { // from class: javafx.scene.effect.ColorInput.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorInput.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColorInput.this;
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
            return 0.0d;
        }
        return this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new DoublePropertyBase() { // from class: javafx.scene.effect.ColorInput.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ColorInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ColorInput.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ColorInput.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.HEIGHT_TAG_NAME;
                }
            };
        }
        return this.height;
    }

    private Paint getPaintInternal() {
        Paint p2 = getPaint();
        return p2 == null ? Color.RED : p2;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Flood peer = (Flood) impl_getImpl();
        peer.setPaint(Toolkit.getPaintAccessor().getPlatformPaint(getPaintInternal()));
        peer.setFloodBounds(new RectBounds((float) getX(), (float) getY(), (float) (getX() + getWidth()), (float) (getY() + getHeight())));
    }

    @Override // javafx.scene.effect.Effect
    boolean impl_checkChainContains(Effect e2) {
        return false;
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        RectBounds ret = new RectBounds((float) getX(), (float) getY(), (float) (getX() + getWidth()), (float) (getY() + getHeight()));
        return transformBounds(tx, ret);
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        return new ColorInput(getX(), getY(), getWidth(), getHeight(), getPaint());
    }
}
