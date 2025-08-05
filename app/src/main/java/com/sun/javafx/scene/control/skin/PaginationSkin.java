package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.PaginationBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/PaginationSkin.class */
public class PaginationSkin extends BehaviorSkinBase<Pagination, PaginationBehavior> {
    private static final double SWIPE_THRESHOLD = 0.3d;
    private static final double TOUCH_THRESHOLD = 15.0d;
    private Pagination pagination;
    private StackPane currentStackPane;
    private StackPane nextStackPane;
    private Timeline timeline;
    private Rectangle clipRect;
    private NavigationControl navigation;
    private int fromIndex;
    private int previousIndex;
    private int currentIndex;
    private int toIndex;
    private int pageCount;
    private int maxPageIndicatorCount;
    private boolean animate;
    private double startTouchPos;
    private double lastTouchPos;
    private long startTouchTime;
    private long lastTouchTime;
    private double touchVelocity;
    private boolean touchThresholdBroken;
    private int touchEventId;
    private boolean nextPageReached;
    private boolean setInitialDirection;
    private int direction;
    private int currentAnimatedIndex;
    private boolean hasPendingAnimation;
    private EventHandler<ActionEvent> swipeAnimationEndEventHandler;
    private EventHandler<ActionEvent> clampAnimationEndEventHandler;
    private final DoubleProperty arrowButtonGap;
    private BooleanProperty arrowsVisible;
    private BooleanProperty pageInformationVisible;
    private ObjectProperty<Side> pageInformationAlignment;
    private BooleanProperty tooltipVisible;
    private static final Duration DURATION = new Duration(125.0d);
    private static final Interpolator interpolator = Interpolator.SPLINE(0.4829d, 0.5709d, 0.6803d, 0.9928d);
    private static final Boolean DEFAULT_ARROW_VISIBLE = Boolean.FALSE;
    private static final Boolean DEFAULT_PAGE_INFORMATION_VISIBLE = Boolean.FALSE;
    private static final Side DEFAULT_PAGE_INFORMATION_ALIGNMENT = Side.BOTTOM;
    private static final Boolean DEFAULT_TOOLTIP_VISIBLE = Boolean.FALSE;

    /* JADX WARN: Multi-variable type inference failed */
    public PaginationSkin(Pagination pagination) {
        super(pagination, new PaginationBehavior(pagination));
        this.animate = true;
        this.touchEventId = -1;
        this.nextPageReached = false;
        this.setInitialDirection = false;
        this.hasPendingAnimation = false;
        this.swipeAnimationEndEventHandler = new EventHandler<ActionEvent>() { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.1
            @Override // javafx.event.EventHandler
            public void handle(ActionEvent t2) {
                PaginationSkin.this.swapPanes();
                PaginationSkin.this.timeline = null;
                if (PaginationSkin.this.hasPendingAnimation) {
                    PaginationSkin.this.animateSwitchPage();
                    PaginationSkin.this.hasPendingAnimation = false;
                }
            }
        };
        this.clampAnimationEndEventHandler = new EventHandler<ActionEvent>() { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.2
            @Override // javafx.event.EventHandler
            public void handle(ActionEvent t2) {
                PaginationSkin.this.currentStackPane.setTranslateX(0.0d);
                PaginationSkin.this.nextStackPane.setTranslateX(0.0d);
                PaginationSkin.this.nextStackPane.setVisible(false);
                PaginationSkin.this.timeline = null;
            }
        };
        this.arrowButtonGap = new StyleableDoubleProperty(60.0d) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.3
            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return PaginationSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "arrowButtonGap";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.ARROW_BUTTON_GAP;
            }
        };
        this.clipRect = new Rectangle();
        ((Pagination) getSkinnable()).setClip(this.clipRect);
        this.pagination = pagination;
        this.currentStackPane = new StackPane();
        this.currentStackPane.getStyleClass().add("page");
        this.nextStackPane = new StackPane();
        this.nextStackPane.getStyleClass().add("page");
        this.nextStackPane.setVisible(false);
        resetIndexes(true);
        this.navigation = new NavigationControl();
        getChildren().addAll(this.currentStackPane, this.nextStackPane, this.navigation);
        pagination.maxPageIndicatorCountProperty().addListener(o2 -> {
            resetIndiciesAndNav();
        });
        registerChangeListener(pagination.widthProperty(), "WIDTH");
        registerChangeListener(pagination.heightProperty(), "HEIGHT");
        registerChangeListener(pagination.pageCountProperty(), "PAGE_COUNT");
        registerChangeListener(pagination.pageFactoryProperty(), "PAGE_FACTORY");
        initializeSwipeAndTouchHandlers();
    }

    protected void resetIndiciesAndNav() {
        resetIndexes(false);
        this.navigation.initializePageIndicators();
        this.navigation.updatePageIndicators();
    }

    public void selectNext() {
        if (getCurrentPageIndex() < getPageCount() - 1) {
            this.pagination.setCurrentPageIndex(getCurrentPageIndex() + 1);
        }
    }

    public void selectPrevious() {
        if (getCurrentPageIndex() > 0) {
            this.pagination.setCurrentPageIndex(getCurrentPageIndex() - 1);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initializeSwipeAndTouchHandlers() {
        Pagination control = (Pagination) getSkinnable();
        ((Pagination) getSkinnable()).addEventHandler(TouchEvent.TOUCH_PRESSED, e2 -> {
            if (this.touchEventId == -1) {
                this.touchEventId = e2.getTouchPoint().getId();
            }
            if (this.touchEventId != e2.getTouchPoint().getId()) {
                return;
            }
            double x2 = e2.getTouchPoint().getX();
            this.startTouchPos = x2;
            this.lastTouchPos = x2;
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.startTouchTime = jCurrentTimeMillis;
            this.lastTouchTime = jCurrentTimeMillis;
            this.touchThresholdBroken = false;
            e2.consume();
        });
        ((Pagination) getSkinnable()).addEventHandler(TouchEvent.TOUCH_MOVED, e3 -> {
            double currentPaneX;
            double nextPaneX;
            double currentPaneX2;
            double nextPaneX2;
            if (this.touchEventId != e3.getTouchPoint().getId()) {
                return;
            }
            double drag = e3.getTouchPoint().getX() - this.lastTouchPos;
            long time = System.currentTimeMillis() - this.lastTouchTime;
            this.touchVelocity = drag / time;
            this.lastTouchPos = e3.getTouchPoint().getX();
            this.lastTouchTime = System.currentTimeMillis();
            double delta = e3.getTouchPoint().getX() - this.startTouchPos;
            if (!this.touchThresholdBroken && Math.abs(delta) > TOUCH_THRESHOLD) {
                this.touchThresholdBroken = true;
            }
            if (this.touchThresholdBroken) {
                double width = control.getWidth() - (snappedLeftInset() + snappedRightInset());
                if (!this.setInitialDirection) {
                    this.setInitialDirection = true;
                    this.direction = delta < 0.0d ? 1 : -1;
                }
                if (delta < 0.0d) {
                    if (this.direction == -1) {
                        this.nextStackPane.getChildren().clear();
                        this.direction = 1;
                    }
                    if (Math.abs(delta) <= width) {
                        currentPaneX2 = delta;
                        nextPaneX2 = width + delta;
                        this.nextPageReached = false;
                    } else {
                        currentPaneX2 = -width;
                        nextPaneX2 = 0.0d;
                        this.nextPageReached = true;
                    }
                    this.currentStackPane.setTranslateX(currentPaneX2);
                    if (getCurrentPageIndex() < getPageCount() - 1) {
                        createPage(this.nextStackPane, this.currentIndex + 1);
                        this.nextStackPane.setVisible(true);
                        this.nextStackPane.setTranslateX(nextPaneX2);
                    } else {
                        this.currentStackPane.setTranslateX(0.0d);
                    }
                } else {
                    if (this.direction == 1) {
                        this.nextStackPane.getChildren().clear();
                        this.direction = -1;
                    }
                    if (Math.abs(delta) <= width) {
                        currentPaneX = delta;
                        nextPaneX = (-width) + delta;
                        this.nextPageReached = false;
                    } else {
                        currentPaneX = width;
                        nextPaneX = 0.0d;
                        this.nextPageReached = true;
                    }
                    this.currentStackPane.setTranslateX(currentPaneX);
                    if (getCurrentPageIndex() != 0) {
                        createPage(this.nextStackPane, this.currentIndex - 1);
                        this.nextStackPane.setVisible(true);
                        this.nextStackPane.setTranslateX(nextPaneX);
                    } else {
                        this.currentStackPane.setTranslateX(0.0d);
                    }
                }
            }
            e3.consume();
        });
        ((Pagination) getSkinnable()).addEventHandler(TouchEvent.TOUCH_RELEASED, e4 -> {
            if (this.touchEventId != e4.getTouchPoint().getId()) {
                return;
            }
            this.touchEventId = -1;
            this.setInitialDirection = false;
            if (this.touchThresholdBroken) {
                double drag = e4.getTouchPoint().getX() - this.startTouchPos;
                long time = System.currentTimeMillis() - this.startTouchTime;
                boolean quick = time < 300;
                double velocity = quick ? drag / time : this.touchVelocity;
                double distance = velocity * 500.0d;
                double width = control.getWidth() - (snappedLeftInset() + snappedRightInset());
                double threshold = Math.abs(distance / width);
                double delta = Math.abs(drag / width);
                if (threshold <= 0.3d && delta <= 0.3d) {
                    animateClamping(this.startTouchPos > e4.getTouchPoint().getSceneX());
                } else if (this.startTouchPos > e4.getTouchPoint().getX()) {
                    selectNext();
                } else {
                    selectPrevious();
                }
            }
            e4.consume();
        });
    }

    private void resetIndexes(boolean usePageIndex) {
        this.maxPageIndicatorCount = getMaxPageIndicatorCount();
        this.pageCount = getPageCount();
        if (this.pageCount > this.maxPageIndicatorCount) {
            this.pageCount = this.maxPageIndicatorCount;
        }
        this.fromIndex = 0;
        this.previousIndex = 0;
        this.currentIndex = usePageIndex ? getCurrentPageIndex() : 0;
        this.toIndex = this.pageCount - 1;
        if (this.pageCount == Integer.MAX_VALUE && this.maxPageIndicatorCount == Integer.MAX_VALUE) {
            this.toIndex = 0;
        }
        boolean isAnimate = this.animate;
        if (isAnimate) {
            this.animate = false;
        }
        this.currentStackPane.getChildren().clear();
        this.nextStackPane.getChildren().clear();
        this.pagination.setCurrentPageIndex(this.currentIndex);
        createPage(this.currentStackPane, this.currentIndex);
        if (isAnimate) {
            this.animate = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean createPage(StackPane pane, int index) {
        if (this.pagination.getPageFactory() != null && pane.getChildren().isEmpty()) {
            Node content = this.pagination.getPageFactory().call(Integer.valueOf(index));
            if (content != null) {
                pane.getChildren().setAll(content);
                return true;
            }
            boolean isAnimate = this.animate;
            if (isAnimate) {
                this.animate = false;
            }
            if (this.pagination.getPageFactory().call(Integer.valueOf(this.previousIndex)) != null) {
                this.pagination.setCurrentPageIndex(this.previousIndex);
            } else {
                this.pagination.setCurrentPageIndex(0);
            }
            if (isAnimate) {
                this.animate = true;
                return false;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public int getPageCount() {
        if (((Pagination) getSkinnable()).getPageCount() < 1) {
            return 1;
        }
        return ((Pagination) getSkinnable()).getPageCount();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public int getMaxPageIndicatorCount() {
        return ((Pagination) getSkinnable()).getMaxPageIndicatorCount();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public int getCurrentPageIndex() {
        return ((Pagination) getSkinnable()).getCurrentPageIndex();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateSwitchPage() {
        if (this.timeline != null) {
            this.timeline.setRate(8.0d);
            this.hasPendingAnimation = true;
        } else {
            if (!this.nextStackPane.isVisible() && !createPage(this.nextStackPane, this.currentAnimatedIndex)) {
                return;
            }
            if (this.nextPageReached) {
                swapPanes();
                this.nextPageReached = false;
            } else {
                this.nextStackPane.setCache(true);
                this.currentStackPane.setCache(true);
                Platform.runLater(() -> {
                    boolean useTranslateX = this.nextStackPane.getTranslateX() != 0.0d;
                    if (this.currentAnimatedIndex > this.previousIndex) {
                        if (!useTranslateX) {
                            this.nextStackPane.setTranslateX(this.currentStackPane.getWidth());
                        }
                        this.nextStackPane.setVisible(true);
                        this.timeline = new Timeline();
                        Duration durationMillis = Duration.millis(0.0d);
                        KeyValue[] keyValueArr = new KeyValue[2];
                        keyValueArr[0] = new KeyValue(this.currentStackPane.translateXProperty(), Double.valueOf(useTranslateX ? this.currentStackPane.getTranslateX() : 0.0d), interpolator);
                        keyValueArr[1] = new KeyValue(this.nextStackPane.translateXProperty(), Double.valueOf(useTranslateX ? this.nextStackPane.getTranslateX() : this.currentStackPane.getWidth()), interpolator);
                        KeyFrame k1 = new KeyFrame(durationMillis, keyValueArr);
                        KeyFrame k2 = new KeyFrame(DURATION, this.swipeAnimationEndEventHandler, new KeyValue(this.currentStackPane.translateXProperty(), Double.valueOf(-this.currentStackPane.getWidth()), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), 0, interpolator));
                        this.timeline.getKeyFrames().setAll(k1, k2);
                        this.timeline.play();
                        return;
                    }
                    if (!useTranslateX) {
                        this.nextStackPane.setTranslateX(-this.currentStackPane.getWidth());
                    }
                    this.nextStackPane.setVisible(true);
                    this.timeline = new Timeline();
                    Duration durationMillis2 = Duration.millis(0.0d);
                    KeyValue[] keyValueArr2 = new KeyValue[2];
                    keyValueArr2[0] = new KeyValue(this.currentStackPane.translateXProperty(), Double.valueOf(useTranslateX ? this.currentStackPane.getTranslateX() : 0.0d), interpolator);
                    keyValueArr2[1] = new KeyValue(this.nextStackPane.translateXProperty(), Double.valueOf(useTranslateX ? this.nextStackPane.getTranslateX() : -this.currentStackPane.getWidth()), interpolator);
                    KeyFrame k12 = new KeyFrame(durationMillis2, keyValueArr2);
                    KeyFrame k22 = new KeyFrame(DURATION, this.swipeAnimationEndEventHandler, new KeyValue(this.currentStackPane.translateXProperty(), Double.valueOf(this.currentStackPane.getWidth()), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), 0, interpolator));
                    this.timeline.getKeyFrames().setAll(k12, k22);
                    this.timeline.play();
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void swapPanes() {
        StackPane temp = this.currentStackPane;
        this.currentStackPane = this.nextStackPane;
        this.nextStackPane = temp;
        this.currentStackPane.setTranslateX(0.0d);
        this.currentStackPane.setCache(false);
        this.nextStackPane.setTranslateX(0.0d);
        this.nextStackPane.setCache(false);
        this.nextStackPane.setVisible(false);
        this.nextStackPane.getChildren().clear();
    }

    private void animateClamping(boolean rightToLeft) {
        if (rightToLeft) {
            this.timeline = new Timeline();
            KeyFrame k1 = new KeyFrame(Duration.millis(0.0d), new KeyValue(this.currentStackPane.translateXProperty(), Double.valueOf(this.currentStackPane.getTranslateX()), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), Double.valueOf(this.nextStackPane.getTranslateX()), interpolator));
            KeyFrame k2 = new KeyFrame(DURATION, this.clampAnimationEndEventHandler, new KeyValue(this.currentStackPane.translateXProperty(), 0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), Double.valueOf(this.currentStackPane.getWidth()), interpolator));
            this.timeline.getKeyFrames().setAll(k1, k2);
            this.timeline.play();
            return;
        }
        this.timeline = new Timeline();
        KeyFrame k12 = new KeyFrame(Duration.millis(0.0d), new KeyValue(this.currentStackPane.translateXProperty(), Double.valueOf(this.currentStackPane.getTranslateX()), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), Double.valueOf(this.nextStackPane.getTranslateX()), interpolator));
        KeyFrame k22 = new KeyFrame(DURATION, this.clampAnimationEndEventHandler, new KeyValue(this.currentStackPane.translateXProperty(), 0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), Double.valueOf(-this.currentStackPane.getWidth()), interpolator));
        this.timeline.getKeyFrames().setAll(k12, k22);
        this.timeline.play();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DoubleProperty arrowButtonGapProperty() {
        return this.arrowButtonGap;
    }

    public final void setArrowsVisible(boolean value) {
        arrowsVisibleProperty().set(value);
    }

    public final boolean isArrowsVisible() {
        return this.arrowsVisible == null ? DEFAULT_ARROW_VISIBLE.booleanValue() : this.arrowsVisible.get();
    }

    public final BooleanProperty arrowsVisibleProperty() {
        if (this.arrowsVisible == null) {
            this.arrowsVisible = new StyleableBooleanProperty(DEFAULT_ARROW_VISIBLE.booleanValue()) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.4
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    ((Pagination) PaginationSkin.this.getSkinnable()).requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.ARROWS_VISIBLE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PaginationSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "arrowVisible";
                }
            };
        }
        return this.arrowsVisible;
    }

    public final void setPageInformationVisible(boolean value) {
        pageInformationVisibleProperty().set(value);
    }

    public final boolean isPageInformationVisible() {
        return this.pageInformationVisible == null ? DEFAULT_PAGE_INFORMATION_VISIBLE.booleanValue() : this.pageInformationVisible.get();
    }

    public final BooleanProperty pageInformationVisibleProperty() {
        if (this.pageInformationVisible == null) {
            this.pageInformationVisible = new StyleableBooleanProperty(DEFAULT_PAGE_INFORMATION_VISIBLE.booleanValue()) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.5
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    ((Pagination) PaginationSkin.this.getSkinnable()).requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.PAGE_INFORMATION_VISIBLE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PaginationSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pageInformationVisible";
                }
            };
        }
        return this.pageInformationVisible;
    }

    public final void setPageInformationAlignment(Side value) {
        pageInformationAlignmentProperty().set(value);
    }

    public final Side getPageInformationAlignment() {
        return this.pageInformationAlignment == null ? DEFAULT_PAGE_INFORMATION_ALIGNMENT : this.pageInformationAlignment.get();
    }

    public final ObjectProperty<Side> pageInformationAlignmentProperty() {
        if (this.pageInformationAlignment == null) {
            this.pageInformationAlignment = new StyleableObjectProperty<Side>(Side.BOTTOM) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.6
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ((Pagination) PaginationSkin.this.getSkinnable()).requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Pagination, Side> getCssMetaData() {
                    return StyleableProperties.PAGE_INFORMATION_ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PaginationSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pageInformationAlignment";
                }
            };
        }
        return this.pageInformationAlignment;
    }

    public final void setTooltipVisible(boolean value) {
        tooltipVisibleProperty().set(value);
    }

    public final boolean isTooltipVisible() {
        return this.tooltipVisible == null ? DEFAULT_TOOLTIP_VISIBLE.booleanValue() : this.tooltipVisible.get();
    }

    public final BooleanProperty tooltipVisibleProperty() {
        if (this.tooltipVisible == null) {
            this.tooltipVisible = new StyleableBooleanProperty(DEFAULT_TOOLTIP_VISIBLE.booleanValue()) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.7
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    ((Pagination) PaginationSkin.this.getSkinnable()).requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.TOOLTIP_VISIBLE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PaginationSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tooltipVisible";
                }
            };
        }
        return this.tooltipVisible;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("PAGE_FACTORY".equals(p2)) {
            if (this.animate && this.timeline != null) {
                this.timeline.setRate(8.0d);
                this.timeline.setOnFinished(arg0 -> {
                    resetIndiciesAndNav();
                });
                return;
            }
            resetIndiciesAndNav();
        } else if ("PAGE_COUNT".equals(p2)) {
            resetIndiciesAndNav();
        } else if ("WIDTH".equals(p2)) {
            this.clipRect.setWidth(((Pagination) getSkinnable()).getWidth());
        } else if ("HEIGHT".equals(p2)) {
            this.clipRect.setHeight(((Pagination) getSkinnable()).getHeight());
        }
        ((Pagination) getSkinnable()).requestLayout();
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double navigationWidth = this.navigation.isVisible() ? snapSize(this.navigation.minWidth(height)) : 0.0d;
        return leftInset + Math.max(this.currentStackPane.minWidth(height), navigationWidth) + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double navigationHeight = this.navigation.isVisible() ? snapSize(this.navigation.minHeight(width)) : 0.0d;
        return topInset + this.currentStackPane.minHeight(width) + navigationHeight + bottomInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double navigationWidth = this.navigation.isVisible() ? snapSize(this.navigation.prefWidth(height)) : 0.0d;
        return leftInset + Math.max(this.currentStackPane.prefWidth(height), navigationWidth) + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double navigationHeight = this.navigation.isVisible() ? snapSize(this.navigation.prefHeight(width)) : 0.0d;
        return topInset + this.currentStackPane.prefHeight(width) + navigationHeight + bottomInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double navigationHeight = this.navigation.isVisible() ? snapSize(this.navigation.prefHeight(-1.0d)) : 0.0d;
        double stackPaneHeight = snapSize(h2 - navigationHeight);
        layoutInArea(this.currentStackPane, x2, y2, w2, stackPaneHeight, 0.0d, HPos.CENTER, VPos.CENTER);
        layoutInArea(this.nextStackPane, x2, y2, w2, stackPaneHeight, 0.0d, HPos.CENTER, VPos.CENTER);
        layoutInArea(this.navigation, x2, stackPaneHeight, w2, navigationHeight, 0.0d, HPos.CENTER, VPos.CENTER);
    }

    @Override // javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case FOCUS_ITEM:
                return this.navigation.indicatorButtons.getSelectedToggle();
            case ITEM_COUNT:
                return Integer.valueOf(this.navigation.indicatorButtons.getToggles().size());
            case ITEM_AT_INDEX:
                Integer index = (Integer) parameters[0];
                if (index == null) {
                    return null;
                }
                return this.navigation.indicatorButtons.getToggles().get(index.intValue());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/PaginationSkin$NavigationControl.class */
    class NavigationControl extends StackPane {
        private HBox controlBox;
        private Button leftArrowButton;
        private StackPane leftArrow;
        private Button rightArrowButton;
        private StackPane rightArrow;
        private ToggleGroup indicatorButtons;
        private Label pageInformation;
        private double minButtonSize;
        private double previousWidth = -1.0d;
        private int previousIndicatorCount = 0;

        public NavigationControl() {
            this.minButtonSize = -1.0d;
            getStyleClass().setAll("pagination-control");
            addEventHandler(MouseEvent.MOUSE_PRESSED, e2 -> {
                PaginationSkin.this.getBehavior().mousePressed(e2);
            });
            addEventHandler(MouseEvent.MOUSE_RELEASED, e3 -> {
                PaginationSkin.this.getBehavior().mouseReleased(e3);
            });
            addEventHandler(MouseEvent.MOUSE_ENTERED, e4 -> {
                PaginationSkin.this.getBehavior().mouseEntered(e4);
            });
            addEventHandler(MouseEvent.MOUSE_EXITED, e5 -> {
                PaginationSkin.this.getBehavior().mouseExited(e5);
            });
            this.controlBox = new HBox();
            this.controlBox.getStyleClass().add("control-box");
            this.leftArrowButton = new Button();
            this.leftArrowButton.setAccessibleText(ControlResources.getString("Accessibility.title.Pagination.PreviousButton"));
            this.minButtonSize = this.leftArrowButton.getFont().getSize() * 2.0d;
            this.leftArrowButton.fontProperty().addListener((arg0, arg1, newFont) -> {
                this.minButtonSize = newFont.getSize() * 2.0d;
                for (Node child : this.controlBox.getChildren()) {
                    ((Control) child).setMinSize(this.minButtonSize, this.minButtonSize);
                }
                requestLayout();
            });
            this.leftArrowButton.setMinSize(this.minButtonSize, this.minButtonSize);
            this.leftArrowButton.prefWidthProperty().bind(this.leftArrowButton.minWidthProperty());
            this.leftArrowButton.prefHeightProperty().bind(this.leftArrowButton.minHeightProperty());
            this.leftArrowButton.getStyleClass().add("left-arrow-button");
            this.leftArrowButton.setFocusTraversable(false);
            HBox.setMargin(this.leftArrowButton, new Insets(0.0d, snapSize(PaginationSkin.this.arrowButtonGap.get()), 0.0d, 0.0d));
            this.leftArrow = new StackPane();
            this.leftArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
            this.leftArrowButton.setGraphic(this.leftArrow);
            this.leftArrow.getStyleClass().add("left-arrow");
            this.rightArrowButton = new Button();
            this.rightArrowButton.setAccessibleText(ControlResources.getString("Accessibility.title.Pagination.NextButton"));
            this.rightArrowButton.setMinSize(this.minButtonSize, this.minButtonSize);
            this.rightArrowButton.prefWidthProperty().bind(this.rightArrowButton.minWidthProperty());
            this.rightArrowButton.prefHeightProperty().bind(this.rightArrowButton.minHeightProperty());
            this.rightArrowButton.getStyleClass().add("right-arrow-button");
            this.rightArrowButton.setFocusTraversable(false);
            HBox.setMargin(this.rightArrowButton, new Insets(0.0d, 0.0d, 0.0d, snapSize(PaginationSkin.this.arrowButtonGap.get())));
            this.rightArrow = new StackPane();
            this.rightArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
            this.rightArrowButton.setGraphic(this.rightArrow);
            this.rightArrow.getStyleClass().add("right-arrow");
            this.indicatorButtons = new ToggleGroup();
            this.pageInformation = new Label();
            this.pageInformation.getStyleClass().add("page-information");
            getChildren().addAll(this.controlBox, this.pageInformation);
            initializeNavigationHandlers();
            initializePageIndicators();
            updatePageIndex();
            PaginationSkin.this.arrowButtonGap.addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() == 0.0d) {
                    HBox.setMargin(this.leftArrowButton, null);
                    HBox.setMargin(this.rightArrowButton, null);
                } else {
                    HBox.setMargin(this.leftArrowButton, new Insets(0.0d, snapSize(newValue.doubleValue()), 0.0d, 0.0d));
                    HBox.setMargin(this.rightArrowButton, new Insets(0.0d, 0.0d, 0.0d, snapSize(newValue.doubleValue())));
                }
            });
        }

        private void initializeNavigationHandlers() {
            this.leftArrowButton.setOnAction(arg0 -> {
                PaginationSkin.this.selectPrevious();
                requestLayout();
            });
            this.rightArrowButton.setOnAction(arg02 -> {
                PaginationSkin.this.selectNext();
                requestLayout();
            });
            PaginationSkin.this.pagination.currentPageIndexProperty().addListener((arg03, arg1, arg2) -> {
                PaginationSkin.this.previousIndex = arg1.intValue();
                PaginationSkin.this.currentIndex = arg2.intValue();
                updatePageIndex();
                if (!PaginationSkin.this.animate) {
                    PaginationSkin.this.createPage(PaginationSkin.this.currentStackPane, PaginationSkin.this.currentIndex);
                    return;
                }
                PaginationSkin.this.currentAnimatedIndex = PaginationSkin.this.currentIndex;
                PaginationSkin.this.animateSwitchPage();
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initializePageIndicators() {
            this.previousIndicatorCount = 0;
            this.controlBox.getChildren().clear();
            clearIndicatorButtons();
            this.controlBox.getChildren().add(this.leftArrowButton);
            for (int i2 = PaginationSkin.this.fromIndex; i2 <= PaginationSkin.this.toIndex; i2++) {
                IndicatorButton ib = PaginationSkin.this.new IndicatorButton(i2);
                ib.setMinSize(this.minButtonSize, this.minButtonSize);
                ib.setToggleGroup(this.indicatorButtons);
                this.controlBox.getChildren().add(ib);
            }
            this.controlBox.getChildren().add(this.rightArrowButton);
        }

        private void clearIndicatorButtons() {
            for (Toggle toggle : this.indicatorButtons.getToggles()) {
                if (toggle instanceof IndicatorButton) {
                    IndicatorButton indicatorButton = (IndicatorButton) toggle;
                    indicatorButton.release();
                }
            }
            this.indicatorButtons.getToggles().clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public void updatePageIndicators() {
            int i2 = 0;
            while (true) {
                if (i2 >= this.indicatorButtons.getToggles().size()) {
                    break;
                }
                IndicatorButton ib = (IndicatorButton) this.indicatorButtons.getToggles().get(i2);
                if (ib.getPageNumber() != PaginationSkin.this.currentIndex) {
                    i2++;
                } else {
                    ib.setSelected(true);
                    updatePageInformation();
                    break;
                }
            }
            ((Pagination) PaginationSkin.this.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        private void updatePageIndex() {
            if (PaginationSkin.this.pageCount == PaginationSkin.this.maxPageIndicatorCount && changePageSet()) {
                initializePageIndicators();
            }
            updatePageIndicators();
            requestLayout();
        }

        private void updatePageInformation() {
            String currentPageNumber = Integer.toString(PaginationSkin.this.currentIndex + 1);
            String lastPageNumber = PaginationSkin.this.getPageCount() == Integer.MAX_VALUE ? "..." : Integer.toString(PaginationSkin.this.getPageCount());
            this.pageInformation.setText(currentPageNumber + "/" + lastPageNumber);
        }

        private void layoutPageIndicators() {
            int lastIndicatorButtonIndex;
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double width = snapSize(getWidth()) - (left + right);
            double controlBoxleft = this.controlBox.snappedLeftInset();
            double controlBoxRight = this.controlBox.snappedRightInset();
            double leftArrowWidth = snapSize(Utils.boundedSize(this.leftArrowButton.prefWidth(-1.0d), this.leftArrowButton.minWidth(-1.0d), this.leftArrowButton.maxWidth(-1.0d)));
            double rightArrowWidth = snapSize(Utils.boundedSize(this.rightArrowButton.prefWidth(-1.0d), this.rightArrowButton.minWidth(-1.0d), this.rightArrowButton.maxWidth(-1.0d)));
            double spacing = snapSize(this.controlBox.getSpacing());
            double w2 = width - (((((controlBoxleft + leftArrowWidth) + (2.0d * PaginationSkin.this.arrowButtonGap.get())) + spacing) + rightArrowWidth) + controlBoxRight);
            if (PaginationSkin.this.isPageInformationVisible() && (Side.LEFT.equals(PaginationSkin.this.getPageInformationAlignment()) || Side.RIGHT.equals(PaginationSkin.this.getPageInformationAlignment()))) {
                w2 -= snapSize(this.pageInformation.prefWidth(-1.0d));
            }
            double x2 = 0.0d;
            int indicatorCount = 0;
            int i2 = 0;
            while (i2 < PaginationSkin.this.getMaxPageIndicatorCount()) {
                int index = i2 < this.indicatorButtons.getToggles().size() ? i2 : this.indicatorButtons.getToggles().size() - 1;
                double iw = this.minButtonSize;
                if (index != -1) {
                    IndicatorButton ib = (IndicatorButton) this.indicatorButtons.getToggles().get(index);
                    iw = snapSize(Utils.boundedSize(ib.prefWidth(-1.0d), ib.minWidth(-1.0d), ib.maxWidth(-1.0d)));
                }
                x2 += iw + spacing;
                if (x2 > w2) {
                    break;
                }
                indicatorCount++;
                i2++;
            }
            if (indicatorCount == 0) {
                indicatorCount = 1;
            }
            if (indicatorCount != this.previousIndicatorCount) {
                if (indicatorCount < PaginationSkin.this.getMaxPageIndicatorCount()) {
                    PaginationSkin.this.maxPageIndicatorCount = indicatorCount;
                } else {
                    PaginationSkin.this.maxPageIndicatorCount = PaginationSkin.this.getMaxPageIndicatorCount();
                }
                if (PaginationSkin.this.pageCount > PaginationSkin.this.maxPageIndicatorCount) {
                    PaginationSkin.this.pageCount = PaginationSkin.this.maxPageIndicatorCount;
                    lastIndicatorButtonIndex = PaginationSkin.this.maxPageIndicatorCount - 1;
                } else if (indicatorCount > PaginationSkin.this.getPageCount()) {
                    PaginationSkin.this.pageCount = PaginationSkin.this.getPageCount();
                    lastIndicatorButtonIndex = PaginationSkin.this.getPageCount() - 1;
                } else {
                    PaginationSkin.this.pageCount = indicatorCount;
                    lastIndicatorButtonIndex = indicatorCount - 1;
                }
                if (PaginationSkin.this.currentIndex >= PaginationSkin.this.toIndex) {
                    PaginationSkin.this.toIndex = PaginationSkin.this.currentIndex;
                    PaginationSkin.this.fromIndex = PaginationSkin.this.toIndex - lastIndicatorButtonIndex;
                } else if (PaginationSkin.this.currentIndex <= PaginationSkin.this.fromIndex) {
                    PaginationSkin.this.fromIndex = PaginationSkin.this.currentIndex;
                    PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + lastIndicatorButtonIndex;
                } else {
                    PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + lastIndicatorButtonIndex;
                }
                if (PaginationSkin.this.toIndex > PaginationSkin.this.getPageCount() - 1) {
                    PaginationSkin.this.toIndex = PaginationSkin.this.getPageCount() - 1;
                }
                if (PaginationSkin.this.fromIndex < 0) {
                    PaginationSkin.this.fromIndex = 0;
                    PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + lastIndicatorButtonIndex;
                }
                initializePageIndicators();
                updatePageIndicators();
                this.previousIndicatorCount = indicatorCount;
            }
        }

        private boolean changePageSet() {
            int index = indexToIndicatorButtonsIndex(PaginationSkin.this.currentIndex);
            int lastIndicatorButtonIndex = PaginationSkin.this.maxPageIndicatorCount - 1;
            if (PaginationSkin.this.previousIndex >= PaginationSkin.this.currentIndex || index != 0 || lastIndicatorButtonIndex == 0 || index % lastIndicatorButtonIndex != 0) {
                if (PaginationSkin.this.currentIndex >= PaginationSkin.this.previousIndex || index != lastIndicatorButtonIndex || lastIndicatorButtonIndex == 0 || index % lastIndicatorButtonIndex != 0) {
                    if (PaginationSkin.this.currentIndex < PaginationSkin.this.fromIndex || PaginationSkin.this.currentIndex > PaginationSkin.this.toIndex) {
                        PaginationSkin.this.fromIndex = PaginationSkin.this.currentIndex - index;
                        PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + lastIndicatorButtonIndex;
                    } else {
                        return false;
                    }
                } else {
                    PaginationSkin.this.toIndex = PaginationSkin.this.currentIndex;
                    PaginationSkin.this.fromIndex = PaginationSkin.this.toIndex - lastIndicatorButtonIndex;
                }
            } else {
                PaginationSkin.this.fromIndex = PaginationSkin.this.currentIndex;
                PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + lastIndicatorButtonIndex;
            }
            if (PaginationSkin.this.toIndex > PaginationSkin.this.getPageCount() - 1) {
                if (PaginationSkin.this.fromIndex > PaginationSkin.this.getPageCount() - 1) {
                    return false;
                }
                PaginationSkin.this.toIndex = PaginationSkin.this.getPageCount() - 1;
            }
            if (PaginationSkin.this.fromIndex < 0) {
                PaginationSkin.this.fromIndex = 0;
                PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + lastIndicatorButtonIndex;
                return true;
            }
            return true;
        }

        private int indexToIndicatorButtonsIndex(int index) {
            if (index >= PaginationSkin.this.fromIndex && index <= PaginationSkin.this.toIndex) {
                return index - PaginationSkin.this.fromIndex;
            }
            int i2 = 0;
            int from = PaginationSkin.this.fromIndex;
            int to = PaginationSkin.this.toIndex;
            if (PaginationSkin.this.currentIndex > PaginationSkin.this.previousIndex) {
                while (from < PaginationSkin.this.getPageCount() && to < PaginationSkin.this.getPageCount()) {
                    from += i2;
                    to += i2;
                    if (index < from || index > to) {
                        i2 += PaginationSkin.this.maxPageIndicatorCount;
                    } else {
                        if (index == from) {
                            return 0;
                        }
                        if (index == to) {
                            return PaginationSkin.this.maxPageIndicatorCount - 1;
                        }
                        return index - from;
                    }
                }
            } else {
                while (from > 0 && to > 0) {
                    from -= i2;
                    to -= i2;
                    if (index < from || index > to) {
                        i2 += PaginationSkin.this.maxPageIndicatorCount;
                    } else {
                        if (index == from) {
                            return 0;
                        }
                        if (index == to) {
                            return PaginationSkin.this.maxPageIndicatorCount - 1;
                        }
                        return index - from;
                    }
                }
            }
            return PaginationSkin.this.maxPageIndicatorCount - 1;
        }

        private Pos sideToPos(Side s2) {
            if (Side.TOP.equals(s2)) {
                return Pos.TOP_CENTER;
            }
            if (Side.RIGHT.equals(s2)) {
                return Pos.CENTER_RIGHT;
            }
            if (Side.BOTTOM.equals(s2)) {
                return Pos.BOTTOM_CENTER;
            }
            return Pos.CENTER_LEFT;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computeMinWidth(double height) {
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double leftArrowWidth = snapSize(Utils.boundedSize(this.leftArrowButton.prefWidth(-1.0d), this.leftArrowButton.minWidth(-1.0d), this.leftArrowButton.maxWidth(-1.0d)));
            double rightArrowWidth = snapSize(Utils.boundedSize(this.rightArrowButton.prefWidth(-1.0d), this.rightArrowButton.minWidth(-1.0d), this.rightArrowButton.maxWidth(-1.0d)));
            double spacing = snapSize(this.controlBox.getSpacing());
            double pageInformationWidth = 0.0d;
            Side side = PaginationSkin.this.getPageInformationAlignment();
            if (Side.LEFT.equals(side) || Side.RIGHT.equals(side)) {
                pageInformationWidth = snapSize(this.pageInformation.prefWidth(-1.0d));
            }
            double arrowGap = PaginationSkin.this.arrowButtonGap.get();
            return left + leftArrowWidth + (2.0d * arrowGap) + this.minButtonSize + (2.0d * spacing) + rightArrowWidth + right + pageInformationWidth;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computeMinHeight(double width) {
            return computePrefHeight(width);
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double controlBoxWidth = snapSize(this.controlBox.prefWidth(height));
            double pageInformationWidth = 0.0d;
            Side side = PaginationSkin.this.getPageInformationAlignment();
            if (Side.LEFT.equals(side) || Side.RIGHT.equals(side)) {
                pageInformationWidth = snapSize(this.pageInformation.prefWidth(-1.0d));
            }
            return left + controlBoxWidth + right + pageInformationWidth;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            double top = snappedTopInset();
            double bottom = snappedBottomInset();
            double boxHeight = snapSize(this.controlBox.prefHeight(width));
            double pageInformationHeight = 0.0d;
            Side side = PaginationSkin.this.getPageInformationAlignment();
            if (Side.TOP.equals(side) || Side.BOTTOM.equals(side)) {
                pageInformationHeight = snapSize(this.pageInformation.prefHeight(-1.0d));
            }
            return top + boxHeight + pageInformationHeight + bottom;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
        protected void layoutChildren() {
            double top = snappedTopInset();
            double bottom = snappedBottomInset();
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double width = snapSize(getWidth()) - (left + right);
            double height = snapSize(getHeight()) - (top + bottom);
            double controlBoxWidth = snapSize(this.controlBox.prefWidth(-1.0d));
            double controlBoxHeight = snapSize(this.controlBox.prefHeight(-1.0d));
            double pageInformationWidth = snapSize(this.pageInformation.prefWidth(-1.0d));
            double pageInformationHeight = snapSize(this.pageInformation.prefHeight(-1.0d));
            this.leftArrowButton.setDisable(false);
            this.rightArrowButton.setDisable(false);
            if (PaginationSkin.this.currentIndex == 0) {
                this.leftArrowButton.setDisable(true);
            }
            if (PaginationSkin.this.currentIndex == PaginationSkin.this.getPageCount() - 1) {
                this.rightArrowButton.setDisable(true);
            }
            applyCss();
            this.leftArrowButton.setVisible(PaginationSkin.this.isArrowsVisible());
            this.rightArrowButton.setVisible(PaginationSkin.this.isArrowsVisible());
            this.pageInformation.setVisible(PaginationSkin.this.isPageInformationVisible());
            layoutPageIndicators();
            this.previousWidth = getWidth();
            HPos controlBoxHPos = this.controlBox.getAlignment().getHpos();
            VPos controlBoxVPos = this.controlBox.getAlignment().getVpos();
            double controlBoxX = left + Utils.computeXOffset(width, controlBoxWidth, controlBoxHPos);
            double controlBoxY = top + Utils.computeYOffset(height, controlBoxHeight, controlBoxVPos);
            if (PaginationSkin.this.isPageInformationVisible()) {
                Pos p2 = sideToPos(PaginationSkin.this.getPageInformationAlignment());
                HPos pageInformationHPos = p2.getHpos();
                VPos pageInformationVPos = p2.getVpos();
                double pageInformationX = left + Utils.computeXOffset(width, pageInformationWidth, pageInformationHPos);
                double pageInformationY = top + Utils.computeYOffset(height, pageInformationHeight, pageInformationVPos);
                if (Side.TOP.equals(PaginationSkin.this.getPageInformationAlignment())) {
                    pageInformationY = top;
                    controlBoxY = top + pageInformationHeight;
                } else if (Side.RIGHT.equals(PaginationSkin.this.getPageInformationAlignment())) {
                    pageInformationX = (width - right) - pageInformationWidth;
                } else if (Side.BOTTOM.equals(PaginationSkin.this.getPageInformationAlignment())) {
                    controlBoxY = top;
                    pageInformationY = top + controlBoxHeight;
                } else if (Side.LEFT.equals(PaginationSkin.this.getPageInformationAlignment())) {
                    pageInformationX = left;
                }
                layoutInArea(this.pageInformation, pageInformationX, pageInformationY, pageInformationWidth, pageInformationHeight, 0.0d, pageInformationHPos, pageInformationVPos);
            }
            layoutInArea(this.controlBox, controlBoxX, controlBoxY, controlBoxWidth, controlBoxHeight, 0.0d, controlBoxHPos, controlBoxVPos);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/PaginationSkin$IndicatorButton.class */
    class IndicatorButton extends ToggleButton {
        private final ListChangeListener<String> updateSkinIndicatorType = c2 -> {
            setIndicatorType();
        };
        private final ChangeListener<Boolean> updateTooltipVisibility = (ob, oldValue, newValue) -> {
            setTooltipVisible(newValue.booleanValue());
        };
        private int pageNumber;

        /* JADX WARN: Multi-variable type inference failed */
        public IndicatorButton(int pageNumber) {
            this.pageNumber = pageNumber;
            setFocusTraversable(false);
            setIndicatorType();
            setTooltipVisible(PaginationSkin.this.isTooltipVisible());
            ((Pagination) PaginationSkin.this.getSkinnable()).getStyleClass().addListener(this.updateSkinIndicatorType);
            setOnAction(new EventHandler<ActionEvent>() { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.IndicatorButton.1
                @Override // javafx.event.EventHandler
                public void handle(ActionEvent arg0) {
                    int selected = PaginationSkin.this.getCurrentPageIndex();
                    if (selected != IndicatorButton.this.pageNumber) {
                        PaginationSkin.this.pagination.setCurrentPageIndex(IndicatorButton.this.pageNumber);
                        IndicatorButton.this.requestLayout();
                    }
                }
            });
            PaginationSkin.this.tooltipVisibleProperty().addListener(this.updateTooltipVisibility);
            prefHeightProperty().bind(minHeightProperty());
            setAccessibleRole(AccessibleRole.PAGE_ITEM);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void setIndicatorType() {
            if (((Pagination) PaginationSkin.this.getSkinnable()).getStyleClass().contains(Pagination.STYLE_CLASS_BULLET)) {
                getStyleClass().remove("number-button");
                getStyleClass().add("bullet-button");
                setText(null);
                prefWidthProperty().bind(minWidthProperty());
                return;
            }
            getStyleClass().remove("bullet-button");
            getStyleClass().add("number-button");
            setText(Integer.toString(this.pageNumber + 1));
            prefWidthProperty().unbind();
        }

        private void setTooltipVisible(boolean b2) {
            if (b2) {
                setTooltip(new Tooltip(Integer.toString(this.pageNumber + 1)));
            } else {
                setTooltip(null);
            }
        }

        public int getPageNumber() {
            return this.pageNumber;
        }

        @Override // javafx.scene.control.ToggleButton, javafx.scene.control.ButtonBase
        public void fire() {
            if (getToggleGroup() == null || !isSelected()) {
                super.fire();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void release() {
            ((Pagination) PaginationSkin.this.getSkinnable()).getStyleClass().removeListener(this.updateSkinIndicatorType);
            PaginationSkin.this.tooltipVisibleProperty().removeListener(this.updateTooltipVisibility);
        }

        @Override // javafx.scene.control.ToggleButton, javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
        public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
            switch (attribute) {
                case TEXT:
                    return getText();
                case SELECTED:
                    return Boolean.valueOf(isSelected());
                default:
                    return super.queryAccessibleAttribute(attribute, parameters);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.control.ButtonBase, javafx.scene.control.Control, javafx.scene.Node
        public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
            switch (action) {
                case REQUEST_FOCUS:
                    ((Pagination) PaginationSkin.this.getSkinnable()).setCurrentPageIndex(this.pageNumber);
                    break;
                default:
                    super.executeAccessibleAction(action, new Object[0]);
                    break;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/PaginationSkin$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Pagination, Boolean> ARROWS_VISIBLE = new CssMetaData<Pagination, Boolean>("-fx-arrows-visible", BooleanConverter.getInstance(), PaginationSkin.DEFAULT_ARROW_VISIBLE) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return skin.arrowsVisible == null || !skin.arrowsVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return (StyleableProperty) skin.arrowsVisibleProperty();
            }
        };
        private static final CssMetaData<Pagination, Boolean> PAGE_INFORMATION_VISIBLE = new CssMetaData<Pagination, Boolean>("-fx-page-information-visible", BooleanConverter.getInstance(), PaginationSkin.DEFAULT_PAGE_INFORMATION_VISIBLE) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return skin.pageInformationVisible == null || !skin.pageInformationVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return (StyleableProperty) skin.pageInformationVisibleProperty();
            }
        };
        private static final CssMetaData<Pagination, Side> PAGE_INFORMATION_ALIGNMENT = new CssMetaData<Pagination, Side>("-fx-page-information-alignment", new EnumConverter(Side.class), PaginationSkin.DEFAULT_PAGE_INFORMATION_ALIGNMENT) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return skin.pageInformationAlignment == null || !skin.pageInformationAlignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Side> getStyleableProperty(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return (StyleableProperty) skin.pageInformationAlignmentProperty();
            }
        };
        private static final CssMetaData<Pagination, Boolean> TOOLTIP_VISIBLE = new CssMetaData<Pagination, Boolean>("-fx-tooltip-visible", BooleanConverter.getInstance(), PaginationSkin.DEFAULT_TOOLTIP_VISIBLE) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return skin.tooltipVisible == null || !skin.tooltipVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return (StyleableProperty) skin.tooltipVisibleProperty();
            }
        };
        private static final CssMetaData<Pagination, Number> ARROW_BUTTON_GAP = new CssMetaData<Pagination, Number>("-fx-arrow-button-gap", SizeConverter.getInstance(), 4) { // from class: com.sun.javafx.scene.control.skin.PaginationSkin.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return skin.arrowButtonGap == null || !skin.arrowButtonGap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Pagination n2) {
                PaginationSkin skin = (PaginationSkin) n2.getSkin();
                return (StyleableProperty) skin.arrowButtonGapProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(SkinBase.getClassCssMetaData());
            styleables.add(ARROWS_VISIBLE);
            styleables.add(PAGE_INFORMATION_VISIBLE);
            styleables.add(PAGE_INFORMATION_ALIGNMENT);
            styleables.add(TOOLTIP_VISIBLE);
            styleables.add(ARROW_BUTTON_GAP);
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
