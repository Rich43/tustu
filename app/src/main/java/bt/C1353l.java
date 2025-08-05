package bt;

import com.efiAnalytics.ui.C1571bi;
import com.efiAnalytics.ui.eJ;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import s.C1818g;

/* renamed from: bt.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/l.class */
public class C1353l extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    G.R f9102a;

    /* renamed from: b, reason: collision with root package name */
    String f9103b;

    /* renamed from: c, reason: collision with root package name */
    static ImageIcon f9104c = null;

    public C1353l(G.R r2, String str) {
        this.f9102a = null;
        this.f9103b = null;
        this.f9102a = r2;
        this.f9103b = str;
        if (f9104c == null) {
            try {
                f9104c = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/help_square_12.png")).getScaledInstance(eJ.a(12), eJ.a(12), 4));
            } catch (Exception e2) {
                bH.C.a("Could no load icon image resources/Help32.png");
            }
        }
        addMouseListener(new C1354m(this));
        b();
    }

    public void a() {
        String strW = this.f9102a.w(this.f9103b);
        if (strW == null || strW.equals("")) {
            return;
        }
        C1571bi c1571bi = new C1571bi(com.efiAnalytics.ui.bV.b(this), C1818g.b(strW), true);
        c1571bi.setSize(eJ.a(322), eJ.a(183));
        c1571bi.a(new C1355n(this));
        Point locationOnScreen = getLocationOnScreen();
        locationOnScreen.f12371y += getHeight() / 2;
        locationOnScreen.f12370x -= eJ.a(10);
        c1571bi.setLocation(locationOnScreen);
        setToolTipText(null);
        c1571bi.setVisible(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        String strW = this.f9102a.w(this.f9103b);
        if (strW == null || strW.length() <= 0) {
            setToolTipText(null);
            setIcon(null);
            return;
        }
        StringBuilder sbA = bH.W.a(bH.W.a(new StringBuilder(C1818g.b(strW)), "\\n", "\n"), "\n", "<br>");
        sbA.insert(0, "<html>");
        sbA.append("</html>");
        setToolTipText(sbA.toString());
        setIcon(f9104c);
    }
}
