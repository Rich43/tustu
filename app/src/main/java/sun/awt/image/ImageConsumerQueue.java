package sun.awt.image;

import java.awt.image.ImageConsumer;

/* loaded from: rt.jar:sun/awt/image/ImageConsumerQueue.class */
class ImageConsumerQueue {
    ImageConsumerQueue next;
    ImageConsumer consumer;
    boolean interested = true;
    Object securityContext;
    boolean secure;

    static ImageConsumerQueue removeConsumer(ImageConsumerQueue imageConsumerQueue, ImageConsumer imageConsumer, boolean z2) {
        ImageConsumerQueue imageConsumerQueue2 = null;
        ImageConsumerQueue imageConsumerQueue3 = imageConsumerQueue;
        while (true) {
            ImageConsumerQueue imageConsumerQueue4 = imageConsumerQueue3;
            if (imageConsumerQueue4 == null) {
                break;
            }
            if (imageConsumerQueue4.consumer == imageConsumer) {
                if (imageConsumerQueue2 == null) {
                    imageConsumerQueue = imageConsumerQueue4.next;
                } else {
                    imageConsumerQueue2.next = imageConsumerQueue4.next;
                }
                imageConsumerQueue4.interested = z2;
            } else {
                imageConsumerQueue2 = imageConsumerQueue4;
                imageConsumerQueue3 = imageConsumerQueue4.next;
            }
        }
        return imageConsumerQueue;
    }

    static boolean isConsumer(ImageConsumerQueue imageConsumerQueue, ImageConsumer imageConsumer) {
        ImageConsumerQueue imageConsumerQueue2 = imageConsumerQueue;
        while (true) {
            ImageConsumerQueue imageConsumerQueue3 = imageConsumerQueue2;
            if (imageConsumerQueue3 != null) {
                if (imageConsumerQueue3.consumer != imageConsumer) {
                    imageConsumerQueue2 = imageConsumerQueue3.next;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    ImageConsumerQueue(InputStreamImageSource inputStreamImageSource, ImageConsumer imageConsumer) {
        this.consumer = imageConsumer;
        if (imageConsumer instanceof ImageRepresentation) {
            if (((ImageRepresentation) imageConsumer).image.source != inputStreamImageSource) {
                throw new SecurityException("ImageRep added to wrong image source");
            }
            this.secure = true;
        } else {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                this.securityContext = securityManager.getSecurityContext();
            } else {
                this.securityContext = null;
            }
        }
    }

    public String toString() {
        return "[" + ((Object) this.consumer) + ", " + (this.interested ? "" : "not ") + "interested" + (this.securityContext != null ? ", " + this.securityContext : "") + "]";
    }
}
