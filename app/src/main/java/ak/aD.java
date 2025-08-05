package ak;

import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:ak/aD.class */
public class aD {

    /* renamed from: a, reason: collision with root package name */
    String f4656a;

    /* renamed from: b, reason: collision with root package name */
    String f4657b;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f4658c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    boolean f4659d = false;

    /* renamed from: e, reason: collision with root package name */
    int f4660e = 0;

    /* renamed from: f, reason: collision with root package name */
    int f4661f = -1;

    /* renamed from: g, reason: collision with root package name */
    private boolean f4662g = false;

    /* renamed from: h, reason: collision with root package name */
    private boolean f4663h = false;

    public aD(String str, String str2) {
        this.f4657b = str2;
        this.f4656a = str;
    }

    public boolean a() {
        if (!this.f4659d) {
            a(this.f4656a, this.f4657b);
        }
        return d() > this.f4660e;
    }

    public String b() {
        if (!a()) {
            return null;
        }
        ArrayList arrayList = this.f4658c;
        int i2 = this.f4660e;
        this.f4660e = i2 + 1;
        return (String) arrayList.get(i2);
    }

    public String a(int i2) {
        if (!this.f4659d) {
            a(this.f4656a, this.f4657b);
        }
        String str = (String) this.f4658c.get(i2);
        this.f4660e = i2 + 1;
        return str;
    }

    public int c() {
        if (!this.f4659d) {
            a(this.f4656a, this.f4657b);
        }
        return this.f4658c.size();
    }

    private void a(String str, String str2) {
        int i2;
        int length = str2.length();
        int length2 = str.length();
        if (length == 1) {
            char cCharAt = str2.charAt(0);
            boolean z2 = false;
            int i3 = 0;
            int i4 = 0;
            while (true) {
                if (i4 > length2) {
                    break;
                }
                if (i4 == length2) {
                    this.f4658c.add(bH.W.i(str.substring(i3)));
                    break;
                }
                if (this.f4663h && str.charAt(i4) == '\"') {
                    z2 = !z2;
                }
                if (!z2 && str.charAt(i4) == cCharAt) {
                    this.f4658c.add(bH.W.i(str.substring(i3, i4)));
                    i3 = i4 + length;
                }
                i4++;
            }
        } else {
            int i5 = 0;
            int iIndexOf = str.indexOf(str2, 0);
            while (true) {
                i2 = iIndexOf;
                if (i2 == -1) {
                    break;
                }
                String strSubstring = str.substring(i5, i2);
                if (!this.f4662g || !strSubstring.trim().isEmpty()) {
                    this.f4658c.add(strSubstring);
                }
                i5 = i2 + length;
                iIndexOf = str.indexOf(str2, i5);
            }
            if (i2 < str.length()) {
                this.f4658c.add(str.substring(i5, length2));
            }
        }
        this.f4659d = true;
    }

    private int d() {
        if (this.f4661f == -1) {
            this.f4661f = this.f4658c.size();
        }
        return this.f4661f;
    }

    public void a(boolean z2) {
        this.f4662g = z2;
    }

    public void b(boolean z2) {
        this.f4663h = z2;
    }
}
