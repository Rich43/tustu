package ao;

import com.efiAnalytics.ui.InterfaceC1579bq;
import javax.swing.JMenu;

/* loaded from: TunerStudioMS.jar:ao/dO.class */
class dO extends JMenu implements InterfaceC1579bq {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5529a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public dO(bP bPVar, String str) {
        super(str);
        this.f5529a = bPVar;
        addMenuListener(new dP(this, bPVar));
    }
}
