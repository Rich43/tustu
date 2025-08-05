package ao;

import bH.C1011s;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import h.C1737b;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:ao/gJ.class */
public class gJ extends JPanel {

    /* renamed from: c, reason: collision with root package name */
    JRadioButton f5927c;

    /* renamed from: d, reason: collision with root package name */
    JRadioButton f5928d;

    /* renamed from: e, reason: collision with root package name */
    JButton f5929e;

    /* renamed from: f, reason: collision with root package name */
    JButton f5930f;

    /* renamed from: a, reason: collision with root package name */
    JComboBox f5925a = new JComboBox();

    /* renamed from: b, reason: collision with root package name */
    JTextField f5926b = new JTextField();

    /* renamed from: h, reason: collision with root package name */
    private boolean f5931h = false;

    /* renamed from: g, reason: collision with root package name */
    JDialog f5932g = null;

    public gJ() {
        setBorder(BorderFactory.createEmptyBorder(com.efiAnalytics.ui.eJ.a(10), com.efiAnalytics.ui.eJ.a(10), com.efiAnalytics.ui.eJ.a(10), com.efiAnalytics.ui.eJ.a(10)));
        setLayout(new BorderLayout());
        add("North", new JLabel("Import Settings Profile"));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        String strD = at.c.a().d();
        ButtonGroup buttonGroup = new ButtonGroup();
        this.f5927c = new JRadioButton("Import into current Setting Profile: " + strD, true);
        jPanel.add(this.f5927c);
        buttonGroup.add(this.f5927c);
        this.f5928d = new JRadioButton("New Profile: ");
        buttonGroup.add(this.f5928d);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("West", this.f5928d);
        jPanel2.add(BorderLayout.CENTER, this.f5926b);
        jPanel.add(jPanel2);
        this.f5926b.setEnabled(false);
        this.f5928d.addActionListener(new gK(this));
        add(BorderLayout.CENTER, jPanel);
        this.f5928d.setEnabled(C1737b.a().a("fa-9fdspoijoijnfdz09jfdsa098j"));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(1));
        this.f5930f = new JButton("Cancel");
        if (bH.I.a()) {
            this.f5929e = new JButton("Ok");
            jPanel3.add(this.f5929e);
        }
        jPanel3.add(this.f5930f);
        if (!bH.I.a()) {
            this.f5929e = new JButton(XIncludeHandler.HTTP_ACCEPT);
            jPanel3.add(this.f5929e);
        }
        add("South", jPanel3);
        this.f5930f.addActionListener(new gL(this));
        this.f5929e.addActionListener(new gM(this));
    }

    public void a(String str) {
        this.f5926b.setText(str);
    }

    public String a() {
        return this.f5926b.getText();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        this.f5931h = true;
        this.f5932g.dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        if (this.f5927c.isSelected() || C1011s.a(this.f5926b.getText())) {
            this.f5932g.dispose();
        } else if (this.f5926b.getText().isEmpty()) {
            com.efiAnalytics.ui.bV.d("You must enter a name for the Setting Profile.", C0645bi.a().b());
        } else {
            com.efiAnalytics.ui.bV.d("Invalid Profile Name: '" + this.f5926b.getText() + "'. Please remove any special characters.", C0645bi.a().b());
        }
    }

    public boolean b() {
        return this.f5928d.isSelected();
    }

    public boolean c() {
        return this.f5931h;
    }

    public void a(Window window) {
        this.f5932g = new JDialog(window, "Setting Profile", Dialog.ModalityType.APPLICATION_MODAL);
        this.f5932g.add(BorderLayout.CENTER, this);
        this.f5932g.pack();
        com.efiAnalytics.ui.bV.a(window, (Component) this.f5932g);
        this.f5932g.setVisible(true);
    }
}
