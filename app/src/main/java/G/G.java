package G;

import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:G/G.class */
public class G extends ArrayList implements Serializable {

    /* renamed from: b, reason: collision with root package name */
    private boolean f393b = false;

    /* renamed from: c, reason: collision with root package name */
    private String f394c = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ F f395a;

    public G(F f2) {
        this.f395a = f2;
    }

    public boolean a() {
        return this.f393b;
    }

    public void a(boolean z2) {
        this.f393b = z2;
    }

    public String b() {
        return this.f394c;
    }

    public void a(String str) {
        this.f394c = str;
    }
}
