package G;

import java.io.File;

/* loaded from: TunerStudioMS.jar:G/cI.class */
public class cI implements cZ {

    /* renamed from: a, reason: collision with root package name */
    R f1047a;

    /* renamed from: b, reason: collision with root package name */
    private String f1048b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f1049c = "";

    public cI(R r2) {
        this.f1047a = r2;
    }

    @Override // G.cZ
    public String a() throws V.g {
        try {
            return this.f1048b + new File(this.f1047a.F()).getParentFile().getAbsolutePath() + this.f1049c;
        } catch (Exception e2) {
            if (this.f1047a == null) {
                throw new V.g("Failed to get Working dir for, No EcuConfiguration set.");
            }
            throw new V.g("Failed to get Working dir for: " + this.f1047a.c() + "\nError: " + e2.getMessage());
        }
    }

    public void a(String str) {
        this.f1048b = str;
    }

    public void b(String str) {
        this.f1049c = str;
    }

    @Override // G.cZ
    public String[] b() {
        return new String[0];
    }
}
