package ak;

import W.C0187m;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* renamed from: ak.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/f.class */
public class C0546f extends W.V {

    /* renamed from: f, reason: collision with root package name */
    String f4822f;

    /* renamed from: r, reason: collision with root package name */
    protected String f4835r;

    /* renamed from: s, reason: collision with root package name */
    protected String f4836s;

    /* renamed from: u, reason: collision with root package name */
    boolean f4839u;

    /* renamed from: q, reason: collision with root package name */
    public static boolean f4834q = true;

    /* renamed from: y, reason: collision with root package name */
    protected static final String[] f4843y = {"ON", "ACTIVE", "HIGH", "SUCCESS", "YES", "TOO HIGH", "OK", Constants._TAG_Y, "TRUE"};

    /* renamed from: z, reason: collision with root package name */
    protected static final String[] f4844z = {"OFF", "INACTIVE", "LOW", "FAILURE", "NO", "TOO LOW", "FALSE"};

    /* renamed from: A, reason: collision with root package name */
    protected static final String[] f4845A = {"Timestamp (mS)", "Elapsed Time", "Time/s", "Time Line", "Offset", "timestamp", "Section Time"};

    /* renamed from: B, reason: collision with root package name */
    protected static final String[] f4846B = {"Timestamp (mS)", "Time Line", "time_ms"};

    /* renamed from: e, reason: collision with root package name */
    W.ah f4821e = null;

    /* renamed from: g, reason: collision with root package name */
    ArrayList f4823g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    File f4824h = null;

    /* renamed from: i, reason: collision with root package name */
    float[] f4825i = null;

    /* renamed from: j, reason: collision with root package name */
    protected String f4826j = "UTF-8";

    /* renamed from: k, reason: collision with root package name */
    int f4827k = -1;

    /* renamed from: l, reason: collision with root package name */
    int f4828l = -1;

    /* renamed from: m, reason: collision with root package name */
    long f4829m = -1;

    /* renamed from: n, reason: collision with root package name */
    String f4830n = null;

    /* renamed from: o, reason: collision with root package name */
    String f4831o = null;

    /* renamed from: p, reason: collision with root package name */
    int f4832p = 0;

    /* renamed from: a, reason: collision with root package name */
    private boolean f4833a = false;

    /* renamed from: t, reason: collision with root package name */
    protected boolean f4837t = false;

    /* renamed from: b, reason: collision with root package name */
    private boolean f4838b = false;

    /* renamed from: v, reason: collision with root package name */
    protected boolean f4840v = false;

    /* renamed from: w, reason: collision with root package name */
    protected String f4841w = null;

    /* renamed from: x, reason: collision with root package name */
    HashMap f4842x = null;

    /* renamed from: C, reason: collision with root package name */
    protected boolean f4847C = false;

    /* renamed from: D, reason: collision with root package name */
    protected boolean f4848D = false;

    /* renamed from: E, reason: collision with root package name */
    protected boolean f4849E = false;

    /* renamed from: F, reason: collision with root package name */
    protected int f4850F = 100;

    /* renamed from: G, reason: collision with root package name */
    protected boolean f4851G = false;

    public C0546f(String str, boolean z2) {
        this.f4822f = m();
        this.f4835r = this.f4822f + this.f4822f;
        this.f4836s = this.f4822f + " " + this.f4822f;
        this.f4822f = str;
        this.f4839u = z2;
    }

    @Override // W.V
    public boolean a(String str) throws V.a {
        int iB = b(str);
        if (iB > 0) {
            this.f4841w = a(str, iB);
        }
        try {
            this.f4824h = new File(str);
            FileInputStream fileInputStream = new FileInputStream(this.f4824h);
            byte[] bArr = new byte[2];
            fileInputStream.read(bArr);
            if (bArr[0] == -1 && bArr[1] == -2) {
                this.f4826j = "UTF-16LE";
                bH.C.c(this.f4826j + " detected.");
            } else if (bArr[0] == -2 && bArr[1] == -1) {
                this.f4826j = FastInfosetSerializer.UTF_16BE;
                bH.C.c(this.f4826j + " detected.");
            } else if (bArr[0] == -17 && bArr[1] == -69) {
                byte[] bArr2 = new byte[1];
                fileInputStream.read(bArr2);
                this.f4826j = "UTF-8";
                if (bArr2[0] == -65) {
                    bH.C.c(this.f4826j + " detected.");
                }
            } else {
                try {
                    fileInputStream.close();
                } catch (Exception e2) {
                }
                fileInputStream = new FileInputStream(this.f4824h);
            }
            this.f4821e = new W.ah(new InputStreamReader(fileInputStream, this.f4826j));
            String str2 = null;
            for (int i2 = 0; i2 <= iB; i2++) {
                str2 = this.f4830n;
                this.f4830n = this.f4821e.a();
            }
            this.f4837t = !this.f4822f.equals("\t") || (iB > 2 && (str2 == null || !str2.trim().equals(PdfOps.DOUBLE_QUOTE__TOKEN)));
            return true;
        } catch (FileNotFoundException e3) {
            throw new V.a("Unable to open file for reading:\n" + str);
        } catch (IOException e4) {
            throw new V.a("Unable to read from file:\n" + str);
        }
    }

    @Override // W.V
    public Iterator b() throws V.a {
        while (this.f4823g.isEmpty()) {
            try {
                String strL = l();
                if (strL.startsWith("\"Elapsed Time\"")) {
                    strL = bH.W.b(strL, PdfOps.DOUBLE_QUOTE__TOKEN, "");
                }
                int i2 = 0;
                if (!strL.startsWith(".") && !strL.startsWith("Data Filename:")) {
                    String[] strArrN = n();
                    boolean z2 = strArrN == null || strArrN.length == 0;
                    int i3 = 0;
                    if (!z2) {
                        int length = strArrN.length;
                        int i4 = 0;
                        while (true) {
                            if (i4 >= length) {
                                break;
                            }
                            if (!strArrN[i4].isEmpty()) {
                                i3++;
                                if (i3 > 5) {
                                    z2 = false;
                                    break;
                                }
                            }
                            i4++;
                        }
                    }
                    for (String str : k(strL).split(Pattern.quote(this.f4822f))) {
                        C0543c c0543c = new C0543c();
                        String strTrim = str.trim();
                        if (strTrim.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || strTrim.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                            strTrim = bH.W.b(strTrim, PdfOps.DOUBLE_QUOTE__TOKEN, "").trim();
                        }
                        if (strTrim.isEmpty()) {
                            strTrim = "Col" + i2;
                        }
                        if (strTrim.equalsIgnoreCase("lambda")) {
                            strTrim = "Lambda";
                        }
                        if (strTrim.contains(LanguageTag.SEP)) {
                            strTrim = bH.W.b(strTrim, LanguageTag.SEP, " ");
                        }
                        if (strTrim.contains(sun.security.pkcs11.wrapper.Constants.INDENT)) {
                            strTrim = bH.W.b(strTrim, sun.security.pkcs11.wrapper.Constants.INDENT, " ");
                        }
                        if (bH.W.a(f4846B, strTrim)) {
                            c0543c.a(3);
                            c0543c.a("Time");
                            c0543c.b(PdfOps.s_TOKEN);
                            strTrim = "Time";
                            c0543c.a(0.001f);
                        } else if (bH.W.a(f4845A, strTrim)) {
                            c0543c.a(3);
                            c0543c.a("Time");
                            c0543c.b(PdfOps.s_TOKEN);
                            strTrim = "Time";
                        } else if (i2 == 0 && this.f4840v) {
                            c0543c.a(3);
                            c0543c.a("Time");
                            c0543c.b(PdfOps.s_TOKEN);
                            strTrim = "Time";
                        }
                        try {
                            if (!strTrim.startsWith("AP Info:")) {
                                if (!z2 && strArrN.length > i2) {
                                    String strB = strArrN[i2];
                                    if (strB.contains("[")) {
                                        strB = bH.W.b(bH.W.b(strB, "[", ""), "]", "");
                                    }
                                    c0543c.b(strB);
                                } else if (strTrim.lastIndexOf("[") > 1 && strTrim.lastIndexOf("]") > strTrim.lastIndexOf("[")) {
                                    int iLastIndexOf = strTrim.lastIndexOf("[");
                                    int iIndexOf = strTrim.indexOf("]", iLastIndexOf);
                                    c0543c.b(strTrim.substring(iLastIndexOf + 1, iIndexOf).trim());
                                    strTrim = (strTrim.substring(0, iLastIndexOf) + strTrim.substring(iIndexOf + 1, strTrim.length())).trim();
                                    this.f4837t = true;
                                } else if (strTrim.lastIndexOf("(") > 1 && strTrim.lastIndexOf(")") > strTrim.lastIndexOf("(")) {
                                    int iLastIndexOf2 = strTrim.lastIndexOf("(");
                                    int iIndexOf2 = strTrim.indexOf(")", iLastIndexOf2);
                                    c0543c.b(strTrim.substring(iLastIndexOf2 + 1, iIndexOf2).trim());
                                    strTrim = (strTrim.substring(0, iLastIndexOf2) + strTrim.substring(iIndexOf2 + 1, strTrim.length())).trim();
                                }
                            }
                            String strB2 = c0543c.b();
                            if (strB2 != null) {
                                if (strB2.equals("On/Off")) {
                                    c0543c.b(4);
                                } else if (strB2.equals("High/Low")) {
                                    c0543c.b(6);
                                } else if (strB2.equals("Active/Inactive") || strB2.equals("Act/Inact")) {
                                    c0543c.b(7);
                                } else if (strB2.equals("Yes/No")) {
                                    c0543c.b(5);
                                } else if (strB2.equals("True/False")) {
                                    c0543c.b(8);
                                }
                            }
                        } catch (Exception e2) {
                            bH.C.c("Thought I could parse units, but it failed on field \"" + strTrim + PdfOps.DOUBLE_QUOTE__TOKEN);
                        }
                        while (strTrim.endsWith("*")) {
                            strTrim = strTrim.substring(0, strTrim.length() - 1);
                        }
                        if (strTrim.trim().equals(SchemaSymbols.ATTVAL_TIME)) {
                            strTrim = "Time";
                        }
                        if (strTrim.contains(CallSiteDescriptor.OPERATOR_DELIMITER)) {
                            strTrim = strTrim.substring(0, strTrim.indexOf(124));
                            this.f4837t = true;
                        }
                        String strTrim2 = strTrim.trim();
                        String str2 = strTrim2;
                        for (int i5 = 0; i5 < 100 && i(str2); i5++) {
                            str2 = strTrim2 + i5;
                        }
                        c0543c.a(str2);
                        C0543c c0543cA = a(c0543c);
                        if (c0543cA != null) {
                            this.f4823g.add(c0543cA);
                            i2++;
                        }
                    }
                }
            } catch (V.a e3) {
                e3.printStackTrace();
                throw new V.a("No Valid Data found in file");
            } catch (IOException e4) {
                e4.printStackTrace();
                throw new V.a("IO Error reading header rows from file.");
            }
        }
        this.f4835r = this.f4822f + this.f4822f;
        this.f4836s = this.f4822f + " " + this.f4822f;
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList.iterator();
    }

    protected String[] n() {
        String[] strArrSplit = null;
        String strL = null;
        try {
            int i2 = 0;
            strL = l();
            strArrSplit = strL.split(Pattern.quote(this.f4822f));
            for (String str : strArrSplit) {
                String strTrim = bH.W.b(str, PdfOps.DOUBLE_QUOTE__TOKEN, "").trim();
                if (strTrim.isEmpty()) {
                    strArrSplit[i2] = strTrim;
                    i2++;
                } else {
                    if (bH.H.a(strTrim) || !Float.isNaN(g(strTrim))) {
                        a(true);
                        c();
                        a(true);
                        return null;
                    }
                    strArrSplit[i2] = strTrim;
                    i2++;
                }
            }
        } catch (V.f e2) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (IOException e3) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        } catch (Exception e4) {
            bH.C.a("Failed to get units from this row:\n" + strL);
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        return strArrSplit;
    }

    public String o() {
        return this.f4822f;
    }

    protected C0543c a(C0543c c0543c) {
        if (c0543c.a().toLowerCase().contains("latitude") || c0543c.a().toLowerCase().contains("longitude") || c0543c.a().toLowerCase().contains("itude") || c0543c.a().equals("Lat") || c0543c.a().equals("Lon")) {
            c0543c.a(7);
        }
        return c0543c;
    }

    protected String d(String str) {
        return bH.W.b(bH.W.b(str, this.f4835r, this.f4836s), this.f4835r, this.f4836s);
    }

    String a(ArrayList arrayList, String str) {
        while (b(arrayList, str)) {
            str = l(str);
        }
        return str;
    }

    private String l(String str) {
        int iLastIndexOf = str.lastIndexOf(".");
        if (iLastIndexOf == -1) {
            return str + ".0";
        }
        String strSubstring = str.substring(iLastIndexOf + 1);
        if (!bH.H.a(strSubstring)) {
            return str + ".0";
        }
        try {
            return str.substring(0, iLastIndexOf + 1) + (Integer.valueOf(strSubstring).intValue() + 1);
        } catch (NumberFormatException e2) {
            return strSubstring + "." + ((int) (Math.random() * 100.0d));
        }
    }

    private boolean b(ArrayList arrayList, String str) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((C0543c) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }

    protected int a(String str, String str2) {
        String strA;
        int i2 = -1;
        W.ah ahVar = null;
        try {
            try {
                this.f4824h = new File(str);
                ahVar = new W.ah(new InputStreamReader(new FileInputStream(this.f4824h)));
                do {
                    strA = ahVar.a();
                    if (strA == null) {
                        break;
                    }
                    i2++;
                } while (!strA.startsWith(str2));
                int i3 = i2;
                if (ahVar != null) {
                    try {
                        ahVar.close();
                    } catch (IOException e2) {
                        Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                return i3;
            } catch (IOException e3) {
                Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                if (ahVar != null) {
                    try {
                        ahVar.close();
                    } catch (IOException e4) {
                        Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    }
                }
                return -1;
            }
        } catch (Throwable th) {
            if (ahVar != null) {
                try {
                    ahVar.close();
                } catch (IOException e5) {
                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x02d7, code lost:
    
        java.util.logging.Logger.getLogger(ak.C0546f.class.getName()).log(java.util.logging.Level.SEVERE, (java.lang.String) null, (java.lang.Throwable) r20);
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x02eb, code lost:
    
        r0 = r14 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x02f3, code lost:
    
        if (r0 == null) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x02f6, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x02fe, code lost:
    
        r20 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0300, code lost:
    
        java.util.logging.Logger.getLogger(ak.C0546f.class.getName()).log(java.util.logging.Level.SEVERE, (java.lang.String) null, (java.lang.Throwable) r20);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01d8, code lost:
    
        if (r7.f4839u == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01db, code lost:
    
        r7.f4822f = h(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x01e4, code lost:
    
        r0 = d(r9, r7.f4822f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01f2, code lost:
    
        if (r14 < 4) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01f8, code lost:
    
        if (r0 <= 5) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x020c, code lost:
    
        if (java.lang.Math.abs(d(r13, r7.f4822f) - r0) >= 2) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0219, code lost:
    
        if (b(r13, r7.f4822f) == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x021c, code lost:
    
        r0 = r14 - 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0224, code lost:
    
        if (r0 == null) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0227, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x022f, code lost:
    
        r20 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0231, code lost:
    
        java.util.logging.Logger.getLogger(ak.C0546f.class.getName()).log(java.util.logging.Level.SEVERE, (java.lang.String) null, (java.lang.Throwable) r20);
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0248, code lost:
    
        if (r14 < 3) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x024e, code lost:
    
        if (r0 <= 5) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0262, code lost:
    
        if (java.lang.Math.abs(d(r12, r7.f4822f) - r0) >= 2) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x026f, code lost:
    
        if (b(r12, r7.f4822f) == false) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0272, code lost:
    
        r0 = r14 - 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x027a, code lost:
    
        if (r0 == null) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x027d, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0285, code lost:
    
        r20 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0287, code lost:
    
        java.util.logging.Logger.getLogger(ak.C0546f.class.getName()).log(java.util.logging.Level.SEVERE, (java.lang.String) null, (java.lang.Throwable) r20);
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x029e, code lost:
    
        if (r0 < 5) goto L103;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x02b2, code lost:
    
        if (java.lang.Math.abs(d(r11, r7.f4822f) - r0) >= 2) goto L103;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x02bf, code lost:
    
        if (b(r11, r7.f4822f) == false) goto L103;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x02c2, code lost:
    
        r0 = r14 - 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02ca, code lost:
    
        if (r0 == null) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x02cd, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x02d5, code lost:
    
        r20 = move-exception;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected int b(java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 883
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ak.C0546f.b(java.lang.String):int");
    }

    protected String a(String str, int i2) {
        StringBuilder sb = new StringBuilder();
        W.ah ahVar = null;
        try {
            try {
                try {
                    this.f4824h = new File(str);
                    ahVar = new W.ah(new InputStreamReader(new FileInputStream(this.f4824h)));
                    for (int i3 = 0; i3 < i2; i3++) {
                        sb.append(ahVar.a());
                        if (i3 < i2 - 1) {
                            sb.append('\n');
                        }
                    }
                    String string = sb.toString();
                    if (ahVar != null) {
                        try {
                            ahVar.close();
                        } catch (IOException e2) {
                            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                        }
                    }
                    return string;
                } catch (FileNotFoundException e3) {
                    throw new V.a("Unable to open file for reading:\n" + str);
                }
            } catch (IOException e4) {
                throw new V.a("Unable to read from file:\n" + str);
            }
        } catch (Throwable th) {
            if (ahVar != null) {
                try {
                    ahVar.close();
                } catch (IOException e5) {
                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
            }
            throw th;
        }
    }

    protected String l() throws V.f {
        if (p()) {
            a(false);
            return this.f4831o;
        }
        this.f4831o = this.f4830n;
        do {
            this.f4830n = this.f4821e.a();
            if (this.f4830n == null || !this.f4830n.isEmpty()) {
                break;
            }
        } while (this.f4830n.indexOf(this.f4822f) == -1);
        this.f4832p++;
        if (this.f4831o == null) {
            throw new V.f("No records available.");
        }
        if (this.f4830n != null) {
            if (this.f4830n.startsWith("\"Firmware ID\"")) {
                this.f4830n = null;
            } else if (this.f4830n.startsWith("[FooterData]")) {
                String str = this.f4831o;
                q();
                this.f4830n = null;
                this.f4831o = str;
            } else if (this.f4830n.startsWith("Raw Log Data")) {
                String str2 = this.f4831o;
                do {
                    this.f4830n = this.f4821e.a();
                    if (this.f4830n == null) {
                        break;
                    }
                } while (!this.f4830n.startsWith("[FooterData]"));
                if (this.f4830n != null && this.f4830n.startsWith("[FooterData]")) {
                    q();
                }
                this.f4830n = null;
                this.f4831o = str2;
            }
        }
        if (this.f4823g.size() > 0 && this.f4831o.trim().equals("")) {
            this.f4831o = "MARK Corrupt file blank record";
            System.out.println("Found a bad row");
        }
        return this.f4831o;
    }

    protected void e(String str) throws C0187m {
        if (str.indexOf("MARK") > -1) {
            throw new C0187m(str.trim());
        }
    }

    @Override // W.V
    public float[] c() throws V.a {
        String strB;
        boolean zP = p();
        try {
            if (this.f4837t && this.f4832p >= 500 && k()) {
                throw new V.a("This Edition is limited to loading 500 rows of data. \nPlease Register to load large log files.");
            }
            String strA = a(l(), zP);
            String str = this.f4822f;
            if (this.f4829m == -1) {
                this.f4829m = this.f4824h.length() / (strA.length() + 3);
            }
            aD aDVar = new aD(strA, str);
            aDVar.b(this.f4838b);
            aDVar.a(this.f4847C);
            this.f4825i = new float[aDVar.c()];
            for (int i2 = 0; i2 < this.f4825i.length && i2 < this.f4823g.size(); i2++) {
                try {
                    strB = aDVar.b().trim();
                } catch (Exception e2) {
                    bH.C.c("Error Parsing record:\n" + strA);
                    strB = "0";
                    e2.printStackTrace();
                }
                if (((C0543c) this.f4823g.get(i2)).f() == 0) {
                    try {
                        if (this.f4848D || (this.f4832p < this.f4850F && strB.contains(","))) {
                            if (!this.f4848D) {
                                this.f4848D = true;
                                bH.C.d("Setting commas for decimals.");
                            }
                            if (strB.contains(".") && strB.contains(",")) {
                                strB = bH.W.b(strB, ".", "");
                            }
                            strB = bH.W.b(strB, ",", ".");
                        }
                        if (strB.isEmpty() || strB.equals("N/A") || strB.equals("NA") || strB.equals("####")) {
                            this.f4825i[i2] = Float.NaN;
                            strB = "";
                        } else {
                            this.f4825i[i2] = Float.parseFloat(strB);
                            if (!this.f4851G) {
                                this.f4851G = true;
                            }
                        }
                    } catch (NumberFormatException e3) {
                        this.f4825i[i2] = Float.NaN;
                    }
                } else {
                    this.f4825i[i2] = Float.NaN;
                }
                if (Float.isNaN(this.f4825i[i2])) {
                    if (strB.isEmpty()) {
                        this.f4825i[i2] = Float.NaN;
                    } else if (strB.indexOf(58) != -1) {
                        this.f4825i[i2] = g(strB);
                    } else if (strB.startsWith("0x") || strB.startsWith("0X")) {
                        try {
                            this.f4825i[i2] = Integer.parseInt(strB.substring(2), 16);
                        } catch (Exception e4) {
                            this.f4825i[i2] = Float.NaN;
                        }
                    } else if (bH.W.a(f4844z, strB)) {
                        this.f4825i[i2] = 0.0f;
                        if (this.f4823g.size() > i2 && ((C0543c) this.f4823g.get(i2)).f() == 0) {
                            a((C0543c) this.f4823g.get(i2), strB);
                        }
                    } else if (bH.W.a(f4843y, strB)) {
                        this.f4825i[i2] = 1.0f;
                        if (this.f4823g.size() > i2 && ((C0543c) this.f4823g.get(i2)).f() == 0) {
                            a((C0543c) this.f4823g.get(i2), strB);
                        }
                    } else if (f(strB)) {
                        this.f4825i[i2] = Float.parseFloat(strB.substring(0, 7) + strB.substring(8));
                    } else if (this.f4851G && !strB.trim().isEmpty() && !strB.trim().equals("NA")) {
                        this.f4825i[i2] = b(i2).a(strB).floatValue();
                    }
                }
                if (this.f4849E || (this.f4832p < this.f4850F && (strB.contains("S ") || strB.contains("E ") || strB.contains("N ") || strB.contains("W ")))) {
                    if (!this.f4849E) {
                        this.f4849E = true;
                    }
                    this.f4831o = bH.W.b(this.f4831o, "S ", LanguageTag.SEP);
                    this.f4831o = bH.W.b(this.f4831o, "E ", "");
                    this.f4831o = bH.W.b(this.f4831o, "N ", "");
                    this.f4831o = bH.W.b(this.f4831o, "W ", LanguageTag.SEP);
                }
            }
            return this.f4825i;
        } catch (IOException e5) {
            e5.printStackTrace();
            throw new V.a("IO Error reading row from file on row " + this.f4832p + ".");
        }
    }

    protected String a(String str, boolean z2) throws C0187m {
        if (!z2) {
            try {
                e(str);
            } catch (C0187m e2) {
                if (!e2.a()) {
                    a(true);
                }
                throw e2;
            }
        }
        if (str.startsWith(this.f4822f)) {
            String str2 = "0";
            if (this.f4825i != null && this.f4825i.length > 0) {
                str2 = this.f4825i[0] + "";
            }
            str = str2 + str;
        }
        String strD = d(str);
        if (strD.endsWith(this.f4822f)) {
            strD = strD + " ";
        }
        if (strD.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            strD = strD.substring(1);
        }
        if (strD.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            strD = strD.substring(0, strD.length() - 1);
        }
        return strD;
    }

    protected boolean f(String str) {
        return str.length() == 10 && str.charAt(4) == '.' && str.charAt(7) == '.' && str.charAt(5) > '/' && str.charAt(5) < '2' && str.charAt(8) > '/' && str.charAt(8) < '4' && str.charAt(0) > '/' && str.charAt(0) < ':' && str.charAt(1) > '/' && str.charAt(1) < ':' && str.charAt(2) > '/' && str.charAt(2) < ':' && str.charAt(3) > '/' && str.charAt(3) < ':' && str.charAt(6) > '/' && str.charAt(6) < ':' && str.charAt(9) > '/' && str.charAt(9) < ':';
    }

    protected void a(C0543c c0543c, String str) {
        if (str.equalsIgnoreCase("Yes") || str.equalsIgnoreCase("No")) {
            c0543c.b(5);
            return;
        }
        if (str.equalsIgnoreCase("High") || str.equalsIgnoreCase("Low")) {
            c0543c.b(6);
        } else if (str.equalsIgnoreCase("Active") || str.equalsIgnoreCase("Inactive")) {
            c0543c.b(7);
        } else {
            c0543c.b(4);
        }
    }

    protected float g(String str) {
        int i2 = 0;
        float f2 = 0.0f;
        float f3 = 0.0f;
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(str, CallSiteDescriptor.TOKEN_DELIMITER);
            if (stringTokenizer.countTokens() > 2) {
                String strTrim = stringTokenizer.nextToken().trim();
                if (strTrim.contains(" ")) {
                    strTrim = strTrim.substring(strTrim.lastIndexOf(" ") + 1);
                }
                i2 = Integer.parseInt(strTrim);
            }
            int i3 = Integer.parseInt(stringTokenizer.nextToken());
            if (stringTokenizer.hasMoreElements()) {
                String strTrim2 = stringTokenizer.nextToken().trim();
                int iIndexOf = strTrim2.indexOf("PM");
                if (iIndexOf != -1) {
                    i2 += 12;
                } else {
                    iIndexOf = strTrim2.indexOf("AM");
                }
                if (iIndexOf != -1) {
                    strTrim2 = strTrim2.substring(0, iIndexOf);
                }
                if (strTrim2.indexOf(" ") != -1) {
                    strTrim2 = strTrim2.substring(0, strTrim2.indexOf(" "));
                }
                f2 = Float.parseFloat(strTrim2);
            }
            if (stringTokenizer.hasMoreElements()) {
                try {
                    f3 = Float.parseFloat(stringTokenizer.nextToken()) / 1000.0f;
                } catch (NumberFormatException e2) {
                }
            }
            this.f4837t = true;
            return (i2 * 3600) + (i3 * 60) + f2 + f3;
        } catch (Exception e3) {
            return Float.NaN;
        }
    }

    @Override // W.V
    public boolean e() {
        if (this.f4830n != null && this.f4830n.trim().length() > 0) {
            return true;
        }
        try {
            this.f4830n = l();
            if (this.f4830n != null && this.f4830n.trim().length() > 0) {
                if (!this.f4830n.startsWith("[FooterData]")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    protected boolean b(String str, String str2) {
        try {
            if (str.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                str = str.substring(1);
            }
            int i2 = 0;
            aD aDVar = new aD(bH.W.b(str, str2, str2 + " ").trim(), str2);
            while (aDVar.a()) {
                if (aDVar.b().trim().length() == 0 && aDVar.a()) {
                    i2++;
                }
                if (i2 > 1) {
                    return false;
                }
            }
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    protected boolean c(String str, String str2) {
        try {
            if (str.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                str = str.substring(1);
            }
            String strSubstring = str.substring(0, str.indexOf(str2));
            if (strSubstring.trim().length() == 0 || strSubstring.contains(CallSiteDescriptor.TOKEN_DELIMITER)) {
                strSubstring = str.substring(str.indexOf(str2) + str2.length(), str.indexOf(str2, str.indexOf(str2) + str2.length()));
            }
            Double.parseDouble(str2.equals(",") ? strSubstring : strSubstring.replace(',', '.'));
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    protected boolean c(String str) {
        if (str == null || str.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) || str.startsWith(".") || str.startsWith("Data Filename:")) {
            return false;
        }
        return this.f4839u ? c(str, "\t") || c(str, ",") || c(str, CallSiteDescriptor.OPERATOR_DELIMITER) || c(str, ";") || c(str, "\",\"") || c(str, "\", \"") || c(str, " ") : c(str, this.f4822f);
    }

    protected String h(String str) {
        return a(str, (List) null);
    }

    protected String a(String str, List list) {
        int iD;
        int iD2;
        int iD3;
        int iD4;
        int iD5;
        String str2 = "\t";
        int iD6 = d(str, str2);
        int iD7 = d(str, "\", \"");
        if (iD7 > iD6) {
            iD6 = iD7;
            str2 = "\", \"";
        }
        if ((list == null || list.contains("\",\"")) && (iD = d(str, "\",\"")) > iD6) {
            iD6 = iD;
            str2 = "\",\"";
        }
        if ((list == null || list.contains(";")) && (iD2 = d(str, ";")) > iD6) {
            iD6 = iD2;
            str2 = ";";
        }
        if ((list == null || list.contains(",")) && (iD3 = d(str, ",")) > iD6) {
            iD6 = iD3;
            str2 = ",";
        }
        if ((list == null || list.contains(CallSiteDescriptor.OPERATOR_DELIMITER)) && (iD4 = d(str, CallSiteDescriptor.OPERATOR_DELIMITER)) > iD6) {
            iD6 = iD4;
            str2 = CallSiteDescriptor.OPERATOR_DELIMITER;
        }
        if ((list == null || list.contains(PdfOps.SINGLE_QUOTE_TOKEN)) && (iD5 = d(str, PdfOps.SINGLE_QUOTE_TOKEN)) > iD6) {
            iD6 = iD5;
            str2 = PdfOps.SINGLE_QUOTE_TOKEN;
        }
        String strReplaceAll = str.trim().replaceAll(" +", " ");
        if ((list == null || list.contains(" ")) && d(strReplaceAll, " ") > iD6) {
            str2 = " ";
        }
        return str2;
    }

    protected int d(String str, String str2) {
        int i2 = 0;
        boolean z2 = false;
        for (int i3 = 0; i3 < (str.length() - str2.length()) + 1; i3++) {
            if (str.charAt(i3) == '\"') {
                z2 = !z2;
            }
            if (str.substring(i3, i3 + str2.length()).equals(str2) && !z2) {
                i2++;
            }
        }
        return i2;
    }

    @Override // W.V
    public long d() {
        return this.f4829m;
    }

    @Override // W.V
    public void a() {
        try {
            this.f4821e.close();
        } catch (Exception e2) {
            bH.C.d("Closed file: " + this.f4824h.getName());
        }
    }

    protected String m() {
        return "\t";
    }

    protected boolean p() {
        return this.f4833a;
    }

    protected void a(boolean z2) {
        this.f4833a = z2;
    }

    @Override // W.V
    public boolean f() {
        return false;
    }

    protected boolean i(String str) {
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            if (((C0543c) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }

    protected void q() {
        this.f4831o = this.f4830n;
        int i2 = 0;
        if (this.f4842x == null) {
            this.f4842x = new HashMap();
        }
        do {
            this.f4830n = this.f4821e.a();
            if (this.f4830n != null) {
                if (this.f4830n.contains("=")) {
                    String[] strArrSplit = this.f4830n.split("=");
                    if (strArrSplit.length >= 2) {
                        this.f4842x.put(strArrSplit[0], strArrSplit[1]);
                    }
                } else {
                    int i3 = i2;
                    i2++;
                    this.f4842x.put("Data" + i3, this.f4830n);
                }
            }
            if (this.f4830n == null) {
                return;
            }
        } while (!this.f4830n.isEmpty());
    }

    @Override // W.V
    public String i() {
        return W.X.f1959a;
    }

    @Override // W.V
    public HashMap g() {
        return this.f4842x;
    }

    @Override // W.V
    public String h() {
        return this.f4841w;
    }

    public void j(String str) {
        this.f4826j = str;
    }

    protected String k(String str) {
        if (str.contains(PdfOps.DOUBLE_QUOTE__TOKEN) && this.f4822f.equals(",")) {
            boolean z2 = false;
            for (int i2 = 0; i2 < str.length(); i2++) {
                if (str.charAt(i2) == '\"') {
                    z2 = !z2;
                }
                if (z2 && str.charAt(i2) == ',') {
                    str = str.substring(0, i2) + str.substring(i2 + 1);
                }
            }
        }
        return str;
    }

    public void b(boolean z2) {
        this.f4838b = z2;
    }
}
