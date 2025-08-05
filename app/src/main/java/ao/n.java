package aO;

import W.C0188n;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:aO/n.class */
class n extends ArrayList {

    /* renamed from: a, reason: collision with root package name */
    int f2706a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ k f2707b;

    n(k kVar) {
        this.f2707b = kVar;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public boolean add(C0188n c0188n) {
        if (c0188n == null) {
            return false;
        }
        super.add(c0188n);
        while (size() > d()) {
            remove(0);
        }
        return true;
    }

    public C0188n a(int i2) throws V.a {
        if (i2 < 0 || i2 >= size()) {
            throw new V.a("Not a valid data page for the currently loaded log. " + i2);
        }
        this.f2706a = i2;
        return (C0188n) super.get(i2);
    }

    public C0188n a() {
        if (this.f2706a >= size() - 1) {
            return null;
        }
        this.f2706a++;
        return (C0188n) super.get(this.f2706a);
    }

    public C0188n b() {
        if (this.f2706a <= 0) {
            return null;
        }
        this.f2706a--;
        return (C0188n) super.get(this.f2706a);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f2706a = -1;
        super.clear();
    }

    public int c() {
        return this.f2706a;
    }

    public int d() {
        return this.f2707b.i();
    }

    public C0188n e() {
        this.f2706a = size() - 1;
        return (C0188n) super.get(this.f2706a);
    }

    public C0188n f() {
        this.f2706a = 0;
        return (C0188n) super.get(this.f2706a);
    }
}
