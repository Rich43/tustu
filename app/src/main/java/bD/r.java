package bD;

import bH.aa;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import com.efiAnalytics.remotefileaccess.RemoteFileAccess;
import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

/* loaded from: TunerStudioMS.jar:bD/r.class */
public class r extends JPanel implements InterfaceC0955a, InterfaceC1565bc {

    /* renamed from: j, reason: collision with root package name */
    private RemoteFileAccess f6686j;

    /* renamed from: a, reason: collision with root package name */
    G f6687a;

    /* renamed from: c, reason: collision with root package name */
    JToolBar f6689c;

    /* renamed from: d, reason: collision with root package name */
    aa f6690d;

    /* renamed from: e, reason: collision with root package name */
    C0962h f6691e;

    /* renamed from: b, reason: collision with root package name */
    C f6688b = null;

    /* renamed from: f, reason: collision with root package name */
    dO f6692f = new dO();

    /* renamed from: g, reason: collision with root package name */
    C0956b f6693g = new C0956b(this);

    /* renamed from: k, reason: collision with root package name */
    private InterfaceC0961g f6694k = null;

    /* renamed from: h, reason: collision with root package name */
    D f6695h = new D(this);

    /* renamed from: i, reason: collision with root package name */
    boolean f6696i = false;

    public r(RemoteFileAccess remoteFileAccess, aa aaVar) {
        this.f6686j = null;
        this.f6687a = null;
        this.f6689c = null;
        this.f6690d = null;
        this.f6691e = null;
        this.f6686j = remoteFileAccess;
        this.f6690d = aaVar;
        setLayout(new BorderLayout());
        remoteFileAccess.addFileDownloadProgressListener(this.f6693g);
        this.f6689c = f();
        add("North", this.f6689c);
        this.f6687a = new G();
        add(BorderLayout.CENTER, this.f6687a);
        this.f6687a.b().addMouseListener(new E(this));
        this.f6691e = new C0962h(aaVar);
        add("South", this.f6691e);
        this.f6692f.a(aaVar == null ? "Cancel" : aaVar.a("Cancel"), new s(this, remoteFileAccess));
        remoteFileAccess.addRefreshNeededListener(this.f6695h);
        e();
    }

    private JToolBar f() {
        int iA = eJ.a(30);
        this.f6689c = new B(this, "Remote Files");
        this.f6689c.setFloatable(false);
        this.f6689c.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0, 3, 3));
        this.f6689c.addSeparator();
        this.f6689c.add(jPanel, "East");
        JButton jButton = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("download24.png")))));
        jButton.setFocusable(false);
        jButton.setToolTipText(this.f6690d.a("Download Selected File(s)"));
        jButton.addActionListener(new t(this));
        jButton.setPreferredSize(new Dimension(iA, iA));
        jPanel.add(jButton);
        JButton jButton2 = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("reload24.png")))));
        jButton2.setFocusable(false);
        jButton2.setToolTipText(this.f6690d.a("Refresh File List"));
        jButton2.addActionListener(new u(this));
        jButton2.setPreferredSize(new Dimension(iA, iA));
        jPanel.add(jButton2);
        JButton jButton3 = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("delete24.png")))));
        jButton3.setFocusable(false);
        jButton3.setToolTipText(this.f6690d.a("Delete Selected File(s)"));
        jButton3.addActionListener(new v(this));
        jButton3.setPreferredSize(new Dimension(iA, iA));
        jPanel.add(jButton3);
        return this.f6689c;
    }

    public void a(boolean z2) {
        this.f6687a.a(z2);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.f6687a.setEnabled(z2);
    }

    @Override // bD.InterfaceC0955a
    public void a() {
        JRootPane rootPane = getRootPane();
        if (rootPane.getGlassPane() instanceof dO) {
            this.f6692f = (dO) rootPane.getGlassPane();
        } else {
            this.f6692f.b(true);
            rootPane.setGlassPane(this.f6692f);
            rootPane.getGlassPane().setVisible(true);
        }
        this.f6692f.a("Preparing Download....");
        this.f6692f.setVisible(true);
    }

    @Override // bD.InterfaceC0955a
    public void d() {
        this.f6692f.setVisible(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2, int i3) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        jPopupMenu.add(this.f6690d.a("Refresh")).addActionListener(new w(this));
        if (this.f6687a.a().size() > 0) {
            jPopupMenu.add(this.f6690d.a("Delete Selected Files")).addActionListener(new x(this));
            jPopupMenu.add(this.f6690d.a("Download Selected Files")).addActionListener(new y(this));
        }
        jPopupMenu.show(this.f6687a.b(), i2, i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        List listA = this.f6687a.a();
        if (listA.size() > 0) {
            setEnabled(false);
            new A(this, listA).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(List list) {
        this.f6696i = false;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            this.f6693g.a((RemoteFileDescriptor) it.next());
        }
        ArrayList arrayList = new ArrayList();
        this.f6687a.d();
        Iterator it2 = list.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            RemoteFileDescriptor remoteFileDescriptor = (RemoteFileDescriptor) it2.next();
            try {
                this.f6686j.readRemoteFile(this.f6686j.getDownloadDirectory(), remoteFileDescriptor);
            } catch (RemoteAccessException e2) {
                String str = "Error downloading " + remoteFileDescriptor.getName() + "\n" + e2.getMessage();
                if (!this.f6696i && !arrayList.contains(str)) {
                    bH.C.a(str, e2, this);
                    arrayList.add(str);
                }
                if (e2.isTerminalToBatch()) {
                    d();
                    break;
                }
            }
        }
        arrayList.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        a();
        b().a(this.f6690d.a("Retrieving File List") + "...");
        List listA = this.f6687a.a();
        String strA = this.f6690d.a("Are you sure you want to delete the following files?");
        int i2 = 0;
        while (true) {
            if (i2 < listA.size()) {
                if (i2 >= 4 && listA.size() != 5) {
                    strA = strA + "\n - (" + (listA.size() - 4) + " More...)";
                    break;
                } else {
                    strA = strA + "\n - " + ((RemoteFileDescriptor) listA.get(i2)).getName();
                    i2++;
                }
            } else {
                break;
            }
        }
        if (listA.isEmpty() || !bV.a(strA, (Component) this, true)) {
            d();
        } else {
            setEnabled(false);
            new z(this, listA).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(List list) {
        double size = list.size();
        int i2 = 0;
        try {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                try {
                    RemoteFileDescriptor remoteFileDescriptor = (RemoteFileDescriptor) it.next();
                    b().a(this.f6690d.a("Deleting file") + ": " + remoteFileDescriptor.getName());
                    int i3 = i2;
                    i2++;
                    b().a(i3 / size);
                    this.f6686j.deleteFile(remoteFileDescriptor);
                    ((F) this.f6687a.f6622a.getModel()).a(remoteFileDescriptor);
                } catch (RemoteAccessException e2) {
                    bH.C.a(e2.getMessage(), e2, this);
                    if (e2.isTerminalToBatch()) {
                        break;
                    }
                }
            }
        } finally {
            d();
        }
    }

    public void e() {
        if (this.f6688b == null || !this.f6688b.isAlive()) {
            this.f6688b = new C(this);
            this.f6688b.start();
            bH.C.c("Refresh File List called");
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f6686j.removeFileDownloadProgressListener(this.f6693g);
        this.f6686j.removeRefreshNeededListener(this.f6695h);
        this.f6686j.cancelReadFile();
    }

    @Override // bD.InterfaceC0955a
    public InterfaceC0961g c() {
        return this.f6694k;
    }

    public void a(InterfaceC0961g interfaceC0961g) {
        this.f6694k = interfaceC0961g;
    }

    @Override // bD.InterfaceC0955a
    public dO b() {
        return this.f6692f;
    }
}
