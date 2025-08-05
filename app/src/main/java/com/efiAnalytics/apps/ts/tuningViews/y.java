package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.ui.bV;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/y.class */
class y extends Thread {

    /* renamed from: a, reason: collision with root package name */
    C1438k f9878a;

    /* renamed from: b, reason: collision with root package name */
    J f9879b;

    /* renamed from: d, reason: collision with root package name */
    private boolean f9880d = false;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1441n f9881c;

    y(C1441n c1441n, C1438k c1438k, J j2) {
        this.f9881c = c1441n;
        this.f9878a = c1438k;
        this.f9879b = j2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (this.f9880d) {
            bH.C.c("Attempting to load a TuningViewFile that has already been loaded");
        }
        try {
            try {
                this.f9880d = true;
                this.f9879b.y();
                F fD = this.f9878a.d();
                if (fD == null) {
                    this.f9880d = false;
                    this.f9879b.e(C1818g.b("This Tuning View contains Password Protected Information. You must have the password to access it."));
                    for (int i2 = 0; i2 < this.f9881c.f9797h.size(); i2++) {
                        if (((y) this.f9881c.f9797h.get(i2)).f9879b.equals(this.f9879b)) {
                            this.f9881c.f9797h.set(i2, new y(this.f9881c, this.f9878a, this.f9879b));
                            this.f9879b.z();
                            return;
                        }
                    }
                }
                this.f9879b.z();
                this.f9879b.a(fD);
            } catch (V.a e2) {
                bV.d("Failed to load Tune View File:\n" + (this.f9878a.a() != null ? this.f9878a.a().getAbsolutePath() : FXMLLoader.NULL_KEYWORD) + "\nError:\n" + e2.getMessage(), this.f9881c.getParent());
                Logger.getLogger(C1441n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                this.f9879b.z();
            }
        } catch (Throwable th) {
            this.f9879b.z();
            throw th;
        }
    }

    public boolean a() {
        return this.f9880d;
    }
}
