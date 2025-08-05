package p;

import G.T;
import d.InterfaceC1709a;
import d.InterfaceC1711c;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:p/z.class */
public class z implements d.f, InterfaceC1773B {

    /* renamed from: b, reason: collision with root package name */
    ArrayList f13259b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    List f13260c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    boolean f13261d = false;

    /* renamed from: a, reason: collision with root package name */
    public static String f13258a = ".action";

    /* renamed from: e, reason: collision with root package name */
    private static z f13262e = null;

    private z() {
    }

    public static z a() {
        if (f13262e == null) {
            f13262e = new z();
        }
        return f13262e;
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x001f  */
    @Override // p.InterfaceC1773B
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.ArrayList b() {
        /*
            r3 = this;
            r0 = r3
            java.util.ArrayList r0 = r0.f13259b
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L1f
            r0 = r3
            boolean r0 = r0.f13261d
            G.T r1 = G.T.a()
            G.R r1 = r1.c()
            if (r1 == 0) goto L1b
            r1 = 1
            goto L1c
        L1b:
            r1 = 0
        L1c:
            if (r0 == r1) goto L23
        L1f:
            r0 = r3
            r0.f()
        L23:
            r0 = r3
            java.util.ArrayList r0 = r0.f13259b
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p.z.b():java.util.ArrayList");
    }

    @Override // p.InterfaceC1773B
    public d.m b(String str) {
        for (int i2 = 0; i2 < b().size(); i2++) {
            d.m mVar = (d.m) this.f13259b.get(i2);
            if (mVar.a().equals(str)) {
                return mVar;
            }
        }
        return null;
    }

    @Override // p.InterfaceC1773B
    public boolean c(String str) {
        for (int i2 = 0; i2 < b().size(); i2++) {
            if (((d.m) this.f13259b.get(i2)).a().equals(str)) {
                this.f13259b.remove(i2);
                d(str);
                e();
                return true;
            }
        }
        return false;
    }

    @Override // p.InterfaceC1773B
    public void a(d.m mVar) {
        for (int i2 = 0; i2 < b().size(); i2++) {
            if (((d.m) this.f13259b.get(i2)).l().equals(mVar.l())) {
                this.f13259b.set(i2, mVar);
                if (b(mVar)) {
                    mVar.n();
                    return;
                } else {
                    bH.C.b("Failed to save updated UserAction: " + mVar.a());
                    return;
                }
            }
        }
        this.f13259b.add(mVar);
        mVar.n();
        if (!b(mVar)) {
            bH.C.b("Failed to save new UserAction: " + mVar.a());
        }
        e();
    }

    @Override // p.InterfaceC1773B
    public void a(InterfaceC1774C interfaceC1774C) {
        this.f13260c.add(interfaceC1774C);
    }

    private void e() {
        Iterator it = this.f13260c.iterator();
        while (it.hasNext()) {
            ((InterfaceC1774C) it.next()).a();
        }
    }

    @Override // p.InterfaceC1773B
    public void c() {
    }

    @Override // p.InterfaceC1773B
    public void d() {
    }

    private void f() {
        this.f13259b.clear();
        this.f13261d = T.a().c() != null;
        for (File file : C1807j.B().listFiles(new C1772A(this))) {
            d.m mVarA = a(file);
            if (this.f13261d || !mVarA.g()) {
                this.f13259b.add(mVarA);
            }
        }
        if (this.f13259b.isEmpty()) {
            this.f13259b.addAll(C1776b.a());
            Iterator it = this.f13259b.iterator();
            while (it.hasNext()) {
                b((d.m) it.next());
            }
        }
        Iterator it2 = this.f13259b.iterator();
        while (it2.hasNext()) {
        }
    }

    private d.m a(File file) {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                properties.load(fileInputStream);
                d.m mVar = new d.m();
                mVar.c(properties);
                try {
                    fileInputStream.close();
                } catch (Exception e2) {
                }
                return mVar;
            } catch (IOException e3) {
                bH.C.a("Unable to load Action file: " + e3.getLocalizedMessage());
                try {
                    fileInputStream.close();
                } catch (Exception e4) {
                }
                return null;
            }
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Exception e5) {
            }
            throw th;
        }
    }

    private boolean d(String str) {
        return new File(C1807j.B(), str + f13258a).delete();
    }

    public boolean b(d.m mVar) {
        File fileB = C1807j.B();
        File file = new File(fileB, mVar.a() + f13258a);
        if (file.exists() && !file.delete()) {
            bH.C.b("Failed to delete existing UserAction file: " + file.getAbsolutePath());
            return false;
        }
        File file2 = new File(fileB, mVar.a() + f13258a);
        FileOutputStream fileOutputStream = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(file2);
                mVar.m().store(fileOutputStream, "Attributes for UserAction: " + mVar.a());
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e2) {
                    }
                }
                return true;
            } catch (IOException e3) {
                bH.C.a("Failed to Save UserAction! Error: " + e3.getLocalizedMessage() + "\nFile:\n" + file2.getAbsolutePath());
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e4) {
                        return false;
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e5) {
                    throw th;
                }
            }
            throw th;
        }
    }

    @Override // d.f
    public InterfaceC1711c a(String str) {
        return b(str);
    }

    @Override // d.f
    public Collection a(InterfaceC1709a interfaceC1709a) {
        if (interfaceC1709a == null) {
            return (Collection) b().clone();
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = b().iterator();
        while (it.hasNext()) {
            InterfaceC1711c interfaceC1711c = (InterfaceC1711c) it.next();
            if (interfaceC1709a.a(interfaceC1711c)) {
                arrayList.add(interfaceC1711c);
            }
        }
        return arrayList;
    }
}
