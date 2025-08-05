package org.apache.commons.math3.analysis.integration.gauss;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.math.BigDecimal;
import java.math.MathContext;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/gauss/LegendreHighPrecisionRuleFactory.class */
public class LegendreHighPrecisionRuleFactory extends BaseRuleFactory<BigDecimal> {
    private final MathContext mContext;
    private final BigDecimal two;
    private final BigDecimal minusOne;
    private final BigDecimal oneHalf;

    public LegendreHighPrecisionRuleFactory() {
        this(MathContext.DECIMAL128);
    }

    public LegendreHighPrecisionRuleFactory(MathContext mContext) {
        this.mContext = mContext;
        this.two = new BigDecimal("2", mContext);
        this.minusOne = new BigDecimal("-1", mContext);
        this.oneHalf = new BigDecimal(JPEG.version, mContext);
    }

    @Override // org.apache.commons.math3.analysis.integration.gauss.BaseRuleFactory
    protected Pair<BigDecimal[], BigDecimal[]> computeRule(int numberOfPoints) throws DimensionMismatchException {
        if (numberOfPoints == 1) {
            return new Pair<>(new BigDecimal[]{BigDecimal.ZERO}, new BigDecimal[]{this.two});
        }
        BigDecimal[] previousPoints = getRuleInternal(numberOfPoints - 1).getFirst();
        BigDecimal[] points = new BigDecimal[numberOfPoints];
        BigDecimal[] weights = new BigDecimal[numberOfPoints];
        int iMax = numberOfPoints / 2;
        int i2 = 0;
        while (i2 < iMax) {
            BigDecimal a2 = i2 == 0 ? this.minusOne : previousPoints[i2 - 1];
            BigDecimal b2 = iMax == 1 ? BigDecimal.ONE : previousPoints[i2];
            BigDecimal pma = BigDecimal.ONE;
            BigDecimal pa = a2;
            BigDecimal pmb = BigDecimal.ONE;
            BigDecimal pb = b2;
            for (int j2 = 1; j2 < numberOfPoints; j2++) {
                BigDecimal b_two_j_p_1 = new BigDecimal((2 * j2) + 1, this.mContext);
                BigDecimal b_j = new BigDecimal(j2, this.mContext);
                BigDecimal b_j_p_1 = new BigDecimal(j2 + 1, this.mContext);
                BigDecimal tmp1 = a2.multiply(b_two_j_p_1, this.mContext);
                BigDecimal tmp12 = pa.multiply(tmp1, this.mContext);
                BigDecimal tmp2 = pma.multiply(b_j, this.mContext);
                BigDecimal ppa = tmp12.subtract(tmp2, this.mContext);
                BigDecimal ppa2 = ppa.divide(b_j_p_1, this.mContext);
                BigDecimal tmp13 = b2.multiply(b_two_j_p_1, this.mContext);
                BigDecimal tmp14 = pb.multiply(tmp13, this.mContext);
                BigDecimal tmp22 = pmb.multiply(b_j, this.mContext);
                BigDecimal ppb = tmp14.subtract(tmp22, this.mContext);
                pma = pa;
                pa = ppa2;
                pmb = pb;
                pb = ppb.divide(b_j_p_1, this.mContext);
            }
            BigDecimal c2 = a2.add(b2, this.mContext).multiply(this.oneHalf, this.mContext);
            BigDecimal pmc = BigDecimal.ONE;
            BigDecimal pc = c2;
            boolean done = false;
            while (!done) {
                BigDecimal tmp15 = b2.subtract(a2, this.mContext);
                BigDecimal tmp23 = c2.ulp().multiply(BigDecimal.TEN, this.mContext);
                done = tmp15.compareTo(tmp23) <= 0;
                pmc = BigDecimal.ONE;
                pc = c2;
                for (int j3 = 1; j3 < numberOfPoints; j3++) {
                    BigDecimal b_two_j_p_12 = new BigDecimal((2 * j3) + 1, this.mContext);
                    BigDecimal b_j2 = new BigDecimal(j3, this.mContext);
                    BigDecimal b_j_p_12 = new BigDecimal(j3 + 1, this.mContext);
                    BigDecimal tmp16 = c2.multiply(b_two_j_p_12, this.mContext);
                    BigDecimal tmp17 = pc.multiply(tmp16, this.mContext);
                    BigDecimal tmp24 = pmc.multiply(b_j2, this.mContext);
                    BigDecimal ppc = tmp17.subtract(tmp24, this.mContext);
                    pmc = pc;
                    pc = ppc.divide(b_j_p_12, this.mContext);
                }
                if (!done) {
                    if (pa.signum() * pc.signum() <= 0) {
                        b2 = c2;
                    } else {
                        a2 = c2;
                        pa = pc;
                    }
                    c2 = a2.add(b2, this.mContext).multiply(this.oneHalf, this.mContext);
                }
            }
            BigDecimal nP = new BigDecimal(numberOfPoints, this.mContext);
            BigDecimal tmp18 = pmc.subtract(c2.multiply(pc, this.mContext), this.mContext);
            BigDecimal tmp19 = tmp18.multiply(nP).pow(2, this.mContext);
            BigDecimal tmp25 = c2.pow(2, this.mContext);
            BigDecimal tmp26 = BigDecimal.ONE.subtract(tmp25, this.mContext).multiply(this.two, this.mContext).divide(tmp19, this.mContext);
            points[i2] = c2;
            weights[i2] = tmp26;
            int idx = (numberOfPoints - i2) - 1;
            points[idx] = c2.negate(this.mContext);
            weights[idx] = tmp26;
            i2++;
        }
        if (numberOfPoints % 2 != 0) {
            BigDecimal pmc2 = BigDecimal.ONE;
            for (int j4 = 1; j4 < numberOfPoints; j4 += 2) {
                BigDecimal b_j3 = new BigDecimal(j4, this.mContext);
                BigDecimal b_j_p_13 = new BigDecimal(j4 + 1, this.mContext);
                pmc2 = pmc2.multiply(b_j3, this.mContext).divide(b_j_p_13, this.mContext).negate(this.mContext);
            }
            BigDecimal nP2 = new BigDecimal(numberOfPoints, this.mContext);
            BigDecimal tmp110 = pmc2.multiply(nP2, this.mContext);
            BigDecimal tmp27 = this.two.divide(tmp110.pow(2, this.mContext), this.mContext);
            points[iMax] = BigDecimal.ZERO;
            weights[iMax] = tmp27;
        }
        return new Pair<>(points, weights);
    }
}
