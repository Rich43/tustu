package javax.imageio;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.util.List;
import javax.imageio.metadata.IIOMetadata;

/* loaded from: rt.jar:javax/imageio/IIOImage.class */
public class IIOImage {
    protected RenderedImage image;
    protected Raster raster;
    protected List<? extends BufferedImage> thumbnails;
    protected IIOMetadata metadata;

    public IIOImage(RenderedImage renderedImage, List<? extends BufferedImage> list, IIOMetadata iIOMetadata) {
        this.thumbnails = null;
        if (renderedImage == null) {
            throw new IllegalArgumentException("image == null!");
        }
        this.image = renderedImage;
        this.raster = null;
        this.thumbnails = list;
        this.metadata = iIOMetadata;
    }

    public IIOImage(Raster raster, List<? extends BufferedImage> list, IIOMetadata iIOMetadata) {
        this.thumbnails = null;
        if (raster == null) {
            throw new IllegalArgumentException("raster == null!");
        }
        this.raster = raster;
        this.image = null;
        this.thumbnails = list;
        this.metadata = iIOMetadata;
    }

    public RenderedImage getRenderedImage() {
        RenderedImage renderedImage;
        synchronized (this) {
            renderedImage = this.image;
        }
        return renderedImage;
    }

    public void setRenderedImage(RenderedImage renderedImage) {
        synchronized (this) {
            if (renderedImage == null) {
                throw new IllegalArgumentException("image == null!");
            }
            this.image = renderedImage;
            this.raster = null;
        }
    }

    public boolean hasRaster() {
        boolean z2;
        synchronized (this) {
            z2 = this.raster != null;
        }
        return z2;
    }

    public Raster getRaster() {
        Raster raster;
        synchronized (this) {
            raster = this.raster;
        }
        return raster;
    }

    public void setRaster(Raster raster) {
        synchronized (this) {
            if (raster == null) {
                throw new IllegalArgumentException("raster == null!");
            }
            this.raster = raster;
            this.image = null;
        }
    }

    public int getNumThumbnails() {
        if (this.thumbnails == null) {
            return 0;
        }
        return this.thumbnails.size();
    }

    public BufferedImage getThumbnail(int i2) {
        if (this.thumbnails == null) {
            throw new IndexOutOfBoundsException("No thumbnails available!");
        }
        return this.thumbnails.get(i2);
    }

    public List<? extends BufferedImage> getThumbnails() {
        return this.thumbnails;
    }

    public void setThumbnails(List<? extends BufferedImage> list) {
        this.thumbnails = list;
    }

    public IIOMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(IIOMetadata iIOMetadata) {
        this.metadata = iIOMetadata;
    }
}
