package bC;

import java.util.Comparator;

/* loaded from: TunerStudioMS.jar:bC/c.class */
class c implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ b f6571a;

    c(b bVar) {
        this.f6571a = bVar;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(Z.e eVar, Z.e eVar2) {
        return eVar.a().compareToIgnoreCase(eVar2.a());
    }
}
