package com.efiAnalytics.apps.ts.dashboard.renderers;

import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/c.class */
class c implements Serializable {

    /* renamed from: b, reason: collision with root package name */
    private float f9532b;

    /* renamed from: c, reason: collision with root package name */
    private float f9533c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ AsymetricSweepRenderer f9534a;

    c(AsymetricSweepRenderer asymetricSweepRenderer, float f2, float f3) {
        this.f9534a = asymetricSweepRenderer;
        this.f9532b = f2;
        this.f9533c = f3;
    }

    public float a() {
        return this.f9533c;
    }

    public float b() {
        return this.f9532b;
    }
}
