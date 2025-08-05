package sun.awt.windows;

import sun.awt.PlatformFont;

/* loaded from: rt.jar:sun/awt/windows/WFontPeer.class */
final class WFontPeer extends PlatformFont {
    private String textComponentFontName;

    private static native void initIDs();

    public WFontPeer(String str, int i2) {
        super(str, i2);
        if (this.fontConfig != null) {
            this.textComponentFontName = ((WFontConfiguration) this.fontConfig).getTextComponentFontName(this.familyName, i2);
        }
    }

    @Override // sun.awt.PlatformFont
    protected char getMissingGlyphCharacter() {
        return (char) 10065;
    }

    static {
        initIDs();
    }
}
