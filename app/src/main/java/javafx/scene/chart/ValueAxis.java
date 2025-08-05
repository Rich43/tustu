package javafx.scene.chart;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.lang.Number;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Side;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/chart/ValueAxis.class */
public abstract class ValueAxis<T extends Number> extends Axis<T> {
    private final Path minorTickPath;
    private double offset;
    double dataMinValue;
    double dataMaxValue;
    private List<T> minorTickMarkValues;
    protected final DoubleProperty currentLowerBound;
    private BooleanProperty minorTickVisible;
    private ReadOnlyDoubleWrapper scale;
    private DoubleProperty upperBound;
    private DoubleProperty lowerBound;
    private final ObjectProperty<StringConverter<T>> tickLabelFormatter;
    private DoubleProperty minorTickLength;
    private IntegerProperty minorTickCount;

    protected abstract List<T> calculateMinorTickMarks();

    public final boolean isMinorTickVisible() {
        return this.minorTickVisible.get();
    }

    public final void setMinorTickVisible(boolean value) {
        this.minorTickVisible.set(value);
    }

    public final BooleanProperty minorTickVisibleProperty() {
        return this.minorTickVisible;
    }

    public final double getScale() {
        return this.scale.get();
    }

    protected final void setScale(double scale) {
        this.scale.set(scale);
    }

    public final ReadOnlyDoubleProperty scaleProperty() {
        return this.scale.getReadOnlyProperty();
    }

    ReadOnlyDoubleWrapper scalePropertyImpl() {
        return this.scale;
    }

    public final double getUpperBound() {
        return this.upperBound.get();
    }

    public final void setUpperBound(double value) {
        this.upperBound.set(value);
    }

    public final DoubleProperty upperBoundProperty() {
        return this.upperBound;
    }

    public final double getLowerBound() {
        return this.lowerBound.get();
    }

    public final void setLowerBound(double value) {
        this.lowerBound.set(value);
    }

    public final DoubleProperty lowerBoundProperty() {
        return this.lowerBound;
    }

    public final StringConverter<T> getTickLabelFormatter() {
        return this.tickLabelFormatter.getValue2();
    }

    public final void setTickLabelFormatter(StringConverter<T> value) {
        this.tickLabelFormatter.setValue(value);
    }

    public final ObjectProperty<StringConverter<T>> tickLabelFormatterProperty() {
        return this.tickLabelFormatter;
    }

    public final double getMinorTickLength() {
        return this.minorTickLength.get();
    }

    public final void setMinorTickLength(double value) {
        this.minorTickLength.set(value);
    }

    public final DoubleProperty minorTickLengthProperty() {
        return this.minorTickLength;
    }

    public final int getMinorTickCount() {
        return this.minorTickCount.get();
    }

    public final void setMinorTickCount(int value) {
        this.minorTickCount.set(value);
    }

    public final IntegerProperty minorTickCountProperty() {
        return this.minorTickCount;
    }

    public ValueAxis() {
        this.minorTickPath = new Path();
        this.minorTickMarkValues = null;
        this.currentLowerBound = new SimpleDoubleProperty(this, "currentLowerBound");
        this.minorTickVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.ValueAxis.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                ValueAxis.this.minorTickPath.setVisible(get());
                ValueAxis.this.requestAxisLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ValueAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "minorTickVisible";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.MINOR_TICK_VISIBLE;
            }
        };
        this.scale = new ReadOnlyDoubleWrapper(this, "scale", 0.0d) { // from class: javafx.scene.chart.ValueAxis.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                ValueAxis.this.requestAxisLayout();
                ValueAxis.this.measureInvalid = true;
            }
        };
        this.upperBound = new DoublePropertyBase(100.0d) { // from class: javafx.scene.chart.ValueAxis.3
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (!ValueAxis.this.isAutoRanging()) {
                    ValueAxis.this.invalidateRange();
                    ValueAxis.this.requestAxisLayout();
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ValueAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "upperBound";
            }
        };
        this.lowerBound = new DoublePropertyBase(0.0d) { // from class: javafx.scene.chart.ValueAxis.4
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (!ValueAxis.this.isAutoRanging()) {
                    ValueAxis.this.invalidateRange();
                    ValueAxis.this.requestAxisLayout();
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ValueAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "lowerBound";
            }
        };
        this.tickLabelFormatter = new ObjectPropertyBase<StringConverter<T>>(null) { // from class: javafx.scene.chart.ValueAxis.5
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ValueAxis.this.invalidateRange();
                ValueAxis.this.requestAxisLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ValueAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "tickLabelFormatter";
            }
        };
        this.minorTickLength = new StyleableDoubleProperty(5.0d) { // from class: javafx.scene.chart.ValueAxis.6
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                ValueAxis.this.requestAxisLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ValueAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "minorTickLength";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.MINOR_TICK_LENGTH;
            }
        };
        this.minorTickCount = new StyleableIntegerProperty(5) { // from class: javafx.scene.chart.ValueAxis.7
            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                ValueAxis.this.invalidateRange();
                ValueAxis.this.requestAxisLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ValueAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "minorTickCount";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.MINOR_TICK_COUNT;
            }
        };
        this.minorTickPath.getStyleClass().add("axis-minor-tick-mark");
        getChildren().add(this.minorTickPath);
    }

    public ValueAxis(double lowerBound, double upperBound) {
        this();
        setAutoRanging(false);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
    }

    @Override // javafx.scene.chart.Axis
    protected final Object autoRange(double length) {
        if (isAutoRanging()) {
            double labelSize = getTickLabelFont().getSize() * 2.0d;
            return autoRange(this.dataMinValue, this.dataMaxValue, length, labelSize);
        }
        return getRange();
    }

    protected final double calculateNewScale(double length, double lowerBound, double upperBound) {
        double newScale;
        Side side = getEffectiveSide();
        if (side.isVertical()) {
            this.offset = length;
            newScale = upperBound - lowerBound == 0.0d ? -length : -(length / (upperBound - lowerBound));
        } else {
            this.offset = 0.0d;
            newScale = upperBound - lowerBound == 0.0d ? length : length / (upperBound - lowerBound);
        }
        return newScale;
    }

    protected Object autoRange(double minValue, double maxValue, double length, double labelSize) {
        return null;
    }

    @Override // javafx.scene.chart.Axis
    protected void tickMarksUpdated() {
        super.tickMarksUpdated();
        this.minorTickMarkValues = calculateMinorTickMarks();
    }

    @Override // javafx.scene.chart.Axis, javafx.scene.Parent
    protected void layoutChildren() {
        Side side = getEffectiveSide();
        double length = side.isVertical() ? getHeight() : getWidth();
        if (!isAutoRanging()) {
            setScale(calculateNewScale(length, getLowerBound(), getUpperBound()));
            this.currentLowerBound.set(getLowerBound());
        }
        super.layoutChildren();
        int numMinorTicks = (getTickMarks().size() - 1) * (Math.max(1, getMinorTickCount()) - 1);
        double neededLength = (getTickMarks().size() + numMinorTicks) * 2;
        this.minorTickPath.getElements().clear();
        double minorTickLength = Math.max(0.0d, getMinorTickLength());
        if (minorTickLength > 0.0d && length > neededLength) {
            if (Side.LEFT.equals(side)) {
                this.minorTickPath.setLayoutX(-0.5d);
                this.minorTickPath.setLayoutY(0.5d);
                for (T value : this.minorTickMarkValues) {
                    double y2 = getDisplayPosition((ValueAxis<T>) value);
                    if (y2 >= 0.0d && y2 <= length) {
                        this.minorTickPath.getElements().addAll(new MoveTo(getWidth() - minorTickLength, y2), new LineTo(getWidth() - 1.0d, y2));
                    }
                }
                return;
            }
            if (Side.RIGHT.equals(side)) {
                this.minorTickPath.setLayoutX(0.5d);
                this.minorTickPath.setLayoutY(0.5d);
                for (T value2 : this.minorTickMarkValues) {
                    double y3 = getDisplayPosition((ValueAxis<T>) value2);
                    if (y3 >= 0.0d && y3 <= length) {
                        this.minorTickPath.getElements().addAll(new MoveTo(1.0d, y3), new LineTo(minorTickLength, y3));
                    }
                }
                return;
            }
            if (Side.TOP.equals(side)) {
                this.minorTickPath.setLayoutX(0.5d);
                this.minorTickPath.setLayoutY(-0.5d);
                for (T value3 : this.minorTickMarkValues) {
                    double x2 = getDisplayPosition((ValueAxis<T>) value3);
                    if (x2 >= 0.0d && x2 <= length) {
                        this.minorTickPath.getElements().addAll(new MoveTo(x2, getHeight() - 1.0d), new LineTo(x2, getHeight() - minorTickLength));
                    }
                }
                return;
            }
            this.minorTickPath.setLayoutX(0.5d);
            this.minorTickPath.setLayoutY(0.5d);
            for (T value4 : this.minorTickMarkValues) {
                double x3 = getDisplayPosition((ValueAxis<T>) value4);
                if (x3 >= 0.0d && x3 <= length) {
                    this.minorTickPath.getElements().addAll(new MoveTo(x3, 1.0d), new LineTo(x3, minorTickLength));
                }
            }
        }
    }

    @Override // javafx.scene.chart.Axis
    public void invalidateRange(List<T> data) {
        if (data.isEmpty()) {
            this.dataMaxValue = getUpperBound();
            this.dataMinValue = getLowerBound();
        } else {
            this.dataMinValue = Double.MAX_VALUE;
            this.dataMaxValue = -1.7976931348623157E308d;
        }
        for (T dataValue : data) {
            this.dataMinValue = Math.min(this.dataMinValue, dataValue.doubleValue());
            this.dataMaxValue = Math.max(this.dataMaxValue, dataValue.doubleValue());
        }
        super.invalidateRange(data);
    }

    @Override // javafx.scene.chart.Axis
    public double getDisplayPosition(T value) {
        return this.offset + ((value.doubleValue() - this.currentLowerBound.get()) * getScale());
    }

    @Override // javafx.scene.chart.Axis
    public T getValueForDisplay(double d2) {
        return (T) toRealValue(((d2 - this.offset) / getScale()) + this.currentLowerBound.get());
    }

    @Override // javafx.scene.chart.Axis
    public double getZeroPosition() {
        if (0.0d < getLowerBound() || 0.0d > getUpperBound()) {
            return Double.NaN;
        }
        return getDisplayPosition((ValueAxis<T>) Double.valueOf(0.0d));
    }

    @Override // javafx.scene.chart.Axis
    public boolean isValueOnAxis(T value) {
        double num = value.doubleValue();
        return num >= getLowerBound() && num <= getUpperBound();
    }

    @Override // javafx.scene.chart.Axis
    public double toNumericValue(T value) {
        if (value == null) {
            return Double.NaN;
        }
        return value.doubleValue();
    }

    @Override // javafx.scene.chart.Axis
    public T toRealValue(double value) {
        return new Double(value);
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/ValueAxis$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<ValueAxis<? extends Number>, Number> MINOR_TICK_LENGTH = new CssMetaData<ValueAxis<? extends Number>, Number>("-fx-minor-tick-length", SizeConverter.getInstance(), Double.valueOf(5.0d)) { // from class: javafx.scene.chart.ValueAxis.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ValueAxis<? extends Number> n2) {
                return ((ValueAxis) n2).minorTickLength == null || !((ValueAxis) n2).minorTickLength.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ValueAxis<? extends Number> n2) {
                return (StyleableProperty) n2.minorTickLengthProperty();
            }
        };
        private static final CssMetaData<ValueAxis<? extends Number>, Number> MINOR_TICK_COUNT = new CssMetaData<ValueAxis<? extends Number>, Number>("-fx-minor-tick-count", SizeConverter.getInstance(), 5) { // from class: javafx.scene.chart.ValueAxis.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ValueAxis<? extends Number> n2) {
                return ((ValueAxis) n2).minorTickCount == null || !((ValueAxis) n2).minorTickCount.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ValueAxis<? extends Number> n2) {
                return (StyleableProperty) n2.minorTickCountProperty();
            }
        };
        private static final CssMetaData<ValueAxis<? extends Number>, Boolean> MINOR_TICK_VISIBLE = new CssMetaData<ValueAxis<? extends Number>, Boolean>("-fx-minor-tick-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.ValueAxis.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ValueAxis<? extends Number> n2) {
                return ((ValueAxis) n2).minorTickVisible == null || !((ValueAxis) n2).minorTickVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(ValueAxis<? extends Number> n2) {
                return (StyleableProperty) n2.minorTickVisibleProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Axis.getClassCssMetaData());
            styleables.add(MINOR_TICK_COUNT);
            styleables.add(MINOR_TICK_LENGTH);
            styleables.add(MINOR_TICK_COUNT);
            styleables.add(MINOR_TICK_VISIBLE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.chart.Axis, javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}
