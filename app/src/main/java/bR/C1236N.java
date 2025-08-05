package br;

import bH.C1007o;
import c.InterfaceC1386e;

/* renamed from: br.N, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/N.class */
class C1236N implements InterfaceC1386e {

    /* renamed from: a, reason: collision with root package name */
    String f8369a;

    /* renamed from: b, reason: collision with root package name */
    G.R f8370b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1233K f8371c;

    public C1236N(C1233K c1233k, G.R r2, String str) {
        this.f8371c = c1233k;
        this.f8369a = "";
        this.f8370b = null;
        this.f8369a = str;
        this.f8370b = r2;
    }

    @Override // c.InterfaceC1386e
    public boolean a() {
        try {
            return C1007o.a(this.f8369a, this.f8370b);
        } catch (Exception e2) {
            bH.C.c(e2.getMessage());
            return true;
        }
    }
}
