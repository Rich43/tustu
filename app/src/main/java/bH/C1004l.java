package bH;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/* renamed from: bH.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/l.class */
public class C1004l {

    /* renamed from: a, reason: collision with root package name */
    private int f7052a = 0;

    /* renamed from: b, reason: collision with root package name */
    private int f7053b = 0;

    public File a(File file, File file2, FileFilter fileFilter) {
        return a(file, file2, fileFilter, null);
    }

    public File a(File file, File file2, FileFilter fileFilter, L l2) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file2));
        this.f7052a = 0;
        this.f7053b = a("", file, fileFilter);
        if (l2 != null) {
            l2.a();
        }
        a("", zipOutputStream, file, fileFilter, l2);
        zipOutputStream.close();
        if (l2 != null) {
            l2.b();
        }
        return file2;
    }

    private void a(String str, ZipOutputStream zipOutputStream, File file, FileFilter fileFilter, L l2) {
        File[] fileArrListFiles = file.listFiles();
        System.out.println("Adding directory " + file.getName());
        for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
            if (fileArrListFiles[i2].isDirectory()) {
                a(str + fileArrListFiles[i2].getName() + "/", zipOutputStream, fileArrListFiles[i2], fileFilter, l2);
            } else if (fileFilter == null || fileFilter.accept(fileArrListFiles[i2])) {
                try {
                    try {
                        System.out.println("Adding file " + fileArrListFiles[i2].getName());
                        byte[] bArr = new byte[1024];
                        FileInputStream fileInputStream = new FileInputStream(fileArrListFiles[i2]);
                        zipOutputStream.putNextEntry(new ZipEntry(str + fileArrListFiles[i2].getName()));
                        while (true) {
                            int i3 = fileInputStream.read(bArr);
                            if (i3 <= 0) {
                                break;
                            } else {
                                zipOutputStream.write(bArr, 0, i3);
                            }
                        }
                        zipOutputStream.closeEntry();
                        fileInputStream.close();
                        this.f7052a++;
                        if (l2 != null) {
                            l2.a(this.f7052a / this.f7053b);
                        }
                    } catch (IOException e2) {
                        System.out.println("IOException :" + ((Object) e2));
                        this.f7052a++;
                        if (l2 != null) {
                            l2.a(this.f7052a / this.f7053b);
                        }
                    }
                } catch (Throwable th) {
                    this.f7052a++;
                    if (l2 != null) {
                        l2.a(this.f7052a / this.f7053b);
                    }
                    throw th;
                }
            }
        }
    }

    private int a(String str, File file, FileFilter fileFilter) {
        int iA = 0;
        File[] fileArrListFiles = file.listFiles();
        for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
            if (fileArrListFiles[i2].isDirectory()) {
                iA += a(str + fileArrListFiles[i2].getName() + "/", fileArrListFiles[i2], fileFilter);
            } else if (fileFilter == null || fileFilter.accept(fileArrListFiles[i2])) {
                iA++;
            }
        }
        return iA;
    }
}
