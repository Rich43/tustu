package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Cell;
import javafx.scene.control.SkinBase;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/CellSkinBase.class */
public class CellSkinBase<C extends Cell, B extends BehaviorBase<C>> extends LabeledSkinBase<C, B> {
    private DoubleProperty cellSize;
    static final double DEFAULT_CELL_SIZE = 24.0d;

    public final double getCellSize() {
        return this.cellSize == null ? DEFAULT_CELL_SIZE : this.cellSize.get();
    }

    public final ReadOnlyDoubleProperty cellSizeProperty() {
        return cellSizePropertyImpl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DoubleProperty cellSizePropertyImpl() {
        if (this.cellSize == null) {
            this.cellSize = new StyleableDoubleProperty(DEFAULT_CELL_SIZE) { // from class: com.sun.javafx.scene.control.skin.CellSkinBase.1
                @Override // javafx.css.StyleableDoubleProperty, javafx.css.StyleableProperty
                public void applyStyle(StyleOrigin origin, Number value) {
                    double size = value == null ? CellSkinBase.DEFAULT_CELL_SIZE : value.doubleValue();
                    super.applyStyle(origin, (Number) Double.valueOf(size <= 0.0d ? CellSkinBase.DEFAULT_CELL_SIZE : size));
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.css.StyleableDoubleProperty, javafx.beans.property.DoublePropertyBase, javafx.beans.value.WritableDoubleValue
                public void set(double value) {
                    super.set(value);
                    ((Cell) CellSkinBase.this.getSkinnable()).requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CellSkinBase.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "cellSize";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.CELL_SIZE;
                }
            };
        }
        return this.cellSize;
    }

    public CellSkinBase(C control, B behavior) {
        super(control, behavior);
        consumeMouseEvents(false);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/CellSkinBase$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Cell<?>, Number> CELL_SIZE = new CssMetaData<Cell<?>, Number>("-fx-cell-size", SizeConverter.getInstance(), Double.valueOf(CellSkinBase.DEFAULT_CELL_SIZE)) { // from class: com.sun.javafx.scene.control.skin.CellSkinBase.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Cell<?> n2) {
                CellSkinBase<?, ?> skin = (CellSkinBase) n2.getSkin();
                return ((CellSkinBase) skin).cellSize == null || !((CellSkinBase) skin).cellSize.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Cell<?> n2) {
                CellSkinBase<?, ?> skin = (CellSkinBase) n2.getSkin();
                return (StyleableProperty) skin.cellSizePropertyImpl();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(SkinBase.getClassCssMetaData());
            styleables.add(CELL_SIZE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.SkinBase
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}
