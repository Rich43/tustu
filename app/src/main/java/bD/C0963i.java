package bD;

import bH.aa;
import com.efiAnalytics.remotefileaccess.DirectoryFiles;
import com.efiAnalytics.remotefileaccess.DirectoryInformation;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import com.efiAnalytics.remotefileaccess.RemoteFileAccess;
import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dO;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;

/* renamed from: bD.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bD/i.class */
public class C0963i extends JPanel implements InterfaceC0955a, InterfaceC1565bc {

    /* renamed from: h, reason: collision with root package name */
    private RemoteFileAccess f6668h;

    /* renamed from: b, reason: collision with root package name */
    aa f6670b;

    /* renamed from: c, reason: collision with root package name */
    C0962h f6671c;

    /* renamed from: a, reason: collision with root package name */
    p f6669a = null;

    /* renamed from: d, reason: collision with root package name */
    dO f6672d = new dO();

    /* renamed from: e, reason: collision with root package name */
    C0956b f6673e = new C0956b(this);

    /* renamed from: i, reason: collision with root package name */
    private InterfaceC0961g f6674i = null;

    /* renamed from: f, reason: collision with root package name */
    q f6675f = new q(this);

    /* renamed from: g, reason: collision with root package name */
    List f6676g = null;

    public C0963i(RemoteFileAccess remoteFileAccess, aa aaVar) {
        this.f6668h = null;
        this.f6670b = null;
        this.f6671c = null;
        this.f6668h = remoteFileAccess;
        this.f6670b = aaVar;
        setLayout(new BorderLayout());
        remoteFileAccess.addFileDownloadProgressListener(this.f6673e);
        add("North", f());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("reload24.png")).getScaledInstance(20, 20, 4));
        JLabel jLabel = new JLabel("");
        jLabel.setIcon(imageIcon);
        jLabel.setFocusable(false);
        jLabel.setToolTipText(a("Refresh SD File Information"));
        jLabel.addMouseListener(new C0964j(this));
        jLabel.setPreferredSize(new Dimension(22, 22));
        jLabel.setFocusable(false);
        jPanel.add("East", jLabel);
        this.f6671c = new C0962h(aaVar);
        jPanel.add(BorderLayout.CENTER, this.f6671c);
        add("South", jPanel);
        remoteFileAccess.addRefreshNeededListener(this.f6675f);
        e();
    }

    private JPanel f() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(1));
        JPanel jPanel2 = new JPanel();
        jPanel2.setBorder(BorderFactory.createTitledBorder(a("SD Card File Management")));
        jPanel2.setLayout(new GridLayout(0, 1, 3, 3));
        JButton jButton = new JButton(a("Download SD Files"), new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("download24.png"))));
        jButton.setFocusable(false);
        jButton.setHorizontalAlignment(2);
        jButton.setToolTipText(a("Download All Files"));
        jButton.addActionListener(new C0965k(this));
        jPanel2.add(jButton);
        JButton jButton2 = new JButton(a("Delete SD Files"), new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("delete24.png"))));
        jButton2.setFocusable(false);
        jButton2.setHorizontalAlignment(2);
        jButton2.setToolTipText(a("Delete SD Files"));
        jButton2.addActionListener(new C0966l(this));
        jPanel2.add(jButton2);
        JButton jButton3 = new JButton(a("Delete SD Files"), new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("upgrade24.png"))));
        jButton3.setFocusable(false);
        jButton3.setHorizontalAlignment(2);
        jButton3.setToolTipText(a("Upgrade, SD Browsing"));
        jButton3.addActionListener(new C0967m(this));
        jPanel2.add(jButton3);
        jPanel.add(jPanel2);
        return jPanel;
    }

    private String a(String str) {
        return this.f6670b == null ? str : this.f6670b.a(str);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
    }

    @Override // bD.InterfaceC0955a
    public void a() {
        JRootPane rootPane = getRootPane();
        if (rootPane.getGlassPane() instanceof dO) {
            this.f6672d = (dO) rootPane.getGlassPane();
        } else {
            this.f6672d.b(true);
            rootPane.setGlassPane(this.f6672d);
            rootPane.getGlassPane().setVisible(true);
        }
        this.f6672d.a(a("Preparing Download") + "....");
        this.f6672d.setVisible(true);
    }

    @Override // bD.InterfaceC0955a
    public void d() {
        this.f6672d.setVisible(false);
    }

    @Override // bD.InterfaceC0955a
    public dO b() {
        return this.f6672d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        a();
        new C0969o(this).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() throws IllegalArgumentException {
        String str;
        List listK = null;
        try {
            b().a(a("Retrieving File List") + "...");
            listK = k();
            str = a("Are you sure you want to download the following files?") + "\n" + a("File Count") + ": " + listK.size() + "\n" + a("Total Size") + ": " + this.f6671c.a() + "\n";
            int i2 = 0;
            while (true) {
                if (i2 < listK.size()) {
                    if (i2 >= 4 && listK.size() != 5) {
                        str = str + "\n - (" + (listK.size() - 4) + " More...)";
                        break;
                    } else {
                        str = str + "\n - " + ((RemoteFileDescriptor) listK.get(i2)).getName();
                        i2++;
                    }
                } else {
                    break;
                }
            }
        } catch (RemoteAccessException e2) {
            bV.d(a(e2.getMessage()), this.f6671c);
            d();
        }
        if (listK.isEmpty()) {
            bV.d(a("No Files Found on SD card."), this);
            d();
            return;
        }
        if (!bV.a(str, (Component) this, true)) {
            d();
            return;
        }
        Iterator it = listK.iterator();
        while (it.hasNext()) {
            this.f6673e.a((RemoteFileDescriptor) it.next());
        }
        ArrayList arrayList = new ArrayList();
        Iterator it2 = listK.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            RemoteFileDescriptor remoteFileDescriptor = (RemoteFileDescriptor) it2.next();
            try {
                this.f6668h.readRemoteFile(this.f6668h.getDownloadDirectory(), remoteFileDescriptor);
            } catch (RemoteAccessException e3) {
                String str2 = a("Error downloading file") + ": " + remoteFileDescriptor.getName() + "\n" + e3.getMessage();
                if (!arrayList.contains(str2)) {
                    bH.C.a(str2, e3, this);
                    arrayList.add(str2);
                }
                if (e3.isTerminalToBatch()) {
                    d();
                    break;
                }
            }
        }
        arrayList.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        a();
        b().a(a("Retrieving File List") + "...");
        new C0968n(this).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() throws IllegalArgumentException {
        String strA;
        List<RemoteFileDescriptor> listK = null;
        try {
            listK = k();
            strA = a("Are you sure you want to delete the following files?");
            int i2 = 0;
            while (true) {
                if (i2 >= listK.size()) {
                    break;
                }
                if (i2 >= 4 && listK.size() != 5) {
                    strA = strA + "\n - (" + (listK.size() - 4) + " More...)";
                    break;
                } else {
                    strA = strA + "\n - " + ((RemoteFileDescriptor) listK.get(i2)).getName();
                    i2++;
                }
            }
        } catch (RemoteAccessException e2) {
            d();
        }
        if (listK.isEmpty()) {
            bV.d(a("No Files Found on SD card."), this);
            d();
            return;
        }
        if (!bV.a(strA, (Component) this, true)) {
            d();
            return;
        }
        double size = listK.size();
        int i3 = 0;
        try {
            for (RemoteFileDescriptor remoteFileDescriptor : listK) {
                try {
                    b().a(a("Deleting file") + ": " + remoteFileDescriptor.getName());
                    int i4 = i3;
                    i3++;
                    b().a(i4 / size);
                    this.f6668h.deleteFile(remoteFileDescriptor);
                } catch (RemoteAccessException e3) {
                    bH.C.a(e3.getMessage(), e3, this);
                    if (e3.isTerminalToBatch()) {
                        break;
                    }
                }
            }
        } finally {
            d();
        }
    }

    public static void a(JComponent jComponent) {
        RootPaneContainer rootPaneContainer = (RootPaneContainer) jComponent.getTopLevelAncestor();
        if (rootPaneContainer != null) {
            rootPaneContainer.getGlassPane().setCursor(Cursor.getPredefinedCursor(3));
            rootPaneContainer.getGlassPane().setVisible(true);
        }
    }

    public static void b(JComponent jComponent) {
        RootPaneContainer rootPaneContainer = (RootPaneContainer) jComponent.getTopLevelAncestor();
        if (rootPaneContainer != null) {
            rootPaneContainer.getGlassPane().setCursor(Cursor.getPredefinedCursor(0));
            rootPaneContainer.getGlassPane().setVisible(false);
        }
    }

    public void e() {
        if (this.f6669a == null || !this.f6669a.isAlive()) {
            a((JComponent) this);
            this.f6669a = new p(this);
            this.f6669a.start();
            bH.C.c("Refresh File List called");
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f6668h.removeFileDownloadProgressListener(this.f6673e);
        this.f6668h.addRefreshNeededListener(this.f6675f);
    }

    @Override // bD.InterfaceC0955a
    public InterfaceC0961g c() {
        return this.f6674i;
    }

    private List k() throws IllegalArgumentException {
        DirectoryFiles filesIn = this.f6668h.getFilesIn(null);
        DirectoryInformation directoryInformation = filesIn.getDirectoryInformation();
        this.f6671c.a(directoryInformation.getFileCount());
        this.f6671c.b(directoryInformation.getUsedBytes());
        this.f6671c.a(directoryInformation.getTotalBytes());
        return filesIn.getFiles();
    }
}
