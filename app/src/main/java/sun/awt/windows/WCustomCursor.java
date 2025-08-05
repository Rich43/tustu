package sun.awt.windows;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import sun.awt.CustomCursor;
import sun.awt.image.IntegerComponentRaster;
import sun.awt.image.ToolkitImage;

/* loaded from: rt.jar:sun/awt/windows/WCustomCursor.class */
final class WCustomCursor extends CustomCursor {
    private native void createCursorIndirect(int[] iArr, byte[] bArr, int i2, int i3, int i4, int i5, int i6);

    static native int getCursorWidth();

    static native int getCursorHeight();

    WCustomCursor(Image image, Point point, String str) throws IndexOutOfBoundsException {
        super(image, point, str);
    }

    @Override // sun.awt.CustomCursor
    protected void createNativeCursor(Image image, int[] iArr, int i2, int i3, int i4, int i5) {
        BufferedImage bufferedImage = new BufferedImage(i2, i3, 1);
        Graphics graphics = bufferedImage.getGraphics();
        try {
            if (image instanceof ToolkitImage) {
                ((ToolkitImage) image).getImageRep().reconstruct(32);
            }
            graphics.drawImage(image, 0, 0, i2, i3, null);
            graphics.dispose();
            WritableRaster raster = bufferedImage.getRaster();
            ((DataBufferInt) raster.getDataBuffer()).getData();
            byte[] bArr = new byte[(i2 * i3) / 8];
            int length = iArr.length;
            for (int i6 = 0; i6 < length; i6++) {
                int i7 = i6 / 8;
                int i8 = 1 << (7 - (i6 % 8));
                if ((iArr[i6] & (-16777216)) == 0) {
                    bArr[i7] = (byte) (bArr[i7] | i8);
                }
            }
            int width = raster.getWidth();
            if (raster instanceof IntegerComponentRaster) {
                width = ((IntegerComponentRaster) raster).getScanlineStride();
            }
            createCursorIndirect(((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData(), bArr, width, raster.getWidth(), raster.getHeight(), i4, i5);
        } catch (Throwable th) {
            graphics.dispose();
            throw th;
        }
    }
}
