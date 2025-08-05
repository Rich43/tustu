package com.efiAnalytics.tunerStudio.search;

import java.io.File;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/j.class */
public class j {

    /* renamed from: a, reason: collision with root package name */
    private File f10194a;

    public j(File file) {
        this.f10194a = file;
    }

    public String a() {
        return this.f10194a.getName();
    }

    public File b() {
        return this.f10194a;
    }

    public String toString() {
        return "projectFolder: " + ((Object) this.f10194a);
    }
}
