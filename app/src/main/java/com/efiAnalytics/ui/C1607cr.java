package com.efiAnalytics.ui;

import java.io.File;
import java.io.FilenameFilter;

/* renamed from: com.efiAnalytics.ui.cr, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cr.class */
class C1607cr implements FilenameFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1606cq f11302a;

    C1607cr(C1606cq c1606cq) {
        this.f11302a = c1606cq;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.toLowerCase().endsWith(".ttf");
    }
}
