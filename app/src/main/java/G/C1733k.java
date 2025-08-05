package g;

import W.C0188n;
import bH.W;
import com.efiAnalytics.ui.bV;
import com.sun.glass.ui.Platform;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import sun.util.locale.LanguageTag;

/* renamed from: g.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/k.class */
public class C1733k {

    /* renamed from: a, reason: collision with root package name */
    public static JFileChooser f12218a = null;

    public static Object[] a(Object[] objArr) {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < objArr.length; i3++) {
                if (Double.parseDouble(objArr[i2].toString()) > Double.parseDouble(objArr[i3].toString())) {
                    Object obj = objArr[i2];
                    objArr[i2] = objArr[i3];
                    objArr[i3] = obj;
                }
            }
        }
        return objArr;
    }

    public static Object[] b(Object[] objArr) {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < objArr.length; i3++) {
                String str = (String) objArr[i2];
                String str2 = (String) objArr[i3];
                if (str.compareTo(str2) > 0) {
                    objArr[i2] = str2;
                    objArr[i3] = str;
                }
            }
        }
        return objArr;
    }

    public static String[] a(String[] strArr) {
        Arrays.sort(strArr, new C1734l());
        return strArr;
    }

    public static String[] b(String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < strArr.length; i3++) {
                String str = strArr[i2];
                String str2 = strArr[i3];
                if (str.compareTo(str2) > 0) {
                    strArr[i2] = str2;
                    strArr[i3] = str;
                }
            }
        }
        return strArr;
    }

    public static String a(String str, String str2, String str3) {
        return W.b(str, str2, str3);
    }

    public static void b(String str, String str2, String str3) throws V.h {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(str)));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                int i2 = bufferedInputStream.read();
                if (i2 == -1) {
                    break;
                } else {
                    stringBuffer.append((char) i2);
                }
            }
            bufferedInputStream.close();
            byte[] bytes = a(stringBuffer.toString(), str2, str3).getBytes();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
            for (byte b2 : bytes) {
                fileOutputStream.write(b2);
            }
            fileOutputStream.close();
        } catch (Exception e2) {
            System.out.println("Error writing replacement file");
            e2.printStackTrace();
            throw new V.h("Error Saving File, check " + h.i.f12255b + "logFile.txt for more detail.");
        }
    }

    public static String[] a(String str, String str2) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, str2);
        String[] strArr = new String[stringTokenizer.countTokens()];
        for (int i2 = 0; i2 < strArr.length && stringTokenizer.hasMoreTokens(); i2++) {
            strArr[i2] = stringTokenizer.nextToken();
        }
        return strArr;
    }

    public static Frame a(Component component) {
        while (component != null && !(component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }

    public static void b(Component component) {
        while (component != null && !(component instanceof Frame)) {
            component.invalidate();
            component = component.getParent();
        }
    }

    public static String a(Component component, String str, String[] strArr, String str2) {
        return a(component, str, strArr, str2, true);
    }

    public static boolean a() {
        return System.getProperty("os.name", "Windows").startsWith(Platform.WINDOWS);
    }

    public static String a(Component component, String str, String[] strArr, String str2, boolean z2) {
        String[] strArrA = a(component, str, strArr, str2, z2, false);
        if (strArrA == null || strArrA.length < 1) {
            return null;
        }
        return strArrA[0];
    }

    public static String[] a(Component component, String str, String[] strArr, String str2, boolean z2, boolean z3) {
        System.getProperty("os.name", "Windows");
        boolean zA = h.i.a("useSwingFileDialog", true);
        String strSubstring = "";
        if (strArr[0].indexOf(File.separator) != -1) {
            strArr[0] = strArr[0].substring(strArr[0].lastIndexOf(File.separator) + 1);
        }
        for (String str3 : strArr) {
            strSubstring = strSubstring + "*." + str3 + ";";
        }
        if (strSubstring.charAt(strSubstring.length() - 1) == ';') {
            strSubstring = strSubstring.substring(0, strSubstring.length() - 1);
        }
        if (!zA) {
            FileDialog fileDialog = z2 ? new FileDialog(a(component), str, 0) : new FileDialog(a(component), str, 1);
            fileDialog.setTitle(str);
            fileDialog.setFile(strSubstring);
            fileDialog.setDirectory(h.i.e("lastFileDir" + strSubstring, "."));
            fileDialog.setVisible(true);
            if (fileDialog.getFile() == null) {
                return null;
            }
            h.i.c("lastFileDir" + strSubstring, fileDialog.getDirectory());
            return new String[]{fileDialog.getDirectory() + fileDialog.getFile()};
        }
        if (f12218a == null) {
            f12218a = new JFileChooser();
            f12218a.setFileView(new C1725c());
        }
        f12218a.setDialogTitle(str);
        f12218a.setMultiSelectionEnabled(z3);
        File defaultDirectory = FileSystemView.getFileSystemView().getDefaultDirectory();
        File file = new File(defaultDirectory, "TunerStudioProjects");
        f12218a.setCurrentDirectory(new File(h.i.e("lastFileDir" + strSubstring, (file.exists() ? file : defaultDirectory).getAbsolutePath())));
        if (str2 != null && !str2.equals("")) {
            f12218a.setSelectedFile(new File(str2));
        } else if (!z2) {
            f12218a.setSelectedFile(new File(b() + "." + strArr[0]));
        }
        if (strArr != null && strArr.length > 0) {
            C1728f c1728f = new C1728f(h.i.f12255b + " Files(" + strSubstring + ")");
            f12218a.resetChoosableFileFilters();
            for (String str4 : strArr) {
                c1728f.b(str4);
            }
            f12218a.addChoosableFileFilter(c1728f);
        }
        if ((z2 ? f12218a.showOpenDialog(component) : f12218a.showSaveDialog(component)) != 0) {
            return null;
        }
        h.i.c("lastFileDir" + strSubstring, f12218a.getCurrentDirectory().getAbsolutePath());
        if (!z3) {
            return new String[]{f12218a.getCurrentDirectory().getAbsolutePath() + File.separator + f12218a.getSelectedFile().getName()};
        }
        File[] selectedFiles = f12218a.getSelectedFiles();
        String[] strArr2 = new String[selectedFiles.length];
        for (int i2 = 0; i2 < selectedFiles.length; i2++) {
            strArr2[i2] = f12218a.getCurrentDirectory().getAbsolutePath() + File.separator + selectedFiles[i2].getName();
        }
        return strArr2;
    }

    public static void a(String str, Component component) {
        a(str, component, false);
    }

    public static boolean a(String str, Component component, boolean z2) {
        return bV.a(str, component, z2);
    }

    public static String a(String str, boolean z2, Component component) {
        return a(null, str, z2, "                                   User defined values required for this formula.", false, component, null);
    }

    public static String a(String str, boolean z2, String str2, boolean z3, Component component) {
        return a(null, str, z2, str2, z3, component, null, null);
    }

    public static String a(String str, String str2, boolean z2, String str3, boolean z3, Component component, C0188n c0188n) {
        return a(str, str2, z2, str3, z3, component, null, c0188n);
    }

    public static String a(String str, String str2, boolean z2, String str3, boolean z3, Component component, InterfaceC1735m interfaceC1735m, C0188n c0188n) {
        C1729g c1729g = new C1729g(bV.a(component), str, str2, z2, str3, z3, interfaceC1735m, c0188n);
        component.requestFocus();
        c1729g.dispose();
        return c1729g.a();
    }

    public static String a(double d2) {
        return a(d2, 1);
    }

    public static String a(double d2, int i2) {
        String strSubstring = d2 + "";
        if (strSubstring.indexOf(".0") != -1) {
            strSubstring = strSubstring.substring(0, strSubstring.indexOf(".0"));
        } else if (strSubstring.indexOf(".") != -1 && strSubstring.length() > strSubstring.indexOf(".") + i2) {
            strSubstring = strSubstring.substring(0, strSubstring.indexOf(".") + i2 + 1);
        }
        return strSubstring;
    }

    public static Color a(double d2, double d3, double d4) {
        int i2 = 255 - 175;
        int i3 = (int) (175 * ((d2 - d3) / (d4 - d3)));
        return new Color(i3 + i2, (175 - i3) + i2, Math.abs(i3 - (175 / 2)));
    }

    public static void a(Frame frame, Dialog dialog) throws HeadlessException {
        Dimension size = frame.getSize();
        Dimension size2 = dialog.getSize();
        Point location = frame.getLocation();
        if (location.getX() > 0.0d && location.getY() > 0.0d) {
            dialog.setLocation((int) (location.getX() + ((size.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((size.getHeight() - size2.getHeight()) / 2.0d)));
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dialog.setLocation((int) (location.getX() + ((screenSize.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((screenSize.getHeight() - size2.getHeight()) / 2.0d)));
        }
    }

    public static String b() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(1) + LanguageTag.SEP + a((calendar.get(2) + 1) + "", '0', 2) + LanguageTag.SEP + a(calendar.get(5) + "", '0', 2) + "_" + a(calendar.get(11) + "", '0', 2) + "." + a(calendar.get(12) + "", '0', 2) + "." + a(calendar.get(13) + "", '0', 2);
    }

    public static String a(String str, char c2, int i2) {
        while (str.length() < i2) {
            str = c2 + str;
        }
        return str;
    }
}
