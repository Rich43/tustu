package java.awt.image;

import java.util.Hashtable;

/* loaded from: rt.jar:java/awt/image/FilteredImageSource.class */
public class FilteredImageSource implements ImageProducer {
    ImageProducer src;
    ImageFilter filter;
    private Hashtable proxies;

    public FilteredImageSource(ImageProducer imageProducer, ImageFilter imageFilter) {
        this.src = imageProducer;
        this.filter = imageFilter;
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void addConsumer(ImageConsumer imageConsumer) {
        if (this.proxies == null) {
            this.proxies = new Hashtable();
        }
        if (!this.proxies.containsKey(imageConsumer)) {
            ImageFilter filterInstance = this.filter.getFilterInstance(imageConsumer);
            this.proxies.put(imageConsumer, filterInstance);
            this.src.addConsumer(filterInstance);
        }
    }

    @Override // java.awt.image.ImageProducer
    public synchronized boolean isConsumer(ImageConsumer imageConsumer) {
        return this.proxies != null && this.proxies.containsKey(imageConsumer);
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void removeConsumer(ImageConsumer imageConsumer) {
        ImageFilter imageFilter;
        if (this.proxies != null && (imageFilter = (ImageFilter) this.proxies.get(imageConsumer)) != null) {
            this.src.removeConsumer(imageFilter);
            this.proxies.remove(imageConsumer);
            if (this.proxies.isEmpty()) {
                this.proxies = null;
            }
        }
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void startProduction(ImageConsumer imageConsumer) {
        if (this.proxies == null) {
            this.proxies = new Hashtable();
        }
        ImageFilter filterInstance = (ImageFilter) this.proxies.get(imageConsumer);
        if (filterInstance == null) {
            filterInstance = this.filter.getFilterInstance(imageConsumer);
            this.proxies.put(imageConsumer, filterInstance);
        }
        this.src.startProduction(filterInstance);
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void requestTopDownLeftRightResend(ImageConsumer imageConsumer) {
        ImageFilter imageFilter;
        if (this.proxies != null && (imageFilter = (ImageFilter) this.proxies.get(imageConsumer)) != null) {
            imageFilter.resendTopDownLeftRight(this.src);
        }
    }
}
