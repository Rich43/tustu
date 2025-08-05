package ao;

import ar.C0836c;
import ar.C0837d;
import ar.C0839f;
import ar.InterfaceC0838e;
import h.C1737b;
import java.util.Iterator;
import javax.swing.Action;

/* renamed from: ao.bc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bc.class */
public class C0639bc implements InterfaceC0838e {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC0642bf f5394a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aQ f5395b;

    C0639bc(aQ aQVar, InterfaceC0642bf interfaceC0642bf) {
        this.f5395b = aQVar;
        this.f5394a = interfaceC0642bf;
    }

    @Override // ar.InterfaceC0838e
    public void a(C0836c c0836c) {
        C0836c c0836cC;
        this.f5394a.a();
        C0645bi.a().c().k();
        if (C0804hg.a().r() != null) {
            if (C1737b.a().a("tabbedQuickViews")) {
                h.i.c("lastSelectedQuickViewName", c0836c.b());
                return;
            }
            if (c0836c == null || c0836c.b().equals(Action.DEFAULT) || (c0836cC = C0839f.a().c(Action.DEFAULT)) == null) {
                return;
            }
            c0836cC.d();
            Iterator it = c0836c.c().iterator();
            while (it.hasNext()) {
                c0836cC.a((C0837d) it.next());
            }
            C0839f.a().a(Action.DEFAULT);
        }
    }

    @Override // ar.InterfaceC0838e
    public boolean a(String str, String str2) {
        this.f5395b.f(str);
        return true;
    }

    @Override // ar.InterfaceC0838e
    public void b(C0836c c0836c) {
    }

    @Override // ar.InterfaceC0838e
    public void c(C0836c c0836c) {
    }

    @Override // ar.InterfaceC0838e
    public void a(String str) {
    }

    @Override // ar.InterfaceC0838e
    public void b(String str) {
    }

    @Override // ar.InterfaceC0838e
    public void c(String str) {
    }
}
