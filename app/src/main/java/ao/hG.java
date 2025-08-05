package ao;

import G.C0135r;
import W.C0171aa;
import W.C0200z;
import av.C0864c;
import av.C0867f;
import av.C0869h;
import av.C0873l;
import h.C1737b;
import java.io.File;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:ao/hG.class */
public class hG {
    public hF a(String str) throws V.a {
        String lowerCase = str.toLowerCase();
        if (lowerCase.endsWith(".msq") || lowerCase.endsWith(".tune")) {
            return b(str);
        }
        if (lowerCase.endsWith(".big")) {
            return d(str);
        }
        if (lowerCase.endsWith(".bigtune")) {
            return C0200z.a(str).startsWith("BigStuff Gen4") ? e(str) : d(str);
        }
        if (lowerCase.endsWith(".map")) {
            return f(str);
        }
        throw new V.a("Unknown file type:\n" + str);
    }

    public hF b(String str) throws V.a {
        File fileC;
        if (C1737b.a().a("lkj094320/    q0-fmtg8vc") && (fileC = c(str)) != null) {
            try {
                C0873l c0873lF = C0873l.f();
                c0873lF.a(fileC, a(new File(str)));
                c0873lF.a(str);
                return c0873lF;
            } catch (V.h e2) {
                bH.C.a("Failed to load IniVeFile for iniFile:" + fileC.getAbsolutePath() + ", and tune file: " + str + "\nDoing it the old fashion way..");
            }
        }
        bM bMVar = new bM();
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(h.i.a("tableLoadList", "veBins1,veBins2,veBins3,advTable1,advTable2,afrBins1,afrBins2"), ",");
            while (stringTokenizer.hasMoreTokens()) {
                bMVar.a(stringTokenizer.nextToken());
            }
            bMVar.i(str);
            return bMVar;
        } catch (V.h e3) {
            System.out.println("Error Loading File: " + e3.getLocalizedMessage());
            e3.printStackTrace();
            V.a aVar = new V.a("Error Loading File.\n" + e3.getMessage());
            aVar.a(e3);
            throw aVar;
        }
    }

    private C0135r[] a(File file) {
        String[] strArrC = null;
        try {
            strArrC = new C0171aa().c(file);
        } catch (V.g e2) {
            bH.C.a("Failed to retrieve settings, loading without.");
        }
        C0135r[] c0135rArr = null;
        if (strArrC != null) {
            c0135rArr = new C0135r[strArrC.length];
            for (int i2 = 0; i2 < strArrC.length; i2++) {
                c0135rArr[i2] = new C0135r();
                c0135rArr[i2].v(strArrC[i2]);
            }
        }
        return c0135rArr;
    }

    private File c(String str) {
        String strA = C0200z.a(str);
        if (1 == 0 || strA == null || strA.isEmpty()) {
            return null;
        }
        try {
            return C1807j.a(C0645bi.a().b(), strA);
        } catch (V.a e2) {
            Logger.getLogger(hG.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    private hF d(String str) throws V.a {
        try {
            C0867f c0867fF = C0867f.f();
            c0867fF.a(str);
            return c0867fF;
        } catch (V.h e2) {
            Logger.getLogger(hG.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            System.out.println("Error Loading BIG File.");
            e2.printStackTrace();
            V.a aVar = new V.a(e2.getMessage());
            aVar.a(e2);
            throw aVar;
        }
    }

    private hF e(String str) throws V.a {
        try {
            C0864c c0864cF = C0864c.f();
            c0864cF.a(str);
            return c0864cF;
        } catch (V.h e2) {
            Logger.getLogger(hG.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            System.out.println("Error Loading BIG File.");
            e2.printStackTrace();
            V.a aVar = new V.a(e2.getMessage());
            aVar.a(e2);
            throw aVar;
        }
    }

    private hF f(String str) throws V.a {
        try {
            C0869h c0869hF = C0869h.f();
            c0869hF.a(str);
            return c0869hF;
        } catch (V.h e2) {
            Logger.getLogger(hG.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            System.out.println("Error Loading DTA File.");
            e2.printStackTrace();
            V.a aVar = new V.a(e2.getMessage());
            aVar.a(e2);
            throw aVar;
        }
    }
}
