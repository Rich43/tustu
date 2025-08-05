package com.sun.imageio.plugins.png;

import java.util.Locale;
import javax.imageio.ImageWriteParam;

/* compiled from: PNGImageWriter.java */
/* loaded from: rt.jar:com/sun/imageio/plugins/png/PNGImageWriteParam.class */
class PNGImageWriteParam extends ImageWriteParam {
    public PNGImageWriteParam(Locale locale) {
        this.canWriteProgressive = true;
        this.locale = locale;
    }
}
