package sun.nio.ch;

import java.io.FileDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import jdk.internal.misc.TerminatingThreadLocal;
import sun.misc.Unsafe;
import sun.misc.VM;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/nio/ch/Util.class */
public class Util {
    private static final int TEMP_BUF_POOL_SIZE;
    private static final long MAX_CACHED_BUFFER_SIZE;
    private static ThreadLocal<BufferCache> bufferCache;
    private static Unsafe unsafe;
    private static int pageSize;
    private static volatile Constructor<?> directByteBufferConstructor;
    private static volatile Constructor<?> directByteBufferRConstructor;
    private static volatile String bugLevel;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Util.class.desiredAssertionStatus();
        TEMP_BUF_POOL_SIZE = IOUtil.IOV_MAX;
        MAX_CACHED_BUFFER_SIZE = getMaxCachedBufferSize();
        bufferCache = new TerminatingThreadLocal<BufferCache>() { // from class: sun.nio.ch.Util.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.lang.ThreadLocal
            public BufferCache initialValue() {
                return new BufferCache();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // jdk.internal.misc.TerminatingThreadLocal
            public void threadTerminated(BufferCache bufferCache2) {
                while (!bufferCache2.isEmpty()) {
                    Util.free(bufferCache2.removeFirst());
                }
            }
        };
        unsafe = Unsafe.getUnsafe();
        pageSize = -1;
        directByteBufferConstructor = null;
        directByteBufferRConstructor = null;
        bugLevel = null;
    }

    private static long getMaxCachedBufferSize() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.nio.ch.Util.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return System.getProperty("jdk.nio.maxCachedBufferSize");
            }
        });
        if (str != null) {
            try {
                long j2 = Long.parseLong(str);
                if (j2 >= 0) {
                    return j2;
                }
                return Long.MAX_VALUE;
            } catch (NumberFormatException e2) {
                return Long.MAX_VALUE;
            }
        }
        return Long.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isBufferTooLarge(int i2) {
        return ((long) i2) > MAX_CACHED_BUFFER_SIZE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isBufferTooLarge(ByteBuffer byteBuffer) {
        return isBufferTooLarge(byteBuffer.capacity());
    }

    /* loaded from: rt.jar:sun/nio/ch/Util$BufferCache.class */
    private static class BufferCache {
        private ByteBuffer[] buffers = new ByteBuffer[Util.TEMP_BUF_POOL_SIZE];
        private int count;
        private int start;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Util.class.desiredAssertionStatus();
        }

        private int next(int i2) {
            return (i2 + 1) % Util.TEMP_BUF_POOL_SIZE;
        }

        BufferCache() {
        }

        ByteBuffer get(int i2) {
            ByteBuffer byteBuffer;
            if (!$assertionsDisabled && Util.isBufferTooLarge(i2)) {
                throw new AssertionError();
            }
            if (this.count == 0) {
                return null;
            }
            ByteBuffer[] byteBufferArr = this.buffers;
            ByteBuffer byteBuffer2 = byteBufferArr[this.start];
            if (byteBuffer2.capacity() < i2) {
                byteBuffer2 = null;
                int i3 = this.start;
                while (true) {
                    int next = next(i3);
                    i3 = next;
                    if (next == this.start || (byteBuffer = byteBufferArr[i3]) == null) {
                        break;
                    }
                    if (byteBuffer.capacity() >= i2) {
                        byteBuffer2 = byteBuffer;
                        break;
                    }
                }
                if (byteBuffer2 == null) {
                    return null;
                }
                byteBufferArr[i3] = byteBufferArr[this.start];
            }
            byteBufferArr[this.start] = null;
            this.start = next(this.start);
            this.count--;
            byteBuffer2.rewind();
            byteBuffer2.limit(i2);
            return byteBuffer2;
        }

        boolean offerFirst(ByteBuffer byteBuffer) {
            if (!$assertionsDisabled && Util.isBufferTooLarge(byteBuffer)) {
                throw new AssertionError();
            }
            if (this.count >= Util.TEMP_BUF_POOL_SIZE) {
                return false;
            }
            this.start = ((this.start + Util.TEMP_BUF_POOL_SIZE) - 1) % Util.TEMP_BUF_POOL_SIZE;
            this.buffers[this.start] = byteBuffer;
            this.count++;
            return true;
        }

        boolean offerLast(ByteBuffer byteBuffer) {
            if (!$assertionsDisabled && Util.isBufferTooLarge(byteBuffer)) {
                throw new AssertionError();
            }
            if (this.count < Util.TEMP_BUF_POOL_SIZE) {
                this.buffers[(this.start + this.count) % Util.TEMP_BUF_POOL_SIZE] = byteBuffer;
                this.count++;
                return true;
            }
            return false;
        }

        boolean isEmpty() {
            return this.count == 0;
        }

        ByteBuffer removeFirst() {
            if (!$assertionsDisabled && this.count <= 0) {
                throw new AssertionError();
            }
            ByteBuffer byteBuffer = this.buffers[this.start];
            this.buffers[this.start] = null;
            this.start = next(this.start);
            this.count--;
            return byteBuffer;
        }
    }

    public static ByteBuffer getTemporaryDirectBuffer(int i2) {
        if (isBufferTooLarge(i2)) {
            return ByteBuffer.allocateDirect(i2);
        }
        BufferCache bufferCache2 = bufferCache.get();
        ByteBuffer byteBuffer = bufferCache2.get(i2);
        if (byteBuffer != null) {
            return byteBuffer;
        }
        if (!bufferCache2.isEmpty()) {
            free(bufferCache2.removeFirst());
        }
        return ByteBuffer.allocateDirect(i2);
    }

    public static void releaseTemporaryDirectBuffer(ByteBuffer byteBuffer) {
        offerFirstTemporaryDirectBuffer(byteBuffer);
    }

    static void offerFirstTemporaryDirectBuffer(ByteBuffer byteBuffer) {
        if (isBufferTooLarge(byteBuffer)) {
            free(byteBuffer);
        } else {
            if (!$assertionsDisabled && byteBuffer == null) {
                throw new AssertionError();
            }
            if (!bufferCache.get().offerFirst(byteBuffer)) {
                free(byteBuffer);
            }
        }
    }

    static void offerLastTemporaryDirectBuffer(ByteBuffer byteBuffer) {
        if (isBufferTooLarge(byteBuffer)) {
            free(byteBuffer);
        } else {
            if (!$assertionsDisabled && byteBuffer == null) {
                throw new AssertionError();
            }
            if (!bufferCache.get().offerLast(byteBuffer)) {
                free(byteBuffer);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static void free(ByteBuffer byteBuffer) {
        ((DirectBuffer) byteBuffer).cleaner().clean();
    }

    static ByteBuffer[] subsequence(ByteBuffer[] byteBufferArr, int i2, int i3) {
        if (i2 == 0 && i3 == byteBufferArr.length) {
            return byteBufferArr;
        }
        ByteBuffer[] byteBufferArr2 = new ByteBuffer[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            byteBufferArr2[i4] = byteBufferArr[i2 + i4];
        }
        return byteBufferArr2;
    }

    static <E> Set<E> ungrowableSet(final Set<E> set) {
        return new Set<E>() { // from class: sun.nio.ch.Util.3
            @Override // java.util.Set
            public int size() {
                return set.size();
            }

            @Override // java.util.Set, java.util.Collection
            public boolean isEmpty() {
                return set.isEmpty();
            }

            @Override // java.util.Set
            public boolean contains(Object obj) {
                return set.contains(obj);
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public Object[] toArray() {
                return set.toArray();
            }

            @Override // java.util.Set, java.util.Collection
            public <T> T[] toArray(T[] tArr) {
                return (T[]) set.toArray(tArr);
            }

            public String toString() {
                return set.toString();
            }

            @Override // java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
            public Iterator<E> iterator() {
                return set.iterator();
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public boolean equals(Object obj) {
                return set.equals(obj);
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public int hashCode() {
                return set.hashCode();
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public void clear() {
                set.clear();
            }

            @Override // java.util.Set
            public boolean remove(Object obj) {
                return set.remove(obj);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean containsAll(Collection<?> collection) {
                return set.containsAll(collection);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean removeAll(Collection<?> collection) {
                return set.removeAll(collection);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean retainAll(Collection<?> collection) {
                return set.retainAll(collection);
            }

            @Override // java.util.Set, java.util.Collection, java.util.List
            public boolean add(E e2) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Set, java.util.Collection
            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static byte _get(long j2) {
        return unsafe.getByte(j2);
    }

    private static void _put(long j2, byte b2) {
        unsafe.putByte(j2, b2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void erase(ByteBuffer byteBuffer) {
        unsafe.setMemory(((DirectBuffer) byteBuffer).address(), byteBuffer.capacity(), (byte) 0);
    }

    static Unsafe unsafe() {
        return unsafe;
    }

    static int pageSize() {
        if (pageSize == -1) {
            pageSize = unsafe().pageSize();
        }
        return pageSize;
    }

    private static void initDBBConstructor() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.ch.Util.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                try {
                    Constructor<?> declaredConstructor = Class.forName("java.nio.DirectByteBuffer").getDeclaredConstructor(Integer.TYPE, Long.TYPE, FileDescriptor.class, Runnable.class);
                    declaredConstructor.setAccessible(true);
                    Constructor unused = Util.directByteBufferConstructor = declaredConstructor;
                    return null;
                } catch (ClassCastException | ClassNotFoundException | IllegalArgumentException | NoSuchMethodException e2) {
                    throw new InternalError(e2);
                }
            }
        });
    }

    static MappedByteBuffer newMappedByteBuffer(int i2, long j2, FileDescriptor fileDescriptor, Runnable runnable) {
        if (directByteBufferConstructor == null) {
            initDBBConstructor();
        }
        try {
            return (MappedByteBuffer) directByteBufferConstructor.newInstance(new Integer(i2), new Long(j2), fileDescriptor, runnable);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e2) {
            throw new InternalError(e2);
        }
    }

    private static void initDBBRConstructor() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.ch.Util.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                try {
                    Constructor<?> declaredConstructor = Class.forName("java.nio.DirectByteBufferR").getDeclaredConstructor(Integer.TYPE, Long.TYPE, FileDescriptor.class, Runnable.class);
                    declaredConstructor.setAccessible(true);
                    Constructor unused = Util.directByteBufferRConstructor = declaredConstructor;
                    return null;
                } catch (ClassCastException | ClassNotFoundException | IllegalArgumentException | NoSuchMethodException e2) {
                    throw new InternalError(e2);
                }
            }
        });
    }

    static MappedByteBuffer newMappedByteBufferR(int i2, long j2, FileDescriptor fileDescriptor, Runnable runnable) {
        if (directByteBufferRConstructor == null) {
            initDBBRConstructor();
        }
        try {
            return (MappedByteBuffer) directByteBufferRConstructor.newInstance(new Integer(i2), new Long(j2), fileDescriptor, runnable);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e2) {
            throw new InternalError(e2);
        }
    }

    static boolean atBugLevel(String str) {
        if (bugLevel == null) {
            if (!VM.isBooted()) {
                return false;
            }
            String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.ch.bugLevel"));
            bugLevel = str2 != null ? str2 : "";
        }
        return bugLevel.equals(str);
    }
}
