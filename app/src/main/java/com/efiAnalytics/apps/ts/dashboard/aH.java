package com.efiAnalytics.apps.ts.dashboard;

import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aH.class */
public class aH extends ArrayList implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private int f9443a = 200;

    public void a(double d2) {
        super.add(0, new Double(d2));
        while (size() > 0 && size() >= a()) {
            super.remove(size() - 1);
        }
    }

    public double a(int i2) {
        if (size() > 0) {
            return ((Double) super.get(i2)).doubleValue();
        }
        return Double.NaN;
    }

    public int a() {
        return this.f9443a;
    }

    public void b(int i2) {
        this.f9443a = i2;
    }
}
