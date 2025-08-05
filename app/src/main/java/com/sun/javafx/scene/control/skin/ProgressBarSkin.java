package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Transition;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ProgressBarSkin.class */
public class ProgressBarSkin extends ProgressIndicatorSkin {
    private DoubleProperty indeterminateBarLength;
    private BooleanProperty indeterminateBarEscape;
    private BooleanProperty indeterminateBarFlip;
    private DoubleProperty indeterminateBarAnimationTime;
    private StackPane bar;
    private StackPane track;
    private Region clipRegion;
    private double barWidth;
    boolean wasIndeterminate;

    /* JADX INFO: Access modifiers changed from: private */
    public DoubleProperty indeterminateBarLengthProperty() {
        if (this.indeterminateBarLength == null) {
            this.indeterminateBarLength = new StyleableDoubleProperty(60.0d) { // from class: com.sun.javafx.scene.control.skin.ProgressBarSkin.1
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "indeterminateBarLength";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_LENGTH;
                }
            };
        }
        return this.indeterminateBarLength;
    }

    private Double getIndeterminateBarLength() {
        return Double.valueOf(this.indeterminateBarLength == null ? 60.0d : this.indeterminateBarLength.get());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BooleanProperty indeterminateBarEscapeProperty() {
        if (this.indeterminateBarEscape == null) {
            this.indeterminateBarEscape = new StyleableBooleanProperty(true) { // from class: com.sun.javafx.scene.control.skin.ProgressBarSkin.2
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "indeterminateBarEscape";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_ESCAPE;
                }
            };
        }
        return this.indeterminateBarEscape;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean getIndeterminateBarEscape() {
        return Boolean.valueOf(this.indeterminateBarEscape == null ? true : this.indeterminateBarEscape.get());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BooleanProperty indeterminateBarFlipProperty() {
        if (this.indeterminateBarFlip == null) {
            this.indeterminateBarFlip = new StyleableBooleanProperty(true) { // from class: com.sun.javafx.scene.control.skin.ProgressBarSkin.3
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "indeterminateBarFlip";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_FLIP;
                }
            };
        }
        return this.indeterminateBarFlip;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Boolean getIndeterminateBarFlip() {
        return Boolean.valueOf(this.indeterminateBarFlip == null ? true : this.indeterminateBarFlip.get());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DoubleProperty indeterminateBarAnimationTimeProperty() {
        if (this.indeterminateBarAnimationTime == null) {
            this.indeterminateBarAnimationTime = new StyleableDoubleProperty(2.0d) { // from class: com.sun.javafx.scene.control.skin.ProgressBarSkin.4
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "indeterminateBarAnimationTime";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_ANIMATION_TIME;
                }
            };
        }
        return this.indeterminateBarAnimationTime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getIndeterminateBarAnimationTime() {
        if (this.indeterminateBarAnimationTime == null) {
            return 2.0d;
        }
        return this.indeterminateBarAnimationTime.get();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ProgressBarSkin(ProgressBar control) {
        super(control);
        this.indeterminateBarLength = null;
        this.indeterminateBarEscape = null;
        this.indeterminateBarFlip = null;
        this.indeterminateBarAnimationTime = null;
        this.wasIndeterminate = false;
        this.barWidth = ((((int) ((control.getWidth() - snappedLeftInset()) - snappedRightInset())) * 2) * Math.min(1.0d, Math.max(0.0d, control.getProgress()))) / 2.0d;
        InvalidationListener listener = valueModel -> {
            updateProgress();
        };
        control.widthProperty().addListener(listener);
        initialize();
        ((ProgressIndicator) getSkinnable()).requestLayout();
    }

    @Override // com.sun.javafx.scene.control.skin.ProgressIndicatorSkin
    protected void initialize() {
        this.track = new StackPane();
        this.track.getStyleClass().setAll("track");
        this.bar = new StackPane();
        this.bar.getStyleClass().setAll("bar");
        getChildren().setAll(this.track, this.bar);
        this.clipRegion = new Region();
        this.bar.backgroundProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.getFills().isEmpty()) {
                BackgroundFill[] fills = new BackgroundFill[newValue.getFills().size()];
                for (int i2 = 0; i2 < newValue.getFills().size(); i2++) {
                    BackgroundFill bf2 = newValue.getFills().get(i2);
                    fills[i2] = new BackgroundFill(Color.BLACK, bf2.getRadii(), bf2.getInsets());
                }
                this.clipRegion.setBackground(new Background(fills));
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ProgressIndicatorSkin
    protected void createIndeterminateTimeline() {
        if (this.indeterminateTransition != null) {
            this.indeterminateTransition.stop();
        }
        ProgressIndicator control = (ProgressIndicator) getSkinnable();
        double w2 = control.getWidth() - (snappedLeftInset() + snappedRightInset());
        double startX = getIndeterminateBarEscape().booleanValue() ? -getIndeterminateBarLength().doubleValue() : 0.0d;
        double endX = getIndeterminateBarEscape().booleanValue() ? w2 : w2 - getIndeterminateBarLength().doubleValue();
        this.indeterminateTransition = new IndeterminateTransition(startX, endX, this);
        this.indeterminateTransition.setCycleCount(-1);
        this.clipRegion.translateXProperty().bind(new When(this.bar.scaleXProperty().isEqualTo(-1.0d, 1.0E-100d)).then((ObservableNumberValue) this.bar.translateXProperty().subtract(w2).add((ObservableNumberValue) indeterminateBarLengthProperty())).otherwise(this.bar.translateXProperty().negate()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ProgressIndicatorSkin
    protected void updateProgress() {
        ProgressIndicator control = (ProgressIndicator) getSkinnable();
        boolean isIndeterminate = control.isIndeterminate();
        if (!isIndeterminate || !this.wasIndeterminate) {
            this.barWidth = ((((int) ((control.getWidth() - snappedLeftInset()) - snappedRightInset())) * 2) * Math.min(1.0d, Math.max(0.0d, control.getProgress()))) / 2.0d;
            ((ProgressIndicator) getSkinnable()).requestLayout();
        }
        this.wasIndeterminate = isIndeterminate;
    }

    @Override // javafx.scene.control.SkinBase
    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        return Double.NEGATIVE_INFINITY;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Math.max(100.0d, leftInset + this.bar.prefWidth(((ProgressIndicator) getSkinnable()).getWidth()) + rightInset);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + this.bar.prefHeight(width) + bottomInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((ProgressIndicator) getSkinnable()).prefWidth(height);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((ProgressIndicator) getSkinnable()).prefHeight(width);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ProgressIndicatorSkin, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        ProgressIndicator control = (ProgressIndicator) getSkinnable();
        boolean isIndeterminate = control.isIndeterminate();
        this.clipRegion.resizeRelocate(0.0d, 0.0d, w2, h2);
        this.track.resizeRelocate(x2, y2, w2, h2);
        this.bar.resizeRelocate(x2, y2, isIndeterminate ? getIndeterminateBarLength().doubleValue() : this.barWidth, h2);
        this.track.setVisible(true);
        if (isIndeterminate) {
            createIndeterminateTimeline();
            if (((ProgressIndicator) getSkinnable()).impl_isTreeVisible()) {
                this.indeterminateTransition.play();
            }
            this.bar.setClip(this.clipRegion);
            return;
        }
        if (this.indeterminateTransition != null) {
            this.indeterminateTransition.stop();
            this.indeterminateTransition = null;
            this.bar.setClip(null);
            this.bar.setScaleX(1.0d);
            this.bar.setTranslateX(0.0d);
            this.clipRegion.translateXProperty().unbind();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ProgressBarSkin$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<ProgressBar, Number> INDETERMINATE_BAR_LENGTH = new CssMetaData<ProgressBar, Number>("-fx-indeterminate-bar-length", SizeConverter.getInstance(), Double.valueOf(60.0d)) { // from class: com.sun.javafx.scene.control.skin.ProgressBarSkin.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ProgressBar n2) {
                ProgressBarSkin skin = (ProgressBarSkin) n2.getSkin();
                return skin.indeterminateBarLength == null || !skin.indeterminateBarLength.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ProgressBar n2) {
                ProgressBarSkin skin = (ProgressBarSkin) n2.getSkin();
                return (StyleableProperty) skin.indeterminateBarLengthProperty();
            }
        };
        private static final CssMetaData<ProgressBar, Boolean> INDETERMINATE_BAR_ESCAPE = new CssMetaData<ProgressBar, Boolean>("-fx-indeterminate-bar-escape", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: com.sun.javafx.scene.control.skin.ProgressBarSkin.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ProgressBar n2) {
                ProgressBarSkin skin = (ProgressBarSkin) n2.getSkin();
                return skin.indeterminateBarEscape == null || !skin.indeterminateBarEscape.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(ProgressBar n2) {
                ProgressBarSkin skin = (ProgressBarSkin) n2.getSkin();
                return (StyleableProperty) skin.indeterminateBarEscapeProperty();
            }
        };
        private static final CssMetaData<ProgressBar, Boolean> INDETERMINATE_BAR_FLIP = new CssMetaData<ProgressBar, Boolean>("-fx-indeterminate-bar-flip", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: com.sun.javafx.scene.control.skin.ProgressBarSkin.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ProgressBar n2) {
                ProgressBarSkin skin = (ProgressBarSkin) n2.getSkin();
                return skin.indeterminateBarFlip == null || !skin.indeterminateBarFlip.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(ProgressBar n2) {
                ProgressBarSkin skin = (ProgressBarSkin) n2.getSkin();
                return (StyleableProperty) skin.indeterminateBarFlipProperty();
            }
        };
        private static final CssMetaData<ProgressBar, Number> INDETERMINATE_BAR_ANIMATION_TIME = new CssMetaData<ProgressBar, Number>("-fx-indeterminate-bar-animation-time", SizeConverter.getInstance(), Double.valueOf(2.0d)) { // from class: com.sun.javafx.scene.control.skin.ProgressBarSkin.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ProgressBar n2) {
                ProgressBarSkin skin = (ProgressBarSkin) n2.getSkin();
                return skin.indeterminateBarAnimationTime == null || !skin.indeterminateBarAnimationTime.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ProgressBar n2) {
                ProgressBarSkin skin = (ProgressBarSkin) n2.getSkin();
                return (StyleableProperty) skin.indeterminateBarAnimationTimeProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(SkinBase.getClassCssMetaData());
            styleables.add(INDETERMINATE_BAR_LENGTH);
            styleables.add(INDETERMINATE_BAR_ESCAPE);
            styleables.add(INDETERMINATE_BAR_FLIP);
            styleables.add(INDETERMINATE_BAR_ANIMATION_TIME);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // com.sun.javafx.scene.control.skin.ProgressIndicatorSkin, javafx.scene.control.SkinBase
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ProgressBarSkin$IndeterminateTransition.class */
    private static class IndeterminateTransition extends Transition {
        private final WeakReference<ProgressBarSkin> skin;
        private final double startX;
        private final double endX;
        private final boolean flip;

        public IndeterminateTransition(double startX, double endX, ProgressBarSkin progressBarSkin) {
            this.startX = startX;
            this.endX = endX;
            this.skin = new WeakReference<>(progressBarSkin);
            this.flip = progressBarSkin.getIndeterminateBarFlip().booleanValue();
            progressBarSkin.getIndeterminateBarEscape();
            setCycleDuration(Duration.seconds(progressBarSkin.getIndeterminateBarAnimationTime() * (this.flip ? 2 : 1)));
        }

        @Override // javafx.animation.Transition
        protected void interpolate(double frac) {
            ProgressBarSkin s2 = this.skin.get();
            if (s2 == null) {
                stop();
                return;
            }
            if (frac <= 0.5d || !this.flip) {
                s2.bar.setScaleX(-1.0d);
                s2.bar.setTranslateX(this.startX + ((this.flip ? 2 : 1) * frac * (this.endX - this.startX)));
            } else {
                s2.bar.setScaleX(1.0d);
                s2.bar.setTranslateX(this.startX + (2.0d * (1.0d - frac) * (this.endX - this.startX)));
            }
        }
    }
}
