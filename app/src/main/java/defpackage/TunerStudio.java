package defpackage;

import A.v;
import V.a;
import aP.C0254bw;
import aP.C0293dh;
import aP.C0338f;
import bH.C;
import bH.C1000h;
import bH.C1011s;
import bH.I;
import com.efiAnalytics.ui.eJ;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import java.awt.Insets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:TunerStudio.class */
public class TunerStudio {

    /* renamed from: a, reason: collision with root package name */
    static C0293dh f1881a = null;

    /* renamed from: b, reason: collision with root package name */
    static String f1882b = "AppDebug";

    /* renamed from: c, reason: collision with root package name */
    static boolean f1883c = false;

    /* renamed from: d, reason: collision with root package name */
    static String f1884d = "";

    public static void main(String[] strArr) {
        String name;
        name = "TunerStudioMS.jar";
        int i2 = 2023;
        try {
            File file = new File(new TunerStudio().getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            name = file.getName().equals("classes") ? "TunerStudioMS.jar" : file.getName();
            if (file != null) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(new Date(file.lastModified()));
                i2 = gregorianCalendar.get(1);
            }
            System.out.println("Jar Name:" + name);
        } catch (URISyntaxException e2) {
            e2.printStackTrace();
        }
        if (I.f()) {
            a();
        }
        new C0254bw().a(strArr, name);
        boolean z2 = true;
        if (strArr.length > 0 && strArr[0].equals("-noSplash")) {
            System.out.println("Splashscreen suppressed.");
            z2 = false;
            strArr[0] = "";
        }
        boolean z3 = z2;
        if (strArr.length > 1) {
            strArr[0] = strArr[1];
        }
        C1798a.cw = C1798a.a().a("tuneFileExt", "msq");
        C1798a.f13328aj = i2;
        boolean zEquals = C1798a.a().a(TransformerFactoryImpl.DEBUG, "false").equals("true");
        if (C1798a.a().a(TransformerFactoryImpl.DEBUG, "false").equals("off")) {
            C.a(false);
        } else if (!zEquals) {
            f1882b = C1798a.f13268b + "AppDebug.txt";
            try {
                f1882b = C1807j.t() + File.separator + f1882b;
                File file2 = new File(f1882b);
                if (file2.exists() && file2.length() > 5000000) {
                    f1883c = !file2.delete();
                }
            } catch (Exception e3) {
                System.out.println("Error deleting log file Output");
            }
            try {
                C1798a.a().a(new File(f1882b));
                PrintStream printStream = new PrintStream(new FileOutputStream(f1882b, !f1883c));
                System.setOut(printStream);
                System.setErr(printStream);
            } catch (Exception e4) {
                System.out.println("Error creating log file Output");
            }
        }
        if (!f1884d.isEmpty()) {
            System.out.println(f1884d);
        }
        System.out.println("Starting on " + v.b());
        System.out.println(C1798a.f13268b + " " + C1798a.f13267a + " started on " + new Date().toString());
        System.out.println("JRE " + System.getProperty("java.version") + ", " + System.getProperty("os.name") + " " + System.getProperty("os.version") + ", " + System.getProperty("os.arch"));
        System.out.println("java.library.path=" + System.getProperty("java.library.path"));
        System.out.println(System.getProperty("java.class.path"));
        try {
            System.setProperty("file.encoding", "UTF-8");
            Field declaredField = Charset.class.getDeclaredField("defaultCharset");
            declaredField.setAccessible(true);
            declaredField.set(null, null);
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        if (C1798a.a().c(C1798a.f13298F, C1798a.f13299G)) {
            System.setProperty("sun.java2d.opengl", "true");
        } else if (C1798a.a().c(C1798a.f13300H, C1798a.f13301I)) {
            System.setProperty("sun.java2d.d3d", "false");
        }
        try {
            UIManager.installLookAndFeel("Night Shade", "de.muntjak.tinylookandfeel.TinyLookAndFeel");
            UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
            for (int i3 = 0; i3 < installedLookAndFeels.length; i3++) {
                System.out.println("Look:" + installedLookAndFeels[i3].getName() + ", ClassName:'" + installedLookAndFeels[i3].getClassName() + PdfOps.SINGLE_QUOTE_TOKEN);
            }
            try {
                System.setProperty("apple.laf.useScreenMenuBar", "false");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", C1798a.a().b());
            } catch (Exception e6) {
                System.out.println("Exception while setting OS X properties: " + e6.getMessage());
            }
            String strA = C1798a.a().a("defaultLookAndFeelClass", UIManager.getCrossPlatformLookAndFeelClassName());
            if (strA.equals("NATIVE")) {
                strA = UIManager.getSystemLookAndFeelClassName();
            }
            String strC = C1798a.a().c("lookAndFeelClass", strA);
            System.out.println("Setting Look & Feel to:" + strC);
            UIManager.put("FileChooser.useSystemExtensionHiding", false);
            boolean z4 = strC.equals("de.muntjak.tinylookandfeel.TinyLookAndFeel") && !eJ.b();
            if (strC.equals("de.muntjak.tinylookandfeel.TinyLookAndFeel")) {
                C1798a.f13415bS = true;
            }
            UIManager.setLookAndFeel(strC);
            if (strC.contains("Nimbus")) {
                UIDefaults defaults = UIManager.getLookAndFeel().getDefaults();
                defaults.put("Table.cellNoFocusBorder", new Insets(0, 0, 0, 0));
                defaults.put("Table.focusCellHighlightBorder", new Insets(0, 0, 0, 0));
                UIManager.getLookAndFeelDefaults().put("Table.cellNoFocusBorder", new Insets(0, 0, 0, 0));
                UIManager.getLookAndFeelDefaults().put("Table.focusCellHighlightBorder", new Insets(0, 0, 0, 0));
            }
            C0338f.a().x();
        } catch (Exception e7) {
            System.out.println("Couldn't set UI");
            e7.printStackTrace();
        }
        if (z3) {
            try {
                SwingUtilities.invokeAndWait(new f());
                if (!I.a()) {
                    Thread.sleep(200L);
                }
                if (I.e()) {
                    Thread.sleep(400L);
                }
            } catch (InterruptedException e8) {
                Logger.getLogger(TunerStudio.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
            } catch (InvocationTargetException e9) {
                Logger.getLogger(TunerStudio.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e9);
            }
        }
        g gVar = new g(z3, strArr);
        C1000h.b().a();
        try {
            SwingUtilities.invokeAndWait(gVar);
        } catch (InterruptedException e10) {
            Logger.getLogger(TunerStudio.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e10);
        } catch (InvocationTargetException e11) {
            Logger.getLogger(TunerStudio.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e11);
        }
        Runtime.getRuntime().addShutdownHook(new h());
    }

    private static void a() {
        File file = new File("./lib/jssc2.8.jar");
        File file2 = new File("./lib/jssc.jar");
        if (!file.exists() || file.length() <= 0 || file.length() == file2.length()) {
            f1884d += "JSSC library appears to be set to the 2.8 version for XP support.\n";
            return;
        }
        f1884d += "Updating JSSC Driver to 2.8 versoin for XP compatability.\n";
        if (!file2.delete()) {
            f1884d += "Unable to delete existing jssc.jar file, try deleting it manually.\n";
            return;
        }
        try {
            C1011s.a(file, file2);
            f1884d += "Successfully changed JSSC Library to 2.8 for XP compatability.\n";
        } catch (a e2) {
            f1884d += "Failed to copy jssc2.8.jar to jssc.jar, try it manually.\n";
        }
    }
}
