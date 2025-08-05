package java.util.regex;

import java.util.HashMap;
import java.util.Locale;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;
import sun.font.CharToGlyphMapper;

/* loaded from: rt.jar:java/util/regex/UnicodeProp.class */
enum UnicodeProp {
    ALPHABETIC { // from class: java.util.regex.UnicodeProp.1
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.isAlphabetic(i2);
        }
    },
    LETTER { // from class: java.util.regex.UnicodeProp.2
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.isLetter(i2);
        }
    },
    IDEOGRAPHIC { // from class: java.util.regex.UnicodeProp.3
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.isIdeographic(i2);
        }
    },
    LOWERCASE { // from class: java.util.regex.UnicodeProp.4
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.isLowerCase(i2);
        }
    },
    UPPERCASE { // from class: java.util.regex.UnicodeProp.5
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.isUpperCase(i2);
        }
    },
    TITLECASE { // from class: java.util.regex.UnicodeProp.6
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.isTitleCase(i2);
        }
    },
    WHITE_SPACE { // from class: java.util.regex.UnicodeProp.7
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return ((CharacterType.SPACE_MASK >> Character.getType(i2)) & 1) != 0 || (i2 >= 9 && i2 <= 13) || i2 == 133;
        }
    },
    CONTROL { // from class: java.util.regex.UnicodeProp.8
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.getType(i2) == 15;
        }
    },
    PUNCTUATION { // from class: java.util.regex.UnicodeProp.9
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return ((CharacterType.PUNCT_MASK >> Character.getType(i2)) & 1) != 0;
        }
    },
    HEX_DIGIT { // from class: java.util.regex.UnicodeProp.10
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return DIGIT.is(i2) || (i2 >= 48 && i2 <= 57) || ((i2 >= 65 && i2 <= 70) || ((i2 >= 97 && i2 <= 102) || ((i2 >= 65296 && i2 <= 65305) || ((i2 >= 65313 && i2 <= 65318) || (i2 >= 65345 && i2 <= 65350)))));
        }
    },
    ASSIGNED { // from class: java.util.regex.UnicodeProp.11
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.getType(i2) != 0;
        }
    },
    NONCHARACTER_CODE_POINT { // from class: java.util.regex.UnicodeProp.12
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return (i2 & CharToGlyphMapper.INVISIBLE_GLYPHS) == 65534 || (i2 >= 64976 && i2 <= 65007);
        }
    },
    DIGIT { // from class: java.util.regex.UnicodeProp.13
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.isDigit(i2);
        }
    },
    ALNUM { // from class: java.util.regex.UnicodeProp.14
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return ALPHABETIC.is(i2) || DIGIT.is(i2);
        }
    },
    BLANK { // from class: java.util.regex.UnicodeProp.15
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return Character.getType(i2) == 12 || i2 == 9;
        }
    },
    GRAPH { // from class: java.util.regex.UnicodeProp.16
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return ((585729 >> Character.getType(i2)) & 1) == 0;
        }
    },
    PRINT { // from class: java.util.regex.UnicodeProp.17
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return (GRAPH.is(i2) || BLANK.is(i2)) && !CONTROL.is(i2);
        }
    },
    WORD { // from class: java.util.regex.UnicodeProp.18
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return ALPHABETIC.is(i2) || ((8389568 >> Character.getType(i2)) & 1) != 0 || JOIN_CONTROL.is(i2);
        }
    },
    JOIN_CONTROL { // from class: java.util.regex.UnicodeProp.19
        @Override // java.util.regex.UnicodeProp
        public boolean is(int i2) {
            return i2 == 8204 || i2 == 8205;
        }
    };

    private static final HashMap<String, String> posix = new HashMap<>();
    private static final HashMap<String, String> aliases = new HashMap<>();

    public abstract boolean is(int i2);

    static {
        posix.put("ALPHA", "ALPHABETIC");
        posix.put("LOWER", "LOWERCASE");
        posix.put("UPPER", "UPPERCASE");
        posix.put("SPACE", "WHITE_SPACE");
        posix.put("PUNCT", "PUNCTUATION");
        posix.put("XDIGIT", "HEX_DIGIT");
        posix.put("ALNUM", "ALNUM");
        posix.put("CNTRL", "CONTROL");
        posix.put("DIGIT", "DIGIT");
        posix.put("BLANK", "BLANK");
        posix.put("GRAPH", "GRAPH");
        posix.put("PRINT", "PRINT");
        aliases.put("WHITESPACE", "WHITE_SPACE");
        aliases.put("HEXDIGIT", "HEX_DIGIT");
        aliases.put("NONCHARACTERCODEPOINT", "NONCHARACTER_CODE_POINT");
        aliases.put("JOINCONTROL", "JOIN_CONTROL");
    }

    public static UnicodeProp forName(String str) {
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        String str2 = aliases.get(upperCase);
        if (str2 != null) {
            upperCase = str2;
        }
        try {
            return valueOf(upperCase);
        } catch (IllegalArgumentException e2) {
            return null;
        }
    }

    public static UnicodeProp forPOSIXName(String str) {
        String str2 = posix.get(str.toUpperCase(Locale.ENGLISH));
        if (str2 == null) {
            return null;
        }
        return valueOf(str2);
    }
}
