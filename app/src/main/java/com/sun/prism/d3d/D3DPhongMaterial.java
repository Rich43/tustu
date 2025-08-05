package com.sun.prism.d3d;

import com.sun.prism.Image;
import com.sun.prism.PhongMaterial;
import com.sun.prism.Texture;
import com.sun.prism.TextureMap;
import com.sun.prism.impl.BasePhongMaterial;
import com.sun.prism.impl.Disposer;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DPhongMaterial.class */
class D3DPhongMaterial extends BasePhongMaterial implements PhongMaterial {
    static int count = 0;
    private final D3DContext context;
    private final long nativeHandle;
    private TextureMap[] maps;

    private D3DPhongMaterial(D3DContext context, long nativeHandle, Disposer.Record disposerRecord) {
        super(disposerRecord);
        this.maps = new TextureMap[MAX_MAP_TYPE];
        this.context = context;
        this.nativeHandle = nativeHandle;
        count++;
    }

    static D3DPhongMaterial create(D3DContext context) {
        long nativeHandle = context.createD3DPhongMaterial();
        return new D3DPhongMaterial(context, nativeHandle, new D3DPhongMaterialDisposerRecord(context, nativeHandle));
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override // com.sun.prism.PhongMaterial
    public void setDiffuseColor(float r2, float g2, float b2, float a2) {
        this.context.setDiffuseColor(this.nativeHandle, r2, g2, b2, a2);
    }

    @Override // com.sun.prism.PhongMaterial
    public void setSpecularColor(boolean set, float r2, float g2, float b2, float a2) {
        this.context.setSpecularColor(this.nativeHandle, set, r2, g2, b2, a2);
    }

    @Override // com.sun.prism.PhongMaterial
    public void setTextureMap(TextureMap map) {
        this.maps[map.getType().ordinal()] = map;
    }

    private Texture setupTexture(TextureMap map, boolean useMipmap) {
        Image image = map.getImage();
        Texture texture = image == null ? null : this.context.getResourceFactory().getCachedTexture(image, Texture.WrapMode.REPEAT, useMipmap);
        long hTexture = texture != null ? ((D3DTexture) texture).getNativeTextureObject() : 0L;
        this.context.setMap(this.nativeHandle, map.getType().ordinal(), hTexture);
        return texture;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0035  */
    @Override // com.sun.prism.PhongMaterial
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void lockTextureMaps() {
        /*
            r4 = this;
            r0 = 0
            r5 = r0
        L2:
            r0 = r5
            int r1 = com.sun.prism.d3d.D3DPhongMaterial.MAX_MAP_TYPE
            if (r0 >= r1) goto L90
            r0 = r4
            com.sun.prism.TextureMap[] r0 = r0.maps
            r1 = r5
            r0 = r0[r1]
            com.sun.prism.Texture r0 = r0.getTexture()
            r6 = r0
            r0 = r4
            com.sun.prism.TextureMap[] r0 = r0.maps
            r1 = r5
            r0 = r0[r1]
            boolean r0 = r0.isDirty()
            if (r0 != 0) goto L35
            r0 = r6
            if (r0 == 0) goto L35
            r0 = r6
            r0.lock()
            r0 = r6
            boolean r0 = r0.isSurfaceLost()
            if (r0 != 0) goto L35
            goto L8a
        L35:
            r0 = r5
            int r1 = com.sun.prism.PhongMaterial.DIFFUSE
            if (r0 == r1) goto L43
            r0 = r5
            int r1 = com.sun.prism.PhongMaterial.SELF_ILLUM
            if (r0 != r1) goto L47
        L43:
            r0 = 1
            goto L48
        L47:
            r0 = 0
        L48:
            r7 = r0
            r0 = r4
            r1 = r4
            com.sun.prism.TextureMap[] r1 = r1.maps
            r2 = r5
            r1 = r1[r2]
            r2 = r7
            com.sun.prism.Texture r0 = r0.setupTexture(r1, r2)
            r6 = r0
            r0 = r4
            com.sun.prism.TextureMap[] r0 = r0.maps
            r1 = r5
            r0 = r0[r1]
            r1 = r6
            r0.setTexture(r1)
            r0 = r4
            com.sun.prism.TextureMap[] r0 = r0.maps
            r1 = r5
            r0 = r0[r1]
            r1 = 0
            r0.setDirty(r1)
            r0 = r4
            com.sun.prism.TextureMap[] r0 = r0.maps
            r1 = r5
            r0 = r0[r1]
            com.sun.prism.Image r0 = r0.getImage()
            if (r0 == 0) goto L8a
            r0 = r6
            if (r0 != 0) goto L8a
            java.lang.Class<com.sun.prism.PhongMaterial> r0 = com.sun.prism.PhongMaterial.class
            java.lang.String r0 = r0.getName()
            r8 = r0
            r0 = r8
            sun.util.logging.PlatformLogger r0 = sun.util.logging.PlatformLogger.getLogger(r0)
            java.lang.String r1 = "Warning: Low on texture resources. Cannot create texture."
            r0.warning(r1)
        L8a:
            int r5 = r5 + 1
            goto L2
        L90:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.prism.d3d.D3DPhongMaterial.lockTextureMaps():void");
    }

    @Override // com.sun.prism.PhongMaterial
    public void unlockTextureMaps() {
        for (int i2 = 0; i2 < MAX_MAP_TYPE; i2++) {
            Texture texture = this.maps[i2].getTexture();
            if (texture != null) {
                texture.unlock();
            }
        }
    }

    @Override // com.sun.prism.impl.BasePhongMaterial, com.sun.prism.Material
    public boolean isValid() {
        return !this.context.isDisposed();
    }

    @Override // com.sun.prism.impl.BaseGraphicsResource, com.sun.prism.GraphicsResource
    public void dispose() {
        this.disposerRecord.dispose();
        count--;
    }

    public int getCount() {
        return count;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DPhongMaterial$D3DPhongMaterialDisposerRecord.class */
    static class D3DPhongMaterialDisposerRecord implements Disposer.Record {
        private final D3DContext context;
        private long nativeHandle;

        D3DPhongMaterialDisposerRecord(D3DContext context, long nativeHandle) {
            this.context = context;
            this.nativeHandle = nativeHandle;
        }

        void traceDispose() {
        }

        @Override // com.sun.prism.impl.Disposer.Record
        public void dispose() {
            if (this.nativeHandle != 0) {
                traceDispose();
                this.context.releaseD3DPhongMaterial(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}
