package bg;

import G.C0073bf;
import java.util.Comparator;

/* renamed from: bg.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/r.class */
class C1138r implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1137q f8096a;

    C1138r(C1137q c1137q) {
        this.f8096a = c1137q;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(C0073bf c0073bf, C0073bf c0073bf2) {
        return c0073bf2.f() - c0073bf.f();
    }
}
