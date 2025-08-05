package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.MultiTexture;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.Texture;
import com.sun.prism.d3d.D3DResource;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.ps.BaseShaderFactory;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DResourceFactory.class */
class D3DResourceFactory extends BaseShaderFactory {
    private final D3DContext context;
    private final int maxTextureSize;
    private final LinkedList<D3DResource.D3DRecord> records;
    private int nFrame;
    private D3DFrameStats frameStats;
    private static final Map<Image, Texture> clampTexCache = new WeakHashMap();
    private static final Map<Image, Texture> repeatTexCache = new WeakHashMap();
    private static final Map<Image, Texture> mipmapTexCache = new WeakHashMap();
    static final int STATS_FREQUENCY = PrismSettings.prismStatFrequency;

    static native long nGetContext(int i2);

    static native boolean nIsDefaultPool(long j2);

    static native int nTestCooperativeLevel(long j2);

    static native int nResetDevice(long j2);

    static native long nCreateTexture(long j2, int i2, int i3, boolean z2, int i4, int i5, int i6, boolean z3);

    static native long nCreateSwapChain(long j2, long j3, boolean z2);

    static native int nReleaseResource(long j2, long j3);

    static native int nGetMaximumTextureSize(long j2);

    static native int nGetTextureWidth(long j2);

    static native int nGetTextureHeight(long j2);

    static native int nReadPixelsI(long j2, long j3, long j4, Buffer buffer, int[] iArr, int i2, int i3);

    static native int nReadPixelsB(long j2, long j3, long j4, Buffer buffer, byte[] bArr, int i2, int i3);

    static native int nUpdateTextureI(long j2, long j3, IntBuffer intBuffer, int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    static native int nUpdateTextureF(long j2, long j3, FloatBuffer floatBuffer, float[] fArr, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    static native int nUpdateTextureB(long j2, long j3, ByteBuffer byteBuffer, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    static native long nGetDevice(long j2);

    static native long nGetNativeTextureObject(long j2);

    D3DResourceFactory(long pContext, Screen screen) {
        super(clampTexCache, repeatTexCache, mipmapTexCache);
        this.records = new LinkedList<>();
        this.nFrame = -1;
        this.context = new D3DContext(pContext, screen, this);
        this.context.initState();
        this.maxTextureSize = computeMaxTextureSize();
        if (PrismSettings.noClampToZero && PrismSettings.verbose) {
            System.out.println("prism.noclamptozero not supported by D3D");
        }
    }

    D3DContext getContext() {
        return this.context;
    }

    @Override // com.sun.prism.ResourceFactory
    public TextureResourcePool getTextureResourcePool() {
        return D3DVramPool.instance;
    }

    private void displayPrismStatistics() {
        if (STATS_FREQUENCY > 0) {
            int i2 = this.nFrame + 1;
            this.nFrame = i2;
            if (i2 == STATS_FREQUENCY) {
                this.nFrame = 0;
                this.frameStats = this.context.getFrameStats(true, this.frameStats);
                if (this.frameStats != null) {
                    System.err.println(this.frameStats.toDebugString(STATS_FREQUENCY));
                }
            }
        }
    }

    @Override // com.sun.prism.impl.BaseResourceFactory, com.sun.prism.ResourceFactory
    public boolean isDeviceReady() {
        if (isDisposed()) {
            return false;
        }
        displayPrismStatistics();
        return this.context.testLostStateAndReset();
    }

    static int nextPowerOfTwo(int val, int max) {
        if (val > max) {
            return 0;
        }
        int i2 = 1;
        while (true) {
            int i3 = i2;
            if (i3 < val) {
                i2 = i3 * 2;
            } else {
                return i3;
            }
        }
    }

    @Override // com.sun.prism.ResourceFactory
    public boolean isCompatibleTexture(Texture tex) {
        return tex instanceof D3DTexture;
    }

    @Override // com.sun.prism.ResourceFactory
    public D3DTexture createTexture(PixelFormat format, Texture.Usage usagehint, Texture.WrapMode wrapMode, int w2, int h2) {
        return createTexture(format, usagehint, wrapMode, w2, h2, false);
    }

    @Override // com.sun.prism.ResourceFactory
    public D3DTexture createTexture(PixelFormat format, Texture.Usage usagehint, Texture.WrapMode wrapMode, int w2, int h2, boolean useMipmap) {
        int allocw;
        int alloch;
        if (checkDisposed()) {
            return null;
        }
        if (!isFormatSupported(format)) {
            throw new UnsupportedOperationException("Pixel format " + ((Object) format) + " not supported on this device");
        }
        if (format == PixelFormat.MULTI_YCbCr_420) {
            throw new UnsupportedOperationException("MULTI_YCbCr_420 textures require a MediaFrame");
        }
        if (PrismSettings.forcePow2) {
            allocw = nextPowerOfTwo(w2, Integer.MAX_VALUE);
            alloch = nextPowerOfTwo(h2, Integer.MAX_VALUE);
        } else {
            allocw = w2;
            alloch = h2;
        }
        if (allocw <= 0 || alloch <= 0) {
            throw new RuntimeException("Illegal texture dimensions (" + allocw + LanguageTag.PRIVATEUSE + alloch + ")");
        }
        int bpp = format.getBytesPerPixelUnit();
        if (allocw >= (Integer.MAX_VALUE / alloch) / bpp) {
            throw new RuntimeException("Illegal texture dimensions (" + allocw + LanguageTag.PRIVATEUSE + alloch + ")");
        }
        D3DVramPool pool = D3DVramPool.instance;
        long size = pool.estimateTextureSize(allocw, alloch, format);
        if (!pool.prepareForAllocation(size)) {
            return null;
        }
        long pResource = nCreateTexture(this.context.getContextHandle(), format.ordinal(), usagehint.ordinal(), false, allocw, alloch, 0, useMipmap);
        if (pResource == 0) {
            return null;
        }
        int texw = nGetTextureWidth(pResource);
        int texh = nGetTextureHeight(pResource);
        if (wrapMode != Texture.WrapMode.CLAMP_NOT_NEEDED && (w2 < texw || h2 < texh)) {
            wrapMode = wrapMode.simulatedVersion();
        }
        return new D3DTexture(this.context, format, wrapMode, pResource, texw, texh, w2, h2, useMipmap);
    }

    @Override // com.sun.prism.ResourceFactory
    public Texture createTexture(MediaFrame frame) {
        if (checkDisposed()) {
            return null;
        }
        frame.holdFrame();
        int width = frame.getWidth();
        int height = frame.getHeight();
        int texWidth = frame.getEncodedWidth();
        int texHeight = frame.getEncodedHeight();
        PixelFormat texFormat = frame.getPixelFormat();
        if (texFormat == PixelFormat.MULTI_YCbCr_420) {
            MultiTexture tex = new MultiTexture(texFormat, Texture.WrapMode.CLAMP_TO_EDGE, width, height);
            for (int index = 0; index < frame.planeCount(); index++) {
                int subWidth = texWidth;
                int subHeight = texHeight;
                if (index == 2 || index == 1) {
                    subWidth /= 2;
                    subHeight /= 2;
                }
                D3DTexture subTex = createTexture(PixelFormat.BYTE_ALPHA, Texture.Usage.DYNAMIC, Texture.WrapMode.CLAMP_TO_EDGE, subWidth, subHeight);
                if (subTex == null) {
                    tex.dispose();
                    return null;
                }
                tex.setTexture(subTex, index);
            }
            frame.releaseFrame();
            return tex;
        }
        if (texWidth <= 0 || texHeight <= 0) {
            frame.releaseFrame();
            throw new RuntimeException("Illegal texture dimensions (" + texWidth + LanguageTag.PRIVATEUSE + texHeight + ")");
        }
        int bpp = texFormat.getBytesPerPixelUnit();
        if (texWidth >= (Integer.MAX_VALUE / texHeight) / bpp) {
            frame.releaseFrame();
            throw new RuntimeException("Illegal texture dimensions (" + texWidth + LanguageTag.PRIVATEUSE + texHeight + ")");
        }
        D3DVramPool pool = D3DVramPool.instance;
        long size = pool.estimateTextureSize(texWidth, texHeight, texFormat);
        if (!pool.prepareForAllocation(size)) {
            return null;
        }
        long pResource = nCreateTexture(this.context.getContextHandle(), texFormat.ordinal(), Texture.Usage.DYNAMIC.ordinal(), false, texWidth, texHeight, 0, false);
        if (0 == pResource) {
            return null;
        }
        int physWidth = nGetTextureWidth(pResource);
        int physHeight = nGetTextureHeight(pResource);
        Texture.WrapMode wrapMode = (texWidth < physWidth || texHeight < physHeight) ? Texture.WrapMode.CLAMP_TO_EDGE_SIMULATED : Texture.WrapMode.CLAMP_TO_EDGE;
        D3DTexture tex2 = new D3DTexture(this.context, texFormat, wrapMode, pResource, physWidth, physHeight, width, height, false);
        frame.releaseFrame();
        return tex2;
    }

    @Override // com.sun.prism.ResourceFactory
    public int getRTTWidth(int w2, Texture.WrapMode wrapMode) {
        return w2;
    }

    @Override // com.sun.prism.ResourceFactory
    public int getRTTHeight(int h2, Texture.WrapMode wrapMode) {
        return h2;
    }

    @Override // com.sun.prism.ResourceFactory
    public D3DRTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode) {
        return createRTTexture(width, height, wrapMode, false);
    }

    @Override // com.sun.prism.ResourceFactory
    public D3DRTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode, boolean msaa) {
        int aaSamples;
        if (checkDisposed()) {
            return null;
        }
        if (PrismSettings.verbose && this.context.isLost()) {
            System.err.println("RT Texture allocation while the device is lost");
        }
        int createw = width;
        int createh = height;
        if (PrismSettings.forcePow2) {
            createw = nextPowerOfTwo(createw, Integer.MAX_VALUE);
            createh = nextPowerOfTwo(createh, Integer.MAX_VALUE);
        }
        if (createw <= 0 || createh <= 0) {
            throw new RuntimeException("Illegal texture dimensions (" + createw + LanguageTag.PRIVATEUSE + createh + ")");
        }
        PixelFormat format = PixelFormat.INT_ARGB_PRE;
        int bpp = format.getBytesPerPixelUnit();
        if (createw >= (Integer.MAX_VALUE / createh) / bpp) {
            throw new RuntimeException("Illegal texture dimensions (" + createw + LanguageTag.PRIVATEUSE + createh + ")");
        }
        D3DVramPool pool = D3DVramPool.instance;
        if (msaa) {
            int maxSamples = D3DPipeline.getInstance().getMaxSamples();
            aaSamples = maxSamples < 2 ? 0 : maxSamples < 4 ? 2 : 4;
        } else {
            aaSamples = 0;
        }
        long size = pool.estimateRTTextureSize(width, height, false);
        if (!pool.prepareForAllocation(size)) {
            return null;
        }
        long pResource = nCreateTexture(this.context.getContextHandle(), format.ordinal(), Texture.Usage.DEFAULT.ordinal(), true, createw, createh, aaSamples, false);
        if (pResource == 0) {
            return null;
        }
        int texw = nGetTextureWidth(pResource);
        int texh = nGetTextureHeight(pResource);
        D3DRTTexture rtt = new D3DRTTexture(this.context, wrapMode, pResource, texw, texh, 0, 0, width, height, aaSamples);
        rtt.createGraphics().clear();
        return rtt;
    }

    @Override // com.sun.prism.ResourceFactory
    public Presentable createPresentable(PresentableState pState) {
        if (checkDisposed()) {
            return null;
        }
        if (PrismSettings.verbose && this.context.isLost()) {
            System.err.println("SwapChain allocation while the device is lost");
        }
        long pResource = nCreateSwapChain(this.context.getContextHandle(), pState.getNativeView(), PrismSettings.isVsyncEnabled);
        if (pResource != 0) {
            int width = pState.getRenderWidth();
            int height = pState.getRenderHeight();
            D3DRTTexture rtt = createRTTexture(width, height, Texture.WrapMode.CLAMP_NOT_NEEDED, pState.isMSAA());
            if (PrismSettings.dirtyOptsEnabled) {
                rtt.contentsUseful();
            }
            if (rtt != null) {
                return new D3DSwapChain(this.context, pResource, rtt, pState.getRenderScale());
            }
            nReleaseResource(this.context.getContextHandle(), pResource);
            return null;
        }
        return null;
    }

    private static ByteBuffer getBuffer(InputStream is) {
        if (is == null) {
            throw new RuntimeException("InputStream must be non-null");
        }
        try {
            int len = 4096;
            byte[] data = new byte[4096];
            BufferedInputStream bis = new BufferedInputStream(is, 4096);
            int offset = 0;
            while (true) {
                int readBytes = bis.read(data, offset, len - offset);
                if (readBytes != -1) {
                    offset += readBytes;
                    if (len - offset == 0) {
                        len *= 2;
                        byte[] newdata = new byte[len];
                        System.arraycopy(data, 0, newdata, 0, data.length);
                        data = newdata;
                    }
                } else {
                    bis.close();
                    ByteBuffer buf = ByteBuffer.allocateDirect(offset);
                    buf.put(data, 0, offset);
                    return buf;
                }
            }
        } catch (IOException e2) {
            throw new RuntimeException("Error loading D3D shader object", e2);
        }
    }

    @Override // com.sun.prism.ps.ShaderFactory
    public Shader createShader(InputStream pixelShaderCode, Map<String, Integer> samplers, Map<String, Integer> params, int maxTexCoordIndex, boolean isPixcoordUsed, boolean isPerVertexColorUsed) {
        if (checkDisposed()) {
            return null;
        }
        long shaderHandle = D3DShader.init(this.context.getContextHandle(), getBuffer(pixelShaderCode), maxTexCoordIndex, isPixcoordUsed, isPerVertexColorUsed);
        return new D3DShader(this.context, shaderHandle, params);
    }

    @Override // com.sun.prism.ps.ShaderFactory
    public Shader createStockShader(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Shader name must be non-null");
        }
        try {
            InputStream stream = (InputStream) AccessController.doPrivileged(() -> {
                return D3DResourceFactory.class.getResourceAsStream("hlsl/" + name + ".obj");
            });
            Class klass = Class.forName("com.sun.prism.shader." + name + "_Loader");
            Method m2 = klass.getMethod("loadShader", ShaderFactory.class, InputStream.class);
            return (Shader) m2.invoke(null, this, stream);
        } catch (Throwable e2) {
            e2.printStackTrace();
            throw new InternalError("Error loading stock shader " + name);
        }
    }

    @Override // com.sun.prism.ResourceFactory
    public boolean isFormatSupported(PixelFormat format) {
        return true;
    }

    private int computeMaxTextureSize() {
        int size = nGetMaximumTextureSize(this.context.getContextHandle());
        if (PrismSettings.verbose) {
            System.err.println("Maximum supported texture size: " + size);
        }
        if (size > PrismSettings.maxTextureSize) {
            size = PrismSettings.maxTextureSize;
            if (PrismSettings.verbose) {
                System.err.println("Maximum texture size clamped to " + size);
            }
        }
        return size;
    }

    @Override // com.sun.prism.ResourceFactory
    public int getMaximumTextureSize() {
        return this.maxTextureSize;
    }

    @Override // com.sun.prism.impl.BaseResourceFactory
    protected void notifyReset() {
        ListIterator<D3DResource.D3DRecord> it = this.records.listIterator();
        while (it.hasNext()) {
            D3DResource.D3DRecord r2 = it.next();
            if (r2.isDefaultPool()) {
                r2.markDisposed();
                it.remove();
            }
        }
        super.notifyReset();
    }

    @Override // com.sun.prism.impl.BaseResourceFactory, com.sun.prism.GraphicsResource
    public void dispose() {
        this.context.dispose();
        ListIterator<D3DResource.D3DRecord> it = this.records.listIterator();
        while (it.hasNext()) {
            D3DResource.D3DRecord r2 = it.next();
            r2.markDisposed();
        }
        this.records.clear();
        super.dispose();
    }

    void addRecord(D3DResource.D3DRecord record) {
        this.records.add(record);
    }

    void removeRecord(D3DResource.D3DRecord record) {
        this.records.remove(record);
    }

    @Override // com.sun.prism.ResourceFactory
    public PhongMaterial createPhongMaterial() {
        if (checkDisposed()) {
            return null;
        }
        return D3DPhongMaterial.create(this.context);
    }

    @Override // com.sun.prism.ResourceFactory
    public MeshView createMeshView(Mesh mesh) {
        if (checkDisposed()) {
            return null;
        }
        return D3DMeshView.create(this.context, (D3DMesh) mesh);
    }

    @Override // com.sun.prism.ResourceFactory
    public Mesh createMesh() {
        if (checkDisposed()) {
            return null;
        }
        return D3DMesh.create(this.context);
    }
}
