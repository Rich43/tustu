package bA;

import c.InterfaceC1386e;
import com.efiAnalytics.ui.InterfaceC1581bs;
import com.efiAnalytics.ui.eA;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:bA/c.class */
public class c extends JCheckBoxMenuItem implements InterfaceC1581bs {

    /* renamed from: a, reason: collision with root package name */
    private boolean f6521a;

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1386e f6522b;

    /* renamed from: c, reason: collision with root package name */
    private InterfaceC1386e f6523c;

    /* renamed from: d, reason: collision with root package name */
    private eA f6524d;

    public c() {
        this.f6521a = true;
        this.f6522b = null;
        this.f6523c = null;
        this.f6524d = null;
    }

    public c(String str, boolean z2, boolean z3) {
        super(str, z3);
        this.f6521a = true;
        this.f6522b = null;
        this.f6523c = null;
        this.f6524d = null;
        a(z2);
    }

    public void a(boolean z2) {
        this.f6521a = z2;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public InterfaceC1386e e() {
        return this.f6522b;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public void a(InterfaceC1386e interfaceC1386e) {
        this.f6522b = interfaceC1386e;
    }

    public eA b() {
        return this.f6524d;
    }

    public void a(eA eAVar) {
        this.f6524d = eAVar;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1581bs
    public InterfaceC1386e i() {
        return this.f6523c;
    }
}
