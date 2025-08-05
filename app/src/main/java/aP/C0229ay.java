package aP;

import java.util.List;

/* renamed from: aP.ay, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ay.class */
class C0229ay extends Thread {

    /* renamed from: a, reason: collision with root package name */
    String f2957a;

    /* renamed from: b, reason: collision with root package name */
    String f2958b;

    /* renamed from: c, reason: collision with root package name */
    List f2959c;

    /* renamed from: d, reason: collision with root package name */
    G.bS f2960d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C0224at f2961e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0229ay(C0224at c0224at, String str, String str2, List list, G.bS bSVar) {
        super("AsyncDeviceFound");
        this.f2961e = c0224at;
        setDaemon(true);
        this.f2957a = str;
        this.f2958b = str2;
        this.f2959c = list;
        this.f2960d = bSVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f2961e.b(this.f2957a, this.f2958b, this.f2959c, this.f2960d);
    }
}
