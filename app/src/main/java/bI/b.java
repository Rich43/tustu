package bI;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: TunerStudioMS.jar:bI/b.class */
public class b extends FilterInputStream {

    /* renamed from: a, reason: collision with root package name */
    private boolean f7077a;

    /* renamed from: b, reason: collision with root package name */
    private int f7078b;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f7079c;

    /* renamed from: d, reason: collision with root package name */
    private int f7080d;

    /* renamed from: e, reason: collision with root package name */
    private int f7081e;

    /* renamed from: f, reason: collision with root package name */
    private int f7082f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f7083g;

    /* renamed from: h, reason: collision with root package name */
    private int f7084h;

    /* renamed from: i, reason: collision with root package name */
    private byte[] f7085i;

    public b(InputStream inputStream, int i2) {
        super(inputStream);
        this.f7084h = i2;
        this.f7083g = (i2 & 8) > 0;
        this.f7077a = (i2 & 1) > 0;
        this.f7080d = this.f7077a ? 4 : 3;
        this.f7079c = new byte[this.f7080d];
        this.f7078b = -1;
        this.f7082f = 0;
        this.f7085i = a.c(i2);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int i2;
        int i3;
        if (this.f7078b < 0) {
            if (this.f7077a) {
                byte[] bArr = new byte[3];
                int i4 = 0;
                for (int i5 = 0; i5 < 3 && (i3 = this.in.read()) >= 0; i5++) {
                    bArr[i5] = (byte) i3;
                    i4++;
                }
                if (i4 <= 0) {
                    return -1;
                }
                a.b(bArr, 0, i4, this.f7079c, 0, this.f7084h);
                this.f7078b = 0;
                this.f7081e = 4;
            } else {
                byte[] bArr2 = new byte[4];
                int i6 = 0;
                while (i6 < 4) {
                    do {
                        i2 = this.in.read();
                        if (i2 < 0) {
                            break;
                        }
                    } while (this.f7085i[i2 & 127] <= -5);
                    if (i2 < 0) {
                        break;
                    }
                    bArr2[i6] = (byte) i2;
                    i6++;
                }
                if (i6 != 4) {
                    if (i6 == 0) {
                        return -1;
                    }
                    throw new IOException("Improperly padded Base64 input.");
                }
                this.f7081e = a.b(bArr2, 0, this.f7079c, 0, this.f7084h);
                this.f7078b = 0;
            }
        }
        if (this.f7078b < 0) {
            throw new IOException("Error in Base64 code reading stream.");
        }
        if (this.f7078b >= this.f7081e) {
            return -1;
        }
        if (this.f7077a && this.f7083g && this.f7082f >= 76) {
            this.f7082f = 0;
            return 10;
        }
        this.f7082f++;
        byte[] bArr3 = this.f7079c;
        int i7 = this.f7078b;
        this.f7078b = i7 + 1;
        byte b2 = bArr3[i7];
        if (this.f7078b >= this.f7080d) {
            this.f7078b = -1;
        }
        return b2 & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = 0;
        while (true) {
            if (i4 >= i3) {
                break;
            }
            int i5 = read();
            if (i5 >= 0) {
                bArr[i2 + i4] = (byte) i5;
                i4++;
            } else if (i4 == 0) {
                return -1;
            }
        }
        return i4;
    }
}
