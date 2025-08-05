package sun.font;

import java.awt.Font;
import java.util.Locale;
import java.util.TreeMap;

/* loaded from: rt.jar:sun/font/FontManagerForSGE.class */
public interface FontManagerForSGE extends FontManager {
    Font[] getCreatedFonts();

    TreeMap<String, String> getCreatedFontFamilyNames();

    Font[] getAllInstalledFonts();

    String[] getInstalledFontFamilyNames(Locale locale);

    void useAlternateFontforJALocales();
}
