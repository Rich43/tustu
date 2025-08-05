package aP;

import G.AbstractC0093bz;
import G.C0088bu;
import G.C0113cs;
import G.C0126i;
import G.C0143z;
import G.InterfaceC0120cz;
import W.C0171aa;
import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFrame;
import r.C1798a;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aP/hC.class */
public class hC implements InterfaceC0120cz {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f3511a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private static final Map f3512b = new HashMap();

    public boolean a(Window window, G.R r2, String str) {
        G.Y yA = r2.h().a();
        G.R rA = r2.a(yA);
        C0143z c0143z = new C0143z(bH.ab.a().b());
        try {
            new C0171aa().a(rA, str);
        } catch (V.g e2) {
            bH.C.a("Error opening Saved tune.", e2, window);
        } catch (W.aj e3) {
            bH.C.a("Password Protected Tune Files. Please configure File Passwords.\nSkipping Difference Report.", e3, window);
            return false;
        }
        ArrayList arrayListA = c0143z.a(r2, r2.h(), yA);
        if (arrayListA.size() == 0) {
            return false;
        }
        a(arrayListA, r2, yA, new File(str));
        this.f3511a.clear();
        return true;
    }

    public boolean a(G.R r2, String str) {
        G.Y yA = r2.h().a();
        G.R rA = r2.a(yA);
        C0143z c0143z = new C0143z(bH.ab.a().b());
        try {
            new C0171aa().a(rA, str);
            return !c0143z.a(r2, r2.h(), yA).isEmpty() || ((double) rA.h().l()) / ((double) r2.h().k()) >= 0.4d;
        } catch (V.g e2) {
            bH.C.a("isDifferenceDetected:: Returning true a file is different, Error opening Saved tune:\n" + str);
            bH.C.a("Saved tune file, it appears corrupt:\n" + str, e2, null);
            e2.printStackTrace();
            return true;
        } catch (W.aj e3) {
            bH.C.a("Password Protected Tune Files. Please configure File Passwords.\nSkipping Difference Report.", e3, null);
            return false;
        }
    }

    @Override // G.InterfaceC0120cz
    public boolean a(ArrayList arrayList, G.R r2, G.Y y2, String str) {
        boolean zA;
        if (!C1798a.a().c(C1798a.f13391bu, C1798a.f13392bv)) {
            a(r2, y2, false);
            return false;
        }
        C0143z c0143z = new C0143z(bH.ab.a().b());
        ArrayList arrayListB = c0143z.b(r2, c0143z.a(r2, arrayList));
        Iterator it = arrayListB.iterator();
        while (it.hasNext()) {
            C0088bu c0088bu = (C0088bu) it.next();
            boolean z2 = c0088bu.T() || c0088bu.S() || c0088bu.L();
            if (!c0088bu.G().isEmpty()) {
                for (AbstractC0093bz abstractC0093bz : c0088bu.G()) {
                    if (abstractC0093bz.b() != null && a(arrayList, abstractC0093bz.b())) {
                        boolean z3 = false;
                        try {
                            z3 = abstractC0093bz.aH() == null || abstractC0093bz.aH().isEmpty() || C0126i.a(abstractC0093bz.aH(), (G.aI) r2) != 0.0d;
                        } catch (ax.U e2) {
                            bH.C.a("Unable to evaluate enable condition: " + abstractC0093bz.aH());
                        }
                        if (!z3) {
                            arrayList.remove(r2.c(abstractC0093bz.b()));
                        }
                    }
                }
                if (!z2) {
                    it.remove();
                }
            } else if (!c0088bu.T() && !c0088bu.S() && !c0088bu.L()) {
                it.remove();
            }
        }
        if (arrayListB.isEmpty() || arrayList.isEmpty()) {
            a(r2, y2, false);
            return false;
        }
        if (C1806i.a().a("67r67r8yhdrtrbyuk")) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new hI(this, r2, y2));
            arrayList2.add(new hF(this, r2));
            arrayList2.add(new hE(this, r2, y2));
            zA = a(arrayList, r2, y2, arrayList2, null);
        } else {
            zA = b(arrayList, r2, y2, str);
        }
        return zA;
    }

    private boolean a(ArrayList arrayList, String str) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((G.aM) it.next()).aJ().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void a(ArrayList arrayList, G.R r2, G.Y y2, File file) {
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new hD(this));
        arrayList2.add(new hH(this, r2, arrayList));
        arrayList2.add(new hG(this, r2, y2, file));
        a(arrayList, r2, y2, arrayList2, file);
    }

    private boolean a(ArrayList arrayList, G.R r2, G.Y y2, ArrayList arrayList2, File file) {
        G.R rA = r2.a(y2);
        JFrame jFrameC = cZ.a().c();
        u.h hVar = null;
        try {
            hVar = new u.h(jFrameC, r2, rA, arrayList, arrayList2, file);
            Iterator it = this.f3511a.iterator();
            while (it.hasNext()) {
                u.f fVar = (u.f) it.next();
                hVar.b(fVar);
                hVar.a(fVar);
            }
            if (hVar.a()) {
                if (hVar != null && hVar.isVisible()) {
                    hVar.dispose();
                }
                f3512b.remove(r2.c());
                return false;
            }
            u.h hVar2 = (u.h) f3512b.get(r2.c());
            if (hVar2 != null) {
                hVar2.dispose();
            }
            f3512b.put(r2.c(), hVar);
            com.efiAnalytics.ui.bV.a((Window) jFrameC, (Component) hVar);
            hVar.setVisible(true);
            boolean zB = hVar.b();
            if (hVar != null && hVar.isVisible()) {
                hVar.dispose();
            }
            f3512b.remove(r2.c());
            return zB;
        } catch (Throwable th) {
            if (hVar != null && hVar.isVisible()) {
                hVar.dispose();
            }
            f3512b.remove(r2.c());
            throw th;
        }
    }

    private boolean b(ArrayList arrayList, G.R r2, G.Y y2, String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        String str2 = "There are differences between your offline tune and that\nwhich was last saved to the controller.\nFor advanced difference reports with side by side values, \nGo to EFIAnalytics.com and upgrade " + C1798a.f13268b + " today!\n\nA basic comparison found " + arrayList.size() + " Settings with different values \non the Controller than in the currently loaded tune (" + C1798a.cw + ") \nfor " + r2.c() + "\n\nWould you like to send your offline values to the Controller?\n\nDifferent Settings:\n";
        new C0143z(bH.ab.a().b());
        bH.C.c("Time to get Panels = " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
        int i2 = 0;
        while (true) {
            if (i2 >= arrayList.size()) {
                break;
            }
            str2 = str2 + ((G.aM) arrayList.get(i2)).aJ();
            if (i2 == 20 - 1 && arrayList.size() - 1 > i2) {
                str2 = str2 + "\n" + ((arrayList.size() - i2) - 1) + " more...";
                break;
            }
            if (i2 < arrayList.size() - 1 && i2 < 20) {
                str2 = str2 + ", ";
            }
            if (i2 > 0 && (i2 + 1) % 5 == 0) {
                str2 = str2 + "\n";
            }
            i2++;
        }
        String[] strArr = {"Send Local Settings", "Use Controller Settings", "Get Graphic Difference Reports"};
        for (int iB = 2; iB == 2; iB = com.efiAnalytics.ui.bV.b(str2, "Tune Differences Detected", cZ.a().c(), strArr)) {
            int iB2 = com.efiAnalytics.ui.bV.b(str2, "Tune Differences Detected", cZ.a().c(), strArr);
            if (iB2 == 0) {
                return a(r2, y2);
            }
            if (iB2 == 1) {
                a(r2, y2, false);
                return false;
            }
            C0338f.a().h("differenceReports");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(G.R r2, G.Y y2, boolean z2) {
        Thread threadCurrentThread = Thread.currentThread();
        try {
            r2.a(threadCurrentThread);
            for (int i2 = 0; i2 < y2.e(); i2++) {
                if (z2) {
                    try {
                        r2.h().b(i2, 0, y2.b(i2));
                    } catch (V.g e2) {
                        e2.printStackTrace();
                        return false;
                    }
                } else {
                    r2.h().a(i2, 0, y2.b(i2));
                }
            }
        } catch (Exception e3) {
        }
        r2.b(threadCurrentThread);
        C0113cs.a().a("controllerSettingsLoaded", 1.0d);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(G.R r2, G.Y y2) {
        File fileA = C0338f.a().a(r2, true);
        a(r2, y2, true);
        r2.h().g();
        boolean zC = C0338f.a().c(r2, fileA.getAbsolutePath());
        C0113cs.a().a("controllerSettingsLoaded", 0.0d);
        return zC;
    }
}
