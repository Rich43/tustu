package ao;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/* renamed from: ao.cf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cf.class */
class C0669cf implements MenuListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5488a;

    C0669cf(bP bPVar) {
        this.f5488a = bPVar;
    }

    @Override // javax.swing.event.MenuListener
    public void menuSelected(MenuEvent menuEvent) {
        this.f5488a.f5347a.p().a((dO) menuEvent.getSource());
    }

    @Override // javax.swing.event.MenuListener
    public void menuDeselected(MenuEvent menuEvent) {
        ((dO) menuEvent.getSource()).removeAll();
    }

    @Override // javax.swing.event.MenuListener
    public void menuCanceled(MenuEvent menuEvent) {
    }
}
