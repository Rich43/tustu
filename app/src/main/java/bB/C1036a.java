package bb;

import aP.C0338f;
import com.efiAnalytics.apps.ts.dashboard.HtmlDisplay;
import com.efiAnalytics.ui.C1603cn;
import com.efiAnalytics.ui.fS;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import s.C1818g;

/* renamed from: bb.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/a.class */
public class C1036a extends JPanel implements bH.L, fS {

    /* renamed from: b, reason: collision with root package name */
    public static String f7741b = "./help/firmwareLoader/archiveProjectInfo.html";

    /* renamed from: a, reason: collision with root package name */
    HtmlDisplay f7740a = new HtmlDisplay();

    /* renamed from: c, reason: collision with root package name */
    JButton f7742c = new JButton(C1818g.b("Backup Project Now"));

    /* renamed from: d, reason: collision with root package name */
    JProgressBar f7743d = new JProgressBar(0, 100);

    /* renamed from: e, reason: collision with root package name */
    C1603cn f7744e = new C1603cn();

    /* renamed from: f, reason: collision with root package name */
    JPanel f7745f = new JPanel();

    /* renamed from: g, reason: collision with root package name */
    String f7746g = "";

    public C1036a() {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Backup Car Project")));
        setLayout(new BorderLayout());
        add(this.f7740a, BorderLayout.CENTER);
        try {
            this.f7740a.setDocumentUrl(f7741b);
        } catch (V.a e2) {
            Logger.getLogger(C1036a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(this.f7743d, "South");
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(this.f7742c, "East");
        jPanel2.add(this.f7745f, BorderLayout.CENTER);
        this.f7745f.setVisible(false);
        this.f7745f.setLayout(new BorderLayout(4, 4));
        this.f7745f.add(new JLabel(C1818g.b("Backup saved to") + ": "), "West");
        this.f7745f.add(this.f7744e, BorderLayout.CENTER);
        jPanel.add(jPanel2, BorderLayout.CENTER);
        add(jPanel, "South");
        this.f7742c.addActionListener(new C1037b(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        this.f7746g = C0338f.a().a(this);
    }

    @Override // bH.L
    public void a() {
        this.f7742c.setEnabled(false);
        this.f7743d.setValue(0);
    }

    @Override // bH.L
    public void a(double d2) {
        this.f7743d.setValue((int) Math.round(d2 + 100.0d));
    }

    @Override // bH.L
    public void b() throws IllegalArgumentException {
        this.f7745f.setVisible(true);
        this.f7744e.a(this.f7746g);
        this.f7743d.setValue(100);
    }

    @Override // com.efiAnalytics.ui.fS
    public boolean g_() {
        return true;
    }
}
