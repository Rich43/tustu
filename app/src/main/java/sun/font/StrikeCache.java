package sun.font;

import java.awt.GraphicsEnvironment;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import sun.java2d.Disposer;
import sun.java2d.pipe.BufferedContext;
import sun.java2d.pipe.RenderQueue;
import sun.java2d.pipe.hw.AccelGraphicsConfig;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/font/StrikeCache.class */
public final class StrikeCache {
    static final Unsafe unsafe = Unsafe.getUnsafe();
    static ReferenceQueue refQueue = Disposer.getQueue();
    static ArrayList<GlyphDisposedListener> disposeListeners = new ArrayList<>(1);
    static int MINSTRIKES = 8;
    static int recentStrikeIndex = 0;
    static FontStrike[] recentStrikes;
    static boolean cacheRefTypeWeak;
    static int nativeAddressSize;
    static int glyphInfoSize;
    static int xAdvanceOffset;
    static int yAdvanceOffset;
    static int boundsOffset;
    static int widthOffset;
    static int heightOffset;
    static int rowBytesOffset;
    static int topLeftXOffset;
    static int topLeftYOffset;
    static int pixelDataOffset;
    static int cacheCellOffset;
    static int managedOffset;
    static long invisibleGlyphPtr;

    /* loaded from: rt.jar:sun/font/StrikeCache$DisposableStrike.class */
    interface DisposableStrike {
        FontStrikeDisposer getDisposer();
    }

    static native void getGlyphCacheDescription(long[] jArr);

    static native void freeIntPointer(int i2);

    static native void freeLongPointer(long j2);

    private static native void freeIntMemory(int[] iArr, long j2);

    private static native void freeLongMemory(long[] jArr, long j2);

    static {
        long[] jArr = new long[13];
        getGlyphCacheDescription(jArr);
        nativeAddressSize = (int) jArr[0];
        glyphInfoSize = (int) jArr[1];
        xAdvanceOffset = (int) jArr[2];
        yAdvanceOffset = (int) jArr[3];
        widthOffset = (int) jArr[4];
        heightOffset = (int) jArr[5];
        rowBytesOffset = (int) jArr[6];
        topLeftXOffset = (int) jArr[7];
        topLeftYOffset = (int) jArr[8];
        pixelDataOffset = (int) jArr[9];
        invisibleGlyphPtr = jArr[10];
        cacheCellOffset = (int) jArr[11];
        managedOffset = (int) jArr[12];
        if (nativeAddressSize < 4) {
            throw new InternalError("Unexpected address size for font data: " + nativeAddressSize);
        }
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.StrikeCache.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                StrikeCache.cacheRefTypeWeak = System.getProperty("sun.java2d.font.reftype", "soft").equals("weak");
                String property = System.getProperty("sun.java2d.font.minstrikes");
                if (property != null) {
                    try {
                        StrikeCache.MINSTRIKES = Integer.parseInt(property);
                        if (StrikeCache.MINSTRIKES <= 0) {
                            StrikeCache.MINSTRIKES = 1;
                        }
                    } catch (NumberFormatException e2) {
                    }
                }
                StrikeCache.recentStrikes = new FontStrike[StrikeCache.MINSTRIKES];
                return null;
            }
        });
    }

    static void refStrike(FontStrike fontStrike) {
        int i2 = recentStrikeIndex;
        recentStrikes[i2] = fontStrike;
        int i3 = i2 + 1;
        if (i3 == MINSTRIKES) {
            i3 = 0;
        }
        recentStrikeIndex = i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void doDispose(FontStrikeDisposer fontStrikeDisposer) {
        if (fontStrikeDisposer.intGlyphImages != null) {
            freeCachedIntMemory(fontStrikeDisposer.intGlyphImages, fontStrikeDisposer.pScalerContext);
            return;
        }
        if (fontStrikeDisposer.longGlyphImages != null) {
            freeCachedLongMemory(fontStrikeDisposer.longGlyphImages, fontStrikeDisposer.pScalerContext);
            return;
        }
        if (fontStrikeDisposer.segIntGlyphImages != null) {
            for (int i2 = 0; i2 < fontStrikeDisposer.segIntGlyphImages.length; i2++) {
                if (fontStrikeDisposer.segIntGlyphImages[i2] != null) {
                    freeCachedIntMemory(fontStrikeDisposer.segIntGlyphImages[i2], fontStrikeDisposer.pScalerContext);
                    fontStrikeDisposer.pScalerContext = 0L;
                    fontStrikeDisposer.segIntGlyphImages[i2] = null;
                }
            }
            if (fontStrikeDisposer.pScalerContext != 0) {
                freeCachedIntMemory(new int[0], fontStrikeDisposer.pScalerContext);
                return;
            }
            return;
        }
        if (fontStrikeDisposer.segLongGlyphImages == null) {
            if (fontStrikeDisposer.pScalerContext != 0) {
                if (longAddresses()) {
                    freeCachedLongMemory(new long[0], fontStrikeDisposer.pScalerContext);
                    return;
                } else {
                    freeCachedIntMemory(new int[0], fontStrikeDisposer.pScalerContext);
                    return;
                }
            }
            return;
        }
        for (int i3 = 0; i3 < fontStrikeDisposer.segLongGlyphImages.length; i3++) {
            if (fontStrikeDisposer.segLongGlyphImages[i3] != null) {
                freeCachedLongMemory(fontStrikeDisposer.segLongGlyphImages[i3], fontStrikeDisposer.pScalerContext);
                fontStrikeDisposer.pScalerContext = 0L;
                fontStrikeDisposer.segLongGlyphImages[i3] = null;
            }
        }
        if (fontStrikeDisposer.pScalerContext != 0) {
            freeCachedLongMemory(new long[0], fontStrikeDisposer.pScalerContext);
        }
    }

    private static boolean longAddresses() {
        return nativeAddressSize == 8;
    }

    static void disposeStrike(final FontStrikeDisposer fontStrikeDisposer) {
        BufferedContext context;
        if (Disposer.pollingQueue) {
            doDispose(fontStrikeDisposer);
            return;
        }
        RenderQueue renderQueue = null;
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (!GraphicsEnvironment.isHeadless()) {
            Object defaultConfiguration = localGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
            if ((defaultConfiguration instanceof AccelGraphicsConfig) && (context = ((AccelGraphicsConfig) defaultConfiguration).getContext()) != null) {
                renderQueue = context.getRenderQueue();
            }
        }
        if (renderQueue != null) {
            renderQueue.lock();
            try {
                renderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.font.StrikeCache.2
                    @Override // java.lang.Runnable
                    public void run() {
                        StrikeCache.doDispose(fontStrikeDisposer);
                        Disposer.pollRemove();
                    }
                });
                renderQueue.unlock();
                return;
            } catch (Throwable th) {
                renderQueue.unlock();
                throw th;
            }
        }
        doDispose(fontStrikeDisposer);
    }

    private static void freeCachedIntMemory(int[] iArr, long j2) {
        synchronized (disposeListeners) {
            if (disposeListeners.size() > 0) {
                ArrayList arrayList = null;
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    if (iArr[i2] != 0 && unsafe.getByte(iArr[i2] + managedOffset) == 0) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(Long.valueOf(iArr[i2]));
                    }
                }
                if (arrayList != null) {
                    notifyDisposeListeners(arrayList);
                }
            }
        }
        freeIntMemory(iArr, j2);
    }

    private static void freeCachedLongMemory(long[] jArr, long j2) {
        synchronized (disposeListeners) {
            if (disposeListeners.size() > 0) {
                ArrayList arrayList = null;
                for (int i2 = 0; i2 < jArr.length; i2++) {
                    if (jArr[i2] != 0 && unsafe.getByte(jArr[i2] + managedOffset) == 0) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(Long.valueOf(jArr[i2]));
                    }
                }
                if (arrayList != null) {
                    notifyDisposeListeners(arrayList);
                }
            }
        }
        freeLongMemory(jArr, j2);
    }

    public static void addGlyphDisposedListener(GlyphDisposedListener glyphDisposedListener) {
        synchronized (disposeListeners) {
            disposeListeners.add(glyphDisposedListener);
        }
    }

    private static void notifyDisposeListeners(ArrayList<Long> arrayList) {
        Iterator<GlyphDisposedListener> it = disposeListeners.iterator();
        while (it.hasNext()) {
            it.next().glyphDisposed(arrayList);
        }
    }

    public static Reference getStrikeRef(FontStrike fontStrike) {
        return getStrikeRef(fontStrike, cacheRefTypeWeak);
    }

    public static Reference getStrikeRef(FontStrike fontStrike, boolean z2) {
        if (fontStrike.disposer == null) {
            if (z2) {
                return new WeakReference(fontStrike);
            }
            return new SoftReference(fontStrike);
        }
        if (z2) {
            return new WeakDisposerRef(fontStrike);
        }
        return new SoftDisposerRef(fontStrike);
    }

    /* loaded from: rt.jar:sun/font/StrikeCache$SoftDisposerRef.class */
    static class SoftDisposerRef extends SoftReference implements DisposableStrike {
        private FontStrikeDisposer disposer;

        @Override // sun.font.StrikeCache.DisposableStrike
        public FontStrikeDisposer getDisposer() {
            return this.disposer;
        }

        SoftDisposerRef(FontStrike fontStrike) {
            super(fontStrike, StrikeCache.refQueue);
            this.disposer = fontStrike.disposer;
            Disposer.addReference(this, this.disposer);
        }
    }

    /* loaded from: rt.jar:sun/font/StrikeCache$WeakDisposerRef.class */
    static class WeakDisposerRef extends WeakReference implements DisposableStrike {
        private FontStrikeDisposer disposer;

        @Override // sun.font.StrikeCache.DisposableStrike
        public FontStrikeDisposer getDisposer() {
            return this.disposer;
        }

        WeakDisposerRef(FontStrike fontStrike) {
            super(fontStrike, StrikeCache.refQueue);
            this.disposer = fontStrike.disposer;
            Disposer.addReference(this, this.disposer);
        }
    }
}
