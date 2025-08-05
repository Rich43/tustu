package ak;

import W.C0187m;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ak/aI.class */
public class aI extends C0546f {

    /* renamed from: a, reason: collision with root package name */
    List f4665a;

    /* renamed from: b, reason: collision with root package name */
    boolean f4666b;

    /* renamed from: H, reason: collision with root package name */
    boolean f4667H;

    public aI() {
        super(",", false);
        this.f4665a = new ArrayList();
        this.f4666b = true;
        this.f4667H = false;
        this.f4665a.add(",");
        this.f4665a.add(";");
        this.f4665a.add("\",\"");
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f1989E;
    }

    @Override // ak.C0546f
    protected int b(String str) {
        int i2 = 0;
        W.ah ahVar = null;
        try {
            try {
                this.f4824h = new File(str);
                ahVar = new W.ah(new InputStreamReader(new FileInputStream(this.f4824h)));
                String strA = ahVar.a();
                while (strA != null && !strA.startsWith("Marker")) {
                    strA = ahVar.a();
                    if (strA.startsWith(",TIME") || strA.startsWith(",MARKE")) {
                        break;
                    }
                    i2++;
                }
                this.f4822f = a(ahVar.a(), this.f4665a);
                int i3 = i2;
                if (ahVar != null) {
                    try {
                        ahVar.close();
                    } catch (IOException e2) {
                        Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                return i3;
            } catch (FileNotFoundException e3) {
                throw new V.a("Unable to open file for reading:\n" + str);
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

    /* JADX WARN: Multi-variable type inference failed */
    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        ArrayList arrayList;
        ArrayList arrayList2 = new ArrayList();
        HashSet hashSet = new HashSet();
        if (this.f4823g.isEmpty()) {
            while (true) {
                try {
                    String strL = l();
                    if (c(strL)) {
                        break;
                    }
                    arrayList2.add(strL);
                } catch (V.a e2) {
                    e2.printStackTrace();
                    throw new V.a("No Valid Data found in file");
                } catch (IOException e3) {
                    e3.printStackTrace();
                    throw new V.a("IO Error reading header rows from file.");
                }
            }
            a(true);
            int i2 = arrayList2.size() == 4 ? 0 + 1 : 0;
            int i3 = i2;
            int i4 = i2 + 1;
            ArrayList arrayList3 = new ArrayList(Arrays.asList(((String) arrayList2.get(i3)).split(Pattern.quote(this.f4822f))));
            int i5 = i4 + 1;
            ArrayList arrayList4 = new ArrayList(Arrays.asList(((String) arrayList2.get(i4)).split(Pattern.quote(this.f4822f))));
            if (arrayList2.size() > i5) {
                int i6 = i5 + 1;
                arrayList = new ArrayList(Arrays.asList(((String) arrayList2.get(i5)).split(Pattern.quote(this.f4822f))));
            } else {
                arrayList = new ArrayList();
                for (int i7 = 0; i7 < arrayList4.size(); i7++) {
                    if (((String) arrayList4.get(i7)).contains("(") && ((String) arrayList4.get(i7)).contains(")")) {
                        String str = (String) arrayList4.get(i7);
                        arrayList.add(str.substring(str.indexOf("(") + 1, str.indexOf(")")));
                        arrayList4.set(i7, str.substring(0, str.indexOf("(")));
                    } else {
                        arrayList.add("");
                    }
                }
            }
            int size = arrayList3.size() > arrayList4.size() ? arrayList3.size() : arrayList4.size();
            for (int size2 = arrayList3.size(); size2 < size; size2++) {
                arrayList3.add("");
            }
            for (int size3 = arrayList4.size(); size3 < size; size3++) {
                arrayList4.add("");
            }
            for (int size4 = arrayList.size(); size4 < size; size4++) {
                arrayList.add("");
            }
            String str2 = "TIME STAMP";
            int i8 = 1;
            for (int i9 = 0; i9 < size; i9++) {
                if (!this.f4667H && (((String) arrayList3.get(i9)).toUpperCase().startsWith("GROUP") || ((String) arrayList3.get(i9)).toUpperCase().startsWith("PID"))) {
                    arrayList3.set(i9, arrayList4.get(i9));
                } else if (!((String) arrayList4.get(i9)).isEmpty()) {
                    arrayList3.set(i9, ((String) arrayList3.get(i9)) + " " + ((String) arrayList4.get(i9)));
                }
                C0543c c0543c = new C0543c();
                if (i9 == 0) {
                    arrayList3.set(i9, ((String) arrayList3.get(i9)) + ((String) arrayList.get(i9)));
                    arrayList.set(i9, "");
                } else if (i9 == 1) {
                    str2 = (String) arrayList3.get(i9);
                    arrayList3.set(i9, ((String) arrayList3.get(i9)) + " " + ((String) arrayList.get(i9)));
                    arrayList.set(i9, PdfOps.s_TOKEN);
                } else if (str2.equals(arrayList3.get(i9))) {
                    int i10 = i8;
                    i8++;
                    arrayList3.set(i9, ((String) arrayList3.get(i9)) + (((String) arrayList.get(i9)).isEmpty() ? "" : " " + ((String) arrayList.get(i9))) + i10);
                    arrayList.set(i9, PdfOps.s_TOKEN);
                }
                String strTrim = ((String) arrayList3.get(i9)).trim();
                if (!hashSet.add(strTrim)) {
                    strTrim = strTrim + " " + (i8 - 1);
                }
                c0543c.a(strTrim);
                c0543c.b(((String) arrayList.get(i9)).trim());
                this.f4823g.add(c0543c);
            }
        }
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            C0543c c0543c2 = (C0543c) it.next();
            c0543c2.a(l(c0543c2.a()));
        }
        ArrayList arrayList5 = new ArrayList();
        Iterator it2 = this.f4823g.iterator();
        while (it2.hasNext()) {
            arrayList5.add(it2.next());
        }
        this.f4832p = 0;
        return arrayList5.iterator();
    }

    private String l(String str) {
        return bH.W.b(bH.W.b(bH.W.b(bH.W.b(str, " - ", " "), LanguageTag.SEP, " "), "(", ""), ")", "");
    }

    @Override // ak.C0546f
    protected String l() throws V.f {
        if (p()) {
            a(false);
        } else {
            this.f4831o = this.f4830n;
            do {
                this.f4830n = this.f4821e.a();
                if (this.f4830n != null && this.f4830n.startsWith(this.f4822f) && c(this.f4830n)) {
                    this.f4830n = "0" + this.f4830n;
                }
                if (this.f4830n == null) {
                    break;
                }
            } while (this.f4830n.isEmpty());
            this.f4832p++;
            if (this.f4831o == null) {
                throw new V.f("No records available.");
            }
            if (this.f4823g.size() > 0 && this.f4831o.trim().equals("")) {
                this.f4831o = "MARK Corrupt file blank record";
                System.out.println("Found a bad row");
            }
            if (this.f4832p < 2) {
                this.f4666b = this.f4830n.startsWith(this.f4822f);
            }
        }
        return this.f4831o;
    }

    @Override // ak.C0546f
    protected String a(String str, boolean z2) throws C0187m {
        String strA = super.a(str, z2);
        if (strA.contains(CallSiteDescriptor.TOKEN_DELIMITER)) {
            String strSubstring = strA.substring(0, strA.indexOf(CallSiteDescriptor.TOKEN_DELIMITER));
            strA = strSubstring.substring(0, strSubstring.lastIndexOf(this.f4822f) + 1) + strA.substring(strA.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1);
        }
        return strA;
    }

    @Override // ak.C0546f
    protected void e(String str) throws C0187m {
        if (this.f4666b && str.contains(this.f4822f) && !str.startsWith("0" + this.f4822f)) {
            a(true);
            this.f4831o = this.f4831o.substring(1);
            throw new C0187m(str.substring(0, str.indexOf(this.f4822f)), false);
        }
    }
}
