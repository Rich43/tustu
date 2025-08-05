package sun.java2d.loops;

import java.awt.Composite;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import sun.awt.image.IntegerComponentRaster;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.SpanIterator;

/* compiled from: CustomComponent.java */
/* loaded from: rt.jar:sun/java2d/loops/OpaqueCopyAnyToArgb.class */
class OpaqueCopyAnyToArgb extends Blit {
    OpaqueCopyAnyToArgb() {
        super(SurfaceType.Any, CompositeType.SrcNoEa, SurfaceType.IntArgb);
    }

    @Override // sun.java2d.loops.Blit
    public void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        Raster raster = surfaceData.getRaster(i2, i3, i6, i7);
        ColorModel colorModel = surfaceData.getColorModel();
        IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster) surfaceData2.getRaster(i4, i5, i6, i7);
        int[] dataStorage = integerComponentRaster.getDataStorage();
        SpanIterator spanIterator = CustomComponent.getRegionOfInterest(surfaceData, surfaceData2, region, i2, i3, i4, i5, i6, i7).getSpanIterator();
        Object dataElements = null;
        int scanlineStride = integerComponentRaster.getScanlineStride();
        int i8 = i2 - i4;
        int i9 = i3 - i5;
        int[] iArr = new int[4];
        while (spanIterator.nextSpan(iArr)) {
            int dataOffset = integerComponentRaster.getDataOffset(0) + (iArr[1] * scanlineStride) + iArr[0];
            for (int i10 = iArr[1]; i10 < iArr[3]; i10++) {
                int i11 = dataOffset;
                for (int i12 = iArr[0]; i12 < iArr[2]; i12++) {
                    dataElements = raster.getDataElements(i12 + i8, i10 + i9, dataElements);
                    int i13 = i11;
                    i11++;
                    dataStorage[i13] = colorModel.getRGB(dataElements);
                }
                dataOffset += scanlineStride;
            }
        }
        integerComponentRaster.markDirty();
    }
}
