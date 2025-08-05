package javafx.scene.control.cell;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeCellBuilder;
import javafx.scene.control.cell.TextFieldTreeCellBuilder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/TextFieldTreeCellBuilder.class */
public class TextFieldTreeCellBuilder<T, B extends TextFieldTreeCellBuilder<T, B>> extends TreeCellBuilder<T, B> {
    private boolean __set;
    private StringConverter<T> converter;

    protected TextFieldTreeCellBuilder() {
    }

    public static <T> TextFieldTreeCellBuilder<T, ?> create() {
        return new TextFieldTreeCellBuilder<>();
    }

    public void applyTo(TextFieldTreeCell<T> x2) {
        super.applyTo((TreeCell) x2);
        if (this.__set) {
            x2.setConverter(this.converter);
        }
    }

    public B converter(StringConverter<T> x2) {
        this.converter = x2;
        this.__set = true;
        return this;
    }

    @Override // javafx.scene.control.TreeCellBuilder, javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public TextFieldTreeCell<T> build2() {
        TextFieldTreeCell<T> x2 = new TextFieldTreeCell<>();
        applyTo((TextFieldTreeCell) x2);
        return x2;
    }
}
