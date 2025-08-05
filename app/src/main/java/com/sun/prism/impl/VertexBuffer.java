package com.sun.prism.impl;

import com.sun.javafx.geom.transform.AffineBase;
import com.sun.prism.paint.Color;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/prism/impl/VertexBuffer.class */
public final class VertexBuffer {
    protected static final int VERTS_PER_QUAD = 4;
    protected static final int FLOATS_PER_TC = 2;
    protected static final int FLOATS_PER_VC = 3;
    protected static final int FLOATS_PER_VERT = 7;
    protected static final int BYTES_PER_VERT = 4;
    protected static final int VCOFF = 0;
    protected static final int TC1OFF = 3;
    protected static final int TC2OFF = 5;
    protected int capacity;
    protected int index = 0;

    /* renamed from: r, reason: collision with root package name */
    protected byte f12016r;

    /* renamed from: g, reason: collision with root package name */
    protected byte f12017g;

    /* renamed from: b, reason: collision with root package name */
    protected byte f12018b;

    /* renamed from: a, reason: collision with root package name */
    protected byte f12019a;
    protected byte[] colorArray;
    protected float[] coordArray;
    private final BaseContext ownerCtx;

    public VertexBuffer(BaseContext owner, int maxQuads) {
        this.ownerCtx = owner;
        this.capacity = maxQuads * 4;
        this.colorArray = new byte[this.capacity * 4];
        this.coordArray = new float[this.capacity * 7];
    }

    public final void setPerVertexColor(Color c2, float extraAlpha) {
        float ca = c2.getAlpha() * extraAlpha;
        this.f12016r = (byte) (c2.getRed() * ca * 255.0f);
        this.f12017g = (byte) (c2.getGreen() * ca * 255.0f);
        this.f12018b = (byte) (c2.getBlue() * ca * 255.0f);
        this.f12019a = (byte) (ca * 255.0f);
    }

    public final void setPerVertexColor(float extraAlpha) {
        byte b2 = (byte) (extraAlpha * 255.0f);
        this.f12019a = b2;
        this.f12018b = b2;
        this.f12017g = b2;
        this.f12016r = b2;
    }

    public final void updateVertexColors(int numVerts) {
        for (int i2 = 0; i2 != numVerts; i2++) {
            putColor(i2);
        }
    }

    private void putColor(int idx) {
        int i2 = idx * 4;
        this.colorArray[i2 + 0] = this.f12016r;
        this.colorArray[i2 + 1] = this.f12017g;
        this.colorArray[i2 + 2] = this.f12018b;
        this.colorArray[i2 + 3] = this.f12019a;
    }

    public final void flush() {
        if (this.index > 0) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, this.index);
            this.index = 0;
        }
    }

    public final void rewind() {
        this.index = 0;
    }

    private void grow() {
        this.capacity *= 2;
        this.colorArray = Arrays.copyOf(this.colorArray, this.capacity * 4);
        this.coordArray = Arrays.copyOf(this.coordArray, this.capacity * 7);
    }

    public final void addVert(float x2, float y2) {
        if (this.index == this.capacity) {
            grow();
        }
        int i2 = 7 * this.index;
        this.coordArray[i2 + 0] = x2;
        this.coordArray[i2 + 1] = y2;
        this.coordArray[i2 + 2] = 0.0f;
        putColor(this.index);
        this.index++;
    }

    public final void addVert(float x2, float y2, float tx, float ty) {
        if (this.index == this.capacity) {
            grow();
        }
        int i2 = 7 * this.index;
        this.coordArray[i2 + 0] = x2;
        this.coordArray[i2 + 1] = y2;
        this.coordArray[i2 + 2] = 0.0f;
        this.coordArray[i2 + 3] = tx;
        this.coordArray[i2 + 4] = ty;
        putColor(this.index);
        this.index++;
    }

    public final void addVert(float x2, float y2, float t0x, float t0y, float t1x, float t1y) {
        if (this.index == this.capacity) {
            grow();
        }
        int i2 = 7 * this.index;
        this.coordArray[i2 + 0] = x2;
        this.coordArray[i2 + 1] = y2;
        this.coordArray[i2 + 2] = 0.0f;
        this.coordArray[i2 + 3] = t0x;
        this.coordArray[i2 + 4] = t0y;
        this.coordArray[i2 + 5] = t1x;
        this.coordArray[i2 + 6] = t1y;
        putColor(this.index);
        this.index++;
    }

    private void addVertNoCheck(float x2, float y2) {
        int i2 = 7 * this.index;
        this.coordArray[i2 + 0] = x2;
        this.coordArray[i2 + 1] = y2;
        this.coordArray[i2 + 2] = 0.0f;
        putColor(this.index);
        this.index++;
    }

    private void addVertNoCheck(float x2, float y2, float tx, float ty) {
        int i2 = 7 * this.index;
        this.coordArray[i2 + 0] = x2;
        this.coordArray[i2 + 1] = y2;
        this.coordArray[i2 + 2] = 0.0f;
        this.coordArray[i2 + 3] = tx;
        this.coordArray[i2 + 4] = ty;
        putColor(this.index);
        this.index++;
    }

    private void addVertNoCheck(float x2, float y2, float t0x, float t0y, float t1x, float t1y) {
        int i2 = 7 * this.index;
        this.coordArray[i2 + 0] = x2;
        this.coordArray[i2 + 1] = y2;
        this.coordArray[i2 + 2] = 0.0f;
        this.coordArray[i2 + 3] = t0x;
        this.coordArray[i2 + 4] = t0y;
        this.coordArray[i2 + 5] = t1x;
        this.coordArray[i2 + 6] = t1y;
        putColor(this.index);
        this.index++;
    }

    private void ensureCapacityForQuad() {
        if (this.index + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, this.index);
            this.index = 0;
        }
    }

    public final void addQuad(float dx1, float dy1, float dx2, float dy2) {
        ensureCapacityForQuad();
        addVertNoCheck(dx1, dy1);
        addVertNoCheck(dx1, dy2);
        addVertNoCheck(dx2, dy1);
        addVertNoCheck(dx2, dy2);
    }

    public final void addQuad(float dx1, float dy1, float dx2, float dy2, float t1x1, float t1y1, float t1x2, float t1y2, float t2x1, float t2y1, float t2x2, float t2y2) {
        ensureCapacityForQuad();
        addVertNoCheck(dx1, dy1, t1x1, t1y1, t2x1, t2y1);
        addVertNoCheck(dx1, dy2, t1x1, t1y2, t2x1, t2y2);
        addVertNoCheck(dx2, dy1, t1x2, t1y1, t2x2, t2y1);
        addVertNoCheck(dx2, dy2, t1x2, t1y2, t2x2, t2y2);
    }

    public final void addMappedQuad(float dx1, float dy1, float dx2, float dy2, float tx11, float ty11, float tx21, float ty21, float tx12, float ty12, float tx22, float ty22) {
        ensureCapacityForQuad();
        addVertNoCheck(dx1, dy1, tx11, ty11);
        addVertNoCheck(dx1, dy2, tx12, ty12);
        addVertNoCheck(dx2, dy1, tx21, ty21);
        addVertNoCheck(dx2, dy2, tx22, ty22);
    }

    public final void addMappedQuad(float dx1, float dy1, float dx2, float dy2, float ux11, float uy11, float ux21, float uy21, float ux12, float uy12, float ux22, float uy22, float vx11, float vy11, float vx21, float vy21, float vx12, float vy12, float vx22, float vy22) {
        ensureCapacityForQuad();
        addVertNoCheck(dx1, dy1, ux11, uy11, vx11, vy11);
        addVertNoCheck(dx1, dy2, ux12, uy12, vx12, vy12);
        addVertNoCheck(dx2, dy1, ux21, uy21, vx21, vy21);
        addVertNoCheck(dx2, dy2, ux22, uy22, vx22, vy22);
    }

    public final void addQuad(float dx1, float dy1, float dx2, float dy2, float tx1, float ty1, float tx2, float ty2, AffineBase tx) {
        addQuad(dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2);
        if (tx != null) {
            int i2 = (7 * this.index) - 7;
            tx.transform(this.coordArray, i2 + 0, this.coordArray, i2 + 5, 1);
            int i3 = i2 - 7;
            tx.transform(this.coordArray, i3 + 0, this.coordArray, i3 + 5, 1);
            int i4 = i3 - 7;
            tx.transform(this.coordArray, i4 + 0, this.coordArray, i4 + 5, 1);
            int i5 = i4 - 7;
            tx.transform(this.coordArray, i5 + 0, this.coordArray, i5 + 5, 1);
        }
    }

    public final void addSuperQuad(float dx1, float dy1, float dx2, float dy2, float tx1, float ty1, float tx2, float ty2, boolean isText) {
        int idx = this.index;
        if (idx + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, idx);
            this.index = 0;
            idx = 0;
        }
        int i2 = 7 * idx;
        float[] farr = this.coordArray;
        float text = isText ? 1.0f : 0.0f;
        float image = isText ? 0.0f : 1.0f;
        farr[i2] = dx1;
        int i3 = i2 + 1;
        farr[i3] = dy1;
        int i4 = i3 + 1;
        farr[i4] = 0.0f;
        int i5 = i4 + 1;
        farr[i5] = tx1;
        int i6 = i5 + 1;
        farr[i6] = ty1;
        int i7 = i6 + 1;
        farr[i7] = image;
        int i8 = i7 + 1;
        farr[i8] = text;
        int i9 = i8 + 1;
        farr[i9] = dx1;
        int i10 = i9 + 1;
        farr[i10] = dy2;
        int i11 = i10 + 1;
        farr[i11] = 0.0f;
        int i12 = i11 + 1;
        farr[i12] = tx1;
        int i13 = i12 + 1;
        farr[i13] = ty2;
        int i14 = i13 + 1;
        farr[i14] = image;
        int i15 = i14 + 1;
        farr[i15] = text;
        int i16 = i15 + 1;
        farr[i16] = dx2;
        int i17 = i16 + 1;
        farr[i17] = dy1;
        int i18 = i17 + 1;
        farr[i18] = 0.0f;
        int i19 = i18 + 1;
        farr[i19] = tx2;
        int i20 = i19 + 1;
        farr[i20] = ty1;
        int i21 = i20 + 1;
        farr[i21] = image;
        int i22 = i21 + 1;
        farr[i22] = text;
        int i23 = i22 + 1;
        farr[i23] = dx2;
        int i24 = i23 + 1;
        farr[i24] = dy2;
        int i25 = i24 + 1;
        farr[i25] = 0.0f;
        int i26 = i25 + 1;
        farr[i26] = tx2;
        int i27 = i26 + 1;
        farr[i27] = ty2;
        int i28 = i27 + 1;
        farr[i28] = image;
        int i29 = i28 + 1;
        farr[i29] = text;
        int i30 = i29 + 1;
        byte[] barr = this.colorArray;
        byte r2 = this.f12016r;
        byte g2 = this.f12017g;
        byte b2 = this.f12018b;
        byte a2 = this.f12019a;
        int j2 = 4 * idx;
        barr[j2] = r2;
        int j3 = j2 + 1;
        barr[j3] = g2;
        int j4 = j3 + 1;
        barr[j4] = b2;
        int j5 = j4 + 1;
        barr[j5] = a2;
        int j6 = j5 + 1;
        barr[j6] = r2;
        int j7 = j6 + 1;
        barr[j7] = g2;
        int j8 = j7 + 1;
        barr[j8] = b2;
        int j9 = j8 + 1;
        barr[j9] = a2;
        int j10 = j9 + 1;
        barr[j10] = r2;
        int j11 = j10 + 1;
        barr[j11] = g2;
        int j12 = j11 + 1;
        barr[j12] = b2;
        int j13 = j12 + 1;
        barr[j13] = a2;
        int j14 = j13 + 1;
        barr[j14] = r2;
        int j15 = j14 + 1;
        barr[j15] = g2;
        int j16 = j15 + 1;
        barr[j16] = b2;
        barr[j16 + 1] = a2;
        this.index = idx + 4;
    }

    public final void addQuad(float dx1, float dy1, float dx2, float dy2, float tx1, float ty1, float tx2, float ty2) {
        int idx = this.index;
        if (idx + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, idx);
            this.index = 0;
            idx = 0;
        }
        int i2 = 7 * idx;
        float[] farr = this.coordArray;
        farr[i2] = dx1;
        int i3 = i2 + 1;
        farr[i3] = dy1;
        int i4 = i3 + 1;
        farr[i4] = 0.0f;
        int i5 = i4 + 1;
        farr[i5] = tx1;
        int i6 = i5 + 1;
        farr[i6] = ty1;
        int i7 = i6 + 3;
        farr[i7] = dx1;
        int i8 = i7 + 1;
        farr[i8] = dy2;
        int i9 = i8 + 1;
        farr[i9] = 0.0f;
        int i10 = i9 + 1;
        farr[i10] = tx1;
        int i11 = i10 + 1;
        farr[i11] = ty2;
        int i12 = i11 + 3;
        farr[i12] = dx2;
        int i13 = i12 + 1;
        farr[i13] = dy1;
        int i14 = i13 + 1;
        farr[i14] = 0.0f;
        int i15 = i14 + 1;
        farr[i15] = tx2;
        int i16 = i15 + 1;
        farr[i16] = ty1;
        int i17 = i16 + 3;
        farr[i17] = dx2;
        int i18 = i17 + 1;
        farr[i18] = dy2;
        int i19 = i18 + 1;
        farr[i19] = 0.0f;
        int i20 = i19 + 1;
        farr[i20] = tx2;
        farr[i20 + 1] = ty2;
        byte[] barr = this.colorArray;
        byte r2 = this.f12016r;
        byte g2 = this.f12017g;
        byte b2 = this.f12018b;
        byte a2 = this.f12019a;
        int j2 = 4 * idx;
        barr[j2] = r2;
        int j3 = j2 + 1;
        barr[j3] = g2;
        int j4 = j3 + 1;
        barr[j4] = b2;
        int j5 = j4 + 1;
        barr[j5] = a2;
        int j6 = j5 + 1;
        barr[j6] = r2;
        int j7 = j6 + 1;
        barr[j7] = g2;
        int j8 = j7 + 1;
        barr[j8] = b2;
        int j9 = j8 + 1;
        barr[j9] = a2;
        int j10 = j9 + 1;
        barr[j10] = r2;
        int j11 = j10 + 1;
        barr[j11] = g2;
        int j12 = j11 + 1;
        barr[j12] = b2;
        int j13 = j12 + 1;
        barr[j13] = a2;
        int j14 = j13 + 1;
        barr[j14] = r2;
        int j15 = j14 + 1;
        barr[j15] = g2;
        int j16 = j15 + 1;
        barr[j16] = b2;
        barr[j16 + 1] = a2;
        this.index = idx + 4;
    }

    public final void addQuadVO(float topopacity, float botopacity, float dx1, float dy1, float dx2, float dy2, float tx1, float ty1, float tx2, float ty2) {
        int idx = this.index;
        if (idx + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, idx);
            this.index = 0;
            idx = 0;
        }
        int i2 = 7 * idx;
        float[] farr = this.coordArray;
        farr[i2] = dx1;
        int i3 = i2 + 1;
        farr[i3] = dy1;
        int i4 = i3 + 1;
        farr[i4] = 0.0f;
        int i5 = i4 + 1;
        farr[i5] = tx1;
        int i6 = i5 + 1;
        farr[i6] = ty1;
        int i7 = i6 + 3;
        farr[i7] = dx1;
        int i8 = i7 + 1;
        farr[i8] = dy2;
        int i9 = i8 + 1;
        farr[i9] = 0.0f;
        int i10 = i9 + 1;
        farr[i10] = tx1;
        int i11 = i10 + 1;
        farr[i11] = ty2;
        int i12 = i11 + 3;
        farr[i12] = dx2;
        int i13 = i12 + 1;
        farr[i13] = dy1;
        int i14 = i13 + 1;
        farr[i14] = 0.0f;
        int i15 = i14 + 1;
        farr[i15] = tx2;
        int i16 = i15 + 1;
        farr[i16] = ty1;
        int i17 = i16 + 3;
        farr[i17] = dx2;
        int i18 = i17 + 1;
        farr[i18] = dy2;
        int i19 = i18 + 1;
        farr[i19] = 0.0f;
        int i20 = i19 + 1;
        farr[i20] = tx2;
        farr[i20 + 1] = ty2;
        byte[] barr = this.colorArray;
        int j2 = 4 * idx;
        byte to = (byte) (topopacity * 255.0f);
        byte bo2 = (byte) (botopacity * 255.0f);
        barr[j2] = to;
        int j3 = j2 + 1;
        barr[j3] = to;
        int j4 = j3 + 1;
        barr[j4] = to;
        int j5 = j4 + 1;
        barr[j5] = to;
        int j6 = j5 + 1;
        barr[j6] = bo2;
        int j7 = j6 + 1;
        barr[j7] = bo2;
        int j8 = j7 + 1;
        barr[j8] = bo2;
        int j9 = j8 + 1;
        barr[j9] = bo2;
        int j10 = j9 + 1;
        barr[j10] = to;
        int j11 = j10 + 1;
        barr[j11] = to;
        int j12 = j11 + 1;
        barr[j12] = to;
        int j13 = j12 + 1;
        barr[j13] = to;
        int j14 = j13 + 1;
        barr[j14] = bo2;
        int j15 = j14 + 1;
        barr[j15] = bo2;
        int j16 = j15 + 1;
        barr[j16] = bo2;
        barr[j16 + 1] = bo2;
        this.index = idx + 4;
    }

    public final void addMappedPgram(float dx11, float dy11, float dx21, float dy21, float dx12, float dy12, float dx22, float dy22, float ux11, float uy11, float ux21, float uy21, float ux12, float uy12, float ux22, float uy22, float vx11, float vy11, float vx22, float vy22, AffineBase tx) {
        addMappedPgram(dx11, dy11, dx21, dy21, dx12, dy12, dx22, dy22, ux11, uy11, ux21, uy21, ux12, uy12, ux22, uy22, vx11, vy11, vx22, vy11, vx11, vy22, vx22, vy22);
        int i2 = (7 * this.index) - 7;
        tx.transform(this.coordArray, i2 + 5, this.coordArray, i2 + 5, 1);
        int i3 = i2 - 7;
        tx.transform(this.coordArray, i3 + 5, this.coordArray, i3 + 5, 1);
        int i4 = i3 - 7;
        tx.transform(this.coordArray, i4 + 5, this.coordArray, i4 + 5, 1);
        int i5 = i4 - 7;
        tx.transform(this.coordArray, i5 + 5, this.coordArray, i5 + 5, 1);
    }

    public final void addMappedPgram(float dx11, float dy11, float dx21, float dy21, float dx12, float dy12, float dx22, float dy22, float ux11, float uy11, float ux21, float uy21, float ux12, float uy12, float ux22, float uy22, float vx, float vy) {
        int idx = this.index;
        if (idx + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, idx);
            this.index = 0;
            idx = 0;
        }
        int i2 = 7 * idx;
        float[] farr = this.coordArray;
        farr[i2] = dx11;
        int i3 = i2 + 1;
        farr[i3] = dy11;
        int i4 = i3 + 1;
        farr[i4] = 0.0f;
        int i5 = i4 + 1;
        farr[i5] = ux11;
        int i6 = i5 + 1;
        farr[i6] = uy11;
        int i7 = i6 + 1;
        farr[i7] = vx;
        int i8 = i7 + 1;
        farr[i8] = vy;
        int i9 = i8 + 1;
        farr[i9] = dx12;
        int i10 = i9 + 1;
        farr[i10] = dy12;
        int i11 = i10 + 1;
        farr[i11] = 0.0f;
        int i12 = i11 + 1;
        farr[i12] = ux12;
        int i13 = i12 + 1;
        farr[i13] = uy12;
        int i14 = i13 + 1;
        farr[i14] = vx;
        int i15 = i14 + 1;
        farr[i15] = vy;
        int i16 = i15 + 1;
        farr[i16] = dx21;
        int i17 = i16 + 1;
        farr[i17] = dy21;
        int i18 = i17 + 1;
        farr[i18] = 0.0f;
        int i19 = i18 + 1;
        farr[i19] = ux21;
        int i20 = i19 + 1;
        farr[i20] = uy21;
        int i21 = i20 + 1;
        farr[i21] = vx;
        int i22 = i21 + 1;
        farr[i22] = vy;
        int i23 = i22 + 1;
        farr[i23] = dx22;
        int i24 = i23 + 1;
        farr[i24] = dy22;
        int i25 = i24 + 1;
        farr[i25] = 0.0f;
        int i26 = i25 + 1;
        farr[i26] = ux22;
        int i27 = i26 + 1;
        farr[i27] = uy22;
        int i28 = i27 + 1;
        farr[i28] = vx;
        farr[i28 + 1] = vy;
        byte[] barr = this.colorArray;
        byte r2 = this.f12016r;
        byte g2 = this.f12017g;
        byte b2 = this.f12018b;
        byte a2 = this.f12019a;
        int j2 = 4 * idx;
        barr[j2] = r2;
        int j3 = j2 + 1;
        barr[j3] = g2;
        int j4 = j3 + 1;
        barr[j4] = b2;
        int j5 = j4 + 1;
        barr[j5] = a2;
        int j6 = j5 + 1;
        barr[j6] = r2;
        int j7 = j6 + 1;
        barr[j7] = g2;
        int j8 = j7 + 1;
        barr[j8] = b2;
        int j9 = j8 + 1;
        barr[j9] = a2;
        int j10 = j9 + 1;
        barr[j10] = r2;
        int j11 = j10 + 1;
        barr[j11] = g2;
        int j12 = j11 + 1;
        barr[j12] = b2;
        int j13 = j12 + 1;
        barr[j13] = a2;
        int j14 = j13 + 1;
        barr[j14] = r2;
        int j15 = j14 + 1;
        barr[j15] = g2;
        int j16 = j15 + 1;
        barr[j16] = b2;
        barr[j16 + 1] = a2;
        this.index = idx + 4;
    }

    public final void addMappedPgram(float dx11, float dy11, float dx21, float dy21, float dx12, float dy12, float dx22, float dy22, float ux11, float uy11, float ux21, float uy21, float ux12, float uy12, float ux22, float uy22, float vx11, float vy11, float vx21, float vy21, float vx12, float vy12, float vx22, float vy22) {
        int idx = this.index;
        if (idx + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, idx);
            this.index = 0;
            idx = 0;
        }
        int i2 = 7 * idx;
        float[] farr = this.coordArray;
        farr[i2] = dx11;
        int i3 = i2 + 1;
        farr[i3] = dy11;
        int i4 = i3 + 1;
        farr[i4] = 0.0f;
        int i5 = i4 + 1;
        farr[i5] = ux11;
        int i6 = i5 + 1;
        farr[i6] = uy11;
        int i7 = i6 + 1;
        farr[i7] = vx11;
        int i8 = i7 + 1;
        farr[i8] = vy11;
        int i9 = i8 + 1;
        farr[i9] = dx12;
        int i10 = i9 + 1;
        farr[i10] = dy12;
        int i11 = i10 + 1;
        farr[i11] = 0.0f;
        int i12 = i11 + 1;
        farr[i12] = ux12;
        int i13 = i12 + 1;
        farr[i13] = uy12;
        int i14 = i13 + 1;
        farr[i14] = vx12;
        int i15 = i14 + 1;
        farr[i15] = vy12;
        int i16 = i15 + 1;
        farr[i16] = dx21;
        int i17 = i16 + 1;
        farr[i17] = dy21;
        int i18 = i17 + 1;
        farr[i18] = 0.0f;
        int i19 = i18 + 1;
        farr[i19] = ux21;
        int i20 = i19 + 1;
        farr[i20] = uy21;
        int i21 = i20 + 1;
        farr[i21] = vx21;
        int i22 = i21 + 1;
        farr[i22] = vy21;
        int i23 = i22 + 1;
        farr[i23] = dx22;
        int i24 = i23 + 1;
        farr[i24] = dy22;
        int i25 = i24 + 1;
        farr[i25] = 0.0f;
        int i26 = i25 + 1;
        farr[i26] = ux22;
        int i27 = i26 + 1;
        farr[i27] = uy22;
        int i28 = i27 + 1;
        farr[i28] = vx22;
        farr[i28 + 1] = vy22;
        byte[] barr = this.colorArray;
        byte r2 = this.f12016r;
        byte g2 = this.f12017g;
        byte b2 = this.f12018b;
        byte a2 = this.f12019a;
        int j2 = 4 * idx;
        barr[j2] = r2;
        int j3 = j2 + 1;
        barr[j3] = g2;
        int j4 = j3 + 1;
        barr[j4] = b2;
        int j5 = j4 + 1;
        barr[j5] = a2;
        int j6 = j5 + 1;
        barr[j6] = r2;
        int j7 = j6 + 1;
        barr[j7] = g2;
        int j8 = j7 + 1;
        barr[j8] = b2;
        int j9 = j8 + 1;
        barr[j9] = a2;
        int j10 = j9 + 1;
        barr[j10] = r2;
        int j11 = j10 + 1;
        barr[j11] = g2;
        int j12 = j11 + 1;
        barr[j12] = b2;
        int j13 = j12 + 1;
        barr[j13] = a2;
        int j14 = j13 + 1;
        barr[j14] = r2;
        int j15 = j14 + 1;
        barr[j15] = g2;
        int j16 = j15 + 1;
        barr[j16] = b2;
        barr[j16 + 1] = a2;
        this.index = idx + 4;
    }
}
