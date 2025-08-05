package bN;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bN/d.class */
public class d {

    /* renamed from: a, reason: collision with root package name */
    private byte[] f7224a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f7225b = false;

    public d(int i2) {
        this.f7224a = new byte[i2];
    }

    public void a(int i2) {
        this.f7224a = C0995c.a(i2, this.f7224a, c());
    }

    public int a() {
        return C0995c.a(this.f7224a, 0, this.f7224a.length, this.f7225b, false);
    }

    public int b() {
        return this.f7224a.length;
    }

    public boolean c() {
        return this.f7225b;
    }

    public void a(boolean z2) {
        this.f7225b = z2;
    }

    public byte[] d() {
        return this.f7224a;
    }

    public void a(byte[] bArr) {
        this.f7224a = bArr;
    }
}
