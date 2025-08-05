package com.sun.imageio.plugins.common;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/ReaderUtil.class */
public class ReaderUtil {
    private static void computeUpdatedPixels(int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int[] iArr, int i11) {
        boolean z2 = false;
        int i12 = -1;
        int i13 = -1;
        int i14 = -1;
        for (int i15 = 0; i15 < i9; i15++) {
            int i16 = i8 + (i15 * i10);
            if (i16 >= i2 && (i16 - i2) % i7 == 0) {
                if (i16 >= i2 + i3) {
                    break;
                }
                int i17 = i4 + ((i16 - i2) / i7);
                if (i17 < i5) {
                    continue;
                } else {
                    if (i17 > i6) {
                        break;
                    }
                    if (!z2) {
                        i12 = i17;
                        z2 = true;
                    } else if (i13 == -1) {
                        i13 = i17;
                    }
                    i14 = i17;
                }
            }
        }
        iArr[i11] = i12;
        if (!z2) {
            iArr[i11 + 2] = 0;
        } else {
            iArr[i11 + 2] = (i14 - i12) + 1;
        }
        iArr[i11 + 4] = Math.max(i13 - i12, 1);
    }

    public static int[] computeUpdatedPixels(Rectangle rectangle, Point point, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13) {
        int[] iArr = new int[6];
        computeUpdatedPixels(rectangle.f12372x, rectangle.width, point.f12370x, i2, i4, i6, i8, i10, i12, iArr, 0);
        computeUpdatedPixels(rectangle.f12373y, rectangle.height, point.f12371y, i3, i5, i7, i9, i11, i13, iArr, 1);
        return iArr;
    }

    public static int readMultiByteInteger(ImageInputStream imageInputStream) throws IOException {
        byte b2 = imageInputStream.readByte();
        int i2 = b2 & Byte.MAX_VALUE;
        while (true) {
            int i3 = i2;
            if ((b2 & 128) == 128) {
                b2 = imageInputStream.readByte();
                i2 = (i3 << 7) | (b2 & Byte.MAX_VALUE);
            } else {
                return i3;
            }
        }
    }

    public static byte[] staggeredReadByteStream(ImageInputStream imageInputStream, int i2) throws IOException {
        byte[] bArr;
        if (i2 < 1024000) {
            bArr = new byte[i2];
            imageInputStream.readFully(bArr, 0, i2);
        } else {
            int i3 = i2;
            int i4 = 0;
            ArrayList<byte[]> arrayList = new ArrayList();
            while (i3 != 0) {
                int iMin = Math.min(i3, 1024000);
                byte[] bArr2 = new byte[iMin];
                imageInputStream.readFully(bArr2, 0, iMin);
                arrayList.add(bArr2);
                i4 += iMin;
                i3 -= iMin;
            }
            bArr = new byte[i4];
            int length = 0;
            for (byte[] bArr3 : arrayList) {
                System.arraycopy(bArr3, 0, bArr, length, bArr3.length);
                length += bArr3.length;
            }
        }
        return bArr;
    }
}
