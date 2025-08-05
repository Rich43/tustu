package sun.awt.windows;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.VolatileImage;
import java.security.AccessController;
import sun.awt.image.BufImgSurfaceData;
import sun.java2d.DestSurfaceProvider;
import sun.java2d.InvalidPipeException;
import sun.java2d.Surface;
import sun.java2d.d3d.D3DSurfaceData;
import sun.java2d.opengl.WGLSurfaceData;
import sun.java2d.pipe.BufferedContext;
import sun.java2d.pipe.RenderQueue;
import sun.java2d.pipe.hw.AccelGraphicsConfig;
import sun.java2d.pipe.hw.AccelSurface;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/awt/windows/TranslucentWindowPainter.class */
abstract class TranslucentWindowPainter {
    protected Window window;
    protected WWindowPeer peer;
    private static final boolean forceOpt = Boolean.valueOf((String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.twp.forceopt", "false"))).booleanValue();
    private static final boolean forceSW = Boolean.valueOf((String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.twp.forcesw", "false"))).booleanValue();

    protected abstract Image getBackBuffer(boolean z2);

    protected abstract boolean update(Image image);

    public abstract void flush();

    public static TranslucentWindowPainter createInstance(WWindowPeer wWindowPeer) throws SecurityException {
        Object graphicsConfiguration = wWindowPeer.getGraphicsConfiguration();
        if (!forceSW && (graphicsConfiguration instanceof AccelGraphicsConfig)) {
            String simpleName = graphicsConfiguration.getClass().getSimpleName();
            if ((((AccelGraphicsConfig) graphicsConfiguration).getContextCapabilities().getCaps() & 256) != 0 || forceOpt) {
                if (simpleName.startsWith("D3D")) {
                    return new VIOptD3DWindowPainter(wWindowPeer);
                }
                if (forceOpt && simpleName.startsWith("WGL")) {
                    return new VIOptWGLWindowPainter(wWindowPeer);
                }
            }
        }
        return new BIWindowPainter(wWindowPeer);
    }

    protected TranslucentWindowPainter(WWindowPeer wWindowPeer) {
        this.peer = wWindowPeer;
        this.window = (Window) wWindowPeer.getTarget();
    }

    public void updateWindow(boolean z2) {
        boolean zUpdate = false;
        Image backBuffer = getBackBuffer(z2);
        while (!zUpdate) {
            if (z2) {
                Graphics2D graphics2D = (Graphics2D) backBuffer.getGraphics();
                try {
                    this.window.paintAll(graphics2D);
                    graphics2D.dispose();
                } catch (Throwable th) {
                    graphics2D.dispose();
                    throw th;
                }
            }
            zUpdate = update(backBuffer);
            if (!zUpdate) {
                z2 = true;
                backBuffer = getBackBuffer(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Image clearImage(Image image) {
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setColor(new Color(0, 0, 0, 0));
        graphics2D.fillRect(0, 0, width, height);
        return image;
    }

    /* loaded from: rt.jar:sun/awt/windows/TranslucentWindowPainter$BIWindowPainter.class */
    private static class BIWindowPainter extends TranslucentWindowPainter {
        private BufferedImage backBuffer;

        protected BIWindowPainter(WWindowPeer wWindowPeer) {
            super(wWindowPeer);
        }

        @Override // sun.awt.windows.TranslucentWindowPainter
        protected Image getBackBuffer(boolean z2) {
            int width = this.window.getWidth();
            int height = this.window.getHeight();
            if (this.backBuffer == null || this.backBuffer.getWidth() != width || this.backBuffer.getHeight() != height) {
                flush();
                this.backBuffer = new BufferedImage(width, height, 3);
            }
            return z2 ? (BufferedImage) TranslucentWindowPainter.clearImage(this.backBuffer) : this.backBuffer;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // sun.awt.windows.TranslucentWindowPainter
        protected boolean update(Image image) {
            VolatileImage volatileImage = null;
            if (image instanceof BufferedImage) {
                BufferedImage bufferedImage = (BufferedImage) image;
                this.peer.updateWindowImpl(((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData(), bufferedImage.getWidth(), bufferedImage.getHeight());
                return true;
            }
            if (image instanceof VolatileImage) {
                volatileImage = (VolatileImage) image;
                if (image instanceof DestSurfaceProvider) {
                    Surface destSurface = ((DestSurfaceProvider) image).getDestSurface();
                    if (destSurface instanceof BufImgSurfaceData) {
                        int width = volatileImage.getWidth();
                        int height = volatileImage.getHeight();
                        this.peer.updateWindowImpl(((DataBufferInt) ((BufImgSurfaceData) destSurface).getRaster(0, 0, width, height).getDataBuffer()).getData(), width, height);
                        return true;
                    }
                }
            }
            BufferedImage bufferedImage2 = (BufferedImage) TranslucentWindowPainter.clearImage(this.backBuffer);
            this.peer.updateWindowImpl(((DataBufferInt) bufferedImage2.getRaster().getDataBuffer()).getData(), bufferedImage2.getWidth(), bufferedImage2.getHeight());
            return volatileImage == null || !volatileImage.contentsLost();
        }

        @Override // sun.awt.windows.TranslucentWindowPainter
        public void flush() {
            if (this.backBuffer != null) {
                this.backBuffer.flush();
                this.backBuffer = null;
            }
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/TranslucentWindowPainter$VIWindowPainter.class */
    private static class VIWindowPainter extends BIWindowPainter {
        private VolatileImage viBB;

        protected VIWindowPainter(WWindowPeer wWindowPeer) {
            super(wWindowPeer);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // sun.awt.windows.TranslucentWindowPainter.BIWindowPainter, sun.awt.windows.TranslucentWindowPainter
        protected Image getBackBuffer(boolean z2) {
            int width = this.window.getWidth();
            int height = this.window.getHeight();
            GraphicsConfiguration graphicsConfiguration = this.peer.getGraphicsConfiguration();
            if (this.viBB == null || this.viBB.getWidth() != width || this.viBB.getHeight() != height || this.viBB.validate(graphicsConfiguration) == 2) {
                flush();
                if (graphicsConfiguration instanceof AccelGraphicsConfig) {
                    this.viBB = ((AccelGraphicsConfig) graphicsConfiguration).createCompatibleVolatileImage(width, height, 3, 2);
                }
                if (this.viBB == null) {
                    this.viBB = graphicsConfiguration.createCompatibleVolatileImage(width, height, 3);
                }
                this.viBB.validate(graphicsConfiguration);
            }
            return z2 ? TranslucentWindowPainter.clearImage(this.viBB) : this.viBB;
        }

        @Override // sun.awt.windows.TranslucentWindowPainter.BIWindowPainter, sun.awt.windows.TranslucentWindowPainter
        public void flush() {
            if (this.viBB != null) {
                this.viBB.flush();
                this.viBB = null;
            }
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/TranslucentWindowPainter$VIOptWindowPainter.class */
    private static abstract class VIOptWindowPainter extends VIWindowPainter {
        protected abstract boolean updateWindowAccel(long j2, int i2, int i3);

        protected VIOptWindowPainter(WWindowPeer wWindowPeer) {
            super(wWindowPeer);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // sun.awt.windows.TranslucentWindowPainter.BIWindowPainter, sun.awt.windows.TranslucentWindowPainter
        protected boolean update(Image image) {
            if (image instanceof DestSurfaceProvider) {
                Surface destSurface = ((DestSurfaceProvider) image).getDestSurface();
                if (destSurface instanceof AccelSurface) {
                    final int width = image.getWidth(null);
                    final int height = image.getHeight(null);
                    final boolean[] zArr = {false};
                    final AccelSurface accelSurface = (AccelSurface) destSurface;
                    RenderQueue renderQueue = accelSurface.getContext().getRenderQueue();
                    renderQueue.lock();
                    try {
                        BufferedContext.validateContext(accelSurface);
                        renderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.awt.windows.TranslucentWindowPainter.VIOptWindowPainter.1
                            @Override // java.lang.Runnable
                            public void run() {
                                zArr[0] = VIOptWindowPainter.this.updateWindowAccel(accelSurface.getNativeOps(), width, height);
                            }
                        });
                        renderQueue.unlock();
                    } catch (InvalidPipeException e2) {
                        renderQueue.unlock();
                    } catch (Throwable th) {
                        renderQueue.unlock();
                        throw th;
                    }
                    return zArr[0];
                }
            }
            return super.update(image);
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/TranslucentWindowPainter$VIOptD3DWindowPainter.class */
    private static class VIOptD3DWindowPainter extends VIOptWindowPainter {
        protected VIOptD3DWindowPainter(WWindowPeer wWindowPeer) {
            super(wWindowPeer);
        }

        @Override // sun.awt.windows.TranslucentWindowPainter.VIOptWindowPainter
        protected boolean updateWindowAccel(long j2, int i2, int i3) {
            return D3DSurfaceData.updateWindowAccelImpl(j2, this.peer.getData(), i2, i3);
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/TranslucentWindowPainter$VIOptWGLWindowPainter.class */
    private static class VIOptWGLWindowPainter extends VIOptWindowPainter {
        protected VIOptWGLWindowPainter(WWindowPeer wWindowPeer) {
            super(wWindowPeer);
        }

        @Override // sun.awt.windows.TranslucentWindowPainter.VIOptWindowPainter
        protected boolean updateWindowAccel(long j2, int i2, int i3) {
            return WGLSurfaceData.updateWindowAccelImpl(j2, this.peer, i2, i3);
        }
    }
}
