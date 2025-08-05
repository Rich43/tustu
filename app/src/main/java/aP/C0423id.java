package aP;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import sun.security.pkcs11.wrapper.Constants;

/* renamed from: aP.id, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/id.class */
public class C0423id extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    static String f3722a = "Open Log";

    /* renamed from: c, reason: collision with root package name */
    private com.efiAnalytics.ui.aP f3723c;

    /* renamed from: d, reason: collision with root package name */
    private JLabel f3724d;

    /* renamed from: e, reason: collision with root package name */
    private JLabel f3725e;

    /* renamed from: f, reason: collision with root package name */
    private JLabel f3726f;

    /* renamed from: b, reason: collision with root package name */
    JScrollPane f3727b;

    public C0423id(Frame frame) throws IllegalArgumentException {
        super(frame, "Raw File Viewer");
        this.f3723c = new com.efiAnalytics.ui.aP();
        this.f3724d = new JLabel();
        this.f3725e = new JLabel(" ");
        this.f3726f = new JLabel(" ");
        this.f3727b = null;
        this.f3727b = new JScrollPane(this.f3723c);
        add(BorderLayout.CENTER, this.f3727b);
        this.f3723c.addCaretListener(new C0424ie(this));
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        jMenuBar.add(jMenu);
        jMenu.add(f3722a).addActionListener(new Cif(this));
        add("North", jMenuBar);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("North", this.f3725e);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout());
        JButton jButton = new JButton(f3722a);
        jButton.setToolTipText("Open a binary file");
        jButton.addActionListener(new C0425ig(this));
        jPanel2.add(jButton);
        JPanel jPanel3 = new JPanel();
        ButtonGroup buttonGroup = new ButtonGroup();
        jPanel3.setLayout(new GridLayout(1, 0));
        JRadioButton jRadioButton = new JRadioButton("Bin", this.f3723c.d() == 2);
        jRadioButton.addActionListener(new C0426ih(this));
        jPanel3.add(jRadioButton);
        buttonGroup.add(jRadioButton);
        JRadioButton jRadioButton2 = new JRadioButton("Dec", this.f3723c.d() == 10);
        jRadioButton2.addActionListener(new C0427ii(this));
        jPanel3.add(jRadioButton2);
        buttonGroup.add(jRadioButton2);
        JRadioButton jRadioButton3 = new JRadioButton("Hex", this.f3723c.d() == 16);
        jRadioButton3.addActionListener(new C0428ij(this));
        jPanel3.add(jRadioButton3);
        buttonGroup.add(jRadioButton3);
        jPanel2.add(jPanel3);
        JSlider jSlider = new JSlider();
        jSlider.setMajorTickSpacing(1);
        jSlider.setMinimum(1);
        jSlider.setMaximum(600);
        jSlider.setValue(this.f3723c.e());
        jSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), "Record Length"));
        jSlider.addChangeListener(new C0429ik(this));
        jPanel2.add(jSlider);
        this.f3724d.setText(Constants.INDENT + this.f3723c.e());
        jPanel2.add(this.f3724d);
        jPanel.add("South", jPanel2);
        jPanel.add(BorderLayout.CENTER, this.f3726f);
        add("South", jPanel);
        setSize(520, 400);
        com.efiAnalytics.ui.bV.a((Window) frame, (Component) this);
    }

    public void a() {
        this.f3725e.setText(this.f3723c.a());
        this.f3726f.setText("Row " + (this.f3723c.getCaretPosition() / this.f3723c.getColumns()) + ": " + this.f3723c.c());
    }

    public void b() {
        String strB = com.efiAnalytics.ui.bV.b(this, "Open Log File", new String[]{"msl", "log", "*"}, ".", "");
        aZ.c cVar = new aZ.c();
        try {
            long jNanoTime = System.nanoTime();
            cVar.a(strB);
            bH.C.c("Time to open:" + ((System.nanoTime() - jNanoTime) / 1000000) + "ms.");
            long jNanoTime2 = System.nanoTime();
            int[] iArrA = cVar.a();
            bH.C.c("Time to read:" + ((System.nanoTime() - jNanoTime2) / 1000000) + "ms.");
            long jNanoTime3 = System.nanoTime();
            this.f3723c.a(iArrA);
            bH.C.c("Time to set bytes:" + ((System.nanoTime() - jNanoTime3) / 1000000) + "ms.");
            bH.C.c(iArrA.length + " read");
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Error opening Log file, check Application log for more detail.\n" + e2.getMessage(), this);
            e2.printStackTrace();
        }
    }

    public void a(String str) {
        if (str.equals(f3722a)) {
            b();
        }
    }

    public void a(int i2) {
        this.f3723c.a(i2);
    }
}
