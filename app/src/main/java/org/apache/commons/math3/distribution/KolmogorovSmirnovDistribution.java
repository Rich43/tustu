package org.apache.commons.math3.distribution;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.BigFractionField;
import org.apache.commons.math3.fraction.FractionConversionException;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/KolmogorovSmirnovDistribution.class */
public class KolmogorovSmirnovDistribution implements Serializable {
    private static final long serialVersionUID = -4670676796862967187L;

    /* renamed from: n, reason: collision with root package name */
    private int f12987n;

    public KolmogorovSmirnovDistribution(int n2) throws NotStrictlyPositiveException {
        if (n2 <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_NUMBER_OF_SAMPLES, Integer.valueOf(n2));
        }
        this.f12987n = n2;
    }

    public double cdf(double d2) throws MathArithmeticException {
        return cdf(d2, false);
    }

    public double cdfExact(double d2) throws MathArithmeticException {
        return cdf(d2, true);
    }

    public double cdf(double d2, boolean exact) throws MathArithmeticException {
        double ninv = 1.0d / this.f12987n;
        double ninvhalf = 0.5d * ninv;
        if (d2 <= ninvhalf) {
            return 0.0d;
        }
        if (ninvhalf >= d2 || d2 > ninv) {
            if (1.0d - ninv <= d2 && d2 < 1.0d) {
                return 1.0d - (2.0d * FastMath.pow(1.0d - d2, this.f12987n));
            }
            if (1.0d <= d2) {
                return 1.0d;
            }
            return exact ? exactK(d2) : roundedK(d2);
        }
        double res = 1.0d;
        double f2 = (2.0d * d2) - ninv;
        for (int i2 = 1; i2 <= this.f12987n; i2++) {
            res *= i2 * f2;
        }
        return res;
    }

    private double exactK(double d2) throws FractionConversionException, NumberIsTooLargeException, MathArithmeticException {
        int k2 = (int) FastMath.ceil(this.f12987n * d2);
        FieldMatrix<BigFraction> H2 = createH(d2);
        BigFraction pFrac = (BigFraction) H2.power(this.f12987n).getEntry(k2 - 1, k2 - 1);
        for (int i2 = 1; i2 <= this.f12987n; i2++) {
            pFrac = pFrac.multiply(i2).divide(this.f12987n);
        }
        return pFrac.bigDecimalValue(20, 4).doubleValue();
    }

    private double roundedK(double d2) throws OutOfRangeException, NotPositiveException, FractionConversionException, NonSquareMatrixException, NumberIsTooLargeException, MathArithmeticException {
        int k2 = (int) FastMath.ceil(this.f12987n * d2);
        FieldMatrix<BigFraction> HBigFraction = createH(d2);
        int m2 = HBigFraction.getRowDimension();
        RealMatrix H2 = new Array2DRowRealMatrix(m2, m2);
        for (int i2 = 0; i2 < m2; i2++) {
            for (int j2 = 0; j2 < m2; j2++) {
                H2.setEntry(i2, j2, ((BigFraction) HBigFraction.getEntry(i2, j2)).doubleValue());
            }
        }
        RealMatrix Hpower = H2.power(this.f12987n);
        double pFrac = Hpower.getEntry(k2 - 1, k2 - 1);
        for (int i3 = 1; i3 <= this.f12987n; i3++) {
            pFrac *= i3 / this.f12987n;
        }
        return pFrac;
    }

    private FieldMatrix<BigFraction> createH(double d2) throws FractionConversionException, NumberIsTooLargeException {
        BigFraction h2;
        int k2 = (int) FastMath.ceil(this.f12987n * d2);
        int m2 = (2 * k2) - 1;
        double hDouble = k2 - (this.f12987n * d2);
        if (hDouble >= 1.0d) {
            throw new NumberIsTooLargeException(Double.valueOf(hDouble), Double.valueOf(1.0d), false);
        }
        try {
            h2 = new BigFraction(hDouble, 1.0E-20d, 10000);
        } catch (FractionConversionException e2) {
            try {
                h2 = new BigFraction(hDouble, 1.0E-10d, 10000);
            } catch (FractionConversionException e3) {
                h2 = new BigFraction(hDouble, 1.0E-5d, 10000);
            }
        }
        BigFraction[][] Hdata = new BigFraction[m2][m2];
        for (int i2 = 0; i2 < m2; i2++) {
            for (int j2 = 0; j2 < m2; j2++) {
                if ((i2 - j2) + 1 < 0) {
                    Hdata[i2][j2] = BigFraction.ZERO;
                } else {
                    Hdata[i2][j2] = BigFraction.ONE;
                }
            }
        }
        BigFraction[] hPowers = new BigFraction[m2];
        hPowers[0] = h2;
        for (int i3 = 1; i3 < m2; i3++) {
            hPowers[i3] = h2.multiply(hPowers[i3 - 1]);
        }
        for (int i4 = 0; i4 < m2; i4++) {
            Hdata[i4][0] = Hdata[i4][0].subtract(hPowers[i4]);
            Hdata[m2 - 1][i4] = Hdata[m2 - 1][i4].subtract(hPowers[(m2 - i4) - 1]);
        }
        if (h2.compareTo(BigFraction.ONE_HALF) == 1) {
            Hdata[m2 - 1][0] = Hdata[m2 - 1][0].add(h2.multiply(2).subtract(1).pow(m2));
        }
        for (int i5 = 0; i5 < m2; i5++) {
            for (int j3 = 0; j3 < i5 + 1; j3++) {
                if ((i5 - j3) + 1 > 0) {
                    for (int g2 = 2; g2 <= (i5 - j3) + 1; g2++) {
                        Hdata[i5][j3] = Hdata[i5][j3].divide(g2);
                    }
                }
            }
        }
        return new Array2DRowFieldMatrix(BigFractionField.getInstance(), Hdata);
    }
}
