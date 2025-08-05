package sun.java2d.opengl;

import sun.java2d.pipe.BufferedContext;
import sun.java2d.pipe.RenderBuffer;
import sun.java2d.pipe.RenderQueue;
import sun.java2d.pipe.hw.ContextCapabilities;

/* loaded from: rt.jar:sun/java2d/opengl/OGLContext.class */
public class OGLContext extends BufferedContext {
    private final OGLGraphicsConfig config;

    static final native String getOGLIdString();

    OGLContext(RenderQueue renderQueue, OGLGraphicsConfig oGLGraphicsConfig) {
        super(renderQueue);
        this.config = oGLGraphicsConfig;
    }

    static void setScratchSurface(OGLGraphicsConfig oGLGraphicsConfig) {
        setScratchSurface(oGLGraphicsConfig.getNativeConfigInfo());
    }

    static void setScratchSurface(long j2) {
        currentContext = null;
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        RenderBuffer buffer = oGLRenderQueue.getBuffer();
        oGLRenderQueue.ensureCapacityAndAlignment(12, 4);
        buffer.putInt(71);
        buffer.putLong(j2);
    }

    static void invalidateCurrentContext() {
        if (currentContext != null) {
            currentContext.invalidateContext();
            currentContext = null;
        }
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.ensureCapacity(4);
        oGLRenderQueue.getBuffer().putInt(75);
        oGLRenderQueue.flushNow();
    }

    @Override // sun.java2d.pipe.BufferedContext
    public RenderQueue getRenderQueue() {
        return OGLRenderQueue.getInstance();
    }

    @Override // sun.java2d.pipe.BufferedContext
    public void saveState() {
        invalidateContext();
        invalidateCurrentContext();
        setScratchSurface(this.config);
        this.rq.ensureCapacity(4);
        this.buf.putInt(78);
        this.rq.flushNow();
    }

    @Override // sun.java2d.pipe.BufferedContext
    public void restoreState() {
        invalidateContext();
        invalidateCurrentContext();
        setScratchSurface(this.config);
        this.rq.ensureCapacity(4);
        this.buf.putInt(79);
        this.rq.flushNow();
    }

    /* loaded from: rt.jar:sun/java2d/opengl/OGLContext$OGLContextCaps.class */
    static class OGLContextCaps extends ContextCapabilities {
        static final int CAPS_EXT_FBOBJECT = 12;
        static final int CAPS_DOUBLEBUFFERED = 65536;
        static final int CAPS_EXT_LCD_SHADER = 131072;
        static final int CAPS_EXT_BIOP_SHADER = 262144;
        static final int CAPS_EXT_GRAD_SHADER = 524288;
        static final int CAPS_EXT_TEXRECT = 1048576;
        static final int CAPS_EXT_TEXBARRIER = 2097152;

        OGLContextCaps(int i2, String str) {
            super(i2, str);
        }

        @Override // sun.java2d.pipe.hw.ContextCapabilities
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer(super.toString());
            if ((this.caps & 12) != 0) {
                stringBuffer.append("CAPS_EXT_FBOBJECT|");
            }
            if ((this.caps & 65536) != 0) {
                stringBuffer.append("CAPS_DOUBLEBUFFERED|");
            }
            if ((this.caps & 131072) != 0) {
                stringBuffer.append("CAPS_EXT_LCD_SHADER|");
            }
            if ((this.caps & 262144) != 0) {
                stringBuffer.append("CAPS_BIOP_SHADER|");
            }
            if ((this.caps & 524288) != 0) {
                stringBuffer.append("CAPS_EXT_GRAD_SHADER|");
            }
            if ((this.caps & 1048576) != 0) {
                stringBuffer.append("CAPS_EXT_TEXRECT|");
            }
            if ((this.caps & 2097152) != 0) {
                stringBuffer.append("CAPS_EXT_TEXBARRIER|");
            }
            return stringBuffer.toString();
        }
    }
}
