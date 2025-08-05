package sun.awt.image;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/awt/image/XbmImageDecoder.class */
public class XbmImageDecoder extends ImageDecoder {
    private static byte[] XbmColormap = {-1, -1, -1, 0, 0, 0};
    private static int XbmHints = 30;

    public XbmImageDecoder(InputStreamImageSource inputStreamImageSource, InputStream inputStream) {
        super(inputStreamImageSource, inputStream);
        if (!(this.input instanceof BufferedInputStream)) {
            this.input = new BufferedInputStream(this.input, 80);
        }
    }

    private static void error(String str) throws ImageFormatException {
        throw new ImageFormatException(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v39 */
    /* JADX WARN: Type inference failed for: r0v64 */
    @Override // sun.awt.image.ImageDecoder
    public void produceImage() throws ImageFormatException, IOException {
        int i2;
        int i3;
        char[] cArr = new char[80];
        int i4 = 0;
        boolean z2 = false;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        boolean z3 = true;
        byte[] bArr = null;
        IndexColorModel indexColorModel = null;
        while (!this.aborted && (i2 = this.input.read()) != -1) {
            if ((97 <= i2 && i2 <= 122) || ((65 <= i2 && i2 <= 90) || ((48 <= i2 && i2 <= 57) || i2 == 35 || i2 == 95))) {
                if (i4 < 78) {
                    int i9 = i4;
                    i4++;
                    cArr[i9] = (char) i2;
                }
            } else if (i4 > 0) {
                int i10 = i4;
                i4 = 0;
                if (z3) {
                    if (i10 != 7 || cArr[0] != '#' || cArr[1] != 'd' || cArr[2] != 'e' || cArr[3] != 'f' || cArr[4] != 'i' || cArr[5] != 'n' || cArr[6] != 'e') {
                        error("Not an XBM file");
                    }
                    z3 = false;
                }
                if (cArr[i10 - 1] == 'h') {
                    z2 = true;
                } else if (cArr[i10 - 1] == 't' && i10 > 1 && cArr[i10 - 2] == 'h') {
                    z2 = 2;
                } else if (i10 > 2 && z2 < 0 && cArr[0] == '0' && cArr[1] == 'x') {
                    int i11 = 0;
                    for (int i12 = 2; i12 < i10; i12++) {
                        char c2 = cArr[i12];
                        if ('0' <= c2 && c2 <= '9') {
                            i3 = c2 - '0';
                        } else if ('A' <= c2 && c2 <= 'Z') {
                            i3 = (c2 - 'A') + 10;
                        } else if ('a' <= c2 && c2 <= 'z') {
                            i3 = (c2 - 'a') + 10;
                        } else {
                            i3 = 0;
                        }
                        i11 = (i11 * 16) + i3;
                    }
                    int i13 = 1;
                    while (true) {
                        int i14 = i13;
                        if (i14 > 128) {
                            break;
                        }
                        if (i7 < i6) {
                            if ((i11 & i14) != 0) {
                                bArr[i7] = 1;
                            } else {
                                bArr[i7] = 0;
                            }
                        }
                        i7++;
                        i13 = i14 << 1;
                    }
                    if (i7 >= i6) {
                        if (setPixels(0, i8, i6, 1, (ColorModel) indexColorModel, bArr, 0, i6) <= 0) {
                            return;
                        }
                        i7 = 0;
                        int i15 = i8;
                        i8++;
                        if (i15 >= i5) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    int i16 = 0;
                    for (int i17 = 0; i17 < i10; i17++) {
                        char c3 = cArr[i17];
                        if ('0' <= c3 && c3 <= '9') {
                            i16 = ((i16 * 10) + c3) - 48;
                        } else {
                            i16 = -1;
                            break;
                        }
                    }
                    if (i16 > 0 && z2 > 0) {
                        if (z2) {
                            i6 = i16;
                        } else {
                            i5 = i16;
                        }
                        if (i6 == 0 || i5 == 0) {
                            z2 = false;
                        } else {
                            indexColorModel = new IndexColorModel(8, 2, XbmColormap, 0, false, 0);
                            setDimensions(i6, i5);
                            setColorModel(indexColorModel);
                            setHints(XbmHints);
                            headerComplete();
                            bArr = new byte[i6];
                            z2 = -1;
                        }
                    }
                }
            } else {
                continue;
            }
        }
        this.input.close();
        imageComplete(3, true);
    }
}
