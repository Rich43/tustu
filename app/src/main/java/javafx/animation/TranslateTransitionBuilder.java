package javafx.animation;

import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/TranslateTransitionBuilder.class */
public final class TranslateTransitionBuilder extends TransitionBuilder<TranslateTransitionBuilder> implements Builder<TranslateTransition> {
    private int __set;
    private double byX;
    private double byY;
    private double byZ;
    private Duration duration;
    private double fromX;
    private double fromY;
    private double fromZ;
    private Node node;
    private double toX;
    private double toY;
    private double toZ;

    protected TranslateTransitionBuilder() {
    }

    public static TranslateTransitionBuilder create() {
        return new TranslateTransitionBuilder();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(TranslateTransition x2) {
        super.applyTo((Transition) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setByX(this.byX);
                    break;
                case 1:
                    x2.setByY(this.byY);
                    break;
                case 2:
                    x2.setByZ(this.byZ);
                    break;
                case 3:
                    x2.setDuration(this.duration);
                    break;
                case 4:
                    x2.setFromX(this.fromX);
                    break;
                case 5:
                    x2.setFromY(this.fromY);
                    break;
                case 6:
                    x2.setFromZ(this.fromZ);
                    break;
                case 7:
                    x2.setNode(this.node);
                    break;
                case 8:
                    x2.setToX(this.toX);
                    break;
                case 9:
                    x2.setToY(this.toY);
                    break;
                case 10:
                    x2.setToZ(this.toZ);
                    break;
            }
        }
    }

    public TranslateTransitionBuilder byX(double x2) {
        this.byX = x2;
        __set(0);
        return this;
    }

    public TranslateTransitionBuilder byY(double x2) {
        this.byY = x2;
        __set(1);
        return this;
    }

    public TranslateTransitionBuilder byZ(double x2) {
        this.byZ = x2;
        __set(2);
        return this;
    }

    public TranslateTransitionBuilder duration(Duration x2) {
        this.duration = x2;
        __set(3);
        return this;
    }

    public TranslateTransitionBuilder fromX(double x2) {
        this.fromX = x2;
        __set(4);
        return this;
    }

    public TranslateTransitionBuilder fromY(double x2) {
        this.fromY = x2;
        __set(5);
        return this;
    }

    public TranslateTransitionBuilder fromZ(double x2) {
        this.fromZ = x2;
        __set(6);
        return this;
    }

    public TranslateTransitionBuilder node(Node x2) {
        this.node = x2;
        __set(7);
        return this;
    }

    public TranslateTransitionBuilder toX(double x2) {
        this.toX = x2;
        __set(8);
        return this;
    }

    public TranslateTransitionBuilder toY(double x2) {
        this.toY = x2;
        __set(9);
        return this;
    }

    public TranslateTransitionBuilder toZ(double x2) {
        this.toZ = x2;
        __set(10);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public TranslateTransition build2() {
        TranslateTransition x2 = new TranslateTransition();
        applyTo(x2);
        return x2;
    }
}
