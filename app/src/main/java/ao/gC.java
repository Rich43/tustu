package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/gC.class */
class gC implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5895a;

    gC(fX fXVar) {
        this.f5895a = fXVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) throws IllegalArgumentException {
        String str = (String) this.f5895a.f5728d.getSelectedItem();
        this.f5895a.b().c(str);
        this.f5895a.d();
        if (C0804hg.a().r() != null && str != null && !this.f5895a.f5757am && str.length() > 0) {
            this.f5895a.b("zTableGenField", str);
        }
        this.f5895a.u();
        this.f5895a.o();
    }
}
