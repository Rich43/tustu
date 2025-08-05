package by;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:by/k.class */
public class k {

    /* renamed from: b, reason: collision with root package name */
    private String f9244b;

    /* renamed from: a, reason: collision with root package name */
    List f9245a = new ArrayList();

    public k(String str) {
        this.f9244b = str;
    }

    public void a(l lVar) {
        this.f9245a.add(lVar);
    }

    public String a() {
        StringBuilder sb = new StringBuilder();
        sb.append(b()).append("(");
        for (int i2 = 0; i2 < this.f9245a.size(); i2++) {
            sb.append('[').append(((l) this.f9245a.get(i2)).a()).append(']');
            if (i2 + 1 < this.f9245a.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String toString() {
        return this.f9245a.isEmpty() ? b() + "()" : b() + "(..)";
    }

    public String b() {
        return this.f9244b;
    }
}
