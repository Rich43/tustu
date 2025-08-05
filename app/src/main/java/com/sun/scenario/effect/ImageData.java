package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/ImageData.class */
public class ImageData {
    private static HashSet<ImageData> alldatas;
    private ImageData sharedOwner;
    private FilterContext fctx;
    private int refcount;
    private Filterable image;
    private final Rectangle bounds;
    private BaseTransform transform;
    private Throwable fromwhere;
    private boolean reusable;

    static {
        AccessController.doPrivileged(() -> {
            if (System.getProperty("decora.showleaks") != null) {
                alldatas = new HashSet<>();
                Runtime.getRuntime().addShutdownHook(new Thread() { // from class: com.sun.scenario.effect.ImageData.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        Iterator datas = ImageData.alldatas.iterator();
                        while (datas.hasNext()) {
                            ImageData id = (ImageData) datas.next();
                            Rectangle r2 = id.getUntransformedBounds();
                            System.out.println("id[" + r2.width + LanguageTag.PRIVATEUSE + r2.height + ", refcount=" + id.refcount + "] leaked from:");
                            id.fromwhere.printStackTrace(System.out);
                        }
                    }
                });
                return null;
            }
            return null;
        });
    }

    public ImageData(FilterContext fctx, Filterable image, Rectangle bounds) {
        this(fctx, image, bounds, BaseTransform.IDENTITY_TRANSFORM);
    }

    public ImageData(FilterContext fctx, Filterable image, Rectangle bounds, BaseTransform transform) {
        this.fctx = fctx;
        this.refcount = 1;
        this.image = image;
        this.bounds = bounds;
        this.transform = transform;
        if (alldatas != null) {
            alldatas.add(this);
            this.fromwhere = new Throwable();
        }
    }

    public ImageData transform(BaseTransform concattx) {
        BaseTransform newtx;
        if (concattx.isIdentity()) {
            return this;
        }
        if (this.transform.isIdentity()) {
            newtx = concattx;
        } else {
            newtx = concattx.copy().deriveWithConcatenation(this.transform);
        }
        return new ImageData(this, newtx, this.bounds);
    }

    private ImageData(ImageData original, BaseTransform transform, Rectangle bounds) {
        this(original.fctx, original.image, bounds, transform);
        this.sharedOwner = original;
    }

    public void setReusable(boolean reusable) {
        if (this.sharedOwner != null) {
            throw new InternalError("cannot make a shared ImageData reusable");
        }
        this.reusable = reusable;
    }

    public FilterContext getFilterContext() {
        return this.fctx;
    }

    public Filterable getUntransformedImage() {
        return this.image;
    }

    public Rectangle getUntransformedBounds() {
        return this.bounds;
    }

    public BaseTransform getTransform() {
        return this.transform;
    }

    public Filterable getTransformedImage(Rectangle clip) {
        if (this.image == null || this.fctx == null) {
            return null;
        }
        if (this.transform.isIdentity()) {
            return this.image;
        }
        Rectangle txbounds = getTransformedBounds(clip);
        return Renderer.getRenderer(this.fctx).transform(this.fctx, this.image, this.transform, this.bounds, txbounds);
    }

    public void releaseTransformedImage(Filterable tximage) {
        if (this.fctx != null && tximage != null && tximage != this.image) {
            Effect.releaseCompatibleImage(this.fctx, tximage);
        }
    }

    public Rectangle getTransformedBounds(Rectangle clip) {
        if (this.transform.isIdentity()) {
            return this.bounds;
        }
        Rectangle txbounds = new Rectangle();
        this.transform.transform(this.bounds, txbounds);
        if (clip != null) {
            txbounds.intersectWith(clip);
        }
        return txbounds;
    }

    public int getReferenceCount() {
        return this.refcount;
    }

    public boolean addref() {
        if (this.reusable && this.refcount == 0 && this.image != null) {
            this.image.lock();
        }
        this.refcount++;
        return (this.image == null || this.image.isLost()) ? false : true;
    }

    public void unref() {
        int i2 = this.refcount - 1;
        this.refcount = i2;
        if (i2 == 0) {
            if (this.sharedOwner != null) {
                this.sharedOwner.unref();
                this.sharedOwner = null;
            } else if (this.fctx != null && this.image != null) {
                if (this.reusable) {
                    this.image.unlock();
                    return;
                }
                Effect.releaseCompatibleImage(this.fctx, this.image);
            }
            this.fctx = null;
            this.image = null;
            if (alldatas != null) {
                alldatas.remove(this);
            }
        }
    }

    public boolean validate(FilterContext fctx) {
        return this.image != null && Renderer.getRenderer(fctx).isImageDataCompatible(this);
    }

    public String toString() {
        return "ImageData{sharedOwner=" + ((Object) this.sharedOwner) + ", fctx=" + ((Object) this.fctx) + ", refcount=" + this.refcount + ", image=" + ((Object) this.image) + ", bounds=" + ((Object) this.bounds) + ", transform=" + ((Object) this.transform) + ", reusable=" + this.reusable + '}';
    }
}
