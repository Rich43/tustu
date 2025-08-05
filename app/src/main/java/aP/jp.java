package aP;

import ay.C0924a;
import ay.C0926c;
import java.awt.Component;
import javax.swing.JMenuItem;
import x.C1891a;

/* loaded from: TunerStudioMS.jar:aP/jp.class */
public class jp extends C1891a {

    /* renamed from: c, reason: collision with root package name */
    jr f3802c = new jr(this);

    public jp() {
        setText("Dash Configuration Server");
        super.a(this.f3802c);
        super.b(this.f3802c);
        C0924a.c().a(new js(this));
        if (C0924a.c().e()) {
            return;
        }
        C0924a.c().g();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(C0926c c0926c) {
        String strA = c0926c.a("url");
        if (strA == null) {
            bH.C.b("Dash Config Service spotted, but does not contain valid url in attributes.");
            return;
        }
        jt jtVar = new jt(this, c(c0926c), c0926c);
        jtVar.addActionListener(new jq(this, strA));
        add((JMenuItem) jtVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(C0926c c0926c) {
        for (Component component : super.getMenuComponents()) {
            if (component instanceof jt) {
                jt jtVar = (jt) component;
                if (jtVar.a().a().equals(c0926c.a()) && jtVar.a().c().equals(c0926c.c())) {
                    super.a(jtVar);
                    return;
                }
            }
        }
    }

    private String c(C0926c c0926c) {
        String strA = c0926c.a("name");
        String strA2 = c0926c.a("appName");
        if (strA2 == null) {
            strA2 = "TS Dash";
        }
        return strA == null ? strA2 + " - IP:" + c0926c.c() : strA2 + " - " + strA + ", IP:" + c0926c.c();
    }
}
