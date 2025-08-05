package bN;

import bH.C;

/* loaded from: TunerStudioMS.jar:bN/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private byte f7221a = 64;

    public boolean a() {
        return (this.f7221a | 64) == this.f7221a;
    }

    public int b() {
        if ((this.f7221a | 4) == this.f7221a && (this.f7221a | 2) == this.f7221a) {
            C.a("Invalid Reserved Address Granularity!");
            return -1;
        }
        if ((this.f7221a | 2) == this.f7221a) {
            return 4;
        }
        return (this.f7221a | 4) == this.f7221a ? 2 : 1;
    }

    public boolean c() {
        return (this.f7221a | 1) == this.f7221a;
    }

    public byte d() {
        return this.f7221a;
    }

    public void a(byte b2) {
        this.f7221a = b2;
    }
}
