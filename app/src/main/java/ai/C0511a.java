package ai;

import java.util.HashMap;

/* renamed from: ai.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ai/a.class */
public class C0511a {

    /* renamed from: a, reason: collision with root package name */
    private static C0511a f4510a = null;

    /* renamed from: b, reason: collision with root package name */
    private HashMap f4511b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    private HashMap f4512c = new HashMap();

    private C0511a() {
    }

    public static C0511a a() {
        if (f4510a == null) {
            f4510a = new C0511a();
        }
        return f4510a;
    }

    public void a(String str, InterfaceC0513c interfaceC0513c) {
        this.f4511b.put(str, interfaceC0513c);
    }

    public void a(String str, InterfaceC0515e interfaceC0515e) {
        this.f4512c.put(str, interfaceC0515e);
    }

    public InterfaceC0515e a(String str) {
        return (InterfaceC0515e) this.f4512c.get(str);
    }
}
