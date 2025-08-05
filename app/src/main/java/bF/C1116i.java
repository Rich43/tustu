package bf;

import G.C0043ac;
import java.util.Comparator;

/* renamed from: bf.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/i.class */
class C1116i implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1108a f8056a;

    C1116i(C1108a c1108a) {
        this.f8056a = c1108a;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(C0043ac c0043ac, C0043ac c0043ac2) {
        return c0043ac.b().toLowerCase().compareTo(c0043ac2.b().toLowerCase());
    }
}
