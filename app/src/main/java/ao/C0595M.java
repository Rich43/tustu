package ao;

import bx.AbstractC1375a;
import g.C1733k;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JOptionPane;

/* renamed from: ao.M, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/M.class */
public class C0595M extends AbstractC1375a {

    /* renamed from: a, reason: collision with root package name */
    private static C0595M f5103a = null;

    private C0595M() {
    }

    public static C0595M a() {
        if (f5103a == null) {
            f5103a = new C0595M();
            f5103a.f();
        }
        return f5103a;
    }

    @Override // bx.InterfaceC1376b
    public boolean a(bx.j jVar) throws bx.x {
        boolean zA;
        if (jVar.a().length() > 20) {
            throw new bx.x("Filter Name Exceeds Max Length of 20");
        }
        try {
            zA = C0596N.a(jVar.c());
        } catch (C0657bu e2) {
            throw new bx.x(e2.getMessage());
        } catch (C0745fb e3) {
            zA = true;
        }
        return zA;
    }

    @Override // bx.AbstractC1375a
    public void a(ArrayList arrayList) {
        arrayList.clear();
        String[] strArrA = bH.R.a(h.i.f("DATA_FILTER_"));
        boolean z2 = false;
        if (strArrA == null || strArrA.length == 0) {
            strArrA = h.i.g("DATA_FILTER_");
            z2 = true;
        }
        for (int i2 = 0; i2 < strArrA.length; i2++) {
            String strB = bH.W.b(strArrA[i2], "DATA_FILTER_", "");
            String strSubstring = "";
            String strC = z2 ? h.i.c(strArrA[i2]) : h.i.e("DATA_FILTER_" + strB, "");
            if (strC.contains(";")) {
                strSubstring = strC.substring(strC.indexOf(";") + 1);
                strC = strC.substring(0, strC.indexOf(";"));
            }
            if (strC.equals("")) {
                bH.C.b("Data filter defined with no formula! Name: " + strB);
            }
            bx.j jVar = new bx.j();
            jVar.a(strB);
            jVar.b(strSubstring);
            jVar.c(strC);
            arrayList.add(jVar);
        }
    }

    @Override // bx.AbstractC1375a
    public void b(ArrayList arrayList) {
        g();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            bx.j jVar = (bx.j) arrayList.get(i2);
            String strB = jVar.b();
            if (strB == null || strB.equals("")) {
                strB = " ";
            }
            h.i.c("DATA_FILTER_" + jVar.a(), jVar.c() + ";" + strB);
        }
    }

    private void g() {
        for (String str : h.i.f("DATA_FILTER_")) {
            h.i.d(str);
        }
    }

    @Override // bx.InterfaceC1376b
    public void b() {
        bP bPVarB = C0645bi.a().b();
        String strA = C1733k.a(bPVarB, "Import Data Filters", new String[]{"filters"}, "*.filters");
        if (strA == null || strA.equals("") || JOptionPane.showConfirmDialog(bPVarB, "Warning!!!!\nAny Data Filters of the same name will be overridden\n\nContinue?") != 0) {
            return;
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(strA));
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Unable to open settings file " + strA + "\nSee log for more detail.", bPVarB);
            e2.printStackTrace();
        }
        Iterator<Object> it = properties.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str.startsWith("DATA_FILTER_")) {
                String strB = bH.W.b(str, "DATA_FILTER_", "");
                String strSubstring = "";
                String property = properties.getProperty("DATA_FILTER_" + strB, "");
                if (property.contains(";")) {
                    strSubstring = property.substring(property.indexOf(";") + 1);
                    property = property.substring(0, property.indexOf(";"));
                }
                if (property.equals("")) {
                    bH.C.b("Data filter defined with no formula! Name: " + strB);
                }
                bx.j jVar = new bx.j();
                jVar.a(strB);
                jVar.b(strSubstring);
                jVar.c(property);
                b(jVar);
            }
        }
    }

    @Override // bx.InterfaceC1376b
    public void c() {
        String strA = C1733k.a(C0645bi.a().b(), "Export Filters", new String[]{"filters"}, "*.filters");
        if (strA == null || strA.equals("")) {
            return;
        }
        if (!strA.toLowerCase().endsWith("filters")) {
            strA = strA + ".filters";
        }
        Properties properties = new Properties();
        Iterator<Object> it = h.i.f12258e.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str.startsWith("DATA_FILTER_")) {
                properties.setProperty(str, h.i.f12258e.getProperty(str));
            }
        }
        try {
            properties.store(new FileOutputStream(new File(strA)), "MegaLogViewer DataFilters");
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Unable to save settings to " + strA + "\nSee log for more detail.", C0645bi.a().b());
            e2.printStackTrace();
        }
    }
}
