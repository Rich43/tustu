package sun.java2d.d3d;

import sun.java2d.pipe.BufferedContext;
import sun.java2d.pipe.RenderBuffer;
import sun.java2d.pipe.RenderQueue;
import sun.java2d.pipe.hw.ContextCapabilities;

/* loaded from: rt.jar:sun/java2d/d3d/D3DContext.class */
class D3DContext extends BufferedContext {
    private final D3DGraphicsDevice device;

    D3DContext(RenderQueue renderQueue, D3DGraphicsDevice d3DGraphicsDevice) {
        super(renderQueue);
        this.device = d3DGraphicsDevice;
    }

    static void invalidateCurrentContext() {
        if (currentContext != null) {
            currentContext.invalidateContext();
            currentContext = null;
        }
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.ensureCapacity(4);
        d3DRenderQueue.getBuffer().putInt(75);
        d3DRenderQueue.flushNow();
    }

    static void setScratchSurface(D3DContext d3DContext) {
        if (d3DContext != currentContext) {
            currentContext = null;
        }
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        RenderBuffer buffer = d3DRenderQueue.getBuffer();
        d3DRenderQueue.ensureCapacity(8);
        buffer.putInt(71);
        buffer.putInt(d3DContext.getDevice().getScreen());
    }

    @Override // sun.java2d.pipe.BufferedContext
    public RenderQueue getRenderQueue() {
        return D3DRenderQueue.getInstance();
    }

    @Override // sun.java2d.pipe.BufferedContext
    public void saveState() {
        invalidateContext();
        invalidateCurrentContext();
        setScratchSurface(this);
        this.rq.ensureCapacity(4);
        this.buf.putInt(78);
        this.rq.flushNow();
    }

    @Override // sun.java2d.pipe.BufferedContext
    public void restoreState() {
        invalidateContext();
        invalidateCurrentContext();
        setScratchSurface(this);
        this.rq.ensureCapacity(4);
        this.buf.putInt(79);
        this.rq.flushNow();
    }

    D3DGraphicsDevice getDevice() {
        return this.device;
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DContext$D3DContextCaps.class */
    static class D3DContextCaps extends ContextCapabilities {
        static final int CAPS_LCD_SHADER = 65536;
        static final int CAPS_BIOP_SHADER = 131072;
        static final int CAPS_DEVICE_OK = 262144;
        static final int CAPS_AA_SHADER = 524288;

        D3DContextCaps(int i2, String str) {
            super(i2, str);
        }

        @Override // sun.java2d.pipe.hw.ContextCapabilities
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer(super.toString());
            if ((this.caps & 65536) != 0) {
                stringBuffer.append("CAPS_LCD_SHADER|");
            }
            if ((this.caps & 131072) != 0) {
                stringBuffer.append("CAPS_BIOP_SHADER|");
            }
            if ((this.caps & 524288) != 0) {
                stringBuffer.append("CAPS_AA_SHADER|");
            }
            if ((this.caps & 262144) != 0) {
                stringBuffer.append("CAPS_DEVICE_OK|");
            }
            return stringBuffer.toString();
        }
    }
}
