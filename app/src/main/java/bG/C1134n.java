package bg;

import G.C0073bf;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* renamed from: bg.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/n.class */
public class C1134n extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextField f8086a = new JTextField("", 20);

    /* renamed from: b, reason: collision with root package name */
    JTextField f8087b = new JTextField("", 20);

    /* renamed from: c, reason: collision with root package name */
    C0073bf f8088c = null;

    public C1134n() {
        setLayout(new GridLayout(1, 0));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("West", new JLabel("Tune View Reference Name:"));
        jPanel.add(BorderLayout.CENTER, this.f8086a);
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("West", new JLabel("Tune View Tab Title:"));
        jPanel2.add(BorderLayout.CENTER, this.f8087b);
        add(jPanel2);
    }
}
