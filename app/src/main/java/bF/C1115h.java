package bf;

import G.C0048ah;
import java.util.Comparator;

/* renamed from: bf.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/h.class */
class C1115h implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1108a f8055a;

    C1115h(C1108a c1108a) {
        this.f8055a = c1108a;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(C0048ah c0048ah, C0048ah c0048ah2) {
        return c0048ah.aJ().toLowerCase().compareTo(c0048ah2.aJ().toLowerCase());
    }
}
