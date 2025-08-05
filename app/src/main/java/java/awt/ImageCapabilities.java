package java.awt;

/* loaded from: rt.jar:java/awt/ImageCapabilities.class */
public class ImageCapabilities implements Cloneable {
    private boolean accelerated;

    public ImageCapabilities(boolean z2) {
        this.accelerated = false;
        this.accelerated = z2;
    }

    public boolean isAccelerated() {
        return this.accelerated;
    }

    public boolean isTrueVolatile() {
        return false;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
