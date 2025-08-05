package be;

import G.C0051ak;
import java.util.Comparator;

/* renamed from: be.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/l.class */
class C1096l implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1094j f7988a;

    C1096l(C1094j c1094j) {
        this.f7988a = c1094j;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(C0051ak c0051ak, C0051ak c0051ak2) {
        return c0051ak.a().toString().compareToIgnoreCase(c0051ak2.a().toString());
    }
}
