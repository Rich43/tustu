package sun.awt.image;

import java.awt.Image;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import sun.awt.AppContext;

/* loaded from: rt.jar:sun/awt/image/ImageCache.class */
public final class ImageCache {
    private final LinkedHashMap<PixelsKey, ImageSoftReference> map;
    private final int maxPixelCount;
    private int currentPixelCount;
    private final ReadWriteLock lock;
    private final ReferenceQueue<Image> referenceQueue;

    /* loaded from: rt.jar:sun/awt/image/ImageCache$PixelsKey.class */
    public interface PixelsKey {
        int getPixelCount();
    }

    public static ImageCache getInstance() {
        return (ImageCache) AppContext.getSoftReferenceValue(ImageCache.class, () -> {
            return new ImageCache();
        });
    }

    ImageCache(int i2) {
        this.map = new LinkedHashMap<>(16, 0.75f, true);
        this.currentPixelCount = 0;
        this.lock = new ReentrantReadWriteLock();
        this.referenceQueue = new ReferenceQueue<>();
        this.maxPixelCount = i2;
    }

    ImageCache() {
        this(2097152);
    }

    public void flush() {
        this.lock.writeLock().lock();
        try {
            this.map.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public Image getImage(PixelsKey pixelsKey) {
        this.lock.readLock().lock();
        try {
            ImageSoftReference imageSoftReference = this.map.get(pixelsKey);
            if (imageSoftReference == null) {
                return null;
            }
            return imageSoftReference.get();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void setImage(PixelsKey pixelsKey, Image image) {
        this.lock.writeLock().lock();
        try {
            ImageSoftReference imageSoftReference = this.map.get(pixelsKey);
            if (imageSoftReference != null) {
                if (imageSoftReference.get() != null) {
                    return;
                }
                this.currentPixelCount -= pixelsKey.getPixelCount();
                this.map.remove(pixelsKey);
            }
            this.currentPixelCount += pixelsKey.getPixelCount();
            if (this.currentPixelCount > this.maxPixelCount) {
                while (true) {
                    ImageSoftReference imageSoftReference2 = (ImageSoftReference) this.referenceQueue.poll();
                    if (imageSoftReference2 == null) {
                        break;
                    }
                    this.map.remove(imageSoftReference2.key);
                    this.currentPixelCount -= imageSoftReference2.key.getPixelCount();
                }
            }
            if (this.currentPixelCount > this.maxPixelCount) {
                Iterator<Map.Entry<PixelsKey, ImageSoftReference>> it = this.map.entrySet().iterator();
                while (this.currentPixelCount > this.maxPixelCount && it.hasNext()) {
                    Map.Entry<PixelsKey, ImageSoftReference> next = it.next();
                    it.remove();
                    Image image2 = next.getValue().get();
                    if (image2 != null) {
                        image2.flush();
                    }
                    this.currentPixelCount -= next.getValue().key.getPixelCount();
                }
            }
            this.map.put(pixelsKey, new ImageSoftReference(pixelsKey, image, this.referenceQueue));
            this.lock.writeLock().unlock();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /* loaded from: rt.jar:sun/awt/image/ImageCache$ImageSoftReference.class */
    private static class ImageSoftReference extends SoftReference<Image> {
        final PixelsKey key;

        ImageSoftReference(PixelsKey pixelsKey, Image image, ReferenceQueue<? super Image> referenceQueue) {
            super(image, referenceQueue);
            this.key = pixelsKey;
        }
    }
}
