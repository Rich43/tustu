package java.nio.charset;

import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:java/nio/charset/CoderResult.class */
public class CoderResult {
    private static final int CR_UNDERFLOW = 0;
    private static final int CR_OVERFLOW = 1;
    private static final int CR_ERROR_MIN = 2;
    private static final int CR_MALFORMED = 2;
    private static final int CR_UNMAPPABLE = 3;
    private static final String[] names;
    private final int type;
    private final int length;
    public static final CoderResult UNDERFLOW;
    public static final CoderResult OVERFLOW;
    private static Cache malformedCache;
    private static Cache unmappableCache;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CoderResult.class.desiredAssertionStatus();
        names = new String[]{"UNDERFLOW", "OVERFLOW", "MALFORMED", "UNMAPPABLE"};
        UNDERFLOW = new CoderResult(0, 0);
        OVERFLOW = new CoderResult(1, 0);
        malformedCache = new Cache() { // from class: java.nio.charset.CoderResult.1
            @Override // java.nio.charset.CoderResult.Cache
            public CoderResult create(int i2) {
                return new CoderResult(2, i2);
            }
        };
        unmappableCache = new Cache() { // from class: java.nio.charset.CoderResult.2
            @Override // java.nio.charset.CoderResult.Cache
            public CoderResult create(int i2) {
                return new CoderResult(3, i2);
            }
        };
    }

    private CoderResult(int i2, int i3) {
        this.type = i2;
        this.length = i3;
    }

    public String toString() {
        String str = names[this.type];
        return isError() ? str + "[" + this.length + "]" : str;
    }

    public boolean isUnderflow() {
        return this.type == 0;
    }

    public boolean isOverflow() {
        return this.type == 1;
    }

    public boolean isError() {
        return this.type >= 2;
    }

    public boolean isMalformed() {
        return this.type == 2;
    }

    public boolean isUnmappable() {
        return this.type == 3;
    }

    public int length() {
        if (!isError()) {
            throw new UnsupportedOperationException();
        }
        return this.length;
    }

    /* loaded from: rt.jar:java/nio/charset/CoderResult$Cache.class */
    private static abstract class Cache {
        private Map<Integer, WeakReference<CoderResult>> cache;

        protected abstract CoderResult create(int i2);

        private Cache() {
            this.cache = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized CoderResult get(int i2) {
            if (i2 <= 0) {
                throw new IllegalArgumentException("Non-positive length");
            }
            Integer num = new Integer(i2);
            CoderResult coderResultCreate = null;
            if (this.cache == null) {
                this.cache = new HashMap();
            } else {
                WeakReference<CoderResult> weakReference = this.cache.get(num);
                if (weakReference != null) {
                    coderResultCreate = weakReference.get();
                }
            }
            if (coderResultCreate == null) {
                coderResultCreate = create(i2);
                this.cache.put(num, new WeakReference<>(coderResultCreate));
            }
            return coderResultCreate;
        }
    }

    public static CoderResult malformedForLength(int i2) {
        return malformedCache.get(i2);
    }

    public static CoderResult unmappableForLength(int i2) {
        return unmappableCache.get(i2);
    }

    public void throwException() throws CharacterCodingException {
        switch (this.type) {
            case 0:
                throw new BufferUnderflowException();
            case 1:
                throw new BufferOverflowException();
            case 2:
                throw new MalformedInputException(this.length);
            case 3:
                throw new UnmappableCharacterException(this.length);
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return;
        }
    }
}
