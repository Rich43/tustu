package javafx.scene.control;

import javafx.scene.control.TablePositionBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TablePositionBuilder.class */
public class TablePositionBuilder<S, T, B extends TablePositionBuilder<S, T, B>> implements Builder<TablePosition<S, T>> {
    private int row;
    private TableColumn<S, T> tableColumn;
    private TableView<S> tableView;

    protected TablePositionBuilder() {
    }

    public static <S, T> TablePositionBuilder<S, T, ?> create() {
        return new TablePositionBuilder<>();
    }

    public B row(int x2) {
        this.row = x2;
        return this;
    }

    public B tableColumn(TableColumn<S, T> x2) {
        this.tableColumn = x2;
        return this;
    }

    public B tableView(TableView<S> x2) {
        this.tableView = x2;
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public TablePosition<S, T> build2() {
        TablePosition<S, T> x2 = new TablePosition<>(this.tableView, this.row, this.tableColumn);
        return x2;
    }
}
