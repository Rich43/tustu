package aP;

import G.C0050aj;
import G.C0083bp;
import G.C0088bu;
import ai.C0512b;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import r.C1798a;
import r.C1806i;
import s.C1818g;
import x.C1891a;

/* renamed from: aP.bi, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bi.class */
public class C0240bi extends JMenuBar {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f3075a;

    /* renamed from: b, reason: collision with root package name */
    C0088bu f3076b;

    /* renamed from: c, reason: collision with root package name */
    G.R f3077c;

    public C0240bi() {
        this.f3075a = null;
        this.f3076b = null;
        this.f3077c = null;
    }

    public C0240bi(G.R r2, C0088bu c0088bu, ArrayList arrayList) {
        this.f3075a = null;
        this.f3076b = null;
        this.f3077c = null;
        this.f3077c = r2;
        a(r2, c0088bu, arrayList);
    }

    public void a(G.R r2, C0088bu c0088bu, ArrayList arrayList) {
        this.f3075a = arrayList;
        this.f3076b = c0088bu;
        List listE = c0088bu.e();
        List listU = c0088bu.U();
        if (listE.size() > 0 && C1806i.a().a("hlk;rd;tporg;'gd")) {
            C1891a c1891aC = c(C1818g.b("File"));
            c1891aC.setMnemonic('F');
            a(c1891aC);
        }
        if (listE.size() > 0) {
            C1891a c1891aC2 = c(C1818g.b("View"));
            c1891aC2.setMnemonic('V');
            JMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(C1818g.b("Show Lower Help Pane"));
            jCheckBoxMenuItem.setSelected(C1798a.a().c(C1798a.ci, C1798a.cj));
            c1891aC2.add(jCheckBoxMenuItem);
            jCheckBoxMenuItem.addActionListener(new C0241bj(this));
        }
        if (!listU.isEmpty() || (c0088bu.aa() != null && !c0088bu.aa().isEmpty())) {
            C1891a c1891aC3 = c(C1818g.b("Tools"));
            c1891aC3.setMnemonic('T');
            boolean z2 = false;
            Iterator it = listU.iterator();
            while (it.hasNext()) {
                a(c1891aC3, (G.bH) it.next());
                z2 = true;
            }
            String strAa = c0088bu.aa();
            if (strAa != null && !strAa.isEmpty() && C1806i.a().a("fv-7rkf74nfd67whn5iuchqj")) {
                if (z2) {
                    c1891aC3.addSeparator();
                }
                boolean z3 = false;
                try {
                    z3 = r2.c(c0088bu.aa()).j(r2.h()) != 0.0d;
                } catch (Exception e2) {
                    bH.C.c("Faied to get Password value.");
                }
                JMenuItem eVar = new bA.e(C1818g.b(z3 ? "Change Dialog Password" : "Password Protect Dialog"), true);
                eVar.addActionListener(new C0243bl(this, c1891aC3, r2, c0088bu));
                c1891aC3.add(eVar);
                if (z3) {
                    bA.e eVar2 = new bA.e(C1818g.b("Clear Dialog Password."), true);
                    eVar2.addActionListener(new C0244bm(this, r2, c0088bu, c1891aC3, eVar2));
                    eVar2.a(new C0245bn(this, c0088bu));
                    c1891aC3.add((JMenuItem) eVar2);
                }
            }
        }
        ArrayList arrayListO = c0088bu.O();
        if (c0088bu.J() > 0 || arrayListO.size() > 0 || arrayList.size() > 0) {
            if (c0088bu.J() > 0) {
                Iterator itI = c0088bu.I();
                while (itI.hasNext()) {
                    G.aA aAVar = (G.aA) itI.next();
                    a(c(C1818g.b(aAVar.e())), aAVar);
                }
            }
            if (arrayListO.size() > 0 || arrayList.size() > 0) {
                JMenu c1891a = new C1891a(C1818g.b("Help"));
                c1891a.setMnemonic('H');
                add(c1891a);
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    C0512b c0512b = (C0512b) it2.next();
                    JMenuItem jMenuItem = new JMenuItem(c0512b.a());
                    jMenuItem.setActionCommand(c0512b.b());
                    jMenuItem.addActionListener(new C0246bo(this));
                    c1891a.add(jMenuItem);
                }
                if (c1891a.getItemCount() > 0) {
                    c1891a.addSeparator();
                }
                Iterator it3 = arrayListO.iterator();
                while (it3.hasNext()) {
                    String str = (String) it3.next();
                    C0050aj c0050ajB = r2.e().b(str);
                    if (c0050ajB != null) {
                        JMenuItem jMenuItem2 = new JMenuItem(C1818g.b(c0050ajB.d()));
                        jMenuItem2.setActionCommand(c0050ajB.a());
                        jMenuItem2.addActionListener(new C0247bp(this));
                        c1891a.add(jMenuItem2);
                    } else {
                        bH.C.b("helpTopic " + str + " is set for dialog, but not found in ini.");
                    }
                }
            }
        }
    }

    private void a(C1891a c1891a) {
        bA.e eVar = new bA.e(C1818g.b("Save dialog settings"), true);
        eVar.setMnemonic('S');
        eVar.setActionCommand("savePartial");
        eVar.setName("savePartial");
        eVar.addActionListener(new C0248bq(this));
        c1891a.add((JMenuItem) eVar);
        bA.e eVar2 = new bA.e(C1818g.b("Load dialog settings"), true);
        eVar2.setMnemonic('L');
        eVar2.setActionCommand("loadPartial");
        eVar2.setName("loadPartial");
        eVar2.addActionListener(new C0249br(this));
        c1891a.add((JMenuItem) eVar2);
    }

    public void a(C1891a c1891a, G.aA aAVar) {
        Iterator itA = aAVar.a();
        while (itA.hasNext()) {
            G.aA aAVar2 = (G.aA) itA.next();
            if (aAVar2.b()) {
                C1891a c1891a2 = new C1891a(C1818g.b(aAVar2.e()));
                c1891a2.b(c1891a2.d());
                a(c1891a2, aAVar2);
                c1891a.add((JMenuItem) c1891a2);
            } else if (aAVar2.c()) {
                c1891a.addSeparator();
            } else {
                bA.e eVar = new bA.e(C1818g.b(aAVar2.e()), true);
                eVar.setActionCommand(aAVar2.d());
                eVar.setName(aAVar2.f() + "");
                eVar.addActionListener(new C0250bs(this));
                c1891a.add((JMenuItem) eVar);
            }
        }
    }

    public void a(C1891a c1891a, G.bH bHVar) {
        bA.e eVar;
        String strB = C1818g.b(bHVar.b());
        if (C1806i.a().a(".ewqlfdds/e;ewropglk")) {
            eVar = new bA.e(strB, true);
            eVar.setActionCommand(bHVar.a());
            eVar.setName(bHVar.aJ());
            eVar.addActionListener(new C0252bu(this, this.f3077c, bHVar));
            if (bHVar.aH() != null) {
                eVar.a(new C0251bt(this, bHVar.aH(), this.f3077c));
            }
        } else {
            eVar = new bA.e("Enable " + strB, true);
            eVar.addActionListener(new C0242bk(this));
        }
        if (C1806i.a().a("-0o4togd;'fdshlew")) {
            return;
        }
        c1891a.add((JMenuItem) eVar);
    }

    private C1891a c(String str) {
        for (int i2 = 0; i2 < getMenuCount(); i2++) {
            C1891a c1891a = (C1891a) getMenu(i2);
            if (c1891a != null && c1891a.getText().equals(str)) {
                return c1891a;
            }
        }
        C1891a c1891a2 = new C1891a(str);
        add((JMenu) c1891a2);
        return c1891a2;
    }

    public void a(String str) {
        C0338f.a().a(this.f3077c, str, com.efiAnalytics.ui.bV.a(this));
    }

    public void b(String str) {
        if (this.f3075a == null) {
            bH.C.c("helpRefs is null, can not show app help");
            return;
        }
        C0512b c0512b = null;
        Iterator it = this.f3075a.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            C0512b c0512b2 = (C0512b) it.next();
            if (c0512b2.b().equals(str)) {
                c0512b = c0512b2;
                break;
            }
        }
        if (c0512b == null) {
            c0512b = new C0512b("Application Help", str);
        }
        C0338f.a().a(c0512b, com.efiAnalytics.ui.bV.a(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        C0338f.a().a(this.f3077c, this.f3076b.aJ() != null ? this.f3076b.aJ() : "", G.bL.c(this.f3077c, this.f3076b));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() throws HeadlessException {
        List listE = this.f3076b.e();
        List<String> listC = G.bL.c(this.f3077c, this.f3076b);
        if (listC.size() > listE.size()) {
            String str = "The settings within this dialog have dependencies on additional settings not in thie dialog.\nTo insure the settings in this dialog are loaded correctly\nthe following additional settings should be loaded also:\n";
            for (String str2 : listC) {
                if (!listE.contains(str2)) {
                    C0083bp c0083bpD = G.bL.d(this.f3077c, str2);
                    str = str + (c0083bpD == null ? str2 : c0083bpD.l()) + "\n";
                }
            }
            if (com.efiAnalytics.ui.bV.a(str, "Load Dialog Settings", com.efiAnalytics.ui.bV.b(this), new String[]{"Include Additional Dependencies", "Load Only Dialog Settings"})) {
                listE = listC;
            }
        }
        C0338f.a().a(cZ.a().c(), this.f3077c, listE);
    }
}
