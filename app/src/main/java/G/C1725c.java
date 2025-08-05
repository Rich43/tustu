package g;

import bH.C;
import com.efiAnalytics.ui.cO;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

/* renamed from: g.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/c.class */
public class C1725c extends FileView {

    /* renamed from: a, reason: collision with root package name */
    ImageIcon f12191a;

    /* renamed from: b, reason: collision with root package name */
    ImageIcon f12192b;

    public C1725c() {
        this.f12191a = null;
        this.f12192b = null;
        try {
            cO cOVarA = cO.a();
            this.f12191a = new ImageIcon(cOVarA.a(cO.f11104t));
            this.f12192b = new ImageIcon(cOVarA.a(cO.f11114D));
        } catch (V.a e2) {
            C.a(e2);
        }
    }

    @Override // javax.swing.filechooser.FileView
    public Icon getIcon(File file) {
        String lowerCase = file.getName().toLowerCase();
        if (lowerCase.endsWith(".msq")) {
            return this.f12191a;
        }
        if (lowerCase.endsWith(".msl") || lowerCase.endsWith(".xls") || lowerCase.endsWith(".log")) {
            return this.f12192b;
        }
        return null;
    }
}
