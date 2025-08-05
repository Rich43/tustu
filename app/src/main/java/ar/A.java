package aR;

import G.R;
import G.T;
import G.bL;
import br.C1232J;
import br.C1242f;
import com.efiAnalytics.ui.cY;
import d.InterfaceC1711c;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aR/A.class */
public class A implements cY, InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f3846a = "toggleAutoTune";

    /* renamed from: b, reason: collision with root package name */
    public static String f3847b = "tableName";

    /* renamed from: c, reason: collision with root package name */
    d.k f3848c = new d.k();

    /* renamed from: d, reason: collision with root package name */
    String f3849d = null;

    public A() {
        d.i iVar = new d.i(f3847b, "");
        iVar.a(0);
        ArrayList arrayList = new ArrayList();
        Iterator it = C1242f.a().c().iterator();
        while (it.hasNext()) {
            arrayList.add((String) it.next());
        }
        iVar.a(arrayList);
        iVar.c("This is the name of the Table in which you wish to toggle Auto Tune active state. If autotune is running, it will stop it, if not running will start it on this table.");
        this.f3848c.add(iVar);
        h();
    }

    private void h() {
        if (T.a().c() == null) {
            this.f3849d = null;
        } else {
            this.f3849d = T.a().c().c();
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return f3846a;
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return C1818g.b("Toggle Auto Tune");
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Settings Dialogs";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return (T.a().c() == null || aE.a.A() == null) ? false : true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws d.e {
        String property = properties.getProperty(f3847b);
        if (property == null) {
            throw new d.e("Table name is required.");
        }
        try {
            C1242f.a().a(property);
        } catch (V.a e2) {
            throw new d.e(e2.getLocalizedMessage());
        }
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty(f3847b);
        if (property == null) {
            throw new d.e("Table name is required.");
        }
        if (!j().contains(property) && C1806i.a().a("poij  fdsz poi9ure895 ms7(")) {
            throw new d.e("Invalid Table Index: " + property);
        }
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        if (this.f3848c.isEmpty() || this.f3849d == null || (i() != null && !this.f3849d.equals(i()))) {
            List listJ = j();
            d.i iVar = new d.i(f3847b, "");
            ArrayList arrayList = new ArrayList();
            Iterator it = listJ.iterator();
            while (it.hasNext()) {
                arrayList.add((String) it.next());
            }
            iVar.a(arrayList);
            this.f3848c.add(iVar);
            this.f3849d = i();
        }
        return this.f3848c;
    }

    private String i() {
        R rC = T.a().c();
        if (rC == null) {
            return null;
        }
        return rC.c();
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return true;
    }

    @Override // com.efiAnalytics.ui.cY
    public List a(ActionListener actionListener) {
        ArrayList arrayList = new ArrayList();
        JMenu jMenu = new JMenu(b());
        arrayList.add(jMenu);
        B b2 = new B(this, actionListener);
        for (String str : j()) {
            JMenuItem jMenuItem = new JMenuItem(bL.c(T.a().c(), str));
            jMenuItem.setActionCommand(str);
            jMenuItem.addActionListener(b2);
            jMenu.add(jMenuItem);
        }
        return arrayList;
    }

    private List j() {
        try {
            return C1232J.a().a(T.a().c());
        } catch (V.g e2) {
            Logger.getLogger(A.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return new ArrayList();
        }
    }
}
