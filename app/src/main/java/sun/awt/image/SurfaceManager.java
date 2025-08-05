package sun.awt.image;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import sun.java2d.SurfaceData;
import sun.java2d.SurfaceDataProxy;

/* loaded from: rt.jar:sun/awt/image/SurfaceManager.class */
public abstract class SurfaceManager {
    private static ImageAccessor imgaccessor;
    private ConcurrentHashMap<Object, Object> cacheMap;

    /* loaded from: rt.jar:sun/awt/image/SurfaceManager$FlushableCacheData.class */
    public interface FlushableCacheData {
        boolean flush(boolean z2);
    }

    /* loaded from: rt.jar:sun/awt/image/SurfaceManager$ImageAccessor.class */
    public static abstract class ImageAccessor {
        public abstract SurfaceManager getSurfaceManager(Image image);

        public abstract void setSurfaceManager(Image image, SurfaceManager surfaceManager);
    }

    /* loaded from: rt.jar:sun/awt/image/SurfaceManager$ProxiedGraphicsConfig.class */
    public interface ProxiedGraphicsConfig {
        Object getProxyKey();
    }

    public abstract SurfaceData getPrimarySurfaceData();

    public abstract SurfaceData restoreContents();

    public static void setImageAccessor(ImageAccessor imageAccessor) {
        if (imgaccessor != null) {
            throw new InternalError("Attempt to set ImageAccessor twice");
        }
        imgaccessor = imageAccessor;
    }

    public static SurfaceManager getManager(Image image) {
        SurfaceManager surfaceManager = imgaccessor.getSurfaceManager(image);
        if (surfaceManager == null) {
            try {
                BufferedImage bufferedImage = (BufferedImage) image;
                surfaceManager = new BufImgSurfaceManager(bufferedImage);
                setManager(bufferedImage, surfaceManager);
            } catch (ClassCastException e2) {
                throw new IllegalArgumentException("Invalid Image variant");
            }
        }
        return surfaceManager;
    }

    public static void setManager(Image image, SurfaceManager surfaceManager) {
        imgaccessor.setSurfaceManager(image, surfaceManager);
    }

    public Object getCacheData(Object obj) {
        if (this.cacheMap == null) {
            return null;
        }
        return this.cacheMap.get(obj);
    }

    public void setCacheData(Object obj, Object obj2) {
        if (this.cacheMap == null) {
            synchronized (this) {
                if (this.cacheMap == null) {
                    this.cacheMap = new ConcurrentHashMap<>(2);
                }
            }
        }
        this.cacheMap.put(obj, obj2);
    }

    public void acceleratedSurfaceLost() {
    }

    public ImageCapabilities getCapabilities(GraphicsConfiguration graphicsConfiguration) {
        return new ImageCapabilitiesGc(graphicsConfiguration);
    }

    /* loaded from: rt.jar:sun/awt/image/SurfaceManager$ImageCapabilitiesGc.class */
    class ImageCapabilitiesGc extends ImageCapabilities {
        GraphicsConfiguration gc;

        public ImageCapabilitiesGc(GraphicsConfiguration graphicsConfiguration) {
            super(false);
            this.gc = graphicsConfiguration;
        }

        @Override // java.awt.ImageCapabilities
        public boolean isAccelerated() {
            Object proxyKey;
            SurfaceDataProxy surfaceDataProxy;
            GraphicsConfiguration defaultConfiguration = this.gc;
            if (defaultConfiguration == null) {
                defaultConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            }
            return (defaultConfiguration instanceof ProxiedGraphicsConfig) && (proxyKey = ((ProxiedGraphicsConfig) defaultConfiguration).getProxyKey()) != null && (surfaceDataProxy = (SurfaceDataProxy) SurfaceManager.this.getCacheData(proxyKey)) != null && surfaceDataProxy.isAccelerated();
        }
    }

    public synchronized void flush() {
        flush(false);
    }

    synchronized void flush(boolean z2) {
        if (this.cacheMap != null) {
            Iterator<Object> it = this.cacheMap.values().iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if ((next instanceof FlushableCacheData) && ((FlushableCacheData) next).flush(z2)) {
                    it.remove();
                }
            }
        }
    }

    public void setAccelerationPriority(float f2) {
        if (f2 == 0.0f) {
            flush(true);
        }
    }

    public static int getImageScale(Image image) {
        if (!(image instanceof VolatileImage)) {
            return 1;
        }
        return getManager(image).getPrimarySurfaceData().getDefaultScale();
    }
}
