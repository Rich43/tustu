package javafx.scene.control;

import javafx.scene.control.TableRowBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TableRowBuilder.class */
public class TableRowBuilder<T, B extends TableRowBuilder<T, B>> extends IndexedCellBuilder<T, B> {
    protected TableRowBuilder() {
    }

    public static <T> TableRowBuilder<T, ?> create() {
        return new TableRowBuilder<>();
    }

    @Override // javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public TableRow<T> build2() {
        TableRow<T> x2 = new TableRow<>();
        applyTo((Cell) x2);
        return x2;
    }
}
