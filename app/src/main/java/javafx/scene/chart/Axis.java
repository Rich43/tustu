package javafx.scene.chart;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.font.LogicalFont;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import javax.swing.JSplitPane;

/* loaded from: jfxrt.jar:javafx/scene/chart/Axis.class */
public abstract class Axis<T> extends Region {
    private Orientation effectiveOrientation;
    private static final PseudoClass TOP_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.TOP);
    private static final PseudoClass BOTTOM_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.BOTTOM);
    private static final PseudoClass LEFT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.LEFT);
    private static final PseudoClass RIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.RIGHT);
    Text measure = new Text();
    private double effectiveTickLabelRotation = Double.NaN;
    private Label axisLabel = new Label();
    private final Path tickMarkPath = new Path();
    private double oldLength = 0.0d;
    boolean rangeValid = false;
    boolean measureInvalid = false;
    boolean tickLabelsVisibleInvalid = false;
    private BitSet labelsToSkip = new BitSet();
    private final ObservableList<TickMark<T>> tickMarks = FXCollections.observableArrayList();
    private final ObservableList<TickMark<T>> unmodifiableTickMarks = FXCollections.unmodifiableObservableList(this.tickMarks);
    private ObjectProperty<Side> side = new StyleableObjectProperty<Side>() { // from class: javafx.scene.chart.Axis.1
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Side edge = get();
            Axis.this.pseudoClassStateChanged(Axis.TOP_PSEUDOCLASS_STATE, edge == Side.TOP);
            Axis.this.pseudoClassStateChanged(Axis.RIGHT_PSEUDOCLASS_STATE, edge == Side.RIGHT);
            Axis.this.pseudoClassStateChanged(Axis.BOTTOM_PSEUDOCLASS_STATE, edge == Side.BOTTOM);
            Axis.this.pseudoClassStateChanged(Axis.LEFT_PSEUDOCLASS_STATE, edge == Side.LEFT);
            Axis.this.requestAxisLayout();
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<Axis<?>, Side> getCssMetaData() {
            return StyleableProperties.SIDE;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "side";
        }
    };
    private ObjectProperty<String> label = new ObjectPropertyBase<String>() { // from class: javafx.scene.chart.Axis.2
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Axis.this.axisLabel.setText(get());
            Axis.this.requestAxisLayout();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "label";
        }
    };
    private BooleanProperty tickMarkVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.Axis.3
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            Axis.this.tickMarkPath.setVisible(get());
            Axis.this.requestAxisLayout();
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
            return StyleableProperties.TICK_MARK_VISIBLE;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "tickMarkVisible";
        }
    };
    private BooleanProperty tickLabelsVisible = new StyleableBooleanProperty(true) { // from class: javafx.scene.chart.Axis.4
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            for (TickMark<T> tick : Axis.this.tickMarks) {
                tick.setTextVisible(get());
            }
            Axis.this.tickLabelsVisibleInvalid = true;
            Axis.this.requestAxisLayout();
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
            return StyleableProperties.TICK_LABELS_VISIBLE;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "tickLabelsVisible";
        }
    };
    private DoubleProperty tickLength = new StyleableDoubleProperty(8.0d) { // from class: javafx.scene.chart.Axis.5
        @Override // javafx.beans.property.DoublePropertyBase
        protected void invalidated() {
            if (Axis.this.tickLength.get() < 0.0d && !Axis.this.tickLength.isBound()) {
                Axis.this.tickLength.set(0.0d);
            }
            Axis.this.requestAxisLayout();
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Number> getCssMetaData() {
            return StyleableProperties.TICK_LENGTH;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "tickLength";
        }
    };
    private BooleanProperty autoRanging = new BooleanPropertyBase(true) { // from class: javafx.scene.chart.Axis.6
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            if (get()) {
                Axis.this.requestAxisLayout();
            }
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "autoRanging";
        }
    };
    private ObjectProperty<Font> tickLabelFont = new StyleableObjectProperty<Font>(Font.font(LogicalFont.SYSTEM, 8.0d)) { // from class: javafx.scene.chart.Axis.7
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Font f2 = get();
            Axis.this.measure.setFont(f2);
            for (TickMark<T> tm : Axis.this.getTickMarks()) {
                tm.textNode.setFont(f2);
            }
            Axis.this.measureInvalid = true;
            Axis.this.requestAxisLayout();
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<Axis<?>, Font> getCssMetaData() {
            return StyleableProperties.TICK_LABEL_FONT;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "tickLabelFont";
        }
    };
    private ObjectProperty<Paint> tickLabelFill = new StyleableObjectProperty<Paint>(Color.BLACK) { // from class: javafx.scene.chart.Axis.8
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            for (TickMark<T> tick : Axis.this.tickMarks) {
                tick.textNode.setFill(Axis.this.getTickLabelFill());
            }
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<Axis<?>, Paint> getCssMetaData() {
            return StyleableProperties.TICK_LABEL_FILL;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "tickLabelFill";
        }
    };
    private DoubleProperty tickLabelGap = new StyleableDoubleProperty(3.0d) { // from class: javafx.scene.chart.Axis.9
        @Override // javafx.beans.property.DoublePropertyBase
        protected void invalidated() {
            Axis.this.requestAxisLayout();
        }

        @Override // javafx.css.StyleableProperty
        public CssMetaData<? extends Styleable, Number> getCssMetaData() {
            return StyleableProperties.TICK_LABEL_TICK_GAP;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "tickLabelGap";
        }
    };
    private BooleanProperty animated = new SimpleBooleanProperty(this, "animated", true);
    private DoubleProperty tickLabelRotation = new DoublePropertyBase(0.0d) { // from class: javafx.scene.chart.Axis.10
        @Override // javafx.beans.property.DoublePropertyBase
        protected void invalidated() {
            if (Axis.this.isAutoRanging()) {
                Axis.this.invalidateRange();
            }
            Axis.this.requestAxisLayout();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Axis.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "tickLabelRotation";
        }
    };

    protected abstract Object autoRange(double d2);

    protected abstract void setRange(Object obj, boolean z2);

    protected abstract Object getRange();

    public abstract double getZeroPosition();

    public abstract double getDisplayPosition(T t2);

    public abstract T getValueForDisplay(double d2);

    public abstract boolean isValueOnAxis(T t2);

    public abstract double toNumericValue(T t2);

    public abstract T toRealValue(double d2);

    protected abstract List<T> calculateTickValues(double d2, Object obj);

    protected abstract String getTickMarkLabel(T t2);

    public ObservableList<TickMark<T>> getTickMarks() {
        return this.unmodifiableTickMarks;
    }

    public final Side getSide() {
        return this.side.get();
    }

    public final void setSide(Side value) {
        this.side.set(value);
    }

    public final ObjectProperty<Side> sideProperty() {
        return this.side;
    }

    final void setEffectiveOrientation(Orientation orientation) {
        this.effectiveOrientation = orientation;
    }

    final Side getEffectiveSide() {
        Side side = getSide();
        if (side == null || ((side.isVertical() && this.effectiveOrientation == Orientation.HORIZONTAL) || (side.isHorizontal() && this.effectiveOrientation == Orientation.VERTICAL))) {
            return this.effectiveOrientation == Orientation.VERTICAL ? Side.LEFT : Side.BOTTOM;
        }
        return side;
    }

    public final String getLabel() {
        return this.label.get();
    }

    public final void setLabel(String value) {
        this.label.set(value);
    }

    public final ObjectProperty<String> labelProperty() {
        return this.label;
    }

    public final boolean isTickMarkVisible() {
        return this.tickMarkVisible.get();
    }

    public final void setTickMarkVisible(boolean value) {
        this.tickMarkVisible.set(value);
    }

    public final BooleanProperty tickMarkVisibleProperty() {
        return this.tickMarkVisible;
    }

    public final boolean isTickLabelsVisible() {
        return this.tickLabelsVisible.get();
    }

    public final void setTickLabelsVisible(boolean value) {
        this.tickLabelsVisible.set(value);
    }

    public final BooleanProperty tickLabelsVisibleProperty() {
        return this.tickLabelsVisible;
    }

    public final double getTickLength() {
        return this.tickLength.get();
    }

    public final void setTickLength(double value) {
        this.tickLength.set(value);
    }

    public final DoubleProperty tickLengthProperty() {
        return this.tickLength;
    }

    public final boolean isAutoRanging() {
        return this.autoRanging.get();
    }

    public final void setAutoRanging(boolean value) {
        this.autoRanging.set(value);
    }

    public final BooleanProperty autoRangingProperty() {
        return this.autoRanging;
    }

    public final Font getTickLabelFont() {
        return this.tickLabelFont.get();
    }

    public final void setTickLabelFont(Font value) {
        this.tickLabelFont.set(value);
    }

    public final ObjectProperty<Font> tickLabelFontProperty() {
        return this.tickLabelFont;
    }

    public final Paint getTickLabelFill() {
        return this.tickLabelFill.get();
    }

    public final void setTickLabelFill(Paint value) {
        this.tickLabelFill.set(value);
    }

    public final ObjectProperty<Paint> tickLabelFillProperty() {
        return this.tickLabelFill;
    }

    public final double getTickLabelGap() {
        return this.tickLabelGap.get();
    }

    public final void setTickLabelGap(double value) {
        this.tickLabelGap.set(value);
    }

    public final DoubleProperty tickLabelGapProperty() {
        return this.tickLabelGap;
    }

    public final boolean getAnimated() {
        return this.animated.get();
    }

    public final void setAnimated(boolean value) {
        this.animated.set(value);
    }

    public final BooleanProperty animatedProperty() {
        return this.animated;
    }

    public final double getTickLabelRotation() {
        return this.tickLabelRotation.getValue2().doubleValue();
    }

    public final void setTickLabelRotation(double value) {
        this.tickLabelRotation.setValue((Number) Double.valueOf(value));
    }

    public final DoubleProperty tickLabelRotationProperty() {
        return this.tickLabelRotation;
    }

    public Axis() {
        getStyleClass().setAll("axis");
        this.axisLabel.getStyleClass().add("axis-label");
        this.axisLabel.setAlignment(Pos.CENTER);
        this.tickMarkPath.getStyleClass().add("axis-tick-mark");
        getChildren().addAll(this.axisLabel, this.tickMarkPath);
    }

    protected final boolean isRangeValid() {
        return this.rangeValid;
    }

    protected final void invalidateRange() {
        this.rangeValid = false;
    }

    protected final boolean shouldAnimate() {
        return getAnimated() && impl_isTreeVisible() && getScene() != null;
    }

    @Override // javafx.scene.Parent
    public void requestLayout() {
    }

    public void requestAxisLayout() {
        super.requestLayout();
    }

    public void invalidateRange(List<T> data) {
        invalidateRange();
        requestAxisLayout();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        Side side = getEffectiveSide();
        if (side.isVertical()) {
            return 100.0d;
        }
        Object range = autoRange(width);
        double maxLabelHeight = 0.0d;
        if (isTickLabelsVisible()) {
            List<T> newTickValues = calculateTickValues(width, range);
            for (T value : newTickValues) {
                maxLabelHeight = Math.max(maxLabelHeight, measureTickMarkSize((Axis<T>) value, range).getHeight());
            }
        }
        double tickLength = (!isTickMarkVisible() || getTickLength() <= 0.0d) ? 0.0d : getTickLength();
        double tickMarkLength = tickLength;
        double labelHeight = (this.axisLabel.getText() == null || this.axisLabel.getText().length() == 0) ? 0.0d : this.axisLabel.prefHeight(-1.0d);
        return maxLabelHeight + getTickLabelGap() + tickMarkLength + labelHeight;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        Side side = getEffectiveSide();
        if (side.isVertical()) {
            Object range = autoRange(height);
            double maxLabelWidth = 0.0d;
            if (isTickLabelsVisible()) {
                List<T> newTickValues = calculateTickValues(height, range);
                for (T value : newTickValues) {
                    maxLabelWidth = Math.max(maxLabelWidth, measureTickMarkSize((Axis<T>) value, range).getWidth());
                }
            }
            double tickLength = (!isTickMarkVisible() || getTickLength() <= 0.0d) ? 0.0d : getTickLength();
            double tickMarkLength = tickLength;
            double labelHeight = (this.axisLabel.getText() == null || this.axisLabel.getText().length() == 0) ? 0.0d : this.axisLabel.prefHeight(-1.0d);
            return maxLabelWidth + getTickLabelGap() + tickMarkLength + labelHeight;
        }
        return 100.0d;
    }

    protected void tickMarksUpdated() {
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        Object range;
        double width = getWidth();
        double height = getHeight();
        double tickLength = (!isTickMarkVisible() || getTickLength() <= 0.0d) ? 0.0d : getTickLength();
        boolean z2 = this.oldLength == 0.0d;
        Side effectiveSide = getEffectiveSide();
        double d2 = effectiveSide.isVertical() ? height : width;
        boolean z3 = !isRangeValid();
        boolean z4 = this.oldLength != d2;
        if (z4 || z3) {
            if (isAutoRanging()) {
                range = autoRange(d2);
                setRange(range, getAnimated() && !z2 && impl_isTreeVisible() && z3);
            } else {
                range = getRange();
            }
            List<T> listCalculateTickValues = calculateTickValues(d2, range);
            Iterator<TickMark<T>> it = this.tickMarks.iterator();
            while (it.hasNext()) {
                TickMark<T> next = it.next();
                if (!shouldAnimate()) {
                    getChildren().remove(next.textNode);
                } else {
                    FadeTransition fadeTransition = new FadeTransition(Duration.millis(250.0d), next.textNode);
                    fadeTransition.setToValue(0.0d);
                    fadeTransition.setOnFinished(actionEvent -> {
                        getChildren().remove(next.textNode);
                    });
                    fadeTransition.play();
                }
                it.remove();
            }
            for (T t2 : listCalculateTickValues) {
                TickMark tickMark = new TickMark();
                tickMark.setValue(t2);
                tickMark.textNode.setText(getTickMarkLabel(t2));
                tickMark.textNode.setFont(getTickLabelFont());
                tickMark.textNode.setFill(getTickLabelFill());
                tickMark.setTextVisible(isTickLabelsVisible());
                if (shouldAnimate()) {
                    tickMark.textNode.setOpacity(0.0d);
                }
                getChildren().add(tickMark.textNode);
                this.tickMarks.add(tickMark);
                if (shouldAnimate()) {
                    FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(750.0d), tickMark.textNode);
                    fadeTransition2.setFromValue(0.0d);
                    fadeTransition2.setToValue(1.0d);
                    fadeTransition2.play();
                }
            }
            tickMarksUpdated();
            this.oldLength = d2;
            this.rangeValid = true;
        }
        if (z4 || z3 || this.measureInvalid || this.tickLabelsVisibleInvalid) {
            this.measureInvalid = false;
            this.tickLabelsVisibleInvalid = false;
            this.labelsToSkip.clear();
            int size = 0;
            double d3 = 0.0d;
            double dRound = 0.0d;
            for (TickMark<T> tickMark2 : this.tickMarks) {
                tickMark2.setPosition(getDisplayPosition(tickMark2.getValue()));
                if (tickMark2.isTextVisible()) {
                    double dMeasureTickMarkSize = measureTickMarkSize((Axis<T>) tickMark2.getValue(), effectiveSide);
                    d3 += dMeasureTickMarkSize;
                    dRound = Math.round(Math.max(dRound, dMeasureTickMarkSize));
                }
            }
            if (dRound > 0.0d && d2 < d3) {
                size = ((int) ((this.tickMarks.size() * dRound) / d2)) + 1;
            }
            if (size > 0) {
                int i2 = 0;
                for (TickMark<T> tickMark3 : this.tickMarks) {
                    if (tickMark3.isTextVisible()) {
                        int i3 = i2;
                        i2++;
                        tickMark3.setTextVisible(i3 % size == 0);
                    }
                }
            }
            if (this.tickMarks.size() > 2) {
                TickMark<T> tickMark4 = this.tickMarks.get(0);
                TickMark<T> tickMark5 = this.tickMarks.get(1);
                if (isTickLabelsOverlap(effectiveSide, tickMark4, tickMark5, getTickLabelGap())) {
                    tickMark5.setTextVisible(false);
                }
                TickMark<T> tickMark6 = this.tickMarks.get(this.tickMarks.size() - 2);
                if (isTickLabelsOverlap(effectiveSide, tickMark6, this.tickMarks.get(this.tickMarks.size() - 1), getTickLabelGap())) {
                    tickMark6.setTextVisible(false);
                }
            }
        }
        this.tickMarkPath.getElements().clear();
        double effectiveTickLabelRotation = getEffectiveTickLabelRotation();
        if (Side.LEFT.equals(effectiveSide)) {
            this.tickMarkPath.setLayoutX(-0.5d);
            this.tickMarkPath.setLayoutY(0.5d);
            if (getLabel() != null) {
                this.axisLabel.getTransforms().setAll(new Translate(0.0d, height), new Rotate(-90.0d, 0.0d, 0.0d));
                this.axisLabel.setLayoutX(0.0d);
                this.axisLabel.setLayoutY(0.0d);
                this.axisLabel.resize(height, Math.ceil(this.axisLabel.prefHeight(width)));
            }
            for (TickMark<T> tickMark7 : this.tickMarks) {
                positionTextNode(tickMark7.textNode, (width - getTickLabelGap()) - tickLength, tickMark7.getPosition(), effectiveTickLabelRotation, effectiveSide);
                updateTickMark(tickMark7, d2, width - tickLength, tickMark7.getPosition(), width, tickMark7.getPosition());
            }
            return;
        }
        if (Side.RIGHT.equals(effectiveSide)) {
            this.tickMarkPath.setLayoutX(0.5d);
            this.tickMarkPath.setLayoutY(0.5d);
            if (getLabel() != null) {
                double dCeil = Math.ceil(this.axisLabel.prefHeight(width));
                this.axisLabel.getTransforms().setAll(new Translate(0.0d, height), new Rotate(-90.0d, 0.0d, 0.0d));
                this.axisLabel.setLayoutX(width - dCeil);
                this.axisLabel.setLayoutY(0.0d);
                this.axisLabel.resize(height, dCeil);
            }
            for (TickMark<T> tickMark8 : this.tickMarks) {
                positionTextNode(tickMark8.textNode, getTickLabelGap() + tickLength, tickMark8.getPosition(), effectiveTickLabelRotation, effectiveSide);
                updateTickMark(tickMark8, d2, 0.0d, tickMark8.getPosition(), tickLength, tickMark8.getPosition());
            }
            return;
        }
        if (Side.TOP.equals(effectiveSide)) {
            this.tickMarkPath.setLayoutX(0.5d);
            this.tickMarkPath.setLayoutY(-0.5d);
            if (getLabel() != null) {
                this.axisLabel.getTransforms().clear();
                this.axisLabel.setLayoutX(0.0d);
                this.axisLabel.setLayoutY(0.0d);
                this.axisLabel.resize(width, Math.ceil(this.axisLabel.prefHeight(width)));
            }
            for (TickMark<T> tickMark9 : this.tickMarks) {
                positionTextNode(tickMark9.textNode, tickMark9.getPosition(), (height - tickLength) - getTickLabelGap(), effectiveTickLabelRotation, effectiveSide);
                updateTickMark(tickMark9, d2, tickMark9.getPosition(), height, tickMark9.getPosition(), height - tickLength);
            }
            return;
        }
        this.tickMarkPath.setLayoutX(0.5d);
        this.tickMarkPath.setLayoutY(0.5d);
        if (getLabel() != null) {
            this.axisLabel.getTransforms().clear();
            double dCeil2 = Math.ceil(this.axisLabel.prefHeight(width));
            this.axisLabel.setLayoutX(0.0d);
            this.axisLabel.setLayoutY(height - dCeil2);
            this.axisLabel.resize(width, dCeil2);
        }
        for (TickMark<T> tickMark10 : this.tickMarks) {
            positionTextNode(tickMark10.textNode, tickMark10.getPosition(), tickLength + getTickLabelGap(), effectiveTickLabelRotation, effectiveSide);
            updateTickMark(tickMark10, d2, tickMark10.getPosition(), 0.0d, tickMark10.getPosition(), tickLength);
        }
    }

    private boolean isTickLabelsOverlap(Side side, TickMark<T> m1, TickMark<T> m2, double gap) {
        if (!m1.isTextVisible() || !m2.isTextVisible()) {
            return false;
        }
        double m1Size = measureTickMarkSize((Axis<T>) m1.getValue(), side);
        double m2Size = measureTickMarkSize((Axis<T>) m2.getValue(), side);
        double m1Start = m1.getPosition() - (m1Size / 2.0d);
        double m1End = m1.getPosition() + (m1Size / 2.0d);
        double m2Start = m2.getPosition() - (m2Size / 2.0d);
        double m2End = m2.getPosition() + (m2Size / 2.0d);
        return side.isVertical() ? m1Start - m2End <= gap : m2Start - m1End <= gap;
    }

    private void positionTextNode(Text node, double posX, double posY, double angle, Side side) {
        node.setLayoutX(0.0d);
        node.setLayoutY(0.0d);
        node.setRotate(angle);
        Bounds bounds = node.getBoundsInParent();
        if (Side.LEFT.equals(side)) {
            node.setLayoutX((posX - bounds.getWidth()) - bounds.getMinX());
            node.setLayoutY((posY - (bounds.getHeight() / 2.0d)) - bounds.getMinY());
        } else if (Side.RIGHT.equals(side)) {
            node.setLayoutX(posX - bounds.getMinX());
            node.setLayoutY((posY - (bounds.getHeight() / 2.0d)) - bounds.getMinY());
        } else if (Side.TOP.equals(side)) {
            node.setLayoutX((posX - (bounds.getWidth() / 2.0d)) - bounds.getMinX());
            node.setLayoutY((posY - bounds.getHeight()) - bounds.getMinY());
        } else {
            node.setLayoutX((posX - (bounds.getWidth() / 2.0d)) - bounds.getMinX());
            node.setLayoutY(posY - bounds.getMinY());
        }
    }

    private void updateTickMark(TickMark<T> tick, double length, double startX, double startY, double endX, double endY) {
        if (tick.getPosition() >= 0.0d && tick.getPosition() <= Math.ceil(length)) {
            tick.textNode.setVisible(tick.isTextVisible());
            this.tickMarkPath.getElements().addAll(new MoveTo(startX, startY), new LineTo(endX, endY));
        } else {
            tick.textNode.setVisible(false);
        }
    }

    protected final Dimension2D measureTickMarkLabelSize(String labelText, double rotation) {
        this.measure.setRotate(rotation);
        this.measure.setText(labelText);
        Bounds bounds = this.measure.getBoundsInParent();
        return new Dimension2D(bounds.getWidth(), bounds.getHeight());
    }

    protected final Dimension2D measureTickMarkSize(T value, double rotation) {
        return measureTickMarkLabelSize(getTickMarkLabel(value), rotation);
    }

    protected Dimension2D measureTickMarkSize(T value, Object range) {
        return measureTickMarkSize((Axis<T>) value, getEffectiveTickLabelRotation());
    }

    private double measureTickMarkSize(T value, Side side) {
        Dimension2D size = measureTickMarkSize((Axis<T>) value, getEffectiveTickLabelRotation());
        return side.isVertical() ? size.getHeight() : size.getWidth();
    }

    final double getEffectiveTickLabelRotation() {
        return (!isAutoRanging() || Double.isNaN(this.effectiveTickLabelRotation)) ? getTickLabelRotation() : this.effectiveTickLabelRotation;
    }

    final void setEffectiveTickLabelRotation(double rotation) {
        this.effectiveTickLabelRotation = rotation;
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/Axis$TickMark.class */
    public static final class TickMark<T> {
        private StringProperty label = new StringPropertyBase() { // from class: javafx.scene.chart.Axis.TickMark.1
            @Override // javafx.beans.property.StringPropertyBase
            protected void invalidated() {
                TickMark.this.textNode.setText(getValue2());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TickMark.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "label";
            }
        };
        private ObjectProperty<T> value = new SimpleObjectProperty(this, "value");
        private DoubleProperty position = new SimpleDoubleProperty(this, Keywords.FUNC_POSITION_STRING);
        Text textNode = new Text();
        private BooleanProperty textVisible = new BooleanPropertyBase(true) { // from class: javafx.scene.chart.Axis.TickMark.2
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                if (!get()) {
                    TickMark.this.textNode.setVisible(false);
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TickMark.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "textVisible";
            }
        };

        public final String getLabel() {
            return this.label.get();
        }

        public final void setLabel(String value) {
            this.label.set(value);
        }

        public final StringExpression labelProperty() {
            return this.label;
        }

        public final T getValue() {
            return this.value.get();
        }

        public final void setValue(T v2) {
            this.value.set(v2);
        }

        public final ObjectExpression<T> valueProperty() {
            return this.value;
        }

        public final double getPosition() {
            return this.position.get();
        }

        public final void setPosition(double value) {
            this.position.set(value);
        }

        public final DoubleExpression positionProperty() {
            return this.position;
        }

        public final boolean isTextVisible() {
            return this.textVisible.get();
        }

        public final void setTextVisible(boolean value) {
            this.textVisible.set(value);
        }

        public String toString() {
            return this.value.get().toString();
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/chart/Axis$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Axis<?>, Side> SIDE = new CssMetaData<Axis<?>, Side>("-fx-side", new EnumConverter(Side.class)) { // from class: javafx.scene.chart.Axis.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Axis<?> n2) {
                return ((Axis) n2).side == null || !((Axis) n2).side.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Side> getStyleableProperty(Axis<?> n2) {
                return (StyleableProperty) n2.sideProperty();
            }
        };
        private static final CssMetaData<Axis<?>, Number> TICK_LENGTH = new CssMetaData<Axis<?>, Number>("-fx-tick-length", SizeConverter.getInstance(), Double.valueOf(8.0d)) { // from class: javafx.scene.chart.Axis.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Axis<?> n2) {
                return ((Axis) n2).tickLength == null || !((Axis) n2).tickLength.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Axis<?> n2) {
                return (StyleableProperty) n2.tickLengthProperty();
            }
        };
        private static final CssMetaData<Axis<?>, Font> TICK_LABEL_FONT = new FontCssMetaData<Axis<?>>("-fx-tick-label-font", Font.font("system", 8.0d)) { // from class: javafx.scene.chart.Axis.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Axis<?> n2) {
                return ((Axis) n2).tickLabelFont == null || !((Axis) n2).tickLabelFont.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Font> getStyleableProperty(Axis<?> n2) {
                return (StyleableProperty) n2.tickLabelFontProperty();
            }
        };
        private static final CssMetaData<Axis<?>, Paint> TICK_LABEL_FILL = new CssMetaData<Axis<?>, Paint>("-fx-tick-label-fill", PaintConverter.getInstance(), Color.BLACK) { // from class: javafx.scene.chart.Axis.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Axis<?> n2) {
                return (((Axis) n2).tickLabelFill == null) | (!((Axis) n2).tickLabelFill.isBound());
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Paint> getStyleableProperty(Axis<?> n2) {
                return (StyleableProperty) n2.tickLabelFillProperty();
            }
        };
        private static final CssMetaData<Axis<?>, Number> TICK_LABEL_TICK_GAP = new CssMetaData<Axis<?>, Number>("-fx-tick-label-gap", SizeConverter.getInstance(), Double.valueOf(3.0d)) { // from class: javafx.scene.chart.Axis.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Axis<?> n2) {
                return ((Axis) n2).tickLabelGap == null || !((Axis) n2).tickLabelGap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Axis<?> n2) {
                return (StyleableProperty) n2.tickLabelGapProperty();
            }
        };
        private static final CssMetaData<Axis<?>, Boolean> TICK_MARK_VISIBLE = new CssMetaData<Axis<?>, Boolean>("-fx-tick-mark-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.Axis.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Axis<?> n2) {
                return ((Axis) n2).tickMarkVisible == null || !((Axis) n2).tickMarkVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Axis<?> n2) {
                return (StyleableProperty) n2.tickMarkVisibleProperty();
            }
        };
        private static final CssMetaData<Axis<?>, Boolean> TICK_LABELS_VISIBLE = new CssMetaData<Axis<?>, Boolean>("-fx-tick-labels-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.chart.Axis.StyleableProperties.7
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Axis<?> n2) {
                return ((Axis) n2).tickLabelsVisible == null || !((Axis) n2).tickLabelsVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Axis<?> n2) {
                return (StyleableProperty) n2.tickLabelsVisibleProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(SIDE);
            styleables.add(TICK_LENGTH);
            styleables.add(TICK_LABEL_FONT);
            styleables.add(TICK_LABEL_FILL);
            styleables.add(TICK_LABEL_TICK_GAP);
            styleables.add(TICK_MARK_VISIBLE);
            styleables.add(TICK_LABELS_VISIBLE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}
