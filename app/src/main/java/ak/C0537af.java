package ak;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* renamed from: ak.af, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/af.class */
class C0537af extends C0541aj {

    /* renamed from: a, reason: collision with root package name */
    protected String f4722a;

    /* renamed from: b, reason: collision with root package name */
    protected int f4723b;

    /* renamed from: c, reason: collision with root package name */
    protected int f4724c;

    /* renamed from: d, reason: collision with root package name */
    protected double[] f4725d;

    /* renamed from: e, reason: collision with root package name */
    protected String[] f4726e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ C0536ae f4727f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0537af(C0536ae c0536ae, int i2) {
        super(c0536ae.f4721s.f4702m.f4689f.f4683o, i2);
        this.f4727f = c0536ae;
        if (this.f4724c > 0) {
            switch (this.f4723b) {
                case 0:
                    this.f4725d = new double[this.f4724c];
                    for (int i3 = 0; i3 < this.f4724c; i3++) {
                        this.f4725d[i3] = ByteBuffer.wrap(c0536ae.f4721s.f4702m.f4689f.f4683o.f4639e, this.f4739u, 8).order(c0536ae.f4721s.f4702m.f4689f.f4683o.f4645k ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN).getDouble();
                        this.f4739u += 8;
                    }
                    return;
                case 9:
                case 132:
                case 133:
                case 65535:
                    return;
                case 11:
                    this.f4725d = new double[this.f4724c];
                    this.f4726e = new String[this.f4724c];
                    for (int i4 = 0; i4 < this.f4724c; i4++) {
                        this.f4725d[i4] = ByteBuffer.wrap(c0536ae.f4721s.f4702m.f4689f.f4683o.f4639e, this.f4739u, 8).order(c0536ae.f4721s.f4702m.f4689f.f4683o.f4645k ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN).getDouble();
                        this.f4739u += 8;
                        this.f4726e[i4] = bH.W.k(c0536ae.f4721s.f4702m.f4689f.f4683o.a(this.f4739u, 32));
                        this.f4739u += 32;
                    }
                    return;
                default:
                    throw new IllegalArgumentException("Channel conversion type " + c0536ae.f4714l.f4723b + " is not supported");
            }
        }
    }
}
