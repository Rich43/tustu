package bQ;

import G.C0113cs;
import G.InterfaceC0109co;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bQ/a.class */
public class a implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    l f7383a;

    /* renamed from: b, reason: collision with root package name */
    J.j f7384b;

    /* renamed from: c, reason: collision with root package name */
    final List f7385c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    int f7386d = 1000;

    /* renamed from: e, reason: collision with root package name */
    int f7387e = 5;

    /* renamed from: f, reason: collision with root package name */
    int f7388f = 0;

    /* renamed from: g, reason: collision with root package name */
    Runnable f7389g = new b(this);

    public a(l lVar, J.j jVar) {
        this.f7384b = jVar;
        this.f7383a = lVar;
    }

    public void a() {
        C0113cs.a().a(J.j.f1472c, this);
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
    }
}
