package bA;

import c.InterfaceC1386e;
import com.efiAnalytics.ui.InterfaceC1581bs;
import javax.swing.JMenuItem;

/* loaded from: TunerStudioMS.jar:bA/e.class */
public class e extends JMenuItem implements InterfaceC1581bs {

    /* renamed from: a, reason: collision with root package name */
    private boolean f6525a;

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1386e f6526b;

    /* renamed from: c, reason: collision with root package name */
    private InterfaceC1386e f6527c;

    public e() {
        this.f6525a = true;
        this.f6526b = null;
        this.f6527c = null;
    }

    public e(String str, boolean z2) {
        super(str);
        this.f6525a = true;
        this.f6526b = null;
        this.f6527c = null;
        a(z2);
    }

    public void a(boolean z2) {
        this.f6525a = z2;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public InterfaceC1386e e() {
        return this.f6526b;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public void a(InterfaceC1386e interfaceC1386e) {
        this.f6526b = interfaceC1386e;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public InterfaceC1386e i() {
        return this.f6527c;
    }

    public void b(InterfaceC1386e interfaceC1386e) {
        this.f6527c = interfaceC1386e;
    }
}
