package java.awt.image;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import sun.awt.image.ImagingLib;

/* loaded from: rt.jar:java/awt/image/BandCombineOp.class */
public class BandCombineOp implements RasterOp {
    float[][] matrix;
    int nrows;
    int ncols;
    RenderingHints hints;

    /* JADX WARN: Type inference failed for: r1v9, types: [float[], float[][]] */
    public BandCombineOp(float[][] fArr, RenderingHints renderingHints) {
        this.nrows = 0;
        this.ncols = 0;
        this.nrows = fArr.length;
        this.ncols = fArr[0].length;
        this.matrix = new float[this.nrows];
        for (int i2 = 0; i2 < this.nrows; i2++) {
            if (this.ncols > fArr[i2].length) {
                throw new IndexOutOfBoundsException("row " + i2 + " too short");
            }
            this.matrix[i2] = Arrays.copyOf(fArr[i2], this.ncols);
        }
        this.hints = renderingHints;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [float[], float[][]] */
    public final float[][] getMatrix() {
        ?? r0 = new float[this.nrows];
        for (int i2 = 0; i2 < this.nrows; i2++) {
            r0[i2] = Arrays.copyOf(this.matrix[i2], this.ncols);
        }
        return r0;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster filter(Raster raster, WritableRaster writableRaster) {
        int numBands = raster.getNumBands();
        if (this.ncols != numBands && this.ncols != numBands + 1) {
            throw new IllegalArgumentException("Number of columns in the matrix (" + this.ncols + ") must be equal to the number of bands ([+1]) in src (" + numBands + ").");
        }
        if (writableRaster == null) {
            writableRaster = createCompatibleDestRaster(raster);
        } else if (this.nrows != writableRaster.getNumBands()) {
            throw new IllegalArgumentException("Number of rows in the matrix (" + this.nrows + ") must be equal to the number of bands ([+1]) in dst (" + numBands + ").");
        }
        if (ImagingLib.filter(this, raster, writableRaster) != null) {
            return writableRaster;
        }
        int[] pixel = null;
        int[] iArr = new int[writableRaster.getNumBands()];
        int minX = raster.getMinX();
        int minY = raster.getMinY();
        int minX2 = writableRaster.getMinX();
        int minY2 = writableRaster.getMinY();
        if (this.ncols == numBands) {
            int i2 = 0;
            while (i2 < raster.getHeight()) {
                int i3 = minX2;
                int i4 = minX;
                int i5 = 0;
                while (i5 < raster.getWidth()) {
                    pixel = raster.getPixel(i4, minY, pixel);
                    for (int i6 = 0; i6 < this.nrows; i6++) {
                        float f2 = 0.0f;
                        for (int i7 = 0; i7 < this.ncols; i7++) {
                            f2 += this.matrix[i6][i7] * pixel[i7];
                        }
                        iArr[i6] = (int) f2;
                    }
                    writableRaster.setPixel(i3, minY2, iArr);
                    i5++;
                    i4++;
                    i3++;
                }
                i2++;
                minY++;
                minY2++;
            }
        } else {
            int i8 = 0;
            while (i8 < raster.getHeight()) {
                int i9 = minX2;
                int i10 = minX;
                int i11 = 0;
                while (i11 < raster.getWidth()) {
                    pixel = raster.getPixel(i10, minY, pixel);
                    for (int i12 = 0; i12 < this.nrows; i12++) {
                        float f3 = 0.0f;
                        for (int i13 = 0; i13 < numBands; i13++) {
                            f3 += this.matrix[i12][i13] * pixel[i13];
                        }
                        iArr[i12] = (int) (f3 + this.matrix[i12][numBands]);
                    }
                    writableRaster.setPixel(i9, minY2, iArr);
                    i11++;
                    i10++;
                    i9++;
                }
                i8++;
                minY++;
                minY2++;
            }
        }
        return writableRaster;
    }

    @Override // java.awt.image.RasterOp
    public final Rectangle2D getBounds2D(Raster raster) {
        return raster.getBounds();
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster createCompatibleDestRaster(Raster raster) {
        int numBands = raster.getNumBands();
        if (this.ncols != numBands && this.ncols != numBands + 1) {
            throw new IllegalArgumentException("Number of columns in the matrix (" + this.ncols + ") must be equal to the number of bands ([+1]) in src (" + numBands + ").");
        }
        if (raster.getNumBands() == this.nrows) {
            return raster.createCompatibleWritableRaster();
        }
        throw new IllegalArgumentException("Don't know how to create a  compatible Raster with " + this.nrows + " bands.");
    }

    @Override // java.awt.image.RasterOp
    public final Point2D getPoint2D(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D.Float();
        }
        point2D2.setLocation(point2D.getX(), point2D.getY());
        return point2D2;
    }

    @Override // java.awt.image.RasterOp
    public final RenderingHints getRenderingHints() {
        return this.hints;
    }
}
