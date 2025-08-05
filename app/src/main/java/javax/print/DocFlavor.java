package javax.print;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import org.icepdf.core.util.PdfOps;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:javax/print/DocFlavor.class */
public class DocFlavor implements Serializable, Cloneable {
    private static final long serialVersionUID = -4512080796965449721L;
    public static final String hostEncoding = (String) AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
    private transient MimeType myMimeType;
    private String myClassName;
    private transient String myStringValue = null;

    public DocFlavor(String str, String str2) {
        if (str2 == null) {
            throw new NullPointerException();
        }
        this.myMimeType = new MimeType(str);
        this.myClassName = str2;
    }

    public String getMimeType() {
        return this.myMimeType.getMimeType();
    }

    public String getMediaType() {
        return this.myMimeType.getMediaType();
    }

    public String getMediaSubtype() {
        return this.myMimeType.getMediaSubtype();
    }

    public String getParameter(String str) {
        return (String) this.myMimeType.getParameterMap().get(str.toLowerCase());
    }

    public String getRepresentationClassName() {
        return this.myClassName;
    }

    public String toString() {
        return getStringValue();
    }

    public int hashCode() {
        return getStringValue().hashCode();
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof DocFlavor) && getStringValue().equals(((DocFlavor) obj).getStringValue());
    }

    private String getStringValue() {
        if (this.myStringValue == null) {
            this.myStringValue = ((Object) this.myMimeType) + "; class=\"" + this.myClassName + PdfOps.DOUBLE_QUOTE__TOKEN;
        }
        return this.myStringValue;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.myMimeType.getMimeType());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.myMimeType = new MimeType((String) objectInputStream.readObject());
    }

    /* loaded from: rt.jar:javax/print/DocFlavor$BYTE_ARRAY.class */
    public static class BYTE_ARRAY extends DocFlavor {
        private static final long serialVersionUID = -9065578006593857475L;
        public static final BYTE_ARRAY TEXT_PLAIN_HOST = new BYTE_ARRAY("text/plain; charset=" + hostEncoding);
        public static final BYTE_ARRAY TEXT_PLAIN_UTF_8 = new BYTE_ARRAY("text/plain; charset=utf-8");
        public static final BYTE_ARRAY TEXT_PLAIN_UTF_16 = new BYTE_ARRAY("text/plain; charset=utf-16");
        public static final BYTE_ARRAY TEXT_PLAIN_UTF_16BE = new BYTE_ARRAY("text/plain; charset=utf-16be");
        public static final BYTE_ARRAY TEXT_PLAIN_UTF_16LE = new BYTE_ARRAY("text/plain; charset=utf-16le");
        public static final BYTE_ARRAY TEXT_PLAIN_US_ASCII = new BYTE_ARRAY("text/plain; charset=us-ascii");
        public static final BYTE_ARRAY TEXT_HTML_HOST = new BYTE_ARRAY("text/html; charset=" + hostEncoding);
        public static final BYTE_ARRAY TEXT_HTML_UTF_8 = new BYTE_ARRAY("text/html; charset=utf-8");
        public static final BYTE_ARRAY TEXT_HTML_UTF_16 = new BYTE_ARRAY("text/html; charset=utf-16");
        public static final BYTE_ARRAY TEXT_HTML_UTF_16BE = new BYTE_ARRAY("text/html; charset=utf-16be");
        public static final BYTE_ARRAY TEXT_HTML_UTF_16LE = new BYTE_ARRAY("text/html; charset=utf-16le");
        public static final BYTE_ARRAY TEXT_HTML_US_ASCII = new BYTE_ARRAY("text/html; charset=us-ascii");
        public static final BYTE_ARRAY PDF = new BYTE_ARRAY("application/pdf");
        public static final BYTE_ARRAY POSTSCRIPT = new BYTE_ARRAY("application/postscript");
        public static final BYTE_ARRAY PCL = new BYTE_ARRAY("application/vnd.hp-PCL");
        public static final BYTE_ARRAY GIF = new BYTE_ARRAY("image/gif");
        public static final BYTE_ARRAY JPEG = new BYTE_ARRAY("image/jpeg");
        public static final BYTE_ARRAY PNG = new BYTE_ARRAY("image/png");
        public static final BYTE_ARRAY AUTOSENSE = new BYTE_ARRAY(Locator.DEFAULT_CONTENT_TYPE);

        public BYTE_ARRAY(String str) {
            super(str, "[B");
        }
    }

    /* loaded from: rt.jar:javax/print/DocFlavor$INPUT_STREAM.class */
    public static class INPUT_STREAM extends DocFlavor {
        private static final long serialVersionUID = -7045842700749194127L;
        public static final INPUT_STREAM TEXT_PLAIN_HOST = new INPUT_STREAM("text/plain; charset=" + hostEncoding);
        public static final INPUT_STREAM TEXT_PLAIN_UTF_8 = new INPUT_STREAM("text/plain; charset=utf-8");
        public static final INPUT_STREAM TEXT_PLAIN_UTF_16 = new INPUT_STREAM("text/plain; charset=utf-16");
        public static final INPUT_STREAM TEXT_PLAIN_UTF_16BE = new INPUT_STREAM("text/plain; charset=utf-16be");
        public static final INPUT_STREAM TEXT_PLAIN_UTF_16LE = new INPUT_STREAM("text/plain; charset=utf-16le");
        public static final INPUT_STREAM TEXT_PLAIN_US_ASCII = new INPUT_STREAM("text/plain; charset=us-ascii");
        public static final INPUT_STREAM TEXT_HTML_HOST = new INPUT_STREAM("text/html; charset=" + hostEncoding);
        public static final INPUT_STREAM TEXT_HTML_UTF_8 = new INPUT_STREAM("text/html; charset=utf-8");
        public static final INPUT_STREAM TEXT_HTML_UTF_16 = new INPUT_STREAM("text/html; charset=utf-16");
        public static final INPUT_STREAM TEXT_HTML_UTF_16BE = new INPUT_STREAM("text/html; charset=utf-16be");
        public static final INPUT_STREAM TEXT_HTML_UTF_16LE = new INPUT_STREAM("text/html; charset=utf-16le");
        public static final INPUT_STREAM TEXT_HTML_US_ASCII = new INPUT_STREAM("text/html; charset=us-ascii");
        public static final INPUT_STREAM PDF = new INPUT_STREAM("application/pdf");
        public static final INPUT_STREAM POSTSCRIPT = new INPUT_STREAM("application/postscript");
        public static final INPUT_STREAM PCL = new INPUT_STREAM("application/vnd.hp-PCL");
        public static final INPUT_STREAM GIF = new INPUT_STREAM("image/gif");
        public static final INPUT_STREAM JPEG = new INPUT_STREAM("image/jpeg");
        public static final INPUT_STREAM PNG = new INPUT_STREAM("image/png");
        public static final INPUT_STREAM AUTOSENSE = new INPUT_STREAM(Locator.DEFAULT_CONTENT_TYPE);

        public INPUT_STREAM(String str) {
            super(str, "java.io.InputStream");
        }
    }

    /* loaded from: rt.jar:javax/print/DocFlavor$URL.class */
    public static class URL extends DocFlavor {
        public static final URL TEXT_PLAIN_HOST = new URL("text/plain; charset=" + hostEncoding);
        public static final URL TEXT_PLAIN_UTF_8 = new URL("text/plain; charset=utf-8");
        public static final URL TEXT_PLAIN_UTF_16 = new URL("text/plain; charset=utf-16");
        public static final URL TEXT_PLAIN_UTF_16BE = new URL("text/plain; charset=utf-16be");
        public static final URL TEXT_PLAIN_UTF_16LE = new URL("text/plain; charset=utf-16le");
        public static final URL TEXT_PLAIN_US_ASCII = new URL("text/plain; charset=us-ascii");
        public static final URL TEXT_HTML_HOST = new URL("text/html; charset=" + hostEncoding);
        public static final URL TEXT_HTML_UTF_8 = new URL("text/html; charset=utf-8");
        public static final URL TEXT_HTML_UTF_16 = new URL("text/html; charset=utf-16");
        public static final URL TEXT_HTML_UTF_16BE = new URL("text/html; charset=utf-16be");
        public static final URL TEXT_HTML_UTF_16LE = new URL("text/html; charset=utf-16le");
        public static final URL TEXT_HTML_US_ASCII = new URL("text/html; charset=us-ascii");
        public static final URL PDF = new URL("application/pdf");
        public static final URL POSTSCRIPT = new URL("application/postscript");
        public static final URL PCL = new URL("application/vnd.hp-PCL");
        public static final URL GIF = new URL("image/gif");
        public static final URL JPEG = new URL("image/jpeg");
        public static final URL PNG = new URL("image/png");
        public static final URL AUTOSENSE = new URL(Locator.DEFAULT_CONTENT_TYPE);

        public URL(String str) {
            super(str, "java.net.URL");
        }
    }

    /* loaded from: rt.jar:javax/print/DocFlavor$CHAR_ARRAY.class */
    public static class CHAR_ARRAY extends DocFlavor {
        private static final long serialVersionUID = -8720590903724405128L;
        public static final CHAR_ARRAY TEXT_PLAIN = new CHAR_ARRAY("text/plain; charset=utf-16");
        public static final CHAR_ARRAY TEXT_HTML = new CHAR_ARRAY("text/html; charset=utf-16");

        public CHAR_ARRAY(String str) {
            super(str, Constants.STATIC_CHAR_DATA_FIELD_SIG);
        }
    }

    /* loaded from: rt.jar:javax/print/DocFlavor$STRING.class */
    public static class STRING extends DocFlavor {
        private static final long serialVersionUID = 4414407504887034035L;
        public static final STRING TEXT_PLAIN = new STRING("text/plain; charset=utf-16");
        public static final STRING TEXT_HTML = new STRING("text/html; charset=utf-16");

        public STRING(String str) {
            super(str, "java.lang.String");
        }
    }

    /* loaded from: rt.jar:javax/print/DocFlavor$READER.class */
    public static class READER extends DocFlavor {
        private static final long serialVersionUID = 7100295812579351567L;
        public static final READER TEXT_PLAIN = new READER("text/plain; charset=utf-16");
        public static final READER TEXT_HTML = new READER("text/html; charset=utf-16");

        public READER(String str) {
            super(str, "java.io.Reader");
        }
    }

    /* loaded from: rt.jar:javax/print/DocFlavor$SERVICE_FORMATTED.class */
    public static class SERVICE_FORMATTED extends DocFlavor {
        private static final long serialVersionUID = 6181337766266637256L;
        public static final SERVICE_FORMATTED RENDERABLE_IMAGE = new SERVICE_FORMATTED("java.awt.image.renderable.RenderableImage");
        public static final SERVICE_FORMATTED PRINTABLE = new SERVICE_FORMATTED("java.awt.print.Printable");
        public static final SERVICE_FORMATTED PAGEABLE = new SERVICE_FORMATTED("java.awt.print.Pageable");

        public SERVICE_FORMATTED(String str) {
            super(DataFlavor.javaJVMLocalObjectMimeType, str);
        }
    }
}
