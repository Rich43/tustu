package aP;

import G.C0129l;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import r.C1798a;
import r.C1807j;
import s.C1818g;

/* renamed from: aP.dc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dc.class */
public class C0288dc implements W.B {

    /* renamed from: a, reason: collision with root package name */
    File f3208a = new File(C1807j.A(), f3206h);

    /* renamed from: b, reason: collision with root package name */
    File f3209b = new File(C1807j.A(), f3207i);

    /* renamed from: c, reason: collision with root package name */
    boolean f3210c = false;

    /* renamed from: g, reason: collision with root package name */
    private static C0288dc f3205g = null;

    /* renamed from: h, reason: collision with root package name */
    private static String f3206h = C1798a.f13268b + ".pipe";

    /* renamed from: i, reason: collision with root package name */
    private static String f3207i = "restore.pipe";

    /* renamed from: d, reason: collision with root package name */
    static String f3211d = "DIE";

    /* renamed from: e, reason: collision with root package name */
    static String f3212e = "OFFLINE";

    /* renamed from: f, reason: collision with root package name */
    static String f3213f = "ONLINE";

    private C0288dc() {
    }

    public static C0288dc a() {
        if (f3205g == null) {
            f3205g = new C0288dc();
        }
        return f3205g;
    }

    public boolean b() {
        return a(false);
    }

    public boolean a(boolean z2) {
        this.f3208a = new File(C1807j.A(), f3206h);
        if (this.f3208a.exists()) {
            this.f3208a.delete();
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0288dc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            this.f3208a = new File(C1807j.A(), f3206h);
            if (this.f3208a.exists()) {
                if (!z2) {
                    String str = C1818g.b("There appears to already be another instance of " + C1798a.f13268b + " running.") + "\n" + C1818g.b("Would you like to use the one that is already running?") + "\n\n";
                    String[] strArr = {C1818g.b("Bring to foreground"), C1818g.b("Start another") + " " + C1798a.f13268b};
                    if (!(JOptionPane.showOptionDialog(cZ.a().c(), str, C1818g.b("Already Running"), 0, 3, null, strArr, strArr[1]) == 0)) {
                        return true;
                    }
                    g();
                    return false;
                }
                e();
            }
        }
        d();
        W.C.a().a(this.f3208a, this);
        W.C.a().a(this.f3209b, this);
        return true;
    }

    @Override // W.B
    public void a(File file) {
        if (this.f3210c) {
            if (file.equals(this.f3208a) && !file.exists()) {
                d();
                return;
            }
            if (file.equals(this.f3208a) && file.exists() && file.length() > 0) {
                b(file);
            } else if (file.equals(this.f3209b) && file.exists()) {
                ((C0293dh) cZ.a().c()).p();
                file.delete();
            }
        }
    }

    private void c() {
        this.f3208a.delete();
    }

    private void d() {
        try {
            this.f3208a.createNewFile();
            this.f3208a.deleteOnExit();
            this.f3210c = true;
        } catch (Exception e2) {
            bH.C.c("Failed to create Pipe File, instance monitoring disabled.");
            this.f3210c = false;
        }
    }

    private boolean e() {
        int i2 = 0;
        for (File file = new File(C1807j.A(), f3206h); file.exists(); file = new File(C1807j.A(), f3206h)) {
            int i3 = i2;
            i2++;
            if (2 <= i3) {
                break;
            }
            file.delete();
            f();
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0288dc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            c();
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e3) {
                Logger.getLogger(C0288dc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        this.f3208a.delete();
        return i2 < 2;
    }

    private void f() {
        try {
            c();
            this.f3208a.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(this.f3208a);
            fileOutputStream.write(f3211d.getBytes());
            fileOutputStream.close();
            this.f3208a.deleteOnExit();
            this.f3210c = true;
            bH.C.c("Created Die file.");
        } catch (IOException e2) {
            bH.C.c("Failed to create Pipe File, instance monitoring disabled.");
            this.f3210c = false;
            Logger.getLogger(C0288dc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private void g() {
        try {
            this.f3209b.createNewFile();
        } catch (IOException e2) {
            bH.C.c("Failed to create restore File, Other instance will not be notified.");
            Logger.getLogger(C0288dc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private void b(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            char[] cArr = new char[50];
            int i2 = fileReader.read(cArr);
            try {
                fileReader.close();
            } catch (IOException e2) {
            }
            bH.C.c("actOnFileContents: read=" + i2);
            if (i2 > 0) {
                String strTrim = new String(cArr).trim();
                bH.C.c("fileContent: " + strTrim);
                if (strTrim.equals(f3211d)) {
                    try {
                        bH.C.c("Received message DIE, cutting my throat.");
                        bH.C.b();
                        Thread.sleep(1000L);
                    } catch (InterruptedException e3) {
                        Logger.getLogger(C0288dc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                    file.delete();
                    Runtime.getRuntime().halt(0);
                } else if (strTrim.equals(f3212e)) {
                    bH.C.c("Received message GO_OFFLINE.");
                    if (G.T.a().c() != null) {
                        G.T.a().c().C().c();
                    }
                } else if (strTrim.equals(f3213f)) {
                    bH.C.c("Received message GO_ONLINE.");
                    if (G.T.a().c() != null) {
                        try {
                            G.T.a().c().C().d();
                        } catch (C0129l e4) {
                            Logger.getLogger(C0288dc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e5) {
            Logger.getLogger(C0288dc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
        } catch (IOException e6) {
            Logger.getLogger(C0288dc.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
        }
    }
}
