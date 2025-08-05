package ao;

import com.efiAnalytics.ui.C1621de;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: ao.bq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bq.class */
public class C0653bq extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    List f5431a;

    /* renamed from: b, reason: collision with root package name */
    final List f5432b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    JDialog f5433c = null;

    /* renamed from: d, reason: collision with root package name */
    C1621de f5434d = new C1621de();

    public C0653bq(List list, boolean z2) {
        this.f5431a = list;
        setLayout(new BorderLayout());
        if (z2) {
            add("North", new JLabel("Select Settings to export", 0));
        } else {
            add("North", new JLabel("Select Settings available to import", 0));
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 2, com.efiAnalytics.ui.eJ.a(5), 0));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            C0656bt c0656bt = (C0656bt) it.next();
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new BorderLayout(1, 1));
            JCheckBox jCheckBox = new JCheckBox(c0656bt.b(), true);
            jPanel2.add(BorderLayout.CENTER, jCheckBox);
            this.f5432b.add(jCheckBox);
            jCheckBox.setActionCommand(c0656bt.a());
            if (!c0656bt.c().isEmpty()) {
                jPanel2.add("East", new com.efiAnalytics.ui.cF(c0656bt.c(), null));
            }
            jPanel.add(jPanel2);
        }
        if (!z2) {
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new BorderLayout());
            jPanel3.add(BorderLayout.CENTER, this.f5434d);
            this.f5434d.a("Replace Current Settings");
            this.f5434d.a("Merge with Current Settings");
            this.f5434d.b("Merge with Current Settings");
            jPanel3.add("East", new com.efiAnalytics.ui.cF("Replace Current Settings: Settings for each category will be removed and replaced with those in this import file.<br>Merge with Current Settings: Current settings will be left in place and only overwridden when a setting with the same name is imported.", null));
            if (jPanel.getComponentCount() % 2 > 0) {
                jPanel.add(new JLabel(""));
            }
            jPanel.add(jPanel3);
        }
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(1));
        JButton jButton = new JButton("OK");
        jButton.addActionListener(new C0654br(this));
        JButton jButton2 = new JButton("Cancel");
        jButton2.addActionListener(new C0655bs(this));
        if (bH.I.a()) {
            jPanel4.add(jButton);
            jPanel4.add(jButton2);
        } else {
            jPanel4.add(jButton2);
            jPanel4.add(jButton);
        }
        add("South", jPanel4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (this.f5433c != null) {
            this.f5433c.dispose();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        this.f5431a.clear();
        if (this.f5433c != null) {
            this.f5433c.dispose();
        }
    }

    public boolean a() {
        return this.f5434d.getSelectedItem() != null && this.f5434d.getSelectedItem().equals("Replace Current Settings");
    }

    public List b() {
        ArrayList arrayList = new ArrayList();
        for (C0656bt c0656bt : this.f5431a) {
            if (a(c0656bt.a())) {
                arrayList.add(c0656bt.a());
                Iterator it = c0656bt.d().iterator();
                while (it.hasNext()) {
                    arrayList.add((String) it.next());
                }
            }
        }
        return arrayList;
    }

    private boolean a(String str) {
        for (JCheckBox jCheckBox : this.f5432b) {
            if (jCheckBox.getActionCommand().equals(str)) {
                return jCheckBox.isSelected();
            }
        }
        return false;
    }

    public void a(Window window) {
        this.f5433c = new JDialog(window, "Setting Selector");
        this.f5433c.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.f5433c.add(BorderLayout.CENTER, this);
        this.f5433c.pack();
        com.efiAnalytics.ui.bV.a(window, (Component) this.f5433c);
        this.f5433c.setVisible(true);
    }
}
