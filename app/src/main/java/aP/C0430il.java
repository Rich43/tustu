package aP;

import java.io.File;
import java.io.FileFilter;
import r.C1798a;

/* renamed from: aP.il, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/il.class */
class C0430il implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    String f3735a;

    C0430il(String str) {
        this.f3735a = "";
        this.f3735a = str;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.isFile() && file.getName().startsWith(this.f3735a) && file.getName().toLowerCase().endsWith(new StringBuilder().append(".").append(C1798a.cw.toLowerCase()).toString());
    }
}
