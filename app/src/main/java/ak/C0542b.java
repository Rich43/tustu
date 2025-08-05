package ak;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;

/* renamed from: ak.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/b.class */
public class C0542b extends C0546f {

    /* renamed from: a, reason: collision with root package name */
    float f4809a;

    /* renamed from: b, reason: collision with root package name */
    float f4810b;

    public C0542b() {
        super(",", false);
        this.f4809a = 20.0f;
        this.f4810b = 0.0f;
        this.f4837t = true;
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
                W.ah ahVar2 = new W.ah(new InputStreamReader(fileInputStream, this.f4826j));
                String strA = ahVar2.a();
                if (strA == null) {
                    int i3 = 0 - 1;
                    if (ahVar2 != null) {
                        try {
                            ahVar2.close();
                        } catch (IOException e3) {
                            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                        }
                    }
                    return i3;
                }
                while (true) {
                    if (i2 != 0 && c(strA)) {
                        if (this.f4839u) {
                            this.f4822f = h(strA);
                        }
                        int iD = d(strA, this.f4822f);
                        if (i2 >= 4 && iD > 5 && Math.abs(d(str5, this.f4822f) - iD) < 2 && b(str5, this.f4822f)) {
                            int i4 = i2 - 4;
                            if (ahVar2 != null) {
                                try {
                                    ahVar2.close();
                                } catch (IOException e4) {
                                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                                }
                            }
                            return i4;
                        }
                        if (i2 >= 3 && iD > 5 && Math.abs(d(str4, this.f4822f) - iD) < 2 && b(str4, this.f4822f)) {
                            int i5 = i2 - 3;
                            if (ahVar2 != null) {
                                try {
                                    ahVar2.close();
                                } catch (IOException e5) {
                                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                                }
                            }
                            return i5;
                        }
                        if (iD <= 5 || Math.abs(d(str3, this.f4822f) - iD) >= 2 || !b(str3, this.f4822f)) {
                            int i6 = i2 - 1;
                            if (ahVar2 != null) {
                                try {
                                    ahVar2.close();
                                } catch (IOException e6) {
                                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                                }
                            }
                            return i6;
                        }
                        int i7 = i2 - 2;
                        if (ahVar2 != null) {
                            try {
                                ahVar2.close();
                            } catch (IOException e7) {
                                Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                            }
                        }
                        return i7;
                    }
                    if (strA == null) {
                        int i8 = i2 - 1;
                        if (ahVar2 != null) {
                            try {
                                ahVar2.close();
                            } catch (IOException e8) {
                                Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
                            }
                        }
                        return i8;
                    }
                    if (!strA.startsWith("MARK")) {
                        str5 = str4;
                        str4 = str3;
                        str3 = str2;
                        str2 = strA;
                        i2++;
                    }
                    if (strA.contains("Sample Rate") && strA.contains(",")) {
                        this.f4809a = Float.parseFloat(bH.W.b(strA.substring(strA.indexOf(",") + 2), PdfOps.DOUBLE_QUOTE__TOKEN, ""));
                    }
                    strA = ahVar2.a();
                    if (strA != null && strA.length() > 2 && strA.endsWith("\",")) {
                        strA = strA.substring(0, strA.length() - 2);
                    }
                    if (strA != null && strA.length() > 2 && strA.startsWith(",")) {
                        if (strA.contains("\",\"")) {
                            this.f4822f = "\",\"";
                            strA = "\"0\"" + strA;
                        } else {
                            strA = "0" + strA;
                        }
                    }
                }
            } catch (FileNotFoundException e9) {
                throw new V.a("Unable to open file for reading:\n" + str);
            } catch (IOException e10) {
                throw new V.a("Unable to read from file:\n" + str);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    ahVar.close();
                } catch (IOException e11) {
                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e11);
                }
            }
            throw th;
        }
    }

    @Override // ak.C0546f
    protected String l() throws V.f {
        String strL = super.l();
        if (strL != null && strL.startsWith(",")) {
            this.f4810b = this.f4832p * (1.0f / this.f4809a);
            strL = this.f4822f.equals(",") ? this.f4810b + strL : PdfOps.DOUBLE_QUOTE__TOKEN + this.f4810b + PdfOps.DOUBLE_QUOTE__TOKEN + strL;
        }
        return strL;
    }
}
