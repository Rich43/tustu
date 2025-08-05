package com.sun.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

/* loaded from: jfxrt.jar:com/sun/prism/Graphics.class */
public interface Graphics {
    BaseTransform getTransformNoClone();

    void setTransform(BaseTransform baseTransform);

    void setTransform(double d2, double d3, double d4, double d5, double d6, double d7);

    void setTransform3D(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13);

    void transform(BaseTransform baseTransform);

    void translate(float f2, float f3);

    void translate(float f2, float f3, float f4);

    void scale(float f2, float f3);

    void scale(float f2, float f3, float f4);

    void setPerspectiveTransform(GeneralTransform3D generalTransform3D);

    void setCamera(NGCamera nGCamera);

    NGCamera getCameraNoClone();

    void setDepthTest(boolean z2);

    boolean isDepthTest();

    void setDepthBuffer(boolean z2);

    boolean isDepthBuffer();

    boolean isAlphaTestShader();

    void setAntialiasedShape(boolean z2);

    boolean isAntialiasedShape();

    RectBounds getFinalClipNoClone();

    Rectangle getClipRect();

    Rectangle getClipRectNoClone();

    void setHasPreCullingBits(boolean z2);

    boolean hasPreCullingBits();

    void setClipRect(Rectangle rectangle);

    void setClipRectIndex(int i2);

    int getClipRectIndex();

    float getExtraAlpha();

    void setExtraAlpha(float f2);

    void setLights(NGLightBase[] nGLightBaseArr);

    NGLightBase[] getLights();

    Paint getPaint();

    void setPaint(Paint paint);

    BasicStroke getStroke();

    void setStroke(BasicStroke basicStroke);

    void setCompositeMode(CompositeMode compositeMode);

    CompositeMode getCompositeMode();

    void clear();

    void clear(Color color);

    void clearQuad(float f2, float f3, float f4, float f5);

    void fill(Shape shape);

    void fillQuad(float f2, float f3, float f4, float f5);

    void fillRect(float f2, float f3, float f4, float f5);

    void fillRoundRect(float f2, float f3, float f4, float f5, float f6, float f7);

    void fillEllipse(float f2, float f3, float f4, float f5);

    void draw(Shape shape);

    void drawLine(float f2, float f3, float f4, float f5);

    void drawRect(float f2, float f3, float f4, float f5);

    void drawRoundRect(float f2, float f3, float f4, float f5, float f6, float f7);

    void drawEllipse(float f2, float f3, float f4, float f5);

    void setNodeBounds(RectBounds rectBounds);

    void drawString(GlyphList glyphList, FontStrike fontStrike, float f2, float f3, Color color, int i2, int i3);

    void blit(RTTexture rTTexture, RTTexture rTTexture2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    void drawTexture(Texture texture, float f2, float f3, float f4, float f5);

    void drawTexture(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    void drawTexture3SliceH(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13);

    void drawTexture3SliceV(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13);

    void drawTexture9Slice(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17);

    void drawTextureVO(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11);

    void drawTextureRaw(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    void drawMappedTextureRaw(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13);

    void sync();

    Screen getAssociatedScreen();

    ResourceFactory getResourceFactory();

    RenderTarget getRenderTarget();

    void setRenderRoot(NodePath nodePath);

    NodePath getRenderRoot();

    void setState3D(boolean z2);

    boolean isState3D();

    void setup3DRendering();

    void setPixelScaleFactor(float f2);

    float getPixelScaleFactor();
}
