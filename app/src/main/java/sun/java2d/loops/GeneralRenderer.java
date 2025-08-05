package sun.java2d.loops;

import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.XorPixelWriter;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/loops/GeneralRenderer.class */
public final class GeneralRenderer {
    static final int OUTCODE_TOP = 1;
    static final int OUTCODE_BOTTOM = 2;
    static final int OUTCODE_LEFT = 4;
    static final int OUTCODE_RIGHT = 8;

    public static void register() {
        GraphicsPrimitiveMgr.register(new GraphicsPrimitive[]{new GraphicsPrimitiveProxy(GeneralRenderer.class, "SetFillRectANY", FillRect.methodSignature, FillRect.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "SetFillPathANY", FillPath.methodSignature, FillPath.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "SetFillSpansANY", FillSpans.methodSignature, FillSpans.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "SetDrawLineANY", DrawLine.methodSignature, DrawLine.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "SetDrawPolygonsANY", DrawPolygons.methodSignature, DrawPolygons.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "SetDrawPathANY", DrawPath.methodSignature, DrawPath.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "SetDrawRectANY", DrawRect.methodSignature, DrawRect.primTypeID, SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "XorFillRectANY", FillRect.methodSignature, FillRect.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "XorFillPathANY", FillPath.methodSignature, FillPath.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "XorFillSpansANY", FillSpans.methodSignature, FillSpans.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "XorDrawLineANY", DrawLine.methodSignature, DrawLine.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "XorDrawPolygonsANY", DrawPolygons.methodSignature, DrawPolygons.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "XorDrawPathANY", DrawPath.methodSignature, DrawPath.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "XorDrawRectANY", DrawRect.methodSignature, DrawRect.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "XorDrawGlyphListANY", DrawGlyphList.methodSignature, DrawGlyphList.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any), new GraphicsPrimitiveProxy(GeneralRenderer.class, "XorDrawGlyphListAAANY", DrawGlyphListAA.methodSignature, DrawGlyphListAA.primTypeID, SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any)});
    }

    static void doDrawPoly(SurfaceData surfaceData, PixelWriter pixelWriter, int[] iArr, int[] iArr2, int i2, int i3, Region region, int i4, int i5, boolean z2) {
        int[] iArrDoDrawLine = null;
        if (i3 <= 0) {
            return;
        }
        int i6 = iArr[i2] + i4;
        int i7 = i6;
        int i8 = iArr2[i2] + i5;
        int i9 = i8;
        while (true) {
            i3--;
            if (i3 <= 0) {
                break;
            }
            i2++;
            int i10 = iArr[i2] + i4;
            int i11 = iArr2[i2] + i5;
            iArrDoDrawLine = doDrawLine(surfaceData, pixelWriter, iArrDoDrawLine, region, i7, i9, i10, i11);
            i7 = i10;
            i9 = i11;
        }
        if (z2) {
            if (i7 != i6 || i9 != i8) {
                doDrawLine(surfaceData, pixelWriter, iArrDoDrawLine, region, i7, i9, i6, i8);
            }
        }
    }

    static void doSetRect(SurfaceData surfaceData, PixelWriter pixelWriter, int i2, int i3, int i4, int i5) {
        pixelWriter.setRaster((WritableRaster) surfaceData.getRaster(i2, i3, i4 - i2, i5 - i3));
        while (i3 < i5) {
            for (int i6 = i2; i6 < i4; i6++) {
                pixelWriter.writePixel(i6, i3);
            }
            i3++;
        }
    }

    static int[] doDrawLine(SurfaceData surfaceData, PixelWriter pixelWriter, int[] iArr, Region region, int i2, int i3, int i4, int i5) {
        boolean z2;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        if (iArr == null) {
            iArr = new int[8];
        }
        iArr[0] = i2;
        iArr[1] = i3;
        iArr[2] = i4;
        iArr[3] = i5;
        if (!adjustLine(iArr, region.getLoX(), region.getLoY(), region.getHiX(), region.getHiY())) {
            return iArr;
        }
        int i11 = iArr[0];
        int i12 = iArr[1];
        int i13 = iArr[2];
        int i14 = iArr[3];
        pixelWriter.setRaster((WritableRaster) surfaceData.getRaster(Math.min(i11, i13), Math.min(i12, i14), Math.abs(i11 - i13) + 1, Math.abs(i12 - i14) + 1));
        if (i11 == i13) {
            if (i12 > i14) {
                do {
                    pixelWriter.writePixel(i11, i12);
                    i12--;
                } while (i12 >= i14);
            } else {
                do {
                    pixelWriter.writePixel(i11, i12);
                    i12++;
                } while (i12 <= i14);
            }
        } else if (i12 == i14) {
            if (i11 > i13) {
                do {
                    pixelWriter.writePixel(i11, i12);
                    i11--;
                } while (i11 >= i13);
            } else {
                do {
                    pixelWriter.writePixel(i11, i12);
                    i11++;
                } while (i11 <= i13);
            }
        } else {
            int i15 = iArr[4];
            int i16 = iArr[5];
            int i17 = iArr[6];
            int i18 = iArr[7];
            if (i17 >= i18) {
                z2 = true;
                i6 = i18 * 2;
                i7 = i17 * 2;
                i8 = i15 < 0 ? -1 : 1;
                i9 = i16 < 0 ? -1 : 1;
                i17 = -i17;
                i10 = i13 - i11;
            } else {
                z2 = false;
                i6 = i17 * 2;
                i7 = i18 * 2;
                i8 = i16 < 0 ? -1 : 1;
                i9 = i15 < 0 ? -1 : 1;
                i18 = -i18;
                i10 = i14 - i12;
            }
            int i19 = -(i7 / 2);
            if (i12 != i3) {
                int i20 = i12 - i3;
                if (i20 < 0) {
                    i20 = -i20;
                }
                i19 += i20 * i17 * 2;
            }
            if (i11 != i2) {
                int i21 = i11 - i2;
                if (i21 < 0) {
                    i21 = -i21;
                }
                i19 += i21 * i18 * 2;
            }
            if (i10 < 0) {
                i10 = -i10;
            }
            if (z2) {
                do {
                    pixelWriter.writePixel(i11, i12);
                    i11 += i8;
                    i19 += i6;
                    if (i19 >= 0) {
                        i12 += i9;
                        i19 -= i7;
                    }
                    i10--;
                } while (i10 >= 0);
            } else {
                do {
                    pixelWriter.writePixel(i11, i12);
                    i12 += i8;
                    i19 += i6;
                    if (i19 >= 0) {
                        i11 += i9;
                        i19 -= i7;
                    }
                    i10--;
                } while (i10 >= 0);
            }
        }
        return iArr;
    }

    public static void doDrawRect(PixelWriter pixelWriter, SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5) {
        if (i4 < 0 || i5 < 0) {
            return;
        }
        int iDimAdd = Region.dimAdd(Region.dimAdd(i2, i4), 1);
        int iDimAdd2 = Region.dimAdd(Region.dimAdd(i3, i5), 1);
        Region boundsIntersectionXYXY = sunGraphics2D.getCompClip().getBoundsIntersectionXYXY(i2, i3, iDimAdd, iDimAdd2);
        if (boundsIntersectionXYXY.isEmpty()) {
            return;
        }
        int loX = boundsIntersectionXYXY.getLoX();
        int loY = boundsIntersectionXYXY.getLoY();
        int hiX = boundsIntersectionXYXY.getHiX();
        int hiY = boundsIntersectionXYXY.getHiY();
        if (i4 < 2 || i5 < 2) {
            doSetRect(surfaceData, pixelWriter, loX, loY, hiX, hiY);
            return;
        }
        if (loY == i3) {
            doSetRect(surfaceData, pixelWriter, loX, loY, hiX, loY + 1);
        }
        if (loX == i2) {
            doSetRect(surfaceData, pixelWriter, loX, loY + 1, loX + 1, hiY - 1);
        }
        if (hiX == iDimAdd) {
            doSetRect(surfaceData, pixelWriter, hiX - 1, loY + 1, hiX, hiY - 1);
        }
        if (hiY == iDimAdd2) {
            doSetRect(surfaceData, pixelWriter, loX, hiY - 1, hiX, hiY);
        }
    }

    static void doDrawGlyphList(SurfaceData surfaceData, PixelWriter pixelWriter, GlyphList glyphList, Region region) {
        int[] bounds = glyphList.getBounds();
        region.clipBoxToBounds(bounds);
        int i2 = bounds[0];
        int i3 = bounds[1];
        int i4 = bounds[2];
        int i5 = bounds[3];
        pixelWriter.setRaster((WritableRaster) surfaceData.getRaster(i2, i3, i4 - i2, i5 - i3));
        int numGlyphs = glyphList.getNumGlyphs();
        for (int i6 = 0; i6 < numGlyphs; i6++) {
            glyphList.setGlyphIndex(i6);
            int[] metrics = glyphList.getMetrics();
            int i7 = metrics[0];
            int i8 = metrics[1];
            int i9 = metrics[2];
            int i10 = i7 + i9;
            int i11 = i8 + metrics[3];
            int i12 = 0;
            if (i7 < i2) {
                i12 = i2 - i7;
                i7 = i2;
            }
            if (i8 < i3) {
                i12 += (i3 - i8) * i9;
                i8 = i3;
            }
            if (i10 > i4) {
                i10 = i4;
            }
            if (i11 > i5) {
                i11 = i5;
            }
            if (i10 > i7 && i11 > i8) {
                byte[] grayBits = glyphList.getGrayBits();
                int i13 = i9 - (i10 - i7);
                for (int i14 = i8; i14 < i11; i14++) {
                    for (int i15 = i7; i15 < i10; i15++) {
                        int i16 = i12;
                        i12++;
                        if (grayBits[i16] < 0) {
                            pixelWriter.writePixel(i15, i14);
                        }
                    }
                    i12 += i13;
                }
            }
        }
    }

    static int outcode(int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8;
        if (i3 < i5) {
            i8 = 1;
        } else if (i3 > i7) {
            i8 = 2;
        } else {
            i8 = 0;
        }
        if (i2 < i4) {
            i8 |= 4;
        } else if (i2 > i6) {
            i8 |= 8;
        }
        return i8;
    }

    public static boolean adjustLine(int[] iArr, int i2, int i3, int i4, int i5) {
        int i6;
        int i7;
        int i8 = i4 - 1;
        int i9 = i5 - 1;
        int i10 = iArr[0];
        int i11 = iArr[1];
        int i12 = iArr[2];
        int i13 = iArr[3];
        if (i8 < i2 || i9 < i3) {
            return false;
        }
        if (i10 == i12) {
            if (i10 < i2 || i10 > i8) {
                return false;
            }
            if (i11 > i13) {
                i11 = i13;
                i13 = i11;
            }
            if (i11 < i3) {
                i11 = i3;
            }
            if (i13 > i9) {
                i13 = i9;
            }
            if (i11 > i13) {
                return false;
            }
            iArr[1] = i11;
            iArr[3] = i13;
            return true;
        }
        if (i11 == i13) {
            if (i11 < i3 || i11 > i9) {
                return false;
            }
            if (i10 > i12) {
                i10 = i12;
                i12 = i10;
            }
            if (i10 < i2) {
                i10 = i2;
            }
            if (i12 > i8) {
                i12 = i8;
            }
            if (i10 > i12) {
                return false;
            }
            iArr[0] = i10;
            iArr[2] = i12;
            return true;
        }
        int i14 = i12 - i10;
        int i15 = i13 - i11;
        int i16 = i14 < 0 ? -i14 : i14;
        int i17 = i15 < 0 ? -i15 : i15;
        boolean z2 = i16 >= i17;
        int iOutcode = outcode(i10, i11, i2, i3, i8, i9);
        int iOutcode2 = outcode(i12, i13, i2, i3, i8, i9);
        while ((iOutcode | iOutcode2) != 0) {
            if ((iOutcode & iOutcode2) != 0) {
                return false;
            }
            if (iOutcode != 0) {
                if (0 != (iOutcode & 3)) {
                    if (0 != (iOutcode & 1)) {
                        i11 = i3;
                    } else {
                        i11 = i9;
                    }
                    int i18 = i11 - iArr[1];
                    if (i18 < 0) {
                        i18 = -i18;
                    }
                    int i19 = (2 * i18 * i16) + i17;
                    if (z2) {
                        i19 += (i17 - i16) - 1;
                    }
                    int i20 = i19 / (2 * i17);
                    if (i14 < 0) {
                        i20 = -i20;
                    }
                    i10 = iArr[0] + i20;
                } else if (0 != (iOutcode & 12)) {
                    if (0 != (iOutcode & 4)) {
                        i10 = i2;
                    } else {
                        i10 = i8;
                    }
                    int i21 = i10 - iArr[0];
                    if (i21 < 0) {
                        i21 = -i21;
                    }
                    int i22 = (2 * i21 * i17) + i16;
                    if (!z2) {
                        i22 += (i16 - i17) - 1;
                    }
                    int i23 = i22 / (2 * i16);
                    if (i15 < 0) {
                        i23 = -i23;
                    }
                    i11 = iArr[1] + i23;
                }
                iOutcode = outcode(i10, i11, i2, i3, i8, i9);
            } else {
                if (0 != (iOutcode2 & 3)) {
                    if (0 != (iOutcode2 & 1)) {
                        i13 = i3;
                    } else {
                        i13 = i9;
                    }
                    int i24 = i13 - iArr[3];
                    if (i24 < 0) {
                        i24 = -i24;
                    }
                    int i25 = (2 * i24 * i16) + i17;
                    if (z2) {
                        i7 = i25 + (i17 - i16);
                    } else {
                        i7 = i25 - 1;
                    }
                    int i26 = i7 / (2 * i17);
                    if (i14 > 0) {
                        i26 = -i26;
                    }
                    i12 = iArr[2] + i26;
                } else if (0 != (iOutcode2 & 12)) {
                    if (0 != (iOutcode2 & 4)) {
                        i12 = i2;
                    } else {
                        i12 = i8;
                    }
                    int i27 = i12 - iArr[2];
                    if (i27 < 0) {
                        i27 = -i27;
                    }
                    int i28 = (2 * i27 * i17) + i16;
                    if (z2) {
                        i6 = i28 - 1;
                    } else {
                        i6 = i28 + (i16 - i17);
                    }
                    int i29 = i6 / (2 * i16);
                    if (i15 > 0) {
                        i29 = -i29;
                    }
                    i13 = iArr[3] + i29;
                }
                iOutcode2 = outcode(i12, i13, i2, i3, i8, i9);
            }
        }
        iArr[0] = i10;
        iArr[1] = i11;
        iArr[2] = i12;
        iArr[3] = i13;
        iArr[4] = i14;
        iArr[5] = i15;
        iArr[6] = i16;
        iArr[7] = i17;
        return true;
    }

    static PixelWriter createSolidPixelWriter(SunGraphics2D sunGraphics2D, SurfaceData surfaceData) {
        return new SolidPixelWriter(surfaceData.getColorModel().getDataElements(sunGraphics2D.eargb, null));
    }

    static PixelWriter createXorPixelWriter(SunGraphics2D sunGraphics2D, SurfaceData surfaceData) {
        ColorModel colorModel = surfaceData.getColorModel();
        Object dataElements = colorModel.getDataElements(sunGraphics2D.eargb, null);
        Object dataElements2 = colorModel.getDataElements(((XORComposite) sunGraphics2D.getComposite()).getXorColor().getRGB(), null);
        switch (colorModel.getTransferType()) {
            case 0:
                return new XorPixelWriter.ByteData(dataElements, dataElements2);
            case 1:
            case 2:
                return new XorPixelWriter.ShortData(dataElements, dataElements2);
            case 3:
                return new XorPixelWriter.IntData(dataElements, dataElements2);
            case 4:
                return new XorPixelWriter.FloatData(dataElements, dataElements2);
            case 5:
                return new XorPixelWriter.DoubleData(dataElements, dataElements2);
            default:
                throw new InternalError("Unsupported XOR pixel type");
        }
    }
}
