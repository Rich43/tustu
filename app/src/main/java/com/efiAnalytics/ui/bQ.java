package com.efiAnalytics.ui;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bQ.class */
class bQ extends ArrayList {

    /* renamed from: b, reason: collision with root package name */
    private String f10968b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f10969c = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bN f10970a;

    bQ(bN bNVar) {
        this.f10970a = bNVar;
    }

    public String a() {
        return this.f10968b;
    }

    public void a(String str) {
        this.f10968b = str;
    }

    public String b() {
        return this.f10969c;
    }

    public void b(String str) {
        this.f10969c = str;
    }

    @Override // java.util.ArrayList
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public bQ clone() {
        bQ bQVar = new bQ(this.f10970a);
        bQVar.f10968b = this.f10968b;
        bQVar.f10969c = this.f10969c;
        Iterator it = iterator();
        while (it.hasNext()) {
            bP bPVar = (bP) it.next();
            bP bPVar2 = new bP(this.f10970a, bPVar.a(), bPVar.b());
            bPVar2.f10962a = bPVar.f10962a;
            bPVar2.f10963b = bPVar.f10963b;
            bQVar.add(bPVar2);
        }
        return bQVar;
    }
}
