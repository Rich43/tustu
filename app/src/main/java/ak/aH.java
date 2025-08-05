package ak;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/* loaded from: TunerStudioMS.jar:ak/aH.class */
public class aH extends C0546f {
    public aH() {
        super(",", true);
    }

    @Override // ak.C0546f, W.V
    public boolean a(String str) throws V.a {
        try {
            this.f4824h = new File(str);
            this.f4821e = new W.ah(new InputStreamReader(new FileInputStream(this.f4824h)));
            this.f4830n = this.f4821e.a();
            while (this.f4830n.indexOf("Sample #") == -1 && !this.f4830n.startsWith(",Time")) {
                this.f4830n = this.f4821e.a();
            }
            return true;
        } catch (FileNotFoundException e2) {
            throw new V.a("Unable to open file for reading:\n" + str);
        } catch (IOException e3) {
            throw new V.a("Unable to read from file:\n" + str);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x01f9, code lost:
    
        a(true);
     */
    @Override // ak.C0546f, W.V
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Iterator b() throws V.a {
        /*
            Method dump skipped, instructions count: 642
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ak.aH.b():java.util.Iterator");
    }

    @Override // ak.C0546f
    protected boolean c(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    @Override // ak.C0546f
    protected String m() {
        return ",";
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f1971m;
    }
}
