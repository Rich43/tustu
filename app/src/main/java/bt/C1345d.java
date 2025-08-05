package bt;

import com.efiAnalytics.ui.InterfaceC1565bc;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

/* renamed from: bt.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/d.class */
public class C1345d extends JPanel {

    /* renamed from: G, reason: collision with root package name */
    ArrayList f9089G = new ArrayList();

    public void a(InterfaceC1565bc interfaceC1565bc) {
        this.f9089G.add(interfaceC1565bc);
    }

    protected void l() {
        Iterator it = this.f9089G.iterator();
        while (it.hasNext()) {
            ((InterfaceC1565bc) it.next()).close();
        }
    }
}
