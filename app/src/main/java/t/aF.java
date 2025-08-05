package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.ui.C1685fp;
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
import javax.swing.JSlider;
import javax.swing.UIManager;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aF.class */
public class aF extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    String f13725a;

    /* renamed from: b, reason: collision with root package name */
    JSlider f13726b;

    /* renamed from: c, reason: collision with root package name */
    JSlider f13727c;

    /* renamed from: d, reason: collision with root package name */
    JSlider f13728d;

    /* renamed from: e, reason: collision with root package name */
    JSlider f13729e;

    /* renamed from: g, reason: collision with root package name */
    JPanel f13730g;

    /* renamed from: h, reason: collision with root package name */
    JLabel f13731h;

    /* renamed from: i, reason: collision with root package name */
    JLabel f13732i;

    /* renamed from: j, reason: collision with root package name */
    JLabel f13733j;

    /* renamed from: k, reason: collision with root package name */
    JLabel f13734k;

    /* renamed from: l, reason: collision with root package name */
    public static String f13735l = C1818g.b("Face Angle / Fill");

    /* renamed from: m, reason: collision with root package name */
    public static String f13736m = C1818g.b("Face Start Angle / Height");

    /* renamed from: n, reason: collision with root package name */
    public static String f13737n = C1818g.b("Sweep Angle");

    /* renamed from: o, reason: collision with root package name */
    public static String f13738o = C1818g.b("Needle Start Angle");

    /* renamed from: p, reason: collision with root package name */
    private boolean f13739p;

    public aF(Window window, C1836ai c1836ai) {
        super(window, C1818g.b("Gauge Angles"));
        this.f13725a = "Gauge Angles";
        this.f13730g = new JPanel();
        this.f13739p = false;
        a(c1836ai);
        this.f13730g.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), this.f13725a));
        this.f13730g.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        this.f13726b = a(0, 0, 360, f13735l);
        this.f13726b.setPaintLabels(true);
        this.f13726b.setSnapToTicks(true);
        this.f13726b.addChangeListener(new aG(this));
        jPanel.add(this.f13726b);
        this.f13727c = a(0, 0, 360, f13736m);
        this.f13727c.setPaintLabels(true);
        this.f13727c.setSnapToTicks(true);
        this.f13727c.addChangeListener(new aH(this));
        jPanel.add(this.f13727c);
        this.f13728d = a(0, 0, 360, f13738o);
        this.f13728d.setPaintLabels(true);
        this.f13728d.setSnapToTicks(true);
        this.f13728d.addChangeListener(new aI(this));
        jPanel.add(this.f13728d);
        this.f13729e = a(0, 0, 360, f13737n);
        this.f13729e.setPaintLabels(true);
        this.f13729e.setSnapToTicks(true);
        this.f13729e.addChangeListener(new aJ(this));
        jPanel.add(this.f13729e);
        this.f13730g.add(BorderLayout.CENTER, jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1));
        this.f13731h = new JLabel();
        this.f13732i = new JLabel();
        this.f13733j = new JLabel();
        this.f13734k = new JLabel();
        jPanel2.add(this.f13731h);
        jPanel2.add(this.f13732i);
        jPanel2.add(this.f13733j);
        jPanel2.add(this.f13734k);
        this.f13730g.add("East", jPanel2);
        add(this.f13730g, BorderLayout.CENTER);
        pack();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) throws IllegalArgumentException {
        this.f13739p = true;
        e(arrayList);
        this.f13739p = false;
    }

    public void e(ArrayList arrayList) throws IllegalArgumentException {
        Gauge gaugeB = b(arrayList);
        if (gaugeB == null) {
            C1685fp.a((Component) a(), false);
            return;
        }
        C1685fp.a((Component) a(), true);
        this.f13726b.setValue(gaugeB.getFaceAngle());
        this.f13727c.setValue(gaugeB.getStartAngle());
        this.f13728d.setValue(gaugeB.getSweepBeginDegree());
        this.f13729e.setValue(gaugeB.getSweepAngle());
        this.f13731h.setText("" + gaugeB.getFaceAngle());
        this.f13732i.setText("" + gaugeB.getStartAngle());
        this.f13733j.setText("" + gaugeB.getSweepBeginDegree());
        this.f13734k.setText("" + gaugeB.getSweepAngle());
        Color color = UIManager.getColor("Label.foreground");
        this.f13731h.setForeground(color);
        this.f13732i.setForeground(color);
        this.f13733j.setForeground(color);
        this.f13734k.setForeground(color);
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if ((abstractC1420s instanceof Gauge) && ((Gauge) abstractC1420s).getFaceAngle() != gaugeB.getFaceAngle()) {
                this.f13731h.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it2 = arrayList.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s2 = (AbstractC1420s) it2.next();
            if ((abstractC1420s2 instanceof Gauge) && ((Gauge) abstractC1420s2).getStartAngle() != gaugeB.getStartAngle()) {
                this.f13732i.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it3 = arrayList.iterator();
        while (true) {
            if (!it3.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s3 = (AbstractC1420s) it3.next();
            if ((abstractC1420s3 instanceof Gauge) && ((Gauge) abstractC1420s3).getSweepBeginDegree() != gaugeB.getSweepBeginDegree()) {
                this.f13733j.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it4 = arrayList.iterator();
        while (it4.hasNext()) {
            AbstractC1420s abstractC1420s4 = (AbstractC1420s) it4.next();
            if ((abstractC1420s4 instanceof Gauge) && ((Gauge) abstractC1420s4).getSweepAngle() != gaugeB.getSweepAngle()) {
                this.f13734k.setForeground(Color.GRAY);
                return;
            }
        }
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

    public JPanel a() {
        return this.f13730g;
    }
}
