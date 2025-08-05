package W;

import G.bO;
import G.bP;
import bH.C1011s;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: W.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/f.class */
class C0180f implements bP {

    /* renamed from: a, reason: collision with root package name */
    File f2127a;

    /* renamed from: b, reason: collision with root package name */
    File f2128b;

    /* renamed from: c, reason: collision with root package name */
    G.R f2129c;

    public C0180f(G.R r2, File file, File file2) {
        this.f2129c = r2;
        this.f2127a = file2;
        this.f2128b = file;
    }

    @Override // G.bP
    public bO a(String str, String str2) throws IOException {
        if (this.f2128b == null) {
            throw new IOException("Cache Dir not set! Cannot load Encoded Data.");
        }
        if (str2 != null) {
            File file = new File(this.f2128b, str2);
            try {
                if (file.exists()) {
                    if (str2.equals(C1011s.c(file))) {
                        byte[] bArrA = C0178d.a(file);
                        bO bOVar = new bO(str);
                        bOVar.a(bArrA);
                        return bOVar;
                    }
                    bH.C.b("Cache File found, but MD5 doesn't match, deleted: " + file.delete());
                }
            } catch (IOException e2) {
                bH.C.b("Failed to load Cache File: \n\t" + file.getAbsolutePath() + "\n\tMsg: " + e2.getMessage());
            }
        }
        if (this.f2127a == null) {
            throw new IOException("Ini File not set! Cannot load Encoded Data from ini.");
        }
        bO bOVarC = this.f2129c.C(str);
        if (bOVarC == null) {
            try {
                new C0172ab().a(this.f2129c, this.f2127a.getAbsolutePath(), str);
                bOVarC = this.f2129c.C(str);
            } catch (V.g e3) {
                Logger.getLogger(C0180f.class.getName()).log(Level.SEVERE, e3.getMessage(), (Throwable) e3);
                throw new IOException(e3.getMessage());
            }
        }
        if (bOVarC == null) {
            bH.C.b("EncodeData '" + str + "' not found in cache or ini. Returning null");
            return null;
        }
        byte[] bArrA2 = bOVarC.a();
        String strA = C1011s.a(bArrA2);
        if (!strA.equalsIgnoreCase(str2)) {
            bH.C.a("Data Checksum does not match the ini file, caching will not work!!!!!!!! INI MD5: " + str2 + ", calc md5: " + strA);
            str2 = strA;
        }
        C0178d.a(new File(this.f2128b, str2), bArrA2);
        return bOVarC;
    }

    @Override // G.bP
    public File b(String str, String str2) {
        File file = new File(this.f2128b, str2);
        if (!file.exists()) {
            C0178d.a(file, a(str, str2).a());
        }
        return file;
    }
}
