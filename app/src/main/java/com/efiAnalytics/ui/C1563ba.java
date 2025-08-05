package com.efiAnalytics.ui;

import javax.swing.JCheckBoxMenuItem;

/* renamed from: com.efiAnalytics.ui.ba, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ba.class */
public class C1563ba implements dR {

    /* renamed from: a, reason: collision with root package name */
    JCheckBoxMenuItem f10983a;

    /* renamed from: b, reason: collision with root package name */
    String f10984b;

    public C1563ba(JCheckBoxMenuItem jCheckBoxMenuItem, String str) {
        this.f10983a = null;
        this.f10984b = "";
        this.f10983a = jCheckBoxMenuItem;
        this.f10984b = str;
    }

    @Override // com.efiAnalytics.ui.dR
    public void a(String str, String str2) {
        this.f10983a.setState(str2 != null && str2.equals("true"));
    }
}
