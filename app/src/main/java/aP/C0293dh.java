package aP;

import az.C0942c;
import bH.C1005m;
import bH.C1011s;
import bH.C1018z;
import bH.InterfaceC0994b;
import com.sun.glass.events.WindowEvent;
import com.sun.media.jfxmedia.MetadataParser;
import f.C1722d;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import org.icepdf.core.util.PdfOps;
import org.icepdf.ri.util.BareBonesBrowserLaunch;
import r.C1798a;
import r.C1806i;
import r.C1807j;
import s.C1818g;
import sun.font.Font2D;
import sun.util.locale.LanguageTag;

/* renamed from: aP.dh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dh.class */
public class C0293dh extends JFrame implements G.S, G.aG, aU, InterfaceC0994b, WindowListener {

    /* renamed from: a, reason: collision with root package name */
    long f3225a;

    /* renamed from: d, reason: collision with root package name */
    private C0308dx f3226d;

    /* renamed from: e, reason: collision with root package name */
    private bY f3227e;

    /* renamed from: b, reason: collision with root package name */
    public static boolean f3228b = false;

    /* renamed from: f, reason: collision with root package name */
    private static String f3229f = C1806i.f13441c;

    /* renamed from: g, reason: collision with root package name */
    private static String f3230g = C1806i.f13440b;

    /* renamed from: h, reason: collision with root package name */
    private boolean f3231h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f3232i;

    /* renamed from: c, reason: collision with root package name */
    boolean f3233c;

    public C0293dh() {
        this(true);
    }

    public C0293dh(boolean z2) throws HeadlessException {
        boolean z3;
        this.f3225a = 604800000L;
        this.f3226d = null;
        this.f3227e = bY.a();
        this.f3231h = false;
        this.f3232i = false;
        this.f3233c = false;
        com.efiAnalytics.ui.cS cSVarA = com.efiAnalytics.ui.cS.a(new C0305du(this));
        cSVarA.f7069c = 800;
        com.efiAnalytics.ui.bV.a(C1798a.a());
        cSVarA.f7069c += Font2D.FWEIGHT_BOLD;
        bI.h.a().a(new az.t());
        cZ.a().a((JFrame) this);
        cSVarA.f7069c += 466;
        y();
        C1018z.i().f7069c = cSVarA.f7069c;
        C1018z.i().a(true);
        String strC = C1798a.a().c(C1798a.cF, "");
        hJ hJVar = new hJ(this);
        boolean zC = C1798a.a().c(C1798a.f13302J, false);
        String strB = bH.W.b(C1798a.f13269c, f3230g, "");
        boolean zX = x();
        if (zX) {
            az.o.a(true);
        }
        if (zC || !cSVarA.a(strC)) {
            this.f3231h = false;
        } else {
            this.f3231h = u();
            if (!this.f3231h) {
                C1798a.a().d(C1798a.f13276j, "");
                try {
                    C1798a.a().i();
                } catch (V.a e2) {
                    Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            strB = C1798a.a().c(C1798a.f13280n, C1798a.f13269c);
            boolean zA = C1806i.a().a(C1798a.f13268b, strB);
            if (zA) {
                C1798a.a().c(zA);
                strB = C1798a.a().c(C1798a.cn, strB);
            }
            if (this.f3231h && C1798a.f13269c.contains(C1806i.f13440b) && !strB.contains(C1806i.f13440b)) {
                C1798a.f13269c = strB + C1806i.f13440b;
            } else {
                C1798a.f13269c = strB;
            }
        }
        if (this.f3231h) {
            bC.a().a(C1798a.f13268b, strB);
        } else {
            C1798a.f13269c += f3229f;
            bC.a().a(C1798a.f13268b, C1798a.f13269c);
        }
        c();
        jI.a();
        if (C1798a.a().c(C1798a.f13291y, false)) {
            C0288dc.a().a(true);
        } else if (!C1798a.a().c(C1798a.f13412bP, C1798a.f13413bQ) && !C0288dc.a().b()) {
            System.exit(0);
        }
        if (z2) {
            g();
        }
        if (C1798a.f13268b.equals(C1798a.f13340av) || C1798a.f13268b.equals(C1798a.f13341aw)) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/MTicon.png")));
        } else if (C1798a.f13268b.equals(C1798a.f13339au)) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/GRicon32.png")));
        } else if (C1798a.f13268b.equals(C1798a.f13337as)) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/BigCommIcon.png")));
        } else if (C1798a.f13268b.equals(C1798a.f13338at)) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/BigCommGen4Icon.png")));
        } else {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/TSicon.gif")));
        }
        jI.a();
        if (C1798a.a().a(C1798a.f13364aT, 1000) == 1000 || Math.random() < 0.1d) {
            new C0294di(this).start();
        }
        if (com.efiAnalytics.ui.bV.d()) {
            C0204a.class.getClass();
            C0204a.a();
        }
        if (zX) {
            C0404hl.a().a("Finishing Upgrade....");
            Thread.yield();
            A();
            z();
            f();
            C1798a.a().b(C1798a.f13362aR, "" + ((System.currentTimeMillis() - this.f3225a) - 1000));
            C1798a.a().b(C1798a.f13363aS, "" + (System.currentTimeMillis() - 1000));
            try {
                C1798a.a().e();
            } catch (V.a e3) {
                bH.C.a("Error saving preference file.", e3, this);
            }
            if (bH.I.f()) {
                File file = new File(".", "environment.txt");
                if (new File(".", "runtime").isDirectory()) {
                    bH.C.d("Running on XP, switching JRE");
                    if (!file.exists()) {
                        bH.C.d("environment.txt missing, creating");
                    } else if (file.delete()) {
                        try {
                            file.createNewFile();
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                            bufferedWriter.append((CharSequence) "-Xms128m -Xmx512m -cp .;./plugins/;./runtime/lib/*;./lib/*.jar -Djava.home=./runtime -Djava.library.path=./lib -jar ");
                            bufferedWriter.append((CharSequence) C1798a.f13275i);
                            bufferedWriter.newLine();
                            bufferedWriter.append((CharSequence) "runtime");
                            bufferedWriter.newLine();
                            bufferedWriter.append((CharSequence) C1798a.f13275i);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            bufferedWriter.close();
                        } catch (IOException e4) {
                            bH.C.a(e4);
                        }
                    } else {
                        bH.C.b("Failed to delete environment.txt");
                    }
                } else {
                    bH.C.d("Running on XP, but cannot switch JRE as there is no runtime dir.");
                }
            }
        }
        d();
        hJVar.b();
        hJVar.start();
        setTitle("");
        addWindowListener(this);
        C0404hl.a().a("Initializing User Interface");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int iC = C1798a.a().c(LanguageTag.PRIVATEUSE, 100);
        if (iC > screenSize.width && !com.efiAnalytics.ui.bV.h()) {
            iC = 100;
        }
        int iC2 = C1798a.a().c(PdfOps.y_TOKEN, 50);
        int iC3 = C1798a.a().c(MetadataParser.WIDTH_TAG_NAME, screenSize.width - 200);
        int iC4 = C1798a.a().c(MetadataParser.HEIGHT_TAG_NAME, screenSize.height - 100);
        ToolTipManager.sharedInstance().setEnabled(true);
        gW gWVar = new gW();
        G.T.a().a(gWVar);
        add(BorderLayout.CENTER, gWVar);
        iR iRVar = new iR((this.f3231h || C1806i.a().a("surpressAds")) ? false : true);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(5, 5));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 1));
        jPanel.add("North", jPanel2);
        cZ.a().a(jPanel2);
        jPanel.add("South", iRVar);
        add("South", jPanel);
        this.f3226d = new C0308dx(this, C0338f.a());
        cZ.a().a(this.f3226d);
        C0338f.a().a(this.f3226d);
        G.T tA = G.T.a();
        tA.a(this);
        tA.a(this.f3226d);
        tA.a(iRVar);
        setBounds(com.efiAnalytics.ui.bV.a(new Rectangle(iC, iC2, iC3, iC4)));
        if (C1798a.a().c("mainWindowMaximized", false)) {
            setExtendedState(6);
        }
        doLayout();
        enableEvents(1L);
        enableEvents(262144L);
        com.efiAnalytics.ui.bV.a((Window) this);
        C0338f.a().a((InterfaceC0994b) this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f3227e);
        G.T.a().a(this.f3227e);
        h();
        if (0 != 0) {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(1) <= 2500 || Locale.getDefault().equals(Locale.US)) {
                calendar.set(2025, 6, 28);
            } else {
                calendar.set(2025 + WindowEvent.FOCUS_GAINED_FORWARD, 6, 28);
            }
            long jA = C1798a.a().a(C1798a.f13363aS, 0L);
            Properties properties = new Properties();
            properties.setProperty(r.q.f13506b, C1798a.f13268b);
            properties.setProperty(r.q.f13507c, C1798a.f13269c);
            if (!this.f3231h && (calendar.getTime().getTime() < System.currentTimeMillis() || jA > calendar.getTimeInMillis())) {
                try {
                    z3 = !l();
                } catch (V.a e5) {
                    bH.C.a("Unable to get upgrade, please try a manual install.", e5, this);
                    z3 = true;
                }
                if (z3) {
                    com.efiAnalytics.ui.cS.a().a(this);
                    com.efiAnalytics.ui.bV.d(r.q.a(properties, r.q.f13510f), this);
                }
                com.efiAnalytics.ui.aN.a(C1798a.f13274h);
                System.exit(0);
            }
            C1798a.a().b(C1798a.f13363aS, "" + System.currentTimeMillis());
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(1979, 5, 1);
            if (!this.f3231h && calendar2.getTime().getTime() > System.currentTimeMillis()) {
                com.efiAnalytics.ui.bV.d(r.q.a(properties, r.q.f13511g), this);
                com.efiAnalytics.ui.aN.a(C1798a.f13274h);
                System.exit(0);
            }
            if (!this.f3231h && calendar.getTime().getTime() < System.currentTimeMillis() + 2592000000L) {
                properties.setProperty(r.q.f13508d, ((long) ((calendar.getTime().getTime() - System.currentTimeMillis()) / 8.64E7d)) + "");
                com.efiAnalytics.ui.bV.d(r.q.a(properties, r.q.f13514j), this);
            }
            if (this.f3231h && C1798a.a().c(C1798a.cM, false)) {
                String strA = r.q.a(properties, r.q.f13512h);
                C1798a.a().e(C1798a.cM);
                try {
                    v();
                } catch (V.a e6) {
                }
                com.efiAnalytics.ui.bV.d(strA, this);
                com.efiAnalytics.ui.aN.a(C1798a.f13274h);
                System.exit(0);
            }
        }
        setVisible(true);
        boolean z4 = C1798a.a().a(C1798a.f13362aR, 0L) + this.f3225a < System.currentTimeMillis() && (C1798a.a().c(C1798a.f13361aQ, true) || !C1018z.i().a(C1798a.a().c(C1798a.cF, "")));
        if (C1798a.a().c(C1798a.f13291y, false) && C1807j.a(".")) {
            C1798a.a().b(C1798a.f13362aR, "0");
            C0338f.a().e(C1818g.b("Updating Application Files"));
            this.f3232i = true;
            new C0307dw(this, true, this).start();
        } else if (z4) {
            new C0307dw(this, false, this).start();
        }
        setTransferHandler(new C0304dt(this));
    }

    public void c() {
        aE.a.f2341a = new C0295dj(this);
        C1818g.a();
        String strA = C1798a.a().a("viewLanguageCode", "en");
        try {
            C0404hl.a().a("Loading Language Content.");
            C1818g.a(strA);
            C1818g.b(" dummyToLoad");
            G.bL.a(C1818g.d());
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d("Unable to set language code.\n" + e2.getMessage(), this);
        }
    }

    public void d() {
        try {
            double d2 = Double.parseDouble(System.getProperty("java.specification.version"));
            if (d2 < 1.8d) {
                if (bH.I.a()) {
                    if (com.efiAnalytics.ui.bV.a(C1798a.f13268b + " is currently running on JRE version " + d2 + ".\nThis application requires JRE 1.8 or higher.\nIf you continue, you will experience problems.\n\nThe easiest way to correct this is to download and run the installer.\n\nWould you like to go to the download page now?", (Component) this, true)) {
                        com.efiAnalytics.ui.aN.a("https://www.efianalytics.com/TunerStudio/download/");
                    }
                } else if (com.efiAnalytics.ui.bV.a(C1798a.f13268b + " is currently running on JRE version " + d2 + ".\nThis application requires JRE 1.8 or higher.\nIf you continue, you will experience problems.\n\nWould you like to get the latest JRE now?", (Component) this, true)) {
                    com.efiAnalytics.ui.aN.a("https://adoptopenjdk.net/");
                }
            }
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Unable to determine the JRE version.\nJRE version 1.8 is required for this application", this);
        }
    }

    protected void e() {
        new C0307dw(this, false, this).start();
    }

    public void f() {
        File file = new File(C1807j.D(), "firmwareLoader");
        File file2 = new File(C1807j.C(), "firmwareLoader");
        try {
            if (file.exists()) {
                C1011s.a(file, file2);
            }
        } catch (V.a e2) {
            Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // javax.swing.JFrame, java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
        super.repaint(j2, i2, i3, i4, i5);
    }

    public static void g() {
        if (cZ.a().e() == null) {
            String strC = C1798a.a().c(C1798a.f13368aX);
            if (strC == null) {
                bH.C.b("No splash Image found, not showing splash screen.");
            } else {
                cZ.a().a(new iE(null, strC, false));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean u() throws HeadlessException {
        if (!C1806i.a().a(";'GD;'GD[PHGD-0HL")) {
            az.o oVarA = az.o.a(new aQ.a(), C1818g.d());
            C1722d c1722dG = oVarA.g();
            if (c1722dG == null) {
                C1798a.f13269c += f3229f;
                this.f3231h = false;
            } else if (c1722dG.a() == 0) {
                this.f3231h = true;
                new C0296dk(this, oVarA).start();
            } else if (c1722dG.a() == 4) {
                com.efiAnalytics.ui.bV.d(C1818g.b(C0942c.f6475h) + "\n" + C1818g.b(C0942c.f6473f), this);
                this.f3231h = false;
            } else if (c1722dG.a() == 1) {
                bH.C.c("renewal over due");
                if (C1798a.a().c(C1798a.f13280n, C1798a.f13269c).contains(C1806i.f13442d)) {
                    com.efiAnalytics.ui.bV.d(C1818g.b("Trial Activation has expired.") + "\n" + C1818g.b("Please connect to the internet to see if trial period is still valid and reactivate."), this);
                    C1798a.a().d(C1798a.f13276j, "");
                    try {
                        C1798a.a().i();
                    } catch (V.a e2) {
                        Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                    this.f3231h = false;
                } else {
                    this.f3231h = true;
                }
            } else if (c1722dG.a() == 2) {
                com.efiAnalytics.ui.bV.d(C1818g.b(C0942c.f6475h) + "\n" + C1818g.b(c1722dG.b()), this);
                this.f3231h = false;
            } else if (c1722dG.a() == 7) {
                com.efiAnalytics.ui.bV.d(C1818g.b(C0942c.f6477j) + "\n" + C1818g.b(c1722dG.b()), this);
                this.f3231h = false;
            } else if (c1722dG.a() == 5) {
                com.efiAnalytics.ui.bV.d(C0942c.c(C1818g.d()) + "\n" + C1818g.b(c1722dG.b()), this);
                this.f3231h = false;
            } else if (c1722dG.a() == 6) {
                String strB = bH.W.b(bH.W.b(C1798a.f13269c, C1806i.f13441c, ""), C1806i.f13440b, "");
                String strC = C1798a.a().c(C1798a.f13280n, "");
                if (strC == null || !strC.contains(C1806i.f13442d)) {
                    com.efiAnalytics.ui.bV.d("The registration information provided is no longer valid for " + C1798a.a().l() + ".\n\nPlease contact EFI Analytics if you believe this registration is valid.", cZ.a().c());
                    com.efiAnalytics.ui.aN.a("https://www.efianalytics.com/register/register.jsp?appName=" + C1798a.f13268b + strB);
                } else {
                    String strC2 = C1798a.a().c(C1798a.cF, "");
                    String strC3 = C1798a.a().c(C1798a.cI, "");
                    if (strC3 == null || strC3.isEmpty() || strC3.equals(strC2)) {
                        C1798a.a().d(C1798a.cF, "");
                        C1798a.a().d(C1798a.f13280n, "");
                        String str = C1798a.f13268b + " Registration Notice!\n\n" + C1818g.b("We apologize, but your trial registration has expired.") + "\n\n" + C1818g.b("Unfortunately, there is no previous registration information on this computer.") + "\n" + C1818g.b("You will need your previous registration if you have one.") + "\n\n" + C1818g.b("Would you like to check for current Upgrades?");
                        String[] strArr = {C1818g.b("Check for Upgrades"), C1818g.b("Enter previous registration"), C1818g.b("Use Lite! Edition")};
                        int iShowOptionDialog = JOptionPane.showOptionDialog(this, str, "Trial Ended", 1, 3, null, strArr, strArr[0]);
                        if (iShowOptionDialog == 0) {
                            com.efiAnalytics.ui.aN.a(C1798a.f13305M);
                        } else if (iShowOptionDialog == 1) {
                            com.efiAnalytics.ui.aN.a(C1798a.f13306N);
                            new C0297dl(this).start();
                        }
                        try {
                            C1798a.a().i();
                        } catch (V.a e3) {
                            Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                        }
                        this.f3231h = false;
                        return this.f3231h;
                    }
                    C1798a.a().d(C1798a.cF, strC3);
                    C1798a.a().d(C1798a.cI, "");
                    C1798a.a().d(C1798a.f13280n, strB);
                    if (C1018z.i().a(strC3)) {
                        try {
                            C1798a.a().i();
                        } catch (V.a e4) {
                            Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        }
                        String strB2 = C1806i.a().b();
                        if (strB2 != null && !strB2.isEmpty() && com.efiAnalytics.ui.bV.a(C1798a.f13268b + " Registration Notice!\n\n" + C1818g.b("We apologize, but your trial registration has expired.") + "\n" + C1818g.b("Your previous registration has be restored.") + "\n\n" + C1818g.b("Would you like to check for current Upgrades?"), "Trial Ended", this, new String[]{C1818g.b("Check for Upgrades"), C1818g.b("Use previous registration")})) {
                            com.efiAnalytics.ui.aN.a(strB2);
                        }
                        return u();
                    }
                }
                try {
                    v();
                } catch (V.a e5) {
                    Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
                this.f3231h = false;
            }
        }
        return this.f3231h;
    }

    private void v() {
        C1798a.a().d(C1798a.cF, "");
        C1798a.a().d(C1798a.cE, "");
        C1798a.a().d(C1798a.cC, "");
        C1798a.a().d(C1798a.cD, "");
        C1798a.a().d(C1798a.f13280n, "");
        C1798a.a().i();
        C1798a.a().e();
    }

    private void b(String str) {
        try {
            String strC = C1798a.a().c(C1798a.cN, "");
            String[] strArrSplit = strC.split(";");
            for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                if (!strArrSplit[i2].equals(str)) {
                    strC = strC + strArrSplit[i2] + ";";
                }
            }
            C1798a.a().b(C1798a.cN, strC);
            C1798a.a().e();
        } catch (V.a e2) {
        }
    }

    public void h() {
        iE iEVarE = cZ.a().e();
        if (iEVarE != null) {
            if ((this.f3231h || C1806i.a().a("surpressAds")) ? false : true) {
                new C0298dm(this).start();
            } else {
                iEVarE.dispose();
                cZ.a().a((iE) null);
            }
        }
    }

    public void i() {
        if (this.f3232i) {
            return;
        }
        if (!C1798a.a().c(C1798a.f13376bf, C1798a.cP)) {
            C0338f.a().G();
            return;
        }
        String strC = C1798a.a().c("lastProjectPath", "");
        if (strC.equals("") || aE.a.u(strC)) {
            String strS = C1807j.s();
            if (new File(strS).exists()) {
                C0404hl.a().a("Opening default Project....");
                C0338f.a().a((Window) this, strS);
                return;
            }
            return;
        }
        C0404hl.a().a("Opening Last Project....");
        jI.a();
        C0338f.a().k((Frame) this);
        if (C1798a.a().c(C1798a.f13377bg, false)) {
            jI.a();
            C0338f.a().a(new C0299dn(this));
        }
    }

    public int j() {
        C1798a.a().c(C1798a.f13364aT, 0);
        bH.C.c("Running speed test");
        bH.T t2 = new bH.T();
        t2.a();
        int iA = t2.a();
        C1798a.a().b(C1798a.f13364aT, "" + iA);
        bH.C.c("Processor Score:" + iA);
        return iA;
    }

    public void k() {
        C1798a.a().b(C1798a.f13362aR, "0");
        new C0307dw(this, true, this).start();
    }

    public boolean a(String str, String str2, String str3, String str4, String str5) throws V.a {
        try {
            bW.d dVarA = new bW.a().a(C1798a.a().c(C1798a.f13358aN, ""), C1798a.f13268b, C1798a.f13267a, C1798a.a().c(C1798a.f13359aO, ""), C1798a.a().c(C1798a.f13364aT, ""), C1798a.a().c(C1798a.cF, ""), str5, C1798a.a().c(C1798a.cO, ""));
            if (dVarA == null) {
                throw new V.a("Update server is currently unavailable");
            }
            if (dVarA.a() == 4 || dVarA.a() == 8) {
                return true;
            }
            b(str3);
            C1798a.a().b(C1798a.cM, "false");
            C1798a.a().b(C1798a.cL, "false");
            return false;
        } catch (IOException e2) {
            System.out.println("Unable to read from update server, connection to server unavailable");
            throw new V.a("Update server is currently unavailable");
        }
    }

    public boolean l() throws V.a {
        Date date = new Date();
        date.setTime(C1798a.a().a(C1798a.f13362aR, (new Date().getTime() - this.f3225a) - 1));
        Date date2 = new Date();
        date2.setTime(System.currentTimeMillis() - this.f3225a);
        boolean z2 = false;
        if (date.before(date2)) {
            bW.a aVar = new bW.a();
            if (!C1005m.a()) {
                throw new V.a("Unable to connect to Internet");
            }
            try {
                String strTrim = bH.W.b(bH.W.b(C1798a.f13269c, f3229f, "").trim(), C1806i.f13442d, "").trim();
                bW.e eVar = new bW.e();
                eVar.a(C1798a.a().c(C1798a.f13358aN, ""));
                eVar.b(C1798a.f13268b);
                eVar.c(C1798a.f13267a);
                eVar.d(C1798a.a().c(C1798a.f13359aO, ""));
                eVar.e(C1798a.a().c(C1798a.f13364aT, ""));
                eVar.f(C1798a.a().c(C1798a.cF, ""));
                eVar.g(strTrim);
                eVar.h(C1798a.a().c(C1798a.cO, ""));
                eVar.i(C1818g.c().getLanguage());
                bW.d dVarA = aVar.a(eVar);
                if (dVarA == null) {
                    return false;
                }
                if (dVarA.a() == 0) {
                    this.f3233c = false;
                    w();
                    C1798a.a().b(C1798a.f13362aR, "" + new Date().getTime());
                    C1798a.a().e();
                    return false;
                }
                if (dVarA.a() == 2) {
                    w();
                    if (!(this.f3232i || com.efiAnalytics.ui.bV.a(dVarA.b(), C1818g.b("Update Available"), this, new String[]{C1818g.b("Complete Update"), C1818g.b("Update Later")}))) {
                        return true;
                    }
                    if (!C1807j.a(".") && com.efiAnalytics.ui.bV.d()) {
                        if (!new File("Elevate.exe").exists()) {
                            if (!com.efiAnalytics.ui.bV.a(C1798a.a().b() + " does not currently have write access to the installation folder.\n\nUsing Auto Update with all Windows after XP requires \nAdministrator rights. Please start " + C1798a.a().b() + "\nUsing Run As Administrator to complete Auto Update. \nWith Windows 7 the \"Run As Administrator\" Option can be found by \nPressing the Shift Key and right clicking on the icon you used to start " + C1798a.a().b() + "\n\nAlternatively you can download the latest installer from:\nhttp://www.efiAnalytics.com/" + bH.W.b(C1798a.f13268b, " ", "") + "/\n\nWould you like to go to the download site now?", (Component) this, true)) {
                                return true;
                            }
                            com.efiAnalytics.ui.aN.a("http://www.efiAnalytics.com/" + C1798a.f13268b + "/");
                            return true;
                        }
                        if (1 == 0) {
                            return false;
                        }
                        C1798a.a().b(C1798a.f13291y, "true");
                        C1798a.a().e();
                        try {
                            if (G.T.a().c() != null && G.T.a().c().R()) {
                                G.T.a().c().C().c();
                            }
                        } catch (Exception e2) {
                        }
                        try {
                            Runtime.getRuntime().exec("Elevate.exe " + bH.W.b(C1798a.f13268b, " ", "") + ".exe");
                            Runtime.getRuntime().halt(0);
                            return true;
                        } catch (IOException e3) {
                            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                            return true;
                        }
                    }
                    try {
                        C0338f.a().e(C1818g.b("Initializing downloads") + "........");
                        aVar.a(new C0300dp(this));
                        aVar.a(dVarA);
                        z2 = true;
                    } catch (IOException e4) {
                        com.efiAnalytics.ui.bV.d("Auto update is unable to upgrade " + C1798a.a().b() + "\nThis is usually due to a lost connection to the internet or insufficient access \nto write to the installation directory.\n\nInternal Error:\n" + e4.getMessage(), this);
                        e4.printStackTrace();
                    }
                } else if (dVarA.a() == 1) {
                    com.efiAnalytics.ui.bV.d(dVarA.b(), this);
                } else if (dVarA.a() == 8) {
                    C1798a.a().b(C1798a.cM, "true");
                } else if (dVarA.a() == 4) {
                }
                C1798a.a().b(C1798a.f13362aR, "" + new Date().getTime());
                C1798a.a().e();
            } catch (IOException e5) {
                System.out.println("Unable to read from update server, connection to server unavailable");
                throw new V.a("Update server is currently unavailable");
            }
        } else {
            System.out.println("No check, last update check=" + date.toString());
        }
        this.f3233c = false;
        if (!z2) {
            return true;
        }
        C1798a.a().b(C1798a.f13291y, "false");
        C1798a.a().e();
        h.i.g();
        C0338f.a().d((Window) this);
        Runtime.getRuntime().halt(0);
        return true;
    }

    private void w() {
        if (C1018z.i().a(C1798a.a().c(C1798a.cF, ""))) {
            C1798a.a().b(C1798a.cK, "true");
        }
    }

    private boolean x() {
        if (C1798a.f13267a.equals(C1798a.a().c(C1798a.f13360aP, ""))) {
            return false;
        }
        C1798a.a().b(C1798a.f13360aP, C1798a.f13267a);
        try {
            h.i.c("version", C1798a.f13267a);
            return true;
        } catch (Exception e2) {
            return true;
        }
    }

    private void y() {
        try {
            if (c(C1798a.a().h().getProperty(C1798a.f13358aN, ""))) {
                return;
            }
            C1798a.a().d(C1798a.f13358aN, "" + ((long) (9.223372036854776E18d * Math.random())));
            String strC = C1798a.a().c(C1798a.f13359aO, "");
            if (strC.equals("")) {
                strC = "" + new Date().getTime();
            }
            C1798a.a().d(C1798a.f13359aO, strC);
            C1798a.a().i();
            C1798a.a().e(C1798a.f13358aN);
            C1798a.a().e(C1798a.f13359aO);
        } catch (Exception e2) {
            System.out.println("Failed to set uid");
            e2.printStackTrace();
        }
    }

    private boolean c(String str) {
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

    public void m() {
        com.efiAnalytics.ui.bV.d("No Updates Available, " + C1798a.f13267a + " is the latest version.", this);
    }

    private void z() {
        C1798a.a().e(C1798a.f13363aS);
        C1798a.a().e(C1798a.cL);
        try {
            iK.a().b();
        } catch (Exception e2) {
            bH.C.b("Failed to clear start screen dir.");
            Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (C1798a.f13267a.equals("2.7.09")) {
            for (int i2 = 0; i2 < 4; i2++) {
                for (String str : C1798a.a().f("graph" + i2)) {
                    C1798a.a().e(str);
                }
            }
            C1798a.a().b("selectionTable2Multiview_Fuel_Table1_checked3D", "true");
        }
        File file = new File("OSX_fixperm-217-leo.sh");
        if (file.exists()) {
            file.delete();
        }
    }

    private void A() {
        String[] list = new File(".").list();
        for (int i2 = 0; i2 < list.length; i2++) {
            if (list[i2].toLowerCase().endsWith(".zip")) {
                new File(list[i2]);
                try {
                    C0404hl.a().a("Updating Files in " + bH.W.b(list[i2], ".zip", ""));
                    if (bH.ad.a(list[i2], bH.W.b(list[i2], ".zip", ""), (String) null).equals(bH.ad.f7040a)) {
                        new File(list[i2]).delete();
                    }
                } catch (ZipException e2) {
                    e2.printStackTrace();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public boolean n() {
        bH.C.d("closeApp Called");
        long jCurrentTimeMillis = System.currentTimeMillis() + 30000;
        if (this.f3233c) {
            setVisible(false);
            System.out.println("Update In Progress, delaying app shut down.");
            while (this.f3233c && System.currentTimeMillis() < jCurrentTimeMillis) {
                try {
                    Thread.currentThread();
                    Thread.sleep(200L);
                } catch (Exception e2) {
                }
            }
            bH.C.d("Delay Close released.");
        }
        return C0338f.a().g();
    }

    public void o() {
        if (getExtendedState() == 6) {
            return;
        }
        getInsets();
        C1798a.a().b(LanguageTag.PRIVATEUSE, "" + getX());
        C1798a.a().b(PdfOps.y_TOKEN, "" + getY());
        C1798a.a().b(MetadataParser.WIDTH_TAG_NAME, "" + getWidth());
        C1798a.a().b(MetadataParser.HEIGHT_TAG_NAME, "" + getHeight());
    }

    @Override // java.awt.event.WindowListener
    public void windowActivated(java.awt.event.WindowEvent windowEvent) {
        if (!C1798a.a().c(C1798a.f13319aa, C1798a.f13320ab) || com.efiAnalytics.ui.dI.a().d()) {
            return;
        }
        com.efiAnalytics.ui.dI.a().b();
    }

    @Override // java.awt.event.WindowListener
    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        new C0301dq(this).start();
        C0338f.a().y();
        C1798a.a().a(true);
        System.exit(0);
    }

    @Override // java.awt.event.WindowListener
    public void windowDeactivated(java.awt.event.WindowEvent windowEvent) {
        if (C1798a.a().c(C1798a.f13319aa, C1798a.f13320ab) || !com.efiAnalytics.ui.dI.a().d()) {
            return;
        }
        com.efiAnalytics.ui.dI.a().c();
    }

    @Override // java.awt.event.WindowListener
    public void windowDeiconified(java.awt.event.WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowIconified(java.awt.event.WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowOpened(java.awt.event.WindowEvent windowEvent) {
    }

    @Override // java.awt.Window, java.awt.Container, java.awt.Component
    public void processEvent(AWTEvent aWTEvent) {
        if ((aWTEvent.getID() == 101 || aWTEvent.getID() == 100) && !bH.I.b()) {
            o();
            if (super.isAlwaysOnTop()) {
                super.setAlwaysOnTop(false);
            }
        }
        if (aWTEvent.getID() == 201 && bH.I.b()) {
            o();
            if (super.isAlwaysOnTop()) {
                super.setAlwaysOnTop(false);
            }
        } else if (aWTEvent.getID() == 209) {
            if (getExtendedState() == 6) {
                C1798a.a().b("mainWindowMaximized", "true");
                a();
            } else {
                C1798a.a().b("mainWindowMaximized", "false");
                a();
            }
        } else if (aWTEvent.getID() == 200 && super.isAlwaysOnTop()) {
            super.setAlwaysOnTop(false);
        }
        super.processEvent(aWTEvent);
    }

    @Override // aP.aU
    public void a() {
        new C0303ds(this).start();
    }

    public void p() {
        EventQueue.invokeLater(new RunnableC0302dr(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void B() {
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

    @Override // java.awt.Frame
    public void setTitle(String str) {
        String str2;
        String strB = C1798a.f13269c;
        if (!this.f3231h && f3228b) {
            strB = strB.contains(C1806i.f13440b) ? C1806i.f13440b : bH.W.b(strB, C1806i.f13441c, "");
        }
        String str3 = C1798a.f13268b + " " + strB + " v" + C1798a.f13267a + " - ";
        String strTrim = str != null ? bH.W.b(str, str3, "").trim() : "";
        if (this.f3231h) {
            str2 = strTrim + " " + C1818g.b("Registered to:") + " " + C1798a.a().c(C1798a.cC, "Invalid") + " " + C1798a.a().c(C1798a.cD, "Registration");
        } else {
            str2 = strTrim + " EFI Simplified ";
        }
        super.setTitle(str3 + str2);
    }

    @Override // G.S
    public void a(G.R r2) {
        if ("" != 0 && !"".equals("")) {
            setTitle(r2.c() + " ()");
            return;
        }
        if (f3228b) {
            setTitle(r2.c() + " (offline)");
            return;
        }
        String strZ = r2.Z();
        if (strZ == null || strZ.isEmpty()) {
            setTitle(r2.c() + " ( " + C1818g.b("Go Online for Firmware Version") + " )");
        } else {
            setTitle(r2.c() + " ( " + strZ + " )");
        }
    }

    @Override // G.S
    public void b(G.R r2) {
        r2.C().b(this);
    }

    @Override // G.S
    public void c(G.R r2) {
        r2.C().a(this);
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) {
        if (C1798a.f13268b.equals(C1798a.f13337as)) {
            setTitle(str + " ( Firmware Version: " + bSVar.b() + ", Serial #" + bSVar.c() + " )");
            return true;
        }
        if (!bSVar.b().equals(aE.a.A().E().i())) {
            return true;
        }
        setTitle(str + " (" + bSVar.c() + ")");
        if (bSVar.c() != null) {
            aE.a.A().setProperty("firmwareDescription", bSVar.c());
        } else {
            aE.a.A().setProperty("firmwareDescription", "");
        }
        C1798a.a().b(C1798a.cO, bSVar.b());
        return true;
    }

    @Override // G.aG
    public void a(String str) {
    }

    @Override // bH.InterfaceC0994b
    public boolean b() {
        return n();
    }

    public String q() {
        String str = "";
        if (str == null || str.equals("")) {
            try {
                str = BareBonesBrowserLaunch.FILE_PREFIX + new File(".").getCanonicalPath() + "/help/learnMore.html";
            } catch (Exception e2) {
                com.efiAnalytics.ui.bV.d("While online go to\n" + C1798a.f13274h, this);
            }
        }
        return str;
    }

    public void r() {
        com.efiAnalytics.ui.dS dSVar = new com.efiAnalytics.ui.dS(this, new C0305du(this));
        com.efiAnalytics.ui.bV.a((Window) this, (Component) dSVar);
        dSVar.setVisible(true);
    }
}
