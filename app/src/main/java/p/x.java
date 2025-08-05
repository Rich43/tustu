package p;

import G.C0134q;
import G.T;
import W.ap;
import W.ar;
import bH.W;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:p/x.class */
public class x implements S.g, S.l {

    /* renamed from: b, reason: collision with root package name */
    ArrayList f13253b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    List f13254c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    boolean f13255d = false;

    /* renamed from: a, reason: collision with root package name */
    public static String f13252a = ".trigger";

    /* renamed from: e, reason: collision with root package name */
    private static x f13256e = null;

    private x() {
    }

    public static x a() {
        if (f13256e == null) {
            f13256e = new x();
        }
        return f13256e;
    }

    @Override // S.l
    public ArrayList b() {
        if (this.f13253b.isEmpty()) {
            d();
        }
        return this.f13253b;
    }

    @Override // S.l
    public S.n a(String str) {
        for (int i2 = 0; i2 < b().size(); i2++) {
            S.n nVar = (S.n) this.f13253b.get(i2);
            if (nVar.a() != null && nVar.a().equals(str)) {
                return nVar;
            }
        }
        return null;
    }

    @Override // S.l
    public boolean b(String str) {
        for (int i2 = 0; i2 < b().size(); i2++) {
            if (((S.h) this.f13253b.get(i2)).a().equals(str)) {
                this.f13253b.remove(i2);
                c(str);
                return true;
            }
        }
        return false;
    }

    @Override // S.l
    public void a(S.n nVar) {
        for (int i2 = 0; i2 < b().size(); i2++) {
            S.h hVar = (S.h) this.f13253b.get(i2);
            if (hVar.a().equals(nVar.a())) {
                this.f13253b.set(i2, nVar);
                if (!b(nVar)) {
                    bH.C.b("Failed to save updated ExpressionEventTrigger: " + nVar.a());
                }
                G.R rC = T.a().c();
                boolean zC = hVar.c();
                S.e.a().a(rC.c(), nVar.a());
                if (zC) {
                    try {
                        S.e.a().a(rC.c(), nVar);
                        return;
                    } catch (C0134q e2) {
                        Logger.getLogger(x.class.getName()).log(Level.SEVERE, "", (Throwable) e2);
                        return;
                    }
                }
                return;
            }
        }
        this.f13253b.add(nVar);
        if (!b(nVar)) {
            bH.C.b("Failed to save new ExpressionEventTrigger: " + nVar.a());
        }
        G.R rC2 = T.a().c();
        boolean zC2 = nVar.c();
        S.e.a().a(rC2.c(), nVar.a());
        if (zC2) {
            try {
                S.e.a().a(rC2.c(), nVar);
            } catch (C0134q e3) {
                Logger.getLogger(x.class.getName()).log(Level.SEVERE, "", (Throwable) e3);
            }
        }
    }

    private void d() {
        this.f13253b.clear();
        boolean z2 = this.f13255d != (T.a().c() != null);
        for (File file : C1807j.B().listFiles(new y(this))) {
            S.n nVarA = a(file);
            if (nVarA != null && z2) {
                this.f13253b.add(nVarA);
            }
        }
        if (this.f13253b.isEmpty()) {
            this.f13253b.addAll(C1776b.b());
        }
        Iterator it = this.f13253b.iterator();
        while (it.hasNext()) {
            S.n nVar = (S.n) it.next();
            b(nVar);
            G.R rC = T.a().c();
            if (rC != null && nVar.c()) {
                try {
                    S.e.a().a(rC.c(), nVar);
                } catch (C0134q e2) {
                    bH.C.a("Failed to start monitoring on Triggered Event: " + nVar.a() + ", err: " + e2.getLocalizedMessage());
                }
            }
        }
        this.f13255d = T.a().c() != null;
    }

    private S.n a(File file) {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                properties.load(fileInputStream);
                S.n nVar = new S.n();
                nVar.a(new ar(properties, ""));
                nVar.g(W.b(file.getName(), f13252a, ""));
                nVar.h("");
                if (nVar.a() != null && !nVar.a().isEmpty()) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e2) {
                    }
                    return nVar;
                }
                bH.C.b("Invalid trigger in trigger file, ignoring:\n" + file.getAbsolutePath());
                try {
                    fileInputStream.close();
                } catch (Exception e3) {
                }
                return null;
            } catch (IOException e4) {
                bH.C.a("Unable to load UserEventTrigger file: " + e4.getLocalizedMessage());
                try {
                    fileInputStream.close();
                } catch (Exception e5) {
                }
                return null;
            }
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Exception e6) {
            }
            throw th;
        }
    }

    private boolean c(String str) {
        return new File(C1807j.B(), str + f13252a).delete();
    }

    public boolean b(S.n nVar) {
        nVar.a(new ar(new Properties(), ""));
        nVar.i("");
        File fileB = C1807j.B();
        File file = new File(fileB, nVar.a() + f13252a);
        if (file.exists() && !file.delete()) {
            bH.C.b("Failed to delete existing ExpressionEventTrigger file: " + file.getAbsolutePath());
            return false;
        }
        File file2 = new File(fileB, nVar.a() + f13252a);
        AutoCloseable autoCloseable = null;
        try {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                ap apVarH = nVar.h();
                if (apVarH instanceof ar) {
                    ((ar) apVarH).a().store(fileOutputStream, "Attributes for ExpressionEventTrigger: " + nVar.a());
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e2) {
                        }
                    }
                    return true;
                }
                bH.C.a("EventTrigger Persistor not instanceof PropertiesPersistor!!");
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e3) {
                    }
                }
                return false;
            } catch (IOException e4) {
                bH.C.a("Failed to Save ExpressionEventTrigger! Error: " + e4.getLocalizedMessage() + "\nFile:\n" + file2.getAbsolutePath());
                if (0 != 0) {
                    try {
                        autoCloseable.close();
                    } catch (Exception e5) {
                        return false;
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    autoCloseable.close();
                } catch (Exception e6) {
                    throw th;
                }
            }
            throw th;
        }
    }

    @Override // S.g
    public S.a a(String str, String str2) {
        return a(str2);
    }

    @Override // S.l
    public List c() {
        ArrayList arrayList = new ArrayList();
        Iterator it = b().iterator();
        while (it.hasNext()) {
            arrayList.add(((S.n) it.next()).a());
        }
        return arrayList;
    }
}
