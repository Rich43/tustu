package W;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.net.ftp.FTP;

/* renamed from: W.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/s.class */
public class C0193s {
    public static BufferedReader a(File file) throws IOException {
        BufferedReader bufferedReader;
        try {
            if (ak.b(file)) {
                byte[] bArrA = new ak().a(file);
                bufferedReader = new BufferedReader(new StringReader(new String(bArrA, a(bArrA) ? "UTF-8" : FTP.DEFAULT_CONTROL_ENCODING)));
            } else if (file.getName().toLowerCase().endsWith(".ecu")) {
                bufferedReader = new BufferedReader(new InputStreamReader(new ax(file), FTP.DEFAULT_CONTROL_ENCODING));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), c(file) ? "UTF-8" : FTP.DEFAULT_CONTROL_ENCODING));
            }
            return bufferedReader;
        } catch (UnsupportedEncodingException e2) {
            throw new IOException("Unsupported Encoding");
        }
    }

    public static BufferedReader a(File file, String str) throws IOException {
        BufferedReader bufferedReader;
        try {
            if (ak.b(file)) {
                byte[] bArrA = new ak().a(file, str);
                bufferedReader = new BufferedReader(new StringReader(new String(bArrA, a(bArrA) ? "UTF-8" : FTP.DEFAULT_CONTROL_ENCODING)));
            } else if (file.getName().toLowerCase().endsWith(".ecu")) {
                bufferedReader = new BufferedReader(new InputStreamReader(new ax(file), FTP.DEFAULT_CONTROL_ENCODING));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), c(file) ? "UTF-8" : FTP.DEFAULT_CONTROL_ENCODING));
            }
            return bufferedReader;
        } catch (UnsupportedEncodingException e2) {
            throw new IOException("Unsupported Encoding");
        }
    }

    public static BufferedReader b(File file) throws IOException {
        BufferedReader bufferedReader;
        try {
            if (ak.b(file)) {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            } else if (file.getName().toLowerCase().endsWith(".ecu")) {
                bufferedReader = new BufferedReader(new InputStreamReader(new ax(file), FTP.DEFAULT_CONTROL_ENCODING));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), c(file) ? "UTF-8" : FTP.DEFAULT_CONTROL_ENCODING));
            }
            return bufferedReader;
        } catch (UnsupportedEncodingException e2) {
            throw new IOException("Unsupported Encoding");
        }
    }

    private static boolean c(File file) {
        byte[] bArr = new byte[3];
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bArr);
                if (bArr[0] == -17 && bArr[1] == -69) {
                    if (bArr[2] == -65) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e2) {
                        }
                        return true;
                    }
                }
                try {
                    fileInputStream.close();
                } catch (Exception e3) {
                }
                BufferedReader bufferedReader = null;
                try {
                    try {
                        bufferedReader = new BufferedReader(new FileReader(file));
                        boolean zA = a(bufferedReader.readLine());
                        try {
                            bufferedReader.close();
                        } catch (Exception e4) {
                        }
                        return zA;
                    } catch (FileNotFoundException e5) {
                        try {
                            bufferedReader.close();
                        } catch (Exception e6) {
                        }
                        return false;
                    } catch (IOException e7) {
                        Logger.getLogger(C0193s.class.getName()).log(Level.WARNING, "Failed to read file", (Throwable) e7);
                        try {
                            bufferedReader.close();
                        } catch (Exception e8) {
                        }
                        return false;
                    }
                } catch (Throwable th) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e9) {
                    }
                    throw th;
                }
            } catch (FileNotFoundException e10) {
                try {
                    fileInputStream.close();
                } catch (Exception e11) {
                }
                return false;
            } catch (IOException e12) {
                Logger.getLogger(C0193s.class.getName()).log(Level.WARNING, "Failed to read file in BOM check", (Throwable) e12);
                try {
                    fileInputStream.close();
                } catch (Exception e13) {
                }
                return false;
            }
        } catch (Throwable th2) {
            try {
                fileInputStream.close();
            } catch (Exception e14) {
            }
            throw th2;
        }
    }

    private static boolean a(byte[] bArr) {
        int i2 = 0;
        while (i2 < 500) {
            try {
                if (bArr[i2] == 10) {
                    break;
                }
                i2++;
            } catch (Exception e2) {
                return false;
            }
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        return a(new String(bArr2, "UTF-8"));
    }

    private static boolean a(String str) {
        if (str == null || !str.contains("=")) {
            return false;
        }
        String strSubstring = str.substring(str.indexOf("=") + 1);
        return strSubstring.contains("UTF-8") || strSubstring.contains(InternalZipConstants.CHARSET_UTF8) || strSubstring.contains("utf-8") || strSubstring.contains("utf8");
    }
}
