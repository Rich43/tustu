package com.sun.javafx.charts;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.chart.Axis;

/* loaded from: jfxrt.jar:com/sun/javafx/charts/ChartLayoutAnimator.class */
public final class ChartLayoutAnimator extends AnimationTimer implements EventHandler<ActionEvent> {
    private Parent nodeToLayout;
    private final Map<Object, Animation> activeTimeLines = new HashMap();
    private final boolean isAxis;

    public ChartLayoutAnimator(Parent nodeToLayout) {
        this.nodeToLayout = nodeToLayout;
        this.isAxis = nodeToLayout instanceof Axis;
    }

    @Override // javafx.animation.AnimationTimer
    public void handle(long l2) {
        if (this.isAxis) {
            ((Axis) this.nodeToLayout).requestAxisLayout();
        } else {
            this.nodeToLayout.requestLayout();
        }
    }

    @Override // javafx.event.EventHandler
    public void handle(ActionEvent actionEvent) {
        this.activeTimeLines.remove(actionEvent.getSource());
        if (this.activeTimeLines.isEmpty()) {
            stop();
        }
        handle(0L);
    }

    public void stop(Object animationID) {
        Animation t2 = this.activeTimeLines.remove(animationID);
        if (t2 != null) {
            t2.stop();
        }
        if (this.activeTimeLines.isEmpty()) {
            stop();
        }
    }

    public Object animate(KeyFrame... keyFrames) {
        Timeline t2 = new Timeline();
        t2.setAutoReverse(false);
        t2.setCycleCount(1);
        t2.getKeyFrames().addAll(keyFrames);
        t2.setOnFinished(this);
        if (this.activeTimeLines.isEmpty()) {
            start();
        }
        this.activeTimeLines.put(t2, t2);
        t2.play();
        return t2;
    }

    public Object animate(Animation animation) {
        SequentialTransition t2 = new SequentialTransition();
        t2.getChildren().add(animation);
        t2.setOnFinished(this);
        if (this.activeTimeLines.isEmpty()) {
            start();
        }
        this.activeTimeLines.put(t2, t2);
        t2.play();
        return t2;
    }
}
