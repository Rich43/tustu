package t;

import com.efiAnalytics.apps.ts.dashboard.DashLabel;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.ui.eJ;
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

/* renamed from: t.as, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/as.class */
public class C1846as extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JTextField f13804a;

    /* renamed from: b, reason: collision with root package name */
    JPanel f13805b;

    public C1846as(Window window, C1836ai c1836ai) {
        super(window, C1818g.b("Label Text"));
        this.f13805b = new JPanel();
        a(c1836ai);
        C1848au c1848au = new C1848au(this);
        this.f13805b.setLayout(new GridLayout(0, 2, eJ.a(1), eJ.a(3)));
        this.f13805b.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), C1818g.b("Label Text")));
        JLabel jLabel = new JLabel(C1818g.b("Text"));
        jLabel.setHorizontalAlignment(4);
        this.f13805b.add(jLabel);
        this.f13804a = new JTextField("", 10);
        this.f13804a.addFocusListener(c1848au);
        this.f13804a.addKeyListener(new C1847at(this));
        this.f13805b.add(this.f13804a);
        add(BorderLayout.CENTER, this.f13805b);
    }

    public void e(ArrayList arrayList) {
        if (arrayList.isEmpty()) {
            this.f13804a.setText("");
            setEnabled(false);
        } else if (arrayList.get(0) != null && (arrayList.get(0) instanceof DashLabel)) {
            setEnabled(true);
            this.f13804a.setText(((DashLabel) arrayList.get(0)).getText().toString());
        } else if (arrayList.size() == 1 && (arrayList.get(0) instanceof DashLabel)) {
            setEnabled(false);
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof DashLabel) {
                arrayList2.add((DashLabel) next);
            }
        }
        e(arrayList2);
    }

    @Override // java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        for (Component component : this.f13805b.getComponents()) {
            component.setEnabled(z2);
        }
        this.f13805b.setEnabled(z2);
    }

    public JPanel a() {
        return this.f13805b;
    }
}
