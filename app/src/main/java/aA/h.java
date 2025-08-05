package aA;

import W.C0178d;
import ay.C0932i;
import bH.C;
import bH.C1005m;
import java.io.File;
import java.io.IOException;
import java.util.zip.CRC32;

/* loaded from: TunerStudioMS.jar:aA/h.class */
public class h extends D.b {

    /* renamed from: a, reason: collision with root package name */
    g f2259a;

    /* renamed from: e, reason: collision with root package name */
    private static h f2262e = null;

    /* renamed from: b, reason: collision with root package name */
    CRC32 f2260b = new CRC32();

    /* renamed from: d, reason: collision with root package name */
    private long f2261d = 2592000000L;

    /* renamed from: f, reason: collision with root package name */
    private boolean f2263f = true;

    /* renamed from: g, reason: collision with root package name */
    private boolean f2265g = false;

    /* renamed from: c, reason: collision with root package name */
    C0932i f2264c = new C0932i();

    private h(g gVar) {
        this.f2259a = gVar;
        this.f2264c.a(true);
        this.f2264c.a(new i(this));
        if (c().length > 0) {
            this.f2264c.c();
        }
    }

    public static void a(g gVar) {
        f2262e = new h(gVar);
    }

    public static h a() {
        return f2262e;
    }

    private boolean a(String str) {
        return this.f2265g || (str != null && str.compareTo("70-B3-D5-64-E0-00") >= 0 && str.compareTo("70-B3-D5-64-E3-FF") <= 0) || str.equals("22-33-44-55-66-77");
    }

    public D.c b(String str, String str2) {
        D.c cVar = new D.c();
        if (C1005m.b() || !this.f2263f) {
            try {
                D.a aVarA = a(str, str2);
                if (aVarA != null && aVarA.k() != null && !aVarA.k().isEmpty()) {
                    a(aVarA, aVarA.k());
                    C.d("Cached Device Activation: " + aVarA.b());
                }
                cVar.a(aVarA);
            } catch (f.h e2) {
                cVar.a(65536);
                return cVar;
            } catch (IOException e3) {
                cVar.a(32768);
            }
        }
        return cVar;
    }

    public D.c c(String str, String str2) {
        D.c cVar = new D.c();
        D.a aVarE = e(str, str2);
        if (!aVarE.c().equals("A") || Math.abs(System.currentTimeMillis() - aVarE.j()) > this.f2261d) {
            boolean z2 = C1005m.b() || !this.f2263f;
            if (z2) {
                try {
                    aVarE = a(str, str2);
                    if (aVarE != null && aVarE.k() != null && !aVarE.k().isEmpty()) {
                        a(aVarE, aVarE.k());
                        C.d("Cached Device Activation: " + aVarE.b());
                    }
                } catch (f.h e2) {
                    cVar.a(65536);
                    return cVar;
                } catch (IOException e3) {
                    cVar.a(32768);
                }
            }
            if (aVarE == null || !z2) {
                try {
                    cVar.a(h(str, str2));
                    cVar.a(32768);
                    return cVar;
                } catch (IOException e4) {
                    C.d("No Pending Activation found for " + str2);
                    aVarE = new D.a();
                    aVarE.b(str2);
                    aVarE.a(str);
                    aVarE.c("I");
                    aVarE.a(0);
                }
            }
        } else {
            new k(this, str, str2).start();
        }
        cVar.a(aVarE);
        return cVar;
    }

    public D.c d(String str, String str2) {
        D.c cVar = new D.c();
        D.a aVarE = e(str, str2);
        if (!aVarE.c().equals("A")) {
            try {
                cVar.a(h(str, str2));
                cVar.a(32768);
                return cVar;
            } catch (IOException e2) {
                C.d("No Pending Activation found for " + str2);
                aVarE = new D.a();
                aVarE.b(str2);
                aVarE.a(str);
                aVarE.c("I");
                aVarE.a(0);
            }
        }
        cVar.a(aVarE);
        return cVar;
    }

    private D.a e(String str, String str2) {
        File fileF = f(str, str2);
        D.a aVar = new D.a();
        if (str.equals("BigStuff Gen4") && str2.equals("22-33-44-55-66-77")) {
            aVar.a(str);
            aVar.b(str2);
            aVar.a(0);
            aVar.d("bigstuff3@comcast.net");
            aVar.e("Demo BigStuff3");
            aVar.f("Gen 4 Project");
            aVar.g("248 249 8040");
            aVar.c("A");
            aVar.a(System.currentTimeMillis());
        } else if (fileF.exists()) {
            try {
                aVar.h(new String(C0178d.a(fileF)));
                aVar.a(fileF.lastModified());
                return aVar;
            } catch (Exception e2) {
                C.d("Unable to read activation file: " + fileF.getAbsolutePath());
                fileF.delete();
            }
        }
        aVar.b(str2);
        aVar.a(str);
        return aVar;
    }

    @Override // D.b
    public D.c a(D.a aVar) {
        D.c cVarA;
        if (!C1005m.b() && this.f2263f && a(aVar.b())) {
            aVar.a(32768);
            aVar.c("A");
            b(aVar);
            cVarA = new D.c();
            cVarA.a(aVar);
            cVarA.a(0);
        } else {
            C.d("Activating on Internet: Connected to internet=" + C1005m.b() + ", Serial:" + aVar.b() + " isKnown: " + a(aVar.b()));
            cVarA = super.a(aVar);
            if (cVarA != null && cVarA.b() != null && cVarA.b().c().equals("A")) {
                a(cVarA.b(), cVarA.b().k());
            }
        }
        return cVarA;
    }

    private File f(String str, String str2) {
        this.f2260b.reset();
        this.f2260b.update((str + "_" + str2).getBytes());
        return new File(this.f2259a.a(), Integer.toHexString((int) this.f2260b.getValue()).toUpperCase());
    }

    private File g(String str, String str2) {
        this.f2260b.reset();
        this.f2260b.update((str + "_" + str2).getBytes());
        return new File(this.f2259a.a(), Integer.toHexString((int) this.f2260b.getValue()).toUpperCase() + ".pend");
    }

    public void b() {
        if (C1005m.a()) {
            for (File file : c()) {
                D.a aVar = new D.a();
                try {
                    aVar.a(C0178d.a(file));
                    aVar.c("I");
                    a(aVar);
                    file.delete();
                } catch (f.h e2) {
                    C.d("Invalid pending file. deletivng... " + e2.getLocalizedMessage());
                } catch (IOException e3) {
                    C.d("Invalid pending file. deletivng... " + e3.getLocalizedMessage());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File[] c() {
        return this.f2259a.a().listFiles(new j(this));
    }

    private D.a h(String str, String str2) throws IOException {
        File fileG = g(str, str2);
        if (fileG.exists()) {
            return a(fileG);
        }
        throw new IOException("File not found");
    }

    private D.a a(File file) {
        byte[] bArrA = C0178d.a(file);
        D.a aVar = new D.a();
        try {
            aVar.a(bArrA);
            return aVar;
        } catch (f.h e2) {
            C.c("Invalid pending Activation, deleting file. " + e2.getLocalizedMessage());
            file.delete();
            return aVar;
        }
    }

    private void b(D.a aVar) {
        File fileG = g(aVar.a(), aVar.b());
        try {
            fileG.delete();
            C0178d.a(fileG, aVar.h());
            this.f2264c.c();
        } catch (IOException e2) {
            C.c("Error 5321987 " + e2.getMessage());
        }
    }

    private void a(D.a aVar, String str) {
        if (aVar.b() == null || aVar.b().isEmpty()) {
            return;
        }
        File fileF = f(aVar.a(), aVar.b());
        if (fileF.exists()) {
            fileF.delete();
        }
        try {
            C0178d.a(fileF, str.getBytes());
        } catch (IOException e2) {
            C.c("Error 4321987 " + e2.getMessage());
        }
    }

    public void a(boolean z2) {
        this.f2265g = z2;
    }
}
