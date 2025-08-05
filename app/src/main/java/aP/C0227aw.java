package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.aw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/aw.class */
class C0227aw implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0224at f2955a;

    C0227aw(C0224at c0224at) {
        this.f2955a = c0224at;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws IllegalArgumentException {
        boolean zIsSelected = this.f2955a.f2937f.isSelected();
        this.f2955a.f2940i.setEnabled(zIsSelected);
        this.f2955a.f2934c.setEnabled(zIsSelected);
        this.f2955a.f2935d.setEnabled(!zIsSelected);
        if (zIsSelected) {
            this.f2955a.m();
        }
    }
}
