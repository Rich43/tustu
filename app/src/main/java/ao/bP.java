package ao;

import ai.C0516f;
import as.C0852g;
import as.InterfaceC0846a;
import at.C0858a;
import au.C0861c;
import ay.C0924a;
import az.C0942c;
import bH.C0995c;
import com.efiAnalytics.ui.C1615cz;
import com.sun.media.jfxmedia.MetadataParser;
import f.C1722d;
import g.C1725c;
import g.C1733k;
import h.C1737b;
import i.C1746f;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import javafx.fxml.FXMLLoader;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import k.C1756d;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;
import org.icepdf.ri.util.BareBonesBrowserLaunch;
import org.slf4j.Marker;
import sun.security.tools.policytool.ToolWindow;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ao/bP.class */
public class bP extends JFrame implements gR, WindowListener {

    /* renamed from: a, reason: collision with root package name */
    aQ f5347a;

    /* renamed from: b, reason: collision with root package name */
    C0659bw f5348b;

    /* renamed from: d, reason: collision with root package name */
    JMenuItem f5350d;

    /* renamed from: e, reason: collision with root package name */
    JMenuItem f5351e;

    /* renamed from: f, reason: collision with root package name */
    JMenu f5352f;

    /* renamed from: g, reason: collision with root package name */
    JMenu f5353g;

    /* renamed from: i, reason: collision with root package name */
    Frame f5355i;

    /* renamed from: m, reason: collision with root package name */
    com.efiAnalytics.ui.eK f5359m;

    /* renamed from: s, reason: collision with root package name */
    boolean f5365s;

    /* renamed from: I, reason: collision with root package name */
    private boolean f5375I;

    /* renamed from: r, reason: collision with root package name */
    static boolean f5364r = false;

    /* renamed from: G, reason: collision with root package name */
    private static final String f5372G = C1737b.f12222b;

    /* renamed from: c, reason: collision with root package name */
    String[] f5349c = aO.a().b();

    /* renamed from: h, reason: collision with root package name */
    JMenu f5354h = null;

    /* renamed from: j, reason: collision with root package name */
    JMenuItem f5356j = null;

    /* renamed from: k, reason: collision with root package name */
    JMenu f5357k = null;

    /* renamed from: l, reason: collision with root package name */
    JCheckBoxMenuItem f5358l = null;

    /* renamed from: n, reason: collision with root package name */
    C0861c f5360n = null;

    /* renamed from: o, reason: collision with root package name */
    C0718eb f5361o = null;

    /* renamed from: p, reason: collision with root package name */
    C0729em f5362p = null;

    /* renamed from: q, reason: collision with root package name */
    long f5363q = 604800000;

    /* renamed from: A, reason: collision with root package name */
    private final String f5366A = "Log Viewer";

    /* renamed from: B, reason: collision with root package name */
    private final String f5367B = "Ignition Log Viewer";

    /* renamed from: C, reason: collision with root package name */
    private final String f5368C = "Scatter Plots";

    /* renamed from: D, reason: collision with root package name */
    private final String f5369D = "Histogram / Table Generator";

    /* renamed from: E, reason: collision with root package name */
    private final String f5370E = "Reference Values";

    /* renamed from: F, reason: collision with root package name */
    private final String f5371F = "Purchase Registration";

    /* renamed from: H, reason: collision with root package name */
    private final String f5373H = "Custom Fields";

    /* renamed from: t, reason: collision with root package name */
    File f5374t = null;

    /* renamed from: u, reason: collision with root package name */
    C0754fk f5376u = new C0754fk();

    /* renamed from: v, reason: collision with root package name */
    gI f5377v = new gI();

    /* renamed from: w, reason: collision with root package name */
    C0852g f5378w = null;

    /* renamed from: x, reason: collision with root package name */
    InterfaceC0846a f5379x = null;

    /* renamed from: y, reason: collision with root package name */
    JMenu f5380y = new dO(this, "Calculated Fields");

    /* renamed from: z, reason: collision with root package name */
    int f5381z = 1;

    public bP() throws HeadlessException {
        this.f5347a = null;
        this.f5348b = null;
        this.f5355i = null;
        this.f5359m = null;
        this.f5365s = false;
        this.f5375I = false;
        bH.Z z2 = new bH.Z();
        z2.a();
        setTitle("");
        this.f5355i = this;
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(h.i.f12281B)));
        com.efiAnalytics.ui.bV.a((Window) this);
        C0645bi.a().a(this);
        com.efiAnalytics.ui.bV.a(new bQ(this));
        I();
        if (G()) {
            K();
            az.o.a(true);
            h.i.c("lastUpdateCheckDate", "" + ((System.currentTimeMillis() - this.f5363q) - 1000));
        }
        bI.h.a().a(new az.t());
        dQ dQVar = new dQ(this);
        com.efiAnalytics.ui.cS.a(dQVar);
        if (0 == 0 && com.efiAnalytics.ui.cS.a(dQVar).a(dQVar.f()) && B()) {
            h.i.f12256c = bH.W.b(h.i.f12256c, f5372G, "");
            this.f5365s = true;
        } else if (0 == 0 && com.efiAnalytics.ui.cS.a(dQVar).a(dQVar.g(), 1)) {
            String str = hD.f6004b;
            Properties properties = new Properties();
            properties.setProperty(hD.f6003a, dQVar.d());
            if (JOptionPane.showConfirmDialog(this, hD.a(properties, str), "Upgrade Registration", 0) == 0) {
                com.efiAnalytics.ui.aN.a("https://www.efianalytics.com/register/upgradeMlvRegistrationKey.jsp?email=" + dQVar.d());
            }
        }
        setTitle("");
        if (1 != 0) {
            bH.C.c("Check 1: " + z2.d());
            z2.e();
            z2.a();
        }
        C1737b.a().a(h.i.f12255b, h.i.f12256c);
        if (1 != 0) {
            bH.C.c("Set Features: " + z2.d());
            z2.e();
            z2.a();
        }
        this.f5348b = new C0659bw(C0804hg.a());
        if (1 != 0) {
            bH.C.c("Create Lower Toolbar: " + z2.d());
            z2.e();
            z2.a();
        }
        add(this.f5348b, "South");
        C0645bi.a().a(this.f5348b);
        if (1 != 0) {
            bH.C.c("Add Lower Toolbar: " + z2.d());
            z2.e();
            z2.a();
        }
        new C0750fg().a();
        this.f5347a = new aQ();
        if (1 != 0) {
            bH.C.c("Create GraphPanel: " + z2.d());
            z2.e();
            z2.a();
        }
        if (this.f5365s) {
            this.f5347a.a(dQVar.b(), dQVar.c(), dQVar.d());
        } else {
            this.f5347a.a(null, null, null);
        }
        if (1 != 0) {
            bH.C.c("Check 2: " + z2.d());
            z2.e();
            z2.a();
        }
        bH.C.c("init components: " + z2.d());
        z2.e();
        z2.a();
        setJMenuBar(L());
        if (1 != 0) {
            bH.C.c("Build Menu: " + z2.d());
            z2.e();
            z2.a();
        }
        this.f5359m = new com.efiAnalytics.ui.eK();
        this.f5359m.add("Log Viewer", this.f5347a);
        new RunnableC0666cc(this).run();
        if (C1737b.a().a("showRegisterTab")) {
            this.f5359m.add("Purchase Registration", new C0756fm());
            this.f5359m.g("Purchase Registration");
        }
        add(this.f5359m, BorderLayout.CENTER);
        this.f5347a.n().a(h.i.a("playbackSpeed", 1.0d), false);
        this.f5347a.n().a(this);
        if (1 != 0) {
            bH.C.c("Build tabs: " + z2.d());
            z2.e();
            z2.a();
        }
        addWindowListener(this);
        int iB = h.i.b(LanguageTag.PRIVATEUSE, 100);
        int iB2 = h.i.b(PdfOps.y_TOKEN, 50);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(com.efiAnalytics.ui.bV.b(new Rectangle(iB, iB2, h.i.b(MetadataParser.WIDTH_TAG_NAME, screenSize.width - 200), h.i.b(MetadataParser.HEIGHT_TAG_NAME, screenSize.height - 200))));
        if (h.i.a(h.i.f12317al, false)) {
            setExtendedState(6);
        }
        doLayout();
        enableEvents(1L);
        enableEvents(262144L);
        bH.C.c("Layout: " + z2.d());
        z2.e();
        z2.a();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(bK.a());
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new dM(this));
        C1733k.f12218a = new JFileChooser();
        C1733k.f12218a.setFileView(new C1725c());
        if (1 != 0) {
            bH.C.c("init File Chooser: " + z2.d());
            z2.e();
            z2.a();
        }
        setVisible(true);
        if (1 != 0) {
            bH.C.c("Set Visible: " + z2.d());
            z2.e();
            z2.a();
        }
        C1756d.a();
        if (h.i.a(h.i.f12278y, false) && h.h.a(".")) {
            h.i.c("lastUpdateCheckDate", "0");
            C0636b.a().a("Updating Application Files");
            this.f5375I = true;
            new dX(this, true, this).start();
        } else if (h.i.a("automaticUpdates", true) || !h.i.a("valid", false)) {
            new dX(this, false, this).start();
        }
        C0924a.c().a(this.f5377v);
        C0924a.c().g();
        setTransferHandler(new dL(this));
    }

    private void z() throws V.a {
        h.i.d("registrationKeyV2", "");
        h.i.d("userEmail", "");
        h.i.d("firstName", "");
        h.i.d("lastName", "");
        h.i.h();
        h.i.g();
    }

    public int a() {
        h.i.b("loopCount", 0);
        bH.T t2 = new bH.T();
        t2.a();
        t2.a();
        int iA = t2.a();
        h.i.c("loopCount", "" + iA);
        return iA;
    }

    private void e(String str) {
        h.i.c("disabledKeys", h.i.e("disabledKeys", "") + str + ";");
        h.i.g();
    }

    private ArrayList A() {
        ArrayList arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(h.i.e("disabledKeys", ""), ";");
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(stringTokenizer.nextToken());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean f(String str) {
        return A().contains(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean B() {
        C0677cn c0677cn = new C0677cn(this);
        C1722d c1722dG = az.o.a(new bI(), c0677cn).g();
        if (c1722dG == null) {
            h.i.f12256c += f5372G;
            this.f5365s = false;
        } else if (c1722dG.a() == 0) {
            this.f5365s = true;
        } else if (c1722dG.a() == 4) {
            com.efiAnalytics.ui.bV.d(C0942c.f6475h + "\n" + C0942c.f6473f, this);
            this.f5365s = false;
        } else if (c1722dG.a() == 1) {
            bH.C.c("renewal over due");
            this.f5365s = true;
        } else if (c1722dG.a() == 2) {
            com.efiAnalytics.ui.bV.d(C0942c.f6475h + "\n" + c1722dG.b(), this);
            this.f5365s = false;
        } else if (c1722dG.a() == 7) {
            com.efiAnalytics.ui.bV.d(C0942c.f6477j + "\n" + c1722dG.b(), this);
            this.f5365s = false;
        } else if (c1722dG.a() == 5) {
            com.efiAnalytics.ui.bV.d(C0942c.c(c0677cn) + "\n" + c1722dG.b(), this);
            this.f5365s = false;
        } else if (c1722dG.a() == 6) {
            bH.W.b(bH.W.b(h.i.f12256c, C1737b.f12222b, ""), "(Beta)", "");
            com.efiAnalytics.ui.bV.d("The registration info provided is no longer valid.\n\nPlease contact EFI Analytics if you are the rightful owner of the registration.", C0645bi.a().b());
            com.efiAnalytics.ui.aN.a(h.i.f12267n);
            try {
                z();
            } catch (V.a e2) {
                Logger.getLogger(bP.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            this.f5365s = false;
        }
        return this.f5365s;
    }

    public boolean b() {
        Date date = new Date();
        date.setTime(h.i.a("lastUpdateCheckDate", (new Date().getTime() - this.f5363q) - 1));
        Date date2 = new Date();
        date2.setTime(System.currentTimeMillis() - this.f5363q);
        boolean z2 = false;
        if (date.before(date2)) {
            System.out.println("processor score: " + a());
            bW.a aVar = new bW.a();
            try {
                bW.d dVarA = aVar.a(h.i.e("uid", ""), h.i.f12255b, h.i.e("version", "" + h.i.f12254a), h.i.e("installDate", ""), h.i.e("loopCount", ""), h.i.e("registrationKeyV2", ""), bH.W.b(h.i.f12256c, f5372G, "").trim(), h.i.e("lastFileFormat", ""));
                if (dVarA == null) {
                    return false;
                }
                if (dVarA.a() == 0) {
                    f5364r = false;
                    F();
                    h.i.c("lastUpdateCheckDate", "" + new Date().getTime());
                    h.i.c(h.i.f12278y, "false");
                    h.i.g();
                } else if (dVarA.a() == 2) {
                    F();
                    if (!(this.f5375I || com.efiAnalytics.ui.bV.a(dVarA.b(), "Update Available", this, new String[]{"Complete Update", "Update Later"}))) {
                        return true;
                    }
                    if (!h.h.a(".") && com.efiAnalytics.ui.bV.d()) {
                        if (!new File("Elevate.exe").exists()) {
                            if (!com.efiAnalytics.ui.bV.a(h.i.f12255b + " does not currently have write access to the installation folder.\n\nUsing Windows 7 and Vista Operating Systems Auto Update requires \nAdministrator rights. Please start " + h.i.f12255b + "\nUsing Run As Administrator to complete Auto Update. \nWith Windows 7 the \"Run As Administrator\" Option can be found by \nPressing the Shift Key and right clicking on the icon you used to start " + h.i.f12255b + "\n\nAlternatively you can download the latest installer from:\n" + h.i.f12266m + "\n\nWould you like to go to the download site now?", (Component) this, true)) {
                                return true;
                            }
                            com.efiAnalytics.ui.aN.a(h.i.f12266m);
                            return true;
                        }
                        if (1 == 0) {
                            return false;
                        }
                        h.i.c(h.i.f12278y, "true");
                        h.i.g();
                        try {
                            Runtime.getRuntime().exec("Elevate.exe " + h.i.f12264k);
                            System.exit(0);
                            return true;
                        } catch (IOException e2) {
                            Logger.getLogger(C0636b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                            return true;
                        }
                    }
                    try {
                        C0636b.a().a("Initializing downloads........");
                        aVar.a(new C0688cy(this));
                        aVar.a(dVarA);
                        h.i.c(h.i.f12278y, "false");
                        h.i.g();
                        z2 = true;
                    } catch (IOException e3) {
                        com.efiAnalytics.ui.bV.d("Auto update is unable to upgrade " + h.i.f12255b + "\nThis is usually due to a lost connection to the internet or insufficient access \nto write to the installation directory.\n\nInternal Error:\n" + e3.getMessage(), this);
                        e3.printStackTrace();
                    }
                } else if (dVarA.a() == 1) {
                    C1733k.a(dVarA.b(), this);
                } else if (dVarA.a() == 8) {
                    e(h.i.e("registrationKeyV2", ""));
                    h.i.c("immutableInterpolation", "true");
                } else if (dVarA.a() == 4) {
                    e(h.i.e("registrationKeyV2", ""));
                    if (dVarA.b() != null && !dVarA.b().equals("")) {
                        C1733k.a(dVarA.b(), this);
                    }
                    h.i.c("quadraticInterpolation", "true");
                    h.i.d("valid");
                    h.i.g();
                    try {
                        z();
                    } catch (V.a e4) {
                        Logger.getLogger(bP.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    }
                    System.exit(0);
                }
                h.i.c("lastUpdateCheckDate", "" + new Date().getTime());
                h.i.g();
            } catch (IOException e5) {
                System.out.println("Unable to read from update server, connection to server unavailable");
                return false;
            }
        } else {
            System.out.println("No check, last update check=" + date.toString());
        }
        f5364r = false;
        if (!z2) {
            return false;
        }
        try {
            String str = h.i.f12265l;
            String str2 = h.i.f12264k;
            String strL = "";
            if (this.f5347a.l() != null && !this.f5347a.l().equals("")) {
                strL = this.f5347a.l();
            }
            Runtime.getRuntime().exec("java Staging \"" + (C1733k.a() ? str2 + " \\\"" + strL + "\\\"" : "java -jar " + str + " " + strL) + PdfOps.DOUBLE_QUOTE__TOKEN);
            l();
            return false;
        } catch (Exception e6) {
            e6.printStackTrace();
            return false;
        }
    }

    public void c() {
        EventQueue.invokeLater(new cJ(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void C() {
        if (!isVisible()) {
            super.setVisible(true);
        }
        int extendedState = super.getExtendedState() & (-2);
        if (!com.efiAnalytics.ui.bV.d()) {
            super.setVisible(false);
            super.setVisible(true);
            setExtendedState(extendedState);
        } else {
            super.setExtendedState(extendedState);
            super.setAlwaysOnTop(true);
            super.toFront();
            super.requestFocus();
            super.setAlwaysOnTop(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void D() {
        if (com.efiAnalytics.ui.bV.a(h.i.f12255b + " " + bH.W.b(h.i.f12256c, C1737b.f12222b, "") + " needs to restart for changes to take effect.\n\nWould you like to restart Now?", (Component) C0645bi.a().b(), true)) {
            C0636b.a().b((Frame) C0645bi.a().b());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E() {
        if (com.efiAnalytics.ui.bV.a("All " + h.i.f12255b + " " + bH.W.b(h.i.f12256c, C1737b.f12222b, "") + " features will be enabled after restarting.\n\nWould you like to restart Now?", (Component) C0645bi.a().b(), true)) {
            C0636b.a().b((Frame) C0645bi.a().b());
        }
    }

    private void F() {
        if (com.efiAnalytics.ui.cS.a().a(h.i.e("registrationKeyV2", ""))) {
            h.i.c("valid", "true");
        }
    }

    private boolean G() {
        if (h.i.f12254a.equals(h.i.e("version", ""))) {
            return H();
        }
        h.i.c("version", h.i.f12254a);
        return true;
    }

    private boolean H() {
        for (String str : new File(".").list()) {
            if (str.toLowerCase().endsWith(".zip")) {
                return true;
            }
        }
        return false;
    }

    private void I() {
        try {
            if (!g(h.i.e("uid", ""))) {
                Date date = new Date();
                h.i.d("uid", J());
                h.i.d("installDate", "" + date.getTime());
                h.i.h();
            }
        } catch (Exception e2) {
            System.out.println("Failed to set uid");
            e2.printStackTrace();
        }
    }

    private boolean g(String str) {
        try {
            if (Long.parseLong(str) == 0) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            Object obj = null;
            int i2 = 0;
            for (char c2 : str.toCharArray()) {
                Character chValueOf = Character.valueOf(c2);
                if (!arrayList.contains(chValueOf)) {
                    arrayList.add(chValueOf);
                }
                if (obj != null && chValueOf.equals(obj)) {
                    i2++;
                }
                obj = chValueOf;
            }
            return !str.contains("234") && !str.contains("678") && arrayList.size() >= 4 && i2 < 4;
        } catch (Exception e2) {
            return false;
        }
    }

    private String J() {
        String string = null;
        try {
            int[] iArrB = C0995c.b(NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress());
            StringBuilder sb = new StringBuilder();
            for (int length = iArrB.length - 1; length >= 0; length--) {
                sb.append(String.format("%03d", Integer.valueOf(iArrB[length])));
            }
            string = sb.toString();
        } catch (Exception e2) {
        }
        if (string == null || string.length() < 10) {
            string = "964" + ((long) (3.602879701896397E16d * Math.random()));
        }
        return string;
    }

    private void K() {
        h.i.c("automaticUpdates", null);
        if (h.i.a("fieldMapping", "Auto").equals("FieldMaps/BigStuff.properties")) {
            h.i.c("fieldMapping", "Auto");
        }
        h.i.c("automaticUpdates", null);
        if (!h.i.e("APPEND_FIELD_HP", "").trim().startsWith("[Field.TP]<")) {
            h.i.c("APPEND_FIELD_HP", "");
        }
        if (h.i.e("userParameter_Smoothing factor 1-10, higher makes smoother", "").equals("")) {
            h.i.c("userParameter_Smoothing factor 1-10, higher makes smoother", "4");
        }
        if (h.i.e("FIELD_MIN_MAX_TQ", "").equals("0;800")) {
            h.i.c("FIELD_MIN_MAX_TQ", "");
        }
        if (h.i.e("FIELD_MIN_MAX_HP", "").equals("0;800")) {
            h.i.c("FIELD_MIN_MAX_HP", "");
        }
        if (h.i.e()) {
            String strE = h.i.e("firstName", "");
            String strE2 = h.i.e("lastName", "");
            String strE3 = h.i.e("registrationKeyV2", "");
            String strE4 = h.i.e("userEmail", "");
            String strE5 = h.i.e("uid", "");
            String strE6 = h.i.e("installDate", "");
            h.i.d("firstName", strE);
            h.i.d("lastName", strE2);
            h.i.d("registrationKeyV2", strE3);
            h.i.d("userEmail", strE4);
            h.i.d("uid", strE5);
            h.i.d("installDate", strE6);
            h.i.d("firstName");
            h.i.d("lastName");
            h.i.d("registrationKeyV2");
            h.i.d("userEmail");
            h.i.d("uid");
            h.i.d("installDate");
            try {
                h.i.h();
                h.i.g();
            } catch (V.a e2) {
                C1733k.a(e2.getMessage(), this.f5355i);
            }
        }
        String[] list = new File(".").list();
        for (int i2 = 0; i2 < list.length; i2++) {
            if (list[i2].toLowerCase().endsWith(".zip")) {
                System.out.println("Finishing installation, expanding: " + list[i2]);
                new File(list[i2]);
                try {
                    if (bH.ad.a(list[i2], bH.W.b(list[i2], ".zip", ""), (String) null).equals(bH.ad.f7040a)) {
                        new File(list[i2]).delete();
                    }
                } catch (ZipException e3) {
                    e3.printStackTrace();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    private JMenuBar L() throws NumberFormatException, HeadlessException {
        JMenuBar jMenuBar = new JMenuBar();
        a(jMenuBar);
        return jMenuBar;
    }

    private JMenuBar a(JMenuBar jMenuBar) throws NumberFormatException, HeadlessException {
        JMenu dOVar = new dO(this, "File");
        dOVar.setMnemonic('F');
        jMenuBar.add(dOVar);
        JMenuItem jMenuItem = new JMenuItem(ToolWindow.OPEN_POLICY_FILE);
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(79, 128));
        jMenuItem.addActionListener(new cS(this));
        dOVar.add(jMenuItem);
        if (C1737b.a().a("spoji[asfi2309jdi234iofwae2344")) {
            bA.e eVar = new bA.e("Open File on TS-Dash", true);
            eVar.setAccelerator(KeyStroke.getKeyStroke(67, 128));
            eVar.addActionListener(new C0694dd(this));
            eVar.a(new Cdo(this));
            dOVar.add((JMenuItem) eVar);
        }
        if (C1737b.a().a("compareMode")) {
            bA.e eVar2 = new bA.e("Compare to", true);
            eVar2.setAccelerator(KeyStroke.getKeyStroke(67, 128));
            eVar2.addActionListener(new C0715dz(this));
            eVar2.a(new bR(this));
            dOVar.add((JMenuItem) eVar2);
            JMenuItem jMenuItem2 = new JMenuItem("Trail Live File");
            jMenuItem2.setAccelerator(KeyStroke.getKeyStroke(84, 128));
            jMenuItem2.addActionListener(new bT(this));
            dOVar.add(jMenuItem2);
            dOVar.add(new dT(this));
        }
        boolean zA = C1737b.a().a("fileEditing");
        JMenuItem jMenuItem3 = new JMenuItem("Export Loaded Log As");
        jMenuItem3.setAccelerator(KeyStroke.getKeyStroke(69, 2));
        jMenuItem3.addActionListener(new bU(this));
        jMenuItem3.setEnabled(zA);
        dOVar.add(jMenuItem3);
        dOVar.add(new JPopupMenu.Separator());
        dOVar.add(U());
        dOVar.addSeparator();
        boolean zA2 = C1737b.a().a("optionalFields");
        JMenuItem jMenuItemO = O();
        jMenuItemO.setEnabled(zA2);
        dOVar.add(jMenuItemO);
        if (C1737b.a().a("fa-9fdspoijoijnfdz09jfdsa098j")) {
            dOVar.add(P());
        }
        dOVar.addSeparator();
        JMenuItem jMenuItem4 = new JMenuItem("Save Graph as PNG or Jpeg");
        jMenuItem4.addActionListener(new bV(this));
        dOVar.add(jMenuItem4);
        dOVar.addSeparator();
        JMenuItem jMenuItem5 = new JMenuItem(ToolWindow.QUIT);
        jMenuItem5.addActionListener(new bW(this));
        dOVar.add(jMenuItem5);
        jMenuBar.add(N());
        JMenu jMenuR = R();
        jMenuR.setMnemonic('V');
        jMenuBar.add(jMenuR);
        JMenu jMenuS = S();
        jMenuS.setMnemonic('O');
        jMenuBar.add(jMenuS);
        JMenu jMenuQ = Q();
        jMenuQ.setMnemonic('C');
        jMenuBar.add(jMenuQ);
        JMenu jMenuM = M();
        jMenuM.setMnemonic('L');
        jMenuBar.add(jMenuM);
        b(jMenuBar);
        return jMenuBar;
    }

    private JMenu M() {
        dO dOVar = new dO(this, "Log Info");
        bA.e eVar = new bA.e("View Log Header", true);
        eVar.a(new bX(this));
        eVar.addActionListener(new bY(this));
        dOVar.add((JMenuItem) eVar);
        if (C1737b.a().a("timeslipData")) {
            dO dOVar2 = new dO(this, "Drag Timeslip Preferences");
            bA.e eVar2 = new bA.e("View / Edit Timeslip", true);
            eVar2.a(new bZ(this));
            eVar2.addActionListener(new C0664ca(this));
            dOVar.add((JMenuItem) eVar2);
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Show Time Slips");
            jCheckBoxMenuItem.setState(h.i.a(h.i.f12335aB, h.i.f12336aC));
            jCheckBoxMenuItem.setToolTipText("<html>When checked, key Timeslip event data<br>will displayed on graph as yellow vertical bars.");
            jCheckBoxMenuItem.addActionListener(new C0665cb(this));
            dOVar2.add((JMenuItem) jCheckBoxMenuItem);
            JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem("Generate Time Slips");
            jCheckBoxMenuItem2.setState(h.i.a(h.i.f12333az, h.i.f12334aA));
            jCheckBoxMenuItem2.setToolTipText("<html>When checked, Timeslip data will be generated from the<br>launch condition if no time slip data has been entered.");
            jCheckBoxMenuItem2.addActionListener(new C0667cd(this));
            dOVar2.add((JMenuItem) jCheckBoxMenuItem2);
            JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem("Jump to Launch");
            jCheckBoxMenuItem3.setState(h.i.a(h.i.f12331ax, h.i.f12332ay));
            jCheckBoxMenuItem3.setToolTipText("<html>When checked, upon opening a log file the cursor<br>will jump to the launch point of the log.");
            jCheckBoxMenuItem3.addActionListener(new C0668ce(this));
            dOVar2.add((JMenuItem) jCheckBoxMenuItem3);
            dOVar.add((JMenuItem) dOVar2);
        }
        return dOVar;
    }

    private JMenu N() {
        dO dOVar = new dO(this, "Search");
        dOVar.setMnemonic('S');
        if (C1737b.a().a("searchLogFiles")) {
            dOVar.addMenuListener(new C0669cf(this));
        } else {
            dOVar.add("Enable Search").addActionListener(new C0670cg(this));
        }
        return dOVar;
    }

    private JMenu O() {
        JMenu jMenu = new JMenu("Settings import / export");
        JMenuItem jMenuItem = new JMenuItem("Export Settings to file");
        jMenuItem.addActionListener(new C0671ch(this));
        jMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem("Import Settings file");
        jMenuItem2.addActionListener(new C0672ci(this));
        jMenu.add(jMenuItem2);
        return jMenu;
    }

    private JMenu P() {
        if (!C1737b.a().a("fa-9fdspoijoijnfdz09jfdsa098j")) {
            return null;
        }
        JMenu jMenu = new JMenu("Settings Profiles");
        at.c.a().a(jMenu);
        JMenuItem jMenuItem = new JMenuItem("New Setting Profile");
        jMenuItem.addActionListener(new C0673cj(this));
        jMenu.add(jMenuItem);
        String strD = at.c.a().d();
        if (!strD.equals("")) {
            JMenuItem jMenuItem2 = new JMenuItem("Update Setting Profile: " + strD);
            jMenuItem2.addActionListener(new C0674ck(this));
            jMenu.add(jMenuItem2);
        }
        String[] strArrA = bH.R.a(C0858a.a());
        JMenu jMenu2 = new JMenu("Delete Profile");
        jMenu.add((JMenuItem) jMenu2);
        C0675cl c0675cl = new C0675cl(this, jMenu2);
        for (String str : strArrA) {
            JMenuItem jMenuItem3 = new JMenuItem(str);
            jMenu2.add(jMenuItem3);
            jMenuItem3.setActionCommand(str);
            jMenuItem3.addActionListener(c0675cl);
        }
        C0676cm c0676cm = new C0676cm(this);
        jMenu.addSeparator();
        ButtonGroup buttonGroup = new ButtonGroup();
        at.c.a().a(buttonGroup);
        for (String str2 : strArrA) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(str2, str2.equals(strD));
            jMenu.add((JMenuItem) jCheckBoxMenuItem);
            jCheckBoxMenuItem.setActionCommand(str2);
            jCheckBoxMenuItem.addActionListener(c0676cm);
            buttonGroup.add(jCheckBoxMenuItem);
        }
        return jMenu;
    }

    private void b(JMenuBar jMenuBar) {
        JMenu jMenu = new JMenu("Help");
        jMenu.setMnemonic('H');
        jMenuBar.add(jMenu);
        JMenuItem jMenuItem = new JMenuItem("Hot Keys");
        jMenuItem.addActionListener(new C0678co(this));
        jMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem("Help Topics");
        jMenuItem2.addActionListener(new C0679cp(this));
        jMenu.add(jMenuItem2);
        if (C1737b.a().a("tuningPanelVisible")) {
            JMenuItem jMenuItem3 = new JMenuItem("VE Analyze");
            jMenuItem3.addActionListener(new C0680cq(this));
            jMenu.add(jMenuItem3);
        }
        JMenuItem jMenuItem4 = new JMenuItem("Online Help");
        jMenuItem4.addActionListener(new C0681cr(this));
        jMenu.add(jMenuItem4);
        jMenu.addSeparator();
        if (!this.f5365s) {
            h.i.c("automaticUpdates", "true");
        }
        JMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Automatic Update Check", h.i.a("automaticUpdates", true));
        jCheckBoxMenuItem.setEnabled(h.i.a("valid", false));
        jCheckBoxMenuItem.addItemListener(new C0682cs(this));
        if (!this.f5365s) {
            jCheckBoxMenuItem.setEnabled(false);
        }
        jMenu.add(jCheckBoxMenuItem);
        JMenuItem jMenuItem5 = new JMenuItem("Check for Updates Now");
        jMenuItem5.addActionListener(new C0683ct(this));
        jMenu.add(jMenuItem5);
        jMenu.addSeparator();
        if (!C1737b.a().a("hideRegisterMenu") && !this.f5365s) {
            JMenuItem jMenuItem6 = new JMenuItem("Purchase Registration");
            jMenuItem6.addActionListener(new C0684cu(this));
            jMenu.add(jMenuItem6);
            JMenuItem jMenuItem7 = new JMenuItem("Enter Registration");
            jMenuItem7.addActionListener(new C0685cv(this));
            jMenu.add(jMenuItem7);
            jMenu.addSeparator();
        }
        if (bH.I.a()) {
            JMenu jMenu2 = new JMenu("Video driver work arounds");
            ButtonGroup buttonGroup = new ButtonGroup();
            JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem("Force OpenGL Active");
            jCheckBoxMenuItem2.addActionListener(new C0686cw(this));
            boolean zA = h.i.a("forceOpenGL", false);
            jCheckBoxMenuItem2.setState(zA);
            jCheckBoxMenuItem2.setToolTipText("When on will force the use of OpenGL for rendering over the default Direct X normally used on Windows.");
            jMenu2.add((JMenuItem) jCheckBoxMenuItem2);
            buttonGroup.add(jCheckBoxMenuItem2);
            JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem("Disable Direct 3D");
            jCheckBoxMenuItem3.addActionListener(new C0687cx(this));
            boolean zA2 = h.i.a("disableD3d", true);
            jCheckBoxMenuItem3.setState(zA2);
            jMenu2.add((JMenuItem) jCheckBoxMenuItem3);
            buttonGroup.add(jCheckBoxMenuItem3);
            JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem("Java Default (D3D On)");
            jCheckBoxMenuItem4.addActionListener(new C0689cz(this));
            jCheckBoxMenuItem4.setState((zA2 || zA) ? false : true);
            jMenu2.add((JMenuItem) jCheckBoxMenuItem4);
            buttonGroup.add(jCheckBoxMenuItem4);
            jMenu.add((JMenuItem) jMenu2);
        }
        if (!C1737b.a().a("hideTunerStudioPromo") && h.i.f12255b.equals("MegaLogViewer")) {
            JMenuItem jMenuItem8 = new JMenuItem("Learn About TunerStudio");
            jMenuItem8.addActionListener(new cA(this));
            jMenu.add(jMenuItem8);
            jMenu.addSeparator();
        }
        JMenuItem jMenuItem9 = new JMenuItem("Create Support Debug Package");
        jMenuItem9.addActionListener(new cB(this));
        jMenu.add(jMenuItem9);
        JMenuItem jMenuItem10 = new JMenuItem("About");
        jMenuItem10.setAccelerator(KeyStroke.getKeyStroke(65, 128));
        jMenuItem10.addActionListener(new cC(this));
        jMenu.add(jMenuItem10);
    }

    public void d() {
        new cD(this).start();
    }

    public void e() {
        String strA = C1733k.a(this, "Import Settings", new String[]{"settings"}, "*.settings");
        if (strA == null || strA.equals("")) {
            return;
        }
        File file = new File(strA);
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(strA);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (Exception e2) {
            C1733k.a("Unable to open settings file " + strA + "\nSee log for more detail.", this);
            e2.printStackTrace();
        }
        gJ gJVar = new gJ();
        String name = file.getName();
        if (name.contains(".")) {
            name = name.substring(0, name.lastIndexOf("."));
        }
        gJVar.a(name);
        gJVar.a(this);
        if (gJVar.c()) {
            return;
        }
        if (gJVar.b()) {
            at.c.a().a(gJVar.a(), file);
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (a(properties, "ROOT_FIELD_")) {
            C0656bt c0656bt = new C0656bt("ROOT_FIELD_", "Normalized Field Name Mapping");
            c0656bt.a("Import Field Name Mappings in this settings file.");
            arrayList.add(c0656bt);
        }
        if (a(properties, "USER_FIELD_")) {
            C0656bt c0656bt2 = new C0656bt("USER_FIELD_", "User Calculated Math Fields");
            c0656bt2.a("Import custom created Calculated Fields in this settings file.");
            arrayList.add(c0656bt2);
        }
        if (a(properties, "APPEND_FIELD_")) {
            C0656bt c0656bt3 = new C0656bt("APPEND_FIELD_", "Active Optional and Calculated Fields");
            c0656bt3.a("Imports which Optional and Calculated Fields are active.");
            arrayList.add(c0656bt3);
        }
        if (a(properties, "FIELD_GROUP_NAME_")) {
            C0656bt c0656bt4 = new C0656bt("FIELD_GROUP_NAME_", "Quick View Tabs");
            c0656bt4.a("All Quick View tabs on this PC");
            c0656bt4.b("FIELD_SELECTED_GROUP_");
            arrayList.add(c0656bt4);
        }
        if (a(properties, "FIELD_MIN_MAX_")) {
            C0656bt c0656bt5 = new C0656bt("FIELD_MIN_MAX_", "Field Min/Max Settings");
            c0656bt5.a("Import set Min & Max values and autoscale settings.");
            arrayList.add(c0656bt5);
        }
        if (C1737b.a().a("tableGenerator") && a(properties, "TABLE_GEN_VIEW_")) {
            C0656bt c0656bt6 = new C0656bt("TABLE_GEN_VIEW_", "Histogram views");
            c0656bt6.a("Import Histogram / Table Generator views.");
            arrayList.add(c0656bt6);
        }
        if (C1737b.a().a("scatterPlots") && a(properties, "SCATTER_PLOT_VIEW_")) {
            C0656bt c0656bt7 = new C0656bt("SCATTER_PLOT_VIEW_", "Scatter Plot Views");
            c0656bt7.a("Import Scatter Plot views.");
            arrayList.add(c0656bt7);
        }
        if (C1737b.a().a("fieldSmoothing") && a(properties, "fieldSmoothingFactor_")) {
            C0656bt c0656bt8 = new C0656bt("fieldSmoothingFactor_", "Field Smoothing");
            c0656bt8.a("Export Smoothing factor for each field it has been set on.");
            arrayList.add(c0656bt8);
        }
        if (a(properties, h.i.f12298S)) {
            C0656bt c0656bt9 = new C0656bt(h.i.f12298S, "Viewing preferences");
            c0656bt9.b("numberOfGraphs");
            c0656bt9.b("numberOfOverlays");
            c0656bt9.b("numberOfOverlayGraphs");
            c0656bt9.a("Import number of graphs, traces per graph, Trace Value Position, etc.");
            arrayList.add(c0656bt9);
        }
        if (a(properties, "DATA_FILTER_")) {
            C0656bt c0656bt10 = new C0656bt("DATA_FILTER_", "Data Filters");
            c0656bt10.a("Import data filters defined for Histograms and Scatter Plots.");
            arrayList.add(c0656bt10);
        }
        if (arrayList.isEmpty()) {
            com.efiAnalytics.ui.bV.d("There are no settings in this file to import.", this);
            return;
        }
        C0653bq c0653bq = new C0653bq(arrayList, false);
        c0653bq.a(this);
        List listB = c0653bq.b();
        if (listB.isEmpty()) {
            return;
        }
        if (c0653bq.a()) {
            for (String str : h.i.f12258e.stringPropertyNames()) {
                if (a(str, listB)) {
                    h.i.d(str);
                }
            }
        } else if (JOptionPane.showConfirmDialog(this, "Warning!!!!\nAny formulas or settings of the same name will be overridden\n\nContinue?") != 0) {
            return;
        }
        Iterator<Object> it = properties.keySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (a(str2, listB)) {
                h.i.f12258e.setProperty(str2, properties.getProperty(str2));
            }
        }
        C1733k.a("The Application will now restart for changes to take effect.", C0645bi.a().b());
        C0636b.a().b((Frame) C0645bi.a().b());
    }

    private boolean a(Properties properties, String str) {
        Iterator<String> it = properties.stringPropertyNames().iterator();
        while (it.hasNext()) {
            if (it.next().startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    public void f() {
        ArrayList arrayList = new ArrayList();
        C0656bt c0656bt = new C0656bt("ROOT_FIELD_", "Normalized Field Name Mapping");
        c0656bt.a("Export Field Name Mappings in the currently active Settings Profile.");
        arrayList.add(c0656bt);
        C0656bt c0656bt2 = new C0656bt("USER_FIELD_", "User Calculated Math Fields");
        c0656bt2.a("Your custom created Calculated Fields");
        arrayList.add(c0656bt2);
        C0656bt c0656bt3 = new C0656bt("APPEND_FIELD_", "Active Optional and Calculated Fields");
        c0656bt3.a("Exports which Optional and Calculated Fields are active.");
        arrayList.add(c0656bt3);
        C0656bt c0656bt4 = new C0656bt("FIELD_GROUP_NAME_", "Quick View Tabs");
        c0656bt4.a("All Quick View tabs on this PC");
        c0656bt4.b("FIELD_SELECTED_GROUP_");
        arrayList.add(c0656bt4);
        C0656bt c0656bt5 = new C0656bt("FIELD_MIN_MAX_", "Field Min/Max Settings");
        c0656bt5.a("Export set Min & Max values and autoscale settings.");
        arrayList.add(c0656bt5);
        C0656bt c0656bt6 = new C0656bt(h.i.f12298S, "Viewing preferences");
        c0656bt6.b("numberOfGraphs");
        c0656bt6.b("numberOfOverlays");
        c0656bt6.b("numberOfOverlayGraphs");
        c0656bt6.a("Export number of graphs, traces per graph, Trace Value Position, etc.");
        arrayList.add(c0656bt6);
        if (C1737b.a().a("tableGenerator")) {
            C0656bt c0656bt7 = new C0656bt("TABLE_GEN_VIEW_", "Histogram Views");
            c0656bt7.a("Export Histogram / Table Generator views.");
            arrayList.add(c0656bt7);
        }
        if (C1737b.a().a("scatterPlots")) {
            C0656bt c0656bt8 = new C0656bt("SCATTER_PLOT_VIEW_", "Scatter Plot Views");
            c0656bt8.a("Export Scatter Plot views.");
            arrayList.add(c0656bt8);
        }
        if (C1737b.a().a("fieldSmoothing")) {
            C0656bt c0656bt9 = new C0656bt("fieldSmoothingFactor_", "Field Smoothing");
            c0656bt9.a("Export Smoothing factor for each field it has been set on.");
            arrayList.add(c0656bt9);
        }
        if (C1737b.a().a("scatterPlots")) {
            C0656bt c0656bt10 = new C0656bt("DATA_FILTER_", "Data Filters");
            c0656bt10.a("Export data filters defined for Histograms and Scatter Plots.");
            arrayList.add(c0656bt10);
        }
        C0653bq c0653bq = new C0653bq(arrayList, true);
        c0653bq.a(this);
        List listB = c0653bq.b();
        if (listB.isEmpty()) {
            return;
        }
        this.f5347a.i();
        String strA = C1733k.a(this, "Export Settings", new String[]{"settings"}, "MLV" + h.i.f12256c + "_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".settings");
        if (strA == null || strA.equals("")) {
            return;
        }
        if (!strA.toLowerCase().endsWith("settings")) {
            strA = strA + ".settings";
        }
        Properties properties = new Properties();
        Iterator<Object> it = h.i.f12258e.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (a(str, listB)) {
                properties.setProperty(str, h.i.f12258e.getProperty(str));
            }
        }
        File file = new File(strA);
        if (!file.exists() || com.efiAnalytics.ui.bV.a("The file " + file.getName() + " already exists.\n\nOverwrite?", (Component) this, true)) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                properties.store(fileOutputStream, "MegaLogViewer Settings");
                fileOutputStream.close();
            } catch (Exception e2) {
                C1733k.a("Unable to save settings to " + strA + "\nSee log for more detail.", this);
                e2.printStackTrace();
            }
        }
    }

    private boolean a(String str, List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (str.startsWith((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    public void g() {
        C1733k.a("No Updates Available, " + h.i.f12254a + " is the latest version.", this.f5355i);
    }

    public void a(JCheckBoxMenuItem jCheckBoxMenuItem) {
        h.i.c("automaticUpdates", "" + jCheckBoxMenuItem.getState());
    }

    @Override // ao.gR
    public void c(double d2) {
        String str = d2 + "";
        for (int i2 = 0; i2 < this.f5352f.getItemCount(); i2++) {
            JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) this.f5352f.getItem(i2);
            ItemListener[] itemListeners = jCheckBoxMenuItem.getItemListeners();
            for (ItemListener itemListener : itemListeners) {
                jCheckBoxMenuItem.removeItemListener(itemListener);
            }
            jCheckBoxMenuItem.setState(jCheckBoxMenuItem.getName().equals(str));
            for (ItemListener itemListener2 : itemListeners) {
                jCheckBoxMenuItem.addItemListener(itemListener2);
            }
        }
    }

    public void a(boolean z2) {
        h.i.c("showGraphHalfMark", z2 + "");
        this.f5347a.o();
    }

    public void b(boolean z2) {
        this.f5347a.d(z2);
        h.i.c("showGauges", z2 + "");
    }

    public void c(boolean z2) {
        this.f5347a.n().c(z2);
        this.f5347a.t();
        h.i.c("showDashboard", z2 + "");
    }

    public void d(boolean z2) {
        this.f5347a.n().a(z2);
        h.i.c("hideSelector", z2 + "");
    }

    private JMenu Q() {
        boolean zA = C1737b.a().a("optionalFields");
        String[] strArrE = h.i.e("FORMULA_GROUP_");
        for (int i2 = 0; i2 < strArrE.length; i2++) {
            String strA = C1733k.a(strArrE[i2], "FORMULA_GROUP_", "");
            JMenu jMenuB = b(strA, "APPEND_FIELD_");
            jMenuB.setText(h.i.b(strArrE[i2]));
            jMenuB.setEnabled(C1737b.a().b(strA));
            this.f5380y.add((JMenuItem) jMenuB);
        }
        this.f5380y.addSeparator();
        String[] strArrB = C1733k.b(h.i.g("APPEND_FIELD_"));
        int i3 = 1;
        this.f5354h = new JMenu("Optional Fields");
        this.f5380y.add((JMenuItem) this.f5354h);
        if (!zA) {
            JMenuItem jMenuItem = new JMenuItem("Enable Optional Fields!");
            jMenuItem.addActionListener(new cE(this));
            this.f5354h.add(jMenuItem);
        }
        ArrayList arrayList = new ArrayList();
        for (int i4 = 0; i4 < strArrB.length; i4++) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(C1733k.a(strArrB[i4], "APPEND_FIELD_", ""));
            jCheckBoxMenuItem.setEnabled(zA);
            jCheckBoxMenuItem.setActionCommand(h.i.c(strArrB[i4]));
            jCheckBoxMenuItem.setName(strArrB[i4]);
            arrayList.add(jCheckBoxMenuItem);
            jCheckBoxMenuItem.setState(!h.i.e(strArrB[i4], "").equals(""));
            jCheckBoxMenuItem.addItemListener(new cF(this));
            this.f5354h.add((JMenuItem) jCheckBoxMenuItem);
            if (i4 > 0 && i4 % 25 == 0 && i4 < strArrB.length - 1) {
                i3++;
                this.f5354h = new JMenu("Optional Fields " + i3);
                this.f5380y.add((JMenuItem) this.f5354h);
            }
        }
        new cG(this, arrayList).start();
        this.f5380y.addSeparator();
        boolean zA2 = C1737b.a().a("customFields");
        this.f5353g = new JMenu("Custom Fields");
        if (!zA2) {
            JMenuItem jMenuItem2 = new JMenuItem("Enable Custom Fields!");
            jMenuItem2.addActionListener(new cH(this));
            this.f5353g.add(jMenuItem2);
        }
        this.f5380y.add((JMenuItem) this.f5353g);
        JMenuItem jMenuItem3 = new JMenuItem("Add Custom Field");
        jMenuItem3.setAccelerator(KeyStroke.getKeyStroke(78, 3));
        jMenuItem3.addActionListener(new cI(this));
        jMenuItem3.setEnabled(zA2);
        this.f5353g.add(jMenuItem3);
        this.f5353g.addSeparator();
        String[] strArrA = C1733k.a(h.i.f("USER_FIELD_"));
        for (int i5 = 0; i5 < strArrA.length; i5++) {
            String strA2 = C1733k.a(strArrA[i5], "USER_FIELD_", "");
            a(b(strA2, h.i.e(strArrA[i5], ""), !h.i.e(new StringBuilder().append("APPEND_FIELD_").append(strA2).toString(), "").equals("")));
        }
        this.f5380y.addSeparator();
        JMenuItem jMenuItem4 = new JMenuItem("Data Filter Editor");
        jMenuItem4.setEnabled(zA2);
        this.f5380y.add(jMenuItem4);
        jMenuItem4.addActionListener(new cK(this));
        this.f5380y.addSeparator();
        JMenuItem jMenuItem5 = new JMenuItem("Field Properties Editor");
        jMenuItem5.addActionListener(new cL(this));
        this.f5380y.add(jMenuItem5);
        return this.f5380y;
    }

    public void a(String str, String str2) {
        C0820r c0820r = new C0820r(this, str, str2);
        if (c0820r.a()) {
            String strB = c0820r.b();
            String strC = c0820r.c();
            JMenu jMenuB = b(strB, strC, true);
            if (!a(strB, jMenuB)) {
                a(jMenuB);
            }
            c("APPEND_FIELD_" + strB, strC, true);
            h.i.c("USER_FIELD_" + strB, c0820r.c());
        }
    }

    private JMenu b(String str, String str2, boolean z2) {
        JMenu jMenu = new JMenu(str);
        jMenu.setName(str);
        Map<TextAttribute, ?> attributes = jMenu.getFont().getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH, Boolean.valueOf(!z2));
        jMenu.setFont(new Font(attributes));
        jMenu.add(new JMenuItem("Formula = " + str2));
        jMenu.add(new JSeparator());
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Enabled");
        String str3 = "USER_FIELD_" + str;
        jCheckBoxMenuItem.setActionCommand(str2);
        jCheckBoxMenuItem.setName("APPEND_FIELD_" + str);
        jCheckBoxMenuItem.setState(z2);
        jCheckBoxMenuItem.addItemListener(new cM(this, jMenu));
        jMenu.add((JMenuItem) jCheckBoxMenuItem);
        JMenuItem jMenuItem = new JMenuItem(ToolWindow.EDIT_KEYSTORE);
        jMenuItem.setActionCommand(str2);
        jMenuItem.setName(str);
        jMenuItem.addActionListener(new cN(this));
        jMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem("Delete");
        jMenuItem2.setActionCommand(str);
        jMenuItem2.addActionListener(new cO(this));
        jMenu.add(jMenuItem2);
        return jMenu;
    }

    private void a(JMenu jMenu) {
        if ((this.f5381z == 1 && this.f5353g.getMenuComponentCount() == 25 + 2) || (this.f5381z > 1 && this.f5353g.getMenuComponentCount() == 25)) {
            StringBuilder sbAppend = new StringBuilder().append("Custom Fields ");
            int i2 = this.f5381z + 1;
            this.f5381z = i2;
            this.f5353g = new JMenu(sbAppend.append(i2).toString());
            this.f5380y.add((JMenuItem) this.f5353g);
        }
        this.f5353g.add((JMenuItem) jMenu);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(String str, JMenu jMenu) {
        h.i.d("USER_FIELD_" + str);
        h.i.d("APPEND_FIELD_" + str);
        for (int i2 = 0; i2 < this.f5380y.getItemCount(); i2++) {
            JMenuItem item = this.f5380y.getItem(i2);
            if ((item instanceof JMenu) && item.getText() != null && item.getText().startsWith("Custom Fields")) {
                JMenu jMenu2 = (JMenu) this.f5380y.getItem(i2);
                for (int i3 = 0; i3 < jMenu2.getItemCount(); i3++) {
                    if (jMenu2.getItem(i3) instanceof JMenu) {
                        JMenu jMenu3 = (JMenu) jMenu2.getItem(i3);
                        if (jMenu3.getName() != null && jMenu3.getName().equals(str)) {
                            jMenu2.remove(i3);
                            if (jMenu == null) {
                                return true;
                            }
                            jMenu2.add(jMenu, i3);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private JMenu b(String str, String str2) {
        String[] strArrA = C1733k.a(h.i.a(str + "_OPTIONS", ""), ";");
        String[] strArrA2 = C1733k.a(h.i.a(str + "_SOLUTIONS", ""), ";");
        JMenu jMenu = new JMenu(str);
        C0811i c0811i = new C0811i();
        for (int i2 = 0; i2 < strArrA.length; i2++) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(bH.W.b(strArrA[i2], "<semi>", ";"));
            c0811i.a(jCheckBoxMenuItem);
            if (h.i.c("DEFAULT_" + str2 + str) != null && h.i.c("DEFAULT_" + str2 + str).equals(strArrA2[i2])) {
                jCheckBoxMenuItem.setText(strArrA[i2] + " (Default)");
            }
            jCheckBoxMenuItem.setName(bH.W.b(strArrA2[i2], "<semi>", ";"));
            jCheckBoxMenuItem.setActionCommand(str2 + str);
            if (h.i.a(str2 + str, h.i.c("DEFAULT_" + str2 + str)).equals(strArrA2[i2]) || (c0811i.f6148b == null && i2 == strArrA.length - 1 && strArrA2[i2].indexOf(VectorFormat.DEFAULT_PREFIX) != -1)) {
                jCheckBoxMenuItem.setState(true);
                c0811i.f6148b = jCheckBoxMenuItem;
            } else {
                jCheckBoxMenuItem.setState(false);
            }
            jCheckBoxMenuItem.addItemListener(new cP(this));
            jMenu.add((JMenuItem) jCheckBoxMenuItem);
        }
        if (h.i.c("DEFAULT_" + str2 + str) != null && !h.i.c("DEFAULT_" + str2 + str).equals("")) {
            jMenu.addSeparator();
            JMenuItem jMenuItem = new JMenuItem("Reset to Default");
            jMenuItem.setActionCommand(str2 + str);
            jMenuItem.addActionListener(new cQ(this));
            jMenu.add(jMenuItem);
        }
        return jMenu;
    }

    private JMenu R() throws NumberFormatException {
        dO dOVar = new dO(this, "View");
        JMenu jMenu = new JMenu("Graphing View Layout");
        C0811i c0811i = new C0811i();
        String strA = h.i.a(h.i.f12293N, h.i.f12297R);
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("2 Table View (Default)", h.i.f12296Q.equals(strA));
        jCheckBoxMenuItem.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/layout2Tables.png"))));
        jCheckBoxMenuItem.addItemListener(new cR(this));
        c0811i.a(jCheckBoxMenuItem);
        jCheckBoxMenuItem.setEnabled(C1737b.a().a("optionalTableLayouts"));
        jMenu.add((JMenuItem) jCheckBoxMenuItem);
        JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem("1 Table View", h.i.f12295P.equals(strA));
        jCheckBoxMenuItem2.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/layout1Table.png"))));
        jCheckBoxMenuItem2.addItemListener(new cT(this));
        c0811i.a(jCheckBoxMenuItem2);
        jCheckBoxMenuItem2.setEnabled(C1737b.a().a("optionalTableLayouts"));
        jMenu.add((JMenuItem) jCheckBoxMenuItem2);
        dOVar.add((JMenuItem) jMenu);
        if (C1737b.a().a("selectableLookAndFeel")) {
            JMenu jMenu2 = new JMenu("Look and Feel");
            UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
            C0811i c0811i2 = new C0811i();
            for (UIManager.LookAndFeelInfo lookAndFeelInfo : installedLookAndFeels) {
                String name = lookAndFeelInfo.getName();
                if (lookAndFeelInfo.getClassName().equals(h.i.a("lookAndFeelClass", UIManager.getSystemLookAndFeelClassName()))) {
                    name = name + " (Default)";
                }
                if (!name.equals("Windows Classic") && !name.startsWith("TinyLookAndFeel")) {
                    JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem(name, true);
                    c0811i2.a(jCheckBoxMenuItem3);
                    jCheckBoxMenuItem3.setActionCommand(lookAndFeelInfo.getClassName());
                    jCheckBoxMenuItem3.setSelected(lookAndFeelInfo.getClassName().equals(h.i.e("lookAndFeelClass", UIManager.getSystemLookAndFeelClassName())));
                    jCheckBoxMenuItem3.addActionListener(new cU(this));
                    jMenu2.add((JMenuItem) jCheckBoxMenuItem3);
                }
            }
            dOVar.add((JMenuItem) jMenu2);
        }
        C0811i c0811i3 = new C0811i();
        JMenu jMenu3 = new JMenu("Trace Value Positions");
        String strE = h.i.e(h.i.f12298S, h.i.f12299T);
        JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem("Show Cursor Values At Top", strE.equals(JSplitPane.TOP));
        jCheckBoxMenuItem4.addItemListener(new cV(this));
        c0811i3.a(jCheckBoxMenuItem4);
        jMenu3.add((JMenuItem) jCheckBoxMenuItem4);
        JCheckBoxMenuItem jCheckBoxMenuItem5 = new JCheckBoxMenuItem("Show Cursor Values At Bottom", strE.equals(JSplitPane.BOTTOM));
        jCheckBoxMenuItem5.addItemListener(new cW(this));
        c0811i3.a(jCheckBoxMenuItem5);
        jMenu3.add((JMenuItem) jCheckBoxMenuItem5);
        JCheckBoxMenuItem jCheckBoxMenuItem6 = new JCheckBoxMenuItem("Show Cursor Values by Field Name", strE.equals("withLabels"));
        jCheckBoxMenuItem6.addItemListener(new cX(this));
        c0811i3.a(jCheckBoxMenuItem6);
        jMenu3.add((JMenuItem) jCheckBoxMenuItem6);
        dOVar.add((JMenuItem) jMenu3);
        cY cYVar = new cY(this);
        C0811i c0811i4 = new C0811i();
        int iB = h.i.b("lineTraceSize", h.i.f12310ae);
        JMenu jMenu4 = new JMenu("Trace Line Style");
        JMenu jMenu5 = new JMenu("Line width");
        int i2 = 1;
        while (i2 < 6) {
            String str = i2 + "";
            if (i2 == 1) {
                str = str + " (Default)";
            }
            JCheckBoxMenuItem jCheckBoxMenuItem7 = new JCheckBoxMenuItem(str, iB == i2);
            jCheckBoxMenuItem7.setActionCommand("" + i2);
            jCheckBoxMenuItem7.addItemListener(cYVar);
            c0811i4.a(jCheckBoxMenuItem7);
            jMenu5.add((JMenuItem) jCheckBoxMenuItem7);
            i2++;
        }
        jMenu4.add((JMenuItem) jMenu5);
        JCheckBoxMenuItem jCheckBoxMenuItem8 = new JCheckBoxMenuItem("Antialias Line", h.i.a(h.i.f12311af, h.i.f12312ag));
        jMenu4.add((JMenuItem) jCheckBoxMenuItem8);
        jCheckBoxMenuItem8.addActionListener(new cZ(this));
        boolean zA = h.i.a(h.i.f12313ah, h.i.f12314ai);
        C0625ap.g(zA);
        JCheckBoxMenuItem jCheckBoxMenuItem9 = new JCheckBoxMenuItem("Patterned Graph Lines", zA);
        jMenu4.add((JMenuItem) jCheckBoxMenuItem9);
        jCheckBoxMenuItem9.addActionListener(new C0691da(this));
        dOVar.add((JMenuItem) jMenu4);
        JCheckBoxMenuItem jCheckBoxMenuItem10 = new JCheckBoxMenuItem("Quick Trace Selection");
        jCheckBoxMenuItem10.setState(h.i.a("fieldSelectionStyle", "standardSelection").equals("selectFromDash"));
        jCheckBoxMenuItem10.addItemListener(new C0692db(this));
        jCheckBoxMenuItem10.setEnabled(C1737b.a().a("optionalQuickSelect"));
        dOVar.add((JMenuItem) jCheckBoxMenuItem10);
        bA.e eVar = new bA.e("Select Displayed Dashboard Fields", true);
        eVar.a(new C0693dc(this));
        eVar.addActionListener(new C0695de(this));
        eVar.setEnabled(C1737b.a().a("selectableFields"));
        dOVar.add((JMenuItem) eVar);
        dOVar.addMenuListener(new C0696df(this));
        JMenu jMenu6 = new JMenu("Maximum Number of Graphs");
        dOVar.add((JMenuItem) jMenu6);
        C0811i c0811i5 = new C0811i();
        int i3 = Integer.parseInt(h.i.b("numberOfGraphs", "" + h.i.f12273t));
        int iB2 = h.i.b("numberOfGraphs", i3);
        for (int i4 = 0; i4 < h.i.f12272s; i4++) {
            JCheckBoxMenuItem jCheckBoxMenuItem11 = new JCheckBoxMenuItem(i4 + 1 == i3 ? "Up to " + (i4 + 1) + " Graphs (Default)" : "Up to " + (i4 + 1) + " Graphs");
            c0811i5.a(jCheckBoxMenuItem11);
            jCheckBoxMenuItem11.setActionCommand("" + (i4 + 1));
            if (i4 + 1 == iB2) {
                jCheckBoxMenuItem11.setSelected(true);
            } else {
                jCheckBoxMenuItem11.setSelected(false);
            }
            jCheckBoxMenuItem11.addActionListener(new C0697dg(this));
            jMenu6.add((JMenuItem) jCheckBoxMenuItem11);
        }
        JMenu jMenu7 = new JMenu("Maximum Traces Per Graph");
        dOVar.add((JMenuItem) jMenu7);
        C0811i c0811i6 = new C0811i();
        int i5 = Integer.parseInt(h.i.b("numberOfOverlays", "" + h.i.f12274u));
        int iB3 = h.i.b("numberOfOverlays", i5);
        for (int i6 = 1; i6 < h.i.f12271r; i6++) {
            JCheckBoxMenuItem jCheckBoxMenuItem12 = new JCheckBoxMenuItem(i6 + 1 == i5 ? "Up to " + (i6 + 1) + " Traces (Default)" : "Up to " + (i6 + 1) + " Traces");
            c0811i6.a(jCheckBoxMenuItem12);
            jCheckBoxMenuItem12.setActionCommand("" + (i6 + 1));
            if (i6 + 1 == iB3) {
                jCheckBoxMenuItem12.setSelected(true);
            } else {
                jCheckBoxMenuItem12.setSelected(false);
            }
            jCheckBoxMenuItem12.addActionListener(new C0698dh(this));
            jMenu7.add((JMenuItem) jCheckBoxMenuItem12);
        }
        JCheckBoxMenuItem jCheckBoxMenuItem13 = new JCheckBoxMenuItem("Show Gauges");
        jCheckBoxMenuItem13.setState(h.i.a("showGauges", true));
        jCheckBoxMenuItem13.addItemListener(new C0699di(this));
        dOVar.add((JMenuItem) jCheckBoxMenuItem13);
        JCheckBoxMenuItem jCheckBoxMenuItem14 = new JCheckBoxMenuItem("Show 50% Graph Line");
        jCheckBoxMenuItem14.setState(h.i.a("showGraphHalfMark", false));
        jCheckBoxMenuItem14.addItemListener(new C0700dj(this));
        dOVar.add((JMenuItem) jCheckBoxMenuItem14);
        JCheckBoxMenuItem jCheckBoxMenuItem15 = new JCheckBoxMenuItem("Keep Graph Centered");
        jCheckBoxMenuItem15.setState(h.i.a("holdGraphCentered", h.i.f12277x));
        jCheckBoxMenuItem15.addItemListener(new C0701dk(this));
        dOVar.add((JMenuItem) jCheckBoxMenuItem15);
        if (C1737b.a().a("tabbedQuickViews")) {
            boolean zA2 = h.i.a(h.i.f12315aj, h.i.f12316ak);
            JCheckBoxMenuItem jCheckBoxMenuItem16 = new JCheckBoxMenuItem("Go to last Quick View on open");
            jCheckBoxMenuItem16.setState(zA2);
            jCheckBoxMenuItem16.addItemListener(new C0702dl(this));
            jCheckBoxMenuItem16.setToolTipText("If checked, the last used Quick View tab will be selected on open, otherwise it will open to the Default tab on open");
            dOVar.add((JMenuItem) jCheckBoxMenuItem16);
        }
        dOVar.addSeparator();
        h.i.a("useSwingFileDialog", true);
        JCheckBoxMenuItem jCheckBoxMenuItem17 = new JCheckBoxMenuItem("Show Dashboard");
        jCheckBoxMenuItem17.setState(h.i.a("showDashboard", true));
        jCheckBoxMenuItem17.addItemListener(new C0703dm(this));
        C0804hg.a().c(new C0704dn(this, jCheckBoxMenuItem17));
        dOVar.add((JMenuItem) jCheckBoxMenuItem17);
        JCheckBoxMenuItem jCheckBoxMenuItem18 = new JCheckBoxMenuItem("Auto Hide Field Select");
        jCheckBoxMenuItem18.setState(h.i.a("hideSelector", h.i.f12270q));
        jCheckBoxMenuItem18.addItemListener(new C0705dp(this));
        dOVar.add((JMenuItem) jCheckBoxMenuItem18);
        if (C1737b.a().a("tuningPanelVisible")) {
            JCheckBoxMenuItem jCheckBoxMenuItem19 = new JCheckBoxMenuItem("Show Tuning Console");
            jCheckBoxMenuItem19.setState(h.i.a("showTuningConsole", h.i.f12269p));
            jCheckBoxMenuItem19.addItemListener(new C0706dq(this));
            C0804hg.a().b(new C0707dr(this, jCheckBoxMenuItem19));
            dOVar.add((JMenuItem) jCheckBoxMenuItem19);
        } else {
            h.i.c("showTuningConsole", "false");
        }
        JCheckBoxMenuItem jCheckBoxMenuItem20 = new JCheckBoxMenuItem("Scale to Fit Full Log On load");
        jCheckBoxMenuItem20.setState(h.i.a(h.i.f12300U, h.i.f12301V));
        jCheckBoxMenuItem20.addItemListener(new C0708ds(this));
        dOVar.add((JMenuItem) jCheckBoxMenuItem20);
        return dOVar;
    }

    private JMenu S() throws HeadlessException {
        dO dOVar = new dO(this, "Options");
        if (!C1737b.a().a("hideRpmX100Option")) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("RPM x 100");
            jCheckBoxMenuItem.setState(h.i.a("RPM_USE_FORMULA", false));
            jCheckBoxMenuItem.addItemListener(new C0709dt(this));
            dOVar.add((JMenuItem) jCheckBoxMenuItem);
        }
        if (C1737b.a().a("fillNaN")) {
            JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem("Fill NaN Values with Last");
            jCheckBoxMenuItem2.setState(h.i.a("fillNaN", h.i.f12279z));
            jCheckBoxMenuItem2.addItemListener(new C0710du(this));
            dOVar.add((JMenuItem) jCheckBoxMenuItem2);
        }
        JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem("Fill Time Gaps");
        jCheckBoxMenuItem3.setState(h.i.a("timeGapsOn", false));
        jCheckBoxMenuItem3.addItemListener(new C0711dv(this));
        dOVar.add((JMenuItem) jCheckBoxMenuItem3);
        JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem("Alphabetize Field Lists");
        jCheckBoxMenuItem4.setState(h.i.a(h.i.f12284E, h.i.f12285F));
        jCheckBoxMenuItem4.addItemListener(new C0712dw(this));
        dOVar.add((JMenuItem) jCheckBoxMenuItem4);
        String[] strArr = C1737b.f12234m;
        if (strArr != null && strArr.length > 0 && !C1737b.a().a("hideYaxisSelection")) {
            JMenu jMenu = new JMenu("Default Y Axis field");
            boolean z2 = false;
            C0811i c0811i = new C0811i();
            String strA = h.i.a("yAxisField", "MAP");
            for (int i2 = 0; i2 < strArr.length; i2++) {
                JCheckBoxMenuItem jCheckBoxMenuItem5 = new JCheckBoxMenuItem(strArr[i2], strArr[i2].equals(strA));
                if (strArr[i2].equals(strA)) {
                    z2 = true;
                }
                jCheckBoxMenuItem5.addItemListener(new C0713dx(this));
                c0811i.a(jCheckBoxMenuItem5);
                jMenu.add((JMenuItem) jCheckBoxMenuItem5);
            }
            JCheckBoxMenuItem jCheckBoxMenuItem6 = new JCheckBoxMenuItem("Other", !z2);
            jCheckBoxMenuItem6.addItemListener(new C0714dy(this));
            c0811i.a(jCheckBoxMenuItem6);
            jMenu.add((JMenuItem) jCheckBoxMenuItem6);
            dOVar.add((JMenuItem) jMenu);
        }
        JMenu jMenu2 = new JMenu("Mouse Wheel Action");
        C0811i c0811i2 = new C0811i();
        String strA2 = h.i.a(h.i.f12286G, h.i.f12289J);
        JCheckBoxMenuItem jCheckBoxMenuItem7 = new JCheckBoxMenuItem("Zoom Graph In / Out (Default)", h.i.f12287H.equals(strA2));
        jCheckBoxMenuItem7.addItemListener(new dA(this));
        c0811i2.a(jCheckBoxMenuItem7);
        jMenu2.add((JMenuItem) jCheckBoxMenuItem7);
        JCheckBoxMenuItem jCheckBoxMenuItem8 = new JCheckBoxMenuItem("Page Data Forward / Back", h.i.f12288I.equals(strA2));
        jCheckBoxMenuItem8.addItemListener(new dB(this));
        c0811i2.a(jCheckBoxMenuItem8);
        jMenu2.add((JMenuItem) jCheckBoxMenuItem8);
        dOVar.add((JMenuItem) jMenu2);
        JCheckBoxMenuItem jCheckBoxMenuItem9 = new JCheckBoxMenuItem("Keep Index in sync with 2nd instance");
        jCheckBoxMenuItem9.setState(h.i.a(h.i.f12337aD, h.i.f12338aE));
        jCheckBoxMenuItem9.addItemListener(new dC(this));
        dOVar.add((JMenuItem) jCheckBoxMenuItem9);
        if (!C1737b.a().a("hideLoadReversedOption")) {
            this.f5358l = new JCheckBoxMenuItem("Load File Reversed");
            this.f5358l.setToolTipText("Only Applies to DTA logs");
            this.f5358l.setState(h.i.a("loadReverse", true));
            this.f5358l.addItemListener(new dD(this));
            this.f5358l.setVisible(false);
            dOVar.add(this.f5358l);
        }
        dOVar.addSeparator();
        if (C1737b.a().a("fieldNameNormalizationEditable")) {
            JMenu jMenu3 = new JMenu("Field Name Standardization");
            JCheckBoxMenuItem jCheckBoxMenuItem10 = new JCheckBoxMenuItem("Use Name Standardization");
            boolean zA = h.i.a("fieldNameNormaization", true);
            jCheckBoxMenuItem10.setSelected(zA);
            jCheckBoxMenuItem10.addActionListener(new dE(this));
            jMenu3.add((JMenuItem) jCheckBoxMenuItem10);
            this.f5356j = new JMenuItem("Edit Standardized Field Mapping");
            this.f5356j.addActionListener(new dF(this));
            this.f5356j.setEnabled(zA);
            jMenu3.add(this.f5356j);
            dOVar.add((JMenuItem) jMenu3);
        }
        String[] strArrE = h.i.e("APP_OPTION_");
        for (int i3 = 0; i3 < strArrE.length; i3++) {
            JMenuItem jMenuItemB = b(C1733k.a(strArrE[i3], "APP_OPTION_", ""), "");
            jMenuItemB.setText(h.i.b(strArrE[i3]));
            dOVar.add(jMenuItemB);
        }
        dOVar.addSeparator();
        String strE = h.i.e("prefFontSize", com.efiAnalytics.ui.eJ.a() + "");
        dG dGVar = new dG(this);
        int[] iArr = {8, 9, 10, 11, 12, 14, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 38, 44, 50};
        int iA = com.efiAnalytics.ui.eJ.a();
        boolean z3 = false;
        JMenu jMenu4 = new JMenu("Font Size");
        C0811i c0811i3 = new C0811i();
        for (int i4 = 0; i4 < iArr.length; i4++) {
            if (!z3 && iArr[i4] > iA) {
                String str = iA + "";
                z3 = true;
                JCheckBoxMenuItem jCheckBoxMenuItem11 = new JCheckBoxMenuItem(str + " (Default)", str.equals(strE));
                jCheckBoxMenuItem11.setActionCommand(str);
                c0811i3.a(jCheckBoxMenuItem11);
                jCheckBoxMenuItem11.addItemListener(dGVar);
                jMenu4.add((JMenuItem) jCheckBoxMenuItem11);
            }
            String str2 = iArr[i4] + "";
            String str3 = str2;
            if (iArr[i4] == iA) {
                str3 = str3 + " (Default)";
                z3 = true;
            }
            JCheckBoxMenuItem jCheckBoxMenuItem12 = new JCheckBoxMenuItem(str3, str2.equals(strE));
            jCheckBoxMenuItem12.setActionCommand(str2);
            c0811i3.a(jCheckBoxMenuItem12);
            jCheckBoxMenuItem12.addItemListener(dGVar);
            jMenu4.add((JMenuItem) jCheckBoxMenuItem12);
        }
        dOVar.add((JMenuItem) jMenu4);
        JMenu jMenu5 = new JMenu("Graph Background");
        a(jMenu5, new dK(this, this.f5347a.n()), "graphBackColor", -1);
        dOVar.add((JMenuItem) jMenu5);
        boolean zA2 = h.i.a(h.i.f12306aa, h.i.f12307ab);
        JMenuItem jCheckBoxMenuItem13 = new JCheckBoxMenuItem("Repeat Graph Colors");
        jCheckBoxMenuItem13.setSelected(zA2);
        jCheckBoxMenuItem13.addActionListener(new dH(this));
        dOVar.add(jCheckBoxMenuItem13);
        this.f5357k = new JMenu("Graph Trace Colors");
        T();
        dOVar.add(this.f5357k);
        return dOVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void T() {
        this.f5357k.removeAll();
        int iA = h.i.a("numberOfGraphs", h.i.f12273t);
        int iA2 = h.i.a("numberOfOverlays", h.i.f12274u);
        new dK(this, this.f5347a.n());
        int i2 = h.i.a(h.i.f12306aa, h.i.f12307ab) ? iA2 : iA2 * iA;
        h.i.a("numberOfGraphs", h.i.f12273t);
        for (int i3 = 0; i3 < i2; i3++) {
            JMenu jMenu = new JMenu("Graph Color " + (i3 + 1));
            a(jMenu, new dK(this, this.f5347a.n()), "graphForeColor" + i3, i3);
            this.f5357k.add((JMenuItem) jMenu);
        }
        for (int i4 = 0; i4 < i2; i4++) {
            C0804hg.a().a(aO.a().a(i4), i4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h(String str) {
        if (str == null) {
            str = a((String) null, "{Custom Alpha-N Y axis field, this is case sensitive.}", false);
            if (str == null || str.equals("")) {
                return;
            }
        }
        if (str.equals("TP ADC") && h.i.e("APPEND_FIELD_TP ADC", "").equals("")) {
            h.i.c("APPEND_FIELD_TP ADC", a((String) null, h.i.c("APPEND_FIELD_TP ADC"), true));
        }
        System.out.println("yAxis set to" + str);
        h.i.c("yAxisField", str);
        if (this.f5347a.f5149g == null || !this.f5347a.f5149g.isVisible() || this.f5347a.f5149g.f6117f == null || !C1733k.a("Tune Settings file must be reloaded for change to take effect.\nReload now?", (Component) this, true)) {
            return;
        }
        this.f5347a.f5149g.d();
    }

    private JMenu U() {
        JMenu jMenu = new JMenu("Play Back");
        this.f5351e = new JMenuItem("Start Play back");
        this.f5351e.addActionListener(new dI(this));
        jMenu.add(this.f5351e);
        this.f5350d = new JMenuItem("Stop Play back");
        this.f5350d.addActionListener(new dJ(this));
        jMenu.add(this.f5350d);
        this.f5352f = new JMenu("Play Speed");
        C0811i c0811i = new C0811i();
        dV dVVar = new dV(this, this.f5347a.n());
        double[] dArr = this.f5347a.n().f6074k;
        for (int i2 = 0; i2 < dArr.length; i2++) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(((int) (100.0d * dArr[i2])) + FXMLLoader.RESOURCE_KEY_PREFIX);
            jCheckBoxMenuItem.setState(dArr[i2] == h.i.a("playbackSpeed", 1.0d));
            jCheckBoxMenuItem.addItemListener(dVVar);
            jCheckBoxMenuItem.setName(dArr[i2] + "");
            c0811i.a(jCheckBoxMenuItem);
            this.f5352f.add((JMenuItem) jCheckBoxMenuItem);
        }
        jMenu.add((JMenuItem) this.f5352f);
        return jMenu;
    }

    private JMenu a(JMenu jMenu, ItemListener itemListener, String str, int i2) {
        C0811i c0811i = new C0811i();
        for (int i3 = 0; i3 < this.f5349c.length; i3++) {
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(this.f5349c[i3]);
            jCheckBoxMenuItem.setState(this.f5349c[i3].equalsIgnoreCase(h.i.b(str)));
            jCheckBoxMenuItem.addItemListener(itemListener);
            jCheckBoxMenuItem.setName(str);
            c0811i.a(jCheckBoxMenuItem);
            jMenu.add((JMenuItem) jCheckBoxMenuItem);
        }
        return jMenu;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean c(String str, String str2, boolean z2) {
        String[] strArrA;
        if (!z2) {
            h.i.d(str);
            return true;
        }
        String strB = bH.W.b(str, "APPEND_FIELD_", "");
        do {
            if (str2.contains("*") || str2.contains(Marker.ANY_NON_NULL_MARKER) || str2.contains("/") || str2.contains(LanguageTag.SEP)) {
                str2 = a(strB, str2, true);
            } else if (C0804hg.a().r() != null) {
                str2 = a(strB, str2, false);
            }
            if (str2 != null && !str2.equals("")) {
                if (str2.trim().equals("")) {
                    h.i.d(str);
                    c(strB, false);
                } else {
                    h.i.c(str, str2);
                    c(strB, true);
                }
                strArrA = C0617ah.a(strB, str2);
                if (strArrA != null && strArrA.length > 0) {
                    str = C0617ah.c(strArrA[0]);
                    str2 = h.i.c(str);
                }
                if (strArrA == null) {
                    break;
                }
            } else {
                h.i.d(str);
                return false;
            }
        } while (strArrA.length > 0);
        e(z2);
        return true;
    }

    private void c(String str, boolean z2) {
        for (int i2 = 0; i2 < this.f5354h.getMenuComponentCount(); i2++) {
            if (this.f5354h.getMenuComponent(i2) instanceof JMenu) {
                JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) this.f5354h.getMenuComponent(i2);
                if (jCheckBoxMenuItem.getText().equals(str)) {
                    jCheckBoxMenuItem.setSelected(z2);
                    return;
                }
            }
        }
    }

    public String a(String str, String str2, boolean z2) {
        return C1733k.a(str, str2, z2, null, true, this, C0804hg.a().r());
    }

    public void h() {
        this.f5347a.n().e();
    }

    public void i() {
        this.f5347a.n().j();
    }

    public void a(String str, boolean z2) {
        h.i.c(str, z2 + "");
        if (this.f5347a.l() == null || !C1733k.a("Log file must be reloaded for change to take effect.\nReload now?", (Component) this, true)) {
            return;
        }
        b(this.f5347a.l(), false);
    }

    public void a(String str, int i2) {
        h.i.c(str, i2 + "");
        if (this.f5347a.l() == null || !C1733k.a("Log file must be reloaded for change to take effect.\nReload now?", (Component) this, true)) {
            return;
        }
        b(this.f5347a.l(), false);
    }

    public void j() {
        this.f5347a.j();
    }

    public void k() {
        this.f5347a.k();
    }

    public void l() {
        if (this.f5347a.h()) {
            if (f5364r) {
                setVisible(false);
                System.out.println("Update In Progress, delaying app shut down.");
                long jCurrentTimeMillis = System.currentTimeMillis() + 30000;
                while (f5364r && jCurrentTimeMillis > System.currentTimeMillis()) {
                    try {
                        Thread.currentThread();
                        Thread.sleep(200L);
                    } catch (Exception e2) {
                    }
                }
            }
            System.exit(0);
        }
    }

    public void m() {
        if (getExtendedState() != 6) {
            getInsets();
            h.i.c(LanguageTag.PRIVATEUSE, "" + getX());
            h.i.c(PdfOps.y_TOKEN, "" + getY());
            h.i.c(MetadataParser.WIDTH_TAG_NAME, "" + getWidth());
            h.i.c(MetadataParser.HEIGHT_TAG_NAME, "" + getHeight());
        }
    }

    public void n() {
        C1746f.a().c();
        h.i.g();
        System.out.println(h.i.f12255b + " shutdown: " + new Date().toString());
        System.out.println("##############################################################\n");
    }

    public void o() {
        FileDialog fileDialog = new FileDialog(this, "Save Graph to Jpeg or PNG", 1);
        fileDialog.setFile(C1615cz.a("graph.png"));
        fileDialog.setDirectory(h.i.e("lastJpegDir", "."));
        fileDialog.setVisible(true);
        fileDialog.setFilenameFilter(new C0757fn());
        if (fileDialog.getFile() != null) {
            String file = fileDialog.getFile();
            if (!file.toLowerCase().endsWith(".jpg") && !file.toLowerCase().endsWith(".jpeg") && !file.toLowerCase().endsWith(".png")) {
                file = file + ".png";
            }
            boolean zA = false;
            try {
                zA = new C1615cz().a(V(), fileDialog.getDirectory(), file, "Graph by " + h.i.f12255b + " " + h.i.f12256c + " " + h.i.f12254a + " " + h.i.f12266m);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (!zA) {
                C1733k.a("File Save Failed.", this);
            }
            h.i.c("lastJpegDir", fileDialog.getDirectory());
        }
    }

    private Component V() {
        if (this.f5359m != null && !this.f5359m.getTitleAt(this.f5359m.getSelectedIndex()).equals("Log Viewer")) {
            return this.f5359m.getTitleAt(this.f5359m.getSelectedIndex()).equals("Ignition Log Viewer") ? this.f5360n.a() : this.f5359m.getTitleAt(this.f5359m.getSelectedIndex()).equals("Scatter Plots") ? this.f5361o.d() : this.f5359m.getTitleAt(this.f5359m.getSelectedIndex()).equals("Histogram / Table Generator") ? this.f5362p.e() : this.f5347a.p();
        }
        return this.f5347a.p();
    }

    public void p() {
        if (this.f5378w == null) {
            if (this.f5379x == null) {
                this.f5379x = new dN(this);
            }
            this.f5378w = new C0852g(this, this.f5379x);
            int iB = h.i.b(h.i.f12320am, -1);
            int iB2 = h.i.b(h.i.f12321an, -1);
            if (iB2 <= 0 || iB <= 0) {
                this.f5378w.pack();
            } else {
                this.f5378w.setSize(iB, iB2);
            }
            this.f5378w.a(new bS(this));
            com.efiAnalytics.ui.bV.a((Window) this.f5355i, (Component) this.f5378w);
            this.f5378w.setVisible(true);
        }
    }

    public void q() {
        if (this.f5378w != null) {
            this.f5378w.dispose();
            this.f5378w = null;
        }
    }

    public void r() {
        a((String[]) null);
    }

    public void a(String[] strArr) {
        if (strArr == null) {
            strArr = b("Open Log File");
        }
        if (strArr != null) {
            a(strArr, false);
        }
        com.efiAnalytics.ui.cS.a().a(this);
        h.i.g();
    }

    public void s() {
        String[] strArrB = b("Trail Log File");
        if (strArrB != null) {
            b(strArrB[0], true);
        }
        h.i.g();
        new dW(this).start();
        com.efiAnalytics.ui.cS.a().a(this);
    }

    public void t() {
        String[] strArrB = b("Open Compare File");
        if (strArrB != null) {
            c(strArrB[0]);
        }
    }

    public void u() {
        a(C0648bl.a(C0648bl.f5415a));
    }

    public void a(String str) {
        C0516f c0516f = new C0516f();
        c0516f.a(new com.efiAnalytics.ui.dQ(h.i.f(), "AppHelpViewer"));
        try {
            c0516f.b(str);
        } catch (V.a e2) {
            bH.C.a("Unable to open help:\n" + str, e2, this.f5355i);
        }
        c0516f.a(this.f5355i, h.i.f12255b + " Help");
    }

    public void v() throws HeadlessException {
        com.efiAnalytics.ui.dS dSVar = new com.efiAnalytics.ui.dS(this, new dQ(this));
        C1733k.a(this, dSVar);
        dSVar.setModal(true);
        dSVar.setVisible(true);
    }

    public String[] b(String str) {
        String[] strArrA = com.efiAnalytics.ui.bV.a(this, str, W(), (String) null, h.i.e("lastFileDir", h.h.d()), true, null, true, new W.ar(h.i.f(), "FileDialog_"));
        if (strArrA != null && strArrA.length >= 1 && strArrA[0] != null && strArrA[0].lastIndexOf(File.separator) != -1) {
            h.i.c("lastFileDir", strArrA[0].substring(0, strArrA[0].lastIndexOf(File.separator)));
        }
        return strArrA;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] W() {
        return C1733k.a(h.i.a("fileExtensions", "msl;xls;txt;dif;msq;csv"), ";");
    }

    public File w() {
        return this.f5374t;
    }

    public void b(String str, boolean z2) {
        this.f5347a.a(new String[]{str}, z2);
        if (this.f5359m != null && this.f5359m.e("Purchase Registration")) {
            this.f5359m.g("Log Viewer");
        }
        this.f5374t = new File(str);
        this.f5376u.a(str);
        setTitle(this.f5374t.getName());
    }

    public void a(String[] strArr, boolean z2) {
        File file = new File(strArr[0]);
        if (C1737b.a().a("triggerLogViewer") && W.U.a(file)) {
            this.f5360n.a(file.getAbsolutePath());
            if (this.f5359m != null) {
                this.f5359m.g("Ignition Log Viewer");
            }
        } else if (!W.U.b(file)) {
            this.f5347a.a(strArr, z2);
            if (this.f5359m != null && !this.f5359m.e("Log Viewer") && !this.f5359m.e("Scatter Plots")) {
                this.f5359m.g("Log Viewer");
            }
            this.f5374t = file;
            if (this.f5358l != null) {
                try {
                    this.f5358l.setVisible(W.X.f1990F.equals(W.X.a(file)));
                } catch (V.a e2) {
                } catch (FileNotFoundException e3) {
                }
            }
        } else if (C1737b.a().a("tuningPanelVisible")) {
            try {
                this.f5347a.b(true);
                h.i.c("showTuningConsole", "true");
                this.f5347a.d(strArr[0]);
            } catch (V.a e4) {
                com.efiAnalytics.ui.bV.d(e4.getMessage(), this);
            }
            this.f5359m.g("Log Viewer");
        } else {
            com.efiAnalytics.ui.bV.d("The Lite! Edition can not load and edit " + strArr[0].substring(strArr[0].lastIndexOf(".")) + " (tune settings) files. \nPlease Check Out the registered version for \nAdvanced Features to bring you a tune like never before.", this);
        }
        String strSubstring = strArr[0].substring(strArr[0].lastIndexOf(File.separator) + 1);
        this.f5376u.a(strArr[0]);
        setTitle(strSubstring);
    }

    public void c(String str) {
        this.f5347a.c(str);
        setTitle(this.f5374t.getName() + " - " + new File(str).getName());
    }

    @Override // java.awt.event.WindowListener
    public void windowActivated(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosed(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        l();
    }

    @Override // java.awt.event.WindowListener
    public void windowDeactivated(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowDeiconified(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowIconified(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override // java.awt.Window, java.awt.Container, java.awt.Component
    public void processEvent(AWTEvent aWTEvent) {
        if ((aWTEvent.getID() == 101 || aWTEvent.getID() == 100) && !bH.I.b()) {
            m();
            if (super.isAlwaysOnTop()) {
                super.setAlwaysOnTop(false);
            }
        }
        if (aWTEvent.getID() == 201 && bH.I.b()) {
            m();
            if (super.isAlwaysOnTop()) {
                super.setAlwaysOnTop(false);
            }
        } else if (aWTEvent.getID() == 209) {
            if (getExtendedState() == 6) {
                h.i.c(h.i.f12317al, "true");
            } else {
                h.i.c(h.i.f12317al, "false");
            }
        }
        super.processEvent(aWTEvent);
    }

    @Override // java.awt.Frame
    public void setTitle(String str) {
        super.setTitle(h.i.f12255b + " " + h.i.f12256c + " " + h.i.f12254a + " - " + str);
    }

    public void d(String str) {
        if (str == null) {
            return;
        }
        if (str.equals("lineGraph")) {
            if (this.f5359m.e("Log Viewer")) {
                return;
            }
            this.f5359m.g("Log Viewer");
        } else if (str.equals("ignitionLogger")) {
            if (this.f5359m.e("Ignition Log Viewer")) {
                return;
            }
            this.f5359m.g("Ignition Log Viewer");
        } else if (str.equals("scatterPlot")) {
            if (this.f5359m.e("Scatter Plots")) {
                return;
            }
            this.f5359m.g("Scatter Plots");
        } else {
            if (!str.equals("histogram") || this.f5359m.e("Histogram / Table Generator")) {
                return;
            }
            this.f5359m.g("Histogram / Table Generator");
        }
    }

    public String x() {
        String strC = h.i.c("registrationUrl");
        if (strC == null || strC.equals("")) {
            try {
                strC = BareBonesBrowserLaunch.FILE_PREFIX + new File(".").getCanonicalPath() + "/help/register.html";
            } catch (Exception e2) {
                C1733k.a("While online go to\n" + h.i.f12267n, this.f5355i);
            }
        }
        return strC;
    }

    protected void y() {
        C1733k.a("Failed to open file for trailing, timeout.", this);
    }

    public void e(boolean z2) {
        if (this.f5347a.l() != null && z2 && 0 == JOptionPane.showConfirmDialog(this, "Log file must be reloaded for change to take effect.\nReload now?", "Reload?", 0)) {
            this.f5347a.v();
            b(this.f5347a.l(), false);
        }
    }
}
