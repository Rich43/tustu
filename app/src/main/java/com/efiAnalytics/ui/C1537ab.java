package com.efiAnalytics.ui;

import javax.swing.table.DefaultTableCellRenderer;

/* renamed from: com.efiAnalytics.ui.ab, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ab.class */
class C1537ab extends DefaultTableCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ S f10822a;

    public C1537ab(S s2) {
        this.f10822a = s2;
        setHorizontalAlignment(0);
    }

    @Override // javax.swing.JLabel
    public String getText() {
        String text = super.getText();
        if (text != null) {
            return bH.W.a(text, this.f10822a.f10697a ? this.f10822a.f10701e.q() : this.f10822a.f10701e.r());
        }
        return text;
    }
}
