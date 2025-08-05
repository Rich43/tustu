package com.efiAnalytics.apps.ts.tuningViews;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/I.class */
final class I implements FilenameFilter {
    I() {
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.startsWith(C1438k.f9785a);
    }
}
