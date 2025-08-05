package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/fG.class */
class fG implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5705a;

    fG(C0764fu c0764fu) {
        this.f5705a = c0764fu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) throws IllegalArgumentException {
        String str = (String) this.f5705a.f5842c.getSelectedItem();
        this.f5705a.f5845f.a(str);
        this.f5705a.d();
        if (str != null && str.length() > 0 && !this.f5705a.f5880T) {
            this.f5705a.a(C0764fu.f5857A, str);
        }
        this.f5705a.w();
    }
}
