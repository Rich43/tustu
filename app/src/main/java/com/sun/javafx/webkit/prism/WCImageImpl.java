package com.sun.javafx.webkit.prism;

import com.sun.javafx.iio.ImageFrame;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundCoords;
import com.sun.prism.image.CompoundTexture;
import com.sun.prism.image.Coords;
import com.sun.prism.image.ViewPort;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.PixelFormat;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCImageImpl.class */
final class WCImageImpl extends PrismImage {
    private static final Logger log;
    private final Image img;
    private Texture texture;
    private CompoundTexture compoundTexture;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WCImageImpl.class.desiredAssertionStatus();
        log = Logger.getLogger(WCImageImpl.class.getName());
    }

    WCImageImpl(int w2, int h2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Creating empty image({0},{1})", new Object[]{Integer.valueOf(w2), Integer.valueOf(h2)});
        }
        this.img = Image.fromIntArgbPreData(new int[w2 * h2], w2, h2);
    }

    WCImageImpl(int[] buffer, int w2, int h2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Creating image({0},{1}) from buffer", new Object[]{Integer.valueOf(w2), Integer.valueOf(h2)});
        }
        this.img = Image.fromIntArgbPreData(buffer, w2, h2);
    }

    WCImageImpl(ImageFrame frame) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Creating image {0}x{1} of type {2} from buffer", new Object[]{Integer.valueOf(frame.getWidth()), Integer.valueOf(frame.getHeight()), frame.getImageType()});
        }
        this.img = Image.convertImageFrame(frame);
    }

    @Override // com.sun.javafx.webkit.prism.PrismImage
    Image getImage() {
        return this.img;
    }

    @Override // com.sun.javafx.webkit.prism.PrismImage
    Graphics getGraphics() {
        return null;
    }

    @Override // com.sun.javafx.webkit.prism.PrismImage
    void draw(Graphics g2, int dstx1, int dsty1, int dstx2, int dsty2, int srcx1, int srcy1, int srcx2, int srcy2) {
        ResourceFactory resourceFactory = g2.getResourceFactory();
        if (resourceFactory.isDisposed()) {
            log.fine("WCImageImpl::draw : skip because device disposed or not ready");
            return;
        }
        if (g2 instanceof PrinterGraphics) {
            Texture t2 = resourceFactory.createTexture(this.img, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED);
            g2.drawTexture(t2, dstx1, dsty1, dstx2, dsty2, srcx1, srcy1, srcx2, srcy2);
            t2.dispose();
            return;
        }
        if (this.texture != null) {
            this.texture.lock();
            if (this.texture.isSurfaceLost()) {
                this.texture = null;
            }
        }
        if (this.texture == null && this.compoundTexture == null) {
            int maxSize = resourceFactory.getMaximumTextureSize();
            if (this.img.getWidth() <= maxSize && this.img.getHeight() <= maxSize) {
                this.texture = resourceFactory.createTexture(this.img, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
                if (!$assertionsDisabled && this.texture == null) {
                    throw new AssertionError();
                }
            } else {
                this.compoundTexture = new CompoundTexture(this.img, maxSize);
            }
        }
        if (this.texture != null) {
            if (!$assertionsDisabled && this.compoundTexture != null) {
                throw new AssertionError();
            }
            g2.drawTexture(this.texture, dstx1, dsty1, dstx2, dsty2, srcx1, srcy1, srcx2, srcy2);
            this.texture.unlock();
            return;
        }
        if (!$assertionsDisabled && this.compoundTexture == null) {
            throw new AssertionError();
        }
        ViewPort viewPort = new ViewPort(srcx1, srcy1, srcx2 - srcx1, srcy2 - srcy1);
        Coords coords = new Coords(dstx2 - dstx1, dsty2 - dsty1, viewPort);
        CompoundCoords compoundCoords = new CompoundCoords(this.compoundTexture, coords);
        compoundCoords.draw(g2, this.compoundTexture, dstx1, dsty1);
    }

    @Override // com.sun.javafx.webkit.prism.PrismImage
    void dispose() {
        if (this.texture != null) {
            this.texture.dispose();
            this.texture = null;
        }
        if (this.compoundTexture != null) {
            this.compoundTexture.dispose();
            this.compoundTexture = null;
        }
    }

    @Override // com.sun.webkit.graphics.WCImage
    public int getWidth() {
        return this.img.getWidth();
    }

    @Override // com.sun.webkit.graphics.WCImage
    public int getHeight() {
        return this.img.getHeight();
    }

    @Override // com.sun.webkit.graphics.WCImage
    public ByteBuffer getPixelBuffer() {
        int w2 = this.img.getWidth();
        int h2 = this.img.getHeight();
        int s2 = w2 * 4;
        ByteBuffer pixels = ByteBuffer.allocate(s2 * h2);
        this.img.getPixels(0, 0, w2, h2, PixelFormat.getByteBgraInstance(), pixels, s2);
        return pixels;
    }

    @Override // com.sun.webkit.graphics.WCImage
    public float getPixelScale() {
        return this.img.getPixelScale();
    }
}
