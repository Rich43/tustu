package javafx.scene.chart;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.layout.Region;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/chart/XYChart.class */
public abstract class XYChart<X, Y> extends Chart {
    static String DEFAULT_COLOR = "default-color";
    private final Axis<X> xAxis;
    private final Axis<Y> yAxis;
    private final BitSet colorBits = new BitSet(8);
    final Map<Series<X, Y>, Integer> seriesColorMap = new HashMap();
    private boolean rangeValid = false;
    private final Line verticalZeroLine = new Line();
    private final Line horizontalZeroLine = new Line();
    private final Path verticalGridLines = new Path();
    private final Path horizontalGridLines = new Path();
    private final Path horizontalRowFill = new Path();
    private final Path verticalRowFill = new Path();
    private final Region plotBackground = new Region();
    private final Group plotArea = new Group() { // from class: javafx.scene.chart.XYChart.1
        @Override // javafx.scene.Parent
        public void requestLayout() {
        }
    };
    private final Group plotContent = new Group();
    private final Rectangle plotAreaClip = new Rectangle();
    private final List<Series<X, Y>> displayedSeries = new ArrayList();
    private final ListChangeListener<Series<X, Y>> seriesChanged = c2 -> {
        ObservableList<? extends Series<X, Y>> series = c2.getList();
        while (c2.next()) {
            if (c2.wasPermutated()) {
                this.displayedSeries.sort((o1, o2) -> {
                    return series.indexOf(o2) - series.indexOf(o1);
                });
            }
            if (c2.getRemoved().size() > 0) {
                updateLegend();
            }
            Set<Series<X, Y>> dupCheck = new HashSet<>(this.displayedSeries);
            dupCheck.removeAll(c2.getRemoved());
            for (Series<X, Y> d2 : c2.getAddedSubList()) {
                if (!dupCheck.add(d2)) {
                    throw new IllegalArgumentException("Duplicate series added");
                }
            }
            for (Series<X, Y> s2 : c2.getRemoved()) {
                s2.setToRemove = true;
                seriesRemoved(s2);
                int idx = this.seriesColorMap.remove(s2).intValue();
                this.colorBits.clear(idx);
            }
            for (int i2 = c2.getFrom(); i2 < c2.getTo() && !c2.wasPermutated(); i2++) {
                Series<X, Y> s3 = (Series) c2.getList().get(i2);
                s3.setChart(this);
                if (s3.setToRemove) {
                    s3.setToRemove = false;
                    s3.getChart().seriesBeingRemovedIsAdded(s3);
                }
                this.displayedSeries.add(s3);
                int nextClearBit = this.colorBits.nextClearBit(0);
                this.colorBits.set(nextClearBit, true);
                s3.defaultColorStyleClass = DEFAULT_COLOR + (nextClearBit % 8);
                this.seriesColorMap.put(s3, Integer.valueOf(nextClearBit % 8));
                seriesAdded(s3, i2);
            }
            if (c2.getFrom() < c2.getTo()) {
                updateLegend();
            }
            seriesChanged(c2);
        }
        invalidateRange();
        requestChartLayout();
    };
    private ObjectProperty<ObservableList<Series<X, Y>>> data = new ObjectPropertyBase<ObservableList<Series<X, Y>>>() { // from class: javafx.scene.chart.XYChart.2
        private ObservableList<Series<X, Y>> old;

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            ObservableList<Series<X, Y>> current = getValue2();
            int saveAnimationState = -1;
            if (this.old != null) {
                this.old.removeListener(XYChart.this.seriesChanged);
                if (current != null && this.old.size() > 0) {
                    saveAnimationState = this.old.get(0).getChart().getAnimated() ? 1 : 2;
                    this.old.get(0).getChart().setAnimated(false);
                }
            }
            if (current != null) {
                current.addListener(XYChart.this.seriesChanged);
            }
            if (this.old != null || current != null) {
                final List<Series<X, Y>> removed = this.old != null ? this.old : Collections.emptyList();
                int toIndex = current != null ? current.size() : 0;
                if (toIndex > 0 || !removed.isEmpty()) {
                    XYChart.this.seriesChanged.onChanged(new NonIterableChange<Series<X, Y>>(0, toIndex, current) { // from class: javafx.scene.chart.XYChart.2.1
                        @Override // javafx.collections.ListChangeListener.Change
                        public List<Series<X, Y>> getRemoved() {
                            return removed;
                        }

                        @Override // com.sun.javafx.collections.NonIterableChange, javafx.collections.ListChangeListener.Change
                        protected int[] getPermutation() {
                            return new int[0];
                        }
                    });
                }
            } else if (this.old != null && this.old.size() > 0) {
                XYChart.this.seriesChanged.onChanged(new NonIterableChange<Series<X, Y>>(0, 0, current) { // from class: javafx.scene.chart.XYChart.2.2
                    @Override // javafx.collections.ListChangeListener.Change
                    public List<Series<X, Y>> getRemoved() {
                        return AnonymousClass2.this.old;
                    }

                    @Override // com.sun.javafx.collections.NonIterableChange, javafx.collections.ListChangeListener.Change
                    protected int[] getPermutation() {
                        return new int[0];
                    }
                });
            }
            if (current != null && current.size() > 0 && saveAnimationState != -1) {
                current.get(0).getChart().setAnimated(saveAnimationState == 1);
            }
            this.old = current;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return XYChart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "data";
        }
    };
    private BooleanProperty verticalGridLinesVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.XYChart.3
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return XYChart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "verticalGridLinesVisible";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
            return StyleableProperties.VERTICAL_GRID_LINE_VISIBLE;
        }
    };
    private BooleanProperty horizontalGridLinesVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.XYChart.4
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return XYChart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "horizontalGridLinesVisible";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
            return StyleableProperties.HORIZONTAL_GRID_LINE_VISIBLE;
        }
    };
    private BooleanProperty alternativeColumnFillVisible = new StyleableBooleanProperty(false) { // from class: javafx.scene.chart.XYChart.5
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return XYChart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "alternativeColumnFillVisible";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
            return StyleableProperties.ALTERNATIVE_COLUMN_FILL_VISIBLE;
        }
    };
    private BooleanProperty alternativeRowFillVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.XYChart.6
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return XYChart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "alternativeRowFillVisible";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
            return StyleableProperties.ALTERNATIVE_ROW_FILL_VISIBLE;
        }
    };
    private BooleanProperty verticalZeroLineVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.XYChart.7
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return XYChart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "verticalZeroLineVisible";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
            return StyleableProperties.VERTICAL_ZERO_LINE_VISIBLE;
        }
    };
    private BooleanProperty horizontalZeroLineVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.XYChart.8
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            XYChart.this.requestChartLayout();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return XYChart.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "horizontalZeroLineVisible";
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
            return StyleableProperties.HORIZONTAL_ZERO_LINE_VISIBLE;
        }
    };

    protected abstract void dataItemAdded(Series<X, Y> series, int i2, Data<X, Y> data);

    protected abstract void dataItemRemoved(Data<X, Y> data, Series<X, Y> series);

    protected abstract void dataItemChanged(Data<X, Y> data);

    protected abstract void seriesAdded(Series<X, Y> series, int i2);

    protected abstract void seriesRemoved(Series<X, Y> series);

    protected abstract void layoutPlotChildren();

    public Axis<X> getXAxis() {
        return this.xAxis;
    }

    public Axis<Y> getYAxis() {
        return this.yAxis;
    }

    public final ObservableList<Series<X, Y>> getData() {
        return this.data.getValue2();
    }

    public final void setData(ObservableList<Series<X, Y>> value) {
        this.data.setValue(value);
    }

    public final ObjectProperty<ObservableList<Series<X, Y>>> dataProperty() {
        return this.data;
    }

    public final boolean getVerticalGridLinesVisible() {
        return this.verticalGridLinesVisible.get();
    }

    public final void setVerticalGridLinesVisible(boolean value) {
        this.verticalGridLinesVisible.set(value);
    }

    public final BooleanProperty verticalGridLinesVisibleProperty() {
        return this.verticalGridLinesVisible;
    }

    public final boolean isHorizontalGridLinesVisible() {
        return this.horizontalGridLinesVisible.get();
    }

    public final void setHorizontalGridLinesVisible(boolean value) {
        this.horizontalGridLinesVisible.set(value);
    }

    public final BooleanProperty horizontalGridLinesVisibleProperty() {
        return this.horizontalGridLinesVisible;
    }

    public final boolean isAlternativeColumnFillVisible() {
        return this.alternativeColumnFillVisible.getValue2().booleanValue();
    }

    public final void setAlternativeColumnFillVisible(boolean value) {
        this.alternativeColumnFillVisible.setValue(Boolean.valueOf(value));
    }

    public final BooleanProperty alternativeColumnFillVisibleProperty() {
        return this.alternativeColumnFillVisible;
    }

    public final boolean isAlternativeRowFillVisible() {
        return this.alternativeRowFillVisible.getValue2().booleanValue();
    }

    public final void setAlternativeRowFillVisible(boolean value) {
        this.alternativeRowFillVisible.setValue(Boolean.valueOf(value));
    }

    public final BooleanProperty alternativeRowFillVisibleProperty() {
        return this.alternativeRowFillVisible;
    }

    public final boolean isVerticalZeroLineVisible() {
        return this.verticalZeroLineVisible.get();
    }

    public final void setVerticalZeroLineVisible(boolean value) {
        this.verticalZeroLineVisible.set(value);
    }

    public final BooleanProperty verticalZeroLineVisibleProperty() {
        return this.verticalZeroLineVisible;
    }

    public final boolean isHorizontalZeroLineVisible() {
        return this.horizontalZeroLineVisible.get();
    }

    public final void setHorizontalZeroLineVisible(boolean value) {
        this.horizontalZeroLineVisible.set(value);
    }

    public final BooleanProperty horizontalZeroLineVisibleProperty() {
        return this.horizontalZeroLineVisible;
    }

    protected ObservableList<Node> getPlotChildren() {
        return this.plotContent.getChildren();
    }

    public XYChart(Axis<X> xAxis, Axis<Y> yAxis) {
        this.xAxis = xAxis;
        if (xAxis.getSide() == null) {
            xAxis.setSide(Side.BOTTOM);
        }
        xAxis.setEffectiveOrientation(Orientation.HORIZONTAL);
        this.yAxis = yAxis;
        if (yAxis.getSide() == null) {
            yAxis.setSide(Side.LEFT);
        }
        yAxis.setEffectiveOrientation(Orientation.VERTICAL);
        xAxis.autoRangingProperty().addListener((ov, t2, t1) -> {
            updateAxisRange();
        });
        yAxis.autoRangingProperty().addListener((ov2, t3, t12) -> {
            updateAxisRange();
        });
        getChartChildren().addAll(this.plotBackground, this.plotArea, xAxis, yAxis);
        this.plotArea.setAutoSizeChildren(false);
        this.plotContent.setAutoSizeChildren(false);
        this.plotAreaClip.setSmooth(false);
        this.plotArea.setClip(this.plotAreaClip);
        this.plotArea.getChildren().addAll(this.verticalRowFill, this.horizontalRowFill, this.verticalGridLines, this.horizontalGridLines, this.verticalZeroLine, this.horizontalZeroLine, this.plotContent);
        this.plotContent.getStyleClass().setAll("plot-content");
        this.plotBackground.getStyleClass().setAll("chart-plot-background");
        this.verticalRowFill.getStyleClass().setAll("chart-alternative-column-fill");
        this.horizontalRowFill.getStyleClass().setAll("chart-alternative-row-fill");
        this.verticalGridLines.getStyleClass().setAll("chart-vertical-grid-lines");
        this.horizontalGridLines.getStyleClass().setAll("chart-horizontal-grid-lines");
        this.verticalZeroLine.getStyleClass().setAll("chart-vertical-zero-line");
        this.horizontalZeroLine.getStyleClass().setAll("chart-horizontal-zero-line");
        this.plotContent.setManaged(false);
        this.plotArea.setManaged(false);
        animatedProperty().addListener((valueModel, oldValue, newValue) -> {
            if (getXAxis() != null) {
                getXAxis().setAnimated(newValue.booleanValue());
            }
            if (getYAxis() != null) {
                getYAxis().setAnimated(newValue.booleanValue());
            }
        });
    }

    final int getDataSize() {
        ObservableList<Series<X, Y>> data = getData();
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void seriesNameChanged() {
        updateLegend();
        requestChartLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dataItemsChanged(Series<X, Y> series, List<Data<X, Y>> removed, int addedFrom, int addedTo, boolean permutation) {
        for (Data<X, Y> item : removed) {
            dataItemRemoved(item, series);
        }
        for (int i2 = addedFrom; i2 < addedTo; i2++) {
            Data<X, Y> item2 = series.getData().get(i2);
            dataItemAdded(series, i2, item2);
        }
        invalidateRange();
        requestChartLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dataXValueChanged(Data<X, Y> data) {
        if (data.getCurrentX() != data.getXValue()) {
            invalidateRange();
        }
        dataItemChanged(data);
        if (shouldAnimate()) {
            animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentXProperty(), data.getCurrentX())), new KeyFrame(Duration.millis(700.0d), new KeyValue(data.currentXProperty(), data.getXValue(), Interpolator.EASE_BOTH)));
        } else {
            data.setCurrentX(data.getXValue());
            requestChartLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dataYValueChanged(Data<X, Y> data) {
        if (data.getCurrentY() != data.getYValue()) {
            invalidateRange();
        }
        dataItemChanged(data);
        if (shouldAnimate()) {
            animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY())), new KeyFrame(Duration.millis(700.0d), new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH)));
        } else {
            data.setCurrentY(data.getYValue());
            requestChartLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dataExtraValueChanged(Data<X, Y> data) {
        if (data.getCurrentY() != data.getYValue()) {
            invalidateRange();
        }
        dataItemChanged(data);
        if (shouldAnimate()) {
            animate(new KeyFrame(Duration.ZERO, new KeyValue(data.currentYProperty(), data.getCurrentY())), new KeyFrame(Duration.millis(700.0d), new KeyValue(data.currentYProperty(), data.getYValue(), Interpolator.EASE_BOTH)));
        } else {
            data.setCurrentY(data.getYValue());
            requestChartLayout();
        }
    }

    protected void updateLegend() {
    }

    void seriesBeingRemovedIsAdded(Series<X, Y> series) {
    }

    void dataBeingRemovedIsAdded(Data<X, Y> item, Series<X, Y> series) {
    }

    protected void seriesChanged(ListChangeListener.Change<? extends Series> c2) {
    }

    private void invalidateRange() {
        this.rangeValid = false;
    }

    protected void updateAxisRange() {
        Axis<X> xa = getXAxis();
        Axis<Y> ya = getYAxis();
        List<X> xData = xa.isAutoRanging() ? new ArrayList<>() : null;
        List<Y> yData = ya.isAutoRanging() ? new ArrayList<>() : null;
        if (xData != null || yData != null) {
            for (Series<X, Y> series : getData()) {
                for (Data<X, Y> data : series.getData()) {
                    if (xData != null) {
                        xData.add(data.getXValue());
                    }
                    if (yData != null) {
                        yData.add(data.getYValue());
                    }
                }
            }
            if (xData != null) {
                xa.invalidateRange(xData);
            }
            if (yData != null) {
                ya.invalidateRange(yData);
            }
        }
    }

    @Override // javafx.scene.chart.Chart
    protected final void layoutChartChildren(double top, double left, double width, double height) {
        if (getData() == null) {
            return;
        }
        if (!this.rangeValid) {
            this.rangeValid = true;
            if (getData() != null) {
                updateAxisRange();
            }
        }
        double top2 = snapPosition(top);
        double left2 = snapPosition(left);
        Axis<X> xa = getXAxis();
        ObservableList<Axis.TickMark<X>> xaTickMarks = xa.getTickMarks();
        Axis<Y> ya = getYAxis();
        ObservableList<Axis.TickMark<Y>> yaTickMarks = ya.getTickMarks();
        if (xa == null || ya == null) {
            return;
        }
        double xAxisWidth = 0.0d;
        double xAxisHeight = 30.0d;
        double yAxisWidth = 0.0d;
        double yAxisHeight = 0.0d;
        for (int count = 0; count < 5; count++) {
            yAxisHeight = snapSize(height - xAxisHeight);
            if (yAxisHeight < 0.0d) {
                yAxisHeight = 0.0d;
            }
            yAxisWidth = ya.prefWidth(yAxisHeight);
            xAxisWidth = snapSize(width - yAxisWidth);
            if (xAxisWidth < 0.0d) {
                xAxisWidth = 0.0d;
            }
            double newXAxisHeight = xa.prefHeight(xAxisWidth);
            if (newXAxisHeight == xAxisHeight) {
                break;
            }
            xAxisHeight = newXAxisHeight;
        }
        double xAxisWidth2 = Math.ceil(xAxisWidth);
        double xAxisHeight2 = Math.ceil(xAxisHeight);
        double yAxisWidth2 = Math.ceil(yAxisWidth);
        double yAxisHeight2 = Math.ceil(yAxisHeight);
        double xAxisY = 0.0d;
        switch (xa.getEffectiveSide()) {
            case TOP:
                xa.setVisible(true);
                xAxisY = top2 + 1.0d;
                top2 += xAxisHeight2;
                break;
            case BOTTOM:
                xa.setVisible(true);
                xAxisY = top2 + yAxisHeight2;
                break;
        }
        double yAxisX = 0.0d;
        switch (ya.getEffectiveSide()) {
            case LEFT:
                ya.setVisible(true);
                yAxisX = left2 + 1.0d;
                left2 += yAxisWidth2;
                break;
            case RIGHT:
                ya.setVisible(true);
                yAxisX = left2 + xAxisWidth2;
                break;
        }
        xa.resizeRelocate(left2, xAxisY, xAxisWidth2, xAxisHeight2);
        ya.resizeRelocate(yAxisX, top2, yAxisWidth2, yAxisHeight2);
        xa.requestAxisLayout();
        xa.layout();
        ya.requestAxisLayout();
        ya.layout();
        layoutPlotChildren();
        double xAxisZero = xa.getZeroPosition();
        double yAxisZero = ya.getZeroPosition();
        if (Double.isNaN(xAxisZero) || !isVerticalZeroLineVisible()) {
            this.verticalZeroLine.setVisible(false);
        } else {
            this.verticalZeroLine.setStartX(left2 + xAxisZero + 0.5d);
            this.verticalZeroLine.setStartY(top2);
            this.verticalZeroLine.setEndX(left2 + xAxisZero + 0.5d);
            this.verticalZeroLine.setEndY(top2 + yAxisHeight2);
            this.verticalZeroLine.setVisible(true);
        }
        if (Double.isNaN(yAxisZero) || !isHorizontalZeroLineVisible()) {
            this.horizontalZeroLine.setVisible(false);
        } else {
            this.horizontalZeroLine.setStartX(left2);
            this.horizontalZeroLine.setStartY(top2 + yAxisZero + 0.5d);
            this.horizontalZeroLine.setEndX(left2 + xAxisWidth2);
            this.horizontalZeroLine.setEndY(top2 + yAxisZero + 0.5d);
            this.horizontalZeroLine.setVisible(true);
        }
        this.plotBackground.resizeRelocate(left2, top2, xAxisWidth2, yAxisHeight2);
        this.plotAreaClip.setX(left2);
        this.plotAreaClip.setY(top2);
        this.plotAreaClip.setWidth(xAxisWidth2 + 1.0d);
        this.plotAreaClip.setHeight(yAxisHeight2 + 1.0d);
        this.plotContent.setLayoutX(left2);
        this.plotContent.setLayoutY(top2);
        this.plotContent.requestLayout();
        this.verticalGridLines.getElements().clear();
        if (getVerticalGridLinesVisible()) {
            for (int i2 = 0; i2 < xaTickMarks.size(); i2++) {
                Axis.TickMark<X> tick = xaTickMarks.get(i2);
                double x2 = xa.getDisplayPosition(tick.getValue());
                if ((x2 != xAxisZero || !isVerticalZeroLineVisible()) && x2 > 0.0d && x2 <= xAxisWidth2) {
                    this.verticalGridLines.getElements().add(new MoveTo(left2 + x2 + 0.5d, top2));
                    this.verticalGridLines.getElements().add(new LineTo(left2 + x2 + 0.5d, top2 + yAxisHeight2));
                }
            }
        }
        this.horizontalGridLines.getElements().clear();
        if (isHorizontalGridLinesVisible()) {
            for (int i3 = 0; i3 < yaTickMarks.size(); i3++) {
                Axis.TickMark<Y> tick2 = yaTickMarks.get(i3);
                double y2 = ya.getDisplayPosition(tick2.getValue());
                if ((y2 != yAxisZero || !isHorizontalZeroLineVisible()) && y2 >= 0.0d && y2 < yAxisHeight2) {
                    this.horizontalGridLines.getElements().add(new MoveTo(left2, top2 + y2 + 0.5d));
                    this.horizontalGridLines.getElements().add(new LineTo(left2 + xAxisWidth2, top2 + y2 + 0.5d));
                }
            }
        }
        this.verticalRowFill.getElements().clear();
        if (isAlternativeColumnFillVisible()) {
            List<Double> tickPositionsPositive = new ArrayList<>();
            List<Double> tickPositionsNegative = new ArrayList<>();
            for (int i4 = 0; i4 < xaTickMarks.size(); i4++) {
                double pos = xa.getDisplayPosition(xaTickMarks.get(i4).getValue());
                if (pos == xAxisZero) {
                    tickPositionsPositive.add(Double.valueOf(pos));
                    tickPositionsNegative.add(Double.valueOf(pos));
                } else if (pos < xAxisZero) {
                    tickPositionsPositive.add(Double.valueOf(pos));
                } else {
                    tickPositionsNegative.add(Double.valueOf(pos));
                }
            }
            Collections.sort(tickPositionsPositive);
            Collections.sort(tickPositionsNegative);
            for (int i5 = 1; i5 < tickPositionsPositive.size(); i5 += 2) {
                if (i5 + 1 < tickPositionsPositive.size()) {
                    double x1 = tickPositionsPositive.get(i5).doubleValue();
                    double x22 = tickPositionsPositive.get(i5 + 1).doubleValue();
                    this.verticalRowFill.getElements().addAll(new MoveTo(left2 + x1, top2), new LineTo(left2 + x1, top2 + yAxisHeight2), new LineTo(left2 + x22, top2 + yAxisHeight2), new LineTo(left2 + x22, top2), new ClosePath());
                }
            }
            for (int i6 = 0; i6 < tickPositionsNegative.size(); i6 += 2) {
                if (i6 + 1 < tickPositionsNegative.size()) {
                    double x12 = tickPositionsNegative.get(i6).doubleValue();
                    double x23 = tickPositionsNegative.get(i6 + 1).doubleValue();
                    this.verticalRowFill.getElements().addAll(new MoveTo(left2 + x12, top2), new LineTo(left2 + x12, top2 + yAxisHeight2), new LineTo(left2 + x23, top2 + yAxisHeight2), new LineTo(left2 + x23, top2), new ClosePath());
                }
            }
        }
        this.horizontalRowFill.getElements().clear();
        if (isAlternativeRowFillVisible()) {
            List<Double> tickPositionsPositive2 = new ArrayList<>();
            List<Double> tickPositionsNegative2 = new ArrayList<>();
            for (int i7 = 0; i7 < yaTickMarks.size(); i7++) {
                double pos2 = ya.getDisplayPosition(yaTickMarks.get(i7).getValue());
                if (pos2 == yAxisZero) {
                    tickPositionsPositive2.add(Double.valueOf(pos2));
                    tickPositionsNegative2.add(Double.valueOf(pos2));
                } else if (pos2 < yAxisZero) {
                    tickPositionsPositive2.add(Double.valueOf(pos2));
                } else {
                    tickPositionsNegative2.add(Double.valueOf(pos2));
                }
            }
            Collections.sort(tickPositionsPositive2);
            Collections.sort(tickPositionsNegative2);
            for (int i8 = 1; i8 < tickPositionsPositive2.size(); i8 += 2) {
                if (i8 + 1 < tickPositionsPositive2.size()) {
                    double y1 = tickPositionsPositive2.get(i8).doubleValue();
                    double y22 = tickPositionsPositive2.get(i8 + 1).doubleValue();
                    this.horizontalRowFill.getElements().addAll(new MoveTo(left2, top2 + y1), new LineTo(left2 + xAxisWidth2, top2 + y1), new LineTo(left2 + xAxisWidth2, top2 + y22), new LineTo(left2, top2 + y22), new ClosePath());
                }
            }
            for (int i9 = 0; i9 < tickPositionsNegative2.size(); i9 += 2) {
                if (i9 + 1 < tickPositionsNegative2.size()) {
                    double y12 = tickPositionsNegative2.get(i9).doubleValue();
                    double y23 = tickPositionsNegative2.get(i9 + 1).doubleValue();
                    this.horizontalRowFill.getElements().addAll(new MoveTo(left2, top2 + y12), new LineTo(left2 + xAxisWidth2, top2 + y12), new LineTo(left2 + xAxisWidth2, top2 + y23), new LineTo(left2, top2 + y23), new ClosePath());
                }
            }
        }
    }

    int getSeriesIndex(Series<X, Y> series) {
        return this.displayedSeries.indexOf(series);
    }

    int getSeriesSize() {
        return this.displayedSeries.size();
    }

    protected final void removeSeriesFromDisplay(Series<X, Y> series) {
        if (series != null) {
            series.setToRemove = false;
        }
        series.setChart(null);
        this.displayedSeries.remove(series);
    }

    protected final Iterator<Series<X, Y>> getDisplayedSeriesIterator() {
        return Collections.unmodifiableList(this.displayedSeries).iterator();
    }

    final KeyFrame[] createSeriesRemoveTimeLine(Series<X, Y> series, long fadeOutTime) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(series.getNode());
        for (Data<X, Y> d2 : series.getData()) {
            if (d2.getNode() != null) {
                nodes.add(d2.getNode());
            }
        }
        KeyValue[] startValues = new KeyValue[nodes.size()];
        KeyValue[] endValues = new KeyValue[nodes.size()];
        for (int j2 = 0; j2 < nodes.size(); j2++) {
            startValues[j2] = new KeyValue(nodes.get(j2).opacityProperty(), 1);
            endValues[j2] = new KeyValue(nodes.get(j2).opacityProperty(), 0);
        }
        return new KeyFrame[]{new KeyFrame(Duration.ZERO, startValues), new KeyFrame(Duration.millis(fadeOutTime), (EventHandler<ActionEvent>) actionEvent -> {
            getPlotChildren().removeAll(nodes);
            removeSeriesFromDisplay(series);
        }, endValues)};
    }

    protected final X getCurrentDisplayedXValue(Data<X, Y> item) {
        return item.getCurrentX();
    }

    protected final void setCurrentDisplayedXValue(Data<X, Y> item, X value) {
        item.setCurrentX(value);
    }

    protected final ObjectProperty<X> currentDisplayedXValueProperty(Data<X, Y> item) {
        return item.currentXProperty();
    }

    protected final Y getCurrentDisplayedYValue(Data<X, Y> item) {
        return item.getCurrentY();
    }

    protected final void setCurrentDisplayedYValue(Data<X, Y> item, Y value) {
        item.setCurrentY(value);
    }

    protected final ObjectProperty<Y> currentDisplayedYValueProperty(Data<X, Y> item) {
        return item.currentYProperty();
    }

    protected final Object getCurrentDisplayedExtraValue(Data<X, Y> item) {
        return item.getCurrentExtraValue();
    }

    protected final void setCurrentDisplayedExtraValue(Data<X, Y> item, Object value) {
        item.setCurrentExtraValue(value);
    }

    protected final ObjectProperty<Object> currentDisplayedExtraValueProperty(Data<X, Y> item) {
        return item.currentExtraValueProperty();
    }

    protected final Iterator<Data<X, Y>> getDisplayedDataIterator(Series<X, Y> series) {
        return Collections.unmodifiableList(((Series) series).displayedData).iterator();
    }

    protected final void removeDataItemFromDisplay(Series<X, Y> series, Data<X, Y> item) {
        series.removeDataItemRef(item);
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/XYChart$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<XYChart<?, ?>, Boolean> HORIZONTAL_GRID_LINE_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-horizontal-grid-lines-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.XYChart.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(XYChart<?, ?> node) {
                return ((XYChart) node).horizontalGridLinesVisible == null || !((XYChart) node).horizontalGridLinesVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> node) {
                return (StyleableProperty) node.horizontalGridLinesVisibleProperty();
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> HORIZONTAL_ZERO_LINE_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-horizontal-zero-line-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.XYChart.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(XYChart<?, ?> node) {
                return ((XYChart) node).horizontalZeroLineVisible == null || !((XYChart) node).horizontalZeroLineVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> node) {
                return (StyleableProperty) node.horizontalZeroLineVisibleProperty();
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> ALTERNATIVE_ROW_FILL_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-alternative-row-fill-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.XYChart.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(XYChart<?, ?> node) {
                return ((XYChart) node).alternativeRowFillVisible == null || !((XYChart) node).alternativeRowFillVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> node) {
                return (StyleableProperty) node.alternativeRowFillVisibleProperty();
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> VERTICAL_GRID_LINE_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-vertical-grid-lines-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.XYChart.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(XYChart<?, ?> node) {
                return ((XYChart) node).verticalGridLinesVisible == null || !((XYChart) node).verticalGridLinesVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> node) {
                return (StyleableProperty) node.verticalGridLinesVisibleProperty();
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> VERTICAL_ZERO_LINE_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-vertical-zero-line-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.XYChart.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(XYChart<?, ?> node) {
                return ((XYChart) node).verticalZeroLineVisible == null || !((XYChart) node).verticalZeroLineVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> node) {
                return (StyleableProperty) node.verticalZeroLineVisibleProperty();
            }
        };
        private static final CssMetaData<XYChart<?, ?>, Boolean> ALTERNATIVE_COLUMN_FILL_VISIBLE = new CssMetaData<XYChart<?, ?>, Boolean>("-fx-alternative-column-fill-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.XYChart.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(XYChart<?, ?> node) {
                return ((XYChart) node).alternativeColumnFillVisible == null || !((XYChart) node).alternativeColumnFillVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(XYChart<?, ?> node) {
                return (StyleableProperty) node.alternativeColumnFillVisibleProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Chart.getClassCssMetaData());
            styleables.add(HORIZONTAL_GRID_LINE_VISIBLE);
            styleables.add(HORIZONTAL_ZERO_LINE_VISIBLE);
            styleables.add(ALTERNATIVE_ROW_FILL_VISIBLE);
            styleables.add(VERTICAL_GRID_LINE_VISIBLE);
            styleables.add(VERTICAL_ZERO_LINE_VISIBLE);
            styleables.add(ALTERNATIVE_COLUMN_FILL_VISIBLE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.chart.Chart, javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/XYChart$Data.class */
    public static final class Data<X, Y> {
        private Series<X, Y> series;
        private boolean setToRemove = false;
        private ObjectProperty<X> xValue = new ObjectPropertyBase<X>() { // from class: javafx.scene.chart.XYChart.Data.1
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                get();
                if (Data.this.series != null) {
                    XYChart<X, Y> chart = Data.this.series.getChart();
                    if (chart != null) {
                        chart.dataXValueChanged(Data.this);
                        return;
                    }
                    return;
                }
                Data.this.setCurrentX(get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Data.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "XValue";
            }
        };
        private ObjectProperty<Y> yValue = new ObjectPropertyBase<Y>() { // from class: javafx.scene.chart.XYChart.Data.2
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                get();
                if (Data.this.series != null) {
                    XYChart<X, Y> chart = Data.this.series.getChart();
                    if (chart != null) {
                        chart.dataYValueChanged(Data.this);
                        return;
                    }
                    return;
                }
                Data.this.setCurrentY(get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Data.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "YValue";
            }
        };
        private ObjectProperty<Object> extraValue = new ObjectPropertyBase<Object>() { // from class: javafx.scene.chart.XYChart.Data.3
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                XYChart<X, Y> chart;
                get();
                if (Data.this.series != null && (chart = Data.this.series.getChart()) != null) {
                    chart.dataExtraValueChanged(Data.this);
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Data.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "extraValue";
            }
        };
        private ObjectProperty<Node> node = new SimpleObjectProperty<Node>(this, "node") { // from class: javafx.scene.chart.XYChart.Data.4
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                Node node = get();
                if (node != null) {
                    node.accessibleTextProperty().unbind();
                    node.accessibleTextProperty().bind(new StringBinding() { // from class: javafx.scene.chart.XYChart.Data.4.1
                        {
                            bind(Data.this.currentXProperty(), Data.this.currentYProperty());
                        }

                        @Override // javafx.beans.binding.StringBinding
                        protected String computeValue() {
                            String seriesName = Data.this.series != null ? Data.this.series.getName() : "";
                            return seriesName + " X Axis is " + Data.this.getCurrentX() + " Y Axis is " + Data.this.getCurrentY();
                        }
                    });
                }
            }
        };
        private ObjectProperty<X> currentX = new SimpleObjectProperty(this, "currentX");
        private ObjectProperty<Y> currentY = new SimpleObjectProperty(this, "currentY");
        private ObjectProperty<Object> currentExtraValue = new SimpleObjectProperty(this, "currentExtraValue");

        void setSeries(Series<X, Y> series) {
            this.series = series;
        }

        public final X getXValue() {
            return this.xValue.get();
        }

        public final void setXValue(X value) {
            this.xValue.set(value);
            if (this.currentX.get() == null || (this.series != null && this.series.getChart() == null)) {
                this.currentX.setValue(value);
            }
        }

        public final ObjectProperty<X> XValueProperty() {
            return this.xValue;
        }

        public final Y getYValue() {
            return this.yValue.get();
        }

        public final void setYValue(Y value) {
            this.yValue.set(value);
            if (this.currentY.get() == null || (this.series != null && this.series.getChart() == null)) {
                this.currentY.setValue(value);
            }
        }

        public final ObjectProperty<Y> YValueProperty() {
            return this.yValue;
        }

        public final Object getExtraValue() {
            return this.extraValue.get();
        }

        public final void setExtraValue(Object value) {
            this.extraValue.set(value);
        }

        public final ObjectProperty<Object> extraValueProperty() {
            return this.extraValue;
        }

        public final Node getNode() {
            return this.node.get();
        }

        public final void setNode(Node value) {
            this.node.set(value);
        }

        public final ObjectProperty<Node> nodeProperty() {
            return this.node;
        }

        final X getCurrentX() {
            return this.currentX.get();
        }

        final void setCurrentX(X value) {
            this.currentX.set(value);
        }

        final ObjectProperty<X> currentXProperty() {
            return this.currentX;
        }

        final Y getCurrentY() {
            return this.currentY.get();
        }

        final void setCurrentY(Y value) {
            this.currentY.set(value);
        }

        final ObjectProperty<Y> currentYProperty() {
            return this.currentY;
        }

        final Object getCurrentExtraValue() {
            return this.currentExtraValue.getValue2();
        }

        final void setCurrentExtraValue(Object value) {
            this.currentExtraValue.setValue(value);
        }

        final ObjectProperty<Object> currentExtraValueProperty() {
            return this.currentExtraValue;
        }

        public Data() {
        }

        public Data(X xValue, Y yValue) {
            setXValue(xValue);
            setYValue(yValue);
            setCurrentX(xValue);
            setCurrentY(yValue);
        }

        public Data(X xValue, Y yValue, Object extraValue) {
            setXValue(xValue);
            setYValue(yValue);
            setExtraValue(extraValue);
            setCurrentX(xValue);
            setCurrentY(yValue);
            setCurrentExtraValue(extraValue);
        }

        public String toString() {
            return "Data[" + ((Object) getXValue()) + "," + ((Object) getYValue()) + "," + getExtraValue() + "]";
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/XYChart$Series.class */
    public static final class Series<X, Y> {
        String defaultColorStyleClass;
        boolean setToRemove;
        private List<Data<X, Y>> displayedData;
        private final ListChangeListener<Data<X, Y>> dataChangeListener;
        private final ReadOnlyObjectWrapper<XYChart<X, Y>> chart;
        private final StringProperty name;
        private ObjectProperty<Node> node;
        private final ObjectProperty<ObservableList<Data<X, Y>>> data;

        public final XYChart<X, Y> getChart() {
            return this.chart.get();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setChart(XYChart<X, Y> value) {
            this.chart.set(value);
        }

        public final ReadOnlyObjectProperty<XYChart<X, Y>> chartProperty() {
            return this.chart.getReadOnlyProperty();
        }

        public final String getName() {
            return this.name.get();
        }

        public final void setName(String value) {
            this.name.set(value);
        }

        public final StringProperty nameProperty() {
            return this.name;
        }

        public final Node getNode() {
            return this.node.get();
        }

        public final void setNode(Node value) {
            this.node.set(value);
        }

        public final ObjectProperty<Node> nodeProperty() {
            return this.node;
        }

        public final ObservableList<Data<X, Y>> getData() {
            return this.data.getValue2();
        }

        public final void setData(ObservableList<Data<X, Y>> value) {
            this.data.setValue(value);
        }

        public final ObjectProperty<ObservableList<Data<X, Y>>> dataProperty() {
            return this.data;
        }

        public Series() {
            this(FXCollections.observableArrayList());
        }

        public Series(ObservableList<Data<X, Y>> data) {
            this.setToRemove = false;
            this.displayedData = new ArrayList();
            this.dataChangeListener = new ListChangeListener<Data<X, Y>>() { // from class: javafx.scene.chart.XYChart.Series.1
                @Override // javafx.collections.ListChangeListener
                public void onChanged(ListChangeListener.Change<? extends Data<X, Y>> c2) {
                    ObservableList<? extends Data<X, Y>> data2 = c2.getList();
                    XYChart<X, Y> chart = Series.this.getChart();
                    while (c2.next()) {
                        if (chart != null) {
                            if (c2.wasPermutated()) {
                                Series.this.displayedData.sort((o1, o2) -> {
                                    return data2.indexOf(o2) - data2.indexOf(o1);
                                });
                                return;
                            }
                            Set<Data<X, Y>> dupCheck = new HashSet<>(Series.this.displayedData);
                            dupCheck.removeAll(c2.getRemoved());
                            for (Data<X, Y> d2 : c2.getAddedSubList()) {
                                if (!dupCheck.add(d2)) {
                                    throw new IllegalArgumentException("Duplicate data added");
                                }
                            }
                            for (Data<X, Y> item : c2.getRemoved()) {
                                ((Data) item).setToRemove = true;
                            }
                            if (c2.getAddedSize() > 0) {
                                for (Data<X, Y> itemPtr : c2.getAddedSubList()) {
                                    if (((Data) itemPtr).setToRemove) {
                                        if (chart != null) {
                                            chart.dataBeingRemovedIsAdded(itemPtr, Series.this);
                                        }
                                        ((Data) itemPtr).setToRemove = false;
                                    }
                                }
                                for (Data<X, Y> d3 : c2.getAddedSubList()) {
                                    d3.setSeries(Series.this);
                                }
                                if (c2.getFrom() == 0) {
                                    Series.this.displayedData.addAll(0, c2.getAddedSubList());
                                } else {
                                    Series.this.displayedData.addAll(Series.this.displayedData.indexOf(data2.get(c2.getFrom() - 1)) + 1, c2.getAddedSubList());
                                }
                            }
                            chart.dataItemsChanged(Series.this, c2.getRemoved(), c2.getFrom(), c2.getTo(), c2.wasPermutated());
                        } else {
                            Set<Data<X, Y>> dupCheck2 = new HashSet<>();
                            for (Data<X, Y> d4 : data2) {
                                if (!dupCheck2.add(d4)) {
                                    throw new IllegalArgumentException("Duplicate data added");
                                }
                            }
                            for (Data<X, Y> d5 : c2.getAddedSubList()) {
                                d5.setSeries(Series.this);
                            }
                        }
                    }
                }
            };
            this.chart = new ReadOnlyObjectWrapper<XYChart<X, Y>>(this, "chart") { // from class: javafx.scene.chart.XYChart.Series.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (get() == null) {
                        Series.this.displayedData.clear();
                    } else {
                        Series.this.displayedData.addAll(Series.this.getData());
                    }
                }
            };
            this.name = new StringPropertyBase() { // from class: javafx.scene.chart.XYChart.Series.3
                @Override // javafx.beans.property.StringPropertyBase
                protected void invalidated() {
                    get();
                    if (Series.this.getChart() != null) {
                        Series.this.getChart().seriesNameChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Series.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "name";
                }
            };
            this.node = new SimpleObjectProperty(this, "node");
            this.data = new ObjectPropertyBase<ObservableList<Data<X, Y>>>() { // from class: javafx.scene.chart.XYChart.Series.4
                private ObservableList<Data<X, Y>> old;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ObservableList<Data<X, Y>> current = getValue2();
                    if (this.old != null) {
                        this.old.removeListener(Series.this.dataChangeListener);
                    }
                    if (current != null) {
                        current.addListener(Series.this.dataChangeListener);
                    }
                    if (this.old != null || current != null) {
                        final List<Data<X, Y>> removed = this.old != null ? this.old : Collections.emptyList();
                        int toIndex = current != null ? current.size() : 0;
                        if (toIndex > 0 || !removed.isEmpty()) {
                            Series.this.dataChangeListener.onChanged(new NonIterableChange<Data<X, Y>>(0, toIndex, current) { // from class: javafx.scene.chart.XYChart.Series.4.1
                                @Override // javafx.collections.ListChangeListener.Change
                                public List<Data<X, Y>> getRemoved() {
                                    return removed;
                                }

                                @Override // com.sun.javafx.collections.NonIterableChange, javafx.collections.ListChangeListener.Change
                                protected int[] getPermutation() {
                                    return new int[0];
                                }
                            });
                        }
                    } else if (this.old != null && this.old.size() > 0) {
                        Series.this.dataChangeListener.onChanged(new NonIterableChange<Data<X, Y>>(0, 0, current) { // from class: javafx.scene.chart.XYChart.Series.4.2
                            @Override // javafx.collections.ListChangeListener.Change
                            public List<Data<X, Y>> getRemoved() {
                                return AnonymousClass4.this.old;
                            }

                            @Override // com.sun.javafx.collections.NonIterableChange, javafx.collections.ListChangeListener.Change
                            protected int[] getPermutation() {
                                return new int[0];
                            }
                        });
                    }
                    this.old = current;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Series.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "data";
                }
            };
            setData(data);
            for (Data<X, Y> item : data) {
                item.setSeries(this);
            }
        }

        public Series(String name, ObservableList<Data<X, Y>> data) {
            this(data);
            setName(name);
        }

        public String toString() {
            return "Series[" + getName() + "]";
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeDataItemRef(Data<X, Y> item) {
            if (item != null) {
                ((Data) item).setToRemove = false;
            }
            this.displayedData.remove(item);
        }

        int getItemIndex(Data<X, Y> item) {
            return this.displayedData.indexOf(item);
        }

        Data<X, Y> getItem(int i2) {
            return this.displayedData.get(i2);
        }

        int getDataSize() {
            return this.displayedData.size();
        }
    }
}
