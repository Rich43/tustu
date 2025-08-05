package bH;

import W.C0193s;
import W.C0200z;
import W.ak;
import W.ax;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* renamed from: bH.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/s.class */
public class C1011s {
    public static boolean a(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            File file = new File(System.getProperty("user.home") + File.separator + str);
            file.createNewFile();
            file.delete();
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private C1011s() {
    }

    public static void a(String str, String str2) throws V.a {
        File file = new File(str);
        File file2 = new File(str2);
        if (file.equals(file2)) {
            return;
        }
        a(file, file2);
    }

    public static boolean a(String str, long j2) {
        System.currentTimeMillis();
        File file = new File(str, "test123abc~~");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            for (int i2 = 0; i2 < j2; i2++) {
                fileOutputStream.write(i2 % 255);
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e2) {
                }
            }
            file.delete();
            return true;
        } catch (FileNotFoundException e3) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e4) {
                }
            }
            file.delete();
            return false;
        } catch (IOException e5) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e6) {
                }
            }
            file.delete();
            return false;
        } catch (Throwable th) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e7) {
                }
            }
            file.delete();
            throw th;
        }
    }

    public static void a(File file, File file2) throws V.a {
        a(file, file2, true);
    }

    public static void a(File file, File file2, boolean z2) throws V.a {
        a(file, file2, null, z2);
    }

    /* JADX WARN: Finally extract failed */
    public static void a(File file, File file2, FileFilter fileFilter, boolean z2) throws V.a {
        if (file.isDirectory()) {
            if (file2.exists() && (z2 || !file2.isDirectory())) {
                b(file2);
            }
            file2.mkdirs();
            File[] fileArrListFiles = file.listFiles();
            for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                a(fileArrListFiles[i2], new File(file2, fileArrListFiles[i2].getName()), fileFilter, z2);
            }
            return;
        }
        if (!file2.exists() || file2.delete()) {
        }
        if (fileFilter == null || fileFilter.accept(file)) {
            try {
                file2.createNewFile();
                BufferedInputStream bufferedInputStream = null;
                BufferedOutputStream bufferedOutputStream = null;
                try {
                    try {
                        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                        for (int i3 = bufferedInputStream.read(); i3 != -1; i3 = bufferedInputStream.read()) {
                            bufferedOutputStream.write(i3);
                        }
                        if (bufferedInputStream != null) {
                            try {
                                bufferedInputStream.close();
                            } catch (IOException e2) {
                                Logger.getLogger(C1011s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                            }
                        }
                        if (bufferedOutputStream != null) {
                            try {
                                bufferedOutputStream.flush();
                                bufferedOutputStream.close();
                            } catch (IOException e3) {
                                Logger.getLogger(C1011s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                            }
                        }
                    } catch (Exception e4) {
                        e4.printStackTrace();
                        throw new V.a("Error copying file:\n" + file.getAbsolutePath() + "\nto:\n" + file2.getAbsolutePath() + "\nError:" + e4.getMessage());
                    }
                } catch (Throwable th) {
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e5) {
                            Logger.getLogger(C1011s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                        }
                    }
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.flush();
                            bufferedOutputStream.close();
                        } catch (IOException e6) {
                            Logger.getLogger(C1011s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                        }
                    }
                    throw th;
                }
            } catch (IOException e7) {
                e7.printStackTrace();
                throw new V.a("Failed to create file:\n" + file2.getAbsolutePath() + "\nReason:\n" + e7.getMessage());
            }
        }
    }

    public static void b(File file, File file2) throws V.a {
        int i2;
        boolean zEndsWith = file.getName().toLowerCase().endsWith(".ecu");
        boolean z2 = file2.getName().toLowerCase().endsWith(".ecu") || ak.b(file);
        if (!file2.exists() || !file2.delete()) {
        }
        try {
            file2.createNewFile();
            BufferedReader bufferedReaderA = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                try {
                    if (z2) {
                        ak akVar = new ak();
                        if (ak.b(file)) {
                            a(file, file2);
                        } else if (zEndsWith) {
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(new ax(file));
                            byte[] bArr = new byte[(int) file.length()];
                            int i3 = 0;
                            do {
                                try {
                                    i2 = bufferedInputStream.read(bArr, i3, bArr.length - i3);
                                    if (i2 > 0) {
                                        i3 += i2;
                                    }
                                } finally {
                                    try {
                                        bufferedInputStream.close();
                                    } catch (IOException e2) {
                                    }
                                }
                            } while (i2 >= 0);
                            akVar.a(bArr, file2, null, C0200z.a(file), C0200z.c(file));
                        } else {
                            akVar.a(file, file2, (String) null);
                        }
                    } else {
                        bufferedReaderA = C0193s.a(file);
                        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                        for (int i4 = bufferedReaderA.read(); i4 != -1; i4 = bufferedReaderA.read()) {
                            bufferedOutputStream.write(i4);
                        }
                    }
                    if (bufferedReaderA != null) {
                        try {
                            bufferedReaderA.close();
                        } catch (IOException e3) {
                            Logger.getLogger(C1011s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                        }
                    }
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (IOException e4) {
                            Logger.getLogger(C1011s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        }
                    }
                } catch (Exception e5) {
                    e5.printStackTrace();
                    throw new V.a("Error copying file:\n" + file.getAbsolutePath() + "\nto:\n" + file2.getAbsolutePath() + "\nError:" + e5.getMessage());
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        bufferedReaderA.close();
                    } catch (IOException e6) {
                        Logger.getLogger(C1011s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                    }
                }
                if (0 != 0) {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e7) {
                        Logger.getLogger(C1011s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                    }
                }
                throw th;
            }
        } catch (IOException e8) {
            e8.printStackTrace();
            throw new V.a("Failed to create file:\n" + file2.getAbsolutePath() + "\nReason:\n" + e8.getMessage());
        }
    }

    public static boolean a(File file) {
        for (File file2 : File.listRoots()) {
            if (file.equals(file2)) {
                return true;
            }
        }
        return false;
    }

    public static String b(String str) {
        if (str != null && str.indexOf(File.separatorChar) != -1) {
            str = str.substring(str.lastIndexOf(File.separatorChar) + 1);
        }
        return str;
    }

    public static void b(File file) {
        if (!file.isDirectory()) {
            if (file.isFile()) {
                C.c("Delete Results " + file.getName() + " " + file.delete());
            }
        } else {
            for (File file2 : file.listFiles()) {
                b(file2);
            }
            C.c("Delete Results " + file.getName() + " " + file.delete());
        }
    }

    public static String a(byte[] bArr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            byte[] bArrDigest = messageDigest.digest(bArr);
            StringBuilder sb = new StringBuilder();
            for (byte b2 : bArrDigest) {
                sb.append(Integer.toString((b2 & 255) + 256, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String c(File file) {
        byte[] bArrD = d(file);
        StringBuilder sb = new StringBuilder();
        for (byte b2 : bArrD) {
            sb.append(Integer.toString((b2 & 255) + 256, 16).substring(1));
        }
        return sb.toString();
    }

    public static byte[] d(File file) {
        return a(new FileInputStream(file));
    }

    public static byte[] a(InputStream inputStream) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            try {
                inputStream = new DigestInputStream(inputStream, messageDigest);
                do {
                } while (inputStream.read(new byte[512]) >= 0);
                inputStream.close();
                return messageDigest.digest();
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                }
                throw th;
            }
        } catch (NoSuchAlgorithmException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static boolean a(URL url) {
        if (url == null || !url.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE)) {
            return true;
        }
        File fileB = b(url);
        return fileB != null && fileB.exists();
    }

    public static File b(URL url) {
        if (url == null || !url.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE)) {
            return null;
        }
        String str = "";
        if (url.getHost() != null && url.getHost().length() > 0) {
            str = str + url.getHost() + CallSiteDescriptor.TOKEN_DELIMITER;
        }
        try {
            str = str + URLDecoder.decode(url.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            Logger.getLogger(C1011s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        return new File(str);
    }

    public static boolean a(File file, byte[] bArr) {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            try {
                byte[] bArr2 = new byte[bArr.length];
                fileInputStream.read(bArr2);
                for (int i2 = 0; i2 < bArr2.length; i2++) {
                    if (bArr2[i2] != bArr[i2]) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e2) {
                        }
                        return false;
                    }
                }
                try {
                    fileInputStream.close();
                } catch (Exception e3) {
                }
                return true;
            } catch (Throwable th) {
                try {
                    fileInputStream.close();
                } catch (Exception e4) {
                }
                throw th;
            }
        } catch (IOException e5) {
            Logger.getLogger(C1011s.class.getName()).log(Level.WARNING, "File not readable.", (Throwable) e5);
            try {
                fileInputStream.close();
            } catch (Exception e6) {
            }
            return false;
        }
    }
}
