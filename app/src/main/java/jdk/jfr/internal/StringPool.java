package jdk.jfr.internal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import sun.misc.Unsafe;
import sun.security.pkcs11.wrapper.PKCS11Constants;

/* loaded from: jfr.jar:jdk/jfr/internal/StringPool.class */
public final class StringPool {
    static final int MIN_LIMIT = 16;
    static final int MAX_LIMIT = 128;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final SimpleStringIdPool sp = new SimpleStringIdPool();
    private static final long epochAddress = JVM.getJVM().getEpochAddress();

    static {
        sp.reset();
    }

    public static long addString(String str) {
        return sp.addString(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean getCurrentEpoch() {
        return unsafe.getByte(epochAddress) == 1;
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/StringPool$SimpleStringIdPool.class */
    private static class SimpleStringIdPool {
        private boolean poolEpoch;
        private long currentSizeUTF16;
        private static final int preCacheMask = 3;
        private final AtomicLong sidIdx = new AtomicLong();
        private final int MAX_SIZE = 32768;
        private final long MAX_SIZE_UTF16 = PKCS11Constants.CKF_EC_UNCOMPRESS;
        private final String[] preCache = {"", "", "", ""};
        private int preCacheOld = 0;
        private final ConcurrentHashMap<String, Long> cache = new ConcurrentHashMap<>(32768, 0.75f);

        SimpleStringIdPool() {
        }

        void reset() {
            reset(StringPool.getCurrentEpoch());
        }

        private void reset(boolean z2) {
            this.cache.clear();
            this.poolEpoch = z2;
            this.currentSizeUTF16 = 0L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long addString(String str) {
            boolean currentEpoch = StringPool.getCurrentEpoch();
            if (this.poolEpoch == currentEpoch) {
                Long l2 = this.cache.get(str);
                if (l2 != null) {
                    return l2.longValue();
                }
            } else {
                reset(currentEpoch);
            }
            if (!preCache(str)) {
                return -1L;
            }
            if (this.cache.size() > 32768 || this.currentSizeUTF16 > PKCS11Constants.CKF_EC_UNCOMPRESS) {
                reset(currentEpoch);
            }
            return storeString(str);
        }

        private long storeString(String str) {
            boolean zAddStringConstant;
            long andIncrement = this.sidIdx.getAndIncrement();
            this.cache.put(str, Long.valueOf(andIncrement));
            synchronized (SimpleStringIdPool.class) {
                zAddStringConstant = JVM.addStringConstant(this.poolEpoch, andIncrement, str);
                this.currentSizeUTF16 += str.length();
            }
            if (zAddStringConstant == this.poolEpoch) {
                return andIncrement;
            }
            return -1L;
        }

        private boolean preCache(String str) {
            if (this.preCache[0].equals(str) || this.preCache[1].equals(str) || this.preCache[2].equals(str) || this.preCache[3].equals(str)) {
                return true;
            }
            this.preCacheOld = (this.preCacheOld - 1) & 3;
            this.preCache[this.preCacheOld] = str;
            return false;
        }
    }
}
