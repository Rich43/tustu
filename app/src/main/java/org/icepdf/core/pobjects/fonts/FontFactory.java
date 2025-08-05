package org.icepdf.core.pobjects.fonts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.fonts.ofont.OFont;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/FontFactory.class */
public class FontFactory {
    private static final Logger logger = Logger.getLogger(FontFactory.class.toString());
    private static boolean awtFontLoading = Defs.sysPropertyBoolean("org.icepdf.core.awtFontLoading", false);
    private static boolean awtFontSubstitution;
    public static final int FONT_OPEN_TYPE = 5;
    public static final int FONT_TRUE_TYPE = 0;
    public static final int FONT_TYPE_0 = 6;
    public static final int FONT_TYPE_1 = 1;
    public static final int FONT_TYPE_3 = 7;
    private static FontFactory fontFactory;
    private static final String FONT_CLASS = "org.icepdf.core.pobjects.fonts.nfont.Font";
    private static final String NFONT_CLASS = "org.icepdf.core.pobjects.fonts.nfont.NFont";
    private static final String NFONT_OPEN_TYPE = "org.icepdf.core.pobjects.fonts.nfont.NFontOpenType";
    private static final String NFONT_TRUE_TYPE = "org.icepdf.core.pobjects.fonts.nfont.NFontTrueType";
    private static final String NFONT_TRUE_TYPE_0 = "org.icepdf.core.pobjects.fonts.nfont.NFontType0";
    private static final String NFONT_TRUE_TYPE_1 = "org.icepdf.core.pobjects.fonts.nfont.NFontType1";
    private static final String NFONT_TRUE_TYPE_3 = "org.icepdf.core.pobjects.fonts.nfont.NFontType3";
    private static boolean foundNFont;

    static {
        try {
            Class.forName(NFONT_CLASS);
        } catch (ClassNotFoundException e2) {
            logger.log(Level.FINE, "NFont font library was not found on the class path");
        }
    }

    public static FontFactory getInstance() {
        if (fontFactory == null) {
            fontFactory = new FontFactory();
        }
        return fontFactory;
    }

    private FontFactory() {
    }

    public Font getFont(Library library, HashMap entries) {
        Font fontDictionary = null;
        if (foundFontEngine()) {
            try {
                Class<?> fontClass = Class.forName(FONT_CLASS);
                Class[] fontArgs = {Library.class, HashMap.class};
                Constructor fontClassConstructor = fontClass.getDeclaredConstructor(fontArgs);
                Object[] fontUrl = {library, entries};
                fontDictionary = (Font) fontClassConstructor.newInstance(fontUrl);
            } catch (Throwable e2) {
                logger.log(Level.FINE, "Could not load font dictionary class", e2);
            }
        } else {
            fontDictionary = new org.icepdf.core.pobjects.fonts.ofont.Font(library, entries);
        }
        return fontDictionary;
    }

    public FontFile createFontFile(Stream fontStream, int fontType) {
        FontFile fontFile = null;
        if (foundFontEngine()) {
            try {
                Class<?> fontClass = getNFontClass(fontType);
                if (fontClass != null) {
                    Class[] bytArrayArg = {byte[].class};
                    Constructor fontClassConstructor = fontClass.getDeclaredConstructor(bytArrayArg);
                    byte[] data = fontStream.getDecodedStreamBytes(0);
                    Object[] fontStreamBytes = {data};
                    if (data.length > 0) {
                        fontFile = (FontFile) fontClassConstructor.newInstance(fontStreamBytes);
                    }
                }
            } catch (Throwable e2) {
                logger.log(Level.FINE, "Could not create instance of font file " + fontType, e2);
            }
        } else if (awtFontLoading) {
            InputStream in = null;
            try {
                in = fontStream.getDecodedByteArrayInputStream();
                java.awt.Font javaFont = java.awt.Font.createFont(fontType, in);
                if (javaFont != null) {
                    fontFile = new OFont(javaFont);
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("Successfully created embedded OFont: " + fontTypeToString(fontType));
                    }
                    try {
                        in.close();
                    } catch (IOException e3) {
                        logger.log(Level.FINE, "Error closing font stream.", (Throwable) e3);
                    }
                }
            } catch (Throwable e4) {
                logger.log(Level.FINE, "Error reading font file with ", e4);
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable th) {
                        logger.log(Level.FINE, "Error closing font stream.", e4);
                    }
                }
            }
        }
        return fontFile;
    }

    public FontFile createFontFile(File file, int fontType) {
        try {
            return createFontFile(file.toURI().toURL(), fontType);
        } catch (Throwable e2) {
            logger.log(Level.FINE, "Could not create instance oof font file " + fontType, e2);
            return null;
        }
    }

    public FontFile createFontFile(URL url, int fontType) {
        FontFile fontFile = null;
        if (foundFontEngine()) {
            try {
                Class<?> fontClass = getNFontClass(fontType);
                if (fontClass != null) {
                    Class[] urlArg = {URL.class};
                    Constructor fontClassConstructor = fontClass.getDeclaredConstructor(urlArg);
                    Object[] fontUrl = {url};
                    fontFile = (FontFile) fontClassConstructor.newInstance(fontUrl);
                }
            } catch (Throwable e2) {
                logger.log(Level.FINE, "Could not create instance oof font file " + fontType, e2);
            }
        } else {
            try {
                java.awt.Font javaFont = java.awt.Font.createFont(fontType, url.openStream());
                if (javaFont != null) {
                    fontFile = new OFont(javaFont);
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("Successfully loaded OFont: " + ((Object) url));
                    }
                }
            } catch (Throwable e3) {
                logger.log(Level.FINE, "Error ready font file with ", e3);
            }
        }
        return fontFile;
    }

    public boolean isAwtFontSubstitution() {
        return awtFontSubstitution;
    }

    public void setAwtFontSubstitution(boolean awtFontSubstitution2) {
        awtFontSubstitution = awtFontSubstitution2;
    }

    public void toggleAwtFontSubstitution() {
        awtFontSubstitution = !awtFontSubstitution;
    }

    private Class getNFontClass(int fontType) throws ClassNotFoundException {
        Class fontClass = null;
        if (5 == fontType) {
            fontClass = Class.forName(NFONT_OPEN_TYPE);
        } else if (0 == fontType) {
            fontClass = Class.forName(NFONT_TRUE_TYPE);
        } else if (6 == fontType) {
            fontClass = Class.forName(NFONT_TRUE_TYPE_0);
        } else if (1 == fontType) {
            fontClass = Class.forName(NFONT_TRUE_TYPE_1);
        } else if (7 == fontType) {
            fontClass = Class.forName(NFONT_TRUE_TYPE_3);
        }
        return fontClass;
    }

    private String fontTypeToString(int fontType) {
        if (fontType == 5) {
            return "Open Type Font";
        }
        if (fontType == 0) {
            return "True Type Font";
        }
        if (fontType == 6) {
            return "Type 0 Font";
        }
        if (fontType == 1) {
            return "Type 1 Font";
        }
        if (fontType == 7) {
            return "Type 3 Font";
        }
        return "unkown font type: " + fontType;
    }

    public boolean foundFontEngine() {
        try {
            Class.forName(NFONT_CLASS);
            foundNFont = true;
        } catch (ClassNotFoundException e2) {
        }
        return foundNFont && !awtFontSubstitution;
    }
}
