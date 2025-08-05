package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ScrollBarBehavior.class */
public class ScrollBarBehavior extends BehaviorBase<ScrollBar> {
    protected static final List<KeyBinding> SCROLL_BAR_BINDINGS = new ArrayList();
    Timeline timeline;

    public ScrollBarBehavior(ScrollBar scrollBar) {
        super(scrollBar, SCROLL_BAR_BINDINGS);
    }

    void home() {
        getControl().setValue(getControl().getMin());
    }

    void decrementValue() {
        getControl().adjustValue(0.0d);
    }

    void end() {
        getControl().setValue(getControl().getMax());
    }

    void incrementValue() {
        getControl().adjustValue(1.0d);
    }

    static {
        SCROLL_BAR_BINDINGS.add(new KeyBinding(KeyCode.F4, "TraverseDebug").alt().ctrl().shift());
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.LEFT, "DecrementValue"));
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_LEFT, "DecrementValue"));
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.UP, "DecrementValue").vertical());
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_UP, "DecrementValue").vertical());
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.RIGHT, "IncrementValue"));
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_RIGHT, "IncrementValue"));
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.DOWN, "IncrementValue").vertical());
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_DOWN, "IncrementValue").vertical());
        SCROLL_BAR_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_RELEASED, "Home"));
        SCROLL_BAR_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_RELEASED, "End"));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected String matchActionForEvent(KeyEvent e2) {
        String action = super.matchActionForEvent(e2);
        if (action != null) {
            if (e2.getCode() == KeyCode.LEFT || e2.getCode() == KeyCode.KP_LEFT) {
                if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    action = getControl().getOrientation() == Orientation.HORIZONTAL ? "IncrementValue" : "DecrementValue";
                }
            } else if ((e2.getCode() == KeyCode.RIGHT || e2.getCode() == KeyCode.KP_RIGHT) && getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                action = getControl().getOrientation() == Orientation.HORIZONTAL ? "DecrementValue" : "IncrementValue";
            }
        }
        return action;
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if ("Home".equals(name)) {
            home();
        } else if ("End".equals(name)) {
            end();
        } else if ("IncrementValue".equals(name)) {
            incrementValue();
        } else if ("DecrementValue".equals(name)) {
            decrementValue();
        } else {
            super.callAction(name);
        }
        super.callAction(name);
    }

    public void trackPress(double position) {
        if (this.timeline != null) {
            return;
        }
        ScrollBar bar = getControl();
        if (!bar.isFocused() && bar.isFocusTraversable()) {
            bar.requestFocus();
        }
        boolean incrementing = position > (bar.getValue() - bar.getMin()) / (bar.getMax() - bar.getMin());
        this.timeline = new Timeline();
        this.timeline.setCycleCount(-1);
        EventHandler<ActionEvent> step = event -> {
            boolean i2 = position > (bar.getValue() - bar.getMin()) / (bar.getMax() - bar.getMin());
            if (incrementing == i2) {
                bar.adjustValue(position);
            } else {
                stopTimeline();
            }
        };
        KeyFrame kf = new KeyFrame(Duration.millis(200.0d), step, new KeyValue[0]);
        this.timeline.getKeyFrames().add(kf);
        this.timeline.play();
        step.handle(null);
    }

    public void trackRelease() {
        stopTimeline();
    }

    public void decButtonPressed() {
        ScrollBar bar = getControl();
        if (!bar.isFocused() && bar.isFocusTraversable()) {
            bar.requestFocus();
        }
        stopTimeline();
        this.timeline = new Timeline();
        this.timeline.setCycleCount(-1);
        EventHandler<ActionEvent> dec = event -> {
            if (bar.getValue() > bar.getMin()) {
                bar.decrement();
            } else {
                stopTimeline();
            }
        };
        KeyFrame kf = new KeyFrame(Duration.millis(200.0d), dec, new KeyValue[0]);
        this.timeline.getKeyFrames().add(kf);
        this.timeline.play();
        dec.handle(null);
    }

    public void decButtonReleased() {
        stopTimeline();
    }

    public void incButtonPressed() {
        ScrollBar bar = getControl();
        if (!bar.isFocused() && bar.isFocusTraversable()) {
            bar.requestFocus();
        }
        stopTimeline();
        this.timeline = new Timeline();
        this.timeline.setCycleCount(-1);
        EventHandler<ActionEvent> inc = event -> {
            if (bar.getValue() < bar.getMax()) {
                bar.increment();
            } else {
                stopTimeline();
            }
        };
        KeyFrame kf = new KeyFrame(Duration.millis(200.0d), inc, new KeyValue[0]);
        this.timeline.getKeyFrames().add(kf);
        this.timeline.play();
        inc.handle(null);
    }

    public void incButtonReleased() {
        stopTimeline();
    }

    public void thumbDragged(double position) {
        ScrollBar scrollbar = getControl();
        stopTimeline();
        if (!scrollbar.isFocused() && scrollbar.isFocusTraversable()) {
            scrollbar.requestFocus();
        }
        double newValue = (position * (scrollbar.getMax() - scrollbar.getMin())) + scrollbar.getMin();
        if (!Double.isNaN(newValue)) {
            scrollbar.setValue(Utils.clamp(scrollbar.getMin(), newValue, scrollbar.getMax()));
        }
    }

    private void stopTimeline() {
        if (this.timeline != null) {
            this.timeline.stop();
            this.timeline = null;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ScrollBarBehavior$ScrollBarKeyBinding.class */
    public static class ScrollBarKeyBinding extends OrientedKeyBinding {
        public ScrollBarKeyBinding(KeyCode code, String action) {
            super(code, action);
        }

        public ScrollBarKeyBinding(KeyCode code, EventType<KeyEvent> type, String action) {
            super(code, type, action);
        }

        @Override // com.sun.javafx.scene.control.behavior.OrientedKeyBinding
        public boolean getVertical(Control control) {
            return ((ScrollBar) control).getOrientation() == Orientation.VERTICAL;
        }
    }
}
