package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGDefaultCamera;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.MeshView;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ps.BaseShaderContext;
import com.sun.prism.ps.Shader;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DContext.class */
class D3DContext extends BaseShaderContext {
    public static final int D3DERR_DEVICEREMOVED = -2005530512;
    public static final int D3DERR_DEVICENOTRESET = -2005530519;
    public static final int D3DERR_DEVICELOST = -2005530520;
    public static final int E_FAIL = -2147467259;
    public static final int D3DERR_OUTOFVIDEOMEMORY = -2005532292;
    public static final int D3D_OK = 0;
    public static final int D3DCOMPMODE_CLEAR = 0;
    public static final int D3DCOMPMODE_SRC = 1;
    public static final int D3DCOMPMODE_SRCOVER = 2;
    public static final int D3DCOMPMODE_DSTOUT = 3;
    public static final int D3DCOMPMODE_ADD = 4;
    public static final int D3DTADDRESS_NOP = 0;
    public static final int D3DTADDRESS_WRAP = 1;
    public static final int D3DTADDRESS_MIRROR = 2;
    public static final int D3DTADDRESS_CLAMP = 3;
    public static final int D3DTADDRESS_BORDER = 4;
    public static final int CULL_BACK = 110;
    public static final int CULL_FRONT = 111;
    public static final int CULL_NONE = 112;
    private static GeneralTransform3D scratchTx = new GeneralTransform3D();
    private static final Affine3D scratchAffine3DTx = new Affine3D();
    private static double[] tempAdjustClipSpaceMat = new double[16];
    private BaseShaderContext.State state;
    private boolean isLost;
    private final long pContext;
    private Vec3d cameraPos;
    private GeneralTransform3D projViewTx;
    private int targetWidth;
    private int targetHeight;
    private final D3DResourceFactory factory;
    public static final int NUM_QUADS;

    private static native int nSetRenderTarget(long j2, long j3, boolean z2, boolean z3);

    private static native int nSetTexture(long j2, long j3, int i2, boolean z2, int i3);

    private static native int nResetTransform(long j2);

    private static native int nSetTransform(long j2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15, double d16, double d17);

    private static native void nSetWorldTransformToIdentity(long j2);

    private static native void nSetWorldTransform(long j2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15, double d16, double d17);

    private static native int nSetCameraPosition(long j2, double d2, double d3, double d4);

    private static native int nSetProjViewMatrix(long j2, boolean z2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15, double d16, double d17);

    private static native int nResetClipRect(long j2);

    private static native int nSetClipRect(long j2, int i2, int i3, int i4, int i5);

    private static native int nSetBlendEnabled(long j2, int i2);

    private static native int nSetDeviceParametersFor2D(long j2);

    private static native int nSetDeviceParametersFor3D(long j2);

    private static native long nCreateD3DMesh(long j2);

    private static native void nReleaseD3DMesh(long j2, long j3);

    private static native boolean nBuildNativeGeometryShort(long j2, long j3, float[] fArr, int i2, short[] sArr, int i3);

    private static native boolean nBuildNativeGeometryInt(long j2, long j3, float[] fArr, int i2, int[] iArr, int i3);

    private static native long nCreateD3DPhongMaterial(long j2);

    private static native void nReleaseD3DPhongMaterial(long j2, long j3);

    private static native void nSetDiffuseColor(long j2, long j3, float f2, float f3, float f4, float f5);

    private static native void nSetSpecularColor(long j2, long j3, boolean z2, float f2, float f3, float f4, float f5);

    private static native void nSetMap(long j2, long j3, int i2, long j4);

    private static native long nCreateD3DMeshView(long j2, long j3);

    private static native void nReleaseD3DMeshView(long j2, long j3);

    private static native void nSetCullingMode(long j2, long j3, int i2);

    private static native void nSetMaterial(long j2, long j3, long j4);

    private static native void nSetWireframe(long j2, long j3, boolean z2);

    private static native void nSetAmbientLight(long j2, long j3, float f2, float f3, float f4);

    private static native void nSetPointLight(long j2, long j3, int i2, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    private static native void nRenderMeshView(long j2, long j3);

    private static native int nDrawIndexedQuads(long j2, float[] fArr, byte[] bArr, int i2);

    private static native void nBlit(long j2, long j3, long j4, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    private static native boolean nGetFrameStats(long j2, D3DFrameStats d3DFrameStats, boolean z2);

    private static native boolean nIsRTTVolatile(long j2);

    public static boolean FAILED(int hr) {
        return hr < 0;
    }

    static {
        NUM_QUADS = PrismSettings.superShader ? 4096 : 256;
    }

    D3DContext(long pContext, Screen screen, D3DResourceFactory factory) {
        super(screen, factory, NUM_QUADS);
        this.isLost = false;
        this.cameraPos = new Vec3d();
        this.projViewTx = new GeneralTransform3D();
        this.targetWidth = 0;
        this.targetHeight = 0;
        this.pContext = pContext;
        this.factory = factory;
    }

    @Override // com.sun.prism.impl.BaseContext
    public D3DResourceFactory getResourceFactory() {
        return this.factory;
    }

    protected void initState() {
        init();
        this.state = new BaseShaderContext.State();
        validate(nSetBlendEnabled(this.pContext, 2));
        validate(nSetDeviceParametersFor2D(this.pContext));
    }

    long getContextHandle() {
        return this.pContext;
    }

    boolean isLost() {
        return this.isLost;
    }

    static void validate(int res) {
        if (PrismSettings.verbose && FAILED(res)) {
            System.err.println("D3D hresult failed :" + hResultToString(res));
            new Exception("Stack trace").printStackTrace(System.err);
        }
    }

    private void setLost() {
        this.isLost = true;
    }

    boolean testLostStateAndReset() {
        if (isDisposed()) {
            return false;
        }
        int hr = D3DResourceFactory.nTestCooperativeLevel(this.pContext);
        if (PrismSettings.verbose && FAILED(hr)) {
            System.err.print("D3DContext::testLostStateAndReset : ");
            switch (hr) {
                case E_FAIL /* -2147467259 */:
                    System.err.println("E_FAIL");
                    break;
                case D3DERR_DEVICELOST /* -2005530520 */:
                    System.err.println("D3DERR_DEVICELOST");
                    break;
                case D3DERR_DEVICENOTRESET /* -2005530519 */:
                    System.err.println("D3DERR_DEVICENOTRESET");
                    break;
                case D3DERR_DEVICEREMOVED /* -2005530512 */:
                    System.err.println("D3DERR_DEVICEREMOVED");
                    break;
                case 0:
                    System.err.println("D3D_OK");
                    break;
                default:
                    System.err.println(String.format("Unknown D3D error 0x%x", Integer.valueOf(hr)));
                    break;
            }
        }
        if (hr == -2005530520) {
            setLost();
        }
        if (hr == -2005530519) {
            boolean wasLost = isLost();
            setLost();
            disposeLCDBuffer();
            this.factory.notifyReset();
            hr = D3DResourceFactory.nResetDevice(this.pContext);
            if (hr == 0) {
                this.isLost = false;
                initState();
                if (!wasLost) {
                    return false;
                }
            }
        }
        if (hr == -2005530512) {
            setLost();
            D3DPipeline.getInstance().reinitialize();
        }
        return !FAILED(hr);
    }

    boolean validatePresent(int res) {
        if (res == -2005530520 || res == -2005530519) {
            setLost();
        } else {
            validate(res);
        }
        return !FAILED(res);
    }

    @Override // com.sun.prism.impl.ps.BaseShaderContext, com.sun.prism.impl.BaseContext
    public void dispose() {
        disposeLCDBuffer();
        this.state = null;
        super.dispose();
    }

    private GeneralTransform3D adjustClipSpace(GeneralTransform3D projViewTx) {
        double[] m2 = projViewTx.get(tempAdjustClipSpaceMat);
        m2[8] = (m2[8] + m2[12]) / 2.0d;
        m2[9] = (m2[9] + m2[13]) / 2.0d;
        m2[10] = (m2[10] + m2[14]) / 2.0d;
        m2[11] = (m2[11] + m2[15]) / 2.0d;
        projViewTx.set(m2);
        return projViewTx;
    }

    @Override // com.sun.prism.impl.ps.BaseShaderContext
    protected BaseShaderContext.State updateRenderTarget(RenderTarget target, NGCamera camera, boolean depthTest) {
        if (checkDisposed()) {
            return null;
        }
        long resourceHandle = ((D3DRenderTarget) target).getResourceHandle();
        int res = nSetRenderTarget(this.pContext, resourceHandle, depthTest, target.isMSAA());
        validate(res);
        if (res == 0) {
            resetLastClip(this.state);
        }
        this.targetWidth = target.getPhysicalWidth();
        this.targetHeight = target.getPhysicalHeight();
        if (camera instanceof NGDefaultCamera) {
            ((NGDefaultCamera) camera).validate(this.targetWidth, this.targetHeight);
            this.projViewTx = adjustClipSpace(camera.getProjViewTx(this.projViewTx));
        } else {
            this.projViewTx = adjustClipSpace(camera.getProjViewTx(this.projViewTx));
            double vw = camera.getViewWidth();
            double vh = camera.getViewHeight();
            if (this.targetWidth != vw || this.targetHeight != vh) {
                this.projViewTx.scale(vw / this.targetWidth, vh / this.targetHeight, 1.0d);
            }
        }
        validate(nSetProjViewMatrix(this.pContext, depthTest, this.projViewTx.get(0), this.projViewTx.get(1), this.projViewTx.get(2), this.projViewTx.get(3), this.projViewTx.get(4), this.projViewTx.get(5), this.projViewTx.get(6), this.projViewTx.get(7), this.projViewTx.get(8), this.projViewTx.get(9), this.projViewTx.get(10), this.projViewTx.get(11), this.projViewTx.get(12), this.projViewTx.get(13), this.projViewTx.get(14), this.projViewTx.get(15)));
        this.cameraPos = camera.getPositionInWorld(this.cameraPos);
        return this.state;
    }

    @Override // com.sun.prism.impl.ps.BaseShaderContext
    protected void updateTexture(int texUnit, Texture tex) {
        long texHandle;
        boolean linear;
        int wrapMode;
        if (tex != null) {
            D3DTexture d3dtex = (D3DTexture) tex;
            texHandle = d3dtex.getNativeSourceHandle();
            linear = tex.getLinearFiltering();
            switch (tex.getWrapMode()) {
                case CLAMP_NOT_NEEDED:
                    wrapMode = 0;
                    break;
                case CLAMP_TO_EDGE:
                case CLAMP_TO_EDGE_SIMULATED:
                case CLAMP_TO_ZERO_SIMULATED:
                    wrapMode = 3;
                    break;
                case CLAMP_TO_ZERO:
                    wrapMode = 4;
                    break;
                case REPEAT:
                case REPEAT_SIMULATED:
                    wrapMode = 1;
                    break;
                default:
                    throw new InternalError("Unrecognized wrap mode: " + ((Object) tex.getWrapMode()));
            }
        } else {
            texHandle = 0;
            linear = false;
            wrapMode = 3;
        }
        validate(nSetTexture(this.pContext, texHandle, texUnit, linear, wrapMode));
    }

    @Override // com.sun.prism.impl.ps.BaseShaderContext
    protected void updateShaderTransform(Shader shader, BaseTransform xform) {
        int res;
        if (xform == null) {
            xform = BaseTransform.IDENTITY_TRANSFORM;
        }
        GeneralTransform3D perspectiveTransform = getPerspectiveTransformNoClone();
        if (xform.isIdentity() && perspectiveTransform.isIdentity()) {
            res = nResetTransform(this.pContext);
        } else if (perspectiveTransform.isIdentity()) {
            res = nSetTransform(this.pContext, xform.getMxx(), xform.getMxy(), xform.getMxz(), xform.getMxt(), xform.getMyx(), xform.getMyy(), xform.getMyz(), xform.getMyt(), xform.getMzx(), xform.getMzy(), xform.getMzz(), xform.getMzt(), 0.0d, 0.0d, 0.0d, 1.0d);
        } else {
            scratchTx.setIdentity().mul(xform).mul(perspectiveTransform);
            res = nSetTransform(this.pContext, scratchTx.get(0), scratchTx.get(1), scratchTx.get(2), scratchTx.get(3), scratchTx.get(4), scratchTx.get(5), scratchTx.get(6), scratchTx.get(7), scratchTx.get(8), scratchTx.get(9), scratchTx.get(10), scratchTx.get(11), scratchTx.get(12), scratchTx.get(13), scratchTx.get(14), scratchTx.get(15));
        }
        validate(res);
    }

    @Override // com.sun.prism.impl.ps.BaseShaderContext
    protected void updateWorldTransform(BaseTransform xform) {
        if (xform == null || xform.isIdentity()) {
            nSetWorldTransformToIdentity(this.pContext);
        } else {
            nSetWorldTransform(this.pContext, xform.getMxx(), xform.getMxy(), xform.getMxz(), xform.getMxt(), xform.getMyx(), xform.getMyy(), xform.getMyz(), xform.getMyt(), xform.getMzx(), xform.getMzy(), xform.getMzz(), xform.getMzt(), 0.0d, 0.0d, 0.0d, 1.0d);
        }
    }

    @Override // com.sun.prism.impl.ps.BaseShaderContext
    protected void updateClipRect(Rectangle clipRect) {
        int res;
        if (clipRect == null || clipRect.isEmpty()) {
            res = nResetClipRect(this.pContext);
        } else {
            int x1 = clipRect.f11913x;
            int y1 = clipRect.f11914y;
            int x2 = x1 + clipRect.width;
            int y2 = y1 + clipRect.height;
            res = nSetClipRect(this.pContext, x1, y1, x2, y2);
        }
        validate(res);
    }

    @Override // com.sun.prism.impl.ps.BaseShaderContext
    protected void updateCompositeMode(CompositeMode mode) {
        int d3dmode;
        switch (mode) {
            case CLEAR:
                d3dmode = 0;
                break;
            case SRC:
                d3dmode = 1;
                break;
            case SRC_OVER:
                d3dmode = 2;
                break;
            case DST_OUT:
                d3dmode = 3;
                break;
            case ADD:
                d3dmode = 4;
                break;
            default:
                throw new InternalError("Unrecognized composite mode: " + ((Object) mode));
        }
        validate(nSetBlendEnabled(this.pContext, d3dmode));
    }

    D3DFrameStats getFrameStats(boolean reset, D3DFrameStats result) {
        if (result == null) {
            result = new D3DFrameStats();
        }
        if (nGetFrameStats(this.pContext, result, reset)) {
            return result;
        }
        return null;
    }

    public boolean isRTTVolatile() {
        if (checkDisposed()) {
            return false;
        }
        return nIsRTTVolatile(this.pContext);
    }

    public static String hResultToString(long hResult) {
        switch ((int) hResult) {
            case D3DERR_OUTOFVIDEOMEMORY /* -2005532292 */:
                return "D3DERR_OUTOFVIDEOMEMORY";
            case D3DERR_DEVICELOST /* -2005530520 */:
                return "D3DERR_DEVICELOST";
            case D3DERR_DEVICENOTRESET /* -2005530519 */:
                return "D3DERR_DEVICENOTRESET";
            case D3DERR_DEVICEREMOVED /* -2005530512 */:
                return "D3DERR_DEVICEREMOVED";
            case 0:
                return "D3D_OK";
            default:
                return "D3D_ERROR " + Long.toHexString(hResult);
        }
    }

    @Override // com.sun.prism.impl.BaseContext
    public void setDeviceParametersFor2D() {
        if (checkDisposed()) {
            return;
        }
        nSetDeviceParametersFor2D(this.pContext);
    }

    @Override // com.sun.prism.impl.BaseContext
    protected void setDeviceParametersFor3D() {
        if (checkDisposed()) {
            return;
        }
        nSetDeviceParametersFor3D(this.pContext);
    }

    long createD3DMesh() {
        if (checkDisposed()) {
            return 0L;
        }
        return nCreateD3DMesh(this.pContext);
    }

    void releaseD3DMesh(long nativeHandle) {
        nReleaseD3DMesh(this.pContext, nativeHandle);
    }

    boolean buildNativeGeometry(long nativeHandle, float[] vertexBuffer, int vertexBufferLength, short[] indexBuffer, int indexBufferLength) {
        return nBuildNativeGeometryShort(this.pContext, nativeHandle, vertexBuffer, vertexBufferLength, indexBuffer, indexBufferLength);
    }

    boolean buildNativeGeometry(long nativeHandle, float[] vertexBuffer, int vertexBufferLength, int[] indexBuffer, int indexBufferLength) {
        return nBuildNativeGeometryInt(this.pContext, nativeHandle, vertexBuffer, vertexBufferLength, indexBuffer, indexBufferLength);
    }

    long createD3DPhongMaterial() {
        return nCreateD3DPhongMaterial(this.pContext);
    }

    void releaseD3DPhongMaterial(long nativeHandle) {
        nReleaseD3DPhongMaterial(this.pContext, nativeHandle);
    }

    void setDiffuseColor(long nativePhongMaterial, float r2, float g2, float b2, float a2) {
        nSetDiffuseColor(this.pContext, nativePhongMaterial, r2, g2, b2, a2);
    }

    void setSpecularColor(long nativePhongMaterial, boolean set, float r2, float g2, float b2, float a2) {
        nSetSpecularColor(this.pContext, nativePhongMaterial, set, r2, g2, b2, a2);
    }

    void setMap(long nativePhongMaterial, int mapType, long nativeTexture) {
        nSetMap(this.pContext, nativePhongMaterial, mapType, nativeTexture);
    }

    long createD3DMeshView(long nativeMesh) {
        return nCreateD3DMeshView(this.pContext, nativeMesh);
    }

    void releaseD3DMeshView(long nativeMeshView) {
        nReleaseD3DMeshView(this.pContext, nativeMeshView);
    }

    void setCullingMode(long nativeMeshView, int cullMode) {
        int cm;
        if (cullMode == MeshView.CULL_NONE) {
            cm = 112;
        } else if (cullMode == MeshView.CULL_BACK) {
            cm = 110;
        } else if (cullMode == MeshView.CULL_FRONT) {
            cm = 111;
        } else {
            throw new IllegalArgumentException("illegal value for CullMode: " + cullMode);
        }
        nSetCullingMode(this.pContext, nativeMeshView, cm);
    }

    void setMaterial(long nativeMeshView, long nativePhongMaterial) {
        nSetMaterial(this.pContext, nativeMeshView, nativePhongMaterial);
    }

    void setWireframe(long nativeMeshView, boolean wireframe) {
        nSetWireframe(this.pContext, nativeMeshView, wireframe);
    }

    void setAmbientLight(long nativeMeshView, float r2, float g2, float b2) {
        nSetAmbientLight(this.pContext, nativeMeshView, r2, g2, b2);
    }

    void setPointLight(long nativeMeshView, int index, float x2, float y2, float z2, float r2, float g2, float b2, float w2) {
        nSetPointLight(this.pContext, nativeMeshView, index, x2, y2, z2, r2, g2, b2, w2);
    }

    @Override // com.sun.prism.impl.BaseContext
    protected void renderQuads(float[] coordArray, byte[] colorArray, int numVertices) {
        int res = nDrawIndexedQuads(this.pContext, coordArray, colorArray, numVertices);
        validate(res);
    }

    void renderMeshView(long nativeMeshView, Graphics g2) {
        scratchTx = scratchTx.set(this.projViewTx);
        float pixelScaleFactor = g2.getPixelScaleFactor();
        if (pixelScaleFactor != 1.0d) {
            scratchTx.scale(pixelScaleFactor, pixelScaleFactor, 1.0d);
        }
        int res = nSetProjViewMatrix(this.pContext, g2.isDepthTest(), scratchTx.get(0), scratchTx.get(1), scratchTx.get(2), scratchTx.get(3), scratchTx.get(4), scratchTx.get(5), scratchTx.get(6), scratchTx.get(7), scratchTx.get(8), scratchTx.get(9), scratchTx.get(10), scratchTx.get(11), scratchTx.get(12), scratchTx.get(13), scratchTx.get(14), scratchTx.get(15));
        validate(res);
        int res2 = nSetCameraPosition(this.pContext, this.cameraPos.f11930x, this.cameraPos.f11931y, this.cameraPos.f11932z);
        validate(res2);
        BaseTransform xform = g2.getTransformNoClone();
        if (pixelScaleFactor != 1.0d) {
            float invPSF = 1.0f / pixelScaleFactor;
            scratchAffine3DTx.setToIdentity();
            scratchAffine3DTx.scale(invPSF, invPSF);
            scratchAffine3DTx.concatenate(xform);
            updateWorldTransform(scratchAffine3DTx);
        } else {
            updateWorldTransform(xform);
        }
        nRenderMeshView(this.pContext, nativeMeshView);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.prism.impl.ps.BaseShaderContext
    public void blit(RTTexture rTTexture, RTTexture rTTexture2, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1) {
        long dstNativeHandle = rTTexture2 == 0 ? 0L : ((D3DTexture) rTTexture2).getNativeSourceHandle();
        long srcNativeHandle = ((D3DTexture) rTTexture).getNativeSourceHandle();
        nBlit(this.pContext, srcNativeHandle, dstNativeHandle, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1);
    }
}
