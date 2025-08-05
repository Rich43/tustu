package aY;

import W.ag;
import java.util.Comparator;

/* loaded from: TunerStudioMS.jar:aY/z.class */
class z implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ s f4087a;

    z(s sVar) {
        this.f4087a = sVar;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(ag agVar, ag agVar2) {
        return agVar.a().getName().compareTo(agVar2.a().getName());
    }
}
