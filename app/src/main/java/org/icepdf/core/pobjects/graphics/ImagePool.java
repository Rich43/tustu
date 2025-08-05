package org.icepdf.core.pobjects.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ImagePool.class */
public class ImagePool {
    private final Map<Reference, BufferedImage> fCache;
    private static int defaultSize;
    private static final Logger log = Logger.getLogger(ImagePool.class.toString());
    private static boolean enabled = Defs.booleanProperty("org.icepdf.core.views.imagePoolEnabled", true);

    static {
        defaultSize = (int) (((Runtime.getRuntime().maxMemory() / 1024) / 1024) / 4);
        defaultSize = Defs.intProperty("org.icepdf.core.views.imagePoolSize", defaultSize);
    }

    public ImagePool() {
        this(defaultSize * 1024 * 1024);
    }

    public ImagePool(long maxCacheSize) {
        this.fCache = Collections.synchronizedMap(new MemoryImageCache(maxCacheSize));
    }

    public void put(Reference ref, BufferedImage image) {
        if (enabled) {
            this.fCache.put(new Reference(ref.getObjectNumber(), ref.getGenerationNumber()), image);
        }
    }

    public BufferedImage get(Reference ref) {
        if (enabled) {
            return this.fCache.get(ref);
        }
        return null;
    }

    public boolean containsKey(Reference ref) {
        if (enabled) {
            return this.fCache.containsKey(ref);
        }
        return false;
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ImagePool$MemoryImageCache.class */
    private static class MemoryImageCache extends LinkedHashMap<Reference, BufferedImage> {
        private final long maxCacheSize;
        private long currentCacheSize;

        public MemoryImageCache(long maxCacheSize) {
            super(16, 0.75f, true);
            this.maxCacheSize = maxCacheSize;
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public BufferedImage put(Reference key, BufferedImage value) {
            if (containsKey(key)) {
                BufferedImage removed = remove(key);
                this.currentCacheSize = (this.currentCacheSize - sizeOf(removed)) + sizeOf(value);
                super.put((MemoryImageCache) key, (Reference) value);
                return removed;
            }
            this.currentCacheSize += sizeOf(value);
            return (BufferedImage) super.put((MemoryImageCache) key, (Reference) value);
        }

        private long sizeOf(BufferedImage image) {
            int dataTypeSize;
            if (image == null) {
                return 0L;
            }
            DataBuffer dataBuffer = image.getRaster().getDataBuffer();
            switch (dataBuffer.getDataType()) {
                case 0:
                    dataTypeSize = 1;
                    break;
                case 1:
                case 2:
                    dataTypeSize = 2;
                    break;
                case 3:
                case 4:
                    dataTypeSize = 4;
                    break;
                case 5:
                case 32:
                default:
                    dataTypeSize = 8;
                    break;
            }
            return dataBuffer.getSize() * dataTypeSize;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<Reference, BufferedImage> eldest) {
            boolean remove = this.currentCacheSize > this.maxCacheSize;
            if (remove) {
                long size = sizeOf(eldest.getValue());
                this.currentCacheSize -= size;
            }
            return remove;
        }
    }
}
