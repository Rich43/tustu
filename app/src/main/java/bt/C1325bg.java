package bt;

import com.efiAnalytics.ui.cQ;

/* renamed from: bt.bg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bg.class */
class C1325bg implements cQ {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f9013a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1324bf f9014b;

    C1325bg(C1324bf c1324bf, String str) {
        this.f9014b = c1324bf;
        this.f9013a = str;
    }

    @Override // com.efiAnalytics.ui.cQ
    public void a(int i2) {
        aE.a.A().setProperty(this.f9013a, Integer.toString(i2));
    }
}
