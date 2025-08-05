package bQ;

import G.C0129l;
import G.C0130m;
import G.F;
import G.R;
import G.T;
import G.Y;
import bH.C;
import bH.C0995c;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.telnet.TelnetCommand;

/* loaded from: TunerStudioMS.jar:bQ/t.class */
public class t {

    /* renamed from: a, reason: collision with root package name */
    private static t f7475a = null;

    /* renamed from: b, reason: collision with root package name */
    private static final byte[] f7476b = "ALL_PC_VARS".getBytes();

    private t() {
    }

    public static t a() {
        if (f7475a == null) {
            f7475a = new t();
        }
        return f7475a;
    }

    public bN.l b() {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(255);
        lVarB.b(new byte[]{0});
        return lVarB;
    }

    public bN.l c() {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(254);
        lVarB.b(new byte[0]);
        return lVarB;
    }

    public bN.t a(byte b2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(250);
        lVarB.b(new byte[1]);
        lVarB.c()[0] = b2;
        return lVarB;
    }

    public bN.t a(int i2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(235);
        lVarB.b(new byte[]{2, 0, (byte) i2});
        return lVarB;
    }

    public List a(F f2, bN.k kVar, int i2, int i3) {
        if (i2 > f2.g() || i2 < 0) {
            throw new C0129l("Invalid Page Number: " + i2);
        }
        ArrayList arrayList = new ArrayList();
        int iU = 0;
        do {
            k kVar2 = new k();
            bN.l lVarB = bN.u.a().b();
            lVarB.a(245);
            int iMin = Math.min(255, (i3 - iU) / kVar.u());
            lVarB.b(new byte[]{(byte) iMin});
            kVar2.a(lVarB);
            int iU2 = (kVar.u() * iMin) / (kVar.i() - 1);
            if ((kVar.u() * iMin) % (kVar.i() - 1) > 0) {
                iU2++;
            }
            kVar2.a(iU2);
            iU += iMin * kVar.u();
            arrayList.add(kVar2);
        } while (iU < i3);
        return arrayList;
    }

    public bN.t a(bN.k kVar, int i2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(246);
        byte[] bArr = new byte[7];
        bArr[2] = 0;
        lVarB.b(C0995c.a(bArr, i2, 3, 4, kVar.g()));
        return lVarB;
    }

    public List a(bN.k kVar, F f2, C0130m c0130m) throws V.g {
        ArrayList arrayList = new ArrayList();
        if (T.a().c(f2.u()) == null) {
            throw new V.g("No Configuration found for name: " + f2.u());
        }
        byte[] bArrA = C0995c.a(c0130m.p());
        int i2 = 0;
        int iL = (kVar.l() * (kVar.i() - 2)) - 2;
        do {
            int iMin = Math.min(iL * kVar.u(), bArrA.length - i2);
            byte[] bArr = new byte[iMin];
            System.arraycopy(bArrA, i2, bArr, 0, iMin);
            arrayList.add(a(kVar, bArr, c0130m));
            i2 += iMin;
        } while (i2 < bArrA.length);
        return arrayList;
    }

    private List a(bN.k kVar, byte[] bArr, C0130m c0130m) {
        ArrayList arrayList = new ArrayList();
        int iMin = Math.min(bArr.length, kVar.i() - (16 + 1));
        byte[] bArr2 = new byte[iMin + 16];
        int length = bArr.length;
        bN.l lVarB = bN.u.a().b();
        lVarB.a(241);
        bArr2[0] = -93;
        C0995c.a(bArr2, length, 1, 2, kVar.g());
        C0995c.a(bArr2, c0130m.f(), 3, 4, kVar.g());
        C0995c.a(bArr2, c0130m.b(), 7, 4, kVar.g());
        byte b2 = 0;
        if (c0130m.g()) {
            b2 = (byte) (0 | 1);
        }
        bArr2[15] = b2;
        System.arraycopy(bArr, 0, bArr2, 16, iMin);
        lVarB.b(bArr2);
        arrayList.add(lVarB);
        int i2 = iMin;
        if (bArr.length > i2) {
            do {
                bN.l lVarB2 = bN.u.a().b();
                lVarB2.a(241);
                int iMin2 = Math.min(bArr.length - i2, kVar.i() - 2);
                byte[] bArr3 = new byte[iMin2 + 1];
                bArr3[0] = -92;
                System.arraycopy(bArr, i2, bArr3, 1, iMin2);
                lVarB2.b(bArr3);
                arrayList.add(lVarB2);
                i2 += iMin2;
            } while (i2 < bArr.length);
        }
        return arrayList;
    }

    public List b(bN.k kVar, F f2, C0130m c0130m) throws V.g {
        ArrayList arrayList = new ArrayList();
        R rC = T.a().c(f2.u());
        if (rC == null) {
            throw new V.g("No Configuration found for name: " + f2.u());
        }
        if (c0130m.o() > f2.g() || c0130m.o() < 0) {
            throw new V.g("Invalid Page Number: " + c0130m.o());
        }
        Y yH = rC.h();
        if (f2.f(c0130m.o()) < c0130m.q() + c0130m.r()) {
            throw new V.g("Invalid Length, longer than page: " + c0130m.r());
        }
        byte[] bArrA = C0995c.a(yH.a(c0130m.o(), c0130m.q(), c0130m.r()));
        int i2 = 0;
        int iL = kVar.l();
        do {
            int iMin = Math.min(iL * kVar.u(), bArrA.length - i2);
            byte[] bArr = new byte[iMin];
            System.arraycopy(bArrA, i2, bArr, 0, iMin);
            arrayList.add(a(kVar, bArr));
            i2 += iMin;
        } while (i2 < bArrA.length);
        return arrayList;
    }

    private List a(bN.k kVar, byte[] bArr) throws V.g {
        ArrayList arrayList = new ArrayList();
        int iU = kVar.u() == 1 ? 1 : kVar.u() - 1;
        int i2 = iU + 1;
        int iMin = Math.min(bArr.length, kVar.i() - i2);
        byte[] bArr2 = new byte[iMin + iU];
        int length = bArr.length / kVar.u();
        if (kVar.u() * length != bArr.length) {
            throw new V.g("Payload size incorrect for current addresse granularity");
        }
        if (bArr.length > 255 * kVar.u()) {
            throw new V.g("Too much data requested for DOWNLOAD! Requested byte count: " + bArr.length + ", Max allowed: " + (kVar.i() - iU));
        }
        bN.l lVarB = bN.u.a().b();
        lVarB.a(240);
        bArr2[0] = (byte) length;
        System.arraycopy(bArr, 0, bArr2, iU, iMin);
        lVarB.b(bArr2);
        arrayList.add(lVarB);
        int i3 = iMin;
        if (bArr.length > i3) {
            do {
                bN.l lVarB2 = bN.u.a().b();
                lVarB2.a(239);
                int iMin2 = Math.min(bArr.length - i3, kVar.i() - i2);
                byte[] bArr3 = new byte[iMin2 + iU];
                bArr3[0] = (byte) (length - (i3 / kVar.u()));
                System.arraycopy(bArr, i3, bArr3, iU, iMin2);
                lVarB2.b(bArr3);
                arrayList.add(lVarB2);
                i3 += iMin2;
            } while (i3 < bArr.length);
        }
        return arrayList;
    }

    public bN.t d() {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(252);
        lVarB.b(new byte[0]);
        return lVarB;
    }

    public bN.t b(bN.k kVar, int i2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(243);
        byte[] bArr = new byte[7];
        C0995c.a(bArr, i2, 3, 4, kVar.g());
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t a(bN.k kVar, boolean z2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(230);
        byte[] bArr = new byte[2];
        if (z2) {
            bArr[0] = 1;
        } else {
            bArr[0] = 0;
        }
        bArr[1] = 0;
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t a(bN.k kVar) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(TelnetCommand.GA);
        lVarB.b(new byte[]{1, 0, 0});
        return lVarB;
    }

    public bN.t b(bN.k kVar) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(218);
        lVarB.b(new byte[0]);
        return lVarB;
    }

    public bN.t c(bN.k kVar) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(217);
        lVarB.b(new byte[0]);
        return lVarB;
    }

    public bN.t c(bN.k kVar, int i2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(216);
        byte[] bArr = new byte[3];
        C0995c.a(bArr, i2, 1, 2, kVar.g());
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t d(bN.k kVar, int i2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(227);
        byte[] bArr = new byte[3];
        C0995c.a(bArr, i2, 1, 2, kVar.g());
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t a(bN.k kVar, int i2, int i3, int i4) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(226);
        byte[] bArr = {0, 0, 0, (byte) i3, (byte) i4};
        C0995c.a(bArr, i2, 1, 2, kVar.g());
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t a(bN.k kVar, boolean z2, boolean z3, int i2, int i3, byte b2, byte b3) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(224);
        byte[] bArr = new byte[7];
        byte b4 = 0;
        if (z2) {
            b4 = (byte) (0 | 16);
        }
        if (z3) {
            b4 = (byte) (b4 | 32);
        }
        bArr[0] = b4;
        C0995c.a(bArr, i2, 1, 2, kVar.g());
        C0995c.a(bArr, i3, 3, 2, kVar.g());
        bArr[5] = b2;
        bArr[4] = b3;
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t a(bN.k kVar, byte b2, byte b3, byte b4, long j2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(225);
        byte[] bArr = {b2, b3, b4, 0, 0, 0, 0};
        C0995c.a(bArr, (int) j2, 3, 4, kVar.g());
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t a(bN.k kVar, int i2, byte b2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(222);
        byte[] bArr = {b2, 0, 0};
        C0995c.a(bArr, i2, 1, 2, kVar.g());
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t e() {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(251);
        lVarB.b(new byte[0]);
        return lVarB;
    }

    public bN.t a(bN.k kVar, byte b2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(221);
        lVarB.b(new byte[]{b2});
        return lVarB;
    }

    public bN.t d(bN.k kVar) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(220);
        lVarB.b(new byte[0]);
        return lVarB;
    }

    public bN.t c(bN.k kVar, F f2, C0130m c0130m) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(241);
        byte[] bytes = c0130m.l().getBytes();
        byte[] bArr = new byte[bytes.length + 1];
        bArr[0] = -95;
        System.arraycopy(bytes, 0, bArr, 1, bytes.length);
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t e(bN.k kVar, int i2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(241);
        lVarB.b(new byte[]{-89, (byte) i2});
        return lVarB;
    }

    public bN.t d(bN.k kVar, F f2, C0130m c0130m) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(241);
        byte[] bytes = c0130m.l().getBytes();
        if (C0995c.c(f7476b, bytes)) {
            lVarB.b(new byte[]{-96, (byte) bV.g.f7642b});
            return lVarB;
        }
        byte[] bArr = new byte[bytes.length + 2];
        bArr[0] = -96;
        bArr[1] = (byte) bV.g.f7641a;
        System.arraycopy(bytes, 0, bArr, 2, bytes.length);
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t f(bN.k kVar, int i2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(241);
        byte[] bArr = new byte[6];
        bArr[0] = -91;
        bArr[1] = (byte) bV.e.f7638a;
        if (i2 < 0 || i2 > 1000000) {
            i2 = 0;
        }
        C0995c.a(bArr, i2, 2, 4, kVar.g());
        C.c("Pause runtime reads for: " + i2 + ", packet: " + C0995c.d(bArr));
        lVarB.b(bArr);
        return lVarB;
    }

    public bN.t b(int i2) {
        bN.l lVarB = bN.u.a().b();
        lVarB.a(241);
        byte[] bArr = new byte[6];
        bArr[0] = -90;
        bArr[1] = (byte) i2;
        lVarB.b(bArr);
        return lVarB;
    }
}
