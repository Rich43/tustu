package javafx.animation;

import javafx.animation.AnimationBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/AnimationBuilder.class */
public abstract class AnimationBuilder<B extends AnimationBuilder<B>> {
    private int __set;
    private boolean autoReverse;
    private int cycleCount;
    private Duration delay;
    private EventHandler<ActionEvent> onFinished;
    private double rate;
    private double targetFramerate;

    protected AnimationBuilder() {
    }

    public void applyTo(Animation x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAutoReverse(this.autoReverse);
        }
        if ((set & 2) != 0) {
            x2.setCycleCount(this.cycleCount);
        }
        if ((set & 4) != 0) {
            x2.setDelay(this.delay);
        }
        if ((set & 8) != 0) {
            x2.setOnFinished(this.onFinished);
        }
        if ((set & 16) != 0) {
            x2.setRate(this.rate);
        }
    }

    public B autoReverse(boolean x2) {
        this.autoReverse = x2;
        this.__set |= 1;
        return this;
    }

    public B cycleCount(int x2) {
        this.cycleCount = x2;
        this.__set |= 2;
        return this;
    }

    public B delay(Duration x2) {
        this.delay = x2;
        this.__set |= 4;
        return this;
    }

    public B onFinished(EventHandler<ActionEvent> x2) {
        this.onFinished = x2;
        this.__set |= 8;
        return this;
    }

    public B rate(double x2) {
        this.rate = x2;
        this.__set |= 16;
        return this;
    }

    public B targetFramerate(double x2) {
        this.targetFramerate = x2;
        return this;
    }
}
