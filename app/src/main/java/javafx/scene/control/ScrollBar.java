package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.ScrollBarSkin;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

/* loaded from: jfxrt.jar:javafx/scene/control/ScrollBar.class */
public class ScrollBar extends Control {
    private DoubleProperty min;
    private DoubleProperty max;
    private DoubleProperty value;
    private ObjectProperty<Orientation> orientation;
    private DoubleProperty unitIncrement;
    private DoubleProperty blockIncrement;
    private DoubleProperty visibleAmount;
    private static final String DEFAULT_STYLE_CLASS = "scroll-bar";
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public ScrollBar() {
        setWidth(20.0d);
        setHeight(100.0d);
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.SCROLL_BAR);
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
        pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
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
            this.min = new SimpleDoubleProperty(this, "min");
        }
        return this.min;
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
            this.max = new SimpleDoubleProperty(this, "max", 100.0d);
        }
        return this.max;
    }

    public final void setValue(double value) {
        valueProperty().set(value);
    }

    public final double getValue() {
        if (this.value == null) {
            return 0.0d;
        }
        return this.value.get();
    }

    public final DoubleProperty valueProperty() {
        if (this.value == null) {
            this.value = new SimpleDoubleProperty(this, "value");
        }
        return this.value;
    }

    public final void setOrientation(Orientation value) {
        orientationProperty().set(value);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.HORIZONTAL : this.orientation.get();
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL) { // from class: javafx.scene.control.ScrollBar.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    boolean vertical = get() == Orientation.VERTICAL;
                    ScrollBar.this.pseudoClassStateChanged(ScrollBar.VERTICAL_PSEUDOCLASS_STATE, vertical);
                    ScrollBar.this.pseudoClassStateChanged(ScrollBar.HORIZONTAL_PSEUDOCLASS_STATE, !vertical);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<ScrollBar, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollBar.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "orientation";
                }
            };
        }
        return this.orientation;
    }

    public final void setUnitIncrement(double value) {
        unitIncrementProperty().set(value);
    }

    public final double getUnitIncrement() {
        if (this.unitIncrement == null) {
            return 1.0d;
        }
        return this.unitIncrement.get();
    }

    public final DoubleProperty unitIncrementProperty() {
        if (this.unitIncrement == null) {
            this.unitIncrement = new StyleableDoubleProperty(1.0d) { // from class: javafx.scene.control.ScrollBar.2
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.UNIT_INCREMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollBar.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "unitIncrement";
                }
            };
        }
        return this.unitIncrement;
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
            this.blockIncrement = new StyleableDoubleProperty(10.0d) { // from class: javafx.scene.control.ScrollBar.3
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.BLOCK_INCREMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollBar.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "blockIncrement";
                }
            };
        }
        return this.blockIncrement;
    }

    public final void setVisibleAmount(double value) {
        visibleAmountProperty().set(value);
    }

    public final double getVisibleAmount() {
        if (this.visibleAmount == null) {
            return 15.0d;
        }
        return this.visibleAmount.get();
    }

    public final DoubleProperty visibleAmountProperty() {
        if (this.visibleAmount == null) {
            this.visibleAmount = new SimpleDoubleProperty(this, "visibleAmount");
        }
        return this.visibleAmount;
    }

    public void adjustValue(double position) {
        double newValue;
        double posValue = ((getMax() - getMin()) * Utils.clamp(0.0d, position, 1.0d)) + getMin();
        if (Double.compare(posValue, getValue()) != 0) {
            if (posValue > getValue()) {
                newValue = getValue() + getBlockIncrement();
            } else {
                newValue = getValue() - getBlockIncrement();
            }
            boolean incrementing = position > (getValue() - getMin()) / (getMax() - getMin());
            if (incrementing && newValue > posValue) {
                newValue = posValue;
            }
            if (!incrementing && newValue < posValue) {
                newValue = posValue;
            }
            setValue(Utils.clamp(getMin(), newValue, getMax()));
        }
    }

    public void increment() {
        setValue(Utils.clamp(getMin(), getValue() + getUnitIncrement(), getMax()));
    }

    public void decrement() {
        setValue(Utils.clamp(getMin(), getValue() - getUnitIncrement(), getMax()));
    }

    private void blockIncrement() {
        adjustValue(getValue() + getBlockIncrement());
    }

    private void blockDecrement() {
        adjustValue(getValue() - getBlockIncrement());
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ScrollBarSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/ScrollBar$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<ScrollBar, Orientation> ORIENTATION = new CssMetaData<ScrollBar, Orientation>("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) { // from class: javafx.scene.control.ScrollBar.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public Orientation getInitialValue(ScrollBar node) {
                return node.getOrientation();
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(ScrollBar n2) {
                return n2.orientation == null || !n2.orientation.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Orientation> getStyleableProperty(ScrollBar n2) {
                return (StyleableProperty) n2.orientationProperty();
            }
        };
        private static final CssMetaData<ScrollBar, Number> UNIT_INCREMENT = new CssMetaData<ScrollBar, Number>("-fx-unit-increment", SizeConverter.getInstance(), Double.valueOf(1.0d)) { // from class: javafx.scene.control.ScrollBar.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ScrollBar n2) {
                return n2.unitIncrement == null || !n2.unitIncrement.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ScrollBar n2) {
                return (StyleableProperty) n2.unitIncrementProperty();
            }
        };
        private static final CssMetaData<ScrollBar, Number> BLOCK_INCREMENT = new CssMetaData<ScrollBar, Number>("-fx-block-increment", SizeConverter.getInstance(), Double.valueOf(10.0d)) { // from class: javafx.scene.control.ScrollBar.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ScrollBar n2) {
                return n2.blockIncrement == null || !n2.blockIncrement.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ScrollBar n2) {
                return (StyleableProperty) n2.blockIncrementProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(ORIENTATION);
            styleables.add(UNIT_INCREMENT);
            styleables.add(BLOCK_INCREMENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
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
            case BLOCK_INCREMENT:
                blockIncrement();
                break;
            case BLOCK_DECREMENT:
                blockDecrement();
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
