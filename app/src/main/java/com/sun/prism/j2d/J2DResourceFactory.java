package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.shape.ShapeRep;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DResourceFactory.class */
class J2DResourceFactory extends BaseResourceFactory {
    private Screen screen;
    private static final Map<Image, Texture> clampTexCache = new WeakHashMap();
    private static final Map<Image, Texture> repeatTexCache = new WeakHashMap();
    private static final Map<Image, Texture> mipmapTexCache = new WeakHashMap();
    private static ShapeRep theRep = new BasicShapeRep();

    J2DResourceFactory(Screen screen) {
        super(clampTexCache, repeatTexCache, mipmapTexCache);
        this.screen = screen;
    }

    J2DPrismGraphics createJ2DPrismGraphics(J2DPresentable target, Graphics2D g2d) {
        return new J2DPrismGraphics(target, g2d);
    }

    @Override // com.sun.prism.ResourceFactory
    public TextureResourcePool getTextureResourcePool() {
        return J2DTexturePool.instance;
    }

    Screen getScreen() {
        return this.screen;
    }

    @Override // com.sun.prism.ResourceFactory
    public ShapeRep createArcRep() {
        return theRep;
    }

    @Override // com.sun.prism.ResourceFactory
    public ShapeRep createEllipseRep() {
        return theRep;
    }

    @Override // com.sun.prism.ResourceFactory
    public ShapeRep createRoundRectRep() {
        return theRep;
    }

    @Override // com.sun.prism.ResourceFactory
    public ShapeRep createPathRep() {
        return theRep;
    }

    @Override // com.sun.prism.ResourceFactory
    public Presentable createPresentable(PresentableState pState) {
        return J2DPresentable.create(pState, this);
    }

    @Override // com.sun.prism.ResourceFactory
    public int getRTTWidth(int w2, Texture.WrapMode wrapMode) {
        return w2;
    }

    @Override // com.sun.prism.ResourceFactory
    public int getRTTHeight(int h2, Texture.WrapMode wrapMode) {
        return h2;
    }

    @Override // com.sun.prism.ResourceFactory
    public RTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode, boolean msaa) {
        return createRTTexture(width, height, wrapMode);
    }

    @Override // com.sun.prism.ResourceFactory
    public RTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode) {
        J2DTexturePool pool = J2DTexturePool.instance;
        long size = pool.estimateRTTextureSize(width, height, false);
        if (!pool.prepareForAllocation(size)) {
            return null;
        }
        return new J2DRTTexture(width, height, this);
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createTexture(PixelFormat formatHint, Texture.Usage usageHint, Texture.WrapMode wrapMode, int w2, int h2) {
        return J2DTexture.create(formatHint, wrapMode, w2, h2);
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createTexture(PixelFormat formatHint, Texture.Usage usageHint, Texture.WrapMode wrapMode, int w2, int h2, boolean useMipmap) {
        return createTexture(formatHint, usageHint, wrapMode, w2, h2);
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createTexture(MediaFrame vdb) {
        vdb.holdFrame();
        if (vdb.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
            MediaFrame newFrame = vdb.convertToFormat(PixelFormat.INT_ARGB_PRE);
            vdb.releaseFrame();
            vdb = newFrame;
            if (null == vdb) {
                return null;
            }
        }
        Texture tex = J2DTexture.create(vdb.getPixelFormat(), Texture.WrapMode.CLAMP_TO_EDGE, vdb.getWidth(), vdb.getHeight());
        vdb.releaseFrame();
        return tex;
    }

    @Override // com.sun.prism.ResourceFactory
    public boolean isCompatibleTexture(Texture tex) {
        return tex instanceof J2DTexture;
    }

    @Override // com.sun.prism.impl.BaseResourceFactory
    protected boolean canClampToZero() {
        return false;
    }

    @Override // com.sun.prism.ResourceFactory
    public int getMaximumTextureSize() {
        return Integer.MAX_VALUE;
    }

    @Override // com.sun.prism.ResourceFactory
    public boolean isFormatSupported(PixelFormat format) {
        switch (format) {
            case BYTE_RGB:
            case BYTE_GRAY:
            case INT_ARGB_PRE:
            case BYTE_BGRA_PRE:
                return true;
            case BYTE_ALPHA:
            case BYTE_APPLE_422:
            case MULTI_YCbCr_420:
            case FLOAT_XYZW:
            default:
                return false;
        }
    }

    @Override // com.sun.prism.impl.BaseResourceFactory, com.sun.prism.GraphicsResource
    public void dispose() {
    }

    @Override // com.sun.prism.ResourceFactory
    public PhongMaterial createPhongMaterial() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.prism.ResourceFactory
    public MeshView createMeshView(Mesh mesh) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.prism.ResourceFactory
    public Mesh createMesh() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
