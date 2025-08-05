package javafx.scene.control;

import java.lang.ref.WeakReference;
import java.util.List;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/scene/control/TablePosition.class */
public class TablePosition<S, T> extends TablePositionBase<TableColumn<S, T>> {
    private final WeakReference<TableView<S>> controlRef;
    private final WeakReference<S> itemRef;
    int fixedColumnIndex;
    private final int nonFixedColumnIndex;

    /* JADX WARN: Multi-variable type inference failed */
    public TablePosition(@NamedArg("tableView") TableView<S> tableView, @NamedArg("row") int row, @NamedArg("tableColumn") TableColumn<S, T> tableColumn) {
        super(row, tableColumn);
        this.fixedColumnIndex = -1;
        this.controlRef = new WeakReference<>(tableView);
        List<S> items = tableView.getItems();
        this.itemRef = new WeakReference<>((items == null || row < 0 || row >= items.size()) ? null : items.get(row));
        this.nonFixedColumnIndex = (tableView == null || tableColumn == 0) ? -1 : tableView.getVisibleLeafIndex(tableColumn);
    }

    @Override // javafx.scene.control.TablePositionBase
    public int getColumn() {
        if (this.fixedColumnIndex > -1) {
            return this.fixedColumnIndex;
        }
        return this.nonFixedColumnIndex;
    }

    public final TableView<S> getTableView() {
        return this.controlRef.get();
    }

    @Override // javafx.scene.control.TablePositionBase
    public final TableColumn<S, T> getTableColumn() {
        return (TableColumn) super.getTableColumn();
    }

    final S getItem() {
        if (this.itemRef == null) {
            return null;
        }
        return this.itemRef.get();
    }

    public String toString() {
        return "TablePosition [ row: " + getRow() + ", column: " + ((Object) getTableColumn()) + ", tableView: " + ((Object) getTableView()) + " ]";
    }
}
