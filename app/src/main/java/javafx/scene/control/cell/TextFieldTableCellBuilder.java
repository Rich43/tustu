package javafx.scene.control.cell;

import javafx.scene.control.Cell;
import javafx.scene.control.TableCellBuilder;
import javafx.scene.control.cell.TextFieldTableCellBuilder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/TextFieldTableCellBuilder.class */
public class TextFieldTableCellBuilder<S, T, B extends TextFieldTableCellBuilder<S, T, B>> extends TableCellBuilder<S, T, B> {
    private boolean __set;
    private StringConverter<T> converter;

    protected TextFieldTableCellBuilder() {
    }

    public static <S, T> TextFieldTableCellBuilder<S, T, ?> create() {
        return new TextFieldTableCellBuilder<>();
    }

    public void applyTo(TextFieldTableCell<S, T> x2) {
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

    @Override // javafx.scene.control.TableCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public TextFieldTableCell<S, T> build2() {
        TextFieldTableCell<S, T> x2 = new TextFieldTableCell<>();
        applyTo((TextFieldTableCell) x2);
        return x2;
    }
}
