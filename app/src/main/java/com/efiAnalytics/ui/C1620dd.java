package com.efiAnalytics.ui;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.filechooser.FileFilter;

/* renamed from: com.efiAnalytics.ui.dd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dd.class */
public class C1620dd extends FileFilter implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    String f11412a = "";

    /* renamed from: b, reason: collision with root package name */
    ArrayList f11413b = new ArrayList();

    public C1620dd(String str) {
        a(str);
    }

    public void a(String str) {
        this.f11412a = str;
    }

    public void b(String str) {
        this.f11413b.add(str.toLowerCase());
    }

    @Override // javax.swing.filechooser.FileFilter
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        Iterator it = this.f11413b.iterator();
        while (it.hasNext()) {
            if (file.getName().toLowerCase().indexOf(("." + ((String) it.next())).toLowerCase()) != -1) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.swing.filechooser.FileFilter
    public String getDescription() {
        return this.f11412a;
    }
}
