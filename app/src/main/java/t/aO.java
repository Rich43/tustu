package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.Cdo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aO.class */
public class aO extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JTextField f13750a;

    /* renamed from: b, reason: collision with root package name */
    JTextField f13751b;

    /* renamed from: c, reason: collision with root package name */
    JTextField f13752c;

    /* renamed from: d, reason: collision with root package name */
    JTextField f13753d;

    /* renamed from: e, reason: collision with root package name */
    JTextField f13754e;

    /* renamed from: g, reason: collision with root package name */
    JTextField f13755g;

    /* renamed from: h, reason: collision with root package name */
    Cdo f13756h;

    /* renamed from: i, reason: collision with root package name */
    Cdo f13757i;

    /* renamed from: j, reason: collision with root package name */
    JPanel f13758j;

    /* renamed from: k, reason: collision with root package name */
    String f13759k;

    /* renamed from: l, reason: collision with root package name */
    String f13760l;

    /* renamed from: m, reason: collision with root package name */
    String f13761m;

    /* renamed from: n, reason: collision with root package name */
    String f13762n;

    /* renamed from: o, reason: collision with root package name */
    String f13763o;

    /* renamed from: p, reason: collision with root package name */
    String f13764p;

    public aO(Window window, C1836ai c1836ai) {
        super(window, C1818g.b("Gauge Values"));
        this.f13758j = new JPanel();
        this.f13759k = null;
        this.f13760l = null;
        this.f13761m = null;
        this.f13762n = null;
        this.f13763o = null;
        this.f13764p = null;
        a(c1836ai);
        bd bdVar = new bd(this);
        this.f13758j.setLayout(new GridLayout(0, 2));
        this.f13758j.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), C1818g.b("Limits and Thresholds")));
        JLabel jLabel = new JLabel(C1818g.b("Current Value"));
        jLabel.setHorizontalAlignment(4);
        this.f13758j.add(jLabel);
        this.f13756h = new Cdo();
        this.f13756h.addFocusListener(bdVar);
        this.f13756h.addKeyListener(new aP(this));
        this.f13758j.add(this.f13756h);
        JLabel jLabel2 = new JLabel(C1818g.b("Historical Peak Value"));
        jLabel2.setHorizontalAlignment(4);
        this.f13758j.add(jLabel2);
        this.f13757i = new Cdo();
        this.f13757i.addFocusListener(bdVar);
        this.f13757i.addKeyListener(new aV(this));
        this.f13758j.add(this.f13757i);
        JLabel jLabel3 = new JLabel(C1818g.b("Minimum Value"));
        jLabel3.setHorizontalAlignment(4);
        this.f13758j.add(jLabel3);
        this.f13750a = new JTextField();
        this.f13750a.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f13750a.addFocusListener(bdVar);
        this.f13750a.addKeyListener(new aW(this));
        this.f13750a.addFocusListener(new aX(this));
        this.f13758j.add(this.f13750a);
        JLabel jLabel4 = new JLabel(C1818g.b("Maximum Value"));
        jLabel4.setHorizontalAlignment(4);
        this.f13758j.add(jLabel4);
        this.f13751b = new JTextField();
        this.f13751b.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f13751b.addFocusListener(bdVar);
        this.f13751b.addKeyListener(new aY(this));
        this.f13751b.addFocusListener(new aZ(this));
        this.f13758j.add(this.f13751b);
        JLabel jLabel5 = new JLabel(C1818g.b("Low Critical"));
        jLabel5.setHorizontalAlignment(4);
        this.f13758j.add(jLabel5);
        this.f13753d = new JTextField();
        this.f13753d.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f13753d.addFocusListener(bdVar);
        this.f13753d.addKeyListener(new ba(this));
        this.f13753d.addFocusListener(new bb(this));
        this.f13758j.add(this.f13753d);
        JLabel jLabel6 = new JLabel(C1818g.b("Low Warning"));
        jLabel6.setHorizontalAlignment(4);
        this.f13758j.add(jLabel6);
        this.f13752c = new JTextField();
        this.f13752c.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f13752c.addFocusListener(bdVar);
        this.f13752c.addKeyListener(new bc(this));
        this.f13752c.addFocusListener(new aQ(this));
        this.f13758j.add(this.f13752c);
        JLabel jLabel7 = new JLabel(C1818g.b("High Warning"));
        jLabel7.setHorizontalAlignment(4);
        this.f13758j.add(jLabel7);
        this.f13754e = new JTextField();
        this.f13754e.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f13754e.addFocusListener(bdVar);
        this.f13754e.addKeyListener(new aR(this));
        this.f13754e.addFocusListener(new aS(this));
        this.f13758j.add(this.f13754e);
        JLabel jLabel8 = new JLabel(C1818g.b("High Critical"));
        jLabel8.setHorizontalAlignment(4);
        this.f13758j.add(jLabel8);
        this.f13755g = new JTextField();
        this.f13755g.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f13755g.addFocusListener(bdVar);
        this.f13755g.addKeyListener(new aT(this));
        this.f13755g.addFocusListener(new aU(this));
        this.f13758j.add(this.f13755g);
        add(BorderLayout.CENTER, this.f13758j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double a(String str) {
        Double dA = bH.H.a((Object) str);
        if (dA == null) {
            return 0.0d;
        }
        return dA.doubleValue();
    }

    public void e(ArrayList arrayList) {
        Gauge gaugeB = b(arrayList);
        if (gaugeB == null) {
            C1685fp.a((Component) a(), false);
            return;
        }
        C1685fp.a((Component) a(), true);
        this.f13759k = gaugeB.getMinVP().toString();
        this.f13760l = gaugeB.getMaxVP().toString();
        this.f13761m = gaugeB.getLowCriticalVP().toString();
        this.f13762n = gaugeB.getLowWarningVP().toString();
        this.f13763o = gaugeB.getHighWarningVP().toString();
        this.f13764p = gaugeB.getHighCriticalVP().toString();
        this.f13756h.setText(bH.W.b(gaugeB.getValue(), gaugeB.getLabelDigits()));
        this.f13750a.setText(gaugeB.getMinVP().toString());
        this.f13751b.setText(gaugeB.getMaxVP().toString());
        this.f13752c.setText(gaugeB.getLowWarningVP().toString());
        this.f13753d.setText(gaugeB.getLowCriticalVP().toString());
        this.f13754e.setText(gaugeB.getHighWarningVP().toString());
        this.f13755g.setText(gaugeB.getHighCriticalVP().toString());
        this.f13757i.setText(bH.W.b(gaugeB.getHistoricalPeakValue(), gaugeB.getLabelDigits()));
        this.f13756h.setForeground(UIManager.getColor("Label.foreground"));
        this.f13750a.setForeground(UIManager.getColor("Label.foreground"));
        this.f13751b.setForeground(UIManager.getColor("Label.foreground"));
        this.f13752c.setForeground(UIManager.getColor("Label.foreground"));
        this.f13753d.setForeground(UIManager.getColor("Label.foreground"));
        this.f13754e.setForeground(UIManager.getColor("Label.foreground"));
        this.f13755g.setForeground(UIManager.getColor("Label.foreground"));
        this.f13757i.setForeground(UIManager.getColor("Label.foreground"));
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if ((abstractC1420s instanceof Gauge) && ((Gauge) abstractC1420s).getValue() != gaugeB.getValue()) {
                this.f13756h.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it2 = arrayList.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s2 = (AbstractC1420s) it2.next();
            if ((abstractC1420s2 instanceof Gauge) && !((Gauge) abstractC1420s2).getMinVP().toString().equals(gaugeB.getMinVP().toString())) {
                this.f13750a.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it3 = arrayList.iterator();
        while (true) {
            if (!it3.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s3 = (AbstractC1420s) it3.next();
            if ((abstractC1420s3 instanceof Gauge) && !((Gauge) abstractC1420s3).getMaxVP().toString().equals(gaugeB.getMaxVP().toString())) {
                this.f13751b.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it4 = arrayList.iterator();
        while (true) {
            if (!it4.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s4 = (AbstractC1420s) it4.next();
            if ((abstractC1420s4 instanceof Gauge) && !((Gauge) abstractC1420s4).getLowWarningVP().toString().equals(gaugeB.getLowWarningVP().toString())) {
                this.f13752c.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it5 = arrayList.iterator();
        while (true) {
            if (!it5.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s5 = (AbstractC1420s) it5.next();
            if ((abstractC1420s5 instanceof Gauge) && !((Gauge) abstractC1420s5).getLowCriticalVP().toString().equals(gaugeB.getLowCriticalVP().toString())) {
                this.f13753d.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it6 = arrayList.iterator();
        while (true) {
            if (!it6.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s6 = (AbstractC1420s) it6.next();
            if ((abstractC1420s6 instanceof Gauge) && !((Gauge) abstractC1420s6).getHighWarningVP().toString().equals(gaugeB.getHighWarningVP().toString())) {
                this.f13754e.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it7 = arrayList.iterator();
        while (true) {
            if (!it7.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s7 = (AbstractC1420s) it7.next();
            if ((abstractC1420s7 instanceof Gauge) && !((Gauge) abstractC1420s7).getHighCriticalVP().toString().equals(gaugeB.getHighCriticalVP().toString())) {
                this.f13755g.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it8 = arrayList.iterator();
        while (it8.hasNext()) {
            AbstractC1420s abstractC1420s8 = (AbstractC1420s) it8.next();
            if ((abstractC1420s8 instanceof Gauge) && ((Gauge) abstractC1420s8).getHistoricalPeakValue() != gaugeB.getHistoricalPeakValue()) {
                this.f13757i.setForeground(Color.GRAY);
                return;
            }
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        e(arrayList);
    }

    public JPanel a() {
        return this.f13758j;
    }
}
