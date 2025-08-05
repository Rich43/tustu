package javafx.scene.shape;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.Area;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/shape/Shape.class */
public abstract class Shape extends Node {
    private ObjectProperty<Paint> fill;
    Paint old_fill;
    private ObjectProperty<Paint> stroke;
    Paint old_stroke;
    private BooleanProperty smooth;
    private static final double MIN_STROKE_WIDTH = 0.0d;
    private static final double MIN_STROKE_MITER_LIMIT = 1.0d;
    private Reference<Runnable> shapeChangeListener;
    private StrokeAttributes strokeAttributes;
    private static final double DEFAULT_STROKE_WIDTH = 1.0d;
    private static final double DEFAULT_STROKE_MITER_LIMIT = 10.0d;
    private static final double DEFAULT_STROKE_DASH_OFFSET = 0.0d;
    private static final StrokeType DEFAULT_STROKE_TYPE = StrokeType.CENTERED;
    private static final StrokeLineJoin DEFAULT_STROKE_LINE_JOIN = StrokeLineJoin.MITER;
    private static final StrokeLineCap DEFAULT_STROKE_LINE_CAP = StrokeLineCap.SQUARE;
    private static final float[] DEFAULT_PG_STROKE_DASH_ARRAY = new float[0];

    @Deprecated
    protected NGShape.Mode impl_mode = NGShape.Mode.FILL;
    private final AbstractNotifyListener platformImageChangeListener = new AbstractNotifyListener() { // from class: javafx.scene.shape.Shape.2
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            Shape.this.impl_markDirty(DirtyBits.SHAPE_FILL);
            Shape.this.impl_markDirty(DirtyBits.SHAPE_STROKE);
            Shape.this.impl_geomChanged();
            Shape.this.checkModeChanged();
        }
    };
    private boolean strokeAttributesDirty = true;

    @Deprecated
    public abstract com.sun.javafx.geom.Shape impl_configShape();

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        throw new AssertionError((Object) "Subclasses of Shape must implement impl_createPGNode");
    }

    StrokeLineJoin convertLineJoin(StrokeLineJoin t2) {
        return t2;
    }

    public final void setStrokeType(StrokeType value) {
        strokeTypeProperty().set(value);
    }

    public final StrokeType getStrokeType() {
        return this.strokeAttributes == null ? DEFAULT_STROKE_TYPE : this.strokeAttributes.getType();
    }

    public final ObjectProperty<StrokeType> strokeTypeProperty() {
        return getStrokeAttributes().typeProperty();
    }

    public final void setStrokeWidth(double value) {
        strokeWidthProperty().set(value);
    }

    public final double getStrokeWidth() {
        if (this.strokeAttributes == null) {
            return 1.0d;
        }
        return this.strokeAttributes.getWidth();
    }

    public final DoubleProperty strokeWidthProperty() {
        return getStrokeAttributes().widthProperty();
    }

    public final void setStrokeLineJoin(StrokeLineJoin value) {
        strokeLineJoinProperty().set(value);
    }

    public final StrokeLineJoin getStrokeLineJoin() {
        return this.strokeAttributes == null ? DEFAULT_STROKE_LINE_JOIN : this.strokeAttributes.getLineJoin();
    }

    public final ObjectProperty<StrokeLineJoin> strokeLineJoinProperty() {
        return getStrokeAttributes().lineJoinProperty();
    }

    public final void setStrokeLineCap(StrokeLineCap value) {
        strokeLineCapProperty().set(value);
    }

    public final StrokeLineCap getStrokeLineCap() {
        return this.strokeAttributes == null ? DEFAULT_STROKE_LINE_CAP : this.strokeAttributes.getLineCap();
    }

    public final ObjectProperty<StrokeLineCap> strokeLineCapProperty() {
        return getStrokeAttributes().lineCapProperty();
    }

    public final void setStrokeMiterLimit(double value) {
        strokeMiterLimitProperty().set(value);
    }

    public final double getStrokeMiterLimit() {
        if (this.strokeAttributes == null) {
            return 10.0d;
        }
        return this.strokeAttributes.getMiterLimit();
    }

    public final DoubleProperty strokeMiterLimitProperty() {
        return getStrokeAttributes().miterLimitProperty();
    }

    public final void setStrokeDashOffset(double value) {
        strokeDashOffsetProperty().set(value);
    }

    public final double getStrokeDashOffset() {
        if (this.strokeAttributes == null) {
            return 0.0d;
        }
        return this.strokeAttributes.getDashOffset();
    }

    public final DoubleProperty strokeDashOffsetProperty() {
        return getStrokeAttributes().dashOffsetProperty();
    }

    public final ObservableList<Double> getStrokeDashArray() {
        return getStrokeAttributes().dashArrayProperty();
    }

    private NGShape.Mode computeMode() {
        if (getFill() != null && getStroke() != null) {
            return NGShape.Mode.STROKE_FILL;
        }
        if (getFill() != null) {
            return NGShape.Mode.FILL;
        }
        if (getStroke() != null) {
            return NGShape.Mode.STROKE;
        }
        return NGShape.Mode.EMPTY;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkModeChanged() {
        NGShape.Mode newMode = computeMode();
        if (this.impl_mode != newMode) {
            this.impl_mode = newMode;
            impl_markDirty(DirtyBits.SHAPE_MODE);
            impl_geomChanged();
        }
    }

    public final void setFill(Paint value) {
        fillProperty().set(value);
    }

    public final Paint getFill() {
        return this.fill == null ? Color.BLACK : this.fill.get();
    }

    public final ObjectProperty<Paint> fillProperty() {
        if (this.fill == null) {
            this.fill = new StyleableObjectProperty<Paint>(Color.BLACK) { // from class: javafx.scene.shape.Shape.1
                boolean needsListener = false;

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Paint _fill = get();
                    if (this.needsListener) {
                        Toolkit.getPaintAccessor().removeListener(Shape.this.old_fill, Shape.this.platformImageChangeListener);
                    }
                    this.needsListener = _fill != null && Toolkit.getPaintAccessor().isMutable(_fill);
                    Shape.this.old_fill = _fill;
                    if (this.needsListener) {
                        Toolkit.getPaintAccessor().addListener(_fill, Shape.this.platformImageChangeListener);
                    }
                    Shape.this.impl_markDirty(DirtyBits.SHAPE_FILL);
                    Shape.this.checkModeChanged();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Shape, Paint> getCssMetaData() {
                    return StyleableProperties.FILL;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shape.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fill";
                }
            };
        }
        return this.fill;
    }

    public final void setStroke(Paint value) {
        strokeProperty().set(value);
    }

    public final Paint getStroke() {
        if (this.stroke == null) {
            return null;
        }
        return this.stroke.get();
    }

    public final ObjectProperty<Paint> strokeProperty() {
        if (this.stroke == null) {
            this.stroke = new StyleableObjectProperty<Paint>() { // from class: javafx.scene.shape.Shape.3
                boolean needsListener = false;

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Paint _stroke = get();
                    if (this.needsListener) {
                        Toolkit.getPaintAccessor().removeListener(Shape.this.old_stroke, Shape.this.platformImageChangeListener);
                    }
                    this.needsListener = _stroke != null && Toolkit.getPaintAccessor().isMutable(_stroke);
                    Shape.this.old_stroke = _stroke;
                    if (this.needsListener) {
                        Toolkit.getPaintAccessor().addListener(_stroke, Shape.this.platformImageChangeListener);
                    }
                    Shape.this.impl_markDirty(DirtyBits.SHAPE_STROKE);
                    Shape.this.checkModeChanged();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Shape, Paint> getCssMetaData() {
                    return StyleableProperties.STROKE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shape.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "stroke";
                }
            };
        }
        return this.stroke;
    }

    public final void setSmooth(boolean value) {
        smoothProperty().set(value);
    }

    public final boolean isSmooth() {
        if (this.smooth == null) {
            return true;
        }
        return this.smooth.get();
    }

    public final BooleanProperty smoothProperty() {
        if (this.smooth == null) {
            this.smooth = new StyleableBooleanProperty(true) { // from class: javafx.scene.shape.Shape.4
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    Shape.this.impl_markDirty(DirtyBits.NODE_SMOOTH);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.SMOOTH;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Shape.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "smooth";
                }
            };
        }
        return this.smooth;
    }

    @Deprecated
    protected Paint impl_cssGetFillInitialValue() {
        return Color.BLACK;
    }

    @Deprecated
    protected Paint impl_cssGetStrokeInitialValue() {
        return null;
    }

    /* loaded from: jfxrt.jar:javafx/scene/shape/Shape$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Shape, Paint> FILL = new CssMetaData<Shape, Paint>("-fx-fill", PaintConverter.getInstance(), Color.BLACK) { // from class: javafx.scene.shape.Shape.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return node.fill == null || !node.fill.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Paint> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.fillProperty();
            }

            @Override // javafx.css.CssMetaData
            public Paint getInitialValue(Shape node) {
                return node.impl_cssGetFillInitialValue();
            }
        };
        private static final CssMetaData<Shape, Boolean> SMOOTH = new CssMetaData<Shape, Boolean>("-fx-smooth", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.shape.Shape.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return node.smooth == null || !node.smooth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.smoothProperty();
            }
        };
        private static final CssMetaData<Shape, Paint> STROKE = new CssMetaData<Shape, Paint>("-fx-stroke", PaintConverter.getInstance()) { // from class: javafx.scene.shape.Shape.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return node.stroke == null || !node.stroke.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Paint> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.strokeProperty();
            }

            @Override // javafx.css.CssMetaData
            public Paint getInitialValue(Shape node) {
                return node.impl_cssGetStrokeInitialValue();
            }
        };
        private static final CssMetaData<Shape, Number[]> STROKE_DASH_ARRAY = new CssMetaData<Shape, Number[]>("-fx-stroke-dash-array", SizeConverter.SequenceConverter.getInstance(), new Double[0]) { // from class: javafx.scene.shape.Shape.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return true;
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number[]> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.getStrokeAttributes().cssDashArrayProperty();
            }
        };
        private static final CssMetaData<Shape, Number> STROKE_DASH_OFFSET = new CssMetaData<Shape, Number>("-fx-stroke-dash-offset", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.shape.Shape.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return node.strokeAttributes == null || node.strokeAttributes.canSetDashOffset();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.strokeDashOffsetProperty();
            }
        };
        private static final CssMetaData<Shape, StrokeLineCap> STROKE_LINE_CAP = new CssMetaData<Shape, StrokeLineCap>("-fx-stroke-line-cap", new EnumConverter(StrokeLineCap.class), StrokeLineCap.SQUARE) { // from class: javafx.scene.shape.Shape.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return node.strokeAttributes == null || node.strokeAttributes.canSetLineCap();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<StrokeLineCap> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.strokeLineCapProperty();
            }
        };
        private static final CssMetaData<Shape, StrokeLineJoin> STROKE_LINE_JOIN = new CssMetaData<Shape, StrokeLineJoin>("-fx-stroke-line-join", new EnumConverter(StrokeLineJoin.class), StrokeLineJoin.MITER) { // from class: javafx.scene.shape.Shape.StyleableProperties.7
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return node.strokeAttributes == null || node.strokeAttributes.canSetLineJoin();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<StrokeLineJoin> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.strokeLineJoinProperty();
            }
        };
        private static final CssMetaData<Shape, StrokeType> STROKE_TYPE = new CssMetaData<Shape, StrokeType>("-fx-stroke-type", new EnumConverter(StrokeType.class), StrokeType.CENTERED) { // from class: javafx.scene.shape.Shape.StyleableProperties.8
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return node.strokeAttributes == null || node.strokeAttributes.canSetType();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<StrokeType> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.strokeTypeProperty();
            }
        };
        private static final CssMetaData<Shape, Number> STROKE_MITER_LIMIT = new CssMetaData<Shape, Number>("-fx-stroke-miter-limit", SizeConverter.getInstance(), Double.valueOf(10.0d)) { // from class: javafx.scene.shape.Shape.StyleableProperties.9
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return node.strokeAttributes == null || node.strokeAttributes.canSetMiterLimit();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.strokeMiterLimitProperty();
            }
        };
        private static final CssMetaData<Shape, Number> STROKE_WIDTH = new CssMetaData<Shape, Number>("-fx-stroke-width", SizeConverter.getInstance(), Double.valueOf(1.0d)) { // from class: javafx.scene.shape.Shape.StyleableProperties.10
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Shape node) {
                return node.strokeAttributes == null || node.strokeAttributes.canSetWidth();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Shape node) {
                return (StyleableProperty) node.strokeWidthProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Node.getClassCssMetaData());
            styleables.add(FILL);
            styleables.add(SMOOTH);
            styleables.add(STROKE);
            styleables.add(STROKE_DASH_ARRAY);
            styleables.add(STROKE_DASH_OFFSET);
            styleables.add(STROKE_LINE_CAP);
            styleables.add(STROKE_LINE_JOIN);
            styleables.add(STROKE_TYPE);
            styleables.add(STROKE_MITER_LIMIT);
            styleables.add(STROKE_WIDTH);
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

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return computeShapeBounds(bounds, tx, impl_configShape());
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        return computeShapeContains(localX, localY, impl_configShape());
    }

    private void updatePGShape() {
        NGShape peer = (NGShape) impl_getPeer();
        if (this.strokeAttributesDirty && getStroke() != null) {
            float[] pgDashArray = hasStrokeDashArray() ? toPGDashArray(getStrokeDashArray()) : DEFAULT_PG_STROKE_DASH_ARRAY;
            peer.setDrawStroke((float) Utils.clampMin(getStrokeWidth(), 0.0d), getStrokeType(), getStrokeLineCap(), convertLineJoin(getStrokeLineJoin()), (float) Utils.clampMin(getStrokeMiterLimit(), 1.0d), pgDashArray, (float) getStrokeDashOffset());
            this.strokeAttributesDirty = false;
        }
        if (impl_isDirty(DirtyBits.SHAPE_MODE)) {
            peer.setMode(this.impl_mode);
        }
        if (impl_isDirty(DirtyBits.SHAPE_FILL)) {
            Paint localFill = getFill();
            peer.setFillPaint(localFill == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(localFill));
        }
        if (impl_isDirty(DirtyBits.SHAPE_STROKE)) {
            Paint localStroke = getStroke();
            peer.setDrawPaint(localStroke == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(localStroke));
        }
        if (impl_isDirty(DirtyBits.NODE_SMOOTH)) {
            peer.setSmooth(isSmooth());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javafx.scene.Node
    @Deprecated
    public void impl_markDirty(DirtyBits dirtyBits) {
        Runnable listener = this.shapeChangeListener != null ? this.shapeChangeListener.get() : null;
        if (listener != null && impl_isDirtyEmpty()) {
            listener.run();
        }
        super.impl_markDirty(dirtyBits);
    }

    @Deprecated
    public void impl_setShapeChangeListener(Runnable listener) {
        if (this.shapeChangeListener != null) {
            this.shapeChangeListener.clear();
        }
        this.shapeChangeListener = listener != null ? new WeakReference(listener) : null;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        updatePGShape();
    }

    BaseBounds computeBounds(BaseBounds bounds, BaseTransform tx, double upad, double dpad, double x2, double y2, double w2, double h2) {
        double x1;
        double y1;
        if (w2 < 0.0d || h2 < 0.0d) {
            return bounds.makeEmpty();
        }
        double x0 = x2;
        double y0 = y2;
        double _dpad = dpad;
        if (tx.isTranslateOrIdentity()) {
            x1 = w2 + x0;
            y1 = h2 + y0;
            if (tx.getType() == 1) {
                double dx = tx.getMxt();
                double dy = tx.getMyt();
                x0 += dx;
                y0 += dy;
                x1 += dx;
                y1 += dy;
            }
            _dpad += upad;
        } else {
            double x02 = x0 - upad;
            double y02 = y0 - upad;
            double x12 = w2 + (upad * 2.0d);
            double y12 = h2 + (upad * 2.0d);
            double mxx = tx.getMxx();
            double mxy = tx.getMxy();
            double myx = tx.getMyx();
            double myy = tx.getMyy();
            double mxt = (x02 * mxx) + (y02 * mxy) + tx.getMxt();
            double myt = (x02 * myx) + (y02 * myy) + tx.getMyt();
            double mxx2 = mxx * x12;
            double mxy2 = mxy * y12;
            double myx2 = myx * x12;
            double myy2 = myy * y12;
            x0 = Math.min(Math.min(0.0d, mxx2), Math.min(mxy2, mxx2 + mxy2)) + mxt;
            y0 = Math.min(Math.min(0.0d, myx2), Math.min(myy2, myx2 + myy2)) + myt;
            x1 = Math.max(Math.max(0.0d, mxx2), Math.max(mxy2, mxx2 + mxy2)) + mxt;
            y1 = Math.max(Math.max(0.0d, myx2), Math.max(myy2, myx2 + myy2)) + myt;
        }
        return bounds.deriveWithNewBounds((float) (x0 - _dpad), (float) (y0 - _dpad), 0.0f, (float) (x1 + _dpad), (float) (y1 + _dpad), 0.0f);
    }

    BaseBounds computeShapeBounds(BaseBounds bounds, BaseTransform tx, com.sun.javafx.geom.Shape s2) {
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return bounds.makeEmpty();
        }
        float[] bbox = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
        boolean includeShape = this.impl_mode != NGShape.Mode.STROKE;
        boolean includeStroke = this.impl_mode != NGShape.Mode.FILL;
        if (includeStroke && getStrokeType() == StrokeType.INSIDE) {
            includeShape = true;
            includeStroke = false;
        }
        if (includeStroke) {
            StrokeType type = getStrokeType();
            double sw = Utils.clampMin(getStrokeWidth(), 0.0d);
            StrokeLineCap cap = getStrokeLineCap();
            StrokeLineJoin join = convertLineJoin(getStrokeLineJoin());
            float miterlimit = (float) Utils.clampMin(getStrokeMiterLimit(), 1.0d);
            Toolkit.getToolkit().accumulateStrokeBounds(s2, bbox, type, sw, cap, join, miterlimit, tx);
            bbox[0] = (float) (bbox[0] - 0.5d);
            bbox[1] = (float) (bbox[1] - 0.5d);
            bbox[2] = (float) (bbox[2] + 0.5d);
            bbox[3] = (float) (bbox[3] + 0.5d);
        } else if (includeShape) {
            com.sun.javafx.geom.Shape.accumulate(bbox, s2, tx);
        }
        if (bbox[2] < bbox[0] || bbox[3] < bbox[1]) {
            return bounds.makeEmpty();
        }
        return bounds.deriveWithNewBounds(bbox[0], bbox[1], 0.0f, bbox[2], bbox[3], 0.0f);
    }

    boolean computeShapeContains(double localX, double localY, com.sun.javafx.geom.Shape s2) {
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return false;
        }
        boolean includeShape = this.impl_mode != NGShape.Mode.STROKE;
        boolean includeStroke = this.impl_mode != NGShape.Mode.FILL;
        if (includeStroke && includeShape && getStrokeType() == StrokeType.INSIDE) {
            includeStroke = false;
        }
        if (includeShape && s2.contains((float) localX, (float) localY)) {
            return true;
        }
        if (includeStroke) {
            StrokeType type = getStrokeType();
            double sw = Utils.clampMin(getStrokeWidth(), 0.0d);
            StrokeLineCap cap = getStrokeLineCap();
            StrokeLineJoin join = convertLineJoin(getStrokeLineJoin());
            float miterlimit = (float) Utils.clampMin(getStrokeMiterLimit(), 1.0d);
            return Toolkit.getToolkit().strokeContains(s2, localX, localY, type, sw, cap, join, miterlimit);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StrokeAttributes getStrokeAttributes() {
        if (this.strokeAttributes == null) {
            this.strokeAttributes = new StrokeAttributes();
        }
        return this.strokeAttributes;
    }

    private boolean hasStrokeDashArray() {
        return this.strokeAttributes != null && this.strokeAttributes.hasDashArray();
    }

    private static float[] toPGDashArray(List<Double> dashArray) {
        int size = dashArray.size();
        float[] pgDashArray = new float[size];
        for (int i2 = 0; i2 < size; i2++) {
            pgDashArray[i2] = dashArray.get(i2).floatValue();
        }
        return pgDashArray;
    }

    /* loaded from: jfxrt.jar:javafx/scene/shape/Shape$StrokeAttributes.class */
    private final class StrokeAttributes {
        private ObjectProperty<StrokeType> type;
        private DoubleProperty width;
        private ObjectProperty<StrokeLineJoin> lineJoin;
        private ObjectProperty<StrokeLineCap> lineCap;
        private DoubleProperty miterLimit;
        private DoubleProperty dashOffset;
        private ObservableList<Double> dashArray;
        private ObjectProperty<Number[]> cssDashArray;

        private StrokeAttributes() {
            this.cssDashArray = null;
        }

        public final StrokeType getType() {
            return this.type == null ? Shape.DEFAULT_STROKE_TYPE : this.type.get();
        }

        public final ObjectProperty<StrokeType> typeProperty() {
            if (this.type == null) {
                this.type = new StyleableObjectProperty<StrokeType>(Shape.DEFAULT_STROKE_TYPE) { // from class: javafx.scene.shape.Shape.StrokeAttributes.1
                    @Override // javafx.beans.property.ObjectPropertyBase
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_TYPE);
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<Shape, StrokeType> getCssMetaData() {
                        return StyleableProperties.STROKE_TYPE;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "strokeType";
                    }
                };
            }
            return this.type;
        }

        public double getWidth() {
            if (this.width == null) {
                return 1.0d;
            }
            return this.width.get();
        }

        public final DoubleProperty widthProperty() {
            if (this.width == null) {
                this.width = new StyleableDoubleProperty(1.0d) { // from class: javafx.scene.shape.Shape.StrokeAttributes.2
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_WIDTH);
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.STROKE_WIDTH;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "strokeWidth";
                    }
                };
            }
            return this.width;
        }

        public StrokeLineJoin getLineJoin() {
            if (this.lineJoin == null) {
                return Shape.DEFAULT_STROKE_LINE_JOIN;
            }
            return this.lineJoin.get();
        }

        public final ObjectProperty<StrokeLineJoin> lineJoinProperty() {
            if (this.lineJoin == null) {
                this.lineJoin = new StyleableObjectProperty<StrokeLineJoin>(Shape.DEFAULT_STROKE_LINE_JOIN) { // from class: javafx.scene.shape.Shape.StrokeAttributes.3
                    @Override // javafx.beans.property.ObjectPropertyBase
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_LINE_JOIN);
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<Shape, StrokeLineJoin> getCssMetaData() {
                        return StyleableProperties.STROKE_LINE_JOIN;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "strokeLineJoin";
                    }
                };
            }
            return this.lineJoin;
        }

        public StrokeLineCap getLineCap() {
            if (this.lineCap == null) {
                return Shape.DEFAULT_STROKE_LINE_CAP;
            }
            return this.lineCap.get();
        }

        public final ObjectProperty<StrokeLineCap> lineCapProperty() {
            if (this.lineCap == null) {
                this.lineCap = new StyleableObjectProperty<StrokeLineCap>(Shape.DEFAULT_STROKE_LINE_CAP) { // from class: javafx.scene.shape.Shape.StrokeAttributes.4
                    @Override // javafx.beans.property.ObjectPropertyBase
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_LINE_CAP);
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<Shape, StrokeLineCap> getCssMetaData() {
                        return StyleableProperties.STROKE_LINE_CAP;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "strokeLineCap";
                    }
                };
            }
            return this.lineCap;
        }

        public double getMiterLimit() {
            if (this.miterLimit == null) {
                return 10.0d;
            }
            return this.miterLimit.get();
        }

        public final DoubleProperty miterLimitProperty() {
            if (this.miterLimit == null) {
                this.miterLimit = new StyleableDoubleProperty(10.0d) { // from class: javafx.scene.shape.Shape.StrokeAttributes.5
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_MITER_LIMIT);
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.STROKE_MITER_LIMIT;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "strokeMiterLimit";
                    }
                };
            }
            return this.miterLimit;
        }

        public double getDashOffset() {
            if (this.dashOffset == null) {
                return 0.0d;
            }
            return this.dashOffset.get();
        }

        public final DoubleProperty dashOffsetProperty() {
            if (this.dashOffset == null) {
                this.dashOffset = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.shape.Shape.StrokeAttributes.6
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_DASH_OFFSET);
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.STROKE_DASH_OFFSET;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "strokeDashOffset";
                    }
                };
            }
            return this.dashOffset;
        }

        public ObservableList<Double> dashArrayProperty() {
            if (this.dashArray == null) {
                this.dashArray = new TrackableObservableList<Double>() { // from class: javafx.scene.shape.Shape.StrokeAttributes.7
                    @Override // com.sun.javafx.collections.TrackableObservableList
                    protected void onChanged(ListChangeListener.Change<Double> c2) {
                        StrokeAttributes.this.invalidated(StyleableProperties.STROKE_DASH_ARRAY);
                    }
                };
            }
            return this.dashArray;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ObjectProperty<Number[]> cssDashArrayProperty() {
            if (this.cssDashArray == null) {
                this.cssDashArray = new StyleableObjectProperty<Number[]>() { // from class: javafx.scene.shape.Shape.StrokeAttributes.8
                    @Override // javafx.css.StyleableObjectProperty, javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                    public void set(Number[] v2) {
                        ObservableList<Double> list = StrokeAttributes.this.dashArrayProperty();
                        list.clear();
                        if (v2 != null && v2.length > 0) {
                            for (Number number : v2) {
                                list.add(Double.valueOf(number.doubleValue()));
                            }
                        }
                    }

                    @Override // javafx.beans.property.ObjectPropertyBase, javafx.beans.value.ObservableObjectValue
                    public Double[] get() {
                        List<Double> list = StrokeAttributes.this.dashArrayProperty();
                        return (Double[]) list.toArray(new Double[list.size()]);
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Shape.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "cssDashArray";
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<Shape, Number[]> getCssMetaData() {
                        return StyleableProperties.STROKE_DASH_ARRAY;
                    }
                };
            }
            return this.cssDashArray;
        }

        public boolean canSetType() {
            return this.type == null || !this.type.isBound();
        }

        public boolean canSetWidth() {
            return this.width == null || !this.width.isBound();
        }

        public boolean canSetLineJoin() {
            return this.lineJoin == null || !this.lineJoin.isBound();
        }

        public boolean canSetLineCap() {
            return this.lineCap == null || !this.lineCap.isBound();
        }

        public boolean canSetMiterLimit() {
            return this.miterLimit == null || !this.miterLimit.isBound();
        }

        public boolean canSetDashOffset() {
            return this.dashOffset == null || !this.dashOffset.isBound();
        }

        public boolean hasDashArray() {
            return this.dashArray != null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void invalidated(CssMetaData<Shape, ?> propertyCssKey) {
            Shape.this.impl_markDirty(DirtyBits.SHAPE_STROKEATTRS);
            Shape.this.strokeAttributesDirty = true;
            if (propertyCssKey != StyleableProperties.STROKE_DASH_OFFSET) {
                Shape.this.impl_geomChanged();
            }
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return alg.processLeafNode(this, ctx);
    }

    public static Shape union(Shape shape1, Shape shape2) {
        Area result = shape1.getTransformedArea();
        result.add(shape2.getTransformedArea());
        return createFromGeomShape(result);
    }

    public static Shape subtract(Shape shape1, Shape shape2) {
        Area result = shape1.getTransformedArea();
        result.subtract(shape2.getTransformedArea());
        return createFromGeomShape(result);
    }

    public static Shape intersect(Shape shape1, Shape shape2) {
        Area result = shape1.getTransformedArea();
        result.intersect(shape2.getTransformedArea());
        return createFromGeomShape(result);
    }

    private Area getTransformedArea() {
        return getTransformedArea(calculateNodeToSceneTransform(this));
    }

    private Area getTransformedArea(BaseTransform transform) {
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return new Area();
        }
        com.sun.javafx.geom.Shape fillShape = impl_configShape();
        if (this.impl_mode == NGShape.Mode.FILL || (this.impl_mode == NGShape.Mode.STROKE_FILL && getStrokeType() == StrokeType.INSIDE)) {
            return createTransformedArea(fillShape, transform);
        }
        StrokeType strokeType = getStrokeType();
        double strokeWidth = Utils.clampMin(getStrokeWidth(), 0.0d);
        StrokeLineCap strokeLineCap = getStrokeLineCap();
        StrokeLineJoin strokeLineJoin = convertLineJoin(getStrokeLineJoin());
        float strokeMiterLimit = (float) Utils.clampMin(getStrokeMiterLimit(), 1.0d);
        float[] dashArray = hasStrokeDashArray() ? toPGDashArray(getStrokeDashArray()) : DEFAULT_PG_STROKE_DASH_ARRAY;
        com.sun.javafx.geom.Shape strokeShape = Toolkit.getToolkit().createStrokedShape(fillShape, strokeType, strokeWidth, strokeLineCap, strokeLineJoin, strokeMiterLimit, dashArray, (float) getStrokeDashOffset());
        if (this.impl_mode == NGShape.Mode.STROKE) {
            return createTransformedArea(strokeShape, transform);
        }
        Area combinedArea = new Area(fillShape);
        combinedArea.add(new Area(strokeShape));
        return createTransformedArea(combinedArea, transform);
    }

    private static BaseTransform calculateNodeToSceneTransform(Node node) {
        Affine3D cumulativeTransformation = new Affine3D();
        do {
            cumulativeTransformation.preConcatenate(node.impl_getLeafTransform());
            node = node.getParent();
        } while (node != null);
        return cumulativeTransformation;
    }

    private static Area createTransformedArea(com.sun.javafx.geom.Shape geomShape, BaseTransform transform) {
        return transform.isIdentity() ? new Area(geomShape) : new Area(geomShape.getPathIterator(transform));
    }

    private static Path createFromGeomShape(com.sun.javafx.geom.Shape geomShape) {
        Path path = new Path();
        ObservableList<PathElement> elements = path.getElements();
        PathIterator iterator = geomShape.getPathIterator(null);
        float[] coords = new float[6];
        while (!iterator.isDone()) {
            int segmentType = iterator.currentSegment(coords);
            switch (segmentType) {
                case 0:
                    elements.add(new MoveTo(coords[0], coords[1]));
                    break;
                case 1:
                    elements.add(new LineTo(coords[0], coords[1]));
                    break;
                case 2:
                    elements.add(new QuadCurveTo(coords[0], coords[1], coords[2], coords[3]));
                    break;
                case 3:
                    elements.add(new CubicCurveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]));
                    break;
                case 4:
                    elements.add(new ClosePath());
                    break;
            }
            iterator.next();
        }
        path.setFillRule(iterator.getWindingRule() == 0 ? FillRule.EVEN_ODD : FillRule.NON_ZERO);
        path.setFill(Color.BLACK);
        path.setStroke(null);
        return path;
    }
}
