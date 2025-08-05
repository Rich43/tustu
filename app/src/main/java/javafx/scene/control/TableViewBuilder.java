package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TableViewBuilder;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TableViewBuilder.class */
public class TableViewBuilder<S, B extends TableViewBuilder<S, B>> extends ControlBuilder<B> implements Builder<TableView<S>> {
    private int __set;
    private Callback<TableView.ResizeFeatures, Boolean> columnResizePolicy;
    private Collection<? extends TableColumn<S, ?>> columns;
    private boolean editable;
    private TableView.TableViewFocusModel<S> focusModel;
    private ObservableList<S> items;
    private Node placeholder;
    private Callback<TableView<S>, TableRow<S>> rowFactory;
    private TableView.TableViewSelectionModel<S> selectionModel;
    private Collection<? extends TableColumn<S, ?>> sortOrder;
    private boolean tableMenuButtonVisible;

    protected TableViewBuilder() {
    }

    public static <S> TableViewBuilder<S, ?> create() {
        return new TableViewBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(TableView<S> x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setColumnResizePolicy(this.columnResizePolicy);
                    break;
                case 1:
                    x2.getColumns().addAll(this.columns);
                    break;
                case 2:
                    x2.setEditable(this.editable);
                    break;
                case 3:
                    x2.setFocusModel(this.focusModel);
                    break;
                case 4:
                    x2.setItems(this.items);
                    break;
                case 5:
                    x2.setPlaceholder(this.placeholder);
                    break;
                case 6:
                    x2.setRowFactory(this.rowFactory);
                    break;
                case 7:
                    x2.setSelectionModel(this.selectionModel);
                    break;
                case 8:
                    x2.getSortOrder().addAll(this.sortOrder);
                    break;
                case 9:
                    x2.setTableMenuButtonVisible(this.tableMenuButtonVisible);
                    break;
            }
        }
    }

    public B columnResizePolicy(Callback<TableView.ResizeFeatures, Boolean> x2) {
        this.columnResizePolicy = x2;
        __set(0);
        return this;
    }

    public B columns(Collection<? extends TableColumn<S, ?>> x2) {
        this.columns = x2;
        __set(1);
        return this;
    }

    public B columns(TableColumn<S, ?>... tableColumnArr) {
        return (B) columns(Arrays.asList(tableColumnArr));
    }

    public B editable(boolean x2) {
        this.editable = x2;
        __set(2);
        return this;
    }

    public B focusModel(TableView.TableViewFocusModel<S> x2) {
        this.focusModel = x2;
        __set(3);
        return this;
    }

    public B items(ObservableList<S> x2) {
        this.items = x2;
        __set(4);
        return this;
    }

    public B placeholder(Node x2) {
        this.placeholder = x2;
        __set(5);
        return this;
    }

    public B rowFactory(Callback<TableView<S>, TableRow<S>> x2) {
        this.rowFactory = x2;
        __set(6);
        return this;
    }

    public B selectionModel(TableView.TableViewSelectionModel<S> x2) {
        this.selectionModel = x2;
        __set(7);
        return this;
    }

    public B sortOrder(Collection<? extends TableColumn<S, ?>> x2) {
        this.sortOrder = x2;
        __set(8);
        return this;
    }

    public B sortOrder(TableColumn<S, ?>... tableColumnArr) {
        return (B) sortOrder(Arrays.asList(tableColumnArr));
    }

    public B tableMenuButtonVisible(boolean x2) {
        this.tableMenuButtonVisible = x2;
        __set(9);
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public TableView<S> build2() {
        TableView<S> x2 = new TableView<>();
        applyTo((TableView) x2);
        return x2;
    }
}
