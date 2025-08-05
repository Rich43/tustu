package E;

import java.net.InetAddress;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:E/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private ArrayList f245a = new ArrayList();

    public void a(byte b2, InetAddress inetAddress) {
        this.f245a.add(bX.c.a(b2, inetAddress));
    }

    public void a(byte b2, int i2) {
        this.f245a.add(bX.c.a(b2, i2));
    }

    public bX.c[] a() {
        return (bX.c[]) this.f245a.toArray(new bX.c[this.f245a.size()]);
    }
}
