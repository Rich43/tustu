package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/gD.class */
class gD implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5896a;

    gD(fX fXVar) {
        this.f5896a = fXVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) throws IllegalArgumentException {
        String str = (String) this.f5896a.f5729e.getSelectedItem();
        this.f5896a.d();
        if (C0804hg.a().r() != null && str != null && !this.f5896a.f5757am && str.length() > 0) {
            this.f5896a.b("zDeltaTableGenField", str);
        }
        this.f5896a.u();
        this.f5896a.o();
    }
}
