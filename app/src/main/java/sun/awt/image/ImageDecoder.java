package sun.awt.image;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;

/* loaded from: rt.jar:sun/awt/image/ImageDecoder.class */
public abstract class ImageDecoder {
    InputStreamImageSource source;
    InputStream input;
    Thread feeder = Thread.currentThread();
    protected boolean aborted;
    protected boolean finished;
    ImageConsumerQueue queue;
    ImageDecoder next;

    public abstract void produceImage() throws IOException, ImageFormatException;

    public ImageDecoder(InputStreamImageSource inputStreamImageSource, InputStream inputStream) {
        this.source = inputStreamImageSource;
        this.input = inputStream;
    }

    public boolean isConsumer(ImageConsumer imageConsumer) {
        return ImageConsumerQueue.isConsumer(this.queue, imageConsumer);
    }

    public void removeConsumer(ImageConsumer imageConsumer) {
        this.queue = ImageConsumerQueue.removeConsumer(this.queue, imageConsumer, false);
        if (!this.finished && this.queue == null) {
            abort();
        }
    }

    protected ImageConsumerQueue nextConsumer(ImageConsumerQueue imageConsumerQueue) {
        synchronized (this.source) {
            if (this.aborted) {
                return null;
            }
            for (ImageConsumerQueue imageConsumerQueue2 = imageConsumerQueue == null ? this.queue : imageConsumerQueue.next; imageConsumerQueue2 != null; imageConsumerQueue2 = imageConsumerQueue2.next) {
                if (imageConsumerQueue2.interested) {
                    return imageConsumerQueue2;
                }
            }
            return null;
        }
    }

    protected int setDimensions(int i2, int i3) {
        ImageConsumerQueue imageConsumerQueue = null;
        int i4 = 0;
        while (true) {
            ImageConsumerQueue imageConsumerQueueNextConsumer = nextConsumer(imageConsumerQueue);
            imageConsumerQueue = imageConsumerQueueNextConsumer;
            if (imageConsumerQueueNextConsumer != null) {
                imageConsumerQueue.consumer.setDimensions(i2, i3);
                i4++;
            } else {
                return i4;
            }
        }
    }

    protected int setProperties(Hashtable hashtable) {
        ImageConsumerQueue imageConsumerQueue = null;
        int i2 = 0;
        while (true) {
            ImageConsumerQueue imageConsumerQueueNextConsumer = nextConsumer(imageConsumerQueue);
            imageConsumerQueue = imageConsumerQueueNextConsumer;
            if (imageConsumerQueueNextConsumer != null) {
                imageConsumerQueue.consumer.setProperties(hashtable);
                i2++;
            } else {
                return i2;
            }
        }
    }

    protected int setColorModel(ColorModel colorModel) {
        ImageConsumerQueue imageConsumerQueue = null;
        int i2 = 0;
        while (true) {
            ImageConsumerQueue imageConsumerQueueNextConsumer = nextConsumer(imageConsumerQueue);
            imageConsumerQueue = imageConsumerQueueNextConsumer;
            if (imageConsumerQueueNextConsumer != null) {
                imageConsumerQueue.consumer.setColorModel(colorModel);
                i2++;
            } else {
                return i2;
            }
        }
    }

    protected int setHints(int i2) {
        ImageConsumerQueue imageConsumerQueue = null;
        int i3 = 0;
        while (true) {
            ImageConsumerQueue imageConsumerQueueNextConsumer = nextConsumer(imageConsumerQueue);
            imageConsumerQueue = imageConsumerQueueNextConsumer;
            if (imageConsumerQueueNextConsumer != null) {
                imageConsumerQueue.consumer.setHints(i2);
                i3++;
            } else {
                return i3;
            }
        }
    }

    protected void headerComplete() {
        this.feeder.setPriority(3);
    }

    protected int setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        this.source.latchConsumers(this);
        ImageConsumerQueue imageConsumerQueue = null;
        int i8 = 0;
        while (true) {
            ImageConsumerQueue imageConsumerQueueNextConsumer = nextConsumer(imageConsumerQueue);
            imageConsumerQueue = imageConsumerQueueNextConsumer;
            if (imageConsumerQueueNextConsumer != null) {
                imageConsumerQueue.consumer.setPixels(i2, i3, i4, i5, colorModel, bArr, i6, i7);
                i8++;
            } else {
                return i8;
            }
        }
    }

    protected int setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7) {
        this.source.latchConsumers(this);
        ImageConsumerQueue imageConsumerQueue = null;
        int i8 = 0;
        while (true) {
            ImageConsumerQueue imageConsumerQueueNextConsumer = nextConsumer(imageConsumerQueue);
            imageConsumerQueue = imageConsumerQueueNextConsumer;
            if (imageConsumerQueueNextConsumer != null) {
                imageConsumerQueue.consumer.setPixels(i2, i3, i4, i5, colorModel, iArr, i6, i7);
                i8++;
            } else {
                return i8;
            }
        }
    }

    protected int imageComplete(int i2, boolean z2) {
        this.source.latchConsumers(this);
        if (z2) {
            this.finished = true;
            this.source.doneDecoding(this);
        }
        ImageConsumerQueue imageConsumerQueue = null;
        int i3 = 0;
        while (true) {
            ImageConsumerQueue imageConsumerQueueNextConsumer = nextConsumer(imageConsumerQueue);
            imageConsumerQueue = imageConsumerQueueNextConsumer;
            if (imageConsumerQueueNextConsumer != null) {
                imageConsumerQueue.consumer.imageComplete(i2);
                i3++;
            } else {
                return i3;
            }
        }
    }

    public void abort() {
        this.aborted = true;
        this.source.doneDecoding(this);
        close();
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.image.ImageDecoder.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                ImageDecoder.this.feeder.interrupt();
                return null;
            }
        });
    }

    public synchronized void close() {
        if (this.input != null) {
            try {
                this.input.close();
            } catch (IOException e2) {
            }
        }
    }
}
