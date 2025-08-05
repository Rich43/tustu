package com.efiAnalytics.tunerStudio.panels;

import W.C0188n;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ai.class */
class ai extends ArrayList {

    /* renamed from: a, reason: collision with root package name */
    int f10064a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10065b;

    ai(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10065b = triggerLoggerPanel;
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
        this.f10064a = i2;
        return (C0188n) super.get(i2);
    }

    public C0188n a() {
        if (this.f10064a >= size() - 1) {
            return null;
        }
        this.f10064a++;
        return (C0188n) super.get(this.f10064a);
    }

    public C0188n b() {
        if (this.f10064a <= 0) {
            return null;
        }
        this.f10064a--;
        return (C0188n) super.get(this.f10064a);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f10064a = -1;
        super.clear();
        this.f10065b.f10039af.clear();
        this.f10065b.f10040ag = 0;
    }

    public int c() {
        return this.f10064a;
    }

    public int d() {
        return TriggerLoggerPanel.f10025S;
    }

    public C0188n e() {
        this.f10064a = size() - 1;
        return (C0188n) super.get(this.f10064a);
    }

    public C0188n f() {
        this.f10064a = 0;
        return (C0188n) super.get(this.f10064a);
    }
}
