package javafx.scene.control;

import javafx.scene.control.IndexedCellBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/IndexedCellBuilder.class */
public class IndexedCellBuilder<T, B extends IndexedCellBuilder<T, B>> extends CellBuilder<T, B> {
    protected IndexedCellBuilder() {
    }

    public static <T> IndexedCellBuilder<T, ?> create() {
        return new IndexedCellBuilder<>();
    }

    @Override // javafx.scene.control.CellBuilder, javafx.util.Builder
    public IndexedCell<T> build() {
        IndexedCell<T> x2 = new IndexedCell<>();
        applyTo((Cell) x2);
        return x2;
    }
}
