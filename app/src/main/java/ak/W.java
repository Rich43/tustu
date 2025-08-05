package ak;

import a.C0202b;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:ak/W.class */
public class W extends C0546f {

    /* renamed from: a, reason: collision with root package name */
    File f4617a;

    /* renamed from: b, reason: collision with root package name */
    C0543c f4618b;

    /* renamed from: H, reason: collision with root package name */
    boolean f4619H;

    /* renamed from: I, reason: collision with root package name */
    int f4620I;

    /* renamed from: J, reason: collision with root package name */
    float f4621J;

    /* renamed from: K, reason: collision with root package name */
    float f4622K;

    /* renamed from: L, reason: collision with root package name */
    int f4623L;

    /* renamed from: M, reason: collision with root package name */
    int f4624M;

    /* renamed from: N, reason: collision with root package name */
    private M.a f4625N;

    public W() {
        super("\t", false);
        this.f4617a = null;
        this.f4618b = null;
        this.f4619H = false;
        this.f4620I = -1;
        this.f4621J = -1.0f;
        this.f4622K = 0.0f;
        this.f4623L = -1;
        this.f4624M = 0;
        this.f4625N = M.a.a(0.0d, 0.0d, 50.0d, Math.pow(3.0d, 2.0d) / 2.0d, Math.pow(5000.0d, 2.0d));
    }

    @Override // ak.C0546f, W.V
    public boolean a(String str) throws V.a {
        this.f4837t = true;
        try {
            File file = new File(str);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[2];
            fileInputStream.read(bArr);
            try {
                fileInputStream.close();
            } catch (Exception e2) {
            }
            if (bArr[0] == 80 && bArr[1] == 75) {
                ArrayList arrayListA = bH.ad.a(file, file.getParentFile(), "maxxecu-log");
                if (arrayListA.size() <= 0) {
                    throw new V.a("Unable to open file:\n" + str);
                }
                str = ((File) arrayListA.get(0)).getAbsolutePath();
                this.f4617a = (File) arrayListA.get(0);
            }
            return super.a(str);
        } catch (FileNotFoundException e3) {
            throw new V.a("Unable to open file for reading:\n" + e3.getLocalizedMessage());
        } catch (IOException e4) {
            throw new V.a("Unable to read from file:\n" + str);
        }
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        Iterator itB = super.b();
        while (itB.hasNext()) {
            W.T t2 = (W.T) itB.next();
            arrayList.add(t2);
            if (t2 instanceof C0543c) {
                ((C0543c) t2).b("");
            }
            if (this.f4620I < 0 && (t2.a().equals("ECU on time") || t2.a().equals("Log Timestamp"))) {
                this.f4620I = i2;
                this.f4619H = t2.a().equals("Log Timestamp");
                bH.C.c("ECU on time index: " + this.f4620I);
            }
            i2++;
        }
        if (this.f4620I >= 0) {
            this.f4618b = new C0543c();
            this.f4618b.a("Time");
            this.f4618b.b(PdfOps.s_TOKEN);
            this.f4618b.a(3);
            arrayList.add(this.f4618b);
        }
        return arrayList.iterator();
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v3, types: [double[], double[][]] */
    @Override // ak.C0546f, W.V
    public float[] c() throws V.a {
        float f2;
        float[] fArrC = super.c();
        if (this.f4618b != null) {
            if (this.f4619H) {
                float f3 = fArrC[this.f4620I] / 1000.0f;
                if (this.f4621J == -1.0f) {
                    this.f4621J = f3;
                } else {
                    this.f4621J += f3;
                }
                fArrC[fArrC.length - 1] = this.f4621J;
            } else {
                float f4 = fArrC[this.f4620I];
                if (f4 >= 15.25d) {
                }
                if (this.f4621J != f4 || this.f4621J == -1.0f) {
                    if (this.f4621J == -1.0f) {
                        this.f4625N.i(new C0202b(new double[]{new double[]{f4}, new double[]{0.0d}, new double[]{0.0d}, new double[]{0.0d}}));
                        this.f4622K = 0.0f;
                    } else {
                        this.f4622K = f4 - this.f4621J;
                    }
                    this.f4621J = f4;
                    if (this.f4623L >= 0) {
                        this.f4624M = this.f4623L + 1;
                    }
                    this.f4623L = 0;
                    f2 = 0.0f;
                } else {
                    this.f4623L++;
                    f2 = this.f4624M > 0 ? this.f4622K * (this.f4623L / this.f4624M) : 0.0f;
                }
                this.f4625N.a();
                this.f4625N.a(new C0202b(new double[]{new double[]{f4 + f2}, new double[]{this.f4621J}}));
                fArrC[fArrC.length - 1] = (float) this.f4625N.b().a(0, 0);
            }
        }
        return fArrC;
    }

    @Override // ak.C0546f, W.V
    public void a() {
        super.a();
        if (this.f4617a != null) {
            this.f4617a.delete();
        }
    }
}
