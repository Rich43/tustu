package java.lang;

import java.text.BreakIterator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import sun.text.Normalizer;

/* loaded from: rt.jar:java/lang/ConditionalSpecialCasing.class */
final class ConditionalSpecialCasing {
    static final int FINAL_CASED = 1;
    static final int AFTER_SOFT_DOTTED = 2;
    static final int MORE_ABOVE = 3;
    static final int AFTER_I = 4;
    static final int NOT_BEFORE_DOT = 5;
    static final int COMBINING_CLASS_ABOVE = 230;
    static Entry[] entry = {new Entry(931, new char[]{962}, new char[]{931}, null, 1), new Entry(304, new char[]{'i', 775}, new char[]{304}, null, 0), new Entry(775, new char[]{775}, new char[0], "lt", 2), new Entry(73, new char[]{'i', 775}, new char[]{'I'}, "lt", 3), new Entry(74, new char[]{'j', 775}, new char[]{'J'}, "lt", 3), new Entry(302, new char[]{303, 775}, new char[]{302}, "lt", 3), new Entry(204, new char[]{'i', 775, 768}, new char[]{204}, "lt", 0), new Entry(205, new char[]{'i', 775, 769}, new char[]{205}, "lt", 0), new Entry(296, new char[]{'i', 775, 771}, new char[]{296}, "lt", 0), new Entry(304, new char[]{'i'}, new char[]{304}, "tr", 0), new Entry(304, new char[]{'i'}, new char[]{304}, "az", 0), new Entry(775, new char[0], new char[]{775}, "tr", 4), new Entry(775, new char[0], new char[]{775}, "az", 4), new Entry(73, new char[]{305}, new char[]{'I'}, "tr", 5), new Entry(73, new char[]{305}, new char[]{'I'}, "az", 5), new Entry(105, new char[]{'i'}, new char[]{304}, "tr", 0), new Entry(105, new char[]{'i'}, new char[]{304}, "az", 0)};
    static Hashtable<Integer, HashSet<Entry>> entryTable = new Hashtable<>();

    ConditionalSpecialCasing() {
    }

    static {
        for (int i2 = 0; i2 < entry.length; i2++) {
            Entry entry2 = entry[i2];
            Integer num = new Integer(entry2.getCodePoint());
            HashSet<Entry> hashSet = entryTable.get(num);
            if (hashSet == null) {
                hashSet = new HashSet<>();
            }
            hashSet.add(entry2);
            entryTable.put(num, hashSet);
        }
    }

    static int toLowerCaseEx(String str, int i2, Locale locale) {
        char[] cArrLookUpTable = lookUpTable(str, i2, locale, true);
        if (cArrLookUpTable != null) {
            if (cArrLookUpTable.length == 1) {
                return cArrLookUpTable[0];
            }
            return -1;
        }
        return Character.toLowerCase(str.codePointAt(i2));
    }

    static int toUpperCaseEx(String str, int i2, Locale locale) {
        char[] cArrLookUpTable = lookUpTable(str, i2, locale, false);
        if (cArrLookUpTable != null) {
            if (cArrLookUpTable.length == 1) {
                return cArrLookUpTable[0];
            }
            return -1;
        }
        return Character.toUpperCaseEx(str.codePointAt(i2));
    }

    static char[] toLowerCaseCharArray(String str, int i2, Locale locale) {
        return lookUpTable(str, i2, locale, true);
    }

    static char[] toUpperCaseCharArray(String str, int i2, Locale locale) {
        char[] cArrLookUpTable = lookUpTable(str, i2, locale, false);
        if (cArrLookUpTable != null) {
            return cArrLookUpTable;
        }
        return Character.toUpperCaseCharArray(str.codePointAt(i2));
    }

    private static char[] lookUpTable(String str, int i2, Locale locale, boolean z2) {
        HashSet<Entry> hashSet = entryTable.get(new Integer(str.codePointAt(i2)));
        char[] lowerCase = null;
        if (hashSet != null) {
            Iterator<Entry> it = hashSet.iterator();
            String language = locale.getLanguage();
            while (it.hasNext()) {
                Entry next = it.next();
                String language2 = next.getLanguage();
                if (language2 == null || language2.equals(language)) {
                    if (isConditionMet(str, i2, locale, next.getCondition())) {
                        lowerCase = z2 ? next.getLowerCase() : next.getUpperCase();
                        if (language2 != null) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return lowerCase;
    }

    private static boolean isConditionMet(String str, int i2, Locale locale, int i3) {
        switch (i3) {
            case 1:
                return isFinalCased(str, i2, locale);
            case 2:
                return isAfterSoftDotted(str, i2);
            case 3:
                return isMoreAbove(str, i2);
            case 4:
                return isAfterI(str, i2);
            case 5:
                return !isBeforeDot(str, i2);
            default:
                return true;
        }
    }

    private static boolean isFinalCased(String str, int i2, Locale locale) {
        BreakIterator wordInstance = BreakIterator.getWordInstance(locale);
        wordInstance.setText(str);
        int iCharCount = i2;
        while (true) {
            int i3 = iCharCount;
            if (i3 >= 0 && !wordInstance.isBoundary(i3)) {
                int iCodePointBefore = str.codePointBefore(i3);
                if (!isCased(iCodePointBefore)) {
                    iCharCount = i3 - Character.charCount(iCodePointBefore);
                } else {
                    int length = str.length();
                    int i4 = i2;
                    int iCharCount2 = Character.charCount(str.codePointAt(i2));
                    while (true) {
                        int i5 = i4 + iCharCount2;
                        if (i5 < length && !wordInstance.isBoundary(i5)) {
                            int iCodePointAt = str.codePointAt(i5);
                            if (!isCased(iCodePointAt)) {
                                i4 = i5;
                                iCharCount2 = Character.charCount(iCodePointAt);
                            } else {
                                return false;
                            }
                        } else {
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    private static boolean isAfterI(String str, int i2) {
        int iCharCount = i2;
        while (true) {
            int i3 = iCharCount;
            if (i3 > 0) {
                int iCodePointBefore = str.codePointBefore(i3);
                if (iCodePointBefore == 73) {
                    return true;
                }
                int combiningClass = Normalizer.getCombiningClass(iCodePointBefore);
                if (combiningClass != 0 && combiningClass != 230) {
                    iCharCount = i3 - Character.charCount(iCodePointBefore);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private static boolean isAfterSoftDotted(String str, int i2) {
        int iCharCount = i2;
        while (true) {
            int i3 = iCharCount;
            if (i3 > 0) {
                int iCodePointBefore = str.codePointBefore(i3);
                if (isSoftDotted(iCodePointBefore)) {
                    return true;
                }
                int combiningClass = Normalizer.getCombiningClass(iCodePointBefore);
                if (combiningClass != 0 && combiningClass != 230) {
                    iCharCount = i3 - Character.charCount(iCodePointBefore);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private static boolean isMoreAbove(String str, int i2) {
        int length = str.length();
        int i3 = i2;
        int iCharCount = Character.charCount(str.codePointAt(i2));
        while (true) {
            int i4 = i3 + iCharCount;
            if (i4 < length) {
                int iCodePointAt = str.codePointAt(i4);
                int combiningClass = Normalizer.getCombiningClass(iCodePointAt);
                if (combiningClass == 230) {
                    return true;
                }
                if (combiningClass != 0) {
                    i3 = i4;
                    iCharCount = Character.charCount(iCodePointAt);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private static boolean isBeforeDot(String str, int i2) {
        int length = str.length();
        int i3 = i2;
        int iCharCount = Character.charCount(str.codePointAt(i2));
        while (true) {
            int i4 = i3 + iCharCount;
            if (i4 < length) {
                int iCodePointAt = str.codePointAt(i4);
                if (iCodePointAt == 775) {
                    return true;
                }
                int combiningClass = Normalizer.getCombiningClass(iCodePointAt);
                if (combiningClass != 0 && combiningClass != 230) {
                    i3 = i4;
                    iCharCount = Character.charCount(iCodePointAt);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private static boolean isCased(int i2) {
        int type = Character.getType(i2);
        if (type == 2 || type == 1 || type == 3) {
            return true;
        }
        if (i2 >= 688 && i2 <= 696) {
            return true;
        }
        if (i2 >= 704 && i2 <= 705) {
            return true;
        }
        if ((i2 >= 736 && i2 <= 740) || i2 == 837 || i2 == 890) {
            return true;
        }
        if (i2 >= 7468 && i2 <= 7521) {
            return true;
        }
        if (i2 >= 8544 && i2 <= 8575) {
            return true;
        }
        if (i2 >= 9398 && i2 <= 9449) {
            return true;
        }
        return false;
    }

    private static boolean isSoftDotted(int i2) {
        switch (i2) {
            case 105:
            case 106:
            case 303:
            case 616:
            case 1110:
            case 1112:
            case 7522:
            case 7725:
            case 7883:
            case 8305:
                return true;
            default:
                return false;
        }
    }

    /* loaded from: rt.jar:java/lang/ConditionalSpecialCasing$Entry.class */
    static class Entry {
        int ch;
        char[] lower;
        char[] upper;
        String lang;
        int condition;

        Entry(int i2, char[] cArr, char[] cArr2, String str, int i3) {
            this.ch = i2;
            this.lower = cArr;
            this.upper = cArr2;
            this.lang = str;
            this.condition = i3;
        }

        int getCodePoint() {
            return this.ch;
        }

        char[] getLowerCase() {
            return this.lower;
        }

        char[] getUpperCase() {
            return this.upper;
        }

        String getLanguage() {
            return this.lang;
        }

        int getCondition() {
            return this.condition;
        }
    }
}
