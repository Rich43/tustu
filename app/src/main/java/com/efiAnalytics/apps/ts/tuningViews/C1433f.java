package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import r.C1798a;
import r.C1807j;
import s.C1818g;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/f.class */
class C1433f implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1429b f9778a;

    C1433f(C1429b c1429b) {
        this.f9778a = c1429b;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strB = bV.b((Component) actionEvent.getSource(), C1818g.b("Select Other Tuning Views"), new String[]{C1798a.cp}, "", C1807j.k().getAbsolutePath());
        if (strB != null) {
            this.f9778a.f9769d.setText(strB);
            this.f9778a.f9766a = new File(strB);
            this.f9778a.a(this.f9778a.f9766a);
        }
    }
}
