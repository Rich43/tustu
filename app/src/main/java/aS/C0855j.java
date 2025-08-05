package as;

import bH.ab;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* renamed from: as.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:as/j.class */
public class C0855j extends JDialog implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    JTextField f6258a;

    /* renamed from: b, reason: collision with root package name */
    JButton f6259b;

    public C0855j(Window window) {
        this(window, true);
    }

    public C0855j(Window window, boolean z2) {
        super(window);
        this.f6258a = new JTextField("", 30);
        this.f6259b = new JButton("...");
        setTitle(a("Remote File Open Preferences"));
        setLayout(new BorderLayout(eJ.a(10), eJ.a(10)));
        add(BorderLayout.CENTER, a());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new C0856k(this));
        jPanel.add(jButton);
        add("South", jPanel);
    }

    private JPanel a() {
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(a("File Download Preferences")));
        jPanel.setLayout(new GridLayout(0, 1, eJ.a(5), eJ.a(5)));
        JComboBox jComboBox = new JComboBox();
        jComboBox.setEditable(false);
        jComboBox.addItem("Open Local File");
        jComboBox.addItem("Overwrite Local File");
        jComboBox.addItem("Rename Downloaded File");
        jComboBox.setSelectedIndex(h.i.b(h.i.f12322ao, h.i.f12323ap));
        jComboBox.addActionListener(new C0857l(this, jComboBox));
        jPanel.add(a("When file already downloaded", jComboBox));
        JComboBox jComboBox2 = new JComboBox();
        jComboBox2.setEditable(false);
        jComboBox2.addItem("Use last Open Location");
        jComboBox2.addItem("Use Specific Folder");
        int iB = h.i.b(h.i.f12327at, h.i.f12328au);
        jComboBox2.setSelectedIndex(iB);
        this.f6258a.setEnabled(iB == h.i.f12329av);
        this.f6259b.setEnabled(iB == h.i.f12329av);
        jComboBox2.addActionListener(new m(this, jComboBox2));
        jPanel.add(a("File Download Location", jComboBox2));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(BorderLayout.CENTER, this.f6258a);
        jPanel2.add("East", this.f6259b);
        this.f6259b.addActionListener(new n(this));
        jPanel.add(jPanel2);
        return jPanel;
    }

    private JPanel a(String str, JComponent jComponent) {
        int iA = eJ.a(100);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        jPanel.add(BorderLayout.CENTER, new JLabel(a(str), 4));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("West", jComponent);
        jComponent.setMinimumSize(new Dimension(iA, 20));
        jPanel.add("East", jPanel2);
        return jPanel;
    }

    private boolean b() {
        if (h.i.b(h.i.f12327at, h.i.f12328au) != h.i.f12329av) {
            return true;
        }
        String text = this.f6258a.getText();
        if (text == null || text.isEmpty()) {
            bV.d(a("A valid Directory is required if downloading to a specific directory."), this.f6258a);
            return false;
        }
        File file = new File(text);
        if (!file.exists() || !file.isDirectory()) {
            bV.d(file.getAbsolutePath() + "\n" + a("is not a valid Directory."), this.f6258a);
            return false;
        }
        if (h.h.a(text)) {
            return true;
        }
        bV.d(a("Do not have write access to directory") + "\n" + file.getAbsolutePath(), this.f6258a);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) {
        return ab.a().a(str);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        if (b()) {
            dispose();
        }
    }
}
