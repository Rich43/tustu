package javafx.scene.control;

import javafx.scene.control.TableColumnBase;

/* loaded from: jfxrt.jar:javafx/scene/control/TableFocusModel.class */
public abstract class TableFocusModel<T, TC extends TableColumnBase<T, ?>> extends FocusModel<T> {
    public abstract void focus(int i2, TC tc);

    public abstract boolean isFocused(int i2, TC tc);

    public abstract void focusAboveCell();

    public abstract void focusBelowCell();

    public abstract void focusLeftCell();

    public abstract void focusRightCell();
}
