package com.sun.javafx.webkit.prism;

import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/RTImage.class */
final class RTImage extends PrismImage implements ResourceFactoryListener {
    private RTTexture txt;
    private final int width;
    private final int height;
    private WeakReference<ResourceFactory> registeredWithFactory = null;
    private ByteBuffer pixelBuffer;
    private float pixelScale;
    private static final Logger log = Logger.getLogger(RTImage.class.getName());

    RTImage(int w2, int h2, float pixelScale) {
        this.width = w2;
        this.height = h2;
        this.pixelScale = pixelScale;
    }

    @Override // com.sun.javafx.webkit.prism.PrismImage
    Image getImage() {
        return Image.fromByteBgraPreData(getPixelBuffer(), getWidth(), getHeight());
    }

    @Override // com.sun.javafx.webkit.prism.PrismImage
    Graphics getGraphics() {
        RTTexture texture = getTexture();
        if (texture == null) {
            return null;
        }
        Graphics g2 = texture.createGraphics();
        g2.transform(PrismGraphicsManager.getPixelScaleTransform());
        return g2;
    }

    private RTTexture getTexture() {
        if (this.txt != null && this.txt.isSurfaceLost()) {
            log.fine("RTImage::getTexture : surface lost: " + ((Object) this));
        }
        ResourceFactory f2 = GraphicsPipeline.getDefaultResourceFactory();
        if (f2 == null || f2.isDisposed()) {
            log.fine("RTImage::getTexture : return null because device disposed or not ready");
            return null;
        }
        if (this.txt == null) {
            this.txt = f2.createRTTexture((int) Math.ceil(this.width * this.pixelScale), (int) Math.ceil(this.height * this.pixelScale), Texture.WrapMode.CLAMP_NOT_NEEDED);
            this.txt.contentsUseful();
            this.txt.makePermanent();
            if (this.registeredWithFactory == null || this.registeredWithFactory.get() != f2) {
                f2.addFactoryListener(this);
                this.registeredWithFactory = new WeakReference<>(f2);
            }
        }
        return this.txt;
    }

    @Override // com.sun.javafx.webkit.prism.PrismImage
    void draw(Graphics g2, int dstx1, int dsty1, int dstx2, int dsty2, int srcx1, int srcy1, int srcx2, int srcy2) {
        if (this.txt == null && g2.getCompositeMode() == CompositeMode.SRC_OVER) {
            return;
        }
        if (g2.getResourceFactory().isDisposed()) {
            log.fine("RTImage::draw : skip because device has been disposed");
            return;
        }
        if (g2 instanceof PrinterGraphics) {
            int w2 = srcx2 - srcx1;
            int h2 = srcy2 - srcy1;
            IntBuffer pixels = IntBuffer.allocate(w2 * h2);
            PrismInvoker.runOnRenderThread(() -> {
                getTexture().readPixels(pixels);
            });
            Image img = Image.fromIntArgbPreData(pixels, w2, h2);
            Texture t2 = g2.getResourceFactory().createTexture(img, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED);
            g2.drawTexture(t2, dstx1, dsty1, dstx2, dsty2, 0.0f, 0.0f, w2, h2);
            t2.dispose();
            return;
        }
        if (this.txt == null) {
            Paint p2 = g2.getPaint();
            g2.setPaint(Color.TRANSPARENT);
            g2.fillQuad(dstx1, dsty1, dstx2, dsty2);
            g2.setPaint(p2);
            return;
        }
        g2.drawTexture(this.txt, dstx1, dsty1, dstx2, dsty2, srcx1 * this.pixelScale, srcy1 * this.pixelScale, srcx2 * this.pixelScale, srcy2 * this.pixelScale);
    }

    @Override // com.sun.javafx.webkit.prism.PrismImage
    void dispose() {
        PrismInvoker.invokeOnRenderThread(() -> {
            if (this.txt != null) {
                this.txt.dispose();
                this.txt = null;
            }
        });
    }

    @Override // com.sun.webkit.graphics.WCImage
    public int getWidth() {
        return this.width;
    }

    @Override // com.sun.webkit.graphics.WCImage
    public int getHeight() {
        return this.height;
    }

    @Override // com.sun.webkit.graphics.WCImage
    public ByteBuffer getPixelBuffer() {
        boolean isNew = false;
        if (this.pixelBuffer == null) {
            this.pixelBuffer = ByteBuffer.allocateDirect(this.width * this.height * 4);
            if (this.pixelBuffer != null) {
                this.pixelBuffer.order(ByteOrder.nativeOrder());
                isNew = true;
            }
        }
        if (isNew || isDirty()) {
            PrismInvoker.runOnRenderThread(() -> {
                ResourceFactory f2 = GraphicsPipeline.getDefaultResourceFactory();
                if (f2 == null || f2.isDisposed()) {
                    log.fine("RTImage::getPixelBuffer : skip because device disposed or not ready");
                    return;
                }
                flushRQ();
                if (this.txt != null && this.pixelBuffer != null) {
                    PixelFormat pf = this.txt.getPixelFormat();
                    if (pf != PixelFormat.INT_ARGB_PRE && pf != PixelFormat.BYTE_BGRA_PRE) {
                        throw new AssertionError((Object) ("Unexpected pixel format: " + ((Object) pf)));
                    }
                    RTTexture t2 = this.txt;
                    if (this.pixelScale != 1.0f) {
                        t2 = f2.createRTTexture(this.width, this.height, Texture.WrapMode.CLAMP_NOT_NEEDED);
                        Graphics g2 = t2.createGraphics();
                        g2.drawTexture(this.txt, 0.0f, 0.0f, this.width, this.height, 0.0f, 0.0f, this.width * this.pixelScale, this.height * this.pixelScale);
                    }
                    this.pixelBuffer.rewind();
                    int[] pixels = t2.getPixels();
                    if (pixels != null) {
                        this.pixelBuffer.asIntBuffer().put(pixels);
                    } else {
                        t2.readPixels(this.pixelBuffer);
                    }
                    if (t2 != this.txt) {
                        t2.dispose();
                    }
                }
            });
        }
        return this.pixelBuffer;
    }

    @Override // com.sun.webkit.graphics.WCImage
    protected void drawPixelBuffer() {
        PrismInvoker.invokeOnRenderThread(new Runnable() { // from class: com.sun.javafx.webkit.prism.RTImage.1
            @Override // java.lang.Runnable
            public void run() {
                Graphics g2 = RTImage.this.getGraphics();
                if (g2 != null && RTImage.this.pixelBuffer != null) {
                    RTImage.this.pixelBuffer.rewind();
                    Image img = Image.fromByteBgraPreData(RTImage.this.pixelBuffer, RTImage.this.width, RTImage.this.height);
                    Texture txt = g2.getResourceFactory().createTexture(img, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED);
                    g2.clear();
                    g2.drawTexture(txt, 0.0f, 0.0f, RTImage.this.width, RTImage.this.height);
                    txt.dispose();
                }
            }
        });
    }

    @Override // com.sun.prism.ResourceFactoryListener
    public void factoryReset() {
        if (this.txt != null) {
            this.txt.dispose();
            this.txt = null;
        }
    }

    @Override // com.sun.prism.ResourceFactoryListener
    public void factoryReleased() {
        if (this.txt != null) {
            this.txt.dispose();
            this.txt = null;
        }
    }

    @Override // com.sun.webkit.graphics.WCImage
    public float getPixelScale() {
        return this.pixelScale;
    }
}
