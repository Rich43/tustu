package bH;

import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* renamed from: bH.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/w.class */
public class C1015w extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextField f7061a = new JTextField();

    /* renamed from: b, reason: collision with root package name */
    JTextField f7062b = new JTextField();

    public C1015w() {
        setLayout(new GridLayout(0, 1));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("West", new JLabel("Hex: "));
        jPanel.add(BorderLayout.CENTER, this.f7061a);
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 0));
        JButton jButton = new JButton("Float to Hex");
        jButton.addActionListener(new C1016x(this));
        jPanel2.add(jButton);
        add(jPanel2);
        JButton jButton2 = new JButton("Hex to Float");
        jButton2.addActionListener(new C1017y(this));
        jPanel2.add(jButton2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add("West", new JLabel("Float: "));
        jPanel3.add(BorderLayout.CENTER, this.f7062b);
        add(jPanel3);
    }

    public void a(Window window) {
        bV.a(this, window, "Float Conversion", (InterfaceC1565bc) null);
    }
}
