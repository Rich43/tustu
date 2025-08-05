package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCellBuilder;
import javafx.scene.control.cell.CheckBoxListCellBuilder;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/CheckBoxListCellBuilder.class */
public class CheckBoxListCellBuilder<T, B extends CheckBoxListCellBuilder<T, B>> extends ListCellBuilder<T, B> {
    private int __set;
    private StringConverter<T> converter;
    private Callback<T, ObservableValue<Boolean>> selectedStateCallback;

    protected CheckBoxListCellBuilder() {
    }

    public static <T> CheckBoxListCellBuilder<T, ?> create() {
        return new CheckBoxListCellBuilder<>();
    }

    public void applyTo(CheckBoxListCell<T> x2) {
        super.applyTo((Cell) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setConverter(this.converter);
        }
        if ((set & 2) != 0) {
            x2.setSelectedStateCallback(this.selectedStateCallback);
        }
    }

    public B converter(StringConverter<T> x2) {
        this.converter = x2;
        this.__set |= 1;
        return this;
    }

    public B selectedStateCallback(Callback<T, ObservableValue<Boolean>> x2) {
        this.selectedStateCallback = x2;
        this.__set |= 2;
        return this;
    }

    @Override // javafx.scene.control.ListCellBuilder, javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public CheckBoxListCell<T> build2() {
        CheckBoxListCell<T> x2 = new CheckBoxListCell<>();
        applyTo((CheckBoxListCell) x2);
        return x2;
    }
}
