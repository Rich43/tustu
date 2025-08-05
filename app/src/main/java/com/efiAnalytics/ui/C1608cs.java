package com.efiAnalytics.ui;

import java.io.File;
import java.io.FilenameFilter;

/* renamed from: com.efiAnalytics.ui.cs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cs.class */
class C1608cs implements FilenameFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1606cq f11303a;

    C1608cs(C1606cq c1606cq) {
        this.f11303a = c1606cq;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String str) {
        return str.toLowerCase().endsWith(".ttf");
    }
}
