package com.sun.prism;

import com.sun.prism.Texture;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.shape.ShapeRep;

/* loaded from: jfxrt.jar:com/sun/prism/ResourceFactory.class */
public interface ResourceFactory extends GraphicsResource {
    boolean isDisposed();

    boolean isDeviceReady();

    TextureResourcePool getTextureResourcePool();

    Texture createTexture(Image image, Texture.Usage usage, Texture.WrapMode wrapMode);

    Texture createTexture(Image image, Texture.Usage usage, Texture.WrapMode wrapMode, boolean z2);

    Texture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int i2, int i3);

    Texture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int i2, int i3, boolean z2);

    Texture createTexture(MediaFrame mediaFrame);

    Texture getCachedTexture(Image image, Texture.WrapMode wrapMode);

    Texture getCachedTexture(Image image, Texture.WrapMode wrapMode, boolean z2);

    boolean isFormatSupported(PixelFormat pixelFormat);

    boolean isWrapModeSupported(Texture.WrapMode wrapMode);

    int getMaximumTextureSize();

    int getRTTWidth(int i2, Texture.WrapMode wrapMode);

    int getRTTHeight(int i2, Texture.WrapMode wrapMode);

    Texture createMaskTexture(int i2, int i3, Texture.WrapMode wrapMode);

    Texture createFloatTexture(int i2, int i3);

    RTTexture createRTTexture(int i2, int i3, Texture.WrapMode wrapMode);

    RTTexture createRTTexture(int i2, int i3, Texture.WrapMode wrapMode, boolean z2);

    boolean isCompatibleTexture(Texture texture);

    Presentable createPresentable(PresentableState presentableState);

    ShapeRep createPathRep();

    ShapeRep createRoundRectRep();

    ShapeRep createEllipseRep();

    ShapeRep createArcRep();

    void addFactoryListener(ResourceFactoryListener resourceFactoryListener);

    void removeFactoryListener(ResourceFactoryListener resourceFactoryListener);

    void setRegionTexture(Texture texture);

    Texture getRegionTexture();

    void setGlyphTexture(Texture texture);

    Texture getGlyphTexture();

    boolean isSuperShaderAllowed();

    PhongMaterial createPhongMaterial();

    MeshView createMeshView(Mesh mesh);

    Mesh createMesh();
}
