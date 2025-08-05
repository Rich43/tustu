package n;

import G.C0088bu;
import G.R;
import com.efiAnalytics.ui.bV;
import java.awt.Window;

/* loaded from: TunerStudioMS.jar:n/h.class */
public class h {

    /* renamed from: a, reason: collision with root package name */
    private static h f12940a = null;

    /* renamed from: b, reason: collision with root package name */
    private l f12941b = null;

    private h() {
    }

    public static h a() {
        if (f12940a == null) {
            f12940a = new h();
        }
        return f12940a;
    }

    public void a(R r2, C0088bu c0088bu, Window window) {
        if (this.f12941b != null) {
            this.f12941b.a(r2, c0088bu, window);
        } else {
            bV.d("ShowEcuDialogActionProvider must be set in EventActions", window);
        }
    }

    public void a(l lVar) {
        this.f12941b = lVar;
    }
}
