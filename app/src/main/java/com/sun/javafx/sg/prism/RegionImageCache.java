package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.logging.PulseLogger;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.packrect.RectanglePacker;
import java.util.HashMap;
import javafx.scene.layout.Background;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/RegionImageCache.class */
class RegionImageCache {
    private static final int MAX_SIZE = 90000;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;
    private HashMap<Integer, CachedImage> imageMap = new HashMap<>();
    private RTTexture backingStore;
    private RectanglePacker hPacker;
    private RectanglePacker vPacker;

    RegionImageCache(ResourceFactory factory) {
        Texture.WrapMode mode;
        int pad;
        if (factory.isWrapModeSupported(Texture.WrapMode.CLAMP_TO_ZERO)) {
            mode = Texture.WrapMode.CLAMP_TO_ZERO;
            pad = 0;
        } else {
            mode = Texture.WrapMode.CLAMP_NOT_NEEDED;
            pad = 1;
        }
        this.backingStore = factory.createRTTexture(2048, 1024, mode);
        this.backingStore.contentsUseful();
        this.backingStore.makePermanent();
        factory.setRegionTexture(this.backingStore);
        this.hPacker = new RectanglePacker(this.backingStore, pad, pad, 1024 - pad, 1024 - pad, false);
        this.vPacker = new RectanglePacker(this.backingStore, 1024, pad, 1024, 1024 - pad, true);
    }

    boolean isImageCachable(int w2, int h2) {
        return 0 < w2 && w2 < 1024 && 0 < h2 && h2 < 1024 && w2 * h2 < MAX_SIZE;
    }

    RTTexture getBackingStore() {
        return this.backingStore;
    }

    boolean getImageLocation(Integer key, Rectangle rect, Background background, Shape shape, Graphics g2) {
        CachedImage cache = this.imageMap.get(key);
        if (cache != null) {
            if (cache.equals(rect.width, rect.height, background, shape)) {
                rect.f11913x = cache.f11959x;
                rect.f11914y = cache.f11960y;
                return false;
            }
            rect.height = -1;
            rect.width = -1;
            return false;
        }
        boolean vertical = rect.height > 64;
        RectanglePacker packer = vertical ? this.vPacker : this.hPacker;
        if (!packer.add(rect)) {
            g2.sync();
            this.vPacker.clear();
            this.hPacker.clear();
            this.imageMap.clear();
            packer.add(rect);
            this.backingStore.createGraphics().clear();
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.incrementCounter("Region image cache flushed");
            }
        }
        this.imageMap.put(key, new CachedImage(rect, background, shape));
        return true;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/RegionImageCache$CachedImage.class */
    static class CachedImage {
        Background background;
        Shape shape;

        /* renamed from: x, reason: collision with root package name */
        int f11959x;

        /* renamed from: y, reason: collision with root package name */
        int f11960y;
        int width;
        int height;

        CachedImage(Rectangle rect, Background background, Shape shape) {
            this.f11959x = rect.f11913x;
            this.f11960y = rect.f11914y;
            this.width = rect.width;
            this.height = rect.height;
            this.background = background;
            this.shape = shape;
        }

        public boolean equals(int width, int height, Background background, Shape shape) {
            return this.width == width && this.height == height && (this.background != null ? this.background.equals(background) : background == null) && (this.shape != null ? this.shape.equals(shape) : shape == null);
        }
    }
}
