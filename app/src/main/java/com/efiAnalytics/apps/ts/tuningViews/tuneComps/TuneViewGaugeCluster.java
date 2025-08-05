package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import G.T;
import com.efiAnalytics.apps.ts.dashboard.C1389ab;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1422u;
import com.efiAnalytics.apps.ts.dashboard.Z;
import com.efiAnalytics.apps.ts.tuningViews.C1435h;
import com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import r.C1807j;
import v.C1883c;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/TuneViewGaugeCluster.class */
public class TuneViewGaugeCluster extends TuneViewComponent {

    /* renamed from: c, reason: collision with root package name */
    C1425x f9848c;

    public TuneViewGaugeCluster() {
        setLayout(new C1435h(this));
        if (T.a().c() != null) {
            this.f9848c = new C1425x(T.a().c());
        } else {
            this.f9848c = new C1425x();
        }
        C1389ab.a(this.f9848c);
        add(this.f9848c, BorderLayout.CENTER);
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void initializeComponents() {
        this.f9848c.a(T.a().c());
        this.f9848c.b();
    }

    public String getEncodedDashboard() {
        Z zI = this.f9848c.i();
        C1883c c1883c = new C1883c(C1807j.G());
        String strB = "";
        try {
            File fileCreateTempFile = File.createTempFile((Math.random() * 2.0E9d) + "", "tempDash");
            c1883c.a(fileCreateTempFile.getAbsolutePath(), zI);
            strB = bI.a.b(fileCreateTempFile.getAbsolutePath());
            fileCreateTempFile.delete();
        } catch (V.a e2) {
            e2.printStackTrace();
            bV.d("Error Saving TuningView Dashboard.\n" + e2.getMessage(), this);
        } catch (IOException e3) {
            e3.printStackTrace();
            bV.d("Unable to write temporary file for Saving TuningView Dashboard.\n" + e3.getMessage(), this);
        }
        return strB;
    }

    public void setEncodedDashboard(String str) {
        C1883c c1883c = new C1883c(C1807j.G());
        try {
            File fileCreateTempFile = File.createTempFile((Math.random() * 2.0E9d) + "", "tempDash");
            bI.a.a(str, fileCreateTempFile.getAbsolutePath());
            this.f9848c.a(c1883c.a(fileCreateTempFile.getAbsolutePath()));
            fileCreateTempFile.delete();
        } catch (V.a e2) {
            e2.printStackTrace();
            bV.d("Error Loading a TuningView Dashboard\n" + e2.getMessage(), this);
        } catch (IOException e3) {
            e3.printStackTrace();
            bV.d("Unable to write temporary file for Loading TuningView Dashboard.\n" + e3.getMessage(), this);
        }
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f9848c.close();
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public boolean isShieldedDuringEdit() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public boolean isDirty() {
        return !this.f9848c.ae();
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void setClean(boolean z2) {
        this.f9848c.k(z2);
    }

    public void addFullScreenRequestListener(InterfaceC1422u interfaceC1422u) {
        this.f9848c.a(interfaceC1422u);
    }

    public void removeFullScreenRequestListener(InterfaceC1422u interfaceC1422u) {
        this.f9848c.b(interfaceC1422u);
    }
}
