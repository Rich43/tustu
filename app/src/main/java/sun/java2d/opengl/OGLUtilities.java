package sun.java2d.opengl;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/opengl/OGLUtilities.class */
class OGLUtilities {
    public static final int UNDEFINED = 0;
    public static final int WINDOW = 1;
    public static final int TEXTURE = 3;
    public static final int FLIP_BACKBUFFER = 4;
    public static final int FBOBJECT = 5;

    private OGLUtilities() {
    }

    public static boolean isQueueFlusherThread() {
        return OGLRenderQueue.isQueueFlusherThread();
    }

    public static boolean invokeWithOGLContextCurrent(Graphics graphics, Runnable runnable) {
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        if (graphics != null) {
            try {
                if (!(graphics instanceof SunGraphics2D)) {
                    return false;
                }
                SurfaceData surfaceData = ((SunGraphics2D) graphics).surfaceData;
                if (!(surfaceData instanceof OGLSurfaceData)) {
                    oGLRenderQueue.unlock();
                    return false;
                }
                OGLContext.validateContext((OGLSurfaceData) surfaceData);
            } finally {
                oGLRenderQueue.unlock();
            }
        }
        oGLRenderQueue.flushAndInvokeNow(runnable);
        OGLContext.invalidateCurrentContext();
        oGLRenderQueue.unlock();
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean invokeWithOGLSharedContextCurrent(GraphicsConfiguration graphicsConfiguration, Runnable runnable) {
        if (!(graphicsConfiguration instanceof OGLGraphicsConfig)) {
            return false;
        }
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            OGLContext.setScratchSurface((OGLGraphicsConfig) graphicsConfiguration);
            oGLRenderQueue.flushAndInvokeNow(runnable);
            OGLContext.invalidateCurrentContext();
            return true;
        } finally {
            oGLRenderQueue.unlock();
        }
    }

    public static Rectangle getOGLViewport(Graphics graphics, int i2, int i3) {
        if (!(graphics instanceof SunGraphics2D)) {
            return null;
        }
        SunGraphics2D sunGraphics2D = (SunGraphics2D) graphics;
        SurfaceData surfaceData = sunGraphics2D.surfaceData;
        return new Rectangle(sunGraphics2D.transX, surfaceData.getBounds().height - (sunGraphics2D.transY + i3), i2, i3);
    }

    public static Rectangle getOGLScissorBox(Graphics graphics) {
        if (!(graphics instanceof SunGraphics2D)) {
            return null;
        }
        SunGraphics2D sunGraphics2D = (SunGraphics2D) graphics;
        SurfaceData surfaceData = sunGraphics2D.surfaceData;
        Region compClip = sunGraphics2D.getCompClip();
        if (!compClip.isRectangular()) {
            return null;
        }
        int loX = compClip.getLoX();
        int loY = compClip.getLoY();
        int width = compClip.getWidth();
        int height = compClip.getHeight();
        return new Rectangle(loX, surfaceData.getBounds().height - (loY + height), width, height);
    }

    public static Object getOGLSurfaceIdentifier(Graphics graphics) {
        if (!(graphics instanceof SunGraphics2D)) {
            return null;
        }
        return ((SunGraphics2D) graphics).surfaceData;
    }

    public static int getOGLSurfaceType(Graphics graphics) {
        if (!(graphics instanceof SunGraphics2D)) {
            return 0;
        }
        SurfaceData surfaceData = ((SunGraphics2D) graphics).surfaceData;
        if (!(surfaceData instanceof OGLSurfaceData)) {
            return 0;
        }
        return ((OGLSurfaceData) surfaceData).getType();
    }

    public static int getOGLTextureType(Graphics graphics) {
        if (!(graphics instanceof SunGraphics2D)) {
            return 0;
        }
        SurfaceData surfaceData = ((SunGraphics2D) graphics).surfaceData;
        if (!(surfaceData instanceof OGLSurfaceData)) {
            return 0;
        }
        return ((OGLSurfaceData) surfaceData).getTextureTarget();
    }
}
