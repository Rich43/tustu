package ak;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ak/aC.class */
public class aC extends C0546f {
    public aC() {
        super(";", false);
    }

    @Override // ak.C0546f, W.V
    public boolean a(String str) throws V.a {
        int iB = b(str);
        if (iB > 0) {
            this.f4841w = a(str, iB);
        }
        try {
            this.f4824h = new File(str);
            FileInputStream fileInputStream = new FileInputStream(this.f4824h);
            this.f4826j = "UTF-8";
            this.f4821e = new W.ah(new InputStreamReader(fileInputStream, this.f4826j));
            for (int i2 = 0; i2 <= iB; i2++) {
                String str2 = this.f4830n;
                this.f4830n = this.f4821e.a();
            }
            this.f4837t = true;
            return true;
        } catch (FileNotFoundException e2) {
            throw new V.a("Unable to open file for reading:\n" + str);
        } catch (IOException e3) {
            throw new V.a("Unable to read from file:\n" + str);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x0139, code lost:
    
        a(true);
     */
    @Override // ak.C0546f, W.V
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Iterator b() throws V.a {
        /*
            Method dump skipped, instructions count: 386
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ak.aC.b():java.util.Iterator");
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
                while (true) {
                    if (i2 != 0) {
                        if (strA.startsWith("'''")) {
                            break;
                        }
                    }
                    i2++;
                    strA = ahVar.a();
                }
                if (ahVar != null) {
                    try {
                        ahVar.close();
                    } catch (IOException e2) {
                        Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                return i2;
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
}
