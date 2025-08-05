package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.bV;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aT.class */
class aT extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aQ f10379a;

    aT(aQ aQVar) {
        this.f10379a = aQVar;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((JTextField) focusEvent.getSource()).selectAll();
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        if (focusEvent.getSource() == this.f10379a.f10376b) {
            try {
                this.f10379a.f10375a.setText(((Integer.parseInt(this.f10379a.f10376b.getText()) * 22) / 10) + "");
            } catch (NumberFormatException e2) {
                bV.d("Weight must be numeric.", this.f10379a.f10376b);
            }
        }
        if (focusEvent.getSource() == this.f10379a.f10375a) {
            try {
                this.f10379a.f10376b.setText(((Integer.parseInt(this.f10379a.f10375a.getText()) * 10) / 22) + "");
            } catch (NumberFormatException e3) {
                bV.d("Weight must be numeric.", this.f10379a.f10375a);
            }
        }
    }
}
