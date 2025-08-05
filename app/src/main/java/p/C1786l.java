package p;

import d.InterfaceC1711c;
import java.util.Objects;

/* renamed from: p.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/l.class */
class C1786l {

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1711c f13215b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1781g f13216a;

    C1786l(C1781g c1781g, InterfaceC1711c interfaceC1711c) {
        this.f13216a = c1781g;
        this.f13215b = interfaceC1711c;
    }

    public InterfaceC1711c a() {
        return this.f13215b;
    }

    public String toString() {
        return this.f13215b.b();
    }

    public boolean equals(Object obj) {
        return obj instanceof String ? obj.equals(this.f13215b.a()) : obj instanceof InterfaceC1711c ? ((InterfaceC1711c) obj).a().equals(this.f13215b.a()) : super.equals(obj);
    }

    public int hashCode() {
        return (79 * 3) + Objects.hashCode(this.f13215b);
    }
}
