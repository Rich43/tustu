package org.icepdf.core.util;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/FontUtil.class */
public class FontUtil {
    private static final String AWT_STYLE_BOLD_ITAL = "boldital";
    private static final String AWT_STYLE_DEMI_ITAL = "demiital";
    private static final String AWT_STYLE_ITAL = "ital";
    private static final String AWT_STYLE_OBLI = "obli";
    private static final String STYLE_BOLD_ITALIC = "bolditalic";
    private static final String STYLE_DEMI_ITALIC = "demiitalic";
    private static final String STYLE_BOLD = "bold";
    private static final String STYLE_DEMI = "demi";
    private static final String STYLE_ITALIC = "italic";
    private static final String STYLE_BLACK = "black";

    public static int guessAWTFontStyle(String name) {
        int decorations;
        String name2 = name.toLowerCase();
        if (name2.indexOf(AWT_STYLE_BOLD_ITAL) > 0 || name2.indexOf(AWT_STYLE_DEMI_ITAL) > 0) {
            decorations = 0 | 3;
        } else if (name2.indexOf(STYLE_BOLD) > 0 || name2.indexOf(STYLE_BLACK) > 0 || name2.indexOf(STYLE_DEMI) > 0) {
            decorations = 0 | 1;
        } else if (name2.indexOf(AWT_STYLE_ITAL) > 0 || name2.indexOf(AWT_STYLE_OBLI) > 0) {
            decorations = 0 | 2;
        } else {
            decorations = 0 | 0;
        }
        return decorations;
    }

    public static String guessFamily(String name) {
        String fam = name;
        int inx = fam.indexOf(44);
        if (inx > 0) {
            fam = fam.substring(0, inx);
        }
        int inx2 = fam.lastIndexOf(45);
        if (inx2 > 0) {
            fam = fam.substring(0, inx2);
        }
        int inx3 = fam.toLowerCase().lastIndexOf(STYLE_BOLD_ITALIC);
        if (inx3 > 0) {
            fam = fam.substring(0, inx3);
        } else {
            int inx4 = fam.toLowerCase().lastIndexOf(STYLE_DEMI_ITALIC);
            if (inx4 > 0) {
                fam = fam.substring(0, inx4);
            } else {
                int inx5 = fam.toLowerCase().lastIndexOf(STYLE_BOLD);
                if (inx5 > 0) {
                    fam = fam.substring(0, inx5);
                } else {
                    int inx6 = fam.toLowerCase().lastIndexOf(STYLE_ITALIC);
                    if (inx6 > 0) {
                        fam = fam.substring(0, inx6);
                    } else {
                        int inx7 = fam.toLowerCase().lastIndexOf(STYLE_BLACK);
                        if (inx7 > 0) {
                            fam = fam.substring(0, inx7);
                        }
                    }
                }
            }
        }
        return fam;
    }

    public static String removeBaseFontSubset(String name) {
        if (name != null && name.length() > 7) {
            int i2 = name.indexOf(43) + 1;
            return name.substring(i2, name.length());
        }
        return name;
    }

    public static String normalizeString(String name) {
        StringBuilder normalized = new StringBuilder(guessFamily(name).toLowerCase());
        int k2 = normalized.length() - 1;
        while (k2 >= 0) {
            if (normalized.charAt(k2) == ' ') {
                normalized.deleteCharAt(k2);
                k2--;
            }
            k2--;
        }
        return normalized.toString();
    }
}
