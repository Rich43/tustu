package ao;

import ay.C0926c;
import ay.InterfaceC0928e;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:ao/gI.class */
public class gI implements InterfaceC0928e {

    /* renamed from: a, reason: collision with root package name */
    List f5922a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f5923b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    String f5924c = "";

    @Override // ay.InterfaceC0928e
    public void a(C0926c c0926c) {
        this.f5923b.add(c0926c);
        String str = "Found Service: " + c0926c.a() + ", ip:" + c0926c.c();
        if (this.f5924c.equals(str)) {
            bH.C.c(str);
            this.f5924c = str;
        }
    }

    @Override // ay.InterfaceC0928e
    public void a() {
        this.f5923b.clear();
    }

    @Override // ay.InterfaceC0928e
    public void b() {
        List list = this.f5922a;
        this.f5922a = this.f5923b;
        this.f5923b = list;
    }
}
