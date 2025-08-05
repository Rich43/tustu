package ao;

import W.C0184j;
import g.C1733k;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.W, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/W.class */
final class C0605W implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0184j f5110a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ Component f5111b;

    C0605W(C0184j c0184j, Component component) {
        this.f5110a = c0184j;
        this.f5111b = component;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        String strA = C1733k.a("{Set " + this.f5110a.a() + " Smoothing Factor}", true, "Smoothing Factor: Larger Number for more Smoothing", true, this.f5111b);
        if (strA == null || strA.isEmpty()) {
            return;
        }
        int i2 = Integer.parseInt(strA);
        if (i2 < 1) {
            C0804hg.a().a(this.f5110a);
        } else {
            C0804hg.a().a(this.f5110a, i2);
        }
    }
}
