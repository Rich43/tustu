package org.apache.commons.math3.primes;

import java.util.List;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/primes/Primes.class */
public class Primes {
    private Primes() {
    }

    public static boolean isPrime(int n2) {
        if (n2 < 2) {
            return false;
        }
        int[] arr$ = SmallPrimes.PRIMES;
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$++) {
            int p2 = arr$[i$];
            if (0 == n2 % p2) {
                return n2 == p2;
            }
        }
        return SmallPrimes.millerRabinPrimeTest(n2);
    }

    public static int nextPrime(int n2) {
        if (n2 < 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.NUMBER_TOO_SMALL, Integer.valueOf(n2), 0);
        }
        if (n2 == 2) {
            return 2;
        }
        int n3 = n2 | 1;
        if (n3 == 1) {
            return 2;
        }
        if (isPrime(n3)) {
            return n3;
        }
        int rem = n3 % 3;
        if (0 == rem) {
            n3 += 2;
        } else if (1 == rem) {
            n3 += 4;
        }
        while (!isPrime(n3)) {
            int n4 = n3 + 2;
            if (isPrime(n4)) {
                return n4;
            }
            n3 = n4 + 4;
        }
        return n3;
    }

    public static List<Integer> primeFactors(int n2) {
        if (n2 < 2) {
            throw new MathIllegalArgumentException(LocalizedFormats.NUMBER_TOO_SMALL, Integer.valueOf(n2), 2);
        }
        return SmallPrimes.trialDivision(n2);
    }
}
