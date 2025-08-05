package ao;

import W.C0184j;
import W.C0188n;
import com.efiAnalytics.ui.C1599cj;
import com.efiAnalytics.ui.InterfaceC1579bq;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/* renamed from: ao.S, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/S.class */
public class C0601S {
    public static InterfaceC1579bq a(InterfaceC1579bq interfaceC1579bq, C0184j c0184j, Component component) {
        int iB = h.i.b("fieldSmoothingFactor_" + c0184j.a(), 0);
        JMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Field Smoothing (" + c0184j.s() + ")", iB > 0);
        jCheckBoxMenuItem.addActionListener(new C0602T(c0184j));
        interfaceC1579bq.add(jCheckBoxMenuItem);
        JMenuItem jMenuItem = new JMenuItem("Smoothing Factor");
        jMenuItem.setEnabled(iB > 0);
        jMenuItem.addActionListener(new C0605W(c0184j, component));
        interfaceC1579bq.add(jMenuItem);
        String strE = h.i.e("FIELD_MIN_MAX_" + c0184j.a(), "");
        String strA = c0184j.a();
        if (strE == null || strE.indexOf(";") == -1) {
            interfaceC1579bq.add(b(strA, (String) null, (String) null));
        } else {
            interfaceC1579bq.add(b(strA, strE.substring(0, strE.indexOf(";")), strE.substring(strE.indexOf(";") + 1, strE.length())));
        }
        if (c0184j.l() && !b(c0184j.a())) {
            interfaceC1579bq.addSeparator();
            String strJ = c0184j.j();
            if (strJ.length() > 100) {
                strJ = strJ.substring(0, 90) + " ...";
            }
            JMenuItem jMenuItem2 = new JMenuItem("Edit Custom Formula: " + strJ);
            jMenuItem2.setActionCommand(c0184j.j());
            jMenuItem2.setName(c0184j.a());
            jMenuItem2.addActionListener(new C0606X());
            interfaceC1579bq.add(jMenuItem2);
        } else if (c0184j.l()) {
            interfaceC1579bq.addSeparator();
            String strJ2 = c0184j.j();
            if (strJ2.length() > 100) {
                strJ2 = strJ2.substring(0, 90) + " ...";
            }
            JMenuItem jMenuItem3 = new JMenuItem("Copy Optional Formula: " + strJ2);
            jMenuItem3.setActionCommand(c0184j.j());
            jMenuItem3.setName(c0184j.a());
            jMenuItem3.addActionListener(new C0607Y());
            interfaceC1579bq.add(jMenuItem3);
        }
        JMenu jMenu = new JMenu("Index offsets");
        C0590H c0590h = new C0590H();
        c0590h.a(c0184j.A());
        c0590h.a(new C0610aa(c0184j));
        jMenu.add(c0590h);
        jMenu.add("Reset Index Offset").addActionListener(new C0611ab(c0184j));
        interfaceC1579bq.add(jMenu);
        return interfaceC1579bq;
    }

    public static InterfaceC1579bq b(InterfaceC1579bq interfaceC1579bq, C0184j c0184j, Component component) {
        JMenuItem jMenuItem = new JMenuItem("Shift Cursor Position to 0");
        jMenuItem.addActionListener(new C0612ac());
        interfaceC1579bq.add(jMenuItem);
        interfaceC1579bq.addSeparator();
        C0613ad c0613ad = new C0613ad();
        ArrayList<String> arrayList = new ArrayList();
        Iterator it = C0804hg.a().r().iterator();
        while (it.hasNext()) {
            C0184j c0184j2 = (C0184j) it.next();
            if (c0184j2.a().equals("Time") || (c0184j2.z() && c0184j2.g() > c0184j2.h() && c0184j2.p() == 0)) {
                arrayList.add(c0184j2.a());
            }
        }
        Collections.sort(arrayList, new C0614ae());
        for (String str : arrayList) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(str.equals("Time") ? "<html><b>" + str + " (Default)</b>" : str, str.equals(c0184j.a()));
            jCheckBoxMenuItem.setActionCommand(str);
            jCheckBoxMenuItem.addActionListener(c0613ad);
            interfaceC1579bq.add(jCheckBoxMenuItem);
        }
        return interfaceC1579bq;
    }

    private static boolean b(String str) {
        return h.i.c(new StringBuilder().append("APPEND_FIELD_").append(str).toString()) != null;
    }

    private static JMenu b(String str, String str2, String str3) {
        JMenu jMenu;
        boolean z2 = false;
        if (str2 == null || str2.isEmpty() || str3 == null || str3.isEmpty()) {
            jMenu = new JMenu(str + "( Min: Auto - Max: Auto )");
            z2 = true;
        } else {
            jMenu = new JMenu(str + " (Min: " + str2 + " Max: " + str3 + ")");
        }
        jMenu.setName(str);
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Autoscale " + str);
        jCheckBoxMenuItem.setSelected(z2);
        jCheckBoxMenuItem.setName(str);
        jCheckBoxMenuItem.addActionListener(new C0603U());
        jMenu.add((JMenuItem) jCheckBoxMenuItem);
        if (!z2) {
            JMenuItem jMenuItem = new JMenuItem("Edit " + str + " Properties");
            jMenuItem.setName(str);
            jMenuItem.addActionListener(new C0604V());
            jMenu.add(jMenuItem);
        }
        return jMenu;
    }

    public static void a(String str, String str2, String str3) {
        C1599cj c1599cj = new C1599cj(C0645bi.a().b(), C0804hg.a().r(), str, str2, str3);
        c1599cj.requestFocus();
        c1599cj.dispose();
        if (c1599cj.f11259a) {
            String strA = c1599cj.a();
            String strB = c1599cj.b();
            String strC = c1599cj.c();
            b(strA, strB, strC);
            a(strA, Float.parseFloat(strB), Float.parseFloat(strC));
        }
    }

    private static void a(String str, float f2, float f3) {
        C0184j c0184jA;
        if (Float.isNaN(f2) || Float.isNaN(f3)) {
            h.i.d("FIELD_MIN_MAX_" + str);
        } else {
            h.i.c("FIELD_MIN_MAX_" + str, f2 + ";" + f3);
        }
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR == null || (c0184jA = c0188nR.a(str)) == null) {
            return;
        }
        c0184jA.g(f2);
        c0184jA.f(f3);
        a();
    }

    private static void a() {
        C0645bi.a().c().i();
        C0645bi.a().c().repaint();
    }

    public static void a(String str) {
        a(str, Float.NaN, Float.NaN);
        a();
    }
}
