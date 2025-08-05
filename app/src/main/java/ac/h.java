package ac;

import G.C0043ac;
import G.C0113cs;
import G.C0126i;
import G.InterfaceC0109co;
import G.J;
import G.R;
import G.T;
import G.aF;
import L.C0168y;
import W.C0179e;
import W.as;
import bH.Z;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ac/h.class */
public abstract class h implements aF {

    /* renamed from: u, reason: collision with root package name */
    private InterfaceC0109co f4216u;

    /* renamed from: e, reason: collision with root package name */
    static List f4198e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    protected static h f4200f = null;

    /* renamed from: b, reason: collision with root package name */
    private static String f4202b = null;

    /* renamed from: q, reason: collision with root package name */
    private static int f4211q = 0;

    /* renamed from: s, reason: collision with root package name */
    private static h f4214s = null;

    /* renamed from: v, reason: collision with root package name */
    private static InterfaceC0486B f4217v = null;

    /* renamed from: d, reason: collision with root package name */
    protected ArrayList f4197d = new ArrayList();

    /* renamed from: a, reason: collision with root package name */
    private List f4199a = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    protected OutputStream f4201g = null;

    /* renamed from: h, reason: collision with root package name */
    protected boolean f4203h = false;

    /* renamed from: i, reason: collision with root package name */
    long f4204i = System.currentTimeMillis();

    /* renamed from: j, reason: collision with root package name */
    long f4205j = -1;

    /* renamed from: k, reason: collision with root package name */
    protected k f4206k = null;

    /* renamed from: c, reason: collision with root package name */
    private long f4207c = -1;

    /* renamed from: l, reason: collision with root package name */
    File f4208l = null;

    /* renamed from: m, reason: collision with root package name */
    boolean f4209m = false;

    /* renamed from: p, reason: collision with root package name */
    private double f4210p = 0.0d;

    /* renamed from: r, reason: collision with root package name */
    private String[] f4212r = null;

    /* renamed from: n, reason: collision with root package name */
    boolean f4213n = false;

    /* renamed from: t, reason: collision with root package name */
    private boolean f4215t = true;

    /* renamed from: w, reason: collision with root package name */
    private boolean f4218w = false;

    /* renamed from: o, reason: collision with root package name */
    final List f4219o = new ArrayList();

    protected h() {
        this.f4216u = null;
        this.f4216u = new i(this);
    }

    protected abstract void a(R[] rArr, OutputStream outputStream);

    public void d(String str) {
        a(this.f4201g, str);
        f(str);
    }

    protected abstract void a(OutputStream outputStream, String str);

    protected abstract void a(OutputStream outputStream, byte[][] bArr);

    protected abstract void a(OutputStream outputStream);

    public void a(InterfaceC0489a interfaceC0489a) throws C0485A {
        if (u()) {
            throw new C0485A("Can not modify LoggerFields while logging.");
        }
        if (this.f4199a.contains(interfaceC0489a)) {
            return;
        }
        this.f4199a.add(interfaceC0489a);
    }

    public void i() throws C0485A {
        if (u()) {
            throw new C0485A("Can not modify LoggerFields while logging.");
        }
        this.f4199a.clear();
    }

    protected List j() {
        return this.f4199a;
    }

    public static h k() {
        return f4214s;
    }

    public void a(h hVar) {
        f4214s = hVar;
    }

    public synchronized void a(String str, String str2) throws V.a {
        a(new String[]{str}, str2);
    }

    public synchronized void a(String[] strArr, String str) throws V.a {
        T tA = T.a();
        R[] rArr = new R[strArr.length];
        R rC = tA.c();
        bH.C.c("Starting Log for " + rArr.length + " Configs.");
        for (int i2 = 0; i2 < strArr.length; i2++) {
            R rC2 = tA.c(strArr[i2]);
            if (i2 <= 0 || !rC2.equals(rC)) {
                rArr[i2] = rC2;
            } else {
                R r2 = rArr[0];
                rArr[0] = rC2;
                rArr[i2] = r2;
            }
            if (rArr[i2] == null) {
                throw new V.a("Configuration '" + strArr[i2] + "' not currently loaded. \nCan't start data Log.");
            }
        }
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < rArr.length; i3++) {
            if (i3 == 0 || (rArr[i3].S() && rArr[i3].C().e(rArr[i3].O()))) {
                arrayList.add(rArr[i3]);
            }
        }
        R[] rArr2 = (R[]) arrayList.toArray(new R[arrayList.size()]);
        ArrayList arrayListA = a(rArr2);
        Z z2 = new Z();
        z2.a();
        HashMap map = new HashMap();
        Iterator it = arrayListA.iterator();
        while (it.hasNext()) {
            q qVar = (q) it.next();
            if (qVar.g() != null) {
                List arrayList2 = (List) map.get(qVar.g());
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList();
                    map.put(qVar.g(), arrayList2);
                }
                if (qVar.c() == null) {
                    bH.C.b("OutputChannel is null! Cannot datalog: " + qVar.a());
                } else {
                    arrayList2.add(qVar.c().aJ());
                }
            }
        }
        for (String str2 : map.keySet()) {
            C0113cs.a().a(str2, (List) map.get(str2), this.f4216u);
        }
        bH.C.c("Time to subscribe all data log channels: " + z2.d());
        try {
            this.f4201g = e(str);
            a(rArr2, this.f4201g);
            ArrayList arrayList3 = new ArrayList();
            for (int i4 = 0; i4 < rArr2.length; i4++) {
                J jC = T.a().c(strArr[i4]).C();
                if (!arrayList3.contains(jC)) {
                    jC.b(this);
                }
                arrayList3.add(jC);
                this.f4197d.add(new m(this, strArr[i4], i4, rArr2[i4].O().n()));
            }
            a(new File(str));
            bH.C.d("Started Data Log to file: " + str);
            C0168y.a();
            this.f4205j = -1L;
            this.f4213n = false;
            this.f4203h = true;
            try {
                String str3 = strArr[0];
                bH.C.d("Channels active on main config " + str3 + ": " + C0113cs.a().b(str3));
            } catch (Exception e2) {
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.a("Could not create file " + str);
        }
    }

    public void l() {
        this.f4213n = true;
        new j(this).start();
    }

    protected void m() throws V.a {
        this.f4203h = false;
        C0168y.b();
        for (int i2 = 0; i2 < this.f4197d.size(); i2 = (i2 - 1) + 1) {
            try {
                m mVar = (m) this.f4197d.get(i2);
                String strA = mVar.a();
                if (T.a().c(strA) == null) {
                    throw new V.a("Configuration '" + strA + "' not currently loaded. \nCan't stop data Log.");
                }
                T.a().c(strA).C().c(this);
                this.f4197d.remove(mVar);
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new V.a("Could Not close log file, error\n" + e2.getMessage());
            }
        }
        a(this.f4201g);
        C0113cs.a().a(this.f4216u);
        q();
        if (!this.f4209m && this.f4208l != null && this.f4208l.exists()) {
            bH.C.d("No records written to log file: " + this.f4208l.getName() + ", will delete.");
            this.f4208l.delete();
        }
        this.f4205j = -1L;
        if (this.f4206k != null) {
            this.f4206k.a();
        }
    }

    protected boolean a(R r2, C0043ac c0043ac) {
        return !a(T.a().c().equals(r2) ? "" : r2.c(), this.f4212r, c0043ac.b()) && r.a(r2, c0043ac);
    }

    private boolean a(String str, String[] strArr, String str2) {
        if (strArr == null || str2 == null) {
            return false;
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (((str == null || str.isEmpty()) && strArr[i2].equals(str2)) || strArr[i2].equals(str + "." + str2)) {
                return true;
            }
        }
        return false;
    }

    protected ArrayList a(R[] rArr) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < rArr.length; i2++) {
            if (rArr[i2].S()) {
                f4211q = 0;
                Iterator it = rArr[i2].g().iterator();
                while (it.hasNext()) {
                    C0043ac c0043ac = (C0043ac) it.next();
                    if (a(rArr[i2], c0043ac)) {
                        q qVar = new q();
                        if (i2 == 0) {
                            String strB = c0043ac.b();
                            int i3 = 0;
                            while (a(arrayList, strB)) {
                                int i4 = i3;
                                i3++;
                                strB = c0043ac.b() + "_" + i4;
                            }
                            qVar.a(strB);
                        } else {
                            String str = rArr[i2].c() + "." + c0043ac.b();
                            int i5 = 0;
                            while (a(arrayList, str)) {
                                int i6 = i5;
                                i5++;
                                str = rArr[i2].c() + "." + c0043ac.b() + "_" + i6;
                            }
                            qVar.a(str);
                        }
                        qVar.b(rArr[i2].c());
                        qVar.a(c0043ac);
                        qVar.a(i2);
                        qVar.a(rArr[i2].g(c0043ac.a()));
                        qVar.b(c0043ac.f());
                        if (qVar.h() > f4211q) {
                            f4211q = qVar.h();
                        }
                        qVar.c(c0043ac.m());
                        qVar.d(c0043ac.n());
                        arrayList.add(qVar);
                    }
                }
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    ((q) it2.next()).c(f4211q);
                }
            }
        }
        return arrayList;
    }

    private boolean a(ArrayList arrayList, String str) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((q) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Override // G.aF
    public synchronized void a(String str, byte[] bArr) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long jNanoTime = System.nanoTime();
        double d2 = this.f4207c != -1 ? (jCurrentTimeMillis - this.f4207c) / 1000.0d : 0.1d;
        this.f4207c = jCurrentTimeMillis;
        this.f4210p = ((this.f4210p * 20.0d) + (1.0d / d2)) / 21.0d;
        m mVarA = a(str);
        if (mVarA == null || bArr == null) {
            return;
        }
        mVarA.a(bArr);
        Iterator it = this.f4197d.iterator();
        while (it.hasNext()) {
            if (((m) it.next()).b() == null) {
                return;
            }
        }
        byte[][] bArr2 = new byte[this.f4197d.size()][0];
        String[] strArr = new String[this.f4197d.size()];
        for (int i2 = 0; i2 < this.f4197d.size(); i2++) {
            m mVar = (m) this.f4197d.get(i2);
            bArr2[i2] = mVar.b();
            mVar.a((byte[]) null);
            strArr[i2] = mVar.a();
        }
        try {
            if (this.f4205j == -1) {
                C0126i.b();
                this.f4205j = System.currentTimeMillis();
            }
            long jNanoTime2 = System.nanoTime() - jNanoTime;
            if (f4217v != null) {
                C0113cs.a().a("dataLogTime", f4217v.a());
            }
            a(this.f4201g, bArr2);
            long jNanoTime3 = (System.nanoTime() - jNanoTime) - jNanoTime2;
            p();
            this.f4209m = true;
            long jNanoTime4 = ((System.nanoTime() - jNanoTime) - jNanoTime2) - jNanoTime3;
            if (System.nanoTime() - jNanoTime > 4.0E7d) {
                NumberFormat numberInstance = DecimalFormat.getNumberInstance();
                numberInstance.setMaximumFractionDigits(6);
                bH.C.b("Data Log write time seems to be taking an unusually long amount of time.");
                bH.C.c("Time to write Data Log Record: " + numberInstance.format(jNanoTime3 / 1000000.0d) + "ms. Time to notify Listeners: " + (jNanoTime4 / 1000000.0d) + "ms. Prework time:" + (jNanoTime2 / 1000000.0d));
            }
        } catch (Exception e2) {
            bH.C.a("Failed to write log record. Error:");
            Logger.getLogger(h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        try {
            this.f4201g.flush();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        if (this.f4213n && f4211q == 0) {
            try {
                m();
                return;
            } catch (V.a e4) {
                bH.C.a(e4.getLocalizedMessage(), e4, null);
                return;
            }
        }
        if (!this.f4213n || f4211q <= 0) {
            return;
        }
        f4211q--;
    }

    private m a(String str) {
        Iterator it = this.f4197d.iterator();
        while (it.hasNext()) {
            m mVar = (m) it.next();
            if (mVar.a().equals(str)) {
                return mVar;
            }
        }
        return null;
    }

    protected OutputStream e(String str) {
        File file = new File(str);
        try {
            file.getParentFile().mkdirs();
            b(file);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        as asVar = new as(new C0179e(new FileOutputStream(file)));
        this.f4208l = file;
        this.f4209m = false;
        return asVar;
    }

    public String n() {
        return f4202b;
    }

    public void a(File file) {
        f4202b = file.getAbsolutePath();
    }

    public double o() {
        return this.f4210p;
    }

    protected void b(File file) {
        if (this.f4215t) {
            Iterator it = f4198e.iterator();
            while (it.hasNext()) {
                ((f) it.next()).a(file);
            }
        }
    }

    protected void p() {
        if (this.f4215t) {
            Iterator it = f4198e.iterator();
            while (it.hasNext()) {
                ((f) it.next()).c();
            }
        }
    }

    protected void q() {
        if (this.f4215t) {
            Iterator it = f4198e.iterator();
            while (it.hasNext()) {
                ((f) it.next()).d();
            }
        }
    }

    protected void f(String str) {
        if (this.f4215t) {
            Iterator it = f4198e.iterator();
            while (it.hasNext()) {
                ((f) it.next()).b(str);
            }
        }
    }

    public void a(f fVar) {
        f4198e.add(fVar);
    }

    public int r() {
        return f4211q;
    }

    public void a(String[] strArr) {
        this.f4212r = strArr;
    }

    public void b(boolean z2) {
        this.f4215t = z2;
    }

    public static InterfaceC0486B s() {
        return f4217v;
    }

    public static void a(InterfaceC0486B interfaceC0486B) {
        f4217v = interfaceC0486B;
    }

    public boolean t() {
        return this.f4218w;
    }

    public void c(boolean z2) {
        this.f4218w = z2;
    }

    public boolean u() {
        return this.f4203h;
    }

    public void d(boolean z2) {
        this.f4203h = z2;
    }
}
