package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.MathUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/moment/FirstMoment.class */
public class FirstMoment extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = 6112755307178490473L;

    /* renamed from: n, reason: collision with root package name */
    protected long f13099n;
    protected double m1;
    protected double dev;
    protected double nDev;

    FirstMoment() {
        this.f13099n = 0L;
        this.m1 = Double.NaN;
        this.dev = Double.NaN;
        this.nDev = Double.NaN;
    }

    FirstMoment(FirstMoment original) throws NullArgumentException {
        copy(original, this);
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void increment(double d2) {
        if (this.f13099n == 0) {
            this.m1 = 0.0d;
        }
        this.f13099n++;
        double n0 = this.f13099n;
        this.dev = d2 - this.m1;
        this.nDev = this.dev / n0;
        this.m1 += this.nDev;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public void clear() {
        this.m1 = Double.NaN;
        this.f13099n = 0L;
        this.dev = Double.NaN;
        this.nDev = Double.NaN;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public double getResult() {
        return this.m1;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public long getN() {
        return this.f13099n;
    }

    @Override // org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic, org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic, org.apache.commons.math3.stat.descriptive.UnivariateStatistic, org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic
    public FirstMoment copy() throws NullArgumentException {
        FirstMoment result = new FirstMoment();
        copy(this, result);
        return result;
    }

    public static void copy(FirstMoment source, FirstMoment dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.f13099n = source.f13099n;
        dest.m1 = source.m1;
        dest.dev = source.dev;
        dest.nDev = source.nDev;
    }
}
