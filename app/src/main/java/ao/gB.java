package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/gB.class */
class gB implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5894a;

    gB(fX fXVar) {
        this.f5894a = fXVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) throws IllegalArgumentException {
        String str = (String) this.f5894a.f5727c.getSelectedItem();
        this.f5894a.b().b(str);
        this.f5894a.d();
        if (C0804hg.a().r() != null && str != null && str.length() > 0) {
            this.f5894a.b("yTableGenField", str);
        }
        this.f5894a.t();
        this.f5894a.o();
    }
}
