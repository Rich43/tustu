package java.awt;

/* loaded from: rt.jar:java/awt/BufferCapabilities.class */
public class BufferCapabilities implements Cloneable {
    private ImageCapabilities frontCaps;
    private ImageCapabilities backCaps;
    private FlipContents flipContents;

    public BufferCapabilities(ImageCapabilities imageCapabilities, ImageCapabilities imageCapabilities2, FlipContents flipContents) {
        if (imageCapabilities == null || imageCapabilities2 == null) {
            throw new IllegalArgumentException("Image capabilities specified cannot be null");
        }
        this.frontCaps = imageCapabilities;
        this.backCaps = imageCapabilities2;
        this.flipContents = flipContents;
    }

    public ImageCapabilities getFrontBufferCapabilities() {
        return this.frontCaps;
    }

    public ImageCapabilities getBackBufferCapabilities() {
        return this.backCaps;
    }

    public boolean isPageFlipping() {
        return getFlipContents() != null;
    }

    public FlipContents getFlipContents() {
        return this.flipContents;
    }

    public boolean isFullScreenRequired() {
        return false;
    }

    public boolean isMultiBufferAvailable() {
        return false;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    /* loaded from: rt.jar:java/awt/BufferCapabilities$FlipContents.class */
    public static final class FlipContents extends AttributeValue {
        private static int I_UNDEFINED = 0;
        private static int I_BACKGROUND = 1;
        private static int I_PRIOR = 2;
        private static int I_COPIED = 3;
        private static final String[] NAMES = {"undefined", "background", "prior", "copied"};
        public static final FlipContents UNDEFINED = new FlipContents(I_UNDEFINED);
        public static final FlipContents BACKGROUND = new FlipContents(I_BACKGROUND);
        public static final FlipContents PRIOR = new FlipContents(I_PRIOR);
        public static final FlipContents COPIED = new FlipContents(I_COPIED);

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        @Override // java.awt.AttributeValue
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        private FlipContents(int i2) {
            super(i2, NAMES);
        }
    }
}
