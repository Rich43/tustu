package com.efiAnalytics.apps.ts.tuningViews;

import java.io.File;
import java.io.FilenameFilter;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/H.class */
final class H implements FilenameFilter {
    H() {
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.toLowerCase().endsWith(C1798a.cp.toLowerCase());
    }
}
