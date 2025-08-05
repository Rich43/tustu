package o;

import d.InterfaceC1711c;
import java.util.Comparator;

/* renamed from: o.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:o/c.class */
class C1767c implements Comparator {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1765a f12950a;

    C1767c(C1765a c1765a) {
        this.f12950a = c1765a;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(InterfaceC1711c interfaceC1711c, InterfaceC1711c interfaceC1711c2) {
        return (interfaceC1711c.c() + interfaceC1711c.b()).compareTo(interfaceC1711c2.c() + interfaceC1711c2.b());
    }
}
