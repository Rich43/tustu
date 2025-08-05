package com.sun.javafx.webkit.prism;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.ImageStorageException;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageDecoder;
import com.sun.webkit.graphics.WCImageFrame;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCImageDecoderImpl.class */
final class WCImageDecoderImpl extends WCImageDecoder {
    private Service<ImageFrame[]> loader;
    private ImageFrame[] frames;
    private PrismImage[] images;
    private volatile byte[] data;
    private String fileNameExtension;
    private static final Logger log = Logger.getLogger(WCImageDecoderImpl.class.getName());
    private static final ThreadLocal<int[]> THREAD_LOCAL_SIZE_ARRAY = new ThreadLocal<int[]>() { // from class: com.sun.javafx.webkit.prism.WCImageDecoderImpl.3
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public int[] initialValue() {
            return new int[2];
        }
    };
    private int imageWidth = 0;
    private int imageHeight = 0;
    private int frameCount = 0;
    private boolean fullDataReceived = false;
    private boolean framesDecoded = false;
    private volatile int dataSize = 0;
    private final ImageLoadListener readerListener = new ImageLoadListener() { // from class: com.sun.javafx.webkit.prism.WCImageDecoderImpl.2
        @Override // com.sun.javafx.iio.ImageLoadListener
        public void imageLoadProgress(ImageLoader l2, float p2) {
        }

        @Override // com.sun.javafx.iio.ImageLoadListener
        public void imageLoadWarning(ImageLoader l2, String warning) {
        }

        @Override // com.sun.javafx.iio.ImageLoadListener
        public void imageLoadMetaData(ImageLoader l2, ImageMetadata metadata) {
            if (WCImageDecoderImpl.log.isLoggable(Level.FINE)) {
                WCImageDecoderImpl.log.fine(String.format("%X Image size %dx%d", Integer.valueOf(hashCode()), metadata.imageWidth, metadata.imageHeight));
            }
            if (WCImageDecoderImpl.this.imageWidth < metadata.imageWidth.intValue()) {
                WCImageDecoderImpl.this.imageWidth = metadata.imageWidth.intValue();
            }
            if (WCImageDecoderImpl.this.imageHeight < metadata.imageHeight.intValue()) {
                WCImageDecoderImpl.this.imageHeight = metadata.imageHeight.intValue();
            }
            WCImageDecoderImpl.this.fileNameExtension = l2.getFormatDescription().getExtensions().get(0);
        }
    };

    WCImageDecoderImpl() {
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected synchronized void destroy() {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X Destroy image decoder", Integer.valueOf(hashCode())));
        }
        destroyLoader();
        this.frames = null;
        this.images = null;
        this.framesDecoded = false;
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected String getFilenameExtension() {
        return "." + this.fileNameExtension;
    }

    private boolean imageSizeAvilable() {
        return this.imageWidth > 0 && this.imageHeight > 0;
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected void addImageData(byte[] dataPortion) {
        if (dataPortion != null) {
            this.fullDataReceived = false;
            if (this.data == null) {
                this.data = Arrays.copyOf(dataPortion, dataPortion.length * 2);
                this.dataSize = dataPortion.length;
            } else {
                int newDataSize = this.dataSize + dataPortion.length;
                if (newDataSize > this.data.length) {
                    resizeDataArray(Math.max(newDataSize, this.data.length * 2));
                }
                System.arraycopy(dataPortion, 0, this.data, this.dataSize, dataPortion.length);
                this.dataSize = newDataSize;
            }
            if (!imageSizeAvilable()) {
                loadFrames();
                return;
            }
            return;
        }
        if (this.data != null && !this.fullDataReceived) {
            if (this.data.length > this.dataSize) {
                resizeDataArray(this.dataSize);
            }
            this.fullDataReceived = true;
        }
    }

    private void destroyLoader() {
        if (this.loader != null) {
            this.loader.cancel();
            this.loader = null;
        }
    }

    private void startLoader() {
        if (this.loader == null) {
            this.loader = new Service<ImageFrame[]>() { // from class: com.sun.javafx.webkit.prism.WCImageDecoderImpl.1
                @Override // javafx.concurrent.Service
                protected Task<ImageFrame[]> createTask() {
                    return new Task<ImageFrame[]>() { // from class: com.sun.javafx.webkit.prism.WCImageDecoderImpl.1.1
                        /* JADX INFO: Access modifiers changed from: protected */
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // javafx.concurrent.Task
                        public ImageFrame[] call() throws Exception {
                            return WCImageDecoderImpl.this.loadFrames();
                        }
                    };
                }
            };
            this.loader.valueProperty().addListener((ov, old, frames) -> {
                if (frames != null && this.loader != null) {
                    setFrames(frames);
                }
            });
        }
        if (!this.loader.isRunning()) {
            this.loader.restart();
        }
    }

    private void resizeDataArray(int newDataSize) {
        byte[] newData = new byte[newDataSize];
        System.arraycopy(this.data, 0, newData, 0, this.dataSize);
        this.data = newData;
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected void loadFromResource(String name) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X Load image from resource '%s'", Integer.valueOf(hashCode()), name));
        }
        String resourceName = WCGraphicsManager.getResourceName(name);
        InputStream in = getClass().getResourceAsStream(resourceName);
        if (in == null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(String.format("%X Unable to open resource '%s'", Integer.valueOf(hashCode()), resourceName));
                return;
            }
            return;
        }
        setFrames(loadFrames(in));
    }

    private synchronized ImageFrame[] loadFrames(InputStream in) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X Decoding frames", Integer.valueOf(hashCode())));
        }
        try {
            ImageFrame[] imageFrameArrLoadAll = ImageStorage.loadAll(in, this.readerListener, 0, 0, true, 1.0f, false);
            try {
                in.close();
            } catch (IOException e2) {
            }
            return imageFrameArrLoadAll;
        } catch (ImageStorageException e3) {
            try {
                in.close();
            } catch (IOException e4) {
            }
            return null;
        } catch (Throwable th) {
            try {
                in.close();
            } catch (IOException e5) {
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ImageFrame[] loadFrames() {
        return loadFrames(new ByteArrayInputStream(this.data, 0, this.dataSize));
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected int[] getImageSize() {
        int[] size = THREAD_LOCAL_SIZE_ARRAY.get();
        size[0] = this.imageWidth;
        size[1] = this.imageHeight;
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X image size = %dx%d", Integer.valueOf(hashCode()), Integer.valueOf(size[0]), Integer.valueOf(size[1])));
        }
        return size;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCImageDecoderImpl$Frame.class */
    private static final class Frame extends WCImageFrame {
        private WCImage image;

        private Frame(WCImage image, String extension) {
            this.image = image;
            this.image.setFileExtension(extension);
        }

        @Override // com.sun.webkit.graphics.WCImageFrame
        public WCImage getFrame() {
            return this.image;
        }

        @Override // com.sun.webkit.graphics.WCImageFrame
        public int[] getSize() {
            int[] size = (int[]) WCImageDecoderImpl.THREAD_LOCAL_SIZE_ARRAY.get();
            size[0] = this.image.getWidth();
            size[1] = this.image.getHeight();
            return size;
        }

        @Override // com.sun.webkit.graphics.WCImageFrame
        protected void destroyDecodedData() {
            this.image = null;
        }
    }

    private synchronized void setFrames(ImageFrame[] frames) {
        this.frames = frames;
        this.images = null;
        this.frameCount = frames == null ? 0 : frames.length;
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected int getFrameCount() {
        if (this.fullDataReceived) {
            getImageFrame(0);
        }
        return this.frameCount;
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected synchronized WCImageFrame getFrame(int idx) {
        ImageFrame frame = getImageFrame(idx);
        if (frame != null) {
            if (log.isLoggable(Level.FINE)) {
                ImageStorage.ImageType type = frame.getImageType();
                log.fine(String.format("%X getFrame(%d): image type = %s", Integer.valueOf(hashCode()), Integer.valueOf(idx), type));
            }
            PrismImage img = getPrismImage(idx, frame);
            return new Frame(img, this.fileNameExtension);
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("%X FAILED getFrame(%d)", Integer.valueOf(hashCode()), Integer.valueOf(idx)));
            return null;
        }
        return null;
    }

    private synchronized ImageMetadata getFrameMetadata(int idx) {
        if (this.frames == null || this.frames.length <= idx || this.frames[idx] == null) {
            return null;
        }
        return this.frames[idx].getMetadata();
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected int getFrameDuration(int idx) {
        ImageMetadata meta = getFrameMetadata(idx);
        int dur = (meta == null || meta.delayTime == null) ? 0 : meta.delayTime.intValue();
        if (dur < 11) {
            dur = 100;
        }
        return dur;
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected int[] getFrameSize(int idx) {
        ImageMetadata meta = getFrameMetadata(idx);
        if (meta == null) {
            return null;
        }
        int[] size = THREAD_LOCAL_SIZE_ARRAY.get();
        size[0] = meta.imageWidth.intValue();
        size[1] = meta.imageHeight.intValue();
        return size;
    }

    @Override // com.sun.webkit.graphics.WCImageDecoder
    protected synchronized boolean getFrameCompleteStatus(int idx) {
        return getFrameMetadata(idx) != null && this.framesDecoded;
    }

    private synchronized ImageFrame getImageFrame(int idx) {
        if (!this.fullDataReceived) {
            startLoader();
        } else if (this.fullDataReceived && !this.framesDecoded) {
            destroyLoader();
            setFrames(loadFrames());
            this.framesDecoded = true;
        }
        if (idx < 0 || this.frames == null || this.frames.length <= idx) {
            return null;
        }
        return this.frames[idx];
    }

    private synchronized PrismImage getPrismImage(int idx, ImageFrame frame) {
        if (this.images == null) {
            this.images = new PrismImage[this.frames.length];
        }
        if (this.images[idx] == null) {
            this.images[idx] = new WCImageImpl(frame);
        }
        return this.images[idx];
    }
}
