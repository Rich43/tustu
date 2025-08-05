package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import com.efiAnalytics.ui.InterfaceC1662et;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/f.class */
class f implements InterfaceC1662et {

    /* renamed from: b, reason: collision with root package name */
    private Properties f9857b = new Properties();

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ SelectableTable f9858a;

    f(SelectableTable selectableTable) {
        this.f9858a = selectableTable;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        a().setProperty(str, str2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        return a().getProperty(str);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        return a().getProperty(str, str2);
    }

    public Properties a() {
        return this.f9857b;
    }

    public void a(Properties properties) {
        this.f9857b = properties;
    }
}
