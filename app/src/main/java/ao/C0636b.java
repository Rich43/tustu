package ao;

import W.C0184j;
import W.C0188n;
import W.C0189o;
import aq.C0833a;
import bu.C1368a;
import bx.C1377c;
import g.C1733k;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.icepdf.core.util.PdfOps;

/* renamed from: ao.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/b.class */
public class C0636b {

    /* renamed from: a, reason: collision with root package name */
    private static C0636b f5305a = null;

    /* renamed from: b, reason: collision with root package name */
    private HashMap f5306b = new HashMap();

    private C0636b() {
    }

    public static C0636b a() {
        if (f5305a == null) {
            f5305a = new C0636b();
        }
        return f5305a;
    }

    public void a(Window window) {
        new aM.a(window, null).a(h.h.a());
    }

    public void b(Window window) {
        C1377c c1377c = new C1377c(C0595M.a(), null);
        c1377c.a(new C0663c(this));
        hz.a(c1377c.d(), "control SPACE");
        c1377c.a(window);
    }

    public void c(Window window) {
        new bB.l(null, new dY()).a(window);
    }

    public void a(Frame frame) {
        new C0833a(new com.efiAnalytics.ui.dQ(h.i.f(), "")).a(frame);
    }

    public void a(C0188n c0188n, File file, Window window) {
        String str;
        C0188n c0188n2 = new C0188n();
        c0188n2.d(c0188n.g());
        c0188n2.a(c0188n.a());
        c0188n2.b(c0188n.b());
        Iterator it = c0188n.iterator();
        while (it.hasNext()) {
            c0188n2.add((C0184j) it.next());
        }
        if (c0188n.h()) {
            for (String str2 : c0188n.i()) {
                c0188n2.a(str2, c0188n.f(str2));
            }
        }
        File parentFile = file.getParentFile();
        String strL = (c0188n.l() == null || c0188n.l().isEmpty()) ? h.i.f12275v : c0188n.l();
        String str3 = strL.equals(",") ? "csv" : "msl";
        if (file.getName().toLowerCase().endsWith(str3)) {
            try {
                str = file.getName().substring(0, file.getName().lastIndexOf(".")) + "_modified." + str3;
            } catch (Exception e2) {
                str = "modifiedLog." + str3;
            }
        } else {
            try {
                str = file.getName().substring(0, file.getName().lastIndexOf(".") + 1) + str3;
            } catch (Exception e3) {
                str = "ExportLog." + str3;
            }
        }
        String strA = com.efiAnalytics.ui.bV.a(C0645bi.a().b(), "Export Log Data", C1733k.a(str3, ";"), str, parentFile.getAbsolutePath());
        if (strA == null || strA.equals("")) {
            return;
        }
        C0189o c0189oA = null;
        try {
            c0189oA = C0189o.a(c0188n2, strA, strL);
        } catch (IOException e4) {
            com.efiAnalytics.ui.bV.d("Error Saving Log File:\n" + e4.getMessage(), C0645bi.a().b());
            e4.printStackTrace();
        }
        c0189oA.a(new C0690d(this, window));
        new C0770g(this, c0189oA, c0188n2).start();
    }

    public void a(String str) {
        JRootPane rootPane = C0645bi.a().b().getRootPane();
        if (!(rootPane.getGlassPane() instanceof com.efiAnalytics.ui.dO)) {
            com.efiAnalytics.ui.dO dOVar = new com.efiAnalytics.ui.dO();
            dOVar.b(true);
            rootPane.setGlassPane(dOVar);
        }
        com.efiAnalytics.ui.dO dOVar2 = (com.efiAnalytics.ui.dO) rootPane.getGlassPane();
        if (str != null) {
            dOVar2.a(str);
        }
        dOVar2.b();
        dOVar2.setVisible(true);
    }

    public void a(double d2) {
        JRootPane rootPane = C0645bi.a().b().getRootPane();
        if (rootPane.getGlassPane() instanceof com.efiAnalytics.ui.dO) {
            ((com.efiAnalytics.ui.dO) rootPane.getGlassPane()).a(d2);
        } else {
            bH.C.d("updateModalPercent called, but Root Pane is not a ProgressPane.");
        }
    }

    public void b(String str) {
        JRootPane rootPane = C0645bi.a().b().getRootPane();
        if (rootPane.getGlassPane() instanceof com.efiAnalytics.ui.dO) {
            ((com.efiAnalytics.ui.dO) rootPane.getGlassPane()).a(str);
        } else {
            bH.C.d("updateModalPercent called, but Root Pane is not a ProgressPane.");
        }
    }

    public void b(Frame frame) {
        String str = h.i.f12265l;
        h.i.g();
        try {
            if (bH.I.a()) {
                Runtime.getRuntime().exec("javaw.exe Staging \"" + ("javaw.exe -cp .;./plugins/;lib;./lib/*.jar -Djava.library.path=lib -jar " + str) + PdfOps.DOUBLE_QUOTE__TOKEN);
            } else if (!bH.I.d() && bH.I.b()) {
                Runtime.getRuntime().exec(new String[]{"./runtime/bin/java", "Staging", "./runtime/bin/java -cp .:./plugins/:lib:./lib/*.jar -Djava.library.path=lib -jar " + str});
            } else {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/java", "Staging", "/usr/bin/java -cp .:./plugins/:lib:./lib/*.jar -Djava.library.path=lib -jar " + str});
            }
            C0645bi.a().b().l();
        } catch (IOException e2) {
            bH.C.a("Failed to restart Application!", e2, frame);
        }
    }

    public void b() {
        boolean z2 = bH.I.a() && UIManager.getLookAndFeel().isNativeLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (UnsupportedLookAndFeelException e2) {
            Logger.getLogger(C0636b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        Font font = UIManager.getFont("Label.font");
        if (font != null && font.getSize() < 14) {
            com.efiAnalytics.ui.eJ.c(font.getSize());
        }
        float f2 = Integer.parseInt(h.i.e("prefFontSize", com.efiAnalytics.ui.eJ.a() + "")) / com.efiAnalytics.ui.eJ.a();
        Set<Object> setKeySet = UIManager.getLookAndFeelDefaults().keySet();
        Object[] array = setKeySet.toArray(new Object[setKeySet.size()]);
        Object[] objArr = new Object[array.length + 1];
        System.arraycopy(array, 0, objArr, 0, array.length);
        objArr[objArr.length - 1] = "defaultFont";
        Font font2 = UIManager.getFont("defaultFont");
        for (Object obj : objArr) {
            if (obj != null && obj.toString().toLowerCase().contains("font")) {
                Font font3 = UIManager.getFont(obj);
                if (font3 != null) {
                    Float fValueOf = (Float) this.f5306b.get(obj);
                    if (fValueOf == null) {
                        this.f5306b.put(obj, Float.valueOf(font3.getSize2D()));
                        fValueOf = Float.valueOf(font3.getSize2D());
                    }
                    UIManager.put(obj, new Font(font3.getFamily(), font3.getStyle(), Math.round(com.efiAnalytics.ui.eJ.a(fValueOf.floatValue() * f2))));
                } else if (font3 == null) {
                    bH.C.c("no update:" + obj);
                }
            } else if (obj != null && obj.toString().equals("ScrollBar.width")) {
                System.out.println(obj);
                if (UIManager.getInt(obj) < 20) {
                    UIManager.put(obj, Integer.valueOf(com.efiAnalytics.ui.eJ.a(UIManager.getInt(obj))));
                }
            }
        }
        if (font2 != null) {
            UIManager.getLookAndFeel().getDefaults().put("defaultFont", new Font(font2.getFamily(), font2.getStyle(), com.efiAnalytics.ui.eJ.a(12)));
        }
        if (C0645bi.a().b() != null) {
            SwingUtilities.updateComponentTreeUI(C0645bi.a().b());
            com.efiAnalytics.ui.bV.e();
        }
    }

    public bC.k d(Window window) {
        return a(window, null);
    }

    public bC.k a(Window window, String str) {
        bC.k kVar = new bC.k(null);
        kVar.a(Z.b.a().c());
        kVar.a(new RunnableC0716e(this));
        if (str != null) {
            kVar.a(str);
        }
        kVar.a(window);
        return kVar;
    }

    public void a(bP bPVar) {
        String strF = C0804hg.a().r().f();
        C0646bj c0646bj = new C0646bj(bPVar);
        c0646bj.a(strF);
        c0646bj.setSize(com.efiAnalytics.ui.eJ.a(800), com.efiAnalytics.ui.eJ.a(600));
        com.efiAnalytics.ui.bV.a((Window) bPVar, (Component) c0646bj);
        c0646bj.setVisible(true);
    }

    public void e(Window window) throws IllegalArgumentException {
        com.efiAnalytics.dialogs.b bVar = new com.efiAnalytics.dialogs.b(window, "Timeslip Editor", null);
        C0188n c0188nR = C0804hg.a().r();
        bVar.a(C1368a.a(c0188nR));
        bVar.a(new C0743f(this, bVar, c0188nR));
        bVar.pack();
        com.efiAnalytics.ui.bV.a(window, (Component) bVar);
        bVar.setVisible(true);
    }
}
