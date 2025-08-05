package p;

import bH.aa;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/* renamed from: p.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/a.class */
public class C1775a extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    aa f13183a;

    /* renamed from: b, reason: collision with root package name */
    J f13184b;

    /* renamed from: c, reason: collision with root package name */
    C1790p f13185c;

    public C1775a(S.l lVar, InterfaceC1773B interfaceC1773B, aa aaVar) {
        this.f13183a = aaVar;
        setLayout(new BorderLayout());
        JTabbedPane jTabbedPane = new JTabbedPane();
        add(BorderLayout.CENTER, new JScrollPane(jTabbedPane));
        this.f13184b = new J(interfaceC1773B, aaVar);
        this.f13185c = new C1790p(lVar, aaVar);
        jTabbedPane.add(a("Action Triggers"), this.f13185c);
        interfaceC1773B.a(this.f13185c);
        jTabbedPane.add(a("User Actions"), this.f13184b);
    }

    private String a(String str) {
        if (this.f13183a != null) {
            str = this.f13183a.a(str);
        }
        return str;
    }

    public void a(Window window) {
        JDialog jDialogB = bV.b(this, window, a("Action Manager"), this);
        jDialogB.pack();
        bV.a((Component) window, (Component) jDialogB);
        jDialogB.setVisible(true);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f13185c.close();
        this.f13184b.close();
    }
}
