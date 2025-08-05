package br;

import G.C0126i;
import G.aI;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.fx;
import java.awt.Component;

/* renamed from: br.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/c.class */
class C1239c implements fx {

    /* renamed from: a, reason: collision with root package name */
    Component f8443a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1237a f8444b;

    public C1239c(C1237a c1237a, Component component) {
        this.f8444b = c1237a;
        this.f8443a = null;
        this.f8443a = component;
    }

    @Override // com.efiAnalytics.ui.fx
    public boolean a(String str) {
        if (str.equals("")) {
            return true;
        }
        try {
            C0126i.a((aI) this.f8444b.f8401a, " " + str + " ");
            return true;
        } catch (V.g e2) {
            bV.d("Error:" + e2.getMessage(), this.f8443a);
            return false;
        }
    }
}
