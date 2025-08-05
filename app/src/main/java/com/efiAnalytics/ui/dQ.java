package com.efiAnalytics.ui;

import java.util.Properties;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dQ.class */
public class dQ implements InterfaceC1662et {

    /* renamed from: a, reason: collision with root package name */
    Properties f11360a;

    /* renamed from: b, reason: collision with root package name */
    private String f11361b;

    public dQ(Properties properties, String str) {
        this.f11360a = null;
        this.f11360a = properties;
        this.f11361b = str;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        if (str2 == null) {
            str2 = "";
        }
        if (this.f11361b == null || this.f11361b.isEmpty()) {
            this.f11360a.setProperty(str, str2);
        } else {
            this.f11360a.setProperty(this.f11361b + "_" + str, str2);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        return (this.f11361b == null || this.f11361b.isEmpty()) ? this.f11360a.getProperty(str) : this.f11360a.getProperty(this.f11361b + "_" + str);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        String property = this.f11360a.getProperty(this.f11361b + "_" + str);
        return (property == null || property.equals("")) ? str2 : property;
    }
}
