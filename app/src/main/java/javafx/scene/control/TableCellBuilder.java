package javafx.scene.control;

import javafx.scene.control.TableCellBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TableCellBuilder.class */
public class TableCellBuilder<S, T, B extends TableCellBuilder<S, T, B>> extends CellBuilder<T, B> {
    protected TableCellBuilder() {
    }

    public static <S, T> TableCellBuilder<S, T, ?> create() {
        return new TableCellBuilder<>();
    }

    @Override // javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public TableCell<S, T> build2() {
        TableCell<S, T> x2 = new TableCell<>();
        applyTo((Cell) x2);
        return x2;
    }
}
