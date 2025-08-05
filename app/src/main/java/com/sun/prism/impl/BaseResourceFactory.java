package com.sun.prism.impl;

import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: jfxrt.jar:com/sun/prism/impl/BaseResourceFactory.class */
public abstract class BaseResourceFactory implements ResourceFactory {
    private final Map<Image, Texture> clampTexCache;
    private final Map<Image, Texture> repeatTexCache;
    private final Map<Image, Texture> mipmapTexCache;
    private final WeakHashMap<ResourceFactoryListener, Boolean> listenerMap;
    private boolean disposed;
    private Texture regionTexture;
    private Texture glyphTexture;
    private boolean superShaderAllowed;

    public BaseResourceFactory() {
        this(new WeakHashMap(), new WeakHashMap(), new WeakHashMap());
    }

    public BaseResourceFactory(Map<Image, Texture> clampTexCache, Map<Image, Texture> repeatTexCache, Map<Image, Texture> mipmapTexCache) {
        this.listenerMap = new WeakHashMap<>();
        this.disposed = false;
        this.clampTexCache = clampTexCache;
        this.repeatTexCache = repeatTexCache;
        this.mipmapTexCache = mipmapTexCache;
    }

    @Override // com.sun.prism.ResourceFactory
    public void addFactoryListener(ResourceFactoryListener l2) {
        this.listenerMap.put(l2, Boolean.TRUE);
    }

    @Override // com.sun.prism.ResourceFactory
    public void removeFactoryListener(ResourceFactoryListener l2) {
        this.listenerMap.remove(l2);
    }

    @Override // com.sun.prism.ResourceFactory
    public boolean isDeviceReady() {
        return !isDisposed();
    }

    protected void clearTextureCache() {
        clearTextureCache(this.clampTexCache);
        clearTextureCache(this.repeatTexCache);
        clearTextureCache(this.mipmapTexCache);
    }

    protected void clearTextureCache(Map<Image, Texture> texCache) {
        Collection<Texture> texAll = texCache.values();
        for (Texture i2 : texAll) {
            i2.dispose();
        }
        texCache.clear();
    }

    protected ResourceFactoryListener[] getFactoryListeners() {
        return (ResourceFactoryListener[]) this.listenerMap.keySet().toArray(new ResourceFactoryListener[0]);
    }

    private void disposeResources() {
        this.clampTexCache.clear();
        this.repeatTexCache.clear();
        this.mipmapTexCache.clear();
        if (this.regionTexture != null) {
            this.regionTexture.dispose();
            this.regionTexture = null;
        }
        if (this.glyphTexture != null) {
            this.glyphTexture.dispose();
            this.glyphTexture = null;
        }
    }

    protected void notifyReset() {
        disposeResources();
        ResourceFactoryListener[] notifyList = getFactoryListeners();
        for (ResourceFactoryListener listener : notifyList) {
            if (null != listener) {
                listener.factoryReset();
            }
        }
    }

    @Override // com.sun.prism.GraphicsResource
    public void dispose() {
        disposeResources();
        this.disposed = true;
        ResourceFactoryListener[] notifyList = getFactoryListeners();
        for (ResourceFactoryListener listener : notifyList) {
            if (null != listener) {
                listener.factoryReleased();
            }
        }
    }

    static long sizeWithMipMap(int w2, int h2, PixelFormat format) {
        long size = 0;
        int bytesPerPixel = format.getBytesPerPixelUnit();
        while (w2 > 1 && h2 > 1) {
            size += w2 * h2;
            w2 = (w2 + 1) >> 1;
            h2 = (h2 + 1) >> 1;
        }
        return (size + 1) * bytesPerPixel;
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture getCachedTexture(Image image, Texture.WrapMode wrapMode) {
        if (checkDisposed()) {
            return null;
        }
        return getCachedTexture(image, wrapMode, false);
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture getCachedTexture(Image image, Texture.WrapMode wrapMode, boolean useMipmap) {
        Map<Image, Texture> texCache;
        if (checkDisposed()) {
            return null;
        }
        if (image == null) {
            throw new IllegalArgumentException("Image must be non-null");
        }
        if (wrapMode == Texture.WrapMode.CLAMP_TO_EDGE) {
            if (useMipmap) {
                throw new IllegalArgumentException("Mipmap not supported with CLAMP mode: useMipmap = " + useMipmap + ", wrapMode = " + ((Object) wrapMode));
            }
            texCache = this.clampTexCache;
        } else if (wrapMode == Texture.WrapMode.REPEAT) {
            texCache = useMipmap ? this.mipmapTexCache : this.repeatTexCache;
        } else {
            throw new IllegalArgumentException("no caching for " + ((Object) wrapMode));
        }
        Texture tex = texCache.get(image);
        if (tex != null) {
            tex.lock();
            if (tex.isSurfaceLost()) {
                texCache.remove(image);
                tex = null;
            }
        }
        int serial = image.getSerial();
        if (!useMipmap && tex == null) {
            Texture othertex = (wrapMode == Texture.WrapMode.REPEAT ? this.clampTexCache : this.repeatTexCache).get(image);
            if (othertex != null) {
                othertex.lock();
                if (!othertex.isSurfaceLost()) {
                    tex = othertex.getSharedTexture(wrapMode);
                    if (tex != null) {
                        tex.contentsUseful();
                        texCache.put(image, tex);
                    }
                }
                othertex.unlock();
            }
        }
        if (tex == null) {
            int w2 = image.getWidth();
            int h2 = image.getHeight();
            TextureResourcePool pool = getTextureResourcePool();
            long size = useMipmap ? sizeWithMipMap(w2, h2, image.getPixelFormat()) : pool.estimateTextureSize(w2, h2, image.getPixelFormat());
            if (!pool.prepareForAllocation(size)) {
                return null;
            }
            tex = createTexture(image, Texture.Usage.DEFAULT, wrapMode, useMipmap);
            if (tex != null) {
                tex.setLastImageSerial(serial);
                texCache.put(image, tex);
            }
        } else if (tex.getLastImageSerial() != serial) {
            tex.update(image, 0, 0, image.getWidth(), image.getHeight(), false);
            tex.setLastImageSerial(serial);
        }
        return tex;
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createTexture(Image image, Texture.Usage usageHint, Texture.WrapMode wrapMode) {
        if (checkDisposed()) {
            return null;
        }
        return createTexture(image, usageHint, wrapMode, false);
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createTexture(Image image, Texture.Usage usageHint, Texture.WrapMode wrapMode, boolean useMipmap) {
        if (checkDisposed()) {
            return null;
        }
        PixelFormat format = image.getPixelFormat();
        int w2 = image.getWidth();
        int h2 = image.getHeight();
        Texture tex = createTexture(format, usageHint, wrapMode, w2, h2, useMipmap);
        if (tex != null) {
            tex.update(image, 0, 0, w2, h2, true);
            tex.contentsUseful();
        }
        return tex;
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createMaskTexture(int width, int height, Texture.WrapMode wrapMode) {
        return createTexture(PixelFormat.BYTE_ALPHA, Texture.Usage.DEFAULT, wrapMode, width, height);
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createFloatTexture(int width, int height) {
        return createTexture(PixelFormat.FLOAT_XYZW, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_ZERO, width, height);
    }

    @Override // com.sun.prism.ResourceFactory
    public void setRegionTexture(Texture texture) {
        if (checkDisposed()) {
            return;
        }
        this.regionTexture = texture;
        this.superShaderAllowed = (!PrismSettings.superShader || this.regionTexture == null || this.glyphTexture == null) ? false : true;
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture getRegionTexture() {
        return this.regionTexture;
    }

    @Override // com.sun.prism.ResourceFactory
    public void setGlyphTexture(Texture texture) {
        if (checkDisposed()) {
            return;
        }
        this.glyphTexture = texture;
        this.superShaderAllowed = (!PrismSettings.superShader || this.regionTexture == null || this.glyphTexture == null) ? false : true;
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture getGlyphTexture() {
        return this.glyphTexture;
    }

    @Override // com.sun.prism.ResourceFactory
    public boolean isSuperShaderAllowed() {
        return this.superShaderAllowed;
    }

    protected boolean canClampToZero() {
        return true;
    }

    protected boolean canClampToEdge() {
        return true;
    }

    protected boolean canRepeat() {
        return true;
    }

    @Override // com.sun.prism.ResourceFactory
    public boolean isWrapModeSupported(Texture.WrapMode mode) {
        switch (mode) {
            case CLAMP_NOT_NEEDED:
                return true;
            case CLAMP_TO_EDGE:
                return canClampToEdge();
            case REPEAT:
                return canRepeat();
            case CLAMP_TO_ZERO:
                return canClampToZero();
            case CLAMP_TO_EDGE_SIMULATED:
            case CLAMP_TO_ZERO_SIMULATED:
            case REPEAT_SIMULATED:
                throw new InternalError("Cannot test support for simulated wrap modes");
            default:
                throw new InternalError("Unrecognized wrap mode: " + ((Object) mode));
        }
    }

    @Override // com.sun.prism.ResourceFactory
    public boolean isDisposed() {
        return this.disposed;
    }

    protected boolean checkDisposed() {
        if (PrismSettings.verbose && isDisposed()) {
            try {
                throw new IllegalStateException("attempt to use resource after factory is disposed");
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
        return isDisposed();
    }
}
