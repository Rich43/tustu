package z;

import A.t;
import A.v;
import G.J;
import G.R;
import bH.C;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: z.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:z/c.class */
public class C1899c {

    /* renamed from: a, reason: collision with root package name */
    public static String f14080a = "MegaSquirtRs232CommManager";

    /* renamed from: b, reason: collision with root package name */
    public static String f14081b = "MegaSquirtRs232CommManagerAlternate";

    /* renamed from: c, reason: collision with root package name */
    public static String f14082c = "Agressive Reinitialize CommManager";

    /* renamed from: d, reason: collision with root package name */
    public static String f14083d = "K-Line Echo Filtering CommManager";

    /* renamed from: e, reason: collision with root package name */
    public static String f14084e = "Multi Interface MegaSquirt Driver";

    /* renamed from: f, reason: collision with root package name */
    public static String f14085f = bQ.l.f7436a;

    /* renamed from: h, reason: collision with root package name */
    private static C1899c f14086h = null;

    /* renamed from: i, reason: collision with root package name */
    private ArrayList f14087i = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    HashMap f14088g = new HashMap();

    public static C1899c a() {
        if (f14086h == null) {
            f14086h = new C1899c();
        }
        return f14086h;
    }

    public J a(R r2, String str, A.i iVar, B.i iVar2, String str2) {
        if (str != null && str.equals(f14084e)) {
            t tVarA = v.a().a(r2);
            String strB = v.a().b(r2);
            if (tVarA.a() == null && strB != null) {
                A.f fVarA = null;
                try {
                    fVarA = iVar.a(strB, str2);
                } catch (IllegalAccessException e2) {
                    Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, IllegalAccess", (Throwable) e2);
                } catch (NoClassDefFoundError e3) {
                    Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, NoClassDefFoundError", (Throwable) e3);
                } catch (UnsatisfiedLinkError e4) {
                    Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, UnsatisfiedLinkError", (Throwable) e4);
                } catch (Error e5) {
                    Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, Other", (Throwable) e5);
                } catch (InstantiationException e6) {
                    Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface", (Throwable) e6);
                }
                if (fVarA == null) {
                    try {
                        fVarA = iVar.a(iVar.b().a(), str2);
                    } catch (IllegalAccessException e7) {
                        Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, IllegalAccess", (Throwable) e7);
                    } catch (InstantiationException e8) {
                        Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface", (Throwable) e8);
                    } catch (Exception e9) {
                        Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, Other", (Throwable) e9);
                    } catch (NoClassDefFoundError e10) {
                        Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, NoClassDefFoundError", (Throwable) e10);
                    } catch (UnsatisfiedLinkError e11) {
                        Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, UnsatisfiedLinkError", (Throwable) e11);
                    } catch (Error e12) {
                        Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, Other", (Throwable) e12);
                    }
                } else {
                    try {
                        v.a().b(r2.c(), fVarA);
                    } catch (Error e13) {
                        Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to set ControllerInterface settings", (Throwable) e13);
                    } catch (Exception e14) {
                        C.a("failed to load Comms Driver: " + e14.getLocalizedMessage());
                    }
                }
                if (fVarA != null) {
                    tVarA.a(fVarA);
                }
            }
            return tVarA;
        }
        if (str == null || !str.equals(f14085f)) {
            t tVarA2 = v.a().a(r2);
            try {
                A.f fVarA2 = iVar.a(iVar.b().a(), str2);
                tVarA2.a(fVarA2);
                v.a().b(r2.c(), fVarA2);
                return tVarA2;
            } catch (Error e15) {
                Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, Error", (Throwable) e15);
                return null;
            } catch (IllegalAccessException e16) {
                Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, IllegalAccess", (Throwable) e16);
                return null;
            } catch (InstantiationException e17) {
                Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface", (Throwable) e17);
                return null;
            } catch (Exception e18) {
                Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, Generl Exception", (Throwable) e18);
                return null;
            }
        }
        bQ.l lVarA = bQ.j.a().a(r2);
        String strB2 = bQ.j.a().b(r2);
        if (strB2 == null) {
            strB2 = iVar.b().a();
        }
        if (lVarA.a() == null && strB2 != null) {
            A.f fVarA3 = null;
            try {
                fVarA3 = iVar.a(strB2, iVar2, str2);
                bQ.j.a().b(r2.c(), fVarA3);
            } catch (IllegalAccessException e19) {
                Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, IllegalAccess", (Throwable) e19);
            } catch (InstantiationException e20) {
                Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface", (Throwable) e20);
            }
            if (fVarA3 == null) {
                try {
                    fVarA3 = iVar.a(iVar.b().a(), str2);
                } catch (IllegalAccessException e21) {
                    Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface, IllegalAccess", (Throwable) e21);
                } catch (InstantiationException e22) {
                    Logger.getLogger(C1899c.class.getName()).log(Level.SEVERE, "Failed to instantiate ControllerInterface", (Throwable) e22);
                }
            } else {
                try {
                    bQ.j.a().b(r2.c(), fVarA3);
                } catch (Error e23) {
                    C.a("failed to load Comms Driver: " + e23.getLocalizedMessage());
                } catch (Exception e24) {
                    C.a("failed to load Comms Driver: " + e24.getLocalizedMessage());
                }
            }
            if (fVarA3 != null) {
                lVarA.a(fVarA3);
            }
        }
        return lVarA;
    }

    public void a(C1900d c1900d) {
        this.f14087i.add(c1900d);
    }

    public Iterator b() {
        return this.f14087i.iterator();
    }

    public boolean a(String str) {
        Iterator it = this.f14087i.iterator();
        while (it.hasNext()) {
            if (((C1900d) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
