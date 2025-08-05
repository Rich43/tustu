package sun.util.locale.provider;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.spi.CalendarNameProvider;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.Era;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/CalendarNameProviderImpl.class */
public class CalendarNameProviderImpl extends CalendarNameProvider implements AvailableLanguageTags {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;
    private static int[] REST_OF_STYLES = {Calendar.SHORT_STANDALONE, 2, Calendar.LONG_STANDALONE, 4, Calendar.NARROW_STANDALONE};

    public CalendarNameProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
        this.type = type;
        this.langtags = set;
    }

    @Override // java.util.spi.CalendarNameProvider
    public String getDisplayName(String str, int i2, int i3, int i4, Locale locale) {
        return getDisplayNameImpl(str, i2, i3, i4, locale, false);
    }

    public String getJavaTimeDisplayName(String str, int i2, int i3, int i4, Locale locale) {
        return getDisplayNameImpl(str, i2, i3, i4, locale, true);
    }

    public String getDisplayNameImpl(String str, int i2, int i3, int i4, Locale locale, boolean z2) {
        String displayName = null;
        String resourceKey = getResourceKey(str, i2, i4, z2);
        if (resourceKey != null) {
            LocaleResources localeResources = LocaleProviderAdapter.forType(this.type).getLocaleResources(locale);
            String[] javaTimeNames = z2 ? localeResources.getJavaTimeNames(resourceKey) : localeResources.getCalendarNames(resourceKey);
            if (javaTimeNames != null && javaTimeNames.length > 0) {
                if (i2 == 7 || i2 == 1) {
                    i3--;
                }
                if (i3 < 0) {
                    return null;
                }
                if (i3 >= javaTimeNames.length) {
                    if (i2 == 0 && "japanese".equals(str)) {
                        Era[] eras = CalendarSystem.forName("japanese").getEras();
                        if (i3 <= eras.length) {
                            if (this.type == LocaleProviderAdapter.Type.CLDR) {
                                LocaleResources localeResources2 = LocaleProviderAdapter.forJRE().getLocaleResources(locale);
                                String resourceKeyFor = getResourceKeyFor(LocaleProviderAdapter.Type.JRE, str, i2, i4, z2);
                                javaTimeNames = z2 ? localeResources2.getJavaTimeNames(resourceKeyFor) : localeResources2.getCalendarNames(resourceKeyFor);
                            }
                            if (javaTimeNames == null || i3 >= javaTimeNames.length) {
                                Era era = eras[i3 - 1];
                                if (z2) {
                                    if (getBaseStyle(i4) == 4) {
                                        return era.getAbbreviation();
                                    }
                                    return era.getName();
                                }
                                if ((i4 & 2) != 0) {
                                    return era.getName();
                                }
                                return era.getAbbreviation();
                            }
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
                displayName = javaTimeNames[i3];
                if (displayName.length() == 0 && (i4 == 32769 || i4 == 32770 || i4 == 32772)) {
                    displayName = getDisplayName(str, i2, i3, getBaseStyle(i4), locale);
                }
            }
        }
        return displayName;
    }

    @Override // java.util.spi.CalendarNameProvider
    public Map<String, Integer> getDisplayNames(String str, int i2, int i3, Locale locale) {
        Map<String, Integer> displayNamesImpl;
        if (i3 == 0) {
            displayNamesImpl = getDisplayNamesImpl(str, i2, 1, locale, false);
            for (int i4 : REST_OF_STYLES) {
                displayNamesImpl.putAll(getDisplayNamesImpl(str, i2, i4, locale, false));
            }
        } else {
            displayNamesImpl = getDisplayNamesImpl(str, i2, i3, locale, false);
        }
        if (displayNamesImpl.isEmpty()) {
            return null;
        }
        return displayNamesImpl;
    }

    public Map<String, Integer> getJavaTimeDisplayNames(String str, int i2, int i3, Locale locale) {
        Map<String, Integer> displayNamesImpl = getDisplayNamesImpl(str, i2, i3, locale, true);
        if (displayNamesImpl.isEmpty()) {
            return null;
        }
        return displayNamesImpl;
    }

    private Map<String, Integer> getDisplayNamesImpl(String str, int i2, int i3, Locale locale, boolean z2) {
        String resourceKey = getResourceKey(str, i2, i3, z2);
        TreeMap treeMap = new TreeMap(LengthBasedComparator.INSTANCE);
        if (resourceKey != null) {
            LocaleResources localeResources = LocaleProviderAdapter.forType(this.type).getLocaleResources(locale);
            String[] javaTimeNames = z2 ? localeResources.getJavaTimeNames(resourceKey) : localeResources.getCalendarNames(resourceKey);
            if (javaTimeNames != null && !hasDuplicates(javaTimeNames)) {
                if (i2 == 1) {
                    if (javaTimeNames.length > 0) {
                        treeMap.put(javaTimeNames[0], 1);
                    }
                } else {
                    int i4 = i2 == 7 ? 1 : 0;
                    for (int i5 = 0; i5 < javaTimeNames.length; i5++) {
                        String str2 = javaTimeNames[i5];
                        if (str2.length() != 0) {
                            treeMap.put(str2, Integer.valueOf(i4 + i5));
                        }
                    }
                }
            }
        }
        return treeMap;
    }

    private static int getBaseStyle(int i2) {
        return i2 & (-32769);
    }

    /* loaded from: rt.jar:sun/util/locale/provider/CalendarNameProviderImpl$LengthBasedComparator.class */
    private static class LengthBasedComparator implements Comparator<String> {
        private static final LengthBasedComparator INSTANCE = new LengthBasedComparator();

        private LengthBasedComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String str, String str2) {
            int length = str2.length() - str.length();
            return length == 0 ? str.compareTo(str2) : length;
        }
    }

    @Override // java.util.spi.LocaleServiceProvider
    public Locale[] getAvailableLocales() {
        return LocaleProviderAdapter.toLocaleArray(this.langtags);
    }

    @Override // java.util.spi.LocaleServiceProvider
    public boolean isSupportedLocale(Locale locale) {
        if (Locale.ROOT.equals(locale)) {
            return true;
        }
        String unicodeLocaleType = null;
        if (locale.hasExtensions()) {
            unicodeLocaleType = locale.getUnicodeLocaleType("ca");
            locale = locale.stripExtensions();
        }
        if (unicodeLocaleType != null) {
            switch (unicodeLocaleType) {
                case "buddhist":
                case "japanese":
                case "gregory":
                case "islamic":
                case "roc":
                    break;
                default:
                    return false;
            }
        }
        if (this.langtags.contains(locale.toLanguageTag())) {
            return true;
        }
        if (this.type == LocaleProviderAdapter.Type.JRE) {
            return this.langtags.contains(locale.toString().replace('_', '-'));
        }
        return false;
    }

    @Override // sun.util.locale.provider.AvailableLanguageTags
    public Set<String> getAvailableLanguageTags() {
        return this.langtags;
    }

    private boolean hasDuplicates(String[] strArr) {
        int length = strArr.length;
        for (int i2 = 0; i2 < length - 1; i2++) {
            String str = strArr[i2];
            if (str != null) {
                for (int i3 = i2 + 1; i3 < length; i3++) {
                    if (str.equals(strArr[i3])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String getResourceKey(String str, int i2, int i3, boolean z2) {
        return getResourceKeyFor(this.type, str, i2, i3, z2);
    }

    private static String getResourceKeyFor(LocaleProviderAdapter.Type type, String str, int i2, int i3, boolean z2) {
        int baseStyle = getBaseStyle(i3);
        boolean z3 = i3 != baseStyle;
        if ("gregory".equals(str)) {
            str = null;
        }
        boolean z4 = baseStyle == 4;
        StringBuilder sb = new StringBuilder();
        if (z2) {
            sb.append("java.time.");
        }
        switch (i2) {
            case 0:
                if (str != null) {
                    sb.append(str).append('.');
                }
                if (z4) {
                    sb.append("narrow.");
                } else if (type == LocaleProviderAdapter.Type.JRE) {
                    if (z2 && baseStyle == 2) {
                        sb.append("long.");
                    }
                    if (baseStyle == 1) {
                        sb.append("short.");
                    }
                } else if (baseStyle == 2) {
                    sb.append("long.");
                }
                sb.append("Eras");
                break;
            case 1:
                if (!z4) {
                    sb.append(str).append(".FirstYear");
                    break;
                }
                break;
            case 2:
                if ("islamic".equals(str)) {
                    sb.append(str).append('.');
                }
                if (z3) {
                    sb.append("standalone.");
                }
                sb.append("Month").append(toStyleName(baseStyle));
                break;
            case 7:
                if (z3 && z4) {
                    sb.append("standalone.");
                }
                sb.append("Day").append(toStyleName(baseStyle));
                break;
            case 9:
                if (z4) {
                    sb.append("narrow.");
                }
                sb.append("AmPmMarkers");
                break;
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

    private static String toStyleName(int i2) {
        switch (i2) {
            case 1:
                return "Abbreviations";
            case 4:
                return "Narrows";
            default:
                return "Names";
        }
    }
}
