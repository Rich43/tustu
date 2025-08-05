package com.efiAnalytics.tuningwidgets.panels;

import java.io.File;
import java.io.FileFilter;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/Z.class */
class Z implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ X f10320a;

    Z(X x2) {
        this.f10320a = x2;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return (file.getName().toLowerCase().endsWith(C1798a.cr) || file.getName().toLowerCase().endsWith(C1798a.cs) || file.getName().toLowerCase().endsWith(C1798a.f13286t) || file.getName().toLowerCase().endsWith(C1798a.cv)) ? false : true;
    }
}
