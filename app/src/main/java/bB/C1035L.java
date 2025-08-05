package bb;

import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import s.C1818g;

/* renamed from: bb.L, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/L.class */
public class C1035L extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    C1047l f7739a;

    public C1035L() {
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout());
        add(BorderLayout.CENTER, jPanel);
        this.f7739a = new C1047l(C1818g.d());
        jPanel.setPreferredSize(new Dimension(eJ.a(500), eJ.a(380)));
        jPanel.add(new JScrollPane(this.f7739a));
        ae.r.a().b();
    }

    public void a(ae.k kVar, ae.m mVar) {
        a(ae.r.a().a(kVar, mVar));
    }

    public void a(List list) {
        this.f7739a.a(list);
        if (list.size() > 0) {
            this.f7739a.a(0);
        }
    }

    public ae.q a() {
        return this.f7739a.c();
    }

    public int b() {
        return this.f7739a.d();
    }
}
