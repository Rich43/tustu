package javafx.scene.control;

import com.sun.javafx.scene.control.skin.DateCellSkin;
import java.time.LocalDate;

/* loaded from: jfxrt.jar:javafx/scene/control/DateCell.class */
public class DateCell extends Cell<LocalDate> {
    private static final String DEFAULT_STYLE_CLASS = "date-cell";

    public DateCell() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    @Override // javafx.scene.control.Cell
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem((DateCell) item, empty);
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new DateCellSkin(this);
    }
}
