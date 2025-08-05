package defpackage;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.Platform;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javafx.fxml.FXMLLoader;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/* JADX WARN: Classes with same name are omitted:
  Staging.class
 */
/* loaded from: TunerStudioMS.jar:Staging.class */
public class Staging implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    JTextPane f1845a = new JTextPane();

    /* renamed from: b, reason: collision with root package name */
    JLabel f1846b = new JLabel("Waiting for Application to Exit.");

    /* renamed from: c, reason: collision with root package name */
    JButton f1847c = new JButton("Cancel");

    /* renamed from: k, reason: collision with root package name */
    private HashMap f1855k = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    public static String f1848d = i();

    /* renamed from: f, reason: collision with root package name */
    private static String f1849f = "File Still Locked, waiting.";

    /* renamed from: e, reason: collision with root package name */
    public static String f1850e = "Successful";

    /* renamed from: g, reason: collision with root package name */
    private static String f1851g = "<html><body><center><h1>Completing EFI Analytics Update</h1></center>Please wait while updated files are extracted. <br>This may take a moment. <br><br>Please wait....<br></body></html>";

    /* renamed from: h, reason: collision with root package name */
    private static String f1852h = "<html><body><center><h1>Completing EFI Analytics Update</h1></center>If You see a message of locked files, <br>make sure all instances of " + f1848d + " are closed. <br><br>Please wait....<br></body></html>";

    /* renamed from: i, reason: collision with root package name */
    private static String f1853i = "<html><body><center><h1>Completing EFI Analytics Update</h1></center>Restarting " + f1848d + ", <br><br>Please wait....<br></body></html>";

    /* renamed from: j, reason: collision with root package name */
    private static String f1854j = "DIE";

    /* renamed from: l, reason: collision with root package name */
    private static float f1856l = -1.0f;

    public Staging() {
        d();
    }

    public static void main(String[] strArr) throws HeadlessException, IllegalArgumentException {
        String strB;
        try {
            PrintStream printStream = new PrintStream(new FileOutputStream(new File(".", "Staging.log"), false));
            System.setOut(printStream);
            System.setErr(printStream);
        } catch (Exception e2) {
            System.out.println("Error creating log file Output");
        }
        System.out.println("Updated: " + new Date().toString());
        System.out.println("Completing Installation, args: ");
        if (strArr == null) {
            System.out.println(FXMLLoader.NULL_KEYWORD);
        } else {
            System.out.println("args len: " + strArr.length);
            for (String str : strArr) {
                System.out.println(str);
            }
        }
        Staging staging = new Staging();
        JFrame jFrameF = null;
        int i2 = 0;
        File file = new File(".");
        String[] list = file.list();
        for (int i3 = 0; i3 < list.length; i3++) {
            if (list[i3].endsWith("temp") || list[i3].toLowerCase().endsWith(".zip")) {
                jFrameF = staging.f();
                break;
            }
        }
        boolean z2 = false;
        int i4 = 0;
        while (true) {
            if (i4 >= list.length) {
                break;
            }
            if (list[i4].toLowerCase().endsWith(".zip")) {
                z2 = true;
                break;
            }
            i4++;
        }
        if (z2 && !a()) {
            System.out.println("zip files detected on non windows");
            try {
                Runtime.getRuntime().exec(new String[]{"bash", "-c", "chmod -R 777 *"}).waitFor();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        h();
        try {
            Thread.sleep(2000L);
        } catch (Exception e4) {
        }
        String[] list2 = file.list();
        for (int i5 = 0; i5 < list2.length; i5++) {
            if (list2[i5].toLowerCase().endsWith(".zip")) {
                System.out.println("Finishing installation, expanding: " + list2[i5]);
                staging.f1846b.setText("Updating files in: " + list2[i5].substring(0, list2[i5].length() - 4));
                try {
                    if (a(list2[i5], list2[i5].replace(".zip", ""), (String) null).equals(f1850e)) {
                        new File(list2[i5]).delete();
                    }
                } catch (ZipException e5) {
                    staging.f1846b.setText("ERROR While Extracting: " + list2[i5]);
                    e5.printStackTrace();
                } catch (IOException e6) {
                    staging.f1846b.setText("ERROR While Extracting: " + list2[i5]);
                    e6.printStackTrace();
                }
            }
        }
        staging.f1845a.setText(f1852h);
        while (true) {
            int i6 = i2;
            i2++;
            if (i6 < 0) {
                break;
            }
            String[] list3 = file.list();
            boolean z3 = true;
            for (int i7 = 0; i7 < list3.length; i7++) {
                System.out.print(FXMLLoader.CONTROLLER_METHOD_PREFIX);
                if (list3[i7].endsWith("temp")) {
                    String str2 = list3[i7];
                    String strSubstring = str2.substring(0, str2.indexOf(".temp"));
                    File file2 = new File(strSubstring);
                    File file3 = new File(str2);
                    if (!file3.exists()) {
                        staging.f1846b.setText("Updated File: " + file2.getName());
                        System.out.println("Updated File: " + file2.getName());
                    } else if (file2.exists() && !file2.delete()) {
                        f1849f += ".";
                        staging.f1846b.setText(f1849f);
                        z3 = false;
                    } else if (file3.renameTo(new File(strSubstring))) {
                        staging.f1846b.setText("Updated File: " + file2.getName());
                    } else {
                        System.out.println("Error completing installation. Unable to rename new file to " + file2.getName());
                        staging.f1846b.setText("Unable to rename new file to " + file2.getName());
                        z3 = false;
                    }
                }
            }
            if (i2 == 15) {
                staging.f1847c.setEnabled(true);
            }
            h();
            if (z3) {
                staging.f1846b.setText("Completed File Updates, Restarting Application.");
                System.out.println("Completed File Updates, Restarting Application.");
                break;
            }
            try {
                Thread.sleep(2000L);
            } catch (Exception e7) {
            }
        }
        if (strArr != null && strArr.length > 0 && (strB = b(strArr[0], "(Beta)", "")) != null && strB.length() > 0) {
            try {
                staging.f1845a.setText(f1853i);
                File absoluteFile = new File(".").getAbsoluteFile();
                System.out.println("Setting launch dir to:\n" + absoluteFile.getAbsolutePath());
                System.out.println("Launching app:\n" + strB);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(strB, (String[]) null, absoluteFile).getInputStream()));
                try {
                    Thread.sleep(2000L);
                    jFrameF.setVisible(false);
                } catch (Exception e8) {
                }
                if (jFrameF != null) {
                    jFrameF.dispose();
                }
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        System.out.println(line);
                    }
                }
                bufferedReader.close();
            } catch (Exception e9) {
                e9.printStackTrace();
            }
        }
        if (jFrameF != null) {
            jFrameF.dispose();
        }
    }

    private JFrame f() throws HeadlessException {
        JFrame jFrame = new JFrame("Completing Auto Update");
        jFrame.setLayout(new BorderLayout(8, 8));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        this.f1845a.setEditable(false);
        this.f1845a.setOpaque(false);
        this.f1845a.setContentType(Clipboard.HTML_TYPE);
        this.f1845a.setText(f1851g);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("North", this.f1846b);
        JProgressBar jProgressBar = new JProgressBar(0, 100);
        jProgressBar.setIndeterminate(true);
        jProgressBar.setStringPainted(false);
        jPanel2.add(BorderLayout.CENTER, jProgressBar);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        jPanel3.add(this.f1847c);
        jPanel2.add("South", jPanel3);
        this.f1847c.setEnabled(false);
        this.f1847c.addActionListener(this);
        jPanel.add("South", jPanel2);
        jPanel.add(BorderLayout.CENTER, this.f1845a);
        jFrame.add(BorderLayout.CENTER, jPanel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int i2 = (screenSize.width - 400) / 2;
        int i3 = (screenSize.height - 300) / 2;
        jFrame.setSize(400, 300);
        jFrame.setLocation(i2, i3);
        jFrame.setVisible(true);
        jFrame.setAlwaysOnTop(true);
        return jFrame;
    }

    private void g() throws HeadlessException {
        JOptionPane.showMessageDialog(this.f1847c, "<html><body><center><h1>Update Cancelled</h1></center>It is taking an excessively long time to update all files.<br><br>Some files appear to be locked, this usually indicates<br> there is still and instance of " + f1848d + " running.<br>Make sure all instances of " + f1848d + " are closed, <br>reboot your computer if nessecary.<br><br>Then Run " + f1848d + " as Administrator.<br>The update will then be completed automatically.</body></html>");
        System.exit(0);
    }

    private static String b(String str, String str2, String str3) {
        int iIndexOf = str.indexOf(str2);
        while (true) {
            int i2 = iIndexOf;
            if (i2 < 0) {
                return str;
            }
            str = str.substring(0, i2) + str3 + str.substring(i2 + str2.length());
            iIndexOf = str.indexOf(str2);
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        if (actionEvent.getSource().equals(this.f1847c)) {
            g();
        }
    }

    public static boolean a() {
        return System.getProperty("os.name", "Windows").startsWith(Platform.WINDOWS);
    }

    public static String a(String str, String str2, String str3) throws IOException {
        ZipFile zipFile = new ZipFile(str);
        Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
        while (enumerationEntries.hasMoreElements()) {
            ZipEntry zipEntryNextElement2 = enumerationEntries.nextElement2();
            String name = zipEntryNextElement2.getName();
            if (name.length() > 4) {
                name = name.substring(name.length() - 4, name.length()).toLowerCase();
            }
            if (str3 == null || zipEntryNextElement2.isDirectory() || name.equals(".jpg") || name.equals("jpeg") || name.equals(".gif")) {
                String strA = a(zipFile, zipEntryNextElement2, str2);
                if (!strA.equals(f1850e)) {
                    return strA;
                }
            }
        }
        zipFile.close();
        return f1850e;
    }

    public static String a(ZipFile zipFile, ZipEntry zipEntry, String str) throws IOException {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        File file = new File(str + "/" + zipEntry.getName());
        String absolutePath = file.getAbsolutePath();
        new File(absolutePath.substring(0, absolutePath.lastIndexOf(File.separator))).mkdirs();
        file.setLastModified(zipEntry.getTime());
        if (zipEntry.isDirectory()) {
            file.mkdir();
        } else {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            while (true) {
                int i2 = inputStream.read();
                if (i2 == -1) {
                    break;
                }
                bufferedOutputStream.write(i2);
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }
        inputStream.close();
        return f1850e;
    }

    private static void h() {
        try {
            File file = new File(b(), f1848d + ".pipe");
            file.delete();
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(f1854j.getBytes());
            fileOutputStream.close();
            file.deleteOnExit();
        } catch (IOException e2) {
            Logger.getLogger(Staging.class.getName()).log(Level.WARNING, "Unable to create instance monitor file", (Throwable) e2);
        }
    }

    public static File b() {
        File file = new File(c() + ".efiAnalytics" + File.separator + f1848d + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String c() {
        return System.getProperty("user.home") + File.separator;
    }

    private static String i() {
        return new File("BigComm.properties").exists() ? "BigComm" : new File("FuelMonster.properties").exists() ? "Fuel Monster" : new File("TuneMonster.properties").exists() ? "Tune Monster" : new File("BigCommGen4.properties").exists() ? "BigComm Gen4" : new File("TunerStudio.properties").exists() ? "TunerStudio" : new File("TSDash.properties").exists() ? "TS Dash" : (new File("MegaLogViewer.properties").exists() || new File("HogLogViewer.properties").exists() || new File("BigStuff3.properties").exists()) ? "MegaLogViewer" : "TunerStudio";
    }

    public void d() {
        boolean z2 = a() && UIManager.getLookAndFeel().isNativeLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (UnsupportedLookAndFeelException e2) {
            Logger.getLogger(Staging.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        UIManager.getFont("Label.font");
        float fE = e() / 12;
        Set<Object> setKeySet = UIManager.getLookAndFeelDefaults().keySet();
        Object[] array = setKeySet.toArray(new Object[setKeySet.size()]);
        Object[] objArr = new Object[array.length + 1];
        System.arraycopy(array, 0, objArr, 0, array.length);
        objArr[objArr.length - 1] = "defaultFont";
        Font font = UIManager.getFont("defaultFont");
        for (Object obj : objArr) {
            if (obj != null && obj.toString().toLowerCase().contains("font")) {
                Font font2 = UIManager.getFont(obj);
                if (font2 != null && !z2) {
                    Float fValueOf = (Float) this.f1855k.get(obj);
                    if (fValueOf == null) {
                        this.f1855k.put(obj, Float.valueOf(font2.getSize2D()));
                        fValueOf = Float.valueOf(font2.getSize2D());
                    }
                    UIManager.put(obj, new Font(font2.getFamily(), font2.getStyle(), Math.round(a(fValueOf.floatValue() * fE))));
                } else if (font2 == null) {
                    System.out.println("no update:" + obj);
                }
            } else if (obj != null && obj.toString().equals("ScrollBar.width")) {
                System.out.println(obj);
                if (UIManager.getInt(obj) < 20) {
                    UIManager.put(obj, Float.valueOf(a(UIManager.getInt(obj))));
                }
            } else if (UIManager.get(obj) instanceof Font) {
                System.out.println("no update:" + obj);
            }
        }
        if (font != null) {
            UIManager.getLookAndFeel().getDefaults().put("defaultFont", new Font(font.getFamily(), font.getStyle(), (int) a(12.0f)));
        }
    }

    public static int e() throws HeadlessException {
        if (!a()) {
            return 12;
        }
        return Math.round(12 * (Toolkit.getDefaultToolkit().getScreenResolution() / 96.0f));
    }

    public static float a(float f2) {
        return a() ? f2 * (j() / 96.0f) : f2;
    }

    private static float j() {
        if (f1856l < 0.0f) {
            try {
                f1856l = Toolkit.getDefaultToolkit().getScreenResolution();
            } catch (Error e2) {
                f1856l = Toolkit.getDefaultToolkit().getScreenResolution();
            }
        }
        return f1856l;
    }
}
