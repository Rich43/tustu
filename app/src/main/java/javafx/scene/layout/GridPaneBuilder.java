package javafx.scene.layout;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPaneBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/GridPaneBuilder.class */
public class GridPaneBuilder<B extends GridPaneBuilder<B>> extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private Collection<? extends ColumnConstraints> columnConstraints;
    private boolean gridLinesVisible;
    private double hgap;
    private Collection<? extends RowConstraints> rowConstraints;
    private double vgap;

    protected GridPaneBuilder() {
    }

    public static GridPaneBuilder<?> create() {
        return new GridPaneBuilder<>();
    }

    public void applyTo(GridPane x2) {
        super.applyTo((Pane) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAlignment(this.alignment);
        }
        if ((set & 2) != 0) {
            x2.getColumnConstraints().addAll(this.columnConstraints);
        }
        if ((set & 4) != 0) {
            x2.setGridLinesVisible(this.gridLinesVisible);
        }
        if ((set & 8) != 0) {
            x2.setHgap(this.hgap);
        }
        if ((set & 16) != 0) {
            x2.getRowConstraints().addAll(this.rowConstraints);
        }
        if ((set & 32) != 0) {
            x2.setVgap(this.vgap);
        }
    }

    public B alignment(Pos x2) {
        this.alignment = x2;
        this.__set |= 1;
        return this;
    }

    public B columnConstraints(Collection<? extends ColumnConstraints> x2) {
        this.columnConstraints = x2;
        this.__set |= 2;
        return this;
    }

    public B columnConstraints(ColumnConstraints... columnConstraintsArr) {
        return (B) columnConstraints(Arrays.asList(columnConstraintsArr));
    }

    public B gridLinesVisible(boolean x2) {
        this.gridLinesVisible = x2;
        this.__set |= 4;
        return this;
    }

    public B hgap(double x2) {
        this.hgap = x2;
        this.__set |= 8;
        return this;
    }

    public B rowConstraints(Collection<? extends RowConstraints> x2) {
        this.rowConstraints = x2;
        this.__set |= 16;
        return this;
    }

    public B rowConstraints(RowConstraints... rowConstraintsArr) {
        return (B) rowConstraints(Arrays.asList(rowConstraintsArr));
    }

    public B vgap(double x2) {
        this.vgap = x2;
        this.__set |= 32;
        return this;
    }

    @Override // javafx.scene.layout.PaneBuilder, javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public GridPane build2() {
        GridPane x2 = new GridPane();
        applyTo(x2);
        return x2;
    }
}
