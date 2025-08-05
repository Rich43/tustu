package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ScrollPaneBehavior;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraverseListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ScrollPaneSkin.class */
public class ScrollPaneSkin extends BehaviorSkinBase<ScrollPane, ScrollPaneBehavior> implements TraverseListener {
    private static final double DEFAULT_PREF_SIZE = 100.0d;
    private static final double DEFAULT_MIN_SIZE = 36.0d;
    private static final double DEFAULT_SB_BREADTH = 12.0d;
    private static final double DEFAULT_EMBEDDED_SB_BREADTH = 8.0d;
    private static final double PAN_THRESHOLD = 0.5d;
    private Node scrollNode;
    private double nodeWidth;
    private double nodeHeight;
    private boolean nodeSizeInvalid;
    private double posX;
    private double posY;
    private boolean hsbvis;
    private boolean vsbvis;
    private double hsbHeight;
    private double vsbWidth;
    private StackPane viewRect;
    private StackPane viewContent;
    private double contentWidth;
    private double contentHeight;
    private StackPane corner;
    protected ScrollBar hsb;
    protected ScrollBar vsb;
    double pressX;
    double pressY;
    double ohvalue;
    double ovvalue;
    private Cursor saveCursor;
    private boolean dragDetected;
    private boolean touchDetected;
    private boolean mouseDown;
    Rectangle clipRect;
    private final InvalidationListener nodeListener;
    private final ChangeListener<Bounds> boundsChangeListener;
    Timeline sbTouchTimeline;
    KeyFrame sbTouchKF1;
    KeyFrame sbTouchKF2;
    Timeline contentsToViewTimeline;
    KeyFrame contentsToViewKF1;
    KeyFrame contentsToViewKF2;
    KeyFrame contentsToViewKF3;
    private boolean tempVisibility;
    private DoubleProperty contentPosX;
    private DoubleProperty contentPosY;

    public ScrollPaneSkin(ScrollPane scrollpane) {
        super(scrollpane, new ScrollPaneBehavior(scrollpane));
        this.nodeSizeInvalid = true;
        this.saveCursor = null;
        this.dragDetected = false;
        this.touchDetected = false;
        this.mouseDown = false;
        this.nodeListener = new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.ScrollPaneSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable valueModel) {
                if (!ScrollPaneSkin.this.nodeSizeInvalid) {
                    Bounds scrollNodeBounds = ScrollPaneSkin.this.scrollNode.getLayoutBounds();
                    double scrollNodeWidth = scrollNodeBounds.getWidth();
                    double scrollNodeHeight = scrollNodeBounds.getHeight();
                    if (ScrollPaneSkin.this.vsbvis == ScrollPaneSkin.this.determineVerticalSBVisible() && ScrollPaneSkin.this.hsbvis == ScrollPaneSkin.this.determineHorizontalSBVisible() && ((scrollNodeWidth == 0.0d || ScrollPaneSkin.this.nodeWidth == scrollNodeWidth) && (scrollNodeHeight == 0.0d || ScrollPaneSkin.this.nodeHeight == scrollNodeHeight))) {
                        if (!ScrollPaneSkin.this.dragDetected) {
                            ScrollPaneSkin.this.updateVerticalSB();
                            ScrollPaneSkin.this.updateHorizontalSB();
                            return;
                        }
                        return;
                    }
                    ((ScrollPane) ScrollPaneSkin.this.getSkinnable()).requestLayout();
                }
            }
        };
        this.boundsChangeListener = new ChangeListener<Bounds>() { // from class: com.sun.javafx.scene.control.skin.ScrollPaneSkin.2
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds newBounds) {
                double oldHeight = oldBounds.getHeight();
                double newHeight = newBounds.getHeight();
                if (oldHeight > 0.0d && oldHeight != newHeight) {
                    double oldPositionY = ScrollPaneSkin.this.snapPosition(ScrollPaneSkin.this.snappedTopInset() - ((ScrollPaneSkin.this.posY / (ScrollPaneSkin.this.vsb.getMax() - ScrollPaneSkin.this.vsb.getMin())) * (oldHeight - ScrollPaneSkin.this.contentHeight)));
                    double newPositionY = ScrollPaneSkin.this.snapPosition(ScrollPaneSkin.this.snappedTopInset() - ((ScrollPaneSkin.this.posY / (ScrollPaneSkin.this.vsb.getMax() - ScrollPaneSkin.this.vsb.getMin())) * (newHeight - ScrollPaneSkin.this.contentHeight)));
                    double newValueY = (oldPositionY / newPositionY) * ScrollPaneSkin.this.vsb.getValue();
                    if (newValueY < 0.0d) {
                        ScrollPaneSkin.this.vsb.setValue(0.0d);
                    } else if (newValueY < 1.0d) {
                        ScrollPaneSkin.this.vsb.setValue(newValueY);
                    } else if (newValueY > 1.0d) {
                        ScrollPaneSkin.this.vsb.setValue(1.0d);
                    }
                }
                double oldWidth = oldBounds.getWidth();
                double newWidth = newBounds.getWidth();
                if (oldWidth > 0.0d && oldWidth != newWidth) {
                    double oldPositionX = ScrollPaneSkin.this.snapPosition(ScrollPaneSkin.this.snappedLeftInset() - ((ScrollPaneSkin.this.posX / (ScrollPaneSkin.this.hsb.getMax() - ScrollPaneSkin.this.hsb.getMin())) * (oldWidth - ScrollPaneSkin.this.contentWidth)));
                    double newPositionX = ScrollPaneSkin.this.snapPosition(ScrollPaneSkin.this.snappedLeftInset() - ((ScrollPaneSkin.this.posX / (ScrollPaneSkin.this.hsb.getMax() - ScrollPaneSkin.this.hsb.getMin())) * (newWidth - ScrollPaneSkin.this.contentWidth)));
                    double newValueX = (oldPositionX / newPositionX) * ScrollPaneSkin.this.hsb.getValue();
                    if (newValueX < 0.0d) {
                        ScrollPaneSkin.this.hsb.setValue(0.0d);
                    } else if (newValueX < 1.0d) {
                        ScrollPaneSkin.this.hsb.setValue(newValueX);
                    } else if (newValueX > 1.0d) {
                        ScrollPaneSkin.this.hsb.setValue(1.0d);
                    }
                }
            }
        };
        initialize();
        registerChangeListener(scrollpane.contentProperty(), "NODE");
        registerChangeListener(scrollpane.fitToWidthProperty(), "FIT_TO_WIDTH");
        registerChangeListener(scrollpane.fitToHeightProperty(), "FIT_TO_HEIGHT");
        registerChangeListener(scrollpane.hbarPolicyProperty(), "HBAR_POLICY");
        registerChangeListener(scrollpane.vbarPolicyProperty(), "VBAR_POLICY");
        registerChangeListener(scrollpane.hvalueProperty(), "HVALUE");
        registerChangeListener(scrollpane.hmaxProperty(), "HMAX");
        registerChangeListener(scrollpane.hminProperty(), "HMIN");
        registerChangeListener(scrollpane.vvalueProperty(), "VVALUE");
        registerChangeListener(scrollpane.vmaxProperty(), "VMAX");
        registerChangeListener(scrollpane.vminProperty(), "VMIN");
        registerChangeListener(scrollpane.prefViewportWidthProperty(), "VIEWPORT_SIZE_HINT");
        registerChangeListener(scrollpane.prefViewportHeightProperty(), "VIEWPORT_SIZE_HINT");
        registerChangeListener(scrollpane.minViewportWidthProperty(), "VIEWPORT_SIZE_HINT");
        registerChangeListener(scrollpane.minViewportHeightProperty(), "VIEWPORT_SIZE_HINT");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [javafx.scene.Parent, javafx.scene.control.Control] */
    private void initialize() {
        ScrollPane control = (ScrollPane) getSkinnable();
        this.scrollNode = control.getContent();
        ParentTraversalEngine traversalEngine = new ParentTraversalEngine(getSkinnable());
        traversalEngine.addTraverseListener(this);
        ((ScrollPane) getSkinnable()).setImpl_traversalEngine(traversalEngine);
        if (this.scrollNode != null) {
            this.scrollNode.layoutBoundsProperty().addListener(this.nodeListener);
            this.scrollNode.layoutBoundsProperty().addListener(this.boundsChangeListener);
        }
        this.viewRect = new StackPane() { // from class: com.sun.javafx.scene.control.skin.ScrollPaneSkin.3
            @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
            protected void layoutChildren() {
                ScrollPaneSkin.this.viewContent.resize(getWidth(), getHeight());
            }
        };
        this.viewRect.setManaged(false);
        this.viewRect.setCache(true);
        this.viewRect.getStyleClass().add("viewport");
        this.clipRect = new Rectangle();
        this.viewRect.setClip(this.clipRect);
        this.hsb = new ScrollBar();
        this.vsb = new ScrollBar();
        this.vsb.setOrientation(Orientation.VERTICAL);
        EventHandler<MouseEvent> barHandler = ev -> {
            ((ScrollPane) getSkinnable()).requestFocus();
        };
        this.hsb.addEventFilter(MouseEvent.MOUSE_PRESSED, barHandler);
        this.vsb.addEventFilter(MouseEvent.MOUSE_PRESSED, barHandler);
        this.corner = new StackPane();
        this.corner.getStyleClass().setAll("corner");
        this.viewContent = new StackPane() { // from class: com.sun.javafx.scene.control.skin.ScrollPaneSkin.4
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
            public void requestLayout() {
                ScrollPaneSkin.this.nodeSizeInvalid = true;
                super.requestLayout();
                ((ScrollPane) ScrollPaneSkin.this.getSkinnable()).requestLayout();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
            protected void layoutChildren() {
                if (ScrollPaneSkin.this.nodeSizeInvalid) {
                    ScrollPaneSkin.this.computeScrollNodeSize(getWidth(), getHeight());
                }
                if (ScrollPaneSkin.this.scrollNode != null && ScrollPaneSkin.this.scrollNode.isResizable()) {
                    ScrollPaneSkin.this.scrollNode.resize(snapSize(ScrollPaneSkin.this.nodeWidth), snapSize(ScrollPaneSkin.this.nodeHeight));
                    if (ScrollPaneSkin.this.vsbvis != ScrollPaneSkin.this.determineVerticalSBVisible() || ScrollPaneSkin.this.hsbvis != ScrollPaneSkin.this.determineHorizontalSBVisible()) {
                        ((ScrollPane) ScrollPaneSkin.this.getSkinnable()).requestLayout();
                    }
                }
                if (ScrollPaneSkin.this.scrollNode != null) {
                    ScrollPaneSkin.this.scrollNode.relocate(0.0d, 0.0d);
                }
            }
        };
        this.viewRect.getChildren().add(this.viewContent);
        if (this.scrollNode != null) {
            this.viewContent.getChildren().add(this.scrollNode);
            this.viewRect.nodeOrientationProperty().bind(this.scrollNode.nodeOrientationProperty());
        }
        getChildren().clear();
        getChildren().addAll(this.viewRect, this.vsb, this.hsb, this.corner);
        InvalidationListener vsbListener = valueModel -> {
            if (!IS_TOUCH_SUPPORTED) {
                this.posY = com.sun.javafx.util.Utils.clamp(((ScrollPane) getSkinnable()).getVmin(), this.vsb.getValue(), ((ScrollPane) getSkinnable()).getVmax());
            } else {
                this.posY = this.vsb.getValue();
            }
            updatePosY();
        };
        this.vsb.valueProperty().addListener(vsbListener);
        InvalidationListener hsbListener = valueModel2 -> {
            if (!IS_TOUCH_SUPPORTED) {
                this.posX = com.sun.javafx.util.Utils.clamp(((ScrollPane) getSkinnable()).getHmin(), this.hsb.getValue(), ((ScrollPane) getSkinnable()).getHmax());
            } else {
                this.posX = this.hsb.getValue();
            }
            updatePosX();
        };
        this.hsb.valueProperty().addListener(hsbListener);
        this.viewRect.setOnMousePressed(e2 -> {
            this.mouseDown = true;
            if (IS_TOUCH_SUPPORTED) {
                startSBReleasedAnimation();
            }
            this.pressX = e2.getX();
            this.pressY = e2.getY();
            this.ohvalue = this.hsb.getValue();
            this.ovvalue = this.vsb.getValue();
        });
        this.viewRect.setOnDragDetected(e3 -> {
            if (IS_TOUCH_SUPPORTED) {
                startSBReleasedAnimation();
            }
            if (((ScrollPane) getSkinnable()).isPannable()) {
                this.dragDetected = true;
                if (this.saveCursor == null) {
                    this.saveCursor = ((ScrollPane) getSkinnable()).getCursor();
                    if (this.saveCursor == null) {
                        this.saveCursor = Cursor.DEFAULT;
                    }
                    ((ScrollPane) getSkinnable()).setCursor(Cursor.MOVE);
                    ((ScrollPane) getSkinnable()).requestLayout();
                }
            }
        });
        this.viewRect.addEventFilter(MouseEvent.MOUSE_RELEASED, e4 -> {
            this.mouseDown = false;
            if (this.dragDetected) {
                if (this.saveCursor != null) {
                    ((ScrollPane) getSkinnable()).setCursor(this.saveCursor);
                    this.saveCursor = null;
                    ((ScrollPane) getSkinnable()).requestLayout();
                }
                this.dragDetected = false;
            }
            if ((this.posY > ((ScrollPane) getSkinnable()).getVmax() || this.posY < ((ScrollPane) getSkinnable()).getVmin() || this.posX > ((ScrollPane) getSkinnable()).getHmax() || this.posX < ((ScrollPane) getSkinnable()).getHmin()) && !this.touchDetected) {
                startContentsToViewport();
            }
        });
        this.viewRect.setOnMouseDragged(e5 -> {
            if (IS_TOUCH_SUPPORTED) {
                startSBReleasedAnimation();
            }
            if (((ScrollPane) getSkinnable()).isPannable() || IS_TOUCH_SUPPORTED) {
                double deltaX = this.pressX - e5.getX();
                double deltaY = this.pressY - e5.getY();
                if (this.hsb.getVisibleAmount() > 0.0d && this.hsb.getVisibleAmount() < this.hsb.getMax() && Math.abs(deltaX) > 0.5d) {
                    if (isReverseNodeOrientation()) {
                        deltaX = -deltaX;
                    }
                    double newHVal = this.ohvalue + ((deltaX / (this.nodeWidth - this.viewRect.getWidth())) * (this.hsb.getMax() - this.hsb.getMin()));
                    if (!IS_TOUCH_SUPPORTED) {
                        if (newHVal > this.hsb.getMax()) {
                            newHVal = this.hsb.getMax();
                        } else if (newHVal < this.hsb.getMin()) {
                            newHVal = this.hsb.getMin();
                        }
                        this.hsb.setValue(newHVal);
                    } else {
                        this.hsb.setValue(newHVal);
                    }
                }
                if (this.vsb.getVisibleAmount() > 0.0d && this.vsb.getVisibleAmount() < this.vsb.getMax() && Math.abs(deltaY) > 0.5d) {
                    double newVVal = this.ovvalue + ((deltaY / (this.nodeHeight - this.viewRect.getHeight())) * (this.vsb.getMax() - this.vsb.getMin()));
                    if (!IS_TOUCH_SUPPORTED) {
                        if (newVVal > this.vsb.getMax()) {
                            newVVal = this.vsb.getMax();
                        } else if (newVVal < this.vsb.getMin()) {
                            newVVal = this.vsb.getMin();
                        }
                        this.vsb.setValue(newVVal);
                    } else {
                        this.vsb.setValue(newVVal);
                    }
                }
            }
            e5.consume();
        });
        EventDispatcher blockEventDispatcher = (event, tail) -> {
            return event;
        };
        EventDispatcher oldHsbEventDispatcher = this.hsb.getEventDispatcher();
        this.hsb.setEventDispatcher((event2, tail2) -> {
            if (event2.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent) event2).isDirect()) {
                return tail2.prepend(blockEventDispatcher).prepend(oldHsbEventDispatcher).dispatchEvent(event2);
            }
            return oldHsbEventDispatcher.dispatchEvent(event2, tail2);
        });
        EventDispatcher oldVsbEventDispatcher = this.vsb.getEventDispatcher();
        this.vsb.setEventDispatcher((event3, tail3) -> {
            if (event3.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent) event3).isDirect()) {
                return tail3.prepend(blockEventDispatcher).prepend(oldVsbEventDispatcher).dispatchEvent(event3);
            }
            return oldVsbEventDispatcher.dispatchEvent(event3, tail3);
        });
        this.viewRect.addEventHandler(ScrollEvent.SCROLL, event4 -> {
            double hPixelValue;
            double vPixelValue;
            if (IS_TOUCH_SUPPORTED) {
                startSBReleasedAnimation();
            }
            if (this.vsb.getVisibleAmount() < this.vsb.getMax()) {
                double vRange = ((ScrollPane) getSkinnable()).getVmax() - ((ScrollPane) getSkinnable()).getVmin();
                if (this.nodeHeight > 0.0d) {
                    vPixelValue = vRange / this.nodeHeight;
                } else {
                    vPixelValue = 0.0d;
                }
                double newValue = this.vsb.getValue() + ((-event4.getDeltaY()) * vPixelValue);
                if (IS_TOUCH_SUPPORTED) {
                    if (!event4.isInertia() || (event4.isInertia() && (this.contentsToViewTimeline == null || this.contentsToViewTimeline.getStatus() == Animation.Status.STOPPED))) {
                        this.vsb.setValue(newValue);
                        if ((newValue > this.vsb.getMax() || newValue < this.vsb.getMin()) && !this.mouseDown && !this.touchDetected) {
                            startContentsToViewport();
                        }
                        event4.consume();
                    }
                } else if ((event4.getDeltaY() > 0.0d && this.vsb.getValue() > this.vsb.getMin()) || (event4.getDeltaY() < 0.0d && this.vsb.getValue() < this.vsb.getMax())) {
                    this.vsb.setValue(newValue);
                    event4.consume();
                }
            }
            if (this.hsb.getVisibleAmount() < this.hsb.getMax()) {
                double hRange = ((ScrollPane) getSkinnable()).getHmax() - ((ScrollPane) getSkinnable()).getHmin();
                if (this.nodeWidth > 0.0d) {
                    hPixelValue = hRange / this.nodeWidth;
                } else {
                    hPixelValue = 0.0d;
                }
                double newValue2 = this.hsb.getValue() + ((-event4.getDeltaX()) * hPixelValue);
                if (!IS_TOUCH_SUPPORTED) {
                    if ((event4.getDeltaX() > 0.0d && this.hsb.getValue() > this.hsb.getMin()) || (event4.getDeltaX() < 0.0d && this.hsb.getValue() < this.hsb.getMax())) {
                        this.hsb.setValue(newValue2);
                        event4.consume();
                        return;
                    }
                    return;
                }
                if (event4.isInertia()) {
                    if (!event4.isInertia()) {
                        return;
                    }
                    if (this.contentsToViewTimeline != null && this.contentsToViewTimeline.getStatus() != Animation.Status.STOPPED) {
                        return;
                    }
                }
                this.hsb.setValue(newValue2);
                if ((newValue2 > this.hsb.getMax() || newValue2 < this.hsb.getMin()) && !this.mouseDown && !this.touchDetected) {
                    startContentsToViewport();
                }
                event4.consume();
            }
        });
        ((ScrollPane) getSkinnable()).addEventHandler(TouchEvent.TOUCH_PRESSED, e6 -> {
            this.touchDetected = true;
            startSBReleasedAnimation();
            e6.consume();
        });
        ((ScrollPane) getSkinnable()).addEventHandler(TouchEvent.TOUCH_RELEASED, e7 -> {
            this.touchDetected = false;
            e7.consume();
        });
        consumeMouseEvents(false);
        this.hsb.setValue(control.getHvalue());
        this.vsb.setValue(control.getVvalue());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("NODE".equals(p2)) {
            if (this.scrollNode != ((ScrollPane) getSkinnable()).getContent()) {
                if (this.scrollNode != null) {
                    this.scrollNode.layoutBoundsProperty().removeListener(this.nodeListener);
                    this.scrollNode.layoutBoundsProperty().removeListener(this.boundsChangeListener);
                    this.viewContent.getChildren().remove(this.scrollNode);
                }
                this.scrollNode = ((ScrollPane) getSkinnable()).getContent();
                if (this.scrollNode != null) {
                    this.nodeWidth = snapSize(this.scrollNode.getLayoutBounds().getWidth());
                    this.nodeHeight = snapSize(this.scrollNode.getLayoutBounds().getHeight());
                    this.viewContent.getChildren().setAll(this.scrollNode);
                    this.scrollNode.layoutBoundsProperty().addListener(this.nodeListener);
                    this.scrollNode.layoutBoundsProperty().addListener(this.boundsChangeListener);
                }
            }
            ((ScrollPane) getSkinnable()).requestLayout();
            return;
        }
        if ("FIT_TO_WIDTH".equals(p2) || "FIT_TO_HEIGHT".equals(p2)) {
            ((ScrollPane) getSkinnable()).requestLayout();
            this.viewRect.requestLayout();
            return;
        }
        if ("HBAR_POLICY".equals(p2) || "VBAR_POLICY".equals(p2)) {
            ((ScrollPane) getSkinnable()).requestLayout();
            return;
        }
        if ("HVALUE".equals(p2)) {
            this.hsb.setValue(((ScrollPane) getSkinnable()).getHvalue());
            return;
        }
        if ("HMAX".equals(p2)) {
            this.hsb.setMax(((ScrollPane) getSkinnable()).getHmax());
            return;
        }
        if ("HMIN".equals(p2)) {
            this.hsb.setMin(((ScrollPane) getSkinnable()).getHmin());
            return;
        }
        if ("VVALUE".equals(p2)) {
            this.vsb.setValue(((ScrollPane) getSkinnable()).getVvalue());
            return;
        }
        if ("VMAX".equals(p2)) {
            this.vsb.setMax(((ScrollPane) getSkinnable()).getVmax());
        } else if ("VMIN".equals(p2)) {
            this.vsb.setMin(((ScrollPane) getSkinnable()).getVmin());
        } else if ("VIEWPORT_SIZE_HINT".equals(p2)) {
            ((ScrollPane) getSkinnable()).requestLayout();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void scrollBoundsIntoView(Bounds b2) {
        double dx = 0.0d;
        double dy = 0.0d;
        if (b2.getMaxX() > this.contentWidth) {
            dx = b2.getMinX() - snappedLeftInset();
        }
        if (b2.getMinX() < snappedLeftInset()) {
            dx = (b2.getMaxX() - this.contentWidth) - snappedLeftInset();
        }
        if (b2.getMaxY() > snappedTopInset() + this.contentHeight) {
            dy = b2.getMinY() - snappedTopInset();
        }
        if (b2.getMinY() < snappedTopInset()) {
            dy = (b2.getMaxY() - this.contentHeight) - snappedTopInset();
        }
        if (dx != 0.0d) {
            double sdx = (dx * (this.hsb.getMax() - this.hsb.getMin())) / (this.nodeWidth - this.contentWidth);
            this.hsb.setValue(this.hsb.getValue() + sdx + ((((-1.0d) * Math.signum(sdx)) * this.hsb.getUnitIncrement()) / 5.0d));
            ((ScrollPane) getSkinnable()).requestLayout();
        }
        if (dy != 0.0d) {
            double sdy = (dy * (this.vsb.getMax() - this.vsb.getMin())) / (this.nodeHeight - this.contentHeight);
            this.vsb.setValue(this.vsb.getValue() + sdy + ((((-1.0d) * Math.signum(sdy)) * this.vsb.getUnitIncrement()) / 5.0d));
            ((ScrollPane) getSkinnable()).requestLayout();
        }
    }

    @Override // com.sun.javafx.scene.traversal.TraverseListener
    public void onTraverse(Node n2, Bounds b2) {
        scrollBoundsIntoView(b2);
    }

    public void hsbIncrement() {
        if (this.hsb != null) {
            this.hsb.increment();
        }
    }

    public void hsbDecrement() {
        if (this.hsb != null) {
            this.hsb.decrement();
        }
    }

    public void hsbPageIncrement() {
        if (this.hsb != null) {
            this.hsb.increment();
        }
    }

    public void hsbPageDecrement() {
        if (this.hsb != null) {
            this.hsb.decrement();
        }
    }

    public void vsbIncrement() {
        if (this.vsb != null) {
            this.vsb.increment();
        }
    }

    public void vsbDecrement() {
        if (this.vsb != null) {
            this.vsb.decrement();
        }
    }

    public void vsbPageIncrement() {
        if (this.vsb != null) {
            this.vsb.increment();
        }
    }

    public void vsbPageDecrement() {
        if (this.vsb != null) {
            this.vsb.decrement();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        ScrollPane sp = (ScrollPane) getSkinnable();
        double vsbWidth = computeVsbSizeHint(sp);
        double minWidth = vsbWidth + snappedLeftInset() + snappedRightInset();
        if (sp.getPrefViewportWidth() > 0.0d) {
            return sp.getPrefViewportWidth() + minWidth;
        }
        if (sp.getContent() != null) {
            return sp.getContent().prefWidth(height) + minWidth;
        }
        return Math.max(minWidth, DEFAULT_PREF_SIZE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        ScrollPane sp = (ScrollPane) getSkinnable();
        double hsbHeight = computeHsbSizeHint(sp);
        double minHeight = hsbHeight + snappedTopInset() + snappedBottomInset();
        if (sp.getPrefViewportHeight() > 0.0d) {
            return sp.getPrefViewportHeight() + minHeight;
        }
        if (sp.getContent() != null) {
            return sp.getContent().prefHeight(width) + minHeight;
        }
        return Math.max(minHeight, DEFAULT_PREF_SIZE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        ScrollPane sp = (ScrollPane) getSkinnable();
        double vsbWidth = computeVsbSizeHint(sp);
        double minWidth = vsbWidth + snappedLeftInset() + snappedRightInset();
        if (sp.getMinViewportWidth() > 0.0d) {
            return sp.getMinViewportWidth() + minWidth;
        }
        double w2 = this.corner.minWidth(-1.0d);
        return w2 > 0.0d ? 3.0d * w2 : DEFAULT_MIN_SIZE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        ScrollPane sp = (ScrollPane) getSkinnable();
        double hsbHeight = computeHsbSizeHint(sp);
        double minHeight = hsbHeight + snappedTopInset() + snappedBottomInset();
        if (sp.getMinViewportHeight() > 0.0d) {
            return sp.getMinViewportHeight() + minHeight;
        }
        double h2 = this.corner.minHeight(-1.0d);
        return h2 > 0.0d ? 3.0d * h2 : DEFAULT_MIN_SIZE;
    }

    private double computeHsbSizeHint(ScrollPane sp) {
        if (sp.getHbarPolicy() == ScrollPane.ScrollBarPolicy.ALWAYS || (sp.getHbarPolicy() == ScrollPane.ScrollBarPolicy.AS_NEEDED && (sp.getPrefViewportHeight() > 0.0d || sp.getMinViewportHeight() > 0.0d))) {
            return this.hsb.prefHeight(-1.0d);
        }
        return 0.0d;
    }

    private double computeVsbSizeHint(ScrollPane sp) {
        if (sp.getVbarPolicy() == ScrollPane.ScrollBarPolicy.ALWAYS || (sp.getVbarPolicy() == ScrollPane.ScrollBarPolicy.AS_NEEDED && (sp.getPrefViewportWidth() > 0.0d || sp.getMinViewportWidth() > 0.0d))) {
            return this.vsb.prefWidth(-1.0d);
        }
        return 0.0d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        ScrollPane control = (ScrollPane) getSkinnable();
        Insets padding = control.getPadding();
        double rightPadding = snapSize(padding.getRight());
        double leftPadding = snapSize(padding.getLeft());
        double topPadding = snapSize(padding.getTop());
        double bottomPadding = snapSize(padding.getBottom());
        this.vsb.setMin(control.getVmin());
        this.vsb.setMax(control.getVmax());
        this.hsb.setMin(control.getHmin());
        this.hsb.setMax(control.getHmax());
        this.contentWidth = w2;
        this.contentHeight = h2;
        double hsbWidth = 0.0d;
        double vsbHeight = 0.0d;
        computeScrollNodeSize(this.contentWidth, this.contentHeight);
        computeScrollBarSize();
        for (int i2 = 0; i2 < 2; i2++) {
            this.vsbvis = determineVerticalSBVisible();
            this.hsbvis = determineHorizontalSBVisible();
            if (this.vsbvis && !IS_TOUCH_SUPPORTED) {
                this.contentWidth = w2 - this.vsbWidth;
            }
            hsbWidth = ((w2 + leftPadding) + rightPadding) - (this.vsbvis ? this.vsbWidth : 0.0d);
            if (this.hsbvis && !IS_TOUCH_SUPPORTED) {
                this.contentHeight = h2 - this.hsbHeight;
            }
            vsbHeight = ((h2 + topPadding) + bottomPadding) - (this.hsbvis ? this.hsbHeight : 0.0d);
        }
        if (this.scrollNode != null && this.scrollNode.isResizable()) {
            if (this.vsbvis && this.hsbvis) {
                computeScrollNodeSize(this.contentWidth, this.contentHeight);
            } else if (this.hsbvis && !this.vsbvis) {
                computeScrollNodeSize(this.contentWidth, this.contentHeight);
                this.vsbvis = determineVerticalSBVisible();
                if (this.vsbvis) {
                    this.contentWidth -= this.vsbWidth;
                    hsbWidth -= this.vsbWidth;
                    computeScrollNodeSize(this.contentWidth, this.contentHeight);
                }
            } else if (this.vsbvis && !this.hsbvis) {
                computeScrollNodeSize(this.contentWidth, this.contentHeight);
                this.hsbvis = determineHorizontalSBVisible();
                if (this.hsbvis) {
                    this.contentHeight -= this.hsbHeight;
                    vsbHeight -= this.hsbHeight;
                    computeScrollNodeSize(this.contentWidth, this.contentHeight);
                }
            }
        }
        double cx = snappedLeftInset() - leftPadding;
        double cy = snappedTopInset() - topPadding;
        this.vsb.setVisible(this.vsbvis);
        if (this.vsbvis) {
            this.vsb.resizeRelocate(((snappedLeftInset() + w2) - this.vsbWidth) + (rightPadding < 1.0d ? 0.0d : rightPadding - 1.0d), cy, this.vsbWidth, vsbHeight);
        }
        updateVerticalSB();
        this.hsb.setVisible(this.hsbvis);
        if (this.hsbvis) {
            this.hsb.resizeRelocate(cx, ((snappedTopInset() + h2) - this.hsbHeight) + (bottomPadding < 1.0d ? 0.0d : bottomPadding - 1.0d), hsbWidth, this.hsbHeight);
        }
        updateHorizontalSB();
        this.viewRect.resizeRelocate(snappedLeftInset(), snappedTopInset(), snapSize(this.contentWidth), snapSize(this.contentHeight));
        resetClip();
        if (this.vsbvis && this.hsbvis) {
            this.corner.setVisible(true);
            double cornerWidth = this.vsbWidth;
            double cornerHeight = this.hsbHeight;
            this.corner.resizeRelocate(snapPosition(this.vsb.getLayoutX()), snapPosition(this.hsb.getLayoutY()), snapSize(cornerWidth), snapSize(cornerHeight));
        } else {
            this.corner.setVisible(false);
        }
        control.setViewportBounds(new BoundingBox(snapPosition(this.viewContent.getLayoutX()), snapPosition(this.viewContent.getLayoutY()), snapSize(this.contentWidth), snapSize(this.contentHeight)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void computeScrollNodeSize(double contentWidth, double contentHeight) {
        if (this.scrollNode != null) {
            if (this.scrollNode.isResizable()) {
                ScrollPane control = (ScrollPane) getSkinnable();
                Orientation bias = this.scrollNode.getContentBias();
                if (bias == null) {
                    this.nodeWidth = snapSize(Utils.boundedSize(control.isFitToWidth() ? contentWidth : this.scrollNode.prefWidth(-1.0d), this.scrollNode.minWidth(-1.0d), this.scrollNode.maxWidth(-1.0d)));
                    this.nodeHeight = snapSize(Utils.boundedSize(control.isFitToHeight() ? contentHeight : this.scrollNode.prefHeight(-1.0d), this.scrollNode.minHeight(-1.0d), this.scrollNode.maxHeight(-1.0d)));
                } else if (bias == Orientation.HORIZONTAL) {
                    this.nodeWidth = snapSize(Utils.boundedSize(control.isFitToWidth() ? contentWidth : this.scrollNode.prefWidth(-1.0d), this.scrollNode.minWidth(-1.0d), this.scrollNode.maxWidth(-1.0d)));
                    this.nodeHeight = snapSize(Utils.boundedSize(control.isFitToHeight() ? contentHeight : this.scrollNode.prefHeight(this.nodeWidth), this.scrollNode.minHeight(this.nodeWidth), this.scrollNode.maxHeight(this.nodeWidth)));
                } else {
                    this.nodeHeight = snapSize(Utils.boundedSize(control.isFitToHeight() ? contentHeight : this.scrollNode.prefHeight(-1.0d), this.scrollNode.minHeight(-1.0d), this.scrollNode.maxHeight(-1.0d)));
                    this.nodeWidth = snapSize(Utils.boundedSize(control.isFitToWidth() ? contentWidth : this.scrollNode.prefWidth(this.nodeHeight), this.scrollNode.minWidth(this.nodeHeight), this.scrollNode.maxWidth(this.nodeHeight)));
                }
            } else {
                this.nodeWidth = snapSize(this.scrollNode.getLayoutBounds().getWidth());
                this.nodeHeight = snapSize(this.scrollNode.getLayoutBounds().getHeight());
            }
            this.nodeSizeInvalid = false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isReverseNodeOrientation() {
        return (this.scrollNode == null || ((ScrollPane) getSkinnable()).getEffectiveNodeOrientation() == this.scrollNode.getEffectiveNodeOrientation()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean determineHorizontalSBVisible() {
        ScrollPane sp = (ScrollPane) getSkinnable();
        if (IS_TOUCH_SUPPORTED) {
            return this.tempVisibility && this.nodeWidth > this.contentWidth;
        }
        ScrollPane.ScrollBarPolicy hbarPolicy = sp.getHbarPolicy();
        if (ScrollPane.ScrollBarPolicy.NEVER == hbarPolicy) {
            return false;
        }
        if (ScrollPane.ScrollBarPolicy.ALWAYS == hbarPolicy) {
            return true;
        }
        return (sp.isFitToWidth() && this.scrollNode != null && this.scrollNode.isResizable()) ? this.nodeWidth > this.contentWidth && this.scrollNode.minWidth(-1.0d) > this.contentWidth : this.nodeWidth > this.contentWidth;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean determineVerticalSBVisible() {
        ScrollPane sp = (ScrollPane) getSkinnable();
        if (IS_TOUCH_SUPPORTED) {
            return this.tempVisibility && this.nodeHeight > this.contentHeight;
        }
        ScrollPane.ScrollBarPolicy vbarPolicy = sp.getVbarPolicy();
        if (ScrollPane.ScrollBarPolicy.NEVER == vbarPolicy) {
            return false;
        }
        if (ScrollPane.ScrollBarPolicy.ALWAYS == vbarPolicy) {
            return true;
        }
        return (sp.isFitToHeight() && this.scrollNode != null && this.scrollNode.isResizable()) ? this.nodeHeight > this.contentHeight && this.scrollNode.minHeight(-1.0d) > this.contentHeight : this.nodeHeight > this.contentHeight;
    }

    private void computeScrollBarSize() {
        this.vsbWidth = snapSize(this.vsb.prefWidth(-1.0d));
        if (this.vsbWidth == 0.0d) {
            if (IS_TOUCH_SUPPORTED) {
                this.vsbWidth = DEFAULT_EMBEDDED_SB_BREADTH;
            } else {
                this.vsbWidth = 12.0d;
            }
        }
        this.hsbHeight = snapSize(this.hsb.prefHeight(-1.0d));
        if (this.hsbHeight == 0.0d) {
            if (IS_TOUCH_SUPPORTED) {
                this.hsbHeight = DEFAULT_EMBEDDED_SB_BREADTH;
            } else {
                this.hsbHeight = 12.0d;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHorizontalSB() {
        double contentRatio = this.nodeWidth * (this.hsb.getMax() - this.hsb.getMin());
        if (contentRatio > 0.0d) {
            this.hsb.setVisibleAmount(this.contentWidth / contentRatio);
            this.hsb.setBlockIncrement(0.9d * this.hsb.getVisibleAmount());
            this.hsb.setUnitIncrement(0.1d * this.hsb.getVisibleAmount());
        } else {
            this.hsb.setVisibleAmount(0.0d);
            this.hsb.setBlockIncrement(0.0d);
            this.hsb.setUnitIncrement(0.0d);
        }
        if (this.hsb.isVisible()) {
            updatePosX();
        } else if (this.nodeWidth > this.contentWidth) {
            updatePosX();
        } else {
            this.viewContent.setLayoutX(0.0d);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVerticalSB() {
        double contentRatio = this.nodeHeight * (this.vsb.getMax() - this.vsb.getMin());
        if (contentRatio > 0.0d) {
            this.vsb.setVisibleAmount(this.contentHeight / contentRatio);
            this.vsb.setBlockIncrement(0.9d * this.vsb.getVisibleAmount());
            this.vsb.setUnitIncrement(0.1d * this.vsb.getVisibleAmount());
        } else {
            this.vsb.setVisibleAmount(0.0d);
            this.vsb.setBlockIncrement(0.0d);
            this.vsb.setUnitIncrement(0.0d);
        }
        if (this.vsb.isVisible()) {
            updatePosY();
        } else if (this.nodeHeight > this.contentHeight) {
            updatePosY();
        } else {
            this.viewContent.setLayoutY(0.0d);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private double updatePosX() {
        ScrollPane sp = (ScrollPane) getSkinnable();
        double x2 = isReverseNodeOrientation() ? this.hsb.getMax() - (this.posX - this.hsb.getMin()) : this.posX;
        double minX = Math.min(((-x2) / (this.hsb.getMax() - this.hsb.getMin())) * (this.nodeWidth - this.contentWidth), 0.0d);
        this.viewContent.setLayoutX(snapPosition(minX));
        if (!sp.hvalueProperty().isBound()) {
            sp.setHvalue(com.sun.javafx.util.Utils.clamp(sp.getHmin(), this.posX, sp.getHmax()));
        }
        return this.posX;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private double updatePosY() {
        ScrollPane sp = (ScrollPane) getSkinnable();
        double minY = Math.min(((-this.posY) / (this.vsb.getMax() - this.vsb.getMin())) * (this.nodeHeight - this.contentHeight), 0.0d);
        this.viewContent.setLayoutY(snapPosition(minY));
        if (!sp.vvalueProperty().isBound()) {
            sp.setVvalue(com.sun.javafx.util.Utils.clamp(sp.getVmin(), this.posY, sp.getVmax()));
        }
        return this.posY;
    }

    private void resetClip() {
        this.clipRect.setWidth(snapSize(this.contentWidth));
        this.clipRect.setHeight(snapSize(this.contentHeight));
    }

    protected void startSBReleasedAnimation() {
        if (this.sbTouchTimeline == null) {
            this.sbTouchTimeline = new Timeline();
            this.sbTouchKF1 = new KeyFrame(Duration.millis(0.0d), (EventHandler<ActionEvent>) event -> {
                this.tempVisibility = true;
                if (this.touchDetected || this.mouseDown) {
                    this.sbTouchTimeline.playFromStart();
                }
            }, new KeyValue[0]);
            this.sbTouchKF2 = new KeyFrame(Duration.millis(1000.0d), (EventHandler<ActionEvent>) event2 -> {
                this.tempVisibility = false;
                ((ScrollPane) getSkinnable()).requestLayout();
            }, new KeyValue[0]);
            this.sbTouchTimeline.getKeyFrames().addAll(this.sbTouchKF1, this.sbTouchKF2);
        }
        this.sbTouchTimeline.playFromStart();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void startContentsToViewport() {
        double newPosX = this.posX;
        double newPosY = this.posY;
        setContentPosX(this.posX);
        setContentPosY(this.posY);
        if (this.posY > ((ScrollPane) getSkinnable()).getVmax()) {
            newPosY = ((ScrollPane) getSkinnable()).getVmax();
        } else if (this.posY < ((ScrollPane) getSkinnable()).getVmin()) {
            newPosY = ((ScrollPane) getSkinnable()).getVmin();
        }
        if (this.posX > ((ScrollPane) getSkinnable()).getHmax()) {
            newPosX = ((ScrollPane) getSkinnable()).getHmax();
        } else if (this.posX < ((ScrollPane) getSkinnable()).getHmin()) {
            newPosX = ((ScrollPane) getSkinnable()).getHmin();
        }
        if (!IS_TOUCH_SUPPORTED) {
            startSBReleasedAnimation();
        }
        if (this.contentsToViewTimeline != null) {
            this.contentsToViewTimeline.stop();
        }
        this.contentsToViewTimeline = new Timeline();
        this.contentsToViewKF1 = new KeyFrame(Duration.millis(50.0d), new KeyValue[0]);
        this.contentsToViewKF2 = new KeyFrame(Duration.millis(150.0d), (EventHandler<ActionEvent>) event -> {
            ((ScrollPane) getSkinnable()).requestLayout();
        }, new KeyValue(this.contentPosX, Double.valueOf(newPosX)), new KeyValue(this.contentPosY, Double.valueOf(newPosY)));
        this.contentsToViewKF3 = new KeyFrame(Duration.millis(1500.0d), new KeyValue[0]);
        this.contentsToViewTimeline.getKeyFrames().addAll(this.contentsToViewKF1, this.contentsToViewKF2, this.contentsToViewKF3);
        this.contentsToViewTimeline.playFromStart();
    }

    private void setContentPosX(double value) {
        contentPosXProperty().set(value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getContentPosX() {
        if (this.contentPosX == null) {
            return 0.0d;
        }
        return this.contentPosX.get();
    }

    private DoubleProperty contentPosXProperty() {
        if (this.contentPosX == null) {
            this.contentPosX = new DoublePropertyBase() { // from class: com.sun.javafx.scene.control.skin.ScrollPaneSkin.5
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ScrollPaneSkin.this.hsb.setValue(ScrollPaneSkin.this.getContentPosX());
                    ((ScrollPane) ScrollPaneSkin.this.getSkinnable()).requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollPaneSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "contentPosX";
                }
            };
        }
        return this.contentPosX;
    }

    private void setContentPosY(double value) {
        contentPosYProperty().set(value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getContentPosY() {
        if (this.contentPosY == null) {
            return 0.0d;
        }
        return this.contentPosY.get();
    }

    private DoubleProperty contentPosYProperty() {
        if (this.contentPosY == null) {
            this.contentPosY = new DoublePropertyBase() { // from class: com.sun.javafx.scene.control.skin.ScrollPaneSkin.6
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    ScrollPaneSkin.this.vsb.setValue(ScrollPaneSkin.this.getContentPosY());
                    ((ScrollPane) ScrollPaneSkin.this.getSkinnable()).requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollPaneSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "contentPosY";
                }
            };
        }
        return this.contentPosY;
    }

    @Override // javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case VERTICAL_SCROLLBAR:
                return this.vsb;
            case HORIZONTAL_SCROLLBAR:
                return this.hsb;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
