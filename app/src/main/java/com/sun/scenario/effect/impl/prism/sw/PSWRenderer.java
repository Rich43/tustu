package com.sun.scenario.effect.impl.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrImage;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.sw.RendererDelegate;
import java.lang.reflect.Constructor;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/sw/PSWRenderer.class */
public class PSWRenderer extends PrRenderer {
    private final Screen screen;
    private final ResourceFactory resourceFactory;
    private final RendererDelegate delegate;
    private Renderer.RendererState state;

    private PSWRenderer(Screen screen, RendererDelegate delegate) {
        this.screen = screen;
        this.resourceFactory = null;
        this.delegate = delegate;
        synchronized (this) {
            this.state = Renderer.RendererState.OK;
        }
    }

    private PSWRenderer(ResourceFactory factory, RendererDelegate delegate) {
        this.screen = null;
        this.resourceFactory = factory;
        this.delegate = delegate;
        synchronized (this) {
            this.state = Renderer.RendererState.OK;
        }
    }

    @Override // com.sun.scenario.effect.impl.prism.PrRenderer
    public PrDrawable createDrawable(RTTexture rtt) {
        return PSWDrawable.create(rtt);
    }

    public static synchronized PSWRenderer createJSWInstance(Screen screen) {
        PSWRenderer ret = null;
        try {
            Class klass = Class.forName("com.sun.scenario.effect.impl.sw.java.JSWRendererDelegate");
            RendererDelegate delegate = (RendererDelegate) klass.newInstance();
            ret = new PSWRenderer(screen, delegate);
        } catch (Throwable th) {
        }
        return ret;
    }

    public static synchronized PSWRenderer createJSWInstance(ResourceFactory factory) {
        PSWRenderer ret = null;
        try {
            Class klass = Class.forName("com.sun.scenario.effect.impl.sw.java.JSWRendererDelegate");
            RendererDelegate delegate = (RendererDelegate) klass.newInstance();
            ret = new PSWRenderer(factory, delegate);
        } catch (Throwable th) {
        }
        return ret;
    }

    public static synchronized PSWRenderer createJSWInstance(FilterContext fctx) {
        PSWRenderer ret = null;
        try {
            ResourceFactory factory = (ResourceFactory) fctx.getReferent();
            ret = createJSWInstance(factory);
        } catch (Throwable th) {
        }
        return ret;
    }

    private static synchronized PSWRenderer createSSEInstance(Screen screen) {
        PSWRenderer ret = null;
        try {
            Class klass = Class.forName("com.sun.scenario.effect.impl.sw.sse.SSERendererDelegate");
            RendererDelegate delegate = (RendererDelegate) klass.newInstance();
            ret = new PSWRenderer(screen, delegate);
        } catch (Throwable th) {
        }
        return ret;
    }

    public static Renderer createRenderer(FilterContext fctx) {
        Object ref = fctx.getReferent();
        GraphicsPipeline pipe = GraphicsPipeline.getPipeline();
        if (pipe == null || !(ref instanceof Screen)) {
            return null;
        }
        Screen screen = (Screen) ref;
        Renderer renderer = createSSEInstance(screen);
        if (renderer == null) {
            renderer = createJSWInstance(screen);
        }
        return renderer;
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public Effect.AccelType getAccelType() {
        return this.delegate.getAccelType();
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
        synchronized (this) {
            this.state = Renderer.RendererState.DISPOSED;
        }
    }

    protected final synchronized void markLost() {
        if (this.state == Renderer.RendererState.OK) {
            this.state = Renderer.RendererState.LOST;
        }
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public int getCompatibleWidth(int w2) {
        if (this.screen != null) {
            return PSWDrawable.getCompatibleWidth(this.screen, w2);
        }
        return this.resourceFactory.getRTTWidth(w2, Texture.WrapMode.CLAMP_TO_EDGE);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public int getCompatibleHeight(int h2) {
        if (this.screen != null) {
            return PSWDrawable.getCompatibleHeight(this.screen, h2);
        }
        return this.resourceFactory.getRTTHeight(h2, Texture.WrapMode.CLAMP_TO_EDGE);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public final PSWDrawable createCompatibleImage(int w2, int h2) {
        if (this.screen != null) {
            return PSWDrawable.create(this.screen, w2, h2);
        }
        RTTexture rtt = this.resourceFactory.createRTTexture(w2, h2, Texture.WrapMode.CLAMP_TO_EDGE);
        return PSWDrawable.create(rtt);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public PSWDrawable getCompatibleImage(int w2, int h2) {
        PSWDrawable im = (PSWDrawable) super.getCompatibleImage(w2, h2);
        if (im == null) {
            markLost();
        }
        return im;
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
        String klassName = this.delegate.getPlatformPeerName(name, unrollCount);
        try {
            Class klass = Class.forName(klassName);
            Constructor ctor = klass.getConstructor(FilterContext.class, Renderer.class, String.class);
            EffectPeer peer = (EffectPeer) ctor.newInstance(fctx, this, name);
            return peer;
        } catch (Exception e2) {
            System.err.println("Error: " + ((Object) getAccelType()) + " peer not found for: " + name + " due to error: " + e2.getMessage());
            return null;
        }
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    protected EffectPeer createPeer(FilterContext fctx, String name, int unrollCount) {
        if (PrRenderer.isIntrinsicPeer(name)) {
            return createIntrinsicPeer(fctx, name);
        }
        return createPlatformPeer(fctx, name, unrollCount);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public boolean isImageDataCompatible(ImageData id) {
        return getRendererState() == Renderer.RendererState.OK && (id.getUntransformedImage() instanceof PSWDrawable);
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public void clearImage(Filterable filterable) {
        PSWDrawable img = (PSWDrawable) filterable;
        img.clear();
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public ImageData createImageData(FilterContext fctx, Filterable src) {
        if (!(src instanceof PrImage)) {
            throw new IllegalArgumentException("Identity source must be PrImage");
        }
        Image img = ((PrImage) src).getImage();
        int w2 = img.getWidth();
        int h2 = img.getHeight();
        PSWDrawable dst = createCompatibleImage(w2, h2);
        if (dst == null) {
            return null;
        }
        Graphics g2 = dst.createGraphics();
        ResourceFactory factory = g2.getResourceFactory();
        Texture tex = factory.createTexture(img, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
        g2.drawTexture(tex, 0.0f, 0.0f, w2, h2);
        g2.sync();
        tex.dispose();
        return new ImageData(fctx, dst, new Rectangle(w2, h2));
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public Filterable transform(FilterContext fctx, Filterable original, BaseTransform transform, Rectangle origBounds, Rectangle xformBounds) {
        PSWDrawable dst = getCompatibleImage(xformBounds.width, xformBounds.height);
        if (dst != null) {
            Graphics g2 = dst.createGraphics();
            g2.translate(-xformBounds.f11913x, -xformBounds.f11914y);
            g2.transform(transform);
            g2.drawTexture(((PSWDrawable) original).getTextureObject(), origBounds.f11913x, origBounds.f11914y, origBounds.width, origBounds.height);
        }
        return dst;
    }

    @Override // com.sun.scenario.effect.impl.Renderer
    public ImageData transform(FilterContext fctx, ImageData original, BaseTransform transform, Rectangle origBounds, Rectangle xformBounds) {
        PSWDrawable dst = getCompatibleImage(xformBounds.width, xformBounds.height);
        if (dst != null) {
            PSWDrawable orig = (PSWDrawable) original.getUntransformedImage();
            Graphics g2 = dst.createGraphics();
            g2.translate(-xformBounds.f11913x, -xformBounds.f11914y);
            g2.transform(transform);
            g2.drawTexture(orig.getTextureObject(), origBounds.f11913x, origBounds.f11914y, origBounds.width, origBounds.height);
        }
        original.unref();
        return new ImageData(fctx, dst, xformBounds);
    }
}
