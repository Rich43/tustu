package ak;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: ak.ab, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/ab.class */
class C0533ab extends C0541aj {

    /* renamed from: a, reason: collision with root package name */
    protected int f4669a;

    /* renamed from: b, reason: collision with root package name */
    protected int f4670b;

    /* renamed from: c, reason: collision with root package name */
    protected int f4671c;

    /* renamed from: d, reason: collision with root package name */
    protected int f4672d;

    /* renamed from: e, reason: collision with root package name */
    protected String f4673e;

    /* renamed from: f, reason: collision with root package name */
    protected String f4674f;

    /* renamed from: g, reason: collision with root package name */
    protected String f4675g;

    /* renamed from: h, reason: collision with root package name */
    protected String f4676h;

    /* renamed from: i, reason: collision with root package name */
    protected String f4677i;

    /* renamed from: j, reason: collision with root package name */
    protected String f4678j;

    /* renamed from: k, reason: collision with root package name */
    protected String f4679k;

    /* renamed from: l, reason: collision with root package name */
    protected al f4680l;

    /* renamed from: m, reason: collision with root package name */
    protected ak f4681m;

    /* renamed from: n, reason: collision with root package name */
    protected List f4682n;

    /* renamed from: o, reason: collision with root package name */
    final /* synthetic */ Z f4683o;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0533ab(Z z2, int i2) {
        super(z2, i2);
        this.f4683o = z2;
        this.f4679k = "";
        if (this.f4669a != 0 && this.f4672d > 0) {
            this.f4682n = new ArrayList(this.f4672d);
            int i3 = this.f4669a;
            for (int i4 = 0; i4 < this.f4672d; i4++) {
                int i5 = 0;
                C0534ac c0534ac = new C0534ac(this, i3);
                Iterator it = c0534ac.f4688e.iterator();
                while (it.hasNext()) {
                    i5 += ((C0535ad) it.next()).f4696g;
                }
                if (i5 > 0) {
                    this.f4682n.add(c0534ac);
                }
                i3 = c0534ac.f4684a;
            }
        }
        if (this.f4670b != 0) {
            this.f4680l = new al(z2, this.f4670b);
        }
        if (this.f4671c != 0) {
            this.f4681m = new ak(z2, this.f4671c);
        }
    }
}
