package r;

import W.C0194t;
import W.C0200z;
import W.R;
import bH.C;
import bH.C1011s;
import bH.I;
import bH.W;
import bH.ad;
import com.efiAnalytics.ui.bV;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.AbstractDocument;
import org.icepdf.core.util.PdfOps;
import v.C1886f;

/* renamed from: r.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/j.class */
public class C1807j {

    /* renamed from: a, reason: collision with root package name */
    public static String f13465a = "config" + File.separator + "settingGroups.xml";

    /* renamed from: b, reason: collision with root package name */
    public static final String f13466b = "config" + File.separator + "ecuDef" + File.separator;

    /* renamed from: c, reason: collision with root package name */
    public static final String f13467c = "startScreenRoot" + File.separator;

    /* renamed from: d, reason: collision with root package name */
    public static final String f13468d = "plugins" + File.separator;

    /* renamed from: e, reason: collision with root package name */
    public static final String f13469e = "Dash" + File.separator;

    /* renamed from: f, reason: collision with root package name */
    public static final String f13470f = "TuneView" + File.separator;

    /* renamed from: g, reason: collision with root package name */
    public static final String f13471g = aE.a.f2348h;

    /* renamed from: h, reason: collision with root package name */
    public static String f13472h = aE.a.f2351k;

    /* renamed from: i, reason: collision with root package name */
    public static final String f13473i = "." + File.separator + "Dash" + File.separator + "backgrounds" + File.separator;

    /* renamed from: j, reason: collision with root package name */
    public static final String f13474j = "." + File.separator + "Dash" + File.separator + "Needles" + File.separator;

    /* renamed from: k, reason: collision with root package name */
    public static final String f13475k = "." + File.separator + "inc" + File.separator + "ms2ReferenceTables.ini";

    /* renamed from: l, reason: collision with root package name */
    public static final String f13476l = "." + File.separator + "inc" + File.separator + "ms2GeneralPorts.ini";

    /* renamed from: m, reason: collision with root package name */
    public static final String f13477m = "." + File.separator + "inc" + File.separator + "ms1BaseVeAnalyzeMaps.ini";

    /* renamed from: n, reason: collision with root package name */
    public static final String f13478n = "." + File.separator + "inc" + File.separator + "ms1ExtraVeAnalyzeMaps.ini";

    /* renamed from: o, reason: collision with root package name */
    public static final String f13479o = "." + File.separator + "inc" + File.separator + "ms3VeAnalyzeMaps.ini";

    /* renamed from: p, reason: collision with root package name */
    public static final String f13480p = "." + File.separator + "inc" + File.separator + "ms2ExtraVeAnalyzeMaps.ini";

    /* renamed from: q, reason: collision with root package name */
    public static final String f13481q = "." + File.separator + "inc" + File.separator + "ms2VeAnalyzeMaps.ini";

    /* renamed from: r, reason: collision with root package name */
    public static final String f13482r = "." + File.separator + "inc" + File.separator + "monsterFirmware1VeAnalyzeMaps.ini";

    /* renamed from: s, reason: collision with root package name */
    public static final String f13483s = "." + File.separator + "inc" + File.separator + "bs3Gen4VEAnalyzeMaps.ini";

    /* renamed from: t, reason: collision with root package name */
    public static final String f13484t = "." + File.separator + "inc" + File.separator + "ms1BaseWueAnalyzeMaps.ecu";

    /* renamed from: u, reason: collision with root package name */
    public static final String f13485u = "." + File.separator + "inc" + File.separator + "ms1ExtraWueAnalyzeMaps.ecu";

    /* renamed from: v, reason: collision with root package name */
    public static final String f13486v = "." + File.separator + "inc" + File.separator + "ms2WueAnalyzeMaps.ecu";

    /* renamed from: w, reason: collision with root package name */
    public static final String f13487w = "." + File.separator + "inc" + File.separator + "ms3_1.2_WueAnalyzeMap.ecu";

    /* renamed from: x, reason: collision with root package name */
    public static final String f13488x = "." + File.separator + "inc" + File.separator + "ms3_1.0_WueAnalyzeMap.ecu";

    /* renamed from: y, reason: collision with root package name */
    public static final String f13489y = f13486v;

    /* renamed from: z, reason: collision with root package name */
    public static final String f13490z = f13486v;

    /* renamed from: A, reason: collision with root package name */
    public static final String f13491A = "." + File.separator + "inc" + File.separator + "ms1BaseDefaultTools.ini";

    /* renamed from: B, reason: collision with root package name */
    public static final String f13492B = "." + File.separator + "inc" + File.separator + "ms1ExtraDefaultTools.ini";

    /* renamed from: C, reason: collision with root package name */
    public static final String f13493C = "." + File.separator + "inc" + File.separator + "msIIBGDefaultTools.ini";

    /* renamed from: D, reason: collision with root package name */
    public static final String f13494D = "." + File.separator + "inc" + File.separator + "ms2ExtraDefaultTools.ini";

    /* renamed from: E, reason: collision with root package name */
    public static final String f13495E = "." + File.separator + "inc" + File.separator + "ms3DefaultTools.ini";

    /* renamed from: F, reason: collision with root package name */
    public static final String f13496F = AbstractDocument.ContentElementName + File.separator;

    /* renamed from: G, reason: collision with root package name */
    public static final String f13497G = "." + File.separator + "inc" + File.separator + "canPcVariables.ini";

    /* renamed from: H, reason: collision with root package name */
    public static String[] f13498H = {"BigStuff3_Gen4_ExampleProject"};

    /* renamed from: I, reason: collision with root package name */
    private static File f13499I = null;

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

    public static File a() {
        return new File(f13496F);
    }

    public static File a(Locale locale) {
        return new File("." + File.separator + f13496F + locale.getLanguage());
    }

    public static File b(Locale locale) {
        File file = new File(A().getAbsolutePath() + File.separator + f13496F + locale.getLanguage());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File b() {
        File file = new File(A().getAbsolutePath() + File.separator + f13468d);
        if (!file.exists()) {
            File file2 = new File(file, "tmp");
            if (!file2.mkdirs()) {
                throw new V.a("Unable to create user ECU definition dir:\n" + file.getAbsolutePath());
            }
            file2.delete();
        } else if (file.isFile()) {
            file.delete();
            file.mkdir();
        }
        return file;
    }

    public static File c() throws V.a {
        File file = new File(A().getAbsolutePath() + File.separator + f13466b);
        if (!file.exists()) {
            File file2 = new File(file, "tmp");
            if (!file2.mkdirs()) {
                throw new V.a("Unable to create user ECU definition dir:\n" + file.getAbsolutePath());
            }
            file2.delete();
        } else if (file.isFile()) {
            file.delete();
            file.mkdir();
        }
        return file;
    }

    public static File c(Locale locale) {
        String str = a(locale).getAbsolutePath() + File.separator;
        File file = new File(str + "staticText.txt");
        return file.exists() ? file : new File(str + "staticText.res");
    }

    public static File d(Locale locale) {
        return new File((b(locale).getAbsolutePath() + File.separator) + "staticText.res");
    }

    public static long a(File file) {
        long length = 0;
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles == null) {
            return 0L;
        }
        for (File file2 : fileArrListFiles) {
            length += file2.length();
        }
        return length;
    }

    public static void a(File file, double d2) {
        long jA = a(file);
        while (jA > d2 * 1024.0d * 1024.0d) {
            File file2 = null;
            File[] fileArrListFiles = file.listFiles();
            for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                if (!fileArrListFiles[i2].isDirectory() && fileArrListFiles[i2].canWrite() && (file2 == null || fileArrListFiles[i2].lastModified() < file2.lastModified())) {
                    file2 = fileArrListFiles[i2];
                }
            }
            if (file2 != null) {
                C.d("Maintain Restore Point size to " + d2 + " size. Deleting:\n" + file2.getAbsolutePath());
            }
            if (file2 != null && !file2.delete()) {
                C.a("Unable to delete oldest restore point file:\n" + file2.getAbsolutePath());
                return;
            }
            jA = a(file);
        }
    }

    public static File a(File file, FileFilter fileFilter) {
        File file2 = null;
        File[] fileArrListFiles = file.listFiles(fileFilter);
        if (fileArrListFiles != null) {
            for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                if (file2 == null || fileArrListFiles[i2].lastModified() > file2.lastModified()) {
                    file2 = fileArrListFiles[i2];
                }
            }
        }
        return file2;
    }

    public static File d() throws V.a {
        File file = new File(A().getAbsolutePath() + File.separator + f13467c);
        if (!file.exists()) {
            File file2 = new File(file, "tmp");
            if (!file2.mkdirs()) {
                throw new V.a("Unable to create Start Screen Root dir:\n" + file.getAbsolutePath());
            }
            file2.delete();
        } else if (file.isFile()) {
            file.delete();
            file.mkdir();
        }
        return file;
    }

    public static File e() {
        File file = new File(C1798a.a().c(C1798a.f13293A, u()));
        return file.exists() ? file : new File(u());
    }

    public static void b(File file) {
        if (!file.isDirectory()) {
            file = file.getParentFile();
        }
        C1798a.a().b(C1798a.f13293A, file.getAbsolutePath());
    }

    public static File f() {
        File file = new File(A(), "Devices");
        if (!file.exists()) {
            File file2 = new File(file, "temp");
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File c(File file) {
        return new File(file, "projectCfg/serial.cfg");
    }

    public static B.i d(File file) {
        File fileC = c(file);
        FileReader fileReader = null;
        try {
            if (!fileC.exists()) {
                return null;
            }
            try {
                Properties properties = new Properties();
                fileReader = new FileReader(fileC);
                properties.load(fileReader);
                B.i iVarA = B.i.a(properties);
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Exception e2) {
                    }
                }
                return iVarA;
            } catch (IOException e3) {
                C.a(e3);
                if (fileReader == null) {
                    return null;
                }
                try {
                    fileReader.close();
                    return null;
                } catch (Exception e4) {
                    return null;
                }
            }
        } catch (Throwable th) {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (Exception e5) {
                }
            }
            throw th;
        }
    }

    public static void a(File file, B.i iVar) {
        B.i iVarD = d(file);
        if (iVarD == null || !iVarD.e().equals(iVar.e()) || !iVarD.i().equals(iVar.i()) || ((iVar.n() != null && (iVarD.n() == null || !iVarD.n().equals(iVar.n()))) || !iVarD.j().equals(iVar.j()))) {
            File fileC = c(file);
            fileC.delete();
            FileWriter fileWriter = null;
            try {
                try {
                    iVar.g(file.getName());
                    Properties propertiesA = iVar.a();
                    propertiesA.remove("protocol");
                    fileWriter = new FileWriter(fileC);
                    propertiesA.store(fileWriter, "Project Device Info");
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (Exception e2) {
                        }
                    }
                } catch (Throwable th) {
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (Exception e3) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e4) {
                Logger.getLogger(C1807j.class.getName()).log(Level.SEVERE, "Failed to save Serial File to project.", (Throwable) e4);
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (Exception e5) {
                    }
                }
            }
        }
    }

    public static List b(String str) {
        File file = new File(u());
        ArrayList arrayList = new ArrayList();
        for (File file2 : file.listFiles(new C1808k())) {
            B.i iVarD = d(file2);
            if (iVarD != null && iVarD.e().equalsIgnoreCase(str)) {
                arrayList.add(file2);
            }
        }
        return arrayList;
    }

    public static File g() {
        return new File(h(), "Acceleration_Performance.dash");
    }

    public static String c(String str) {
        try {
            String canonicalPath = new File(".").getCanonicalPath();
            if (canonicalPath.endsWith("/")) {
                canonicalPath = canonicalPath.substring(0, canonicalPath.length() - 1);
            }
            str = W.b(str, canonicalPath, ".").replace('\\', '/');
        } catch (IOException e2) {
        }
        return str;
    }

    public static File h() {
        return new File(f13469e);
    }

    public static File i() {
        return new File(h(), "Backgrounds");
    }

    public static File j() {
        File file = new File(C1798a.a().c(C1798a.f13292z, new File(u(), f13469e).getAbsolutePath()));
        return file.exists() ? file : new File(u());
    }

    public static File k() {
        File file = new File(C1798a.a().c(C1798a.f13294B, new File(u(), f13470f).getAbsolutePath()));
        return file.exists() ? file : a((aE.a) null);
    }

    public static File a(aE.a aVar) {
        File file = aVar == null ? new File(u(), f13470f) : aVar.S() ? new File(q(), f13470f) : new File(aE.a.A().t(), f13470f);
        if (!file.exists()) {
            File file2 = new File(file, "temp");
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File l() {
        return new File(".", f13470f);
    }

    public static void e(File file) {
        if (!file.isDirectory()) {
            file = file.getParentFile();
        }
        C1798a.a().b(C1798a.f13292z, file.getAbsolutePath());
    }

    public static String m() {
        return new File(f13466b).getAbsolutePath() + File.separator;
    }

    public static File[] n() throws V.a {
        try {
            return C0194t.a(o());
        } catch (IOException e2) {
            throw new V.a(e2.getMessage());
        }
    }

    public static File[] o() {
        return new File[]{new File(m()), c()};
    }

    public static File a(Window window, String str) {
        File fileD = d(str);
        if (fileD == null) {
            try {
                R rA = C1886f.a(window, str);
                if (rA != null && rA.a()) {
                    fileD = rA.c();
                }
            } catch (V.a e2) {
                bV.d(e2.getMessage(), window);
            }
        }
        if (fileD == null) {
            C.c("No Config File found, prompting user.");
            bV.d("Can not find the Controller Definition file (ini,ecu) for\nSerial Signature: '" + str + "'\n\nPlease select the appropriate configuration file.", window);
            String strB = bV.b(window, "Find ecu definition file", new String[]{"ini", "ecu"}, "", "");
            if (strB == null || strB.equals("")) {
                return null;
            }
            File file = new File(strB);
            if (!file.exists()) {
                bV.d("File not found:\n" + strB, window);
                return null;
            }
            String strA = C0200z.a(strB);
            if (strA == null || strA.equals("")) {
                if (!bV.a("The ECU Definition File:\n" + strB + "\nDoes not appear valid, there is no serial signature defined.", (Component) window, true)) {
                    return null;
                }
            } else if (!str.equals(strA) && !bV.a("Signatures do not match!\nIn Tune File (" + C1798a.cw + "): " + str + "\nIn Definition file: " + strA + "\n\nAre you sure you want to load the ." + C1798a.cw + " with this ECU Definition (ini)?", (Component) window, true)) {
                return null;
            }
            try {
                C1011s.a(file, new File(c(), C0200z.b(strA)));
            } catch (Exception e3) {
                C.d("Unable to copy ini file " + strB + " for future use, no directory access");
            }
            fileD = new File(strB);
        }
        return fileD;
    }

    public static File d(String str) throws V.a {
        try {
            return C0194t.a(o(), str);
        } catch (IOException e2) {
            throw new V.a(e2.getMessage());
        }
    }

    public static File e(String str) throws V.a {
        try {
            return C0194t.a(new File[]{c(), new File(m())}, str);
        } catch (IOException e2) {
            throw new V.a(e2.getMessage());
        }
    }

    public static File[] p() {
        return f(f13469e);
    }

    public static File[] f(String str) throws V.a {
        C1809l c1809l = new C1809l();
        File file = new File(str);
        if (file.exists()) {
            return file.listFiles(c1809l);
        }
        throw new V.a("Dash & Gauge directory not found, expected at:\n" + file.getAbsolutePath() + "\nYour installation appears corrupt.");
    }

    public static File a(String str, String str2) {
        return new File(g(str), str2 + ".table");
    }

    public static File b(aE.a aVar) {
        return aVar.K();
    }

    public static File a(aE.a aVar, G.R r2) {
        return new File(b(aVar), r2.c() + "_" + W.a() + "." + C1798a.cw);
    }

    public static File g(String str) {
        File file = new File(str, f13471g);
        if (!file.exists()) {
            C.c("projectCfg directory not found, creating new folder:\n\t" + file.getAbsolutePath());
            file.mkdirs();
        }
        return file;
    }

    public static File q() {
        return new File(u(), "SharedProjectComps");
    }

    public static File r() {
        return I.a() ? new File(((Object) FileSystemView.getFileSystemView().getDefaultDirectory()) + File.separator + W.b(C1798a.f13268b, " ", "") + "Projects/") : new File(((Object) FileSystemView.getFileSystemView().getHomeDirectory()) + File.separator + W.b(C1798a.f13268b, " ", "") + "Projects/");
    }

    public static String s() {
        return u() + C1798a.a().a(C1798a.f13283q, "DefaultProject");
    }

    public static String t() {
        String strC = C1798a.a().c(C1798a.f13367aW, "");
        if (!strC.equals("") && new File(strC).exists()) {
            return strC;
        }
        String strC2 = C1798a.a().c(C1798a.f13366aV, r().getAbsolutePath());
        File file = new File(strC2);
        if (!file.exists()) {
            file.mkdir();
        }
        return strC2;
    }

    public static String u() {
        String strC = C1798a.a().c(C1798a.f13366aV, r().getAbsolutePath());
        boolean z2 = false;
        boolean zExists = new File(strC).exists();
        if (zExists) {
            zExists = false;
            File[] fileArrListFiles = new File(strC).listFiles();
            int length = fileArrListFiles.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                File file = fileArrListFiles[i2];
                if (file.isDirectory() && !file.getName().equals("SharedProjectComps")) {
                    zExists = true;
                    break;
                }
                i2++;
            }
        }
        if (!zExists || 0 != 0) {
            n.j.a().a("One time setup, Copying Default Projects and components..");
            strC = r().getAbsolutePath();
            File file2 = new File(strC);
            if (!file2.exists()) {
                file2.mkdir();
            }
            File file3 = new File("Projects");
            File[] fileArrListFiles2 = file3.listFiles();
            C1811n c1811n = new C1811n();
            for (int i3 = 0; fileArrListFiles2 != null && i3 < fileArrListFiles2.length; i3++) {
                String str = file3.getAbsolutePath() + File.separator + fileArrListFiles2[i3].getName();
                if (new File(str).isDirectory() && new File(new StringBuilder().append(str).append(File.separator).append("projectCfg").append(File.separator).append("project.properties").toString()).exists()) {
                    File file4 = new File(str);
                    try {
                        String canonicalPath = file4.getCanonicalPath();
                        strC = new File(strC).getCanonicalPath();
                        new File(strC + File.separator + fileArrListFiles2[i3].getName()).mkdir();
                        String str2 = bV.d() ? "xcopy /E/I \"" + canonicalPath + "\" \"" + strC + File.separator + fileArrListFiles2[i3].getName() + PdfOps.DOUBLE_QUOTE__TOKEN : "cp -r " + canonicalPath + " " + strC;
                        c1811n.a(new File(strC, file4.getName()).getAbsolutePath());
                        C.d(str2);
                        Runtime.getRuntime().exec(str2);
                        Thread.sleep(1500L);
                        Runtime.getRuntime().exec(str2);
                        if (i3 + 1 < fileArrListFiles2.length) {
                            Thread.sleep(500L);
                        }
                    } catch (IOException e2) {
                        C.b("Failed to copy example project to project folder.\n" + e2.getMessage());
                    } catch (InterruptedException e3) {
                        C.b("copy project process interrupted. Copying to project folder.\n" + e3.getMessage());
                    }
                } else if (str.toLowerCase().endsWith(".zip")) {
                    try {
                        if (!ad.a(str, strC, (String) null).equals(ad.f7040a)) {
                            C.b("Tried to extract sample Project, but failed. Will try again:\n" + str);
                            if (ad.a(str, strC, (String) null).equals(ad.f7040a)) {
                                C.d("Second attempt to extract sample Project Succeded:\n" + str);
                            } else {
                                C.a("Failed second attempt to extract sample Project, Giving up:\n" + str);
                            }
                        }
                        c1811n.a(new File(strC, fileArrListFiles2[i3].getName().substring(0, fileArrListFiles2[i3].getName().lastIndexOf("."))).getAbsolutePath());
                    } catch (ZipException e4) {
                        C.a("Failed to extract sample Project, Will try any others:\n" + str, e4, null);
                    } catch (IOException e5) {
                        C.a("Failed to extract sample Project, Will try any others:\n" + str, e5, null);
                    }
                } else {
                    C.d("Found file " + fileArrListFiles2[i3].getName() + " in the example projects dir, but it is not a valid project or archive.");
                }
            }
            C1798a.a().b(C1798a.f13366aV, strC);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e6) {
                Logger.getLogger(C1807j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
            }
        }
        if (!strC.endsWith(File.separator)) {
            strC = strC + File.separator;
        }
        if (C1806i.a().a("09u8 geaphg3098ulk5")) {
            File file5 = new File(strC, "SharedProjectComps");
            z2 = !file5.exists() || file5.list().length == 0;
        }
        if (z2) {
            C.d("sharedCompsMissing = true");
            File file6 = new File("Projects", "SharedProjectComps.zip");
            if (file6.exists()) {
                String absolutePath = file6.getAbsolutePath();
                try {
                    if (!ad.a(absolutePath, strC, (String) null).equals(ad.f7040a)) {
                        C.b("Tried to extract sample Project, but failed. Will try again:\n" + absolutePath);
                        if (ad.a(absolutePath, strC, (String) null).equals(ad.f7040a)) {
                            C.d("Second attempt to extract sample Project Succeded:\n" + absolutePath);
                        } else {
                            C.a("Failed second attempt to extract sample Project, Giving up:\n" + absolutePath);
                        }
                    }
                } catch (ZipException e7) {
                    C.a("Failed to extract sample Project, Will try any others:\n" + absolutePath, e7, null);
                } catch (IOException e8) {
                    C.a("Failed to extract sample Project, Will try any others:\n" + absolutePath, e8, null);
                }
            } else {
                C.b("Shared Project Comps file not found:\n" + file6.getAbsolutePath());
            }
        }
        return strC;
    }

    public static String[] v() {
        return f13498H;
    }

    public static String w() {
        return f13473i;
    }

    public static String x() {
        return f13474j;
    }

    public static File y() {
        File file = new File(A(), "dashCache");
        if (!file.exists()) {
            File file2 = new File(file, "tmp");
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File z() {
        File file = new File(System.getProperty("user.home"), ".efiAnalytics");
        if (!file.exists()) {
            File file2 = new File(file, Constants.ATTRNAME_TEST);
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File A() {
        File file = new File(z(), C1798a.f13268b);
        if (!file.exists()) {
            File file2 = new File(file, "tmp");
            file2.mkdirs();
            file2.delete();
        }
        if (I.d()) {
            File file3 = new File(E() + ".efianalytics" + File.separator + C1798a.f13268b + File.separator);
            if (file3.exists() && !file3.equals(file)) {
                try {
                    C1011s.a(file, file3, false);
                    C1011s.b(file3);
                } catch (V.a e2) {
                    Logger.getLogger(C1807j.class.getName()).log(Level.WARNING, "Files to migrate .efianalytics to .efiAnalytics", (Throwable) e2);
                }
            }
        }
        return file;
    }

    public static File B() {
        File file = new File(A(), "userActions");
        if (!file.exists()) {
            File file2 = new File(file, "ewq98ewq");
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File C() {
        File file = new File(A(), "help");
        if (!file.exists()) {
            File file2 = new File(file, "tmp");
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File D() {
        return new File(".", "help");
    }

    public static String E() {
        return System.getProperty("user.home") + File.separator;
    }

    public static File F() {
        File file = new File(G(), "fonts");
        if (!file.exists()) {
            File file2 = new File(file, "temp");
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File G() {
        File file = new File(A(), "dashImages");
        if (!file.exists()) {
            File file2 = new File(file, "temp");
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File H() {
        File file = new File(A(), "cache");
        if (!file.exists()) {
            File file2 = new File(file, "temp");
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }

    public static File I() {
        File file = new File(A(), "inc");
        if (!file.exists()) {
            File file2 = new File(file, "temp");
            file2.mkdirs();
            file2.delete();
        }
        return file;
    }
}
