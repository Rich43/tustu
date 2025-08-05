package sun.awt.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Hashtable;

/* loaded from: rt.jar:sun/awt/image/ToolkitImage.class */
public class ToolkitImage extends Image {
    ImageProducer source;
    InputStreamImageSource src;
    ImageRepresentation imagerep;
    private int width = -1;
    private int height = -1;
    private Hashtable properties;
    private int availinfo;

    static {
        NativeLibLoader.loadLibraries();
    }

    protected ToolkitImage() {
    }

    public ToolkitImage(ImageProducer imageProducer) {
        this.source = imageProducer;
        if (imageProducer instanceof InputStreamImageSource) {
            this.src = (InputStreamImageSource) imageProducer;
        }
    }

    @Override // java.awt.Image
    public ImageProducer getSource() {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        return this.source;
    }

    public int getWidth() {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 1) == 0) {
            reconstruct(1);
        }
        return this.width;
    }

    @Override // java.awt.Image
    public synchronized int getWidth(ImageObserver imageObserver) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 1) == 0) {
            addWatcher(imageObserver, true);
            if ((this.availinfo & 1) == 0) {
                return -1;
            }
        }
        return this.width;
    }

    public int getHeight() {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 2) == 0) {
            reconstruct(2);
        }
        return this.height;
    }

    @Override // java.awt.Image
    public synchronized int getHeight(ImageObserver imageObserver) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 2) == 0) {
            addWatcher(imageObserver, true);
            if ((this.availinfo & 2) == 0) {
                return -1;
            }
        }
        return this.height;
    }

    @Override // java.awt.Image
    public Object getProperty(String str, ImageObserver imageObserver) {
        if (str == null) {
            throw new NullPointerException("null property name is not allowed");
        }
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if (this.properties == null) {
            addWatcher(imageObserver, true);
            if (this.properties == null) {
                return null;
            }
        }
        Object obj = this.properties.get(str);
        if (obj == null) {
            obj = Image.UndefinedProperty;
        }
        return obj;
    }

    public boolean hasError() {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        return (this.availinfo & 64) != 0;
    }

    public int check(ImageObserver imageObserver) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 64) == 0 && ((this.availinfo ^ (-1)) & 7) != 0) {
            addWatcher(imageObserver, false);
        }
        return this.availinfo;
    }

    public void preload(ImageObserver imageObserver) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 32) == 0) {
            addWatcher(imageObserver, true);
        }
    }

    private synchronized void addWatcher(ImageObserver imageObserver, boolean z2) {
        if ((this.availinfo & 64) != 0) {
            if (imageObserver != null) {
                imageObserver.imageUpdate(this, 192, -1, -1, -1, -1);
            }
        } else {
            ImageRepresentation imageRep = getImageRep();
            imageRep.addWatcher(imageObserver);
            if (z2) {
                imageRep.startProduction();
            }
        }
    }

    private synchronized void reconstruct(int i2) {
        if ((i2 & (this.availinfo ^ (-1))) == 0 || (this.availinfo & 64) != 0) {
            return;
        }
        getImageRep().startProduction();
        while ((i2 & (this.availinfo ^ (-1))) != 0) {
            try {
                wait();
                if ((this.availinfo & 64) != 0) {
                    return;
                }
            } catch (InterruptedException e2) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    synchronized void addInfo(int i2) {
        this.availinfo |= i2;
        notifyAll();
    }

    void setDimensions(int i2, int i3) {
        this.width = i2;
        this.height = i3;
        addInfo(3);
    }

    void setProperties(Hashtable hashtable) {
        if (hashtable == null) {
            hashtable = new Hashtable();
        }
        this.properties = hashtable;
        addInfo(4);
    }

    synchronized void infoDone(int i2) {
        if (i2 == 1 || ((this.availinfo ^ (-1)) & 3) != 0) {
            addInfo(64);
        } else if ((this.availinfo & 4) == 0) {
            setProperties(null);
        }
    }

    @Override // java.awt.Image
    public void flush() {
        ImageRepresentation imageRepresentation;
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        synchronized (this) {
            this.availinfo &= -65;
            imageRepresentation = this.imagerep;
            this.imagerep = null;
        }
        if (imageRepresentation != null) {
            imageRepresentation.abort();
        }
        if (this.src != null) {
            this.src.flush();
        }
    }

    protected ImageRepresentation makeImageRep() {
        return new ImageRepresentation(this, ColorModel.getRGBdefault(), false);
    }

    public synchronized ImageRepresentation getImageRep() {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if (this.imagerep == null) {
            this.imagerep = makeImageRep();
        }
        return this.imagerep;
    }

    @Override // java.awt.Image
    public Graphics getGraphics() {
        throw new UnsupportedOperationException("getGraphics() not valid for images created with createImage(producer)");
    }

    public ColorModel getColorModel() {
        return getImageRep().getColorModel();
    }

    public BufferedImage getBufferedImage() {
        return getImageRep().getBufferedImage();
    }

    @Override // java.awt.Image
    public void setAccelerationPriority(float f2) {
        super.setAccelerationPriority(f2);
        getImageRep().setAccelerationPriority(this.accelerationPriority);
    }
}
