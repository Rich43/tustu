package com.sun.prism.d3d;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DFrameStats.class */
class D3DFrameStats {
    public int numTrianglesDrawn;
    public int numDrawCalls;
    public int numBufferLocks;
    public int numTextureLocks;
    public int numTextureTransferBytes;
    public int numSetTexture;
    public int numSetPixelShader;
    public int numRenderTargetSwitch;

    D3DFrameStats() {
    }

    static int divr(int x2, int d2) {
        return (x2 + (d2 / 2)) / d2;
    }

    public String toDebugString(int nFrames) {
        return "D3D Statistics per last " + nFrames + " frame(s) :\n\tnumTrianglesDrawn=" + divr(this.numTrianglesDrawn, nFrames) + ", numDrawCalls=" + divr(this.numDrawCalls, nFrames) + ", numBufferLocks=" + divr(this.numBufferLocks, nFrames) + "\n\tnumTextureLocks=" + divr(this.numTextureLocks, nFrames) + ", numTextureTransferKBytes=" + divr(this.numTextureTransferBytes / 1024, nFrames) + "\n\tnumRenderTargetSwitch=" + divr(this.numRenderTargetSwitch, nFrames) + ", numSetTexture=" + divr(this.numSetTexture, nFrames) + ", numSetPixelShader=" + divr(this.numSetPixelShader, nFrames);
    }
}
