package javax.imageio.plugins.bmp;

import com.sun.imageio.plugins.bmp.BMPCompressionTypes;
import java.util.Locale;
import javax.imageio.ImageWriteParam;

/* loaded from: rt.jar:javax/imageio/plugins/bmp/BMPImageWriteParam.class */
public class BMPImageWriteParam extends ImageWriteParam {
    private boolean topDown;

    public BMPImageWriteParam(Locale locale) {
        super(locale);
        this.topDown = false;
        this.compressionTypes = BMPCompressionTypes.getCompressionTypes();
        this.canWriteCompressed = true;
        this.compressionMode = 3;
        this.compressionType = this.compressionTypes[0];
    }

    public BMPImageWriteParam() {
        this(null);
    }

    public void setTopDown(boolean z2) {
        this.topDown = z2;
    }

    public boolean isTopDown() {
        return this.topDown;
    }
}
