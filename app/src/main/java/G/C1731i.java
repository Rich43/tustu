package g;

import com.efiAnalytics.ui.C1621de;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: g.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/i.class */
class C1731i implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    C1621de f12211a;

    /* renamed from: b, reason: collision with root package name */
    C1732j f12212b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1729g f12213c;

    C1731i(C1729g c1729g, C1621de c1621de, C1732j c1732j) {
        this.f12213c = c1729g;
        this.f12211a = c1621de;
        this.f12212b = c1732j;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f12212b.c((String) this.f12211a.getSelectedItem());
    }
}
