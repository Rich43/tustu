package java.nio.charset;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import org.apache.commons.net.ftp.FTP;

/* loaded from: rt.jar:java/nio/charset/StandardCharsets.class */
public final class StandardCharsets {
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName(FTP.DEFAULT_CONTROL_ENCODING);
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset UTF_16BE = Charset.forName(FastInfosetSerializer.UTF_16BE);
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_16 = Charset.forName("UTF-16");

    private StandardCharsets() {
        throw new AssertionError((Object) "No java.nio.charset.StandardCharsets instances for you!");
    }
}
