package javafx.scene.effect;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.effect.Identity;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.image.Image;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/effect/ImageInput.class */
public class ImageInput extends Effect {
    private ObjectProperty<Image> source;
    private final AbstractNotifyListener platformImageChangeListener = new AbstractNotifyListener() { // from class: javafx.scene.effect.ImageInput.1
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            ImageInput.this.effectBoundsChanged();
        }
    };
    private Image oldImage;

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12653x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12654y;

    public ImageInput() {
    }

    public ImageInput(Image source) {
        setSource(source);
    }

    public ImageInput(Image source, double x2, double y2) {
        setSource(source);
        setX(x2);
        setY(y2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public Identity impl_createImpl() {
        return new Identity(null);
    }

    public final void setSource(Image value) {
        sourceProperty().set(value);
    }

    public final Image getSource() {
        if (this.source == null) {
            return null;
        }
        return this.source.get();
    }

    public final ObjectProperty<Image> sourceProperty() {
        if (this.source == null) {
            this.source = new ObjectPropertyBase<Image>() { // from class: javafx.scene.effect.ImageInput.2
                private boolean needsListeners = false;

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Image _image = get();
                    Toolkit.ImageAccessor accessor = Toolkit.getImageAccessor();
                    if (this.needsListeners) {
                        accessor.getImageProperty(ImageInput.this.oldImage).removeListener(ImageInput.this.platformImageChangeListener.getWeakListener());
                    }
                    this.needsListeners = _image != null && (accessor.isAnimation(_image) || _image.getProgress() < 1.0d);
                    ImageInput.this.oldImage = _image;
                    if (this.needsListeners) {
                        accessor.getImageProperty(_image).addListener(ImageInput.this.platformImageChangeListener.getWeakListener());
                    }
                    ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ImageInput.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageInput.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "source";
                }
            };
        }
        return this.source;
    }

    public final void setX(double value) {
        xProperty().set(value);
    }

    public final double getX() {
        if (this.f12653x == null) {
            return 0.0d;
        }
        return this.f12653x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12653x == null) {
            this.f12653x = new DoublePropertyBase() { // from class: javafx.scene.effect.ImageInput.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ImageInput.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageInput.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12653x;
    }

    public final void setY(double value) {
        yProperty().set(value);
    }

    public final double getY() {
        if (this.f12654y == null) {
            return 0.0d;
        }
        return this.f12654y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12654y == null) {
            this.f12654y = new DoublePropertyBase() { // from class: javafx.scene.effect.ImageInput.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ImageInput.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageInput.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12654y;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Identity peer = (Identity) impl_getImpl();
        Image localSource = getSource();
        if (localSource != null && localSource.impl_getPlatformImage() != null) {
            peer.setSource(Toolkit.getToolkit().toFilterable(localSource));
        } else {
            peer.setSource(null);
        }
        peer.setLocation(new Point2D((float) getX(), (float) getY()));
    }

    @Override // javafx.scene.effect.Effect
    boolean impl_checkChainContains(Effect e2) {
        return false;
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        Image localSource = getSource();
        if (localSource != null && localSource.impl_getPlatformImage() != null) {
            float localX = (float) getX();
            float localY = (float) getY();
            float localWidth = (float) localSource.getWidth();
            float localHeight = (float) localSource.getHeight();
            BaseBounds r2 = new RectBounds(localX, localY, localX + localWidth, localY + localHeight);
            return transformBounds(tx, r2);
        }
        return new RectBounds();
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        return new ImageInput(getSource(), getX(), getY());
    }
}
