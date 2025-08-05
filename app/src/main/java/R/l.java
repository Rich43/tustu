package R;

/* loaded from: TunerStudioMS.jar:R/l.class */
public abstract class l {

    /* renamed from: a, reason: collision with root package name */
    public static String f1811a = "basicUserReview";

    /* renamed from: b, reason: collision with root package name */
    public static String f1812b = "submitTranslationUpdate";

    /* renamed from: c, reason: collision with root package name */
    public static String f1813c = "submitProposedTranslation";

    /* renamed from: d, reason: collision with root package name */
    private long f1814d = System.currentTimeMillis();

    public abstract String a();

    public final String k() {
        return a() + "_" + this.f1814d;
    }

    public long l() {
        return this.f1814d;
    }

    public void a(long j2) {
        this.f1814d = j2;
    }
}
