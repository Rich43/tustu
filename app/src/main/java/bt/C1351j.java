package bt;

import com.efiAnalytics.ui.C1571bi;
import com.efiAnalytics.ui.eJ;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import s.C1818g;

/* renamed from: bt.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/j.class */
public class C1351j extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    ImageIcon f9099a;

    /* renamed from: b, reason: collision with root package name */
    String f9100b;

    public C1351j(String str) {
        this.f9099a = null;
        this.f9100b = null;
        this.f9100b = str;
        try {
            this.f9099a = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Help32.png")).getScaledInstance(20, 20, 4));
            addMouseListener(new C1352k(this));
        } catch (Exception e2) {
            bH.C.a("Could no load icon image resources/comment3.png");
        }
        setIcon(this.f9099a);
        b();
        setOpaque(false);
    }

    public void a() throws HeadlessException {
        if (this.f9100b == null) {
            this.f9100b = "";
        }
        String str = this.f9100b;
        if (str == null || str.equals("")) {
            return;
        }
        C1571bi c1571bi = new C1571bi(com.efiAnalytics.ui.bV.b(this), C1818g.b(str), true);
        c1571bi.setSize(eJ.a(322), eJ.a(183));
        Point locationOnScreen = getLocationOnScreen();
        locationOnScreen.f12371y += getHeight() / 2;
        locationOnScreen.f12370x -= eJ.a(10);
        if (locationOnScreen.f12370x + c1571bi.getWidth() > Toolkit.getDefaultToolkit().getScreenSize().width) {
        }
        c1571bi.setLocation(locationOnScreen);
        setToolTipText(null);
        c1571bi.setVisible(true);
    }

    private void b() {
        if (this.f9100b == null || this.f9100b.length() <= 0) {
            setToolTipText(null);
            setIcon(null);
            return;
        }
        String str = this.f9100b;
        StringBuilder sbA = bH.W.a(bH.W.a(new StringBuilder(C1818g.b(this.f9100b)), "\\n", "\n"), "\n", "<br>");
        sbA.insert(0, "<html>");
        sbA.append("</html>");
        setToolTipText(sbA.toString());
    }
}
