package sun.awt.windows;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import sun.awt.AWTCharset;

/* loaded from: rt.jar:sun/awt/windows/WDefaultFontCharset.class */
final class WDefaultFontCharset extends AWTCharset {
    private String fontName;

    /* JADX INFO: Access modifiers changed from: private */
    public native synchronized boolean canConvert(char c2);

    private static native void initIDs();

    static {
        initIDs();
    }

    WDefaultFontCharset(String str) {
        super("WDefaultFontCharset", Charset.forName("windows-1252"));
        this.fontName = str;
    }

    @Override // sun.awt.AWTCharset, java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder();
    }

    /* loaded from: rt.jar:sun/awt/windows/WDefaultFontCharset$Encoder.class */
    private class Encoder extends AWTCharset.Encoder {
        private Encoder() {
            super(WDefaultFontCharset.this);
        }

        @Override // sun.awt.AWTCharset.Encoder, java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return WDefaultFontCharset.this.canConvert(c2);
        }
    }
}
