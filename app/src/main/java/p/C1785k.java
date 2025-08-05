package p;

import d.InterfaceC1711c;
import java.util.Comparator;

/* renamed from: p.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/k.class */
class C1785k implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1781g f13214a;

    C1785k(C1781g c1781g) {
        this.f13214a = c1781g;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(InterfaceC1711c interfaceC1711c, InterfaceC1711c interfaceC1711c2) {
        return interfaceC1711c.b().toLowerCase().compareTo(interfaceC1711c2.b().toLowerCase());
    }
}
