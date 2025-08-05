package java.awt;

import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.ReplicateScaleFilter;
import sun.awt.image.SurfaceManager;

/* loaded from: rt.jar:java/awt/Image.class */
public abstract class Image {
    protected float accelerationPriority = 0.5f;
    public static final int SCALE_DEFAULT = 1;
    public static final int SCALE_FAST = 2;
    public static final int SCALE_SMOOTH = 4;
    public static final int SCALE_REPLICATE = 8;
    public static final int SCALE_AREA_AVERAGING = 16;
    SurfaceManager surfaceManager;
    private static ImageCapabilities defaultImageCaps = new ImageCapabilities(false);
    public static final Object UndefinedProperty = new Object();

    public abstract int getWidth(ImageObserver imageObserver);

    public abstract int getHeight(ImageObserver imageObserver);

    public abstract ImageProducer getSource();

    public abstract Graphics getGraphics();

    public abstract Object getProperty(String str, ImageObserver imageObserver);

    static {
        SurfaceManager.setImageAccessor(new SurfaceManager.ImageAccessor() { // from class: java.awt.Image.1
            @Override // sun.awt.image.SurfaceManager.ImageAccessor
            public SurfaceManager getSurfaceManager(Image image) {
                return image.surfaceManager;
            }

            @Override // sun.awt.image.SurfaceManager.ImageAccessor
            public void setSurfaceManager(Image image, SurfaceManager surfaceManager) {
                image.surfaceManager = surfaceManager;
            }
        });
    }

    public Image getScaledInstance(int i2, int i3, int i4) {
        ReplicateScaleFilter replicateScaleFilter;
        if ((i4 & 20) != 0) {
            replicateScaleFilter = new AreaAveragingScaleFilter(i2, i3);
        } else {
            replicateScaleFilter = new ReplicateScaleFilter(i2, i3);
        }
        return Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(getSource(), replicateScaleFilter));
    }

    public void flush() {
        if (this.surfaceManager != null) {
            this.surfaceManager.flush();
        }
    }

    public ImageCapabilities getCapabilities(GraphicsConfiguration graphicsConfiguration) {
        if (this.surfaceManager != null) {
            return this.surfaceManager.getCapabilities(graphicsConfiguration);
        }
        return defaultImageCaps;
    }

    public void setAccelerationPriority(float f2) {
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Priority must be a value between 0 and 1, inclusive");
        }
        this.accelerationPriority = f2;
        if (this.surfaceManager != null) {
            this.surfaceManager.setAccelerationPriority(this.accelerationPriority);
        }
    }

    public float getAccelerationPriority() {
        return this.accelerationPriority;
    }
}
