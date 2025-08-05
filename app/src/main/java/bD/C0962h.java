package bD;

import bH.W;
import bH.aa;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: bD.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bD/h.class */
public class C0962h extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JLabel f6664a = new JLabel(" ?", 2);

    /* renamed from: b, reason: collision with root package name */
    JLabel f6665b = new JLabel("? KB", 2);

    /* renamed from: c, reason: collision with root package name */
    JLabel f6666c = new JLabel("?.?? GB", 2);

    /* renamed from: d, reason: collision with root package name */
    aa f6667d;

    public C0962h(aa aaVar) {
        this.f6667d = null;
        this.f6667d = aaVar;
        setLayout(new GridLayout(1, 0));
        add(a(this.f6666c, "Total Size:"));
        add(a(this.f6664a, "Files:"));
        add(a(this.f6665b, "Used:"));
        setMinimumSize(eJ.a(400, 24));
        setPreferredSize(eJ.a(400, 24));
    }

    public void a(long j2) throws IllegalArgumentException {
        if (j2 > 0) {
            this.f6666c.setText(W.a(j2));
        } else {
            this.f6666c.setText(a("Unknown"));
        }
    }

    public void b(long j2) throws IllegalArgumentException {
        this.f6665b.setText(W.a(j2));
    }

    public void a(int i2) throws IllegalArgumentException {
        this.f6664a.setText("" + i2);
    }

    private String a(String str) {
        return this.f6667d != null ? this.f6667d.a(str) : str;
    }

    private JPanel a(JLabel jLabel, String str) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(1));
        jPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(3, 3));
        jPanel2.add("West", new JLabel(this.f6667d.a(str)));
        jPanel2.add(BorderLayout.CENTER, jLabel);
        jPanel.add(jPanel2);
        return jPanel;
    }

    public String a() {
        return this.f6665b.getText();
    }
}
