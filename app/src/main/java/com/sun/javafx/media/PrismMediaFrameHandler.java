package com.sun.javafx.media;

import com.sun.glass.ui.Screen;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.control.VideoFormat;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: jfxrt.jar:com/sun/javafx/media/PrismMediaFrameHandler.class */
public class PrismMediaFrameHandler implements ResourceFactoryListener {
    private static Map<Object, PrismMediaFrameHandler> handlers;
    private final Map<Screen, TextureMapEntry> textures = new WeakHashMap(1);
    private WeakReference<ResourceFactory> registeredWithFactory = null;
    private final RenderJob releaseRenderJob = new RenderJob(() -> {
        releaseData();
    });

    public static synchronized PrismMediaFrameHandler getHandler(Object provider) {
        if (provider == null) {
            throw new IllegalArgumentException("provider must be non-null");
        }
        if (handlers == null) {
            handlers = new WeakHashMap(1);
        }
        PrismMediaFrameHandler ret = handlers.get(provider);
        if (ret == null) {
            ret = new PrismMediaFrameHandler(provider);
            handlers.put(provider, ret);
        }
        return ret;
    }

    private PrismMediaFrameHandler(Object provider) {
    }

    public Texture getTexture(Graphics g2, VideoDataBuffer currentFrame) {
        Screen screen = g2.getAssociatedScreen();
        TextureMapEntry tme = this.textures.get(screen);
        if (null == currentFrame) {
            if (this.textures.containsKey(screen)) {
                this.textures.remove(screen);
                return null;
            }
            return null;
        }
        if (null == tme) {
            tme = new TextureMapEntry();
            this.textures.put(screen, tme);
        }
        if (tme.texture != null) {
            tme.texture.lock();
            if (tme.texture.isSurfaceLost()) {
                tme.texture = null;
            }
        }
        if (null == tme.texture || tme.lastFrameTime != currentFrame.getTimestamp()) {
            updateTexture(g2, currentFrame, tme);
        }
        return tme.texture;
    }

    private void updateTexture(Graphics g2, VideoDataBuffer vdb, TextureMapEntry tme) {
        Screen screen = g2.getAssociatedScreen();
        if (tme.texture != null && (tme.encodedWidth != vdb.getEncodedWidth() || tme.encodedHeight != vdb.getEncodedHeight())) {
            tme.texture.dispose();
            tme.texture = null;
        }
        PrismFrameBuffer prismBuffer = new PrismFrameBuffer(vdb);
        if (tme.texture == null) {
            ResourceFactory factory = GraphicsPipeline.getDefaultResourceFactory();
            if (this.registeredWithFactory == null || this.registeredWithFactory.get() != factory) {
                factory.addFactoryListener(this);
                this.registeredWithFactory = new WeakReference<>(factory);
            }
            tme.texture = GraphicsPipeline.getPipeline().getResourceFactory(screen).createTexture(prismBuffer);
            tme.encodedWidth = vdb.getEncodedWidth();
            tme.encodedHeight = vdb.getEncodedHeight();
        }
        if (tme.texture != null) {
            tme.texture.update(prismBuffer, false);
        }
        tme.lastFrameTime = vdb.getTimestamp();
    }

    private void releaseData() {
        for (TextureMapEntry tme : this.textures.values()) {
            if (tme != null && tme.texture != null) {
                tme.texture.dispose();
            }
        }
        this.textures.clear();
    }

    public void releaseTextures() {
        Toolkit tk = Toolkit.getToolkit();
        tk.addRenderJob(this.releaseRenderJob);
    }

    @Override // com.sun.prism.ResourceFactoryListener
    public void factoryReset() {
        releaseData();
    }

    @Override // com.sun.prism.ResourceFactoryListener
    public void factoryReleased() {
        releaseData();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/media/PrismMediaFrameHandler$PrismFrameBuffer.class */
    private class PrismFrameBuffer implements MediaFrame {
        private final PixelFormat videoFormat;
        private final VideoDataBuffer master;

        public PrismFrameBuffer(VideoDataBuffer sourceBuffer) {
            if (null == sourceBuffer) {
                throw new NullPointerException();
            }
            this.master = sourceBuffer;
            switch (this.master.getFormat()) {
                case BGRA_PRE:
                    this.videoFormat = PixelFormat.INT_ARGB_PRE;
                    return;
                case YCbCr_420p:
                    this.videoFormat = PixelFormat.MULTI_YCbCr_420;
                    return;
                case YCbCr_422:
                    this.videoFormat = PixelFormat.BYTE_APPLE_422;
                    return;
                case ARGB:
                default:
                    throw new IllegalArgumentException("Unsupported video format " + ((Object) this.master.getFormat()));
            }
        }

        @Override // com.sun.prism.MediaFrame
        public ByteBuffer getBufferForPlane(int plane) {
            return this.master.getBufferForPlane(plane);
        }

        @Override // com.sun.prism.MediaFrame
        public void holdFrame() {
            this.master.holdFrame();
        }

        @Override // com.sun.prism.MediaFrame
        public void releaseFrame() {
            this.master.releaseFrame();
        }

        @Override // com.sun.prism.MediaFrame
        public PixelFormat getPixelFormat() {
            return this.videoFormat;
        }

        @Override // com.sun.prism.MediaFrame
        public int getWidth() {
            return this.master.getWidth();
        }

        @Override // com.sun.prism.MediaFrame
        public int getHeight() {
            return this.master.getHeight();
        }

        @Override // com.sun.prism.MediaFrame
        public int getEncodedWidth() {
            return this.master.getEncodedWidth();
        }

        @Override // com.sun.prism.MediaFrame
        public int getEncodedHeight() {
            return this.master.getEncodedHeight();
        }

        @Override // com.sun.prism.MediaFrame
        public int planeCount() {
            return this.master.getPlaneCount();
        }

        @Override // com.sun.prism.MediaFrame
        public int[] planeStrides() {
            return this.master.getPlaneStrides();
        }

        @Override // com.sun.prism.MediaFrame
        public int strideForPlane(int planeIndex) {
            return this.master.getStrideForPlane(planeIndex);
        }

        @Override // com.sun.prism.MediaFrame
        public MediaFrame convertToFormat(PixelFormat fmt) {
            VideoDataBuffer newVDB;
            if (fmt == getPixelFormat()) {
                return this;
            }
            if (fmt != PixelFormat.INT_ARGB_PRE || null == (newVDB = this.master.convertToFormat(VideoFormat.BGRA_PRE))) {
                return null;
            }
            return PrismMediaFrameHandler.this.new PrismFrameBuffer(newVDB);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/media/PrismMediaFrameHandler$TextureMapEntry.class */
    private static class TextureMapEntry {
        public double lastFrameTime;
        public Texture texture;
        public int encodedWidth;
        public int encodedHeight;

        private TextureMapEntry() {
            this.lastFrameTime = -1.0d;
        }
    }
}
