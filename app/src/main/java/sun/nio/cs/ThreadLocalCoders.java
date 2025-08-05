package sun.nio.cs;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/* loaded from: rt.jar:sun/nio/cs/ThreadLocalCoders.class */
public class ThreadLocalCoders {
    private static final int CACHE_SIZE = 3;
    private static Cache decoderCache = new Cache(3) { // from class: sun.nio.cs.ThreadLocalCoders.1
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ThreadLocalCoders.class.desiredAssertionStatus();
        }

        @Override // sun.nio.cs.ThreadLocalCoders.Cache
        boolean hasName(Object obj, Object obj2) {
            if (obj2 instanceof String) {
                return ((CharsetDecoder) obj).charset().name().equals(obj2);
            }
            if (obj2 instanceof Charset) {
                return ((CharsetDecoder) obj).charset().equals(obj2);
            }
            return false;
        }

        @Override // sun.nio.cs.ThreadLocalCoders.Cache
        Object create(Object obj) {
            if (obj instanceof String) {
                return Charset.forName((String) obj).newDecoder();
            }
            if (obj instanceof Charset) {
                return ((Charset) obj).newDecoder();
            }
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }
    };
    private static Cache encoderCache = new Cache(3) { // from class: sun.nio.cs.ThreadLocalCoders.2
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ThreadLocalCoders.class.desiredAssertionStatus();
        }

        @Override // sun.nio.cs.ThreadLocalCoders.Cache
        boolean hasName(Object obj, Object obj2) {
            if (obj2 instanceof String) {
                return ((CharsetEncoder) obj).charset().name().equals(obj2);
            }
            if (obj2 instanceof Charset) {
                return ((CharsetEncoder) obj).charset().equals(obj2);
            }
            return false;
        }

        @Override // sun.nio.cs.ThreadLocalCoders.Cache
        Object create(Object obj) {
            if (obj instanceof String) {
                return Charset.forName((String) obj).newEncoder();
            }
            if (obj instanceof Charset) {
                return ((Charset) obj).newEncoder();
            }
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }
    };

    /* loaded from: rt.jar:sun/nio/cs/ThreadLocalCoders$Cache.class */
    private static abstract class Cache {
        private ThreadLocal<Object[]> cache = new ThreadLocal<>();
        private final int size;

        abstract Object create(Object obj);

        abstract boolean hasName(Object obj, Object obj2);

        Cache(int i2) {
            this.size = i2;
        }

        private void moveToFront(Object[] objArr, int i2) {
            Object obj = objArr[i2];
            for (int i3 = i2; i3 > 0; i3--) {
                objArr[i3] = objArr[i3 - 1];
            }
            objArr[0] = obj;
        }

        Object forName(Object obj) {
            Object[] objArr = this.cache.get();
            if (objArr == null) {
                objArr = new Object[this.size];
                this.cache.set(objArr);
            } else {
                for (int i2 = 0; i2 < objArr.length; i2++) {
                    Object obj2 = objArr[i2];
                    if (obj2 != null && hasName(obj2, obj)) {
                        if (i2 > 0) {
                            moveToFront(objArr, i2);
                        }
                        return obj2;
                    }
                }
            }
            Object objCreate = create(obj);
            objArr[objArr.length - 1] = objCreate;
            moveToFront(objArr, objArr.length - 1);
            return objCreate;
        }
    }

    public static CharsetDecoder decoderFor(Object obj) {
        CharsetDecoder charsetDecoder = (CharsetDecoder) decoderCache.forName(obj);
        charsetDecoder.reset();
        return charsetDecoder;
    }

    public static CharsetEncoder encoderFor(Object obj) {
        CharsetEncoder charsetEncoder = (CharsetEncoder) encoderCache.forName(obj);
        charsetEncoder.reset();
        return charsetEncoder;
    }
}
