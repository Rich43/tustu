package com.sun.javafx.iio.common;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageMetadata;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/common/ImageLoaderImpl.class */
public abstract class ImageLoaderImpl implements ImageLoader {
    protected ImageFormatDescription formatDescription;
    protected HashSet<ImageLoadListener> listeners;
    protected int lastPercentDone = -1;

    protected ImageLoaderImpl(ImageFormatDescription formatDescription) {
        if (formatDescription == null) {
            throw new IllegalArgumentException("formatDescription == null!");
        }
        this.formatDescription = formatDescription;
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public final ImageFormatDescription getFormatDescription() {
        return this.formatDescription;
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public final void addListener(ImageLoadListener listener) {
        if (this.listeners == null) {
            this.listeners = new HashSet<>();
        }
        this.listeners.add(listener);
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public final void removeListener(ImageLoadListener listener) {
        if (this.listeners != null) {
            this.listeners.remove(listener);
        }
    }

    protected void emitWarning(String warning) {
        if (this.listeners != null && !this.listeners.isEmpty()) {
            Iterator<ImageLoadListener> iter = this.listeners.iterator();
            while (iter.hasNext()) {
                ImageLoadListener l2 = iter.next();
                l2.imageLoadWarning(this, warning);
            }
        }
    }

    protected void updateImageProgress(float percentageDone) {
        if (this.listeners != null && !this.listeners.isEmpty()) {
            int percentDone = (int) percentageDone;
            if (((5 * percentDone) / 5) % 5 == 0 && percentDone != this.lastPercentDone) {
                this.lastPercentDone = percentDone;
                Iterator<ImageLoadListener> iter = this.listeners.iterator();
                while (iter.hasNext()) {
                    ImageLoadListener listener = iter.next();
                    listener.imageLoadProgress(this, percentDone);
                }
            }
        }
    }

    protected void updateImageMetadata(ImageMetadata metadata) {
        if (this.listeners != null && !this.listeners.isEmpty()) {
            Iterator<ImageLoadListener> iter = this.listeners.iterator();
            while (iter.hasNext()) {
                ImageLoadListener l2 = iter.next();
                l2.imageLoadMetaData(this, metadata);
            }
        }
    }
}
