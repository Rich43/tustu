package javafx.scene.image;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGImageView;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.MetadataParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

@DefaultProperty(MetadataParser.IMAGE_TAG_NAME)
/* loaded from: jfxrt.jar:javafx/scene/image/ImageView.class */
public class ImageView extends Node {
    private ObjectProperty<Image> image;
    private Image oldImage;
    private StringProperty imageUrl;
    private final AbstractNotifyListener platformImageChangeListener;

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12660x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12661y;
    private DoubleProperty fitWidth;
    private DoubleProperty fitHeight;
    private BooleanProperty preserveRatio;
    private BooleanProperty smooth;
    public static final boolean SMOOTH_DEFAULT = Toolkit.getToolkit().getDefaultImageSmooth();
    private ObjectProperty<Rectangle2D> viewport;
    private double destWidth;
    private double destHeight;
    private boolean validWH;
    private static final String DEFAULT_STYLE_CLASS = "image-view";

    public ImageView() {
        this.imageUrl = null;
        this.platformImageChangeListener = new AbstractNotifyListener() { // from class: javafx.scene.image.ImageView.3
            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable valueModel) {
                ImageView.this.invalidateWidthHeight();
                ImageView.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                ImageView.this.impl_geomChanged();
            }
        };
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    public ImageView(String url) {
        this(new Image(url));
    }

    public ImageView(Image image) {
        this.imageUrl = null;
        this.platformImageChangeListener = new AbstractNotifyListener() { // from class: javafx.scene.image.ImageView.3
            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable valueModel) {
                ImageView.this.invalidateWidthHeight();
                ImageView.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                ImageView.this.impl_geomChanged();
            }
        };
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        setImage(image);
    }

    public final void setImage(Image value) {
        imageProperty().set(value);
    }

    public final Image getImage() {
        if (this.image == null) {
            return null;
        }
        return this.image.get();
    }

    public final ObjectProperty<Image> imageProperty() {
        if (this.image == null) {
            this.image = new ObjectPropertyBase<Image>() { // from class: javafx.scene.image.ImageView.1
                private boolean needsListeners = false;

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Image _image = get();
                    boolean dimensionChanged = _image == null || ImageView.this.oldImage == null || ImageView.this.oldImage.getWidth() != _image.getWidth() || ImageView.this.oldImage.getHeight() != _image.getHeight();
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(ImageView.this.oldImage).removeListener(ImageView.this.platformImageChangeListener.getWeakListener());
                    }
                    this.needsListeners = _image != null && (_image.isAnimation() || _image.getProgress() < 1.0d);
                    ImageView.this.oldImage = _image;
                    if (this.needsListeners) {
                        Toolkit.getImageAccessor().getImageProperty(_image).addListener(ImageView.this.platformImageChangeListener.getWeakListener());
                    }
                    if (dimensionChanged) {
                        ImageView.this.invalidateWidthHeight();
                        ImageView.this.impl_geomChanged();
                    }
                    ImageView.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.IMAGE_TAG_NAME;
                }
            };
        }
        return this.image;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StringProperty imageUrlProperty() {
        if (this.imageUrl == null) {
            this.imageUrl = new StyleableStringProperty() { // from class: javafx.scene.image.ImageView.2
                @Override // javafx.beans.property.StringPropertyBase
                protected void invalidated() {
                    String imageUrl = get();
                    if (imageUrl != null) {
                        ImageView.this.setImage(StyleManager.getInstance().getCachedImage(imageUrl));
                    } else {
                        ImageView.this.setImage(null);
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "imageUrl";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, String> getCssMetaData() {
                    return StyleableProperties.IMAGE;
                }
            };
        }
        return this.imageUrl;
    }

    public final void setX(double value) {
        xProperty().set(value);
    }

    public final double getX() {
        if (this.f12660x == null) {
            return 0.0d;
        }
        return this.f12660x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12660x == null) {
            this.f12660x = new DoublePropertyBase() { // from class: javafx.scene.image.ImageView.4
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ImageView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    ImageView.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12660x;
    }

    public final void setY(double value) {
        yProperty().set(value);
    }

    public final double getY() {
        if (this.f12661y == null) {
            return 0.0d;
        }
        return this.f12661y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12661y == null) {
            this.f12661y = new DoublePropertyBase() { // from class: javafx.scene.image.ImageView.5
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ImageView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    ImageView.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12661y;
    }

    public final void setFitWidth(double value) {
        fitWidthProperty().set(value);
    }

    public final double getFitWidth() {
        if (this.fitWidth == null) {
            return 0.0d;
        }
        return this.fitWidth.get();
    }

    public final DoubleProperty fitWidthProperty() {
        if (this.fitWidth == null) {
            this.fitWidth = new DoublePropertyBase() { // from class: javafx.scene.image.ImageView.6
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ImageView.this.invalidateWidthHeight();
                    ImageView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                    ImageView.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fitWidth";
                }
            };
        }
        return this.fitWidth;
    }

    public final void setFitHeight(double value) {
        fitHeightProperty().set(value);
    }

    public final double getFitHeight() {
        if (this.fitHeight == null) {
            return 0.0d;
        }
        return this.fitHeight.get();
    }

    public final DoubleProperty fitHeightProperty() {
        if (this.fitHeight == null) {
            this.fitHeight = new DoublePropertyBase() { // from class: javafx.scene.image.ImageView.7
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ImageView.this.invalidateWidthHeight();
                    ImageView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                    ImageView.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fitHeight";
                }
            };
        }
        return this.fitHeight;
    }

    public final void setPreserveRatio(boolean value) {
        preserveRatioProperty().set(value);
    }

    public final boolean isPreserveRatio() {
        if (this.preserveRatio == null) {
            return false;
        }
        return this.preserveRatio.get();
    }

    public final BooleanProperty preserveRatioProperty() {
        if (this.preserveRatio == null) {
            this.preserveRatio = new BooleanPropertyBase() { // from class: javafx.scene.image.ImageView.8
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    ImageView.this.invalidateWidthHeight();
                    ImageView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                    ImageView.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "preserveRatio";
                }
            };
        }
        return this.preserveRatio;
    }

    public final void setSmooth(boolean value) {
        smoothProperty().set(value);
    }

    public final boolean isSmooth() {
        return this.smooth == null ? SMOOTH_DEFAULT : this.smooth.get();
    }

    public final BooleanProperty smoothProperty() {
        if (this.smooth == null) {
            this.smooth = new BooleanPropertyBase(SMOOTH_DEFAULT) { // from class: javafx.scene.image.ImageView.9
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    ImageView.this.impl_markDirty(DirtyBits.NODE_SMOOTH);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "smooth";
                }
            };
        }
        return this.smooth;
    }

    public final void setViewport(Rectangle2D value) {
        viewportProperty().set(value);
    }

    public final Rectangle2D getViewport() {
        if (this.viewport == null) {
            return null;
        }
        return this.viewport.get();
    }

    public final ObjectProperty<Rectangle2D> viewportProperty() {
        if (this.viewport == null) {
            this.viewport = new ObjectPropertyBase<Rectangle2D>() { // from class: javafx.scene.image.ImageView.10
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ImageView.this.invalidateWidthHeight();
                    ImageView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                    ImageView.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ImageView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "viewport";
                }
            };
        }
        return this.viewport;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGImageView();
    }

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        recomputeWidthHeight();
        BaseBounds bounds2 = bounds.deriveWithNewBounds((float) getX(), (float) getY(), 0.0f, (float) (getX() + this.destWidth), (float) (getY() + this.destHeight), 0.0f);
        return tx.transform(bounds2, bounds2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateWidthHeight() {
        this.validWH = false;
    }

    private void recomputeWidthHeight() {
        if (this.validWH) {
            return;
        }
        Image localImage = getImage();
        Rectangle2D localViewport = getViewport();
        double w2 = 0.0d;
        double h2 = 0.0d;
        if (localViewport != null && localViewport.getWidth() > 0.0d && localViewport.getHeight() > 0.0d) {
            w2 = localViewport.getWidth();
            h2 = localViewport.getHeight();
        } else if (localImage != null) {
            w2 = localImage.getWidth();
            h2 = localImage.getHeight();
        }
        double localFitWidth = getFitWidth();
        double localFitHeight = getFitHeight();
        if (!isPreserveRatio() || w2 <= 0.0d || h2 <= 0.0d || (localFitWidth <= 0.0d && localFitHeight <= 0.0d)) {
            if (localFitWidth > 0.0d) {
                w2 = localFitWidth;
            }
            if (localFitHeight > 0.0d) {
                h2 = localFitHeight;
            }
        } else if (localFitWidth <= 0.0d || (localFitHeight > 0.0d && localFitWidth * h2 > localFitHeight * w2)) {
            w2 = (w2 * localFitHeight) / h2;
            h2 = localFitHeight;
        } else {
            h2 = (h2 * localFitWidth) / w2;
            w2 = localFitWidth;
        }
        this.destWidth = w2;
        this.destHeight = h2;
        this.validWH = true;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        if (getImage() == null) {
            return false;
        }
        recomputeWidthHeight();
        double dx = localX - getX();
        double dy = localY - getY();
        Image localImage = getImage();
        double srcWidth = localImage.getWidth();
        double srcHeight = localImage.getHeight();
        double viewWidth = srcWidth;
        double viewHeight = srcHeight;
        double vw = 0.0d;
        double vh = 0.0d;
        double vminx = 0.0d;
        double vminy = 0.0d;
        Rectangle2D localViewport = getViewport();
        if (localViewport != null) {
            vw = localViewport.getWidth();
            vh = localViewport.getHeight();
            vminx = localViewport.getMinX();
            vminy = localViewport.getMinY();
        }
        if (vw > 0.0d && vh > 0.0d) {
            viewWidth = vw;
            viewHeight = vh;
        }
        double dx2 = vminx + ((dx * viewWidth) / this.destWidth);
        double dy2 = vminy + ((dy * viewHeight) / this.destHeight);
        if (dx2 < 0.0d || dy2 < 0.0d || dx2 >= srcWidth || dy2 >= srcHeight || dx2 < vminx || dy2 < vminy || dx2 >= vminx + viewWidth || dy2 >= vminy + viewHeight) {
            return false;
        }
        return Toolkit.getToolkit().imageContains(localImage.impl_getPlatformImage(), (float) dx2, (float) dy2);
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/ImageView$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<ImageView, String> IMAGE = new CssMetaData<ImageView, String>("-fx-image", URLConverter.getInstance()) { // from class: javafx.scene.image.ImageView.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ImageView n2) {
                return n2.image == null || !n2.image.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<String> getStyleableProperty(ImageView n2) {
                return (StyleableProperty) n2.imageUrlProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Node.getClassCssMetaData());
            styleables.add(IMAGE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    void updateViewport() {
        recomputeWidthHeight();
        if (getImage() == null || getImage().impl_getPlatformImage() == null) {
            return;
        }
        Rectangle2D localViewport = getViewport();
        NGImageView peer = (NGImageView) impl_getPeer();
        if (localViewport != null) {
            peer.setViewport((float) localViewport.getMinX(), (float) localViewport.getMinY(), (float) localViewport.getWidth(), (float) localViewport.getHeight(), (float) this.destWidth, (float) this.destHeight);
        } else {
            peer.setViewport(0.0f, 0.0f, 0.0f, 0.0f, (float) this.destWidth, (float) this.destHeight);
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        NGImageView peer = (NGImageView) impl_getPeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            peer.setX((float) getX());
            peer.setY((float) getY());
        }
        if (impl_isDirty(DirtyBits.NODE_SMOOTH)) {
            peer.setSmooth(isSmooth());
        }
        if (impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            peer.setImage(getImage() != null ? getImage().impl_getPlatformImage() : null);
        }
        if (impl_isDirty(DirtyBits.NODE_VIEWPORT) || impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            updateViewport();
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return alg.processLeafNode(this, ctx);
    }
}
