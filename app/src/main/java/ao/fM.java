package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/fM.class */
class fM implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5711a;

    fM(C0764fu c0764fu) {
        this.f5711a = c0764fu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) throws IllegalArgumentException {
        String str = (String) this.f5711a.f5844e.getSelectedItem();
        this.f5711a.f5845f.c(str);
        this.f5711a.d();
        if (str != null && !this.f5711a.f5880T && str.length() > 0) {
            this.f5711a.a(C0764fu.f5859C, str);
        }
        this.f5711a.y();
    }
}
