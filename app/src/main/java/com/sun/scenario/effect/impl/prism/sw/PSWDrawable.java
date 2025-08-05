package com.sun.scenario.effect.impl.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import java.nio.IntBuffer;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/sw/PSWDrawable.class */
public class PSWDrawable extends PrDrawable implements HeapImage {
    private RTTexture rtt;
    private Image image;
    private boolean heapDirty;
    private boolean vramDirty;

    private PSWDrawable(RTTexture rtt, boolean isDirty) {
        super(rtt);
        this.rtt = rtt;
        this.vramDirty = isDirty;
    }

    public static PSWDrawable create(RTTexture rtt) {
        return new PSWDrawable(rtt, true);
    }

    static int getCompatibleWidth(Screen screen, int w2) {
        ResourceFactory factory = GraphicsPipeline.getPipeline().getResourceFactory(screen);
        return factory.getRTTWidth(w2, Texture.WrapMode.CLAMP_TO_ZERO);
    }

    static int getCompatibleHeight(Screen screen, int h2) {
        ResourceFactory factory = GraphicsPipeline.getPipeline().getResourceFactory(screen);
        return factory.getRTTHeight(h2, Texture.WrapMode.CLAMP_TO_ZERO);
    }

    static PSWDrawable create(Screen screen, int width, int height) {
        ResourceFactory factory = GraphicsPipeline.getPipeline().getResourceFactory(screen);
        RTTexture rtt = factory.createRTTexture(width, height, Texture.WrapMode.CLAMP_TO_ZERO);
        return new PSWDrawable(rtt, false);
    }

    @Override // com.sun.scenario.effect.impl.prism.PrTexture, com.sun.scenario.effect.LockableResource
    public boolean isLost() {
        return this.rtt == null || this.rtt.isSurfaceLost();
    }

    @Override // com.sun.scenario.effect.Filterable
    public void flush() {
        if (this.rtt != null) {
            this.rtt.dispose();
            this.rtt = null;
            this.image = null;
        }
    }

    @Override // com.sun.scenario.effect.Filterable
    public Object getData() {
        return this;
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getContentWidth() {
        return this.rtt.getContentWidth();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getContentHeight() {
        return this.rtt.getContentHeight();
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable, com.sun.scenario.effect.Filterable
    public int getMaxContentWidth() {
        return this.rtt.getMaxContentWidth();
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable, com.sun.scenario.effect.Filterable
    public int getMaxContentHeight() {
        return this.rtt.getMaxContentHeight();
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable, com.sun.scenario.effect.Filterable
    public void setContentWidth(int contentW) {
        this.rtt.setContentWidth(contentW);
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable, com.sun.scenario.effect.Filterable
    public void setContentHeight(int contentH) {
        this.rtt.setContentHeight(contentH);
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getPhysicalWidth() {
        return this.rtt.getContentWidth();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getPhysicalHeight() {
        return this.rtt.getContentHeight();
    }

    @Override // com.sun.scenario.effect.impl.HeapImage
    public int getScanlineStride() {
        return this.rtt.getContentWidth();
    }

    @Override // com.sun.scenario.effect.impl.HeapImage
    public int[] getPixelArray() {
        int[] pixels = this.rtt.getPixels();
        if (pixels != null) {
            return pixels;
        }
        if (this.image == null) {
            int width = this.rtt.getContentWidth();
            int height = this.rtt.getContentHeight();
            this.image = Image.fromIntArgbPreData(new int[width * height], width, height);
        }
        IntBuffer buf = (IntBuffer) this.image.getPixelBuffer();
        if (this.vramDirty) {
            this.rtt.readPixels(buf);
            this.vramDirty = false;
        }
        this.heapDirty = true;
        return buf.array();
    }

    @Override // com.sun.scenario.effect.impl.prism.PrTexture
    public RTTexture getTextureObject() {
        if (this.heapDirty) {
            int width = this.rtt.getContentWidth();
            int height = this.rtt.getContentHeight();
            Screen screen = this.rtt.getAssociatedScreen();
            ResourceFactory factory = GraphicsPipeline.getPipeline().getResourceFactory(screen);
            Texture tex = factory.createTexture(this.image, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
            Graphics g2 = createGraphics();
            g2.drawTexture(tex, 0.0f, 0.0f, width, height);
            g2.sync();
            tex.dispose();
            this.heapDirty = false;
        }
        return this.rtt;
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable
    public Graphics createGraphics() {
        this.vramDirty = true;
        return this.rtt.createGraphics();
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable
    public void clear() {
        Graphics g2 = createGraphics();
        g2.clear();
        if (this.image != null) {
            IntBuffer buf = (IntBuffer) this.image.getPixelBuffer();
            Arrays.fill(buf.array(), 0);
        }
        this.heapDirty = false;
        this.vramDirty = false;
    }
}
