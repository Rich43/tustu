package t;

import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/be.class */
public class be extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JTextField f13833a;

    /* renamed from: b, reason: collision with root package name */
    JTextField f13834b;

    /* renamed from: c, reason: collision with root package name */
    JPanel f13835c;

    public be(Window window, C1836ai c1836ai) {
        super(window, C1818g.b("Indicator Text"));
        this.f13835c = new JPanel();
        a(c1836ai);
        bh bhVar = new bh(this);
        this.f13835c.setLayout(new GridLayout(0, 2, 1, 3));
        this.f13835c.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), C1818g.b("Indicator Text")));
        JLabel jLabel = new JLabel(C1818g.b("On Text"));
        jLabel.setHorizontalAlignment(4);
        this.f13835c.add(jLabel);
        this.f13833a = new JTextField("", 10);
        this.f13833a.addFocusListener(bhVar);
        this.f13833a.addKeyListener(new bf(this));
        this.f13835c.add(this.f13833a);
        JLabel jLabel2 = new JLabel(C1818g.b("Off Text"));
        jLabel2.setHorizontalAlignment(4);
        this.f13835c.add(jLabel2);
        this.f13834b = new JTextField("", 10);
        this.f13834b.addFocusListener(bhVar);
        this.f13834b.addKeyListener(new bg(this));
        this.f13835c.add(this.f13834b);
        add(BorderLayout.CENTER, this.f13835c);
    }

    public void e(ArrayList arrayList) {
        if (arrayList.isEmpty()) {
            this.f13833a.setText("");
            this.f13834b.setText("");
            setEnabled(false);
        } else {
            if (arrayList.get(0) == null || !(arrayList.get(0) instanceof Indicator)) {
                if (arrayList.size() == 1 && (arrayList.get(0) instanceof Indicator)) {
                    setEnabled(false);
                    return;
                }
                return;
            }
            setEnabled(true);
            Indicator indicator = (Indicator) arrayList.get(0);
            this.f13833a.setText(indicator.getOnText().toString());
            this.f13834b.setText(indicator.getOffText().toString());
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof Indicator) {
                arrayList2.add(next);
            }
        }
        e(arrayList2);
    }

    @Override // java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        for (Component component : this.f13835c.getComponents()) {
            component.setEnabled(z2);
        }
        this.f13835c.setEnabled(z2);
    }

    public JPanel a() {
        return this.f13835c;
    }
}
