package ba;

import G.R;
import W.B;
import aP.C0338f;
import aP.cZ;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import r.C1798a;

/* renamed from: ba.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ba/a.class */
public class C1023a implements B {

    /* renamed from: a, reason: collision with root package name */
    private R f7711a;

    public C1023a(R r2) {
        this.f7711a = null;
        this.f7711a = r2;
    }

    @Override // W.B
    public void a(File file) throws HeadlessException {
        if (bV.a("The file '" + file.getName() + "' has been changed by another program.\n\nWould you like " + C1798a.f13268b + " to load the changes?", (Component) cZ.a().c(), true)) {
            C0338f.a().a(cZ.a().c(), this.f7711a, file.getAbsolutePath());
        }
    }
}
