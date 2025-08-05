package p;

import bH.aa;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

/* loaded from: TunerStudioMS.jar:p/J.class */
public class J extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    aa f13163a;

    /* renamed from: b, reason: collision with root package name */
    InterfaceC1773B f13165b;

    /* renamed from: c, reason: collision with root package name */
    R f13166c;

    /* renamed from: d, reason: collision with root package name */
    D f13167d;

    /* renamed from: f, reason: collision with root package name */
    JButton f13169f;

    /* renamed from: g, reason: collision with root package name */
    JButton f13170g;

    /* renamed from: h, reason: collision with root package name */
    private List f13164h = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    int f13168e = -1;

    public J(InterfaceC1773B interfaceC1773B, aa aaVar) {
        this.f13163a = null;
        this.f13163a = aaVar;
        this.f13166c = new R(aaVar);
        this.f13165b = interfaceC1773B;
        setBorder(BorderFactory.createEmptyBorder(eJ.a(10), eJ.a(10), eJ.a(10), eJ.a(10)));
        setLayout(new BorderLayout());
        Iterator it = interfaceC1773B.b().iterator();
        while (it.hasNext()) {
            this.f13166c.a((d.m) it.next());
        }
        this.f13166c.getSelectionModel().addListSelectionListener(new Q(this, null));
        JScrollPane jScrollPane = new JScrollPane(this.f13166c);
        this.f13166c.setFillsViewportHeight(true);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane.setPreferredSize(eJ.a(320, 180));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(a("User Actions")));
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        add(BorderLayout.CENTER, jPanel);
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        JButton jButton = new JButton(a("New User Action"), new ImageIcon(eJ.a(cO.a().a(cO.f11088d, this, 24))));
        jButton.addActionListener(new K(this));
        jToolBar.add(jButton);
        this.f13169f = new JButton(a("Delete User Action"), new ImageIcon(eJ.a(cO.a().a(cO.f11089e, this, 24))));
        this.f13169f.addActionListener(new L(this));
        jToolBar.add(this.f13169f);
        this.f13169f.setEnabled(false);
        this.f13170g = new JButton(a("Test User Action"), new ImageIcon(eJ.a(cO.a().a(cO.f11111A, this, 24))));
        this.f13170g.addActionListener(new M(this));
        jToolBar.add(this.f13170g);
        this.f13170g.setEnabled(false);
        jPanel.add("North", jToolBar);
        this.f13167d = new D(aaVar);
        this.f13167d.setBorder(BorderFactory.createTitledBorder(a("User Action Editor")));
        this.f13167d.a(new N(this));
        add("South", this.f13167d);
    }

    private JMenuBar a() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu(a("File"));
        jMenuBar.add(jMenu);
        JMenuItem jMenuItem = new JMenuItem(a("Export User Actions"));
        jMenuItem.addActionListener(new O(this));
        jMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem(a("Import User Actions"));
        jMenuItem2.addActionListener(new P(this));
        jMenu.add(jMenuItem2);
        return jMenuBar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        d.m mVar = new d.m();
        this.f13166c.getSelectionModel().clearSelection();
        this.f13167d.a(mVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (this.f13166c.getSelectedRow() >= 0) {
            try {
                this.f13166c.a(this.f13166c.getSelectedRow()).a(new Properties());
            } catch (d.e e2) {
                bV.d(a(e2.getMessage()), this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean d() {
        d.m mVarB = this.f13167d.b();
        if (mVarB == null) {
            bV.d(a("Editor returned no Action."), this);
            return false;
        }
        if (this.f13167d.e() && this.f13165b.b(mVarB.a()) != null && !bV.a(a("A User Action with the name already exists.") + "\n" + a("If you save this Action with this name it will replace the existing Action.") + "\n\n" + a("Are you sure you wish to replace the existing Action?"), (Component) this, true)) {
            return false;
        }
        try {
            mVarB.k();
            this.f13165b.a(mVarB);
            this.f13167d.d();
            this.f13166c.a(mVarB);
            this.f13166c.a();
            mVarB.n();
            return true;
        } catch (d.e e2) {
            bV.d(a(e2.getMessage()), this);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        d.m mVarB = this.f13166c.b();
        if (mVarB != null) {
            this.f13166c.a(mVarB.a());
            this.f13165b.c(mVarB.a());
            this.f13166c.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) {
        return this.f13163a != null ? this.f13163a.a(str) : str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        this.f13165b.d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        this.f13165b.c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        if (this.f13167d.c() && bV.a(a("The User Action currently being edited has not been saved.") + "\n" + a("Would you like to save it now?"), (Component) this.f13167d, true)) {
            if (!d()) {
                if (this.f13168e >= 0) {
                    this.f13166c.getSelectionModel().setSelectionInterval(i2, i2);
                    return;
                }
                return;
            }
            this.f13167d.d();
        }
        this.f13169f.setEnabled(true);
        this.f13170g.setEnabled(this.f13166c.getSelectedRowCount() > 0);
        this.f13168e = i2;
        this.f13167d.b(this.f13166c.a(i2));
    }

    public void a(Window window) {
        JDialog jDialogB = bV.b(this, window, a("User Action Editor"), this);
        jDialogB.add("North", a());
        jDialogB.pack();
        bV.a((Component) window, (Component) jDialogB);
        jDialogB.setVisible(true);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
    }
}
