package javafx.scene.control.cell;

import javafx.scene.control.Cell;
import javafx.scene.control.ListCellBuilder;
import javafx.scene.control.cell.TextFieldListCellBuilder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/TextFieldListCellBuilder.class */
public class TextFieldListCellBuilder<T, B extends TextFieldListCellBuilder<T, B>> extends ListCellBuilder<T, B> {
    private boolean __set;
    private StringConverter<T> converter;

    protected TextFieldListCellBuilder() {
    }

    public static <T> TextFieldListCellBuilder<T, ?> create() {
        return new TextFieldListCellBuilder<>();
    }

    public void applyTo(TextFieldListCell<T> x2) {
        super.applyTo((Cell) x2);
        if (this.__set) {
            x2.setConverter(this.converter);
        }
    }

    public B converter(StringConverter<T> x2) {
        this.converter = x2;
        this.__set = true;
        return this;
    }

    @Override // javafx.scene.control.ListCellBuilder, javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public TextFieldListCell<T> build2() {
        TextFieldListCell<T> x2 = new TextFieldListCell<>();
        applyTo((TextFieldListCell) x2);
        return x2;
    }
}
