package com.efiAnalytics.tuningwidgets.panels;

import java.io.File;
import java.io.FileFilter;
import r.C1798a;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.aa, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aa.class */
class C1484aa implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ X f10380a;

    C1484aa(X x2) {
        this.f10380a = x2;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return (file.getParentFile().getName().equals("restorePoints") || file.getName().toLowerCase().endsWith(C1798a.f13286t)) ? false : true;
    }
}
