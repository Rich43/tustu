package ak;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:ak/aw.class */
public class aw extends R {
    public aw() {
        super(",", true);
    }

    @Override // ak.C0546f, W.V
    public boolean a(String str) throws V.a {
        try {
            this.f4824h = new File(str);
            this.f4821e = new W.ah(new InputStreamReader(new FileInputStream(this.f4824h), FTP.DEFAULT_CONTROL_ENCODING));
            this.f4830n = this.f4821e.a();
            while (!this.f4830n.startsWith("\"Time\"") && !this.f4830n.startsWith("Time")) {
                this.f4830n = this.f4821e.a();
            }
            return true;
        } catch (FileNotFoundException e2) {
            throw new V.a("Unable to open file for reading:\n" + str);
        } catch (IOException e3) {
            throw new V.a("Unable to read from file:\n" + str);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x0222, code lost:
    
        a(true);
     */
    @Override // ak.C0546f, W.V
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Iterator b() throws V.a {
        /*
            Method dump skipped, instructions count: 683
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ak.aw.b():java.util.Iterator");
    }

    private C0543c b(C0543c c0543c) {
        if (c0543c.a().equals("Time")) {
            c0543c.a(0.001f);
            c0543c.b(PdfOps.s_TOKEN);
        }
        return c0543c;
    }

    @Override // ak.C0546f
    protected String m() {
        return ",";
    }

    @Override // ak.R, ak.C0546f, W.V
    public String i() {
        return W.X.f1972n;
    }
}
