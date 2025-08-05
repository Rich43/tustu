package javafx.scene.chart;

import com.sun.javafx.charts.ChartLayoutAnimator;
import com.sun.javafx.css.converters.SizeConverter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Side;
import javafx.util.Duration;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/chart/NumberAxis.class */
public final class NumberAxis extends ValueAxis<Number> {
    private Object currentAnimationID;
    private final ChartLayoutAnimator animator;
    private final StringProperty currentFormatterProperty;
    private final DefaultFormatter defaultFormatter;
    private BooleanProperty forceZeroInRange;
    private DoubleProperty tickUnit;

    public final boolean isForceZeroInRange() {
        return this.forceZeroInRange.getValue2().booleanValue();
    }

    public final void setForceZeroInRange(boolean value) {
        this.forceZeroInRange.setValue(Boolean.valueOf(value));
    }

    public final BooleanProperty forceZeroInRangeProperty() {
        return this.forceZeroInRange;
    }

    public final double getTickUnit() {
        return this.tickUnit.get();
    }

    public final void setTickUnit(double value) {
        this.tickUnit.set(value);
    }

    public final DoubleProperty tickUnitProperty() {
        return this.tickUnit;
    }

    public NumberAxis() {
        this.animator = new ChartLayoutAnimator(this);
        this.currentFormatterProperty = new SimpleStringProperty(this, "currentFormatter", "");
        this.defaultFormatter = new DefaultFormatter(this);
        this.forceZeroInRange = new BooleanPropertyBase(true) { // from class: javafx.scene.chart.NumberAxis.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                if (NumberAxis.this.isAutoRanging()) {
                    NumberAxis.this.requestAxisLayout();
                    NumberAxis.this.invalidateRange();
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return NumberAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "forceZeroInRange";
            }
        };
        this.tickUnit = new StyleableDoubleProperty(5.0d) { // from class: javafx.scene.chart.NumberAxis.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (!NumberAxis.this.isAutoRanging()) {
                    NumberAxis.this.invalidateRange();
                    NumberAxis.this.requestAxisLayout();
                }
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.TICK_UNIT;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return NumberAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "tickUnit";
            }
        };
    }

    public NumberAxis(double lowerBound, double upperBound, double tickUnit) {
        super(lowerBound, upperBound);
        this.animator = new ChartLayoutAnimator(this);
        this.currentFormatterProperty = new SimpleStringProperty(this, "currentFormatter", "");
        this.defaultFormatter = new DefaultFormatter(this);
        this.forceZeroInRange = new BooleanPropertyBase(true) { // from class: javafx.scene.chart.NumberAxis.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                if (NumberAxis.this.isAutoRanging()) {
                    NumberAxis.this.requestAxisLayout();
                    NumberAxis.this.invalidateRange();
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return NumberAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "forceZeroInRange";
            }
        };
        this.tickUnit = new StyleableDoubleProperty(5.0d) { // from class: javafx.scene.chart.NumberAxis.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (!NumberAxis.this.isAutoRanging()) {
                    NumberAxis.this.invalidateRange();
                    NumberAxis.this.requestAxisLayout();
                }
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.TICK_UNIT;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return NumberAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "tickUnit";
            }
        };
        setTickUnit(tickUnit);
    }

    public NumberAxis(String axisLabel, double lowerBound, double upperBound, double tickUnit) {
        super(lowerBound, upperBound);
        this.animator = new ChartLayoutAnimator(this);
        this.currentFormatterProperty = new SimpleStringProperty(this, "currentFormatter", "");
        this.defaultFormatter = new DefaultFormatter(this);
        this.forceZeroInRange = new BooleanPropertyBase(true) { // from class: javafx.scene.chart.NumberAxis.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                if (NumberAxis.this.isAutoRanging()) {
                    NumberAxis.this.requestAxisLayout();
                    NumberAxis.this.invalidateRange();
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return NumberAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "forceZeroInRange";
            }
        };
        this.tickUnit = new StyleableDoubleProperty(5.0d) { // from class: javafx.scene.chart.NumberAxis.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (!NumberAxis.this.isAutoRanging()) {
                    NumberAxis.this.invalidateRange();
                    NumberAxis.this.requestAxisLayout();
                }
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.TICK_UNIT;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return NumberAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "tickUnit";
            }
        };
        setTickUnit(tickUnit);
        setLabel(axisLabel);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javafx.scene.chart.Axis
    public String getTickMarkLabel(Number value) {
        StringConverter<Number> formatter = getTickLabelFormatter();
        if (formatter == null) {
            formatter = this.defaultFormatter;
        }
        return formatter.toString(value);
    }

    @Override // javafx.scene.chart.Axis
    protected Object getRange() {
        return new Object[]{Double.valueOf(getLowerBound()), Double.valueOf(getUpperBound()), Double.valueOf(getTickUnit()), Double.valueOf(getScale()), this.currentFormatterProperty.get()};
    }

    @Override // javafx.scene.chart.Axis
    protected void setRange(Object range, boolean animate) {
        Object[] rangeProps = (Object[]) range;
        double lowerBound = ((Double) rangeProps[0]).doubleValue();
        double upperBound = ((Double) rangeProps[1]).doubleValue();
        double tickUnit = ((Double) rangeProps[2]).doubleValue();
        double scale = ((Double) rangeProps[3]).doubleValue();
        String formatter = (String) rangeProps[4];
        this.currentFormatterProperty.set(formatter);
        double oldLowerBound = getLowerBound();
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
        setTickUnit(tickUnit);
        if (animate) {
            this.animator.stop(this.currentAnimationID);
            this.currentAnimationID = this.animator.animate(new KeyFrame(Duration.ZERO, new KeyValue(this.currentLowerBound, Double.valueOf(oldLowerBound)), new KeyValue(scalePropertyImpl(), Double.valueOf(getScale()))), new KeyFrame(Duration.millis(700.0d), new KeyValue(this.currentLowerBound, Double.valueOf(lowerBound)), new KeyValue(scalePropertyImpl(), Double.valueOf(scale))));
        } else {
            this.currentLowerBound.set(lowerBound);
            setScale(scale);
        }
    }

    @Override // javafx.scene.chart.Axis
    protected List<Number> calculateTickValues(double length, Object range) {
        Object[] rangeProps = (Object[]) range;
        double lowerBound = ((Double) rangeProps[0]).doubleValue();
        double upperBound = ((Double) rangeProps[1]).doubleValue();
        double tickUnit = ((Double) rangeProps[2]).doubleValue();
        List<Number> tickValues = new ArrayList<>();
        if (lowerBound == upperBound) {
            tickValues.add(Double.valueOf(lowerBound));
        } else if (tickUnit <= 0.0d) {
            tickValues.add(Double.valueOf(lowerBound));
            tickValues.add(Double.valueOf(upperBound));
        } else if (tickUnit > 0.0d) {
            tickValues.add(Double.valueOf(lowerBound));
            if ((upperBound - lowerBound) / tickUnit > 2000.0d) {
                System.err.println("Warning we tried to create more than 2000 major tick marks on a NumberAxis. Lower Bound=" + lowerBound + ", Upper Bound=" + upperBound + ", Tick Unit=" + tickUnit);
            } else if (lowerBound + tickUnit < upperBound) {
                double major = Math.rint(tickUnit) == tickUnit ? Math.ceil(lowerBound) : lowerBound + tickUnit;
                int count = (int) Math.ceil((upperBound - major) / tickUnit);
                for (int i2 = 0; major < upperBound && i2 < count; i2++) {
                    if (!tickValues.contains(Double.valueOf(major))) {
                        tickValues.add(Double.valueOf(major));
                    }
                    major += tickUnit;
                }
            }
            tickValues.add(Double.valueOf(upperBound));
        }
        return tickValues;
    }

    @Override // javafx.scene.chart.ValueAxis
    protected List<Number> calculateMinorTickMarks() {
        List<Number> minorTickMarks = new ArrayList<>();
        double lowerBound = getLowerBound();
        double upperBound = getUpperBound();
        double tickUnit = getTickUnit();
        double minorUnit = tickUnit / Math.max(1, getMinorTickCount());
        if (tickUnit > 0.0d) {
            if ((upperBound - lowerBound) / minorUnit > 10000.0d) {
                System.err.println("Warning we tried to create more than 10000 minor tick marks on a NumberAxis. Lower Bound=" + getLowerBound() + ", Upper Bound=" + getUpperBound() + ", Tick Unit=" + tickUnit);
                return minorTickMarks;
            }
            boolean tickUnitIsInteger = Math.rint(tickUnit) == tickUnit;
            if (tickUnitIsInteger) {
                double minor = Math.floor(lowerBound) + minorUnit;
                int count = (int) Math.ceil((Math.ceil(lowerBound) - minor) / minorUnit);
                for (int i2 = 0; minor < Math.ceil(lowerBound) && i2 < count; i2++) {
                    if (minor > lowerBound) {
                        minorTickMarks.add(Double.valueOf(minor));
                    }
                    minor += minorUnit;
                }
            }
            double major = tickUnitIsInteger ? Math.ceil(lowerBound) : lowerBound;
            int count2 = (int) Math.ceil((upperBound - major) / tickUnit);
            for (int i3 = 0; major < upperBound && i3 < count2; i3++) {
                double next = Math.min(major + tickUnit, upperBound);
                double minor2 = major + minorUnit;
                int minorCount = (int) Math.ceil((next - minor2) / minorUnit);
                for (int j2 = 0; minor2 < next && j2 < minorCount; j2++) {
                    minorTickMarks.add(Double.valueOf(minor2));
                    minor2 += minorUnit;
                }
                major += tickUnit;
            }
        }
        return minorTickMarks;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javafx.scene.chart.Axis
    public Dimension2D measureTickMarkSize(Number value, Object range) {
        Object[] rangeProps = (Object[]) range;
        String formatter = (String) rangeProps[4];
        return measureTickMarkSize(value, getTickLabelRotation(), formatter);
    }

    private Dimension2D measureTickMarkSize(Number value, double rotation, String numFormatter) {
        String labelText;
        StringConverter<Number> formatter = getTickLabelFormatter();
        if (formatter == null) {
            formatter = this.defaultFormatter;
        }
        if (!(formatter instanceof DefaultFormatter)) {
            labelText = formatter.toString(value);
        } else {
            labelText = ((DefaultFormatter) formatter).toString(value, numFormatter);
        }
        return measureTickMarkLabelSize(labelText, rotation);
    }

    @Override // javafx.scene.chart.ValueAxis
    protected Object autoRange(double minValue, double maxValue, double length, double labelSize) {
        double dAbs;
        Side side = getEffectiveSide();
        if (isForceZeroInRange()) {
            if (maxValue < 0.0d) {
                maxValue = 0.0d;
            } else if (minValue > 0.0d) {
                minValue = 0.0d;
            }
        }
        int numOfTickMarks = Math.max((int) Math.floor(length / labelSize), 2);
        int minorTickCount = Math.max(getMinorTickCount(), 1);
        double range = maxValue - minValue;
        if (range != 0.0d && range / (numOfTickMarks * minorTickCount) <= Math.ulp(minValue)) {
            range = 0.0d;
        }
        if (range == 0.0d) {
            dAbs = minValue == 0.0d ? 2.0d : Math.abs(minValue) * 0.02d;
        } else {
            dAbs = Math.abs(range) * 1.02d;
        }
        double paddedRange = dAbs;
        double padding = (paddedRange - range) / 2.0d;
        double paddedMin = minValue - padding;
        double paddedMax = maxValue + padding;
        if ((paddedMin < 0.0d && minValue >= 0.0d) || (paddedMin > 0.0d && minValue <= 0.0d)) {
            paddedMin = 0.0d;
        }
        if ((paddedMax < 0.0d && maxValue >= 0.0d) || (paddedMax > 0.0d && maxValue <= 0.0d)) {
            paddedMax = 0.0d;
        }
        double tickUnit = paddedRange / numOfTickMarks;
        double tickUnitRounded = 0.0d;
        double minRounded = 0.0d;
        double maxRounded = 0.0d;
        int count = 0;
        double reqLength = Double.MAX_VALUE;
        String formatter = "0.00000000";
        while (true) {
            if (reqLength <= length && count <= 20) {
                break;
            }
            int exp = (int) Math.floor(Math.log10(tickUnit));
            double mant = tickUnit / Math.pow(10.0d, exp);
            double ratio = mant;
            if (mant > 5.0d) {
                exp++;
                ratio = 1.0d;
            } else if (mant > 1.0d) {
                ratio = mant > 2.5d ? 5.0d : 2.5d;
            }
            if (exp > 1) {
                formatter = "#,##0";
            } else if (exp == 1) {
                formatter = "0";
            } else {
                boolean ratioHasFrac = Math.rint(ratio) != ratio;
                StringBuilder formatterB = new StringBuilder("0");
                int n2 = ratioHasFrac ? Math.abs(exp) + 1 : Math.abs(exp);
                if (n2 > 0) {
                    formatterB.append(".");
                }
                for (int i2 = 0; i2 < n2; i2++) {
                    formatterB.append("0");
                }
                formatter = formatterB.toString();
            }
            tickUnitRounded = ratio * Math.pow(10.0d, exp);
            minRounded = Math.floor(paddedMin / tickUnitRounded) * tickUnitRounded;
            maxRounded = Math.ceil(paddedMax / tickUnitRounded) * tickUnitRounded;
            double maxReqTickGap = 0.0d;
            double last = 0.0d;
            count = (int) Math.ceil((maxRounded - minRounded) / tickUnitRounded);
            double major = minRounded;
            for (int i3 = 0; major <= maxRounded && i3 < count; i3++) {
                Dimension2D markSize = measureTickMarkSize(Double.valueOf(major), getTickLabelRotation(), formatter);
                double size = side.isVertical() ? markSize.getHeight() : markSize.getWidth();
                if (i3 == 0) {
                    last = size / 2.0d;
                } else {
                    maxReqTickGap = Math.max(maxReqTickGap, last + 6.0d + (size / 2.0d));
                }
                major += tickUnitRounded;
            }
            reqLength = (count - 1) * maxReqTickGap;
            tickUnit = tickUnitRounded;
            if (numOfTickMarks == 2 && reqLength > length) {
                break;
            }
            if (reqLength > length || count > 20) {
                tickUnit *= 2.0d;
            }
        }
        double newScale = calculateNewScale(length, minRounded, maxRounded);
        return new Object[]{Double.valueOf(minRounded), Double.valueOf(maxRounded), Double.valueOf(tickUnitRounded), Double.valueOf(newScale), formatter};
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/NumberAxis$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<NumberAxis, Number> TICK_UNIT = new CssMetaData<NumberAxis, Number>("-fx-tick-unit", SizeConverter.getInstance(), Double.valueOf(5.0d)) { // from class: javafx.scene.chart.NumberAxis.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(NumberAxis n2) {
                return n2.tickUnit == null || !n2.tickUnit.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(NumberAxis n2) {
                return (StyleableProperty) n2.tickUnitProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(ValueAxis.getClassCssMetaData());
            styleables.add(TICK_UNIT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.chart.ValueAxis, javafx.scene.chart.Axis, javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/NumberAxis$DefaultFormatter.class */
    public static class DefaultFormatter extends StringConverter<Number> {
        private DecimalFormat formatter;
        private String prefix;
        private String suffix;

        public DefaultFormatter(NumberAxis axis) {
            this.prefix = null;
            this.suffix = null;
            this.formatter = axis.isAutoRanging() ? new DecimalFormat(axis.currentFormatterProperty.get()) : new DecimalFormat();
            ChangeListener<Object> axisListener = (observable, oldValue, newValue) -> {
                this.formatter = axis.isAutoRanging() ? new DecimalFormat(axis.currentFormatterProperty.get()) : new DecimalFormat();
            };
            axis.currentFormatterProperty.addListener(axisListener);
            axis.autoRangingProperty().addListener(axisListener);
        }

        public DefaultFormatter(NumberAxis axis, String prefix, String suffix) {
            this(axis);
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override // javafx.util.StringConverter
        public String toString(Number object) {
            return toString(object, this.formatter);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String toString(Number object, String numFormatter) {
            if (numFormatter == null || numFormatter.isEmpty()) {
                return toString(object, this.formatter);
            }
            return toString(object, new DecimalFormat(numFormatter));
        }

        private String toString(Number object, DecimalFormat formatter) {
            if (this.prefix != null && this.suffix != null) {
                return this.prefix + formatter.format(object) + this.suffix;
            }
            if (this.prefix != null) {
                return this.prefix + formatter.format(object);
            }
            if (this.suffix != null) {
                return formatter.format(object) + this.suffix;
            }
            return formatter.format(object);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.util.StringConverter
        public Number fromString(String string) {
            try {
                int prefixLength = this.prefix == null ? 0 : this.prefix.length();
                int suffixLength = this.suffix == null ? 0 : this.suffix.length();
                return this.formatter.parse(string.substring(prefixLength, string.length() - suffixLength));
            } catch (ParseException e2) {
                return null;
            }
        }
    }
}
