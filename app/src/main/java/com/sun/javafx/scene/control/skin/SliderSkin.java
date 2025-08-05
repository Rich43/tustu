package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SliderBehavior;
import javafx.animation.Transition;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SliderSkin.class */
public class SliderSkin extends BehaviorSkinBase<Slider, SliderBehavior> {
    private NumberAxis tickLine;
    private double trackToTickGap;
    private boolean showTickMarks;
    private double thumbWidth;
    private double thumbHeight;
    private double trackStart;
    private double trackLength;
    private double thumbTop;
    private double thumbLeft;
    private double preDragThumbPos;
    private Point2D dragStart;
    private StackPane thumb;
    private StackPane track;
    private boolean trackClicked;
    StringConverter<Number> stringConverterWrapper;

    public SliderSkin(Slider slider) {
        super(slider, new SliderBehavior(slider));
        this.tickLine = null;
        this.trackToTickGap = 2.0d;
        this.trackClicked = false;
        this.stringConverterWrapper = new StringConverter<Number>() { // from class: com.sun.javafx.scene.control.skin.SliderSkin.2
            Slider slider;

            /* JADX WARN: Multi-variable type inference failed */
            {
                this.slider = (Slider) SliderSkin.this.getSkinnable();
            }

            @Override // javafx.util.StringConverter
            public String toString(Number object) {
                return object != null ? this.slider.getLabelFormatter().toString(Double.valueOf(object.doubleValue())) : "";
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.util.StringConverter
            public Number fromString(String string) {
                return this.slider.getLabelFormatter().fromString(string);
            }
        };
        initialize();
        slider.requestLayout();
        registerChangeListener(slider.minProperty(), "MIN");
        registerChangeListener(slider.maxProperty(), "MAX");
        registerChangeListener(slider.valueProperty(), "VALUE");
        registerChangeListener(slider.orientationProperty(), "ORIENTATION");
        registerChangeListener(slider.showTickMarksProperty(), "SHOW_TICK_MARKS");
        registerChangeListener(slider.showTickLabelsProperty(), "SHOW_TICK_LABELS");
        registerChangeListener(slider.majorTickUnitProperty(), "MAJOR_TICK_UNIT");
        registerChangeListener(slider.minorTickCountProperty(), "MINOR_TICK_COUNT");
        registerChangeListener(slider.labelFormatterProperty(), "TICK_LABEL_FORMATTER");
        registerChangeListener(slider.snapToTicksProperty(), "SNAP_TO_TICKS");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initialize() {
        this.thumb = new StackPane() { // from class: com.sun.javafx.scene.control.skin.SliderSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.scene.Parent, javafx.scene.Node
            public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
                switch (AnonymousClass4.$SwitchMap$javafx$scene$AccessibleAttribute[attribute.ordinal()]) {
                    case 1:
                        return Double.valueOf(((Slider) SliderSkin.this.getSkinnable()).getValue());
                    default:
                        return super.queryAccessibleAttribute(attribute, parameters);
                }
            }
        };
        this.thumb.getStyleClass().setAll("thumb");
        this.thumb.setAccessibleRole(AccessibleRole.THUMB);
        this.track = new StackPane();
        this.track.getStyleClass().setAll("track");
        getChildren().clear();
        getChildren().addAll(this.track, this.thumb);
        setShowTickMarks(((Slider) getSkinnable()).isShowTickMarks(), ((Slider) getSkinnable()).isShowTickLabels());
        this.track.setOnMousePressed(me -> {
            if (!this.thumb.isPressed()) {
                this.trackClicked = true;
                if (((Slider) getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
                    getBehavior().trackPress(me, me.getX() / this.trackLength);
                } else {
                    getBehavior().trackPress(me, me.getY() / this.trackLength);
                }
                this.trackClicked = false;
            }
        });
        this.track.setOnMouseDragged(me2 -> {
            if (!this.thumb.isPressed()) {
                if (((Slider) getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
                    getBehavior().trackPress(me2, me2.getX() / this.trackLength);
                } else {
                    getBehavior().trackPress(me2, me2.getY() / this.trackLength);
                }
            }
        });
        this.thumb.setOnMousePressed(me3 -> {
            getBehavior().thumbPressed(me3, 0.0d);
            this.dragStart = this.thumb.localToParent(me3.getX(), me3.getY());
            this.preDragThumbPos = (((Slider) getSkinnable()).getValue() - ((Slider) getSkinnable()).getMin()) / (((Slider) getSkinnable()).getMax() - ((Slider) getSkinnable()).getMin());
        });
        this.thumb.setOnMouseReleased(me4 -> {
            getBehavior().thumbReleased(me4);
        });
        this.thumb.setOnMouseDragged(me5 -> {
            Point2D cur = this.thumb.localToParent(me5.getX(), me5.getY());
            double dragPos = ((Slider) getSkinnable()).getOrientation() == Orientation.HORIZONTAL ? cur.getX() - this.dragStart.getX() : -(cur.getY() - this.dragStart.getY());
            getBehavior().thumbDragged(me5, this.preDragThumbPos + (dragPos / this.trackLength));
        });
    }

    /* renamed from: com.sun.javafx.scene.control.skin.SliderSkin$4, reason: invalid class name */
    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SliderSkin$4.class */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$AccessibleAttribute = new int[AccessibleAttribute.values().length];

        static {
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.VALUE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setShowTickMarks(boolean ticksVisible, boolean labelsVisible) {
        this.showTickMarks = ticksVisible || labelsVisible;
        Slider slider = (Slider) getSkinnable();
        if (this.showTickMarks) {
            if (this.tickLine == null) {
                this.tickLine = new NumberAxis();
                this.tickLine.setAutoRanging(false);
                NumberAxis numberAxis = this.tickLine;
                Side side = (slider.getOrientation() == Orientation.VERTICAL || slider.getOrientation() == null) ? Side.RIGHT : Side.BOTTOM;
                numberAxis.setSide(side);
                this.tickLine.setUpperBound(slider.getMax());
                this.tickLine.setLowerBound(slider.getMin());
                this.tickLine.setTickUnit(slider.getMajorTickUnit());
                this.tickLine.setTickMarkVisible(ticksVisible);
                this.tickLine.setTickLabelsVisible(labelsVisible);
                this.tickLine.setMinorTickVisible(ticksVisible);
                this.tickLine.setMinorTickCount(Math.max(slider.getMinorTickCount(), 0) + 1);
                if (slider.getLabelFormatter() != null) {
                    this.tickLine.setTickLabelFormatter(this.stringConverterWrapper);
                }
                getChildren().clear();
                getChildren().addAll(this.tickLine, this.track, this.thumb);
            } else {
                this.tickLine.setTickLabelsVisible(labelsVisible);
                this.tickLine.setTickMarkVisible(ticksVisible);
                this.tickLine.setMinorTickVisible(ticksVisible);
            }
        } else {
            getChildren().clear();
            getChildren().addAll(this.track, this.thumb);
        }
        ((Slider) getSkinnable()).requestLayout();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        Slider slider = (Slider) getSkinnable();
        if ("ORIENTATION".equals(p2)) {
            if (this.showTickMarks && this.tickLine != null) {
                NumberAxis numberAxis = this.tickLine;
                Side side = (slider.getOrientation() == Orientation.VERTICAL || slider.getOrientation() == null) ? Side.RIGHT : Side.BOTTOM;
                numberAxis.setSide(side);
            }
            ((Slider) getSkinnable()).requestLayout();
            return;
        }
        if ("VALUE".equals(p2)) {
            positionThumb(this.trackClicked);
            return;
        }
        if ("MIN".equals(p2)) {
            if (this.showTickMarks && this.tickLine != null) {
                this.tickLine.setLowerBound(slider.getMin());
            }
            ((Slider) getSkinnable()).requestLayout();
            return;
        }
        if ("MAX".equals(p2)) {
            if (this.showTickMarks && this.tickLine != null) {
                this.tickLine.setUpperBound(slider.getMax());
            }
            ((Slider) getSkinnable()).requestLayout();
            return;
        }
        if ("SHOW_TICK_MARKS".equals(p2) || "SHOW_TICK_LABELS".equals(p2)) {
            setShowTickMarks(slider.isShowTickMarks(), slider.isShowTickLabels());
            return;
        }
        if ("MAJOR_TICK_UNIT".equals(p2)) {
            if (this.tickLine != null) {
                this.tickLine.setTickUnit(slider.getMajorTickUnit());
                ((Slider) getSkinnable()).requestLayout();
                return;
            }
            return;
        }
        if ("MINOR_TICK_COUNT".equals(p2)) {
            if (this.tickLine != null) {
                this.tickLine.setMinorTickCount(Math.max(slider.getMinorTickCount(), 0) + 1);
                ((Slider) getSkinnable()).requestLayout();
                return;
            }
            return;
        }
        if ("TICK_LABEL_FORMATTER".equals(p2)) {
            if (this.tickLine != null) {
                if (slider.getLabelFormatter() == null) {
                    this.tickLine.setTickLabelFormatter(null);
                    return;
                } else {
                    this.tickLine.setTickLabelFormatter(this.stringConverterWrapper);
                    this.tickLine.requestAxisLayout();
                    return;
                }
            }
            return;
        }
        if ("SNAP_TO_TICKS".equals(p2)) {
            slider.adjustValue(slider.getValue());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void positionThumb(boolean animate) {
        Slider s2 = (Slider) getSkinnable();
        if (s2.getValue() > s2.getMax()) {
            return;
        }
        boolean horizontal = s2.getOrientation() == Orientation.HORIZONTAL;
        final double endX = horizontal ? this.trackStart + ((this.trackLength * ((s2.getValue() - s2.getMin()) / (s2.getMax() - s2.getMin()))) - (this.thumbWidth / 2.0d)) : this.thumbLeft;
        final double endY = horizontal ? this.thumbTop : (snappedTopInset() + this.trackLength) - (this.trackLength * ((s2.getValue() - s2.getMin()) / (s2.getMax() - s2.getMin())));
        if (animate) {
            final double startX = this.thumb.getLayoutX();
            final double startY = this.thumb.getLayoutY();
            Transition transition = new Transition() { // from class: com.sun.javafx.scene.control.skin.SliderSkin.3
                {
                    setCycleDuration(Duration.millis(200.0d));
                }

                @Override // javafx.animation.Transition
                protected void interpolate(double frac) {
                    if (!Double.isNaN(startX)) {
                        SliderSkin.this.thumb.setLayoutX(startX + (frac * (endX - startX)));
                    }
                    if (!Double.isNaN(startY)) {
                        SliderSkin.this.thumb.setLayoutY(startY + (frac * (endY - startY)));
                    }
                }
            };
            transition.play();
            return;
        }
        this.thumb.setLayoutX(endX);
        this.thumb.setLayoutY(endY);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        this.thumbWidth = snapSize(this.thumb.prefWidth(-1.0d));
        this.thumbHeight = snapSize(this.thumb.prefHeight(-1.0d));
        this.thumb.resize(this.thumbWidth, this.thumbHeight);
        double topLeftHorizontalRadius = (this.track.getBackground() != null && this.track.getBackground().getFills().size() > 0) ? this.track.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius() : 0.0d;
        double trackRadius = topLeftHorizontalRadius;
        if (((Slider) getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
            double tickLineHeight = this.showTickMarks ? this.tickLine.prefHeight(-1.0d) : 0.0d;
            double trackHeight = snapSize(this.track.prefHeight(-1.0d));
            double trackAreaHeight = Math.max(trackHeight, this.thumbHeight);
            double totalHeightNeeded = trackAreaHeight + (this.showTickMarks ? this.trackToTickGap + tickLineHeight : 0.0d);
            double startY = y2 + ((h2 - totalHeightNeeded) / 2.0d);
            this.trackLength = snapSize(w2 - this.thumbWidth);
            this.trackStart = snapPosition(x2 + (this.thumbWidth / 2.0d));
            double trackTop = (int) (startY + ((trackAreaHeight - trackHeight) / 2.0d));
            this.thumbTop = (int) (startY + ((trackAreaHeight - this.thumbHeight) / 2.0d));
            positionThumb(false);
            this.track.resizeRelocate((int) (this.trackStart - trackRadius), trackTop, (int) (this.trackLength + trackRadius + trackRadius), trackHeight);
            if (this.showTickMarks) {
                this.tickLine.setLayoutX(this.trackStart);
                this.tickLine.setLayoutY(trackTop + trackHeight + this.trackToTickGap);
                this.tickLine.resize(this.trackLength, tickLineHeight);
                this.tickLine.requestAxisLayout();
                return;
            }
            if (this.tickLine != null) {
                this.tickLine.resize(0.0d, 0.0d);
                this.tickLine.requestAxisLayout();
            }
            this.tickLine = null;
            return;
        }
        double tickLineWidth = this.showTickMarks ? this.tickLine.prefWidth(-1.0d) : 0.0d;
        double trackWidth = snapSize(this.track.prefWidth(-1.0d));
        double trackAreaWidth = Math.max(trackWidth, this.thumbWidth);
        double totalWidthNeeded = trackAreaWidth + (this.showTickMarks ? this.trackToTickGap + tickLineWidth : 0.0d);
        double startX = x2 + ((w2 - totalWidthNeeded) / 2.0d);
        this.trackLength = snapSize(h2 - this.thumbHeight);
        this.trackStart = snapPosition(y2 + (this.thumbHeight / 2.0d));
        double trackLeft = (int) (startX + ((trackAreaWidth - trackWidth) / 2.0d));
        this.thumbLeft = (int) (startX + ((trackAreaWidth - this.thumbWidth) / 2.0d));
        positionThumb(false);
        this.track.resizeRelocate(trackLeft, (int) (this.trackStart - trackRadius), trackWidth, (int) (this.trackLength + trackRadius + trackRadius));
        if (this.showTickMarks) {
            this.tickLine.setLayoutX(trackLeft + trackWidth + this.trackToTickGap);
            this.tickLine.setLayoutY(this.trackStart);
            this.tickLine.resize(tickLineWidth, this.trackLength);
            this.tickLine.requestAxisLayout();
            return;
        }
        if (this.tickLine != null) {
            this.tickLine.resize(0.0d, 0.0d);
            this.tickLine.requestAxisLayout();
        }
        this.tickLine = null;
    }

    double minTrackLength() {
        return 2.0d * this.thumb.prefWidth(-1.0d);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        Slider s2 = (Slider) getSkinnable();
        if (s2.getOrientation() == Orientation.HORIZONTAL) {
            return leftInset + minTrackLength() + this.thumb.minWidth(-1.0d) + rightInset;
        }
        return leftInset + this.thumb.prefWidth(-1.0d) + rightInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        Slider s2 = (Slider) getSkinnable();
        if (s2.getOrientation() == Orientation.HORIZONTAL) {
            double axisHeight = this.showTickMarks ? this.tickLine.prefHeight(-1.0d) + this.trackToTickGap : 0.0d;
            return topInset + this.thumb.prefHeight(-1.0d) + axisHeight + bottomInset;
        }
        return topInset + minTrackLength() + this.thumb.prefHeight(-1.0d) + bottomInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        Slider s2 = (Slider) getSkinnable();
        if (s2.getOrientation() == Orientation.HORIZONTAL) {
            if (this.showTickMarks) {
                return Math.max(140.0d, this.tickLine.prefWidth(-1.0d));
            }
            return 140.0d;
        }
        double axisWidth = this.showTickMarks ? this.tickLine.prefWidth(-1.0d) + this.trackToTickGap : 0.0d;
        return leftInset + Math.max(this.thumb.prefWidth(-1.0d), this.track.prefWidth(-1.0d)) + axisWidth + rightInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        Slider s2 = (Slider) getSkinnable();
        if (s2.getOrientation() == Orientation.HORIZONTAL) {
            return topInset + Math.max(this.thumb.prefHeight(-1.0d), this.track.prefHeight(-1.0d)) + (this.showTickMarks ? this.trackToTickGap + this.tickLine.prefHeight(-1.0d) : 0.0d) + bottomInset;
        }
        if (this.showTickMarks) {
            return Math.max(140.0d, this.tickLine.prefHeight(-1.0d));
        }
        return 140.0d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (((Slider) getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
            return Double.MAX_VALUE;
        }
        return ((Slider) getSkinnable()).prefWidth(-1.0d);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (((Slider) getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
            return ((Slider) getSkinnable()).prefHeight(width);
        }
        return Double.MAX_VALUE;
    }
}
