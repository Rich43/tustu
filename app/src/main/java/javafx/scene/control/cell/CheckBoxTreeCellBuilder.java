package javafx.scene.control.cell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeCellBuilder;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTreeCellBuilder;
import javafx.util.Callback;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/CheckBoxTreeCellBuilder.class */
public class CheckBoxTreeCellBuilder<T, B extends CheckBoxTreeCellBuilder<T, B>> extends TreeCellBuilder<T, B> {
    private int __set;
    private StringConverter<TreeItem<T>> converter;
    private Callback<TreeItem<T>, ObservableValue<Boolean>> selectedStateCallback;

    protected CheckBoxTreeCellBuilder() {
    }

    public static <T> CheckBoxTreeCellBuilder<T, ?> create() {
        return new CheckBoxTreeCellBuilder<>();
    }

    public void applyTo(CheckBoxTreeCell<T> x2) {
        super.applyTo((TreeCell) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setConverter(this.converter);
        }
        if ((set & 2) != 0) {
            x2.setSelectedStateCallback(this.selectedStateCallback);
        }
    }

    public B converter(StringConverter<TreeItem<T>> x2) {
        this.converter = x2;
        this.__set |= 1;
        return this;
    }

    public B selectedStateCallback(Callback<TreeItem<T>, ObservableValue<Boolean>> x2) {
        this.selectedStateCallback = x2;
        this.__set |= 2;
        return this;
    }

    @Override // javafx.scene.control.TreeCellBuilder, javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public CheckBoxTreeCell<T> build2() {
        CheckBoxTreeCell<T> x2 = new CheckBoxTreeCell<>();
        applyTo((CheckBoxTreeCell) x2);
        return x2;
    }
}
