package com.efiAnalytics.ui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bY.class */
final class bY extends FileFilter {
    bY() {
    }

    @Override // javax.swing.filechooser.FileFilter
    public boolean accept(File file) {
        return file.isDirectory();
    }

    @Override // javax.swing.filechooser.FileFilter
    public String getDescription() {
        return bV.f10979h.l() + " " + bV.f10979h.m() + " Projects";
    }
}
