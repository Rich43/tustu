package ak;

import java.util.ArrayList;
import java.util.List;

/* renamed from: ak.ac, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/ac.class */
class C0534ac extends C0541aj {

    /* renamed from: a, reason: collision with root package name */
    protected int f4684a;

    /* renamed from: b, reason: collision with root package name */
    protected int f4685b;

    /* renamed from: c, reason: collision with root package name */
    protected int f4686c;

    /* renamed from: d, reason: collision with root package name */
    protected int f4687d;

    /* renamed from: e, reason: collision with root package name */
    protected List f4688e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ C0533ab f4689f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0534ac(C0533ab c0533ab, int i2) {
        super(c0533ab.f4683o, i2);
        this.f4689f = c0533ab;
        if (this.f4685b == 0 || this.f4687d <= 0) {
            return;
        }
        this.f4688e = new ArrayList(this.f4687d);
        int i3 = this.f4685b;
        for (int i4 = 0; i4 < this.f4687d; i4++) {
            C0535ad c0535ad = new C0535ad(this, i3);
            this.f4688e.add(c0535ad);
            i3 = c0535ad.f4690a;
        }
    }

    @Override // ak.C0541aj
    public String toString() {
        return "DataGroupBlock{\n\t nChannelGroups=" + this.f4687d + "\n\t cgBlocks=" + ((Object) this.f4688e) + "\n}";
    }
}
