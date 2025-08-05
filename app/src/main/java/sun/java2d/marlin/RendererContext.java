package sun.java2d.marlin;

import java.awt.geom.Path2D;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;
import sun.java2d.ReentrantContext;
import sun.java2d.marlin.MarlinRenderingEngine;

/* loaded from: rt.jar:sun/java2d/marlin/RendererContext.class */
final class RendererContext extends ReentrantContext implements MarlinConst {
    private static final AtomicInteger contextCount = new AtomicInteger(1);
    static final RendererStats stats;
    private static final boolean USE_CACHE_HARD_REF;
    final String name;
    final MarlinRenderingEngine.NormalizingPathIterator nPCPathIterator;
    final MarlinRenderingEngine.NormalizingPathIterator nPQPathIterator;
    final TransformingPathConsumer2D transformerPC2D;
    final Renderer renderer;
    final Stroker stroker;
    final Dasher dasher;
    final MarlinTileGenerator ptg;
    final MarlinCache cache;
    boolean dirty = false;
    WeakReference<ArrayCachesHolder> refArrayCaches = null;
    ArrayCachesHolder hardRefArrayCaches = null;
    final float[] float6 = new float[6];
    final Curve curve = new Curve();
    Path2D.Float p2d = null;
    final CollinearSimplifier simplifier = new CollinearSimplifier();
    int stroking = 0;

    static {
        stats = !doStats ? null : RendererStats.getInstance();
        USE_CACHE_HARD_REF = doStats || MarlinRenderingEngine.REF_TYPE == 2;
    }

    static RendererContext createContext() {
        RendererContext rendererContext = new RendererContext("ctx" + Integer.toString(contextCount.getAndIncrement()));
        if (stats != null) {
            stats.allContexts.add(rendererContext);
        }
        return rendererContext;
    }

    RendererContext(String str) {
        if (logCreateContext) {
            MarlinUtils.logInfo("new RendererContext = " + str);
        }
        this.name = str;
        this.nPCPathIterator = new MarlinRenderingEngine.NormalizingPathIterator.NearestPixelCenter(this.float6);
        this.nPQPathIterator = new MarlinRenderingEngine.NormalizingPathIterator.NearestPixelQuarter(this.float6);
        this.transformerPC2D = new TransformingPathConsumer2D();
        this.cache = new MarlinCache(this);
        this.renderer = new Renderer(this);
        this.ptg = new MarlinTileGenerator(this.renderer);
        this.stroker = new Stroker(this);
        this.dasher = new Dasher(this);
    }

    void dispose() {
        this.stroking = 0;
        if (!USE_CACHE_HARD_REF) {
            this.hardRefArrayCaches = null;
        }
        if (this.dirty) {
            this.nPCPathIterator.dispose();
            this.nPQPathIterator.dispose();
            this.dasher.dispose();
            this.stroker.dispose();
            this.dirty = false;
        }
    }

    ArrayCachesHolder getArrayCachesHolder() {
        ArrayCachesHolder arrayCachesHolder = this.hardRefArrayCaches;
        if (arrayCachesHolder == null) {
            arrayCachesHolder = this.refArrayCaches != null ? this.refArrayCaches.get() : null;
            if (arrayCachesHolder == null) {
                if (logCreateContext) {
                    MarlinUtils.logInfo("new ArrayCachesHolder for RendererContext = " + this.name);
                }
                arrayCachesHolder = new ArrayCachesHolder();
                if (USE_CACHE_HARD_REF) {
                    this.hardRefArrayCaches = arrayCachesHolder;
                }
                this.refArrayCaches = new WeakReference<>(arrayCachesHolder);
            }
        }
        return arrayCachesHolder;
    }

    ByteArrayCache getDirtyByteArrayCache(int i2) {
        return getArrayCachesHolder().dirtyByteArrayCaches[ArrayCache.getBucketDirtyBytes(i2)];
    }

    byte[] getDirtyByteArray(int i2) {
        if (i2 <= ArrayCache.MAX_DIRTY_BYTE_ARRAY_SIZE) {
            return getDirtyByteArrayCache(i2).getArray();
        }
        if (doStats) {
            ArrayCache.incOversize();
        }
        if (doLogOverSize) {
            MarlinUtils.logInfo("getDirtyByteArray[oversize]: length=\t" + i2);
        }
        return new byte[i2];
    }

    void putDirtyByteArray(byte[] bArr) {
        int length = bArr.length;
        if ((length & 1) == 0 && length <= ArrayCache.MAX_DIRTY_BYTE_ARRAY_SIZE) {
            getDirtyByteArrayCache(length).putDirtyArray(bArr, length);
        }
    }

    byte[] widenDirtyByteArray(byte[] bArr, int i2, int i3) {
        int length = bArr.length;
        if (doChecks && length >= i3) {
            return bArr;
        }
        if (doStats) {
            ArrayCache.incResizeDirtyByte();
        }
        byte[] dirtyByteArray = getDirtyByteArray(ArrayCache.getNewSize(i2, i3));
        System.arraycopy(bArr, 0, dirtyByteArray, 0, i2);
        putDirtyByteArray(bArr);
        if (doLogWidenArray) {
            MarlinUtils.logInfo("widenDirtyByteArray[" + dirtyByteArray.length + "]: usedSize=\t" + i2 + "\tlength=\t" + length + "\tneeded length=\t" + i3);
        }
        return dirtyByteArray;
    }

    IntArrayCache getIntArrayCache(int i2) {
        return getArrayCachesHolder().intArrayCaches[ArrayCache.getBucket(i2)];
    }

    int[] getIntArray(int i2) {
        if (i2 <= ArrayCache.MAX_ARRAY_SIZE) {
            return getIntArrayCache(i2).getArray();
        }
        if (doStats) {
            ArrayCache.incOversize();
        }
        if (doLogOverSize) {
            MarlinUtils.logInfo("getIntArray[oversize]: length=\t" + i2);
        }
        return new int[i2];
    }

    int[] widenIntArray(int[] iArr, int i2, int i3, int i4) {
        int length = iArr.length;
        if (doChecks && length >= i3) {
            return iArr;
        }
        if (doStats) {
            ArrayCache.incResizeInt();
        }
        int[] intArray = getIntArray(ArrayCache.getNewSize(i2, i3));
        System.arraycopy(iArr, 0, intArray, 0, i2);
        putIntArray(iArr, 0, i4);
        if (doLogWidenArray) {
            MarlinUtils.logInfo("widenIntArray[" + intArray.length + "]: usedSize=\t" + i2 + "\tlength=\t" + length + "\tneeded length=\t" + i3);
        }
        return intArray;
    }

    void putIntArray(int[] iArr, int i2, int i3) {
        int length = iArr.length;
        if ((length & 1) == 0 && length <= ArrayCache.MAX_ARRAY_SIZE) {
            getIntArrayCache(length).putArray(iArr, length, i2, i3);
        }
    }

    IntArrayCache getDirtyIntArrayCache(int i2) {
        return getArrayCachesHolder().dirtyIntArrayCaches[ArrayCache.getBucket(i2)];
    }

    int[] getDirtyIntArray(int i2) {
        if (i2 <= ArrayCache.MAX_ARRAY_SIZE) {
            return getDirtyIntArrayCache(i2).getArray();
        }
        if (doStats) {
            ArrayCache.incOversize();
        }
        if (doLogOverSize) {
            MarlinUtils.logInfo("getDirtyIntArray[oversize]: length=\t" + i2);
        }
        return new int[i2];
    }

    int[] widenDirtyIntArray(int[] iArr, int i2, int i3) {
        int length = iArr.length;
        if (doChecks && length >= i3) {
            return iArr;
        }
        if (doStats) {
            ArrayCache.incResizeDirtyInt();
        }
        int[] dirtyIntArray = getDirtyIntArray(ArrayCache.getNewSize(i2, i3));
        System.arraycopy(iArr, 0, dirtyIntArray, 0, i2);
        putDirtyIntArray(iArr);
        if (doLogWidenArray) {
            MarlinUtils.logInfo("widenDirtyIntArray[" + dirtyIntArray.length + "]: usedSize=\t" + i2 + "\tlength=\t" + length + "\tneeded length=\t" + i3);
        }
        return dirtyIntArray;
    }

    void putDirtyIntArray(int[] iArr) {
        int length = iArr.length;
        if ((length & 1) == 0 && length <= ArrayCache.MAX_ARRAY_SIZE) {
            getDirtyIntArrayCache(length).putDirtyArray(iArr, length);
        }
    }

    FloatArrayCache getDirtyFloatArrayCache(int i2) {
        return getArrayCachesHolder().dirtyFloatArrayCaches[ArrayCache.getBucket(i2)];
    }

    float[] getDirtyFloatArray(int i2) {
        if (i2 <= ArrayCache.MAX_ARRAY_SIZE) {
            return getDirtyFloatArrayCache(i2).getArray();
        }
        if (doStats) {
            ArrayCache.incOversize();
        }
        if (doLogOverSize) {
            MarlinUtils.logInfo("getDirtyFloatArray[oversize]: length=\t" + i2);
        }
        return new float[i2];
    }

    float[] widenDirtyFloatArray(float[] fArr, int i2, int i3) {
        int length = fArr.length;
        if (doChecks && length >= i3) {
            return fArr;
        }
        if (doStats) {
            ArrayCache.incResizeDirtyFloat();
        }
        float[] dirtyFloatArray = getDirtyFloatArray(ArrayCache.getNewSize(i2, i3));
        System.arraycopy(fArr, 0, dirtyFloatArray, 0, i2);
        putDirtyFloatArray(fArr);
        if (doLogWidenArray) {
            MarlinUtils.logInfo("widenDirtyFloatArray[" + dirtyFloatArray.length + "]: usedSize=\t" + i2 + "\tlength=\t" + length + "\tneeded length=\t" + i3);
        }
        return dirtyFloatArray;
    }

    void putDirtyFloatArray(float[] fArr) {
        int length = fArr.length;
        if ((length & 1) == 0 && length <= ArrayCache.MAX_ARRAY_SIZE) {
            getDirtyFloatArrayCache(length).putDirtyArray(fArr, length);
        }
    }

    /* loaded from: rt.jar:sun/java2d/marlin/RendererContext$ArrayCachesHolder.class */
    static final class ArrayCachesHolder {
        final IntArrayCache[] intArrayCaches = new IntArrayCache[4];
        final IntArrayCache[] dirtyIntArrayCaches = new IntArrayCache[4];
        final FloatArrayCache[] dirtyFloatArrayCaches = new FloatArrayCache[4];
        final ByteArrayCache[] dirtyByteArrayCaches = new ByteArrayCache[4];

        ArrayCachesHolder() {
            for (int i2 = 0; i2 < 4; i2++) {
                this.intArrayCaches[i2] = new IntArrayCache(ArrayCache.ARRAY_SIZES[i2]);
                this.dirtyIntArrayCaches[i2] = new IntArrayCache(ArrayCache.ARRAY_SIZES[i2]);
                this.dirtyFloatArrayCaches[i2] = new FloatArrayCache(ArrayCache.ARRAY_SIZES[i2]);
                this.dirtyByteArrayCaches[i2] = new ByteArrayCache(ArrayCache.DIRTY_BYTE_ARRAY_SIZES[i2]);
            }
        }
    }
}
