package bB;

import java.util.Comparator;

/* loaded from: TunerStudioMS.jar:bB/i.class */
class i implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f6550a;

    i(h hVar) {
        this.f6550a = hVar;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(r rVar, r rVar2) {
        return rVar.e().compareToIgnoreCase(rVar2.e());
    }
}
