package sun.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:sun/swing/CachedPainter.class */
public abstract class CachedPainter {
    private static final Map<Object, ImageCache> cacheMap = new HashMap();

    protected abstract void paintToImage(Component component, Image image, Graphics graphics, int i2, int i3, Object[] objArr);

    private static ImageCache getCache(Object obj) {
        ImageCache imageCache;
        synchronized (CachedPainter.class) {
            ImageCache imageCache2 = cacheMap.get(obj);
            if (imageCache2 == null) {
                imageCache2 = new ImageCache(1);
                cacheMap.put(obj, imageCache2);
            }
            imageCache = imageCache2;
        }
        return imageCache;
    }

    public CachedPainter(int i2) {
        getCache(getClass()).setMaxCount(i2);
    }

    public void paint(Component component, Graphics graphics, int i2, int i3, int i4, int i5, Object... objArr) {
        if (i4 <= 0 || i5 <= 0) {
            return;
        }
        synchronized (CachedPainter.class) {
            paint0(component, graphics, i2, i3, i4, i5, objArr);
        }
    }

    private void paint0(Component component, Graphics graphics, int i2, int i3, int i4, int i5, Object... objArr) {
        Class<?> cls = getClass();
        GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration(component);
        ImageCache cache = getCache(cls);
        Image image = cache.getImage(cls, graphicsConfiguration, i4, i5, objArr);
        int i6 = 0;
        do {
            boolean z2 = false;
            if (image instanceof VolatileImage) {
                switch (((VolatileImage) image).validate(graphicsConfiguration)) {
                    case 1:
                        z2 = true;
                        break;
                    case 2:
                        ((VolatileImage) image).flush();
                        image = null;
                        break;
                }
            }
            if (image == null) {
                image = createImage(component, i4, i5, graphicsConfiguration, objArr);
                cache.setImage(cls, graphicsConfiguration, i4, i5, objArr, image);
                z2 = true;
            }
            if (z2) {
                Graphics graphics2 = image.getGraphics();
                paintToImage(component, image, graphics2, i4, i5, objArr);
                graphics2.dispose();
            }
            paintImage(component, graphics, i2, i3, i4, i5, image, objArr);
            if (!(image instanceof VolatileImage) || !((VolatileImage) image).contentsLost()) {
                return;
            } else {
                i6++;
            }
        } while (i6 < 3);
    }

    protected void paintImage(Component component, Graphics graphics, int i2, int i3, int i4, int i5, Image image, Object[] objArr) {
        graphics.drawImage(image, i2, i3, null);
    }

    protected Image createImage(Component component, int i2, int i3, GraphicsConfiguration graphicsConfiguration, Object[] objArr) {
        if (graphicsConfiguration == null) {
            return new BufferedImage(i2, i3, 1);
        }
        return graphicsConfiguration.createCompatibleVolatileImage(i2, i3);
    }

    protected void flush() {
        synchronized (CachedPainter.class) {
            getCache(getClass()).flush();
        }
    }

    private GraphicsConfiguration getGraphicsConfiguration(Component component) {
        if (component == null) {
            return null;
        }
        return component.getGraphicsConfiguration();
    }
}
