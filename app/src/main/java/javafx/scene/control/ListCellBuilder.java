package javafx.scene.control;

import javafx.scene.control.ListCellBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ListCellBuilder.class */
public class ListCellBuilder<T, B extends ListCellBuilder<T, B>> extends IndexedCellBuilder<T, B> {
    protected ListCellBuilder() {
    }

    public static <T> ListCellBuilder<T, ?> create() {
        return new ListCellBuilder<>();
    }

    @Override // javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public ListCell<T> build2() {
        ListCell<T> x2 = new ListCell<>();
        applyTo((Cell) x2);
        return x2;
    }
}
