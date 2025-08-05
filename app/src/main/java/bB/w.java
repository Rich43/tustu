package bb;

import com.efiAnalytics.ui.bV;

/* loaded from: TunerStudioMS.jar:bb/w.class */
class w implements ae.u {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ v f7838a;

    w(v vVar) {
        this.f7838a = vVar;
    }

    @Override // ae.u
    public void a(double d2) {
        this.f7838a.f7825d.setValue((int) Math.round(d2 * 100.0d));
    }

    @Override // ae.u
    public void a(String str) {
        this.f7838a.f7824c.setText(str);
    }

    @Override // ae.u
    public boolean b(String str) {
        if (this.f7838a.f7833l) {
            return this.f7838a.f7829h.a(str);
        }
        if (!this.f7838a.f7832k) {
            return this.f7838a.f7828g.a(str);
        }
        bV.d(str, this.f7838a.f7830i);
        return true;
    }
}
