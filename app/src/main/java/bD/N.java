package bD;

import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:bD/N.class */
class N extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextField f6643a = new JTextField();

    /* renamed from: b, reason: collision with root package name */
    JTextField f6644b = new JTextField();

    /* renamed from: c, reason: collision with root package name */
    String f6645c;

    /* renamed from: d, reason: collision with root package name */
    File f6646d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ I f6647e;

    public N(I i2, File file) {
        this.f6647e = i2;
        this.f6646d = file;
        int iA = eJ.a(5);
        setBorder(BorderFactory.createEmptyBorder(iA, iA, iA, iA));
        setLayout(new GridLayout(1, 0, iA, iA));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("West", new JLabel(i2.a("Default Name") + ": "));
        jPanel.add(BorderLayout.CENTER, this.f6643a);
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        this.f6643a.setEditable(false);
        jPanel2.add("West", new JLabel(i2.a("New Name") + ": "));
        jPanel2.add(BorderLayout.CENTER, this.f6644b);
        add(jPanel2);
        String name = file.getName();
        if (name.contains(".")) {
            this.f6645c = name.substring(name.lastIndexOf("."));
            name = name.substring(0, name.lastIndexOf("."));
        } else {
            this.f6645c = "";
        }
        this.f6644b.addFocusListener(new O(this, i2));
        this.f6643a.setText(name);
        this.f6644b.setText(name);
    }

    public File a() {
        return new File(this.f6646d.getParent(), this.f6644b.getText() + this.f6645c);
    }

    public String b() {
        return this.f6644b.getText();
    }

    public void a(String str) {
        this.f6644b.setText(str);
    }
}
