package com.efiAnalytics.ui;

import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cI.class */
public class cI extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    ImageIcon f11065a;

    /* renamed from: b, reason: collision with root package name */
    String f11066b;

    /* renamed from: c, reason: collision with root package name */
    private bH.aa f11067c;

    public cI(String str) {
        this(str, null);
    }

    public cI(String str, bH.aa aaVar) {
        this.f11065a = null;
        this.f11066b = null;
        this.f11067c = null;
        this.f11066b = str;
        this.f11067c = aaVar;
        try {
            this.f11065a = new ImageIcon(cO.a().a(cO.f11090f, this));
            setPreferredSize(eJ.a(26, 26));
            addMouseListener(new cJ(this));
        } catch (Exception e2) {
            bH.C.a("Could no load icon image resources/comment3.png");
        }
        setIcon(this.f11065a);
        b();
        setOpaque(false);
    }

    public void a() throws HeadlessException {
        if (this.f11066b == null) {
            this.f11066b = "";
        }
        String str = this.f11066b;
        if (str == null || str.equals("")) {
            return;
        }
        C1571bi c1571bi = new C1571bi(bV.b(this), a(str), true);
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
        if (this.f11066b == null || this.f11066b.length() <= 0) {
            setToolTipText(null);
            setIcon(null);
            return;
        }
        String str = this.f11066b;
        StringBuilder sbA = bH.W.a(bH.W.a(new StringBuilder(a(this.f11066b)), "\\n", "\n"), "\n", "<br>");
        sbA.insert(0, "<html>");
        sbA.append("</html>");
        setToolTipText(sbA.toString());
    }

    private String a(String str) {
        return this.f11067c != null ? this.f11067c.a(str) : str;
    }
}
