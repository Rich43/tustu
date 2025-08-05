package p;

import java.util.Comparator;

/* renamed from: p.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/e.class */
class C1779e implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1777c f13193a;

    C1779e(C1777c c1777c) {
        this.f13193a = c1777c;
    }

    @Override // java.util.Comparator
    public int compare(Object obj, Object obj2) {
        return obj.toString().compareTo(obj2.toString());
    }
}
