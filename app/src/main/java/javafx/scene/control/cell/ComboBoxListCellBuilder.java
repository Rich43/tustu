package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCellBuilder;
import javafx.scene.control.cell.ComboBoxListCellBuilder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/ComboBoxListCellBuilder.class */
public class ComboBoxListCellBuilder<T, B extends ComboBoxListCellBuilder<T, B>> extends ListCellBuilder<T, B> {
    private int __set;
    private boolean comboBoxEditable;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ComboBoxListCellBuilder() {
    }

    public static <T> ComboBoxListCellBuilder<T, ?> create() {
        return new ComboBoxListCellBuilder<>();
    }

    public void applyTo(ComboBoxListCell<T> x2) {
        super.applyTo((Cell) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setComboBoxEditable(this.comboBoxEditable);
        }
        if ((set & 2) != 0) {
            x2.setConverter(this.converter);
        }
        if ((set & 4) != 0) {
            x2.getItems().addAll(this.items);
        }
    }

    public B comboBoxEditable(boolean x2) {
        this.comboBoxEditable = x2;
        this.__set |= 1;
        return this;
    }

    public B converter(StringConverter<T> x2) {
        this.converter = x2;
        this.__set |= 2;
        return this;
    }

    public B items(Collection<? extends T> x2) {
        this.items = x2;
        this.__set |= 4;
        return this;
    }

    public B items(T... tArr) {
        return (B) items(Arrays.asList(tArr));
    }

    @Override // javafx.scene.control.ListCellBuilder, javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public ComboBoxListCell<T> build2() {
        ComboBoxListCell<T> x2 = new ComboBoxListCell<>();
        applyTo((ComboBoxListCell) x2);
        return x2;
    }
}
