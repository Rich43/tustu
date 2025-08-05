package aP;

import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import r.C1798a;
import s.C1818g;

/* renamed from: aP.hu, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hu.class */
public class C0413hu extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JCheckBox f3618a = new JCheckBox(C1818g.b("Always launch new MegaLogViewer"));

    /* renamed from: b, reason: collision with root package name */
    String f3619b = C1818g.b("MegaLogViewer Preferences");

    public C0413hu() {
        setBorder(BorderFactory.createTitledBorder(this.f3619b));
        setLayout(new GridLayout(0, 1));
        this.f3618a.setSelected(C1798a.a().c(C1798a.f13416bT, C1798a.f13417bU));
        this.f3618a.addActionListener(new C0414hv(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, this.f3618a);
        jPanel.add("East", new com.efiAnalytics.ui.cF("If checked, a new instnace of MegaLogViewer will be launched for each use. Otherwise the log will be sent to the already running MegaLogViewer", C1818g.d()));
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        JButton jButton = new JButton(C1818g.b("Reset and Clear MegaLogViewer Location"));
        jButton.addActionListener(new C0415hw(this));
        jPanel2.add(jButton);
        add(jPanel2);
    }

    public static void a(Component component) {
        C0413hu c0413hu = new C0413hu();
        com.efiAnalytics.ui.bV.a(c0413hu, component, c0413hu.f3619b, (InterfaceC1565bc) null);
    }
}
