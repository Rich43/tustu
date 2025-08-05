package com.efiAnalytics.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;

/* renamed from: com.efiAnalytics.ui.ee, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ee.class */
public class C1647ee implements FocusListener {
    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        if (focusEvent.getSource() instanceof JTextComponent) {
            ((JTextComponent) focusEvent.getSource()).selectAll();
        }
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
