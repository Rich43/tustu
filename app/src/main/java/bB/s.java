package bb;

import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:bb/s.class */
class s extends JPanel {

    /* renamed from: c, reason: collision with root package name */
    Image f7813c;

    /* renamed from: d, reason: collision with root package name */
    Image f7814d;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ C1051p f7816f;

    /* renamed from: a, reason: collision with root package name */
    JLabel f7811a = new JLabel();

    /* renamed from: b, reason: collision with root package name */
    JLabel f7812b = new JLabel("", 4);

    /* renamed from: e, reason: collision with root package name */
    boolean f7815e = false;

    s(C1051p c1051p) {
        this.f7816f = c1051p;
        this.f7813c = null;
        this.f7814d = null;
        setLayout(new BorderLayout(eJ.a(10), eJ.a(10)));
        add(BorderLayout.CENTER, this.f7812b);
        add("East", this.f7811a);
        try {
            this.f7813c = cO.a().a(cO.f11119I);
            this.f7814d = cO.a().a(cO.f11120J);
        } catch (V.a e2) {
            bV.d("Error Loading Images:\n" + e2.getLocalizedMessage(), this);
        }
        this.f7811a.setIcon(new ImageIcon(this.f7813c));
        t tVar = new t(this, c1051p);
        this.f7811a.addMouseListener(tVar);
        this.f7812b.addMouseListener(tVar);
    }

    public void a(String str) {
        this.f7812b.setText(str);
    }

    public void a() {
        if (this.f7815e) {
            return;
        }
        this.f7811a.setIcon(new ImageIcon(this.f7814d));
        this.f7811a.validate();
        synchronized (this.f7816f.f7803a) {
            this.f7816f.f7803a.notifyAll();
        }
        this.f7815e = true;
        this.f7816f.f7807e.setVisible(false);
        this.f7816f.f7807e.repaint();
    }
}
