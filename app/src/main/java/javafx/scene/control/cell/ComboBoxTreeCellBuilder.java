package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeCellBuilder;
import javafx.scene.control.cell.ComboBoxTreeCellBuilder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/ComboBoxTreeCellBuilder.class */
public class ComboBoxTreeCellBuilder<T, B extends ComboBoxTreeCellBuilder<T, B>> extends TreeCellBuilder<T, B> {
    private int __set;
    private boolean comboBoxEditable;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ComboBoxTreeCellBuilder() {
    }

    public static <T> ComboBoxTreeCellBuilder<T, ?> create() {
        return new ComboBoxTreeCellBuilder<>();
    }

    public void applyTo(ComboBoxTreeCell<T> x2) {
        super.applyTo((TreeCell) x2);
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

    @Override // javafx.scene.control.TreeCellBuilder, javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public ComboBoxTreeCell<T> build2() {
        ComboBoxTreeCell<T> x2 = new ComboBoxTreeCell<>();
        applyTo((ComboBoxTreeCell) x2);
        return x2;
    }
}
