package com.sun.javafx.webkit.prism;

import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.webkit.graphics.WCImage;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Base64;
import java.util.Iterator;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritablePixelFormat;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/PrismImage.class */
abstract class PrismImage extends WCImage {
    abstract Image getImage();

    abstract Graphics getGraphics();

    abstract void draw(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    abstract void dispose();

    PrismImage() {
    }

    @Override // com.sun.webkit.graphics.WCImage
    public Object getPlatformImage() {
        return getImage();
    }

    @Override // com.sun.webkit.graphics.Ref
    public void deref() {
        super.deref();
        if (!hasRefs()) {
            dispose();
        }
    }

    @Override // com.sun.webkit.graphics.WCImage
    protected final byte[] toData(String mimeType) {
        BufferedImage image = toBufferedImage(mimeType.equals("image/jpeg"));
        if (image != null) {
            Iterator<ImageWriter> it = ImageIO.getImageWritersByMIMEType(mimeType);
            while (it.hasNext()) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ImageWriter writer = it.next();
                try {
                    writer.setOutput(ImageIO.createImageOutputStream(output));
                    writer.write(image);
                    writer.dispose();
                    return output.toByteArray();
                } catch (IOException e2) {
                    writer.dispose();
                } catch (Throwable th) {
                    writer.dispose();
                    throw th;
                }
            }
            return null;
        }
        return null;
    }

    @Override // com.sun.webkit.graphics.WCImage
    protected final String toDataURL(String mimeType) {
        byte[] data = toData(mimeType);
        if (data != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("data:").append(mimeType).append(";base64,");
            sb.append(Base64.getMimeEncoder().encodeToString(data));
            return sb.toString();
        }
        return null;
    }

    private static int getBestBufferedImageType(PixelFormat<?> fxFormat) {
        switch (fxFormat.getType()) {
            case BYTE_BGRA_PRE:
            case INT_ARGB_PRE:
            default:
                return 3;
            case BYTE_BGRA:
            case INT_ARGB:
                return 2;
            case BYTE_RGB:
                return 1;
            case BYTE_INDEXED:
                return fxFormat.isPremultiplied() ? 3 : 2;
        }
    }

    private static WritablePixelFormat<IntBuffer> getAssociatedPixelFormat(BufferedImage bimg) {
        switch (bimg.getType()) {
            case 1:
            case 3:
                return PixelFormat.getIntArgbPreInstance();
            case 2:
                return PixelFormat.getIntArgbInstance();
            default:
                throw new InternalError("Failed to validate BufferedImage type");
        }
    }

    private static BufferedImage fromFXImage(Image img, boolean forceRGB) {
        int iw = img.getWidth();
        int ih = img.getHeight();
        int destImageType = forceRGB ? 1 : getBestBufferedImageType(img.getPlatformPixelFormat());
        BufferedImage bimg = new BufferedImage(iw, ih, destImageType);
        DataBufferInt db = (DataBufferInt) bimg.getRaster().getDataBuffer();
        int[] data = db.getData();
        int offset = bimg.getRaster().getDataBuffer().getOffset();
        int scan = 0;
        SampleModel sm = bimg.getRaster().getSampleModel();
        if (sm instanceof SinglePixelPackedSampleModel) {
            scan = ((SinglePixelPackedSampleModel) sm).getScanlineStride();
        }
        WritablePixelFormat<IntBuffer> pf = getAssociatedPixelFormat(bimg);
        img.getPixels(0, 0, iw, ih, pf, data, offset, scan);
        return bimg;
    }

    private BufferedImage toBufferedImage(boolean forceRGB) {
        try {
            return fromFXImage(getImage(), forceRGB);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    @Override // com.sun.webkit.graphics.WCImage
    public BufferedImage toBufferedImage() {
        return toBufferedImage(false);
    }
}
