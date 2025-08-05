package p;

import bH.aa;
import com.efiAnalytics.ui.C1621de;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cF;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* renamed from: p.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/c.class */
public class C1777c extends JPanel {

    /* renamed from: f, reason: collision with root package name */
    private d.i f13186f = null;

    /* renamed from: a, reason: collision with root package name */
    JLabel f13187a = new JLabel("", 4);

    /* renamed from: b, reason: collision with root package name */
    JTextField f13188b = new JTextField();

    /* renamed from: c, reason: collision with root package name */
    C1621de f13189c = new C1621de();

    /* renamed from: d, reason: collision with root package name */
    cF f13190d;

    /* renamed from: e, reason: collision with root package name */
    aa f13191e;

    public C1777c(d.i iVar, aa aaVar) throws IllegalArgumentException {
        this.f13191e = aaVar;
        setLayout(new BorderLayout(eJ.a(1), eJ.a(5)));
        add("West", this.f13187a);
        Dimension preferredSize = this.f13187a.getPreferredSize();
        preferredSize.width = eJ.a(170);
        this.f13187a.setPreferredSize(preferredSize);
        if (iVar.a() == 0) {
            add(BorderLayout.CENTER, this.f13189c);
        } else {
            add(BorderLayout.CENTER, this.f13188b);
        }
        setBorder(BorderFactory.createEmptyBorder(eJ.a(3), eJ.a(2), eJ.a(0), eJ.a(2)));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0, eJ.a(2), eJ.a(2)));
        if (iVar.a() == 7) {
            JButton jButton = new JButton("...");
            jButton.addActionListener(new C1778d(this));
            jButton.setPreferredSize(eJ.a(14, 12));
            jPanel.add(jButton);
        }
        this.f13190d = new cF("No Help Available", aaVar);
        jPanel.add(this.f13190d);
        add("East", jPanel);
        a(iVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        String strA = bV.a((Component) this, this.f13186f.e(), new String[]{""}, "", "", true);
        if (strA == null || strA.isEmpty()) {
            return;
        }
        this.f13188b.setText(strA);
    }

    public void a(d.i iVar) throws IllegalArgumentException {
        this.f13186f = iVar;
        if (this.f13186f.a() == 0 && this.f13186f.b() != null) {
            List listB = this.f13186f.b();
            Collections.sort(listB, new C1779e(this));
            Iterator it = listB.iterator();
            while (it.hasNext()) {
                this.f13189c.addItem(it.next());
            }
            this.f13189c.setEnabled(true);
        }
        setName(iVar.c());
        this.f13187a.setText(iVar.c());
        this.f13190d.a(this.f13186f.f());
        this.f13190d.setEnabled((this.f13186f.f() == null || this.f13186f.f().isEmpty()) ? false : true);
        a(iVar.d());
    }

    public d.i a() {
        return this.f13186f;
    }

    public String b() {
        return this.f13186f.a() == 0 ? this.f13189c.getSelectedItem() instanceof d.h ? ((d.h) this.f13189c.getSelectedItem()).a() : this.f13189c.getSelectedItem() != null ? this.f13189c.getSelectedItem().toString() : "" : this.f13188b.getText();
    }

    public void a(String str) {
        this.f13188b.setText(str);
        if (this.f13189c.getItemCount() > 0 && (this.f13189c.getItemAt(0) instanceof d.h)) {
            this.f13189c.setSelectedItem(new d.h(str, ""));
            return;
        }
        if (str != null && !str.isEmpty() && !this.f13189c.c(str)) {
            this.f13189c.a(str);
            this.f13189c.setEnabled(false);
        }
        this.f13189c.setSelectedItem(str);
    }
}
