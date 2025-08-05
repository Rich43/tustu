package javafx.scene.chart;

import javafx.collections.ObservableList;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/chart/CategoryAxisBuilder.class */
public final class CategoryAxisBuilder extends AxisBuilder<String, CategoryAxisBuilder> {
    private int __set;
    private ObservableList<String> categories;
    private double endMargin;
    private boolean gapStartAndEnd;
    private double startMargin;

    protected CategoryAxisBuilder() {
    }

    public static CategoryAxisBuilder create() {
        return new CategoryAxisBuilder();
    }

    public void applyTo(CategoryAxis x2) {
        super.applyTo((Axis) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setCategories(this.categories);
        }
        if ((set & 2) != 0) {
            x2.setEndMargin(this.endMargin);
        }
        if ((set & 4) != 0) {
            x2.setGapStartAndEnd(this.gapStartAndEnd);
        }
        if ((set & 8) != 0) {
            x2.setStartMargin(this.startMargin);
        }
    }

    public CategoryAxisBuilder categories(ObservableList<String> x2) {
        this.categories = x2;
        this.__set |= 1;
        return this;
    }

    public CategoryAxisBuilder endMargin(double x2) {
        this.endMargin = x2;
        this.__set |= 2;
        return this;
    }

    public CategoryAxisBuilder gapStartAndEnd(boolean x2) {
        this.gapStartAndEnd = x2;
        this.__set |= 4;
        return this;
    }

    public CategoryAxisBuilder startMargin(double x2) {
        this.startMargin = x2;
        this.__set |= 8;
        return this;
    }

    @Override // javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public CategoryAxis build2() {
        CategoryAxis x2 = new CategoryAxis();
        applyTo(x2);
        return x2;
    }
}
