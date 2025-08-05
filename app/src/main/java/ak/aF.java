package ak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:ak/aF.class */
public class aF extends C0546f {
    public aF() {
        super("\t", false);
        this.f4837t = true;
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        Iterator itB = super.b();
        ArrayList arrayList = new ArrayList();
        while (itB.hasNext()) {
            W.T t2 = (W.T) itB.next();
            if (t2.a().equals("RTime")) {
                ((C0543c) t2).a("Time");
            }
            if (t2.a().equals("Frame")) {
                ((C0543c) t2).a(0);
            }
            if (!t2.a().contains("Afr Adjustment Required") && (!t2.a().startsWith("Col") || t2.a().length() > 5)) {
                if (!t2.a().trim().isEmpty()) {
                    arrayList.add(t2);
                }
            }
        }
        return arrayList.iterator();
    }

    @Override // ak.C0546f, W.V
    public float[] c() throws V.a {
        String strSubstring;
        try {
            if (this.f4837t && this.f4832p >= 500 && k()) {
                throw new V.a("This Edition is limited to loading 500 rows of data. \nPlease Register to load large log files.");
            }
            String strL = l();
            e(strL);
            if (strL.startsWith(this.f4822f)) {
                String str = "0";
                if (this.f4825i != null && this.f4825i.length > 0) {
                    str = this.f4825i[0] + "";
                }
                strL = str + strL;
            }
            if (this.f4829m == -1) {
                this.f4829m = this.f4824h.length() / (strL.length() + 3);
            }
            aD aDVar = new aD(strL, this.f4822f);
            this.f4825i = new float[aDVar.c()];
            int i2 = 0;
            while (i2 < this.f4825i.length) {
                try {
                    strSubstring = aDVar.b().trim();
                } catch (Exception e2) {
                    bH.C.c("Error Parsing record:\n" + strL);
                    strSubstring = "0";
                    e2.printStackTrace();
                }
                if (strSubstring.indexOf("[") != -1) {
                    strSubstring = bH.W.b(bH.W.b(strSubstring, "[", ""), "]", "");
                    if (strSubstring.contains(" ")) {
                        String[] strArrSplit = strSubstring.split(Constants.INDENT);
                        try {
                            this.f4825i[i2] = Float.parseFloat(strArrSplit[0]);
                        } catch (NumberFormatException e3) {
                            this.f4825i[i2] = Float.NaN;
                        }
                        i2++;
                        strSubstring = strArrSplit[1];
                    }
                } else if ((strSubstring.contains(LanguageTag.SEP) || strSubstring.contains(Marker.ANY_NON_NULL_MARKER) || strSubstring.contains(PdfOps.F_TOKEN) || strSubstring.contains("!")) && strSubstring.contains(" ")) {
                    strSubstring = strSubstring.substring(strSubstring.lastIndexOf(" "), strSubstring.length());
                }
                try {
                    this.f4825i[i2] = Float.parseFloat(bH.W.b(strSubstring, ",", "."));
                } catch (NumberFormatException e4) {
                    this.f4825i[i2] = Float.NaN;
                }
                i2++;
            }
            return this.f4825i;
        } catch (IOException e5) {
            e5.printStackTrace();
            throw new V.a("IO Error reading row from file on row " + this.f4832p + ".");
        }
    }

    @Override // ak.C0546f
    protected void e(String str) {
        if (str.indexOf("Frame") > -1) {
        }
    }

    @Override // ak.C0546f
    protected boolean c(String str) {
        return (str == null || str.startsWith("Frame") || str.isEmpty()) ? false : true;
    }

    @Override // ak.C0546f
    protected int b(String str) {
        int i2 = 0;
        BufferedReader bufferedReader = null;
        try {
            try {
                try {
                    this.f4824h = new File(str);
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(this.f4824h)));
                    String strTrim = bufferedReader2.readLine().trim();
                    if (strTrim == null) {
                        int i3 = 0 - 1;
                        if (bufferedReader2 != null) {
                            try {
                                bufferedReader2.close();
                            } catch (IOException e2) {
                                Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                            }
                        }
                        return i3;
                    }
                    while (true) {
                        if (i2 != 0 && strTrim.startsWith("Frame")) {
                            int i4 = i2;
                            if (bufferedReader2 != null) {
                                try {
                                    bufferedReader2.close();
                                } catch (IOException e3) {
                                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                                }
                            }
                            return i4;
                        }
                        if (strTrim == null) {
                            int i5 = i2 - 1;
                            if (bufferedReader2 != null) {
                                try {
                                    bufferedReader2.close();
                                } catch (IOException e4) {
                                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                                }
                            }
                            return i5;
                        }
                        if (strTrim.startsWith("Frame")) {
                            int i6 = i2;
                            if (bufferedReader2 != null) {
                                try {
                                    bufferedReader2.close();
                                } catch (IOException e5) {
                                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                                }
                            }
                            return i6;
                        }
                        strTrim = bufferedReader2.readLine().trim();
                        i2++;
                    }
                } catch (IOException e6) {
                    throw new V.a("Unable to read from file:\n" + str);
                } catch (NullPointerException e7) {
                    throw new V.a("Unable to read from file:\n" + str);
                }
            } catch (FileNotFoundException e8) {
                throw new V.a("Unable to open file for reading:\n" + str);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    bufferedReader.close();
                } catch (IOException e9) {
                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e9);
                }
            }
            throw th;
        }
    }
}
