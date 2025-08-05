package ao;

import W.C0184j;
import W.C0188n;
import com.efiAnalytics.ui.BinTableView;
import com.efiAnalytics.ui.C1562b;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.sun.corba.se.impl.util.Version;
import com.sun.imageio.plugins.jpeg.JPEG;
import g.C1733k;
import h.C1737b;
import i.InterfaceC1741a;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import sun.security.tools.policytool.ToolWindow;

/* renamed from: ao.eu, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/eu.class */
public class C0737eu extends JDialog implements InterfaceC0609a, InterfaceC1741a {

    /* renamed from: a, reason: collision with root package name */
    hI f5665a;

    /* renamed from: b, reason: collision with root package name */
    C0806hi f5666b;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f5667c;

    /* renamed from: d, reason: collision with root package name */
    hE f5668d;

    /* renamed from: e, reason: collision with root package name */
    hH f5669e;

    /* renamed from: f, reason: collision with root package name */
    hH f5670f;

    /* renamed from: g, reason: collision with root package name */
    hH f5671g;

    /* renamed from: h, reason: collision with root package name */
    hF f5672h;

    /* renamed from: i, reason: collision with root package name */
    hI f5673i;

    /* renamed from: j, reason: collision with root package name */
    String f5674j;

    /* renamed from: k, reason: collision with root package name */
    String f5675k;

    /* renamed from: l, reason: collision with root package name */
    C0188n f5676l;

    /* renamed from: m, reason: collision with root package name */
    JPanel f5677m;

    /* renamed from: n, reason: collision with root package name */
    JPanel f5678n;

    /* renamed from: o, reason: collision with root package name */
    JSlider f5679o;

    /* renamed from: p, reason: collision with root package name */
    C0618ai f5680p;

    /* renamed from: q, reason: collision with root package name */
    boolean f5681q;

    /* renamed from: r, reason: collision with root package name */
    boolean f5682r;

    /* renamed from: s, reason: collision with root package name */
    String f5683s;

    /* renamed from: t, reason: collision with root package name */
    String f5684t;

    /* renamed from: u, reason: collision with root package name */
    String f5685u;

    /* renamed from: v, reason: collision with root package name */
    String f5686v;

    /* renamed from: w, reason: collision with root package name */
    eZ f5687w;

    /* renamed from: x, reason: collision with root package name */
    String f5688x;

    /* renamed from: y, reason: collision with root package name */
    com.efiAnalytics.ui.dQ f5689y;

    /* renamed from: z, reason: collision with root package name */
    ArrayList f5690z;

    public C0737eu(hF hFVar, hH hHVar, hH hHVar2, C0188n c0188n, Frame frame, String str) throws NumberFormatException {
        super(frame, str, false);
        this.f5665a = null;
        this.f5666b = new C0806hi();
        this.f5667c = new ArrayList();
        this.f5668d = null;
        this.f5669e = null;
        this.f5670f = null;
        this.f5671g = null;
        this.f5672h = null;
        this.f5673i = null;
        this.f5676l = null;
        this.f5677m = null;
        this.f5678n = null;
        this.f5679o = null;
        this.f5680p = null;
        this.f5681q = true;
        this.f5682r = true;
        this.f5683s = Version.BUILD;
        this.f5684t = "10000000.0";
        this.f5685u = "0";
        this.f5686v = "600";
        this.f5687w = null;
        this.f5688x = "MAP";
        this.f5689y = null;
        this.f5690z = new ArrayList();
        this.f5668d = new hE();
        if (c0188n.a("Load") != null) {
            this.f5688x = "Load";
        }
        this.f5688x = h.i.a("yAxisField", h.g.a().a(h.g.f12251j));
        getContentPane().add("North", d());
        a(c0188n);
        this.f5668d.a(this);
        this.f5669e = b(hHVar);
        this.f5671g = hHVar.e();
        hHVar2 = hHVar2 == null ? this.f5671g : hHVar2;
        this.f5670f = hHVar2;
        this.f5672h = hFVar;
        this.f5676l = c0188n;
        this.f5665a = new hI();
        b();
        this.f5665a.a(this.f5669e);
        int iA = h.i.a("prefFontSize", com.efiAnalytics.ui.eJ.a(10));
        this.f5665a.c(iA);
        this.f5689y = new com.efiAnalytics.ui.dQ(h.i.f(), "VEAnalyze_");
        this.f5665a.a(this.f5689y);
        this.f5665a.h().a();
        this.f5665a.b(hHVar.i());
        if (c0188n.a(h.g.a().a(h.g.f12243b)) == null) {
            this.f5681q = false;
            c("AFR");
        } else if (r0.f() < 1.1d) {
            h.i.c("calcType", "Gego");
            this.f5682r = false;
        }
        JPanel jPanel = new JPanel();
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel.setLayout(new BorderLayout());
        jPanel2.add("North", this.f5665a);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout(2, 2));
        JButton jButton = new JButton("Run Analysis");
        jButton.addActionListener(new C0738ev(this));
        jPanel3.add("North", jButton);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(2, 0, 2, 2));
        String strE = h.i.e("calcType", "AFR");
        jPanel4.setBorder(BorderFactory.createEtchedBorder(1));
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton jRadioButton = new JRadioButton();
        jRadioButton.setText("Wideband AFR");
        jRadioButton.setToolTipText("For log files based on Wideband O2. Select correct WB sensor from menu.");
        jRadioButton.setSelected(strE.equals("AFR"));
        jRadioButton.addChangeListener(new eG(this));
        buttonGroup.add(jRadioButton);
        jPanel4.add(jRadioButton);
        JRadioButton jRadioButton2 = new JRadioButton();
        jRadioButton2.setText("Narrowband O2");
        jRadioButton2.setToolTipText("Used for log files with standard 0-1V Narrowband O2 sensor");
        jRadioButton2.setEnabled(this.f5681q);
        jRadioButton2.setSelected(strE.equals("Gego"));
        jRadioButton2.addChangeListener(new eQ(this));
        buttonGroup.add(jRadioButton2);
        jPanel4.add(jRadioButton2);
        jPanel3.add(BorderLayout.CENTER, jPanel4);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new GridLayout(2, 0, 2, 2));
        JButton jButton2 = new JButton("Accept New Table");
        jButton2.addActionListener(new eR(this));
        jPanel5.add(jButton2);
        JButton jButton3 = new JButton(ToolWindow.QUIT);
        jButton3.addActionListener(new eS(this));
        jPanel5.add(jButton3);
        jPanel3.add("East", jPanel5);
        jPanel3.setBorder(BorderFactory.createEtchedBorder(1));
        jPanel2.add("South", jPanel3);
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BorderLayout());
        jPanel6.setBorder(BorderFactory.createEtchedBorder(1));
        this.f5677m = new JPanel();
        this.f5677m.setLayout(new BorderLayout());
        C0744fa c0744fa = new C0744fa(this);
        c0744fa.addItemListener(new eT(this));
        String[] strArrA = hz.a(hFVar);
        boolean zA = hz.a(strArrA, c0744fa, new eU(this));
        if (hz.a(strArrA, c0744fa, new eV(this)) && !zA) {
            h.g.a().a(h.g.f12249h, h.g.a().a(h.g.f12250i));
        }
        c0744fa.c(this.f5671g.z());
        JPanel jPanel7 = new JPanel();
        jPanel7.setLayout(new GridLayout(1, 0, 2, 2));
        this.f5680p = new C0618ai(" ");
        this.f5680p.setFont(c0744fa.getFont());
        this.f5680p.a(new eW(this));
        jPanel7.add(this.f5680p);
        jPanel7.add(c0744fa);
        TreeSet<String> treeSet = new TreeSet();
        Iterator it = c0188n.iterator();
        while (it.hasNext()) {
            treeSet.add(((C0184j) it.next()).a());
        }
        JPanel jPanel8 = new JPanel();
        jPanel8.setLayout(new BorderLayout(2, 2));
        jPanel8.add("South", jPanel7);
        this.f5678n = new JPanel();
        this.f5678n.setLayout(new GridLayout(0, 2, 2, 2));
        jPanel8.add(BorderLayout.CENTER, this.f5678n);
        jPanel3.add("South", jPanel8);
        JPanel jPanel9 = new JPanel();
        jPanel9.setLayout(new GridLayout(1, 2, 2, 2));
        JPanel jPanel10 = new JPanel();
        jPanel10.setLayout(new GridLayout(1, 1));
        jPanel10.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), "EGO Sensor Field"));
        JComboBox jComboBox = new JComboBox();
        jPanel10.add(jComboBox);
        jComboBox.setEditable(false);
        jPanel9.add(jPanel10);
        JPanel jPanel11 = new JPanel();
        jPanel11.setLayout(new GridLayout());
        jPanel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), "EGO Correction Field"));
        JComboBox jComboBox2 = new JComboBox();
        jPanel11.add(jComboBox2);
        jComboBox2.setEditable(false);
        jComboBox2.addItem("--- None ---");
        for (String str2 : treeSet) {
            jComboBox.addItem(str2);
            jComboBox2.addItem(str2);
        }
        this.f5675k = h();
        jComboBox2.setSelectedItem(this.f5675k);
        this.f5674j = b(c0188n);
        if (this.f5674j == null || this.f5674j.isEmpty() || !treeSet.contains(this.f5674j)) {
            com.efiAnalytics.ui.bV.d("Did not find expected EGO / AFR Sensor in current data log.\n\nPlease select your preferred AFR / Lambda or O2 field name\nfrom the \"EGO Sensor Field\" dropdown", frame);
        } else {
            jComboBox.setSelectedItem(this.f5674j);
        }
        jComboBox.addActionListener(new C0739ew(this));
        jComboBox2.addActionListener(new C0740ex(this));
        jPanel9.add(jPanel11);
        JPanel jPanel12 = new JPanel();
        jPanel12.setLayout(new FlowLayout(1));
        this.f5673i = new hI();
        this.f5673i.c(iA);
        jPanel12.add(this.f5673i);
        this.f5677m.add(BorderLayout.CENTER, jPanel12);
        hHVar2 = (strE.equals("Gego") || hHVar2 == null) ? this.f5671g : hHVar2;
        c0744fa.b(hHVar2.z());
        b(hHVar2.z());
        JPanel jPanel13 = new JPanel();
        JPanel jPanel14 = new JPanel();
        JPanel jPanel15 = new JPanel();
        jPanel14.setLayout(new GridLayout(0, 2, 2, 2));
        jPanel13.setLayout(new BorderLayout());
        this.f5679o = new JSlider();
        this.f5679o.setMajorTickSpacing(1);
        this.f5679o.setMinimum(0);
        this.f5679o.setMaximum(9);
        this.f5679o.setValue(h.i.a("WBafrOffSet", 2));
        this.f5679o.setPaintTrack(true);
        this.f5679o.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), "Lambda signal delay"));
        this.f5679o.setToolTipText("<html>Compensation for EGO sensor reading delay.<br>Value of 1 will base calculated VE on the AFR 1 records later.<br>1 to 3 works best in most circumstances. Lower RPMs generally have more<br>sensor delay than higher RPMs.");
        this.f5679o.setPaintLabels(true);
        this.f5679o.setSnapToTicks(true);
        this.f5679o.addChangeListener(new C0741ey(this));
        this.f5679o.setEnabled(C1737b.a().a("advancedVeAnalyze"));
        jPanel13.add("North", jPanel15);
        jPanel13.add(BorderLayout.CENTER, jPanel9);
        JButton jButton4 = new JButton("Lambda Delay Table");
        jButton4.addActionListener(new C0742ez(this));
        if (C1737b.a().a("advancedVeAnalyze")) {
            jPanel15.add(jButton4);
        } else {
            jPanel15.add(this.f5679o);
        }
        JComboBox jComboBox3 = new JComboBox();
        jComboBox3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), "Cell Change"));
        jComboBox3.setEditable(false);
        jComboBox3.addItem(new eX(this, "Easy", JPEG.version));
        jComboBox3.addItem(new eX(this, "Normal", "3.0"));
        jComboBox3.addItem(new eX(this, "Hard", "15.0"));
        jComboBox3.addItem(new eX(this, "Very Hard", "100.0"));
        jPanel15.add(jComboBox3);
        jComboBox3.setToolTipText("<html>This impacts the amount of data required to move <br>the cell value or could be looked at as the cell<br>resistance to change. If set to Hard, the initial cell value <br>is more respected and will require stronger analysis data <br>to change.</html>");
        eX eXVar = null;
        eX eXVar2 = new eX(this, "", h.i.e("baseWeight", "3.0"));
        int i2 = 0;
        while (true) {
            if (i2 >= jComboBox3.getItemCount()) {
                break;
            }
            eX eXVar3 = (eX) jComboBox3.getItemAt(i2);
            if (eXVar3.equals(eXVar2)) {
                eXVar = eXVar3;
                break;
            }
            i2++;
        }
        if (eXVar != null) {
            jComboBox3.setSelectedItem(eXVar);
        }
        jComboBox3.addItemListener(new eA(this));
        jComboBox3.setEnabled(C1737b.a().a("advancedVeAnalyze"));
        Dimension dimension = new Dimension(jButton4.getPreferredSize().width, jComboBox3.getPreferredSize().height);
        jButton4.setPreferredSize(dimension);
        jButton4.setMinimumSize(dimension);
        JButton jButton5 = new JButton("Max Value Change: " + i());
        jPanel14.add(jButton5);
        jButton5.addActionListener(new eB(this, jButton5));
        JButton jButton6 = new JButton("Max Percent Change: " + j());
        jPanel14.add(jButton6);
        jButton6.addActionListener(new eC(this, jButton6));
        JButton jButton7 = new JButton("Min RPM: " + h.i.a("veAnalysisMinRPM", "500"));
        jButton7.setToolTipText("<html>Log entries with RPM lower that this will be<br>filtered and not included in VE Analysis.<br>Used to filter non running records or <br>inaccurrate idle AFR readings.</html>");
        jButton7.addActionListener(new eD(this));
        jPanel14.add(jButton7);
        JButton jButton8 = new JButton("Max RPM: " + h.i.a("veAnalysisMaxRPM", "18000"));
        jButton8.setToolTipText("<html>Log entries with RPM above this will be<br>filtered and not included in VE Analysis.<br>Used to filter faulty AFR caused by rev limiter fuel/spark cuts.<br>Leave high value if not needed.</html>");
        jButton8.addActionListener(new eE(this));
        jPanel14.add(jButton8);
        this.f5685u = h.i.a("veAnalyzeYAxisKeyMin", "0");
        JButton jButton9 = new JButton("Min " + this.f5688x + ": " + this.f5685u);
        jButton9.setToolTipText("<html>Log entries with " + this.f5688x + " below this value will be<br>filtered and not included in VE Analysis.<br>Used to perform VE Analyze on part of a log file.<br>Leave at 0 for whole log file</html>");
        jButton9.addActionListener(new eF(this));
        jPanel14.add(jButton9);
        this.f5686v = h.i.a("veAnalyzeYAxisKeyMax", "600");
        JButton jButton10 = new JButton("Max " + this.f5688x + ": " + this.f5686v);
        jButton10.setToolTipText("<html>Log entries with " + this.f5688x + " greater than this will be<br>filtered and not included in VE Analysis.<br>Used to perform VE Analyze on part of a VE table.<br>Leave at number higher than maximum for whole log file.</html>");
        jButton10.addActionListener(new eH(this));
        jPanel14.add(jButton10);
        JButton jButton11 = new JButton("Min Time: Log Start");
        jButton11.setToolTipText("<html>Log entries with Time before that this will be<br>filtered and not included in VE Analysis.<br>Used to perform VE Analyze on part of a VE table.<br>Leave at 0.0 for whole log file</html>");
        jButton11.addActionListener(new eI(this));
        this.f5678n.add(jButton11);
        JButton jButton12 = new JButton("Max Time: Log End");
        jButton12.setToolTipText("<html>Log entries with Time after this will be<br>filtered and not included in VE Analysis.<br>Used to perform VE Analyze on part of a log file.<br>Leave at number higher than maximum Time for whole log file.</html>");
        jButton12.addActionListener(new eJ(this));
        this.f5678n.add(jButton12);
        JButton jButton13 = new JButton("Min " + h.g.a().a(h.g.f12244c) + ": " + h.i.e("minVEAnalysisCLT", "160"));
        jButton13.setToolTipText("<html>Log entries with CLT below this will be<br>filtered and not included in VE Analysis.<br>This is to filter records where warmup enrichment<br>is enabled. This should be 160 or higher in most cases.</html>");
        jButton13.addActionListener(new eK(this));
        jPanel14.add(jButton13);
        JButton jButton14 = new JButton("Custom Filter:" + (h.i.e("customerVEAnalysisFilter", "").equals("") ? "Off" : "On"));
        jButton14.setEnabled(C1737b.a().a("advancedVeAnalyze"));
        if (C1737b.a().a("advancedVeAnalyze")) {
            jButton14.setToolTipText("<html>Performs custom filter<br>as evaluated by MLV math parser.<br>See website for more details.<br>ex. ([MAP]-[MAP-1]) &gt; 20 </html>");
            jButton14.addActionListener(new eL(this));
        } else {
            jButton14.setToolTipText("<html>Disabled on this Version.<br>Performs custom filter<br>as evaluated by MLV math parser.<br>See website for more details.<br>ex. ([MAP]-[MAP-1]) &gt; 20 </html>");
        }
        jPanel14.add(jButton14);
        jPanel13.add("South", jPanel14);
        this.f5677m.add("South", jPanel13);
        a(h.i.a("showVeAnalyzeAdvanced", false));
        jPanel6.add("South", this.f5677m);
        jPanel.add("East", jPanel6);
        getContentPane().add(BorderLayout.CENTER, jPanel);
        getContentPane().add("South", this.f5666b);
        this.f5666b.a(false);
        this.f5687w = new eZ(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f5687w);
        pack();
        setResizable(false);
    }

    private JMenuBar d() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Specialized Options");
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Disable Overrun Filter");
        jCheckBoxMenuItem.setState(h.i.a("veAnalysisDisableOverrunFilter", false));
        jCheckBoxMenuItem.addItemListener(new eM(this));
        jMenu.add((JMenuItem) jCheckBoxMenuItem);
        JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem("Disable Bit6 and Bit 7 Filter");
        jCheckBoxMenuItem2.setState(h.i.a("veAnalysisBit6Bit7Filter", false));
        jCheckBoxMenuItem2.addItemListener(new eN(this));
        jMenu.add((JMenuItem) jCheckBoxMenuItem2);
        JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem("Enable Dead O2 Filter");
        jCheckBoxMenuItem3.setState(h.i.a(h.i.f12340aG, h.i.f12341aH));
        jCheckBoxMenuItem3.setToolTipText("When enabled, records will be filtered when at the min or max value found in the log.");
        jCheckBoxMenuItem3.addItemListener(new eO(this));
        jMenu.add((JMenuItem) jCheckBoxMenuItem3);
        jMenuBar.add(jMenu);
        JMenu jMenu2 = new JMenu("Help");
        JMenuItem jMenuItem = new JMenuItem("VE Analyze");
        jMenuItem.addActionListener(new eP(this));
        jMenu2.add(jMenuItem);
        jMenuBar.add(jMenu2);
        return jMenuBar;
    }

    public void a(String str) throws HeadlessException {
        C0649bm c0649bmA = C0804hg.a().A();
        c0649bmA.a(str);
        c0649bmA.a(this, h.i.f12255b + " help");
    }

    private void a(C0188n c0188n) {
        String strE = h.i.e("minVEAnalysisCLT", "160");
        if (strE.equals("160") || strE.equals("71")) {
            String strA = h.g.a().a(h.g.f12244c);
            try {
                if (c0188n.a(strA).f() > 130.0d) {
                    h.i.c("minVEAnalysisCLT", "160");
                } else {
                    h.i.c("minVEAnalysisCLT", "71");
                }
            } catch (Exception e2) {
                if (c0188n.a("PW In1") == null) {
                    C1733k.a("WARNING!! " + strA + " field not found in log file.\nIt is recommended that Engine Coolant is available for VE Analyze.", this);
                }
            }
        }
    }

    public void a(boolean z2) {
        if (z2) {
            this.f5680p.a("Hide Advanced Settings");
        } else {
            this.f5680p.a("Advanced Settings");
        }
        this.f5677m.setVisible(z2);
        this.f5678n.setVisible(z2);
        h.i.c("showVeAnalyzeAdvanced", "" + z2);
        pack();
    }

    protected void a(JButton jButton) {
        String strA = C1733k.a("{New Min RPM}", true, "Log Records with RPM below this valuewill be filtered from VE Analysis.", true, (Component) this);
        if (strA == null || strA.equals("")) {
            return;
        }
        h.i.c("veAnalysisMinRPM", strA);
        jButton.setText("Min RPM: " + strA);
    }

    protected void b(JButton jButton) {
        String strA = C1733k.a("{New Max RPM}", true, "Log Records with RPM above this value will be filtered from VE Analysis.", true, (Component) this);
        if (strA == null || strA.equals("")) {
            return;
        }
        h.i.c("veAnalysisMaxRPM", strA);
        jButton.setText("Max RPM: " + strA);
    }

    protected void c(JButton jButton) {
        String strA = h.g.a().a("Time");
        String strA2 = C1733k.a("{New Min " + strA + "}", true, "Log Records with " + strA + " before this valuewill be filtered from VE Analysis.", true, (Component) this);
        if (strA2 == null || strA2.equals("")) {
            return;
        }
        this.f5683s = strA2;
        if (this.f5683s.equals(Version.BUILD)) {
            jButton.setText("Min " + strA + ": Log Start");
        } else {
            jButton.setText("Min " + strA + ": " + this.f5683s);
        }
    }

    protected void d(JButton jButton) {
        String strA = h.g.a().a("Time");
        String strA2 = C1733k.a("{New Max Time}", true, "Log Records with Time after this value will be filtered from VE Analysis.", true, (Component) this);
        if (strA2 == null || strA2.equals("")) {
            return;
        }
        this.f5684t = strA2;
        if (this.f5684t.equals("10000000.0")) {
            jButton.setText("Max " + strA + ": Log End");
        } else {
            jButton.setText("Max " + strA + ": " + this.f5684t);
        }
    }

    protected void e(JButton jButton) {
        String strA = C1733k.a("{New Min " + this.f5688x + "}", true, "Log Records with " + this.f5688x + " below this valuewill be filtered from VE Analysis.", true, (Component) this);
        if (strA == null || strA.equals("")) {
            return;
        }
        this.f5685u = strA;
        jButton.setText("Min " + this.f5688x + ": " + this.f5685u);
        h.i.c("veAnalyzeYAxisKeyMin", this.f5685u);
    }

    protected void f(JButton jButton) {
        String strA = C1733k.a("{New Max " + this.f5688x + "}", true, "Log Records with " + this.f5688x + " greater than this value will be filtered from VE Analysis.", true, (Component) this);
        if (strA == null || strA.equals("")) {
            return;
        }
        this.f5686v = strA;
        jButton.setText("Max " + this.f5688x + ": " + this.f5686v);
        h.i.c("veAnalyzeYAxisKeyMax", this.f5686v);
    }

    protected void g(JButton jButton) {
        String strA = h.g.a().a(h.g.f12244c);
        String strA2 = C1733k.a("{New Minimum " + strA + " (Coolant temperature)}", true, "Log Records with " + strA + " less than this value\nwill be filtered from VE Analysis. Fuel enrichments during engine warm up \nwill negatively impact results. Air cooled or other special setups may need to \nlower this, otherwise normally 160" + bH.S.a() + "F to 180" + bH.S.a() + "F or 71" + bH.S.a() + "C to 82" + bH.S.a() + "C", true, (Component) this);
        if (strA2 == null || strA2.equals("")) {
            return;
        }
        h.i.c("minVEAnalysisCLT", strA2);
        jButton.setText("Min " + strA + ": " + h.i.e("minVEAnalysisCLT", "160"));
    }

    protected void h(JButton jButton) {
        String strA = C1733k.a(null, "{Custom filter condition}", false, "Performs custom filter as evaluated by MLV math parser.\nSee website for more details. \nex. abs([MAP]-[MAP-1]) > 20 \nfilters any record where MAP change is greater than 20 kpa from previous record.\nTo deactivate custom filter, press cancel or clear and press Ok.", true, this, new eY(this), null);
        if (strA == null || strA.equals("")) {
            h.i.d("customerVEAnalysisFilter");
        }
        h.i.c("customerVEAnalysisFilter", strA);
        jButton.setText("Custom Filter:" + (strA.equals("") ? "Off" : "On"));
    }

    protected hH b(hH hHVar) {
        hH hHVar2 = new hH();
        hHVar2.a(hHVar.getRowCount(), hHVar.getColumnCount());
        hHVar2.a(hHVar.j());
        hH hHVar3 = (hH) C1677fh.a(hHVar, hHVar2);
        hHVar3.d(hHVar.v());
        hHVar3.a(hHVar.g());
        hHVar3.q();
        return hHVar3;
    }

    public void b(boolean z2) {
        if (this.f5671g != null && this.f5671g.r()) {
            this.f5671g.f();
        }
        if (z2) {
            g();
        }
        setVisible(false);
        if (this.f5687w != null) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f5687w);
        }
        Iterator it = this.f5690z.iterator();
        while (it.hasNext()) {
            ((InterfaceC1565bc) it.next()).close();
        }
        dispose();
        System.gc();
    }

    public void a(InterfaceC1565bc interfaceC1565bc) {
        this.f5690z.add(interfaceC1565bc);
    }

    public void b(String str) {
        if (this.f5672h.b(str) != null) {
            this.f5670f = this.f5672h.b(str);
        }
        if (str.equals(this.f5671g.z())) {
            this.f5670f = this.f5671g;
        }
        a(this.f5670f.z(), this.f5670f);
        this.f5665a.b("Ve: " + this.f5669e.z());
        this.f5665a.a("<html>Perform Analysis on <b>" + this.f5669e.z() + "</b>, using AFR table: <b>" + this.f5670f.z() + "</b><html>");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        h.i.c("calcType", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() throws NumberFormatException {
        int i2 = 1;
        this.f5670f = (hH) this.f5673i.g();
        if (h.i.e("calcType", "AFR").equals("Gego")) {
            if (this.f5676l.a(this.f5674j) != null && r0.f() > 1.2d && !com.efiAnalytics.ui.bV.a("You are set to Narrowband Mode, but the selected EGO Sensor Field appears to be a \nWideband based on the O2 sensor voltages.\n\nAre you sure you want to process this file in Narrowband mode?", (Component) this, true)) {
                return;
            } else {
                i2 = 2;
            }
        }
        this.f5668d.a(Double.parseDouble(h.i.e("baseWeight", "3.0")));
        C0587E[] c0587eArrA = a();
        try {
            C1701s c1701sA = null;
            if (C1737b.a().a("advancedVeAnalyze")) {
                c1701sA = bt.bO.a().a(this.f5665a.g(), this.f5665a.getName());
            }
            this.f5668d.a(this.f5669e, this.f5670f, c1701sA, this.f5676l, i2, this.f5674j, this.f5675k, c0587eArrA);
        } catch (V.g e2) {
            C1733k.a(e2.getMessage(), this);
            Logger.getLogger(C0737eu.class.getName()).log(Level.WARNING, "Failed to get Lambda Delay Table", (Throwable) e2);
        } catch (V.h e3) {
            C1733k.a(e3.getMessage(), this);
        }
    }

    @Override // ao.InterfaceC0609a
    public void a(C1562b[][] c1562bArr, int i2, int i3) {
        if (i2 % 50 == 0 || i2 == i3) {
            this.f5666b.b(i2 / i3);
        }
        this.f5665a.h().m();
    }

    @Override // ao.InterfaceC0609a
    public void a(C1562b[][] c1562bArr, hH hHVar, int i2) {
        this.f5665a.repaint();
        double d2 = (i2 * 100.0d) / this.f5676l.d();
        String str = (("<html><body><br><b>VE Analysis Summary</b><br>Total log records: " + this.f5676l.d() + "<br>") + "Filtered log records: " + i2 + " (" + C1733k.a(d2) + "&#37;)<br>") + "Log records used: " + (this.f5676l.d() - i2) + "<br>";
        int i3 = 0;
        double d3 = 0.0d;
        int i4 = 0;
        double dK = 0.0d;
        int length = c1562bArr.length * c1562bArr[0].length;
        int iMax = Math.max(1, hHVar.g());
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(iMax);
        double dPow = 5.0d * Math.pow(0.1d, iMax);
        for (int length2 = c1562bArr.length - 1; length2 >= 0; length2--) {
            for (int i5 = 0; i5 < c1562bArr[length2].length; i5++) {
                if (c1562bArr[length2][i5].j() > 0) {
                    i4++;
                    dK += c1562bArr[length2][i5].k();
                    double dAbs = Math.abs(c1562bArr[length2][i5].i().doubleValue() - Double.parseDouble(c1562bArr[length2][i5].b()));
                    if (dAbs > dPow) {
                        i3++;
                        if (dAbs > d3) {
                            d3 = dAbs;
                        }
                    }
                }
            }
        }
        double d4 = 100.0d * (i4 / length);
        double d5 = 100.0d * (i3 / length);
        String str2 = (((((str + "<br>") + "Total table cells: " + length + "<br>") + "Cells Analyzed: " + i4 + " (" + C1733k.a(d4) + "&#37;)<br>") + "Average cell weight: " + C1733k.a(dK / i4) + "<br>") + "Cell values altered more than " + numberFormat.format(dPow) + " : " + i3 + " (" + C1733k.a(d5) + "&#37;)<br>") + "Max Cell Value Change: " + numberFormat.format(d3) + "<br>";
        if (d2 > 80.0d) {
            str2 = (((str2 + " <br>") + "<b><font color=\"red\">!!!!!!!! WARNING !!!!!!!!</font></b><br>") + d2 + "&#37; of your log was filtered.<br>") + "Check your filter settings in Advanced Settings.<br>";
            C0587E[] c0587eArrC = this.f5668d.c();
            if (c0587eArrC != null) {
                int iA = 0;
                String strB = "";
                for (int i6 = 0; i6 < c0587eArrC.length; i6++) {
                    if (c0587eArrC[i6].a() > iA) {
                        iA = c0587eArrC[i6].a();
                        strB = c0587eArrC[i6].b();
                    }
                }
                if (strB.isEmpty()) {
                    strB = "AFR Value equal Min AFR Value";
                    iA = i2;
                }
                str2 = str2 + "The settings for Filter <br>\"" + strB + "\"<br> caused " + iA + " records to be filtered.<br>";
            }
        }
        C1733k.a(str2 + "<br></body></html>", this);
    }

    @Override // ao.InterfaceC0609a
    public void a(V.h hVar) {
        C1733k.a(hVar.getMessage(), this);
        hVar.printStackTrace();
    }

    @Override // ao.InterfaceC0609a
    public void a(hH hHVar) {
        this.f5665a.h().setModel(hHVar);
        hHVar.q();
    }

    public void a(hM hMVar) {
        this.f5667c.add(hMVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        try {
            com.efiAnalytics.tuningwidgets.panels.aP aPVar = new com.efiAnalytics.tuningwidgets.panels.aP(null, bt.bO.a().a(this.f5665a.g(), this.f5665a.getName()), this.f5665a.getName());
            a(aPVar);
            aPVar.setPreferredSize(new Dimension(com.efiAnalytics.ui.eJ.a(290), com.efiAnalytics.ui.eJ.a(170)));
            com.efiAnalytics.ui.bV.a(aPVar, this, "Lambda Delay (ms)", aPVar);
        } catch (V.g e2) {
            bH.C.a("Can not show Lambda delay Table.", e2, this);
        }
    }

    private void g() {
        Iterator it = this.f5667c.iterator();
        while (it.hasNext()) {
            hM hMVar = (hM) it.next();
            hH hHVar = (hH) this.f5665a.h().getModel();
            hMVar.a(hHVar.z(), hHVar);
            if (this.f5670f != null && !this.f5670f.z().startsWith(Action.DEFAULT) && this.f5670f.p() && this.f5673i.i()) {
                hMVar.a(this.f5670f.z(), this.f5670f);
            }
        }
    }

    public C0587E[] a() {
        ArrayList arrayList = new ArrayList();
        C0587E c0587e = new C0587E();
        c0587e.b(4);
        c0587e.a("RPM");
        c0587e.b(h.i.a("veAnalysisMinRPM", "500"));
        c0587e.d("Min RPM");
        arrayList.add(c0587e);
        C0587E c0587e2 = new C0587E();
        c0587e2.b(2);
        c0587e2.a("RPM");
        c0587e2.b(h.i.a("veAnalysisMaxRPM", "18000"));
        c0587e2.d("Max RPM");
        arrayList.add(c0587e2);
        C0587E c0587e3 = new C0587E();
        c0587e3.b(32);
        c0587e3.a("Engine");
        c0587e3.a(16);
        c0587e3.d("Accel enrich Flag");
        arrayList.add(c0587e3);
        if (!h.i.a("veAnalysisBit6Bit7Filter", false)) {
            C0587E c0587e4 = new C0587E();
            c0587e4.b(32);
            c0587e4.a("Engine");
            c0587e4.a(64);
            c0587e4.d("MAP Accel enrich");
            arrayList.add(c0587e4);
        }
        C0587E c0587e5 = new C0587E();
        c0587e5.b(32);
        c0587e5.a("Engine");
        c0587e5.a(4);
        c0587e5.d("ASE Flag");
        arrayList.add(c0587e5);
        C0587E c0587e6 = new C0587E();
        c0587e6.b(4);
        String strA = h.g.a().a(h.g.f12244c);
        c0587e6.a(strA);
        c0587e6.a(Float.parseFloat(h.i.e("minVEAnalysisCLT", "160")));
        c0587e6.d("Min " + strA);
        arrayList.add(c0587e6);
        String strA2 = h.g.a().a("Time");
        C0587E c0587e7 = new C0587E();
        c0587e7.b(4);
        c0587e7.a(strA2);
        c0587e7.b(this.f5683s);
        c0587e7.d("Min " + strA2);
        arrayList.add(c0587e7);
        C0587E c0587e8 = new C0587E();
        c0587e8.b(2);
        c0587e8.a(strA2);
        c0587e8.b(this.f5684t);
        c0587e8.d("Max " + strA2);
        arrayList.add(c0587e8);
        C0587E c0587e9 = new C0587E();
        c0587e9.b(4);
        c0587e9.a(this.f5688x);
        c0587e9.b(this.f5685u);
        c0587e9.d("Min " + this.f5688x);
        arrayList.add(c0587e9);
        C0587E c0587e10 = new C0587E();
        c0587e10.b(2);
        c0587e10.a(this.f5688x);
        c0587e10.b(this.f5686v);
        c0587e10.d("Max " + this.f5688x);
        arrayList.add(c0587e10);
        if (!h.i.a("veAnalysisDisableOverrunFilter", false)) {
            C0587E c0587e11 = new C0587E();
            String strA3 = h.g.a().a(h.g.f12248g);
            c0587e11.b(1);
            c0587e11.a(strA3);
            c0587e11.a(0);
            c0587e11.d(strA3 + " = 0ms.");
            if (strA3 != null && strA3.trim().length() > 0) {
                arrayList.add(c0587e11);
            }
        }
        String strE = h.i.e("customerVEAnalysisFilter", "");
        if (!strE.equals("")) {
            C0587E c0587e12 = new C0587E();
            c0587e12.b(64);
            c0587e12.c(strE);
            c0587e12.d("Customer Filter " + strE);
            arrayList.add(c0587e12);
        }
        if (this.f5676l.a("dMAP_Corr") != null) {
            C0587E c0587e13 = new C0587E();
            c0587e13.b(64);
            c0587e13.c("Math.abs([dMAP_Corr]) > 0.05");
            c0587e13.d("Accel Enrich Filter ");
            arrayList.add(c0587e13);
        }
        String strB = b(this.f5676l);
        if (!bH.W.c(strB) && strB.toLowerCase().contains("lambda") && this.f5676l.a(strB) != null) {
            C0587E c0587e14 = new C0587E();
            c0587e14.b(64);
            c0587e14.c("[" + strB + "] > 1.5 || [" + strB + "] < 0.4 ");
            c0587e14.d(strB + " Out Of Range");
            arrayList.add(c0587e14);
            bH.C.c("Added filter Lambda Out of range filter based on " + strB);
        }
        C0587E c0587e15 = new C0587E();
        String strA4 = h.g.a().a(h.g.f12243b);
        c0587e15.b(8);
        c0587e15.a(strA4);
        c0587e15.a(0.0f);
        c0587e15.d("No O2 Reading");
        arrayList.add(c0587e15);
        if (this.f5676l.a("Engine") != null && !h.i.a("veAnalysisBit6Bit7Filter", false)) {
            C0587E c0587e16 = new C0587E();
            c0587e16.b(64);
            c0587e16.c("([Engine] | 64 == 64) || ([Engine-1] | 64 == 64) || ([Engine] | 128 == 128) || ([Engine-1] | 128 == 128)");
            c0587e16.d("Accel Enrich Filter ");
            arrayList.add(c0587e16);
        }
        C0587E[] c0587eArr = new C0587E[arrayList.size()];
        for (int i2 = 0; i2 < c0587eArr.length; i2++) {
            c0587eArr[i2] = (C0587E) arrayList.get(i2);
        }
        return c0587eArr;
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        if (this.f5676l == null || !isVisible() || this.f5665a == null || !this.f5665a.isVisible()) {
            return;
        }
        String string = "";
        String string2 = "";
        try {
            string = Float.toString(this.f5676l.a(this.f5669e.v()).d(i2));
            string2 = Float.toString(this.f5676l.a(this.f5669e.w()).d(i2));
            this.f5665a.h().a(string, string2);
            if (this.f5673i != null) {
                BinTableView binTableViewH = this.f5673i.h();
                try {
                    string = Float.toString(this.f5676l.a(this.f5673i.g().v()).d(i2));
                    string2 = Float.toString(this.f5676l.a(this.f5673i.g().w()).d(i2));
                } catch (Exception e2) {
                }
                binTableViewH.a(string, string2);
            }
        } catch (Exception e3) {
            System.out.println("NewVeDialog::yVal=" + string + ", rpm=" + string2);
            e3.printStackTrace();
        }
    }

    public void a(String str, hH hHVar) {
        h.i.c("lastVeAnalysisAfrTable", hHVar.z());
        this.f5673i.a(hHVar);
        this.f5673i.h().getTableHeader().setToolTipText("RPM");
        this.f5673i.f().setToolTipText(hHVar.v());
        this.f5673i.b(str);
        pack();
        this.f5673i.k();
    }

    public C1562b[][] b() {
        double d2;
        double d3;
        C1589c c1589c = new C1589c();
        try {
            d2 = Double.parseDouble(h.i.a("veAnalysisWeightThreshold", Version.BUILD));
            System.out.println("weightThreshold set to:" + d2);
        } catch (Exception e2) {
            d2 = 0.0d;
            System.out.println("Error retrieving veAnalysisWeightThreshold from properties file. using 0.0");
        }
        try {
            d3 = Double.parseDouble(h.i.a("veAnalysisWindowThreshold", Version.BUILD));
        } catch (Exception e3) {
            d3 = 0.0d;
            System.out.println("Error retrieving veAnalysisWindowThreshold from properties file. using 0.0");
        }
        c1589c.c(d3);
        c1589c.a(d2);
        return this.f5669e.a(this.f5670f, c1589c);
    }

    protected void c() throws HeadlessException {
        C0649bm c0649bmA = C0804hg.a().A();
        String strA = C0648bl.a(C0648bl.f5416b);
        c0649bmA.a(strA);
        System.out.println("url:" + strA);
        c0649bmA.a(this, h.i.f12255b + " help");
    }

    private String b(C0188n c0188n) {
        String strA = h.g.a().a(h.g.f12249h);
        String strE = h.i.e(h.i.f12308ac, strA);
        String strA2 = h.g.a().a(h.g.f12243b);
        return c0188n.a(strE) != null ? strE : c0188n.a(strA) != null ? strA : this.f5676l.a("AFR(WBO2)") != null ? "AFR(WBO2)" : this.f5676l.a("Lambda") != null ? "Lambda" : this.f5676l.a(strA2) != null ? strA2 : "";
    }

    private String h() {
        return h.i.e(h.i.f12309ad, h.g.a().a(h.g.f12246e));
    }

    private String i() {
        return h.i.a("veAnalysisMaxValChange", "50");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(JButton jButton) {
        String strA = C1733k.a("{New Maximum Cell Value Change}", true, "This is the most a cell will be changed in valuefrom the starting value", true, (Component) this);
        if (strA == null || strA.equals("")) {
            return;
        }
        h.i.c("veAnalysisMaxValChange", strA);
        jButton.setText("Max Value Change: " + strA);
    }

    private String j() {
        return h.i.a("veAnalysisMaxPercentChange", "50");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j(JButton jButton) {
        String strA = C1733k.a("{New Maximum Cell Percent Change}", true, "This is the most a cell will be changed in percent from the starting value", true, (Component) this);
        if (strA == null || strA.equals("")) {
            return;
        }
        h.i.c("veAnalysisMaxPercentChange", strA);
        jButton.setText("Max Percent Change: " + strA);
    }
}
