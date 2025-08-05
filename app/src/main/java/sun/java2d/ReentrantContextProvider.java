package sun.java2d;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import sun.java2d.ReentrantContext;

/* loaded from: rt.jar:sun/java2d/ReentrantContextProvider.class */
public abstract class ReentrantContextProvider<K extends ReentrantContext> {
    static final byte USAGE_TL_INACTIVE = 0;
    static final byte USAGE_TL_IN_USE = 1;
    static final byte USAGE_CLQ = 2;
    public static final int REF_HARD = 0;
    public static final int REF_SOFT = 1;
    public static final int REF_WEAK = 2;
    private final int refType;

    protected abstract K newContext();

    public abstract K acquire();

    public abstract void release(K k2);

    protected ReentrantContextProvider(int i2) {
        this.refType = i2;
    }

    protected final Reference<K> getOrCreateReference(K k2) {
        if (k2.reference == null) {
            switch (this.refType) {
                case 0:
                    k2.reference = new HardReference(k2);
                    break;
                case 1:
                    k2.reference = new SoftReference(k2);
                    break;
                case 2:
                default:
                    k2.reference = new WeakReference(k2);
                    break;
            }
        }
        return (Reference<K>) k2.reference;
    }

    /* loaded from: rt.jar:sun/java2d/ReentrantContextProvider$HardReference.class */
    static final class HardReference<V> extends WeakReference<V> {
        private final V strongRef;

        HardReference(V v2) {
            super(null);
            this.strongRef = v2;
        }

        @Override // java.lang.ref.Reference
        public V get() {
            return this.strongRef;
        }
    }
}
