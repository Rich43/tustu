package com.sun.prism.sw;

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
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.shape.BasicRoundRectRep;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.shape.ShapeRep;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWResourceFactory.class */
final class SWResourceFactory extends BaseResourceFactory implements ResourceFactory {
    private static final Map<Image, Texture> clampTexCache = new WeakHashMap();
    private static final Map<Image, Texture> repeatTexCache = new WeakHashMap();
    private static final Map<Image, Texture> mipmapTexCache = new WeakHashMap();
    private static final ShapeRep theRep = new BasicShapeRep();
    private static final ShapeRep rectRep = new BasicRoundRectRep();
    private Screen screen;
    private final SWContext context;

    public SWResourceFactory(Screen screen) {
        super(clampTexCache, repeatTexCache, mipmapTexCache);
        this.screen = screen;
        this.context = new SWContext(this);
    }

    @Override // com.sun.prism.ResourceFactory
    public TextureResourcePool getTextureResourcePool() {
        return SWTexturePool.instance;
    }

    public Screen getScreen() {
        return this.screen;
    }

    SWContext getContext() {
        return this.context;
    }

    @Override // com.sun.prism.impl.BaseResourceFactory, com.sun.prism.GraphicsResource
    public void dispose() {
        this.context.dispose();
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
        return rectRep;
    }

    @Override // com.sun.prism.ResourceFactory
    public ShapeRep createPathRep() {
        return theRep;
    }

    @Override // com.sun.prism.ResourceFactory
    public Presentable createPresentable(PresentableState pState) {
        if (PrismSettings.debug) {
            System.out.println("+ SWRF.createPresentable()");
        }
        return new SWPresentable(pState, this);
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
    public boolean isCompatibleTexture(Texture tex) {
        return tex instanceof SWTexture;
    }

    @Override // com.sun.prism.ResourceFactory
    public RTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode, boolean msaa) {
        return createRTTexture(width, height, wrapMode);
    }

    @Override // com.sun.prism.ResourceFactory
    public RTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode) {
        SWTexturePool pool = SWTexturePool.instance;
        long size = pool.estimateRTTextureSize(width, height, false);
        if (!pool.prepareForAllocation(size)) {
            return null;
        }
        return new SWRTTexture(this, width, height);
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

    @Override // com.sun.prism.impl.BaseResourceFactory
    protected boolean canClampToZero() {
        return false;
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createTexture(MediaFrame vdb) {
        return new SWArgbPreTexture(this, Texture.WrapMode.CLAMP_TO_EDGE, vdb.getWidth(), vdb.getHeight());
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createTexture(PixelFormat formatHint, Texture.Usage usageHint, Texture.WrapMode wrapMode, int w2, int h2) {
        SWTexturePool pool = SWTexturePool.instance;
        long size = pool.estimateTextureSize(w2, h2, formatHint);
        if (!pool.prepareForAllocation(size)) {
            return null;
        }
        return SWTexture.create(this, formatHint, wrapMode, w2, h2);
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createTexture(PixelFormat formatHint, Texture.Usage usageHint, Texture.WrapMode wrapMode, int w2, int h2, boolean useMipmap) {
        return createTexture(formatHint, usageHint, wrapMode, w2, h2);
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
