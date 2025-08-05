package ak;

import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:ak/al.class */
class al extends C0541aj {

    /* renamed from: a, reason: collision with root package name */
    protected int f4745a;

    /* renamed from: b, reason: collision with root package name */
    protected String f4746b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ Z f4747c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public al(Z z2, int i2) {
        super(z2, i2);
        this.f4747c = z2;
        this.f4746b = bH.W.k(z2.a(i2 + 4, this.f4745a - 4));
    }

    @Override // ak.C0541aj
    public String toString() {
        return PdfOps.DOUBLE_QUOTE__TOKEN + this.f4746b + PdfOps.DOUBLE_QUOTE__TOKEN;
    }
}
