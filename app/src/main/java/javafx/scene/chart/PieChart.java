package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Side;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/chart/PieChart.class */
public class PieChart extends Chart {
    private static final int MIN_PIE_RADIUS = 25;
    private static final double LABEL_TICK_GAP = 6.0d;
    private static final double LABEL_BALL_RADIUS = 2.0d;
    private BitSet colorBits;
    private double centerX;
    private double centerY;
    private double pieRadius;
    private Data begin;
    private final Path labelLinePath;
    private Legend legend;
    private Data dataItemBeingRemoved;
    private Timeline dataRemoveTimeline;
    private final ListChangeListener<Data> dataChangeListener;
    private ObjectProperty<ObservableList<Data>> data;
    private DoubleProperty startAngle;
    private BooleanProperty clockwise;
    private DoubleProperty labelLineLength;
    private BooleanProperty labelsVisible;

    public final ObservableList<Data> getData() {
        return this.data.getValue2();
    }

    public final void setData(ObservableList<Data> value) {
        this.data.setValue(value);
    }

    public final ObjectProperty<ObservableList<Data>> dataProperty() {
        return this.data;
    }

    public final double getStartAngle() {
        return this.startAngle.getValue2().doubleValue();
    }

    public final void setStartAngle(double value) {
        this.startAngle.setValue((Number) Double.valueOf(value));
    }

    public final DoubleProperty startAngleProperty() {
        return this.startAngle;
    }

    public final void setClockwise(boolean value) {
        this.clockwise.setValue(Boolean.valueOf(value));
    }

    public final boolean isClockwise() {
        return this.clockwise.getValue2().booleanValue();
    }

    public final BooleanProperty clockwiseProperty() {
        return this.clockwise;
    }

    public final double getLabelLineLength() {
        return this.labelLineLength.getValue2().doubleValue();
    }

    public final void setLabelLineLength(double value) {
        this.labelLineLength.setValue((Number) Double.valueOf(value));
    }

    public final DoubleProperty labelLineLengthProperty() {
        return this.labelLineLength;
    }

    public final void setLabelsVisible(boolean value) {
        this.labelsVisible.setValue(Boolean.valueOf(value));
    }

    public final boolean getLabelsVisible() {
        return this.labelsVisible.getValue2().booleanValue();
    }

    public final BooleanProperty labelsVisibleProperty() {
        return this.labelsVisible;
    }

    public PieChart() {
        this(FXCollections.observableArrayList());
    }

    public PieChart(ObservableList<Data> data) {
        this.colorBits = new BitSet(8);
        this.begin = null;
        this.labelLinePath = new Path() { // from class: javafx.scene.chart.PieChart.1
            @Override // javafx.scene.Node
            public boolean usesMirroring() {
                return false;
            }
        };
        this.legend = new Legend();
        this.dataItemBeingRemoved = null;
        this.dataRemoveTimeline = null;
        this.dataChangeListener = c2 -> {
            while (c2.next()) {
                if (c2.wasPermutated()) {
                    Data ptr = this.begin;
                    for (int i2 = 0; i2 < getData().size(); i2++) {
                        Data item = getData().get(i2);
                        updateDataItemStyleClass(item, i2);
                        if (i2 == 0) {
                            this.begin = item;
                            ptr = this.begin;
                            this.begin.next = null;
                        } else {
                            ptr.next = item;
                            item.next = null;
                            ptr = item;
                        }
                    }
                    if (isLegendVisible()) {
                        updateLegend();
                    }
                    requestChartLayout();
                    return;
                }
                for (int i3 = c2.getFrom(); i3 < c2.getTo(); i3++) {
                    Data item2 = getData().get(i3);
                    item2.setChart(this);
                    if (this.begin == null) {
                        this.begin = item2;
                        this.begin.next = null;
                    } else if (i3 != 0) {
                        Data ptr2 = this.begin;
                        for (int j2 = 0; j2 < i3 - 1; j2++) {
                            ptr2 = ptr2.next;
                        }
                        item2.next = ptr2.next;
                        ptr2.next = item2;
                    } else {
                        item2.next = this.begin;
                        this.begin = item2;
                    }
                }
                Iterator it = c2.getRemoved().iterator();
                while (it.hasNext()) {
                    dataItemRemoved((Data) it.next());
                }
                for (int i4 = c2.getFrom(); i4 < c2.getTo(); i4++) {
                    Data item3 = getData().get(i4);
                    item3.defaultColorIndex = this.colorBits.nextClearBit(0);
                    this.colorBits.set(item3.defaultColorIndex);
                    dataItemAdded(item3, i4);
                }
                if (c2.wasRemoved() || c2.wasAdded()) {
                    for (int i5 = 0; i5 < getData().size(); i5++) {
                        updateDataItemStyleClass(getData().get(i5), i5);
                    }
                    if (isLegendVisible()) {
                        updateLegend();
                    }
                }
            }
            requestChartLayout();
        };
        this.data = new ObjectPropertyBase<ObservableList<Data>>() { // from class: javafx.scene.chart.PieChart.2
            private ObservableList<Data> old;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ObservableList<Data> current = getValue2();
                if (this.old != null) {
                    this.old.removeListener(PieChart.this.dataChangeListener);
                }
                if (current != null) {
                    current.addListener(PieChart.this.dataChangeListener);
                }
                if (this.old != null || current != null) {
                    final List<Data> removed = this.old != null ? this.old : Collections.emptyList();
                    int toIndex = current != null ? current.size() : 0;
                    if (toIndex > 0 || !removed.isEmpty()) {
                        PieChart.this.dataChangeListener.onChanged(new NonIterableChange<Data>(0, toIndex, current) { // from class: javafx.scene.chart.PieChart.2.1
                            @Override // javafx.collections.ListChangeListener.Change
                            public List<Data> getRemoved() {
                                return removed;
                            }

                            @Override // javafx.collections.ListChangeListener.Change
                            public boolean wasPermutated() {
                                return false;
                            }

                            @Override // com.sun.javafx.collections.NonIterableChange, javafx.collections.ListChangeListener.Change
                            protected int[] getPermutation() {
                                return new int[0];
                            }
                        });
                    }
                } else if (this.old != null && this.old.size() > 0) {
                    PieChart.this.dataChangeListener.onChanged(new NonIterableChange<Data>(0, 0, current) { // from class: javafx.scene.chart.PieChart.2.2
                        @Override // javafx.collections.ListChangeListener.Change
                        public List<Data> getRemoved() {
                            return AnonymousClass2.this.old;
                        }

                        @Override // javafx.collections.ListChangeListener.Change
                        public boolean wasPermutated() {
                            return false;
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
                return PieChart.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "data";
            }
        };
        this.startAngle = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.chart.PieChart.3
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                get();
                PieChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return PieChart.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "startAngle";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.START_ANGLE;
            }
        };
        this.clockwise = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.PieChart.4
            @Override // javafx.beans.property.BooleanPropertyBase
            public void invalidated() {
                get();
                PieChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return PieChart.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "clockwise";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.CLOCKWISE;
            }
        };
        this.labelLineLength = new StyleableDoubleProperty(20.0d) { // from class: javafx.scene.chart.PieChart.5
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                get();
                PieChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return PieChart.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "labelLineLength";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.LABEL_LINE_LENGTH;
            }
        };
        this.labelsVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.PieChart.6
            @Override // javafx.beans.property.BooleanPropertyBase
            public void invalidated() {
                get();
                PieChart.this.requestChartLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return PieChart.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "labelsVisible";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.LABELS_VISIBLE;
            }
        };
        getChartChildren().add(this.labelLinePath);
        this.labelLinePath.getStyleClass().add("chart-pie-label-line");
        setLegend(this.legend);
        setData(data);
        this.useChartContentMirroring = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dataNameChanged(Data item) {
        item.textNode.setText(item.getName());
        requestChartLayout();
        updateLegend();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dataPieValueChanged(Data item) {
        if (shouldAnimate()) {
            animate(new KeyFrame(Duration.ZERO, new KeyValue(item.currentPieValueProperty(), Double.valueOf(item.getCurrentPieValue()))), new KeyFrame(Duration.millis(500.0d), new KeyValue(item.currentPieValueProperty(), Double.valueOf(item.getPieValue()), Interpolator.EASE_BOTH)));
        } else {
            item.setCurrentPieValue(item.getPieValue());
            requestChartLayout();
        }
    }

    private Node createArcRegion(Data item) {
        Node arcRegion = item.getNode();
        if (arcRegion == null) {
            arcRegion = new Region();
            arcRegion.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            arcRegion.setPickOnBounds(false);
            item.setNode(arcRegion);
        }
        return arcRegion;
    }

    private Text createPieLabel(Data item) {
        Text text = item.textNode;
        text.setText(item.getName());
        return text;
    }

    private void updateDataItemStyleClass(Data item, int index) {
        Node node = item.getNode();
        if (node != null) {
            node.getStyleClass().setAll("chart-pie", "data" + index, "default-color" + (item.defaultColorIndex % 8));
            if (item.getPieValue() < 0.0d) {
                node.getStyleClass().add("negative");
            }
        }
    }

    private void dataItemAdded(Data item, int index) {
        Node shape = createArcRegion(item);
        Text text = createPieLabel(item);
        item.getChart().getChartChildren().add(shape);
        if (shouldAnimate()) {
            if (this.dataRemoveTimeline != null && this.dataRemoveTimeline.getStatus().equals(Animation.Status.RUNNING) && this.dataItemBeingRemoved == item) {
                this.dataRemoveTimeline.stop();
                this.dataRemoveTimeline = null;
                getChartChildren().remove(item.textNode);
                getChartChildren().remove(shape);
                removeDataItemRef(item);
            }
            animate(new KeyFrame(Duration.ZERO, new KeyValue(item.currentPieValueProperty(), Double.valueOf(item.getCurrentPieValue())), new KeyValue(item.radiusMultiplierProperty(), Double.valueOf(item.getRadiusMultiplier()))), new KeyFrame(Duration.millis(500.0d), (EventHandler<ActionEvent>) actionEvent -> {
                text.setOpacity(0.0d);
                if (item.getChart() == null) {
                    item.setChart(this);
                }
                item.getChart().getChartChildren().add(text);
                FadeTransition ft = new FadeTransition(Duration.millis(150.0d), text);
                ft.setToValue(1.0d);
                ft.play();
            }, new KeyValue(item.currentPieValueProperty(), Double.valueOf(item.getPieValue()), Interpolator.EASE_BOTH), new KeyValue(item.radiusMultiplierProperty(), 1, Interpolator.EASE_BOTH)));
        } else {
            getChartChildren().add(text);
            item.setRadiusMultiplier(1.0d);
            item.setCurrentPieValue(item.getPieValue());
        }
        for (int i2 = 0; i2 < getChartChildren().size(); i2++) {
            Node n2 = getChartChildren().get(i2);
            if (n2 instanceof Text) {
                n2.toFront();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDataItemRef(Data item) {
        Data ptr;
        if (this.begin == item) {
            this.begin = item.next;
            return;
        }
        Data data = this.begin;
        while (true) {
            ptr = data;
            if (ptr == null || ptr.next == item) {
                break;
            } else {
                data = ptr.next;
            }
        }
        if (ptr == null) {
            return;
        }
        ptr.next = item.next;
    }

    private Timeline createDataRemoveTimeline(Data item) {
        Node shape = item.getNode();
        Timeline t2 = new Timeline();
        t2.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(item.currentPieValueProperty(), Double.valueOf(item.getCurrentPieValue())), new KeyValue(item.radiusMultiplierProperty(), Double.valueOf(item.getRadiusMultiplier()))), new KeyFrame(Duration.millis(500.0d), (EventHandler<ActionEvent>) actionEvent -> {
            this.colorBits.clear(item.defaultColorIndex);
            getChartChildren().remove(shape);
            FadeTransition ft = new FadeTransition(Duration.millis(150.0d), item.textNode);
            ft.setFromValue(1.0d);
            ft.setToValue(0.0d);
            ft.setOnFinished(new EventHandler<ActionEvent>() { // from class: javafx.scene.chart.PieChart.7
                @Override // javafx.event.EventHandler
                public void handle(ActionEvent actionEvent) {
                    PieChart.this.getChartChildren().remove(item.textNode);
                    item.setChart(null);
                    PieChart.this.removeDataItemRef(item);
                    item.textNode.setOpacity(1.0d);
                }
            });
            ft.play();
        }, new KeyValue(item.currentPieValueProperty(), 0, Interpolator.EASE_BOTH), new KeyValue(item.radiusMultiplierProperty(), 0)));
        return t2;
    }

    private void dataItemRemoved(Data item) {
        Node shape = item.getNode();
        if (shouldAnimate()) {
            this.dataRemoveTimeline = createDataRemoveTimeline(item);
            this.dataItemBeingRemoved = item;
            animate(this.dataRemoveTimeline);
        } else {
            this.colorBits.clear(item.defaultColorIndex);
            getChartChildren().remove(item.textNode);
            getChartChildren().remove(shape);
            item.setChart(null);
            removeDataItemRef(item);
        }
    }

    @Override // javafx.scene.chart.Chart
    protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
        double d2;
        double dAbs;
        this.centerX = (contentWidth / 2.0d) + left;
        this.centerY = (contentHeight / 2.0d) + top;
        double total = 0.0d;
        Data data = this.begin;
        while (true) {
            Data item = data;
            if (item == null) {
                break;
            }
            total += Math.abs(item.getCurrentPieValue());
            data = item.next;
        }
        double scale = total != 0.0d ? 360.0d / total : 0.0d;
        this.labelLinePath.getElements().clear();
        double[] labelsX = null;
        double[] labelsY = null;
        double[] labelAngles = null;
        double labelScale = 1.0d;
        ArrayList<LabelLayoutInfo> fullPie = null;
        boolean shouldShowLabels = getLabelsVisible();
        if (getLabelsVisible()) {
            double xPad = 0.0d;
            double yPad = 0.0d;
            labelsX = new double[getDataSize()];
            labelsY = new double[getDataSize()];
            labelAngles = new double[getDataSize()];
            fullPie = new ArrayList<>();
            int index = 0;
            double start = getStartAngle();
            Data data2 = this.begin;
            while (true) {
                Data item2 = data2;
                if (item2 == null) {
                    break;
                }
                item2.textNode.getTransforms().clear();
                double size = isClockwise() ? (-scale) * Math.abs(item2.getCurrentPieValue()) : scale * Math.abs(item2.getCurrentPieValue());
                labelAngles[index] = normalizeAngle(start + (size / 2.0d));
                double sproutX = calcX(labelAngles[index], getLabelLineLength(), 0.0d);
                double sproutY = calcY(labelAngles[index], getLabelLineLength(), 0.0d);
                labelsX[index] = sproutX;
                labelsY[index] = sproutY;
                xPad = Math.max(xPad, 2.0d * (item2.textNode.getLayoutBounds().getWidth() + LABEL_TICK_GAP + Math.abs(sproutX)));
                yPad = sproutY > 0.0d ? Math.max(yPad, 2.0d * Math.abs(sproutY + item2.textNode.getLayoutBounds().getMaxY())) : Math.max(yPad, 2.0d * Math.abs(sproutY + item2.textNode.getLayoutBounds().getMinY()));
                start += size;
                index++;
                data2 = item2.next;
            }
            this.pieRadius = Math.min(contentWidth - xPad, contentHeight - yPad) / 2.0d;
            if (this.pieRadius < 25.0d) {
                double roomX = (contentWidth - 25.0d) - 25.0d;
                double roomY = (contentHeight - 25.0d) - 25.0d;
                labelScale = Math.min(roomX / xPad, roomY / yPad);
                if ((this.begin == null && labelScale < 0.7d) || this.begin.textNode.getFont().getSize() * labelScale < 9.0d) {
                    shouldShowLabels = false;
                    labelScale = 1.0d;
                } else {
                    this.pieRadius = 25.0d;
                    for (int i2 = 0; i2 < labelsX.length; i2++) {
                        labelsX[i2] = labelsX[i2] * labelScale;
                        labelsY[i2] = labelsY[i2] * labelScale;
                    }
                }
            }
        }
        if (!shouldShowLabels) {
            this.pieRadius = Math.min(contentWidth, contentHeight) / 2.0d;
        }
        if (getChartChildren().size() > 0) {
            int index2 = 0;
            Data data3 = this.begin;
            while (true) {
                Data item3 = data3;
                if (item3 == null) {
                    break;
                }
                item3.textNode.setVisible(shouldShowLabels);
                if (shouldShowLabels) {
                    double size2 = isClockwise() ? (-scale) * Math.abs(item3.getCurrentPieValue()) : scale * Math.abs(item3.getCurrentPieValue());
                    boolean isLeftSide = labelAngles[index2] <= -90.0d || labelAngles[index2] >= 90.0d;
                    double sliceCenterEdgeX = calcX(labelAngles[index2], this.pieRadius, this.centerX);
                    double sliceCenterEdgeY = calcY(labelAngles[index2], this.pieRadius, this.centerY);
                    double xval = isLeftSide ? ((labelsX[index2] + sliceCenterEdgeX) - item3.textNode.getLayoutBounds().getMaxX()) - LABEL_TICK_GAP : ((labelsX[index2] + sliceCenterEdgeX) - item3.textNode.getLayoutBounds().getMinX()) + LABEL_TICK_GAP;
                    double yval = ((labelsY[index2] + sliceCenterEdgeY) - (item3.textNode.getLayoutBounds().getMinY() / 2.0d)) - 2.0d;
                    double lineEndX = sliceCenterEdgeX + labelsX[index2];
                    double lineEndY = sliceCenterEdgeY + labelsY[index2];
                    fullPie.add(new LabelLayoutInfo(sliceCenterEdgeX, sliceCenterEdgeY, lineEndX, lineEndY, xval, yval, item3.textNode, Math.abs(size2)));
                    if (labelScale < 1.0d) {
                        item3.textNode.getTransforms().add(new Scale(labelScale, labelScale, isLeftSide ? item3.textNode.getLayoutBounds().getWidth() : 0.0d, 0.0d));
                    }
                }
                index2++;
                data3 = item3.next;
            }
            resolveCollision(fullPie);
            double sAngle = getStartAngle();
            Data data4 = this.begin;
            while (true) {
                Data item4 = data4;
                if (item4 == null) {
                    break;
                }
                Node node = item4.getNode();
                Arc arc = null;
                if (node != null && (node instanceof Region)) {
                    Region arcRegion = (Region) node;
                    if (arcRegion.getShape() == null) {
                        arc = new Arc();
                        arcRegion.setShape(arc);
                    } else {
                        arc = (Arc) arcRegion.getShape();
                    }
                    arcRegion.setShape(null);
                    arcRegion.setShape(arc);
                    arcRegion.setScaleShape(false);
                    arcRegion.setCenterShape(false);
                    arcRegion.setCacheShape(false);
                }
                if (isClockwise()) {
                    d2 = -scale;
                    dAbs = Math.abs(item4.getCurrentPieValue());
                } else {
                    d2 = scale;
                    dAbs = Math.abs(item4.getCurrentPieValue());
                }
                double size3 = d2 * dAbs;
                arc.setStartAngle(sAngle);
                arc.setLength(size3);
                arc.setType(ArcType.ROUND);
                arc.setRadiusX(this.pieRadius * item4.getRadiusMultiplier());
                arc.setRadiusY(this.pieRadius * item4.getRadiusMultiplier());
                node.setLayoutX(this.centerX);
                node.setLayoutY(this.centerY);
                sAngle += size3;
                data4 = item4.next;
            }
            if (fullPie != null) {
                Iterator<LabelLayoutInfo> it = fullPie.iterator();
                while (it.hasNext()) {
                    LabelLayoutInfo info = it.next();
                    if (info.text.isVisible()) {
                        drawLabelLinePath(info);
                    }
                }
            }
        }
    }

    private void resolveCollision(ArrayList<LabelLayoutInfo> list) {
        int boxH = this.begin != null ? (int) this.begin.textNode.getLayoutBounds().getHeight() : 0;
        int i2 = 0;
        for (int j2 = 1; list != null && j2 < list.size(); j2++) {
            LabelLayoutInfo box1 = list.get(i2);
            LabelLayoutInfo box2 = list.get(j2);
            if (box1.text.isVisible() && box2.text.isVisible() && (!fuzzyGT(box2.textY, box1.textY) ? fuzzyLT((box1.textY - boxH) - box2.textY, 2.0d) : fuzzyLT((box2.textY - boxH) - box1.textY, 2.0d)) && (!fuzzyGT(box1.textX, box2.textX) ? fuzzyLT(box2.textX - box1.textX, box1.text.prefWidth(-1.0d)) : fuzzyLT(box1.textX - box2.textX, box2.text.prefWidth(-1.0d)))) {
                if (fuzzyLT(box1.size, box2.size)) {
                    box1.text.setVisible(false);
                    i2 = j2;
                } else {
                    box2.text.setVisible(false);
                }
            } else {
                i2 = j2;
            }
        }
    }

    private int fuzzyCompare(double o1, double o2) {
        if (Math.abs(o1 - o2) < 1.0E-5d) {
            return 0;
        }
        return o1 < o2 ? -1 : 1;
    }

    private boolean fuzzyGT(double o1, double o2) {
        return fuzzyCompare(o1, o2) == 1;
    }

    private boolean fuzzyLT(double o1, double o2) {
        return fuzzyCompare(o1, o2) == -1;
    }

    private void drawLabelLinePath(LabelLayoutInfo info) {
        info.text.setLayoutX(info.textX);
        info.text.setLayoutY(info.textY);
        this.labelLinePath.getElements().add(new MoveTo(info.startX, info.startY));
        this.labelLinePath.getElements().add(new LineTo(info.endX, info.endY));
        this.labelLinePath.getElements().add(new MoveTo(info.endX - 2.0d, info.endY));
        this.labelLinePath.getElements().add(new ArcTo(2.0d, 2.0d, 90.0d, info.endX, info.endY - 2.0d, false, true));
        this.labelLinePath.getElements().add(new ArcTo(2.0d, 2.0d, 90.0d, info.endX + 2.0d, info.endY, false, true));
        this.labelLinePath.getElements().add(new ArcTo(2.0d, 2.0d, 90.0d, info.endX, info.endY + 2.0d, false, true));
        this.labelLinePath.getElements().add(new ArcTo(2.0d, 2.0d, 90.0d, info.endX - 2.0d, info.endY, false, true));
        this.labelLinePath.getElements().add(new ClosePath());
    }

    private void updateLegend() {
        Node legendNode = getLegend();
        if (legendNode == null || legendNode == this.legend) {
            this.legend.setVertical(getLegendSide().equals(Side.LEFT) || getLegendSide().equals(Side.RIGHT));
            this.legend.getItems().clear();
            if (getData() != null) {
                for (Data item : getData()) {
                    Legend.LegendItem legenditem = new Legend.LegendItem(item.getName());
                    legenditem.getSymbol().getStyleClass().addAll(item.getNode().getStyleClass());
                    legenditem.getSymbol().getStyleClass().add("pie-legend-symbol");
                    this.legend.getItems().add(legenditem);
                }
            }
            if (this.legend.getItems().size() > 0) {
                if (legendNode == null) {
                    setLegend(this.legend);
                    return;
                }
                return;
            }
            setLegend(null);
        }
    }

    private int getDataSize() {
        int count = 0;
        Data data = this.begin;
        while (true) {
            Data d2 = data;
            if (d2 != null) {
                count++;
                data = d2.next;
            } else {
                return count;
            }
        }
    }

    private static double calcX(double angle, double radius, double centerX) {
        return centerX + (radius * Math.cos(Math.toRadians(-angle)));
    }

    private static double calcY(double angle, double radius, double centerY) {
        return centerY + (radius * Math.sin(Math.toRadians(-angle)));
    }

    private static double normalizeAngle(double angle) {
        double a2 = angle % 360.0d;
        if (a2 <= -180.0d) {
            a2 += 360.0d;
        }
        if (a2 > 180.0d) {
            a2 -= 360.0d;
        }
        return a2;
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/PieChart$LabelLayoutInfo.class */
    static final class LabelLayoutInfo {
        double startX;
        double startY;
        double endX;
        double endY;
        double textX;
        double textY;
        Text text;
        double size;

        public LabelLayoutInfo(double startX, double startY, double endX, double endY, double textX, double textY, Text text, double size) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.textX = textX;
            this.textY = textY;
            this.text = text;
            this.size = size;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/PieChart$Data.class */
    public static final class Data {
        private int defaultColorIndex;
        private Text textNode = new Text();
        private Data next = null;
        private ReadOnlyObjectWrapper<PieChart> chart = new ReadOnlyObjectWrapper<>(this, "chart");
        private StringProperty name = new StringPropertyBase() { // from class: javafx.scene.chart.PieChart.Data.1
            @Override // javafx.beans.property.StringPropertyBase
            protected void invalidated() {
                if (Data.this.getChart() != null) {
                    Data.this.getChart().dataNameChanged(Data.this);
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Data.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "name";
            }
        };
        private DoubleProperty pieValue = new DoublePropertyBase() { // from class: javafx.scene.chart.PieChart.Data.2
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (Data.this.getChart() != null) {
                    Data.this.getChart().dataPieValueChanged(Data.this);
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Data.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "pieValue";
            }
        };
        private DoubleProperty currentPieValue = new SimpleDoubleProperty(this, "currentPieValue");
        private DoubleProperty radiusMultiplier = new SimpleDoubleProperty(this, "radiusMultiplier");
        private ReadOnlyObjectWrapper<Node> node = new ReadOnlyObjectWrapper<>(this, "node");

        public final PieChart getChart() {
            return this.chart.getValue2();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setChart(PieChart value) {
            this.chart.setValue(value);
        }

        public final ReadOnlyObjectProperty<PieChart> chartProperty() {
            return this.chart.getReadOnlyProperty();
        }

        public final void setName(String value) {
            this.name.setValue(value);
        }

        public final String getName() {
            return this.name.getValue2();
        }

        public final StringProperty nameProperty() {
            return this.name;
        }

        public final double getPieValue() {
            return this.pieValue.getValue2().doubleValue();
        }

        public final void setPieValue(double value) {
            this.pieValue.setValue((Number) Double.valueOf(value));
        }

        public final DoubleProperty pieValueProperty() {
            return this.pieValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double getCurrentPieValue() {
            return this.currentPieValue.getValue2().doubleValue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCurrentPieValue(double value) {
            this.currentPieValue.setValue((Number) Double.valueOf(value));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public DoubleProperty currentPieValueProperty() {
            return this.currentPieValue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double getRadiusMultiplier() {
            return this.radiusMultiplier.getValue2().doubleValue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRadiusMultiplier(double value) {
            this.radiusMultiplier.setValue((Number) Double.valueOf(value));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public DoubleProperty radiusMultiplierProperty() {
            return this.radiusMultiplier;
        }

        public Node getNode() {
            return this.node.getValue2();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setNode(Node value) {
            this.node.setValue(value);
        }

        public ReadOnlyObjectProperty<Node> nodeProperty() {
            return this.node.getReadOnlyProperty();
        }

        public Data(String name, double value) {
            setName(name);
            setPieValue(value);
            this.textNode.getStyleClass().addAll("text", "chart-pie-label");
            this.textNode.setAccessibleRole(AccessibleRole.TEXT);
            this.textNode.setAccessibleRoleDescription("slice");
            this.textNode.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
            this.textNode.accessibleTextProperty().bind(new StringBinding() { // from class: javafx.scene.chart.PieChart.Data.3
                {
                    bind(Data.this.nameProperty(), Data.this.currentPieValueProperty());
                }

                @Override // javafx.beans.binding.StringBinding
                protected String computeValue() {
                    return Data.this.getName() + " represents " + Data.this.getCurrentPieValue() + " percent";
                }
            });
        }

        public String toString() {
            return "Data[" + getName() + "," + getPieValue() + "]";
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/PieChart$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<PieChart, Boolean> CLOCKWISE = new CssMetaData<PieChart, Boolean>("-fx-clockwise", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.PieChart.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(PieChart node) {
                return node.clockwise == null || !node.clockwise.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(PieChart node) {
                return (StyleableProperty) node.clockwiseProperty();
            }
        };
        private static final CssMetaData<PieChart, Boolean> LABELS_VISIBLE = new CssMetaData<PieChart, Boolean>("-fx-pie-label-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.PieChart.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(PieChart node) {
                return node.labelsVisible == null || !node.labelsVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(PieChart node) {
                return (StyleableProperty) node.labelsVisibleProperty();
            }
        };
        private static final CssMetaData<PieChart, Number> LABEL_LINE_LENGTH = new CssMetaData<PieChart, Number>("-fx-label-line-length", SizeConverter.getInstance(), Double.valueOf(20.0d)) { // from class: javafx.scene.chart.PieChart.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(PieChart node) {
                return node.labelLineLength == null || !node.labelLineLength.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(PieChart node) {
                return (StyleableProperty) node.labelLineLengthProperty();
            }
        };
        private static final CssMetaData<PieChart, Number> START_ANGLE = new CssMetaData<PieChart, Number>("-fx-start-angle", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.chart.PieChart.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(PieChart node) {
                return node.startAngle == null || !node.startAngle.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(PieChart node) {
                return (StyleableProperty) node.startAngleProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Chart.getClassCssMetaData());
            styleables.add(CLOCKWISE);
            styleables.add(LABELS_VISIBLE);
            styleables.add(LABEL_LINE_LENGTH);
            styleables.add(START_ANGLE);
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
}
