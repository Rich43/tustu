package bO;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bO/m.class */
public class m {

    /* renamed from: a, reason: collision with root package name */
    private int f7381a = 0;

    public int a() {
        return 7 & this.f7381a;
    }

    public boolean b() {
        return (this.f7381a & 8) != 0;
    }

    public int c() {
        return (this.f7381a & 240) >> 4;
    }

    public int d() {
        return this.f7381a;
    }

    public void a(byte b2) {
        this.f7381a = C0995c.a(b2);
    }

    public String toString() {
        return "timestampModeByte=" + Integer.toBinaryString(this.f7381a) + ", timestampSize=" + a() + ", timestampUnits=" + c() + ", fixedTimestamp=" + b();
    }
}
