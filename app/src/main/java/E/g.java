package E;

import java.net.InetAddress;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:E/g.class */
public class g {

    /* renamed from: a, reason: collision with root package name */
    private InetAddress f270a;

    /* renamed from: b, reason: collision with root package name */
    private InetAddress f271b;

    /* renamed from: c, reason: collision with root package name */
    private InetAddress f272c;

    /* renamed from: d, reason: collision with root package name */
    private int f273d = 3600;

    /* renamed from: e, reason: collision with root package name */
    private ArrayList f274e = new ArrayList();

    public void a(String str) {
        this.f270a = j.a(str);
    }

    public void b(String str) {
        this.f271b = j.a(str);
    }

    public void c(String str) {
        this.f272c = j.a(str);
    }

    public void a(int i2) {
        this.f273d = i2;
    }

    public void d(String str) {
        this.f274e.add(j.a(str));
    }

    public InetAddress a() {
        return this.f270a;
    }

    public InetAddress b() {
        return this.f271b;
    }

    public InetAddress c() {
        return this.f272c;
    }

    public int d() {
        return this.f273d;
    }
}
