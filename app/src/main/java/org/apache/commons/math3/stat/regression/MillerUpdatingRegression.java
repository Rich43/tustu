package org.apache.commons.math3.stat.regression;

import java.util.Arrays;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/regression/MillerUpdatingRegression.class */
public class MillerUpdatingRegression implements UpdatingMultipleLinearRegression {
    private final int nvars;

    /* renamed from: d, reason: collision with root package name */
    private final double[] f13109d;
    private final double[] rhs;

    /* renamed from: r, reason: collision with root package name */
    private final double[] f13110r;
    private final double[] tol;
    private final double[] rss;
    private final int[] vorder;
    private final double[] work_tolset;
    private long nobs;
    private double sserr;
    private boolean rss_set;
    private boolean tol_set;
    private final boolean[] lindep;
    private final double[] x_sing;
    private final double[] work_sing;
    private double sumy;
    private double sumsqy;
    private boolean hasIntercept;
    private final double epsilon;

    private MillerUpdatingRegression() {
        this(-1, false, Double.NaN);
    }

    public MillerUpdatingRegression(int numberOfVariables, boolean includeConstant, double errorTolerance) throws ModelSpecificationException {
        this.nobs = 0L;
        this.sserr = 0.0d;
        this.rss_set = false;
        this.tol_set = false;
        this.sumy = 0.0d;
        this.sumsqy = 0.0d;
        if (numberOfVariables < 1) {
            throw new ModelSpecificationException(LocalizedFormats.NO_REGRESSORS, new Object[0]);
        }
        if (includeConstant) {
            this.nvars = numberOfVariables + 1;
        } else {
            this.nvars = numberOfVariables;
        }
        this.hasIntercept = includeConstant;
        this.nobs = 0L;
        this.f13109d = new double[this.nvars];
        this.rhs = new double[this.nvars];
        this.f13110r = new double[(this.nvars * (this.nvars - 1)) / 2];
        this.tol = new double[this.nvars];
        this.rss = new double[this.nvars];
        this.vorder = new int[this.nvars];
        this.x_sing = new double[this.nvars];
        this.work_sing = new double[this.nvars];
        this.work_tolset = new double[this.nvars];
        this.lindep = new boolean[this.nvars];
        for (int i2 = 0; i2 < this.nvars; i2++) {
            this.vorder[i2] = i2;
        }
        if (errorTolerance > 0.0d) {
            this.epsilon = errorTolerance;
        } else {
            this.epsilon = -errorTolerance;
        }
    }

    public MillerUpdatingRegression(int numberOfVariables, boolean includeConstant) throws ModelSpecificationException {
        this(numberOfVariables, includeConstant, Precision.EPSILON);
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public boolean hasIntercept() {
        return this.hasIntercept;
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public long getN() {
        return this.nobs;
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public void addObservation(double[] x2, double y2) throws ModelSpecificationException {
        if ((!this.hasIntercept && x2.length != this.nvars) || (this.hasIntercept && x2.length + 1 != this.nvars)) {
            throw new ModelSpecificationException(LocalizedFormats.INVALID_REGRESSION_OBSERVATION, Integer.valueOf(x2.length), Integer.valueOf(this.nvars));
        }
        if (!this.hasIntercept) {
            include(MathArrays.copyOf(x2, x2.length), 1.0d, y2);
        } else {
            double[] tmp = new double[x2.length + 1];
            System.arraycopy(x2, 0, tmp, 1, x2.length);
            tmp[0] = 1.0d;
            include(tmp, 1.0d, y2);
        }
        this.nobs++;
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public void addObservations(double[][] x2, double[] y2) throws ModelSpecificationException {
        if (x2 == null || y2 == null || x2.length != y2.length) {
            LocalizedFormats localizedFormats = LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE;
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(x2 == null ? 0 : x2.length);
            objArr[1] = Integer.valueOf(y2 == null ? 0 : y2.length);
            throw new ModelSpecificationException(localizedFormats, objArr);
        }
        if (x2.length == 0) {
            throw new ModelSpecificationException(LocalizedFormats.NO_DATA, new Object[0]);
        }
        if (x2[0].length + 1 > x2.length) {
            throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, Integer.valueOf(x2.length), Integer.valueOf(x2[0].length));
        }
        for (int i2 = 0; i2 < x2.length; i2++) {
            addObservation(x2[i2], y2[i2]);
        }
    }

    private void include(double[] x2, double wi, double yi) {
        double dpi;
        int nextr = 0;
        double w2 = wi;
        double y2 = yi;
        this.rss_set = false;
        this.sumy = smartAdd(yi, this.sumy);
        this.sumsqy = smartAdd(this.sumsqy, yi * yi);
        for (int i2 = 0; i2 < x2.length; i2++) {
            if (w2 == 0.0d) {
                return;
            }
            double xi = x2[i2];
            if (xi == 0.0d) {
                nextr += (this.nvars - i2) - 1;
            } else {
                double di = this.f13109d[i2];
                double wxi = w2 * xi;
                double _w = w2;
                if (di != 0.0d) {
                    dpi = smartAdd(di, wxi * xi);
                    double tmp = (wxi * xi) / di;
                    if (FastMath.abs(tmp) > Precision.EPSILON) {
                        w2 = (di * w2) / dpi;
                    }
                } else {
                    dpi = wxi * xi;
                    w2 = 0.0d;
                }
                this.f13109d[i2] = dpi;
                for (int k2 = i2 + 1; k2 < this.nvars; k2++) {
                    double xk = x2[k2];
                    x2[k2] = smartAdd(xk, (-xi) * this.f13110r[nextr]);
                    if (di != 0.0d) {
                        this.f13110r[nextr] = smartAdd(di * this.f13110r[nextr], (_w * xi) * xk) / dpi;
                    } else {
                        this.f13110r[nextr] = xk / xi;
                    }
                    nextr++;
                }
                double xk2 = y2;
                y2 = smartAdd(xk2, (-xi) * this.rhs[i2]);
                if (di != 0.0d) {
                    this.rhs[i2] = smartAdd(di * this.rhs[i2], wxi * xk2) / dpi;
                } else {
                    this.rhs[i2] = xk2 / xi;
                }
            }
        }
        this.sserr = smartAdd(this.sserr, w2 * y2 * y2);
    }

    private double smartAdd(double a2, double b2) {
        double _a = FastMath.abs(a2);
        double _b = FastMath.abs(b2);
        if (_a > _b) {
            double eps = _a * Precision.EPSILON;
            if (_b > eps) {
                return a2 + b2;
            }
            return a2;
        }
        double eps2 = _b * Precision.EPSILON;
        if (_a > eps2) {
            return a2 + b2;
        }
        return b2;
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public void clear() {
        Arrays.fill(this.f13109d, 0.0d);
        Arrays.fill(this.rhs, 0.0d);
        Arrays.fill(this.f13110r, 0.0d);
        Arrays.fill(this.tol, 0.0d);
        Arrays.fill(this.rss, 0.0d);
        Arrays.fill(this.work_tolset, 0.0d);
        Arrays.fill(this.work_sing, 0.0d);
        Arrays.fill(this.x_sing, 0.0d);
        Arrays.fill(this.lindep, false);
        for (int i2 = 0; i2 < this.nvars; i2++) {
            this.vorder[i2] = i2;
        }
        this.nobs = 0L;
        this.sserr = 0.0d;
        this.sumy = 0.0d;
        this.sumsqy = 0.0d;
        this.rss_set = false;
        this.tol_set = false;
    }

    private void tolset() {
        double eps = this.epsilon;
        for (int i2 = 0; i2 < this.nvars; i2++) {
            this.work_tolset[i2] = FastMath.sqrt(this.f13109d[i2]);
        }
        this.tol[0] = eps * this.work_tolset[0];
        for (int col = 1; col < this.nvars; col++) {
            int pos = col - 1;
            double total = this.work_tolset[col];
            for (int row = 0; row < col; row++) {
                total += FastMath.abs(this.f13110r[pos]) * this.work_tolset[row];
                pos += (this.nvars - row) - 2;
            }
            this.tol[col] = eps * total;
        }
        this.tol_set = true;
    }

    private double[] regcf(int nreq) throws ModelSpecificationException {
        if (nreq < 1) {
            throw new ModelSpecificationException(LocalizedFormats.NO_REGRESSORS, new Object[0]);
        }
        if (nreq > this.nvars) {
            throw new ModelSpecificationException(LocalizedFormats.TOO_MANY_REGRESSORS, Integer.valueOf(nreq), Integer.valueOf(this.nvars));
        }
        if (!this.tol_set) {
            tolset();
        }
        double[] ret = new double[nreq];
        boolean rankProblem = false;
        for (int i2 = nreq - 1; i2 > -1; i2--) {
            if (FastMath.sqrt(this.f13109d[i2]) < this.tol[i2]) {
                ret[i2] = 0.0d;
                this.f13109d[i2] = 0.0d;
                rankProblem = true;
            } else {
                ret[i2] = this.rhs[i2];
                int nextr = (i2 * (((this.nvars + this.nvars) - i2) - 1)) / 2;
                for (int j2 = i2 + 1; j2 < nreq; j2++) {
                    ret[i2] = smartAdd(ret[i2], (-this.f13110r[nextr]) * ret[j2]);
                    nextr++;
                }
            }
        }
        if (rankProblem) {
            for (int i3 = 0; i3 < nreq; i3++) {
                if (this.lindep[i3]) {
                    ret[i3] = Double.NaN;
                }
            }
        }
        return ret;
    }

    private void singcheck() {
        for (int i2 = 0; i2 < this.nvars; i2++) {
            this.work_sing[i2] = FastMath.sqrt(this.f13109d[i2]);
        }
        for (int col = 0; col < this.nvars; col++) {
            double temp = this.tol[col];
            int pos = col - 1;
            for (int row = 0; row < col - 1; row++) {
                if (FastMath.abs(this.f13110r[pos]) * this.work_sing[row] < temp) {
                    this.f13110r[pos] = 0.0d;
                }
                pos += (this.nvars - row) - 2;
            }
            this.lindep[col] = false;
            if (this.work_sing[col] < temp) {
                this.lindep[col] = true;
                if (col < this.nvars - 1) {
                    Arrays.fill(this.x_sing, 0.0d);
                    int _pi = (col * (((this.nvars + this.nvars) - col) - 1)) / 2;
                    int _xi = col + 1;
                    while (_xi < this.nvars) {
                        this.x_sing[_xi] = this.f13110r[_pi];
                        this.f13110r[_pi] = 0.0d;
                        _xi++;
                        _pi++;
                    }
                    double y2 = this.rhs[col];
                    double weight = this.f13109d[col];
                    this.f13109d[col] = 0.0d;
                    this.rhs[col] = 0.0d;
                    include(this.x_sing, weight, y2);
                } else {
                    this.sserr += this.f13109d[col] * this.rhs[col] * this.rhs[col];
                }
            }
        }
    }

    private void ss() {
        double total = this.sserr;
        this.rss[this.nvars - 1] = this.sserr;
        for (int i2 = this.nvars - 1; i2 > 0; i2--) {
            total += this.f13109d[i2] * this.rhs[i2] * this.rhs[i2];
            this.rss[i2 - 1] = total;
        }
        this.rss_set = true;
    }

    private double[] cov(int nreq) {
        double total;
        if (this.nobs <= nreq) {
            return null;
        }
        double rnk = 0.0d;
        for (int i2 = 0; i2 < nreq; i2++) {
            if (!this.lindep[i2]) {
                rnk += 1.0d;
            }
        }
        double var = this.rss[nreq - 1] / (this.nobs - rnk);
        double[] rinv = new double[(nreq * (nreq - 1)) / 2];
        inverse(rinv, nreq);
        double[] covmat = new double[(nreq * (nreq + 1)) / 2];
        Arrays.fill(covmat, Double.NaN);
        int start = 0;
        for (int row = 0; row < nreq; row++) {
            int pos2 = start;
            if (!this.lindep[row]) {
                for (int col = row; col < nreq; col++) {
                    if (!this.lindep[col]) {
                        int pos1 = (start + col) - row;
                        if (row == col) {
                            total = 1.0d / this.f13109d[col];
                        } else {
                            total = rinv[pos1 - 1] / this.f13109d[col];
                        }
                        for (int k2 = col + 1; k2 < nreq; k2++) {
                            if (!this.lindep[k2]) {
                                total += (rinv[pos1] * rinv[pos2]) / this.f13109d[k2];
                            }
                            pos1++;
                            pos2++;
                        }
                        covmat[(((col + 1) * col) / 2) + row] = total * var;
                    } else {
                        pos2 += (nreq - col) - 1;
                    }
                }
            }
            start += (nreq - row) - 1;
        }
        return covmat;
    }

    private void inverse(double[] rinv, int nreq) {
        int pos = ((nreq * (nreq - 1)) / 2) - 1;
        Arrays.fill(rinv, Double.NaN);
        for (int row = nreq - 1; row > 0; row--) {
            if (!this.lindep[row]) {
                int start = ((row - 1) * ((this.nvars + this.nvars) - row)) / 2;
                for (int col = nreq; col > row; col--) {
                    int pos1 = start;
                    int pos2 = pos;
                    double total = 0.0d;
                    for (int k2 = row; k2 < col - 1; k2++) {
                        pos2 += (nreq - k2) - 1;
                        if (!this.lindep[k2]) {
                            total += (-this.f13110r[pos1]) * rinv[pos2];
                        }
                        pos1++;
                    }
                    rinv[pos] = total - this.f13110r[pos1];
                    pos--;
                }
            } else {
                pos -= nreq - row;
            }
        }
    }

    public double[] getPartialCorrelations(int in) {
        double[] output = new double[(((this.nvars - in) + 1) * (this.nvars - in)) / 2];
        int rms_off = -in;
        int wrk_off = -(in + 1);
        double[] rms = new double[this.nvars - in];
        double[] work = new double[(this.nvars - in) - 1];
        int offXX = ((this.nvars - in) * ((this.nvars - in) - 1)) / 2;
        if (in < -1 || in >= this.nvars) {
            return null;
        }
        int nvm = this.nvars - 1;
        int base_pos = this.f13110r.length - (((nvm - in) * ((nvm - in) + 1)) / 2);
        if (this.f13109d[in] > 0.0d) {
            rms[in + rms_off] = 1.0d / FastMath.sqrt(this.f13109d[in]);
        }
        for (int col = in + 1; col < this.nvars; col++) {
            int pos = ((base_pos + col) - 1) - in;
            double sumxx = this.f13109d[col];
            for (int row = in; row < col; row++) {
                sumxx += this.f13109d[row] * this.f13110r[pos] * this.f13110r[pos];
                pos += (this.nvars - row) - 2;
            }
            if (sumxx > 0.0d) {
                rms[col + rms_off] = 1.0d / FastMath.sqrt(sumxx);
            } else {
                rms[col + rms_off] = 0.0d;
            }
        }
        double sumyy = this.sserr;
        for (int row2 = in; row2 < this.nvars; row2++) {
            sumyy += this.f13109d[row2] * this.rhs[row2] * this.rhs[row2];
        }
        if (sumyy > 0.0d) {
            sumyy = 1.0d / FastMath.sqrt(sumyy);
        }
        int pos2 = 0;
        for (int col1 = in; col1 < this.nvars; col1++) {
            double sumxy = 0.0d;
            Arrays.fill(work, 0.0d);
            int pos1 = ((base_pos + col1) - in) - 1;
            for (int row3 = in; row3 < col1; row3++) {
                int pos22 = pos1 + 1;
                for (int col2 = col1 + 1; col2 < this.nvars; col2++) {
                    int i2 = col2 + wrk_off;
                    work[i2] = work[i2] + (this.f13109d[row3] * this.f13110r[pos1] * this.f13110r[pos22]);
                    pos22++;
                }
                sumxy += this.f13109d[row3] * this.f13110r[pos1] * this.rhs[row3];
                pos1 += (this.nvars - row3) - 2;
            }
            int pos23 = pos1 + 1;
            for (int col22 = col1 + 1; col22 < this.nvars; col22++) {
                int i3 = col22 + wrk_off;
                work[i3] = work[i3] + (this.f13109d[col1] * this.f13110r[pos23]);
                pos23++;
                output[(((((col22 - 1) - in) * (col22 - in)) / 2) + col1) - in] = work[col22 + wrk_off] * rms[col1 + rms_off] * rms[col22 + rms_off];
                pos2++;
            }
            output[col1 + rms_off + offXX] = (sumxy + (this.f13109d[col1] * this.rhs[col1])) * rms[col1 + rms_off] * sumyy;
        }
        return output;
    }

    private void vmove(int from, int to) {
        int first;
        int inc;
        int count;
        boolean bSkipTo40 = false;
        if (from == to) {
            return;
        }
        if (!this.rss_set) {
            ss();
        }
        if (from < to) {
            first = from;
            inc = 1;
            count = to - from;
        } else {
            first = from - 1;
            inc = -1;
            count = from - to;
        }
        int m2 = first;
        for (int idx = 0; idx < count; idx++) {
            int m1 = (m2 * (((this.nvars + this.nvars) - m2) - 1)) / 2;
            int m22 = ((m1 + this.nvars) - m2) - 1;
            int mp1 = m2 + 1;
            double d1 = this.f13109d[m2];
            double d2 = this.f13109d[mp1];
            if (d1 > this.epsilon || d2 > this.epsilon) {
                double X2 = this.f13110r[m1];
                if (FastMath.abs(X2) * FastMath.sqrt(d1) < this.tol[mp1]) {
                    X2 = 0.0d;
                }
                if (d1 < this.epsilon || FastMath.abs(X2) < this.epsilon) {
                    this.f13109d[m2] = d2;
                    this.f13109d[mp1] = d1;
                    this.f13110r[m1] = 0.0d;
                    for (int col = m2 + 2; col < this.nvars; col++) {
                        m1++;
                        double X3 = this.f13110r[m1];
                        this.f13110r[m1] = this.f13110r[m22];
                        this.f13110r[m22] = X3;
                        m22++;
                    }
                    X2 = this.rhs[m2];
                    this.rhs[m2] = this.rhs[mp1];
                    this.rhs[mp1] = X2;
                    bSkipTo40 = true;
                } else if (d2 < this.epsilon) {
                    this.f13109d[m2] = d1 * X2 * X2;
                    this.f13110r[m1] = 1.0d / X2;
                    for (int _i = m1 + 1; _i < ((m1 + this.nvars) - m2) - 1; _i++) {
                        double[] dArr = this.f13110r;
                        int i2 = _i;
                        dArr[i2] = dArr[i2] / X2;
                    }
                    double[] dArr2 = this.rhs;
                    int i3 = m2;
                    dArr2[i3] = dArr2[i3] / X2;
                    bSkipTo40 = true;
                }
                if (!bSkipTo40) {
                    double d1new = d2 + (d1 * X2 * X2);
                    double cbar = d2 / d1new;
                    double sbar = (X2 * d1) / d1new;
                    double d2new = d1 * cbar;
                    this.f13109d[m2] = d1new;
                    this.f13109d[mp1] = d2new;
                    this.f13110r[m1] = sbar;
                    for (int col2 = m2 + 2; col2 < this.nvars; col2++) {
                        m1++;
                        double Y2 = this.f13110r[m1];
                        this.f13110r[m1] = (cbar * this.f13110r[m22]) + (sbar * Y2);
                        this.f13110r[m22] = Y2 - (X2 * this.f13110r[m22]);
                        m22++;
                    }
                    double Y3 = this.rhs[m2];
                    this.rhs[m2] = (cbar * this.rhs[mp1]) + (sbar * Y3);
                    this.rhs[mp1] = Y3 - (X2 * this.rhs[mp1]);
                }
            }
            if (m2 > 0) {
                int pos = m2;
                for (int row = 0; row < m2; row++) {
                    double X4 = this.f13110r[pos];
                    this.f13110r[pos] = this.f13110r[pos - 1];
                    this.f13110r[pos - 1] = X4;
                    pos += (this.nvars - row) - 2;
                }
            }
            int m12 = this.vorder[m2];
            this.vorder[m2] = this.vorder[mp1];
            this.vorder[mp1] = m12;
            double X5 = this.tol[m2];
            this.tol[m2] = this.tol[mp1];
            this.tol[mp1] = X5;
            this.rss[m2] = this.rss[mp1] + (this.f13109d[mp1] * this.rhs[mp1] * this.rhs[mp1]);
            m2 += inc;
        }
    }

    private int reorderRegressors(int[] list, int pos1) {
        if (list.length < 1 || list.length > (this.nvars + 1) - pos1) {
            return -1;
        }
        int next = pos1;
        for (int i2 = pos1; i2 < this.nvars; i2++) {
            int l2 = this.vorder[i2];
            int j2 = 0;
            while (true) {
                if (j2 >= list.length) {
                    break;
                }
                if (l2 != list[j2] || i2 <= next) {
                    j2++;
                } else {
                    vmove(i2, next);
                    next++;
                    if (next >= list.length + pos1) {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    public double getDiagonalOfHatMatrix(double[] row_data) {
        double[] xrow;
        double[] wk = new double[this.nvars];
        if (row_data.length > this.nvars) {
            return Double.NaN;
        }
        if (this.hasIntercept) {
            xrow = new double[row_data.length + 1];
            xrow[0] = 1.0d;
            System.arraycopy(row_data, 0, xrow, 1, row_data.length);
        } else {
            xrow = row_data;
        }
        double hii = 0.0d;
        for (int col = 0; col < xrow.length; col++) {
            if (FastMath.sqrt(this.f13109d[col]) < this.tol[col]) {
                wk[col] = 0.0d;
            } else {
                int pos = col - 1;
                double total = xrow[col];
                for (int row = 0; row < col; row++) {
                    total = smartAdd(total, (-wk[row]) * this.f13110r[pos]);
                    pos += (this.nvars - row) - 2;
                }
                wk[col] = total;
                hii = smartAdd(hii, (total * total) / this.f13109d[col]);
            }
        }
        return hii;
    }

    public int[] getOrderOfRegressors() {
        return MathArrays.copyOf(this.vorder);
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public RegressionResults regress() throws ModelSpecificationException {
        return regress(this.nvars);
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v5, types: [double[], double[][]] */
    public RegressionResults regress(int numberOfRegressors) throws ModelSpecificationException {
        int i2;
        int i3;
        if (this.nobs <= numberOfRegressors) {
            throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, Long.valueOf(this.nobs), Integer.valueOf(numberOfRegressors));
        }
        if (numberOfRegressors > this.nvars) {
            throw new ModelSpecificationException(LocalizedFormats.TOO_MANY_REGRESSORS, Integer.valueOf(numberOfRegressors), Integer.valueOf(this.nvars));
        }
        tolset();
        singcheck();
        double[] beta = regcf(numberOfRegressors);
        ss();
        double[] cov = cov(numberOfRegressors);
        int rnk = 0;
        for (int i4 = 0; i4 < this.lindep.length; i4++) {
            if (!this.lindep[i4]) {
                rnk++;
            }
        }
        boolean needsReorder = false;
        int i5 = 0;
        while (true) {
            if (i5 >= numberOfRegressors) {
                break;
            }
            if (this.vorder[i5] == i5) {
                i5++;
            } else {
                needsReorder = true;
                break;
            }
        }
        if (!needsReorder) {
            return new RegressionResults(beta, new double[]{cov}, true, this.nobs, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
        }
        double[] betaNew = new double[beta.length];
        double[] covNew = new double[cov.length];
        int[] newIndices = new int[beta.length];
        for (int i6 = 0; i6 < this.nvars; i6++) {
            for (int j2 = 0; j2 < numberOfRegressors; j2++) {
                if (this.vorder[j2] == i6) {
                    betaNew[i6] = beta[j2];
                    newIndices[i6] = j2;
                }
            }
        }
        int idx1 = 0;
        for (int i7 = 0; i7 < beta.length; i7++) {
            int _i = newIndices[i7];
            int j3 = 0;
            while (j3 <= i7) {
                int _j = newIndices[j3];
                if (_i > _j) {
                    i2 = (_i * (_i + 1)) / 2;
                    i3 = _j;
                } else {
                    i2 = (_j * (_j + 1)) / 2;
                    i3 = _i;
                }
                int idx2 = i2 + i3;
                covNew[idx1] = cov[idx2];
                j3++;
                idx1++;
            }
        }
        return new RegressionResults(betaNew, new double[]{covNew}, true, this.nobs, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v5, types: [double[], double[][]] */
    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public RegressionResults regress(int[] variablesToInclude) throws ModelSpecificationException {
        int[] series;
        int i2;
        int i3;
        if (variablesToInclude.length > this.nvars) {
            throw new ModelSpecificationException(LocalizedFormats.TOO_MANY_REGRESSORS, Integer.valueOf(variablesToInclude.length), Integer.valueOf(this.nvars));
        }
        if (this.nobs <= this.nvars) {
            throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, Long.valueOf(this.nobs), Integer.valueOf(this.nvars));
        }
        Arrays.sort(variablesToInclude);
        int iExclude = 0;
        for (int i4 = 0; i4 < variablesToInclude.length; i4++) {
            if (i4 >= this.nvars) {
                throw new ModelSpecificationException(LocalizedFormats.INDEX_LARGER_THAN_MAX, Integer.valueOf(i4), Integer.valueOf(this.nvars));
            }
            if (i4 > 0 && variablesToInclude[i4] == variablesToInclude[i4 - 1]) {
                variablesToInclude[i4] = -1;
                iExclude++;
            }
        }
        if (iExclude > 0) {
            int j2 = 0;
            series = new int[variablesToInclude.length - iExclude];
            for (int i5 = 0; i5 < variablesToInclude.length; i5++) {
                if (variablesToInclude[i5] > -1) {
                    series[j2] = variablesToInclude[i5];
                    j2++;
                }
            }
        } else {
            series = variablesToInclude;
        }
        reorderRegressors(series, 0);
        tolset();
        singcheck();
        double[] beta = regcf(series.length);
        ss();
        double[] cov = cov(series.length);
        int rnk = 0;
        for (int i6 = 0; i6 < this.lindep.length; i6++) {
            if (!this.lindep[i6]) {
                rnk++;
            }
        }
        boolean needsReorder = false;
        int i7 = 0;
        while (true) {
            if (i7 >= this.nvars) {
                break;
            }
            if (this.vorder[i7] == series[i7]) {
                i7++;
            } else {
                needsReorder = true;
                break;
            }
        }
        if (!needsReorder) {
            return new RegressionResults(beta, new double[]{cov}, true, this.nobs, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
        }
        double[] betaNew = new double[beta.length];
        int[] newIndices = new int[beta.length];
        for (int i8 = 0; i8 < series.length; i8++) {
            for (int j3 = 0; j3 < this.vorder.length; j3++) {
                if (this.vorder[j3] == series[i8]) {
                    betaNew[i8] = beta[j3];
                    newIndices[i8] = j3;
                }
            }
        }
        double[] covNew = new double[cov.length];
        int idx1 = 0;
        for (int i9 = 0; i9 < beta.length; i9++) {
            int _i = newIndices[i9];
            int j4 = 0;
            while (j4 <= i9) {
                int _j = newIndices[j4];
                if (_i > _j) {
                    i2 = (_i * (_i + 1)) / 2;
                    i3 = _j;
                } else {
                    i2 = (_j * (_j + 1)) / 2;
                    i3 = _i;
                }
                int idx2 = i2 + i3;
                covNew[idx1] = cov[idx2];
                j4++;
                idx1++;
            }
        }
        return new RegressionResults(betaNew, new double[]{covNew}, true, this.nobs, rnk, this.sumy, this.sumsqy, this.sserr, this.hasIntercept, false);
    }
}
