package javafx.scene.control;

import javafx.scene.control.CellBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/CellBuilder.class */
public class CellBuilder<T, B extends CellBuilder<T, B>> extends LabeledBuilder<B> implements Builder<Cell<T>> {
    private int __set;
    private boolean editable;
    private T item;

    protected CellBuilder() {
    }

    public static <T> CellBuilder<T, ?> create() {
        return new CellBuilder<>();
    }

    public void applyTo(Cell<T> x2) {
        super.applyTo((Labeled) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setEditable(this.editable);
        }
        if ((set & 2) != 0) {
            x2.setItem(this.item);
        }
    }

    public B editable(boolean x2) {
        this.editable = x2;
        this.__set |= 1;
        return this;
    }

    public B item(T x2) {
        this.item = x2;
        this.__set |= 2;
        return this;
    }

    @Override // javafx.util.Builder
    public Cell<T> build() {
        Cell<T> x2 = new Cell<>();
        applyTo((Cell) x2);
        return x2;
    }
}
