package s;

import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import r.C1806i;

/* renamed from: s.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:s/b.class */
public class C1813b extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    JLabel f13516a;

    /* renamed from: b, reason: collision with root package name */
    JLabel f13517b;

    /* renamed from: c, reason: collision with root package name */
    JTextField f13518c;

    /* renamed from: d, reason: collision with root package name */
    InterfaceC1817f f13519d;

    /* renamed from: e, reason: collision with root package name */
    String f13520e;

    public C1813b(InterfaceC1817f interfaceC1817f) throws IllegalArgumentException {
        super(bV.b(interfaceC1817f.getComponent()), C1818g.b("Update Translation"), Dialog.ModalityType.APPLICATION_MODAL);
        this.f13516a = new JLabel("", 0);
        this.f13517b = new JLabel("", 0);
        this.f13518c = new JTextField();
        this.f13519d = null;
        this.f13520e = null;
        this.f13519d = interfaceC1817f;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(a("")));
        add(BorderLayout.CENTER, jPanel);
        String displayLanguage = C1818g.c().getDisplayLanguage();
        C1818g.c().getLanguage();
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1, 4, 4));
        jPanel2.add(new JLabel(a("Update Translation")));
        jPanel2.add(new JLabel(a("English verbiage:"), 2));
        this.f13517b.setText(interfaceC1817f.a());
        jPanel2.add(this.f13517b);
        jPanel2.add(new JLabel(a("Current Translation") + " (" + displayLanguage + "):", 2));
        this.f13516a.setText(C1818g.b(interfaceC1817f.a()));
        jPanel2.add(this.f13516a);
        jPanel2.add(new JLabel(a("Proposed Translation") + " (" + displayLanguage + "):", 2));
        jPanel2.add(this.f13518c);
        this.f13518c.setText(C1818g.b(interfaceC1817f.a()));
        jPanel.add(BorderLayout.CENTER, jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        JButton jButton = new JButton(a("Submit Translation Update"));
        jButton.addActionListener(new C1814c(this));
        jPanel3.add(jButton);
        JButton jButton2 = new JButton(a("Cancel"));
        jButton2.addActionListener(new C1815d(this));
        jPanel3.add(jButton2);
        jPanel.add("South", jPanel3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        if (!this.f13520e.equals(this.f13518c.getText())) {
            this.f13519d.a(this.f13518c.getText());
        }
        dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        dispose();
    }

    private String a(String str) {
        return C1818g.b(str);
    }

    public static void a(InterfaceC1817f interfaceC1817f) {
        if (C1806i.a().a("-=[pfds;'43-0pd")) {
            C1813b c1813b = new C1813b(interfaceC1817f);
            c1813b.pack();
            bV.a(interfaceC1817f.getComponent(), c1813b);
            c1813b.setVisible(true);
        }
    }
}
