package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.Cell;
import javafx.scene.control.TableCellBuilder;
import javafx.scene.control.cell.ChoiceBoxTableCellBuilder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/ChoiceBoxTableCellBuilder.class */
public class ChoiceBoxTableCellBuilder<S, T, B extends ChoiceBoxTableCellBuilder<S, T, B>> extends TableCellBuilder<S, T, B> {
    private int __set;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ChoiceBoxTableCellBuilder() {
    }

    public static <S, T> ChoiceBoxTableCellBuilder<S, T, ?> create() {
        return new ChoiceBoxTableCellBuilder<>();
    }

    public void applyTo(ChoiceBoxTableCell<S, T> x2) {
        super.applyTo((Cell) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setConverter(this.converter);
        }
        if ((set & 2) != 0) {
            x2.getItems().addAll(this.items);
        }
    }

    public B converter(StringConverter<T> x2) {
        this.converter = x2;
        this.__set |= 1;
        return this;
    }

    public B items(Collection<? extends T> x2) {
        this.items = x2;
        this.__set |= 2;
        return this;
    }

    public B items(T... tArr) {
        return (B) items(Arrays.asList(tArr));
    }

    @Override // javafx.scene.control.TableCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public ChoiceBoxTableCell<S, T> build2() {
        ChoiceBoxTableCell<S, T> x2 = new ChoiceBoxTableCell<>();
        applyTo((ChoiceBoxTableCell) x2);
        return x2;
    }
}
