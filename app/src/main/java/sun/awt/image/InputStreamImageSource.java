package sun.awt.image;

import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:sun/awt/image/InputStreamImageSource.class */
public abstract class InputStreamImageSource implements ImageProducer, ImageFetchable {
    ImageConsumerQueue consumers;
    ImageDecoder decoder;
    ImageDecoder decoders;
    boolean awaitingFetch = false;

    abstract boolean checkSecurity(Object obj, boolean z2);

    protected abstract ImageDecoder getDecoder();

    int countConsumers(ImageConsumerQueue imageConsumerQueue) {
        int i2 = 0;
        while (imageConsumerQueue != null) {
            i2++;
            imageConsumerQueue = imageConsumerQueue.next;
        }
        return i2;
    }

    synchronized int countConsumers() {
        int iCountConsumers = countConsumers(this.consumers);
        for (ImageDecoder imageDecoder = this.decoders; imageDecoder != null; imageDecoder = imageDecoder.next) {
            iCountConsumers += countConsumers(imageDecoder.queue);
        }
        return iCountConsumers;
    }

    @Override // java.awt.image.ImageProducer
    public void addConsumer(ImageConsumer imageConsumer) {
        addConsumer(imageConsumer, false);
    }

    synchronized void printQueue(ImageConsumerQueue imageConsumerQueue, String str) {
        while (imageConsumerQueue != null) {
            System.out.println(str + ((Object) imageConsumerQueue));
            imageConsumerQueue = imageConsumerQueue.next;
        }
    }

    synchronized void printQueues(String str) {
        System.out.println(str + "[ -----------");
        printQueue(this.consumers, Constants.INDENT);
        ImageDecoder imageDecoder = this.decoders;
        while (true) {
            ImageDecoder imageDecoder2 = imageDecoder;
            if (imageDecoder2 != null) {
                System.out.println("    " + ((Object) imageDecoder2));
                printQueue(imageDecoder2.queue, GoToActionDialog.EMPTY_DESTINATION);
                imageDecoder = imageDecoder2.next;
            } else {
                System.out.println("----------- ]" + str);
                return;
            }
        }
    }

    synchronized void addConsumer(ImageConsumer imageConsumer, boolean z2) {
        ImageConsumerQueue imageConsumerQueue;
        checkSecurity(null, false);
        ImageDecoder imageDecoder = this.decoders;
        while (true) {
            ImageDecoder imageDecoder2 = imageDecoder;
            if (imageDecoder2 != null) {
                if (!imageDecoder2.isConsumer(imageConsumer)) {
                    imageDecoder = imageDecoder2.next;
                } else {
                    return;
                }
            } else {
                ImageConsumerQueue imageConsumerQueue2 = this.consumers;
                while (true) {
                    imageConsumerQueue = imageConsumerQueue2;
                    if (imageConsumerQueue == null || imageConsumerQueue.consumer == imageConsumer) {
                        break;
                    } else {
                        imageConsumerQueue2 = imageConsumerQueue.next;
                    }
                }
                if (imageConsumerQueue == null) {
                    ImageConsumerQueue imageConsumerQueue3 = new ImageConsumerQueue(this, imageConsumer);
                    imageConsumerQueue3.next = this.consumers;
                    this.consumers = imageConsumerQueue3;
                } else {
                    if (!imageConsumerQueue.secure) {
                        Object securityContext = null;
                        SecurityManager securityManager = System.getSecurityManager();
                        if (securityManager != null) {
                            securityContext = securityManager.getSecurityContext();
                        }
                        if (imageConsumerQueue.securityContext == null) {
                            imageConsumerQueue.securityContext = securityContext;
                        } else if (!imageConsumerQueue.securityContext.equals(securityContext)) {
                            errorConsumer(imageConsumerQueue, false);
                            throw new SecurityException("Applets are trading image data!");
                        }
                    }
                    imageConsumerQueue.interested = true;
                }
                if (z2 && this.decoder == null) {
                    startProduction();
                    return;
                }
                return;
            }
        }
    }

    @Override // java.awt.image.ImageProducer
    public synchronized boolean isConsumer(ImageConsumer imageConsumer) {
        ImageDecoder imageDecoder = this.decoders;
        while (true) {
            ImageDecoder imageDecoder2 = imageDecoder;
            if (imageDecoder2 != null) {
                if (!imageDecoder2.isConsumer(imageConsumer)) {
                    imageDecoder = imageDecoder2.next;
                } else {
                    return true;
                }
            } else {
                return ImageConsumerQueue.isConsumer(this.consumers, imageConsumer);
            }
        }
    }

    private void errorAllConsumers(ImageConsumerQueue imageConsumerQueue, boolean z2) {
        while (imageConsumerQueue != null) {
            if (imageConsumerQueue.interested) {
                errorConsumer(imageConsumerQueue, z2);
            }
            imageConsumerQueue = imageConsumerQueue.next;
        }
    }

    private void errorConsumer(ImageConsumerQueue imageConsumerQueue, boolean z2) {
        imageConsumerQueue.consumer.imageComplete(1);
        if (z2 && (imageConsumerQueue.consumer instanceof ImageRepresentation)) {
            ((ImageRepresentation) imageConsumerQueue.consumer).image.flush();
        }
        removeConsumer(imageConsumerQueue.consumer);
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void removeConsumer(ImageConsumer imageConsumer) {
        ImageDecoder imageDecoder = this.decoders;
        while (true) {
            ImageDecoder imageDecoder2 = imageDecoder;
            if (imageDecoder2 != null) {
                imageDecoder2.removeConsumer(imageConsumer);
                imageDecoder = imageDecoder2.next;
            } else {
                this.consumers = ImageConsumerQueue.removeConsumer(this.consumers, imageConsumer, false);
                return;
            }
        }
    }

    @Override // java.awt.image.ImageProducer
    public void startProduction(ImageConsumer imageConsumer) {
        addConsumer(imageConsumer, true);
    }

    private synchronized void startProduction() {
        if (!this.awaitingFetch) {
            if (ImageFetcher.add(this)) {
                this.awaitingFetch = true;
                return;
            }
            ImageConsumerQueue imageConsumerQueue = this.consumers;
            this.consumers = null;
            errorAllConsumers(imageConsumerQueue, false);
        }
    }

    private synchronized void stopProduction() {
        if (this.awaitingFetch) {
            ImageFetcher.remove(this);
            this.awaitingFetch = false;
        }
    }

    @Override // java.awt.image.ImageProducer
    public void requestTopDownLeftRightResend(ImageConsumer imageConsumer) {
    }

    protected ImageDecoder decoderForType(InputStream inputStream, String str) {
        return null;
    }

    protected ImageDecoder getDecoder(InputStream inputStream) {
        if (!inputStream.markSupported()) {
            inputStream = new BufferedInputStream(inputStream);
        }
        try {
            inputStream.mark(8);
            int i2 = inputStream.read();
            int i3 = inputStream.read();
            int i4 = inputStream.read();
            int i5 = inputStream.read();
            int i6 = inputStream.read();
            int i7 = inputStream.read();
            int i8 = inputStream.read();
            int i9 = inputStream.read();
            inputStream.reset();
            inputStream.mark(-1);
            if (i2 == 71 && i3 == 73 && i4 == 70 && i5 == 56) {
                return new GifImageDecoder(this, inputStream);
            }
            if (i2 == 255 && i3 == 216 && i4 == 255) {
                return new JPEGImageDecoder(this, inputStream);
            }
            if (i2 == 35 && i3 == 100 && i4 == 101 && i5 == 102) {
                return new XbmImageDecoder(this, inputStream);
            }
            if (i2 == 137 && i3 == 80 && i4 == 78 && i5 == 71 && i6 == 13 && i7 == 10 && i8 == 26 && i9 == 10) {
                return new PNGImageDecoder(this, inputStream);
            }
            return null;
        } catch (IOException e2) {
            return null;
        }
    }

    @Override // sun.awt.image.ImageFetchable
    public void doFetch() {
        synchronized (this) {
            if (this.consumers == null) {
                this.awaitingFetch = false;
                return;
            }
            ImageDecoder decoder = getDecoder();
            if (decoder == null) {
                badDecoder();
                return;
            }
            setDecoder(decoder);
            try {
                try {
                    decoder.produceImage();
                    removeDecoder(decoder);
                    if (Thread.currentThread().isInterrupted() || !Thread.currentThread().isAlive()) {
                        errorAllConsumers(decoder.queue, true);
                    } else {
                        errorAllConsumers(decoder.queue, false);
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    removeDecoder(decoder);
                    if (Thread.currentThread().isInterrupted() || !Thread.currentThread().isAlive()) {
                        errorAllConsumers(decoder.queue, true);
                    } else {
                        errorAllConsumers(decoder.queue, false);
                    }
                } catch (ImageFormatException e3) {
                    e3.printStackTrace();
                    removeDecoder(decoder);
                    if (Thread.currentThread().isInterrupted() || !Thread.currentThread().isAlive()) {
                        errorAllConsumers(decoder.queue, true);
                    } else {
                        errorAllConsumers(decoder.queue, false);
                    }
                }
            } catch (Throwable th) {
                removeDecoder(decoder);
                if (Thread.currentThread().isInterrupted() || !Thread.currentThread().isAlive()) {
                    errorAllConsumers(decoder.queue, true);
                } else {
                    errorAllConsumers(decoder.queue, false);
                }
                throw th;
            }
        }
    }

    private void badDecoder() {
        ImageConsumerQueue imageConsumerQueue;
        synchronized (this) {
            imageConsumerQueue = this.consumers;
            this.consumers = null;
            this.awaitingFetch = false;
        }
        errorAllConsumers(imageConsumerQueue, false);
    }

    private void setDecoder(ImageDecoder imageDecoder) {
        ImageConsumerQueue imageConsumerQueue;
        synchronized (this) {
            imageDecoder.next = this.decoders;
            this.decoders = imageDecoder;
            this.decoder = imageDecoder;
            imageConsumerQueue = this.consumers;
            imageDecoder.queue = imageConsumerQueue;
            this.consumers = null;
            this.awaitingFetch = false;
        }
        while (imageConsumerQueue != null) {
            if (imageConsumerQueue.interested && !checkSecurity(imageConsumerQueue.securityContext, true)) {
                errorConsumer(imageConsumerQueue, false);
            }
            imageConsumerQueue = imageConsumerQueue.next;
        }
    }

    private synchronized void removeDecoder(ImageDecoder imageDecoder) {
        doneDecoding(imageDecoder);
        ImageDecoder imageDecoder2 = null;
        ImageDecoder imageDecoder3 = this.decoders;
        while (true) {
            ImageDecoder imageDecoder4 = imageDecoder3;
            if (imageDecoder4 != null) {
                if (imageDecoder4 == imageDecoder) {
                    if (imageDecoder2 == null) {
                        this.decoders = imageDecoder4.next;
                        return;
                    } else {
                        imageDecoder2.next = imageDecoder4.next;
                        return;
                    }
                }
                imageDecoder2 = imageDecoder4;
                imageDecoder3 = imageDecoder4.next;
            } else {
                return;
            }
        }
    }

    synchronized void doneDecoding(ImageDecoder imageDecoder) {
        if (this.decoder == imageDecoder) {
            this.decoder = null;
            if (this.consumers != null) {
                startProduction();
            }
        }
    }

    void latchConsumers(ImageDecoder imageDecoder) {
        doneDecoding(imageDecoder);
    }

    synchronized void flush() {
        this.decoder = null;
    }
}
