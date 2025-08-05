package aP;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/bO.class */
public class bO extends Thread {

    /* renamed from: a, reason: collision with root package name */
    com.efiAnalytics.ui.eB f2987a;

    /* renamed from: b, reason: collision with root package name */
    Window f2988b;

    /* renamed from: c, reason: collision with root package name */
    aZ.a f2989c;

    /* renamed from: d, reason: collision with root package name */
    G.R[] f2990d;

    /* renamed from: e, reason: collision with root package name */
    long f2991e;

    /* renamed from: f, reason: collision with root package name */
    public static String f2992f = "FRD";

    /* renamed from: g, reason: collision with root package name */
    public static String f2993g = "SD";

    /* renamed from: h, reason: collision with root package name */
    ArrayList f2994h;

    /* renamed from: i, reason: collision with root package name */
    String f2995i;

    /* renamed from: j, reason: collision with root package name */
    int f2996j;

    /* renamed from: k, reason: collision with root package name */
    boolean f2997k;

    public bO(Window window, G.R[] rArr) {
        super("FRD Processor");
        this.f2987a = null;
        this.f2988b = null;
        this.f2989c = null;
        this.f2990d = null;
        this.f2991e = 0L;
        this.f2994h = new ArrayList();
        this.f2995i = "";
        this.f2996j = 0;
        this.f2997k = true;
        setDaemon(true);
        this.f2988b = window;
        this.f2990d = rArr;
    }

    public void a(File file, File file2) {
        bR bRVar = new bR(this);
        bRVar.a(file);
        bRVar.b(file2);
        String str = f2993g;
        if (file.getName().toLowerCase().endsWith(".frd")) {
            str = f2992f;
        }
        bRVar.a(str);
        this.f2994h.add(bRVar);
    }

    @Override // java.lang.Thread
    public void start() {
        this.f2991e = System.currentTimeMillis();
        super.start();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IllegalArgumentException {
        boolean zA;
        this.f2987a = new com.efiAnalytics.ui.eB(this.f2988b, "Transform Raw Log files to MSL File", "Converting all selected log files, please wait...", true, false);
        this.f2987a.a(new bP(this));
        this.f2987a.setVisible(true);
        ArrayList arrayList = new ArrayList();
        File fileB = null;
        for (int i2 = 0; i2 < this.f2994h.size(); i2++) {
            this.f2996j = i2;
            bR bRVar = (bR) this.f2994h.get(i2);
            File fileA = bRVar.a();
            fileB = bRVar.b();
            arrayList.add(fileB);
            String strC = bRVar.c();
            this.f2987a.a("Converting log " + fileA.getName() + ", please wait...");
            if (strC.equals(f2992f)) {
                this.f2989c = new aZ.f();
            } else {
                this.f2989c = new aZ.j();
            }
            this.f2989c.a(new bQ(this));
            try {
                zA = this.f2989c.a(this.f2990d, fileA);
            } catch (V.a e2) {
                zA = com.efiAnalytics.ui.bV.a(e2.getMessage() + "\n\nWould you like attempt processing this file anyway?", (Component) this.f2988b, true);
            }
            if (!zA) {
                this.f2987a.dispose();
                return;
            }
            try {
                this.f2989c.a(this.f2990d, fileA, fileB, false);
            } catch (V.a e3) {
                e3.printStackTrace();
                String str = "Did not complete log conversion for " + fileA.getName();
                if (i2 < this.f2994h.size()) {
                    str = str + "\nWill continue to try remaining files.";
                }
                bH.C.a(str, e3, this.f2988b);
            }
            if (!this.f2997k) {
                break;
            }
        }
        this.f2987a.dispose();
        if (fileB != null && this.f2994h.size() == 1) {
            if (com.efiAnalytics.ui.bV.a(C1818g.b("File ready for viewing, would you like to open it now?"), (Component) this.f2988b, true)) {
                C0338f.a().a(fileB);
            }
        } else {
            if (this.f2995i == null || this.f2995i.trim().length() <= 0) {
                return;
            }
            J j2 = new J(this.f2995i);
            j2.a(this.f2988b, "Data Transformation Results Log");
            if (arrayList.size() <= 0 || !com.efiAnalytics.ui.bV.a(C1818g.b("File ready for viewing, would you like to open it now?"), (Component) j2, true)) {
                return;
            }
            C0338f.a().a((File[]) arrayList.toArray(new File[arrayList.size()]));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f2997k = false;
        this.f2989c.w();
    }
}
