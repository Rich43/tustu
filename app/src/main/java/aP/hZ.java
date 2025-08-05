package aP;

import G.C0045ae;
import G.C0125h;
import G.C0135r;
import W.C0171aa;
import W.C0172ab;
import W.C0196v;
import W.C0197w;
import W.C0200z;
import bH.C1011s;
import bo.C1205a;
import bs.C1265C;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import r.C1798a;
import r.C1806i;
import r.C1807j;
import s.C1812a;
import v.C1883c;
import z.C1899c;

/* loaded from: TunerStudioMS.jar:aP/hZ.class */
public class hZ {

    /* renamed from: a, reason: collision with root package name */
    private boolean f3552a = false;

    /* renamed from: b, reason: collision with root package name */
    private static boolean f3553b = true;

    public aE.a a(String str) {
        G.R r2 = new G.R();
        G.T tA = G.T.a();
        C0125h.a().b();
        aE.a aVar = new aE.a();
        aVar.e(C1806i.a().a("poij fe07r32;lkjrew09345rv"));
        r2.q(str);
        aVar.a(str);
        aVar.i(new File(str).getName());
        I.k.a().a(new W.ao(aVar));
        r2.a(aVar.u());
        C0125h.a().a(r2);
        r2.a(C1812a.a());
        boolean z2 = C1798a.a().c(C1798a.f13387bq, C1798a.f13388br) && C1798a.a().c(C1798a.f13389bs, C1798a.f13390bt);
        bH.Z z3 = new bH.Z();
        z3.a();
        String[] strArr = {"baudRate", "commPort"};
        String[] strArr2 = {"CommSettingBaud Rate", "CommSettingCom Port"};
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (aVar.getProperty(strArr2[i2]) == null && aVar.getProperty(strArr[i2]) != null) {
                aVar.setProperty(strArr2[i2], aVar.getProperty(strArr[i2]));
            }
        }
        File[] fileArr = {aVar.j()};
        String strA = C0200z.a(fileArr[0]);
        if ((C1806i.a().a("432;LK;LKFS") || strA.startsWith("MS1/Extra format 029y3")) && (!C1798a.f13267a.equals(aVar.R()) || C1806i.a().a("432;LK;LKFS"))) {
            bH.Z z4 = new bH.Z();
            z4.a();
            File fileE = C1807j.e(strA);
            if (fileE != null) {
                double dC = C0200z.c(fileArr[0]);
                double dC2 = C0200z.c(fileE);
                if (dC2 > dC) {
                    bH.C.d("Updating project ecu def from version: " + dC + " to version: " + dC2 + " from installer.");
                    File file = new File(fileArr[0].getParentFile(), fileArr[0].getName() + ".bak");
                    if (file.exists()) {
                        file.delete();
                    }
                    C1011s.b(fileArr[0], file);
                    C1011s.b(fileE, fileArr[0]);
                } else if (C1806i.a().a("432;LK;LKFS")) {
                    bH.C.d("Checking Server for newer iniVersion.");
                    C0197w.a(strA, dC2, C1807j.c());
                }
            }
            aVar.x(C1798a.f13267a);
            bH.C.c("ECU def update check time: " + z4.d());
        }
        X.c.a().a(W.ak.b(aVar.j()));
        String[] strArrZ = aVar.z();
        if (strArrZ == null) {
            throw new V.e("Project Corrupt, no configuration options found.");
        }
        boolean z5 = false;
        String string = null;
        if (a()) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(aVar.u()).append("_");
                sb.append(strA);
                for (String str2 : strArrZ) {
                    sb.append("_").append(str2);
                }
                sb.append(aVar.j().lastModified());
                sb.append(z2).append("_");
                sb.append(aVar.j().length());
                new bH.Z().a();
                string = sb.toString();
                Object objA = X.c.a().a(string, aVar.j().getParentFile());
                if (objA == null) {
                    bH.C.d("failed to load cached config definition, will load ini. Error: Matching Config not found in cache");
                } else {
                    try {
                        r2.a((C0045ae) objA);
                        r2.q(str);
                        r2.a(aVar.u());
                        r2.a(C1812a.a());
                        r2.j();
                        z5 = true;
                    } catch (Exception e2) {
                        bH.C.d("failed to load cached config, will load ini. Error: " + e2.getLocalizedMessage());
                    }
                }
            } catch (IOException e3) {
                bH.C.d("failed to load cached config, will load ini. Error: " + e3.getLocalizedMessage());
            } catch (ClassNotFoundException e4) {
                bH.C.d("failed to load cached config, will load ini. Error: " + e4.getLocalizedMessage());
            }
        }
        if (!z5) {
            String strV = aVar.v();
            if (strV.toLowerCase().indexOf(".ini") <= 0 && !strV.toLowerCase().endsWith(".ecu")) {
                throw new V.a("Unidentified ecu configuration file extension for:\n" + strV);
            }
            C0196v.a().a(fileArr);
            for (int i3 = 0; i3 < strArrZ.length; i3++) {
                C0135r c0135rC = C0196v.a().c(strArrZ[i3]);
                if (c0135rC != null) {
                    r2.a(c0135rC);
                } else {
                    r2.a(new C0135r(strArrZ[i3]));
                }
            }
            C0172ab c0172ab = new C0172ab();
            c0172ab.a(b());
            try {
                if (!C1806i.a().a("HF-05[P54;'FD") && r2.c("tsCanId") == null) {
                    r2 = c0172ab.a(r2, C1807j.f13497G, false);
                }
            } catch (V.g e5) {
                e5.printStackTrace();
                com.efiAnalytics.ui.bV.d("Unable to load PcVariables to " + r2.c() + "\n" + e5.getMessage(), null);
            }
            try {
                r2 = c0172ab.a(r2, strV, true, z2);
                try {
                    I.p.a().a(r2);
                } catch (V.g e6) {
                    bH.C.b("Failed to load Supplemental ECU Definition for Main Controller. Message:" + e6.getLocalizedMessage());
                    e6.printStackTrace();
                }
                C1205a.a(r2);
                if (a() && string != null) {
                    C0045ae c0045aeAb = r2.ab();
                    try {
                        X.c.a().a(aVar.j().getParentFile());
                        X.c.a().a(c0045aeAb, string, aVar.j().getParentFile());
                    } catch (Exception e7) {
                        Logger.getLogger(hZ.class.getName()).log(Level.SEVERE, "Failed to Cache Config. cacheKey=" + string, (Throwable) e7);
                    }
                }
            } catch (V.g e8) {
                e8.printStackTrace();
                throw e8;
            }
        }
        bH.C.d("Time to load primary config: " + z3.d() + "ms. used cached config: " + z5);
        if (aVar.d() != null && !aVar.d().equals("")) {
            r2.O().a(G.cJ.a().b(aVar.d()));
            G.cT cTVarA = G.cJ.a().a(aVar.d());
            r2.O().a(cTVarA);
            if (cTVarA != null) {
                r2.O().d(true);
            }
        } else if (C1798a.f13268b.equals(C1798a.f13337as)) {
            r2.O().a(new J.a());
            r2.O().a(new J.b());
            r2.O().d(true);
        }
        try {
            File fileD = aVar.d(r2.c());
            if (fileD.exists()) {
                try {
                    new C0171aa().a(r2, fileD.getAbsolutePath());
                } catch (Exception e9) {
                    bH.C.d(fileD.getName() + " either missing or invalid for " + r2.c());
                    fileD.delete();
                    e9.printStackTrace();
                }
            }
            int iB = aVar.b(r2.O().x());
            G.aM aMVarC = r2.c("tsCanId");
            if (iB >= 0 && aMVarC != null) {
                try {
                    aMVarC.a(r2.p(), iB);
                } catch (Exception e10) {
                    Logger.getLogger(hZ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e10);
                }
            }
            r2.O().r(iB);
        } catch (Exception e11) {
            e11.printStackTrace();
            com.efiAnalytics.ui.bV.d("Unable to load PcVariables to " + r2.c() + "\n" + e11.getMessage(), null);
        }
        r2.O().b(aVar.r());
        try {
            r2.O().j(aVar.m(r2.c()));
        } catch (Exception e12) {
            r2.O().p(SerialPort.BAUDRATE_115200);
        }
        r2.O().k(aVar.n(r2.c()));
        try {
            r2.O().k(aVar.n(r2.c()));
        } catch (Exception e13) {
        }
        try {
            int iC = aVar.c(r2.O().as());
            if (!C1806i.a().a(" 98 98  0gep9gds09kfg09") && iC > 15) {
                iC = 15;
            }
            r2.c(iC);
        } catch (NumberFormatException e14) {
            r2.c(15);
            bH.C.d("Unable to set Data Rate, using default 15/sec");
        }
        try {
            C1265C.a().a(r2);
        } catch (V.g e15) {
            bH.C.a("Unable to get Wue Analyze Supported Curves.", e15, this);
        }
        r2.D(aVar.r(r2.c()));
        aVar.a(r2);
        String strC = aVar.C();
        if (strC == null) {
            strC = C1798a.f13371ba;
        }
        B.i iVarD = C1807j.d(new File(str));
        if (iVarD == null) {
        }
        if (iVarD == null || iVarD.e() == null || iVarD.e().isEmpty()) {
            B.b.c().a(null);
        } else {
            B.b.c().a(iVarD);
        }
        try {
            r2.c(C1899c.a().a(r2, strC, aV.w.c(), iVarD, null));
        } catch (Error e16) {
            bH.C.a("Unable to load Communication Driver: " + strC);
            e16.printStackTrace();
        } catch (Exception e17) {
            com.efiAnalytics.ui.bV.d("Unable to load Communication Driver.", cZ.a().c());
            bH.C.a("Unable to load Communication Driver: " + strC);
            bH.C.a(e17);
            try {
                r2.c(C1899c.a().a(r2, aV.w.c().b().a(), aV.w.c(), iVarD, null));
            } catch (Error e18) {
                bH.C.a("Unable to load Communication Driver: " + strC);
                e18.printStackTrace();
            } catch (Exception e19) {
                bH.C.a("Unable to load Communication Driver again: " + strC);
            }
        }
        if (0 == 0 || !(r2.C() instanceof bQ.l)) {
            Iterator itI = aVar.I();
            while (itI.hasNext()) {
                aE.d dVar = (aE.d) itI.next();
                try {
                    C0172ab c0172ab2 = new C0172ab();
                    c0172ab2.a(b());
                    String strA2 = dVar.a(aVar);
                    G.R r3 = new G.R();
                    r3.q(str);
                    r3.a(dVar.a());
                    String[] strArrD = dVar.d();
                    G.F f2 = new G.F();
                    f2.a(r3);
                    r3.a(f2);
                    for (String str3 : strArrD) {
                        r3.a(new C0135r(str3));
                    }
                    if (r3.c("tsCanId") == null) {
                        r3 = c0172ab2.a(r3, C1807j.f13497G, false, z2);
                    }
                    r3.O().r(dVar.e());
                    G.R rA = c0172ab2.a(r3, strA2, true, z2);
                    try {
                        I.p.a().a(rA);
                    } catch (V.g e20) {
                        bH.C.b("Failed to load Supplemental ECU Definition for Controller " + rA.c() + ". Message:" + e20.getLocalizedMessage());
                        e20.printStackTrace();
                    }
                    rA.a(C1812a.a());
                    try {
                        if (new File(aVar.k(rA.c())).exists()) {
                            c0172ab2.b(true);
                            rA = c0172ab2.a(rA, aVar.k(rA.c()), true, z2);
                            c0172ab2.b(false);
                        }
                        C1205a.a(rA);
                        if (0 != 0 && !(r2.C() instanceof bQ.l)) {
                            rA.O().n(true);
                        } else if (r2.C() instanceof bQ.l) {
                            try {
                                try {
                                    rA.c(C1899c.a().a(rA, strC, aV.w.c(), iVarD, rA.c()));
                                } catch (Exception e21) {
                                    com.efiAnalytics.ui.bV.d("Unable to load Communication Driver for CAN Device.", cZ.a().c());
                                    bH.C.a("Unable to load Communication Driver " + strC + " for CAN Device: " + rA.c());
                                    bH.C.a(e21);
                                }
                            } catch (Error e22) {
                                bH.C.a("Unable to load Communication Driver " + strC + " for CAN Device: " + rA.c());
                                e22.printStackTrace();
                            }
                        } else {
                            rA.a(r2.C());
                        }
                        rA.O().b(true);
                        rA.O().c(dVar.f());
                        File fileD2 = aVar.d(rA.c());
                        if (fileD2.exists()) {
                            try {
                                new C0171aa().a(rA, fileD2.getAbsolutePath());
                            } catch (Exception e23) {
                                bH.C.d(fileD2.getName() + " either missing or invalid for " + r2.c());
                                e23.printStackTrace();
                                fileD2.delete();
                            }
                        }
                        tA.a(rA);
                        aVar.a(rA);
                        int iE = dVar.e();
                        G.aM aMVarC2 = rA.c("tsCanId");
                        if (aMVarC2 != null) {
                            try {
                                aMVarC2.a(rA.p(), iE);
                            } catch (Exception e24) {
                                Logger.getLogger(hZ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e24);
                            }
                            G.aR.a().a(dVar.a(), aMVarC2.aJ(), new C0420ia(this, dVar));
                        }
                        rA.O().r(iE);
                        rA.D(dVar.h());
                    } catch (V.g e25) {
                        e25.printStackTrace();
                        throw new V.a(e25.getMessage());
                    }
                } catch (V.g e26) {
                    e26.printStackTrace();
                    throw new V.a(e26.getMessage());
                }
            }
        }
        try {
            if (new File(aVar.x()).exists()) {
                C0172ab c0172ab3 = new C0172ab();
                c0172ab3.a(b());
                c0172ab3.b(true);
                r2 = c0172ab3.a(r2, aVar.x(), true, z2);
                c0172ab3.b(false);
            }
        } catch (Exception e27) {
            e27.printStackTrace();
        }
        Y y2 = new Y(aVar);
        tA.a(r2);
        if (r2.c("tsCanId") != null) {
            try {
                G.aR.a().a(r2.c(), "tsCanId", y2);
            } catch (V.a e28) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e28);
            }
        }
        return aVar;
    }

    public aE.a a(String str, File file, C0135r[] c0135rArr) {
        aE.a aVar = new aE.a();
        aVar.h(C1807j.u() + "Temp");
        aVar.i("Temp");
        G.R r2 = new G.R();
        r2.a(aVar.u());
        r2.a(C1812a.a());
        if (c0135rArr != null) {
            for (C0135r c0135r : c0135rArr) {
                r2.a(c0135r);
            }
            aVar.a(c0135rArr);
        }
        C0172ab c0172ab = new C0172ab();
        c0172ab.a(b());
        try {
            r2.q(aVar.t());
            G.R rA = c0172ab.a(r2, file.getCanonicalPath());
            rA.c(C1899c.a().a(rA, C1798a.f13371ba, aV.w.c(), null, null));
            rA.O().b(aVar.r());
            aVar.a();
            aVar.l(file.getName());
            C1011s.a(file, aVar.j());
            aVar.p("Temporary Project for viewing " + C1798a.cw + " files");
            aVar.a(c0135rArr);
            com.efiAnalytics.apps.ts.dashboard.Z zA = new C1388aa().a(rA, "FrontPage", 2, 4);
            zA.b(str);
            if (aVar.U()) {
                aVar.e();
            } else {
                new C1883c(C1807j.G()).a(aVar.l().getAbsolutePath(), zA);
                aVar.o(aVar.n());
            }
            aVar.b();
            return aVar;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Error loading Configuration based on \n" + file.getAbsolutePath(), e2);
        }
    }

    public aE.a a(String str, String str2, File file, C0135r[] c0135rArr) {
        aE.a aVar = new aE.a();
        aVar.e(C1806i.a().a("poij fe07r32;lkjrew09345rv"));
        String str3 = C1807j.u() + str;
        aVar.h(str3);
        aVar.i(str);
        G.R r2 = new G.R();
        r2.a(aVar.u());
        r2.a(C1812a.a());
        if (c0135rArr != null) {
            for (C0135r c0135r : c0135rArr) {
                r2.a(c0135r);
            }
            aVar.a(c0135rArr);
        }
        C0172ab c0172ab = new C0172ab();
        B.i iVarD = C1807j.d(new File(str3));
        try {
            r2.q(aVar.t());
            G.R rA = c0172ab.a(r2, file.getCanonicalPath());
            rA.c(C1899c.a().a(rA, C1798a.f13371ba, aV.w.c(), iVarD, null));
            G.T.a().a(rA);
            rA.O().b(aVar.r());
            aVar.a();
            aVar.l(file.getName());
            aVar.l(file.getName().toLowerCase().endsWith(".ecu") ? "mainController.ecu" : "mainController.ini");
            C1011s.a(file, aVar.j());
            String str4 = "Project " + str;
            if (B.b.c().d() != null && !B.b.c().d().e().isEmpty()) {
                str4 = str4 + "\nfor serial Number: " + B.b.c().d().e();
                aVar.c(str, bQ.l.f7436a);
            }
            aVar.p(str4);
            aVar.a(c0135rArr);
            com.efiAnalytics.apps.ts.dashboard.Z zA = new C1388aa().a(rA, "FrontPage", 2, 4);
            zA.b(str2);
            C1883c c1883c = new C1883c(C1807j.G());
            if (!aVar.l().exists()) {
                c1883c.a(aVar.l().getAbsolutePath(), zA);
            }
            aVar.o(aVar.n());
            if (rA.C() instanceof bQ.l) {
                A.f fVarA = ((bQ.l) rA.C()).a();
                bQ.j.a().a(str, fVarA.h());
                bQ.j.a().a(str, fVarA);
                A.v.a().a(str, fVarA.h());
                A.v.a().a(str, fVarA);
            }
            if (aVar.U()) {
                aVar.e();
            }
            aVar.b();
            return aVar;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Error loading Configuration based on \n" + file.getAbsolutePath(), e2);
        }
    }

    public boolean a() {
        return this.f3552a;
    }

    public static boolean b() {
        return f3553b;
    }
}
