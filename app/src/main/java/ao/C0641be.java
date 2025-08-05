package ao;

import W.C0184j;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: ao.be, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/be.class */
public class C0641be {

    /* renamed from: a, reason: collision with root package name */
    private static C0641be f5396a = null;

    /* renamed from: b, reason: collision with root package name */
    private List f5397b = new ArrayList();

    private C0641be() {
    }

    public static C0641be a() {
        if (f5396a == null) {
            f5396a = new C0641be();
        }
        return f5396a;
    }

    public void a(boolean z2) {
        Iterator it = this.f5397b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0640bd) it.next()).d(z2);
        }
    }

    public void a(String str, C0184j c0184j) {
        if (str.indexOf(46) == -1) {
            a(str, c0184j, 0);
        }
        a(str.substring(0, str.lastIndexOf(46)), c0184j, Integer.parseInt(str.substring(str.lastIndexOf(46) + 1, str.length())));
    }

    public void a(String str, C0184j c0184j, int i2) {
        b(str, c0184j, i2);
    }

    private void b(String str, C0184j c0184j, int i2) {
        Iterator it = this.f5397b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0640bd) it.next()).a(str, c0184j, i2);
        }
    }

    public void a(InterfaceC0640bd interfaceC0640bd) {
        this.f5397b.add(interfaceC0640bd);
    }
}
