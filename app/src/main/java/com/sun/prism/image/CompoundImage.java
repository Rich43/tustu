package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;

/* loaded from: jfxrt.jar:com/sun/prism/image/CompoundImage.class */
public abstract class CompoundImage {
    public static final int BORDER_SIZE_DEFAULT = 1;
    protected final int[] uSubdivision;
    protected final int[] u0;
    protected final int[] u1;
    protected final int[] vSubdivision;
    protected final int[] v0;
    protected final int[] v1;
    protected final int uSections;
    protected final int vSections;
    protected final int uBorderSize;
    protected final int vBorderSize;
    protected Image[] tiles;

    protected abstract Texture getTile(int i2, int i3, ResourceFactory resourceFactory);

    public CompoundImage(Image image, int maxSize) {
        this(image, maxSize, 1);
    }

    public CompoundImage(Image image, int maxSize, int borderSize) {
        borderSize = 4 * borderSize >= maxSize ? maxSize / 4 : borderSize;
        int imgW = image.getWidth();
        int imgH = image.getHeight();
        this.uBorderSize = imgW <= maxSize ? 0 : borderSize;
        this.vBorderSize = imgH <= maxSize ? 0 : borderSize;
        this.uSubdivision = subdivideUVs(imgW, maxSize, this.uBorderSize);
        this.vSubdivision = subdivideUVs(imgH, maxSize, this.vBorderSize);
        this.uSections = this.uSubdivision.length - 1;
        this.vSections = this.vSubdivision.length - 1;
        this.u0 = new int[this.uSections];
        this.u1 = new int[this.uSections];
        this.v0 = new int[this.vSections];
        this.v1 = new int[this.vSections];
        this.tiles = new Image[this.uSections * this.vSections];
        for (int y2 = 0; y2 != this.vSections; y2++) {
            this.v0[y2] = this.vSubdivision[y2] - uBorder(y2);
            this.v1[y2] = this.vSubdivision[y2 + 1] + dBorder(y2);
        }
        for (int x2 = 0; x2 != this.uSections; x2++) {
            this.u0[x2] = this.uSubdivision[x2] - lBorder(x2);
            this.u1[x2] = this.uSubdivision[x2 + 1] + rBorder(x2);
        }
        for (int y3 = 0; y3 != this.vSections; y3++) {
            for (int x3 = 0; x3 != this.uSections; x3++) {
                this.tiles[(y3 * this.uSections) + x3] = image.createSubImage(this.u0[x3], this.v0[y3], this.u1[x3] - this.u0[x3], this.v1[y3] - this.v0[y3]);
            }
        }
    }

    private int lBorder(int i2) {
        if (i2 > 0) {
            return this.uBorderSize;
        }
        return 0;
    }

    private int rBorder(int i2) {
        if (i2 < this.uSections - 1) {
            return this.uBorderSize;
        }
        return 0;
    }

    private int uBorder(int i2) {
        if (i2 > 0) {
            return this.vBorderSize;
        }
        return 0;
    }

    private int dBorder(int i2) {
        if (i2 < this.vSections - 1) {
            return this.vBorderSize;
        }
        return 0;
    }

    private static int[] subdivideUVs(int size, int maxSize, int borderSize) {
        int contSize = maxSize - (borderSize * 2);
        int nImages = (((size - (borderSize * 2)) + contSize) - 1) / contSize;
        int[] data = new int[nImages + 1];
        data[0] = 0;
        data[nImages] = size;
        for (int i2 = 1; i2 < nImages; i2++) {
            data[i2] = borderSize + (contSize * i2);
        }
        return data;
    }

    public void drawLazy(Graphics g2, Coords crd, float x2, float y2) {
        new CompoundCoords(this, crd).draw(g2, this, x2, y2);
    }
}
