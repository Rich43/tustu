package javafx.animation;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/SequentialTransitionBuilder.class */
public final class SequentialTransitionBuilder extends TransitionBuilder<SequentialTransitionBuilder> implements Builder<SequentialTransition> {
    private int __set;
    private Collection<? extends Animation> children;
    private Node node;

    protected SequentialTransitionBuilder() {
    }

    public static SequentialTransitionBuilder create() {
        return new SequentialTransitionBuilder();
    }

    public void applyTo(SequentialTransition x2) {
        super.applyTo((Transition) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getChildren().addAll(this.children);
        }
        if ((set & 2) != 0) {
            x2.setNode(this.node);
        }
    }

    public SequentialTransitionBuilder children(Collection<? extends Animation> x2) {
        this.children = x2;
        this.__set |= 1;
        return this;
    }

    public SequentialTransitionBuilder children(Animation... x2) {
        return children(Arrays.asList(x2));
    }

    public SequentialTransitionBuilder node(Node x2) {
        this.node = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public SequentialTransition build2() {
        SequentialTransition x2 = new SequentialTransition();
        applyTo(x2);
        return x2;
    }
}
