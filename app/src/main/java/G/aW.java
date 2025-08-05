package G;

import java.io.Serializable;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:G/aW.class */
public class aW extends Q implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f660a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f661b = null;

    /* renamed from: c, reason: collision with root package name */
    private dh f662c = new B(0.0d);

    /* renamed from: d, reason: collision with root package name */
    private int f663d = 1;

    /* renamed from: e, reason: collision with root package name */
    private String f664e = null;

    /* renamed from: f, reason: collision with root package name */
    private String f665f = null;

    /* renamed from: g, reason: collision with root package name */
    private boolean f666g = true;

    public String a() {
        return this.f660a;
    }

    public void a(String str) {
        this.f660a = str;
    }

    public String b() {
        return this.f661b;
    }

    public double c() {
        return this.f662c.a();
    }

    public void a(dh dhVar) {
        this.f662c = dhVar;
    }

    public int d() {
        return this.f663d;
    }

    public void a(int i2) {
        this.f663d = i2;
    }

    public void b(String str) {
        if (str.equals("=")) {
            this.f663d = 1;
            return;
        }
        if (str.equals(">")) {
            this.f663d = 2;
            return;
        }
        if (str.equals("<")) {
            this.f663d = 4;
            return;
        }
        if (str.equals("&")) {
            this.f663d = 32;
            return;
        }
        if (str.equals(CallSiteDescriptor.OPERATOR_DELIMITER)) {
            this.f663d = 64;
            return;
        }
        if (str.equals("<=")) {
            this.f663d = 8;
        } else if (str.equals(">=")) {
            this.f663d = 16;
        } else if (str.equals("custom")) {
            this.f663d = 128;
        }
    }

    public static boolean c(String str) {
        return str.equals("std_xAxisMin") || str.equals("std_xAxisMax") || str.equals("std_yAxisMin") || str.equals("std_yAxisMax") || str.equals("std_DeadLambda") || str.equals("std_Expression") || str.equals("std_Custom");
    }

    public String e() {
        return this.f664e;
    }

    public void d(String str) {
        this.f664e = str;
    }

    public String f() {
        return this.f665f;
    }

    public void e(String str) {
        this.f665f = str;
    }

    public boolean g() {
        return this.f666g;
    }

    public void a(boolean z2) {
        this.f666g = z2;
    }
}
