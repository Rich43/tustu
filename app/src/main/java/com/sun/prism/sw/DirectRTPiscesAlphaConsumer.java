package com.sun.prism.sw;

import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Renderer;
import com.sun.pisces.PiscesRenderer;

/* loaded from: jfxrt.jar:com/sun/prism/sw/DirectRTPiscesAlphaConsumer.class */
final class DirectRTPiscesAlphaConsumer implements AlphaConsumer {
    private byte[] alpha_map;
    private int outpix_xmin;
    private int outpix_ymin;

    /* renamed from: w, reason: collision with root package name */
    private int f12030w;

    /* renamed from: h, reason: collision with root package name */
    private int f12031h;
    private int rowNum;
    private PiscesRenderer pr;

    DirectRTPiscesAlphaConsumer() {
    }

    void initConsumer(Renderer renderer, PiscesRenderer pr) {
        this.outpix_xmin = renderer.getOutpixMinX();
        this.outpix_ymin = renderer.getOutpixMinY();
        this.f12030w = renderer.getOutpixMaxX() - this.outpix_xmin;
        if (this.f12030w < 0) {
            this.f12030w = 0;
        }
        this.f12031h = renderer.getOutpixMaxY() - this.outpix_ymin;
        if (this.f12031h < 0) {
            this.f12031h = 0;
        }
        this.rowNum = 0;
        this.pr = pr;
    }

    @Override // com.sun.openpisces.AlphaConsumer
    public int getOriginX() {
        return this.outpix_xmin;
    }

    @Override // com.sun.openpisces.AlphaConsumer
    public int getOriginY() {
        return this.outpix_ymin;
    }

    @Override // com.sun.openpisces.AlphaConsumer
    public int getWidth() {
        return this.f12030w;
    }

    @Override // com.sun.openpisces.AlphaConsumer
    public int getHeight() {
        return this.f12031h;
    }

    @Override // com.sun.openpisces.AlphaConsumer
    public void setMaxAlpha(int maxalpha) {
        if (this.alpha_map == null || this.alpha_map.length != maxalpha + 1) {
            this.alpha_map = new byte[maxalpha + 1];
            for (int i2 = 0; i2 <= maxalpha; i2++) {
                this.alpha_map[i2] = (byte) (((i2 * 255) + (maxalpha / 2)) / maxalpha);
            }
        }
    }

    @Override // com.sun.openpisces.AlphaConsumer
    public void setAndClearRelativeAlphas(int[] alphaDeltas, int pix_y, int firstdelta, int lastdelta) {
        this.pr.emitAndClearAlphaRow(this.alpha_map, alphaDeltas, pix_y, firstdelta, lastdelta, this.rowNum);
        this.rowNum++;
    }
}
