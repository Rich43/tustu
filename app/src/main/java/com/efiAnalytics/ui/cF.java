package com.efiAnalytics.ui;

import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cF.class */
public class cF extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    static ImageIcon f11060a = null;

    /* renamed from: c, reason: collision with root package name */
    private String f11061c;

    /* renamed from: b, reason: collision with root package name */
    bH.aa f11062b;

    public cF(String str, bH.aa aaVar) {
        this(str, aaVar, 32);
    }

    public cF(String str, bH.aa aaVar, int i2) {
        this.f11062b = aaVar;
        this.f11061c = str;
        if (f11060a == null) {
            try {
                f11060a = new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/help_square_12.png")), this));
            } catch (Exception e2) {
                bH.C.a("Could no load icon image resources/Help32.png");
            }
        }
        addMouseListener(new cG(this));
        b();
    }

    private String b(String str) {
        return this.f11062b != null ? this.f11062b.a(str) : str;
    }

    public void a() {
        String str = this.f11061c;
        if (str == null || str.equals("")) {
            return;
        }
        C1571bi c1571bi = new C1571bi(bV.b(this), b(str), true);
        c1571bi.setSize(eJ.a(322), eJ.a(183));
        c1571bi.a(new cH(this));
        Point locationOnScreen = getLocationOnScreen();
        locationOnScreen.f12371y += getHeight() / 2;
        locationOnScreen.f12370x -= eJ.a(10);
        c1571bi.setLocation(locationOnScreen);
        setToolTipText(null);
        c1571bi.setVisible(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        if (this.f11061c == null || this.f11061c.length() <= 0) {
            setToolTipText(null);
            setIcon(null);
            return;
        }
        this.f11061c = b(this.f11061c);
        boolean z2 = (this.f11061c.length() <= 150 || this.f11061c.contains("\n") || this.f11061c.contains("<br>")) ? false : true;
        StringBuilder sbA = bH.W.a(bH.W.a(new StringBuilder(this.f11061c), "\\n", "\n"), "\n", "<br>");
        if (z2) {
            sbA = bH.W.a(sbA, 50, "<br>");
        }
        sbA.insert(0, "<html>");
        sbA.append("</html>");
        setToolTipText(sbA.toString());
        setIcon(f11060a);
    }

    public void a(String str) {
        this.f11061c = b(str);
        b();
    }
}
