package com.efiAnalytics.ui;

import java.util.ArrayList;
import java.util.Collection;

/* renamed from: com.efiAnalytics.ui.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/l.class */
class C1694l implements InterfaceC1697o {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1642e f11727a;

    C1694l(C1642e c1642e) {
        this.f11727a = c1642e;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1697o
    public Collection a(String str) {
        ArrayList arrayList = new ArrayList();
        if (str.endsWith("*")) {
            str = str.substring(0, str.length() - 1);
        }
        if (this.f11727a.f11465d.contains(str)) {
            return this.f11727a.f11465d;
        }
        for (String str2 : this.f11727a.f11465d) {
            if (str.trim().isEmpty() || str2.toLowerCase().contains(str.toLowerCase())) {
                arrayList.add(str2);
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.addAll(this.f11727a.f11465d);
        }
        return arrayList;
    }
}
