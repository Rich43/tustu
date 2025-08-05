package ae;

import G.bS;
import W.C0200z;
import ad.C0493a;
import ad.C0494b;
import bH.C;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: TunerStudioMS.jar:ae/k.class */
public class k {

    /* renamed from: b, reason: collision with root package name */
    private List f4372b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private List f4373c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private File f4374d = null;

    /* renamed from: e, reason: collision with root package name */
    private File f4375e = null;

    /* renamed from: f, reason: collision with root package name */
    private File f4376f = null;

    /* renamed from: g, reason: collision with root package name */
    private File f4377g = null;

    /* renamed from: h, reason: collision with root package name */
    private HashMap f4378h = new HashMap();

    /* renamed from: a, reason: collision with root package name */
    List f4379a = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private InterfaceC0501e f4380i = new C0498b();

    public boolean a() {
        return !this.f4373c.isEmpty();
    }

    public boolean b() {
        return !this.f4372b.isEmpty();
    }

    public void a(File file) {
        this.f4372b.add(file);
    }

    public void b(File file) {
        this.f4373c.add(file);
    }

    public List c() {
        return this.f4372b;
    }

    public List d() {
        return this.f4373c;
    }

    public C0493a c(File file) {
        C0493a c0493aA = (C0493a) this.f4378h.get(file);
        if (c0493aA == null) {
            c0493aA = new C0494b().a(file);
            this.f4378h.put(file, c0493aA);
        }
        return c0493aA;
    }

    public File e() {
        return this.f4374d;
    }

    public void d(File file) {
        this.f4374d = file;
    }

    public File f() {
        return this.f4375e;
    }

    public void e(File file) {
        this.f4375e = file;
    }

    public File g() {
        return this.f4377g;
    }

    public void f(File file) {
        this.f4377g = file;
    }

    public InterfaceC0501e h() {
        return this.f4380i;
    }

    public File i() {
        return this.f4376f;
    }

    public void g(File file) {
        this.f4376f = file;
    }

    public File a(bS bSVar) {
        List<File> listC = c();
        if (!b()) {
            return null;
        }
        for (File file : listC) {
            String strA = C0200z.a(file);
            if (strA != null && strA.equals(bSVar.b())) {
                return file;
            }
        }
        C.b("Did not find an exact match for firmware signature: " + bSVar.b() + "\nReturning:" + ((File) listC.get(0)).getAbsolutePath());
        return (File) listC.get(0);
    }
}
