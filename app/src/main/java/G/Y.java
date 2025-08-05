package G;

import bH.C0995c;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:G/Y.class */
public class Y extends Q {

    /* renamed from: a, reason: collision with root package name */
    List f505a;

    /* renamed from: b, reason: collision with root package name */
    List f506b;

    /* renamed from: c, reason: collision with root package name */
    List f507c;

    /* renamed from: d, reason: collision with root package name */
    ArrayList f508d;

    /* renamed from: e, reason: collision with root package name */
    int f509e;

    /* renamed from: f, reason: collision with root package name */
    boolean f510f;

    /* renamed from: g, reason: collision with root package name */
    C0041aa f511g;

    /* renamed from: k, reason: collision with root package name */
    private R f512k;

    /* renamed from: h, reason: collision with root package name */
    boolean f513h;

    /* renamed from: l, reason: collision with root package name */
    private boolean f514l;

    /* renamed from: i, reason: collision with root package name */
    int[][] f515i;

    /* renamed from: m, reason: collision with root package name */
    private long f516m;

    /* renamed from: j, reason: collision with root package name */
    public static int f517j = Integer.MIN_VALUE;

    /* renamed from: n, reason: collision with root package name */
    private boolean f518n;

    private Y() {
        this.f505a = Collections.synchronizedList(new ArrayList());
        this.f506b = Collections.synchronizedList(new ArrayList());
        this.f507c = Collections.synchronizedList(new ArrayList());
        this.f508d = new ArrayList();
        this.f509e = -1;
        this.f510f = false;
        this.f511g = null;
        this.f512k = null;
        this.f513h = true;
        this.f514l = false;
        this.f515i = (int[][]) null;
        this.f516m = 21L;
        this.f518n = false;
    }

    public Y(R r2) {
        this.f505a = Collections.synchronizedList(new ArrayList());
        this.f506b = Collections.synchronizedList(new ArrayList());
        this.f507c = Collections.synchronizedList(new ArrayList());
        this.f508d = new ArrayList();
        this.f509e = -1;
        this.f510f = false;
        this.f511g = null;
        this.f512k = null;
        this.f513h = true;
        this.f514l = false;
        this.f515i = (int[][]) null;
        this.f516m = 21L;
        this.f518n = false;
        this.f512k = r2;
        a(r2.O());
    }

    public Y(R r2, int[][] iArr) {
        this.f505a = Collections.synchronizedList(new ArrayList());
        this.f506b = Collections.synchronizedList(new ArrayList());
        this.f507c = Collections.synchronizedList(new ArrayList());
        this.f508d = new ArrayList();
        this.f509e = -1;
        this.f510f = false;
        this.f511g = null;
        this.f512k = null;
        this.f513h = true;
        this.f514l = false;
        this.f515i = (int[][]) null;
        this.f516m = 21L;
        this.f518n = false;
        this.f512k = r2;
        this.f515i = iArr;
    }

    public Y a() {
        Y y2 = new Y();
        if (this.f512k == null || this.f512k.O() == null) {
            y2.f515i = new int[this.f515i.length][this.f515i[0].length];
        } else {
            y2.a(this.f512k.O());
        }
        for (int i2 = 0; i2 < y2.f515i.length; i2++) {
            for (int i3 = 0; i3 < y2.f515i[i2].length; i3++) {
                y2.f515i[i2][i3] = this.f515i[i2][i3];
            }
        }
        y2.f513h = this.f513h;
        return y2;
    }

    public boolean b() {
        return this.f513h;
    }

    private void n() {
        if (this.f509e < this.f507c.size() - 1) {
            for (int size = this.f507c.size() - 1; size > this.f509e; size--) {
                this.f507c.remove(size);
            }
            d(false);
        }
    }

    public void c() throws V.g {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.f509e > 0) {
            jCurrentTimeMillis = ((C0041aa) this.f507c.get(this.f509e - 1)).f() - this.f516m;
        }
        this.f510f = true;
        while (true) {
            if (this.f509e <= 0) {
                break;
            }
            this.f509e--;
            this.f511g = (C0041aa) this.f507c.get(this.f509e);
            if (this.f511g.f() < jCurrentTimeMillis) {
                this.f509e++;
                break;
            }
            if (this.f509e == this.f507c.size() - 1) {
                this.f507c.set(this.f509e, b(this.f511g.a(), this.f511g.b(), this.f511g.c().length));
            } else {
                this.f507c.set(this.f509e, b(this.f511g.a(), this.f511g.b(), this.f511g.c().length));
            }
            a(this.f511g.a(), this.f511g.b(), this.f511g.c(), false);
            if (this.f509e == this.f507c.size() - 1) {
                d(true);
            }
            jCurrentTimeMillis = this.f511g.f() - this.f516m;
        }
        this.f510f = false;
        if (this.f509e == 0) {
            c(false);
        }
        if (this.f509e == this.f507c.size() - 2) {
            d(true);
        }
    }

    public void d() throws V.g {
        long jF = 0;
        if (this.f509e < this.f507c.size()) {
        }
        while (this.f509e < this.f507c.size()) {
            this.f511g = (C0041aa) this.f507c.get(this.f509e);
            if (jF - this.f511g.f() > this.f516m) {
                break;
            }
            C0041aa c0041aaB = b(this.f511g.a(), this.f511g.b(), this.f511g.c().length);
            c0041aaB.a(this.f511g.f());
            this.f507c.set(this.f509e, c0041aaB);
            a(this.f511g.a(), this.f511g.b(), this.f511g.c(), false);
            jF = this.f511g.f();
            this.f509e++;
        }
        if (this.f509e == this.f507c.size()) {
            d(false);
        }
        if (this.f509e >= 1) {
            c(true);
        }
    }

    public void a(int i2, int i3, int[] iArr) {
        a(i2, i3, iArr, false, false);
    }

    public synchronized void a(int i2, int i3, int[] iArr, boolean z2, boolean z3) {
        C0041aa c0041aaB = b(i2, i3, iArr.length);
        if (!(c0041aaB.equals(this.f511g) && C0995c.c(c0041aaB.c(), iArr) && !z2) && a(i2, i3, iArr, z2) && z3) {
            n();
            a(c0041aaB);
        }
    }

    private void a(C0041aa c0041aa) {
        if (this.f510f) {
            return;
        }
        this.f507c.add(c0041aa);
        this.f509e = this.f507c.size();
        c(true);
    }

    public synchronized boolean b(int i2, int i3, int[] iArr) throws V.g {
        boolean z2 = false;
        for (int i4 = 0; i4 < iArr.length; i4++) {
            try {
                if (this.f515i[i2][i3 + i4] != iArr[i4]) {
                    this.f515i[i2][i3 + i4] = iArr[i4];
                    this.f514l = true;
                    z2 = true;
                }
            } catch (Exception e2) {
                throw new V.g("Unable to set bytes \npage:" + i2 + ", offset:" + i3 + ", bytes:\n" + ((Object) iArr) + "\n" + e2.getMessage(), e2);
            }
        }
        return z2;
    }

    protected synchronized boolean a(int i2, int i3, int[] iArr, boolean z2) throws V.g {
        int i4 = -1;
        int i5 = -1;
        for (int i6 = 0; i6 < iArr.length; i6++) {
            try {
                if (this.f515i[i2][i3 + i6] != iArr[i6]) {
                    if (this.f518n) {
                        throw new V.g("Attempt to update readonly ECU Data.");
                    }
                    this.f515i[i2][i3 + i6] = iArr[i6];
                    this.f514l = true;
                    if (i4 == -1) {
                        i4 = i6;
                    }
                    i5 = i6;
                }
            } catch (Exception e2) {
                throw new V.g("Unable to set bytes \npage:" + i2 + ", offset:" + i3 + ", bytes:\n" + C0995c.a(iArr, 8) + "\n" + e2.getMessage(), e2);
            }
        }
        if (z2 || i4 != -1) {
            if (i5 < 0) {
                if (z2) {
                    c(i2, i3, iArr);
                } else {
                    int[] iArr2 = new int[1];
                    System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
                    c(i2, i3, iArr2);
                }
            } else if (i5 - i4 < iArr.length - 1) {
                int[] iArr3 = new int[(i5 - i4) + 1];
                System.arraycopy(iArr, i4, iArr3, 0, iArr3.length);
                c(i2, i3 + i4, iArr3);
            } else {
                c(i2, i3, iArr);
            }
        }
        return i4 != -1;
    }

    public void a(boolean z2) {
        this.f513h = z2;
    }

    public void a(F f2) {
        a(f2.g());
        int[] iArrL = f2.l();
        for (int i2 = 0; i2 < iArrL.length; i2++) {
            a(i2, iArrL[i2]);
        }
        f();
    }

    public int e() {
        return this.f515i.length;
    }

    public void a(int i2) {
        this.f515i = new int[i2][1];
    }

    public void a(int i2, int i3) {
        this.f515i[i2] = new int[i3];
    }

    public void f() {
        for (int i2 = 0; i2 < this.f515i.length; i2++) {
            for (int i3 = 0; i3 < this.f515i[i2].length; i3++) {
                this.f515i[i2][i3] = f517j;
            }
        }
    }

    public int[] b(int i2) {
        return this.f515i[i2];
    }

    public int c(int i2) {
        return this.f515i[i2].length;
    }

    public int[] a(int i2, int i3, int i4) {
        int[] iArr = new int[i4];
        for (int i5 = 0; i5 < iArr.length; i5++) {
            if (i3 + i5 >= this.f515i[i2].length) {
            }
            iArr[i5] = this.f515i[i2][i3 + i5];
        }
        return iArr;
    }

    public int b(int i2, int i3) {
        return this.f515i[i2][i3];
    }

    @Override // G.Q
    public String toString() {
        if (this.f515i == null) {
            return "EcuData not initialized";
        }
        String str = "";
        for (int i2 = 0; i2 < this.f515i.length; i2++) {
            String str2 = str + "page:" + i2;
            for (int i3 = 0; i3 < this.f515i[0].length; i3++) {
                if (i3 % 16 == 0) {
                    str2 = str2 + "\n";
                }
                str2 = str2 + LanguageTag.PRIVATEUSE + bH.W.a("" + Integer.toString(this.f515i[i2][i3], 16), '0', 2) + "\t";
            }
            str = str2 + "\n";
        }
        return str;
    }

    public void a(InterfaceC0042ab interfaceC0042ab) {
        if (this.f505a.contains(interfaceC0042ab)) {
            return;
        }
        this.f505a.add(interfaceC0042ab);
    }

    public void b(InterfaceC0042ab interfaceC0042ab) {
        this.f505a.remove(interfaceC0042ab);
    }

    protected void c(int i2, int i3, int[] iArr) {
        synchronized (this.f505a) {
            for (InterfaceC0042ab interfaceC0042ab : this.f505a) {
                try {
                    interfaceC0042ab.a(this.f512k.c(), i2, i3, iArr);
                } catch (Exception e2) {
                    bH.C.c("EcuData::notifyDataUpdated - Error notifying: " + ((Object) interfaceC0042ab));
                    e2.printStackTrace();
                }
            }
        }
    }

    public void a(dg dgVar) {
        this.f506b.add(dgVar);
    }

    public void b(dg dgVar) {
        this.f506b.remove(dgVar);
    }

    private void c(boolean z2) {
        for (dg dgVar : this.f506b) {
            try {
                dgVar.a(z2);
            } catch (Exception e2) {
                bH.C.c("EcuData::notifyUndoAvailable - Error notifying: " + ((Object) dgVar));
                e2.printStackTrace();
            }
        }
    }

    private void d(boolean z2) {
        for (dg dgVar : this.f506b) {
            try {
                dgVar.b(z2);
            } catch (Exception e2) {
                bH.C.c("EcuData::notifyUndoAvailable - Error notifying: " + ((Object) dgVar));
                e2.printStackTrace();
            }
        }
    }

    @Override // G.Q
    public boolean aM() {
        return this.f514l;
    }

    public void g() {
        this.f514l = false;
        this.f507c.clear();
        this.f509e = -1;
        c(false);
        d(false);
    }

    public boolean h() {
        if (this.f515i == null) {
            return true;
        }
        for (int i2 = 0; i2 < this.f515i.length; i2++) {
            for (int i3 = 0; i3 < this.f515i[i2].length; i3++) {
                if (this.f515i[i2][i3] > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public String i() {
        if (this.f512k == null) {
            return null;
        }
        return this.f512k.c();
    }

    private C0041aa b(int i2, int i3, int i4) {
        C0041aa c0041aa = new C0041aa(this);
        c0041aa.a(i2);
        c0041aa.b(i3);
        int[] iArr = new int[i4];
        for (int i5 = i3; i5 < i3 + i4; i5++) {
            iArr[i5 - i3] = this.f515i[i2][i5];
        }
        c0041aa.a(iArr);
        return c0041aa;
    }

    public void a(InterfaceC0071bd interfaceC0071bd) {
        if (this.f508d.contains(interfaceC0071bd)) {
            return;
        }
        this.f508d.add(interfaceC0071bd);
    }

    public boolean j() {
        return this.f518n;
    }

    public void b(boolean z2) {
        this.f518n = z2;
    }

    public int k() {
        int length = 0;
        for (int i2 = 0; i2 < this.f515i.length; i2++) {
            length += this.f515i[i2].length;
        }
        return length;
    }

    public int l() {
        int i2 = 0;
        for (int i3 = 0; i3 < this.f515i.length; i3++) {
            for (int i4 = 0; i4 < this.f515i[i3].length; i4++) {
                if (this.f515i[i3][i4] == f517j) {
                    i2++;
                }
            }
        }
        return i2;
    }

    public void m() {
        if (this.f507c.size() > 0) {
            this.f507c.clear();
            c(false);
        }
    }
}
