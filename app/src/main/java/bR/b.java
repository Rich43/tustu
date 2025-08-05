package bR;

import bH.C;
import bH.C0995c;
import bH.ac;
import bN.o;
import bN.t;
import java.util.zip.CRC32;

/* loaded from: TunerStudioMS.jar:bR/b.class */
public class b {

    /* renamed from: a, reason: collision with root package name */
    ac f7499a = new ac();

    /* renamed from: b, reason: collision with root package name */
    CRC32 f7500b = new CRC32();

    /* renamed from: c, reason: collision with root package name */
    int f7501c = 8;

    /* renamed from: d, reason: collision with root package name */
    boolean f7502d = true;

    public boolean a() {
        return this.f7502d;
    }

    public boolean a(bN.k kVar, byte[] bArr, t tVar) throws o {
        byte[] bArrC = tVar.c();
        if (bArrC.length != 7) {
            C.c("Invalid CRC Response Packet: \n" + C0995c.d(bArrC));
            throw new o("Invalid Checksum packet length. Expected 8, received: " + (bArrC.length + 1));
        }
        int iA = C0995c.a(bArrC[0]);
        if (iA != 8) {
            if (iA != 9) {
                this.f7502d = false;
                return false;
            }
            this.f7501c = 9;
            this.f7500b.reset();
            this.f7500b.update(bArr);
            return ((int) this.f7500b.getValue()) == C0995c.a(bArrC, 3, 4, kVar.g(), false);
        }
        this.f7501c = 8;
        this.f7499a.b();
        this.f7499a.a(bArr);
        int iA2 = this.f7499a.a();
        int iA3 = C0995c.a(bArrC, 3, 4, kVar.g(), false);
        C.c("Controller CRC: 0x" + Integer.toHexString(iA3).toUpperCase() + ", Local CRC: 0x" + Integer.toHexString(iA2).toUpperCase());
        if (iA2 != iA3) {
            C.c("CRC doesn't match!");
        }
        return iA2 == iA3;
    }

    public byte[] a(byte[] bArr) {
        if (this.f7501c == 8) {
            this.f7499a.b();
            this.f7499a.a(bArr);
            return C0995c.a(this.f7499a.a(), new byte[2], true);
        }
        if (this.f7501c != 9) {
            return new byte[0];
        }
        this.f7500b.reset();
        this.f7500b.update(bArr);
        return C0995c.a((int) this.f7500b.getValue(), new byte[4], true);
    }
}
