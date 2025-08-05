package ao;

import java.awt.Window;
import javax.swing.JDialog;

/* renamed from: ao.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/d.class */
class C0690d implements W.aq {

    /* renamed from: a, reason: collision with root package name */
    JDialog f5511a = null;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ Window f5512b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0636b f5513c;

    C0690d(C0636b c0636b, Window window) {
        this.f5513c = c0636b;
        this.f5512b = window;
    }

    @Override // W.aq
    public void a(double d2) {
        if (this.f5511a == null) {
            this.f5511a = com.efiAnalytics.ui.bV.a(this.f5512b, "Writing to file");
        }
    }

    @Override // W.aq
    public void a() {
        if (this.f5511a != null) {
            this.f5511a.dispose();
        }
    }
}
