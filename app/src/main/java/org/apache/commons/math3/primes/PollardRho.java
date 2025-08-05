package org.apache.commons.math3.primes;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/primes/PollardRho.class */
class PollardRho {
    private PollardRho() {
    }

    public static List<Integer> primeFactors(int n2) {
        List<Integer> factors = new ArrayList<>();
        int n3 = SmallPrimes.smallTrialDivision(n2, factors);
        if (1 == n3) {
            return factors;
        }
        if (SmallPrimes.millerRabinPrimeTest(n3)) {
            factors.add(Integer.valueOf(n3));
            return factors;
        }
        int divisor = rhoBrent(n3);
        factors.add(Integer.valueOf(divisor));
        factors.add(Integer.valueOf(n3 / divisor));
        return factors;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x00b4, code lost:
    
        r0 = gcdPositive(org.apache.commons.math3.util.FastMath.abs(r14), r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00c2, code lost:
    
        if (1 == r0) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00c7, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00c8, code lost:
    
        r12 = r12 + 25;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static int rhoBrent(int r5) {
        /*
            Method dump skipped, instructions count: 219
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.primes.PollardRho.rhoBrent(int):int");
    }

    static int gcdPositive(int a2, int b2) {
        if (a2 == 0) {
            return b2;
        }
        if (b2 == 0) {
            return a2;
        }
        int aTwos = Integer.numberOfTrailingZeros(a2);
        int a3 = a2 >> aTwos;
        int bTwos = Integer.numberOfTrailingZeros(b2);
        int b3 = b2 >> bTwos;
        int shift = FastMath.min(aTwos, bTwos);
        while (a3 != b3) {
            int delta = a3 - b3;
            b3 = FastMath.min(a3, b3);
            int a4 = FastMath.abs(delta);
            a3 = a4 >> Integer.numberOfTrailingZeros(a4);
        }
        return a3 << shift;
    }
}
