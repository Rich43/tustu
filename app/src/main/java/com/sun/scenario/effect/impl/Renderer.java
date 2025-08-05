package com.sun.scenario.effect.impl;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.LockableResource;
import java.security.AccessController;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/Renderer.class */
public abstract class Renderer {
    public static final String rootPkg = "com.sun.scenario.effect";
    private Map<String, EffectPeer> peerCache = Collections.synchronizedMap(new HashMap(5));
    private final ImagePool imagePool = new ImagePool();
    private static final Map<FilterContext, Renderer> rendererMap = new HashMap(1);
    protected static final boolean verbose = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("decora.verbose"));
    })).booleanValue();

    /* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/Renderer$RendererState.class */
    public enum RendererState {
        NOTREADY,
        OK,
        LOST,
        DISPOSED
    }

    public abstract Effect.AccelType getAccelType();

    public abstract int getCompatibleWidth(int i2);

    public abstract int getCompatibleHeight(int i2);

    public abstract PoolFilterable createCompatibleImage(int i2, int i3);

    public abstract void clearImage(Filterable filterable);

    public abstract ImageData createImageData(FilterContext filterContext, Filterable filterable);

    public abstract Filterable transform(FilterContext filterContext, Filterable filterable, BaseTransform baseTransform, Rectangle rectangle, Rectangle rectangle2);

    public abstract ImageData transform(FilterContext filterContext, ImageData imageData, BaseTransform baseTransform, Rectangle rectangle, Rectangle rectangle2);

    public abstract RendererState getRendererState();

    protected abstract EffectPeer createPeer(FilterContext filterContext, String str, int i2);

    protected abstract Renderer getBackupRenderer();

    public abstract boolean isImageDataCompatible(ImageData imageData);

    protected Renderer() {
    }

    public PoolFilterable getCompatibleImage(int w2, int h2) {
        return this.imagePool.checkOut(this, w2, h2);
    }

    public void releaseCompatibleImage(Filterable image) {
        ImagePool pool;
        if ((image instanceof PoolFilterable) && (pool = ((PoolFilterable) image).getImagePool()) != null) {
            pool.checkIn((PoolFilterable) image);
        } else {
            image.unlock();
        }
    }

    public void releasePurgatory() {
        this.imagePool.releasePurgatory();
    }

    public ImageData transform(FilterContext fctx, ImageData img, int xpow2scales, int ypow2scales) {
        if (!img.getTransform().isIdentity()) {
            throw new InternalError("transform by powers of 2 requires untransformed source");
        }
        if ((xpow2scales | ypow2scales) == 0) {
            return img;
        }
        Affine2D at2 = new Affine2D();
        while (true) {
            if (xpow2scales >= -1 && ypow2scales >= -1) {
                break;
            }
            Rectangle origbounds = img.getUntransformedBounds();
            Rectangle newbounds = new Rectangle(origbounds);
            double xscale = 1.0d;
            double yscale = 1.0d;
            if (xpow2scales < 0) {
                xscale = 0.5d;
                newbounds.width = (origbounds.width + 1) / 2;
                newbounds.f11913x /= 2;
                xpow2scales++;
            }
            if (ypow2scales < 0) {
                yscale = 0.5d;
                newbounds.height = (origbounds.height + 1) / 2;
                newbounds.f11914y /= 2;
                ypow2scales++;
            }
            at2.setToScale(xscale, yscale);
            img = transform(fctx, img, at2, origbounds, newbounds);
        }
        if ((xpow2scales | ypow2scales) != 0) {
            double xscale2 = xpow2scales < 0 ? 0.5d : 1 << xpow2scales;
            double yscale2 = ypow2scales < 0 ? 0.5d : 1 << ypow2scales;
            at2.setToScale(xscale2, yscale2);
            img = img.transform(at2);
        }
        return img;
    }

    public LockableResource createFloatTexture(int w2, int h2) {
        throw new InternalError();
    }

    public void updateFloatTexture(LockableResource texture, FloatMap map) {
        throw new InternalError();
    }

    public final synchronized EffectPeer getPeerInstance(FilterContext fctx, String name, int unrollCount) {
        EffectPeer peer;
        EffectPeer peer2 = this.peerCache.get(name);
        if (peer2 != null) {
            return peer2;
        }
        if (unrollCount > 0 && (peer = this.peerCache.get(name + "_" + unrollCount)) != null) {
            return peer;
        }
        EffectPeer peer3 = createPeer(fctx, name, unrollCount);
        if (peer3 == null) {
            throw new RuntimeException("Could not create peer  " + name + " for renderer " + ((Object) this));
        }
        this.peerCache.put(peer3.getUniqueName(), peer3);
        return peer3;
    }

    protected Collection<EffectPeer> getPeers() {
        return this.peerCache.values();
    }

    protected static Renderer getSoftwareRenderer() {
        return RendererFactory.getSoftwareRenderer();
    }

    protected Renderer getRendererForSize(Effect effect, int approxW, int approxH) {
        return this;
    }

    public static synchronized Renderer getRenderer(FilterContext fctx) {
        if (fctx == null) {
            throw new IllegalArgumentException("FilterContext must be non-null");
        }
        Renderer r2 = rendererMap.get(fctx);
        if (r2 != null) {
            if (r2.getRendererState() == RendererState.NOTREADY) {
                return r2;
            }
            if (r2.getRendererState() == RendererState.OK) {
                return r2;
            }
            if (r2.getRendererState() == RendererState.LOST) {
                return r2.getBackupRenderer();
            }
            if (r2.getRendererState() == RendererState.DISPOSED) {
                r2 = null;
            }
        }
        if (r2 == null) {
            Collection<Renderer> renderers = rendererMap.values();
            Iterator<Renderer> iter = renderers.iterator();
            while (iter.hasNext()) {
                Renderer ren = iter.next();
                if (ren.getRendererState() == RendererState.DISPOSED) {
                    ren.imagePool.dispose();
                    iter.remove();
                }
            }
            r2 = RendererFactory.createRenderer(fctx);
            if (r2 == null) {
                throw new RuntimeException("Error creating a Renderer");
            }
            if (verbose) {
                String klassName = r2.getClass().getName();
                String rname = klassName.substring(klassName.lastIndexOf(".") + 1);
                Object screen = fctx.getReferent();
                System.out.println("Created " + rname + " (AccelType=" + ((Object) r2.getAccelType()) + ") for " + screen);
            }
            rendererMap.put(fctx, r2);
        }
        return r2;
    }

    public static Renderer getRenderer(FilterContext fctx, Effect effect, int approxW, int approxH) {
        return getRenderer(fctx).getRendererForSize(effect, approxW, approxH);
    }
}
