package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Graphics;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import java.nio.Buffer;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGExternalNode.class */
public class NGExternalNode extends NGNode {
    private Texture dsttexture;
    private BufferData bufferData;
    private final AtomicReference<RenderData> renderData = new AtomicReference<>(null);
    private RenderData rd;
    private volatile ReentrantLock bufferLock;

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        RenderData curRenderData = this.renderData.getAndSet(null);
        if (curRenderData != null) {
            this.rd = curRenderData;
        }
        if (this.rd == null) {
            return;
        }
        int x2 = this.rd.bdata.srcbounds.f11913x;
        int y2 = this.rd.bdata.srcbounds.f11914y;
        int w2 = this.rd.bdata.srcbounds.width;
        int h2 = this.rd.bdata.srcbounds.height;
        if (this.dsttexture != null) {
            this.dsttexture.lock();
            if (this.dsttexture.isSurfaceLost() || this.dsttexture.getContentWidth() != w2 || this.dsttexture.getContentHeight() != h2) {
                this.dsttexture.unlock();
                this.dsttexture.dispose();
                this.rd = this.rd.copyAddDirtyRect(0, 0, w2, h2);
                this.dsttexture = createTexture(g2, this.rd);
            }
        } else {
            this.dsttexture = createTexture(g2, this.rd);
        }
        if (this.dsttexture == null) {
            return;
        }
        if (curRenderData != null) {
            try {
                this.bufferLock.lock();
                try {
                    this.dsttexture.update(this.rd.bdata.srcbuffer, PixelFormat.INT_ARGB_PRE, this.rd.dirtyRect.f11913x, this.rd.dirtyRect.f11914y, x2 + this.rd.dirtyRect.f11913x, y2 + this.rd.dirtyRect.f11914y, this.rd.dirtyRect.width, this.rd.dirtyRect.height, this.rd.bdata.linestride * 4, false);
                    this.bufferLock.unlock();
                    if (this.rd.clearTarget) {
                        g2.clearQuad(0.0f, 0.0f, this.rd.bdata.usrwidth, this.rd.bdata.usrheight);
                    }
                } catch (Throwable th) {
                    this.bufferLock.unlock();
                    throw th;
                }
            } catch (Throwable th2) {
                this.dsttexture.unlock();
                throw th2;
            }
        }
        g2.drawTexture(this.dsttexture, 0.0f, 0.0f, this.rd.bdata.usrwidth, this.rd.bdata.usrheight, 0.0f, 0.0f, w2, h2);
        this.dsttexture.unlock();
    }

    private Texture createTexture(Graphics g2, RenderData rd) {
        ResourceFactory factory = g2.getResourceFactory();
        if (factory.isDisposed()) {
            return null;
        }
        Texture txt = factory.createTexture(PixelFormat.INT_ARGB_PRE, Texture.Usage.DYNAMIC, Texture.WrapMode.CLAMP_NOT_NEEDED, rd.bdata.srcbounds.width, rd.bdata.srcbounds.height);
        if (txt != null) {
            txt.contentsUseful();
        } else {
            System.err.println("NGExternalNode: failed to create a texture");
        }
        return txt;
    }

    public void setLock(ReentrantLock lock) {
        this.bufferLock = lock;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGExternalNode$BufferData.class */
    private static class BufferData {
        final Buffer srcbuffer;
        final int linestride;
        final Rectangle srcbounds;
        final float usrwidth;
        final float usrheight;
        final int scale;

        BufferData(Buffer srcbuffer, int linestride, int x2, int y2, int width, int height, float usrWidth, float usrHeight, int scale) {
            this.srcbuffer = srcbuffer;
            this.scale = scale;
            this.linestride = linestride;
            this.srcbounds = scale(new Rectangle(x2, y2, width, height));
            this.usrwidth = usrWidth;
            this.usrheight = usrHeight;
        }

        Rectangle scale(Rectangle r2) {
            r2.f11913x *= this.scale;
            r2.f11914y *= this.scale;
            r2.width *= this.scale;
            r2.height *= this.scale;
            return r2;
        }

        BufferData copyWithBounds(int x2, int y2, int width, int height, float usrWidth, float usrHeight) {
            return new BufferData(this.srcbuffer, this.linestride, x2, y2, width, height, usrWidth, usrHeight, this.scale);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGExternalNode$RenderData.class */
    private static class RenderData {
        final BufferData bdata;
        final Rectangle dirtyRect;
        final boolean clearTarget;

        RenderData(BufferData bdata, int dirtyX, int dirtyY, int dirtyWidth, int dirtyHeight, boolean clearTarget) {
            this(bdata, dirtyX, dirtyY, dirtyWidth, dirtyHeight, clearTarget, true);
        }

        RenderData(BufferData bdata, int dirtyX, int dirtyY, int dirtyWidth, int dirtyHeight, boolean clearTarget, boolean applyScale) {
            this.bdata = bdata;
            Rectangle r2 = new Rectangle(dirtyX, dirtyY, dirtyWidth, dirtyHeight);
            this.dirtyRect = applyScale ? bdata.scale(r2) : r2;
            this.dirtyRect.intersectWith(bdata.srcbounds);
            this.clearTarget = clearTarget;
        }

        RenderData copyAddDirtyRect(int dirtyX, int dirtyY, int dirtyWidth, int dirtyHeight) {
            Rectangle r2 = this.bdata.scale(new Rectangle(dirtyX, dirtyY, dirtyWidth, dirtyHeight));
            r2.add(this.dirtyRect);
            return new RenderData(this.bdata, r2.f11913x, r2.f11914y, r2.width, r2.height, this.clearTarget, false);
        }
    }

    public void setImageBuffer(Buffer buffer, int x2, int y2, int width, int height, float usrWidth, float usrHeight, int linestride, int scale) {
        this.bufferData = new BufferData(buffer, linestride, x2, y2, width, height, usrWidth, usrHeight, scale);
        this.renderData.set(new RenderData(this.bufferData, x2, y2, width, height, true));
    }

    public void setImageBounds(int x2, int y2, int width, int height, float usrWidth, float usrHeight) {
        boolean shrinked = ((float) width) < this.bufferData.usrwidth || ((float) height) < this.bufferData.usrheight;
        this.bufferData = this.bufferData.copyWithBounds(x2, y2, width, height, usrWidth, usrHeight);
        this.renderData.updateAndGet(prev -> {
            boolean clearTarget = prev != null ? prev.clearTarget : false;
            return new RenderData(this.bufferData, x2, y2, width, height, clearTarget | shrinked);
        });
    }

    public void repaintDirtyRegion(int dirtyX, int dirtyY, int dirtyWidth, int dirtyHeight) {
        this.renderData.updateAndGet(prev -> {
            if (prev != null) {
                return prev.copyAddDirtyRect(dirtyX, dirtyY, dirtyWidth, dirtyHeight);
            }
            return new RenderData(this.bufferData, dirtyX, dirtyY, dirtyWidth, dirtyHeight, false);
        });
    }

    public void markContentDirty() {
        visualsChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return false;
    }
}
