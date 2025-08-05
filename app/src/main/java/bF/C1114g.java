package bf;

import G.aH;
import java.util.Comparator;

/* renamed from: bf.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/g.class */
class C1114g implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1108a f8054a;

    C1114g(C1108a c1108a) {
        this.f8054a = c1108a;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(aH aHVar, aH aHVar2) {
        return aHVar.aJ().toLowerCase().compareTo(aHVar2.aJ().toLowerCase());
    }
}
