package G;

import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:G/A.class */
public class A implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    public dh f292a;

    /* renamed from: b, reason: collision with root package name */
    public dh f293b;

    public A(dh dhVar, dh dhVar2) {
        this.f292a = dhVar;
        this.f293b = dhVar2;
    }

    public A(int i2, int i3) {
        this.f292a = new B(i2);
        this.f293b = new B(i3);
    }

    public int a() {
        return (int) Math.round(this.f292a.a());
    }

    public int b() {
        return (int) Math.round(this.f293b.a());
    }

    public dh c() {
        return this.f292a;
    }

    public dh d() {
        return this.f293b;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof A)) {
            return super.equals(obj);
        }
        A a2 = (A) obj;
        return a2.b() == b() && a2.a() == a2.a();
    }
}
