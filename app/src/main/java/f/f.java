package f;

import bH.C;
import bH.C0995c;
import bH.I;
import bH.W;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:f/f.class */
public class f {
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0056, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0061, code lost:
    
        r6 = r0.trim();
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0049, code lost:
    
        r0 = r7.readLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x004f, code lost:
    
        if (r0 != null) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a() {
        /*
            Method dump skipped, instructions count: 224
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: f.f.a():java.lang.String");
    }

    public static String b() {
        String str;
        String strSubstring;
        try {
            String strA = a();
            if (strA != null) {
                if (strA.trim().length() > 2) {
                    return strA;
                }
            }
            str = "";
        } catch (Exception e2) {
            str = "";
        }
        try {
            File fileCreateTempFile = File.createTempFile("mbId", ".vbs");
            fileCreateTempFile.deleteOnExit();
            FileWriter fileWriter = new FileWriter(fileCreateTempFile);
            fileWriter.write("Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\nSet colItems = objWMIService.ExecQuery _ \n   (\"Select * from Win32_BaseBoard\") \nFor Each objItem in colItems \n    Wscript.Echo objItem.SerialNumber \n    exit for  ' do the first cpu only! \nNext \n");
            fileWriter.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cscript //NoLogo " + fileCreateTempFile.getPath()).getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                str = str + line;
            }
            bufferedReader.close();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        if (str != null) {
            strSubstring = str.trim();
            if (strSubstring.length() < 8 || strSubstring.contains("123456789") || strSubstring.getBytes().length != strSubstring.length() || !C0995c.a(strSubstring.getBytes()) || strSubstring.indexOf(" ") > 0) {
                strSubstring = "";
            }
        } else {
            strSubstring = "";
        }
        if (strSubstring.length() > 25) {
            strSubstring = strSubstring.substring(0, 24);
        }
        return strSubstring;
    }

    public static String c() {
        String line;
        try {
            Process processExec = Runtime.getRuntime().exec(((Object) m()) + "\\wmic.exe cpu get ProcessorId");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
            while (true) {
                String line2 = bufferedReader.readLine();
                if (line2 == null) {
                    return null;
                }
                if (line2.toLowerCase().startsWith("processorid")) {
                    do {
                        line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                    } while (line.isEmpty());
                    String strTrim = line.endsWith("\n") ? line.substring(0, line.length() - 1).trim() : line.substring(0, line.length()).trim();
                    processExec.destroy();
                    if (strTrim != null && strTrim.length() > 50) {
                        strTrim = strTrim.substring(strTrim.length() - 49, strTrim.length());
                    }
                    if (strTrim != null && strTrim.length() > 10) {
                        return strTrim;
                    }
                }
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public static String d() {
        return I.d() ? e() : I.b() ? g() : c();
    }

    public static String e() {
        try {
            Process processExec = Runtime.getRuntime().exec(new File("/usr/bin/cat").exists() ? "/usr/bin/cat /proc/cpuinfo" : new File("/sbin/cat").exists() ? "/sbin/cat /proc/cpuinfo" : "/bin/cat /proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    return null;
                }
                if (line.toLowerCase().startsWith("serial") && line.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) != -1) {
                    String strSubstring = line.substring(line.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1);
                    String strTrim = strSubstring.endsWith("\n") ? strSubstring.substring(0, strSubstring.length() - 1).trim() : strSubstring.substring(0, strSubstring.length()).trim();
                    processExec.destroy();
                    if (strTrim != null && strTrim.length() > 50) {
                        strTrim = strTrim.substring(strTrim.length() - 49, strTrim.length());
                    }
                    if (strTrim != null && !strTrim.isEmpty()) {
                        return strTrim;
                    }
                }
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public static byte[] f() {
        Process processExec = null;
        try {
            processExec = Runtime.getRuntime().exec(new File("/usr/bin/ip").exists() ? "/usr/bin/ip link" : new File("/sbin/ip").exists() ? "/sbin/ip link" : "/bin/ip link");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
            String upperCase = null;
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    if (processExec == null) {
                        return null;
                    }
                    try {
                        processExec.destroy();
                        return null;
                    } catch (Exception e2) {
                        return null;
                    }
                }
                String strTrim = line.trim();
                if (strTrim.toLowerCase().indexOf("ether") != -1 && strTrim.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) != -1) {
                    String[] strArrSplit = strTrim.toLowerCase().split(" ");
                    int length = strArrSplit.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        }
                        String str = strArrSplit[i2];
                        if (str.contains(CallSiteDescriptor.TOKEN_DELIMITER)) {
                            upperCase = str;
                            break;
                        }
                        i2++;
                    }
                    if (upperCase == null) {
                        upperCase = "";
                    }
                    upperCase = (upperCase.endsWith("\n") ? upperCase.substring(0, upperCase.length() - 1).trim() : upperCase.trim()).toUpperCase();
                    if (upperCase.length() > 50) {
                        upperCase = upperCase.substring(upperCase.length() - 49, upperCase.length());
                    }
                    if (upperCase != null && !upperCase.isEmpty()) {
                        byte[] bArrC = c(upperCase);
                        if (!a(bArrC)) {
                            if (processExec != null) {
                                try {
                                    processExec.destroy();
                                } catch (Exception e3) {
                                }
                            }
                            return bArrC;
                        }
                    }
                }
            }
        } catch (Exception e4) {
            if (processExec == null) {
                return null;
            }
            try {
                processExec.destroy();
                return null;
            } catch (Exception e5) {
                return null;
            }
        } catch (Throwable th) {
            if (processExec != null) {
                try {
                    processExec.destroy();
                } catch (Exception e6) {
                }
            }
            throw th;
        }
    }

    public static String g() {
        try {
            Process processExec = Runtime.getRuntime().exec("ioreg -l | grep IOPlatformSerialNumber");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    return null;
                }
                if (line.toLowerCase().contains("ioplatformserial") && line.indexOf("=") != -1) {
                    String strSubstring = line.substring(line.indexOf("=") + 1);
                    String strB = W.b(strSubstring.endsWith("\n") ? strSubstring.substring(0, strSubstring.length() - 1).trim() : strSubstring.substring(0, strSubstring.length()).trim(), PdfOps.DOUBLE_QUOTE__TOKEN, "");
                    processExec.destroy();
                    if (strB != null && strB.length() > 50) {
                        strB = strB.substring(strB.length() - 49, strB.length());
                    }
                    if (strB != null && !strB.isEmpty()) {
                        return strB;
                    }
                }
            }
        } catch (Exception e2) {
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0118, code lost:
    
        r8.destroy();
        r7 = r7 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String h() {
        /*
            Method dump skipped, instructions count: 297
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: f.f.h():java.lang.String");
    }

    public static String i() {
        Process processExec = null;
        for (int i2 = 0; i2 < 5; i2++) {
            try {
                String strA = a(i2);
                if (strA != null && strA.contains("/dev")) {
                    processExec = new File("/sbin/udevadm").exists() ? Runtime.getRuntime().exec("/sbin/udevadm info --query=property --name=" + strA) : new File("/bin/udevadm").exists() ? Runtime.getRuntime().exec("/bin/udevadm info --query=property --name=" + strA) : Runtime.getRuntime().exec("/usr/bin/udevadm info --query=property --name=" + strA);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
                    String strSubstring = null;
                    String strSubstring2 = null;
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        if (line.contains("ID_SERIAL_SHORT") && line.indexOf("=") != -1) {
                            strSubstring = line.substring(line.indexOf("=") + 1);
                            if (strSubstring.endsWith("\n")) {
                                strSubstring = strSubstring.substring(0, strSubstring.length() - 1);
                            }
                            if (strSubstring != null && strSubstring.length() > 50) {
                                strSubstring = strSubstring.substring(0, 49);
                            }
                        } else if (line.contains("ID_SERIAL") && line.indexOf("=") != -1) {
                            strSubstring2 = line.substring(line.indexOf("=") + 1);
                            if (strSubstring2.endsWith("\n")) {
                                strSubstring2 = strSubstring2.substring(0, strSubstring2.length() - 1);
                            }
                            if (strSubstring2 != null && strSubstring2.length() > 50) {
                                strSubstring2 = strSubstring2.substring(0, 49);
                            }
                        } else if (line.contains("ID_FS_UUID") && line.indexOf("=") != -1) {
                            strSubstring2 = line.substring(line.indexOf("=") + 1);
                            if (strSubstring2.endsWith("\n")) {
                                strSubstring2 = strSubstring2.substring(0, strSubstring2.length() - 1);
                            }
                            if (strSubstring2 != null && strSubstring2.length() > 50) {
                                strSubstring2 = strSubstring2.substring(0, 49);
                            }
                        }
                    }
                    if (strSubstring != null && !strSubstring.isEmpty()) {
                        String str = strSubstring;
                        if (processExec != null) {
                            processExec.destroy();
                        }
                        return str;
                    }
                    if (strSubstring2 != null && !strSubstring2.isEmpty()) {
                        String str2 = strSubstring2;
                        if (processExec != null) {
                            processExec.destroy();
                        }
                        return str2;
                    }
                }
                if (processExec != null) {
                    processExec.destroy();
                }
            } catch (IOException e2) {
                if (processExec != null) {
                    processExec.destroy();
                }
            } catch (Throwable th) {
                if (processExec != null) {
                    processExec.destroy();
                }
                throw th;
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0043, code lost:
    
        r13 = r0.indexOf(" ");
        r0 = r0.indexOf("\t");
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0058, code lost:
    
        if (r13 != (-1)) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x005d, code lost:
    
        if (r0 > 0) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0062, code lost:
    
        if (r13 <= 0) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0067, code lost:
    
        if (r0 <= 0) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006e, code lost:
    
        if (r0 >= r13) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0071, code lost:
    
        r13 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0075, code lost:
    
        r0 = r0.substring(0, r13).trim();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0087, code lost:
    
        if (r7 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x008a, code lost:
    
        r7.destroy();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0090, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a(int r6) {
        /*
            r0 = 0
            r7 = r0
            java.lang.Runtime r0 = java.lang.Runtime.getRuntime()     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            java.lang.String r1 = "df"
            java.lang.Process r0 = r0.exec(r1)     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r7 = r0
            r0 = r7
            java.io.InputStream r0 = r0.getInputStream()     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r8 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r1 = r0
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r3 = r2
            r4 = r8
            r3.<init>(r4)     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r1.<init>(r2)     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r9 = r0
            r0 = 0
            r10 = r0
            r0 = 0
            r12 = r0
        L26:
            r0 = r9
            java.lang.String r0 = r0.readLine()     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r1 = r0
            r11 = r1
            if (r0 == 0) goto L91
            r0 = r11
            java.lang.String r1 = "/dev"
            boolean r0 = r0.startsWith(r1)     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            if (r0 == 0) goto L26
            r0 = r12
            int r12 = r12 + 1
            r1 = r6
            if (r0 != r1) goto L26
            r0 = r11
            java.lang.String r1 = " "
            int r0 = r0.indexOf(r1)     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r13 = r0
            r0 = r11
            java.lang.String r1 = "\t"
            int r0 = r0.indexOf(r1)     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r14 = r0
            r0 = r13
            r1 = -1
            if (r0 != r1) goto L60
            r0 = r14
            if (r0 > 0) goto L71
        L60:
            r0 = r13
            if (r0 <= 0) goto L75
            r0 = r14
            if (r0 <= 0) goto L75
            r0 = r14
            r1 = r13
            if (r0 >= r1) goto L75
        L71:
            r0 = r14
            r13 = r0
        L75:
            r0 = r11
            r1 = 0
            r2 = r13
            java.lang.String r0 = r0.substring(r1, r2)     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            java.lang.String r0 = r0.trim()     // Catch: java.io.IOException -> L9c java.lang.Throwable -> La9
            r10 = r0
            r0 = r10
            r15 = r0
            r0 = r7
            if (r0 == 0) goto L8e
            r0 = r7
            r0.destroy()
        L8e:
            r0 = r15
            return r0
        L91:
            r0 = r7
            if (r0 == 0) goto Lb6
            r0 = r7
            r0.destroy()
            goto Lb6
        L9c:
            r8 = move-exception
            r0 = 0
            r9 = r0
            r0 = r7
            if (r0 == 0) goto La7
            r0 = r7
            r0.destroy()
        La7:
            r0 = r9
            return r0
        La9:
            r16 = move-exception
            r0 = r7
            if (r0 == 0) goto Lb3
            r0 = r7
            r0.destroy()
        Lb3:
            r0 = r16
            throw r0
        Lb6:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: f.f.a(int):java.lang.String");
    }

    public static String j() {
        String strTrim;
        try {
            Process processExec = Runtime.getRuntime().exec("/usr/sbin/diskutil info /");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
            do {
                String line = bufferedReader.readLine();
                if (line == null) {
                    processExec.destroy();
                    return null;
                }
                strTrim = line.trim();
            } while (!strTrim.startsWith("Volume UUID:"));
            String strTrim2 = W.b(strTrim, "Volume UUID:", "").trim();
            if (strTrim2.endsWith("\n")) {
                strTrim2 = strTrim2.substring(0, strTrim2.length() - 1);
            }
            processExec.destroy();
            if (strTrim2 != null && strTrim2.length() > 50) {
                strTrim2 = strTrim2.substring(0, 49);
            }
            return strTrim2;
        } catch (IOException e2) {
            return null;
        }
    }

    public static String a(String str) {
        String strSubstring;
        String strB = "";
        try {
            strB = b(str);
        } catch (Exception e2) {
        }
        if (strB != null && strB.length() > 2) {
            return strB;
        }
        try {
            File fileCreateTempFile = File.createTempFile("hdSerial", ".vbs");
            fileCreateTempFile.deleteOnExit();
            FileWriter fileWriter = new FileWriter(fileCreateTempFile);
            fileWriter.write("Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\nSet colDrives = objFSO.Drives\nSet objDrive = colDrives.item(\"" + str + "\")\nWscript.Echo objDrive.SerialNumber");
            fileWriter.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cscript //NoLogo " + fileCreateTempFile.getPath()).getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                strB = strB + line;
            }
            bufferedReader.close();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        if (strB != null) {
            strSubstring = strB.trim();
            if (strSubstring.length() < 8 || strSubstring.indexOf(" ") > 0) {
                strSubstring = "";
            }
        } else {
            strSubstring = "";
        }
        if (strSubstring.length() > 40) {
            strSubstring = strSubstring.substring(0, 39);
        }
        return strSubstring.trim();
    }

    public static String b(String str) {
        String strSubstring = "";
        try {
            if (!str.endsWith(CallSiteDescriptor.TOKEN_DELIMITER)) {
                str = str + CallSiteDescriptor.TOKEN_DELIMITER;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cmd /c vol " + str).getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                if (line.contains("Serial")) {
                    String strTrim = line.trim();
                    strSubstring = strTrim.substring(strTrim.lastIndexOf(" ") + 1);
                }
            }
            bufferedReader.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (strSubstring == null) {
            strSubstring = "";
        }
        if (strSubstring.length() > 40) {
            strSubstring = strSubstring.substring(0, 39);
        }
        return strSubstring.trim();
    }

    public static String k() throws UnknownHostException {
        byte[] bArrL = l();
        if (bArrL == null) {
            return null;
        }
        int[] iArrB = C0995c.b(bArrL);
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < iArrB.length; i2++) {
            sb.append(W.a(Integer.toHexString(iArrB[i2]).toUpperCase(), '0', 2));
            if (i2 < iArrB.length - 1) {
                sb.append(CallSiteDescriptor.TOKEN_DELIMITER);
            }
        }
        return sb.toString();
    }

    private static byte[] c(String str) {
        if (str == null || str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) == -1) {
            return new byte[0];
        }
        String[] strArrSplit = str.split(CallSiteDescriptor.TOKEN_DELIMITER);
        byte[] bArr = new byte[strArrSplit.length];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) Integer.parseInt(strArrSplit[i2], 16);
        }
        return bArr;
    }

    private static boolean a(byte[] bArr) {
        if (bArr == null || bArr.length <= 0 || (bArr[0] & 2) != 0 || bArr.length < 6) {
            return true;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length && i3 < 6; i3++) {
            if (bArr[i3] == 0 && bArr[i3] == 255) {
                i2++;
            }
            if (i2 > bArr.length - 3) {
                return true;
            }
        }
        return false;
    }

    public static byte[] l() throws UnknownHostException {
        byte[] hardwareAddress;
        if (I.d()) {
            try {
                byte[] bArrF = f();
                if (bArrF != null) {
                    if (bArrF.length > 5) {
                        return bArrF;
                    }
                }
            } catch (Exception e2) {
            }
        }
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                byte[] hardwareAddress2 = networkInterfaces.nextElement2().getHardwareAddress();
                if (!a(hardwareAddress2)) {
                    return hardwareAddress2;
                }
            }
        } catch (SocketException e3) {
            C.c("Not found, try alternative.");
        }
        try {
            Enumeration<NetworkInterface> networkInterfaces2 = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces2.hasMoreElements()) {
                try {
                    hardwareAddress = networkInterfaces2.nextElement2().getHardwareAddress();
                } catch (SocketException e4) {
                }
                if (!a(hardwareAddress)) {
                    return hardwareAddress;
                }
            }
        } catch (SocketException e5) {
        } catch (UnknownHostException e6) {
        }
        try {
            NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            if (byInetAddress == null) {
                return null;
            }
            return byInetAddress.getHardwareAddress();
        } catch (SocketException e7) {
            throw new UnknownHostException("no mac found");
        }
    }

    private static File m() throws IOException {
        File file = new File(System.getenv("SystemRoot"), "System32" + File.separatorChar + "wbem");
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        throw new IOException('\"' + file.getAbsolutePath() + "\" does not exist or is not a directory!");
    }
}
