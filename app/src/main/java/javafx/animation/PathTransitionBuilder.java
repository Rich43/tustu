package javafx.animation;

import javafx.animation.PathTransition;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/PathTransitionBuilder.class */
public final class PathTransitionBuilder extends TransitionBuilder<PathTransitionBuilder> implements Builder<PathTransition> {
    private int __set;
    private Duration duration;
    private Node node;
    private PathTransition.OrientationType orientation;
    private Shape path;

    protected PathTransitionBuilder() {
    }

    public static PathTransitionBuilder create() {
        return new PathTransitionBuilder();
    }

    public void applyTo(PathTransition x2) {
        super.applyTo((Transition) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setDuration(this.duration);
        }
        if ((set & 2) != 0) {
            x2.setNode(this.node);
        }
        if ((set & 4) != 0) {
            x2.setOrientation(this.orientation);
        }
        if ((set & 8) != 0) {
            x2.setPath(this.path);
        }
    }

    public PathTransitionBuilder duration(Duration x2) {
        this.duration = x2;
        this.__set |= 1;
        return this;
    }

    public PathTransitionBuilder node(Node x2) {
        this.node = x2;
        this.__set |= 2;
        return this;
    }

    public PathTransitionBuilder orientation(PathTransition.OrientationType x2) {
        this.orientation = x2;
        this.__set |= 4;
        return this;
    }

    public PathTransitionBuilder path(Shape x2) {
        this.path = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public PathTransition build2() {
        PathTransition x2 = new PathTransition();
        applyTo(x2);
        return x2;
    }
}
