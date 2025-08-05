package W;

import G.C0135r;
import G.C0136s;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:W/I.class */
public class I {
    public ArrayList a(String str) throws IOException {
        BufferedReader bufferedReaderA = C0193s.a(new File(str));
        ArrayList arrayList = new ArrayList();
        while (true) {
            String line = bufferedReaderA.readLine();
            if (line == null) {
                return arrayList;
            }
            if (line.trim().startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                String strTrim = line.trim().substring(1).trim();
                if (strTrim.indexOf(59) != -1) {
                    strTrim = strTrim.substring(0, strTrim.indexOf(59)).trim();
                }
                if (strTrim.startsWith("set") || strTrim.startsWith("unset")) {
                    if (strTrim.indexOf("MSLVV_COMPATIBLE") == -1 && strTrim.indexOf("TUNERSTUDIO") == -1 && strTrim.indexOf("INI_VERSION_2") == -1) {
                        H h2 = new H();
                        h2.a(strTrim.substring(strTrim.lastIndexOf(" ")).trim());
                        h2.a(strTrim.startsWith("set"));
                        arrayList.add(h2);
                    }
                }
            }
        }
    }

    public C0136s[] a(C0136s[] c0136sArr, String str) {
        long jNanoTime = System.nanoTime();
        try {
            J jA = new C0172ab().a(str);
            ArrayList arrayList = new ArrayList();
            Iterator it = jA.a().iterator();
            while (it.hasNext()) {
                String strB = ((M) it.next()).b();
                if (strB.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) && strB.lastIndexOf(" ") != -1) {
                    String strSubstring = strB.substring(strB.lastIndexOf(" ") + 1);
                    for (int i2 = 0; i2 < c0136sArr.length; i2++) {
                        if (!arrayList.contains(c0136sArr[i2]) && c0136sArr[i2].d(strSubstring)) {
                            arrayList.add(c0136sArr[i2]);
                        }
                    }
                }
            }
            ArrayList arrayList2 = new ArrayList();
            for (int i3 = 0; i3 < c0136sArr.length; i3++) {
                if (arrayList.contains(c0136sArr[i3])) {
                    arrayList2.add(c0136sArr[i3]);
                }
            }
            C0136s[] c0136sArr2 = new C0136s[arrayList2.size()];
            bH.C.c("Filtered ConfigurationOptionGroup in " + ((System.nanoTime() - jNanoTime) / 1000000) + "ms.");
            return (C0136s[]) arrayList2.toArray(c0136sArr2);
        } catch (V.g e2) {
            String str2 = "Error in [SettingGroups]\nReported Error:\n" + e2.getMessage();
            e2.printStackTrace();
            throw new V.a(str2);
        }
    }

    public ArrayList a(J j2) throws V.g {
        ArrayList arrayList = new ArrayList();
        C0136s c0136s = null;
        Iterator it = j2.b("SettingGroups").iterator();
        while (it.hasNext()) {
            String strB = ((M) it.next()).b();
            try {
                if (strB.startsWith("settingGroup")) {
                    c0136s = new C0136s();
                    String[] strArrC = Q.c(Q.a(strB));
                    c0136s.v(strArrC[0].trim());
                    c0136s.b(strArrC[0].trim());
                    if (strArrC.length > 1) {
                        c0136s.c(bH.W.b(strArrC[1], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    } else {
                        c0136s.c(c0136s.c());
                    }
                    arrayList.add(c0136s);
                } else if (strB.startsWith("settingOption")) {
                    C0135r c0135r = new C0135r();
                    String[] strArrC2 = Q.c(Q.a(strB));
                    String str = strArrC2[0];
                    if (str.equals("") || str.equals("DEFAULT") || (strArrC2.length > 2 && strArrC2[2].indexOf("true") != -1)) {
                        c0135r.a(true);
                        c0135r.v(str);
                        c0135r.b(true);
                    } else {
                        c0135r.v(str);
                    }
                    if (strArrC2.length > 0) {
                        c0135r.a(bH.W.b(strArrC2[1], PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    } else {
                        c0135r.a(c0135r.aJ());
                    }
                    c0136s.a(c0135r);
                }
            } catch (Exception e2) {
                String str2 = "Invalid Ini entry for reference table, Ignored:\n" + strB + "\n";
                bH.C.a(str2);
                e2.printStackTrace();
                throw new V.g(str2);
            }
        }
        return arrayList;
    }
}
