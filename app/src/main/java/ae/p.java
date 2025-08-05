package ae;

import G.C0123f;
import G.J;
import G.bS;
import G.cP;
import bH.C0995c;
import bH.W;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ae/p.class */
public class p implements A.g {

    /* renamed from: d, reason: collision with root package name */
    private A.f f4426d = null;

    /* renamed from: e, reason: collision with root package name */
    private bS f4427e = null;

    /* renamed from: a, reason: collision with root package name */
    q f4428a = null;

    /* renamed from: b, reason: collision with root package name */
    C0123f f4429b = new C0123f();

    /* renamed from: c, reason: collision with root package name */
    long f4430c = System.currentTimeMillis();

    public byte[] a(byte[] bArr, int i2) throws v {
        byte[] bArrA = a(bArr);
        byte[] bArrB = b(bArrA, b() == null ? i2 : i2 + b().a());
        if (bArrB != null && bArrB.length > 0) {
            bArrB = a(bArrA, bArrB);
        }
        return bArrB;
    }

    public byte[] b(byte[] bArr, int i2) {
        return a(bArr, i2, 250);
    }

    public byte[] a(byte[] bArr, int i2, int i3) throws IOException {
        byte[] bArrB = i2 > 0 ? this.f4429b.b(i2) : this.f4429b.b(1024);
        int iO = i3 + a().o();
        int i4 = 0;
        InputStream inputStreamI = a().i();
        if (inputStreamI.available() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Purging orphan bytes:\n");
            while (inputStreamI.available() > 0) {
                stringBuffer.append("0x").append(Integer.toString(inputStreamI.read(), 16).toUpperCase()).append(" ");
            }
            a(stringBuffer.toString());
        }
        if (J.I()) {
            a("TX", bArr);
        }
        if (1 != 0) {
            a().j().write(bArr);
            a().j().flush();
        } else {
            OutputStream outputStreamJ = a().j();
            for (byte b2 : bArr) {
                outputStreamJ.write(b2);
                a(2);
                outputStreamJ.flush();
            }
        }
        if (i2 > 0) {
            a(1);
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        while (true) {
            if (i4 >= i2) {
                break;
            }
            if (inputStreamI.available() > 0) {
                int i5 = i4;
                i4++;
                bArrB[i5] = (byte) inputStreamI.read();
            } else {
                try {
                    Thread.sleep(3L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
            if (jCurrentTimeMillis2 > iO && i4 < i2) {
                a("READ Timout after " + jCurrentTimeMillis2 + "ms. Bytes Read:" + i4 + " bytes, Expected:" + i2 + " Raw buffer:\n" + C0995c.d(bArrB));
                if (i4 > 0) {
                    byte[] bArrB2 = this.f4429b.b(i4);
                    System.arraycopy(bArrB2, 0, bArrB, 0, bArrB2.length);
                    bArrB = bArrB2;
                } else {
                    bArrB = new byte[0];
                }
            }
        }
        if (i4 > 0 && bArrB.length != i4) {
            byte[] bArr2 = new byte[i4];
            System.arraycopy(bArrB, 0, bArr2, 0, bArr2.length);
            bArrB = bArr2;
        } else if (i4 == 0) {
            bArrB = new byte[0];
        }
        if (J.I() && i2 > 0) {
            a("RX", bArrB);
        }
        return bArrB;
    }

    private byte[] a(byte[] bArr) {
        return this.f4428a.b(this.f4427e) == null ? bArr : this.f4428a.b(this.f4427e).a(bArr);
    }

    private void a(String str, byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str).append(": ");
        for (byte b2 : bArr) {
            sb.append(W.a(Integer.toHexString(255 & b2), '0', 2)).append(" ");
        }
        System.out.println(sb.toString());
    }

    private void a(String str) {
        System.out.println("Time: " + ((System.currentTimeMillis() - this.f4430c) / 1000.0d) + ", " + str);
    }

    private byte[] a(byte[] bArr, byte[] bArr2) throws v {
        if (this.f4428a.a(this.f4427e) == null) {
            return bArr2;
        }
        cP cPVarA = this.f4428a.a(this.f4427e);
        if (cPVarA.a(bArr, bArr2)) {
            return cPVarA.a(bArr2);
        }
        v vVar = new v(cPVarA.c());
        vVar.a(cPVarA.b(cPVarA.d(bArr2)));
        throw vVar;
    }

    private cP b() {
        if (this.f4428a.a(this.f4427e) == null) {
            return null;
        }
        return this.f4428a.a(this.f4427e);
    }

    @Override // A.g, A.h
    public A.f a() {
        return this.f4426d;
    }

    public void a(A.f fVar) {
        this.f4426d = fVar;
    }

    public void a(q qVar) {
        this.f4428a = qVar;
    }

    private void a(int i2) {
        try {
            Thread.sleep(i2);
        } catch (InterruptedException e2) {
            Logger.getLogger(p.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
