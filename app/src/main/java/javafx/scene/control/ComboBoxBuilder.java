package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBoxBuilder;
import javafx.util.Builder;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ComboBoxBuilder.class */
public class ComboBoxBuilder<T, B extends ComboBoxBuilder<T, B>> extends ComboBoxBaseBuilder<T, B> implements Builder<ComboBox<T>> {
    private int __set;
    private ListCell<T> buttonCell;
    private Callback<ListView<T>, ListCell<T>> cellFactory;
    private StringConverter<T> converter;
    private ObservableList<T> items;
    private SingleSelectionModel<T> selectionModel;
    private int visibleRowCount;

    protected ComboBoxBuilder() {
    }

    public static <T> ComboBoxBuilder<T, ?> create() {
        return new ComboBoxBuilder<>();
    }

    public void applyTo(ComboBox<T> x2) {
        super.applyTo((ComboBoxBase) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setButtonCell(this.buttonCell);
        }
        if ((set & 2) != 0) {
            x2.setCellFactory(this.cellFactory);
        }
        if ((set & 4) != 0) {
            x2.setConverter(this.converter);
        }
        if ((set & 8) != 0) {
            x2.setItems(this.items);
        }
        if ((set & 16) != 0) {
            x2.setSelectionModel(this.selectionModel);
        }
        if ((set & 32) != 0) {
            x2.setVisibleRowCount(this.visibleRowCount);
        }
    }

    public B buttonCell(ListCell<T> x2) {
        this.buttonCell = x2;
        this.__set |= 1;
        return this;
    }

    public B cellFactory(Callback<ListView<T>, ListCell<T>> x2) {
        this.cellFactory = x2;
        this.__set |= 2;
        return this;
    }

    public B converter(StringConverter<T> x2) {
        this.converter = x2;
        this.__set |= 4;
        return this;
    }

    public B items(ObservableList<T> x2) {
        this.items = x2;
        this.__set |= 8;
        return this;
    }

    public B selectionModel(SingleSelectionModel<T> x2) {
        this.selectionModel = x2;
        this.__set |= 16;
        return this;
    }

    public B visibleRowCount(int x2) {
        this.visibleRowCount = x2;
        this.__set |= 32;
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public ComboBox<T> build2() {
        ComboBox<T> x2 = new ComboBox<>();
        applyTo((ComboBox) x2);
        return x2;
    }
}
