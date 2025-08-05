package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyListUI.class */
public class TinyListUI extends BasicListUI {
    private JComponent list;

    public TinyListUI() {
    }

    public TinyListUI(JComponent jComponent) {
        this.list = jComponent;
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyListUI(jComponent);
    }
}
