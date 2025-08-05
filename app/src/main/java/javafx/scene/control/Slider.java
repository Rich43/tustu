package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.SliderSkin;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/Slider.class */
public class Slider extends Control {
    private DoubleProperty max;
    private DoubleProperty min;
    private DoubleProperty value;
    private BooleanProperty valueChanging;
    private ObjectProperty<Orientation> orientation;
    private BooleanProperty showTickLabels;
    private BooleanProperty showTickMarks;
    private DoubleProperty majorTickUnit;
    private IntegerProperty minorTickCount;
    private BooleanProperty snapToTicks;
    private ObjectProperty<StringConverter<Double>> labelFormatter;
    private DoubleProperty blockIncrement;
    private static final String DEFAULT_STYLE_CLASS = "slider";
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public Slider() {
        initialize();
    }

    public Slider(double min, double max, double value) {
        setMax(max);
        setMin(min);
        setValue(value);
        adjustValues();
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.SLIDER);
    }

    public final void setMax(double value) {
        maxProperty().set(value);
    }

    public final double getMax() {
        if (this.max == null) {
            return 100.0d;
        }
        return this.max.get();
    }

    public final DoubleProperty maxProperty() {
        if (this.max == null) {
            this.max = new DoublePropertyBase(100.0d) { // from class: javafx.scene.control.Slider.1
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (get() < Slider.this.getMin()) {
                        Slider.this.setMin(get());
                    }
                    Slider.this.adjustValues();
                    Slider.this.notifyAccessibleAttributeChanged(AccessibleAttribute.MAX_VALUE);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "max";
                }
            };
        }
        return this.max;
    }

    public final void setMin(double value) {
        minProperty().set(value);
    }

    public final double getMin() {
        if (this.min == null) {
            return 0.0d;
        }
        return this.min.get();
    }

    public final DoubleProperty minProperty() {
        if (this.min == null) {
            this.min = new DoublePropertyBase(0.0d) { // from class: javafx.scene.control.Slider.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (get() > Slider.this.getMax()) {
                        Slider.this.setMax(get());
                    }
                    Slider.this.adjustValues();
                    Slider.this.notifyAccessibleAttributeChanged(AccessibleAttribute.MIN_VALUE);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "min";
                }
            };
        }
        return this.min;
    }

    public final void setValue(double value) {
        if (!valueProperty().isBound()) {
            valueProperty().set(value);
        }
    }

    public final double getValue() {
        if (this.value == null) {
            return 0.0d;
        }
        return this.value.get();
    }

    public final DoubleProperty valueProperty() {
        if (this.value == null) {
            this.value = new DoublePropertyBase(0.0d) { // from class: javafx.scene.control.Slider.3
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    Slider.this.adjustValues();
                    Slider.this.notifyAccessibleAttributeChanged(AccessibleAttribute.VALUE);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "value";
                }
            };
        }
        return this.value;
    }

    public final void setValueChanging(boolean value) {
        valueChangingProperty().set(value);
    }

    public final boolean isValueChanging() {
        if (this.valueChanging == null) {
            return false;
        }
        return this.valueChanging.get();
    }

    public final BooleanProperty valueChangingProperty() {
        if (this.valueChanging == null) {
            this.valueChanging = new SimpleBooleanProperty(this, "valueChanging", false);
        }
        return this.valueChanging;
    }

    public final void setOrientation(Orientation value) {
        orientationProperty().set(value);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.HORIZONTAL : this.orientation.get();
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL) { // from class: javafx.scene.control.Slider.4
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    boolean vertical = get() == Orientation.VERTICAL;
                    Slider.this.pseudoClassStateChanged(Slider.VERTICAL_PSEUDOCLASS_STATE, vertical);
                    Slider.this.pseudoClassStateChanged(Slider.HORIZONTAL_PSEUDOCLASS_STATE, !vertical);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Slider, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "orientation";
                }
            };
        }
        return this.orientation;
    }

    public final void setShowTickLabels(boolean value) {
        showTickLabelsProperty().set(value);
    }

    public final boolean isShowTickLabels() {
        if (this.showTickLabels == null) {
            return false;
        }
        return this.showTickLabels.get();
    }

    public final BooleanProperty showTickLabelsProperty() {
        if (this.showTickLabels == null) {
            this.showTickLabels = new StyleableBooleanProperty(false) { // from class: javafx.scene.control.Slider.5
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.SHOW_TICK_LABELS;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "showTickLabels";
                }
            };
        }
        return this.showTickLabels;
    }

    public final void setShowTickMarks(boolean value) {
        showTickMarksProperty().set(value);
    }

    public final boolean isShowTickMarks() {
        if (this.showTickMarks == null) {
            return false;
        }
        return this.showTickMarks.get();
    }

    public final BooleanProperty showTickMarksProperty() {
        if (this.showTickMarks == null) {
            this.showTickMarks = new StyleableBooleanProperty(false) { // from class: javafx.scene.control.Slider.6
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.SHOW_TICK_MARKS;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "showTickMarks";
                }
            };
        }
        return this.showTickMarks;
    }

    public final void setMajorTickUnit(double value) {
        if (value <= 0.0d) {
            throw new IllegalArgumentException("MajorTickUnit cannot be less than or equal to 0.");
        }
        majorTickUnitProperty().set(value);
    }

    public final double getMajorTickUnit() {
        if (this.majorTickUnit == null) {
            return 25.0d;
        }
        return this.majorTickUnit.get();
    }

    public final DoubleProperty majorTickUnitProperty() {
        if (this.majorTickUnit == null) {
            this.majorTickUnit = new StyleableDoubleProperty(25.0d) { // from class: javafx.scene.control.Slider.7
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (get() <= 0.0d) {
                        throw new IllegalArgumentException("MajorTickUnit cannot be less than or equal to 0.");
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.MAJOR_TICK_UNIT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "majorTickUnit";
                }
            };
        }
        return this.majorTickUnit;
    }

    public final void setMinorTickCount(int value) {
        minorTickCountProperty().set(value);
    }

    public final int getMinorTickCount() {
        if (this.minorTickCount == null) {
            return 3;
        }
        return this.minorTickCount.get();
    }

    public final IntegerProperty minorTickCountProperty() {
        if (this.minorTickCount == null) {
            this.minorTickCount = new StyleableIntegerProperty(3) { // from class: javafx.scene.control.Slider.8
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.MINOR_TICK_COUNT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "minorTickCount";
                }
            };
        }
        return this.minorTickCount;
    }

    public final void setSnapToTicks(boolean value) {
        snapToTicksProperty().set(value);
    }

    public final boolean isSnapToTicks() {
        if (this.snapToTicks == null) {
            return false;
        }
        return this.snapToTicks.get();
    }

    public final BooleanProperty snapToTicksProperty() {
        if (this.snapToTicks == null) {
            this.snapToTicks = new StyleableBooleanProperty(false) { // from class: javafx.scene.control.Slider.9
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.SNAP_TO_TICKS;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "snapToTicks";
                }
            };
        }
        return this.snapToTicks;
    }

    public final void setLabelFormatter(StringConverter<Double> value) {
        labelFormatterProperty().set(value);
    }

    public final StringConverter<Double> getLabelFormatter() {
        if (this.labelFormatter == null) {
            return null;
        }
        return this.labelFormatter.get();
    }

    public final ObjectProperty<StringConverter<Double>> labelFormatterProperty() {
        if (this.labelFormatter == null) {
            this.labelFormatter = new SimpleObjectProperty(this, "labelFormatter");
        }
        return this.labelFormatter;
    }

    public final void setBlockIncrement(double value) {
        blockIncrementProperty().set(value);
    }

    public final double getBlockIncrement() {
        if (this.blockIncrement == null) {
            return 10.0d;
        }
        return this.blockIncrement.get();
    }

    public final DoubleProperty blockIncrementProperty() {
        if (this.blockIncrement == null) {
            this.blockIncrement = new StyleableDoubleProperty(10.0d) { // from class: javafx.scene.control.Slider.10
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.BLOCK_INCREMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Slider.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "blockIncrement";
                }
            };
        }
        return this.blockIncrement;
    }

    public void adjustValue(double newValue) {
        double _min = getMin();
        double _max = getMax();
        if (_max <= _min) {
            return;
        }
        double newValue2 = newValue < _min ? _min : newValue;
        setValue(snapValueToTicks(newValue2 > _max ? _max : newValue2));
    }

    public void increment() {
        adjustValue(getValue() + getBlockIncrement());
    }

    public void decrement() {
        adjustValue(getValue() - getBlockIncrement());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustValues() {
        if (getValue() < getMin() || getValue() > getMax()) {
            setValue(Utils.clamp(getMin(), getValue(), getMax()));
        }
    }

    private double snapValueToTicks(double val) {
        double tickSpacing;
        double v2 = val;
        if (isSnapToTicks()) {
            if (getMinorTickCount() != 0) {
                tickSpacing = getMajorTickUnit() / (Math.max(getMinorTickCount(), 0) + 1);
            } else {
                tickSpacing = getMajorTickUnit();
            }
            int prevTick = (int) ((v2 - getMin()) / tickSpacing);
            double prevTickValue = (prevTick * tickSpacing) + getMin();
            double nextTickValue = ((prevTick + 1) * tickSpacing) + getMin();
            v2 = Utils.nearest(prevTickValue, v2, nextTickValue);
        }
        return Utils.clamp(getMin(), v2, getMax());
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new SliderSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/Slider$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Slider, Number> BLOCK_INCREMENT = new CssMetaData<Slider, Number>("-fx-block-increment", SizeConverter.getInstance(), Double.valueOf(10.0d)) { // from class: javafx.scene.control.Slider.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Slider n2) {
                return n2.blockIncrement == null || !n2.blockIncrement.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Slider n2) {
                return (StyleableProperty) n2.blockIncrementProperty();
            }
        };
        private static final CssMetaData<Slider, Boolean> SHOW_TICK_LABELS = new CssMetaData<Slider, Boolean>("-fx-show-tick-labels", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.control.Slider.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Slider n2) {
                return n2.showTickLabels == null || !n2.showTickLabels.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Slider n2) {
                return (StyleableProperty) n2.showTickLabelsProperty();
            }
        };
        private static final CssMetaData<Slider, Boolean> SHOW_TICK_MARKS = new CssMetaData<Slider, Boolean>("-fx-show-tick-marks", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.control.Slider.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Slider n2) {
                return n2.showTickMarks == null || !n2.showTickMarks.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Slider n2) {
                return (StyleableProperty) n2.showTickMarksProperty();
            }
        };
        private static final CssMetaData<Slider, Boolean> SNAP_TO_TICKS = new CssMetaData<Slider, Boolean>("-fx-snap-to-ticks", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.control.Slider.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Slider n2) {
                return n2.snapToTicks == null || !n2.snapToTicks.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Slider n2) {
                return (StyleableProperty) n2.snapToTicksProperty();
            }
        };
        private static final CssMetaData<Slider, Number> MAJOR_TICK_UNIT = new CssMetaData<Slider, Number>("-fx-major-tick-unit", SizeConverter.getInstance(), Double.valueOf(25.0d)) { // from class: javafx.scene.control.Slider.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Slider n2) {
                return n2.majorTickUnit == null || !n2.majorTickUnit.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Slider n2) {
                return (StyleableProperty) n2.majorTickUnitProperty();
            }
        };
        private static final CssMetaData<Slider, Number> MINOR_TICK_COUNT = new CssMetaData<Slider, Number>("-fx-minor-tick-count", SizeConverter.getInstance(), Double.valueOf(3.0d)) { // from class: javafx.scene.control.Slider.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Slider n2) {
                return n2.minorTickCount == null || !n2.minorTickCount.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Slider n2) {
                return (StyleableProperty) n2.minorTickCountProperty();
            }
        };
        private static final CssMetaData<Slider, Orientation> ORIENTATION = new CssMetaData<Slider, Orientation>("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) { // from class: javafx.scene.control.Slider.StyleableProperties.7
            @Override // javafx.css.CssMetaData
            public Orientation getInitialValue(Slider node) {
                return node.getOrientation();
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(Slider n2) {
                return n2.orientation == null || !n2.orientation.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Orientation> getStyleableProperty(Slider n2) {
                return (StyleableProperty) n2.orientationProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(BLOCK_INCREMENT);
            styleables.add(SHOW_TICK_LABELS);
            styleables.add(SHOW_TICK_MARKS);
            styleables.add(SNAP_TO_TICKS);
            styleables.add(MAJOR_TICK_UNIT);
            styleables.add(MINOR_TICK_COUNT);
            styleables.add(ORIENTATION);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Control
    @Deprecated
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case VALUE:
                return Double.valueOf(getValue());
            case MAX_VALUE:
                return Double.valueOf(getMax());
            case MIN_VALUE:
                return Double.valueOf(getMin());
            case ORIENTATION:
                return getOrientation();
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case INCREMENT:
                increment();
                break;
            case DECREMENT:
                decrement();
                break;
            case SET_VALUE:
                Double value = (Double) parameters[0];
                if (value != null) {
                    setValue(value.doubleValue());
                    break;
                }
                break;
            default:
                super.executeAccessibleAction(action, parameters);
                break;
        }
    }
}
