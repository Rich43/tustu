package aP;

import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1798a;
import r.C1806i;
import s.C1818g;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: TunerStudioMS.jar:aP/aV.class */
public class aV extends JPanel implements A.o {

    /* renamed from: a, reason: collision with root package name */
    JLabel f2855a;

    /* renamed from: b, reason: collision with root package name */
    JLabel f2856b;

    /* renamed from: c, reason: collision with root package name */
    JLabel f2857c;

    /* renamed from: d, reason: collision with root package name */
    JLabel f2858d;

    /* renamed from: e, reason: collision with root package name */
    JLabel f2859e;

    /* renamed from: f, reason: collision with root package name */
    JLabel f2860f;

    /* renamed from: g, reason: collision with root package name */
    JList f2861g;

    /* renamed from: h, reason: collision with root package name */
    DefaultListModel f2862h;

    /* renamed from: i, reason: collision with root package name */
    JPanel f2863i;

    /* renamed from: j, reason: collision with root package name */
    com.efiAnalytics.ui.dM f2864j;

    /* renamed from: k, reason: collision with root package name */
    JButton f2865k;

    /* renamed from: l, reason: collision with root package name */
    JButton f2866l;

    /* renamed from: m, reason: collision with root package name */
    JButton f2867m;

    /* renamed from: n, reason: collision with root package name */
    ArrayList f2868n;

    /* renamed from: o, reason: collision with root package name */
    JDialog f2869o;

    /* renamed from: p, reason: collision with root package name */
    boolean f2870p;

    /* renamed from: q, reason: collision with root package name */
    boolean f2871q;

    /* renamed from: s, reason: collision with root package name */
    private boolean f2872s;

    /* renamed from: t, reason: collision with root package name */
    private C0236be f2873t;

    /* renamed from: r, reason: collision with root package name */
    public static int[] f2874r = {21848};

    public aV(Window window) {
        this();
        a(window);
        b();
    }

    public aV() {
        this.f2855a = new JLabel();
        this.f2856b = new JLabel(C1818g.b("Searching for known devices."));
        this.f2857c = new JLabel(" ");
        this.f2858d = new JLabel(" ");
        this.f2859e = new JLabel(" ");
        this.f2860f = new JLabel("Searching on RS232 and USB D2XX, Upgrade for Wireless connectivity.");
        this.f2861g = new JList();
        this.f2862h = new DefaultListModel();
        this.f2863i = null;
        this.f2864j = new com.efiAnalytics.ui.dM();
        this.f2868n = new ArrayList();
        this.f2869o = null;
        this.f2870p = false;
        this.f2871q = false;
        this.f2872s = true;
        this.f2873t = null;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(15, 15));
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Port Search")));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(15, 15));
        this.f2855a.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/search.gif"))));
        this.f2855a.setMinimumSize(new Dimension(40, 32));
        this.f2855a.setPreferredSize(new Dimension(40, 32));
        this.f2855a.setVisible(false);
        jPanel2.add("West", this.f2855a);
        Component jLabel = new JLabel("");
        jLabel.setMinimumSize(new Dimension(16, 32));
        jLabel.setPreferredSize(new Dimension(16, 32));
        jPanel2.add("East", jLabel);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(0, 1));
        jPanel3.add(new JLabel(" "));
        jPanel3.add(this.f2856b);
        jPanel3.add(this.f2857c);
        jPanel3.add(this.f2858d);
        jPanel3.add(new JLabel(" "));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add("North", jPanel3);
        this.f2861g.setVisibleRowCount(5);
        this.f2861g.setMinimumSize(com.efiAnalytics.ui.eJ.a(240, 120));
        this.f2861g.setPreferredSize(com.efiAnalytics.ui.eJ.a(240, 120));
        this.f2861g.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f2861g.setModel(this.f2862h);
        this.f2861g.addListSelectionListener(new aW(this));
        jPanel4.add(BorderLayout.CENTER, this.f2861g);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new GridLayout(0, 1));
        jPanel5.add(this.f2859e);
        jPanel4.add("South", jPanel5);
        jPanel2.add(BorderLayout.CENTER, jPanel4);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        this.f2865k = new JButton(C1818g.b("Internet Find"));
        this.f2865k.setEnabled(false);
        this.f2865k.addActionListener(new aX(this));
        this.f2866l = new JButton(C1818g.b("Have Local"));
        this.f2866l.setEnabled(false);
        this.f2866l.addActionListener(new aY(this));
        this.f2867m = new JButton(C1818g.b(XIncludeHandler.HTTP_ACCEPT));
        this.f2867m.setEnabled(false);
        this.f2867m.addActionListener(new aZ(this));
        JButton jButton = new JButton(C1818g.b("Cancel"));
        jButton.addActionListener(new C0232ba(this));
        this.f2863i = new JPanel();
        this.f2863i.setLayout(new FlowLayout(2));
        this.f2863i.add(jButton);
        this.f2863i.add(this.f2867m);
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BorderLayout(10, 10));
        jPanel6.add("North", this.f2864j);
        jPanel6.add(BorderLayout.CENTER, this.f2863i);
        jPanel.add("South", jPanel6);
        jPanel.add("West", new JLabel(Constants.INDENT));
        add(BorderLayout.CENTER, jPanel);
    }

    public void b() {
        this.f2871q = false;
        A.j jVarA = A.j.a();
        String strD = C1798a.a().d("deviceSearchQueryCommands");
        if (strD != null && !strD.isEmpty()) {
            jVarA.b(strD);
        }
        jVarA.a(this);
        this.f2862h.removeAllElements();
        new C0233bb(this).start();
        for (int i2 : f2874r) {
            B.e.a(i2).a(this);
            B.e.a(i2).a();
        }
        this.f2855a.setVisible(true);
        this.f2855a.validate();
        this.f2855a.repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        A.j.a().a(c());
    }

    public void b(A.x xVar) {
        a(xVar, C1798a.a().d("deviceSearchQueryCommands"));
    }

    public void a(A.x xVar, String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(xVar);
        A.j jVarA = A.j.a();
        if (str != null && !str.isEmpty()) {
            jVarA.b(str);
        }
        jVarA.a(this);
        this.f2862h.removeAllElements();
        new C0234bc(this, jVarA, arrayList).start();
        this.f2855a.setVisible(true);
        this.f2855a.validate();
        this.f2855a.repaint();
    }

    public void a(boolean z2) {
        this.f2863i.setVisible(z2);
        this.f2863i.validate();
    }

    public void a(Window window) {
        if (this.f2869o != null) {
            this.f2869o.dispose();
        }
        this.f2869o = new JDialog(window, C1818g.b("Detect Device"));
        this.f2869o.setLayout(new GridLayout());
        this.f2869o.add(this);
        this.f2869o.setSize(com.efiAnalytics.ui.eJ.a(750), com.efiAnalytics.ui.eJ.a(360));
        com.efiAnalytics.ui.bV.a(window, (Component) this.f2869o);
        this.f2869o.setVisible(true);
    }

    protected List c() {
        ArrayList arrayList = new ArrayList();
        if (C1806i.a().a(";'[PGS0P;'G0[F;")) {
            try {
                A.x xVar = new A.x();
                aB.b bVar = (aB.b) aV.w.c().a(aB.b.f2274c, "DEFAULT_INSTANCE");
                xVar.a(bVar);
                List listT = bVar.t();
                for (String str : C1798a.a().a("deviceSearchBaudRates", "115200;9600;").split(";")) {
                    xVar.a("Baud Rate", str);
                }
                Iterator it = listT.iterator();
                while (it.hasNext()) {
                    xVar.a("Device Serial #", (String) it.next());
                }
                xVar.a("Device Serial #");
                arrayList.add(xVar);
            } catch (Exception e2) {
                Logger.getLogger(aV.class.getName()).log(Level.WARNING, "Unable to load D2XX driver.", (Throwable) e2);
            } catch (UnsatisfiedLinkError e3) {
                Logger.getLogger(aV.class.getName()).log(Level.WARNING, "Failed to load JSSC Driver", (Throwable) e3);
            }
        }
        if (C1806i.a().a("fdsp[pp[ds';'")) {
            try {
                A.x xVar2 = new A.x();
                xVar2.a(aV.w.c().a(aD.a.f2298d, "DEFAULT_INSTANCE"));
                for (String str2 : C1798a.a().a("deviceSearchBaudRates", "115200;9600;").split(";")) {
                    xVar2.a("Baud Rate", str2);
                }
                for (String str3 : new z.i().a()) {
                    xVar2.a("Com Port", str3);
                }
                xVar2.a("Com Port");
                arrayList.add(xVar2);
            } catch (Exception e4) {
                Logger.getLogger(aV.class.getName()).log(Level.WARNING, "Failed to load JSSC Driver", (Throwable) e4);
            } catch (UnsatisfiedLinkError e5) {
                Logger.getLogger(aV.class.getName()).log(Level.WARNING, "Failed to load JSSC Driver", (Throwable) e5);
            }
        }
        if (C1806i.a().a("GD;';LFDS-0DSL;") && aC.b.a()) {
            this.f2873t = new C0236be(this, arrayList);
            if (1 != 0) {
                try {
                    RemoteDevice[] remoteDeviceArrRetrieveDevices = LocalDevice.getLocalDevice().getDiscoveryAgent().retrieveDevices(1);
                    if (remoteDeviceArrRetrieveDevices == null || remoteDeviceArrRetrieveDevices.length <= 0) {
                        this.f2873t.start();
                    } else {
                        A.x xVar3 = new A.x();
                        xVar3.a((aC.a) aV.w.c().a(aC.a.f2289e, "DEFAULT_INSTANCE"));
                        for (RemoteDevice remoteDevice : remoteDeviceArrRetrieveDevices) {
                            if (remoteDevice.isTrustedDevice()) {
                                xVar3.a("Bluetooth Device", remoteDevice.getBluetoothAddress());
                            }
                        }
                        arrayList.add(xVar3);
                    }
                } catch (Exception e6) {
                    bH.C.d("Failed to detect Bluetooth devices.");
                } catch (UnsatisfiedLinkError e7) {
                    Logger.getLogger(aV.class.getName()).log(Level.WARNING, "Failed to load JSSC Driver", (Throwable) e7);
                }
            } else {
                List<RemoteDevice> listA = aC.b.a(false);
                if (listA != null) {
                    try {
                        if (listA.size() > 0) {
                            A.x xVar4 = new A.x();
                            xVar4.a((aC.a) aV.w.c().a(aC.a.f2289e, "DEFAULT_INSTANCE"));
                            for (RemoteDevice remoteDevice2 : listA) {
                                String strB = bH.W.b(remoteDevice2.getFriendlyName(false), " ", "");
                                if (!remoteDevice2.isTrustedDevice() && strB.toLowerCase().startsWith("efianaly")) {
                                    aC.b.a(remoteDevice2, "1234");
                                }
                                if (remoteDevice2.isTrustedDevice()) {
                                    xVar4.a("Bluetooth Device", remoteDevice2.getBluetoothAddress());
                                }
                            }
                            arrayList.add(xVar4);
                        }
                    } catch (Exception e8) {
                        bH.C.d("Failed to detect Bluetooth devices.");
                    } catch (UnsatisfiedLinkError e9) {
                        Logger.getLogger(aV.class.getName()).log(Level.WARNING, "Failed to load JSSC Driver", (Throwable) e9);
                    }
                }
            }
        }
        try {
            if (C1806i.a().a("LKFDS;LK;lkfs;lk")) {
                A.x xVar5 = new A.x();
                xVar5.a(aV.w.c().a(B.l.f168b, "DEFAULT_INSTANCE"));
                for (int i2 = 0; i2 < 2; i2++) {
                    xVar5.a(B.l.f178j, "192.168.1.8" + i2);
                }
                xVar5.a(B.l.f179k, "2000");
                arrayList.add(xVar5);
            }
        } catch (IllegalAccessException e10) {
            Logger.getLogger(aV.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e10);
        } catch (InstantiationException e11) {
            Logger.getLogger(aV.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e11);
        }
        return arrayList;
    }

    public void d() {
        e();
    }

    public void e() {
        A.j jVarA = A.j.a();
        jVarA.b(this);
        jVarA.b();
        for (int i2 : f2874r) {
            B.e.a(i2).b(this);
        }
        if (this.f2869o != null) {
            this.f2869o.dispose();
        }
    }

    public void f() {
    }

    public void g() {
    }

    public void h() {
        G.bS bSVarK = k();
        A.j.a().b();
        if (bSVarK != null && bSVarK.b() != null && !this.f2870p) {
            m();
            d();
        } else if (!this.f2870p || !this.f2872s) {
            com.efiAnalytics.ui.bV.d(C1818g.b("No device has been detected") + "\n" + C1818g.b("closing, but not setting anything."), this);
        } else {
            m();
            d();
        }
    }

    private G.bS k() {
        C0237bf c0237bf = (C0237bf) this.f2861g.getSelectedValue();
        if (c0237bf != null) {
            return c0237bf.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() throws IllegalArgumentException {
        C0237bf c0237bf = (C0237bf) this.f2861g.getSelectedValue();
        if (c0237bf != null) {
            G.bS bSVarA = c0237bf.a();
            if (a(bSVarA)) {
                String strC = bSVarA.c();
                if ((strC == null ? bSVarA.b() : strC).equals("Unknown")) {
                    String str = "Serial Sig " + bSVarA.b() + " Compatible";
                }
                this.f2856b.setText(C1818g.b("Found Firmware") + ": " + bSVarA.c());
                this.f2857c.setText(C1818g.b("Serial Signature") + ": " + bSVarA.b());
                this.f2858d.setText(C1818g.b("On") + CallSiteDescriptor.TOKEN_DELIMITER + c0237bf.d());
                bH.C.c("Device " + bSVarA.c() + ", requiring serial Signature:" + bSVarA.b() + " found at:" + c0237bf.d());
                this.f2870p = false;
            } else {
                this.f2856b.setText(C1818g.b("Firmware not loaded."));
                this.f2857c.setText(C1818g.b("Check help for to learn about loading firmware."));
                bH.C.c("Found a controller with no firmware at:" + c0237bf.d());
                this.f2870p = true;
            }
        } else {
            this.f2856b.setText("");
            this.f2857c.setText("");
            this.f2858d.setText(" ");
        }
        if (this.f2863i.isVisible()) {
            return;
        }
        SwingUtilities.invokeLater(new RunnableC0235bd(this));
    }

    private boolean a(G.bS bSVar) {
        if (bSVar == null) {
            return true;
        }
        byte[] bArrA = bSVar.a();
        return ((bArrA[0] & 224) == 224 && (bArrA[1] & 240) == 0 && bArrA[2] == 62) ? false : true;
    }

    @Override // A.o
    public void a() throws IllegalArgumentException {
        if (this.f2871q) {
            return;
        }
        this.f2866l.setEnabled(true);
        this.f2865k.setEnabled(true);
        this.f2856b.setText(C1818g.b("No Controller found."));
        this.f2857c.setText(C1818g.b("Check your connections and be sure you have COM port drivers installed."));
        this.f2858d.setText(C1818g.b("Consult your serial cable provider for the correct drivers."));
        bH.C.c("Device not found");
        this.f2859e.setText(C1818g.b("If your Controller is not attached, you can configure it manually to work offline."));
    }

    @Override // A.o
    public void b(double d2) throws IllegalArgumentException {
        this.f2864j.a(d2);
        if (d2 == 1.0d) {
            this.f2859e.setText(C1818g.b("Device Search Complete! Controllers found:") + " " + this.f2862h.size());
            this.f2855a.setIcon(null);
        }
    }

    @Override // A.o
    public void a(String str) throws IllegalArgumentException {
        this.f2859e.setText(str);
    }

    public void a(A.o oVar) {
        if (this.f2868n.contains(oVar)) {
            return;
        }
        this.f2868n.add(oVar);
    }

    public void b(A.o oVar) {
        this.f2868n.remove(oVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() {
        if (this.f2861g.getSelectedValue() != null) {
            C0237bf c0237bf = (C0237bf) this.f2861g.getSelectedValue();
            Iterator it = this.f2868n.iterator();
            while (it.hasNext()) {
                ((A.o) it.next()).a(c0237bf.d(), c0237bf.b(), c0237bf.c(), c0237bf.a());
            }
        }
    }

    @Override // A.o
    public boolean a(String str, String str2, List list, G.bS bSVar) throws IllegalArgumentException {
        this.f2871q = true;
        this.f2867m.setEnabled(true);
        C0237bf c0237bf = new C0237bf(this, str2, list, str, bSVar);
        if (this.f2862h.contains(c0237bf)) {
            return true;
        }
        this.f2862h.addElement(c0237bf);
        if (this.f2861g.getSelectedIndex() < 0) {
            this.f2861g.setSelectedIndex(0);
        }
        this.f2859e.setText(C1818g.b("Devices found:") + " " + this.f2862h.size() + " - " + C1818g.b("Searching for other controllers.."));
        return true;
    }

    public void b(boolean z2) {
        this.f2872s = z2;
    }

    public void i() {
        A.j.a().b();
        this.f2855a.setVisible(false);
        if (this.f2861g.getSelectedIndex() >= 0) {
            this.f2861g.setSelectedIndex(this.f2861g.getSelectedIndex());
        }
        this.f2873t = null;
        A.j.a().d();
    }

    @Override // A.o
    public void a(A.x xVar) {
        if (xVar == null || !(xVar.d() instanceof aC.a) || this.f2873t == null) {
            return;
        }
        this.f2873t.start();
    }
}
