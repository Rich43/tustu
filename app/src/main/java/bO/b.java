package bO;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bO/b.class */
public class b {

    /* renamed from: a, reason: collision with root package name */
    public final int f7331a = 0;

    /* renamed from: b, reason: collision with root package name */
    public final int f7332b = 1;

    /* renamed from: c, reason: collision with root package name */
    public final int f7333c = 2;

    /* renamed from: d, reason: collision with root package name */
    public final int f7334d = 3;

    /* renamed from: e, reason: collision with root package name */
    public final int f7335e = 4;

    /* renamed from: f, reason: collision with root package name */
    public final int f7336f = 5;

    /* renamed from: g, reason: collision with root package name */
    public final int f7337g = 0;

    /* renamed from: h, reason: collision with root package name */
    public final int f7338h = 1;

    /* renamed from: i, reason: collision with root package name */
    public final int f7339i = 2;

    /* renamed from: j, reason: collision with root package name */
    public final int f7340j = 3;

    /* renamed from: k, reason: collision with root package name */
    int f7341k = 0;

    public void a(byte b2) {
        this.f7341k = C0995c.a(b2);
    }

    public int a() {
        return (this.f7341k >> 6) & 3;
    }

    public byte b() {
        return (byte) this.f7341k;
    }
}
