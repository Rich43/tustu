package sun.awt.image;

import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/awt/image/JPEGImageDecoder.class */
public class JPEGImageDecoder extends ImageDecoder {
    private static ColorModel RGBcolormodel;
    private static ColorModel ARGBcolormodel;
    private static ColorModel Graycolormodel;
    private static final Class InputStreamClass = InputStream.class;
    private ColorModel colormodel;
    Hashtable props;
    private static final int hintflags = 22;

    private static native void initIDs(Class cls);

    private native void readImage(InputStream inputStream, byte[] bArr) throws ImageFormatException, IOException;

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.image.JPEGImageDecoder.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("jpeg");
                return null;
            }
        });
        initIDs(InputStreamClass);
        RGBcolormodel = new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
        ARGBcolormodel = ColorModel.getRGBdefault();
        byte[] bArr = new byte[256];
        for (int i2 = 0; i2 < 256; i2++) {
            bArr[i2] = (byte) i2;
        }
        Graycolormodel = new IndexColorModel(8, 256, bArr, bArr, bArr);
    }

    public JPEGImageDecoder(InputStreamImageSource inputStreamImageSource, InputStream inputStream) {
        super(inputStreamImageSource, inputStream);
        this.props = new Hashtable();
    }

    private static void error(String str) throws ImageFormatException {
        throw new ImageFormatException(str);
    }

    public boolean sendHeaderInfo(int i2, int i3, boolean z2, boolean z3, boolean z4) {
        setDimensions(i2, i3);
        setProperties(this.props);
        if (z2) {
            this.colormodel = Graycolormodel;
        } else if (z3) {
            this.colormodel = ARGBcolormodel;
        } else {
            this.colormodel = RGBcolormodel;
        }
        setColorModel(this.colormodel);
        if (!z4) {
            int i4 = 22 | 8;
        }
        setHints(22);
        headerComplete();
        return true;
    }

    public boolean sendPixels(int[] iArr, int i2) {
        if (setPixels(0, i2, iArr.length, 1, this.colormodel, iArr, 0, iArr.length) <= 0) {
            this.aborted = true;
        }
        return !this.aborted;
    }

    public boolean sendPixels(byte[] bArr, int i2) {
        if (setPixels(0, i2, bArr.length, 1, this.colormodel, bArr, 0, bArr.length) <= 0) {
            this.aborted = true;
        }
        return !this.aborted;
    }

    @Override // sun.awt.image.ImageDecoder
    public void produceImage() throws IOException, ImageFormatException {
        try {
            readImage(this.input, new byte[1024]);
            if (!this.aborted) {
                imageComplete(3, true);
            }
        } catch (IOException e2) {
            if (!this.aborted) {
                throw e2;
            }
        } finally {
            close();
        }
    }
}
