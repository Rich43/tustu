package bt;

import G.C0047ag;
import G.C0048ah;
import G.C0051ak;
import G.C0070bc;
import G.C0072be;
import G.C0076bi;
import G.C0077bj;
import G.C0079bl;
import G.C0081bn;
import G.C0084bq;
import G.C0086bs;
import G.C0088bu;
import G.C0092by;
import ai.InterfaceC0515e;
import bH.C1007o;
import c.InterfaceC1385d;
import com.efiAnalytics.tuningwidgets.panels.C1488ae;
import com.efiAnalytics.tuningwidgets.panels.C1516n;
import com.efiAnalytics.tuningwidgets.portEditor.OutputPortEditor;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import r.C1806i;

/* renamed from: bt.bf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bf.class */
public class C1324bf extends C1348g implements bY, InterfaceC1385d, InterfaceC1565bc {

    /* renamed from: n, reason: collision with root package name */
    C0088bu f9009n;

    /* renamed from: o, reason: collision with root package name */
    G.R f9010o;

    /* renamed from: a, reason: collision with root package name */
    private boolean f9011a;

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1662et f9012b;

    public C1324bf() {
        this.f9009n = null;
        this.f9010o = null;
        this.f9011a = true;
        this.f9012b = null;
    }

    public C1324bf(G.R r2, C0088bu c0088bu) {
        this(r2, c0088bu, true);
    }

    public C1324bf(G.R r2, C0088bu c0088bu, String str, InterfaceC1662et interfaceC1662et) {
        this(r2, c0088bu, true, true, null, interfaceC1662et);
    }

    public C1324bf(G.R r2, C0088bu c0088bu, boolean z2) {
        this(r2, c0088bu, z2, true, null);
    }

    private C1324bf(G.R r2, C0088bu c0088bu, boolean z2, boolean z3, String str) {
        this(r2, c0088bu, z2, z3, str, null);
    }

    private C1324bf(G.R r2, C0088bu c0088bu, boolean z2, boolean z3, String str, InterfaceC1662et interfaceC1662et) {
        this.f9009n = null;
        this.f9010o = null;
        this.f9011a = true;
        this.f9012b = null;
        this.f9011a = z2;
        this.f9009n = c0088bu;
        this.f9010o = r2;
        this.f9012b = interfaceC1662et;
        if (str != null && !str.isEmpty()) {
            setName(str);
        } else if (c0088bu.aJ().isEmpty() && c0088bu.L()) {
            Iterator itK = c0088bu.K();
            while (itK.hasNext()) {
                C0088bu c0088bu2 = (C0088bu) itK.next();
                if (c0088bu2.aJ() != null && !c0088bu2.aJ().isEmpty()) {
                    setName(c0088bu2.aJ());
                }
            }
        } else {
            setName(c0088bu.aJ());
        }
        if (z3) {
            try {
                a(r2, c0088bu);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void a(G.R r2, C0088bu c0088bu) {
        if (this.f9009n == null) {
            this.f9009n = c0088bu;
        }
        if (this.f9010o == null) {
            this.f9010o = r2;
        }
        InterfaceC1284L interfaceC1284LA = C1283K.a().a(c0088bu);
        if (interfaceC1284LA != null) {
            a(r2, interfaceC1284LA, c0088bu);
            return;
        }
        if (c0088bu instanceof C0072be) {
            a(r2, (C0072be) c0088bu);
            return;
        }
        if (c0088bu instanceof C0079bl) {
            a(r2, (C0079bl) c0088bu);
            return;
        }
        if (c0088bu instanceof C0076bi) {
            a(r2, (C0076bi) c0088bu);
            return;
        }
        if (c0088bu instanceof G.bI) {
            a(r2, (G.bI) c0088bu);
            return;
        }
        if (c0088bu instanceof C0077bj) {
            a(r2, (C0077bj) c0088bu);
            return;
        }
        if (c0088bu instanceof C0084bq) {
            a(r2, (C0084bq) c0088bu);
            return;
        }
        if (c0088bu instanceof G.aS) {
            a(r2, (G.aS) c0088bu);
            return;
        }
        if (c0088bu instanceof C0047ag) {
            a(r2, (C0047ag) c0088bu);
            return;
        }
        if (c0088bu instanceof C0070bc) {
            a(r2, (C0070bc) c0088bu);
            return;
        }
        if (c0088bu instanceof G.bC) {
            a(r2, (G.bC) c0088bu);
            return;
        }
        if (c0088bu instanceof G.bA) {
            a(r2, (G.bA) c0088bu);
            return;
        }
        if (c0088bu instanceof C0086bs) {
            a(r2, (C0086bs) c0088bu);
            return;
        }
        if (c0088bu instanceof C0092by) {
            a(r2, (C0092by) c0088bu);
        } else if (c0088bu instanceof C0081bn) {
            bH.C.a("FTP Browser should have a Handler!!");
        } else {
            b(r2, c0088bu);
        }
    }

    protected void a(G.R r2, C0072be c0072be) {
        Component c1320bb;
        setLayout(new BorderLayout());
        C0076bi c0076biD = null;
        if (!c0072be.k()) {
            c0076biD = r2.e().d(c0072be.aJ());
        }
        if (c0076biD == null || !C1806i.a().a("sgds  gdsdggsxbcuj")) {
            C1337bs c1337bs = new C1337bs(r2, c0072be, m());
            a(c1337bs);
            add(BorderLayout.CENTER, c1337bs);
            return;
        }
        if (this.f9012b != null) {
            c1320bb = new C1320bb(r2, c0076biD, c0072be, this.f9012b);
        } else if (getName() == null || getName().length() <= 0 || getName().equals(c0072be.aJ())) {
            c1320bb = new C1320bb(r2, c0076biD, c0072be);
        } else {
            c1320bb = new C1320bb(r2, c0076biD, c0072be, new C1350i(getName() + "Multiview_" + c0072be.aJ()));
        }
        a((InterfaceC1385d) c1320bb);
        add(BorderLayout.CENTER, c1320bb);
    }

    protected void a(G.R r2, C0079bl c0079bl) {
        try {
            setLayout(new BorderLayout());
            add(BorderLayout.CENTER, new C1303al(r2, c0079bl, this.f9011a, this.f9012b));
            if (c0079bl.y() != null) {
                Dimension dimensionA = eJ.a(c0079bl.y().a(), c0079bl.y().b());
                super.setPreferredSize(dimensionA);
                super.setMinimumSize(dimensionA);
            }
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d("Unable to show CurveGraph" + c0079bl.aJ() + "\n" + e2.getMessage(), this);
            Logger.getLogger(C1324bf.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    protected void a(G.R r2, G.bI bIVar) {
        setLayout(new BorderLayout());
        bz bzVar = new bz(r2, bIVar);
        if (bIVar.aH() != null && !bIVar.aH().isEmpty()) {
            bzVar.b_(bIVar.aH());
        }
        add(BorderLayout.CENTER, bzVar);
        a(bzVar);
    }

    protected void a(G.R r2, C0076bi c0076bi) {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, new U(r2, c0076bi, true, this.f9012b));
    }

    private void a(InterfaceC1385d interfaceC1385d) {
        if (interfaceC1385d.a_() != null && a_() != null && !a_().isEmpty() && !interfaceC1385d.a_().equals(a_())) {
            interfaceC1385d.b_("(" + interfaceC1385d.a_() + ") && (" + a_() + ")");
        } else if (interfaceC1385d.a_() != null) {
            interfaceC1385d.b_(interfaceC1385d.a_());
        } else if (a_() != null) {
            interfaceC1385d.b_(a_());
        }
    }

    protected void a(G.R r2, C0084bq c0084bq) {
        if (C1806i.a().a("0532fewkjfewpoijrew98")) {
            setLayout(new BorderLayout());
            add(BorderLayout.CENTER, new C1360s(r2, c0084bq));
        } else {
            setMaximumSize(new Dimension(1, 1));
            setPreferredSize(new Dimension(1, 1));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x040b  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x04c0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:246:0x043f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x031d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void b(G.R r9, G.C0088bu r10) {
        /*
            Method dump skipped, instructions count: 1906
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: bt.C1324bf.b(G.R, G.bu):void");
    }

    protected void a(G.R r2, C0077bj c0077bj) {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, new C1288P(r2, c0077bj));
    }

    public void close() {
        Object[] components = getComponents();
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (components[i2] instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) components[i2]).close();
            }
        }
    }

    public ArrayList k() {
        return a(new ArrayList(), (Component) this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ArrayList a(ArrayList arrayList, Component component) {
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
                a(arrayList, container.getComponent(i2));
            }
        }
        if (component instanceof InterfaceC0515e) {
            InterfaceC0515e interfaceC0515e = (InterfaceC0515e) component;
            if (interfaceC0515e.a() != null && !arrayList.contains(interfaceC0515e.a())) {
                arrayList.add(interfaceC0515e.a());
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void a(Component component, boolean z2) {
        if (component instanceof InterfaceC1349h) {
            ((InterfaceC1349h) component).a();
            return;
        }
        if (!(component instanceof Container)) {
            component.setEnabled(z2);
            return;
        }
        Container container = (Container) component;
        for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
            a(container.getComponent(i2), z2);
        }
    }

    public void a() {
        new RunnableC1327bi(this, this).run();
    }

    private void a(G.R r2, G.bC bCVar) {
        setLayout(new BorderLayout());
        Component c1324bf = null;
        if (bCVar.aJ().equals("std_injection")) {
            c1324bf = new com.efiAnalytics.tuningwidgets.panels.aF(r2);
        } else if (bCVar.aJ().equals("std_realtime")) {
            bH.C.a("Realtime Display should have used handler?");
        } else if (bCVar.aJ().equals("std_trigwiz")) {
            bH.C.a("TriggerWizard should have used handler?");
        } else if (bCVar.aJ().equals("std_ms3SdConsole")) {
            bH.C.a("MS3 SD Card Console should have used handler?");
        } else if (bCVar.aJ().equals("std_ms3Rtc")) {
            bH.C.a("MS3 Real-Time Clock should have used handler?");
        } else if (bCVar.aJ().equals("std_accel")) {
            K.a aVar = new K.a(r2, bCVar.a(), C1806i.a().a("0532fewkjfewpoijrew98"));
            aVar.s("");
            c1324bf = new C1324bf(r2, aVar);
        }
        if (c1324bf != null) {
            add(BorderLayout.CENTER, c1324bf);
        } else {
            bH.C.b("Undefined dialog panel: " + ((Object) bCVar));
        }
    }

    private void a(G.R r2, G.aS aSVar) {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, new OutputPortEditor(r2, aSVar));
    }

    private void a(G.R r2, C0047ag c0047ag) {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, new C1516n(r2, c0047ag));
    }

    private void a(G.R r2, C0070bc c0070bc) {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, new C1488ae(r2, c0070bc));
    }

    public boolean l() {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if ((getComponent(i2) instanceof aT) && ((aT) getComponent(i2)).k()) {
                return true;
            }
            if ((getComponent(i2) instanceof C1324bf) && ((C1324bf) getComponent(i2)).l()) {
                return true;
            }
        }
        return false;
    }

    public void a(ArrayList arrayList) {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (getComponent(i2) instanceof InterfaceC1356o) {
                InterfaceC1356o interfaceC1356o = (InterfaceC1356o) getComponent(i2);
                if (arrayList.contains(interfaceC1356o.c())) {
                    interfaceC1356o.a(true);
                }
            } else if (getComponent(i2) instanceof C1324bf) {
                ((C1324bf) getComponent(i2)).a(arrayList);
            }
        }
    }

    public boolean m() {
        return this.f9011a;
    }

    private void a(G.R r2, G.bA bAVar) {
        setLayout(new BorderLayout());
        bG bGVar = new bG();
        bGVar.setBackground(Color.BLACK);
        bGVar.a(r2, bAVar.a());
        add(BorderLayout.CENTER, bGVar);
    }

    private void a(G.R r2, C0086bs c0086bs) {
        setLayout(new BorderLayout());
        C1359r c1359r = new C1359r(r2, c0086bs);
        c1359r.a(c0086bs.b());
        Iterator it = c0086bs.a().iterator();
        while (it.hasNext()) {
            c1359r.a((C0051ak) it.next());
        }
        add(BorderLayout.CENTER, c1359r);
    }

    private void a(G.R r2, C0092by c0092by) {
        setLayout(new BorderLayout());
        C1276D c1276d = new C1276D(r2, c0092by);
        c1276d.a(c0092by.a());
        Iterator it = c0092by.b().iterator();
        while (it.hasNext()) {
            c1276d.a((C0048ah) it.next());
        }
        add(BorderLayout.CENTER, c1276d);
    }

    private void a(G.R r2, InterfaceC1284L interfaceC1284L, C0088bu c0088bu) {
        setLayout(new BorderLayout());
        try {
            add(BorderLayout.CENTER, interfaceC1284L.a(r2, c0088bu));
        } catch (V.a e2) {
            Window[] windows = Window.getWindows();
            com.efiAnalytics.ui.bV.d(e2.getLocalizedMessage(), windows.length > 0 ? windows[0] : null);
        }
    }

    @Override // bt.C1348g, c.InterfaceC1385d
    public String a_() {
        return this.f9009n == null ? "" : super.a_();
    }

    public boolean n() {
        return l() || c();
    }

    @Override // java.awt.Component
    public boolean hasFocus() {
        return getComponentCount() == 1 ? getComponent(0).hasFocus() : super.hasFocus();
    }

    private boolean c() {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if ((getComponent(i2) instanceof C1324bf) && ((C1324bf) getComponent(i2)).c()) {
                return true;
            }
        }
        return false;
    }

    public void b() {
        if (this.f9009n == null || this.f9009n.V() == null || this.f9009n.V().equals("")) {
            return;
        }
        boolean zA = true;
        try {
            zA = C1007o.a(this.f9009n.V(), this.f9010o);
        } catch (V.g e2) {
            Logger.getLogger(aT.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (isVisible() && !zA) {
            setVisible(false);
            if (getParent() instanceof JPanel) {
                ((JPanel) getParent()).revalidate();
                return;
            }
            return;
        }
        if (isVisible() || !zA) {
            return;
        }
        setVisible(true);
        if (getParent() instanceof JPanel) {
            ((JPanel) getParent()).revalidate();
        }
    }

    public InterfaceC1662et o() {
        return this.f9012b;
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f9012b = interfaceC1662et;
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f9010o;
    }

    public void a(G.R r2) {
        this.f9010o = r2;
    }

    public List p() {
        return a((List) new ArrayList(), (Component) this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public List a(List list, Component component) {
        if (component instanceof InterfaceC1282J) {
            list.add((InterfaceC1282J) component);
        }
        if (component instanceof Container) {
            for (Component component2 : ((Container) component).getComponents()) {
                list = a(list, component2);
            }
        }
        return list;
    }
}
