package G;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/* loaded from: TunerStudioMS.jar:G/bO.class */
public class bO extends Q implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    public static String f856a = "TuningView";

    /* renamed from: b, reason: collision with root package name */
    public static String f857b = "DashBoard";

    /* renamed from: c, reason: collision with root package name */
    public static String f858c = "Other";

    /* renamed from: d, reason: collision with root package name */
    private final byte[] f855d = new byte[8192];

    /* renamed from: e, reason: collision with root package name */
    private String f859e = null;

    /* renamed from: f, reason: collision with root package name */
    private String f860f = null;

    /* renamed from: g, reason: collision with root package name */
    private byte[] f861g = null;

    public bO(String str) {
        super.v(str);
    }

    public byte[] a() throws IOException {
        if (this.f861g != null) {
            return this.f861g;
        }
        if (this.f860f == null) {
            throw new IOException("Either Decoded Data or Encoded Data must be set.");
        }
        InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(bI.d.a(this.f860f)));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int i2 = inflaterInputStream.read(this.f855d);
            if (i2 <= 0) {
                this.f861g = byteArrayOutputStream.toByteArray();
                return this.f861g;
            }
            byteArrayOutputStream.write(this.f855d, 0, i2);
        }
    }

    public void a(String str) {
        this.f860f = str;
        this.f861g = null;
    }

    public String b() throws IOException {
        if (this.f860f != null) {
            return this.f860f;
        }
        if (this.f861g == null) {
            throw new IOException("Neither Base64 or decoded data has been set.");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
        Throwable th = null;
        try {
            try {
                deflaterOutputStream.write(this.f861g);
                if (deflaterOutputStream != null) {
                    if (0 != 0) {
                        try {
                            deflaterOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        deflaterOutputStream.close();
                    }
                }
                this.f860f = bI.d.a(byteArrayOutputStream.toByteArray());
                return this.f860f;
            } finally {
            }
        } catch (Throwable th3) {
            if (deflaterOutputStream != null) {
                if (th != null) {
                    try {
                        deflaterOutputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    deflaterOutputStream.close();
                }
            }
            throw th3;
        }
    }

    public void a(byte[] bArr) {
        this.f861g = bArr;
    }
}
