package ac;

import W.C0188n;
import bH.C0995c;
import bH.W;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ac/y.class */
public class y {
    public static void a(C0188n c0188n, String str) {
        int iA;
        long jB;
        String strA;
        long length;
        byte[] bArr;
        int i2;
        String strG = c0188n.g();
        if (c0188n.h() && strG.contains("NEW_INFO_PROVIDER,[FooterData]")) {
            strG = strG.substring(0, strG.indexOf("NEW_INFO_PROVIDER,[FooterData]") - 1);
        }
        if (c0188n.h()) {
            StringBuilder sb = new StringBuilder(strG);
            sb.append("\n").append("NEW_INFO_PROVIDER").append(",[FooterData],Type:").append("Properties").append("\n");
            for (String str2 : c0188n.i()) {
                sb.append(str2).append("=").append(c0188n.f(str2)).append("\n");
            }
            strG = sb.toString();
        }
        FileInputStream fileInputStream = null;
        File file = new File(str);
        try {
            fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] bArr2 = new byte[20];
            if (bufferedInputStream.read(bArr2) != bArr2.length) {
                throw new IOException("Read incomplete header, file not valid?");
            }
            if (bArr2[0] != 77 || bArr2[1] != 76 || bArr2[2] != 86 || bArr2[3] != 76 || bArr2[4] != 71) {
                throw new IOException("Not a valid .mlg file");
            }
            int iA2 = C0995c.a(bArr2, 6, 2, true, false);
            if (iA2 > 2) {
                throw new V.a("File Format Version: " + iA2 + "\nMaximum supported Format Version: 2\nYou likely need a newer version of this application to load this file.");
            }
            if (iA2 == 1) {
                iA = C0995c.a(bArr2, 12, 2, true, false);
                jB = C0995c.b(bArr2, 14, 4, true, false);
            } else {
                iA = C0995c.a(bArr2, 12, 4, true, false);
                jB = C0995c.b(bArr2, 16, 4, true, false);
            }
            long jB2 = C0995c.b(bArr2, 8, 4, true, false) * 1000;
            byte[] bArr3 = new byte[((int) jB) - bArr2.length];
            int i3 = 0;
            do {
                i3 += bufferedInputStream.read(bArr3, i3, bArr3.length - i3);
            } while (i3 < bArr3.length);
            byte[] bArr4 = new byte[bArr2.length + bArr3.length];
            System.arraycopy(bArr2, 0, bArr4, 0, bArr2.length);
            System.arraycopy(bArr3, 0, bArr4, bArr2.length, bArr3.length);
            if (iA > 0) {
                strA = W.a(bArr4, iA, ((int) jB) - iA);
            } else {
                strA = "";
                iA = (int) jB;
            }
            if (strG.length() > 0) {
                byte[] bytes = strG.getBytes();
                bArr = new byte[bytes.length + 1];
                System.arraycopy(bytes, 0, bArr, 0, bytes.length);
                bArr[bytes.length] = 0;
                length = iA + bArr.length;
            } else {
                iA = 0;
                length = jB - strA.length();
                bArr = new byte[0];
            }
            if (iA2 == 1) {
                C0995c.a(bArr4, iA, 12, 2, true);
                C0995c.a(bArr4, (int) length, 14, 4, true);
            } else {
                C0995c.a(bArr4, iA, 12, 4, true);
                C0995c.a(bArr4, (int) length, 16, 4, true);
            }
            File file2 = new File(str + ".tmp");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            try {
                bufferedOutputStream.write(bArr4, 0, iA);
                bufferedOutputStream.write(bArr);
                byte[] bArr5 = new byte[1024];
                do {
                    i2 = bufferedInputStream.read(bArr5);
                    if (i2 > 0) {
                        bufferedOutputStream.write(bArr5, 0, i2);
                    }
                } while (i2 != -1);
                file.delete();
                file2.renameTo(file);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e2) {
                        Logger.getLogger(y.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
            } finally {
                try {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    bufferedInputStream.close();
                } catch (IOException e3) {
                }
            }
        } catch (Throwable th) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e4) {
                    Logger.getLogger(y.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                }
            }
            throw th;
        }
    }
}
