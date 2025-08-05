package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.PaginationBuilder;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/PaginationBuilder.class */
public class PaginationBuilder<B extends PaginationBuilder<B>> extends ControlBuilder<B> implements Builder<Pagination> {
    private int __set;
    private int currentPageIndex;
    private int maxPageIndicatorCount;
    private int pageCount;
    private Callback<Integer, Node> pageFactory;

    protected PaginationBuilder() {
    }

    public static PaginationBuilder<?> create() {
        return new PaginationBuilder<>();
    }

    public void applyTo(Pagination x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setCurrentPageIndex(this.currentPageIndex);
        }
        if ((set & 2) != 0) {
            x2.setMaxPageIndicatorCount(this.maxPageIndicatorCount);
        }
        if ((set & 4) != 0) {
            x2.setPageCount(this.pageCount);
        }
        if ((set & 8) != 0) {
            x2.setPageFactory(this.pageFactory);
        }
    }

    public B currentPageIndex(int x2) {
        this.currentPageIndex = x2;
        this.__set |= 1;
        return this;
    }

    public B maxPageIndicatorCount(int x2) {
        this.maxPageIndicatorCount = x2;
        this.__set |= 2;
        return this;
    }

    public B pageCount(int x2) {
        this.pageCount = x2;
        this.__set |= 4;
        return this;
    }

    public B pageFactory(Callback<Integer, Node> x2) {
        this.pageFactory = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Pagination build2() {
        Pagination x2 = new Pagination();
        applyTo(x2);
        return x2;
    }
}
