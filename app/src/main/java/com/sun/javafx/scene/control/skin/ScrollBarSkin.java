package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ScrollBarBehavior;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ScrollBarSkin.class */
public class ScrollBarSkin extends BehaviorSkinBase<ScrollBar, ScrollBarBehavior> {
    public static final int DEFAULT_LENGTH = 100;
    public static final int DEFAULT_WIDTH = 20;
    private StackPane thumb;
    private StackPane trackBackground;
    private StackPane track;
    private EndButton incButton;
    private EndButton decButton;
    private double trackLength;
    private double thumbLength;
    private double preDragThumbPos;
    private Point2D dragStart;
    private double trackPos;
    private static final double DEFAULT_EMBEDDED_SB_BREADTH = 8.0d;

    /* JADX WARN: Multi-variable type inference failed */
    public ScrollBarSkin(ScrollBar scrollbar) {
        super(scrollbar, new ScrollBarBehavior(scrollbar));
        initialize();
        ((ScrollBar) getSkinnable()).requestLayout();
        registerChangeListener(scrollbar.minProperty(), "MIN");
        registerChangeListener(scrollbar.maxProperty(), "MAX");
        registerChangeListener(scrollbar.valueProperty(), "VALUE");
        registerChangeListener(scrollbar.orientationProperty(), "ORIENTATION");
        registerChangeListener(scrollbar.visibleAmountProperty(), "VISIBLE_AMOUNT");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initialize() {
        this.track = new StackPane();
        this.track.getStyleClass().setAll("track");
        this.trackBackground = new StackPane();
        this.trackBackground.getStyleClass().setAll("track-background");
        this.thumb = new StackPane() { // from class: com.sun.javafx.scene.control.skin.ScrollBarSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.scene.Parent, javafx.scene.Node
            public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
                switch (AnonymousClass4.$SwitchMap$javafx$scene$AccessibleAttribute[attribute.ordinal()]) {
                    case 1:
                        return Double.valueOf(((ScrollBar) ScrollBarSkin.this.getSkinnable()).getValue());
                    default:
                        return super.queryAccessibleAttribute(attribute, parameters);
                }
            }
        };
        this.thumb.getStyleClass().setAll("thumb");
        this.thumb.setAccessibleRole(AccessibleRole.THUMB);
        if (!IS_TOUCH_SUPPORTED) {
            this.incButton = new EndButton("increment-button", "increment-arrow") { // from class: com.sun.javafx.scene.control.skin.ScrollBarSkin.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.scene.Node
                public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
                    switch (AnonymousClass4.$SwitchMap$javafx$scene$AccessibleAction[action.ordinal()]) {
                        case 1:
                            ((ScrollBar) ScrollBarSkin.this.getSkinnable()).increment();
                            break;
                        default:
                            super.executeAccessibleAction(action, parameters);
                            break;
                    }
                }
            };
            this.incButton.setAccessibleRole(AccessibleRole.INCREMENT_BUTTON);
            this.incButton.setOnMousePressed(me -> {
                if (!this.thumb.isVisible() || this.trackLength > this.thumbLength) {
                    getBehavior().incButtonPressed();
                }
                me.consume();
            });
            this.incButton.setOnMouseReleased(me2 -> {
                if (!this.thumb.isVisible() || this.trackLength > this.thumbLength) {
                    getBehavior().incButtonReleased();
                }
                me2.consume();
            });
            this.decButton = new EndButton("decrement-button", "decrement-arrow") { // from class: com.sun.javafx.scene.control.skin.ScrollBarSkin.3
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.scene.Node
                public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
                    switch (AnonymousClass4.$SwitchMap$javafx$scene$AccessibleAction[action.ordinal()]) {
                        case 1:
                            ((ScrollBar) ScrollBarSkin.this.getSkinnable()).decrement();
                            break;
                        default:
                            super.executeAccessibleAction(action, parameters);
                            break;
                    }
                }
            };
            this.decButton.setAccessibleRole(AccessibleRole.DECREMENT_BUTTON);
            this.decButton.setOnMousePressed(me3 -> {
                if (!this.thumb.isVisible() || this.trackLength > this.thumbLength) {
                    getBehavior().decButtonPressed();
                }
                me3.consume();
            });
            this.decButton.setOnMouseReleased(me4 -> {
                if (!this.thumb.isVisible() || this.trackLength > this.thumbLength) {
                    getBehavior().decButtonReleased();
                }
                me4.consume();
            });
        }
        this.track.setOnMousePressed(me5 -> {
            if (!this.thumb.isPressed() && me5.getButton() == MouseButton.PRIMARY) {
                if (((ScrollBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                    if (this.trackLength != 0.0d) {
                        getBehavior().trackPress(me5.getY() / this.trackLength);
                        me5.consume();
                        return;
                    }
                    return;
                }
                if (this.trackLength != 0.0d) {
                    getBehavior().trackPress(me5.getX() / this.trackLength);
                    me5.consume();
                }
            }
        });
        this.track.setOnMouseReleased(me6 -> {
            getBehavior().trackRelease();
            me6.consume();
        });
        this.thumb.setOnMousePressed(me7 -> {
            if (me7.isSynthesized()) {
                me7.consume();
            } else if (((ScrollBar) getSkinnable()).getMax() > ((ScrollBar) getSkinnable()).getMin()) {
                this.dragStart = this.thumb.localToParent(me7.getX(), me7.getY());
                double clampedValue = com.sun.javafx.util.Utils.clamp(((ScrollBar) getSkinnable()).getMin(), ((ScrollBar) getSkinnable()).getValue(), ((ScrollBar) getSkinnable()).getMax());
                this.preDragThumbPos = (clampedValue - ((ScrollBar) getSkinnable()).getMin()) / (((ScrollBar) getSkinnable()).getMax() - ((ScrollBar) getSkinnable()).getMin());
                me7.consume();
            }
        });
        this.thumb.setOnMouseDragged(me8 -> {
            if (me8.isSynthesized()) {
                me8.consume();
                return;
            }
            if (((ScrollBar) getSkinnable()).getMax() > ((ScrollBar) getSkinnable()).getMin()) {
                if (this.trackLength > this.thumbLength) {
                    Point2D cur = this.thumb.localToParent(me8.getX(), me8.getY());
                    if (this.dragStart == null) {
                        this.dragStart = this.thumb.localToParent(me8.getX(), me8.getY());
                    }
                    double dragPos = ((ScrollBar) getSkinnable()).getOrientation() == Orientation.VERTICAL ? cur.getY() - this.dragStart.getY() : cur.getX() - this.dragStart.getX();
                    getBehavior().thumbDragged(this.preDragThumbPos + (dragPos / (this.trackLength - this.thumbLength)));
                }
                me8.consume();
            }
        });
        this.thumb.setOnScrollStarted(se -> {
            if (se.isDirect() && ((ScrollBar) getSkinnable()).getMax() > ((ScrollBar) getSkinnable()).getMin()) {
                this.dragStart = this.thumb.localToParent(se.getX(), se.getY());
                double clampedValue = com.sun.javafx.util.Utils.clamp(((ScrollBar) getSkinnable()).getMin(), ((ScrollBar) getSkinnable()).getValue(), ((ScrollBar) getSkinnable()).getMax());
                this.preDragThumbPos = (clampedValue - ((ScrollBar) getSkinnable()).getMin()) / (((ScrollBar) getSkinnable()).getMax() - ((ScrollBar) getSkinnable()).getMin());
                se.consume();
            }
        });
        this.thumb.setOnScroll(event -> {
            if (event.isDirect() && ((ScrollBar) getSkinnable()).getMax() > ((ScrollBar) getSkinnable()).getMin()) {
                if (this.trackLength > this.thumbLength) {
                    Point2D cur = this.thumb.localToParent(event.getX(), event.getY());
                    if (this.dragStart == null) {
                        this.dragStart = this.thumb.localToParent(event.getX(), event.getY());
                    }
                    double dragPos = ((ScrollBar) getSkinnable()).getOrientation() == Orientation.VERTICAL ? cur.getY() - this.dragStart.getY() : cur.getX() - this.dragStart.getX();
                    getBehavior().thumbDragged(this.preDragThumbPos + (dragPos / (this.trackLength - this.thumbLength)));
                }
                event.consume();
            }
        });
        ((ScrollBar) getSkinnable()).addEventHandler(ScrollEvent.SCROLL, event2 -> {
            if (this.trackLength > this.thumbLength) {
                double dx = event2.getDeltaX();
                double dy = event2.getDeltaY();
                double dx2 = Math.abs(dx) < Math.abs(dy) ? dy : dx;
                ScrollBar sb = (ScrollBar) getSkinnable();
                double delta = ((ScrollBar) getSkinnable()).getOrientation() == Orientation.VERTICAL ? dy : dx2;
                if (event2.isDirect()) {
                    if (this.trackLength > this.thumbLength) {
                        getBehavior().thumbDragged((((ScrollBar) getSkinnable()).getOrientation() == Orientation.VERTICAL ? event2.getY() : event2.getX()) / this.trackLength);
                        event2.consume();
                        return;
                    }
                    return;
                }
                if (delta > 0.0d && sb.getValue() > sb.getMin()) {
                    sb.decrement();
                    event2.consume();
                } else if (delta < 0.0d && sb.getValue() < sb.getMax()) {
                    sb.increment();
                    event2.consume();
                }
            }
        });
        getChildren().clear();
        if (!IS_TOUCH_SUPPORTED) {
            getChildren().addAll(this.trackBackground, this.incButton, this.decButton, this.track, this.thumb);
        } else {
            getChildren().addAll(this.track, this.thumb);
        }
    }

    /* renamed from: com.sun.javafx.scene.control.skin.ScrollBarSkin$4, reason: invalid class name */
    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ScrollBarSkin$4.class */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$AccessibleAttribute;
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$AccessibleAction = new int[AccessibleAction.values().length];

        static {
            try {
                $SwitchMap$javafx$scene$AccessibleAction[AccessibleAction.FIRE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$javafx$scene$AccessibleAttribute = new int[AccessibleAttribute.values().length];
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.VALUE.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("ORIENTATION".equals(p2)) {
            ((ScrollBar) getSkinnable()).requestLayout();
            return;
        }
        if ("MIN".equals(p2) || "MAX".equals(p2) || "VISIBLE_AMOUNT".equals(p2)) {
            positionThumb();
            ((ScrollBar) getSkinnable()).requestLayout();
        } else if ("VALUE".equals(p2)) {
            positionThumb();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    double getBreadth() {
        if (!IS_TOUCH_SUPPORTED) {
            if (((ScrollBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                return Math.max(this.decButton.prefWidth(-1.0d), this.incButton.prefWidth(-1.0d)) + snappedLeftInset() + snappedRightInset();
            }
            return Math.max(this.decButton.prefHeight(-1.0d), this.incButton.prefHeight(-1.0d)) + snappedTopInset() + snappedBottomInset();
        }
        if (((ScrollBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            return Math.max(DEFAULT_EMBEDDED_SB_BREADTH, DEFAULT_EMBEDDED_SB_BREADTH) + snappedLeftInset() + snappedRightInset();
        }
        return Math.max(DEFAULT_EMBEDDED_SB_BREADTH, DEFAULT_EMBEDDED_SB_BREADTH) + snappedTopInset() + snappedBottomInset();
    }

    double minThumbLength() {
        return 1.5d * getBreadth();
    }

    double minTrackLength() {
        return 2.0d * getBreadth();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (((ScrollBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            return getBreadth();
        }
        if (!IS_TOUCH_SUPPORTED) {
            return this.decButton.minWidth(-1.0d) + this.incButton.minWidth(-1.0d) + minTrackLength() + leftInset + rightInset;
        }
        return minTrackLength() + leftInset + rightInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (((ScrollBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            if (!IS_TOUCH_SUPPORTED) {
                return this.decButton.minHeight(-1.0d) + this.incButton.minHeight(-1.0d) + minTrackLength() + topInset + bottomInset;
            }
            return minTrackLength() + topInset + bottomInset;
        }
        return getBreadth();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        ScrollBar s2 = (ScrollBar) getSkinnable();
        return s2.getOrientation() == Orientation.VERTICAL ? getBreadth() : 100.0d + leftInset + rightInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        ScrollBar s2 = (ScrollBar) getSkinnable();
        return s2.getOrientation() == Orientation.VERTICAL ? 100.0d + topInset + bottomInset : getBreadth();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        ScrollBar s2 = (ScrollBar) getSkinnable();
        if (s2.getOrientation() == Orientation.VERTICAL) {
            return s2.prefWidth(-1.0d);
        }
        return Double.MAX_VALUE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        ScrollBar s2 = (ScrollBar) getSkinnable();
        if (s2.getOrientation() == Orientation.VERTICAL) {
            return Double.MAX_VALUE;
        }
        return s2.prefHeight(-1.0d);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void positionThumb() {
        ScrollBar s2 = (ScrollBar) getSkinnable();
        double clampedValue = com.sun.javafx.util.Utils.clamp(s2.getMin(), s2.getValue(), s2.getMax());
        this.trackPos = s2.getMax() - s2.getMin() > 0.0d ? ((this.trackLength - this.thumbLength) * (clampedValue - s2.getMin())) / (s2.getMax() - s2.getMin()) : 0.0d;
        if (!IS_TOUCH_SUPPORTED) {
            if (s2.getOrientation() == Orientation.VERTICAL) {
                this.trackPos += this.decButton.prefHeight(-1.0d);
            } else {
                this.trackPos += this.decButton.prefWidth(-1.0d);
            }
        }
        this.thumb.setTranslateX(snapPosition(s2.getOrientation() == Orientation.VERTICAL ? snappedLeftInset() : this.trackPos + snappedLeftInset()));
        this.thumb.setTranslateY(snapPosition(s2.getOrientation() == Orientation.VERTICAL ? this.trackPos + snappedTopInset() : snappedTopInset()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double visiblePortion;
        ScrollBar s2 = (ScrollBar) getSkinnable();
        if (s2.getMax() > s2.getMin()) {
            visiblePortion = s2.getVisibleAmount() / (s2.getMax() - s2.getMin());
        } else {
            visiblePortion = 1.0d;
        }
        if (s2.getOrientation() == Orientation.VERTICAL) {
            if (!IS_TOUCH_SUPPORTED) {
                double decHeight = snapSize(this.decButton.prefHeight(-1.0d));
                double incHeight = snapSize(this.incButton.prefHeight(-1.0d));
                this.decButton.resize(w2, decHeight);
                this.incButton.resize(w2, incHeight);
                this.trackLength = snapSize(h2 - (decHeight + incHeight));
                this.thumbLength = snapSize(com.sun.javafx.util.Utils.clamp(minThumbLength(), this.trackLength * visiblePortion, this.trackLength));
                this.trackBackground.resizeRelocate(snapPosition(x2), snapPosition(y2), w2, this.trackLength + decHeight + incHeight);
                this.decButton.relocate(snapPosition(x2), snapPosition(y2));
                this.incButton.relocate(snapPosition(x2), snapPosition((y2 + h2) - incHeight));
                this.track.resizeRelocate(snapPosition(x2), snapPosition(y2 + decHeight), w2, this.trackLength);
                this.thumb.resize(snapSize(x2 >= 0.0d ? w2 : w2 + x2), this.thumbLength);
                positionThumb();
            } else {
                this.trackLength = snapSize(h2);
                this.thumbLength = snapSize(com.sun.javafx.util.Utils.clamp(minThumbLength(), this.trackLength * visiblePortion, this.trackLength));
                this.track.resizeRelocate(snapPosition(x2), snapPosition(y2), w2, this.trackLength);
                this.thumb.resize(snapSize(x2 >= 0.0d ? w2 : w2 + x2), this.thumbLength);
                positionThumb();
            }
        } else {
            if (!IS_TOUCH_SUPPORTED) {
                double decWidth = snapSize(this.decButton.prefWidth(-1.0d));
                double incWidth = snapSize(this.incButton.prefWidth(-1.0d));
                this.decButton.resize(decWidth, h2);
                this.incButton.resize(incWidth, h2);
                this.trackLength = snapSize(w2 - (decWidth + incWidth));
                this.thumbLength = snapSize(com.sun.javafx.util.Utils.clamp(minThumbLength(), this.trackLength * visiblePortion, this.trackLength));
                this.trackBackground.resizeRelocate(snapPosition(x2), snapPosition(y2), this.trackLength + decWidth + incWidth, h2);
                this.decButton.relocate(snapPosition(x2), snapPosition(y2));
                this.incButton.relocate(snapPosition((x2 + w2) - incWidth), snapPosition(y2));
                this.track.resizeRelocate(snapPosition(x2 + decWidth), snapPosition(y2), this.trackLength, h2);
                this.thumb.resize(this.thumbLength, snapSize(y2 >= 0.0d ? h2 : h2 + y2));
                positionThumb();
            } else {
                this.trackLength = snapSize(w2);
                this.thumbLength = snapSize(com.sun.javafx.util.Utils.clamp(minThumbLength(), this.trackLength * visiblePortion, this.trackLength));
                this.track.resizeRelocate(snapPosition(x2), snapPosition(y2), this.trackLength, h2);
                this.thumb.resize(this.thumbLength, snapSize(y2 >= 0.0d ? h2 : h2 + y2));
                positionThumb();
            }
            s2.resize(snapSize(s2.getWidth()), snapSize(s2.getHeight()));
        }
        if ((s2.getOrientation() == Orientation.VERTICAL && h2 >= computeMinHeight(-1.0d, (int) y2, snappedRightInset(), snappedBottomInset(), (int) x2) - (y2 + snappedBottomInset())) || (s2.getOrientation() == Orientation.HORIZONTAL && w2 >= computeMinWidth(-1.0d, (int) y2, snappedRightInset(), snappedBottomInset(), (int) x2) - (x2 + snappedRightInset()))) {
            this.trackBackground.setVisible(true);
            this.track.setVisible(true);
            this.thumb.setVisible(true);
            if (!IS_TOUCH_SUPPORTED) {
                this.incButton.setVisible(true);
                this.decButton.setVisible(true);
                return;
            }
            return;
        }
        this.trackBackground.setVisible(false);
        this.track.setVisible(false);
        this.thumb.setVisible(false);
        if (IS_TOUCH_SUPPORTED) {
            return;
        }
        if (h2 >= this.decButton.computeMinWidth(-1.0d)) {
            this.decButton.setVisible(true);
        } else {
            this.decButton.setVisible(false);
        }
        if (h2 >= this.incButton.computeMinWidth(-1.0d)) {
            this.incButton.setVisible(true);
        } else {
            this.incButton.setVisible(false);
        }
    }

    public Node getThumb() {
        return this.thumb;
    }

    public Node getTrack() {
        return this.track;
    }

    public Node getIncButton() {
        return this.incButton;
    }

    public Node getDecButton() {
        return this.decButton;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ScrollBarSkin$EndButton.class */
    private static class EndButton extends Region {
        private Region arrow;

        private EndButton(String styleClass, String arrowStyleClass) {
            getStyleClass().setAll(styleClass);
            this.arrow = new Region();
            this.arrow.getStyleClass().setAll(arrowStyleClass);
            getChildren().setAll(this.arrow);
            requestLayout();
        }

        @Override // javafx.scene.Parent
        protected void layoutChildren() {
            double top = snappedTopInset();
            double left = snappedLeftInset();
            double bottom = snappedBottomInset();
            double right = snappedRightInset();
            double aw2 = snapSize(this.arrow.prefWidth(-1.0d));
            double ah2 = snapSize(this.arrow.prefHeight(-1.0d));
            double yPos = snapPosition((getHeight() - ((top + bottom) + ah2)) / 2.0d);
            double xPos = snapPosition((getWidth() - ((left + right) + aw2)) / 2.0d);
            this.arrow.resizeRelocate(xPos + left, yPos + top, aw2, ah2);
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computeMinHeight(double width) {
            return prefHeight(-1.0d);
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computeMinWidth(double height) {
            return prefWidth(-1.0d);
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double aw2 = snapSize(this.arrow.prefWidth(-1.0d));
            return left + aw2 + right;
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            double top = snappedTopInset();
            double bottom = snappedBottomInset();
            double ah2 = snapSize(this.arrow.prefHeight(-1.0d));
            return top + ah2 + bottom;
        }
    }
}
