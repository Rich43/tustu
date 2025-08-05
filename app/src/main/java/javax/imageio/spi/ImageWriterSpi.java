package javax.imageio.spi;

import java.awt.image.RenderedImage;
import java.io.IOException;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:javax/imageio/spi/ImageWriterSpi.class */
public abstract class ImageWriterSpi extends ImageReaderWriterSpi {

    @Deprecated
    public static final Class[] STANDARD_OUTPUT_TYPE = {ImageOutputStream.class};
    protected Class[] outputTypes;
    protected String[] readerSpiNames;
    private Class writerClass;

    public abstract boolean canEncodeImage(ImageTypeSpecifier imageTypeSpecifier);

    public abstract ImageWriter createWriterInstance(Object obj) throws IOException;

    protected ImageWriterSpi() {
        this.outputTypes = null;
        this.readerSpiNames = null;
        this.writerClass = null;
    }

    public ImageWriterSpi(String str, String str2, String[] strArr, String[] strArr2, String[] strArr3, String str3, Class[] clsArr, String[] strArr4, boolean z2, String str4, String str5, String[] strArr5, String[] strArr6, boolean z3, String str6, String str7, String[] strArr7, String[] strArr8) {
        super(str, str2, strArr, strArr2, strArr3, str3, z2, str4, str5, strArr5, strArr6, z3, str6, str7, strArr7, strArr8);
        this.outputTypes = null;
        this.readerSpiNames = null;
        this.writerClass = null;
        if (clsArr == null) {
            throw new IllegalArgumentException("outputTypes == null!");
        }
        if (clsArr.length == 0) {
            throw new IllegalArgumentException("outputTypes.length == 0!");
        }
        this.outputTypes = clsArr == STANDARD_OUTPUT_TYPE ? new Class[]{ImageOutputStream.class} : (Class[]) clsArr.clone();
        if (strArr4 != null && strArr4.length > 0) {
            this.readerSpiNames = (String[]) strArr4.clone();
        }
    }

    public boolean isFormatLossless() {
        return true;
    }

    public Class[] getOutputTypes() {
        return (Class[]) this.outputTypes.clone();
    }

    public boolean canEncodeImage(RenderedImage renderedImage) {
        return canEncodeImage(ImageTypeSpecifier.createFromRenderedImage(renderedImage));
    }

    public ImageWriter createWriterInstance() throws IOException {
        return createWriterInstance(null);
    }

    public boolean isOwnWriter(ImageWriter imageWriter) {
        if (imageWriter == null) {
            throw new IllegalArgumentException("writer == null!");
        }
        return imageWriter.getClass().getName().equals(this.pluginClassName);
    }

    public String[] getImageReaderSpiNames() {
        if (this.readerSpiNames == null) {
            return null;
        }
        return (String[]) this.readerSpiNames.clone();
    }
}
