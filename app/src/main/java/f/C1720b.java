package f;

import com.intel.bluetooth.BlueCoveImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Properties;

/* renamed from: f.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:f/b.class */
public class C1720b {

    /* renamed from: a, reason: collision with root package name */
    private String f12157a = "";

    /* renamed from: b, reason: collision with root package name */
    private String f12158b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f12159c = "";

    /* renamed from: d, reason: collision with root package name */
    private String f12160d = "";

    /* renamed from: e, reason: collision with root package name */
    private String f12161e = "";

    /* renamed from: f, reason: collision with root package name */
    private String f12162f = "";

    /* renamed from: g, reason: collision with root package name */
    private String f12163g = "";

    /* renamed from: h, reason: collision with root package name */
    private String f12164h = "";

    /* renamed from: i, reason: collision with root package name */
    private String f12165i = "";

    public String a() throws IOException {
        Properties properties = new Properties();
        properties.setProperty(BlueCoveImpl.STACK_OSX, b());
        properties.setProperty("dId", h());
        properties.setProperty("hId", c());
        properties.setProperty("mId", d());
        properties.setProperty("regKey", e());
        properties.setProperty("email", f());
        properties.setProperty("uid", g());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        properties.store(byteArrayOutputStream, "");
        return bI.a.a(bI.i.a(byteArrayOutputStream.toByteArray()), 16);
    }

    public String b() {
        return this.f12157a;
    }

    public void a(String str) {
        if (str != null) {
            this.f12157a = str;
        } else {
            this.f12157a = "";
        }
    }

    public String c() {
        return this.f12158b;
    }

    public void b(String str) {
        if (str != null) {
            this.f12158b = str;
        }
    }

    public String d() {
        return this.f12159c;
    }

    public void c(String str) {
        if (str != null) {
            this.f12159c = str;
        }
    }

    public String e() {
        return this.f12161e;
    }

    public void d(String str) {
        this.f12161e = str;
    }

    public String f() {
        return this.f12162f;
    }

    public void e(String str) {
        if (str != null) {
            this.f12162f = str;
        }
    }

    public String g() {
        return this.f12165i;
    }

    public void f(String str) {
        if (str != null) {
            this.f12165i = str;
        }
    }

    public void g(String str) {
        this.f12163g = str;
    }

    public void h(String str) {
        this.f12164h = str;
    }

    public String h() {
        return this.f12160d;
    }

    public void i(String str) {
        if (str != null) {
            this.f12160d = str;
        }
    }

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
}
