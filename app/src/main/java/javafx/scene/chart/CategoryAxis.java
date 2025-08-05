package javafx.scene.chart;

import com.sun.javafx.charts.ChartLayoutAnimator;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Side;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/chart/CategoryAxis.class */
public final class CategoryAxis extends Axis<String> {
    private List<String> allDataCategories;
    private boolean changeIsLocal;
    private final DoubleProperty firstCategoryPos;
    private Object currentAnimationID;
    private final ChartLayoutAnimator animator;
    private ListChangeListener<String> itemsListener;
    private DoubleProperty startMargin;
    private DoubleProperty endMargin;
    private BooleanProperty gapStartAndEnd;
    private ObjectProperty<ObservableList<String>> categories;
    private final ReadOnlyDoubleWrapper categorySpacing;

    public final double getStartMargin() {
        return this.startMargin.getValue2().doubleValue();
    }

    public final void setStartMargin(double value) {
        this.startMargin.setValue((Number) Double.valueOf(value));
    }

    public final DoubleProperty startMarginProperty() {
        return this.startMargin;
    }

    public final double getEndMargin() {
        return this.endMargin.getValue2().doubleValue();
    }

    public final void setEndMargin(double value) {
        this.endMargin.setValue((Number) Double.valueOf(value));
    }

    public final DoubleProperty endMarginProperty() {
        return this.endMargin;
    }

    public final boolean isGapStartAndEnd() {
        return this.gapStartAndEnd.getValue2().booleanValue();
    }

    public final void setGapStartAndEnd(boolean value) {
        this.gapStartAndEnd.setValue(Boolean.valueOf(value));
    }

    public final BooleanProperty gapStartAndEndProperty() {
        return this.gapStartAndEnd;
    }

    public final void setCategories(ObservableList<String> value) {
        this.categories.set(value);
        if (!this.changeIsLocal) {
            setAutoRanging(false);
            this.allDataCategories.clear();
            this.allDataCategories.addAll(getCategories());
        }
        requestAxisLayout();
    }

    private void checkAndRemoveDuplicates(String category) {
        if (getDuplicate() != null) {
            getCategories().remove(category);
            throw new IllegalArgumentException("Duplicate category ; " + category + " already present");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDuplicate() {
        if (getCategories() != null) {
            for (int i2 = 0; i2 < getCategories().size(); i2++) {
                for (int j2 = 0; j2 < getCategories().size(); j2++) {
                    if (getCategories().get(i2).equals(getCategories().get(j2)) && i2 != j2) {
                        return getCategories().get(i2);
                    }
                }
            }
            return null;
        }
        return null;
    }

    public final ObservableList<String> getCategories() {
        return this.categories.get();
    }

    public final double getCategorySpacing() {
        return this.categorySpacing.get();
    }

    public final ReadOnlyDoubleProperty categorySpacingProperty() {
        return this.categorySpacing.getReadOnlyProperty();
    }

    public CategoryAxis() {
        this.allDataCategories = new ArrayList();
        this.changeIsLocal = false;
        this.firstCategoryPos = new SimpleDoubleProperty(this, "firstCategoryPos", 0.0d);
        this.animator = new ChartLayoutAnimator(this);
        this.itemsListener = c2 -> {
            while (c2.next()) {
                if (!c2.getAddedSubList().isEmpty()) {
                    for (String addedStr : c2.getAddedSubList()) {
                        checkAndRemoveDuplicates(addedStr);
                    }
                }
                if (!isAutoRanging()) {
                    this.allDataCategories.clear();
                    this.allDataCategories.addAll(getCategories());
                    this.rangeValid = false;
                }
                requestAxisLayout();
            }
        };
        this.startMargin = new StyleableDoubleProperty(5.0d) { // from class: javafx.scene.chart.CategoryAxis.1
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                CategoryAxis.this.requestAxisLayout();
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.START_MARGIN;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return CategoryAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "startMargin";
            }
        };
        this.endMargin = new StyleableDoubleProperty(5.0d) { // from class: javafx.scene.chart.CategoryAxis.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                CategoryAxis.this.requestAxisLayout();
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.END_MARGIN;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return CategoryAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "endMargin";
            }
        };
        this.gapStartAndEnd = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.CategoryAxis.3
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                CategoryAxis.this.requestAxisLayout();
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.GAP_START_AND_END;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return CategoryAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "gapStartAndEnd";
            }
        };
        this.categories = new ObjectPropertyBase<ObservableList<String>>() { // from class: javafx.scene.chart.CategoryAxis.4
            ObservableList<String> old;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                if (CategoryAxis.this.getDuplicate() != null) {
                    throw new IllegalArgumentException("Duplicate category added; " + CategoryAxis.this.getDuplicate() + " already present");
                }
                ObservableList<String> newItems = get();
                if (this.old != newItems) {
                    if (this.old != null) {
                        this.old.removeListener(CategoryAxis.this.itemsListener);
                    }
                    if (newItems != null) {
                        newItems.addListener(CategoryAxis.this.itemsListener);
                    }
                    this.old = newItems;
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return CategoryAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "categories";
            }
        };
        this.categorySpacing = new ReadOnlyDoubleWrapper(this, "categorySpacing", 1.0d);
        this.changeIsLocal = true;
        setCategories(FXCollections.observableArrayList());
        this.changeIsLocal = false;
    }

    public CategoryAxis(ObservableList<String> categories) {
        this.allDataCategories = new ArrayList();
        this.changeIsLocal = false;
        this.firstCategoryPos = new SimpleDoubleProperty(this, "firstCategoryPos", 0.0d);
        this.animator = new ChartLayoutAnimator(this);
        this.itemsListener = c2 -> {
            while (c2.next()) {
                if (!c2.getAddedSubList().isEmpty()) {
                    for (String addedStr : c2.getAddedSubList()) {
                        checkAndRemoveDuplicates(addedStr);
                    }
                }
                if (!isAutoRanging()) {
                    this.allDataCategories.clear();
                    this.allDataCategories.addAll(getCategories());
                    this.rangeValid = false;
                }
                requestAxisLayout();
            }
        };
        this.startMargin = new StyleableDoubleProperty(5.0d) { // from class: javafx.scene.chart.CategoryAxis.1
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                CategoryAxis.this.requestAxisLayout();
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.START_MARGIN;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return CategoryAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "startMargin";
            }
        };
        this.endMargin = new StyleableDoubleProperty(5.0d) { // from class: javafx.scene.chart.CategoryAxis.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                CategoryAxis.this.requestAxisLayout();
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.END_MARGIN;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return CategoryAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "endMargin";
            }
        };
        this.gapStartAndEnd = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.CategoryAxis.3
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                CategoryAxis.this.requestAxisLayout();
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.GAP_START_AND_END;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return CategoryAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "gapStartAndEnd";
            }
        };
        this.categories = new ObjectPropertyBase<ObservableList<String>>() { // from class: javafx.scene.chart.CategoryAxis.4
            ObservableList<String> old;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                if (CategoryAxis.this.getDuplicate() != null) {
                    throw new IllegalArgumentException("Duplicate category added; " + CategoryAxis.this.getDuplicate() + " already present");
                }
                ObservableList<String> newItems = get();
                if (this.old != newItems) {
                    if (this.old != null) {
                        this.old.removeListener(CategoryAxis.this.itemsListener);
                    }
                    if (newItems != null) {
                        newItems.addListener(CategoryAxis.this.itemsListener);
                    }
                    this.old = newItems;
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return CategoryAxis.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "categories";
            }
        };
        this.categorySpacing = new ReadOnlyDoubleWrapper(this, "categorySpacing", 1.0d);
        setCategories(categories);
    }

    private double calculateNewSpacing(double length, List<String> categories) {
        getEffectiveSide();
        double newCategorySpacing = 1.0d;
        if (categories != null) {
            double bVal = isGapStartAndEnd() ? categories.size() : categories.size() - 1;
            newCategorySpacing = bVal == 0.0d ? 1.0d : ((length - getStartMargin()) - getEndMargin()) / bVal;
        }
        if (!isAutoRanging()) {
            this.categorySpacing.set(newCategorySpacing);
        }
        return newCategorySpacing;
    }

    private double calculateNewFirstPos(double length, double catSpacing) {
        double newPos;
        Side side = getEffectiveSide();
        double offset = isGapStartAndEnd() ? catSpacing / 2.0d : 0.0d;
        if (side.isHorizontal()) {
            newPos = 0.0d + getStartMargin() + offset;
        } else {
            newPos = (length - getStartMargin()) - offset;
        }
        if (!isAutoRanging()) {
            this.firstCategoryPos.set(newPos);
        }
        return newPos;
    }

    @Override // javafx.scene.chart.Axis
    protected Object getRange() {
        return new Object[]{getCategories(), Double.valueOf(this.categorySpacing.get()), Double.valueOf(this.firstCategoryPos.get()), Double.valueOf(getEffectiveTickLabelRotation())};
    }

    @Override // javafx.scene.chart.Axis
    protected void setRange(Object range, boolean animate) {
        Object[] rangeArray = (Object[]) range;
        List<String> categories = (List) rangeArray[0];
        double newCategorySpacing = ((Double) rangeArray[1]).doubleValue();
        double newFirstCategoryPos = ((Double) rangeArray[2]).doubleValue();
        setEffectiveTickLabelRotation(((Double) rangeArray[3]).doubleValue());
        this.changeIsLocal = true;
        setCategories(FXCollections.observableArrayList(categories));
        this.changeIsLocal = false;
        if (animate) {
            this.animator.stop(this.currentAnimationID);
            this.currentAnimationID = this.animator.animate(new KeyFrame(Duration.ZERO, new KeyValue(this.firstCategoryPos, Double.valueOf(this.firstCategoryPos.get())), new KeyValue(this.categorySpacing, Double.valueOf(this.categorySpacing.get()))), new KeyFrame(Duration.millis(1000.0d), new KeyValue(this.firstCategoryPos, Double.valueOf(newFirstCategoryPos)), new KeyValue(this.categorySpacing, Double.valueOf(newCategorySpacing))));
        } else {
            this.categorySpacing.set(newCategorySpacing);
            this.firstCategoryPos.set(newFirstCategoryPos);
        }
    }

    @Override // javafx.scene.chart.Axis
    protected Object autoRange(double length) {
        Side side = getEffectiveSide();
        double newCategorySpacing = calculateNewSpacing(length, this.allDataCategories);
        double newFirstPos = calculateNewFirstPos(length, newCategorySpacing);
        double tickLabelRotation = getTickLabelRotation();
        if (length >= 0.0d) {
            double requiredLengthToDisplay = calculateRequiredSize(side.isVertical(), tickLabelRotation);
            if (requiredLengthToDisplay > length) {
                if (side.isHorizontal() && tickLabelRotation != 90.0d) {
                    tickLabelRotation = 90.0d;
                }
                if (side.isVertical() && tickLabelRotation != 0.0d) {
                    tickLabelRotation = 0.0d;
                }
            }
        }
        return new Object[]{this.allDataCategories, Double.valueOf(newCategorySpacing), Double.valueOf(newFirstPos), Double.valueOf(tickLabelRotation)};
    }

    private double calculateRequiredSize(boolean axisVertical, double tickLabelRotation) {
        double maxReqTickGap = 0.0d;
        double last = 0.0d;
        boolean first = true;
        for (String category : this.allDataCategories) {
            Dimension2D textSize = measureTickMarkSize((CategoryAxis) category, tickLabelRotation);
            double size = (axisVertical || tickLabelRotation != 0.0d) ? textSize.getHeight() : textSize.getWidth();
            if (first) {
                first = false;
                last = size / 2.0d;
            } else {
                maxReqTickGap = Math.max(maxReqTickGap, last + 6.0d + (size / 2.0d));
            }
        }
        return getStartMargin() + (maxReqTickGap * this.allDataCategories.size()) + getEndMargin();
    }

    @Override // javafx.scene.chart.Axis
    protected List<String> calculateTickValues(double length, Object range) {
        Object[] rangeArray = (Object[]) range;
        return (List) rangeArray[0];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javafx.scene.chart.Axis
    public String getTickMarkLabel(String value) {
        return value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javafx.scene.chart.Axis
    public Dimension2D measureTickMarkSize(String value, Object range) {
        Object[] rangeArray = (Object[]) range;
        double tickLabelRotation = ((Double) rangeArray[3]).doubleValue();
        return measureTickMarkSize((CategoryAxis) value, tickLabelRotation);
    }

    @Override // javafx.scene.chart.Axis
    public void invalidateRange(List<String> data) {
        super.invalidateRange(data);
        List<String> categoryNames = new ArrayList<>();
        categoryNames.addAll(this.allDataCategories);
        for (String cat : this.allDataCategories) {
            if (!data.contains(cat)) {
                categoryNames.remove(cat);
            }
        }
        int i2 = 0;
        while (i2 < data.size()) {
            int len = categoryNames.size();
            if (!categoryNames.contains(data.get(i2))) {
                categoryNames.add(i2 > len ? len : i2, data.get(i2));
            }
            i2++;
        }
        this.allDataCategories.clear();
        this.allDataCategories.addAll(categoryNames);
    }

    final List<String> getAllDataCategories() {
        return this.allDataCategories;
    }

    @Override // javafx.scene.chart.Axis
    public double getDisplayPosition(String value) {
        ObservableList<String> cat = getCategories();
        if (!cat.contains(value)) {
            return Double.NaN;
        }
        if (getEffectiveSide().isHorizontal()) {
            return this.firstCategoryPos.get() + (cat.indexOf(value) * this.categorySpacing.get());
        }
        return this.firstCategoryPos.get() + (cat.indexOf(value) * this.categorySpacing.get() * (-1.0d));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.scene.chart.Axis
    public String getValueForDisplay(double displayPosition) {
        if (getEffectiveSide().isHorizontal()) {
            if (displayPosition < 0.0d || displayPosition > getWidth()) {
                return null;
            }
            double d2 = (displayPosition - this.firstCategoryPos.get()) / this.categorySpacing.get();
            return toRealValue(d2);
        }
        if (displayPosition < 0.0d || displayPosition > getHeight()) {
            return null;
        }
        double d3 = (displayPosition - this.firstCategoryPos.get()) / (this.categorySpacing.get() * (-1.0d));
        return toRealValue(d3);
    }

    @Override // javafx.scene.chart.Axis
    public boolean isValueOnAxis(String value) {
        return getCategories().indexOf(new StringBuilder().append("").append(value).toString()) != -1;
    }

    @Override // javafx.scene.chart.Axis
    public double toNumericValue(String value) {
        return getCategories().indexOf(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.scene.chart.Axis
    public String toRealValue(double value) {
        int index = (int) Math.round(value);
        List<String> categories = getCategories();
        if (index >= 0 && index < categories.size()) {
            return getCategories().get(index);
        }
        return null;
    }

    @Override // javafx.scene.chart.Axis
    public double getZeroPosition() {
        return Double.NaN;
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/CategoryAxis$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<CategoryAxis, Number> START_MARGIN = new CssMetaData<CategoryAxis, Number>("-fx-start-margin", SizeConverter.getInstance(), Double.valueOf(5.0d)) { // from class: javafx.scene.chart.CategoryAxis.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(CategoryAxis n2) {
                return n2.startMargin == null || !n2.startMargin.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(CategoryAxis n2) {
                return (StyleableProperty) n2.startMarginProperty();
            }
        };
        private static final CssMetaData<CategoryAxis, Number> END_MARGIN = new CssMetaData<CategoryAxis, Number>("-fx-end-margin", SizeConverter.getInstance(), Double.valueOf(5.0d)) { // from class: javafx.scene.chart.CategoryAxis.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(CategoryAxis n2) {
                return n2.endMargin == null || !n2.endMargin.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(CategoryAxis n2) {
                return (StyleableProperty) n2.endMarginProperty();
            }
        };
        private static final CssMetaData<CategoryAxis, Boolean> GAP_START_AND_END = new CssMetaData<CategoryAxis, Boolean>("-fx-gap-start-and-end", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.CategoryAxis.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(CategoryAxis n2) {
                return n2.gapStartAndEnd == null || !n2.gapStartAndEnd.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(CategoryAxis n2) {
                return (StyleableProperty) n2.gapStartAndEndProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Axis.getClassCssMetaData());
            styleables.add(START_MARGIN);
            styleables.add(END_MARGIN);
            styleables.add(GAP_START_AND_END);
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
