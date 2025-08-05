package com.efiAnalytics.tunerStudio.search;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/f.class */
public class f {

    /* renamed from: a, reason: collision with root package name */
    private long f10184a = 0;

    /* renamed from: b, reason: collision with root package name */
    private B.i f10185b = null;

    /* renamed from: c, reason: collision with root package name */
    private D.a f10186c = new D.a();

    /* renamed from: d, reason: collision with root package name */
    private j f10187d = null;

    public String toString() throws SecurityException {
        Field[] declaredFields = getClass().getDeclaredFields();
        AccessibleObject.setAccessible(declaredFields, true);
        String name = getClass().getName();
        for (Field field : declaredFields) {
            try {
                name = name + "\n\t" + field.getName() + "=" + field.get(this) + ", ";
            } catch (Exception e2) {
            }
        }
        return name + "\n";
    }

    public void a(String str) {
        this.f10186c.b(str);
        this.f10185b.d(str);
    }

    public String a() {
        return this.f10185b.e();
    }

    public String b() {
        return this.f10185b.i();
    }

    public String c() {
        return this.f10186c.c();
    }

    public String d() {
        if (f() != null) {
            return f().b();
        }
        return null;
    }

    public long e() {
        return this.f10184a;
    }

    public void a(long j2) {
        this.f10184a = j2;
    }

    public B.i f() {
        return this.f10185b;
    }

    public void a(B.i iVar) {
        this.f10185b = iVar;
        this.f10186c.a(iVar.i());
        this.f10186c.b(iVar.e());
    }

    public D.a g() {
        return this.f10186c;
    }

    public void a(D.a aVar) {
        if (aVar == null) {
            return;
        }
        this.f10186c = aVar;
    }

    public j h() {
        return this.f10187d;
    }

    public void a(j jVar) {
        this.f10187d = jVar;
    }
}
