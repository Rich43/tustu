package as;

import ay.C0924a;
import ay.C0926c;
import ay.InterfaceC0939p;
import bH.ab;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/* renamed from: as.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:as/g.class */
public class C0852g extends JDialog implements InterfaceC0939p, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    C0854i f6252a;

    /* renamed from: b, reason: collision with root package name */
    List f6253b;

    /* renamed from: c, reason: collision with root package name */
    private InterfaceC0846a f6254c;

    /* renamed from: d, reason: collision with root package name */
    private List f6255d;

    public C0852g(Window window, InterfaceC0846a interfaceC0846a) {
        this(window, interfaceC0846a, true);
    }

    public C0852g(Window window, InterfaceC0846a interfaceC0846a, boolean z2) {
        super(window, "Remote File Browser");
        this.f6253b = new ArrayList();
        this.f6254c = null;
        this.f6255d = new ArrayList();
        super.setDefaultCloseOperation(2);
        this.f6254c = interfaceC0846a;
        setLayout(new BorderLayout());
        this.f6252a = new C0854i(this);
        add(BorderLayout.CENTER, this.f6252a);
        C0924a.c().a(this);
        Iterator it = C0924a.c().d().iterator();
        while (it.hasNext()) {
            a((C0926c) it.next());
        }
        if (z2) {
            JMenuBar jMenuBar = new JMenuBar();
            JMenu jMenu = new JMenu(a("Options"));
            jMenuBar.add(jMenu);
            JMenuItem jMenuItem = new JMenuItem(a("Preferences"));
            jMenu.add(jMenuItem);
            jMenuItem.addActionListener(new C0853h(this));
            setJMenuBar(jMenuBar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        C0855j c0855j = new C0855j(this);
        c0855j.pack();
        bV.a((Window) this, (Component) c0855j);
        c0855j.setVisible(true);
    }

    public void a(InterfaceC1565bc interfaceC1565bc) {
        this.f6255d.add(interfaceC1565bc);
    }

    public void a(C0926c c0926c) {
        int iF = f(c0926c);
        if (iF >= 0) {
            this.f6252a.setEnabledAt(iF, true);
            return;
        }
        C0847b c0847b = new C0847b(c0926c);
        c0847b.a(this.f6254c);
        this.f6253b.add(c0847b);
        this.f6252a.add(c0847b, e(c0926c));
    }

    private String e(C0926c c0926c) {
        String str = "";
        Iterator it = c0926c.b().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String str2 = (String) it.next();
            if (str2.equalsIgnoreCase("name")) {
                str = c0926c.a(str2) + " - ";
                break;
            }
        }
        return str + c0926c.c();
    }

    public void b(C0926c c0926c) {
        int iF = f(c0926c);
        if (iF >= 0) {
            this.f6252a.setEnabledAt(iF, false);
        }
    }

    private int f(C0926c c0926c) {
        for (int i2 = 0; i2 < this.f6253b.size(); i2++) {
            if (c0926c.c().equals(((C0847b) this.f6253b.get(i2)).b().c())) {
                return i2;
            }
        }
        return -1;
    }

    @Override // java.awt.Window
    public void dispose() {
        if (isVisible()) {
            h.i.c(h.i.f12320am, getWidth() + "");
            h.i.c(h.i.f12321an, getHeight() + "");
        }
        close();
        super.dispose();
    }

    @Override // java.awt.Dialog, java.awt.Window, java.awt.Component
    public void setVisible(boolean z2) {
        if (isVisible()) {
            h.i.c(h.i.f12320am, getWidth() + "");
            h.i.c(h.i.f12321an, getHeight() + "");
        }
        super.setVisible(z2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        C0924a.c().b(this);
        Iterator it = this.f6253b.iterator();
        while (it.hasNext()) {
            ((C0847b) it.next()).close();
        }
        Iterator it2 = this.f6255d.iterator();
        while (it2.hasNext()) {
            ((InterfaceC1565bc) it2.next()).close();
        }
    }

    @Override // ay.InterfaceC0939p
    public void c(C0926c c0926c) {
        b(c0926c);
    }

    @Override // ay.InterfaceC0939p
    public void d(C0926c c0926c) {
        a(c0926c);
    }

    private String a(String str) {
        return ab.a().a(str);
    }
}
