package com.efiAnalytics.tuningwidgets.panels;

import java.io.File;
import java.io.FileFilter;
import r.C1798a;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.ab, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/ab.class */
class C1485ab implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ X f10381a;

    C1485ab(X x2) {
        this.f10381a = x2;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return !file.getName().toLowerCase().endsWith(C1798a.f13286t);
    }
}
