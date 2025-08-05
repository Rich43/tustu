package aY;

import G.T;
import aP.N;
import aP.P;
import com.efiAnalytics.ui.bV;
import com.sun.glass.ui.Clipboard;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import r.C1798a;

/* renamed from: aY.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aY/a.class */
public class C0475a extends JDialog implements P {

    /* renamed from: a, reason: collision with root package name */
    JTextField f4033a;

    /* renamed from: b, reason: collision with root package name */
    JTextField f4034b;

    /* renamed from: c, reason: collision with root package name */
    JButton f4035c;

    /* renamed from: d, reason: collision with root package name */
    JButton f4036d;

    /* renamed from: e, reason: collision with root package name */
    JToggleButton f4037e;

    /* renamed from: f, reason: collision with root package name */
    JLabel f4038f;

    /* renamed from: g, reason: collision with root package name */
    JLabel f4039g;

    /* renamed from: h, reason: collision with root package name */
    JLabel f4040h;

    /* renamed from: i, reason: collision with root package name */
    N f4041i;

    /* renamed from: j, reason: collision with root package name */
    int f4042j;

    /* renamed from: k, reason: collision with root package name */
    String f4043k;

    public C0475a(Window window) {
        super(window, "Automated msq loader");
        this.f4033a = new JTextField("");
        this.f4034b = new JTextField("");
        this.f4035c = new JButton("Browse");
        this.f4036d = new JButton("Browse");
        this.f4037e = new JToggleButton("Start Test");
        this.f4038f = new JLabel("", 2);
        this.f4039g = new JLabel("", 2);
        this.f4040h = new JLabel("", 2);
        this.f4041i = new N(T.a().c());
        this.f4042j = 0;
        this.f4043k = "<html><center>This dialog will continuously load the 2 msq files alternating.<br>The second will be loaded once the previous has completed writing to the controller.<br>data verification will be performed after every page burn by means of crc check or <br>a full page read and byte compare.</center></html>";
        this.f4041i.a(this);
        c();
    }

    private void c() {
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 1));
        jPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextPane jTextPane = new JTextPane();
        jTextPane.setOpaque(true);
        jTextPane.setContentType(Clipboard.HTML_TYPE);
        jTextPane.setEditable(false);
        jTextPane.setText(this.f4043k);
        jPanel2.add(jTextPane);
        add("North", jPanel2);
        jPanel.setLayout(new GridLayout(0, 1));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add("West", new JLabel("MSQ file 1:"));
        jPanel4.add(BorderLayout.CENTER, this.f4033a);
        jPanel4.add("East", this.f4035c);
        this.f4035c.addActionListener(new C0476b(this));
        jPanel3.add("South", jPanel4);
        jPanel.add(jPanel3);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BorderLayout());
        jPanel6.add("West", new JLabel("MSQ file 2:"));
        jPanel6.add(BorderLayout.CENTER, this.f4034b);
        jPanel6.add("East", this.f4036d);
        this.f4036d.addActionListener(new C0477c(this));
        jPanel5.add("South", jPanel6);
        jPanel.add(jPanel5);
        JPanel jPanel7 = new JPanel();
        jPanel7.setLayout(new BorderLayout());
        JPanel jPanel8 = new JPanel();
        jPanel8.setLayout(new GridLayout(1, 0));
        jPanel8.add(new JLabel("Total File Loads:", 4));
        jPanel8.add(this.f4039g);
        jPanel8.add(new JLabel("Bytes Sent:", 4));
        jPanel8.add(this.f4040h);
        jPanel8.add(new JLabel("Time to send Last:", 4));
        jPanel8.add(this.f4038f);
        jPanel7.add("South", jPanel8);
        jPanel.add(jPanel7);
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel9 = new JPanel();
        jPanel9.setLayout(new FlowLayout(1));
        this.f4037e.addActionListener(new d(this));
        jPanel9.add(this.f4037e);
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new e(this));
        jPanel9.add(jButton);
        add("South", jPanel9);
    }

    public void a() {
        try {
            this.f4041i.a(new File(this.f4033a.getText()));
            this.f4041i.b(new File(this.f4034b.getText()));
            this.f4041i.a();
            this.f4041i.start();
            this.f4037e.setSelected(true);
            this.f4037e.setText("Stop Test");
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this);
            this.f4037e.setSelected(false);
            Logger.getLogger(C0475a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public void b() {
        this.f4041i.b();
        this.f4041i = new N(T.a().c());
        this.f4037e.setSelected(false);
        this.f4037e.setText("Start Test");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String d() {
        return bV.b(this, "Open Tune File", new String[]{C1798a.cw}, "", aE.a.A() != null ? aE.a.A().t() : "");
    }

    @Override // aP.P
    public void a(int i2, int i3) throws IllegalArgumentException {
        JLabel jLabel = this.f4039g;
        StringBuilder sbAppend = new StringBuilder().append("");
        int i4 = this.f4042j;
        this.f4042j = i4 + 1;
        jLabel.setText(sbAppend.append(i4).toString());
        this.f4038f.setText((i2 / 1000.0d) + "s.");
        this.f4040h.setText("" + i3);
    }
}
