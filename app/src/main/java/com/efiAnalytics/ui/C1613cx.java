package com.efiAnalytics.ui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/* renamed from: com.efiAnalytics.ui.cx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cx.class */
public class C1613cx extends FileFilter {

    /* renamed from: b, reason: collision with root package name */
    private String f11308b;

    /* renamed from: a, reason: collision with root package name */
    String f11309a = null;

    public C1613cx(String str) {
        this.f11308b = str;
    }

    @Override // javax.swing.filechooser.FileFilter
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().toLowerCase().endsWith(new StringBuilder().append(".").append(this.f11308b).toString());
    }

    @Override // javax.swing.filechooser.FileFilter
    public String getDescription() {
        String lowerCase;
        if (this.f11309a != null) {
            return this.f11309a;
        }
        lowerCase = this.f11308b.toLowerCase();
        switch (lowerCase) {
            case "mlg":
                return "MegaLogViewer Binary Data Log (*.mlg)";
            case "msl":
                return "MegaLogViewer ASCII Data Log (*.msl)";
            case "csv":
                return "Comma Separated Values (*.csv)";
            case "msq":
                return "Tune File (*.msq)";
            case "bigtune":
                return "BigStuff Calibration (*.bigtune)";
            case "tsproj":
                return "TunerStudio Project Archive (*.tsproj)";
            case "bsproj":
                return "BigComm Project Archive (*.bsproj)";
            case "xls":
                return "Excel (*.xls)";
            default:
                return this.f11308b + " File (*." + lowerCase + ")";
        }
    }

    public String a() {
        return this.f11308b;
    }
}
