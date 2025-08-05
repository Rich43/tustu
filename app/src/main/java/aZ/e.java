package aZ;

import G.R;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: TunerStudioMS.jar:aZ/e.class */
public class e extends ac.h {

    /* renamed from: b, reason: collision with root package name */
    private static e f4102b = null;

    /* renamed from: a, reason: collision with root package name */
    byte f4103a = 0;

    private e() {
    }

    public static e a() {
        if (f4102b == null) {
            f4102b = new e();
        }
        return f4102b;
    }

    @Override // ac.h
    protected void a(R[] rArr, OutputStream outputStream) {
        try {
            outputStream.write(new byte[]{70, 82, 68, 0, 0, 0, 0, 1});
            outputStream.write(b());
            String strI = rArr[0].i();
            byte[] bArr = new byte[63];
            int i2 = 0;
            while (i2 < bArr.length) {
                bArr[i2] = i2 < strI.length() ? strI.getBytes()[i2] : (byte) 0;
                i2++;
            }
            outputStream.write(bArr);
            outputStream.write(0);
            outputStream.write(0);
            outputStream.write(0);
            outputStream.write(81);
            outputStream.write(0);
            outputStream.write(rArr[0].O().n());
            outputStream.flush();
        } catch (IOException e2) {
            C.a("Unable to write to Logfile.", e2, null);
        }
    }

    private byte[] b() {
        return C0995c.a((int) (System.currentTimeMillis() / 1000), new byte[4], true);
    }

    @Override // ac.h
    protected void a(OutputStream outputStream, String str) {
        try {
            outputStream.write(2);
            outputStream.write(b());
        } catch (IOException e2) {
            C.a("Unable to write MARK to Logfile.", e2, null);
        }
    }

    @Override // ac.h
    protected void a(OutputStream outputStream, byte[][] bArr) {
        try {
            outputStream.write(1);
            byte b2 = this.f4103a;
            this.f4103a = (byte) (b2 + 1);
            outputStream.write(b2);
            for (int i2 = 0; i2 < bArr[0].length; i2++) {
                outputStream.write(bArr[0][i2]);
            }
            outputStream.flush();
        } catch (IOException e2) {
            C.a("Unable to write MARK to Logfile.", e2, null);
        }
    }

    @Override // ac.h
    protected void a(OutputStream outputStream) {
    }
}
