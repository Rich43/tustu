package javafx.scene.image;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.javafx.runtime.async.AsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.MetadataParser;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CancellationException;
import java.util.jar.Pack200;
import java.util.regex.Pattern;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoublePropertyBase;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.PixelFormat;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/scene/image/Image.class */
public class Image {
    private static final Pattern URL_QUICKMATCH;
    private final String url;
    private final InputStream impl_source;
    private ReadOnlyDoubleWrapper progress;
    private final double requestedWidth;
    private final double requestedHeight;
    private DoublePropertyImpl width;
    private DoublePropertyImpl height;
    private final boolean preserveRatio;
    private final boolean smooth;
    private final boolean backgroundLoading;
    private ReadOnlyBooleanWrapper error;
    private ReadOnlyObjectWrapper<Exception> exception;
    private ObjectPropertyImpl<PlatformImage> platformImage;
    private ImageTask backgroundTask;
    private Animation animation;
    private volatile boolean isAnimated;
    private PlatformImage[] animFrames;
    private static final int MAX_RUNNING_TASKS = 4;
    private static int runningTasks;
    private static final Queue<ImageTask> pendingTasks;
    private PixelReader reader;

    static {
        Toolkit.setImageAccessor(new Toolkit.ImageAccessor() { // from class: javafx.scene.image.Image.1
            @Override // com.sun.javafx.tk.Toolkit.ImageAccessor
            public boolean isAnimation(Image image) {
                return image.isAnimation();
            }

            @Override // com.sun.javafx.tk.Toolkit.ImageAccessor
            public ReadOnlyObjectProperty<PlatformImage> getImageProperty(Image image) {
                return image.acc_platformImageProperty();
            }

            @Override // com.sun.javafx.tk.Toolkit.ImageAccessor
            public int[] getPreColors(PixelFormat<ByteBuffer> pf) {
                return ((PixelFormat.IndexedPixelFormat) pf).getPreColors();
            }

            @Override // com.sun.javafx.tk.Toolkit.ImageAccessor
            public int[] getNonPreColors(PixelFormat<ByteBuffer> pf) {
                return ((PixelFormat.IndexedPixelFormat) pf).getNonPreColors();
            }
        });
        URL_QUICKMATCH = Pattern.compile("^\\p{Alpha}[\\p{Alnum}+.-]*:.*$");
        runningTasks = 0;
        pendingTasks = new LinkedList();
    }

    @Deprecated
    public final String impl_getUrl() {
        return this.url;
    }

    final InputStream getImpl_source() {
        return this.impl_source;
    }

    final void setProgress(double value) {
        progressPropertyImpl().set(value);
    }

    public final double getProgress() {
        if (this.progress == null) {
            return 0.0d;
        }
        return this.progress.get();
    }

    public final ReadOnlyDoubleProperty progressProperty() {
        return progressPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyDoubleWrapper progressPropertyImpl() {
        if (this.progress == null) {
            this.progress = new ReadOnlyDoubleWrapper(this, "progress");
        }
        return this.progress;
    }

    public final double getRequestedWidth() {
        return this.requestedWidth;
    }

    public final double getRequestedHeight() {
        return this.requestedHeight;
    }

    public final double getWidth() {
        if (this.width == null) {
            return 0.0d;
        }
        return this.width.get();
    }

    public final ReadOnlyDoubleProperty widthProperty() {
        return widthPropertyImpl();
    }

    private DoublePropertyImpl widthPropertyImpl() {
        if (this.width == null) {
            this.width = new DoublePropertyImpl(MetadataParser.WIDTH_TAG_NAME);
        }
        return this.width;
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/Image$DoublePropertyImpl.class */
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
            return Image.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return this.name;
        }
    }

    public final double getHeight() {
        if (this.height == null) {
            return 0.0d;
        }
        return this.height.get();
    }

    public final ReadOnlyDoubleProperty heightProperty() {
        return heightPropertyImpl();
    }

    private DoublePropertyImpl heightPropertyImpl() {
        if (this.height == null) {
            this.height = new DoublePropertyImpl(MetadataParser.HEIGHT_TAG_NAME);
        }
        return this.height;
    }

    public final boolean isPreserveRatio() {
        return this.preserveRatio;
    }

    public final boolean isSmooth() {
        return this.smooth;
    }

    public final boolean isBackgroundLoading() {
        return this.backgroundLoading;
    }

    private void setError(boolean value) {
        errorPropertyImpl().set(value);
    }

    public final boolean isError() {
        if (this.error == null) {
            return false;
        }
        return this.error.get();
    }

    public final ReadOnlyBooleanProperty errorProperty() {
        return errorPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper errorPropertyImpl() {
        if (this.error == null) {
            this.error = new ReadOnlyBooleanWrapper(this, Pack200.Packer.ERROR);
        }
        return this.error;
    }

    private void setException(Exception value) {
        exceptionPropertyImpl().set(value);
    }

    public final Exception getException() {
        if (this.exception == null) {
            return null;
        }
        return this.exception.get();
    }

    public final ReadOnlyObjectProperty<Exception> exceptionProperty() {
        return exceptionPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Exception> exceptionPropertyImpl() {
        if (this.exception == null) {
            this.exception = new ReadOnlyObjectWrapper<>(this, "exception");
        }
        return this.exception;
    }

    @Deprecated
    public final Object impl_getPlatformImage() {
        if (this.platformImage == null) {
            return null;
        }
        return this.platformImage.get();
    }

    final ReadOnlyObjectProperty<PlatformImage> acc_platformImageProperty() {
        return platformImagePropertyImpl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ObjectPropertyImpl<PlatformImage> platformImagePropertyImpl() {
        if (this.platformImage == null) {
            this.platformImage = new ObjectPropertyImpl<>("platformImage");
        }
        return this.platformImage;
    }

    void pixelsDirty() {
        platformImagePropertyImpl().fireValueChangedEvent();
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/Image$ObjectPropertyImpl.class */
    private final class ObjectPropertyImpl<T> extends ReadOnlyObjectPropertyBase<T> {
        private final String name;
        private T value;
        private boolean valid = true;

        public ObjectPropertyImpl(String name) {
            this.name = name;
        }

        public void store(T value) {
            this.value = value;
        }

        public void set(T value) {
            if (this.value != value) {
                this.value = value;
                markInvalid();
            }
        }

        @Override // javafx.beans.property.ReadOnlyObjectPropertyBase
        public void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }

        private void markInvalid() {
            if (this.valid) {
                this.valid = false;
                fireValueChangedEvent();
            }
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public T get() {
            this.valid = true;
            return this.value;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Image.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return this.name;
        }
    }

    public Image(@NamedArg("url") String url) {
        this(validateUrl(url), null, 0.0d, 0.0d, false, false, false);
        initialize(null);
    }

    public Image(@NamedArg("url") String url, @NamedArg("backgroundLoading") boolean backgroundLoading) {
        this(validateUrl(url), null, 0.0d, 0.0d, false, false, backgroundLoading);
        initialize(null);
    }

    public Image(@NamedArg("url") String url, @NamedArg("requestedWidth") double requestedWidth, @NamedArg("requestedHeight") double requestedHeight, @NamedArg("preserveRatio") boolean preserveRatio, @NamedArg("smooth") boolean smooth) {
        this(validateUrl(url), null, requestedWidth, requestedHeight, preserveRatio, smooth, false);
        initialize(null);
    }

    public Image(@NamedArg(value = "url", defaultValue = "\"\"") String url, @NamedArg("requestedWidth") double requestedWidth, @NamedArg("requestedHeight") double requestedHeight, @NamedArg("preserveRatio") boolean preserveRatio, @NamedArg(value = "smooth", defaultValue = "true") boolean smooth, @NamedArg("backgroundLoading") boolean backgroundLoading) {
        this(validateUrl(url), null, requestedWidth, requestedHeight, preserveRatio, smooth, backgroundLoading);
        initialize(null);
    }

    public Image(@NamedArg(BeanAdapter.IS_PREFIX) InputStream is) {
        this(null, validateInputStream(is), 0.0d, 0.0d, false, false, false);
        initialize(null);
    }

    public Image(@NamedArg(BeanAdapter.IS_PREFIX) InputStream is, @NamedArg("requestedWidth") double requestedWidth, @NamedArg("requestedHeight") double requestedHeight, @NamedArg("preserveRatio") boolean preserveRatio, @NamedArg("smooth") boolean smooth) {
        this(null, validateInputStream(is), requestedWidth, requestedHeight, preserveRatio, smooth, false);
        initialize(null);
    }

    Image(int width, int height) {
        this(null, null, width, height, false, false, false);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Image dimensions must be positive (w,h > 0)");
        }
        initialize(Toolkit.getToolkit().createPlatformImage(width, height));
    }

    private Image(Object externalImage) {
        this(null, null, 0.0d, 0.0d, false, false, false);
        initialize(externalImage);
    }

    private Image(String url, InputStream is, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth, boolean backgroundLoading) {
        this.url = url;
        this.impl_source = is;
        this.requestedWidth = requestedWidth;
        this.requestedHeight = requestedHeight;
        this.preserveRatio = preserveRatio;
        this.smooth = smooth;
        this.backgroundLoading = backgroundLoading;
    }

    public void cancel() {
        if (this.backgroundTask != null) {
            this.backgroundTask.cancel();
        }
    }

    void dispose() {
        cancel();
        Platform.runLater(() -> {
            if (this.animation != null) {
                this.animation.stop();
            }
        });
    }

    private void initialize(Object externalImage) {
        ImageLoader loader;
        if (externalImage != null) {
            ImageLoader loader2 = loadPlatformImage(externalImage);
            finishImage(loader2);
        } else {
            if (isBackgroundLoading() && this.impl_source == null) {
                loadInBackground();
                return;
            }
            if (this.impl_source != null) {
                loader = loadImage(this.impl_source, getRequestedWidth(), getRequestedHeight(), isPreserveRatio(), isSmooth());
            } else {
                loader = loadImage(impl_getUrl(), getRequestedWidth(), getRequestedHeight(), isPreserveRatio(), isSmooth());
            }
            finishImage(loader);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishImage(ImageLoader loader) {
        Exception loadingException = loader.getException();
        if (loadingException != null) {
            finishImage(loadingException);
            return;
        }
        if (loader.getFrameCount() > 1) {
            initializeAnimatedImage(loader);
        } else {
            PlatformImage pi = loader.getFrame(0);
            double w2 = loader.getWidth() / pi.getPixelScale();
            double h2 = loader.getHeight() / pi.getPixelScale();
            setPlatformImageWH(pi, w2, h2);
        }
        setProgress(1.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishImage(Exception exception) {
        setException(exception);
        setError(true);
        setPlatformImageWH(null, 0.0d, 0.0d);
        setProgress(1.0d);
    }

    private void initializeAnimatedImage(ImageLoader loader) {
        int frameCount = loader.getFrameCount();
        this.animFrames = new PlatformImage[frameCount];
        for (int i2 = 0; i2 < frameCount; i2++) {
            this.animFrames[i2] = loader.getFrame(i2);
        }
        PlatformImage zeroFrame = loader.getFrame(0);
        double w2 = loader.getWidth() / zeroFrame.getPixelScale();
        double h2 = loader.getHeight() / zeroFrame.getPixelScale();
        setPlatformImageWH(zeroFrame, w2, h2);
        this.isAnimated = true;
        Platform.runLater(() -> {
            this.animation = new Animation(this, loader);
            this.animation.start();
        });
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/Image$Animation.class */
    private static final class Animation {
        final WeakReference<Image> imageRef;
        final SimpleIntegerProperty frameIndex = new SimpleIntegerProperty() { // from class: javafx.scene.image.Image.Animation.1
            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                Animation.this.updateImage(get());
            }
        };
        final Timeline timeline = new Timeline();

        public Animation(Image image, ImageLoader loader) {
            this.imageRef = new WeakReference<>(image);
            int loopCount = loader.getLoopCount();
            this.timeline.setCycleCount(loopCount == 0 ? -1 : loopCount);
            int frameCount = loader.getFrameCount();
            int duration = 0;
            for (int i2 = 0; i2 < frameCount; i2++) {
                addKeyFrame(i2, duration);
                duration += loader.getFrameDelay(i2);
            }
            this.timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration), new KeyValue[0]));
        }

        public void start() {
            this.timeline.play();
        }

        public void stop() {
            this.timeline.stop();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateImage(int frameIndex) {
            Image image = this.imageRef.get();
            if (image != null) {
                image.platformImagePropertyImpl().set(image.animFrames[frameIndex]);
            } else {
                this.timeline.stop();
            }
        }

        private void addKeyFrame(int index, double duration) {
            this.timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration), new KeyValue(this.frameIndex, Integer.valueOf(index), Interpolator.DISCRETE)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cycleTasks() {
        synchronized (pendingTasks) {
            runningTasks--;
            ImageTask nextTask = pendingTasks.poll();
            if (nextTask != null) {
                runningTasks++;
                nextTask.start();
            }
        }
    }

    private void loadInBackground() {
        this.backgroundTask = new ImageTask();
        synchronized (pendingTasks) {
            if (runningTasks >= 4) {
                pendingTasks.offer(this.backgroundTask);
            } else {
                runningTasks++;
                this.backgroundTask.start();
            }
        }
    }

    @Deprecated
    public static Image impl_fromPlatformImage(Object image) {
        return new Image(image);
    }

    private void setPlatformImageWH(PlatformImage newPlatformImage, double newWidth, double newHeight) {
        if (impl_getPlatformImage() == newPlatformImage && getWidth() == newWidth && getHeight() == newHeight) {
            return;
        }
        Object oldPlatformImage = impl_getPlatformImage();
        double oldWidth = getWidth();
        double oldHeight = getHeight();
        storePlatformImageWH(newPlatformImage, newWidth, newHeight);
        if (oldPlatformImage != newPlatformImage) {
            platformImagePropertyImpl().fireValueChangedEvent();
        }
        if (oldWidth != newWidth) {
            widthPropertyImpl().fireValueChangedEvent();
        }
        if (oldHeight != newHeight) {
            heightPropertyImpl().fireValueChangedEvent();
        }
    }

    private void storePlatformImageWH(PlatformImage platformImage, double width, double height) {
        platformImagePropertyImpl().store(platformImage);
        widthPropertyImpl().store(width);
        heightPropertyImpl().store(height);
    }

    void setPlatformImage(PlatformImage newPlatformImage) {
        this.platformImage.set(newPlatformImage);
    }

    /* loaded from: jfxrt.jar:javafx/scene/image/Image$ImageTask.class */
    private final class ImageTask implements AsyncOperationListener<ImageLoader> {
        private final AsyncOperation peer = constructPeer();

        public ImageTask() {
        }

        @Override // com.sun.javafx.runtime.async.AsyncOperationListener
        public void onCancel() {
            Image.this.finishImage(new CancellationException("Loading cancelled"));
            Image.this.cycleTasks();
        }

        @Override // com.sun.javafx.runtime.async.AsyncOperationListener
        public void onException(Exception exception) {
            Image.this.finishImage(exception);
            Image.this.cycleTasks();
        }

        @Override // com.sun.javafx.runtime.async.AsyncOperationListener
        public void onCompletion(ImageLoader value) {
            Image.this.finishImage(value);
            Image.this.cycleTasks();
        }

        @Override // com.sun.javafx.runtime.async.AsyncOperationListener
        public void onProgress(int cur, int max) {
            if (max > 0) {
                double curProgress = cur / max;
                if (curProgress < 1.0d && curProgress >= Image.this.getProgress() + 0.1d) {
                    Image.this.setProgress(curProgress);
                }
            }
        }

        public void start() {
            this.peer.start();
        }

        public void cancel() {
            this.peer.cancel();
        }

        private AsyncOperation constructPeer() {
            return Image.loadImageAsync(this, Image.this.url, Image.this.requestedWidth, Image.this.requestedHeight, Image.this.preserveRatio, Image.this.smooth);
        }
    }

    private static ImageLoader loadImage(String url, double width, double height, boolean preserveRatio, boolean smooth) {
        return Toolkit.getToolkit().loadImage(url, (int) width, (int) height, preserveRatio, smooth);
    }

    private static ImageLoader loadImage(InputStream stream, double width, double height, boolean preserveRatio, boolean smooth) {
        return Toolkit.getToolkit().loadImage(stream, (int) width, (int) height, preserveRatio, smooth);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AsyncOperation loadImageAsync(AsyncOperationListener<? extends ImageLoader> listener, String url, double width, double height, boolean preserveRatio, boolean smooth) {
        return Toolkit.getToolkit().loadImageAsync(listener, url, (int) width, (int) height, preserveRatio, smooth);
    }

    private static ImageLoader loadPlatformImage(Object platformImage) {
        return Toolkit.getToolkit().loadPlatformImage(platformImage);
    }

    private static String validateUrl(String url) {
        URL resource;
        if (url == null) {
            throw new NullPointerException("URL must not be null");
        }
        if (url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL must not be empty");
        }
        try {
            if (!URL_QUICKMATCH.matcher(url).matches()) {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (url.charAt(0) == '/') {
                    resource = contextClassLoader.getResource(url.substring(1));
                } else {
                    resource = contextClassLoader.getResource(url);
                }
                if (resource == null) {
                    throw new IllegalArgumentException("Invalid URL or resource not found");
                }
                return resource.toString();
            }
            return new URL(url).toString();
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException(constructDetailedExceptionMessage("Invalid URL", e2), e2);
        } catch (MalformedURLException e3) {
            throw new IllegalArgumentException(constructDetailedExceptionMessage("Invalid URL", e3), e3);
        }
    }

    private static InputStream validateInputStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("Input stream must not be null");
        }
        return inputStream;
    }

    private static String constructDetailedExceptionMessage(String mainMessage, Throwable cause) {
        if (cause == null) {
            return mainMessage;
        }
        String causeMessage = cause.getMessage();
        return constructDetailedExceptionMessage(causeMessage != null ? mainMessage + ": " + causeMessage : mainMessage, cause.getCause());
    }

    boolean isAnimation() {
        return this.isAnimated;
    }

    boolean pixelsReadable() {
        return (getProgress() < 1.0d || isAnimation() || isError()) ? false : true;
    }

    public final PixelReader getPixelReader() {
        if (!pixelsReadable()) {
            return null;
        }
        if (this.reader == null) {
            this.reader = new PixelReader() { // from class: javafx.scene.image.Image.2
                @Override // javafx.scene.image.PixelReader
                public PixelFormat getPixelFormat() {
                    PlatformImage pimg = (PlatformImage) Image.this.platformImage.get();
                    return pimg.getPlatformPixelFormat();
                }

                @Override // javafx.scene.image.PixelReader
                public int getArgb(int x2, int y2) {
                    PlatformImage pimg = (PlatformImage) Image.this.platformImage.get();
                    return pimg.getArgb(x2, y2);
                }

                @Override // javafx.scene.image.PixelReader
                public Color getColor(int x2, int y2) {
                    int argb = getArgb(x2, y2);
                    int a2 = argb >>> 24;
                    int r2 = (argb >> 16) & 255;
                    int g2 = (argb >> 8) & 255;
                    int b2 = argb & 255;
                    return Color.rgb(r2, g2, b2, a2 / 255.0d);
                }

                @Override // javafx.scene.image.PixelReader
                public <T extends Buffer> void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<T> pixelformat, T buffer, int scanlineStride) {
                    PlatformImage pimg = (PlatformImage) Image.this.platformImage.get();
                    pimg.getPixels(x2, y2, w2, h2, pixelformat, buffer, scanlineStride);
                }

                @Override // javafx.scene.image.PixelReader
                public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<ByteBuffer> pixelformat, byte[] buffer, int offset, int scanlineStride) {
                    PlatformImage pimg = (PlatformImage) Image.this.platformImage.get();
                    pimg.getPixels(x2, y2, w2, h2, pixelformat, buffer, offset, scanlineStride);
                }

                @Override // javafx.scene.image.PixelReader
                public void getPixels(int x2, int y2, int w2, int h2, WritablePixelFormat<IntBuffer> pixelformat, int[] buffer, int offset, int scanlineStride) {
                    PlatformImage pimg = (PlatformImage) Image.this.platformImage.get();
                    pimg.getPixels(x2, y2, w2, h2, pixelformat, buffer, offset, scanlineStride);
                }
            };
        }
        return this.reader;
    }

    PlatformImage getWritablePlatformImage() {
        PlatformImage pimg = this.platformImage.get();
        if (!pimg.isWritable()) {
            pimg = pimg.promoteToWritableImage();
            this.platformImage.set(pimg);
        }
        return pimg;
    }
}
