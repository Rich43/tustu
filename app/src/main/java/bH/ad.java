package bH;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: TunerStudioMS.jar:bH/ad.class */
public class ad {

    /* renamed from: a, reason: collision with root package name */
    public static String f7040a = "Successful";

    public static ArrayList a(File file, File file2, String str) {
        return a(file, file2, str, (L) null);
    }

    public static ArrayList a(File file, File file2, String str, L l2) {
        if (l2 != null) {
            try {
                l2.a();
            } catch (Throwable th) {
                if (l2 != null) {
                    l2.b();
                }
                throw th;
            }
        }
        ZipFile zipFile = new ZipFile(file);
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
        while (enumerationEntries.hasMoreElements()) {
            enumerationEntries.nextElement2();
            i2++;
        }
        if (l2 != null) {
            l2.a(0.0d);
        }
        List listA = a(str);
        int i3 = 0;
        Enumeration<? extends ZipEntry> enumerationEntries2 = zipFile.entries();
        while (enumerationEntries2.hasMoreElements()) {
            ZipEntry zipEntryNextElement2 = enumerationEntries2.nextElement2();
            String name = zipEntryNextElement2.getName();
            String lowerCase = zipEntryNextElement2.getName().contains(".") ? zipEntryNextElement2.getName().substring(zipEntryNextElement2.getName().lastIndexOf(".") + 1).toLowerCase() : "";
            if (name.length() > 4) {
                name = name.substring(name.length() - 4, name.length()).toLowerCase();
            }
            if (str == null || zipEntryNextElement2.isDirectory() || name.equals(".jpg") || name.equals("jpeg") || name.equals(".gif") || listA.contains(lowerCase)) {
                arrayList.add(a(zipFile, zipEntryNextElement2, file2));
            }
            i3++;
            if (l2 != null) {
                l2.a(i3 / i2);
            }
        }
        zipFile.close();
        if (l2 != null) {
            l2.b();
        }
        return arrayList;
    }

    private static List a(String str) {
        ArrayList arrayList = new ArrayList();
        if (str != null && !str.isEmpty()) {
            for (String str2 : str.split(";")) {
                arrayList.add(str2.toLowerCase());
            }
        }
        return arrayList;
    }

    public static File a(ZipFile zipFile, ZipEntry zipEntry, File file) throws IOException {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        try {
            File file2 = new File(file, "/" + zipEntry.getName());
            String absolutePath = file2.getAbsolutePath();
            new File(absolutePath.substring(0, absolutePath.lastIndexOf(File.separator))).mkdirs();
            file2.setLastModified(zipEntry.getTime());
            if (zipEntry.isDirectory()) {
                file2.mkdir();
            } else {
                if (file2.exists()) {
                    file2.delete();
                }
                file2.createNewFile();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
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
            return file2;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e2) {
            }
        }
    }

    public static String a(String str, String str2, String str3) throws IOException {
        ZipFile zipFile = new ZipFile(str);
        C.d("Expanding Zip File: " + str + ", to dir: " + str2);
        Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
        while (enumerationEntries.hasMoreElements()) {
            ZipEntry zipEntryNextElement2 = enumerationEntries.nextElement2();
            String name = zipEntryNextElement2.getName();
            if (name.length() > 4) {
                name = name.substring(name.length() - 4, name.length()).toLowerCase();
            }
            if (str3 == null || zipEntryNextElement2.isDirectory() || name.equals(".jpg") || name.equals("jpeg") || name.equals(".gif")) {
                String strA = a(zipFile, zipEntryNextElement2, str2);
                if (!strA.equals(f7040a)) {
                    return strA;
                }
            }
        }
        zipFile.close();
        return f7040a;
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
        return f7040a;
    }

    public static File a(File file, File file2, FileFilter fileFilter) {
        return new C1004l().a(file, file2, fileFilter);
    }

    public static File a(File file, File file2, FileFilter fileFilter, L l2) {
        return new C1004l().a(file, file2, fileFilter, l2);
    }
}
