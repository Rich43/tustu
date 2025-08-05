package bt;

import com.efiAnalytics.ui.C1571bi;
import com.efiAnalytics.ui.eJ;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import r.C1806i;

/* renamed from: bt.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/a.class */
public class C1291a extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    G.R f8737a;

    /* renamed from: b, reason: collision with root package name */
    String f8738b;

    /* renamed from: c, reason: collision with root package name */
    static ImageIcon f8739c = null;

    /* renamed from: d, reason: collision with root package name */
    static ImageIcon f8740d = null;

    public C1291a(G.R r2, String str) {
        this.f8737a = null;
        this.f8738b = null;
        if (C1806i.a().a("lkjfgblkjgdoijre98u")) {
            this.f8737a = r2;
            this.f8738b = str;
            if (f8739c == null || f8740d == null) {
                try {
                    f8740d = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/comment3.png")).getScaledInstance(eJ.a(14), eJ.a(14), 4));
                    f8739c = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/comment2.png")).getScaledInstance(eJ.a(14), eJ.a(14), 4));
                } catch (Exception e2) {
                    bH.C.a("Could no load icon image resources/comment3.png");
                }
            }
            addMouseListener(new C1318b(this));
            b();
        }
    }

    public void a() {
        String strO = this.f8737a.o(this.f8738b);
        if (strO == null) {
            strO = "";
        }
        if (strO.contains("</head>")) {
            strO = "<html>" + strO.substring(strO.indexOf("</head>") + 7);
        }
        C1571bi c1571bi = new C1571bi(com.efiAnalytics.ui.bV.b(this), strO);
        c1571bi.a(new C1344c(this));
        c1571bi.setSize(eJ.a(322), eJ.a(183));
        Point locationOnScreen = getLocationOnScreen();
        locationOnScreen.f12371y += getHeight() / 2;
        locationOnScreen.f12370x -= eJ.a(10);
        c1571bi.setLocation(locationOnScreen);
        c1571bi.setVisible(true);
        setToolTipText(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        if (this.f8737a.o(this.f8738b) == null || this.f8737a.o(this.f8738b).length() <= 0) {
            setToolTipText("Click to add notes for this setting.");
            setIcon(f8739c);
            return;
        }
        StringBuilder sbA = bH.W.a(new StringBuilder(bH.W.d(this.f8737a.o(this.f8738b))), "\n", "<br>");
        if (!sbA.toString().startsWith("<html>")) {
            sbA.insert(0, "<html>");
        }
        if (!sbA.toString().endsWith("</html>")) {
            sbA.append("</html>");
        }
        setToolTipText(sbA.toString());
        setIcon(f8740d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        this.f8737a.b(this.f8738b, bH.W.d(str));
        b();
    }
}
