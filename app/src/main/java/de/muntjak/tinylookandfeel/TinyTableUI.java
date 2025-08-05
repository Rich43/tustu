package de.muntjak.tinylookandfeel;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTableUI.class */
public class TinyTableUI extends BasicTableUI {
    JTable table;

    public TinyTableUI() {
    }

    public TinyTableUI(JComponent jComponent) {
        this.table = (JTable) jComponent;
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyTableUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicTableUI
    protected void installDefaults() {
        super.installDefaults();
    }
}
