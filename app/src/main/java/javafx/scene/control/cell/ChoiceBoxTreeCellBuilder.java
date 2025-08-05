package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeCellBuilder;
import javafx.scene.control.cell.ChoiceBoxTreeCellBuilder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/ChoiceBoxTreeCellBuilder.class */
public class ChoiceBoxTreeCellBuilder<T, B extends ChoiceBoxTreeCellBuilder<T, B>> extends TreeCellBuilder<T, B> {
    private int __set;
    private StringConverter<T> converter;
    private Collection<? extends T> items;

    protected ChoiceBoxTreeCellBuilder() {
    }

    public static <T> ChoiceBoxTreeCellBuilder<T, ?> create() {
        return new ChoiceBoxTreeCellBuilder<>();
    }

    public void applyTo(ChoiceBoxTreeCell<T> x2) {
        super.applyTo((TreeCell) x2);
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

    @Override // javafx.scene.control.TreeCellBuilder, javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public ChoiceBoxTreeCell<T> build2() {
        ChoiceBoxTreeCell<T> x2 = new ChoiceBoxTreeCell<>();
        applyTo((ChoiceBoxTreeCell) x2);
        return x2;
    }
}
