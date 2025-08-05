package javax.swing.plaf.nimbus;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import javax.swing.plaf.nimbus.Effect;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/InnerShadowEffect.class */
class InnerShadowEffect extends ShadowEffect {
    InnerShadowEffect() {
    }

    @Override // javax.swing.plaf.nimbus.Effect
    Effect.EffectType getEffectType() {
        return Effect.EffectType.OVER;
    }

    @Override // javax.swing.plaf.nimbus.Effect
    BufferedImage applyEffect(BufferedImage bufferedImage, BufferedImage bufferedImage2, int i2, int i3) {
        if (bufferedImage == null || bufferedImage.getType() != 2) {
            throw new IllegalArgumentException("Effect only works with source images of type BufferedImage.TYPE_INT_ARGB.");
        }
        if (bufferedImage2 != null && bufferedImage2.getType() != 2) {
            throw new IllegalArgumentException("Effect only works with destination images of type BufferedImage.TYPE_INT_ARGB.");
        }
        double radians = Math.toRadians(this.angle - 90);
        int iSin = (int) (Math.sin(radians) * this.distance);
        int iCos = (int) (Math.cos(radians) * this.distance);
        int i4 = iSin + this.size;
        int i5 = iSin + this.size;
        int i6 = i2 + iSin + this.size + this.size;
        int i7 = i3 + iSin + this.size;
        int[] tmpIntArray = getArrayCache().getTmpIntArray(i2);
        byte[] tmpByteArray1 = getArrayCache().getTmpByteArray1(i6 * i7);
        Arrays.fill(tmpByteArray1, (byte) -1);
        byte[] tmpByteArray2 = getArrayCache().getTmpByteArray2(i6 * i7);
        byte[] tmpByteArray3 = getArrayCache().getTmpByteArray3(i6 * i7);
        WritableRaster raster = bufferedImage.getRaster();
        for (int i8 = 0; i8 < i3; i8++) {
            int i9 = (i8 + i5) * i6;
            raster.getDataElements(0, i8, i2, 1, tmpIntArray);
            for (int i10 = 0; i10 < i2; i10++) {
                tmpByteArray1[i9 + i10 + i4] = (byte) ((255 - ((tmpIntArray[i10] & (-16777216)) >>> 24)) & 255);
            }
        }
        float[] fArrCreateGaussianKernel = EffectUtils.createGaussianKernel(this.size * 2);
        EffectUtils.blur(tmpByteArray1, tmpByteArray3, i6, i7, fArrCreateGaussianKernel, this.size * 2);
        EffectUtils.blur(tmpByteArray3, tmpByteArray2, i7, i6, fArrCreateGaussianKernel, this.size * 2);
        float fMin = Math.min(1.0f / (1.0f - (0.01f * this.spread)), 255.0f);
        for (int i11 = 0; i11 < tmpByteArray2.length; i11++) {
            int i12 = (int) ((tmpByteArray2[i11] & 255) * fMin);
            tmpByteArray2[i11] = i12 > 255 ? (byte) -1 : (byte) i12;
        }
        if (bufferedImage2 == null) {
            bufferedImage2 = new BufferedImage(i2, i3, 2);
        }
        WritableRaster raster2 = bufferedImage2.getRaster();
        int red = this.color.getRed();
        int green = this.color.getGreen();
        int blue = this.color.getBlue();
        for (int i13 = 0; i13 < i3; i13++) {
            int i14 = i13 + i5;
            int i15 = i14 * i6;
            int i16 = (i14 - iCos) * i6;
            for (int i17 = 0; i17 < i2; i17++) {
                int i18 = i17 + i4;
                tmpIntArray[i17] = ((((byte) Math.min(255 - (tmpByteArray1[i15 + i18] & 255), tmpByteArray2[i16 + (i18 - iSin)] & 255)) & 255) << 24) | (red << 16) | (green << 8) | blue;
            }
            raster2.setDataElements(0, i13, i2, 1, tmpIntArray);
        }
        return bufferedImage2;
    }
}
