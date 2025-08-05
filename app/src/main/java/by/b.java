package by;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: TunerStudioMS.jar:by/b.class */
public class b implements m {

    /* renamed from: a, reason: collision with root package name */
    List f9233a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    boolean f9234b = false;

    @Override // by.m
    public List a() {
        if (!this.f9234b) {
            Collections.sort(this.f9233a, new c(this));
            this.f9234b = true;
        }
        return this.f9233a;
    }

    public void a(k kVar) {
        this.f9233a.add(kVar);
    }
}
