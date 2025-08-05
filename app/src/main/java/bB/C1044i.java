package bb;

import G.bS;
import java.awt.Cursor;
import java.util.List;
import s.C1818g;

/* renamed from: bb.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/i.class */
class C1044i implements A.o {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1039d f7771a;

    C1044i(C1039d c1039d) {
        this.f7771a = c1039d;
    }

    @Override // A.o
    public boolean a(String str, String str2, List list, bS bSVar) {
        return true;
    }

    @Override // A.o
    public void b(double d2) {
    }

    @Override // A.o
    public void a(String str) {
    }

    @Override // A.o
    public void a(A.x xVar) {
    }

    @Override // A.o
    public void a() throws IllegalArgumentException {
        this.f7771a.f7758d.setText(C1818g.b("No Controller Found"));
        this.f7771a.f7760f.setEnabled(true);
        if (this.f7771a.f7764j != null) {
            this.f7771a.f7764j.a();
        }
        this.f7771a.setCursor(Cursor.getDefaultCursor());
    }
}
