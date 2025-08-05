package sun.awt.image;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

/* loaded from: rt.jar:sun/awt/image/ByteArrayImageSource.class */
public class ByteArrayImageSource extends InputStreamImageSource {
    byte[] imagedata;
    int imageoffset;
    int imagelength;

    public ByteArrayImageSource(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ByteArrayImageSource(byte[] bArr, int i2, int i3) {
        this.imagedata = bArr;
        this.imageoffset = i2;
        this.imagelength = i3;
    }

    @Override // sun.awt.image.InputStreamImageSource
    final boolean checkSecurity(Object obj, boolean z2) {
        return true;
    }

    @Override // sun.awt.image.InputStreamImageSource
    protected ImageDecoder getDecoder() {
        return getDecoder(new BufferedInputStream(new ByteArrayInputStream(this.imagedata, this.imageoffset, this.imagelength)));
    }
}
