package com.sun.javafx.tk.quantum;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.ImageStorageException;
import com.sun.javafx.runtime.async.AbstractRemoteResource;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.prism.Image;
import com.sun.prism.impl.PrismSettings;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PrismImageLoader2.class */
class PrismImageLoader2 implements ImageLoader {
    private static PlatformLogger imageioLogger = null;
    private Image[] images;
    private int[] delayTimes;
    private int loopCount;
    private int width;
    private int height;
    private float pixelScale;
    private Exception exception;

    public PrismImageLoader2(String url, int width, int height, boolean preserveRatio, float pixelScale, boolean smooth) {
        loadAll(url, width, height, preserveRatio, pixelScale, smooth);
    }

    public PrismImageLoader2(InputStream stream, int width, int height, boolean preserveRatio, boolean smooth) {
        loadAll(stream, width, height, preserveRatio, smooth);
    }

    @Override // com.sun.javafx.tk.ImageLoader
    public int getWidth() {
        return this.width;
    }

    @Override // com.sun.javafx.tk.ImageLoader
    public int getHeight() {
        return this.height;
    }

    @Override // com.sun.javafx.tk.ImageLoader
    public int getFrameCount() {
        if (this.images == null) {
            return 0;
        }
        return this.images.length;
    }

    @Override // com.sun.javafx.tk.ImageLoader
    public PlatformImage getFrame(int index) {
        if (this.images == null) {
            return null;
        }
        return this.images[index];
    }

    @Override // com.sun.javafx.tk.ImageLoader
    public int getFrameDelay(int index) {
        if (this.images == null) {
            return 0;
        }
        return this.delayTimes[index];
    }

    @Override // com.sun.javafx.tk.ImageLoader
    public int getLoopCount() {
        if (this.images == null) {
            return 0;
        }
        return this.loopCount;
    }

    @Override // com.sun.javafx.tk.ImageLoader
    public Exception getException() {
        return this.exception;
    }

    private void loadAll(String url, int w2, int h2, boolean preserveRatio, float pixelScale, boolean smooth) {
        ImageLoadListener listener = new PrismLoadListener();
        try {
            ImageFrame[] imgFrames = ImageStorage.loadAll(url, listener, w2, h2, preserveRatio, pixelScale, smooth);
            convertAll(imgFrames);
        } catch (ImageStorageException e2) {
            handleException(e2);
        } catch (Exception e3) {
            handleException(e3);
        }
    }

    private void loadAll(InputStream stream, int w2, int h2, boolean preserveRatio, boolean smooth) {
        ImageLoadListener listener = new PrismLoadListener();
        try {
            ImageFrame[] imgFrames = ImageStorage.loadAll(stream, listener, w2, h2, preserveRatio, 1.0f, smooth);
            convertAll(imgFrames);
        } catch (ImageStorageException e2) {
            handleException(e2);
        } catch (Exception e3) {
            handleException(e3);
        }
    }

    private void handleException(ImageStorageException isException) {
        Throwable exceptionCause = isException.getCause();
        if (exceptionCause instanceof Exception) {
            handleException((Exception) exceptionCause);
        } else {
            handleException((Exception) isException);
        }
    }

    private void handleException(Exception exception) {
        if (PrismSettings.verbose) {
            exception.printStackTrace(System.err);
        }
        this.exception = exception;
    }

    private void convertAll(ImageFrame[] imgFrames) {
        int numFrames = imgFrames.length;
        this.images = new Image[numFrames];
        this.delayTimes = new int[numFrames];
        for (int i2 = 0; i2 < numFrames; i2++) {
            ImageFrame frame = imgFrames[i2];
            this.images[i2] = Image.convertImageFrame(frame);
            ImageMetadata metadata = frame.getMetadata();
            if (metadata != null) {
                Integer delay = metadata.delayTime;
                if (delay != null) {
                    this.delayTimes[i2] = delay.intValue();
                }
                Integer loopCount = metadata.loopCount;
                if (loopCount != null) {
                    this.loopCount = loopCount.intValue();
                }
            }
            if (i2 == 0) {
                this.width = frame.getWidth();
                this.height = frame.getHeight();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized PlatformLogger getImageioLogger() {
        if (imageioLogger == null) {
            imageioLogger = PlatformLogger.getLogger("javafx.scene.image");
        }
        return imageioLogger;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PrismImageLoader2$PrismLoadListener.class */
    private class PrismLoadListener implements ImageLoadListener {
        private PrismLoadListener() {
        }

        @Override // com.sun.javafx.iio.ImageLoadListener
        public void imageLoadWarning(com.sun.javafx.iio.ImageLoader loader, String message) {
            PrismImageLoader2.getImageioLogger().warning(message);
        }

        @Override // com.sun.javafx.iio.ImageLoadListener
        public void imageLoadProgress(com.sun.javafx.iio.ImageLoader loader, float percentageComplete) {
        }

        @Override // com.sun.javafx.iio.ImageLoadListener
        public void imageLoadMetaData(com.sun.javafx.iio.ImageLoader loader, ImageMetadata metadata) {
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PrismImageLoader2$AsyncImageLoader.class */
    static final class AsyncImageLoader extends AbstractRemoteResource<PrismImageLoader2> {
        private static final ExecutorService BG_LOADING_EXECUTOR = createExecutor();
        private final AccessControlContext acc;
        int width;
        int height;
        boolean preserveRatio;
        boolean smooth;

        public AsyncImageLoader(AsyncOperationListener<PrismImageLoader2> listener, String url, int width, int height, boolean preserveRatio, boolean smooth) {
            super(url, listener);
            this.width = width;
            this.height = height;
            this.preserveRatio = preserveRatio;
            this.smooth = smooth;
            this.acc = AccessController.getContext();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.javafx.runtime.async.AbstractRemoteResource
        public PrismImageLoader2 processStream(InputStream stream) throws IOException {
            return new PrismImageLoader2(stream, this.width, this.height, this.preserveRatio, this.smooth);
        }

        @Override // com.sun.javafx.runtime.async.AbstractRemoteResource, java.util.concurrent.Callable
        public PrismImageLoader2 call() throws IOException {
            try {
                return (PrismImageLoader2) AccessController.doPrivileged(() -> {
                    return (PrismImageLoader2) super.call();
                }, this.acc);
            } catch (PrivilegedActionException e2) {
                Throwable cause = e2.getCause();
                if (cause instanceof IOException) {
                    throw ((IOException) cause);
                }
                throw new UndeclaredThrowableException(cause);
            }
        }

        @Override // com.sun.javafx.runtime.async.AbstractAsyncOperation, com.sun.javafx.runtime.async.AsyncOperation
        public void start() {
            BG_LOADING_EXECUTOR.execute(this.future);
        }

        private static ExecutorService createExecutor() {
            ThreadGroup bgLoadingThreadGroup = (ThreadGroup) AccessController.doPrivileged(() -> {
                return new ThreadGroup(QuantumToolkit.getFxUserThread().getThreadGroup(), "Background image loading thread pool");
            });
            ThreadFactory bgLoadingThreadFactory = runnable -> {
                return (Thread) AccessController.doPrivileged(() -> {
                    Thread newThread = new Thread(bgLoadingThreadGroup, runnable);
                    newThread.setPriority(1);
                    return newThread;
                });
            };
            ExecutorService bgLoadingExecutor = Executors.newCachedThreadPool(bgLoadingThreadFactory);
            ((ThreadPoolExecutor) bgLoadingExecutor).setKeepAliveTime(1L, TimeUnit.SECONDS);
            return bgLoadingExecutor;
        }
    }
}
