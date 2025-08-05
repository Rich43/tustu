package com.sun.scenario.effect.impl.prism.ps;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.LockableResource;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.hw.ShaderSource;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrImage;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.FloatBuffer;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSRenderer.class */
public class PPSRenderer extends PrRenderer {
    private ResourceFactory rf;
    private Screen screen;
    private final ShaderSource shaderSource;
    private Renderer.RendererState state;
    private boolean needsSWDispMap;
    private final ResourceFactoryListener listener = new ResourceFactoryListener() { // from class: com.sun.scenario.effect.impl.prism.ps.PPSRenderer.1
        @Override // com.sun.prism.ResourceFactoryListener
        public void factoryReset() {
            PPSRenderer.this.dispose();
        }

        @Override // com.sun.prism.ResourceFactoryListener
        public void factoryReleased() {
            PPSRenderer.this.dispose();
        }
    };

    private PPSRenderer(Screen screen, ShaderSource shaderSource) {
        this.shaderSource = shaderSource;
        this.screen = screen;
        synchronized (this) {
            this.state = Renderer.RendererState.NOTREADY;
        }
    }

    private boolean validate() {
        Renderer.RendererState st = getRendererState();
        switch (st) {
            case NOTREADY:
                if (this.rf == null) {
                    this.rf = GraphicsPipeline.getPipeline().getResourceFactory(this.screen);
                    if (this.rf == null) {
                        return false;
                    }
                }
                if (this.rf.isDisposed()) {
                    dispose();
                    return false;
                }
                this.rf.addFactoryListener(this.listener);
                this.needsSWDispMap = !this.rf.isFormatSupported(PixelFormat.FLOAT_XYZW);
                synchronized (this) {
                    this.state = Renderer.RendererState.OK;
                }
                return true;
            case OK:
            case LOST:
                return true;
            case DISPOSED:
            default:
                return false;
        }
    }

    @Override // com.sun.scenario.effect.impl.prism.PrRenderer
    public PrDrawable createDrawable(RTTexture rtt) {
        if (!validate()) {
            return null;
        }
        return PPSDrawable.create(rtt);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public Effect.AccelType getAccelType() {
        return this.shaderSource.getAccelType();
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public synchronized Renderer.RendererState getRendererState() {
        return this.state;
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    protected Renderer getBackupRenderer() {
        return this;
    }

    protected void dispose() {
        for (EffectPeer peer : getPeers()) {
            peer.dispose();
        }
        synchronized (this) {
            this.state = Renderer.RendererState.DISPOSED;
        }
        this.rf.removeFactoryListener(this.listener);
        this.rf = null;
        this.screen = null;
    }

    protected final synchronized void markLost() {
        if (this.state == Renderer.RendererState.NOTREADY || this.state == Renderer.RendererState.OK) {
            this.state = Renderer.RendererState.LOST;
        }
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public int getCompatibleWidth(int w2) {
        if (!validate()) {
            return -1;
        }
        return PPSDrawable.getCompatibleWidth(this.rf, w2);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public int getCompatibleHeight(int h2) {
        if (!validate()) {
            return -1;
        }
        return PPSDrawable.getCompatibleHeight(this.rf, h2);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public final PPSDrawable createCompatibleImage(int w2, int h2) {
        if (!validate()) {
            return null;
        }
        return PPSDrawable.create(this.rf, w2, h2);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public PPSDrawable getCompatibleImage(int w2, int h2) {
        if (!validate()) {
            return null;
        }
        PPSDrawable im = (PPSDrawable) super.getCompatibleImage(w2, h2);
        if (im == null) {
            markLost();
        }
        return im;
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public LockableResource createFloatTexture(int w2, int h2) {
        if (!validate()) {
            return null;
        }
        Texture prismTex = this.rf.createFloatTexture(w2, h2);
        return new PrTexture(prismTex);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public void updateFloatTexture(LockableResource texture, FloatMap map) {
        if (!validate()) {
            return;
        }
        FloatBuffer buf = map.getBuffer();
        int w2 = map.getWidth();
        int h2 = map.getHeight();
        Image img = Image.fromFloatMapData(buf, w2, h2);
        Texture prismTex = ((PrTexture) texture).getTextureObject();
        prismTex.update(img);
    }

    public Shader createShader(String name, Map<String, Integer> samplers, Map<String, Integer> params, boolean isPixcoordUsed) {
        if (!validate()) {
            return null;
        }
        if (PrismSettings.verbose) {
            System.out.println("PPSRenderer: scenario.effect - createShader: " + name);
        }
        InputStream pscode = this.shaderSource.loadSource(name);
        int maxTexCoordIndex = samplers.keySet().size() - 1;
        ShaderFactory factory = (ShaderFactory) this.rf;
        return factory.createShader(pscode, samplers, params, maxTexCoordIndex, isPixcoordUsed, false);
    }

    private EffectPeer createIntrinsicPeer(FilterContext fctx, String name) {
        try {
            Class klass = Class.forName("com.sun.scenario.effect.impl.prism.Pr" + name + "Peer");
            Constructor ctor = klass.getConstructor(FilterContext.class, Renderer.class, String.class);
            EffectPeer peer = (EffectPeer) ctor.newInstance(fctx, this, name);
            return peer;
        } catch (Exception e2) {
            return null;
        }
    }

    private EffectPeer createPlatformPeer(FilterContext fctx, String name, int unrollCount) {
        String shaderName = name;
        if (unrollCount > 0) {
            shaderName = shaderName + "_" + unrollCount;
        }
        try {
            Class klass = Class.forName("com.sun.scenario.effect.impl.prism.ps.PPS" + name + "Peer");
            Constructor ctor = klass.getConstructor(FilterContext.class, Renderer.class, String.class);
            EffectPeer peer = (EffectPeer) ctor.newInstance(fctx, this, shaderName);
            return peer;
        } catch (Exception e2) {
            System.err.println("Error: Prism peer not found for: " + name + " due to error: " + e2.getMessage());
            return null;
        }
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    protected EffectPeer createPeer(FilterContext fctx, String name, int unrollCount) {
        if (PrRenderer.isIntrinsicPeer(name)) {
            return createIntrinsicPeer(fctx, name);
        }
        if (this.needsSWDispMap && name.equals("DisplacementMap")) {
            PrFilterContext swctx = ((PrFilterContext) fctx).getSoftwareInstance();
            return new PPStoPSWDisplacementMapPeer(swctx, this, name);
        }
        return createPlatformPeer(fctx, name, unrollCount);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public boolean isImageDataCompatible(ImageData id) {
        if (getRendererState() == Renderer.RendererState.OK) {
            Filterable f2 = id.getUntransformedImage();
            return (f2 instanceof PrDrawable) && !f2.isLost();
        }
        return false;
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public void clearImage(Filterable filterable) {
        PPSDrawable img = (PPSDrawable) filterable;
        img.clear();
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public ImageData createImageData(FilterContext fctx, Filterable src) {
        BaseTransform tx;
        if (!validate()) {
            return null;
        }
        if (!(src instanceof PrImage)) {
            throw new IllegalArgumentException("Identity source must be PrImage");
        }
        Image img = ((PrImage) src).getImage();
        int w2 = img.getWidth();
        int h2 = img.getHeight();
        PPSDrawable dst = createCompatibleImage(w2, h2);
        if (dst == null) {
            return null;
        }
        Graphics g2 = dst.createGraphics();
        ResourceFactory factory = g2.getResourceFactory();
        Texture tex = factory.createTexture(img, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
        g2.drawTexture(tex, 0.0f, 0.0f, w2, h2);
        g2.sync();
        tex.dispose();
        float ps = img.getPixelScale();
        if (ps != 1.0f) {
            float ps2 = 1.0f / ps;
            tx = BaseTransform.getScaleInstance(ps2, ps2);
        } else {
            tx = BaseTransform.IDENTITY_TRANSFORM;
        }
        ImageData id = new ImageData(fctx, dst, new Rectangle(w2, h2), tx);
        return id;
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public Filterable transform(FilterContext fctx, Filterable original, BaseTransform transform, Rectangle origBounds, Rectangle xformBounds) {
        if (!validate()) {
            return null;
        }
        PPSDrawable dst = getCompatibleImage(xformBounds.width, xformBounds.height);
        if (dst != null) {
            Graphics g2 = dst.createGraphics();
            g2.translate(-xformBounds.f11913x, -xformBounds.f11914y);
            g2.transform(transform);
            g2.drawTexture(((PPSDrawable) original).getTextureObject(), origBounds.f11913x, origBounds.f11914y, origBounds.width, origBounds.height);
        }
        return dst;
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public ImageData transform(FilterContext fctx, ImageData original, BaseTransform transform, Rectangle origBounds, Rectangle xformBounds) {
        if (!validate()) {
            return null;
        }
        PPSDrawable dst = getCompatibleImage(xformBounds.width, xformBounds.height);
        if (dst != null) {
            PPSDrawable orig = (PPSDrawable) original.getUntransformedImage();
            Graphics g2 = dst.createGraphics();
            g2.translate(-xformBounds.f11913x, -xformBounds.f11914y);
            g2.transform(transform);
            g2.drawTexture(orig.getTextureObject(), origBounds.f11913x, origBounds.f11914y, origBounds.width, origBounds.height);
        }
        original.unref();
        return new ImageData(fctx, dst, xformBounds);
    }

    private static ShaderSource createShaderSource(String name) {
        try {
            Class klass = Class.forName(name);
            return (ShaderSource) klass.newInstance();
        } catch (ClassNotFoundException e2) {
            System.err.println(name + " class not found");
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    public static Renderer createRenderer(FilterContext fctx) {
        ShaderSource shaderSource;
        Object ref = fctx.getReferent();
        GraphicsPipeline pipe = GraphicsPipeline.getPipeline();
        if (pipe == null || !(ref instanceof Screen)) {
            return null;
        }
        Screen screen = (Screen) ref;
        if (pipe.supportsShader(GraphicsPipeline.ShaderType.HLSL, GraphicsPipeline.ShaderModel.SM3)) {
            shaderSource = createShaderSource("com.sun.scenario.effect.impl.hw.d3d.D3DShaderSource");
        } else if (pipe.supportsShader(GraphicsPipeline.ShaderType.GLSL, GraphicsPipeline.ShaderModel.SM3)) {
            shaderSource = createShaderSource("com.sun.scenario.effect.impl.es2.ES2ShaderSource");
        } else {
            throw new InternalError("Unknown GraphicsPipeline");
        }
        if (shaderSource == null) {
            return null;
        }
        return new PPSRenderer(screen, shaderSource);
    }
}
