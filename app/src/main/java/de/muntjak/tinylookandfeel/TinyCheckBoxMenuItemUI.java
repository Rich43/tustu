package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyCheckBoxMenuItemUI.class */
public class TinyCheckBoxMenuItemUI extends TinyMenuItemUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyCheckBoxMenuItemUI();
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected String getPropertyPrefix() {
        return "CheckBoxMenuItem";
    }
}
