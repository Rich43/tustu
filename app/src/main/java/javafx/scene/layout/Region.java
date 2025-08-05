package javafx.scene.layout;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.ShapeConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGRegion;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.TempState;
import com.sun.media.jfxmedia.MetadataParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.util.Callback;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/layout/Region.class */
public class Region extends Parent {
    public static final double USE_PREF_SIZE = Double.NEGATIVE_INFINITY;
    public static final double USE_COMPUTED_SIZE = -1.0d;
    static Vec2d TEMP_VEC2D = new Vec2d();
    private BooleanProperty snapToPixel;
    private ObjectProperty<Insets> opaqueInsets;
    private ReadOnlyDoubleWrapper width;
    private double _width;
    private ReadOnlyDoubleWrapper height;
    private double _height;
    private DoubleProperty minWidth;
    private DoubleProperty minHeight;
    private DoubleProperty prefWidth;
    private DoubleProperty prefHeight;
    private DoubleProperty maxWidth;
    private DoubleProperty maxHeight;
    private Shape _shape;
    private boolean cornersValid;
    private List<CornerRadii> normalizedFillCorners;
    private List<CornerRadii> normalizedStrokeCorners;
    private Bounds boundingBox;
    private InvalidationListener imageChangeListener = observable -> {
        ReadOnlyObjectPropertyBase imageProperty = (ReadOnlyObjectPropertyBase) observable;
        Image image = (Image) imageProperty.getBean();
        Toolkit.ImageAccessor acc = Toolkit.getImageAccessor();
        if (image.getProgress() == 1.0d && !acc.isAnimation(image)) {
            removeImageListener(image);
        }
        impl_markDirty(DirtyBits.NODE_CONTENTS);
    };
    private boolean _snapToPixel = true;
    private ObjectProperty<Insets> padding = new StyleableObjectProperty<Insets>(Insets.EMPTY) { // from class: javafx.scene.layout.Region.2
        private Insets lastValidValue = Insets.EMPTY;

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Region.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "padding";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<Region, Insets> getCssMetaData() {
            return StyleableProperties.PADDING;
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        public void invalidated() {
            Insets newValue = get();
            if (newValue == null) {
                if (isBound()) {
                    unbind();
                }
                set(this.lastValidValue);
                throw new NullPointerException("cannot set padding to null");
            }
            if (!newValue.equals(this.lastValidValue)) {
                this.lastValidValue = newValue;
                Region.this.insets.fireValueChanged();
            }
        }
    };
    private final ObjectProperty<Background> background = new StyleableObjectProperty<Background>(null) { // from class: javafx.scene.layout.Region.3
        private Background old = null;

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Region.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "background";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<Region, Background> getCssMetaData() {
            return StyleableProperties.BACKGROUND;
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Background b2 = get();
            if (this.old != null) {
                if (this.old.equals(b2)) {
                    return;
                }
            } else if (b2 == null) {
                return;
            }
            if (this.old == null || b2 == null || !this.old.getOutsets().equals(b2.getOutsets())) {
                Region.this.impl_geomChanged();
                Region.this.insets.fireValueChanged();
            }
            if (b2 != null) {
                for (BackgroundImage i2 : b2.getImages()) {
                    Image image = i2.image;
                    Toolkit.ImageAccessor acc = Toolkit.getImageAccessor();
                    if (acc.isAnimation(image) || image.getProgress() < 1.0d) {
                        Region.this.addImageListener(image);
                    }
                }
            }
            if (this.old != null) {
                for (BackgroundImage i3 : this.old.getImages()) {
                    Region.this.removeImageListener(i3.image);
                }
            }
            Region.this.impl_markDirty(DirtyBits.SHAPE_FILL);
            Region.this.cornersValid = false;
            this.old = b2;
        }
    };
    private final ObjectProperty<Border> border = new StyleableObjectProperty<Border>(null) { // from class: javafx.scene.layout.Region.4
        private Border old = null;

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Region.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "border";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<Region, Border> getCssMetaData() {
            return StyleableProperties.BORDER;
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Border b2 = get();
            if (this.old != null) {
                if (this.old.equals(b2)) {
                    return;
                }
            } else if (b2 == null) {
                return;
            }
            if (this.old == null || b2 == null || !this.old.getOutsets().equals(b2.getOutsets())) {
                Region.this.impl_geomChanged();
            }
            if (this.old == null || b2 == null || !this.old.getInsets().equals(b2.getInsets())) {
                Region.this.insets.fireValueChanged();
            }
            if (b2 != null) {
                for (BorderImage i2 : b2.getImages()) {
                    Image image = i2.image;
                    Toolkit.ImageAccessor acc = Toolkit.getImageAccessor();
                    if (acc.isAnimation(image) || image.getProgress() < 1.0d) {
                        Region.this.addImageListener(image);
                    }
                }
            }
            if (this.old != null) {
                for (BorderImage i3 : this.old.getImages()) {
                    Region.this.removeImageListener(i3.image);
                }
            }
            Region.this.impl_markDirty(DirtyBits.SHAPE_STROKE);
            Region.this.cornersValid = false;
            this.old = b2;
        }
    };
    private final InsetsProperty insets = new InsetsProperty();
    private double snappedTopInset = 0.0d;
    private double snappedRightInset = 0.0d;
    private double snappedBottomInset = 0.0d;
    private double snappedLeftInset = 0.0d;
    private double _minWidth = -1.0d;
    private double _minHeight = -1.0d;
    private double _prefWidth = -1.0d;
    private double _prefHeight = -1.0d;
    private double _maxWidth = -1.0d;
    private double _maxHeight = -1.0d;
    private ObjectProperty<Shape> shape = null;
    private BooleanProperty scaleShape = null;
    private BooleanProperty centerShape = null;
    private BooleanProperty cacheShape = null;

    static double boundedSize(double min, double pref, double max) {
        double a2 = pref >= min ? pref : min;
        double b2 = min >= max ? min : max;
        return a2 <= b2 ? a2 : b2;
    }

    double adjustWidthByMargin(double width, Insets margin) {
        if (margin == null || margin == Insets.EMPTY) {
            return width;
        }
        boolean isSnapToPixel = isSnapToPixel();
        return (width - snapSpace(margin.getLeft(), isSnapToPixel)) - snapSpace(margin.getRight(), isSnapToPixel);
    }

    double adjustHeightByMargin(double height, Insets margin) {
        if (margin == null || margin == Insets.EMPTY) {
            return height;
        }
        boolean isSnapToPixel = isSnapToPixel();
        return (height - snapSpace(margin.getTop(), isSnapToPixel)) - snapSpace(margin.getBottom(), isSnapToPixel);
    }

    private static double snapSpace(double value, boolean snapToPixel) {
        return snapToPixel ? Math.round(value) : value;
    }

    private static double snapSize(double value, boolean snapToPixel) {
        return snapToPixel ? Math.ceil(value) : value;
    }

    private static double snapPosition(double value, boolean snapToPixel) {
        return snapToPixel ? Math.round(value) : value;
    }

    private static double snapPortion(double value, boolean snapToPixel) {
        if (!snapToPixel) {
            return value;
        }
        if (value == 0.0d) {
            return 0.0d;
        }
        return value > 0.0d ? Math.max(1.0d, Math.floor(value)) : Math.min(-1.0d, Math.ceil(value));
    }

    double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins, Function<Integer, Double> positionToWidth, double areaHeight, boolean fillHeight) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, fillHeight, isSnapToPixel());
    }

    static double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins, Function<Integer, Double> positionToWidth, double areaHeight, boolean fillHeight, boolean snapToPixel) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, fillHeight, getMinBaselineComplement(children), snapToPixel);
    }

    double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins, Function<Integer, Double> positionToWidth, double areaHeight, boolean fillHeight, double minComplement) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, fillHeight, minComplement, isSnapToPixel());
    }

    static double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins, Function<Integer, Double> positionToWidth, double areaHeight, boolean fillHeight, double minComplement, boolean snapToPixel) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, (Function<Integer, Boolean>) t2 -> {
            return Boolean.valueOf(fillHeight);
        }, minComplement, snapToPixel);
    }

    double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins, Function<Integer, Double> positionToWidth, double areaHeight, Function<Integer, Boolean> fillHeight, double minComplement) {
        return getAreaBaselineOffset(children, margins, positionToWidth, areaHeight, fillHeight, minComplement, isSnapToPixel());
    }

    static double getAreaBaselineOffset(List<Node> children, Callback<Node, Insets> margins, Function<Integer, Double> positionToWidth, double areaHeight, Function<Integer, Boolean> fillHeight, double minComplement, boolean snapToPixel) {
        double dMax;
        double b2 = 0.0d;
        for (int i2 = 0; i2 < children.size(); i2++) {
            Node n2 = children.get(i2);
            Insets margin = margins.call(n2);
            double top = margin != null ? snapSpace(margin.getTop(), snapToPixel) : 0.0d;
            double bottom = margin != null ? snapSpace(margin.getBottom(), snapToPixel) : 0.0d;
            double bo2 = n2.getBaselineOffset();
            if (bo2 == Double.NEGATIVE_INFINITY) {
                double alt = -1.0d;
                if (n2.getContentBias() == Orientation.HORIZONTAL) {
                    alt = positionToWidth.apply(Integer.valueOf(i2)).doubleValue();
                }
                if (fillHeight.apply(Integer.valueOf(i2)).booleanValue()) {
                    dMax = Math.max(b2, top + boundedSize(n2.minHeight(alt), ((areaHeight - minComplement) - top) - bottom, n2.maxHeight(alt)));
                } else {
                    dMax = Math.max(b2, top + boundedSize(n2.minHeight(alt), n2.prefHeight(alt), Math.min(n2.maxHeight(alt), ((areaHeight - minComplement) - top) - bottom)));
                }
            } else {
                dMax = Math.max(b2, top + bo2);
            }
            b2 = dMax;
        }
        return b2;
    }

    static double getMinBaselineComplement(List<Node> children) {
        return getBaselineComplement(children, true, false);
    }

    static double getPrefBaselineComplement(List<Node> children) {
        return getBaselineComplement(children, false, false);
    }

    static double getMaxBaselineComplement(List<Node> children) {
        return getBaselineComplement(children, false, true);
    }

    private static double getBaselineComplement(List<Node> children, boolean min, boolean max) {
        double bc2 = 0.0d;
        for (Node n2 : children) {
            double bo2 = n2.getBaselineOffset();
            if (bo2 != Double.NEGATIVE_INFINITY) {
                if (n2.isResizable()) {
                    bc2 = Math.max(bc2, (min ? n2.minHeight(-1.0d) : max ? n2.maxHeight(-1.0d) : n2.prefHeight(-1.0d)) - bo2);
                } else {
                    bc2 = Math.max(bc2, n2.getLayoutBounds().getHeight() - bo2);
                }
            }
        }
        return bc2;
    }

    static double computeXOffset(double width, double contentWidth, HPos hpos) {
        switch (hpos) {
            case LEFT:
                return 0.0d;
            case CENTER:
                return (width - contentWidth) / 2.0d;
            case RIGHT:
                return width - contentWidth;
            default:
                throw new AssertionError((Object) "Unhandled hPos");
        }
    }

    static double computeYOffset(double height, double contentHeight, VPos vpos) {
        switch (vpos) {
            case BASELINE:
            case TOP:
                return 0.0d;
            case CENTER:
                return (height - contentHeight) / 2.0d;
            case BOTTOM:
                return height - contentHeight;
            default:
                throw new AssertionError((Object) "Unhandled vPos");
        }
    }

    static double[] createDoubleArray(int length, double value) {
        double[] array = new double[length];
        for (int i2 = 0; i2 < length; i2++) {
            array[i2] = value;
        }
        return array;
    }

    public Region() {
        setPickOnBounds(true);
    }

    public final boolean isSnapToPixel() {
        return this._snapToPixel;
    }

    public final void setSnapToPixel(boolean value) {
        if (this.snapToPixel == null) {
            if (this._snapToPixel != value) {
                this._snapToPixel = value;
                updateSnappedInsets();
                requestParentLayout();
                return;
            }
            return;
        }
        this.snapToPixel.set(value);
    }

    public final BooleanProperty snapToPixelProperty() {
        if (this.snapToPixel == null) {
            this.snapToPixel = new StyleableBooleanProperty(this._snapToPixel) { // from class: javafx.scene.layout.Region.1
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Region.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "snapToPixel";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.SNAP_TO_PIXEL;
                }

                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    boolean value = get();
                    if (Region.this._snapToPixel != value) {
                        Region.this._snapToPixel = value;
                        Region.this.updateSnappedInsets();
                        Region.this.requestParentLayout();
                    }
                }
            };
        }
        return this.snapToPixel;
    }

    public final void setPadding(Insets value) {
        this.padding.set(value);
    }

    public final Insets getPadding() {
        return this.padding.get();
    }

    public final ObjectProperty<Insets> paddingProperty() {
        return this.padding;
    }

    public final void setBackground(Background value) {
        this.background.set(value);
    }

    public final Background getBackground() {
        return this.background.get();
    }

    public final ObjectProperty<Background> backgroundProperty() {
        return this.background;
    }

    public final void setBorder(Border value) {
        this.border.set(value);
    }

    public final Border getBorder() {
        return this.border.get();
    }

    public final ObjectProperty<Border> borderProperty() {
        return this.border;
    }

    void addImageListener(Image image) {
        Toolkit.ImageAccessor acc = Toolkit.getImageAccessor();
        acc.getImageProperty(image).addListener(this.imageChangeListener);
    }

    void removeImageListener(Image image) {
        Toolkit.ImageAccessor acc = Toolkit.getImageAccessor();
        acc.getImageProperty(image).removeListener(this.imageChangeListener);
    }

    public final ObjectProperty<Insets> opaqueInsetsProperty() {
        if (this.opaqueInsets == null) {
            this.opaqueInsets = new StyleableObjectProperty<Insets>() { // from class: javafx.scene.layout.Region.5
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Region.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "opaqueInsets";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Region, Insets> getCssMetaData() {
                    return StyleableProperties.OPAQUE_INSETS;
                }

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Region.this.impl_markDirty(DirtyBits.SHAPE_FILL);
                }
            };
        }
        return this.opaqueInsets;
    }

    public final void setOpaqueInsets(Insets value) {
        opaqueInsetsProperty().set(value);
    }

    public final Insets getOpaqueInsets() {
        if (this.opaqueInsets == null) {
            return null;
        }
        return this.opaqueInsets.get();
    }

    public final Insets getInsets() {
        return this.insets.get();
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/Region$InsetsProperty.class */
    private final class InsetsProperty extends ReadOnlyObjectProperty<Insets> {
        private Insets cache;
        private ExpressionHelper<Insets> helper;

        private InsetsProperty() {
            this.cache = null;
            this.helper = null;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Region.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "insets";
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void addListener(ChangeListener<? super Insets> listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void removeListener(ChangeListener<? super Insets> listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        void fireValueChanged() {
            this.cache = null;
            Region.this.updateSnappedInsets();
            Region.this.requestLayout();
            ExpressionHelper.fireValueChangedEvent(this.helper);
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public Insets get() {
            if (Region.this._shape != null) {
                return Region.this.getPadding();
            }
            Border b2 = Region.this.getBorder();
            if (b2 == null || Insets.EMPTY.equals(b2.getInsets())) {
                return Region.this.getPadding();
            }
            if (this.cache == null) {
                Insets borderInsets = b2.getInsets();
                Insets paddingInsets = Region.this.getPadding();
                this.cache = new Insets(borderInsets.getTop() + paddingInsets.getTop(), borderInsets.getRight() + paddingInsets.getRight(), borderInsets.getBottom() + paddingInsets.getBottom(), borderInsets.getLeft() + paddingInsets.getLeft());
            }
            return this.cache;
        }
    }

    public final ReadOnlyObjectProperty<Insets> insetsProperty() {
        return this.insets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSnappedInsets() {
        Insets insets = getInsets();
        if (this._snapToPixel) {
            this.snappedTopInset = Math.ceil(insets.getTop());
            this.snappedRightInset = Math.ceil(insets.getRight());
            this.snappedBottomInset = Math.ceil(insets.getBottom());
            this.snappedLeftInset = Math.ceil(insets.getLeft());
            return;
        }
        this.snappedTopInset = insets.getTop();
        this.snappedRightInset = insets.getRight();
        this.snappedBottomInset = insets.getBottom();
        this.snappedLeftInset = insets.getLeft();
    }

    protected void setWidth(double value) {
        if (this.width == null) {
            widthChanged(value);
        } else {
            this.width.set(value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void widthChanged(double value) {
        if (value != this._width) {
            this._width = value;
            this.cornersValid = false;
            this.boundingBox = null;
            impl_layoutBoundsChanged();
            impl_geomChanged();
            impl_markDirty(DirtyBits.NODE_GEOMETRY);
            setNeedsLayout(true);
            requestParentLayout();
        }
    }

    public final double getWidth() {
        return this.width == null ? this._width : this.width.get();
    }

    public final ReadOnlyDoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new ReadOnlyDoubleWrapper(this._width) { // from class: javafx.scene.layout.Region.6
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Region.this.widthChanged(get());
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Region.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.WIDTH_TAG_NAME;
                }
            };
        }
        return this.width.getReadOnlyProperty();
    }

    protected void setHeight(double value) {
        if (this.height == null) {
            heightChanged(value);
        } else {
            this.height.set(value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void heightChanged(double value) {
        if (this._height != value) {
            this._height = value;
            this.cornersValid = false;
            this.boundingBox = null;
            impl_geomChanged();
            impl_layoutBoundsChanged();
            impl_markDirty(DirtyBits.NODE_GEOMETRY);
            setNeedsLayout(true);
            requestParentLayout();
        }
    }

    public final double getHeight() {
        return this.height == null ? this._height : this.height.get();
    }

    public final ReadOnlyDoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new ReadOnlyDoubleWrapper(this._height) { // from class: javafx.scene.layout.Region.7
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Region.this.heightChanged(get());
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Region.this;
                }

                @Override // javafx.beans.property.SimpleDoubleProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.HEIGHT_TAG_NAME;
                }
            };
        }
        return this.height.getReadOnlyProperty();
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/Region$MinPrefMaxProperty.class */
    private final class MinPrefMaxProperty extends StyleableDoubleProperty {
        private final String name;
        private final CssMetaData<? extends Styleable, Number> cssMetaData;

        MinPrefMaxProperty(String name, double initialValue, CssMetaData<? extends Styleable, Number> cssMetaData) {
            super(initialValue);
            this.name = name;
            this.cssMetaData = cssMetaData;
        }

        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            Region.this.requestParentLayout();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Region.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return this.name;
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Number> getCssMetaData() {
            return this.cssMetaData;
        }
    }

    public final void setMinWidth(double value) {
        if (this.minWidth == null) {
            this._minWidth = value;
            requestParentLayout();
        } else {
            this.minWidth.set(value);
        }
    }

    public final double getMinWidth() {
        return this.minWidth == null ? this._minWidth : this.minWidth.get();
    }

    public final DoubleProperty minWidthProperty() {
        if (this.minWidth == null) {
            this.minWidth = new MinPrefMaxProperty("minWidth", this._minWidth, StyleableProperties.MIN_WIDTH);
        }
        return this.minWidth;
    }

    public final void setMinHeight(double value) {
        if (this.minHeight == null) {
            this._minHeight = value;
            requestParentLayout();
        } else {
            this.minHeight.set(value);
        }
    }

    public final double getMinHeight() {
        return this.minHeight == null ? this._minHeight : this.minHeight.get();
    }

    public final DoubleProperty minHeightProperty() {
        if (this.minHeight == null) {
            this.minHeight = new MinPrefMaxProperty("minHeight", this._minHeight, StyleableProperties.MIN_HEIGHT);
        }
        return this.minHeight;
    }

    public void setMinSize(double minWidth, double minHeight) {
        setMinWidth(minWidth);
        setMinHeight(minHeight);
    }

    public final void setPrefWidth(double value) {
        if (this.prefWidth == null) {
            this._prefWidth = value;
            requestParentLayout();
        } else {
            this.prefWidth.set(value);
        }
    }

    public final double getPrefWidth() {
        return this.prefWidth == null ? this._prefWidth : this.prefWidth.get();
    }

    public final DoubleProperty prefWidthProperty() {
        if (this.prefWidth == null) {
            this.prefWidth = new MinPrefMaxProperty("prefWidth", this._prefWidth, StyleableProperties.PREF_WIDTH);
        }
        return this.prefWidth;
    }

    public final void setPrefHeight(double value) {
        if (this.prefHeight == null) {
            this._prefHeight = value;
            requestParentLayout();
        } else {
            this.prefHeight.set(value);
        }
    }

    public final double getPrefHeight() {
        return this.prefHeight == null ? this._prefHeight : this.prefHeight.get();
    }

    public final DoubleProperty prefHeightProperty() {
        if (this.prefHeight == null) {
            this.prefHeight = new MinPrefMaxProperty("prefHeight", this._prefHeight, StyleableProperties.PREF_HEIGHT);
        }
        return this.prefHeight;
    }

    public void setPrefSize(double prefWidth, double prefHeight) {
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
    }

    public final void setMaxWidth(double value) {
        if (this.maxWidth == null) {
            this._maxWidth = value;
            requestParentLayout();
        } else {
            this.maxWidth.set(value);
        }
    }

    public final double getMaxWidth() {
        return this.maxWidth == null ? this._maxWidth : this.maxWidth.get();
    }

    public final DoubleProperty maxWidthProperty() {
        if (this.maxWidth == null) {
            this.maxWidth = new MinPrefMaxProperty("maxWidth", this._maxWidth, StyleableProperties.MAX_WIDTH);
        }
        return this.maxWidth;
    }

    public final void setMaxHeight(double value) {
        if (this.maxHeight == null) {
            this._maxHeight = value;
            requestParentLayout();
        } else {
            this.maxHeight.set(value);
        }
    }

    public final double getMaxHeight() {
        return this.maxHeight == null ? this._maxHeight : this.maxHeight.get();
    }

    public final DoubleProperty maxHeightProperty() {
        if (this.maxHeight == null) {
            this.maxHeight = new MinPrefMaxProperty("maxHeight", this._maxHeight, StyleableProperties.MAX_HEIGHT);
        }
        return this.maxHeight;
    }

    public void setMaxSize(double maxWidth, double maxHeight) {
        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
    }

    public final Shape getShape() {
        return this.shape == null ? this._shape : this.shape.get();
    }

    public final void setShape(Shape value) {
        shapeProperty().set(value);
    }

    public final ObjectProperty<Shape> shapeProperty() {
        if (this.shape == null) {
            this.shape = new ShapeProperty();
        }
        return this.shape;
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/Region$ShapeProperty.class */
    private final class ShapeProperty extends StyleableObjectProperty<Shape> implements Runnable {
        private ShapeProperty() {
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Region.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "shape";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<Region, Shape> getCssMetaData() {
            return StyleableProperties.SHAPE;
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Shape value = get();
            if (Region.this._shape != value) {
                if (Region.this._shape != null) {
                    Region.this._shape.impl_setShapeChangeListener(null);
                }
                if (value != null) {
                    value.impl_setShapeChangeListener(this);
                }
                run();
                if (Region.this._shape == null || value == null) {
                    Region.this.insets.fireValueChanged();
                }
                Region.this._shape = value;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            Region.this.impl_geomChanged();
            Region.this.impl_markDirty(DirtyBits.REGION_SHAPE);
        }
    }

    public final void setScaleShape(boolean value) {
        scaleShapeProperty().set(value);
    }

    public final boolean isScaleShape() {
        if (this.scaleShape == null) {
            return true;
        }
        return this.scaleShape.get();
    }

    public final BooleanProperty scaleShapeProperty() {
        if (this.scaleShape == null) {
            this.scaleShape = new StyleableBooleanProperty(true) { // from class: javafx.scene.layout.Region.8
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Region.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "scaleShape";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.SCALE_SHAPE;
                }

                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    Region.this.impl_geomChanged();
                    Region.this.impl_markDirty(DirtyBits.REGION_SHAPE);
                }
            };
        }
        return this.scaleShape;
    }

    public final void setCenterShape(boolean value) {
        centerShapeProperty().set(value);
    }

    public final boolean isCenterShape() {
        if (this.centerShape == null) {
            return true;
        }
        return this.centerShape.get();
    }

    public final BooleanProperty centerShapeProperty() {
        if (this.centerShape == null) {
            this.centerShape = new StyleableBooleanProperty(true) { // from class: javafx.scene.layout.Region.9
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Region.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "centerShape";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.POSITION_SHAPE;
                }

                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    Region.this.impl_geomChanged();
                    Region.this.impl_markDirty(DirtyBits.REGION_SHAPE);
                }
            };
        }
        return this.centerShape;
    }

    public final void setCacheShape(boolean value) {
        cacheShapeProperty().set(value);
    }

    public final boolean isCacheShape() {
        if (this.cacheShape == null) {
            return true;
        }
        return this.cacheShape.get();
    }

    public final BooleanProperty cacheShapeProperty() {
        if (this.cacheShape == null) {
            this.cacheShape = new StyleableBooleanProperty(true) { // from class: javafx.scene.layout.Region.10
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Region.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "cacheShape";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.CACHE_SHAPE;
                }
            };
        }
        return this.cacheShape;
    }

    @Override // javafx.scene.Node
    public boolean isResizable() {
        return true;
    }

    @Override // javafx.scene.Node
    public void resize(double width, double height) {
        setWidth(width);
        setHeight(height);
        PlatformLogger logger = Logging.getLayoutLogger();
        if (logger.isLoggable(PlatformLogger.Level.FINER)) {
            logger.finer(toString() + " resized to " + width + " x " + height);
        }
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public final double minWidth(double height) {
        double override = getMinWidth();
        if (override == -1.0d) {
            return super.minWidth(height);
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefWidth(height);
        }
        if (Double.isNaN(override) || override < 0.0d) {
            return 0.0d;
        }
        return override;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public final double minHeight(double width) {
        double override = getMinHeight();
        if (override == -1.0d) {
            return super.minHeight(width);
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefHeight(width);
        }
        if (Double.isNaN(override) || override < 0.0d) {
            return 0.0d;
        }
        return override;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public final double prefWidth(double height) {
        double override = getPrefWidth();
        if (override == -1.0d) {
            return super.prefWidth(height);
        }
        if (Double.isNaN(override) || override < 0.0d) {
            return 0.0d;
        }
        return override;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public final double prefHeight(double width) {
        double override = getPrefHeight();
        if (override == -1.0d) {
            return super.prefHeight(width);
        }
        if (Double.isNaN(override) || override < 0.0d) {
            return 0.0d;
        }
        return override;
    }

    @Override // javafx.scene.Node
    public final double maxWidth(double height) {
        double override = getMaxWidth();
        if (override == -1.0d) {
            return computeMaxWidth(height);
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefWidth(height);
        }
        if (Double.isNaN(override) || override < 0.0d) {
            return 0.0d;
        }
        return override;
    }

    @Override // javafx.scene.Node
    public final double maxHeight(double width) {
        double override = getMaxHeight();
        if (override == -1.0d) {
            return computeMaxHeight(width);
        }
        if (override == Double.NEGATIVE_INFINITY) {
            return prefHeight(width);
        }
        if (Double.isNaN(override) || override < 0.0d) {
            return 0.0d;
        }
        return override;
    }

    @Override // javafx.scene.Parent
    protected double computeMinWidth(double height) {
        return getInsets().getLeft() + getInsets().getRight();
    }

    @Override // javafx.scene.Parent
    protected double computeMinHeight(double width) {
        return getInsets().getTop() + getInsets().getBottom();
    }

    @Override // javafx.scene.Parent
    protected double computePrefWidth(double height) {
        double w2 = super.computePrefWidth(height);
        return getInsets().getLeft() + w2 + getInsets().getRight();
    }

    @Override // javafx.scene.Parent
    protected double computePrefHeight(double width) {
        double h2 = super.computePrefHeight(width);
        return getInsets().getTop() + h2 + getInsets().getBottom();
    }

    protected double computeMaxWidth(double height) {
        return Double.MAX_VALUE;
    }

    protected double computeMaxHeight(double width) {
        return Double.MAX_VALUE;
    }

    protected double snapSpace(double value) {
        return snapSpace(value, isSnapToPixel());
    }

    protected double snapSize(double value) {
        return snapSize(value, isSnapToPixel());
    }

    protected double snapPosition(double value) {
        return snapPosition(value, isSnapToPixel());
    }

    double snapPortion(double value) {
        return snapPortion(value, isSnapToPixel());
    }

    public final double snappedTopInset() {
        return this.snappedTopInset;
    }

    public final double snappedBottomInset() {
        return this.snappedBottomInset;
    }

    public final double snappedLeftInset() {
        return this.snappedLeftInset;
    }

    public final double snappedRightInset() {
        return this.snappedRightInset;
    }

    double computeChildMinAreaWidth(Node child, Insets margin) {
        return computeChildMinAreaWidth(child, -1.0d, margin, -1.0d, false);
    }

    double computeChildMinAreaWidth(Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        boolean snap = isSnapToPixel();
        double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0.0d;
        double right = margin != null ? snapSpace(margin.getRight(), snap) : 0.0d;
        double alt = -1.0d;
        if (height != -1.0d && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) {
            double top = margin != null ? snapSpace(margin.getTop(), snap) : 0.0d;
            double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0.0d;
            double bo2 = child.getBaselineOffset();
            double contentHeight = (bo2 != Double.NEGATIVE_INFINITY || baselineComplement == -1.0d) ? (height - top) - bottom : ((height - top) - bottom) - baselineComplement;
            alt = fillHeight ? snapSize(boundedSize(child.minHeight(-1.0d), contentHeight, child.maxHeight(-1.0d))) : snapSize(boundedSize(child.minHeight(-1.0d), child.prefHeight(-1.0d), Math.min(child.maxHeight(-1.0d), contentHeight)));
        }
        return left + snapSize(child.minWidth(alt)) + right;
    }

    double computeChildMinAreaHeight(Node child, Insets margin) {
        return computeChildMinAreaHeight(child, -1.0d, margin, -1.0d);
    }

    double computeChildMinAreaHeight(Node child, double minBaselineComplement, Insets margin, double width) {
        boolean snap = isSnapToPixel();
        double top = margin != null ? snapSpace(margin.getTop(), snap) : 0.0d;
        double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0.0d;
        double alt = -1.0d;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) {
            double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0.0d;
            double right = margin != null ? snapSpace(margin.getRight(), snap) : 0.0d;
            alt = snapSize(width != -1.0d ? boundedSize(child.minWidth(-1.0d), (width - left) - right, child.maxWidth(-1.0d)) : child.maxWidth(-1.0d));
        }
        if (minBaselineComplement != -1.0d) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == Double.NEGATIVE_INFINITY) {
                return top + snapSize(child.minHeight(alt)) + bottom + minBaselineComplement;
            }
            return baseline + minBaselineComplement;
        }
        return top + snapSize(child.minHeight(alt)) + bottom;
    }

    double computeChildPrefAreaWidth(Node child, Insets margin) {
        return computeChildPrefAreaWidth(child, -1.0d, margin, -1.0d, false);
    }

    double computeChildPrefAreaWidth(Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        boolean snap = isSnapToPixel();
        double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0.0d;
        double right = margin != null ? snapSpace(margin.getRight(), snap) : 0.0d;
        double alt = -1.0d;
        if (height != -1.0d && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) {
            double top = margin != null ? snapSpace(margin.getTop(), snap) : 0.0d;
            double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0.0d;
            double bo2 = child.getBaselineOffset();
            double contentHeight = (bo2 != Double.NEGATIVE_INFINITY || baselineComplement == -1.0d) ? (height - top) - bottom : ((height - top) - bottom) - baselineComplement;
            alt = fillHeight ? snapSize(boundedSize(child.minHeight(-1.0d), contentHeight, child.maxHeight(-1.0d))) : snapSize(boundedSize(child.minHeight(-1.0d), child.prefHeight(-1.0d), Math.min(child.maxHeight(-1.0d), contentHeight)));
        }
        return left + snapSize(boundedSize(child.minWidth(alt), child.prefWidth(alt), child.maxWidth(alt))) + right;
    }

    double computeChildPrefAreaHeight(Node child, Insets margin) {
        return computeChildPrefAreaHeight(child, -1.0d, margin, -1.0d);
    }

    double computeChildPrefAreaHeight(Node child, double prefBaselineComplement, Insets margin, double width) {
        boolean snap = isSnapToPixel();
        double top = margin != null ? snapSpace(margin.getTop(), snap) : 0.0d;
        double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0.0d;
        double alt = -1.0d;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) {
            double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0.0d;
            double right = margin != null ? snapSpace(margin.getRight(), snap) : 0.0d;
            alt = snapSize(boundedSize(child.minWidth(-1.0d), width != -1.0d ? (width - left) - right : child.prefWidth(-1.0d), child.maxWidth(-1.0d)));
        }
        if (prefBaselineComplement != -1.0d) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == Double.NEGATIVE_INFINITY) {
                return top + snapSize(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt))) + bottom + prefBaselineComplement;
            }
            return top + baseline + prefBaselineComplement + bottom;
        }
        return top + snapSize(boundedSize(child.minHeight(alt), child.prefHeight(alt), child.maxHeight(alt))) + bottom;
    }

    double computeChildMaxAreaWidth(Node child, double baselineComplement, Insets margin, double height, boolean fillHeight) {
        double max = child.maxWidth(-1.0d);
        if (max == Double.MAX_VALUE) {
            return max;
        }
        boolean snap = isSnapToPixel();
        double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0.0d;
        double right = margin != null ? snapSpace(margin.getRight(), snap) : 0.0d;
        double alt = -1.0d;
        if (height != -1.0d && child.isResizable() && child.getContentBias() == Orientation.VERTICAL) {
            double top = margin != null ? snapSpace(margin.getTop(), snap) : 0.0d;
            double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0.0d;
            double bo2 = child.getBaselineOffset();
            double contentHeight = (bo2 != Double.NEGATIVE_INFINITY || baselineComplement == -1.0d) ? (height - top) - bottom : ((height - top) - bottom) - baselineComplement;
            if (fillHeight) {
                alt = snapSize(boundedSize(child.minHeight(-1.0d), contentHeight, child.maxHeight(-1.0d)));
            } else {
                alt = snapSize(boundedSize(child.minHeight(-1.0d), child.prefHeight(-1.0d), Math.min(child.maxHeight(-1.0d), contentHeight)));
            }
            max = child.maxWidth(alt);
        }
        return left + snapSize(boundedSize(child.minWidth(alt), max, Double.MAX_VALUE)) + right;
    }

    double computeChildMaxAreaHeight(Node child, double maxBaselineComplement, Insets margin, double width) {
        double max = child.maxHeight(-1.0d);
        if (max == Double.MAX_VALUE) {
            return max;
        }
        boolean snap = isSnapToPixel();
        double top = margin != null ? snapSpace(margin.getTop(), snap) : 0.0d;
        double bottom = margin != null ? snapSpace(margin.getBottom(), snap) : 0.0d;
        double alt = -1.0d;
        if (child.isResizable() && child.getContentBias() == Orientation.HORIZONTAL) {
            double left = margin != null ? snapSpace(margin.getLeft(), snap) : 0.0d;
            double right = margin != null ? snapSpace(margin.getRight(), snap) : 0.0d;
            alt = snapSize(width != -1.0d ? boundedSize(child.minWidth(-1.0d), (width - left) - right, child.maxWidth(-1.0d)) : child.minWidth(-1.0d));
            max = child.maxHeight(alt);
        }
        if (maxBaselineComplement != -1.0d) {
            double baseline = child.getBaselineOffset();
            if (child.isResizable() && baseline == Double.NEGATIVE_INFINITY) {
                return top + snapSize(boundedSize(child.minHeight(alt), child.maxHeight(alt), Double.MAX_VALUE)) + bottom + maxBaselineComplement;
            }
            return top + baseline + maxBaselineComplement + bottom;
        }
        return top + snapSize(boundedSize(child.minHeight(alt), max, Double.MAX_VALUE)) + bottom;
    }

    double computeMaxMinAreaWidth(List<Node> children, Callback<Node, Insets> margins) {
        return getMaxAreaWidth(children, margins, new double[]{-1.0d}, false, true);
    }

    double computeMaxMinAreaWidth(List<Node> children, Callback<Node, Insets> margins, double height, boolean fillHeight) {
        return getMaxAreaWidth(children, margins, new double[]{height}, fillHeight, true);
    }

    double computeMaxMinAreaWidth(List<Node> children, Callback<Node, Insets> childMargins, double[] childHeights, boolean fillHeight) {
        return getMaxAreaWidth(children, childMargins, childHeights, fillHeight, true);
    }

    double computeMaxMinAreaHeight(List<Node> children, Callback<Node, Insets> margins, VPos valignment) {
        return getMaxAreaHeight(children, margins, null, valignment, true);
    }

    double computeMaxMinAreaHeight(List<Node> children, Callback<Node, Insets> margins, VPos valignment, double width) {
        return getMaxAreaHeight(children, margins, new double[]{width}, valignment, true);
    }

    double computeMaxMinAreaHeight(List<Node> children, Callback<Node, Insets> childMargins, double[] childWidths, VPos valignment) {
        return getMaxAreaHeight(children, childMargins, childWidths, valignment, true);
    }

    double computeMaxPrefAreaWidth(List<Node> children, Callback<Node, Insets> margins) {
        return getMaxAreaWidth(children, margins, new double[]{-1.0d}, false, false);
    }

    double computeMaxPrefAreaWidth(List<Node> children, Callback<Node, Insets> margins, double height, boolean fillHeight) {
        return getMaxAreaWidth(children, margins, new double[]{height}, fillHeight, false);
    }

    double computeMaxPrefAreaWidth(List<Node> children, Callback<Node, Insets> childMargins, double[] childHeights, boolean fillHeight) {
        return getMaxAreaWidth(children, childMargins, childHeights, fillHeight, false);
    }

    double computeMaxPrefAreaHeight(List<Node> children, Callback<Node, Insets> margins, VPos valignment) {
        return getMaxAreaHeight(children, margins, null, valignment, false);
    }

    double computeMaxPrefAreaHeight(List<Node> children, Callback<Node, Insets> margins, double width, VPos valignment) {
        return getMaxAreaHeight(children, margins, new double[]{width}, valignment, false);
    }

    double computeMaxPrefAreaHeight(List<Node> children, Callback<Node, Insets> childMargins, double[] childWidths, VPos valignment) {
        return getMaxAreaHeight(children, childMargins, childWidths, valignment, false);
    }

    static Vec2d boundedNodeSizeWithBias(Node node, double areaWidth, double areaHeight, boolean fillWidth, boolean fillHeight, Vec2d result) {
        double childHeight;
        double childWidth;
        if (result == null) {
            result = new Vec2d();
        }
        Orientation bias = node.getContentBias();
        if (bias == null) {
            childWidth = boundedSize(node.minWidth(-1.0d), fillWidth ? areaWidth : Math.min(areaWidth, node.prefWidth(-1.0d)), node.maxWidth(-1.0d));
            childHeight = boundedSize(node.minHeight(-1.0d), fillHeight ? areaHeight : Math.min(areaHeight, node.prefHeight(-1.0d)), node.maxHeight(-1.0d));
        } else if (bias == Orientation.HORIZONTAL) {
            childWidth = boundedSize(node.minWidth(-1.0d), fillWidth ? areaWidth : Math.min(areaWidth, node.prefWidth(-1.0d)), node.maxWidth(-1.0d));
            childHeight = boundedSize(node.minHeight(childWidth), fillHeight ? areaHeight : Math.min(areaHeight, node.prefHeight(childWidth)), node.maxHeight(childWidth));
        } else {
            childHeight = boundedSize(node.minHeight(-1.0d), fillHeight ? areaHeight : Math.min(areaHeight, node.prefHeight(-1.0d)), node.maxHeight(-1.0d));
            childWidth = boundedSize(node.minWidth(childHeight), fillWidth ? areaWidth : Math.min(areaWidth, node.prefWidth(childHeight)), node.maxWidth(childHeight));
        }
        result.set(childWidth, childHeight);
        return result;
    }

    private double getMaxAreaHeight(List<Node> children, Callback<Node, Insets> childMargins, double[] childWidths, VPos valignment, boolean minimum) {
        double dComputeChildPrefAreaHeight;
        double singleChildWidth = childWidths == null ? -1.0d : childWidths.length == 1 ? childWidths[0] : Double.NaN;
        if (valignment == VPos.BASELINE) {
            double maxAbove = 0.0d;
            double maxBelow = 0.0d;
            int maxPos = children.size();
            for (int i2 = 0; i2 < maxPos; i2++) {
                Node child = children.get(i2);
                double childWidth = Double.isNaN(singleChildWidth) ? childWidths[i2] : singleChildWidth;
                Insets margin = childMargins.call(child);
                double top = margin != null ? snapSpace(margin.getTop()) : 0.0d;
                double bottom = margin != null ? snapSpace(margin.getBottom()) : 0.0d;
                double baseline = child.getBaselineOffset();
                double childHeight = minimum ? snapSize(child.minHeight(childWidth)) : snapSize(child.prefHeight(childWidth));
                if (baseline == Double.NEGATIVE_INFINITY) {
                    maxAbove = Math.max(maxAbove, childHeight + top);
                } else {
                    maxAbove = Math.max(maxAbove, baseline + top);
                    maxBelow = Math.max(maxBelow, (snapSpace(minimum ? snapSize(child.minHeight(childWidth)) : snapSize(child.prefHeight(childWidth))) - baseline) + bottom);
                }
            }
            return maxAbove + maxBelow;
        }
        double max = 0.0d;
        int maxPos2 = children.size();
        for (int i3 = 0; i3 < maxPos2; i3++) {
            Node child2 = children.get(i3);
            Insets margin2 = childMargins.call(child2);
            double childWidth2 = Double.isNaN(singleChildWidth) ? childWidths[i3] : singleChildWidth;
            double d2 = max;
            if (minimum) {
                dComputeChildPrefAreaHeight = computeChildMinAreaHeight(child2, -1.0d, margin2, childWidth2);
            } else {
                dComputeChildPrefAreaHeight = computeChildPrefAreaHeight(child2, -1.0d, margin2, childWidth2);
            }
            max = Math.max(d2, dComputeChildPrefAreaHeight);
        }
        return max;
    }

    private double getMaxAreaWidth(List<Node> children, Callback<Node, Insets> childMargins, double[] childHeights, boolean fillHeight, boolean minimum) {
        double dComputeChildPrefAreaWidth;
        double singleChildHeight = childHeights == null ? -1.0d : childHeights.length == 1 ? childHeights[0] : Double.NaN;
        double max = 0.0d;
        int maxPos = children.size();
        for (int i2 = 0; i2 < maxPos; i2++) {
            Node child = children.get(i2);
            Insets margin = childMargins.call(child);
            double childHeight = Double.isNaN(singleChildHeight) ? childHeights[i2] : singleChildHeight;
            double d2 = max;
            if (minimum) {
                dComputeChildPrefAreaWidth = computeChildMinAreaWidth(children.get(i2), -1.0d, margin, childHeight, fillHeight);
            } else {
                dComputeChildPrefAreaWidth = computeChildPrefAreaWidth(child, -1.0d, margin, childHeight, fillHeight);
            }
            max = Math.max(d2, dComputeChildPrefAreaWidth);
        }
        return max;
    }

    protected void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, HPos halignment, VPos valignment) {
        positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, Insets.EMPTY, halignment, valignment, isSnapToPixel());
    }

    public static void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, Insets margin, HPos halignment, VPos valignment, boolean isSnapToPixel) {
        Insets childMargin = margin != null ? margin : Insets.EMPTY;
        position(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, snapSpace(childMargin.getTop(), isSnapToPixel), snapSpace(childMargin.getRight(), isSnapToPixel), snapSpace(childMargin.getBottom(), isSnapToPixel), snapSpace(childMargin.getLeft(), isSnapToPixel), halignment, valignment, isSnapToPixel);
    }

    protected void layoutInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, HPos halignment, VPos valignment) {
        layoutInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, Insets.EMPTY, halignment, valignment);
    }

    protected void layoutInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, Insets margin, HPos halignment, VPos valignment) {
        layoutInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, margin, true, true, halignment, valignment);
    }

    protected void layoutInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, Insets margin, boolean fillWidth, boolean fillHeight, HPos halignment, VPos valignment) {
        layoutInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, margin, fillWidth, fillHeight, halignment, valignment, isSnapToPixel());
    }

    public static void layoutInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, Insets margin, boolean fillWidth, boolean fillHeight, HPos halignment, VPos valignment, boolean isSnapToPixel) {
        Insets childMargin = margin != null ? margin : Insets.EMPTY;
        double top = snapSpace(childMargin.getTop(), isSnapToPixel);
        double bottom = snapSpace(childMargin.getBottom(), isSnapToPixel);
        double left = snapSpace(childMargin.getLeft(), isSnapToPixel);
        double right = snapSpace(childMargin.getRight(), isSnapToPixel);
        if (valignment == VPos.BASELINE) {
            double bo2 = child.getBaselineOffset();
            if (bo2 == Double.NEGATIVE_INFINITY) {
                if (child.isResizable()) {
                    bottom += snapSpace(areaHeight - areaBaselineOffset, isSnapToPixel);
                } else {
                    top = snapSpace(areaBaselineOffset - child.getLayoutBounds().getHeight(), isSnapToPixel);
                }
            } else {
                top = snapSpace(areaBaselineOffset - bo2, isSnapToPixel);
            }
        }
        if (child.isResizable()) {
            Vec2d size = boundedNodeSizeWithBias(child, (areaWidth - left) - right, (areaHeight - top) - bottom, fillWidth, fillHeight, TEMP_VEC2D);
            child.resize(snapSize(size.f11926x, isSnapToPixel), snapSize(size.f11927y, isSnapToPixel));
        }
        position(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, top, right, bottom, left, halignment, valignment, isSnapToPixel);
    }

    private static void position(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, double topMargin, double rightMargin, double bottomMargin, double leftMargin, HPos hpos, VPos vpos, boolean isSnapToPixel) {
        double yoffset;
        double xoffset = leftMargin + computeXOffset((areaWidth - leftMargin) - rightMargin, child.getLayoutBounds().getWidth(), hpos);
        if (vpos == VPos.BASELINE) {
            double bo2 = child.getBaselineOffset();
            if (bo2 == Double.NEGATIVE_INFINITY) {
                yoffset = areaBaselineOffset - child.getLayoutBounds().getHeight();
            } else {
                yoffset = areaBaselineOffset - bo2;
            }
        } else {
            yoffset = topMargin + computeYOffset((areaHeight - topMargin) - bottomMargin, child.getLayoutBounds().getHeight(), vpos);
        }
        double x2 = snapPosition(areaX + xoffset, isSnapToPixel);
        double y2 = snapPosition(areaY + yoffset, isSnapToPixel);
        child.relocate(x2, y2);
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this._shape != null) {
            this._shape.impl_syncPeer();
        }
        NGRegion pg = (NGRegion) impl_getPeer();
        if (!this.cornersValid) {
            validateCorners();
        }
        boolean sizeChanged = impl_isDirty(DirtyBits.NODE_GEOMETRY);
        if (sizeChanged) {
            pg.setSize((float) getWidth(), (float) getHeight());
        }
        boolean shapeChanged = impl_isDirty(DirtyBits.REGION_SHAPE);
        if (shapeChanged) {
            pg.updateShape(this._shape, isScaleShape(), isCenterShape(), isCacheShape());
        }
        pg.updateFillCorners(this.normalizedFillCorners);
        boolean backgroundChanged = impl_isDirty(DirtyBits.SHAPE_FILL);
        Background bg2 = getBackground();
        if (backgroundChanged) {
            pg.updateBackground(bg2);
        }
        if (impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            pg.imagesUpdated();
        }
        pg.updateStrokeCorners(this.normalizedStrokeCorners);
        if (impl_isDirty(DirtyBits.SHAPE_STROKE)) {
            pg.updateBorder(getBorder());
        }
        if (sizeChanged || backgroundChanged || shapeChanged) {
            Insets i2 = getOpaqueInsets();
            if (this._shape != null) {
                if (i2 != null) {
                    pg.setOpaqueInsets((float) i2.getTop(), (float) i2.getRight(), (float) i2.getBottom(), (float) i2.getLeft());
                    return;
                } else {
                    pg.setOpaqueInsets(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
                    return;
                }
            }
            if (bg2 == null || bg2.isEmpty()) {
                pg.setOpaqueInsets(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
                return;
            }
            double[] trbl = new double[4];
            bg2.computeOpaqueInsets(getWidth(), getHeight(), trbl);
            if (i2 != null) {
                trbl[0] = Double.isNaN(trbl[0]) ? i2.getTop() : Double.isNaN(i2.getTop()) ? trbl[0] : Math.min(trbl[0], i2.getTop());
                trbl[1] = Double.isNaN(trbl[1]) ? i2.getRight() : Double.isNaN(i2.getRight()) ? trbl[1] : Math.min(trbl[1], i2.getRight());
                trbl[2] = Double.isNaN(trbl[2]) ? i2.getBottom() : Double.isNaN(i2.getBottom()) ? trbl[2] : Math.min(trbl[2], i2.getBottom());
                trbl[3] = Double.isNaN(trbl[3]) ? i2.getLeft() : Double.isNaN(i2.getLeft()) ? trbl[3] : Math.min(trbl[3], i2.getLeft());
            }
            pg.setOpaqueInsets((float) trbl[0], (float) trbl[1], (float) trbl[2], (float) trbl[3]);
        }
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public NGNode impl_createPeer() {
        return new NGRegion();
    }

    private boolean shapeContains(com.sun.javafx.geom.Shape shape, double x2, double y2, double topOffset, double rightOffset, double bottomOffset, double leftOffset) {
        double resX = x2;
        double resY = y2;
        RectBounds bounds = shape.getBounds();
        if (isScaleShape()) {
            resX = (resX - leftOffset) * (bounds.getWidth() / ((getWidth() - leftOffset) - rightOffset));
            resY = (resY - topOffset) * (bounds.getHeight() / ((getHeight() - topOffset) - bottomOffset));
            if (isCenterShape()) {
                resX += bounds.getMinX();
                resY += bounds.getMinY();
            }
        } else if (isCenterShape()) {
            double boundsWidth = bounds.getWidth();
            double boundsHeight = bounds.getHeight();
            double scaleFactorX = boundsWidth / ((boundsWidth - leftOffset) - rightOffset);
            double scaleFactorY = boundsHeight / ((boundsHeight - topOffset) - bottomOffset);
            resX = (scaleFactorX * (resX - (leftOffset + ((getWidth() - boundsWidth) / 2.0d)))) + bounds.getMinX();
            resY = (scaleFactorY * (resY - (topOffset + ((getHeight() - boundsHeight) / 2.0d)))) + bounds.getMinY();
        } else if (topOffset != 0.0d || rightOffset != 0.0d || bottomOffset != 0.0d || leftOffset != 0.0d) {
            double scaleFactorX2 = bounds.getWidth() / ((bounds.getWidth() - leftOffset) - rightOffset);
            double scaleFactorY2 = bounds.getHeight() / ((bounds.getHeight() - topOffset) - bottomOffset);
            resX = (scaleFactorX2 * ((resX - leftOffset) - bounds.getMinX())) + bounds.getMinX();
            resY = (scaleFactorY2 * ((resY - topOffset) - bounds.getMinY())) + bounds.getMinY();
        }
        return shape.contains((float) resX, (float) resY);
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        double x2 = getWidth();
        double y2 = getHeight();
        Background background = getBackground();
        if (this._shape != null) {
            if (background != null && !background.getFills().isEmpty()) {
                List<BackgroundFill> fills = background.getFills();
                double topO = Double.MAX_VALUE;
                double leftO = Double.MAX_VALUE;
                double bottomO = Double.MAX_VALUE;
                double rightO = Double.MAX_VALUE;
                int max = fills.size();
                for (int i2 = 0; i2 < max; i2++) {
                    BackgroundFill bf2 = fills.get(0);
                    topO = Math.min(topO, bf2.getInsets().getTop());
                    leftO = Math.min(leftO, bf2.getInsets().getLeft());
                    bottomO = Math.min(bottomO, bf2.getInsets().getBottom());
                    rightO = Math.min(rightO, bf2.getInsets().getRight());
                }
                return shapeContains(this._shape.impl_configShape(), localX, localY, topO, leftO, bottomO, rightO);
            }
            return false;
        }
        if (background != null) {
            List<BackgroundFill> fills2 = background.getFills();
            int max2 = fills2.size();
            for (int i3 = 0; i3 < max2; i3++) {
                BackgroundFill bgFill = fills2.get(i3);
                if (contains(localX, localY, 0.0d, 0.0d, x2, y2, bgFill.getInsets(), getNormalizedFillCorner(i3))) {
                    return true;
                }
            }
        }
        Border border = getBorder();
        if (border != null) {
            List<BorderStroke> strokes = border.getStrokes();
            int max3 = strokes.size();
            for (int i4 = 0; i4 < max3; i4++) {
                BorderStroke strokeBorder = strokes.get(i4);
                if (contains(localX, localY, 0.0d, 0.0d, x2, y2, strokeBorder.getWidths(), false, strokeBorder.getInsets(), getNormalizedStrokeCorner(i4))) {
                    return true;
                }
            }
            List<BorderImage> images = border.getImages();
            int max4 = images.size();
            for (int i5 = 0; i5 < max4; i5++) {
                BorderImage borderImage = images.get(i5);
                if (contains(localX, localY, 0.0d, 0.0d, x2, y2, borderImage.getWidths(), borderImage.isFilled(), borderImage.getInsets(), CornerRadii.EMPTY)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean contains(double px, double py, double x1, double y1, double x2, double y2, BorderWidths widths, boolean filled, Insets insets, CornerRadii rad) {
        if (filled) {
            if (contains(px, py, x1, y1, x2, y2, insets, rad)) {
                return true;
            }
            return false;
        }
        boolean insideOuterEdge = contains(px, py, x1, y1, x2, y2, insets, rad);
        if (insideOuterEdge) {
            boolean outsideInnerEdge = !contains(px, py, x1 + (widths.isLeftAsPercentage() ? getWidth() * widths.getLeft() : widths.getLeft()), y1 + (widths.isTopAsPercentage() ? getHeight() * widths.getTop() : widths.getTop()), x2 - (widths.isRightAsPercentage() ? getWidth() * widths.getRight() : widths.getRight()), y2 - (widths.isBottomAsPercentage() ? getHeight() * widths.getBottom() : widths.getBottom()), insets, rad);
            return outsideInnerEdge;
        }
        return false;
    }

    private boolean contains(double px, double py, double x1, double y1, double x2, double y2, Insets insets, CornerRadii rad) {
        double centerX;
        double centerY;
        double a2;
        double b2;
        double rrx0 = x1 + insets.getLeft();
        double rry0 = y1 + insets.getTop();
        double rrx1 = x2 - insets.getRight();
        double rry1 = y2 - insets.getBottom();
        if (px >= rrx0 && py >= rry0 && px <= rrx1 && py <= rry1) {
            double tlhr = rad.getTopLeftHorizontalRadius();
            if (rad.isUniform() && tlhr == 0.0d) {
                return true;
            }
            double tlvr = rad.getTopLeftVerticalRadius();
            double trhr = rad.getTopRightHorizontalRadius();
            double trvr = rad.getTopRightVerticalRadius();
            double blhr = rad.getBottomLeftHorizontalRadius();
            double blvr = rad.getBottomLeftVerticalRadius();
            double brhr = rad.getBottomRightHorizontalRadius();
            double brvr = rad.getBottomRightVerticalRadius();
            if (px <= rrx0 + tlhr && py <= rry0 + tlvr) {
                centerX = rrx0 + tlhr;
                centerY = rry0 + tlvr;
                a2 = tlhr;
                b2 = tlvr;
            } else if (px >= rrx1 - trhr && py <= rry0 + trvr) {
                centerX = rrx1 - trhr;
                centerY = rry0 + trvr;
                a2 = trhr;
                b2 = trvr;
            } else if (px >= rrx1 - brhr && py >= rry1 - brvr) {
                centerX = rrx1 - brhr;
                centerY = rry1 - brvr;
                a2 = brhr;
                b2 = brvr;
            } else if (px <= rrx0 + blhr && py >= rry1 - blvr) {
                centerX = rrx0 + blhr;
                centerY = rry1 - blvr;
                a2 = blhr;
                b2 = blvr;
            } else {
                return true;
            }
            double x3 = px - centerX;
            double y3 = py - centerY;
            double result = ((x3 * x3) / (a2 * a2)) + ((y3 * y3) / (b2 * b2));
            return result - 1.0E-7d <= 1.0d;
        }
        return false;
    }

    private CornerRadii getNormalizedFillCorner(int i2) {
        if (!this.cornersValid) {
            validateCorners();
        }
        if (this.normalizedFillCorners == null) {
            return getBackground().getFills().get(i2).getRadii();
        }
        return this.normalizedFillCorners.get(i2);
    }

    private CornerRadii getNormalizedStrokeCorner(int i2) {
        if (!this.cornersValid) {
            validateCorners();
        }
        if (this.normalizedStrokeCorners == null) {
            return getBorder().getStrokes().get(i2).getRadii();
        }
        return this.normalizedStrokeCorners.get(i2);
    }

    private void validateCorners() {
        double width = getWidth();
        double height = getHeight();
        List<CornerRadii> newFillCorners = null;
        List<CornerRadii> newStrokeCorners = null;
        Background background = getBackground();
        List<BackgroundFill> fills = background == null ? Collections.EMPTY_LIST : background.getFills();
        for (int i2 = 0; i2 < fills.size(); i2++) {
            BackgroundFill fill = fills.get(i2);
            CornerRadii origRadii = fill.getRadii();
            Insets origInsets = fill.getInsets();
            CornerRadii newRadii = normalize(origRadii, origInsets, width, height);
            if (origRadii != newRadii) {
                if (newFillCorners == null) {
                    newFillCorners = Arrays.asList(new CornerRadii[fills.size()]);
                }
                newFillCorners.set(i2, newRadii);
            }
        }
        Border border = getBorder();
        List<BorderStroke> strokes = border == null ? Collections.EMPTY_LIST : border.getStrokes();
        for (int i3 = 0; i3 < strokes.size(); i3++) {
            BorderStroke stroke = strokes.get(i3);
            CornerRadii origRadii2 = stroke.getRadii();
            Insets origInsets2 = stroke.getInsets();
            CornerRadii newRadii2 = normalize(origRadii2, origInsets2, width, height);
            if (origRadii2 != newRadii2) {
                if (newStrokeCorners == null) {
                    newStrokeCorners = Arrays.asList(new CornerRadii[strokes.size()]);
                }
                newStrokeCorners.set(i3, newRadii2);
            }
        }
        if (newFillCorners != null) {
            for (int i4 = 0; i4 < fills.size(); i4++) {
                if (newFillCorners.get(i4) == null) {
                    newFillCorners.set(i4, fills.get(i4).getRadii());
                }
            }
            newFillCorners = Collections.unmodifiableList(newFillCorners);
        }
        if (newStrokeCorners != null) {
            for (int i5 = 0; i5 < strokes.size(); i5++) {
                if (newStrokeCorners.get(i5) == null) {
                    newStrokeCorners.set(i5, strokes.get(i5).getRadii());
                }
            }
            newStrokeCorners = Collections.unmodifiableList(newStrokeCorners);
        }
        this.normalizedFillCorners = newFillCorners;
        this.normalizedStrokeCorners = newStrokeCorners;
        this.cornersValid = true;
    }

    private static CornerRadii normalize(CornerRadii radii, Insets insets, double width, double height) {
        double width2 = width - (insets.getLeft() + insets.getRight());
        double height2 = height - (insets.getTop() + insets.getBottom());
        if (width2 <= 0.0d || height2 <= 0.0d) {
            return CornerRadii.EMPTY;
        }
        double tlvr = radii.getTopLeftVerticalRadius();
        double tlhr = radii.getTopLeftHorizontalRadius();
        double trvr = radii.getTopRightVerticalRadius();
        double trhr = radii.getTopRightHorizontalRadius();
        double brvr = radii.getBottomRightVerticalRadius();
        double brhr = radii.getBottomRightHorizontalRadius();
        double blvr = radii.getBottomLeftVerticalRadius();
        double blhr = radii.getBottomLeftHorizontalRadius();
        if (radii.hasPercentBasedRadii) {
            if (radii.isTopLeftVerticalRadiusAsPercentage()) {
                tlvr *= height2;
            }
            if (radii.isTopLeftHorizontalRadiusAsPercentage()) {
                tlhr *= width2;
            }
            if (radii.isTopRightVerticalRadiusAsPercentage()) {
                trvr *= height2;
            }
            if (radii.isTopRightHorizontalRadiusAsPercentage()) {
                trhr *= width2;
            }
            if (radii.isBottomRightVerticalRadiusAsPercentage()) {
                brvr *= height2;
            }
            if (radii.isBottomRightHorizontalRadiusAsPercentage()) {
                brhr *= width2;
            }
            if (radii.isBottomLeftVerticalRadiusAsPercentage()) {
                blvr *= height2;
            }
            if (radii.isBottomLeftHorizontalRadiusAsPercentage()) {
                blhr *= width2;
            }
        }
        double scale = 1.0d;
        if (tlhr + trhr > width2) {
            scale = Math.min(1.0d, width2 / (tlhr + trhr));
        }
        if (blhr + brhr > width2) {
            scale = Math.min(scale, width2 / (blhr + brhr));
        }
        if (tlvr + blvr > height2) {
            scale = Math.min(scale, height2 / (tlvr + blvr));
        }
        if (trvr + brvr > height2) {
            scale = Math.min(scale, height2 / (trvr + brvr));
        }
        if (scale < 1.0d) {
            tlvr *= scale;
            tlhr *= scale;
            trvr *= scale;
            trhr *= scale;
            brvr *= scale;
            brhr *= scale;
            blvr *= scale;
            blhr *= scale;
        }
        if (radii.hasPercentBasedRadii || scale < 1.0d) {
            return new CornerRadii(tlhr, tlvr, trvr, trhr, brhr, brvr, blvr, blhr, false, false, false, false, false, false, false, false);
        }
        return radii;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    @Deprecated
    protected void impl_pickNodeLocal(PickRay pickRay, PickResultChooser result) {
        double boundsDistance = impl_intersectsBounds(pickRay);
        if (!Double.isNaN(boundsDistance)) {
            ObservableList<Node> children = getChildren();
            for (int i2 = children.size() - 1; i2 >= 0; i2--) {
                children.get(i2).impl_pickNode(pickRay, result);
                if (result.isClosed()) {
                    return;
                }
            }
            impl_intersects(pickRay, result);
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected final Bounds impl_computeLayoutBounds() {
        if (this.boundingBox == null) {
            this.boundingBox = new BoundingBox(0.0d, 0.0d, 0.0d, getWidth(), getHeight(), 0.0d);
        }
        return this.boundingBox;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected final void impl_notifyLayoutBoundsChanged() {
    }

    private BaseBounds computeShapeBounds(BaseBounds bounds) {
        BorderStrokeStyle rightStyle;
        com.sun.javafx.geom.Shape s2 = this._shape.impl_configShape();
        float[] bbox = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
        Background bg2 = getBackground();
        if (bg2 != null) {
            RectBounds sBounds = s2.getBounds();
            Insets bgOutsets = bg2.getOutsets();
            bbox[0] = sBounds.getMinX() - ((float) bgOutsets.getLeft());
            bbox[1] = sBounds.getMinY() - ((float) bgOutsets.getTop());
            bbox[2] = sBounds.getMaxX() + ((float) bgOutsets.getBottom());
            bbox[3] = sBounds.getMaxY() + ((float) bgOutsets.getRight());
        }
        Border b2 = getBorder();
        if (b2 != null && b2.getStrokes().size() > 0) {
            for (BorderStroke bs2 : b2.getStrokes()) {
                if (bs2.getTopStyle() != null) {
                    rightStyle = bs2.getTopStyle();
                } else if (bs2.getLeftStyle() != null) {
                    rightStyle = bs2.getLeftStyle();
                } else if (bs2.getBottomStyle() != null) {
                    rightStyle = bs2.getBottomStyle();
                } else {
                    rightStyle = bs2.getRightStyle() != null ? bs2.getRightStyle() : null;
                }
                BorderStrokeStyle bss = rightStyle;
                if (bss != null && bss != BorderStrokeStyle.NONE) {
                    StrokeType type = bss.getType();
                    double sw = Math.max(bs2.getWidths().top, 0.0d);
                    StrokeLineCap cap = bss.getLineCap();
                    StrokeLineJoin join = bss.getLineJoin();
                    float miterlimit = (float) Math.max(bss.getMiterLimit(), 1.0d);
                    Toolkit.getToolkit().accumulateStrokeBounds(s2, bbox, type, sw, cap, join, miterlimit, BaseTransform.IDENTITY_TRANSFORM);
                }
            }
        }
        if (bbox[2] < bbox[0] || bbox[3] < bbox[1]) {
            return bounds.makeEmpty();
        }
        return bounds.deriveWithNewBounds(bbox[0], bbox[1], 0.0f, bbox[2], bbox[3], 0.0f);
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        double bx1;
        double by1;
        double bx2;
        double by2;
        double bx22 = getWidth();
        double by22 = getHeight();
        if (this._shape != null && !isScaleShape()) {
            BaseBounds shapeBounds = computeShapeBounds(bounds);
            double shapeWidth = shapeBounds.getWidth();
            double shapeHeight = shapeBounds.getHeight();
            if (isCenterShape()) {
                bx1 = (bx22 - shapeWidth) / 2.0d;
                by1 = (by22 - shapeHeight) / 2.0d;
                bx2 = bx1 + shapeWidth;
                by2 = by1 + shapeHeight;
            } else {
                bx1 = shapeBounds.getMinX();
                by1 = shapeBounds.getMinY();
                bx2 = shapeBounds.getMaxX();
                by2 = shapeBounds.getMaxY();
            }
        } else {
            Background background = getBackground();
            Border border = getBorder();
            Insets backgroundOutsets = background == null ? Insets.EMPTY : background.getOutsets();
            Insets borderOutsets = border == null ? Insets.EMPTY : border.getOutsets();
            bx1 = 0.0d - Math.max(backgroundOutsets.getLeft(), borderOutsets.getLeft());
            by1 = 0.0d - Math.max(backgroundOutsets.getTop(), borderOutsets.getTop());
            bx2 = bx22 + Math.max(backgroundOutsets.getRight(), borderOutsets.getRight());
            by2 = by22 + Math.max(backgroundOutsets.getBottom(), borderOutsets.getBottom());
        }
        BaseBounds cb = super.impl_computeGeomBounds(bounds, tx);
        if (cb.isEmpty()) {
            BaseBounds bounds2 = bounds.deriveWithNewBounds((float) bx1, (float) by1, 0.0f, (float) bx2, (float) by2, 0.0f);
            return tx.transform(bounds2, bounds2);
        }
        BaseBounds tempBounds = TempState.getInstance().bounds.deriveWithNewBounds((float) bx1, (float) by1, 0.0f, (float) bx2, (float) by2, 0.0f);
        BaseBounds bb2 = tx.transform(tempBounds, tempBounds);
        return cb.deriveWithUnion(bb2);
    }

    public String getUserAgentStylesheet() {
        return null;
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/Region$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Region, Insets> PADDING = new CssMetaData<Region, Insets>("-fx-padding", InsetsConverter.getInstance(), Insets.EMPTY) { // from class: javafx.scene.layout.Region.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.padding == null || !node.padding.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Insets> getStyleableProperty(Region node) {
                return (StyleableProperty) node.paddingProperty();
            }
        };
        private static final CssMetaData<Region, Insets> OPAQUE_INSETS = new CssMetaData<Region, Insets>("-fx-opaque-insets", InsetsConverter.getInstance(), null) { // from class: javafx.scene.layout.Region.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.opaqueInsets == null || !node.opaqueInsets.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Insets> getStyleableProperty(Region node) {
                return (StyleableProperty) node.opaqueInsetsProperty();
            }
        };
        private static final CssMetaData<Region, Background> BACKGROUND = new CssMetaData<Region, Background>("-fx-region-background", BackgroundConverter.INSTANCE, null, false, Background.getClassCssMetaData()) { // from class: javafx.scene.layout.Region.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return !node.background.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Background> getStyleableProperty(Region node) {
                return (StyleableProperty) node.background;
            }
        };
        private static final CssMetaData<Region, Border> BORDER = new CssMetaData<Region, Border>("-fx-region-border", BorderConverter.getInstance(), null, false, Border.getClassCssMetaData()) { // from class: javafx.scene.layout.Region.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return !node.border.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Border> getStyleableProperty(Region node) {
                return (StyleableProperty) node.border;
            }
        };
        private static final CssMetaData<Region, Shape> SHAPE = new CssMetaData<Region, Shape>("-fx-shape", ShapeConverter.getInstance()) { // from class: javafx.scene.layout.Region.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.shape == null || !node.shape.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Shape> getStyleableProperty(Region node) {
                return (StyleableProperty) node.shapeProperty();
            }
        };
        private static final CssMetaData<Region, Boolean> SCALE_SHAPE = new CssMetaData<Region, Boolean>("-fx-scale-shape", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.layout.Region.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.scaleShape == null || !node.scaleShape.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Region node) {
                return (StyleableProperty) node.scaleShapeProperty();
            }
        };
        private static final CssMetaData<Region, Boolean> POSITION_SHAPE = new CssMetaData<Region, Boolean>("-fx-position-shape", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.layout.Region.StyleableProperties.7
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.centerShape == null || !node.centerShape.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Region node) {
                return (StyleableProperty) node.centerShapeProperty();
            }
        };
        private static final CssMetaData<Region, Boolean> CACHE_SHAPE = new CssMetaData<Region, Boolean>("-fx-cache-shape", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.layout.Region.StyleableProperties.8
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.cacheShape == null || !node.cacheShape.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Region node) {
                return (StyleableProperty) node.cacheShapeProperty();
            }
        };
        private static final CssMetaData<Region, Boolean> SNAP_TO_PIXEL = new CssMetaData<Region, Boolean>("-fx-snap-to-pixel", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.layout.Region.StyleableProperties.9
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.snapToPixel == null || !node.snapToPixel.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Region node) {
                return (StyleableProperty) node.snapToPixelProperty();
            }
        };
        private static final CssMetaData<Region, Number> MIN_HEIGHT = new CssMetaData<Region, Number>("-fx-min-height", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.layout.Region.StyleableProperties.10
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.minHeight == null || !node.minHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Region node) {
                return (StyleableProperty) node.minHeightProperty();
            }
        };
        private static final CssMetaData<Region, Number> PREF_HEIGHT = new CssMetaData<Region, Number>("-fx-pref-height", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.layout.Region.StyleableProperties.11
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.prefHeight == null || !node.prefHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Region node) {
                return (StyleableProperty) node.prefHeightProperty();
            }
        };
        private static final CssMetaData<Region, Number> MAX_HEIGHT = new CssMetaData<Region, Number>("-fx-max-height", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.layout.Region.StyleableProperties.12
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.maxHeight == null || !node.maxHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Region node) {
                return (StyleableProperty) node.maxHeightProperty();
            }
        };
        private static final CssMetaData<Region, Number> MIN_WIDTH = new CssMetaData<Region, Number>("-fx-min-width", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.layout.Region.StyleableProperties.13
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.minWidth == null || !node.minWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Region node) {
                return (StyleableProperty) node.minWidthProperty();
            }
        };
        private static final CssMetaData<Region, Number> PREF_WIDTH = new CssMetaData<Region, Number>("-fx-pref-width", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.layout.Region.StyleableProperties.14
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.prefWidth == null || !node.prefWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Region node) {
                return (StyleableProperty) node.prefWidthProperty();
            }
        };
        private static final CssMetaData<Region, Number> MAX_WIDTH = new CssMetaData<Region, Number>("-fx-max-width", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.layout.Region.StyleableProperties.15
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Region node) {
                return node.maxWidth == null || !node.maxWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Region node) {
                return (StyleableProperty) node.maxWidthProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Parent.getClassCssMetaData());
            styleables.add(PADDING);
            styleables.add(BACKGROUND);
            styleables.add(BORDER);
            styleables.add(OPAQUE_INSETS);
            styleables.add(SHAPE);
            styleables.add(SCALE_SHAPE);
            styleables.add(POSITION_SHAPE);
            styleables.add(SNAP_TO_PIXEL);
            styleables.add(MIN_WIDTH);
            styleables.add(PREF_WIDTH);
            styleables.add(MAX_WIDTH);
            styleables.add(MIN_HEIGHT);
            styleables.add(PREF_HEIGHT);
            styleables.add(MAX_HEIGHT);
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
}
