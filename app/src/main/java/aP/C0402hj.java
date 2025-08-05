package aP;

import javax.swing.SwingUtilities;

/* renamed from: aP.hj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hj.class */
class C0402hj implements G.aF {

    /* renamed from: a, reason: collision with root package name */
    boolean f3579a = false;

    /* renamed from: b, reason: collision with root package name */
    long f3580b = System.currentTimeMillis();

    /* renamed from: c, reason: collision with root package name */
    int f3581c = 750;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C0394hb f3582d;

    C0402hj(C0394hb c0394hb) {
        this.f3582d = c0394hb;
    }

    @Override // G.aF
    public void a(String str, byte[] bArr) {
        if (System.currentTimeMillis() - this.f3580b > this.f3581c) {
            SwingUtilities.invokeLater(this.f3582d.f3568m);
            this.f3580b = System.currentTimeMillis();
        }
    }
}
