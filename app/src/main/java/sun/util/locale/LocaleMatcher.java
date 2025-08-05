package sun.util.locale;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:sun/util/locale/LocaleMatcher.class */
public final class LocaleMatcher {
    public static List<Locale> filter(List<Locale.LanguageRange> list, Collection<Locale> collection, Locale.FilteringMode filteringMode) {
        if (list.isEmpty() || collection.isEmpty()) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        Iterator<Locale> it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().toLanguageTag());
        }
        List<String> listFilterTags = filterTags(list, arrayList, filteringMode);
        ArrayList arrayList2 = new ArrayList(listFilterTags.size());
        Iterator<String> it2 = listFilterTags.iterator();
        while (it2.hasNext()) {
            arrayList2.add(Locale.forLanguageTag(it2.next()));
        }
        return arrayList2;
    }

    public static List<String> filterTags(List<Locale.LanguageRange> list, Collection<String> collection, Locale.FilteringMode filteringMode) {
        String strReplaceAll;
        if (list.isEmpty() || collection.isEmpty()) {
            return new ArrayList();
        }
        if (filteringMode == Locale.FilteringMode.EXTENDED_FILTERING) {
            return filterExtended(list, collection);
        }
        ArrayList arrayList = new ArrayList();
        for (Locale.LanguageRange languageRange : list) {
            String range = languageRange.getRange();
            if (range.startsWith("*-") || range.indexOf("-*") != -1) {
                if (filteringMode == Locale.FilteringMode.AUTOSELECT_FILTERING) {
                    return filterExtended(list, collection);
                }
                if (filteringMode == Locale.FilteringMode.MAP_EXTENDED_RANGES) {
                    if (range.charAt(0) == '*') {
                        strReplaceAll = "*";
                    } else {
                        strReplaceAll = range.replaceAll("-[*]", "");
                    }
                    arrayList.add(new Locale.LanguageRange(strReplaceAll, languageRange.getWeight()));
                } else if (filteringMode == Locale.FilteringMode.REJECT_EXTENDED_RANGES) {
                    throw new IllegalArgumentException("An extended range \"" + range + "\" found in REJECT_EXTENDED_RANGES mode.");
                }
            } else {
                arrayList.add(languageRange);
            }
        }
        return filterBasic(arrayList, collection);
    }

    private static List<String> filterBasic(List<Locale.LanguageRange> list, Collection<String> collection) {
        int length;
        ArrayList arrayList = new ArrayList();
        Iterator<Locale.LanguageRange> it = list.iterator();
        while (it.hasNext()) {
            String range = it.next().getRange();
            if (range.equals("*")) {
                return new ArrayList(collection);
            }
            Iterator<String> it2 = collection.iterator();
            while (it2.hasNext()) {
                String lowerCase = it2.next().toLowerCase();
                if (lowerCase.startsWith(range) && (lowerCase.length() == (length = range.length()) || lowerCase.charAt(length) == '-')) {
                    if (!arrayList.contains(lowerCase)) {
                        arrayList.add(lowerCase);
                    }
                }
            }
        }
        return arrayList;
    }

    private static List<String> filterExtended(List<Locale.LanguageRange> list, Collection<String> collection) {
        ArrayList arrayList = new ArrayList();
        Iterator<Locale.LanguageRange> it = list.iterator();
        while (it.hasNext()) {
            String range = it.next().getRange();
            if (range.equals("*")) {
                return new ArrayList(collection);
            }
            String[] strArrSplit = range.split(LanguageTag.SEP);
            Iterator<String> it2 = collection.iterator();
            while (it2.hasNext()) {
                String lowerCase = it2.next().toLowerCase();
                String[] strArrSplit2 = lowerCase.split(LanguageTag.SEP);
                if (strArrSplit[0].equals(strArrSplit2[0]) || strArrSplit[0].equals("*")) {
                    int i2 = 1;
                    int i3 = 1;
                    while (i2 < strArrSplit.length && i3 < strArrSplit2.length) {
                        if (strArrSplit[i2].equals("*")) {
                            i2++;
                        } else if (strArrSplit[i2].equals(strArrSplit2[i3])) {
                            i2++;
                            i3++;
                        } else {
                            if (strArrSplit2[i3].length() == 1 && !strArrSplit2[i3].equals("*")) {
                                break;
                            }
                            i3++;
                        }
                    }
                    if (strArrSplit.length == i2 && !arrayList.contains(lowerCase)) {
                        arrayList.add(lowerCase);
                    }
                }
            }
        }
        return arrayList;
    }

    public static Locale lookup(List<Locale.LanguageRange> list, Collection<Locale> collection) {
        if (list.isEmpty() || collection.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<Locale> it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().toLanguageTag());
        }
        String strLookupTag = lookupTag(list, arrayList);
        if (strLookupTag == null) {
            return null;
        }
        return Locale.forLanguageTag(strLookupTag);
    }

    public static String lookupTag(List<Locale.LanguageRange> list, Collection<String> collection) {
        if (list.isEmpty() || collection.isEmpty()) {
            return null;
        }
        Iterator<Locale.LanguageRange> it = list.iterator();
        while (it.hasNext()) {
            String range = it.next().getRange();
            if (!range.equals("*")) {
                String strReplaceAll = range.replaceAll("\\x2A", "\\\\p{Alnum}*");
                while (strReplaceAll.length() > 0) {
                    Iterator<String> it2 = collection.iterator();
                    while (it2.hasNext()) {
                        String lowerCase = it2.next().toLowerCase();
                        if (lowerCase.matches(strReplaceAll)) {
                            return lowerCase;
                        }
                    }
                    int iLastIndexOf = strReplaceAll.lastIndexOf(45);
                    if (iLastIndexOf >= 0) {
                        strReplaceAll = strReplaceAll.substring(0, iLastIndexOf);
                        if (strReplaceAll.lastIndexOf(45) == strReplaceAll.length() - 2) {
                            strReplaceAll = strReplaceAll.substring(0, strReplaceAll.length() - 2);
                        }
                    } else {
                        strReplaceAll = "";
                    }
                }
            }
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static List<Locale.LanguageRange> parse(String str) {
        String strSubstring;
        double d2;
        String lowerCase = str.replaceAll(" ", "").toLowerCase();
        if (lowerCase.startsWith("accept-language:")) {
            lowerCase = lowerCase.substring(16);
        }
        String[] strArrSplit = lowerCase.split(",");
        ArrayList arrayList = new ArrayList(strArrSplit.length);
        ArrayList arrayList2 = new ArrayList();
        int i2 = 0;
        for (String str2 : strArrSplit) {
            int iIndexOf = str2.indexOf(";q=");
            if (iIndexOf == -1) {
                strSubstring = str2;
                d2 = 1.0d;
            } else {
                strSubstring = str2.substring(0, iIndexOf);
                int i3 = iIndexOf + 3;
                try {
                    d2 = Double.parseDouble(str2.substring(i3));
                    if (d2 < 0.0d || d2 > 1.0d) {
                        throw new IllegalArgumentException("weight=" + d2 + " for language range \"" + strSubstring + "\". It must be between 0.0 and 1.0.");
                    }
                } catch (Exception e2) {
                    throw new IllegalArgumentException("weight=\"" + str2.substring(i3) + "\" for language range \"" + strSubstring + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
            }
            if (!arrayList2.contains(strSubstring)) {
                Locale.LanguageRange languageRange = new Locale.LanguageRange(strSubstring, d2);
                int i4 = i2;
                int i5 = 0;
                while (true) {
                    if (i5 >= i2) {
                        break;
                    }
                    if (((Locale.LanguageRange) arrayList.get(i5)).getWeight() >= d2) {
                        i5++;
                    } else {
                        i4 = i5;
                        break;
                    }
                }
                arrayList.add(i4, languageRange);
                i2++;
                arrayList2.add(strSubstring);
                String equivalentForRegionAndVariant = getEquivalentForRegionAndVariant(strSubstring);
                if (equivalentForRegionAndVariant != null && !arrayList2.contains(equivalentForRegionAndVariant)) {
                    arrayList.add(i4 + 1, new Locale.LanguageRange(equivalentForRegionAndVariant, d2));
                    i2++;
                    arrayList2.add(equivalentForRegionAndVariant);
                }
                String[] equivalentsForLanguage = getEquivalentsForLanguage(strSubstring);
                if (equivalentsForLanguage != null) {
                    for (String str3 : equivalentsForLanguage) {
                        if (!arrayList2.contains(str3)) {
                            arrayList.add(i4 + 1, new Locale.LanguageRange(str3, d2));
                            i2++;
                            arrayList2.add(str3);
                        }
                        String equivalentForRegionAndVariant2 = getEquivalentForRegionAndVariant(str3);
                        if (equivalentForRegionAndVariant2 != null && !arrayList2.contains(equivalentForRegionAndVariant2)) {
                            arrayList.add(i4 + 1, new Locale.LanguageRange(equivalentForRegionAndVariant2, d2));
                            i2++;
                            arrayList2.add(equivalentForRegionAndVariant2);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    private static String[] getEquivalentsForLanguage(String str) {
        String strSubstring = str;
        while (true) {
            String str2 = strSubstring;
            if (str2.length() > 0) {
                if (LocaleEquivalentMaps.singleEquivMap.containsKey(str2)) {
                    return new String[]{str.replaceFirst(str2, LocaleEquivalentMaps.singleEquivMap.get(str2))};
                }
                if (LocaleEquivalentMaps.multiEquivsMap.containsKey(str2)) {
                    String[] strArr = LocaleEquivalentMaps.multiEquivsMap.get(str2);
                    for (int i2 = 0; i2 < strArr.length; i2++) {
                        strArr[i2] = str.replaceFirst(str2, strArr[i2]);
                    }
                    return strArr;
                }
                int iLastIndexOf = str2.lastIndexOf(45);
                if (iLastIndexOf != -1) {
                    strSubstring = str2.substring(0, iLastIndexOf);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private static String getEquivalentForRegionAndVariant(String str) {
        int extentionKeyIndex = getExtentionKeyIndex(str);
        for (String str2 : LocaleEquivalentMaps.regionVariantEquivMap.keySet()) {
            int iIndexOf = str.indexOf(str2);
            if (iIndexOf != -1 && (extentionKeyIndex == Integer.MIN_VALUE || iIndexOf <= extentionKeyIndex)) {
                int length = iIndexOf + str2.length();
                if (str.length() == length || str.charAt(length) == '-') {
                    return str.replaceFirst(str2, LocaleEquivalentMaps.regionVariantEquivMap.get(str2));
                }
            }
        }
        return null;
    }

    private static int getExtentionKeyIndex(String str) {
        char[] charArray = str.toCharArray();
        int i2 = Integer.MIN_VALUE;
        for (int i3 = 1; i3 < charArray.length; i3++) {
            if (charArray[i3] == '-') {
                if (i3 - i2 == 2) {
                    return i2;
                }
                i2 = i3;
            }
        }
        return Integer.MIN_VALUE;
    }

    public static List<Locale.LanguageRange> mapEquivalents(List<Locale.LanguageRange> list, Map<String, List<String>> map) {
        if (list.isEmpty()) {
            return new ArrayList();
        }
        if (map == null || map.isEmpty()) {
            return new ArrayList(list);
        }
        HashMap map2 = new HashMap();
        for (String str : map.keySet()) {
            map2.put(str.toLowerCase(), str);
        }
        ArrayList arrayList = new ArrayList();
        for (Locale.LanguageRange languageRange : list) {
            String range = languageRange.getRange();
            String strSubstring = range;
            boolean z2 = false;
            while (true) {
                if (strSubstring.length() <= 0) {
                    break;
                }
                if (map2.containsKey(strSubstring)) {
                    z2 = true;
                    List<String> list2 = map.get(map2.get(strSubstring));
                    if (list2 != null) {
                        int length = strSubstring.length();
                        Iterator<String> it = list2.iterator();
                        while (it.hasNext()) {
                            arrayList.add(new Locale.LanguageRange(it.next().toLowerCase() + range.substring(length), languageRange.getWeight()));
                        }
                    }
                } else {
                    int iLastIndexOf = strSubstring.lastIndexOf(45);
                    if (iLastIndexOf == -1) {
                        break;
                    }
                    strSubstring = strSubstring.substring(0, iLastIndexOf);
                }
            }
            if (!z2) {
                arrayList.add(languageRange);
            }
        }
        return arrayList;
    }

    private LocaleMatcher() {
    }
}
