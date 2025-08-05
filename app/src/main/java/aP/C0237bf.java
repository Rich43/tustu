package aP;

import java.util.List;
import s.C1818g;

/* renamed from: aP.bf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bf.class */
class C0237bf {

    /* renamed from: b, reason: collision with root package name */
    private String f3066b;

    /* renamed from: c, reason: collision with root package name */
    private List f3067c;

    /* renamed from: d, reason: collision with root package name */
    private G.bS f3068d;

    /* renamed from: e, reason: collision with root package name */
    private String f3069e;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aV f3070a;

    C0237bf(aV aVVar, String str, List list, String str2, G.bS bSVar) {
        this.f3070a = aVVar;
        this.f3068d = null;
        this.f3066b = str;
        this.f3067c = list;
        this.f3068d = bSVar;
        this.f3069e = str2;
    }

    public String toString() {
        return this.f3069e + ", " + C1818g.b("Found") + ": " + a().c();
    }

    public G.bS a() {
        return this.f3068d;
    }

    public String b() {
        return this.f3066b;
    }

    public List c() {
        return this.f3067c;
    }

    public String d() {
        return this.f3069e;
    }

    public boolean equals(Object obj) {
        return obj instanceof C0237bf ? toString().equals(obj.toString()) : super.equals(obj);
    }
}
