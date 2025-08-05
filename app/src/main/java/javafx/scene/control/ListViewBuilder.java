package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.ListViewBuilder;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ListViewBuilder.class */
public class ListViewBuilder<T, B extends ListViewBuilder<T, B>> extends ControlBuilder<B> implements Builder<ListView<T>> {
    private int __set;
    private Callback<ListView<T>, ListCell<T>> cellFactory;
    private boolean editable;
    private FocusModel<T> focusModel;
    private ObservableList<T> items;
    private EventHandler<ListView.EditEvent<T>> onEditCancel;
    private EventHandler<ListView.EditEvent<T>> onEditCommit;
    private EventHandler<ListView.EditEvent<T>> onEditStart;
    private Orientation orientation;
    private MultipleSelectionModel<T> selectionModel;

    protected ListViewBuilder() {
    }

    public static <T> ListViewBuilder<T, ?> create() {
        return new ListViewBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(ListView<T> x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setCellFactory(this.cellFactory);
                    break;
                case 1:
                    x2.setEditable(this.editable);
                    break;
                case 2:
                    x2.setFocusModel(this.focusModel);
                    break;
                case 3:
                    x2.setItems(this.items);
                    break;
                case 4:
                    x2.setOnEditCancel(this.onEditCancel);
                    break;
                case 5:
                    x2.setOnEditCommit(this.onEditCommit);
                    break;
                case 6:
                    x2.setOnEditStart(this.onEditStart);
                    break;
                case 7:
                    x2.setOrientation(this.orientation);
                    break;
                case 8:
                    x2.setSelectionModel(this.selectionModel);
                    break;
            }
        }
    }

    public B cellFactory(Callback<ListView<T>, ListCell<T>> x2) {
        this.cellFactory = x2;
        __set(0);
        return this;
    }

    public B editable(boolean x2) {
        this.editable = x2;
        __set(1);
        return this;
    }

    public B focusModel(FocusModel<T> x2) {
        this.focusModel = x2;
        __set(2);
        return this;
    }

    public B items(ObservableList<T> x2) {
        this.items = x2;
        __set(3);
        return this;
    }

    public B onEditCancel(EventHandler<ListView.EditEvent<T>> x2) {
        this.onEditCancel = x2;
        __set(4);
        return this;
    }

    public B onEditCommit(EventHandler<ListView.EditEvent<T>> x2) {
        this.onEditCommit = x2;
        __set(5);
        return this;
    }

    public B onEditStart(EventHandler<ListView.EditEvent<T>> x2) {
        this.onEditStart = x2;
        __set(6);
        return this;
    }

    public B orientation(Orientation x2) {
        this.orientation = x2;
        __set(7);
        return this;
    }

    public B selectionModel(MultipleSelectionModel<T> x2) {
        this.selectionModel = x2;
        __set(8);
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public ListView<T> build2() {
        ListView<T> x2 = new ListView<>();
        applyTo((ListView) x2);
        return x2;
    }
}
