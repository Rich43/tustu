package ao;

import i.InterfaceC1741a;
import java.util.Iterator;

/* renamed from: ao.ej, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ej.class */
class C0726ej implements InterfaceC1741a {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0718eb f5636a;

    C0726ej(C0718eb c0718eb) {
        this.f5636a = c0718eb;
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        Iterator it = this.f5636a.f5615a.values().iterator();
        while (it.hasNext()) {
            ((C0764fu) it.next()).a(i2);
        }
    }
}
