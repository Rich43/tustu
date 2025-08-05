package aZ;

import ac.o;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/* loaded from: TunerStudioMS.jar:aZ/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    int f4088a = 0;

    /* renamed from: b, reason: collision with root package name */
    String f4089b = "";

    /* renamed from: c, reason: collision with root package name */
    BufferedInputStream f4090c = null;

    /* renamed from: d, reason: collision with root package name */
    int[] f4091d = {0};

    /* renamed from: e, reason: collision with root package name */
    int f4092e = 0;

    /* renamed from: f, reason: collision with root package name */
    String[] f4093f = null;

    /* renamed from: g, reason: collision with root package name */
    String[] f4094g = null;

    /* renamed from: h, reason: collision with root package name */
    int f4095h = 0;

    /* renamed from: i, reason: collision with root package name */
    long f4096i = 0;

    /* renamed from: j, reason: collision with root package name */
    static int f4097j = 1024;

    public void a(String str) {
        f();
        File file = new File(str);
        this.f4096i = file.length();
        this.f4090c = new BufferedInputStream(new FileInputStream(file));
    }

    public int[] a() throws IOException {
        int iD = (int) this.f4096i;
        try {
            iD = d();
        } catch (Exception e2) {
        }
        int[] iArr = new int[iD];
        int i2 = 0;
        this.f4090c.skip(this.f4088a);
        do {
            iArr[i2] = this.f4090c.read();
            i2++;
            if (i2 >= iArr.length) {
                break;
            }
        } while (iArr[i2] != -1);
        return iArr;
    }

    public int b() {
        return this.f4095h;
    }

    public int c() {
        int i2 = 0;
        for (int i3 : e()) {
            i2 += i3;
        }
        return i2 + 4;
    }

    public int d() {
        return c() * b();
    }

    public int[] e() {
        String[] strArrB = b(o.f4236c);
        this.f4092e = 0;
        int[] iArr = new int[strArrB.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = Integer.parseInt(strArrB[i2]);
            this.f4092e = iArr[i2];
        }
        this.f4092e = 4;
        return iArr;
    }

    private String[] b(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(this.f4089b.substring(this.f4089b.indexOf(61, this.f4089b.indexOf(str)) + 1, this.f4089b.indexOf(10, this.f4089b.indexOf(str))), ";");
        String[] strArr = new String[stringTokenizer.countTokens()];
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            strArr[i2] = stringTokenizer.nextToken();
            i2++;
        }
        return strArr;
    }

    public void f() {
        if (this.f4090c != null) {
            try {
                this.f4090c.close();
                this.f4091d[0] = 0;
                this.f4093f = null;
                this.f4094g = null;
                this.f4092e = 0;
            } catch (Exception e2) {
            }
        }
    }
}
