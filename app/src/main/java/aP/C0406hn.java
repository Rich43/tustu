package aP;

import bH.C0995c;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1798a;
import s.C1818g;
import sun.security.tools.policytool.ToolWindow;

/* renamed from: aP.hn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hn.class */
public class C0406hn extends JDialog implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final int f3590a = 450;

    /* renamed from: b, reason: collision with root package name */
    final int f3591b = 600;

    /* renamed from: d, reason: collision with root package name */
    private JButton f3592d;

    /* renamed from: e, reason: collision with root package name */
    private JButton f3593e;

    /* renamed from: f, reason: collision with root package name */
    private JButton f3594f;

    /* renamed from: g, reason: collision with root package name */
    private JPanel f3595g;

    /* renamed from: h, reason: collision with root package name */
    private JPanel f3596h;

    /* renamed from: i, reason: collision with root package name */
    private JTextField f3597i;

    /* renamed from: c, reason: collision with root package name */
    JButton f3598c;

    /* renamed from: j, reason: collision with root package name */
    private JTextArea f3599j;

    /* renamed from: k, reason: collision with root package name */
    private JTextArea f3600k;

    /* renamed from: l, reason: collision with root package name */
    private C0412ht f3601l;

    /* renamed from: m, reason: collision with root package name */
    private z.n f3602m;

    /* renamed from: n, reason: collision with root package name */
    private z.k f3603n;

    /* renamed from: o, reason: collision with root package name */
    private Properties f3604o;

    /* renamed from: p, reason: collision with root package name */
    private int f3605p;

    public C0406hn(Frame frame, boolean z2) {
        super(frame, "Mini-Term", true);
        this.f3590a = 450;
        this.f3591b = 600;
        this.f3604o = null;
        this.f3605p = 3;
        this.f3602m = new z.n();
        h();
        this.f3605p = C1798a.a().c(C1798a.f13345aA, 3);
        addWindowListener(new C0411hs(this, this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0));
        jPanel.add(new JLabel(C1818g.b("Output Format") + CallSiteDescriptor.TOKEN_DELIMITER, 0));
        ButtonGroup buttonGroup = new ButtonGroup();
        JCheckBox jCheckBox = new JCheckBox("ASCII");
        buttonGroup.add(jCheckBox);
        if (this.f3605p == 3) {
            jCheckBox.setSelected(true);
        }
        jCheckBox.addItemListener(new C0407ho(this));
        jPanel.add(jCheckBox);
        JCheckBox jCheckBox2 = new JCheckBox("Hex", this.f3605p == 2);
        buttonGroup.add(jCheckBox2);
        if (this.f3605p == 2) {
            jCheckBox2.setSelected(true);
        }
        jCheckBox2.addItemListener(new C0408hp(this));
        jPanel.add(jCheckBox2);
        JCheckBox jCheckBox3 = new JCheckBox("Decimal");
        buttonGroup.add(jCheckBox3);
        if (this.f3605p == 1) {
            jCheckBox3.setSelected(true);
        }
        jCheckBox3.addItemListener(new C0409hq(this));
        jPanel.add(jCheckBox3);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("North", jPanel);
        if (z2) {
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new BorderLayout());
            this.f3597i = new JTextField();
            jPanel3.add(BorderLayout.CENTER, this.f3597i);
            this.f3598c = new JButton("Send");
            this.f3598c.addActionListener(new C0410hr(this));
            jPanel3.add("East", this.f3598c);
            jPanel2.add(BorderLayout.CENTER, jPanel3);
        }
        add("North", jPanel2);
        this.f3596h = new JPanel();
        this.f3596h.setLayout(new GridLayout(2, 1, 2, 2));
        add("East", new JLabel());
        add("West", new JLabel());
        this.f3599j = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(this.f3599j);
        this.f3599j.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f3596h.add(jScrollPane);
        this.f3600k = new JTextArea();
        JScrollPane jScrollPane2 = new JScrollPane(this.f3600k);
        this.f3600k.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f3600k.setEditable(false);
        this.f3596h.add(jScrollPane2);
        add(this.f3596h, BorderLayout.CENTER);
        G.R rC = G.T.a().c();
        boolean z3 = rC != null && rC.C().q();
        this.f3601l = new C0412ht(this, frame);
        if (z3) {
            for (Component component : this.f3601l.getComponents()) {
                component.setEnabled(false);
            }
        }
        this.f3595g = new JPanel();
        this.f3592d = new JButton(C1818g.b("Open Port"));
        this.f3592d.addActionListener(this);
        this.f3592d.setEnabled(!z3);
        this.f3595g.add(this.f3592d);
        this.f3593e = new JButton(C1818g.b("Close Port"));
        this.f3593e.addActionListener(this);
        this.f3593e.setEnabled(false);
        this.f3593e.setEnabled(!z3);
        this.f3595g.add(this.f3593e);
        this.f3594f = new JButton(C1818g.b(ToolWindow.QUIT));
        this.f3594f.addActionListener(this);
        this.f3595g.add(this.f3594f);
        JPanel jPanel4 = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        jPanel4.setLayout(gridBagLayout);
        gridBagConstraints.gridwidth = 0;
        gridBagLayout.setConstraints(this.f3601l, gridBagConstraints);
        gridBagConstraints.weightx = 1.0d;
        jPanel4.add(this.f3601l);
        gridBagLayout.setConstraints(this.f3595g, gridBagConstraints);
        jPanel4.add(this.f3595g);
        add(jPanel4, "South");
        this.f3603n = new z.k(this, this.f3602m, this.f3599j, this.f3600k);
        a();
        setSize(600, 450);
        this.f3599j.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() throws HeadlessException {
        String text = this.f3597i.getText();
        try {
            int[] iArrA = C0995c.a(text.contains(",") ? text.split(",") : text.split(" "));
            if (0 != 0 && G.T.a().c() != null && G.T.a().c().O().C() != null) {
                iArrA = C0995c.b(G.T.a().c().O().C().a(C0995c.a(iArrA)));
            }
            bH.C.c("Sending: " + C0995c.b(iArrA));
            this.f3603n.a(iArrA);
        } catch (IOException e2) {
            JOptionPane.showMessageDialog(this.rootPane, "Error sending bytes.\nCheck the port is open.Reported Error:\n" + e2.getMessage());
            Logger.getLogger(C0406hn.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (Exception e3) {
            JOptionPane.showMessageDialog(this.rootPane, "Error Parsing bytes.\nMust be in the format:\n<number><space><number><space>...\nExample: 0x02 0x66 0x00 0xF4 0xDB 0xDF 0x21\nReported Error:\n" + e3.getMessage());
            Logger.getLogger(C0406hn.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    public void a() {
        this.f3601l.a();
    }

    public void a(int i2) {
        if (i2 == 2 || i2 == 1 || i2 == 3) {
            this.f3605p = i2;
            bH.C.c("format changed to:" + i2);
            C1798a.a().b(C1798a.f13345aA, "" + i2);
        }
    }

    public int b() {
        return this.f3605p;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();
        this.f3599j.requestFocus();
        if (actionCommand.equals(C1818g.b("Open Port"))) {
            try {
                c();
            } catch (z.m e2) {
                com.efiAnalytics.ui.bV.d("Error Opening Port!\nError opening port,\n" + e2.getMessage() + ".\nSelect new settings, try again.", this);
            }
        }
        if (actionCommand.equals(C1818g.b("Close Port"))) {
            e();
        }
        if (actionCommand.equals(C1818g.b(ToolWindow.QUIT))) {
            e();
            dispose();
        }
    }

    public void c() {
        this.f3592d.setEnabled(false);
        Cursor cursor = getCursor();
        a(Cursor.getPredefinedCursor(3));
        this.f3601l.b();
        try {
            this.f3603n.a();
            d();
            a(cursor);
        } catch (z.m e2) {
            this.f3592d.setEnabled(true);
            a(cursor);
            throw e2;
        }
    }

    public void d() {
        this.f3592d.setEnabled(false);
        this.f3593e.setEnabled(true);
    }

    public void e() {
        this.f3603n.c();
        this.f3592d.setEnabled(true);
        this.f3593e.setEnabled(false);
    }

    private void a(Cursor cursor) {
        setCursor(cursor);
        this.f3600k.setCursor(cursor);
        this.f3599j.setCursor(cursor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        this.f3603n.c();
        dispose();
    }

    private void h() {
        C1798a c1798aA = C1798a.a();
        this.f3605p = c1798aA.c(C1798a.f13345aA, 2);
        this.f3602m.a(c1798aA.c(C1798a.f13344az, "COM1"));
        this.f3602m.b(c1798aA.c(C1798a.f13343ay, "9600"));
    }

    @Override // java.awt.Dialog, java.awt.Window, java.awt.Component
    public void setVisible(boolean z2) {
        super.setVisible(z2);
        if (z2) {
            this.f3599j.requestFocus();
        }
    }

    public void a(String str) {
        this.f3601l.a(str);
    }

    public void b(String str) {
        this.f3601l.b(str);
    }
}
