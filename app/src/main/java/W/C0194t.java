package W;

import java.io.File;
import java.io.IOException;

/* renamed from: W.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/t.class */
public class C0194t {

    /* renamed from: a, reason: collision with root package name */
    public static String f2183a = null;

    public static File a(File[] fileArr, String str) throws IOException {
        String[] strArrB;
        if (str == null && f2183a != null) {
            return new File(f2183a);
        }
        File[] fileArrA = a(fileArr);
        File file = null;
        double d2 = -2.0d;
        for (int i2 = 0; i2 < fileArrA.length; i2++) {
            if (fileArrA[i2].isFile() && (strArrB = C0200z.b(fileArrA[i2])) != null) {
                for (int i3 = 0; i3 < strArrB.length; i3++) {
                    if (strArrB[i3] != null && strArrB[i3].equals(str)) {
                        double dC = C0200z.c(fileArrA[i2]);
                        if (dC > d2) {
                            d2 = dC;
                            file = fileArrA[i2];
                        }
                    }
                }
            }
        }
        return file;
    }

    public static File[] a(File[] fileArr) throws IOException {
        File[] fileArr2;
        if (fileArr.length <= 0) {
            throw new IOException("Ecu Definition directory not found, expected at:\n" + fileArr[0].getAbsolutePath() + "\nYour installation appears corrupt.");
        }
        C0195u c0195u = new C0195u();
        File[] fileArr3 = null;
        for (int i2 = 0; i2 < fileArr.length; i2++) {
            File[] fileArrListFiles = fileArr[i2].listFiles(c0195u);
            if (i2 == 0 || fileArr3 == null) {
                fileArr2 = fileArrListFiles;
            } else {
                File[] fileArr4 = new File[fileArr3.length + fileArrListFiles.length];
                System.arraycopy(fileArr3, 0, fileArr4, 0, fileArr3.length);
                System.arraycopy(fileArrListFiles, 0, fileArr4, fileArr3.length, fileArrListFiles.length);
                fileArr2 = fileArr4;
            }
            fileArr3 = fileArr2;
        }
        return fileArr3;
    }
}
