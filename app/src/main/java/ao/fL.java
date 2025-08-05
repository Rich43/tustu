package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/fL.class */
class fL implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5710a;

    fL(C0764fu c0764fu) {
        this.f5710a = c0764fu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) throws IllegalArgumentException {
        String str = (String) this.f5710a.f5843d.getSelectedItem();
        this.f5710a.f5845f.b(str);
        this.f5710a.d();
        if (str != null && str.length() > 0 && !this.f5710a.f5880T) {
            this.f5710a.a(C0764fu.f5858B, str);
        }
        this.f5710a.x();
    }
}
