package bD;

import java.util.Comparator;
import java.util.Date;

/* renamed from: bD.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bD/d.class */
class C0958d implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0957c f6660a;

    C0958d(C0957c c0957c) {
        this.f6660a = c0957c;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(Date date, Date date2) {
        return (int) (date2.getTime() - date.getTime());
    }
}
