package com.efiAnalytics.tuningwidgets.panels;

import ai.C0516f;
import bH.C1005m;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JPanel;
import r.C1798a;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.ad, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/ad.class */
public class C1487ad extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    C0516f f10387a;

    public C1487ad() {
        this.f10387a = null;
        this.f10387a = new C0516f();
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f10387a);
        File file = new File(".");
        String str = C1005m.b() ? "file:///" + file.getAbsolutePath() + "/help/" + C1798a.f13288v : "file:///" + file.getAbsolutePath() + "/help/" + C1798a.f13289w;
        try {
            this.f10387a.b(str);
        } catch (V.a e2) {
            bH.C.a("unable to open:\n" + str + "\n" + e2.getMessage());
        }
        this.f10387a.a(false);
    }
}
