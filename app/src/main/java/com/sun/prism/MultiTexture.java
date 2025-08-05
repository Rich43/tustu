package com.sun.prism;

import com.sun.prism.Texture;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: jfxrt.jar:com/sun/prism/MultiTexture.class */
public final class MultiTexture implements Texture {
    private int width;
    private int height;
    private PixelFormat format;
    private Texture.WrapMode wrapMode;
    private boolean linearFiltering;
    private final ArrayList<Texture> textures;
    private int lastImageSerial;

    public MultiTexture(PixelFormat format, Texture.WrapMode wrapMode, int width, int height) {
        this.linearFiltering = true;
        this.width = width;
        this.height = height;
        this.format = format;
        this.wrapMode = wrapMode;
        this.textures = new ArrayList<>(4);
    }

    private MultiTexture(MultiTexture sharedTex, Texture.WrapMode newMode) {
        this(sharedTex.format, newMode, sharedTex.width, sharedTex.height);
        for (int i2 = 0; i2 < sharedTex.textureCount(); i2++) {
            Texture t2 = sharedTex.getTexture(i2);
            setTexture(t2.getSharedTexture(newMode), i2);
        }
        this.linearFiltering = sharedTex.linearFiltering;
        this.lastImageSerial = sharedTex.lastImageSerial;
    }

    @Override // com.sun.prism.Texture
    public Texture getSharedTexture(Texture.WrapMode altMode) {
        assertLocked();
        if (this.wrapMode == altMode) {
            lock();
            return this;
        }
        switch (altMode) {
            case REPEAT:
                if (this.wrapMode != Texture.WrapMode.CLAMP_TO_EDGE) {
                    return null;
                }
                break;
            case CLAMP_TO_EDGE:
                if (this.wrapMode != Texture.WrapMode.REPEAT) {
                    return null;
                }
                break;
            default:
                return null;
        }
        Texture altTex = new MultiTexture(this, altMode);
        altTex.lock();
        return altTex;
    }

    public int textureCount() {
        return this.textures.size();
    }

    public void setTexture(Texture tex, int index) {
        if (!tex.getWrapMode().isCompatibleWith(this.wrapMode)) {
            throw new IllegalArgumentException("texture wrap mode must match multi-texture mode");
        }
        if (this.textures.size() < index + 1) {
            for (int ii = this.textures.size(); ii < index; ii++) {
                this.textures.add(null);
            }
            this.textures.add(tex);
        } else {
            this.textures.set(index, tex);
        }
        tex.setLinearFiltering(this.linearFiltering);
    }

    public Texture getTexture(int index) {
        return this.textures.get(index);
    }

    public Texture[] getTextures() {
        return (Texture[]) this.textures.toArray(new Texture[this.textures.size()]);
    }

    public void removeTexture(Texture tex) {
        this.textures.remove(tex);
    }

    public void removeTexture(int index) {
        this.textures.remove(index);
    }

    @Override // com.sun.prism.Texture
    public PixelFormat getPixelFormat() {
        return this.format;
    }

    @Override // com.sun.prism.Texture
    public int getPhysicalWidth() {
        return this.width;
    }

    @Override // com.sun.prism.Texture
    public int getPhysicalHeight() {
        return this.height;
    }

    @Override // com.sun.prism.Texture
    public int getContentX() {
        return 0;
    }

    @Override // com.sun.prism.Texture
    public int getContentY() {
        return 0;
    }

    @Override // com.sun.prism.Texture
    public int getContentWidth() {
        return this.width;
    }

    @Override // com.sun.prism.Texture
    public int getContentHeight() {
        return this.height;
    }

    @Override // com.sun.prism.Texture
    public int getLastImageSerial() {
        return this.lastImageSerial;
    }

    @Override // com.sun.prism.Texture
    public void setLastImageSerial(int serial) {
        this.lastImageSerial = serial;
    }

    @Override // com.sun.prism.Texture
    public void update(Image img) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override // com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override // com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty, int srcw, int srch) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override // com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty, int srcw, int srch, boolean skipFlush) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override // com.sun.prism.Texture
    public void update(Buffer buffer, PixelFormat format, int dstx, int dsty, int srcx, int srcy, int srcw, int srch, int srcscan, boolean skipFlush) {
        throw new UnsupportedOperationException("Update from generic Buffer not supported");
    }

    @Override // com.sun.prism.Texture
    public void update(MediaFrame frame, boolean skipFlush) {
        if (frame.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            int encWidth = frame.getEncodedWidth();
            int encHeight = frame.getEncodedHeight();
            for (int index = 0; index < frame.planeCount(); index++) {
                Texture tex = this.textures.get(index);
                if (null != tex) {
                    int texWidth = encWidth;
                    int texHeight = encHeight;
                    if (index == 2 || index == 1) {
                        texWidth /= 2;
                        texHeight /= 2;
                    }
                    ByteBuffer pixels = frame.getBufferForPlane(index);
                    tex.update(pixels, PixelFormat.BYTE_ALPHA, 0, 0, 0, 0, texWidth, texHeight, frame.strideForPlane(index), skipFlush);
                }
            }
            return;
        }
        throw new IllegalArgumentException("Invalid pixel format in MediaFrame");
    }

    @Override // com.sun.prism.Texture
    public Texture.WrapMode getWrapMode() {
        return this.wrapMode;
    }

    @Override // com.sun.prism.Texture
    public boolean getUseMipmap() {
        return false;
    }

    @Override // com.sun.prism.Texture
    public boolean getLinearFiltering() {
        return this.linearFiltering;
    }

    @Override // com.sun.prism.Texture
    public void setLinearFiltering(boolean linear) {
        this.linearFiltering = linear;
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            tex.setLinearFiltering(linear);
        }
    }

    @Override // com.sun.prism.Texture
    public void lock() {
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            tex.lock();
        }
    }

    @Override // com.sun.prism.Texture
    public void unlock() {
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            tex.unlock();
        }
    }

    @Override // com.sun.prism.Texture
    public boolean isLocked() {
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            if (tex.isLocked()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.prism.Texture
    public int getLockCount() {
        int count = 0;
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            count = Math.max(count, tex.getLockCount());
        }
        return count;
    }

    @Override // com.sun.prism.Texture
    public void assertLocked() {
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            tex.assertLocked();
        }
    }

    @Override // com.sun.prism.Texture
    public void makePermanent() {
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            tex.makePermanent();
        }
    }

    @Override // com.sun.prism.Texture
    public void contentsUseful() {
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            tex.contentsUseful();
        }
    }

    @Override // com.sun.prism.Texture
    public void contentsNotUseful() {
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            tex.contentsNotUseful();
        }
    }

    @Override // com.sun.prism.Texture
    public boolean isSurfaceLost() {
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            if (tex.isSurfaceLost()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.prism.GraphicsResource
    public void dispose() {
        Iterator<Texture> it = this.textures.iterator();
        while (it.hasNext()) {
            Texture tex = it.next();
            tex.dispose();
        }
        this.textures.clear();
    }

    @Override // com.sun.prism.Texture
    public int getMaxContentWidth() {
        return getPhysicalWidth();
    }

    @Override // com.sun.prism.Texture
    public int getMaxContentHeight() {
        return getPhysicalHeight();
    }

    @Override // com.sun.prism.Texture
    public void setContentWidth(int contentWidth) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // com.sun.prism.Texture
    public void setContentHeight(int contentHeight) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
