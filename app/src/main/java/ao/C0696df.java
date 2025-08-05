package ao;

import c.InterfaceC1386e;
import com.efiAnalytics.ui.InterfaceC1581bs;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/* renamed from: ao.df, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/df.class */
class C0696df implements MenuListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5553a;

    C0696df(bP bPVar) {
        this.f5553a = bPVar;
    }

    @Override // javax.swing.event.MenuListener
    public void menuCanceled(MenuEvent menuEvent) {
    }

    @Override // javax.swing.event.MenuListener
    public void menuDeselected(MenuEvent menuEvent) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.swing.event.MenuListener
    public void menuSelected(MenuEvent menuEvent) {
        for (Q.a aVar : ((JMenu) menuEvent.getSource()).getMenuComponents()) {
            if (aVar instanceof InterfaceC1581bs) {
                InterfaceC1386e interfaceC1386eE = ((InterfaceC1581bs) aVar).e();
                aVar.setEnabled(interfaceC1386eE == null || interfaceC1386eE.a());
            }
        }
    }
}
