package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.DatePicker;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/DatePickerBehavior.class */
public class DatePickerBehavior extends ComboBoxBaseBehavior<LocalDate> {
    protected static final List<KeyBinding> DATE_PICKER_BINDINGS = new ArrayList();

    public DatePickerBehavior(DatePicker datePicker) {
        super(datePicker, DATE_PICKER_BINDINGS);
    }

    static {
        DATE_PICKER_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
    }

    @Override // com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior
    public void onAutoHide() {
        DatePicker datePicker = (DatePicker) getControl();
        DatePickerSkin cpSkin = (DatePickerSkin) datePicker.getSkin();
        cpSkin.syncWithAutoUpdate();
        if (!datePicker.isShowing()) {
            super.onAutoHide();
        }
    }
}
