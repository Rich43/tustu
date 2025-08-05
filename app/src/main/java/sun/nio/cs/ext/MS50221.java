package sun.nio.cs.ext;

import java.nio.charset.Charset;

/* loaded from: charsets.jar:sun/nio/cs/ext/MS50221.class */
public class MS50221 extends MS50220 {
    public MS50221() {
        super("x-windows-50221", ExtendedCharsets.aliasesFor("x-windows-50221"));
    }

    @Override // sun.nio.cs.ext.MS50220, sun.nio.cs.ext.ISO2022_JP, sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "MS50221";
    }

    @Override // sun.nio.cs.ext.MS50220, sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return super.contains(charset) || (charset instanceof JIS_X_0212) || (charset instanceof MS50221);
    }

    @Override // sun.nio.cs.ext.MS50220, sun.nio.cs.ext.ISO2022_JP
    protected boolean doSBKANA() {
        return true;
    }
}
