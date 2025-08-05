package com.efiAnalytics.apps.ts.dashboard.renderers;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/a.class */
class a implements PathIterator, Serializable {

    /* renamed from: a, reason: collision with root package name */
    b f9517a;

    /* renamed from: d, reason: collision with root package name */
    private AffineTransform f9518d = null;

    /* renamed from: b, reason: collision with root package name */
    int f9519b = -1;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ AsymetricSweepRenderer f9520c;

    a(AsymetricSweepRenderer asymetricSweepRenderer, b bVar) {
        this.f9520c = asymetricSweepRenderer;
        this.f9517a = bVar;
    }

    @Override // java.awt.geom.PathIterator
    public int getWindingRule() {
        return 0;
    }

    @Override // java.awt.geom.PathIterator
    public boolean isDone() {
        return this.f9519b > 5;
    }

    @Override // java.awt.geom.PathIterator
    public void next() {
        this.f9519b++;
    }

    public void a() {
        this.f9519b = -1;
    }

    private float b() {
        return (float) (this.f9518d != null ? this.f9518d.getTranslateX() : 0.0d);
    }

    private float c() {
        return (float) (this.f9518d != null ? this.f9518d.getTranslateY() : 0.0d);
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(float[] fArr) {
        float f2 = this.f9517a.f9521a.isCounterClockwise() ? -1.0f : 1.0f;
        float width = this.f9517a.f9521a.isCounterClockwise() ? this.f9517a.f9521a.getWidth() - (this.f9517a.f9521a.getBorderWidth() / 2) : 0.0f;
        if (this.f9519b < 0) {
            this.f9519b = 0;
        }
        if (this.f9519b == 0) {
            fArr[0] = (f2 * this.f9517a.f9522b.x1) + b() + width;
            fArr[1] = this.f9517a.f9522b.y1 + c();
            return 0;
        }
        if (this.f9519b == 1) {
            fArr[0] = (f2 * this.f9517a.f9522b.ctrlx1) + b() + width;
            fArr[1] = this.f9517a.f9522b.ctrly1 + c();
            fArr[2] = (f2 * this.f9517a.f9522b.ctrlx2) + b() + width;
            fArr[3] = this.f9517a.f9522b.ctrly2 + c();
            fArr[4] = (f2 * this.f9517a.f9522b.x2) + b() + width;
            fArr[5] = this.f9517a.f9522b.y2 + c();
            return 3;
        }
        if (this.f9519b == 2) {
            fArr[0] = (f2 * this.f9517a.f9523c.x2) + b() + width;
            fArr[1] = this.f9517a.f9523c.y2 + c();
            return 1;
        }
        if (this.f9519b != 3) {
            if (this.f9519b != 4) {
                return 4;
            }
            fArr[0] = (f2 * this.f9517a.f9522b.x1) + b() + width;
            fArr[1] = this.f9517a.f9522b.y1 + c();
            return 1;
        }
        fArr[0] = (f2 * this.f9517a.f9523c.ctrlx2) + b() + width;
        fArr[1] = this.f9517a.f9523c.ctrly2 + c();
        fArr[2] = (f2 * this.f9517a.f9523c.ctrlx1) + b() + width;
        fArr[3] = this.f9517a.f9523c.ctrly1 + c();
        fArr[4] = (f2 * this.f9517a.f9523c.x1) + b() + width;
        fArr[5] = this.f9517a.f9523c.y1 + c();
        return 3;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        float f2 = this.f9517a.f9521a.isCounterClockwise() ? -1.0f : 1.0f;
        float width = this.f9517a.f9521a.isCounterClockwise() ? this.f9517a.f9521a.getWidth() - (this.f9517a.f9521a.getBorderWidth() / 2) : 0.0f;
        if (this.f9519b < 0) {
            this.f9519b = 0;
        }
        if (this.f9519b == 0) {
            dArr[0] = (f2 * this.f9517a.f9522b.x1) + b() + width;
            dArr[1] = this.f9517a.f9522b.y1 + c();
            return 0;
        }
        if (this.f9519b == 1) {
            dArr[0] = (f2 * this.f9517a.f9522b.ctrlx1) + b() + width;
            dArr[1] = this.f9517a.f9522b.ctrly1 + c();
            dArr[2] = (f2 * this.f9517a.f9522b.ctrlx2) + b() + width;
            dArr[3] = this.f9517a.f9522b.ctrly2 + c();
            dArr[4] = (f2 * this.f9517a.f9522b.x2) + b() + width;
            dArr[5] = this.f9517a.f9522b.y2 + c();
            return 3;
        }
        if (this.f9519b == 2) {
            dArr[0] = (f2 * this.f9517a.f9523c.x2) + b() + width;
            dArr[1] = this.f9517a.f9523c.y2 + c();
            return 1;
        }
        if (this.f9519b != 3) {
            if (this.f9519b != 4) {
                return 4;
            }
            dArr[0] = (f2 * this.f9517a.f9522b.x1) + b() + width;
            dArr[1] = this.f9517a.f9522b.y1 + c();
            return 1;
        }
        dArr[0] = (f2 * this.f9517a.f9523c.ctrlx2) + b() + width;
        dArr[1] = this.f9517a.f9523c.ctrly2 + c();
        dArr[2] = (f2 * this.f9517a.f9523c.ctrlx1) + b() + width;
        dArr[3] = this.f9517a.f9523c.ctrly1 + c();
        dArr[4] = (f2 * this.f9517a.f9523c.x1) + b() + width;
        dArr[5] = this.f9517a.f9523c.y1 + c();
        return 3;
    }

    public void a(AffineTransform affineTransform) {
        this.f9518d = affineTransform;
    }
}
