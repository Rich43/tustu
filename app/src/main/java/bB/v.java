package bb;

import ae.C0500d;
import ae.C0502f;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.fS;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import r.C1807j;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bb/v.class */
public class v extends JPanel implements ae.w, fS {

    /* renamed from: a, reason: collision with root package name */
    w f7822a = new w(this);

    /* renamed from: b, reason: collision with root package name */
    C0502f f7823b = null;

    /* renamed from: c, reason: collision with root package name */
    JTextPane f7824c = new JTextPane();

    /* renamed from: d, reason: collision with root package name */
    JProgressBar f7825d = new JProgressBar(0, 100);

    /* renamed from: e, reason: collision with root package name */
    JLabel f7826e = new JLabel();

    /* renamed from: f, reason: collision with root package name */
    C1046k f7827f = new C1046k(C1818g.b("Firmware Loaded"), false);

    /* renamed from: g, reason: collision with root package name */
    C1051p f7828g = new C1051p(C1818g.b("Preparing for firmware load"));

    /* renamed from: h, reason: collision with root package name */
    C1051p f7829h = new C1051p(C1818g.b("Finalizing firmware load"));

    /* renamed from: i, reason: collision with root package name */
    JPanel f7830i = new JPanel();

    /* renamed from: j, reason: collision with root package name */
    CardLayout f7831j = new CardLayout();

    /* renamed from: k, reason: collision with root package name */
    boolean f7832k = false;

    /* renamed from: l, reason: collision with root package name */
    boolean f7833l = false;

    /* renamed from: m, reason: collision with root package name */
    boolean f7834m = false;

    /* renamed from: n, reason: collision with root package name */
    private static final File f7835n = new File(C1807j.C(), "firmwareLoader/firmwareLoadCompleteNoProject.html");

    /* renamed from: o, reason: collision with root package name */
    private static final File f7836o = new File(C1807j.C(), "firmwareLoader/firmwareLoadCompleteProject.html");

    /* renamed from: p, reason: collision with root package name */
    private static final File f7837p = new File(C1807j.C(), "firmwareLoader/loadError.html");

    public v() {
        setLayout(new BorderLayout());
        this.f7830i.setLayout(this.f7831j);
        this.f7830i.add(this.f7828g, "PrepPanel");
        try {
            Image imageA = cO.a().a(cO.f11118H);
            if (0 != 0) {
                imageA = cO.a().a(cO.f11118H).getScaledInstance(eJ.a(671), eJ.a(300), 1);
            }
            this.f7826e.setIcon(new ImageIcon(imageA));
            this.f7826e.setHorizontalAlignment(0);
        } catch (V.a e2) {
            Logger.getLogger(v.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f7830i.add(this.f7826e, "Animation");
        this.f7830i.add(this.f7829h, "PostPanel");
        this.f7830i.add(this.f7827f, "Complete");
        this.f7827f.a(true);
        this.f7831j.show(this.f7830i, "PrepPanel");
        add(this.f7830i, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        this.f7824c.setMinimumSize(new Dimension(200, eJ.a() * 3));
        jPanel.setLayout(new BorderLayout());
        jPanel.add(this.f7824c, "North");
        jPanel.add(this.f7825d, BorderLayout.CENTER);
        add(jPanel, "South");
    }

    public void a(C0502f c0502f) {
        c0502f.a(this.f7822a);
        this.f7832k = false;
        this.f7833l = false;
        this.f7834m = false;
        this.f7823b = c0502f;
        c0502f.a(this);
    }

    public boolean a() {
        return this.f7834m;
    }

    @Override // com.efiAnalytics.ui.fS
    public boolean g_() {
        return true;
    }

    @Override // ae.w
    public void a(List list) {
        if (b(list)) {
            this.f7833l = true;
            this.f7834m = true;
            try {
                this.f7827f.a(f7837p);
            } catch (V.a e2) {
                Logger.getLogger(v.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            this.f7825d.setValue(100);
            this.f7823b.b(this.f7822a);
            this.f7831j.show(this.f7830i, "Complete");
            return;
        }
        if (this.f7823b == null) {
            bV.d("No Valid Loader Set, Can not complete.", this);
            return;
        }
        if (!this.f7832k) {
            this.f7823b.b(this);
            this.f7832k = true;
            this.f7831j.show(this.f7830i, "Animation");
        } else if (!this.f7833l) {
            this.f7833l = true;
            this.f7831j.show(this.f7830i, "PostPanel");
            this.f7823b.c(this);
        } else {
            this.f7834m = true;
            this.f7825d.setValue(100);
            try {
                this.f7827f.a(aE.a.A() == null ? f7835n : f7836o);
            } catch (V.a e3) {
            }
            this.f7823b.b(this.f7822a);
            this.f7831j.show(this.f7830i, "Complete");
        }
    }

    private boolean b(List list) {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            C0500d c0500d = (C0500d) it.next();
            if (c0500d.a() == C0500d.f4347b) {
                stringBuffer2.append(c0500d.b()).append("\n\n");
            } else if (c0500d.a() == C0500d.f4348c) {
                stringBuffer.append(c0500d.b()).append("\n\n");
            }
        }
        if (stringBuffer.length() > 0 || stringBuffer2.length() > 0) {
            StringBuilder sb = new StringBuilder();
            if (stringBuffer2.length() > 0) {
                sb.append(C1818g.b("Could not complete due to the following errors:")).append("\n").append(stringBuffer2);
            }
            if (stringBuffer.length() > 0) {
                sb.append(C1818g.b("Could not complete due to the following errors:")).append("\n").append(stringBuffer);
            }
            bV.d(sb.toString(), this);
        }
        return stringBuffer2.length() > 0;
    }
}
