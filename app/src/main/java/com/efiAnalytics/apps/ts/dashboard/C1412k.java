package com.efiAnalytics.apps.ts.dashboard;

import java.io.File;
import java.io.FileFilter;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/k.class */
class C1412k implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1408g f9506a;

    C1412k(C1408g c1408g) {
        this.f9506a = c1408g;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.getName().toLowerCase().endsWith(".gauge");
    }
}
