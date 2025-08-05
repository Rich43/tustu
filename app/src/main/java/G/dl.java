package G;

/* loaded from: TunerStudioMS.jar:G/dl.class */
public class dl implements cZ {

    /* renamed from: a, reason: collision with root package name */
    R f1217a;

    /* renamed from: b, reason: collision with root package name */
    private String f1218b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f1219c = "";

    public dl(R r2) {
        this.f1217a = r2;
    }

    @Override // G.cZ
    public String a() throws V.g {
        try {
            return this.f1218b + this.f1217a.F() + this.f1219c;
        } catch (Exception e2) {
            if (this.f1217a == null) {
                throw new V.g("Failed to get Working dir for, No EcuConfiguration set.");
            }
            throw new V.g("Failed to get Working dir for: " + this.f1217a.c() + "\nError: " + e2.getMessage());
        }
    }

    public void a(String str) {
        this.f1218b = str;
    }

    public void b(String str) {
        this.f1219c = str;
    }

    @Override // G.cZ
    public String[] b() {
        return new String[0];
    }
}
