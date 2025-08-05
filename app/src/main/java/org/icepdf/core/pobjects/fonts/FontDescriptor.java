package org.icepdf.core.pobjects.fonts;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/FontDescriptor.class */
public class FontDescriptor extends Dictionary {
    private FontFile font;
    private static final Logger logger = Logger.getLogger(FontDescriptor.class.toString());
    public static final Name TYPE = new Name("FontDescriptor");
    public static final Name FONT_NAME = new Name("FontName");
    public static final Name FONT_FAMILY = new Name("FontFamily");
    public static final Name MISSING_Stretch = new Name("FontStretch");
    public static final Name FONT_WEIGHT = new Name("FontWeight");
    public static final Name FLAGS = new Name("Flags");
    public static final Name FONT_BBOX = new Name("FontBBox");
    public static final Name ITALIC_ANGLE = new Name("ItalicAngle");
    public static final Name ASCENT = new Name("Ascent");
    public static final Name DESCENT = new Name("Descent");
    public static final Name LEADING = new Name("Leading");
    public static final Name CAP_HEIGHT = new Name("CapHeight");
    public static final Name X_HEIGHT = new Name("XHeight");
    public static final Name STEM_V = new Name("StemV");
    public static final Name STEM_H = new Name("StemH");
    public static final Name AVG_WIDTH = new Name("AvgWidth");
    public static final Name MAX_WIDTH = new Name("MaxWidth");
    public static final Name MISSING_WIDTH = new Name("MissingWidth");
    public static final Name FONT_FILE = new Name("FontFile");
    public static final Name FONT_FILE_2 = new Name("FontFile2");
    public static final Name FONT_FILE_3 = new Name("FontFile3");
    public static final Name FONT_FILE_3_TYPE_1C = new Name("Type1C");
    public static final Name FONT_FILE_3_CID_FONT_TYPE_0 = new Name("CIDFontType0");
    public static final Name FONT_FILE_3_CID_FONT_TYPE_2 = new Name("CIDFontType2");
    public static final Name FONT_FILE_3_CID_FONT_TYPE_0C = new Name("CIDFontType0C");
    public static final Name FONT_FILE_3_OPEN_TYPE = new Name("OpenType");

    public FontDescriptor(Library l2, HashMap h2) {
        super(l2, h2);
    }

    public static FontDescriptor createDescriptor(Library library, AFM afm) {
        HashMap<Name, Object> properties = new HashMap<>(7);
        properties.put(FONT_NAME, afm.getFontName());
        properties.put(FONT_FAMILY, afm.getFamilyName());
        properties.put(FONT_BBOX, afm.getFontBBox());
        properties.put(ITALIC_ANGLE, Float.valueOf(afm.getItalicAngle()));
        properties.put(MAX_WIDTH, Float.valueOf(afm.getMaxWidth()));
        properties.put(AVG_WIDTH, Integer.valueOf(afm.getAvgWidth()));
        properties.put(FLAGS, Integer.valueOf(afm.getFlags()));
        return new FontDescriptor(library, properties);
    }

    public String getFontName() {
        Object value = this.library.getObject(this.entries, FONT_NAME);
        if (value instanceof Name) {
            return ((Name) value).getName();
        }
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public String getFontFamily() {
        Object value = this.library.getObject(this.entries, FONT_FAMILY);
        if (value instanceof StringObject) {
            StringObject familyName = (StringObject) value;
            return familyName.getDecryptedLiteralString(this.library.getSecurityManager());
        }
        return FONT_NAME.getName();
    }

    public float getFontWeight() {
        Object value = this.library.getObject(this.entries, FONT_WEIGHT);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return 0.0f;
    }

    public float getMissingWidth() {
        Object value = this.library.getObject(this.entries, MISSING_WIDTH);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return 0.0f;
    }

    public float getAverageWidth() {
        Object value = this.library.getObject(this.entries, AVG_WIDTH);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return 0.0f;
    }

    public float getMaxWidth() {
        Object value = this.library.getObject(this.entries, MAX_WIDTH);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return 0.0f;
    }

    public float getAscent() {
        Object value = this.library.getObject(this.entries, ASCENT);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return 0.0f;
    }

    public float getDescent() {
        Object value = this.library.getObject(this.entries, DESCENT);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return 0.0f;
    }

    public FontFile getEmbeddedFont() {
        return this.font;
    }

    public PRectangle getFontBBox() {
        Object value = this.library.getObject(this.entries, FONT_BBOX);
        if (value instanceof List) {
            List rectangle = (List) value;
            return new PRectangle(rectangle);
        }
        return null;
    }

    public int getFlags() {
        Object value = this.library.getObject(this.entries, FLAGS);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        Stream fontStream;
        Stream fontStream2;
        if (this.inited) {
            return;
        }
        try {
            FontFactory fontFactory = FontFactory.getInstance();
            if (this.entries.containsKey(FONT_FILE) && (fontStream2 = (Stream) this.library.getObject(this.entries, FONT_FILE)) != null) {
                this.font = fontFactory.createFontFile(fontStream2, 1);
            }
            if (this.entries.containsKey(FONT_FILE_2) && (fontStream = (Stream) this.library.getObject(this.entries, FONT_FILE_2)) != null) {
                this.font = fontFactory.createFontFile(fontStream, 0);
            }
            if (this.entries.containsKey(FONT_FILE_3)) {
                Stream fontStream3 = (Stream) this.library.getObject(this.entries, FONT_FILE_3);
                Name subType = (Name) fontStream3.getObject(SUBTYPE_KEY);
                if (subType != null && (subType.equals(FONT_FILE_3_TYPE_1C) || subType.equals(FONT_FILE_3_CID_FONT_TYPE_0) || subType.equals(FONT_FILE_3_CID_FONT_TYPE_0C))) {
                    this.font = fontFactory.createFontFile(fontStream3, 1);
                }
                if (subType != null && subType.equals(FONT_FILE_3_OPEN_TYPE)) {
                    this.font = fontFactory.createFontFile(fontStream3, 5);
                }
            }
        } catch (Throwable e2) {
            logger.log(Level.FINE, "Error Reading Embedded Font ", e2);
        }
        this.inited = true;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        String name = null;
        if (this.font != null) {
            name = this.font.getName();
        }
        return ((Object) super.getPObjectReference()) + " FONTDESCRIPTOR= " + this.entries.toString() + " - " + name;
    }
}
