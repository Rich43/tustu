package ao;

import com.efiAnalytics.ui.InterfaceC1579bq;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/* renamed from: ao.O, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/O.class */
public class C0597O {
    public static InterfaceC1579bq a(InterfaceC1579bq interfaceC1579bq, String str, List list, List list2, Component component) {
        JMenuItem jMenuItem = new JMenuItem("Save " + str + " Fields As");
        jMenuItem.addActionListener(new C0598P(list2, str, component));
        interfaceC1579bq.add(jMenuItem);
        JMenu jMenu = new JMenu("Delete Field group");
        List<String> listA = a();
        C0599Q c0599q = new C0599Q(component);
        for (String str2 : listA) {
            JMenuItem jMenuItemAdd = jMenu.add(str2);
            jMenuItemAdd.addActionListener(c0599q);
            jMenuItemAdd.setName(str2);
        }
        interfaceC1579bq.add(jMenu);
        interfaceC1579bq.addSeparator();
        ActionListener c0600r = new C0600R(list);
        for (String str3 : a()) {
            JMenuItem jMenuItemAdd2 = interfaceC1579bq.add(str3);
            jMenuItemAdd2.addActionListener(c0600r);
            jMenuItemAdd2.setName(str3);
        }
        return interfaceC1579bq;
    }

    public static void a(List list, String str) {
        List listA = a(str);
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (listA.size() > i2) {
                ((JComboBox) list.get(i2)).setSelectedItem(listA.get(i2));
            } else {
                ((JComboBox) list.get(i2)).setSelectedItem(" ");
            }
        }
    }

    public static void a(String str, List list) {
        String strSubstring = "";
        Iterator it = list.iterator();
        while (it.hasNext()) {
            strSubstring = strSubstring + ((String) it.next()) + ",";
        }
        if (strSubstring.endsWith(",")) {
            strSubstring = strSubstring.substring(0, strSubstring.length() - 1);
        }
        h.i.c("GRAPH_FIELD_GROUP_NAME_" + str, strSubstring);
    }

    public static List a() {
        ArrayList arrayList = new ArrayList();
        for (String str : h.i.e("GRAPH_FIELD_GROUP_NAME_")) {
            arrayList.add(bH.W.b(str, "GRAPH_FIELD_GROUP_NAME_", ""));
        }
        return arrayList;
    }

    public static List a(String str) {
        String strF = h.i.f("GRAPH_FIELD_GROUP_NAME_" + str, "");
        ArrayList arrayList = new ArrayList();
        for (String str2 : strF.split(",")) {
            arrayList.add(str2);
        }
        return arrayList;
    }
}
