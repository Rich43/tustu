package G;

/* loaded from: TunerStudioMS.jar:G/E.class */
public class E extends AbstractC0093bz {

    /* renamed from: a, reason: collision with root package name */
    private String f297a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f298b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f299c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f300d = false;

    @Override // G.AbstractC0093bz
    public String b() {
        return this.f299c;
    }

    public String a() {
        return this.f297a;
    }

    public void a(String str) {
        this.f297a = str;
    }

    public String c() {
        return this.f298b;
    }

    public void b(String str) {
        this.f298b = str;
    }

    public String d() {
        return this.f299c;
    }

    public void c(String str) {
        this.f299c = str;
    }

    public void d(String str) throws V.g {
        if (!str.equals("filter32BitChannels")) {
            throw new V.g("Unknown channelSelector Option: " + str + ", Ignored. Known Options: filter32BitChannels");
        }
        a(true);
    }

    public boolean e() {
        return this.f300d;
    }

    public void a(boolean z2) {
        this.f300d = z2;
    }
}
