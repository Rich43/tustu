package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;

/* loaded from: jfxrt.jar:com/sun/prism/image/CompoundCoords.class */
public class CompoundCoords {
    private int xImg0;
    private int xImg1;
    private int yImg0;
    private int yImg1;
    private Coords[] tileCoords;

    public CompoundCoords(CompoundImage t2, Coords crd) {
        int xImg0 = find1(fastFloor(crd.u0), t2.uSubdivision);
        int xImg1 = find2(fastCeil(crd.u1), t2.uSubdivision);
        int yImg0 = find1(fastFloor(crd.v0), t2.vSubdivision);
        int yImg1 = find2(fastCeil(crd.v1), t2.vSubdivision);
        if (xImg0 < 0 || xImg1 < 0 || yImg0 < 0 || yImg1 < 0) {
            return;
        }
        this.xImg0 = xImg0;
        this.xImg1 = xImg1;
        this.yImg0 = yImg0;
        this.yImg1 = yImg1;
        this.tileCoords = new Coords[((xImg1 - xImg0) + 1) * ((yImg1 - yImg0) + 1)];
        float[] xMedian = new float[xImg1 - xImg0];
        float[] yMedian = new float[yImg1 - yImg0];
        for (int x2 = xImg0; x2 < xImg1; x2++) {
            xMedian[x2 - xImg0] = crd.getX(t2.uSubdivision[x2 + 1]);
        }
        for (int y2 = yImg0; y2 < yImg1; y2++) {
            yMedian[y2 - yImg0] = crd.getY(t2.vSubdivision[y2 + 1]);
        }
        int idx = 0;
        int y3 = yImg0;
        while (y3 <= yImg1) {
            float v0 = (y3 == yImg0 ? crd.v0 : t2.vSubdivision[y3]) - t2.v0[y3];
            float v1 = (y3 == yImg1 ? crd.v1 : t2.vSubdivision[y3 + 1]) - t2.v0[y3];
            float y0 = y3 == yImg0 ? crd.y0 : yMedian[(y3 - yImg0) - 1];
            float y1 = y3 == yImg1 ? crd.y1 : yMedian[y3 - yImg0];
            int x3 = xImg0;
            while (x3 <= xImg1) {
                Coords segment = new Coords();
                segment.v0 = v0;
                segment.v1 = v1;
                segment.y0 = y0;
                segment.y1 = y1;
                segment.u0 = (x3 == xImg0 ? crd.u0 : t2.uSubdivision[x3]) - t2.u0[x3];
                segment.u1 = (x3 == xImg1 ? crd.u1 : t2.uSubdivision[x3 + 1]) - t2.u0[x3];
                segment.x0 = x3 == xImg0 ? crd.x0 : xMedian[(x3 - xImg0) - 1];
                segment.x1 = x3 == xImg1 ? crd.x1 : xMedian[x3 - xImg0];
                int i2 = idx;
                idx++;
                this.tileCoords[i2] = segment;
                x3++;
            }
            y3++;
        }
    }

    public void draw(Graphics g2, CompoundImage t2, float xS, float yS) {
        if (this.tileCoords == null) {
            return;
        }
        ResourceFactory factory = g2.getResourceFactory();
        int idx = 0;
        for (int y2 = this.yImg0; y2 <= this.yImg1; y2++) {
            for (int x2 = this.xImg0; x2 <= this.xImg1; x2++) {
                Texture tex = t2.getTile(x2, y2, factory);
                int i2 = idx;
                idx++;
                this.tileCoords[i2].draw(tex, g2, xS, yS);
                tex.unlock();
            }
        }
    }

    private static int find1(int x2, int[] array) {
        for (int i2 = 0; i2 < array.length - 1; i2++) {
            if (array[i2] <= x2 && x2 < array[i2 + 1]) {
                return i2;
            }
        }
        return -1;
    }

    private static int find2(int x2, int[] array) {
        for (int i2 = 0; i2 < array.length - 1; i2++) {
            if (array[i2] < x2 && x2 <= array[i2 + 1]) {
                return i2;
            }
        }
        return -1;
    }

    private static int fastFloor(float x2) {
        int ix = (int) x2;
        return ((float) ix) <= x2 ? ix : ix - 1;
    }

    private static int fastCeil(float x2) {
        int ix = (int) x2;
        return ((float) ix) >= x2 ? ix : ix + 1;
    }
}
