package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBuilder;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TableColumnBuilder.class */
public class TableColumnBuilder<S, T, B extends TableColumnBuilder<S, T, B>> implements Builder<TableColumn<S, T>> {
    private int __set;
    private Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory;
    private Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> cellValueFactory;
    private Collection<? extends TableColumn<S, ?>> columns;
    private Comparator<T> comparator;
    private ContextMenu contextMenu;
    private boolean editable;
    private Node graphic;
    private String id;
    private double maxWidth;
    private double minWidth;
    private EventHandler<TableColumn.CellEditEvent<S, T>> onEditCancel;
    private EventHandler<TableColumn.CellEditEvent<S, T>> onEditCommit;
    private EventHandler<TableColumn.CellEditEvent<S, T>> onEditStart;
    private double prefWidth;
    private boolean resizable;
    private boolean sortable;
    private Node sortNode;
    private TableColumn.SortType sortType;
    private String style;
    private Collection<? extends String> styleClass;
    private String text;
    private Object userData;
    private boolean visible;

    protected TableColumnBuilder() {
    }

    public static <S, T> TableColumnBuilder<S, T, ?> create() {
        return new TableColumnBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(TableColumn<S, T> x2) {
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setCellFactory(this.cellFactory);
                    break;
                case 1:
                    x2.setCellValueFactory(this.cellValueFactory);
                    break;
                case 2:
                    x2.getColumns().addAll(this.columns);
                    break;
                case 3:
                    x2.setComparator(this.comparator);
                    break;
                case 4:
                    x2.setContextMenu(this.contextMenu);
                    break;
                case 5:
                    x2.setEditable(this.editable);
                    break;
                case 6:
                    x2.setGraphic(this.graphic);
                    break;
                case 7:
                    x2.setId(this.id);
                    break;
                case 8:
                    x2.setMaxWidth(this.maxWidth);
                    break;
                case 9:
                    x2.setMinWidth(this.minWidth);
                    break;
                case 10:
                    x2.setOnEditCancel(this.onEditCancel);
                    break;
                case 11:
                    x2.setOnEditCommit(this.onEditCommit);
                    break;
                case 12:
                    x2.setOnEditStart(this.onEditStart);
                    break;
                case 13:
                    x2.setPrefWidth(this.prefWidth);
                    break;
                case 14:
                    x2.setResizable(this.resizable);
                    break;
                case 15:
                    x2.setSortable(this.sortable);
                    break;
                case 16:
                    x2.setSortNode(this.sortNode);
                    break;
                case 17:
                    x2.setSortType(this.sortType);
                    break;
                case 18:
                    x2.setStyle(this.style);
                    break;
                case 19:
                    x2.getStyleClass().addAll(this.styleClass);
                    break;
                case 20:
                    x2.setText(this.text);
                    break;
                case 21:
                    x2.setUserData(this.userData);
                    break;
                case 22:
                    x2.setVisible(this.visible);
                    break;
            }
        }
    }

    public B cellFactory(Callback<TableColumn<S, T>, TableCell<S, T>> x2) {
        this.cellFactory = x2;
        __set(0);
        return this;
    }

    public B cellValueFactory(Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> x2) {
        this.cellValueFactory = x2;
        __set(1);
        return this;
    }

    public B columns(Collection<? extends TableColumn<S, ?>> x2) {
        this.columns = x2;
        __set(2);
        return this;
    }

    public B columns(TableColumn<S, ?>... tableColumnArr) {
        return (B) columns(Arrays.asList(tableColumnArr));
    }

    public B comparator(Comparator<T> x2) {
        this.comparator = x2;
        __set(3);
        return this;
    }

    public B contextMenu(ContextMenu x2) {
        this.contextMenu = x2;
        __set(4);
        return this;
    }

    public B editable(boolean x2) {
        this.editable = x2;
        __set(5);
        return this;
    }

    public B graphic(Node x2) {
        this.graphic = x2;
        __set(6);
        return this;
    }

    public B id(String x2) {
        this.id = x2;
        __set(7);
        return this;
    }

    public B maxWidth(double x2) {
        this.maxWidth = x2;
        __set(8);
        return this;
    }

    public B minWidth(double x2) {
        this.minWidth = x2;
        __set(9);
        return this;
    }

    public B onEditCancel(EventHandler<TableColumn.CellEditEvent<S, T>> x2) {
        this.onEditCancel = x2;
        __set(10);
        return this;
    }

    public B onEditCommit(EventHandler<TableColumn.CellEditEvent<S, T>> x2) {
        this.onEditCommit = x2;
        __set(11);
        return this;
    }

    public B onEditStart(EventHandler<TableColumn.CellEditEvent<S, T>> x2) {
        this.onEditStart = x2;
        __set(12);
        return this;
    }

    public B prefWidth(double x2) {
        this.prefWidth = x2;
        __set(13);
        return this;
    }

    public B resizable(boolean x2) {
        this.resizable = x2;
        __set(14);
        return this;
    }

    public B sortable(boolean x2) {
        this.sortable = x2;
        __set(15);
        return this;
    }

    public B sortNode(Node x2) {
        this.sortNode = x2;
        __set(16);
        return this;
    }

    public B sortType(TableColumn.SortType x2) {
        this.sortType = x2;
        __set(17);
        return this;
    }

    public B style(String x2) {
        this.style = x2;
        __set(18);
        return this;
    }

    public B styleClass(Collection<? extends String> x2) {
        this.styleClass = x2;
        __set(19);
        return this;
    }

    public B styleClass(String... strArr) {
        return (B) styleClass(Arrays.asList(strArr));
    }

    public B text(String x2) {
        this.text = x2;
        __set(20);
        return this;
    }

    public B userData(Object x2) {
        this.userData = x2;
        __set(21);
        return this;
    }

    public B visible(boolean x2) {
        this.visible = x2;
        __set(22);
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public TableColumn<S, T> build2() {
        TableColumn<S, T> x2 = new TableColumn<>();
        applyTo(x2);
        return x2;
    }
}
