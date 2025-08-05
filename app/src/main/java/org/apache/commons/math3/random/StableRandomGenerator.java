package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/StableRandomGenerator.class */
public class StableRandomGenerator implements NormalizedRandomGenerator {
    private final RandomGenerator generator;
    private final double alpha;
    private final double beta;
    private final double zeta;

    public StableRandomGenerator(RandomGenerator generator, double alpha, double beta) throws OutOfRangeException, NullArgumentException {
        if (generator == null) {
            throw new NullArgumentException();
        }
        if (alpha <= 0.0d || alpha > 2.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_LEFT, Double.valueOf(alpha), 0, 2);
        }
        if (beta < -1.0d || beta > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_SIMPLE, Double.valueOf(beta), -1, 1);
        }
        this.generator = generator;
        this.alpha = alpha;
        this.beta = beta;
        if (alpha < 2.0d && beta != 0.0d) {
            this.zeta = beta * FastMath.tan((3.141592653589793d * alpha) / 2.0d);
        } else {
            this.zeta = 0.0d;
        }
    }

    @Override // org.apache.commons.math3.random.NormalizedRandomGenerator
    public double nextNormalizedDouble() {
        double x2;
        double omega = -FastMath.log(this.generator.nextDouble());
        double phi = 3.141592653589793d * (this.generator.nextDouble() - 0.5d);
        if (this.alpha == 2.0d) {
            return FastMath.sqrt(2.0d * omega) * FastMath.sin(phi);
        }
        if (this.beta != 0.0d) {
            double cosPhi = FastMath.cos(phi);
            if (FastMath.abs(this.alpha - 1.0d) > 1.0E-8d) {
                double alphaPhi = this.alpha * phi;
                double invAlphaPhi = phi - alphaPhi;
                x2 = (((FastMath.sin(alphaPhi) + (this.zeta * FastMath.cos(alphaPhi))) / cosPhi) * (FastMath.cos(invAlphaPhi) + (this.zeta * FastMath.sin(invAlphaPhi)))) / FastMath.pow(omega * cosPhi, (1.0d - this.alpha) / this.alpha);
            } else {
                double betaPhi = 1.5707963267948966d + (this.beta * phi);
                x2 = 0.6366197723675814d * ((betaPhi * FastMath.tan(phi)) - (this.beta * FastMath.log(((1.5707963267948966d * omega) * cosPhi) / betaPhi)));
                if (this.alpha != 1.0d) {
                    x2 += this.beta * FastMath.tan((3.141592653589793d * this.alpha) / 2.0d);
                }
            }
        } else if (this.alpha == 1.0d) {
            x2 = FastMath.tan(phi);
        } else {
            x2 = (FastMath.pow(omega * FastMath.cos((1.0d - this.alpha) * phi), (1.0d / this.alpha) - 1.0d) * FastMath.sin(this.alpha * phi)) / FastMath.pow(FastMath.cos(phi), 1.0d / this.alpha);
        }
        return x2;
    }
}
