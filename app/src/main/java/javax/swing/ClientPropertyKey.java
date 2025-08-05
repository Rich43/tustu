package javax.swing;

import sun.awt.AWTAccessor;

/* loaded from: rt.jar:javax/swing/ClientPropertyKey.class */
enum ClientPropertyKey {
    JComponent_INPUT_VERIFIER(true),
    JComponent_TRANSFER_HANDLER(true),
    JComponent_ANCESTOR_NOTIFIER(true),
    PopupFactory_FORCE_HEAVYWEIGHT_POPUP(true);

    private final boolean reportValueNotSerializable;

    static {
        AWTAccessor.setClientPropertyKeyAccessor(new AWTAccessor.ClientPropertyKeyAccessor() { // from class: javax.swing.ClientPropertyKey.1
            @Override // sun.awt.AWTAccessor.ClientPropertyKeyAccessor
            public Object getJComponent_TRANSFER_HANDLER() {
                return ClientPropertyKey.JComponent_TRANSFER_HANDLER;
            }
        });
    }

    ClientPropertyKey() {
        this(false);
    }

    ClientPropertyKey(boolean z2) {
        this.reportValueNotSerializable = z2;
    }

    public boolean getReportValueNotSerializable() {
        return this.reportValueNotSerializable;
    }
}
