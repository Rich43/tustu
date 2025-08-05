package com.efiAnalytics.apps.ts.tuningViews;

import G.C0088bu;
import W.C0200z;
import bH.Z;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.SelectableTable;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.TuneSettingsPanel;
import com.efiAnalytics.ui.bV;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Action;
import r.C1807j;
import s.C1818g;
import v.C1887g;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/k.class */
public class C1438k {

    /* renamed from: a, reason: collision with root package name */
    public static String f9785a = "TuneView_";

    /* renamed from: b, reason: collision with root package name */
    File f9786b;

    /* renamed from: c, reason: collision with root package name */
    F f9787c = null;

    /* renamed from: d, reason: collision with root package name */
    private String f9788d = null;

    public C1438k(File file) {
        this.f9786b = null;
        this.f9786b = file;
    }

    public File a() {
        return this.f9786b;
    }

    public String b() {
        return this.f9787c != null ? this.f9787c.b() : C0200z.d(this.f9786b);
    }

    public String c() {
        return this.f9787c != null ? this.f9787c.e() : C0200z.a(this.f9786b, "enabledCondition");
    }

    public String toString() {
        String strB = this.f9787c != null ? this.f9787c.b() : C0200z.d(this.f9786b);
        return (this.f9788d == null || this.f9788d.isEmpty()) ? this.f9786b.getAbsolutePath().startsWith(C1807j.l().getAbsolutePath()) ? strB + " (" + C1818g.b(Action.DEFAULT) + ")" : strB + " (" + C1818g.b("User Tune View") + ")" : strB + " (" + C1818g.b(this.f9788d) + ")";
    }

    public F d() {
        if (this.f9787c == null && this.f9786b != null && this.f9786b.exists()) {
            Z z2 = new Z();
            z2.a();
            this.f9787c = new C1887g().a(this.f9786b);
            bH.C.d("Time to read " + this.f9786b.getName() + ": " + z2.d() + "ms.");
            if (!a(this.f9787c)) {
                this.f9787c = null;
            }
        }
        return this.f9787c;
    }

    public boolean a(F f2) {
        C0088bu c0088buC;
        new ArrayList();
        Iterator it = f2.iterator();
        while (it.hasNext()) {
            TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
            String ecuConfigurationName = tuneViewComponent.getEcuConfigurationName();
            G.R rC = (ecuConfigurationName == null || ecuConfigurationName.isEmpty() || G.T.a().c(ecuConfigurationName) == null) ? G.T.a().c() : G.T.a().c(ecuConfigurationName);
            String selectedTableName = null;
            if (tuneViewComponent instanceof TuneSettingsPanel) {
                selectedTableName = ((TuneSettingsPanel) tuneViewComponent).getSettingPanelName();
            } else if (tuneViewComponent instanceof SelectableTable) {
                selectedTableName = ((SelectableTable) tuneViewComponent).getSelectedTableName();
            }
            if (selectedTableName != null && !selectedTableName.isEmpty() && (c0088buC = rC.e().c(selectedTableName)) != null && !com.efiAnalytics.tuningwidgets.panels.W.a(rC, c0088buC, bV.c())) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof C1438k)) {
            return (!(obj instanceof File) || this.f9786b == null) ? super.equals(obj) : obj.equals(this.f9786b);
        }
        C1438k c1438k = (C1438k) obj;
        return (c1438k.f9787c == null || this.f9787c == null) ? (c1438k.f9786b == null || this.f9786b == null) ? super.equals(obj) : c1438k.f9786b.equals(this.f9786b) : this.f9787c.equals(c1438k.f9787c);
    }

    public void a(String str) {
        this.f9788d = str;
    }
}
