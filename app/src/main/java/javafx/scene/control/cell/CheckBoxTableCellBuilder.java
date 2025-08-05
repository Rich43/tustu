package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Cell;
import javafx.scene.control.TableCellBuilder;
import javafx.scene.control.cell.CheckBoxTableCellBuilder;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/CheckBoxTableCellBuilder.class */
public class CheckBoxTableCellBuilder<S, T, B extends CheckBoxTableCellBuilder<S, T, B>> extends TableCellBuilder<S, T, B> {
    private int __set;
    private StringConverter<T> converter;
    private Callback<Integer, ObservableValue<Boolean>> selectedStateCallback;

    protected CheckBoxTableCellBuilder() {
    }

    public static <S, T> CheckBoxTableCellBuilder<S, T, ?> create() {
        return new CheckBoxTableCellBuilder<>();
    }

    public void applyTo(CheckBoxTableCell<S, T> x2) {
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

    public B selectedStateCallback(Callback<Integer, ObservableValue<Boolean>> x2) {
        this.selectedStateCallback = x2;
        this.__set |= 2;
        return this;
    }

    @Override // javafx.scene.control.TableCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public CheckBoxTableCell<S, T> build2() {
        CheckBoxTableCell<S, T> x2 = new CheckBoxTableCell<>();
        applyTo((CheckBoxTableCell) x2);
        return x2;
    }
}
