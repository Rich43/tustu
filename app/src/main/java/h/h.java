package h;

import bH.W;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.File;
import javax.swing.filechooser.FileSystemView;

/* loaded from: TunerStudioMS.jar:h/h.class */
public class h {
    public static File a() {
        File file = new File(System.getProperty("user.home"), ".efiAnalytics");
        if (!file.exists()) {
            File file2 = new File(file, Constants.ATTRNAME_TEST);
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File b() {
        W.b(i.f12256c, C1737b.f12222b, "");
        File file = new File(a(), i.f12255b + W.b(i.f12256c, C1737b.f12224d, ""));
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File c() {
        File file = new File(b(), "inc");
        if (!file.exists()) {
            File file2 = new File(file, Constants.ATTRNAME_TEST);
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static String d() {
        String strE = i.e(i.f12282C, "");
        if (strE.equals("") || !new File(strE).exists()) {
            String string = FileSystemView.getFileSystemView().getDefaultDirectory().toString();
            if (i.f12256c.equals(C1737b.f12229i)) {
                strE = string + File.separator + "TTS" + File.separator;
                File file = new File(strE);
                if (!file.exists()) {
                    strE = FileSystemView.getFileSystemView().getDefaultDirectory().toString() + File.separator + "Power Vision" + File.separator;
                    file = new File(strE);
                }
                if (!file.exists()) {
                    strE = FileSystemView.getFileSystemView().getDefaultDirectory().toString();
                    new File(strE);
                }
            } else {
                strE = string + File.separator + W.b("TunerStudio", " ", "") + "Projects/";
                File file2 = new File(strE);
                if (!file2.exists()) {
                    strE = FileSystemView.getFileSystemView().getDefaultDirectory().toString() + File.separator + W.b("TuneMonster", " ", "") + "Projects/";
                    file2 = new File(strE);
                }
                if (!file2.exists()) {
                    strE = FileSystemView.getFileSystemView().getDefaultDirectory().toString();
                    new File(strE);
                }
            }
            i.c(i.f12282C, strE);
        }
        if (!strE.endsWith(File.separator)) {
            strE = strE + File.separator;
        }
        return strE;
    }

    public static boolean a(String str) {
        File file = new File(str + File.separator + "test123555.4");
        try {
            if (file.exists()) {
                return file.delete();
            }
            file.createNewFile();
            return file.delete();
        } catch (Exception e2) {
            return false;
        }
    }

    public static File e() {
        File file = new File(b(), "settingProfiles");
        if (!file.exists()) {
            File file2 = new File(file, Constants.ATTRNAME_TEST);
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }
}
