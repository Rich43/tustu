package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/gA.class */
class gA implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5893a;

    gA(fX fXVar) {
        this.f5893a = fXVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) throws IllegalArgumentException {
        String str = (String) this.f5893a.f5726b.getSelectedItem();
        this.f5893a.b().a(str);
        this.f5893a.d();
        if (C0804hg.a().r() != null && str != null && str.length() > 0) {
            this.f5893a.b("xTableGenField", str);
        }
        this.f5893a.s();
        this.f5893a.o();
    }
}
