package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

/* loaded from: TunerStudioMS.jar:t/aK.class */
public class aK extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JTextField f13744a;

    /* renamed from: b, reason: collision with root package name */
    JTextField f13745b;

    /* renamed from: c, reason: collision with root package name */
    JPanel f13746c;

    public aK(Window window, C1836ai c1836ai) {
        super(window, C1818g.b("Gauge Text"));
        this.f13746c = new JPanel();
        a(c1836ai);
        aN aNVar = new aN(this);
        this.f13746c.setLayout(new GridLayout(0, 1, eJ.a(1), eJ.a(3)));
        this.f13746c.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), C1818g.b("Gauge Text")));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(2, 2));
        JLabel jLabel = new JLabel(C1818g.b("Gauge Title"));
        jLabel.setMinimumSize(new Dimension(eJ.a(80), eJ.a(18)));
        jLabel.setPreferredSize(new Dimension(eJ.a(80), eJ.a(18)));
        jLabel.setHorizontalAlignment(4);
        jPanel.add("West", jLabel);
        this.f13744a = new JTextField("", 23);
        this.f13744a.addFocusListener(aNVar);
        this.f13744a.addKeyListener(new aL(this));
        jPanel.add(BorderLayout.CENTER, this.f13744a);
        this.f13746c.add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(2, 2));
        JLabel jLabel2 = new JLabel(C1818g.b("Gauge Units"));
        jLabel2.setMinimumSize(eJ.a(80, 18));
        jLabel2.setPreferredSize(eJ.a(80, 18));
        jLabel2.setHorizontalAlignment(4);
        jPanel2.add("West", jLabel2);
        this.f13745b = new JTextField("", 23);
        this.f13745b.addFocusListener(aNVar);
        this.f13745b.addKeyListener(new aM(this));
        jPanel2.add(BorderLayout.CENTER, this.f13745b);
        this.f13746c.add(jPanel2);
        add(BorderLayout.CENTER, this.f13746c);
    }

    public void e(ArrayList arrayList) {
        Gauge gaugeB = b(arrayList);
        if (gaugeB == null) {
            C1685fp.a((Component) a(), false);
            return;
        }
        C1685fp.a((Component) a(), true);
        this.f13744a.setText(gaugeB.getTitle().toString());
        this.f13745b.setText(gaugeB.getUnits().toString());
        this.f13744a.setForeground(UIManager.getColor("Label.foreground"));
        this.f13745b.setForeground(UIManager.getColor("Label.foreground"));
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if ((abstractC1420s instanceof Gauge) && !((Gauge) abstractC1420s).getTitle().equals(gaugeB.getTitle())) {
                this.f13744a.setForeground(Color.GRAY);
                break;
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            AbstractC1420s abstractC1420s2 = (AbstractC1420s) it2.next();
            if ((abstractC1420s2 instanceof Gauge) && !((Gauge) abstractC1420s2).getUnits().equals(gaugeB.getUnits())) {
                this.f13745b.setForeground(Color.GRAY);
                return;
            }
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        e(arrayList);
    }

    @Override // java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        for (Component component : this.f13746c.getComponents()) {
            component.setEnabled(z2);
        }
        this.f13746c.setEnabled(z2);
    }

    public JPanel a() {
        return this.f13746c;
    }
}
