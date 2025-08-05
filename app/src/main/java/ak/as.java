package ak;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ak/as.class */
public class as extends C0546f {
    public as() {
        super(",", false);
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        String strL = null;
        while (this.f4823g.isEmpty()) {
            try {
                String strL2 = l();
                strL = l();
                String strL3 = l();
                int i2 = 0;
                String[] strArrSplit = strL2.split(Pattern.quote(this.f4822f));
                String[] strArrSplit2 = strL3.split(Pattern.quote(this.f4822f));
                for (int i3 = 0; i3 < strArrSplit.length; i3++) {
                    String str = strArrSplit[i3];
                    if (strArrSplit2.length > i3 && strArrSplit2[i3] != null) {
                        String strB = bH.W.b(strArrSplit2[i3], PdfOps.DOUBLE_QUOTE__TOKEN, "");
                        if (!strB.isEmpty()) {
                            str = strB;
                        }
                    }
                    C0543c c0543c = new C0543c();
                    String strTrim = str.trim();
                    if (strTrim.isEmpty()) {
                        strTrim = "Col" + i2;
                    }
                    if (strTrim.equalsIgnoreCase("lambda")) {
                        strTrim = "Lambda";
                    }
                    if (strTrim.contains(LanguageTag.SEP)) {
                        strTrim = bH.W.b(strTrim, LanguageTag.SEP, " ");
                    }
                    if (strTrim.contains(Constants.INDENT)) {
                        strTrim = bH.W.b(strTrim, Constants.INDENT, " ");
                    }
                    if (strTrim.equals("TimeStamp")) {
                        c0543c.a(3);
                        c0543c.a("Time");
                        c0543c.b(PdfOps.s_TOKEN);
                        strTrim = "Time";
                    }
                    if (strTrim.trim().equals(SchemaSymbols.ATTVAL_TIME)) {
                        strTrim = "Time";
                    }
                    this.f4837t = true;
                    String strTrim2 = strTrim.trim();
                    String str2 = strTrim2;
                    for (int i4 = 0; i4 < 100 && i(str2); i4++) {
                        str2 = strTrim2 + i4;
                    }
                    c0543c.a(str2);
                    C0543c c0543cA = a(c0543c);
                    if (c0543cA != null) {
                        this.f4823g.add(c0543cA);
                        i2++;
                    }
                }
            } catch (V.a e2) {
                e2.printStackTrace();
                throw new V.a("No Valid Data found in file");
            } catch (IOException e3) {
                e3.printStackTrace();
                throw new V.a("IO Error reading header rows from file.");
            }
        }
        this.f4835r = this.f4822f + this.f4822f;
        this.f4836s = this.f4822f + " " + this.f4822f;
        try {
            int i5 = 0;
            strL = l();
            for (String str3 : strL.split(Pattern.quote(this.f4822f))) {
                String strTrim3 = bH.W.b(str3, PdfOps.DOUBLE_QUOTE__TOKEN, "").trim();
                if (strTrim3 != null && strTrim3.equals("sec.ms")) {
                    strTrim3 = PdfOps.s_TOKEN;
                }
                if (i5 < this.f4823g.size()) {
                    String strB2 = ((C0543c) this.f4823g.get(i5)).b();
                    if (!strTrim3.isEmpty() || strB2 == null || strB2.isEmpty()) {
                        if (bH.H.a(strTrim3) || !Float.isNaN(g(strTrim3))) {
                            a(true);
                            c();
                            a(true);
                            break;
                        }
                        ((C0543c) this.f4823g.get(i5)).b(strTrim3);
                        if (strTrim3.equals("On/Off")) {
                            ((C0543c) this.f4823g.get(i5)).b(4);
                        } else if (strTrim3.equals("High/Low")) {
                            ((C0543c) this.f4823g.get(i5)).b(6);
                        } else if (strTrim3.equals("Active/Inactive") || strTrim3.equals("Act/Inact")) {
                            ((C0543c) this.f4823g.get(i5)).b(7);
                        } else if (strTrim3.equals("Yes/No")) {
                            ((C0543c) this.f4823g.get(i5)).b(5);
                        }
                        i5++;
                    } else {
                        i5++;
                    }
                }
            }
        } catch (V.f e4) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        } catch (IOException e5) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
        } catch (Exception e6) {
            bH.C.a("Failed to get units from this row:\n" + strL);
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList.iterator();
    }

    @Override // ak.C0546f
    protected int b(String str) {
        String str2 = "";
        String str3 = "";
        String str4 = "";
        String str5 = "";
        int i2 = 0;
        W.ah ahVar = null;
        try {
            try {
                this.f4824h = new File(str);
                W.ah ahVar2 = new W.ah(new InputStreamReader(new FileInputStream(this.f4824h)));
                String strA = ahVar2.a();
                if (strA == null) {
                    int i3 = 0 - 1;
                    if (ahVar2 != null) {
                        try {
                            ahVar2.close();
                        } catch (IOException e2) {
                            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                        }
                    }
                    return i3;
                }
                while (true) {
                    if (i2 != 0 && (str2 == null || str2.startsWith("TimeStamp"))) {
                        break;
                    }
                    str5 = str4;
                    str4 = str3;
                    str3 = str2;
                    str2 = strA;
                    i2++;
                    strA = ahVar2.a();
                }
                if (this.f4839u) {
                    this.f4822f = h(strA);
                }
                int iD = d(strA, this.f4822f);
                if (i2 >= 4 && d(str5, this.f4822f) == iD && b(str5, this.f4822f)) {
                    int i4 = i2 - 4;
                    if (ahVar2 != null) {
                        try {
                            ahVar2.close();
                        } catch (IOException e3) {
                            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                        }
                    }
                    return i4;
                }
                if (i2 >= 3 && d(str4, this.f4822f) == iD && b(str4, this.f4822f)) {
                    int i5 = i2 - 3;
                    if (ahVar2 != null) {
                        try {
                            ahVar2.close();
                        } catch (IOException e4) {
                            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        }
                    }
                    return i5;
                }
                if (d(str3, this.f4822f) == iD && b(str3, this.f4822f)) {
                    int i6 = i2 - 2;
                    if (ahVar2 != null) {
                        try {
                            ahVar2.close();
                        } catch (IOException e5) {
                            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                        }
                    }
                    return i6;
                }
                int i7 = i2 - 1;
                if (ahVar2 != null) {
                    try {
                        ahVar2.close();
                    } catch (IOException e6) {
                        Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                    }
                }
                return i7;
            } catch (FileNotFoundException e7) {
                throw new V.a("Unable to open file for reading:\n" + str);
            } catch (IOException e8) {
                throw new V.a("Unable to read from file:\n" + str);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    ahVar.close();
                } catch (IOException e9) {
                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e9);
                }
            }
            throw th;
        }
    }
}
