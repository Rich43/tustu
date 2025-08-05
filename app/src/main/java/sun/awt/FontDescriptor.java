package sun.awt;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/awt/FontDescriptor.class */
public class FontDescriptor implements Cloneable {
    String nativeName;
    public CharsetEncoder encoder;
    String charsetName;
    private int[] exclusionRanges;
    public CharsetEncoder unicodeEncoder;
    boolean useUnicode = false;
    static boolean isLE;

    private static native void initIDs();

    static {
        NativeLibLoader.loadLibraries();
        initIDs();
        isLE = !"UnicodeBig".equals((String) AccessController.doPrivileged(new GetPropertyAction("sun.io.unicode.encoding", "UnicodeBig")));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FontDescriptor(String str, CharsetEncoder charsetEncoder, int[] iArr) {
        this.nativeName = str;
        this.encoder = charsetEncoder;
        this.exclusionRanges = iArr;
        Charset charset = charsetEncoder.charset();
        if (charset instanceof HistoricallyNamedCharset) {
            this.charsetName = ((HistoricallyNamedCharset) charset).historicalName();
        } else {
            this.charsetName = charset.name();
        }
    }

    public String getNativeName() {
        return this.nativeName;
    }

    public CharsetEncoder getFontCharsetEncoder() {
        return this.encoder;
    }

    public String getFontCharsetName() {
        return this.charsetName;
    }

    public int[] getExclusionRanges() {
        return this.exclusionRanges;
    }

    public boolean isExcluded(char c2) {
        int i2 = 0;
        while (i2 < this.exclusionRanges.length) {
            int i3 = i2;
            int i4 = i2 + 1;
            int i5 = this.exclusionRanges[i3];
            i2 = i4 + 1;
            int i6 = this.exclusionRanges[i4];
            if (c2 >= i5 && c2 <= i6) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return super.toString() + " [" + this.nativeName + CallSiteDescriptor.OPERATOR_DELIMITER + ((Object) this.encoder) + "]";
    }

    public boolean useUnicode() {
        CharsetEncoder charsetEncoderNewEncoder;
        if (this.useUnicode && this.unicodeEncoder == null) {
            try {
                if (isLE) {
                    charsetEncoderNewEncoder = StandardCharsets.UTF_16LE.newEncoder();
                } else {
                    charsetEncoderNewEncoder = StandardCharsets.UTF_16BE.newEncoder();
                }
                this.unicodeEncoder = charsetEncoderNewEncoder;
            } catch (IllegalArgumentException e2) {
            }
        }
        return this.useUnicode;
    }
}
