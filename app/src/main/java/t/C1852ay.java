package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: t.ay, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/ay.class */
public class C1852ay extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JPanel f13825a;

    /* renamed from: b, reason: collision with root package name */
    JCheckBox f13826b;

    public C1852ay(Window window, C1836ai c1836ai) {
        super(window, "Select Italic Font");
        this.f13825a = new JPanel();
        this.f13826b = new JCheckBox(C1818g.b("Use Italic Font"));
        a(c1836ai);
        this.f13825a.setBorder(BorderFactory.createTitledBorder(C1818g.b("Italic Font")));
        this.f13825a.setLayout(new GridLayout(1, 1));
        this.f13825a.add(this.f13826b);
        this.f13826b.addActionListener(new C1853az(this));
        add(BorderLayout.CENTER, this.f13825a);
        pack();
    }

    public JPanel a() {
        return this.f13825a;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        if (arrayList.size() > 0) {
            this.f13826b.setSelected(((AbstractC1420s) arrayList.get(0)).isItalicFont());
        }
    }
}
