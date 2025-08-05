package br;

import G.C0113cs;
import G.InterfaceC0109co;
import com.efiAnalytics.ui.cA;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: br.H, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/H.class */
class C1230H implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f8347a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1255s f8348b;

    C1230H(C1255s c1255s, G.R r2, String str) {
        this.f8348b = c1255s;
        try {
            C0113cs.a().a(r2.c(), str, this);
        } catch (V.a e2) {
            bH.C.a("Paint updates disabled for Component. " + e2.getMessage());
            e2.printStackTrace();
        }
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        Iterator it = this.f8347a.iterator();
        while (it.hasNext()) {
            ((cA) it.next()).a();
        }
    }

    public void a(cA cAVar) {
        this.f8347a.add(cAVar);
    }
}
