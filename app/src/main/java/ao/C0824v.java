package ao;

import W.C0184j;
import java.util.Comparator;

/* renamed from: ao.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/v.class */
class C0824v implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0823u f6185a;

    C0824v(C0823u c0823u) {
        this.f6185a = c0823u;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(C0184j c0184j, C0184j c0184j2) {
        return (this.f6185a.a(c0184j) || this.f6185a.a(c0184j2)) ? !this.f6185a.a(c0184j) ? (c0184j.p() + c0184j.a()).compareTo(c0184j2.a()) : !this.f6185a.a(c0184j2) ? c0184j.a().compareTo(c0184j2.p() + c0184j2.a()) : c0184j.a().compareTo(c0184j2.a()) : (c0184j.p() + c0184j.a()).compareTo(c0184j2.p() + c0184j2.a());
    }
}
