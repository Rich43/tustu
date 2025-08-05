package com.sun.prism.j2d;

import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgr;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseTexture;
import com.sun.prism.impl.ManagedResource;
import com.sun.prism.impl.PrismSettings;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DTexture.class */
class J2DTexture extends BaseTexture<J2DTexResource> {
    private final PixelSetter setter;

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DTexture$J2DTexResource.class */
    static class J2DTexResource extends ManagedResource<BufferedImage> {
        public J2DTexResource(BufferedImage bimg) {
            super(bimg, J2DTexturePool.instance);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.prism.impl.ManagedResource
        public void free() {
            ((BufferedImage) this.resource).flush();
        }
    }

    static J2DTexture create(PixelFormat format, Texture.WrapMode wrapMode, int w2, int h2) {
        int type;
        PixelSetter setter;
        switch (format) {
            case BYTE_RGB:
                type = 5;
                setter = ByteBgr.setter;
                break;
            case BYTE_GRAY:
                type = 10;
                setter = ByteGray.setter;
                break;
            case INT_ARGB_PRE:
            case BYTE_BGRA_PRE:
                type = 3;
                setter = IntArgbPre.setter;
                break;
            default:
                throw new InternalError("Unrecognized PixelFormat (" + ((Object) format) + ")!");
        }
        J2DTexturePool pool = J2DTexturePool.instance;
        long size = J2DTexturePool.size(w2, h2, type);
        if (!pool.prepareForAllocation(size)) {
            return null;
        }
        BufferedImage bimg = new BufferedImage(w2, h2, type);
        return new J2DTexture(bimg, format, setter, wrapMode);
    }

    J2DTexture(BufferedImage bimg, PixelFormat format, PixelSetter setter, Texture.WrapMode wrapMode) {
        super(new J2DTexResource(bimg), format, wrapMode, bimg.getWidth(), bimg.getHeight());
        this.setter = setter;
    }

    J2DTexture(J2DTexture sharedTex, Texture.WrapMode altMode) {
        super(sharedTex, altMode, false);
        this.setter = sharedTex.setter;
    }

    @Override // com.sun.prism.impl.BaseTexture
    protected Texture createSharedTexture(Texture.WrapMode newMode) {
        return new J2DTexture(this, newMode);
    }

    BufferedImage getBufferedImage() {
        return ((J2DTexResource) this.resource).getResource();
    }

    private static PixelGetter getGetter(PixelFormat format) {
        switch (format) {
            case BYTE_RGB:
                return ByteRgb.getter;
            case BYTE_GRAY:
                return ByteGray.getter;
            case INT_ARGB_PRE:
                return IntArgbPre.getter;
            case BYTE_BGRA_PRE:
                return ByteBgraPre.getter;
            default:
                throw new InternalError("Unrecognized PixelFormat (" + ((Object) format) + ")!");
        }
    }

    private static Buffer getDstBuffer(BufferedImage bimg) {
        if (bimg.getType() == 3) {
            int[] dstbuf = ((DataBufferInt) bimg.getRaster().getDataBuffer()).getData();
            return IntBuffer.wrap(dstbuf);
        }
        byte[] dstbuf2 = ((DataBufferByte) bimg.getRaster().getDataBuffer()).getData();
        return ByteBuffer.wrap(dstbuf2);
    }

    void updateFromBuffer(BufferedImage bimg, Buffer buffer, PixelFormat format, int dstx, int dsty, int srcx, int srcy, int srcw, int srch, int srcscan) {
        PixelGetter getter = getGetter(format);
        PixelConverter converter = PixelUtils.getConverter(getter, this.setter);
        if (PrismSettings.debug) {
            System.out.println("src = [" + srcx + ", " + srcy + "] x [" + srcw + ", " + srch + "], dst = [" + dstx + ", " + dsty + "]");
            System.out.println("bimg = " + ((Object) bimg));
            System.out.println("format = " + ((Object) format) + ", buffer = " + ((Object) buffer));
            System.out.println("getter = " + ((Object) getter) + ", setter = " + ((Object) this.setter));
            System.out.println("converter = " + ((Object) converter));
        }
        int dstscan = bimg.getWidth() * this.setter.getNumElements();
        int dstoffset = (dsty * dstscan) + (dstx * this.setter.getNumElements());
        if (getter instanceof IntPixelGetter) {
            srcscan /= 4;
        }
        int srcoffset = buffer.position() + (srcy * srcscan) + (srcx * getter.getNumElements());
        converter.convert(buffer, srcoffset, srcscan, getDstBuffer(bimg), dstoffset, dstscan, srcw, srch);
    }

    @Override // com.sun.prism.Texture
    public void update(Buffer buffer, PixelFormat format, int dstx, int dsty, int srcx, int srcy, int srcw, int srch, int srcscan, boolean skipFlush) {
        BufferedImage bimg = getBufferedImage();
        buffer.position(0);
        updateFromBuffer(bimg, buffer, format, dstx, dsty, srcx, srcy, srcw, srch, srcscan);
    }

    @Override // com.sun.prism.Texture
    public void update(MediaFrame frame, boolean skipFlush) {
        frame.holdFrame();
        if (frame.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
            MediaFrame newFrame = frame.convertToFormat(PixelFormat.INT_ARGB_PRE);
            frame.releaseFrame();
            frame = newFrame;
            if (null == frame) {
                return;
            }
        }
        ByteBuffer bbuf = frame.getBufferForPlane(0);
        BufferedImage bimg = getBufferedImage();
        updateFromBuffer(bimg, bbuf.asIntBuffer(), PixelFormat.INT_ARGB_PRE, 0, 0, 0, 0, frame.getWidth(), frame.getHeight(), frame.strideForPlane(0));
        frame.releaseFrame();
    }
}
