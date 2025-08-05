package javax.swing.plaf.nimbus;

import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ImageCache.class */
class ImageCache {
    private final LinkedHashMap<Integer, PixelCountSoftReference> map;
    private final int maxPixelCount;
    private final int maxSingleImagePixelSize;
    private int currentPixelCount;
    private ReadWriteLock lock;
    private ReferenceQueue<Image> referenceQueue;
    private static final ImageCache instance = new ImageCache();

    static ImageCache getInstance() {
        return instance;
    }

    public ImageCache() {
        this.map = new LinkedHashMap<>(16, 0.75f, true);
        this.currentPixelCount = 0;
        this.lock = new ReentrantReadWriteLock();
        this.referenceQueue = new ReferenceQueue<>();
        this.maxPixelCount = 2097152;
        this.maxSingleImagePixelSize = 90000;
    }

    public ImageCache(int i2, int i3) {
        this.map = new LinkedHashMap<>(16, 0.75f, true);
        this.currentPixelCount = 0;
        this.lock = new ReentrantReadWriteLock();
        this.referenceQueue = new ReferenceQueue<>();
        this.maxPixelCount = i2;
        this.maxSingleImagePixelSize = i3;
    }

    public void flush() {
        this.lock.readLock().lock();
        try {
            this.map.clear();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public boolean isImageCachable(int i2, int i3) {
        return i2 * i3 < this.maxSingleImagePixelSize;
    }

    public Image getImage(GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object... objArr) {
        this.lock.readLock().lock();
        try {
            PixelCountSoftReference pixelCountSoftReference = this.map.get(Integer.valueOf(hash(graphicsConfiguration, i2, i3, objArr)));
            if (pixelCountSoftReference != null && pixelCountSoftReference.equals(graphicsConfiguration, i2, i3, objArr)) {
                Image image = pixelCountSoftReference.get();
                this.lock.readLock().unlock();
                return image;
            }
            return null;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public boolean setImage(Image image, GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object... objArr) {
        if (!isImageCachable(i2, i3)) {
            return false;
        }
        int iHash = hash(graphicsConfiguration, i2, i3, objArr);
        this.lock.writeLock().lock();
        try {
            PixelCountSoftReference pixelCountSoftReference = this.map.get(Integer.valueOf(iHash));
            if (pixelCountSoftReference != null && pixelCountSoftReference.get() == image) {
                return true;
            }
            if (pixelCountSoftReference != null) {
                this.currentPixelCount -= pixelCountSoftReference.pixelCount;
                this.map.remove(Integer.valueOf(iHash));
            }
            int width = image.getWidth(null) * image.getHeight(null);
            this.currentPixelCount += width;
            if (this.currentPixelCount > this.maxPixelCount) {
                while (true) {
                    PixelCountSoftReference pixelCountSoftReference2 = (PixelCountSoftReference) this.referenceQueue.poll();
                    if (pixelCountSoftReference2 == null) {
                        break;
                    }
                    this.map.remove(Integer.valueOf(pixelCountSoftReference2.hash));
                    this.currentPixelCount -= pixelCountSoftReference2.pixelCount;
                }
            }
            if (this.currentPixelCount > this.maxPixelCount) {
                Iterator<Map.Entry<Integer, PixelCountSoftReference>> it = this.map.entrySet().iterator();
                while (this.currentPixelCount > this.maxPixelCount && it.hasNext()) {
                    Map.Entry<Integer, PixelCountSoftReference> next = it.next();
                    it.remove();
                    Image image2 = next.getValue().get();
                    if (image2 != null) {
                        image2.flush();
                    }
                    this.currentPixelCount -= next.getValue().pixelCount;
                }
            }
            this.map.put(Integer.valueOf(iHash), new PixelCountSoftReference(image, this.referenceQueue, width, iHash, graphicsConfiguration, i2, i3, objArr));
            this.lock.writeLock().unlock();
            return true;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private int hash(GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object... objArr) {
        return (31 * ((31 * ((31 * (graphicsConfiguration != null ? graphicsConfiguration.hashCode() : 0)) + i2)) + i3)) + Arrays.deepHashCode(objArr);
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/ImageCache$PixelCountSoftReference.class */
    private static class PixelCountSoftReference extends SoftReference<Image> {
        private final int pixelCount;
        private final int hash;
        private final GraphicsConfiguration config;

        /* renamed from: w, reason: collision with root package name */
        private final int f12826w;

        /* renamed from: h, reason: collision with root package name */
        private final int f12827h;
        private final Object[] args;

        public PixelCountSoftReference(Image image, ReferenceQueue<? super Image> referenceQueue, int i2, int i3, GraphicsConfiguration graphicsConfiguration, int i4, int i5, Object[] objArr) {
            super(image, referenceQueue);
            this.pixelCount = i2;
            this.hash = i3;
            this.config = graphicsConfiguration;
            this.f12826w = i4;
            this.f12827h = i5;
            this.args = objArr;
        }

        public boolean equals(GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object[] objArr) {
            return graphicsConfiguration == this.config && i2 == this.f12826w && i3 == this.f12827h && Arrays.equals(objArr, this.args);
        }
    }
}
