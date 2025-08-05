package com.efiAnalytics.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;

/* renamed from: com.efiAnalytics.ui.cq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cq.class */
public class C1606cq {

    /* renamed from: d, reason: collision with root package name */
    private static C1606cq f11295d = null;

    /* renamed from: e, reason: collision with root package name */
    private String[] f11296e = null;

    /* renamed from: f, reason: collision with root package name */
    private String[] f11297f = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f11298a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    HashMap f11299b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    HashMap f11300c = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    private File f11301g = null;

    private C1606cq() {
    }

    public static C1606cq a() {
        if (f11295d == null) {
            f11295d = new C1606cq();
        }
        return f11295d;
    }

    public File a(String str, File file) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.f11300c.get(str) != null) {
            return null;
        }
        File fileB = (File) this.f11299b.get(str);
        if (fileB == null && file != null && file.exists() && file.isDirectory()) {
            fileB = b(str, file);
        }
        if (fileB == null) {
            File[] fileArrB = b();
            for (int i2 = 0; i2 < fileArrB.length && fileB == null; i2++) {
                fileB = b(str, fileArrB[i2]);
                if (fileB != null) {
                    bH.C.c("Time: " + (System.currentTimeMillis() - jCurrentTimeMillis) + ", Found Font: " + str + " in " + fileB.getAbsolutePath());
                    return fileB;
                }
            }
        }
        if (fileB == null) {
            bH.C.c("Time (Cached): " + (System.currentTimeMillis() - jCurrentTimeMillis) + ", Not found Font: " + str);
            this.f11300c.put(str, true);
        }
        return fileB;
    }

    public File b(String str, File file) {
        Object family;
        String name;
        if (file == null || !file.exists() || !file.isDirectory()) {
            return null;
        }
        File[] fileArrListFiles = file.listFiles(new C1607cr(this));
        File file2 = null;
        for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
            try {
                Font fontCreateFont = Font.createFont(0, new FileInputStream(fileArrListFiles[i2]));
                family = fontCreateFont.getFamily();
                name = fontCreateFont.getName();
            } catch (FontFormatException e2) {
                bH.C.a("Unable to load font, invalid font file '" + str + "' from file: " + fileArrListFiles[i2].getAbsolutePath());
            } catch (Exception e3) {
                Logger.getLogger(C1606cq.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
            if (str.equals(family) || str.equals(name)) {
                this.f11299b.put(str, fileArrListFiles[i2]);
                return fileArrListFiles[i2];
            }
            if (file2 == null && name.startsWith(str)) {
                file2 = fileArrListFiles[i2];
            }
        }
        if (file2 != null) {
            this.f11299b.put(str, file2);
        }
        return file2;
    }

    public Font a(String str, int i2, int i3) {
        Font fontA;
        if (str != null) {
            try {
                if (!str.equals("")) {
                    if (this.f11298a.containsKey(str)) {
                        return ((Font) this.f11298a.get(str)).deriveFont(i2, i3);
                    }
                    if (a(this.f11296e, str)) {
                        Font font = new Font(str, i2, i3);
                        this.f11298a.put(str, font);
                        return font;
                    }
                    if (this.f11301g == null) {
                        return new Font(str, i2, i3);
                    }
                    File fileB = (File) this.f11299b.get(str);
                    if (fileB == null) {
                        fileB = b(str, this.f11301g);
                    }
                    if (fileB == null || !fileB.exists()) {
                        if (!str.contains("Normal") && (fontA = a(str + " Normal", i2, i3)) != null) {
                            this.f11298a.put(str, fontA);
                            return fontA.deriveFont(i2, i3);
                        }
                        Font font2 = new Font(str, i2, i3);
                        this.f11298a.put(str, font2);
                        bH.C.c("Font for '" + str + "' not found.");
                        return font2;
                    }
                    try {
                        Font fontCreateFont = Font.createFont(0, new FileInputStream(fileB));
                        this.f11298a.put(str, fontCreateFont);
                        String[] strArr = new String[this.f11297f.length + 1];
                        strArr[0] = str;
                        System.arraycopy(this.f11297f, 0, strArr, 1, this.f11297f.length);
                        this.f11297f = bH.R.a(this.f11297f);
                        return fontCreateFont.deriveFont(i2, i3);
                    } catch (Exception e2) {
                        bH.C.d("Failed to load font file for: " + str + ", looked in: " + fileB.getAbsolutePath());
                        return new Font(str, i2, i3);
                    }
                }
            } catch (Exception e3) {
                Font font3 = new Font(str, i2, i3);
                if (str == null) {
                    str = FXMLLoader.NULL_KEYWORD;
                }
                this.f11298a.put(str, font3);
                return font3;
            }
        }
        return new Font("", i2, i3);
    }

    private boolean a(String[] strArr, String str) {
        for (String str2 : strArr) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public File[] b() {
        if (C1685fp.b()) {
            File[] fileArr = new File[1];
            File file = new File("/usr/X11R6/lib/X11/fonts/TrueType");
            if (file.exists()) {
                fileArr[0] = file;
                return fileArr;
            }
        } else if (C1685fp.a()) {
            File[] fileArr2 = {new File("/Library/Fonts"), new File("/System/Library/Fonts"), new File("$HOME/Library/Fonts")};
        } else {
            File[] fileArr3 = new File[1];
            for (File file2 : File.listRoots()) {
                File file3 = new File(file2, "Windows/Fonts");
                if (file3.exists()) {
                    fileArr3[0] = file3;
                    return fileArr3;
                }
            }
        }
        bH.C.c("Unable to locate OS Font Directory(s)");
        return new File[0];
    }

    public String[] c() {
        if (this.f11297f == null) {
            System.currentTimeMillis();
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.f11296e = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            this.f11297f = (String[]) this.f11296e.clone();
            if (this.f11301g != null && this.f11301g.exists()) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(Arrays.asList(this.f11297f));
                boolean z2 = false;
                for (String str : b(this.f11301g)) {
                    if (!arrayList.contains(str)) {
                        arrayList.add(str);
                        z2 = true;
                    }
                }
                if (z2) {
                    this.f11297f = (String[]) arrayList.toArray(new String[arrayList.size()]);
                    this.f11297f = bH.R.a(this.f11297f);
                }
            }
            bH.C.c("Time to retreive font name list: " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms.");
        }
        return this.f11297f;
    }

    public void a(File file) {
        this.f11301g = file;
    }

    public List b(File file) {
        ArrayList arrayList = new ArrayList();
        if (file != null && file.exists() && file.isDirectory()) {
            File[] fileArrListFiles = file.listFiles(new C1608cs(this));
            for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                try {
                    Font fontCreateFont = Font.createFont(0, new FileInputStream(fileArrListFiles[i2]));
                    fontCreateFont.getFamily();
                    arrayList.add(fontCreateFont.getName());
                } catch (FontFormatException e2) {
                    bH.C.c("Font Error on: " + fileArrListFiles[i2].getAbsolutePath());
                    Logger.getLogger(C1685fp.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                } catch (IOException e3) {
                    Logger.getLogger(C1685fp.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        }
        return arrayList;
    }

    public void d() {
        this.f11300c.clear();
    }
}
