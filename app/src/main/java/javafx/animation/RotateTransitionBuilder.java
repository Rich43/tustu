package javafx.animation;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/RotateTransitionBuilder.class */
public final class RotateTransitionBuilder extends TransitionBuilder<RotateTransitionBuilder> implements Builder<RotateTransition> {
    private int __set;
    private Point3D axis;
    private double byAngle;
    private Duration duration;
    private double fromAngle;
    private Node node;
    private double toAngle;

    protected RotateTransitionBuilder() {
    }

    public static RotateTransitionBuilder create() {
        return new RotateTransitionBuilder();
    }

    public void applyTo(RotateTransition x2) {
        super.applyTo((Transition) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAxis(this.axis);
        }
        if ((set & 2) != 0) {
            x2.setByAngle(this.byAngle);
        }
        if ((set & 4) != 0) {
            x2.setDuration(this.duration);
        }
        if ((set & 8) != 0) {
            x2.setFromAngle(this.fromAngle);
        }
        if ((set & 16) != 0) {
            x2.setNode(this.node);
        }
        if ((set & 32) != 0) {
            x2.setToAngle(this.toAngle);
        }
    }

    public RotateTransitionBuilder axis(Point3D x2) {
        this.axis = x2;
        this.__set |= 1;
        return this;
    }

    public RotateTransitionBuilder byAngle(double x2) {
        this.byAngle = x2;
        this.__set |= 2;
        return this;
    }

    public RotateTransitionBuilder duration(Duration x2) {
        this.duration = x2;
        this.__set |= 4;
        return this;
    }

    public RotateTransitionBuilder fromAngle(double x2) {
        this.fromAngle = x2;
        this.__set |= 8;
        return this;
    }

    public RotateTransitionBuilder node(Node x2) {
        this.node = x2;
        this.__set |= 16;
        return this;
    }

    public RotateTransitionBuilder toAngle(double x2) {
        this.toAngle = x2;
        this.__set |= 32;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public RotateTransition build2() {
        RotateTransition x2 = new RotateTransition();
        applyTo(x2);
        return x2;
    }
}
