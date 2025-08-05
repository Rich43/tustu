package javax.imageio.spi;

import java.io.IOException;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:javax/imageio/spi/ImageReaderSpi.class */
public abstract class ImageReaderSpi extends ImageReaderWriterSpi {

    @Deprecated
    public static final Class[] STANDARD_INPUT_TYPE = {ImageInputStream.class};
    protected Class[] inputTypes;
    protected String[] writerSpiNames;
    private Class readerClass;

    public abstract boolean canDecodeInput(Object obj) throws IOException;

    public abstract ImageReader createReaderInstance(Object obj) throws IOException;

    protected ImageReaderSpi() {
        this.inputTypes = null;
        this.writerSpiNames = null;
        this.readerClass = null;
    }

    public ImageReaderSpi(String str, String str2, String[] strArr, String[] strArr2, String[] strArr3, String str3, Class[] clsArr, String[] strArr4, boolean z2, String str4, String str5, String[] strArr5, String[] strArr6, boolean z3, String str6, String str7, String[] strArr7, String[] strArr8) {
        super(str, str2, strArr, strArr2, strArr3, str3, z2, str4, str5, strArr5, strArr6, z3, str6, str7, strArr7, strArr8);
        this.inputTypes = null;
        this.writerSpiNames = null;
        this.readerClass = null;
        if (clsArr == null) {
            throw new IllegalArgumentException("inputTypes == null!");
        }
        if (clsArr.length == 0) {
            throw new IllegalArgumentException("inputTypes.length == 0!");
        }
        this.inputTypes = clsArr == STANDARD_INPUT_TYPE ? new Class[]{ImageInputStream.class} : (Class[]) clsArr.clone();
        if (strArr4 != null && strArr4.length > 0) {
            this.writerSpiNames = (String[]) strArr4.clone();
        }
    }

    public Class[] getInputTypes() {
        return (Class[]) this.inputTypes.clone();
    }

    public ImageReader createReaderInstance() throws IOException {
        return createReaderInstance(null);
    }

    public boolean isOwnReader(ImageReader imageReader) {
        if (imageReader == null) {
            throw new IllegalArgumentException("reader == null!");
        }
        return imageReader.getClass().getName().equals(this.pluginClassName);
    }

    public String[] getImageWriterSpiNames() {
        if (this.writerSpiNames == null) {
            return null;
        }
        return (String[]) this.writerSpiNames.clone();
    }
}
