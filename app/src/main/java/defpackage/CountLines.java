package defpackage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:CountLines.class */
public class CountLines {

    /* renamed from: a, reason: collision with root package name */
    private static int f230a = 1;

    /* renamed from: b, reason: collision with root package name */
    private static int f231b = 0;

    public int a(String str, String str2, char c2) {
        int iA = 0;
        String[] list = new File(str).list();
        for (int i2 = 0; i2 < list.length; i2++) {
            File file = new File(new StringBuffer(String.valueOf(str)).append("/").append(list[i2]).toString());
            if (file.exists()) {
                if (file.isFile() && list[i2].indexOf(str2) != -1) {
                    f231b++;
                    try {
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                        while (true) {
                            char c3 = (byte) bufferedInputStream.read();
                            if (c3 == 65535) {
                                break;
                            }
                            if (c3 == c2) {
                                iA++;
                            }
                        }
                    } catch (IOException unused) {
                        System.out.println(new StringBuffer("Unable to open ").append((Object) file).append(" for reading.").toString());
                    }
                } else if (file.isDirectory()) {
                    f230a++;
                    iA += new CountLines().a(file.getAbsolutePath(), str2, c2);
                }
            }
        }
        return iA;
    }

    public static void main(String[] strArr) {
        String str = null;
        if (strArr != null && strArr.length > 0) {
            str = strArr[0];
        }
        if (str == null) {
            System.out.println("must supply file type to check. ie. *.java");
            return;
        }
        String strTrim = str.trim();
        if (strTrim.startsWith("*")) {
            strTrim = strTrim.substring(1);
        }
        System.out.println(new StringBuffer("Counted ").append(new CountLines().a(".", strTrim, '\n')).append(" lines of code in ").append(f231b).append(" files and ").append(f230a).append(" directories.").toString());
    }
}
