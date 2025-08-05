package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter;
import com.efiAnalytics.ui.C1605cp;
import com.efiAnalytics.ui.C1606cq;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import r.C1807j;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.ad, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/ad.class */
public class C1391ad extends JPanel implements InterfaceC1390ac, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    Gauge f9463a;

    /* renamed from: b, reason: collision with root package name */
    JDialog f9464b = null;

    /* renamed from: c, reason: collision with root package name */
    boolean f9465c;

    public C1391ad(Gauge gauge) {
        this.f9463a = null;
        this.f9465c = false;
        this.f9463a = gauge;
        this.f9465c = gauge.isRunDemo();
        c();
    }

    private void c() {
        aF aFVar = new aF(this);
        setLayout(new GridLayout(0, 2));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        jPanel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Output Channel / Style"));
        jPanel2.setLayout(new GridLayout(0, 1));
        JPanel jPanel3 = new JPanel();
        jPanel2.add(jPanel3);
        jPanel3.add(new JLabel("OutputChannel"));
        G.R rC = G.T.a().c();
        JComboBox jComboBox = new JComboBox();
        jComboBox.setEditable(false);
        jComboBox.addItem("");
        for (Object obj : bH.W.a((Object[]) rC.s())) {
            jComboBox.addItem(obj);
        }
        jComboBox.setSelectedItem(this.f9463a.getOutputChannel());
        jComboBox.addActionListener(new C1392ae(this));
        jPanel3.add(jComboBox);
        JPanel jPanel4 = new JPanel();
        jPanel4.add(new JLabel("Gauge Style"));
        GaugePainter[] gaugePainterArrA = com.efiAnalytics.apps.ts.dashboard.renderers.e.a();
        JComboBox jComboBox2 = new JComboBox();
        jComboBox2.setEditable(false);
        for (GaugePainter gaugePainter : gaugePainterArrA) {
            jComboBox2.addItem(new aE(this, gaugePainter));
        }
        jComboBox2.setSelectedItem(new aE(this, this.f9463a.getGaugePainter()));
        jComboBox2.addActionListener(new ap(this));
        jPanel4.add(jComboBox2);
        jPanel2.add(jPanel4);
        jPanel.add("North", jPanel2);
        JPanel jPanel5 = new JPanel();
        jPanel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Gauge Angles"));
        jPanel5.setLayout(new GridLayout(0, 1));
        JSlider jSliderA = a(this.f9463a.getFaceAngle(), 0, 360, "Face Angle");
        jSliderA.setPaintLabels(true);
        jSliderA.setSnapToTicks(true);
        jSliderA.addChangeListener(new ax(this));
        jPanel5.add(jSliderA);
        JSlider jSliderA2 = a(this.f9463a.getFaceAngle(), 0, 360, "Face Start Angle");
        jSliderA2.setPaintLabels(true);
        jSliderA2.setSnapToTicks(true);
        jSliderA2.addChangeListener(new ay(this));
        jPanel5.add(jSliderA2);
        JSlider jSliderA3 = a(this.f9463a.getSweepBeginDegree(), 0, 360, "Needle Start Angle");
        jSliderA3.setPaintLabels(true);
        jSliderA3.setSnapToTicks(true);
        jSliderA3.addChangeListener(new az(this));
        jPanel5.add(jSliderA3);
        JSlider jSliderA4 = a(this.f9463a.getSweepAngle(), 0, 360, "Sweep Angle");
        jSliderA4.setPaintLabels(true);
        jSliderA4.setSnapToTicks(true);
        jSliderA4.addChangeListener(new aA(this));
        jPanel5.add(jSliderA4);
        jPanel.add(BorderLayout.CENTER, jPanel5);
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BorderLayout());
        JPanel jPanel7 = new JPanel();
        jPanel7.setLayout(new GridLayout(0, 1));
        jPanel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Border Settings"));
        JSlider jSliderA5 = a(this.f9463a.getBorderWidth(), 0, 25, "Border Thickness");
        jSliderA5.setPaintLabels(true);
        jSliderA5.setSnapToTicks(true);
        jSliderA5.addChangeListener(new aB(this));
        jPanel7.add(jSliderA5);
        jPanel6.add("North", jPanel7);
        JPanel jPanel8 = new JPanel();
        jPanel8.setLayout(new GridLayout(0, 1));
        JSlider jSliderA6 = a(this.f9463a.getFontSizeAdjustment(), -6, 6, "Font Size");
        jSliderA6.setPaintLabels(true);
        jSliderA6.setSnapToTicks(true);
        jSliderA6.setValue(this.f9463a.getFontSizeAdjustment());
        jSliderA6.addChangeListener(new aC(this));
        jPanel8.add(jSliderA6);
        JComboBox jComboBox3 = new JComboBox();
        jComboBox3.setEditable(false);
        jComboBox3.addItem("");
        for (String str : b()) {
            jComboBox3.addItem(str);
        }
        jComboBox3.setSelectedItem(this.f9463a.getFontFamily());
        jComboBox3.addActionListener(new aD(this));
        JPanel jPanel9 = new JPanel();
        jPanel9.add(jComboBox3);
        jPanel8.add(jPanel9);
        jPanel6.add("South", jPanel8);
        jPanel.add("South", jPanel6);
        add(jPanel);
        Font font = new Font("Arial Unicode MS", 1, eJ.a(12));
        JPanel jPanel10 = new JPanel();
        jPanel10.setLayout(new BorderLayout());
        JPanel jPanel11 = new JPanel();
        jPanel11.setLayout(new GridLayout(0, 2));
        jPanel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Title / units"));
        JLabel jLabel = new JLabel("Gauge Title");
        jLabel.setHorizontalAlignment(4);
        jPanel11.add(jLabel);
        JTextField jTextField = new JTextField(this.f9463a.title());
        jTextField.addFocusListener(aFVar);
        jTextField.addKeyListener(new C1393af(this));
        jPanel11.add(jTextField);
        JLabel jLabel2 = new JLabel("Gauge Units");
        jLabel2.setHorizontalAlignment(4);
        jPanel11.add(jLabel2);
        JTextField jTextField2 = new JTextField(this.f9463a.units());
        jTextField2.addFocusListener(aFVar);
        jTextField2.addKeyListener(new C1394ag(this));
        jPanel11.add(jTextField2);
        JLabel jLabel3 = new JLabel("Value");
        jLabel3.setHorizontalAlignment(4);
        jPanel11.add(jLabel3);
        JTextField jTextField3 = new JTextField(this.f9463a.getValue() + "");
        jTextField3.addFocusListener(aFVar);
        jTextField3.addKeyListener(new C1395ah(this));
        jPanel11.add(jTextField3);
        jPanel10.add("North", jPanel11);
        JPanel jPanel12 = new JPanel();
        jPanel12.setLayout(new GridLayout(0, 1, 4, 4));
        jPanel12.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Gauge Colors"));
        C1605cp c1605cp = new C1605cp("Border Color");
        c1605cp.setFont(font);
        c1605cp.a(this.f9463a.getTrimColor());
        c1605cp.b(20);
        c1605cp.a(new C1396ai(this));
        jPanel12.add(c1605cp);
        C1605cp c1605cp2 = new C1605cp("Face Color");
        c1605cp2.setFont(font);
        c1605cp2.a(this.f9463a.getBackColor());
        c1605cp2.b(20);
        c1605cp2.a(new C1397aj(this));
        jPanel12.add(c1605cp2);
        C1605cp c1605cp3 = new C1605cp("Font Color");
        c1605cp3.setFont(font);
        c1605cp3.a(this.f9463a.getFontColor());
        c1605cp3.b(20);
        c1605cp3.a(new C1398ak(this));
        jPanel12.add(c1605cp3);
        C1605cp c1605cp4 = new C1605cp("Needle Color");
        c1605cp4.setFont(font);
        c1605cp4.a(this.f9463a.getNeedleColor());
        c1605cp4.b(20);
        c1605cp4.a(new C1399al(this));
        jPanel12.add(c1605cp4);
        C1605cp c1605cp5 = new C1605cp("Warning Color");
        c1605cp5.setFont(font);
        c1605cp5.a(this.f9463a.getWarnColor());
        c1605cp5.b(20);
        c1605cp5.a(new C1400am(this));
        jPanel12.add(c1605cp5);
        C1605cp c1605cp6 = new C1605cp("Critical Limits Color");
        c1605cp6.setFont(font);
        c1605cp6.a(this.f9463a.getCriticalColor());
        c1605cp6.b(20);
        c1605cp6.a(new C1401an(this));
        jPanel12.add(c1605cp6);
        jPanel10.add(BorderLayout.CENTER, jPanel12);
        JPanel jPanel13 = new JPanel();
        jPanel13.setLayout(new GridLayout(0, 2));
        jPanel13.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Limits and Thresholds"));
        JLabel jLabel4 = new JLabel("Minimum Value");
        jLabel4.setHorizontalAlignment(4);
        jPanel13.add(jLabel4);
        JTextField jTextField4 = new JTextField(bH.W.a(this.f9463a.min()));
        jTextField4.addFocusListener(aFVar);
        jTextField4.addKeyListener(new C1402ao(this));
        jPanel13.add(jTextField4);
        JLabel jLabel5 = new JLabel("Maximum Value");
        jLabel5.setHorizontalAlignment(4);
        jPanel13.add(jLabel5);
        JTextField jTextField5 = new JTextField(bH.W.a(this.f9463a.max()));
        jTextField5.addFocusListener(aFVar);
        jTextField5.addKeyListener(new aq(this));
        jPanel13.add(jTextField5);
        JLabel jLabel6 = new JLabel("Low Critical");
        jLabel6.setHorizontalAlignment(4);
        jPanel13.add(jLabel6);
        JTextField jTextField6 = new JTextField(bH.W.a(this.f9463a.lowCritical()));
        jTextField6.addFocusListener(aFVar);
        jTextField6.addKeyListener(new ar(this));
        jPanel13.add(jTextField6);
        JLabel jLabel7 = new JLabel("Low Warning");
        jLabel7.setHorizontalAlignment(4);
        jPanel13.add(jLabel7);
        JTextField jTextField7 = new JTextField(bH.W.a(this.f9463a.lowWarning()));
        jTextField7.addFocusListener(aFVar);
        jTextField7.addKeyListener(new as(this));
        jPanel13.add(jTextField7);
        JLabel jLabel8 = new JLabel("High Warning");
        jLabel8.setHorizontalAlignment(4);
        jPanel13.add(jLabel8);
        JTextField jTextField8 = new JTextField(bH.W.a(this.f9463a.highWarning()));
        jTextField8.addFocusListener(aFVar);
        jTextField8.addKeyListener(new at(this));
        jPanel13.add(jTextField8);
        JLabel jLabel9 = new JLabel("High Critical");
        jLabel9.setHorizontalAlignment(4);
        jPanel13.add(jLabel9);
        JTextField jTextField9 = new JTextField(bH.W.a(this.f9463a.highCritical()));
        jTextField9.addFocusListener(aFVar);
        jTextField9.addKeyListener(new au(this));
        jPanel13.add(jTextField9);
        JPanel jPanel14 = new JPanel();
        JTextField jTextField10 = new JTextField(this.f9463a.getValueDigitsVP().toString());
        jTextField10.addFocusListener(aFVar);
        jTextField10.addKeyListener(new av(this));
        jPanel14.add(jTextField10);
        JPanel jPanel15 = new JPanel();
        jPanel15.setLayout(new BorderLayout());
        jPanel15.add(BorderLayout.CENTER, jPanel13);
        jPanel15.add("South", jPanel14);
        jPanel10.add("South", jPanel15);
        add(jPanel10);
    }

    protected Gauge a() {
        return this.f9463a;
    }

    private JSlider a(int i2, int i3, int i4, String str) {
        JSlider jSlider = new JSlider();
        jSlider.setMajorTickSpacing(10 / (i4 - i3));
        jSlider.setMinimum(i3);
        jSlider.setMaximum(i4);
        jSlider.setValue(i2);
        jSlider.setPaintTrack(true);
        jSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), str));
        return jSlider;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1390ac
    public void a(AbstractC1420s abstractC1420s) {
        abstractC1420s.repaint();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f9463a.setRunDemo(this.f9465c);
    }

    public void a(Component component) {
        this.f9464b = new JDialog(bV.a(component), "Adjust Gauge Settings");
        this.f9464b.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton("Close");
        jButton.addActionListener(new aw(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        jPanel.add(jButton);
        this.f9464b.add("South", jPanel);
        this.f9464b.pack();
        this.f9464b.setLocationRelativeTo(this.f9463a);
        this.f9464b.setLocation(this.f9464b.getX() + (this.f9464b.getWidth() / 2) + (this.f9463a.getWidth() / 2), this.f9464b.getY());
        this.f9464b.setVisible(true);
    }

    public static String[] b() {
        C1606cq.a().a(C1807j.F());
        return C1606cq.a().c();
    }
}
