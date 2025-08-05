package Y;

import G.R;
import G.aH;
import V.g;
import bH.C;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:Y/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    String f2205a = "\\s+";

    /* renamed from: b, reason: collision with root package name */
    int f2206b = 0;

    /* renamed from: c, reason: collision with root package name */
    int f2207c = 4;

    /* renamed from: d, reason: collision with root package name */
    int f2208d = 5;

    public void a(R r2, File file, boolean z2) {
        for (b bVar : a(file)) {
            aH aHVarG = r2.g(bVar.c());
            if (z2 && aHVarG == null) {
                aH aHVar = new aH(r2.c());
                aHVar.v(bVar.c());
                try {
                    if (bVar.b() == 1) {
                        aHVar.b("U08");
                    } else if (bVar.b() == 2) {
                        aHVar.b("U16");
                    } else if (bVar.b() == 4) {
                        aHVar.b("S32");
                    } else {
                        C.b("Invalid size for " + bVar.c() + " setting to U08");
                        aHVar.b("U08");
                    }
                    aHVar.b(0);
                    aHVar.a(ControllerParameter.PARAM_CLASS_SCALAR);
                    aHVar.a(1.0d);
                    aHVar.b(0.0d);
                    aHVar.a(a(r2));
                    r2.a(aHVar);
                } catch (g e2) {
                    Logger.getLogger(a.class.getName()).log(Level.SEVERE, "Bad Param class, shouldn't happen", (Throwable) e2);
                }
            } else if (aHVarG != null) {
                aHVarG.a(bVar.a());
            }
        }
    }

    private int a(R r2) {
        int iA = 0;
        Iterator itQ = r2.q();
        while (itQ.hasNext()) {
            aH aHVar = (aH) itQ.next();
            if (aHVar.a() + aHVar.l() > iA) {
                iA = aHVar.a() + aHVar.l();
            }
        }
        return iA;
    }

    public List a(File file) {
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        try {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                String[] strArrSplit = line.split("\\s+");
                if (a(strArrSplit)) {
                    int i2 = Integer.parseInt(strArrSplit[this.f2207c], 16);
                    int i3 = Integer.parseInt(strArrSplit[this.f2206b], 16);
                    b bVar = new b(this);
                    bVar.a(strArrSplit[this.f2208d]);
                    bVar.a(i3);
                    bVar.b(i2);
                    arrayList.add(bVar);
                }
            }
            return arrayList;
        } finally {
            try {
                bufferedReader.close();
            } catch (Exception e2) {
                C.c("failed to close reader");
            }
        }
    }

    private boolean a(String[] strArr) {
        return strArr.length - 1 >= this.f2208d && a(strArr[this.f2206b]) && a(strArr[this.f2207c]) && Integer.parseInt(strArr[this.f2207c], 16) != 0;
    }

    private boolean a(String str) {
        try {
            Integer.parseInt(str, 16);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }
}
