package bb;

import bc.C1064k;
import com.efiAnalytics.ui.fS;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

/* renamed from: bb.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/c.class */
public class C1038c extends JPanel implements fS {

    /* renamed from: a, reason: collision with root package name */
    boolean f7749a;

    /* renamed from: e, reason: collision with root package name */
    private ae.q f7748e = null;

    /* renamed from: b, reason: collision with root package name */
    C1064k f7750b = new C1064k();

    /* renamed from: c, reason: collision with root package name */
    JTextPane f7751c = new JTextPane();

    /* renamed from: d, reason: collision with root package name */
    JProgressBar f7752d = new JProgressBar(0, 100);

    public C1038c(boolean z2) {
        this.f7749a = true;
        this.f7749a = z2;
        setLayout(new BorderLayout(1, 1));
        add(this.f7750b, BorderLayout.CENTER);
    }

    @Override // com.efiAnalytics.ui.fS
    public boolean g_() {
        return false;
    }

    public void a(ae.q qVar, ae.k kVar) {
        this.f7748e = qVar;
        this.f7750b.a(qVar, kVar, this.f7749a);
    }

    public boolean a() {
        return this.f7750b.a();
    }
}
