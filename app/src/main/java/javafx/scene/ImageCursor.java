package javafx.scene;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.MetadataParser;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoublePropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;

/* loaded from: jfxrt.jar:javafx/scene/ImageCursor.class */
public class ImageCursor extends Cursor {
    private ObjectPropertyImpl<Image> image;
    private DoublePropertyImpl hotspotX;
    private DoublePropertyImpl hotspotY;
    private CursorFrame currentCursorFrame;
    private ImageCursorFrame firstCursorFrame;
    private Map<Object, ImageCursorFrame> otherCursorFrames;
    private int activeCounter;
    private InvalidationListener imageListener;

    public final Image getImage() {
        if (this.image == null) {
            return null;
        }
        return this.image.get();
    }

    public final ReadOnlyObjectProperty<Image> imageProperty() {
        return imagePropertyImpl();
    }

    private ObjectPropertyImpl<Image> imagePropertyImpl() {
        if (this.image == null) {
            this.image = new ObjectPropertyImpl<>(MetadataParser.IMAGE_TAG_NAME);
        }
        return this.image;
    }

    public final double getHotspotX() {
        if (this.hotspotX == null) {
            return 0.0d;
        }
        return this.hotspotX.get();
    }

    public final ReadOnlyDoubleProperty hotspotXProperty() {
        return hotspotXPropertyImpl();
    }

    private DoublePropertyImpl hotspotXPropertyImpl() {
        if (this.hotspotX == null) {
            this.hotspotX = new DoublePropertyImpl("hotspotX");
        }
        return this.hotspotX;
    }

    public final double getHotspotY() {
        if (this.hotspotY == null) {
            return 0.0d;
        }
        return this.hotspotY.get();
    }

    public final ReadOnlyDoubleProperty hotspotYProperty() {
        return hotspotYPropertyImpl();
    }

    private DoublePropertyImpl hotspotYPropertyImpl() {
        if (this.hotspotY == null) {
            this.hotspotY = new DoublePropertyImpl("hotspotY");
        }
        return this.hotspotY;
    }

    public ImageCursor() {
    }

    public ImageCursor(@NamedArg(MetadataParser.IMAGE_TAG_NAME) Image image) {
        this(image, 0.0d, 0.0d);
    }

    public ImageCursor(@NamedArg(MetadataParser.IMAGE_TAG_NAME) Image image, @NamedArg("hotspotX") double hotspotX, @NamedArg("hotspotY") double hotspotY) {
        if (image != null && image.getProgress() < 1.0d) {
            DelayedInitialization.applyTo(this, image, hotspotX, hotspotY);
        } else {
            initialize(image, hotspotX, hotspotY);
        }
    }

    public static Dimension2D getBestSize(double preferredWidth, double preferredHeight) {
        return Toolkit.getToolkit().getBestCursorSize((int) preferredWidth, (int) preferredHeight);
    }

    public static int getMaximumColors() {
        return Toolkit.getToolkit().getMaximumCursorColors();
    }

    public static ImageCursor chooseBestCursor(Image[] images, double hotspotX, double hotspotY) {
        ImageCursor imageCursor = new ImageCursor();
        if (needsDelayedInitialization(images)) {
            DelayedInitialization.applyTo(imageCursor, images, hotspotX, hotspotY);
        } else {
            imageCursor.initialize(images, hotspotX, hotspotY);
        }
        return imageCursor;
    }

    @Override // javafx.scene.Cursor
    CursorFrame getCurrentFrame() {
        if (this.currentCursorFrame != null) {
            return this.currentCursorFrame;
        }
        Image cursorImage = getImage();
        if (cursorImage == null) {
            this.currentCursorFrame = Cursor.DEFAULT.getCurrentFrame();
            return this.currentCursorFrame;
        }
        Object cursorPlatformImage = cursorImage.impl_getPlatformImage();
        if (cursorPlatformImage == null) {
            this.currentCursorFrame = Cursor.DEFAULT.getCurrentFrame();
            return this.currentCursorFrame;
        }
        if (this.firstCursorFrame == null) {
            this.firstCursorFrame = new ImageCursorFrame(cursorPlatformImage, cursorImage.getWidth(), cursorImage.getHeight(), getHotspotX(), getHotspotY());
            this.currentCursorFrame = this.firstCursorFrame;
        } else if (this.firstCursorFrame.getPlatformImage() == cursorPlatformImage) {
            this.currentCursorFrame = this.firstCursorFrame;
        } else {
            if (this.otherCursorFrames == null) {
                this.otherCursorFrames = new HashMap();
            }
            this.currentCursorFrame = this.otherCursorFrames.get(cursorPlatformImage);
            if (this.currentCursorFrame == null) {
                ImageCursorFrame newCursorFrame = new ImageCursorFrame(cursorPlatformImage, cursorImage.getWidth(), cursorImage.getHeight(), getHotspotX(), getHotspotY());
                this.otherCursorFrames.put(cursorPlatformImage, newCursorFrame);
                this.currentCursorFrame = newCursorFrame;
            }
        }
        return this.currentCursorFrame;
    }

    private void invalidateCurrentFrame() {
        this.currentCursorFrame = null;
    }

    @Override // javafx.scene.Cursor
    void activate() {
        int i2 = this.activeCounter + 1;
        this.activeCounter = i2;
        if (i2 == 1) {
            bindImage(getImage());
            invalidateCurrentFrame();
        }
    }

    @Override // javafx.scene.Cursor
    void deactivate() {
        int i2 = this.activeCounter - 1;
        this.activeCounter = i2;
        if (i2 == 0) {
            unbindImage(getImage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initialize(Image[] images, double hotspotX, double hotspotY) {
        Dimension2D dim = getBestSize(1.0d, 1.0d);
        if (images.length == 0 || dim.getWidth() == 0.0d || dim.getHeight() == 0.0d) {
            return;
        }
        if (images.length == 1) {
            initialize(images[0], hotspotX, hotspotY);
            return;
        }
        Image bestImage = findBestImage(images);
        double scaleX = bestImage.getWidth() / images[0].getWidth();
        double scaleY = bestImage.getHeight() / images[0].getHeight();
        initialize(bestImage, hotspotX * scaleX, hotspotY * scaleY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initialize(Image newImage, double newHotspotX, double newHotspotY) {
        Image oldImage = getImage();
        double oldHotspotX = getHotspotX();
        double oldHotspotY = getHotspotY();
        if (newImage == null || newImage.getWidth() < 1.0d || newImage.getHeight() < 1.0d) {
            newHotspotX = 0.0d;
            newHotspotY = 0.0d;
        } else {
            if (newHotspotX < 0.0d) {
                newHotspotX = 0.0d;
            }
            if (newHotspotX > newImage.getWidth() - 1.0d) {
                newHotspotX = newImage.getWidth() - 1.0d;
            }
            if (newHotspotY < 0.0d) {
                newHotspotY = 0.0d;
            }
            if (newHotspotY > newImage.getHeight() - 1.0d) {
                newHotspotY = newImage.getHeight() - 1.0d;
            }
        }
        imagePropertyImpl().store(newImage);
        hotspotXPropertyImpl().store(newHotspotX);
        hotspotYPropertyImpl().store(newHotspotY);
        if (oldImage != newImage) {
            if (this.activeCounter > 0) {
                unbindImage(oldImage);
                bindImage(newImage);
            }
            invalidateCurrentFrame();
            this.image.fireValueChangedEvent();
        }
        if (oldHotspotX != newHotspotX) {
            this.hotspotX.fireValueChangedEvent();
        }
        if (oldHotspotY != newHotspotY) {
            this.hotspotY.fireValueChangedEvent();
        }
    }

    private InvalidationListener getImageListener() {
        if (this.imageListener == null) {
            this.imageListener = valueModel -> {
                invalidateCurrentFrame();
            };
        }
        return this.imageListener;
    }

    private void bindImage(Image toImage) {
        if (toImage == null) {
            return;
        }
        Toolkit.getImageAccessor().getImageProperty(toImage).addListener(getImageListener());
    }

    private void unbindImage(Image fromImage) {
        if (fromImage == null) {
            return;
        }
        Toolkit.getImageAccessor().getImageProperty(fromImage).removeListener(getImageListener());
    }

    private static boolean needsDelayedInitialization(Image[] images) {
        for (Image image : images) {
            if (image.getProgress() < 1.0d) {
                return true;
            }
        }
        return false;
    }

    private static Image findBestImage(Image[] images) {
        for (Image image : images) {
            Dimension2D dim = getBestSize((int) image.getWidth(), (int) image.getHeight());
            if (dim.getWidth() == image.getWidth() && dim.getHeight() == image.getHeight()) {
                return image;
            }
        }
        Image bestImage = null;
        double bestRatio = Double.MAX_VALUE;
        for (Image image2 : images) {
            if (image2.getWidth() > 0.0d && image2.getHeight() > 0.0d) {
                Dimension2D dim2 = getBestSize(image2.getWidth(), image2.getHeight());
                double ratioX = dim2.getWidth() / image2.getWidth();
                double ratioY = dim2.getHeight() / image2.getHeight();
                if (ratioX >= 1.0d && ratioY >= 1.0d) {
                    double ratio = Math.max(ratioX, ratioY);
                    if (ratio < bestRatio) {
                        bestImage = image2;
                        bestRatio = ratio;
                    }
                }
            }
        }
        if (bestImage != null) {
            return bestImage;
        }
        for (Image image3 : images) {
            if (image3.getWidth() > 0.0d && image3.getHeight() > 0.0d) {
                Dimension2D dim3 = getBestSize(image3.getWidth(), image3.getHeight());
                if (dim3.getWidth() > 0.0d && dim3.getHeight() > 0.0d) {
                    double ratioX2 = dim3.getWidth() / image3.getWidth();
                    if (ratioX2 < 1.0d) {
                        ratioX2 = 1.0d / ratioX2;
                    }
                    double ratioY2 = dim3.getHeight() / image3.getHeight();
                    if (ratioY2 < 1.0d) {
                        ratioY2 = 1.0d / ratioY2;
                    }
                    double ratio2 = Math.max(ratioX2, ratioY2);
                    if (ratio2 < bestRatio) {
                        bestImage = image3;
                        bestRatio = ratio2;
                    }
                }
            }
        }
        if (bestImage != null) {
            return bestImage;
        }
        return images[0];
    }

    /* loaded from: jfxrt.jar:javafx/scene/ImageCursor$DoublePropertyImpl.class */
    private final class DoublePropertyImpl extends ReadOnlyDoublePropertyBase {
        private final String name;
        private double value;

        public DoublePropertyImpl(String name) {
            this.name = name;
        }

        public void store(double value) {
            this.value = value;
        }

        @Override // javafx.beans.property.ReadOnlyDoublePropertyBase, javafx.beans.property.adapter.ReadOnlyJavaBeanProperty
        public void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }

        @Override // javafx.beans.value.ObservableDoubleValue
        public double get() {
            return this.value;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ImageCursor.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return this.name;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/ImageCursor$ObjectPropertyImpl.class */
    private final class ObjectPropertyImpl<T> extends ReadOnlyObjectPropertyBase<T> {
        private final String name;
        private T value;

        public ObjectPropertyImpl(String name) {
            this.name = name;
        }

        public void store(T value) {
            this.value = value;
        }

        @Override // javafx.beans.property.ReadOnlyObjectPropertyBase
        public void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public T get() {
            return this.value;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ImageCursor.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return this.name;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/ImageCursor$DelayedInitialization.class */
    private static final class DelayedInitialization implements InvalidationListener {
        private final ImageCursor targetCursor;
        private final Image[] images;
        private final double hotspotX;
        private final double hotspotY;
        private final boolean initAsSingle;
        private int waitForImages;

        private DelayedInitialization(ImageCursor targetCursor, Image[] images, double hotspotX, double hotspotY, boolean initAsSingle) {
            this.targetCursor = targetCursor;
            this.images = images;
            this.hotspotX = hotspotX;
            this.hotspotY = hotspotY;
            this.initAsSingle = initAsSingle;
        }

        public static void applyTo(ImageCursor imageCursor, Image[] images, double hotspotX, double hotspotY) {
            DelayedInitialization delayedInitialization = new DelayedInitialization(imageCursor, (Image[]) Arrays.copyOf(images, images.length), hotspotX, hotspotY, false);
            delayedInitialization.start();
        }

        public static void applyTo(ImageCursor imageCursor, Image image, double hotspotX, double hotspotY) {
            DelayedInitialization delayedInitialization = new DelayedInitialization(imageCursor, new Image[]{image}, hotspotX, hotspotY, true);
            delayedInitialization.start();
        }

        private void start() {
            for (Image image : this.images) {
                if (image.getProgress() < 1.0d) {
                    this.waitForImages++;
                    image.progressProperty().addListener(this);
                }
            }
        }

        private void cleanupAndFinishInitialization() {
            for (Image image : this.images) {
                image.progressProperty().removeListener(this);
            }
            if (this.initAsSingle) {
                this.targetCursor.initialize(this.images[0], this.hotspotX, this.hotspotY);
            } else {
                this.targetCursor.initialize(this.images, this.hotspotX, this.hotspotY);
            }
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            if (((ReadOnlyDoubleProperty) valueModel).get() == 1.0d) {
                int i2 = this.waitForImages - 1;
                this.waitForImages = i2;
                if (i2 == 0) {
                    cleanupAndFinishInitialization();
                }
            }
        }
    }
}
