package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Window;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ProgressIndicatorSkin.class */
public class ProgressIndicatorSkin extends BehaviorSkinBase<ProgressIndicator, BehaviorBase<ProgressIndicator>> {
    private ObjectProperty<Paint> progressColor;
    private IntegerProperty indeterminateSegmentCount;
    private final BooleanProperty spinEnabled;
    private static final String DONE = ControlResources.getString("ProgressIndicator.doneString");
    private static final Text doneText = new Text(DONE);
    private IndeterminateSpinner spinner;
    private DeterminateIndicator determinateIndicator;
    private ProgressIndicator control;
    protected Animation indeterminateTransition;
    private ReadOnlyObjectProperty<Window> windowProperty;
    private ReadOnlyBooleanProperty windowShowingProperty;
    protected final Duration CLIPPED_DELAY;
    protected final Duration UNCLIPPED_DELAY;
    private static final CssMetaData<ProgressIndicator, Paint> PROGRESS_COLOR;
    private static final CssMetaData<ProgressIndicator, Number> INDETERMINATE_SEGMENT_COUNT;
    private static final CssMetaData<ProgressIndicator, Boolean> SPIN_ENABLED;
    public static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    Paint getProgressColor() {
        return this.progressColor.get();
    }

    static {
        doneText.getStyleClass().add("text");
        PROGRESS_COLOR = new CssMetaData<ProgressIndicator, Paint>("-fx-progress-color", PaintConverter.getInstance(), null) { // from class: com.sun.javafx.scene.control.skin.ProgressIndicatorSkin.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ProgressIndicator n2) {
                ProgressIndicatorSkin skin = (ProgressIndicatorSkin) n2.getSkin();
                return skin.progressColor == null || !skin.progressColor.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Paint> getStyleableProperty(ProgressIndicator n2) {
                ProgressIndicatorSkin skin = (ProgressIndicatorSkin) n2.getSkin();
                return (StyleableProperty) skin.progressColor;
            }
        };
        INDETERMINATE_SEGMENT_COUNT = new CssMetaData<ProgressIndicator, Number>("-fx-indeterminate-segment-count", SizeConverter.getInstance(), 8) { // from class: com.sun.javafx.scene.control.skin.ProgressIndicatorSkin.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ProgressIndicator n2) {
                ProgressIndicatorSkin skin = (ProgressIndicatorSkin) n2.getSkin();
                return skin.indeterminateSegmentCount == null || !skin.indeterminateSegmentCount.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ProgressIndicator n2) {
                ProgressIndicatorSkin skin = (ProgressIndicatorSkin) n2.getSkin();
                return (StyleableProperty) skin.indeterminateSegmentCount;
            }
        };
        SPIN_ENABLED = new CssMetaData<ProgressIndicator, Boolean>("-fx-spin-enabled", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: com.sun.javafx.scene.control.skin.ProgressIndicatorSkin.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ProgressIndicator node) {
                ProgressIndicatorSkin skin = (ProgressIndicatorSkin) node.getSkin();
                return skin.spinEnabled == null || !skin.spinEnabled.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(ProgressIndicator node) {
                ProgressIndicatorSkin skin = (ProgressIndicatorSkin) node.getSkin();
                return (StyleableProperty) skin.spinEnabled;
            }
        };
        List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(SkinBase.getClassCssMetaData());
        styleables.add(PROGRESS_COLOR);
        styleables.add(INDETERMINATE_SEGMENT_COUNT);
        styleables.add(SPIN_ENABLED);
        STYLEABLES = Collections.unmodifiableList(styleables);
    }

    public ProgressIndicatorSkin(ProgressIndicator control) {
        super(control, new BehaviorBase(control, Collections.emptyList()));
        this.progressColor = new StyleableObjectProperty<Paint>(null) { // from class: com.sun.javafx.scene.control.skin.ProgressIndicatorSkin.1
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                Paint value = get();
                if (value == null || (value instanceof Color)) {
                    if (ProgressIndicatorSkin.this.spinner != null) {
                        ProgressIndicatorSkin.this.spinner.setFillOverride(value);
                    }
                    if (ProgressIndicatorSkin.this.determinateIndicator == null) {
                        return;
                    }
                    ProgressIndicatorSkin.this.determinateIndicator.setFillOverride(value);
                    return;
                }
                if (isBound()) {
                    unbind();
                }
                set(null);
                throw new IllegalArgumentException("Only Color objects are supported");
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ProgressIndicatorSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "progressColorProperty";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<ProgressIndicator, Paint> getCssMetaData() {
                return ProgressIndicatorSkin.PROGRESS_COLOR;
            }
        };
        this.indeterminateSegmentCount = new StyleableIntegerProperty(8) { // from class: com.sun.javafx.scene.control.skin.ProgressIndicatorSkin.2
            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                if (ProgressIndicatorSkin.this.spinner == null) {
                    return;
                }
                ProgressIndicatorSkin.this.spinner.rebuild();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ProgressIndicatorSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "indeterminateSegmentCount";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return ProgressIndicatorSkin.INDETERMINATE_SEGMENT_COUNT;
            }
        };
        this.spinEnabled = new StyleableBooleanProperty(false) { // from class: com.sun.javafx.scene.control.skin.ProgressIndicatorSkin.3
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                if (ProgressIndicatorSkin.this.spinner != null) {
                    ProgressIndicatorSkin.this.spinner.setSpinEnabled(get());
                }
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return ProgressIndicatorSkin.SPIN_ENABLED;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ProgressIndicatorSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "spinEnabled";
            }
        };
        this.windowProperty = null;
        this.windowShowingProperty = null;
        this.CLIPPED_DELAY = new Duration(300.0d);
        this.UNCLIPPED_DELAY = new Duration(0.0d);
        this.control = control;
        registerChangeListener(control.indeterminateProperty(), "INDETERMINATE");
        registerChangeListener(control.progressProperty(), "PROGRESS");
        registerChangeListener(control.visibleProperty(), "VISIBLE");
        registerChangeListener(control.parentProperty(), "PARENT");
        registerChangeListener(control.sceneProperty(), "SCENE");
        updateWindowListeners();
        initialize();
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("INDETERMINATE".equals(p2)) {
            initialize();
            return;
        }
        if ("PROGRESS".equals(p2)) {
            updateProgress();
            return;
        }
        if ("VISIBLE".equals(p2)) {
            updateAnimation();
            return;
        }
        if ("PARENT".equals(p2)) {
            updateAnimation();
            return;
        }
        if ("SCENE".equals(p2)) {
            updateWindowListeners();
            updateAnimation();
        } else if ("WINDOW".equals(p2)) {
            updateWindowListeners();
            updateAnimation();
        } else if ("WINDOWSHOWING".equals(p2)) {
            updateAnimation();
        }
    }

    protected void initialize() {
        boolean isIndeterminate = this.control.isIndeterminate();
        if (isIndeterminate) {
            this.determinateIndicator = null;
            this.spinner = new IndeterminateSpinner(this.spinEnabled.get(), this.progressColor.get());
            getChildren().setAll(this.spinner);
            if (this.control.impl_isTreeVisible() && this.indeterminateTransition != null) {
                this.indeterminateTransition.play();
                return;
            }
            return;
        }
        if (this.spinner != null) {
            if (this.indeterminateTransition != null) {
                this.indeterminateTransition.stop();
            }
            this.spinner = null;
        }
        this.determinateIndicator = new DeterminateIndicator(this.control, this, this.progressColor.get());
        getChildren().setAll(this.determinateIndicator);
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase, javafx.scene.control.SkinBase, javafx.scene.control.Skin
    public void dispose() {
        super.dispose();
        if (this.indeterminateTransition != null) {
            this.indeterminateTransition.stop();
            this.indeterminateTransition = null;
        }
        if (this.spinner != null) {
            this.spinner = null;
        }
        this.control = null;
    }

    protected void updateProgress() {
        if (this.determinateIndicator == null) {
            return;
        }
        this.determinateIndicator.updateProgress(this.control.getProgress());
    }

    protected void createIndeterminateTimeline() {
        if (this.spinner == null) {
            return;
        }
        this.spinner.rebuildTimeline();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void pauseTimeline(boolean pause) {
        if (((ProgressIndicator) getSkinnable()).isIndeterminate()) {
            if (this.indeterminateTransition == null) {
                createIndeterminateTimeline();
            }
            if (pause) {
                this.indeterminateTransition.pause();
            } else {
                this.indeterminateTransition.play();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void updateAnimation() {
        ProgressIndicator control = (ProgressIndicator) getSkinnable();
        boolean isTreeVisible = (!control.isVisible() || control.getParent() == null || control.getScene() == null || control.getScene().getWindow() == null || !control.getScene().getWindow().isShowing()) ? false : true;
        if (this.indeterminateTransition != null) {
            pauseTimeline(!isTreeVisible);
        } else if (isTreeVisible) {
            createIndeterminateTimeline();
        }
    }

    private void updateWindowListeners() {
        if (this.windowProperty != null) {
            unregisterChangeListener(this.windowProperty);
            this.windowProperty = null;
            unregisterChangeListener(this.windowShowingProperty);
            this.windowShowingProperty = null;
        }
        if (this.control.getScene() != null && this.control.getScene().getWindow() != null) {
            this.windowProperty = this.control.getScene().windowProperty();
            this.windowShowingProperty = this.control.getScene().getWindow().showingProperty();
            registerChangeListener(this.windowProperty, "WINDOW");
            registerChangeListener(this.windowShowingProperty, "WINDOWSHOWING");
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        if (this.spinner != null && this.control.isIndeterminate()) {
            this.spinner.layoutChildren();
            this.spinner.resizeRelocate(0.0d, 0.0d, w2, h2);
        } else if (this.determinateIndicator != null) {
            this.determinateIndicator.layoutChildren();
            this.determinateIndicator.resizeRelocate(0.0d, 0.0d, w2, h2);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ProgressIndicatorSkin$DeterminateIndicator.class */
    private class DeterminateIndicator extends Region {
        private double textGap = 2.0d;
        private int intProgress;
        private int degProgress;
        private Text text;
        private StackPane indicator;
        private StackPane progress;
        private StackPane tick;
        private Arc arcShape;
        private Circle indicatorCircle;

        public DeterminateIndicator(ProgressIndicator control, ProgressIndicatorSkin s2, Paint fillOverride) {
            getStyleClass().add("determinate-indicator");
            this.intProgress = (int) Math.round(control.getProgress() * 100.0d);
            this.degProgress = (int) (360.0d * control.getProgress());
            getChildren().clear();
            this.text = new Text(control.getProgress() >= 1.0d ? ProgressIndicatorSkin.DONE : "" + this.intProgress + FXMLLoader.RESOURCE_KEY_PREFIX);
            this.text.setTextOrigin(VPos.TOP);
            this.text.getStyleClass().setAll("text", "percentage");
            this.indicator = new StackPane();
            this.indicator.setScaleShape(false);
            this.indicator.setCenterShape(false);
            this.indicator.getStyleClass().setAll("indicator");
            this.indicatorCircle = new Circle();
            this.indicator.setShape(this.indicatorCircle);
            this.arcShape = new Arc();
            this.arcShape.setType(ArcType.ROUND);
            this.arcShape.setStartAngle(90.0d);
            this.progress = new StackPane();
            this.progress.getStyleClass().setAll("progress");
            this.progress.setScaleShape(false);
            this.progress.setCenterShape(false);
            this.progress.setShape(this.arcShape);
            this.progress.getChildren().clear();
            setFillOverride(fillOverride);
            this.tick = new StackPane();
            this.tick.getStyleClass().setAll("tick");
            getChildren().setAll(this.indicator, this.progress, this.text, this.tick);
            updateProgress(control.getProgress());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setFillOverride(Paint fillOverride) {
            if (fillOverride instanceof Color) {
                Color c2 = (Color) fillOverride;
                this.progress.setStyle("-fx-background-color: rgba(" + ((int) (255.0d * c2.getRed())) + "," + ((int) (255.0d * c2.getGreen())) + "," + ((int) (255.0d * c2.getBlue())) + "," + c2.getOpacity() + ");");
            } else {
                this.progress.setStyle(null);
            }
        }

        @Override // javafx.scene.Node
        public boolean usesMirroring() {
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateProgress(double progress) {
            this.intProgress = (int) Math.round(progress * 100.0d);
            this.text.setText(progress >= 1.0d ? ProgressIndicatorSkin.DONE : "" + this.intProgress + FXMLLoader.RESOURCE_KEY_PREFIX);
            this.degProgress = (int) (360.0d * progress);
            this.arcShape.setLength(-this.degProgress);
            requestLayout();
        }

        @Override // javafx.scene.Parent
        protected void layoutChildren() {
            double doneTextHeight = ProgressIndicatorSkin.doneText.getLayoutBounds().getHeight();
            double left = ProgressIndicatorSkin.this.control.snappedLeftInset();
            double right = ProgressIndicatorSkin.this.control.snappedRightInset();
            double top = ProgressIndicatorSkin.this.control.snappedTopInset();
            double bottom = ProgressIndicatorSkin.this.control.snappedBottomInset();
            double areaW = (ProgressIndicatorSkin.this.control.getWidth() - left) - right;
            double areaH = (((ProgressIndicatorSkin.this.control.getHeight() - top) - bottom) - this.textGap) - doneTextHeight;
            double radiusW = areaW / 2.0d;
            double radiusH = areaH / 2.0d;
            double radius = Math.floor(Math.min(radiusW, radiusH));
            double centerX = snapPosition(left + radiusW);
            double centerY = snapPosition(top + radius);
            double iLeft = this.indicator.snappedLeftInset();
            double iRight = this.indicator.snappedRightInset();
            double iTop = this.indicator.snappedTopInset();
            double iBottom = this.indicator.snappedBottomInset();
            double progressRadius = snapSize(Math.min(Math.min(radius - iLeft, radius - iRight), Math.min(radius - iTop, radius - iBottom)));
            this.indicatorCircle.setRadius(radius);
            this.indicator.setLayoutX(centerX);
            this.indicator.setLayoutY(centerY);
            this.arcShape.setRadiusX(progressRadius);
            this.arcShape.setRadiusY(progressRadius);
            this.progress.setLayoutX(centerX);
            this.progress.setLayoutY(centerY);
            double pLeft = this.progress.snappedLeftInset();
            double pRight = this.progress.snappedRightInset();
            double pTop = this.progress.snappedTopInset();
            double pBottom = this.progress.snappedBottomInset();
            double indicatorRadius = snapSize(Math.min(Math.min(progressRadius - pLeft, progressRadius - pRight), Math.min(progressRadius - pTop, progressRadius - pBottom)));
            double squareBoxHalfWidth = Math.ceil(Math.sqrt((indicatorRadius * indicatorRadius) / 2.0d));
            double dSqrt = indicatorRadius * (Math.sqrt(2.0d) / 2.0d);
            this.tick.setLayoutX(centerX - squareBoxHalfWidth);
            this.tick.setLayoutY(centerY - squareBoxHalfWidth);
            this.tick.resize(squareBoxHalfWidth + squareBoxHalfWidth, squareBoxHalfWidth + squareBoxHalfWidth);
            this.tick.setVisible(ProgressIndicatorSkin.this.control.getProgress() >= 1.0d);
            double textWidth = this.text.getLayoutBounds().getWidth();
            double textHeight = this.text.getLayoutBounds().getHeight();
            if (ProgressIndicatorSkin.this.control.getWidth() >= textWidth && ProgressIndicatorSkin.this.control.getHeight() >= textHeight) {
                if (!this.text.isVisible()) {
                    this.text.setVisible(true);
                }
                this.text.setLayoutY(snapPosition(centerY + radius + this.textGap));
                this.text.setLayoutX(snapPosition(centerX - (textWidth / 2.0d)));
                return;
            }
            if (this.text.isVisible()) {
                this.text.setVisible(false);
            }
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            double left = ProgressIndicatorSkin.this.control.snappedLeftInset();
            double right = ProgressIndicatorSkin.this.control.snappedRightInset();
            double iLeft = this.indicator.snappedLeftInset();
            double iRight = this.indicator.snappedRightInset();
            double iTop = this.indicator.snappedTopInset();
            double iBottom = this.indicator.snappedBottomInset();
            double indicatorMax = snapSize(Math.max(Math.max(iLeft, iRight), Math.max(iTop, iBottom)));
            double pLeft = this.progress.snappedLeftInset();
            double pRight = this.progress.snappedRightInset();
            double pTop = this.progress.snappedTopInset();
            double pBottom = this.progress.snappedBottomInset();
            double progressMax = snapSize(Math.max(Math.max(pLeft, pRight), Math.max(pTop, pBottom)));
            double tLeft = this.tick.snappedLeftInset();
            double tRight = this.tick.snappedRightInset();
            double indicatorWidth = indicatorMax + progressMax + tLeft + tRight + progressMax + indicatorMax;
            return left + Math.max(indicatorWidth, ProgressIndicatorSkin.doneText.getLayoutBounds().getWidth()) + right;
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            double top = ProgressIndicatorSkin.this.control.snappedTopInset();
            double bottom = ProgressIndicatorSkin.this.control.snappedBottomInset();
            double iLeft = this.indicator.snappedLeftInset();
            double iRight = this.indicator.snappedRightInset();
            double iTop = this.indicator.snappedTopInset();
            double iBottom = this.indicator.snappedBottomInset();
            double indicatorMax = snapSize(Math.max(Math.max(iLeft, iRight), Math.max(iTop, iBottom)));
            double pLeft = this.progress.snappedLeftInset();
            double pRight = this.progress.snappedRightInset();
            double pTop = this.progress.snappedTopInset();
            double pBottom = this.progress.snappedBottomInset();
            double progressMax = snapSize(Math.max(Math.max(pLeft, pRight), Math.max(pTop, pBottom)));
            double tTop = this.tick.snappedTopInset();
            double tBottom = this.tick.snappedBottomInset();
            double indicatorHeight = indicatorMax + progressMax + tTop + tBottom + progressMax + indicatorMax;
            return top + indicatorHeight + this.textGap + ProgressIndicatorSkin.doneText.getLayoutBounds().getHeight() + bottom;
        }

        @Override // javafx.scene.layout.Region
        protected double computeMaxWidth(double height) {
            return computePrefWidth(height);
        }

        @Override // javafx.scene.layout.Region
        protected double computeMaxHeight(double width) {
            return computePrefHeight(width);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ProgressIndicatorSkin$IndeterminateSpinner.class */
    private final class IndeterminateSpinner extends Region {
        private IndicatorPaths pathsG;
        private final List<Double> opacities;
        private boolean spinEnabled;
        private Paint fillOverride;

        private IndeterminateSpinner(boolean spinEnabled, Paint fillOverride) {
            this.opacities = new ArrayList();
            this.spinEnabled = false;
            this.fillOverride = null;
            this.spinEnabled = spinEnabled;
            this.fillOverride = fillOverride;
            setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            getStyleClass().setAll("spinner");
            this.pathsG = new IndicatorPaths();
            getChildren().add(this.pathsG);
            rebuild();
            rebuildTimeline();
        }

        public void setFillOverride(Paint fillOverride) {
            this.fillOverride = fillOverride;
            rebuild();
        }

        public void setSpinEnabled(boolean spinEnabled) {
            this.spinEnabled = spinEnabled;
            rebuildTimeline();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void rebuildTimeline() {
            if (this.spinEnabled) {
                if (ProgressIndicatorSkin.this.indeterminateTransition == null) {
                    ProgressIndicatorSkin.this.indeterminateTransition = new Timeline();
                    ProgressIndicatorSkin.this.indeterminateTransition.setCycleCount(-1);
                    ProgressIndicatorSkin.this.indeterminateTransition.setDelay(ProgressIndicatorSkin.this.UNCLIPPED_DELAY);
                } else {
                    ProgressIndicatorSkin.this.indeterminateTransition.stop();
                    ((Timeline) ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().clear();
                }
                ObservableList<KeyFrame> keyFrames = FXCollections.observableArrayList();
                keyFrames.add(new KeyFrame(Duration.millis(1.0d), new KeyValue(this.pathsG.rotateProperty(), 360)));
                keyFrames.add(new KeyFrame(Duration.millis(3900.0d), new KeyValue(this.pathsG.rotateProperty(), 0)));
                for (int i2 = 100; i2 <= 3900; i2 += 100) {
                    keyFrames.add(new KeyFrame(Duration.millis(i2), (EventHandler<ActionEvent>) event -> {
                        shiftColors();
                    }, new KeyValue[0]));
                }
                ((Timeline) ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().setAll(keyFrames);
                ProgressIndicatorSkin.this.indeterminateTransition.playFromStart();
                return;
            }
            if (ProgressIndicatorSkin.this.indeterminateTransition != null) {
                ProgressIndicatorSkin.this.indeterminateTransition.stop();
                ((Timeline) ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().clear();
                ProgressIndicatorSkin.this.indeterminateTransition = null;
            }
        }

        /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ProgressIndicatorSkin$IndeterminateSpinner$IndicatorPaths.class */
        private class IndicatorPaths extends Pane {
            private IndicatorPaths() {
            }

            @Override // javafx.scene.layout.Region, javafx.scene.Parent
            protected double computePrefWidth(double height) {
                double w2 = 0.0d;
                for (Node child : getChildren()) {
                    if (child instanceof Region) {
                        Region region = (Region) child;
                        if (region.getShape() != null) {
                            w2 = Math.max(w2, region.getShape().getLayoutBounds().getMaxX());
                        } else {
                            w2 = Math.max(w2, region.prefWidth(height));
                        }
                    }
                }
                return w2;
            }

            @Override // javafx.scene.layout.Region, javafx.scene.Parent
            protected double computePrefHeight(double width) {
                double h2 = 0.0d;
                for (Node child : getChildren()) {
                    if (child instanceof Region) {
                        Region region = (Region) child;
                        if (region.getShape() != null) {
                            h2 = Math.max(h2, region.getShape().getLayoutBounds().getMaxY());
                        } else {
                            h2 = Math.max(h2, region.prefHeight(width));
                        }
                    }
                }
                return h2;
            }

            @Override // javafx.scene.Parent
            protected void layoutChildren() {
                double scale = getWidth() / computePrefWidth(-1.0d);
                for (Node child : getChildren()) {
                    if (child instanceof Region) {
                        Region region = (Region) child;
                        if (region.getShape() != null) {
                            region.resize(region.getShape().getLayoutBounds().getMaxX(), region.getShape().getLayoutBounds().getMaxY());
                            region.getTransforms().setAll(new Scale(scale, scale, 0.0d, 0.0d));
                        } else {
                            region.autosize();
                        }
                    }
                }
            }
        }

        @Override // javafx.scene.Parent
        protected void layoutChildren() {
            double w2 = (ProgressIndicatorSkin.this.control.getWidth() - ProgressIndicatorSkin.this.control.snappedLeftInset()) - ProgressIndicatorSkin.this.control.snappedRightInset();
            double h2 = (ProgressIndicatorSkin.this.control.getHeight() - ProgressIndicatorSkin.this.control.snappedTopInset()) - ProgressIndicatorSkin.this.control.snappedBottomInset();
            double prefW = this.pathsG.prefWidth(-1.0d);
            double prefH = this.pathsG.prefHeight(-1.0d);
            double scaleX = w2 / prefW;
            double scale = scaleX;
            if (scaleX * prefH > h2) {
                scale = h2 / prefH;
            }
            double indicatorW = prefW * scale;
            double indicatorH = prefH * scale;
            this.pathsG.resizeRelocate((w2 - indicatorW) / 2.0d, (h2 - indicatorH) / 2.0d, indicatorW, indicatorH);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void rebuild() {
            int segments = ProgressIndicatorSkin.this.indeterminateSegmentCount.get();
            this.opacities.clear();
            this.pathsG.getChildren().clear();
            double step = 0.8d / (segments - 1);
            for (int i2 = 0; i2 < segments; i2++) {
                Region region = new Region();
                region.setScaleShape(false);
                region.setCenterShape(false);
                region.getStyleClass().addAll("segment", "segment" + i2);
                if (this.fillOverride instanceof Color) {
                    Color c2 = (Color) this.fillOverride;
                    region.setStyle("-fx-background-color: rgba(" + ((int) (255.0d * c2.getRed())) + "," + ((int) (255.0d * c2.getGreen())) + "," + ((int) (255.0d * c2.getBlue())) + "," + c2.getOpacity() + ");");
                } else {
                    region.setStyle(null);
                }
                this.pathsG.getChildren().add(region);
                this.opacities.add(Double.valueOf(Math.max(0.1d, 1.0d - (step * i2))));
            }
        }

        private void shiftColors() {
            if (this.opacities.size() <= 0) {
                return;
            }
            int segments = ProgressIndicatorSkin.this.indeterminateSegmentCount.get();
            Collections.rotate(this.opacities, -1);
            for (int i2 = 0; i2 < segments; i2++) {
                this.pathsG.getChildren().get(i2).setOpacity(this.opacities.get(i2).doubleValue());
            }
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override // javafx.scene.control.SkinBase
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}
