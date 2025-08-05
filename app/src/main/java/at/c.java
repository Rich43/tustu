package at;

import ao.C0636b;
import ao.C0645bi;
import ao.C0653bq;
import ao.C0656bt;
import ar.C0839f;
import bH.C;
import bH.C1011s;
import com.efiAnalytics.ui.bV;
import g.C1729g;
import g.C1733k;
import h.C1737b;
import h.i;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/* loaded from: TunerStudioMS.jar:at/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private static c f6267a = null;

    /* renamed from: b, reason: collision with root package name */
    private JMenu f6268b = null;

    /* renamed from: c, reason: collision with root package name */
    private ButtonGroup f6269c = null;

    /* renamed from: d, reason: collision with root package name */
    private Map f6270d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    private String[] f6271e = {"graph0", "graph1", "graph2", "graph3", "graph4", "graph5", "graph6", "graph7", "graph8", "graph9"};

    private c() {
    }

    public static c a() {
        if (f6267a == null) {
            f6267a = new c();
        }
        return f6267a;
    }

    public void a(String str, File file) {
        try {
            new C0858a();
            C0858a.a(str, file);
            for (int i2 = 0; i2 < this.f6268b.getMenuComponentCount(); i2++) {
                JMenuItem item = this.f6268b.getItem(i2);
                if ((item instanceof JCheckBoxMenuItem) && item.getText().equals(str)) {
                    item.setSelected(true);
                    return;
                }
            }
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(str);
            jCheckBoxMenuItem.setActionCommand(str);
            jCheckBoxMenuItem.addActionListener(new d(this));
            if (this.f6269c != null) {
                this.f6269c.add(jCheckBoxMenuItem);
            }
            this.f6268b.add((JMenuItem) jCheckBoxMenuItem);
        } catch (V.a e2) {
            Logger.getLogger(c.class.getName()).log(Level.INFO, "Error saving Settings File.", (Throwable) e2);
            bV.d(e2.getLocalizedMessage(), this.f6268b);
        }
    }

    public void b() {
        i.c("userParameter_New Setting Profile Name", d());
        C1729g c1729g = null;
        do {
            if (c1729g != null) {
                bV.d("Invalid Profile Name: '" + c1729g.a() + "'. Please remove any special characters.", C0645bi.a().b());
            }
            c1729g = new C1729g(C0645bi.a().b(), "{New Setting Profile Name}", false, "       Save current setting components as", true);
            if (!c1729g.f12195a) {
                break;
            }
        } while (!C1011s.a(c1729g.a()));
        if (c1729g.f12195a) {
            String strA = c1729g.a();
            a(strA);
            for (int i2 = 0; i2 < this.f6268b.getMenuComponentCount(); i2++) {
                JMenuItem item = this.f6268b.getItem(i2);
                if ((item instanceof JCheckBoxMenuItem) && item.getText().equals(strA)) {
                    item.setSelected(true);
                    return;
                }
            }
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(strA);
            jCheckBoxMenuItem.setActionCommand(strA);
            jCheckBoxMenuItem.addActionListener(new e(this));
            if (this.f6269c != null) {
                this.f6269c.add(jCheckBoxMenuItem);
            }
            jCheckBoxMenuItem.setSelected(true);
            this.f6268b.add((JMenuItem) jCheckBoxMenuItem);
        }
    }

    public void a(String str) {
        ArrayList arrayList = new ArrayList();
        C0656bt c0656bt = new C0656bt("USER_FIELD_", "User Calculated Math Fields");
        c0656bt.a("Your custom created Calculated Fields");
        arrayList.add(c0656bt);
        C0656bt c0656bt2 = new C0656bt("ROOT_FIELD_", "Normalized Field Name Mapping");
        c0656bt2.a("Export Field Name Mappings that are currently active.");
        arrayList.add(c0656bt2);
        C0656bt c0656bt3 = new C0656bt("APPEND_FIELD_", "Active Optional and Calculated Fields");
        c0656bt3.a("Exports which Optional and Calculated Fields are active.");
        arrayList.add(c0656bt3);
        C0656bt c0656bt4 = new C0656bt("FIELD_GROUP_NAME_", "Quick View Tabs");
        c0656bt4.a("All Quick View tabs on this PC");
        c0656bt4.b("FIELD_SELECTED_GROUP_");
        c0656bt4.b("graph0");
        c0656bt4.b("graph1");
        c0656bt4.b("graph2");
        c0656bt4.b("graph3");
        c0656bt4.b("graph4");
        c0656bt4.b("graph5");
        c0656bt4.b("graph6");
        c0656bt4.b("graph7");
        c0656bt4.b("graph8");
        c0656bt4.b("graph9");
        arrayList.add(c0656bt4);
        C0656bt c0656bt5 = new C0656bt("FIELD_MIN_MAX_", "Field Min/Max Settings");
        c0656bt5.a("Export set Min & Max values and autoscale settings.");
        arrayList.add(c0656bt5);
        C0656bt c0656bt6 = new C0656bt(i.f12298S, "Viewing preferences");
        c0656bt6.b("numberOfGraphs");
        c0656bt6.b("numberOfOverlays");
        c0656bt6.b("numberOfOverlayGraphs");
        c0656bt6.a("Export number of graphs, traces per graph, Trace Value Position, etc.");
        arrayList.add(c0656bt6);
        if (C1737b.a().a("tableGenerator")) {
            C0656bt c0656bt7 = new C0656bt("TABLE_GEN_VIEW_", "Histogram Views");
            c0656bt7.a("Export Histogram / Table Generator views.");
            arrayList.add(c0656bt7);
        }
        if (C1737b.a().a("scatterPlots")) {
            C0656bt c0656bt8 = new C0656bt("SCATTER_PLOT_VIEW_", "Scatter Plot Views");
            c0656bt8.a("Export Scatter Plot views.");
            arrayList.add(c0656bt8);
        }
        if (C1737b.a().a("fieldSmoothing")) {
            C0656bt c0656bt9 = new C0656bt("fieldSmoothingFactor_", "Field Smoothing");
            c0656bt9.a("Export Smoothing factor for each field it has been set on.");
            arrayList.add(c0656bt9);
        }
        if (C1737b.a().a("scatterPlots")) {
            C0656bt c0656bt10 = new C0656bt("DATA_FILTER_", "Data Filters");
            c0656bt10.a("Export data filters defined for Histograms and Scatter Plots.");
            arrayList.add(c0656bt10);
        }
        C0656bt c0656bt11 = new C0656bt("lastFileDir", "File Folders");
        c0656bt11.a("Saves you las used file dirs to the profile.");
        c0656bt11.b(i.f12282C);
        c0656bt11.b(i.f12283D);
        arrayList.add(c0656bt11);
        C0653bq c0653bq = new C0653bq(arrayList, true);
        c0653bq.a(C0645bi.a().b());
        List listB = c0653bq.b();
        if (listB.isEmpty()) {
            return;
        }
        a(str, listB);
    }

    public void c() {
        String strD = d();
        if (strD.equals("")) {
            return;
        }
        List list = (List) this.f6270d.get(strD);
        if (list == null) {
            C.b("Prefixes for Setting Profile " + strD + " not set!");
        } else if (list.isEmpty()) {
            C.b("Prefixes for Setting Profile " + strD + " are empty!");
        } else {
            a(strD, list);
        }
    }

    public void a(String str, List list) {
        C0645bi.a().e().i();
        File fileA = C0858a.a(str);
        Properties properties = new Properties();
        Iterator<Object> it = i.f12258e.keySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (b(str2, list)) {
                properties.setProperty(str2, i.f12258e.getProperty(str2));
            }
        }
        this.f6270d.put(str, list);
        try {
            if (!fileA.exists() && !fileA.createNewFile()) {
                C1733k.a("Unable to write to file\n" + fileA.getAbsolutePath() + "\nFailed to save setting profile.", C0645bi.a().b());
            }
            FileOutputStream fileOutputStream = new FileOutputStream(fileA);
            properties.store(fileOutputStream, "MegaLogViewer Settings");
            fileOutputStream.close();
        } catch (Exception e2) {
            C1733k.a("Unable to save settings to " + fileA.getAbsolutePath() + "\nSee log for more detail.", C0645bi.a().b());
            e2.printStackTrace();
        }
    }

    private boolean d(String str) {
        for (String str2 : this.f6271e) {
            if (str.startsWith(str2)) {
                return true;
            }
        }
        return false;
    }

    private boolean b(String str, List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (str.startsWith((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean b(String str) {
        Component[] components = this.f6268b.getComponents();
        int length = components.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            Component component = components[i2];
            if (component instanceof JMenuItem) {
                JMenuItem jMenuItem = (JMenuItem) component;
                if (jMenuItem.getText().equals(str)) {
                    this.f6268b.remove(jMenuItem);
                    break;
                }
            }
            i2++;
        }
        boolean zB = C0858a.b(str);
        if (d().equals(str)) {
            i.c("SettingProfile", "");
        }
        return zB;
    }

    public String d() {
        return i.e("SettingProfile", "");
    }

    public void c(String str) {
        c();
        if (a(str, true)) {
            i.c("SettingProfile", str);
            C1733k.a("The Application will now restart for changes to take effect.", C0645bi.a().b());
            C0839f.a().a(true);
            C0636b.a().b((Frame) C0645bi.a().b());
        }
    }

    public boolean a(String str, boolean z2) {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(C0858a.a(str));
            properties.load(fileInputStream);
            fileInputStream.close();
            ArrayList arrayList = new ArrayList();
            if (a(properties, "USER_FIELD_")) {
                arrayList.add("USER_FIELD_");
                C.d("Load profile " + str + ", loading User Fields");
            }
            if (a(properties, "ROOT_FIELD_")) {
                arrayList.add("ROOT_FIELD_");
                C.d("Load profile " + str + ", loading Normalized Name Mappings");
            }
            if (a(properties, "APPEND_FIELD_")) {
                arrayList.add("APPEND_FIELD_");
                C.d("Load profile " + str + ", loading Select Fields");
            }
            if (a(properties, "FIELD_GROUP_NAME_")) {
                arrayList.add("FIELD_GROUP_NAME_");
                arrayList.add("FIELD_SELECTED_GROUP_");
                arrayList.add("graph0");
                arrayList.add("graph1");
                arrayList.add("graph2");
                arrayList.add("graph3");
                arrayList.add("graph4");
                arrayList.add("graph5");
                arrayList.add("graph6");
                arrayList.add("graph7");
                arrayList.add("graph8");
                arrayList.add("graph9");
                C.d("Load profile " + str + ", loading Group Fields");
            }
            if (a(properties, "FIELD_MIN_MAX_")) {
                arrayList.add("FIELD_MIN_MAX_");
                C.d("Load profile " + str + ", loading Mins/Maxes");
            }
            if (C1737b.a().a("tableGenerator") && a(properties, "TABLE_GEN_VIEW_")) {
                arrayList.add("TABLE_GEN_VIEW_");
                C.d("Load profile " + str + ", loading histogram Views");
            }
            if (C1737b.a().a("scatterPlots") && a(properties, "SCATTER_PLOT_VIEW_")) {
                arrayList.add("SCATTER_PLOT_VIEW_");
                C.d("Load profile " + str + ", loading Scatter Plot Views");
            }
            if (C1737b.a().a("fieldSmoothing") && a(properties, "fieldSmoothingFactor_")) {
                arrayList.add("fieldSmoothingFactor_");
                C.d("Load profile " + str + ", loading Smoothing Factors");
            }
            if (a(properties, i.f12298S)) {
                arrayList.add(i.f12298S);
                arrayList.add("numberOfGraphs");
                arrayList.add("numberOfOverlays");
                arrayList.add("numberOfOverlayGraphs");
                C.d("Load profile " + str + ", loading User Preferences");
            }
            if (a(properties, "lastFileDir")) {
                arrayList.add("lastFileDir");
                arrayList.add(i.f12282C);
                arrayList.add(i.f12283D);
                C.d("Load profile " + str + ", loading User Folders");
            }
            if (a(properties, "DATA_FILTER_")) {
                arrayList.add("DATA_FILTER_");
                C.d("Load profile " + str + ", loading Data Filters");
            }
            if (arrayList.isEmpty()) {
                bV.d("There are no settings in this file to import.", C0645bi.a().b());
                return false;
            }
            this.f6270d.put(str, arrayList);
            for (String str2 : i.f12258e.stringPropertyNames()) {
                if (b(str2, arrayList)) {
                    if (d(str2)) {
                        i.c(str2, " ");
                    } else {
                        i.d(str2);
                    }
                }
            }
            Iterator<Object> it = properties.keySet().iterator();
            while (it.hasNext()) {
                String str3 = (String) it.next();
                if (b(str3, arrayList)) {
                    i.f12258e.setProperty(str3, properties.getProperty(str3));
                }
            }
            return true;
        } catch (Exception e2) {
            C1733k.a("Unable to load profile settings " + str + "\nerror loading store file.\nSee log for more detail.", C0645bi.a().b());
            e2.printStackTrace();
            return false;
        }
    }

    private boolean a(Properties properties, String str) {
        Iterator<String> it = properties.stringPropertyNames().iterator();
        while (it.hasNext()) {
            if (it.next().startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    public void a(JMenu jMenu) {
        this.f6268b = jMenu;
    }

    public void a(ButtonGroup buttonGroup) {
        this.f6269c = buttonGroup;
    }
}
