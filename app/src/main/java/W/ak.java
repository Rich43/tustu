package W;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: TunerStudioMS.jar:W/ak.class */
public class ak {

    /* renamed from: d, reason: collision with root package name */
    private String f2086d = "F8EK54DI5JFU2JF,HG3BF9GBDF84BASXS";

    /* renamed from: a, reason: collision with root package name */
    static String f2087a = "Some text, try this out for size";

    /* renamed from: e, reason: collision with root package name */
    private static String f2088e = null;

    /* renamed from: f, reason: collision with root package name */
    private static char[] f2089f = {'E', 'F', 'I', 'A', 'K', 'e', 'y', '!', '2', '9', '8', '4', '5', '3', '4', '6'};

    /* renamed from: b, reason: collision with root package name */
    static int f2090b = 300;

    /* renamed from: c, reason: collision with root package name */
    static int f2091c = 1;

    public void a(File file, File file2, String str) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Can not find input file: " + file.getAbsolutePath());
        }
        try {
            byte[] bArrC = c(C0178d.a(file), str);
            if (str == null) {
                str = "";
            }
            try {
                byte[] bArrC2 = c(a(str, bArrC), null);
                String strA = C0200z.a(file);
                double dC = C0200z.c(file);
                C0178d.a(file2, a(dC > 0.0d ? "EFIAPP\nversion:" + f2091c + "\nsignature = " + strA + "\niniVersion=" + dC + "\n" : "EFIAPP\nversion:" + f2091c + "\nsignature = " + strA + "\n", bArrC2));
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new IOException("Failed to decrypt file; " + e2.getMessage());
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new IOException("Failed to decrypt file; " + e3.getMessage());
        }
    }

    public byte[] a(byte[] bArr, String str) {
        return a(bArr, str, b(bArr), a(bArr));
    }

    public byte[] a(byte[] bArr, String str, String str2, double d2) throws IOException {
        try {
            byte[] bArrC = c(bArr, str);
            if (str == null) {
                str = "";
            }
            try {
                return a(d2 > 0.0d ? "EFIAPP\nversion:" + f2091c + "\nsignature = \"" + str2 + "\"\niniVersion=" + d2 + "\n" : "EFIAPP\nversion:" + f2091c + "\nsignature = \"" + str2 + "\"\n", c(a(str, bArrC), null));
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new IOException("Failed to decrypt file; " + e2.getMessage());
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new IOException("Failed to decrypt file; " + e3.getMessage());
        }
    }

    public void a(byte[] bArr, File file, String str, String str2, double d2) throws IOException {
        C0178d.a(file, a(bArr, str, str2, d2));
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x00be, code lost:
    
        if (r8.length() != 1) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00c9, code lost:
    
        if (r8.getBytes()[0] != 20) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00cc, code lost:
    
        r8 = "20";
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00cf, code lost:
    
        r0 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00d3, code lost:
    
        if (r7 == null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00d6, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00dd, code lost:
    
        r13 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00df, code lost:
    
        java.util.logging.Logger.getLogger(W.ak.class.getName()).log(java.util.logging.Level.SEVERE, (java.lang.String) null, (java.lang.Throwable) r13);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String b(byte[] r6) {
        /*
            Method dump skipped, instructions count: 358
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: W.ak.b(byte[]):java.lang.String");
    }

    public double a(byte[] bArr) {
        BufferedReader bufferedReaderC = null;
        try {
            try {
                bufferedReaderC = c(bArr);
                String strTrim = null;
                int i2 = -1;
                while (true) {
                    String line = bufferedReaderC.readLine();
                    String strSubstring = line;
                    if (line == null) {
                        break;
                    }
                    int i3 = i2;
                    i2++;
                    if (i3 >= 100) {
                        break;
                    }
                    if (strSubstring.indexOf(";") != -1) {
                        strSubstring = strSubstring.substring(0, strSubstring.indexOf(";"));
                    }
                    if (strSubstring.indexOf("iniVersion") != -1) {
                        strTrim = strSubstring.substring(strSubstring.indexOf("=") + 1, strSubstring.length()).trim();
                        break;
                    }
                }
                double d2 = strTrim == null ? 0.0d : Double.parseDouble(strTrim);
                if (bufferedReaderC != null) {
                    try {
                        bufferedReaderC.close();
                    } catch (Exception e2) {
                        Logger.getLogger(C0200z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                return d2;
            } catch (Exception e3) {
                e3.printStackTrace();
                if (bufferedReaderC != null) {
                    try {
                        bufferedReaderC.close();
                    } catch (Exception e4) {
                        Logger.getLogger(C0200z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    }
                }
                return -1.0d;
            }
        } catch (Throwable th) {
            if (bufferedReaderC != null) {
                try {
                    bufferedReaderC.close();
                } catch (Exception e5) {
                    Logger.getLogger(C0200z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
            }
            throw th;
        }
    }

    private BufferedReader c(byte[] bArr) {
        byte[] bArr2;
        if (bArr.length > 2000) {
            bArr2 = new byte[2000];
            System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        } else {
            bArr2 = bArr;
        }
        return new BufferedReader(new StringReader(new String(bArr2)));
    }

    public byte[] a(File file) throws IOException {
        try {
            return a(file, true, (String) null);
        } catch (aj e2) {
            Logger.getLogger(ak.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new IOException("Error Decrypting File");
        }
    }

    public byte[] a(File file, String str) {
        return a(file, false, str);
    }

    private byte[] a(File file, boolean z2, String str) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Can not find input file: " + file.getAbsolutePath());
        }
        try {
            return a(C0178d.a(file), z2, str);
        } catch (FileNotFoundException e2) {
            throw e2;
        } catch (IOException e3) {
            throw e3;
        }
    }

    public byte[] b(byte[] bArr, String str) {
        return a(bArr, false, str);
    }

    private byte[] a(byte[] bArr, boolean z2, String str) throws aj, IOException {
        try {
            try {
                al alVarD = d(d(d(bArr).b(), null));
                if (z2 && str == null) {
                    str = alVarD.a();
                }
                try {
                    return d(alVarD.b(), str);
                } catch (aj e2) {
                    throw e2;
                } catch (Exception e3) {
                    e3.printStackTrace();
                    throw new IOException("Failed to decrypt file; " + e3.getMessage());
                }
            } catch (Exception e4) {
                throw new IOException("Failed to decrypt file; " + e4.getMessage());
            }
        } catch (aj e5) {
            throw e5;
        } catch (FileNotFoundException e6) {
            throw e6;
        } catch (IOException e7) {
            throw e7;
        } catch (Exception e8) {
            e8.printStackTrace();
            throw new IOException("Failed to parse data properly. " + e8.getMessage());
        }
    }

    private byte[] a(String str, byte[] bArr) throws UnsupportedEncodingException {
        byte[] bArr2 = new byte[bArr.length + f2090b];
        byte[] bytes = str.getBytes("UTF-8");
        System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
        System.arraycopy(bArr, 0, bArr2, f2090b, bArr.length);
        return bArr2;
    }

    private al d(byte[] bArr) {
        byte[] bArr2 = new byte[f2090b];
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        byte[] bArr3 = new byte[bArr.length - f2090b];
        System.arraycopy(bArr, f2090b, bArr3, 0, bArr3.length);
        al alVar = new al(this);
        alVar.a(bH.W.a(bArr2));
        alVar.a(bArr3);
        return alVar;
    }

    private byte[] c(byte[] bArr, String str) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        String strA = a(str);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, new SecretKeySpec(strA.getBytes("UTF-8"), "AES"), new IvParameterSpec("WWWWWWWWWXXXXXXX".getBytes("UTF-8")));
        return cipher.doFinal(bArr);
    }

    private byte[] d(byte[] bArr, String str) throws NoSuchPaddingException, NoSuchAlgorithmException, aj, InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        String strA = a(str);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, new SecretKeySpec(strA.getBytes("UTF-8"), "AES"), new IvParameterSpec("WWWWWWWWWXXXXXXX".getBytes("UTF-8")));
        try {
            return cipher.doFinal(bArr);
        } catch (BadPaddingException e2) {
            throw new aj("Invalid Password");
        } catch (IllegalBlockSizeException e3) {
            throw new aj("Invalid Password");
        }
    }

    private String a(String str) throws UnsupportedEncodingException {
        String strA = a();
        if (str != null) {
            CRC32 crc32 = new CRC32();
            crc32.update(str.getBytes("UTF-8"));
            String str2 = Long.toHexString(crc32.getValue()) + str;
            strA = str2.length() > 16 ? str2.substring(0, 16) : str2 + a().substring(str2.length());
        }
        return strA;
    }

    public static boolean b(File file) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            boolean zEquals = bufferedReader.readLine().equals("EFIAPP");
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e2) {
                    Logger.getLogger(ak.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            return zEquals;
        } catch (Exception e3) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e4) {
                    Logger.getLogger(ak.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                }
            }
            return false;
        } catch (Throwable th) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e5) {
                    Logger.getLogger(ak.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
            }
            throw th;
        }
    }

    private String a() {
        if (f2088e == null) {
            f2088e = new String(f2089f);
        }
        return f2088e;
    }

    public static void a(char[] cArr) {
        f2089f = cArr;
    }
}
