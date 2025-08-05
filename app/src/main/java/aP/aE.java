package aP;

import G.C0135r;
import G.C0136s;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/aE.class */
public class aE extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    C0136s[] f2803a = null;

    /* renamed from: b, reason: collision with root package name */
    List f2804b = new ArrayList();

    public aE() {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Configuration Settings")));
    }

    public void a() {
        removeAll();
        repaint();
    }

    public void a(C0136s[] c0136sArr) {
        removeAll();
        this.f2803a = c0136sArr;
        setLayout(new GridLayout(0, 1));
        if (c0136sArr.length <= 0) {
            add(new JLabel("No settings of this type."));
            return;
        }
        for (int i2 = 0; i2 < c0136sArr.length; i2++) {
            JComboBox jComboBox = new JComboBox();
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new GridLayout(1, 1));
            jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b(c0136sArr[i2].d())));
            jComboBox.setName(c0136sArr[i2].c());
            jComboBox.setEditable(false);
            ArrayList arrayList = new ArrayList();
            Iterator itA = c0136sArr[i2].a();
            while (itA.hasNext()) {
                arrayList.add(itA.next());
            }
            Iterator it = bH.R.a(arrayList).iterator();
            while (it.hasNext()) {
                jComboBox.addItem(it.next());
            }
            jComboBox.setSelectedItem(c0136sArr[i2].b());
            jPanel.add(jComboBox);
            add(jPanel);
            this.f2804b.add(jComboBox);
        }
    }

    public C0135r[] b() {
        C0135r[] c0135rArr = new C0135r[this.f2804b.size()];
        for (int i2 = 0; i2 < this.f2804b.size(); i2++) {
            c0135rArr[i2] = (C0135r) ((JComboBox) this.f2804b.get(i2)).getSelectedItem();
        }
        return c0135rArr;
    }

    public C0136s[] c() {
        return this.f2803a;
    }

    public String[] d() {
        String[] strArr = new String[this.f2804b.size()];
        for (int i2 = 0; i2 < this.f2804b.size(); i2++) {
            Object selectedItem = ((JComboBox) this.f2804b.get(i2)).getSelectedItem();
            if (selectedItem instanceof C0135r) {
                strArr[i2] = ((C0135r) selectedItem).aJ();
            } else {
                strArr[i2] = (String) selectedItem;
            }
        }
        return strArr;
    }

    public void a(C0135r c0135r) {
        for (int i2 = 0; i2 < this.f2804b.size(); i2++) {
            JComboBox jComboBox = (JComboBox) this.f2804b.get(i2);
            for (int i3 = 0; i3 < jComboBox.getItemCount(); i3++) {
                if (c0135r.equals(jComboBox.getItemAt(i3))) {
                    bH.C.c("Setting: " + jComboBox.getName() + " to " + jComboBox.getItemAt(i3));
                    jComboBox.setSelectedIndex(i3);
                }
            }
        }
    }

    public void a(String str) {
        for (int i2 = 0; i2 < this.f2804b.size(); i2++) {
            JComboBox jComboBox = (JComboBox) this.f2804b.get(i2);
            for (int i3 = 0; i3 < jComboBox.getItemCount(); i3++) {
                Object itemAt = jComboBox.getItemAt(i3);
                if (itemAt.equals(str)) {
                    jComboBox.setSelectedItem(itemAt);
                }
            }
        }
    }

    public boolean e() {
        return true;
    }
}
