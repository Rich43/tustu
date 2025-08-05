package org.apache.commons.math3.primes;

import com.sun.corba.se.impl.io.ObjectStreamClass;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.glass.events.DndEvent;
import com.sun.glass.events.TouchEvent;
import com.sun.jndi.ldap.LdapCtx;
import com.sun.media.sound.DLSModulator;
import com.sun.org.apache.xerces.internal.parsers.XMLGrammarCachingConfiguration;
import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import java.awt.AWTEvent;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.math.BigInteger;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NNTPReply;
import sun.awt.ModalityEvent;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.ccache.FileCCacheConstants;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/primes/SmallPrimes.class */
class SmallPrimes {
    public static final int[] PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, KeyEvent.VK_INPUT_METHOD_ON_OFF, 269, 271, DLSModulator.CONN_DST_VIB_STARTDELAY, NNTPReply.AUTHENTICATION_ACCEPTED, 283, 293, 307, 311, 313, 317, FTPReply.NEED_PASSWORD, 337, 347, 349, 353, 359, 367, 373, 379, 383, LdapCtx.DEFAULT_PORT, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, Event.SCROLL_END, DndEvent.PERFORM, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, TouchEvent.TOUCH_PRESSED, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, Krb5.ASN1_BAD_LENGTH, Krb5.ASN1_BAD_TYPE, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, Event.F2, Event.F6, Event.F12, Event.SCROLL_LOCK, OpCodes.NODETYPE_TEXT, 1033, 1039, ORBConstants.DEFAULT_ACTIVATION_PORT, 1051, 1061, 1063, 1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201, 1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, FileCCacheConstants.KRB5_FCC_FVNO_3, 1289, 1291, 1297, ModalityEvent.MODALITY_POPPED, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499, 1511, 1523, 1531, 1543, 1549, ObjectStreamClass.CLASS_MASK, 1559, 1567, 1571, 1579, 1583, 1597, 1601, 1607, 1609, 1613, 1619, 1621, 1627, 1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699, 1709, 1721, 1723, 1733, 1741, 1747, 1753, 1759, 1777, 1783, 1787, 1789, 1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873, 1877, 1879, 1889, 1901, 1907, 1913, 1931, 1933, 1949, 1951, 1973, 1979, 1987, 1993, 1997, AWTEvent.RESERVED_ID_MAX, Types.ARRAY, Types.NCLOB, 2017, 2027, 2029, XMLGrammarCachingConfiguration.BIG_PRIME, 2053, 2063, 2069, 2081, 2083, 2087, ORBConstants.DEFAULT_INS_PORT, 2099, 2111, 2113, 2129, 2131, 2137, 2141, 2143, 2153, 2161, 2179, 2203, 2207, 2213, 2221, 2237, 2239, 2243, 2251, 2267, 2269, 2273, 2281, 2287, 2293, 2297, 2309, 2311, 2333, 2339, 2341, 2347, 2351, 2357, 2371, 2377, 2381, 2383, 2389, 2393, 2399, 2411, 2417, 2423, 2437, 2441, 2447, 2459, 2467, 2473, 2477, 2503, 2521, 2531, 2539, 2543, 2549, 2551, 2557, 2579, 2591, 2593, 2609, 2617, 2621, 2633, 2647, 2657, 2659, 2663, 2671, 2677, 2683, 2687, 2689, 2693, 2699, 2707, 2711, 2713, 2719, 2729, 2731, 2741, 2749, 2753, 2767, 2777, 2789, 2791, 2797, 2801, 2803, 2819, 2833, 2837, 2843, 2851, 2857, 2861, 2879, 2887, 2897, 2903, 2909, 2917, 2927, 2939, 2953, 2957, 2963, 2969, 2971, 2999, 3001, 3011, 3019, 3023, 3037, 3041, 3049, 3061, 3067, 3079, 3083, 3089, 3109, 3119, 3121, 3137, 3163, 3167, 3169, 3181, 3187, 3191, 3203, 3209, 3217, 3221, 3229, 3251, 3253, 3257, 3259, 3271, 3299, 3301, 3307, 3313, 3319, 3323, GIOPVersion.VERSION_13_XX, 3331, 3343, 3347, 3359, 3361, 3371, 3373, 3389, ObjectStreamClass.METHOD_MASK, 3407, 3413, 3433, 3449, 3457, 3461, 3463, 3467, 3469, 3491, 3499, 3511, 3517, 3527, 3529, 3533, 3539, 3541, 3547, 3557, 3559, 3571, 3581, 3583, 3593, 3607, 3613, 3617, 3623, 3631, 3637, 3643, 3659, 3671};
    public static final int PRIMES_LAST = PRIMES[PRIMES.length - 1];

    private SmallPrimes() {
    }

    public static int smallTrialDivision(int n2, List<Integer> factors) {
        int[] arr$ = PRIMES;
        for (int p2 : arr$) {
            while (0 == n2 % p2) {
                n2 /= p2;
                factors.add(Integer.valueOf(p2));
            }
        }
        return n2;
    }

    public static int boundedTrialDivision(int n2, int maxFactor, List<Integer> factors) {
        int f2 = PRIMES_LAST + 2;
        while (true) {
            if (f2 > maxFactor) {
                break;
            }
            if (0 == n2 % f2) {
                n2 /= f2;
                factors.add(Integer.valueOf(f2));
                break;
            }
            int f3 = f2 + 4;
            if (0 == n2 % f3) {
                n2 /= f3;
                factors.add(Integer.valueOf(f3));
                break;
            }
            f2 = f3 + 2;
        }
        if (n2 != 1) {
            factors.add(Integer.valueOf(n2));
        }
        return n2;
    }

    public static List<Integer> trialDivision(int n2) {
        List<Integer> factors = new ArrayList<>(32);
        int n3 = smallTrialDivision(n2, factors);
        if (1 == n3) {
            return factors;
        }
        int bound = (int) FastMath.sqrt(n3);
        boundedTrialDivision(n3, bound, factors);
        return factors;
    }

    public static boolean millerRabinPrimeTest(int n2) throws RuntimeException {
        int nMinus1 = n2 - 1;
        int s2 = Integer.numberOfTrailingZeros(nMinus1);
        int r2 = nMinus1 >> s2;
        int t2 = 1;
        if (n2 >= 2047) {
            t2 = 2;
        }
        if (n2 >= 1373653) {
            t2 = 3;
        }
        if (n2 >= 25326001) {
            t2 = 4;
        }
        BigInteger br2 = BigInteger.valueOf(r2);
        BigInteger bn2 = BigInteger.valueOf(n2);
        for (int i2 = 0; i2 < t2; i2++) {
            BigInteger a2 = BigInteger.valueOf(PRIMES[i2]);
            BigInteger bPow = a2.modPow(br2, bn2);
            int y2 = bPow.intValue();
            if (1 != y2 && y2 != nMinus1) {
                for (int j2 = 1; j2 <= s2 - 1 && nMinus1 != y2; j2++) {
                    long square = y2 * y2;
                    y2 = (int) (square % n2);
                    if (1 == y2) {
                        return false;
                    }
                }
                if (nMinus1 != y2) {
                    return false;
                }
            }
        }
        return true;
    }
}
