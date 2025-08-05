package ao;

import ai.C0516f;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JPanel;

/* renamed from: ao.fm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fm.class */
public class C0756fm extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    C0516f f5810a;

    public C0756fm() {
        this.f5810a = null;
        this.f5810a = new C0516f();
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f5810a);
        String str = "file:///" + new File(".").getAbsolutePath() + "/help/register.html";
        try {
            this.f5810a.b(str);
        } catch (V.a e2) {
            bH.C.a("unable to open:\n" + str + "\n" + e2.getMessage());
        }
        this.f5810a.a(false);
    }
}
