package W;

import java.util.Properties;

/* loaded from: TunerStudioMS.jar:W/ar.class */
public class ar implements ap {

    /* renamed from: a, reason: collision with root package name */
    private Properties f2099a;

    /* renamed from: b, reason: collision with root package name */
    private String f2100b;

    public ar(Properties properties, String str) {
        this.f2099a = properties;
        if (str != null) {
            this.f2100b = str;
        } else {
            this.f2100b = "";
        }
    }

    @Override // W.ap
    public void a(String str, String str2) {
        this.f2099a.setProperty(this.f2100b + str, str2);
    }

    @Override // W.ap
    public String b(String str, String str2) {
        return this.f2099a.getProperty(this.f2100b + str, str2);
    }

    public Properties a() {
        return this.f2099a;
    }
}
