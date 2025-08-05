package bt;

import G.cZ;
import s.C1818g;

/* renamed from: bt.bd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bd.class */
class C1322bd implements G.aN {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ cZ f9006a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1320bb f9007b;

    C1322bd(C1320bb c1320bb, cZ cZVar) {
        this.f9007b = c1320bb;
        this.f9006a = cZVar;
    }

    @Override // G.aN
    public void a(String str, String str2) throws IllegalArgumentException {
        String string;
        try {
            string = C1818g.b(this.f9006a.a());
        } catch (V.g e2) {
            string = this.f9006a.toString();
        }
        this.f9007b.f9001i.setText(string);
    }
}
