package bD;

import java.util.Comparator;

/* renamed from: bD.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bD/e.class */
class C0959e implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0957c f6661a;

    C0959e(C0957c c0957c) {
        this.f6661a = c0957c;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(Long l2, Long l3) {
        return (int) (l3.longValue() - l2.longValue());
    }
}
