package bs;

import aP.C0338f;
import ai.C0512b;
import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bs/s.class */
class s implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f8629a;

    s(k kVar) {
        this.f8629a = kVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C0512b c0512b = new C0512b();
        c0512b.a(C1818g.b("WUE Analyze Live Help"));
        c0512b.b("/help/wueAnalyzeLive.html");
        C0338f.a().a(c0512b, bV.a(this.f8629a.f8614k));
    }
}
