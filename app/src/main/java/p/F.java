package p;

import d.InterfaceC1711c;
import java.util.Comparator;

/* loaded from: TunerStudioMS.jar:p/F.class */
class F implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f13158a;

    F(D d2) {
        this.f13158a = d2;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(InterfaceC1711c interfaceC1711c, InterfaceC1711c interfaceC1711c2) {
        return interfaceC1711c.b().toLowerCase().compareTo(interfaceC1711c2.b().toLowerCase());
    }
}
