package G;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/* loaded from: TunerStudioMS.jar:G/Q.class */
public class Q implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f471a = "";

    /* renamed from: b, reason: collision with root package name */
    private String f472b = "";

    /* renamed from: c, reason: collision with root package name */
    private String f473c = null;

    /* renamed from: d, reason: collision with root package name */
    private boolean f474d = false;

    /* renamed from: e, reason: collision with root package name */
    private boolean f475e = false;

    /* renamed from: f, reason: collision with root package name */
    private String f476f = null;

    public String aH() {
        return this.f476f;
    }

    public void u(String str) {
        this.f476f = str;
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

    public String aI() {
        return this.f473c;
    }

    public String aJ() {
        return this.f472b;
    }

    public void v(String str) {
        this.f472b = str;
    }

    public String aK() {
        return this.f471a;
    }

    public void w(String str) {
        this.f471a = str;
    }

    public boolean aL() {
        return this.f474d;
    }

    public void q(boolean z2) {
        this.f474d = z2;
    }

    public boolean aM() {
        return this.f475e;
    }
}
