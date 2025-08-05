package ao;

import java.util.ArrayList;
import java.util.List;

/* renamed from: ao.bt, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bt.class */
public class C0656bt {

    /* renamed from: a, reason: collision with root package name */
    private String f5437a;

    /* renamed from: b, reason: collision with root package name */
    private String f5438b;

    /* renamed from: c, reason: collision with root package name */
    private String f5439c = "";

    /* renamed from: d, reason: collision with root package name */
    private List f5440d = new ArrayList();

    public C0656bt(String str, String str2) {
        this.f5437a = str;
        this.f5438b = str2;
    }

    public String a() {
        return this.f5437a;
    }

    public String b() {
        return this.f5438b;
    }

    public String c() {
        return this.f5439c;
    }

    public void a(String str) {
        this.f5439c = str;
    }

    public void b(String str) {
        this.f5440d.add(str);
    }

    public List d() {
        return this.f5440d;
    }
}
