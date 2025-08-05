package com.sun.prism.sw;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.util.Logging;
import com.sun.openpisces.Renderer;
import com.sun.pisces.PiscesRenderer;
import com.sun.prism.BasicStroke;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.OpenPiscesPrismUtils;
import com.sun.prism.impl.shape.ShapeUtil;
import java.lang.ref.SoftReference;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWContext.class */
final class SWContext {
    private final ResourceFactory factory;
    private final ShapeRenderer shapeRenderer;
    private SoftReference<SWRTTexture> readBackBufferRef;
    private SoftReference<SWArgbPreTexture> imagePaintTextureRef;

    /* loaded from: jfxrt.jar:com/sun/prism/sw/SWContext$ShapeRenderer.class */
    interface ShapeRenderer {
        void renderShape(PiscesRenderer piscesRenderer, Shape shape, BasicStroke basicStroke, BaseTransform baseTransform, Rectangle rectangle, boolean z2);

        void dispose();
    }

    /* loaded from: jfxrt.jar:com/sun/prism/sw/SWContext$NativeShapeRenderer.class */
    class NativeShapeRenderer implements ShapeRenderer {
        private SoftReference<SWMaskTexture> maskTextureRef;

        NativeShapeRenderer() {
        }

        @Override // com.sun.prism.sw.SWContext.ShapeRenderer
        public void renderShape(PiscesRenderer pr, Shape shape, BasicStroke stroke, BaseTransform tr, Rectangle clip, boolean antialiasedShape) {
            try {
                MaskData mask = ShapeUtil.rasterizeShape(shape, stroke, clip.toRectBounds(), tr, true, antialiasedShape);
                SWMaskTexture tex = validateMaskTexture(mask.getWidth(), mask.getHeight());
                mask.uploadToTexture(tex, 0, 0, false);
                pr.fillAlphaMask(tex.getDataNoClone(), mask.getOriginX(), mask.getOriginY(), mask.getWidth(), mask.getHeight(), 0, tex.getPhysicalWidth());
            } catch (Throwable ex) {
                if (PrismSettings.verbose) {
                    ex.printStackTrace();
                }
                Logging.getJavaFXLogger().warning("Cannot rasterize Shape: " + ex.toString());
            }
        }

        private SWMaskTexture initMaskTexture(int width, int height) {
            SWMaskTexture tex = (SWMaskTexture) SWContext.this.factory.createMaskTexture(width, height, Texture.WrapMode.CLAMP_NOT_NEEDED);
            this.maskTextureRef = new SoftReference<>(tex);
            return tex;
        }

        private void disposeMaskTexture() {
            if (this.maskTextureRef != null) {
                this.maskTextureRef.clear();
                this.maskTextureRef = null;
            }
        }

        private SWMaskTexture validateMaskTexture(int width, int height) {
            SWMaskTexture tex;
            if (this.maskTextureRef == null) {
                tex = initMaskTexture(width, height);
            } else {
                tex = this.maskTextureRef.get();
                if (tex == null || tex.getPhysicalWidth() < width || tex.getPhysicalHeight() < height) {
                    disposeMaskTexture();
                    tex = initMaskTexture(width, height);
                }
            }
            return tex;
        }

        @Override // com.sun.prism.sw.SWContext.ShapeRenderer
        public void dispose() {
            disposeMaskTexture();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/sw/SWContext$JavaShapeRenderer.class */
    class JavaShapeRenderer implements ShapeRenderer {
        private final DirectRTPiscesAlphaConsumer alphaConsumer = new DirectRTPiscesAlphaConsumer();

        JavaShapeRenderer() {
        }

        @Override // com.sun.prism.sw.SWContext.ShapeRenderer
        public void renderShape(PiscesRenderer pr, Shape shape, BasicStroke stroke, BaseTransform tr, Rectangle clip, boolean antialiasedShape) {
            if (stroke != null && stroke.getType() != 0) {
                shape = stroke.createStrokedShape(shape);
                stroke = null;
            }
            try {
                Renderer r2 = OpenPiscesPrismUtils.setupRenderer(shape, stroke, tr, clip, antialiasedShape);
                this.alphaConsumer.initConsumer(r2, pr);
                r2.produceAlphas(this.alphaConsumer);
            } catch (Throwable ex) {
                if (PrismSettings.verbose) {
                    ex.printStackTrace();
                }
                Logging.getJavaFXLogger().warning("Cannot rasterize Shape: " + ex.toString());
            }
        }

        @Override // com.sun.prism.sw.SWContext.ShapeRenderer
        public void dispose() {
        }
    }

    SWContext(ResourceFactory factory) {
        this.factory = factory;
        this.shapeRenderer = PrismSettings.doNativePisces ? new NativeShapeRenderer() : new JavaShapeRenderer();
    }

    void renderShape(PiscesRenderer pr, Shape shape, BasicStroke stroke, BaseTransform tr, Rectangle clip, boolean antialiasedShape) {
        this.shapeRenderer.renderShape(pr, shape, stroke, tr, clip, antialiasedShape);
    }

    private SWRTTexture initRBBuffer(int width, int height) {
        SWRTTexture tex = (SWRTTexture) this.factory.createRTTexture(width, height, Texture.WrapMode.CLAMP_NOT_NEEDED);
        this.readBackBufferRef = new SoftReference<>(tex);
        return tex;
    }

    private void disposeRBBuffer() {
        if (this.readBackBufferRef != null) {
            this.readBackBufferRef.clear();
            this.readBackBufferRef = null;
        }
    }

    SWRTTexture validateRBBuffer(int width, int height) {
        SWRTTexture tex;
        if (this.readBackBufferRef == null) {
            tex = initRBBuffer(width, height);
        } else {
            tex = this.readBackBufferRef.get();
            if (tex == null || tex.getPhysicalWidth() < width || tex.getPhysicalHeight() < height) {
                disposeRBBuffer();
                tex = initRBBuffer(width, height);
            }
            tex.setContentWidth(width);
            tex.setContentHeight(height);
        }
        return tex;
    }

    private SWArgbPreTexture initImagePaintTexture(int width, int height) {
        SWArgbPreTexture tex = (SWArgbPreTexture) this.factory.createTexture(PixelFormat.INT_ARGB_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.REPEAT, width, height);
        this.imagePaintTextureRef = new SoftReference<>(tex);
        return tex;
    }

    private void disposeImagePaintTexture() {
        if (this.imagePaintTextureRef != null) {
            this.imagePaintTextureRef.clear();
            this.imagePaintTextureRef = null;
        }
    }

    SWArgbPreTexture validateImagePaintTexture(int width, int height) {
        SWArgbPreTexture tex;
        if (this.imagePaintTextureRef == null) {
            tex = initImagePaintTexture(width, height);
        } else {
            tex = this.imagePaintTextureRef.get();
            if (tex == null || tex.getPhysicalWidth() < width || tex.getPhysicalHeight() < height) {
                disposeImagePaintTexture();
                tex = initImagePaintTexture(width, height);
            }
            tex.setContentWidth(width);
            tex.setContentHeight(height);
        }
        return tex;
    }

    void dispose() {
        disposeRBBuffer();
        disposeImagePaintTexture();
        this.shapeRenderer.dispose();
    }
}
