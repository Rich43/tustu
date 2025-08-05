package sun.font;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:sun/font/FontFamily.class */
public class FontFamily {
    private static ConcurrentHashMap<String, FontFamily> familyNameMap = new ConcurrentHashMap<>();
    private static HashMap<String, FontFamily> allLocaleNames;
    protected String familyName;
    protected Font2D plain;
    protected Font2D bold;
    protected Font2D italic;
    protected Font2D bolditalic;
    protected boolean logicalFont;
    protected int familyRank;
    private int familyWidth;

    public static FontFamily getFamily(String str) {
        return familyNameMap.get(str.toLowerCase(Locale.ENGLISH));
    }

    public static String[] getAllFamilyNames() {
        return null;
    }

    static void remove(Font2D font2D) {
        String familyName = font2D.getFamilyName(Locale.ENGLISH);
        FontFamily family = getFamily(familyName);
        if (family == null) {
            return;
        }
        if (family.plain == font2D) {
            family.plain = null;
        }
        if (family.bold == font2D) {
            family.bold = null;
        }
        if (family.italic == font2D) {
            family.italic = null;
        }
        if (family.bolditalic == font2D) {
            family.bolditalic = null;
        }
        if (family.plain == null && family.bold == null && family.plain == null && family.bold == null) {
            familyNameMap.remove(familyName);
        }
    }

    public FontFamily(String str, boolean z2, int i2) {
        this.logicalFont = false;
        this.familyWidth = 0;
        this.logicalFont = z2;
        this.familyName = str;
        this.familyRank = i2;
        familyNameMap.put(str.toLowerCase(Locale.ENGLISH), this);
    }

    FontFamily(String str) {
        this.logicalFont = false;
        this.familyWidth = 0;
        this.logicalFont = false;
        this.familyName = str;
        this.familyRank = 4;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public int getRank() {
        return this.familyRank;
    }

    private boolean isFromSameSource(Font2D font2D) {
        if (!(font2D instanceof FileFont)) {
            return false;
        }
        FileFont fileFont = null;
        if (this.plain instanceof FileFont) {
            fileFont = (FileFont) this.plain;
        } else if (this.bold instanceof FileFont) {
            fileFont = (FileFont) this.bold;
        } else if (this.italic instanceof FileFont) {
            fileFont = (FileFont) this.italic;
        } else if (this.bolditalic instanceof FileFont) {
            fileFont = (FileFont) this.bolditalic;
        }
        if (fileFont == null) {
            return false;
        }
        File parentFile = new File(fileFont.platName).getParentFile();
        File parentFile2 = new File(((FileFont) font2D).platName).getParentFile();
        if (parentFile != null) {
            try {
                parentFile = parentFile.getCanonicalFile();
            } catch (IOException e2) {
            }
        }
        if (parentFile2 != null) {
            try {
                parentFile2 = parentFile2.getCanonicalFile();
            } catch (IOException e3) {
            }
        }
        return Objects.equals(parentFile2, parentFile);
    }

    private boolean preferredWidth(Font2D font2D) {
        int width = font2D.getWidth();
        if (this.familyWidth == 0) {
            this.familyWidth = width;
            return true;
        }
        if (width == this.familyWidth) {
            return true;
        }
        if (Math.abs(5 - width) < Math.abs(5 - this.familyWidth)) {
            if (FontUtilities.debugFonts()) {
                FontUtilities.getLogger().info("Found more preferred width. New width = " + width + " Old width = " + this.familyWidth + " in font " + ((Object) font2D) + " nulling out fonts plain: " + ((Object) this.plain) + " bold: " + ((Object) this.bold) + " italic: " + ((Object) this.italic) + " bolditalic: " + ((Object) this.bolditalic));
            }
            this.familyWidth = width;
            this.bolditalic = null;
            this.italic = null;
            this.bold = null;
            this.plain = null;
            return true;
        }
        if (FontUtilities.debugFonts()) {
            FontUtilities.getLogger().info("Family rejecting font " + ((Object) font2D) + " of less preferred width " + width);
            return false;
        }
        return false;
    }

    private boolean closerWeight(Font2D font2D, Font2D font2D2, int i2) {
        if (this.familyWidth != font2D2.getWidth()) {
            return false;
        }
        if (font2D == null) {
            return true;
        }
        if (FontUtilities.debugFonts()) {
            FontUtilities.getLogger().info("New weight for style " + i2 + ". Curr.font=" + ((Object) font2D) + " New font=" + ((Object) font2D2) + " Curr.weight=" + font2D.getWeight() + " New weight=" + font2D2.getWeight());
        }
        int weight = font2D2.getWeight();
        switch (i2) {
            case 0:
            case 2:
                return weight <= 400 && weight > font2D.getWeight();
            case 1:
            case 3:
                return Math.abs(weight - Font2D.FWEIGHT_BOLD) < Math.abs(font2D.getWeight() - Font2D.FWEIGHT_BOLD);
            default:
                return false;
        }
    }

    public void setFont(Font2D font2D, int i2) {
        String str;
        if (FontUtilities.isLogging()) {
            if (font2D instanceof CompositeFont) {
                str = "Request to add " + font2D.getFamilyName(null) + " with style " + i2 + " to family " + this.familyName;
            } else {
                str = "Request to add " + ((Object) font2D) + " with style " + i2 + " to family " + ((Object) this);
            }
            FontUtilities.getLogger().info(str);
        }
        if (font2D.getRank() > this.familyRank && !isFromSameSource(font2D)) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().warning("Rejecting adding " + ((Object) font2D) + " of lower rank " + font2D.getRank() + " to family " + ((Object) this) + " of rank " + this.familyRank);
            }
            return;
        }
        switch (i2) {
            case 0:
                if (preferredWidth(font2D) && closerWeight(this.plain, font2D, i2)) {
                    this.plain = font2D;
                    break;
                }
                break;
            case 1:
                if (preferredWidth(font2D) && closerWeight(this.bold, font2D, i2)) {
                    this.bold = font2D;
                    break;
                }
                break;
            case 2:
                if (preferredWidth(font2D) && closerWeight(this.italic, font2D, i2)) {
                    this.italic = font2D;
                    break;
                }
                break;
            case 3:
                if (preferredWidth(font2D) && closerWeight(this.bolditalic, font2D, i2)) {
                    this.bolditalic = font2D;
                    break;
                }
                break;
        }
    }

    public Font2D getFontWithExactStyleMatch(int i2) {
        switch (i2) {
            case 0:
                return this.plain;
            case 1:
                return this.bold;
            case 2:
                return this.italic;
            case 3:
                return this.bolditalic;
            default:
                return null;
        }
    }

    public Font2D getFont(int i2) {
        switch (i2) {
            case 0:
                return this.plain;
            case 1:
                if (this.bold != null) {
                    return this.bold;
                }
                if (this.plain != null && this.plain.canDoStyle(i2)) {
                    return this.plain;
                }
                return null;
            case 2:
                if (this.italic != null) {
                    return this.italic;
                }
                if (this.plain != null && this.plain.canDoStyle(i2)) {
                    return this.plain;
                }
                return null;
            case 3:
                if (this.bolditalic != null) {
                    return this.bolditalic;
                }
                if (this.bold != null && this.bold.canDoStyle(i2)) {
                    return this.bold;
                }
                if (this.italic != null && this.italic.canDoStyle(i2)) {
                    return this.italic;
                }
                if (this.plain != null && this.plain.canDoStyle(i2)) {
                    return this.plain;
                }
                return null;
            default:
                return null;
        }
    }

    Font2D getClosestStyle(int i2) {
        switch (i2) {
            case 0:
                if (this.bold != null) {
                    return this.bold;
                }
                if (this.italic != null) {
                    return this.italic;
                }
                return this.bolditalic;
            case 1:
                if (this.plain != null) {
                    return this.plain;
                }
                if (this.bolditalic != null) {
                    return this.bolditalic;
                }
                return this.italic;
            case 2:
                if (this.bolditalic != null) {
                    return this.bolditalic;
                }
                if (this.plain != null) {
                    return this.plain;
                }
                return this.bold;
            case 3:
                if (this.italic != null) {
                    return this.italic;
                }
                if (this.bold != null) {
                    return this.bold;
                }
                return this.plain;
            default:
                return null;
        }
    }

    static synchronized void addLocaleNames(FontFamily fontFamily, String[] strArr) {
        if (allLocaleNames == null) {
            allLocaleNames = new HashMap<>();
        }
        for (String str : strArr) {
            allLocaleNames.put(str.toLowerCase(), fontFamily);
        }
    }

    public static synchronized FontFamily getLocaleFamily(String str) {
        if (allLocaleNames == null) {
            return null;
        }
        return allLocaleNames.get(str.toLowerCase());
    }

    public static FontFamily[] getAllFontFamilies() {
        return (FontFamily[]) familyNameMap.values().toArray(new FontFamily[0]);
    }

    public String toString() {
        return "Font family: " + this.familyName + " plain=" + ((Object) this.plain) + " bold=" + ((Object) this.bold) + " italic=" + ((Object) this.italic) + " bolditalic=" + ((Object) this.bolditalic);
    }
}
