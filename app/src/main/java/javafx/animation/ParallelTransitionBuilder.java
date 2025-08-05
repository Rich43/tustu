package javafx.animation;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/ParallelTransitionBuilder.class */
public final class ParallelTransitionBuilder extends TransitionBuilder<ParallelTransitionBuilder> implements Builder<ParallelTransition> {
    private int __set;
    private Collection<? extends Animation> children;
    private Node node;

    protected ParallelTransitionBuilder() {
    }

    public static ParallelTransitionBuilder create() {
        return new ParallelTransitionBuilder();
    }

    public void applyTo(ParallelTransition x2) {
        super.applyTo((Transition) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getChildren().addAll(this.children);
        }
        if ((set & 2) != 0) {
            x2.setNode(this.node);
        }
    }

    public ParallelTransitionBuilder children(Collection<? extends Animation> x2) {
        this.children = x2;
        this.__set |= 1;
        return this;
    }

    public ParallelTransitionBuilder children(Animation... x2) {
        return children(Arrays.asList(x2));
    }

    public ParallelTransitionBuilder node(Node x2) {
        this.node = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ParallelTransition build2() {
        ParallelTransition x2 = new ParallelTransition();
        applyTo(x2);
        return x2;
    }
}
