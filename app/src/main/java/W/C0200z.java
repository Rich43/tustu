package W;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.security.x509.X509CertImpl;

/* renamed from: W.z, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/z.class */
public class C0200z {
    public static String a(String str) {
        return a(new File(str));
    }

    public static String a(File file) {
        String[] strArrB = b(file);
        if (strArrB == null || strArrB.length == 0) {
            return null;
        }
        return strArrB[0];
    }

    public static String[] b(File file) {
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            bH.C.c("File Not Found for signature: " + file.getAbsolutePath());
            return null;
        }
        String strSubstring = null;
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReaderB = null;
        try {
            try {
                bufferedReaderB = C0193s.b(file);
                int i2 = -1;
                while (true) {
                    String line = bufferedReaderB.readLine();
                    String strSubstring2 = line;
                    if (line == null) {
                        break;
                    }
                    if (!strSubstring2.startsWith("<versionInfo") && strSubstring2.contains(";")) {
                        strSubstring2 = strSubstring2.substring(0, strSubstring2.indexOf(";"));
                    }
                    if (strSubstring2.contains(X509CertImpl.SIGNATURE) || strSubstring2.contains("firmwareSignature")) {
                        int iIndexOf = strSubstring2.contains("signature=") ? strSubstring2.indexOf(PdfOps.DOUBLE_QUOTE__TOKEN, strSubstring2.indexOf("signature=")) + 1 : strSubstring2.indexOf(PdfOps.DOUBLE_QUOTE__TOKEN, strSubstring2.indexOf("ignature")) + 1;
                        int iIndexOf2 = strSubstring2.indexOf(PdfOps.DOUBLE_QUOTE__TOKEN, iIndexOf + 1);
                        if (iIndexOf == 0) {
                            strSubstring = strSubstring2.substring(strSubstring2.indexOf("=") + 1, strSubstring2.length()).trim();
                            if (strSubstring.contains(";")) {
                                strSubstring = strSubstring.substring(0, strSubstring.indexOf(";"));
                            }
                        } else {
                            try {
                                strSubstring = strSubstring2.substring(iIndexOf, iIndexOf2);
                            } catch (Exception e2) {
                            }
                        }
                        if (strSubstring != null) {
                            if (strSubstring.length() == 1 && strSubstring.getBytes()[0] == 20) {
                                strSubstring = "20";
                            }
                            arrayList.add(strSubstring);
                            i2 = 0;
                        }
                    }
                    if (i2 > 4) {
                        break;
                    }
                    if (i2 != -1) {
                        i2++;
                    }
                }
                String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
                if (bufferedReaderB != null) {
                    try {
                        bufferedReaderB.close();
                    } catch (Exception e3) {
                        Logger.getLogger(C0200z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                }
                return strArr;
            } catch (Exception e4) {
                e4.printStackTrace();
                if (bufferedReaderB != null) {
                    try {
                        bufferedReaderB.close();
                    } catch (Exception e5) {
                        Logger.getLogger(C0200z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            if (bufferedReaderB != null) {
                try {
                    bufferedReaderB.close();
                } catch (Exception e6) {
                    Logger.getLogger(C0200z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                }
            }
            throw th;
        }
    }

    public static double c(File file) {
        if (file == null) {
            return -1.0d;
        }
        if (!file.exists()) {
            bH.C.c("File Not Found for iniVersion: " + file.getAbsolutePath());
            return -1.0d;
        }
        String strTrim = null;
        BufferedReader bufferedReaderB = null;
        try {
            try {
                bufferedReaderB = C0193s.b(file);
                int i2 = -1;
                while (true) {
                    String line = bufferedReaderB.readLine();
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
                if (bufferedReaderB != null) {
                    try {
                        bufferedReaderB.close();
                    } catch (Exception e2) {
                        Logger.getLogger(C0200z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                return d2;
            } catch (Exception e3) {
                e3.printStackTrace();
                if (bufferedReaderB != null) {
                    try {
                        bufferedReaderB.close();
                    } catch (Exception e4) {
                        Logger.getLogger(C0200z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    }
                }
                return -1.0d;
            }
        } catch (Throwable th) {
            if (bufferedReaderB != null) {
                try {
                    bufferedReaderB.close();
                } catch (Exception e5) {
                    Logger.getLogger(C0200z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0071, code lost:
    
        r0 = r8.indexOf(org.icepdf.core.util.PdfOps.DOUBLE_QUOTE__TOKEN, r8.indexOf("viewName=")) + 1;
        r6 = r8.substring(r0, r8.indexOf(org.icepdf.core.util.PdfOps.DOUBLE_QUOTE__TOKEN, r0 + 1)).trim();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String d(java.io.File r5) {
        /*
            Method dump skipped, instructions count: 268
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: W.C0200z.d(java.io.File):java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x008f, code lost:
    
        r0 = r9.indexOf(org.icepdf.core.util.PdfOps.DOUBLE_QUOTE__TOKEN, r9.indexOf(r0)) + 1;
        r7 = r9.substring(r0, r9.indexOf(org.icepdf.core.util.PdfOps.DOUBLE_QUOTE__TOKEN, r0)).trim();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a(java.io.File r5, java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 302
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: W.C0200z.a(java.io.File, java.lang.String):java.lang.String");
    }

    public static ArrayList a(File file, String str, FileFilter fileFilter) {
        ArrayList arrayList = new ArrayList();
        File[] fileArrListFiles = fileFilter == null ? file.listFiles() : file.listFiles(fileFilter);
        if (fileArrListFiles != null) {
            String lowerCase = str.toLowerCase();
            C0171aa c0171aa = new C0171aa();
            for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                if (fileArrListFiles[i2].isFile() && fileArrListFiles[i2].getName().toLowerCase().endsWith(lowerCase)) {
                    try {
                        arrayList.add(c0171aa.b(fileArrListFiles[i2]));
                    } catch (V.g e2) {
                        bH.C.d("Failed to get MsqSummary for " + ((Object) fileArrListFiles[i2]) + "\nReported Error:" + e2.getMessage());
                    }
                }
            }
        }
        return arrayList;
    }

    public static String b(String str) {
        String strTrim = bH.W.b(bH.W.b(bH.W.b(bH.W.b(bH.W.e(str), " ", "").trim(), "*", "").trim(), FXMLLoader.ESCAPE_PREFIX, ""), "/", "").trim();
        return str.startsWith("Monster") ? strTrim + ".ecu" : strTrim + ".ini";
    }
}
