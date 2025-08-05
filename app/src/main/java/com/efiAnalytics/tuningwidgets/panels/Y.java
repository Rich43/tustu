package com.efiAnalytics.tuningwidgets.panels;

import java.io.File;
import java.io.FileFilter;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/Y.class */
class Y implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ X f10319a;

    Y(X x2) {
        this.f10319a = x2;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return (file.getName().toLowerCase().endsWith(C1798a.cr) || file.getName().toLowerCase().endsWith(C1798a.cs) || file.getName().toLowerCase().endsWith(C1798a.cv) || file.getName().toLowerCase().endsWith(C1798a.f13286t) || file.getParentFile().getName().equals("restorePoints")) ? false : true;
    }
}
