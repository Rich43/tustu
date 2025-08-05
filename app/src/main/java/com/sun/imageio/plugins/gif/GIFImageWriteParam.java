package com.sun.imageio.plugins.gif;

import java.util.Locale;
import javax.imageio.ImageWriteParam;

/* compiled from: GIFImageWriter.java */
/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFImageWriteParam.class */
class GIFImageWriteParam extends ImageWriteParam {
    GIFImageWriteParam(Locale locale) {
        super(locale);
        this.canWriteCompressed = true;
        this.canWriteProgressive = true;
        this.compressionTypes = new String[]{"LZW", "lzw"};
        this.compressionType = this.compressionTypes[0];
    }

    @Override // javax.imageio.ImageWriteParam
    public void setCompressionMode(int i2) {
        if (i2 == 0) {
            throw new UnsupportedOperationException("MODE_DISABLED is not supported.");
        }
        super.setCompressionMode(i2);
    }
}
