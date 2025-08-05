package javafx.scene;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.GroupBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/GroupBuilder.class */
public class GroupBuilder<B extends GroupBuilder<B>> extends ParentBuilder<B> implements Builder<Group> {
    private int __set;
    private boolean autoSizeChildren;
    private Collection<? extends Node> children;

    protected GroupBuilder() {
    }

    public static GroupBuilder<?> create() {
        return new GroupBuilder<>();
    }

    public void applyTo(Group x2) {
        super.applyTo((Parent) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAutoSizeChildren(this.autoSizeChildren);
        }
        if ((set & 2) != 0) {
            x2.getChildren().addAll(this.children);
        }
    }

    public B autoSizeChildren(boolean x2) {
        this.autoSizeChildren = x2;
        this.__set |= 1;
        return this;
    }

    public B children(Collection<? extends Node> x2) {
        this.children = x2;
        this.__set |= 2;
        return this;
    }

    public B children(Node... nodeArr) {
        return (B) children(Arrays.asList(nodeArr));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Group build2() {
        Group x2 = new Group();
        applyTo(x2);
        return x2;
    }
}
