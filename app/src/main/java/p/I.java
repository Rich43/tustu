package p;

import d.InterfaceC1711c;
import java.util.Objects;

/* loaded from: TunerStudioMS.jar:p/I.class */
class I {

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1711c f13161b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f13162a;

    I(D d2, InterfaceC1711c interfaceC1711c) {
        this.f13162a = d2;
        this.f13161b = interfaceC1711c;
    }

    public InterfaceC1711c a() {
        return this.f13161b;
    }

    public String toString() {
        return this.f13161b.b();
    }

    public boolean equals(Object obj) {
        return obj instanceof String ? obj.equals(this.f13161b.a()) : obj instanceof InterfaceC1711c ? ((InterfaceC1711c) obj).a().equals(this.f13161b.a()) : super.equals(obj);
    }

    public int hashCode() {
        return (79 * 3) + Objects.hashCode(this.f13161b);
    }
}
