package com.efiAnalytics.tunerStudio.panels;

import com.efiAnalytics.ui.InterfaceC1662et;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/Q.class */
class Q implements InterfaceC1662et {

    /* renamed from: a, reason: collision with root package name */
    String f9965a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ J f9966b;

    Q(J j2) {
        this.f9966b = j2;
        this.f9965a = "refTable_" + this.f9966b.e() + "_";
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        aE.a aVarA = aE.a.A();
        if (aVarA != null) {
            aVarA.setProperty(this.f9965a + str, str2);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        aE.a aVarA = aE.a.A();
        return aVarA != null ? aVarA.getProperty(this.f9965a + str, "") : "";
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        return str2;
    }
}
