package ao;

import com.efiAnalytics.ui.InterfaceC1671fb;

/* renamed from: ao.ee, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ee.class */
class C0721ee implements InterfaceC1671fb {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0718eb f5626a;

    C0721ee(C0718eb c0718eb) {
        this.f5626a = c0718eb;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1671fb
    public void a(int i2, int i3) {
        this.f5626a.a(i2, i3);
        h.i.c("scatterPlotRows", "" + i2);
        h.i.c("scatterPlotCols", "" + i3);
    }
}
