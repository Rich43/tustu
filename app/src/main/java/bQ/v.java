package bQ;

import G.aH;
import java.util.Comparator;

/* loaded from: TunerStudioMS.jar:bQ/v.class */
class v implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ u f7488a;

    v(u uVar) {
        this.f7488a = uVar;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(aH aHVar, aH aHVar2) {
        return (int) (aHVar.x() - aHVar2.x());
    }
}
