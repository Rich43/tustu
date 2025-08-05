package com.efiAnalytics.ui;

import com.sun.glass.ui.Platform;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bV.class */
public class bV {

    /* renamed from: a, reason: collision with root package name */
    static String f10971a = null;

    /* renamed from: b, reason: collision with root package name */
    static JFileChooser f10972b = null;

    /* renamed from: c, reason: collision with root package name */
    static JFileChooser f10973c = null;

    /* renamed from: d, reason: collision with root package name */
    static JFileChooser f10974d = null;

    /* renamed from: e, reason: collision with root package name */
    static JColorChooser f10975e = null;

    /* renamed from: f, reason: collision with root package name */
    static Color f10976f = Color.black;

    /* renamed from: g, reason: collision with root package name */
    static bH.aa f10977g = null;

    /* renamed from: j, reason: collision with root package name */
    private static Window f10978j = null;

    /* renamed from: h, reason: collision with root package name */
    static bU f10979h = null;

    /* renamed from: k, reason: collision with root package name */
    private static File f10980k = null;

    /* renamed from: i, reason: collision with root package name */
    static boolean f10981i = false;

    public static void a(bH.aa aaVar) {
        f10977g = aaVar;
    }

    public static bH.aa a() {
        return f10977g;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(String str) {
        if (f10977g != null) {
            str = f10977g.a(str);
        }
        return str;
    }

    public static JDialog a(Window window, String str) {
        JDialog jDialog = new JDialog(window, b("Please wait"));
        jDialog.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(b("Please wait")));
        jPanel.setLayout(new BorderLayout(10, 10));
        jPanel.add(BorderLayout.CENTER, new JLabel(str, 0));
        JProgressBar jProgressBar = new JProgressBar(0, 100);
        jProgressBar.setIndeterminate(true);
        jPanel.add("South", jProgressBar);
        jDialog.add(BorderLayout.CENTER, jPanel);
        jDialog.pack();
        jDialog.setSize(jDialog.getWidth() + 50, jDialog.getHeight() + 30);
        a(window, (Component) jDialog);
        jDialog.setAlwaysOnTop(true);
        jDialog.setVisible(true);
        return jDialog;
    }

    public static Rectangle a(Rectangle rectangle) throws HeadlessException {
        for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            Rectangle bounds = graphicsDevice.getDefaultConfiguration().getBounds();
            if (bounds.contains(rectangle.f12372x, rectangle.f12373y) || bounds.contains(rectangle.f12372x + rectangle.width, rectangle.f12373y) || bounds.contains(rectangle.f12372x + rectangle.width, rectangle.f12373y + rectangle.height) || bounds.contains(rectangle.f12372x, rectangle.f12373y + rectangle.height)) {
                if (rectangle.f12372x < bounds.f12372x) {
                    rectangle.f12372x = bounds.f12372x;
                }
                if (rectangle.f12373y < bounds.f12373y) {
                    rectangle.f12373y = bounds.f12373y;
                }
                if (rectangle.width > bounds.width) {
                    rectangle.width = bounds.width;
                }
                if (rectangle.height > bounds.height) {
                    rectangle.height = bounds.height;
                }
                if (rectangle.f12372x + rectangle.width > bounds.f12372x + bounds.width) {
                    rectangle.f12372x = (bounds.f12372x + bounds.width) - rectangle.width;
                }
                if (rectangle.f12373y + rectangle.height > bounds.f12373y + bounds.height) {
                    rectangle.f12373y = (bounds.f12373y + bounds.height) - rectangle.height;
                }
                return rectangle;
            }
        }
        if (1 != 0) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (rectangle.f12372x < 0) {
                rectangle.f12372x = 0;
            }
            if (rectangle.f12373y < 0) {
                rectangle.f12373y = 0;
            }
            if (rectangle.f12372x + rectangle.width > screenSize.width) {
                rectangle.f12372x = screenSize.width - rectangle.width;
            }
            if (rectangle.f12373y + rectangle.height > screenSize.height) {
                rectangle.f12373y = screenSize.height - rectangle.height;
            }
        }
        return rectangle;
    }

    public static Rectangle b(Rectangle rectangle) throws HeadlessException {
        if (a(rectangle.f12372x, rectangle.f12373y) && a(rectangle.f12372x + rectangle.width, rectangle.f12373y) && a(rectangle.f12372x, rectangle.f12373y + rectangle.height) && a(rectangle.f12372x + rectangle.width, rectangle.f12373y + rectangle.height)) {
            return rectangle;
        }
        Point pointB = b();
        if (rectangle.f12372x < 0) {
            rectangle.f12372x = 0;
        }
        if (rectangle.f12372x > pointB.f12370x - 100) {
            rectangle.f12372x = pointB.f12370x - 100;
        }
        if (rectangle.f12373y < 0) {
            rectangle.f12373y = 0;
        }
        if (rectangle.f12373y > pointB.f12371y - 100) {
            rectangle.f12373y = pointB.f12371y - 100;
        }
        return rectangle;
    }

    public static boolean a(int i2, int i3) throws HeadlessException {
        for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            if (graphicsDevice.getDefaultConfiguration().getBounds().contains(i2, i3)) {
                return true;
            }
        }
        return false;
    }

    public static Point b() throws HeadlessException {
        int i2 = 0;
        int i3 = 0;
        for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            Rectangle bounds = graphicsDevice.getDefaultConfiguration().getBounds();
            if (bounds.f12372x + bounds.width > i2) {
                i2 = bounds.f12372x + bounds.width;
            }
            if (bounds.f12373y + bounds.height > i3) {
                i3 = bounds.f12373y + bounds.height;
            }
        }
        return new Point(i2, i3);
    }

    public static Window c() {
        return f10978j;
    }

    public static void a(Window window) {
        f10978j = window;
    }

    private bV() {
    }

    public static void a(bU bUVar) {
        f10979h = bUVar;
    }

    public static Frame a(Component component) {
        while (component != null && !(component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }

    public static Window b(Component component) {
        while (component != null && !(component instanceof Window)) {
            component = component.getParent();
        }
        return (Window) component;
    }

    public static String a(Component component, String str, String[] strArr, String str2, String str3) {
        return a(component, str, strArr, str2, str3, false, (AbstractC1600ck) null);
    }

    public static String b(Component component, String str, String[] strArr, String str2, String str3) {
        return a(component, str, strArr, str2, str3, true, (AbstractC1600ck) null);
    }

    public static String a(Component component, String str, String[] strArr, String str2, String str3, AbstractC1600ck abstractC1600ck) {
        return a(component, str, strArr, str2, str3, false, abstractC1600ck);
    }

    public static String b(Component component, String str, String[] strArr, String str2, String str3, AbstractC1600ck abstractC1600ck) {
        return a(component, str, strArr, str2, str3, true, abstractC1600ck);
    }

    public static boolean d() {
        return System.getProperty("os.name", "Windows").startsWith(Platform.WINDOWS);
    }

    public static void a(Window window, Component component) {
        component.setLocation(window.getX() + ((window.getWidth() - component.getWidth()) / 2), window.getY() + ((window.getHeight() - component.getHeight()) / 2));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (component.getLocation().f12370x < 0 || component.getLocation().f12371y < 0 || ((component.getLocation().f12370x + component.getWidth() > screenSize.width && !h()) || component.getLocation().f12371y + component.getHeight() > screenSize.height)) {
            int i2 = component.getLocation().f12370x < 0 ? 0 : component.getLocation().f12370x;
            int width = i2 + component.getWidth() > screenSize.width ? screenSize.width - component.getWidth() : i2;
            int i3 = component.getLocation().f12371y < 0 ? 0 : component.getLocation().f12371y;
            component.setLocation(width, i3 + component.getHeight() > screenSize.height ? screenSize.height - component.getHeight() : i3);
        }
    }

    public static void a(Component component, Component component2) {
        component2.setLocation(component.getLocationOnScreen().f12370x + ((component.getWidth() - component2.getWidth()) / 2), component.getLocationOnScreen().f12371y + ((component.getHeight() - component2.getHeight()) / 2));
        component2.setBounds(a(component2.getBounds()));
    }

    public static void b(Window window) throws HeadlessException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((screenSize.width - window.getWidth()) / 2, (screenSize.height - window.getHeight()) / 2);
    }

    public static String a(Component component, String str, String[] strArr, String str2, String str3, boolean z2) {
        return a(component, str, strArr, str2, str3, z2, (AbstractC1600ck) null);
    }

    public static String a(Component component, String str, String[] strArr, String str2, String str3, boolean z2, AbstractC1600ck abstractC1600ck) {
        String[] strArrA = a(component, str, strArr, str2, str3, z2, abstractC1600ck, false);
        if (strArrA == null || strArrA.length <= 0) {
            return null;
        }
        return strArrA[0];
    }

    public static String[] a(Component component, String str, String[] strArr, String str2, String str3, boolean z2, AbstractC1600ck abstractC1600ck, boolean z3) {
        return a(component, str, strArr, str2, str3, z2, abstractC1600ck, z3, null);
    }

    public static String[] a(Component component, String str, String[] strArr, String str2, String str3, boolean z2, AbstractC1600ck abstractC1600ck, boolean z3, W.ap apVar) {
        String strSubstring = "";
        if (strArr != null) {
            if (strArr.length <= 0 || strArr[0].contains(File.separator)) {
                strSubstring = strArr[0].substring(strArr[0].lastIndexOf(File.separator) + 1);
            } else {
                for (String str4 : strArr) {
                    strSubstring = strSubstring + "*." + str4 + ";";
                }
                if (strSubstring.charAt(strSubstring.length() - 1) == ';') {
                    strSubstring = strSubstring.substring(0, strSubstring.length() - 1);
                }
            }
        }
        if (1 == 0) {
            FileDialog fileDialog = z2 ? new FileDialog(a(component), str, 0) : new FileDialog(a(component), str, 1);
            fileDialog.setTitle(str);
            fileDialog.setFile(strSubstring);
            fileDialog.setDirectory(str3);
            fileDialog.setVisible(true);
            if (fileDialog.getFile() != null) {
                return new String[]{fileDialog.getDirectory() + fileDialog.getFile()};
            }
            return null;
        }
        if (f10972b == null) {
            g();
        } else {
            for (FileFilter fileFilter : f10972b.getChoosableFileFilters()) {
                f10972b.removeChoosableFileFilter(fileFilter);
            }
        }
        f10972b.setDialogTitle(str);
        if (str3 != null) {
            f10972b.setCurrentDirectory(new File(str3));
        }
        if (str2 == null || str2.equals("")) {
            f10972b.setSelectedFile(new File(""));
        } else {
            f10972b.setSelectedFile(new File(str2));
        }
        if (abstractC1600ck != null) {
            f10972b.setAccessory(abstractC1600ck);
            f10972b.addPropertyChangeListener(abstractC1600ck);
        }
        f10972b.setMultiSelectionEnabled(z3);
        if (strArr != null && strArr.length > 0) {
            String str5 = f10979h.l() + " " + b("files");
            if (z2) {
                C1620dd c1620dd = new C1620dd((strArr.length <= 5 || strSubstring.length() <= 30) ? str5 + " (" + strSubstring + ")" : str5 + " (" + strSubstring.substring(0, 30) + "....)");
                for (String str6 : strArr) {
                    c1620dd.b(str6);
                }
                f10972b.addChoosableFileFilter(c1620dd);
                f10972b.setFileFilter(c1620dd);
            } else {
                C1613cx c1613cx = null;
                for (String str7 : strArr) {
                    C1613cx c1613cx2 = new C1613cx(str7);
                    f10972b.addChoosableFileFilter(c1613cx2);
                    if (c1613cx == null) {
                        c1613cx = c1613cx2;
                    }
                }
                f10972b.setFileFilter(c1613cx);
                f10972b.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY, new bW(str2));
            }
        }
        f10972b.setAcceptAllFileFilterUsed(true);
        if (apVar != null) {
            try {
                int i2 = Integer.parseInt(apVar.b("fdWidth", ""));
                int i3 = Integer.parseInt(apVar.b("fdHeight", ""));
                if (i2 > 0 && i3 > 0) {
                    f10972b.setPreferredSize(new Dimension(i2, i3));
                }
            } catch (Exception e2) {
                bH.C.b("No File dialog size saved, using defaults.");
            }
        }
        int iShowOpenDialog = z2 ? f10972b.showOpenDialog(component) : f10972b.showSaveDialog(component);
        if (abstractC1600ck != null) {
            f10972b.setAccessory(null);
            f10972b.removePropertyChangeListener(abstractC1600ck);
        }
        String str8 = ".";
        if (f10972b.getCurrentDirectory() != null && f10972b.getSelectedFile() != null) {
            str8 = f10972b.getCurrentDirectory().getAbsolutePath() + File.separator + f10972b.getSelectedFile().getName();
        }
        if (apVar != null) {
            apVar.a("fdWidth", "" + f10972b.getWidth());
            apVar.a("fdHeight", "" + f10972b.getHeight());
        }
        if (iShowOpenDialog != 0) {
            return null;
        }
        if (f10979h != null && f10979h.n() != null) {
            f10979h.b(f10979h.n(), f10972b.getCurrentDirectory().getAbsolutePath() + File.separator);
        }
        j();
        if (!z3) {
            return new String[]{str8};
        }
        File[] selectedFiles = f10972b.getSelectedFiles();
        String[] strArr2 = new String[selectedFiles.length];
        for (int i4 = 0; i4 < selectedFiles.length; i4++) {
            strArr2[i4] = f10972b.getCurrentDirectory().getAbsolutePath() + File.separator + selectedFiles[i4].getName();
        }
        return strArr2;
    }

    public static String a(String str, Component component) {
        if (f10973c == null) {
            f10973c = new JFileChooser();
            f10973c.setFileSelectionMode(1);
            f10973c.setDialogTitle(b("Select Directory"));
            C1620dd c1620dd = new C1620dd("");
            f10973c.setAcceptAllFileFilterUsed(false);
            f10973c.addChoosableFileFilter(c1620dd);
            if (d()) {
                LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    SwingUtilities.updateComponentTreeUI(f10973c);
                    UIManager.setLookAndFeel(lookAndFeel);
                } catch (Exception e2) {
                    bH.C.c("Unable to set windows look & feel on Directory Chooser Dialog");
                }
            }
        }
        f10973c.setCurrentDirectory(new File(str));
        if (f10973c.showDialog(component, b("Select")) == 0) {
            return f10973c.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public static void e() {
        f10972b = null;
        f10973c = null;
        f10974d = null;
    }

    public static boolean f() {
        return f10974d != null;
    }

    public static void a(FileView fileView) {
        if (f10974d == null) {
            f10974d = new JFileChooser();
            f10974d.setFileSelectionMode(2);
            f10974d.setDialogTitle(b("Open Project"));
            f10974d.addChoosableFileFilter(new C1620dd(""));
            f10974d.setAcceptAllFileFilterUsed(false);
            f10974d.setFileView(fileView);
            f10974d.setFileFilter(new bY());
            f10974d.addPropertyChangeListener(new bZ());
        }
        if (d()) {
            LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(f10974d);
                UIManager.setLookAndFeel(lookAndFeel);
            } catch (Exception e2) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e3) {
                    Logger.getLogger(bV.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    bH.C.c("Unable to set cross platform look & feel also on Project Dialog");
                }
                SwingUtilities.updateComponentTreeUI(f10974d);
                bH.C.c("Unable to set windows look & feel on Project Dialog");
            }
        }
    }

    public static String a(String str, Component component, String str2, AbstractC1600ck abstractC1600ck) {
        f10974d.setCurrentDirectory(new File(str));
        f10974d.setAccessory(abstractC1600ck);
        if (str2 != null) {
            f10974d.setDialogTitle(b(str2));
        } else {
            f10974d.setDialogTitle(b("Open Project"));
        }
        if (f10974d.showDialog(component, b("Select")) == 0) {
            return f10974d.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public static String b(String str, Component component) {
        return a(str, component, (String) null, (AbstractC1600ck) null);
    }

    private static void j() {
        if (f10980k != null) {
            File file = new File(f10980k, "fileChooser.ser");
            if (file.exists()) {
                file.delete();
            }
            try {
                new X.d().a(f10972b, file);
            } catch (IOException e2) {
                bH.C.c("Error saving cached FileChooser: " + e2.getLocalizedMessage());
                e2.printStackTrace();
            }
        }
    }

    public static void g() {
        if (f10972b == null) {
            if (f10980k != null) {
                File file = new File(f10980k, "fileChooser.ser");
                if (file.exists()) {
                    try {
                        f10972b = (JFileChooser) new X.d().a(file);
                    } catch (IOException | ClassNotFoundException e2) {
                        bH.C.c("No cached FileChooser found.");
                    }
                }
            }
            if (f10972b == null) {
                f10972b = new JFileChooser();
                LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
                if (d() && lookAndFeel.getName().equals("Metal")) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        SwingUtilities.updateComponentTreeUI(f10972b);
                        UIManager.setLookAndFeel(lookAndFeel);
                    } catch (Exception e3) {
                        bH.C.c("Unable to set windows look & feel on File Chooser Dialog");
                    }
                }
            }
            k();
        }
    }

    public static void c(String str, Component component) throws HeadlessException {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new RunnableC1590ca(str, component));
            return;
        }
        JDialog jDialogCreateDialog = new JOptionPane(str, 3).createDialog(component, b("Information"));
        jDialogCreateDialog.setModal(false);
        jDialogCreateDialog.setVisible(true);
    }

    public static void d(String str, Component component) {
        if (SwingUtilities.isEventDispatchThread()) {
            JOptionPane.showMessageDialog(component, str);
            return;
        }
        try {
            SwingUtilities.invokeAndWait(new RunnableC1591cb(component, str));
        } catch (InterruptedException e2) {
            Logger.getLogger(bV.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (InvocationTargetException e3) {
            Logger.getLogger(bV.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    public static boolean a(String str, Component component, boolean z2) {
        if (z2) {
            return JOptionPane.showConfirmDialog(component, str, "", 0) == 0;
        }
        d(str, component);
        return true;
    }

    public static boolean b(String str, Component component, boolean z2) {
        if (z2) {
            return JOptionPane.showConfirmDialog(component, str, "", 0, 2) == 0;
        }
        d(str, component);
        return true;
    }

    public static boolean a(String str, String str2, Component component, String[] strArr) {
        return JOptionPane.showOptionDialog(component, str, str2, 0, 3, null, strArr, strArr[0]) == 0;
    }

    public static int b(String str, String str2, Component component, String[] strArr) {
        return JOptionPane.showOptionDialog(component, str, str2, strArr.length == 3 ? 1 : 0, 3, null, strArr, strArr[0]);
    }

    public static String a(Component component, boolean z2, String str, String str2) {
        C1686fq c1686fq = new C1686fq(b(component), z2, str, str2, f10977g);
        c1686fq.setVisible(true);
        component.requestFocus();
        String strA = c1686fq.a();
        c1686fq.dispose();
        return strA;
    }

    public static String a(String str, boolean z2, Component component) {
        return a(str, z2, (String) null, false, component);
    }

    public static String a(String str, boolean z2, String str2, boolean z3, Component component) {
        C1688fs c1688fs = new C1688fs(a(component), str, z2, str2, z3);
        component.requestFocus();
        c1688fs.dispose();
        return c1688fs.a();
    }

    public static String a(String str, boolean z2, String str2, boolean z3, Component component, String[] strArr) {
        C1688fs c1688fs = new C1688fs(a(component), str, z2, str2, z3, strArr);
        component.requestFocus();
        c1688fs.dispose();
        return c1688fs.a();
    }

    public static String a(String str, boolean z2, String str2, boolean z3, Component component, fw fwVar, fx fxVar) {
        return a(str, z2, str2, z3, component, fwVar, fxVar, (bH.aa) null);
    }

    public static String a(String str, boolean z2, String str2, boolean z3, Component component, fw fwVar, fx fxVar, bH.aa aaVar) {
        C1688fs c1688fs = new C1688fs(a(component), str, z2, str2, z3, fwVar, fxVar, aaVar);
        component.requestFocus();
        c1688fs.dispose();
        return c1688fs.a();
    }

    public static Color a(Component component, String str, Color color) throws HeadlessException {
        if (f10975e == null) {
            f10975e = new JColorChooser();
        }
        f10975e.setColor(color);
        f10976f = color;
        JDialog jDialogCreateDialog = JColorChooser.createDialog(component, str, true, f10975e, new C1592cc(), new C1593cd());
        Point mousePosition = component.getMousePosition();
        if (mousePosition != null) {
            Point locationOnScreen = component.getLocationOnScreen();
            jDialogCreateDialog.setLocation((mousePosition.f12370x + locationOnScreen.f12370x) - (jDialogCreateDialog.getWidth() / 2), (mousePosition.f12371y + locationOnScreen.f12371y) - (jDialogCreateDialog.getHeight() / 2));
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (jDialogCreateDialog.getLocation().f12370x < 0 || jDialogCreateDialog.getLocation().f12371y < 0 || jDialogCreateDialog.getLocation().f12370x + jDialogCreateDialog.getWidth() > screenSize.width || jDialogCreateDialog.getLocation().f12371y + jDialogCreateDialog.getHeight() > screenSize.height) {
                int i2 = jDialogCreateDialog.getLocation().f12370x < 0 ? 0 : jDialogCreateDialog.getLocation().f12370x;
                int width = i2 + jDialogCreateDialog.getWidth() > screenSize.width ? screenSize.width - jDialogCreateDialog.getWidth() : i2;
                int i3 = jDialogCreateDialog.getLocation().f12371y < 0 ? 0 : jDialogCreateDialog.getLocation().f12371y;
                jDialogCreateDialog.setLocation(width, i3 + jDialogCreateDialog.getHeight() > screenSize.height ? screenSize.height - jDialogCreateDialog.getHeight() : i3);
            }
        }
        jDialogCreateDialog.setVisible(true);
        return f10975e.getColor();
    }

    public static JDialog a(Component component, Component component2, String str, InterfaceC1565bc interfaceC1565bc) {
        JDialog jDialogB = b(component, component2, b(str), interfaceC1565bc);
        jDialogB.pack();
        if (component2 != null) {
            a(b(component2), (Component) jDialogB);
        }
        jDialogB.setVisible(true);
        return jDialogB;
    }

    public static JDialog b(Component component, Component component2, String str, InterfaceC1565bc interfaceC1565bc) {
        return a(component, component2, str, interfaceC1565bc, "Close");
    }

    public static JDialog a(Component component, Component component2, String str, InterfaceC1565bc interfaceC1565bc, String str2) {
        Window windowB = b(component2);
        if (windowB != null && !(windowB instanceof Frame) && !(windowB instanceof Dialog)) {
            windowB = null;
        }
        JDialog jDialog = new JDialog(windowB, str);
        jDialog.add(BorderLayout.CENTER, component);
        JButton jButton = new JButton(b(str2));
        jButton.addActionListener(new C1594ce());
        if (interfaceC1565bc != null) {
            jButton.addActionListener(new C1564bb(interfaceC1565bc));
        }
        jDialog.enableInputMethods(true);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        jPanel.add(jButton);
        jDialog.add("South", jPanel);
        return jDialog;
    }

    public static void a(Container container, boolean z2) {
        for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
            Component component = container.getComponent(i2);
            component.setEnabled(z2);
            if (component instanceof Container) {
                a((Container) component, z2);
            }
        }
    }

    public static boolean h() throws HeadlessException {
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        return screenDevices != null && screenDevices.length > 1;
    }

    public static String a(Component component, String str) {
        Window windowB = b(component);
        JPanel jPanel = new JPanel();
        JLabel jLabel = new JLabel(str);
        JPasswordField jPasswordField = new JPasswordField(16);
        jPanel.add(jLabel);
        jPanel.add(jPasswordField);
        new C1595cf(jPasswordField).start();
        String[] strArr = {"OK", "Cancel"};
        if (JOptionPane.showOptionDialog(windowB, jPanel, "Password", 1, -1, null, strArr, strArr[0]) == 0) {
            return new String(jPasswordField.getPassword());
        }
        return null;
    }

    private static void k() {
        f10972b.getActionMap().put("delAction", new bX());
        f10972b.getInputMap(2).put(KeyStroke.getKeyStroke("DELETE"), "delAction");
    }

    public static int i() {
        return JOptionPane.showConfirmDialog(f10972b, b("Are you sure want to delete this file?"), b("Confirm"), 0);
    }
}
