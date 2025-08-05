package W;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import sun.java2d.marlin.MarlinConst;

/* renamed from: W.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/d.class */
public class C0178d {
    public static byte[] a(File file) {
        byte[] bArr = new byte[(int) file.length()];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        int i2 = 0;
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            do {
                int i3 = bufferedInputStream.read(bArr, i2, bArr.length - i2);
                if (i3 > 0) {
                    i2 += i3;
                    jCurrentTimeMillis = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - jCurrentTimeMillis > MarlinConst.statDump) {
                    throw new IOException("Timeout reading file.");
                }
                if (i3 < 0) {
                    break;
                }
            } while (i2 < bArr.length);
            if (i2 == bArr.length) {
                return bArr;
            }
            throw new IOException("Only read " + i2 + " bytes of file that is " + bArr.length);
        } finally {
            try {
                bufferedInputStream.close();
            } catch (IOException e2) {
            }
        }
    }

    public static void a(File file, byte[] bArr) {
        file.createNewFile();
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bufferedOutputStream.write(bArr);
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                }
            }
        } catch (Throwable th) {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e3) {
                    throw th;
                }
            }
            throw th;
        }
    }
}
