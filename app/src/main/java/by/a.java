package by;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:by/a.class */
class a implements i {

    /* renamed from: b, reason: collision with root package name */
    private String f9231b = null;

    /* renamed from: a, reason: collision with root package name */
    List f9232a = new ArrayList();

    a() {
    }

    @Override // by.i
    public String a() {
        return this.f9231b;
    }

    @Override // by.i
    public List b() {
        return this.f9232a;
    }

    public void a(String str) {
        this.f9232a.add(str);
    }

    public void b(String str) {
        this.f9231b = str;
    }
}
