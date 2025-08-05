package java.awt.image;

/* loaded from: rt.jar:java/awt/image/ImageProducer.class */
public interface ImageProducer {
    void addConsumer(ImageConsumer imageConsumer);

    boolean isConsumer(ImageConsumer imageConsumer);

    void removeConsumer(ImageConsumer imageConsumer);

    void startProduction(ImageConsumer imageConsumer);

    void requestTopDownLeftRightResend(ImageConsumer imageConsumer);
}
