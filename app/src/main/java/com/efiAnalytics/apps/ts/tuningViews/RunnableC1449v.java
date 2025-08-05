package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.ui.bV;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/v.class */
class RunnableC1449v implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    C1438k f9870a;

    /* renamed from: b, reason: collision with root package name */
    List f9871b;

    /* renamed from: c, reason: collision with root package name */
    boolean f9872c = false;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C1441n f9873d;

    RunnableC1449v(C1441n c1441n, List list, C1438k c1438k) {
        this.f9873d = c1441n;
        this.f9870a = c1438k;
        this.f9871b = list;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.f9873d.a(this.f9870a);
            this.f9871b.remove(this.f9870a);
        } catch (V.a e2) {
            bV.d("Failed to load Tune View File:\n" + (this.f9870a.a() != null ? this.f9870a.a().getAbsolutePath() : FXMLLoader.NULL_KEYWORD) + "\nError:\n" + e2.getMessage(), this.f9873d.getParent());
            Logger.getLogger(C1441n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
