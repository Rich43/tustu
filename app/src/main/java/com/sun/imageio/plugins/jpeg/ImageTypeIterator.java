package com.sun.imageio.plugins.jpeg;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.imageio.ImageTypeSpecifier;

/* compiled from: JPEGImageReader.java */
/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/ImageTypeIterator.class */
class ImageTypeIterator implements Iterator<ImageTypeSpecifier> {
    private Iterator<ImageTypeProducer> producers;
    private ImageTypeSpecifier theNext = null;

    public ImageTypeIterator(Iterator<ImageTypeProducer> it) {
        this.producers = it;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.theNext != null) {
            return true;
        }
        if (!this.producers.hasNext()) {
            return false;
        }
        do {
            this.theNext = this.producers.next().getType();
            if (this.theNext != null) {
                break;
            }
        } while (this.producers.hasNext());
        return this.theNext != null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public ImageTypeSpecifier next() {
        if (this.theNext != null || hasNext()) {
            ImageTypeSpecifier imageTypeSpecifier = this.theNext;
            this.theNext = null;
            return imageTypeSpecifier;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public void remove() {
        this.producers.remove();
    }
}
