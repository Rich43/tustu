package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/SpinnerBehavior.class */
public class SpinnerBehavior<T> extends BehaviorBase<Spinner<T>> {
    private static final double INITIAL_DURATION_MS = 750.0d;
    private final int STEP_AMOUNT = 1;
    private boolean isIncrementing;
    private Timeline timeline;
    final EventHandler<ActionEvent> spinningKeyFrameEventHandler;
    private boolean keyDown;
    protected static final List<KeyBinding> SPINNER_BINDINGS = new ArrayList();

    public SpinnerBehavior(Spinner<T> spinner) {
        super(spinner, SPINNER_BINDINGS);
        this.STEP_AMOUNT = 1;
        this.isIncrementing = false;
        this.spinningKeyFrameEventHandler = event -> {
            SpinnerValueFactory<T> valueFactory = getControl().getValueFactory();
            if (valueFactory == null) {
                return;
            }
            if (this.isIncrementing) {
                increment(1);
            } else {
                decrement(1);
            }
        };
    }

    static {
        SPINNER_BINDINGS.add(new KeyBinding(KeyCode.UP, "increment-up"));
        SPINNER_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "increment-right"));
        SPINNER_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "decrement-left"));
        SPINNER_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "decrement-down"));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        boolean vertical;
        vertical = arrowsAreVertical();
        switch (name) {
            case "increment-up":
                if (!vertical) {
                    traverseUp();
                    break;
                } else {
                    increment(1);
                    break;
                }
            case "increment-right":
                if (!vertical) {
                    increment(1);
                    break;
                } else {
                    traverseRight();
                    break;
                }
            case "decrement-down":
                if (!vertical) {
                    traverseDown();
                    break;
                } else {
                    decrement(1);
                    break;
                }
            case "decrement-left":
                if (!vertical) {
                    decrement(1);
                    break;
                } else {
                    traverseLeft();
                    break;
                }
            default:
                super.callAction(name);
                break;
        }
    }

    public void increment(int steps) {
        getControl().increment(steps);
    }

    public void decrement(int steps) {
        getControl().decrement(steps);
    }

    public void startSpinning(boolean increment) {
        this.isIncrementing = increment;
        if (this.timeline != null) {
            this.timeline.stop();
        }
        this.timeline = new Timeline();
        this.timeline.setCycleCount(-1);
        KeyFrame kf = new KeyFrame(Duration.millis(INITIAL_DURATION_MS), this.spinningKeyFrameEventHandler, new KeyValue[0]);
        this.timeline.getKeyFrames().setAll(kf);
        this.timeline.playFromStart();
        this.timeline.play();
        this.spinningKeyFrameEventHandler.handle(null);
    }

    public void stopSpinning() {
        if (this.timeline != null) {
            this.timeline.stop();
        }
    }

    private boolean arrowsAreVertical() {
        List<String> styleClass = getControl().getStyleClass();
        return (styleClass.contains(Spinner.STYLE_CLASS_ARROWS_ON_LEFT_HORIZONTAL) || styleClass.contains(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL) || styleClass.contains(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL)) ? false : true;
    }
}
