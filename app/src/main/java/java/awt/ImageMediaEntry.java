package java.awt;

import java.awt.image.ImageObserver;
import java.io.Serializable;

/* compiled from: MediaTracker.java */
/* loaded from: rt.jar:java/awt/ImageMediaEntry.class */
class ImageMediaEntry extends MediaEntry implements ImageObserver, Serializable {
    Image image;
    int width;
    int height;
    private static final long serialVersionUID = 4739377000350280650L;

    ImageMediaEntry(MediaTracker mediaTracker, Image image, int i2, int i3, int i4) {
        super(mediaTracker, i2);
        this.image = image;
        this.width = i3;
        this.height = i4;
    }

    boolean matches(Image image, int i2, int i3) {
        return this.image == image && this.width == i2 && this.height == i3;
    }

    @Override // java.awt.MediaEntry
    Object getMedia() {
        return this.image;
    }

    @Override // java.awt.MediaEntry
    synchronized int getStatus(boolean z2, boolean z3) {
        if (z3) {
            int i2 = parseflags(this.tracker.target.checkImage(this.image, this.width, this.height, null));
            if (i2 == 0) {
                if ((this.status & 12) != 0) {
                    setStatus(2);
                }
            } else if (i2 != this.status) {
                setStatus(i2);
            }
        }
        return super.getStatus(z2, z3);
    }

    @Override // java.awt.MediaEntry
    void startLoad() {
        if (this.tracker.target.prepareImage(this.image, this.width, this.height, this)) {
            setStatus(8);
        }
    }

    int parseflags(int i2) {
        if ((i2 & 64) != 0) {
            return 4;
        }
        if ((i2 & 128) != 0) {
            return 2;
        }
        if ((i2 & 48) != 0) {
            return 8;
        }
        return 0;
    }

    @Override // java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        if (this.cancelled) {
            return false;
        }
        int i7 = parseflags(i2);
        if (i7 != 0 && i7 != this.status) {
            setStatus(i7);
        }
        return (this.status & 1) != 0;
    }
}
