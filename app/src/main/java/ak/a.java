package aK;

import G.C0113cs;
import G.C0129l;
import G.J;
import ak.aD;
import bH.C;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:aK/a.class */
public class a implements A.g {

    /* renamed from: g, reason: collision with root package name */
    private static a f2535g = null;

    /* renamed from: a, reason: collision with root package name */
    public static int f2536a = 0;

    /* renamed from: b, reason: collision with root package name */
    public static int f2537b = 1;

    /* renamed from: c, reason: collision with root package name */
    public static int f2538c = 2;

    /* renamed from: d, reason: collision with root package name */
    f f2539d;

    /* renamed from: i, reason: collision with root package name */
    private long f2541i;

    /* renamed from: q, reason: collision with root package name */
    private volatile b f2549q;

    /* renamed from: r, reason: collision with root package name */
    private static volatile b f2550r;

    /* renamed from: h, reason: collision with root package name */
    private String f2540h = null;

    /* renamed from: j, reason: collision with root package name */
    private int f2542j = 0;

    /* renamed from: k, reason: collision with root package name */
    private String f2543k = "EFIAnalytics GPS";

    /* renamed from: l, reason: collision with root package name */
    private float f2544l = 10.0f;

    /* renamed from: m, reason: collision with root package name */
    private int f2545m = f2536a;

    /* renamed from: n, reason: collision with root package name */
    private boolean f2546n = false;

    /* renamed from: o, reason: collision with root package name */
    private boolean f2547o = false;

    /* renamed from: p, reason: collision with root package name */
    private int f2548p = 0;

    /* renamed from: s, reason: collision with root package name */
    private List f2551s = new ArrayList();

    /* renamed from: t, reason: collision with root package name */
    private A.f f2552t = null;

    /* renamed from: u, reason: collision with root package name */
    private c f2553u = new c(this);

    /* renamed from: v, reason: collision with root package name */
    private Writer f2554v = null;

    /* renamed from: e, reason: collision with root package name */
    final Object f2555e = new Object();

    /* renamed from: f, reason: collision with root package name */
    SimpleDateFormat f2556f = null;

    private a() {
    }

    public static a b() {
        if (f2535g == null) {
            f2535g = new a();
        }
        return f2535g;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public f a(String str) throws NumberFormatException {
        aD aDVar = new aD(str, ",");
        String strB = aDVar.b();
        if (strB.equals("$GPGGA") || strB.equals("$GNGGA")) {
            String strB2 = aDVar.b();
            String strB3 = aDVar.b();
            String strB4 = aDVar.b();
            String strB5 = aDVar.b();
            String strB6 = aDVar.b();
            String strB7 = aDVar.b();
            String strB8 = aDVar.b();
            String strB9 = aDVar.b();
            String strB10 = aDVar.b();
            if (strB7 != null && !strB7.equals("") && !strB7.equals("0")) {
                try {
                    this.f2548p = Integer.parseInt(strB7);
                } catch (NumberFormatException e2) {
                    this.f2548p = 1;
                }
                long jB = b(strB2);
                if (this.f2545m != f2537b) {
                    a(f2537b, null, jB);
                }
                if (!strB2.equals(this.f2540h)) {
                    this.f2540h = strB2;
                    this.f2541i = b(strB2);
                    this.f2539d.a(this.f2541i);
                }
                if (strB3 != null && !strB3.equals("")) {
                    this.f2539d.a(a(strB3, strB4));
                }
                if (strB5 != null && !strB5.equals("")) {
                    this.f2539d.b(b(strB5, strB6));
                }
                if (strB9 != null && !strB9.equals("")) {
                    this.f2539d.c(Float.parseFloat(strB9) * this.f2544l);
                }
                if (strB10 != null && !strB10.equals("")) {
                    this.f2539d.c(Double.parseDouble(strB10));
                }
                if (strB8 != null && !strB8.equals("")) {
                    HashMap map = new HashMap();
                    int i2 = Integer.parseInt(strB8);
                    map.put("satellites", Integer.valueOf(i2));
                    this.f2539d.a(map);
                    if (this.f2542j != i2) {
                        this.f2542j = i2;
                        a(this.f2545m, map, jB);
                    }
                }
                this.f2546n = true;
                if (this.f2546n && this.f2547o) {
                    a(this.f2539d);
                }
            } else if (strB7 != null && strB7.equals("0")) {
                this.f2548p = 0;
                if (this.f2545m != f2538c) {
                    a(f2538c, null, b(strB2));
                }
            }
        } else if (strB.equals("$GPRMC") || strB.equals("$GNRMC")) {
            String strB11 = aDVar.b();
            String strB12 = aDVar.b();
            String strB13 = aDVar.b();
            String strB14 = aDVar.b();
            String strB15 = aDVar.b();
            String strB16 = aDVar.b();
            String strB17 = aDVar.b();
            String strB18 = aDVar.b();
            if (strB12 != null && !strB12.equals("") && strB12.equals("A")) {
                if (this.f2545m != f2537b) {
                    a(f2537b, null, b(strB11));
                }
                if (!strB11.equals(this.f2540h)) {
                    this.f2540h = strB11;
                    this.f2541i = b(strB11);
                    this.f2539d.a(this.f2541i);
                }
                if (strB13 != null && !strB13.equals("")) {
                    this.f2539d.a(a(strB13, strB14));
                }
                if (strB15 != null && !strB15.equals("")) {
                    this.f2539d.b(b(strB15, strB16));
                }
                if (strB17 != null && !strB17.equals("")) {
                    this.f2539d.a(c(strB17, "N"));
                }
                if (strB18 != null && !strB18.equals("")) {
                    this.f2539d.b(Float.parseFloat(strB18));
                }
                this.f2547o = true;
                if (this.f2546n && this.f2547o) {
                    a(this.f2539d);
                }
            } else if (strB12.equals("V") && strB12 != null && this.f2545m != f2538c) {
                a(f2538c, null, b(strB11));
            }
        }
        return this.f2539d;
    }

    private double a(String str, String str2) throws NumberFormatException {
        double d2 = 0.0d;
        if (str != null && str2 != null && !str.equals("") && !str2.equals("")) {
            double d3 = Double.parseDouble(str);
            double dFloor = Math.floor(d3 / 100.0d);
            double d4 = ((d3 / 100.0d) - dFloor) / 0.6d;
            if (str2.equals(PdfOps.S_TOKEN)) {
                d2 = -(dFloor + d4);
            } else if (str2.equals("N")) {
                d2 = dFloor + d4;
            }
        }
        return d2;
    }

    private double b(String str, String str2) throws NumberFormatException {
        double d2 = 0.0d;
        if (str != null && str2 != null && !str.equals("") && !str2.equals("")) {
            double d3 = Double.parseDouble(str);
            double dFloor = Math.floor(d3 / 100.0d);
            double d4 = ((d3 / 100.0d) - dFloor) / 0.6d;
            if (str2.equals(PdfOps.W_TOKEN)) {
                d2 = -(dFloor + d4);
            } else if (str2.equals("E")) {
                d2 = dFloor + d4;
            }
        }
        return d2;
    }

    private float c(String str, String str2) {
        float f2 = 0.0f;
        if (str != null && str2 != null && !str.equals("") && !str2.equals("")) {
            float f3 = Float.parseFloat(str) / 3.6f;
            if (str2.equals(PdfOps.K_TOKEN)) {
                f2 = f3;
            } else if (str2.equals("N")) {
                f2 = f3 * 1.852f;
            }
        }
        return f2;
    }

    private SimpleDateFormat i() {
        if (this.f2556f == null) {
            this.f2556f = new SimpleDateFormat("HHmmss.SSS");
            this.f2556f.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return this.f2556f;
    }

    private long b(String str) {
        long j2 = 0;
        SimpleDateFormat simpleDateFormatI = i();
        if (str != null && str != null) {
            try {
                long jCurrentTimeMillis = System.currentTimeMillis();
                long time = (jCurrentTimeMillis - (jCurrentTimeMillis % 86400000)) + simpleDateFormatI.parse(String.format((Locale) null, "%010.3f", Double.valueOf(Double.parseDouble(str)))).getTime();
                j2 = time - jCurrentTimeMillis > 43200000 ? time - 86400000 : jCurrentTimeMillis - time > 43200000 ? time + 86400000 : time;
            } catch (ParseException e2) {
                C.a("Error while parsing NMEA time: " + ((Object) e2));
            }
        }
        return j2;
    }

    private void a(int i2, HashMap map, long j2) {
        this.f2540h = null;
        this.f2546n = false;
        this.f2547o = false;
        synchronized (this.f2551s) {
            Iterator it = this.f2551s.iterator();
            while (it.hasNext()) {
                ((g) it.next()).a(this.f2543k, i2, map);
            }
        }
        if (this.f2545m != i2) {
            this.f2539d = new f(this.f2543k);
            this.f2545m = i2;
        }
    }

    private void a(f fVar) {
        this.f2540h = null;
        this.f2546n = false;
        this.f2547o = false;
        if (this.f2539d != null) {
            synchronized (this.f2551s) {
                Iterator it = this.f2551s.iterator();
                while (it.hasNext()) {
                    ((g) it.next()).a(fVar);
                }
            }
            this.f2539d = new f(this.f2543k);
        }
    }

    public void a(g gVar) {
        synchronized (this.f2551s) {
            if (!this.f2551s.contains(gVar)) {
                this.f2551s.add(gVar);
            }
        }
    }

    public void b(g gVar) {
        synchronized (this.f2551s) {
            this.f2551s.remove(gVar);
        }
    }

    public void c() {
        HashMap mapH = null;
        long jA = this.f2541i;
        if (this.f2539d != null) {
            jA = this.f2539d.a();
            mapH = this.f2539d.h();
        }
        a(this.f2545m, mapH, jA);
    }

    public synchronized void d() {
        if (this.f2552t == null) {
            throw new C0129l("Attempt to start External GPS with not Interface set.");
        }
        C0113cs.a().a(I.h.f1376n, 0.0d);
        C0113cs.a().a(I.h.f1375m, 0.0d);
        if (this.f2549q == null) {
            this.f2549q = new b(this);
            this.f2549q.setDaemon(true);
            this.f2545m = f2536a;
            this.f2540h = null;
            this.f2546n = false;
            this.f2547o = false;
            this.f2542j = 0;
            this.f2539d = null;
            c();
            this.f2549q.start();
        }
        C.c("GPS started");
    }

    public boolean e() {
        return this.f2549q != null && this.f2549q.f2558b;
    }

    public synchronized void f() {
        if (this.f2549q != null) {
            this.f2549q.b();
            this.f2549q = null;
            this.f2545m = f2536a;
        }
        c();
        C0113cs.a().a(I.h.f1376n, 0.0d);
        C0113cs.a().a(I.h.f1375m, 0.0d);
        if (this.f2554v != null) {
            try {
                this.f2554v.close();
            } catch (Exception e2) {
                Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        C.c("GPSManager.stop() " + this.f2543k);
    }

    public void a(A.f fVar) {
        this.f2552t = fVar;
        fVar.a(this.f2553u);
    }

    @Override // A.g, A.h
    public A.f a() {
        return this.f2552t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        if (J.I()) {
            if (this.f2554v == null) {
                File file = new File(C1807j.A(), "NEMA_Log.txt");
                if (file.exists()) {
                    file.delete();
                }
                try {
                    file.createNewFile();
                    this.f2554v = new BufferedWriter(new FileWriter(file));
                } catch (IOException e2) {
                    Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            if (this.f2554v != null) {
                try {
                    this.f2554v.append((CharSequence) str).append('\n');
                    this.f2554v.flush();
                } catch (IOException e3) {
                    Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    this.f2554v = null;
                }
            }
        }
    }

    public boolean g() {
        return this.f2552t != null && this.f2552t.k() == 3;
    }
}
