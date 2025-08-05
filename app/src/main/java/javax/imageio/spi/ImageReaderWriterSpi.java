package javax.imageio.spi;

import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.metadata.IIOMetadataFormatImpl;

/* loaded from: rt.jar:javax/imageio/spi/ImageReaderWriterSpi.class */
public abstract class ImageReaderWriterSpi extends IIOServiceProvider {
    protected String[] names;
    protected String[] suffixes;
    protected String[] MIMETypes;
    protected String pluginClassName;
    protected boolean supportsStandardStreamMetadataFormat;
    protected String nativeStreamMetadataFormatName;
    protected String nativeStreamMetadataFormatClassName;
    protected String[] extraStreamMetadataFormatNames;
    protected String[] extraStreamMetadataFormatClassNames;
    protected boolean supportsStandardImageMetadataFormat;
    protected String nativeImageMetadataFormatName;
    protected String nativeImageMetadataFormatClassName;
    protected String[] extraImageMetadataFormatNames;
    protected String[] extraImageMetadataFormatClassNames;

    public ImageReaderWriterSpi(String str, String str2, String[] strArr, String[] strArr2, String[] strArr3, String str3, boolean z2, String str4, String str5, String[] strArr4, String[] strArr5, boolean z3, String str6, String str7, String[] strArr6, String[] strArr7) {
        super(str, str2);
        this.names = null;
        this.suffixes = null;
        this.MIMETypes = null;
        this.pluginClassName = null;
        this.supportsStandardStreamMetadataFormat = false;
        this.nativeStreamMetadataFormatName = null;
        this.nativeStreamMetadataFormatClassName = null;
        this.extraStreamMetadataFormatNames = null;
        this.extraStreamMetadataFormatClassNames = null;
        this.supportsStandardImageMetadataFormat = false;
        this.nativeImageMetadataFormatName = null;
        this.nativeImageMetadataFormatClassName = null;
        this.extraImageMetadataFormatNames = null;
        this.extraImageMetadataFormatClassNames = null;
        if (strArr == null) {
            throw new IllegalArgumentException("names == null!");
        }
        if (strArr.length == 0) {
            throw new IllegalArgumentException("names.length == 0!");
        }
        if (str3 == null) {
            throw new IllegalArgumentException("pluginClassName == null!");
        }
        this.names = (String[]) strArr.clone();
        if (strArr2 != null && strArr2.length > 0) {
            this.suffixes = (String[]) strArr2.clone();
        }
        if (strArr3 != null && strArr3.length > 0) {
            this.MIMETypes = (String[]) strArr3.clone();
        }
        this.pluginClassName = str3;
        this.supportsStandardStreamMetadataFormat = z2;
        this.nativeStreamMetadataFormatName = str4;
        this.nativeStreamMetadataFormatClassName = str5;
        if (strArr4 != null && strArr4.length > 0) {
            this.extraStreamMetadataFormatNames = (String[]) strArr4.clone();
        }
        if (strArr5 != null && strArr5.length > 0) {
            this.extraStreamMetadataFormatClassNames = (String[]) strArr5.clone();
        }
        this.supportsStandardImageMetadataFormat = z3;
        this.nativeImageMetadataFormatName = str6;
        this.nativeImageMetadataFormatClassName = str7;
        if (strArr6 != null && strArr6.length > 0) {
            this.extraImageMetadataFormatNames = (String[]) strArr6.clone();
        }
        if (strArr7 != null && strArr7.length > 0) {
            this.extraImageMetadataFormatClassNames = (String[]) strArr7.clone();
        }
    }

    public ImageReaderWriterSpi() {
        this.names = null;
        this.suffixes = null;
        this.MIMETypes = null;
        this.pluginClassName = null;
        this.supportsStandardStreamMetadataFormat = false;
        this.nativeStreamMetadataFormatName = null;
        this.nativeStreamMetadataFormatClassName = null;
        this.extraStreamMetadataFormatNames = null;
        this.extraStreamMetadataFormatClassNames = null;
        this.supportsStandardImageMetadataFormat = false;
        this.nativeImageMetadataFormatName = null;
        this.nativeImageMetadataFormatClassName = null;
        this.extraImageMetadataFormatNames = null;
        this.extraImageMetadataFormatClassNames = null;
    }

    public String[] getFormatNames() {
        return (String[]) this.names.clone();
    }

    public String[] getFileSuffixes() {
        if (this.suffixes == null) {
            return null;
        }
        return (String[]) this.suffixes.clone();
    }

    public String[] getMIMETypes() {
        if (this.MIMETypes == null) {
            return null;
        }
        return (String[]) this.MIMETypes.clone();
    }

    public String getPluginClassName() {
        return this.pluginClassName;
    }

    public boolean isStandardStreamMetadataFormatSupported() {
        return this.supportsStandardStreamMetadataFormat;
    }

    public String getNativeStreamMetadataFormatName() {
        return this.nativeStreamMetadataFormatName;
    }

    public String[] getExtraStreamMetadataFormatNames() {
        if (this.extraStreamMetadataFormatNames == null) {
            return null;
        }
        return (String[]) this.extraStreamMetadataFormatNames.clone();
    }

    public boolean isStandardImageMetadataFormatSupported() {
        return this.supportsStandardImageMetadataFormat;
    }

    public String getNativeImageMetadataFormatName() {
        return this.nativeImageMetadataFormatName;
    }

    public String[] getExtraImageMetadataFormatNames() {
        if (this.extraImageMetadataFormatNames == null) {
            return null;
        }
        return (String[]) this.extraImageMetadataFormatNames.clone();
    }

    public IIOMetadataFormat getStreamMetadataFormat(String str) {
        return getMetadataFormat(str, this.supportsStandardStreamMetadataFormat, this.nativeStreamMetadataFormatName, this.nativeStreamMetadataFormatClassName, this.extraStreamMetadataFormatNames, this.extraStreamMetadataFormatClassNames);
    }

    public IIOMetadataFormat getImageMetadataFormat(String str) {
        return getMetadataFormat(str, this.supportsStandardImageMetadataFormat, this.nativeImageMetadataFormatName, this.nativeImageMetadataFormatClassName, this.extraImageMetadataFormatNames, this.extraImageMetadataFormatClassNames);
    }

    private IIOMetadataFormat getMetadataFormat(String str, boolean z2, String str2, String str3, String[] strArr, String[] strArr2) {
        if (str == null) {
            throw new IllegalArgumentException("formatName == null!");
        }
        if (z2 && str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
            return IIOMetadataFormatImpl.getStandardFormatInstance();
        }
        String str4 = null;
        if (str.equals(str2)) {
            str4 = str3;
        } else if (strArr != null) {
            int i2 = 0;
            while (true) {
                if (i2 >= strArr.length) {
                    break;
                }
                if (!str.equals(strArr[i2])) {
                    i2++;
                } else {
                    str4 = strArr2[i2];
                    break;
                }
            }
        }
        if (str4 == null) {
            throw new IllegalArgumentException("Unsupported format name");
        }
        try {
            return (IIOMetadataFormat) Class.forName(str4, true, ClassLoader.getSystemClassLoader()).getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e2) {
            IllegalStateException illegalStateException = new IllegalStateException("Can't obtain format");
            illegalStateException.initCause(e2);
            throw illegalStateException;
        }
    }
}
