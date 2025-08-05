package W;

import bH.C0995c;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/* loaded from: TunerStudioMS.jar:W/af.class */
public class af {
    public static Date a(File file) {
        FileInputStream fileInputStream = null;
        try {
            try {
                try {
                    fileInputStream = new FileInputStream(file);
                    int[] iArr = new int[26 + 4];
                    for (int i2 = 0; i2 < iArr.length; i2++) {
                        iArr[i2] = fileInputStream.read();
                    }
                    Date dateA = a(C0995c.b(iArr, 26, 4, true, true));
                    try {
                        fileInputStream.close();
                    } catch (IOException e2) {
                        bH.C.b("MS3 SD Log Transformer: Failed to close file???");
                    }
                    return dateA;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    throw new V.a("Failed to read header from file:\n" + file.getAbsolutePath());
                }
            } catch (FileNotFoundException e4) {
                throw new V.a("File not found:\n" + file.getAbsolutePath());
            }
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (IOException e5) {
                bH.C.b("MS3 SD Log Transformer: Failed to close file???");
            }
            throw th;
        }
    }

    public static Date a(int i2) {
        int iA = C0995c.a(i2, 25, 31) + 1980;
        int iA2 = C0995c.a(i2, 21, 24) - 1;
        int iA3 = C0995c.a(i2, 16, 20);
        int iA4 = C0995c.a(i2, 11, 15);
        int iA5 = C0995c.a(i2, 5, 10);
        int iA6 = C0995c.a(i2, 0, 4) * 2;
        Calendar calendar = Calendar.getInstance();
        calendar.set(iA, iA2, iA3, iA4, iA5, iA6);
        return calendar.getTime();
    }
}
