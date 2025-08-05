package ak;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/* renamed from: ak.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/d.class */
public class C0544d extends C0546f {
    public C0544d() {
        super(",", false);
    }

    @Override // ak.C0546f, W.V
    public boolean a(String str) throws V.a {
        this.f4841w = a(str, 2);
        try {
            this.f4824h = new File(str);
            this.f4821e = new W.ah(new InputStreamReader(new FileInputStream(this.f4824h)));
            this.f4830n = this.f4821e.a();
            while (!this.f4830n.contains(",Log Records Used") && !this.f4830n.contains(",Replay Records Used") && !this.f4830n.startsWith("Log for BigComm Pro")) {
                this.f4830n = this.f4821e.a();
            }
            String strB = "-1";
            if (this.f4830n.contains(",Log Records Used")) {
                strB = bH.W.b(this.f4830n, ",Log Records Used", "");
            } else if (this.f4830n.contains(",Replay Records Used")) {
                strB = bH.W.b(this.f4830n, ",Replay Records Used", "");
            }
            this.f4829m = Integer.parseInt(strB);
            this.f4830n = this.f4821e.a();
            if (!this.f4830n.isEmpty()) {
                return true;
            }
            this.f4830n = this.f4821e.a();
            return true;
        } catch (FileNotFoundException e2) {
            throw new V.a("Unable to open file for reading:\n" + str);
        } catch (IOException e3) {
            throw new V.a("Unable to read from file:\n" + str);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:81:0x033b, code lost:
    
        a(true);
        r0 = c();
        r0 = r6.split(r5.f4822f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0352, code lost:
    
        if (r0 == null) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x035f, code lost:
    
        if (r0.length < r5.f4823g.size()) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0362, code lost:
    
        r12 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x036e, code lost:
    
        if (r12 >= r5.f4823g.size()) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x037b, code lost:
    
        if (r0[r12].contains(".") != false) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x037e, code lost:
    
        ((ak.C0543c) r5.f4823g.get(r12)).a(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x038e, code lost:
    
        r12 = r12 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0394, code lost:
    
        a(true);
     */
    @Override // ak.C0546f, W.V
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Iterator b() throws V.a {
        /*
            Method dump skipped, instructions count: 1056
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ak.C0544d.b():java.util.Iterator");
    }

    @Override // ak.C0546f, W.V
    public boolean e() {
        if (this.f4830n != null && this.f4830n.trim().length() > 0) {
            return (this.f4830n == null || this.f4830n.trim().length() <= 0 || this.f4830n.startsWith("Raw Log Data.")) ? false : true;
        }
        try {
            this.f4830n = l();
            if (this.f4830n != null && this.f4830n.trim().length() > 0) {
                if (!this.f4830n.startsWith("Raw Log Data.")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
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
        return W.X.f1981w;
    }
}
