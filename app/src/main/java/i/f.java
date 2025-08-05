package I;

import G.C0113cs;
import G.T;
import G.aF;
import G.aG;
import G.bS;
import a.C0202b;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: TunerStudioMS.jar:I/f.class */
public class f implements aF, aG {

    /* renamed from: a, reason: collision with root package name */
    public static String f1358a = "runtimeDataRate";

    /* renamed from: b, reason: collision with root package name */
    float f1359b = 0.0f;

    /* renamed from: c, reason: collision with root package name */
    Queue f1360c = new LinkedList();

    /* renamed from: d, reason: collision with root package name */
    int f1361d = 10;

    /* renamed from: e, reason: collision with root package name */
    private M.a f1362e = M.a.a(0.0d, 0.0d, 50.0d, Math.pow(3.0d, 2.0d) / 2.0d, Math.pow(5000.0d, 2.0d));

    /* JADX WARN: Type inference failed for: r3v1, types: [double[], double[][]] */
    @Override // G.aF
    public void a(String str, byte[] bArr) {
        if (T.a().c() == null || !T.a().c().c().equals(str)) {
            return;
        }
        long jNanoTime = System.nanoTime();
        int size = this.f1360c.size();
        float fLongValue = size == 0 ? 10.0f : size < this.f1361d ? (size * 1.0E9f) / (jNanoTime - ((Long) this.f1360c.peek()).longValue()) : (size * 1.0E9f) / (jNanoTime - ((Long) this.f1360c.remove()).longValue());
        float f2 = this.f1359b / 8.0f;
        this.f1362e.i(new C0202b(new double[]{new double[]{fLongValue}, new double[]{0.0d}, new double[]{0.0d}, new double[]{0.0d}}));
        this.f1362e.a();
        this.f1359b = ((this.f1359b * f2) + ((float) this.f1362e.b().a(0, 0))) / (f2 + 1.0f);
        C0113cs.a().a(f1358a, this.f1359b * 1.0f);
        if (size > fLongValue * 1.15d) {
            this.f1361d = (int) Math.ceil(fLongValue);
            this.f1360c.clear();
        } else if (size < fLongValue * 0.85d) {
            this.f1361d = (int) Math.ceil(fLongValue);
        }
        while (this.f1360c.size() > this.f1361d) {
            this.f1360c.remove();
        }
        this.f1360c.add(Long.valueOf(jNanoTime));
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        this.f1359b = 0.0f;
        C0113cs.a().a(f1358a, 0.0d);
    }
}
