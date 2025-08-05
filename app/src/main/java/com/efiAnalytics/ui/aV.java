package com.efiAnalytics.ui;

import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aV.class */
class aV {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f10814a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aU f10815b;

    aV(aU aUVar) {
        this.f10815b = aUVar;
    }

    public ArrayList a(int i2) {
        if (this.f10814a.size() <= i2) {
            for (int size = this.f10814a.size(); size <= i2; size++) {
                this.f10814a.add(new ArrayList());
            }
        }
        return (ArrayList) this.f10814a.get(i2);
    }

    public void a(int i2, aZ aZVar) {
        a(i2).add(aZVar);
    }

    public void a(int i2, int i3, aZ aZVar) {
        a(i3).add(i2, aZVar);
    }

    public int a() {
        return this.f10814a.size();
    }

    public void b() {
        this.f10814a.clear();
    }
}
