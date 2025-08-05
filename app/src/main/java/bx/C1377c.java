package bx;

import bH.aa;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

/* renamed from: bx.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bx/c.class */
public class C1377c extends JPanel implements l, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    aa f9188a;

    /* renamed from: b, reason: collision with root package name */
    o f9189b;

    /* renamed from: c, reason: collision with root package name */
    m f9190c;

    /* renamed from: d, reason: collision with root package name */
    InterfaceC1376b f9191d;

    /* renamed from: e, reason: collision with root package name */
    int f9192e = -1;

    /* renamed from: f, reason: collision with root package name */
    JButton f9193f;

    public C1377c(InterfaceC1376b interfaceC1376b, aa aaVar) {
        this.f9188a = null;
        this.f9189b = null;
        this.f9190c = null;
        this.f9188a = aaVar;
        this.f9191d = interfaceC1376b;
        interfaceC1376b.a(this);
        setBorder(BorderFactory.createEmptyBorder(eJ.a(10), eJ.a(10), eJ.a(10), eJ.a(10)));
        setLayout(new BorderLayout());
        this.f9189b = new o(aaVar);
        Iterator it = interfaceC1376b.d().iterator();
        while (it.hasNext()) {
            this.f9189b.a((j) it.next());
        }
        this.f9189b.getSelectionModel().addListSelectionListener(new i(this, null));
        JScrollPane jScrollPane = new JScrollPane(this.f9189b);
        this.f9189b.setFillsViewportHeight(true);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane.setPreferredSize(eJ.a(320, 180));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(c("Data Filters")));
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        add(BorderLayout.CENTER, jPanel);
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        JButton jButton = new JButton(c("New Data Filter"), new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("add24.png")))));
        jButton.addActionListener(new C1378d(this));
        jToolBar.add(jButton);
        this.f9193f = new JButton(c("Delete Data Filter"), new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("delete24.png")))));
        this.f9193f.addActionListener(new C1379e(this));
        jToolBar.add(this.f9193f);
        this.f9193f.setEnabled(false);
        jPanel.add("North", jToolBar);
        this.f9190c = new m(aaVar);
        this.f9190c.setBorder(BorderFactory.createTitledBorder(c("Filter Editor")));
        this.f9190c.a(new C1380f(this));
        add("South", this.f9190c);
    }

    private JMenuBar e() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        jMenuBar.add(jMenu);
        JMenuItem jMenuItem = new JMenuItem("Export Filters");
        jMenuItem.addActionListener(new C1381g(this));
        jMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem("Import Filters");
        jMenuItem2.addActionListener(new h(this));
        jMenu.add(jMenuItem2);
        return jMenuBar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        this.f9191d.c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        this.f9191d.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        if (this.f9190c.d() && bV.a(c("The Filter currently being edited has not been saved.") + "\n" + c("Would you like to save it now?"), (Component) this.f9190c, true)) {
            if (!a()) {
                if (this.f9192e >= 0) {
                    this.f9189b.getSelectionModel().setSelectionInterval(i2, i2);
                    return;
                }
                return;
            }
            this.f9190c.b();
        }
        this.f9193f.setEnabled(true);
        this.f9192e = i2;
        this.f9190c.a(this.f9189b.a(i2));
    }

    public boolean a() {
        j jVarA = this.f9190c.a();
        try {
            if (!this.f9191d.a(jVarA)) {
                return false;
            }
            this.f9191d.b(jVarA);
            this.f9190c.b();
            return true;
        } catch (x e2) {
            bV.d(c(e2.getLocalizedMessage()), this);
            return false;
        }
    }

    public void b() {
        this.f9190c.b(new j());
    }

    public void b(String str) {
        this.f9191d.a(str);
    }

    public void c() {
        j jVarA = this.f9190c.a();
        if (this.f9190c != null) {
            b(jVarA.a());
            this.f9193f.setEnabled(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String c(String str) {
        return this.f9188a != null ? this.f9188a.a(str) : str;
    }

    public void a(Window window) {
        JDialog jDialogB = bV.b(this, window, c("Data Filter Editor"), this);
        jDialogB.add("North", e());
        jDialogB.pack();
        bV.a((Component) window, (Component) jDialogB);
        jDialogB.setVisible(true);
    }

    public void a(k kVar) {
        this.f9190c.a(kVar);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        if (this.f9190c.d()) {
            a();
        }
    }

    @Override // bx.l
    public void a(j jVar) {
        this.f9189b.b(jVar);
    }

    @Override // bx.l
    public void a(String str) {
        this.f9189b.a(str);
    }

    @Override // bx.l
    public void b(j jVar) {
        this.f9189b.b(jVar);
    }

    public JTextPane d() {
        return this.f9190c.e();
    }
}
