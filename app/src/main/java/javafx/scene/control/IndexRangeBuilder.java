package javafx.scene.control;

import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/IndexRangeBuilder.class */
public final class IndexRangeBuilder implements Builder<IndexRange> {
    private int end;
    private int start;

    protected IndexRangeBuilder() {
    }

    public static IndexRangeBuilder create() {
        return new IndexRangeBuilder();
    }

    public IndexRangeBuilder end(int x2) {
        this.end = x2;
        return this;
    }

    public IndexRangeBuilder start(int x2) {
        this.start = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public IndexRange build() {
        IndexRange x2 = new IndexRange(this.start, this.end);
        return x2;
    }
}
