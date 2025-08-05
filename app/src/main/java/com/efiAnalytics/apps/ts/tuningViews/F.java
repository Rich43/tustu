package com.efiAnalytics.apps.ts.tuningViews;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/F.class */
public class F extends ArrayList {

    /* renamed from: a, reason: collision with root package name */
    private String f9680a = "";

    /* renamed from: b, reason: collision with root package name */
    private String f9681b = "Tuning View";

    /* renamed from: c, reason: collision with root package name */
    private String f9682c = null;

    /* renamed from: d, reason: collision with root package name */
    private File f9683d = null;

    /* renamed from: e, reason: collision with root package name */
    private String f9684e = null;

    public String a() {
        return this.f9680a;
    }

    public void a(String str) {
        this.f9680a = str;
    }

    public String b() {
        return this.f9681b;
    }

    public void b(String str) {
        this.f9681b = str;
    }

    public String c() {
        if (this.f9682c != null && !this.f9682c.isEmpty()) {
            return this.f9682c;
        }
        if (this.f9683d != null && this.f9683d.exists()) {
            try {
                this.f9682c = bI.a.b(this.f9683d.getAbsolutePath());
            } catch (IOException e2) {
                bH.C.a("Failed to write TuningView Preview File.");
                Logger.getLogger(F.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                return this.f9682c;
            }
        }
        return this.f9682c;
    }

    public void c(String str) {
        this.f9682c = str;
        if (str == null || str.isEmpty()) {
            return;
        }
        this.f9683d = null;
    }

    public void a(File file) {
        this.f9683d = file;
        if (file == null || !file.exists()) {
            return;
        }
        this.f9682c = null;
    }

    public File d() {
        if (this.f9683d != null) {
            return this.f9683d;
        }
        if (this.f9682c == null || this.f9682c.isEmpty()) {
            return null;
        }
        this.f9683d = File.createTempFile(b() + Math.random(), ".png");
        this.f9683d.deleteOnExit();
        bI.a.a(this.f9682c, this.f9683d.getAbsolutePath());
        return this.f9683d;
    }

    public String e() {
        return this.f9684e;
    }

    public void d(String str) {
        this.f9684e = str;
    }
}
