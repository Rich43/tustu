package com.efiAnalytics.ui;

/* renamed from: com.efiAnalytics.ui.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/r.class */
public class C1700r implements InterfaceC1648ef {

    /* renamed from: b, reason: collision with root package name */
    private int f11735b;

    /* renamed from: a, reason: collision with root package name */
    int f11736a;

    public C1700r(int i2, int i3) {
        this.f11735b = i2;
        this.f11736a = i3;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1648ef
    public int a() {
        return this.f11735b;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1648ef
    public int b() {
        return this.f11736a;
    }

    public String toString() {
        return "SelectedPoint mIndex:" + this.f11735b + ", index:" + this.f11736a;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1648ef
    public void a(int i2) {
        this.f11735b = i2;
    }
}
