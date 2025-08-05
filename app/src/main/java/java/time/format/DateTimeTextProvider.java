package java.time.format;

import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseChronology;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalField;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.util.locale.provider.CalendarDataUtility;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:java/time/format/DateTimeTextProvider.class */
class DateTimeTextProvider {
    private static final ConcurrentMap<Map.Entry<TemporalField, Locale>, Object> CACHE = new ConcurrentHashMap(16, 0.75f, 2);
    private static final Comparator<Map.Entry<String, Long>> COMPARATOR = new Comparator<Map.Entry<String, Long>>() { // from class: java.time.format.DateTimeTextProvider.1
        @Override // java.util.Comparator
        public int compare(Map.Entry<String, Long> entry, Map.Entry<String, Long> entry2) {
            return entry2.getKey().length() - entry.getKey().length();
        }
    };

    DateTimeTextProvider() {
    }

    static DateTimeTextProvider getInstance() {
        return new DateTimeTextProvider();
    }

    public String getText(TemporalField temporalField, long j2, TextStyle textStyle, Locale locale) {
        Object objFindStore = findStore(temporalField, locale);
        if (objFindStore instanceof LocaleStore) {
            return ((LocaleStore) objFindStore).getText(j2, textStyle);
        }
        return null;
    }

    public String getText(Chronology chronology, TemporalField temporalField, long j2, TextStyle textStyle, Locale locale) {
        int i2;
        int i3;
        if (chronology == IsoChronology.INSTANCE || !(temporalField instanceof ChronoField)) {
            return getText(temporalField, j2, textStyle, locale);
        }
        if (temporalField == ChronoField.ERA) {
            i2 = 0;
            if (chronology != JapaneseChronology.INSTANCE) {
                i3 = (int) j2;
            } else if (j2 == -999) {
                i3 = 0;
            } else {
                i3 = ((int) j2) + 2;
            }
        } else if (temporalField == ChronoField.MONTH_OF_YEAR) {
            i2 = 2;
            i3 = ((int) j2) - 1;
        } else if (temporalField == ChronoField.DAY_OF_WEEK) {
            i2 = 7;
            i3 = ((int) j2) + 1;
            if (i3 > 7) {
                i3 = 1;
            }
        } else if (temporalField == ChronoField.AMPM_OF_DAY) {
            i2 = 9;
            i3 = (int) j2;
        } else {
            return null;
        }
        return CalendarDataUtility.retrieveJavaTimeFieldValueName(chronology.getCalendarType(), i2, i3, textStyle.toCalendarStyle(), locale);
    }

    public Iterator<Map.Entry<String, Long>> getTextIterator(TemporalField temporalField, TextStyle textStyle, Locale locale) {
        Object objFindStore = findStore(temporalField, locale);
        if (objFindStore instanceof LocaleStore) {
            return ((LocaleStore) objFindStore).getTextIterator(textStyle);
        }
        return null;
    }

    public Iterator<Map.Entry<String, Long>> getTextIterator(Chronology chronology, TemporalField temporalField, TextStyle textStyle, Locale locale) {
        int i2;
        if (chronology == IsoChronology.INSTANCE || !(temporalField instanceof ChronoField)) {
            return getTextIterator(temporalField, textStyle, locale);
        }
        switch ((ChronoField) temporalField) {
            case ERA:
                i2 = 0;
                break;
            case MONTH_OF_YEAR:
                i2 = 2;
                break;
            case DAY_OF_WEEK:
                i2 = 7;
                break;
            case AMPM_OF_DAY:
                i2 = 9;
                break;
            default:
                return null;
        }
        Map<String, Integer> mapRetrieveJavaTimeFieldValueNames = CalendarDataUtility.retrieveJavaTimeFieldValueNames(chronology.getCalendarType(), i2, textStyle == null ? 0 : textStyle.toCalendarStyle(), locale);
        if (mapRetrieveJavaTimeFieldValueNames == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(mapRetrieveJavaTimeFieldValueNames.size());
        switch (i2) {
            case 0:
                for (Map.Entry<String, Integer> entry : mapRetrieveJavaTimeFieldValueNames.entrySet()) {
                    int iIntValue = entry.getValue().intValue();
                    if (chronology == JapaneseChronology.INSTANCE) {
                        iIntValue = iIntValue == 0 ? -999 : iIntValue - 2;
                    }
                    arrayList.add(createEntry(entry.getKey(), Long.valueOf(iIntValue)));
                }
                break;
            case 2:
                Iterator<Map.Entry<String, Integer>> it = mapRetrieveJavaTimeFieldValueNames.entrySet().iterator();
                while (it.hasNext()) {
                    arrayList.add(createEntry(it.next().getKey(), Long.valueOf(r0.getValue().intValue() + 1)));
                }
                break;
            case 7:
                Iterator<Map.Entry<String, Integer>> it2 = mapRetrieveJavaTimeFieldValueNames.entrySet().iterator();
                while (it2.hasNext()) {
                    arrayList.add(createEntry(it2.next().getKey(), Long.valueOf(toWeekDay(r0.getValue().intValue()))));
                }
                break;
            default:
                Iterator<Map.Entry<String, Integer>> it3 = mapRetrieveJavaTimeFieldValueNames.entrySet().iterator();
                while (it3.hasNext()) {
                    arrayList.add(createEntry(it3.next().getKey(), Long.valueOf(r0.getValue().intValue())));
                }
                break;
        }
        return arrayList.iterator();
    }

    private Object findStore(TemporalField temporalField, Locale locale) {
        Map.Entry<TemporalField, Locale> entryCreateEntry = createEntry(temporalField, locale);
        Object obj = CACHE.get(entryCreateEntry);
        if (obj == null) {
            CACHE.putIfAbsent(entryCreateEntry, createStore(temporalField, locale));
            obj = CACHE.get(entryCreateEntry);
        }
        return obj;
    }

    private static int toWeekDay(int i2) {
        if (i2 == 1) {
            return 7;
        }
        return i2 - 1;
    }

    private Object createStore(TemporalField temporalField, Locale locale) {
        Map<String, Integer> mapRetrieveJavaTimeFieldValueNames;
        String strRetrieveJavaTimeFieldValueName;
        String strRetrieveJavaTimeFieldValueName2;
        Map<String, Integer> mapRetrieveJavaTimeFieldValueNames2;
        HashMap map = new HashMap();
        if (temporalField == ChronoField.ERA) {
            for (TextStyle textStyle : TextStyle.values()) {
                if (!textStyle.isStandalone() && (mapRetrieveJavaTimeFieldValueNames2 = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", 0, textStyle.toCalendarStyle(), locale)) != null) {
                    HashMap map2 = new HashMap();
                    Iterator<Map.Entry<String, Integer>> it = mapRetrieveJavaTimeFieldValueNames2.entrySet().iterator();
                    while (it.hasNext()) {
                        map2.put(Long.valueOf(r0.getValue().intValue()), it.next().getKey());
                    }
                    if (!map2.isEmpty()) {
                        map.put(textStyle, map2);
                    }
                }
            }
            return new LocaleStore(map);
        }
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            for (TextStyle textStyle2 : TextStyle.values()) {
                Map<String, Integer> mapRetrieveJavaTimeFieldValueNames3 = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", 2, textStyle2.toCalendarStyle(), locale);
                HashMap map3 = new HashMap();
                if (mapRetrieveJavaTimeFieldValueNames3 != null) {
                    Iterator<Map.Entry<String, Integer>> it2 = mapRetrieveJavaTimeFieldValueNames3.entrySet().iterator();
                    while (it2.hasNext()) {
                        map3.put(Long.valueOf(r0.getValue().intValue() + 1), it2.next().getKey());
                    }
                } else {
                    for (int i2 = 0; i2 <= 11 && (strRetrieveJavaTimeFieldValueName2 = CalendarDataUtility.retrieveJavaTimeFieldValueName("gregory", 2, i2, textStyle2.toCalendarStyle(), locale)) != null; i2++) {
                        map3.put(Long.valueOf(i2 + 1), strRetrieveJavaTimeFieldValueName2);
                    }
                }
                if (!map3.isEmpty()) {
                    map.put(textStyle2, map3);
                }
            }
            return new LocaleStore(map);
        }
        if (temporalField == ChronoField.DAY_OF_WEEK) {
            for (TextStyle textStyle3 : TextStyle.values()) {
                Map<String, Integer> mapRetrieveJavaTimeFieldValueNames4 = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", 7, textStyle3.toCalendarStyle(), locale);
                HashMap map4 = new HashMap();
                if (mapRetrieveJavaTimeFieldValueNames4 != null) {
                    Iterator<Map.Entry<String, Integer>> it3 = mapRetrieveJavaTimeFieldValueNames4.entrySet().iterator();
                    while (it3.hasNext()) {
                        map4.put(Long.valueOf(toWeekDay(r0.getValue().intValue())), it3.next().getKey());
                    }
                } else {
                    for (int i3 = 1; i3 <= 7 && (strRetrieveJavaTimeFieldValueName = CalendarDataUtility.retrieveJavaTimeFieldValueName("gregory", 7, i3, textStyle3.toCalendarStyle(), locale)) != null; i3++) {
                        map4.put(Long.valueOf(toWeekDay(i3)), strRetrieveJavaTimeFieldValueName);
                    }
                }
                if (!map4.isEmpty()) {
                    map.put(textStyle3, map4);
                }
            }
            return new LocaleStore(map);
        }
        if (temporalField == ChronoField.AMPM_OF_DAY) {
            for (TextStyle textStyle4 : TextStyle.values()) {
                if (!textStyle4.isStandalone() && (mapRetrieveJavaTimeFieldValueNames = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", 9, textStyle4.toCalendarStyle(), locale)) != null) {
                    HashMap map5 = new HashMap();
                    Iterator<Map.Entry<String, Integer>> it4 = mapRetrieveJavaTimeFieldValueNames.entrySet().iterator();
                    while (it4.hasNext()) {
                        map5.put(Long.valueOf(r0.getValue().intValue()), it4.next().getKey());
                    }
                    if (!map5.isEmpty()) {
                        map.put(textStyle4, map5);
                    }
                }
            }
            return new LocaleStore(map);
        }
        if (temporalField == IsoFields.QUARTER_OF_YEAR) {
            String[] strArr = {"QuarterNames", "standalone.QuarterNames", "QuarterAbbreviations", "standalone.QuarterAbbreviations", "QuarterNarrows", "standalone.QuarterNarrows"};
            for (int i4 = 0; i4 < strArr.length; i4++) {
                String[] strArr2 = (String[]) getLocalizedResource(strArr[i4], locale);
                if (strArr2 != null) {
                    HashMap map6 = new HashMap();
                    for (int i5 = 0; i5 < strArr2.length; i5++) {
                        map6.put(Long.valueOf(i5 + 1), strArr2[i5]);
                    }
                    map.put(TextStyle.values()[i4], map6);
                }
            }
            return new LocaleStore(map);
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <A, B> Map.Entry<A, B> createEntry(A a2, B b2) {
        return new AbstractMap.SimpleImmutableEntry(a2, b2);
    }

    static <T> T getLocalizedResource(String str, Locale locale) {
        ResourceBundle javaTimeFormatData = LocaleProviderAdapter.getResourceBundleBased().getLocaleResources(locale).getJavaTimeFormatData();
        if (javaTimeFormatData.containsKey(str)) {
            return (T) javaTimeFormatData.getObject(str);
        }
        return null;
    }

    /* loaded from: rt.jar:java/time/format/DateTimeTextProvider$LocaleStore.class */
    static final class LocaleStore {
        private final Map<TextStyle, Map<Long, String>> valueTextMap;
        private final Map<TextStyle, List<Map.Entry<String, Long>>> parsable;

        /* JADX WARN: Removed duplicated region for block: B:8:0x0065  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        LocaleStore(java.util.Map<java.time.format.TextStyle, java.util.Map<java.lang.Long, java.lang.String>> r6) {
            /*
                r5 = this;
                r0 = r5
                r0.<init>()
                r0 = r5
                r1 = r6
                r0.valueTextMap = r1
                java.util.HashMap r0 = new java.util.HashMap
                r1 = r0
                r1.<init>()
                r7 = r0
                java.util.ArrayList r0 = new java.util.ArrayList
                r1 = r0
                r1.<init>()
                r8 = r0
                r0 = r6
                java.util.Set r0 = r0.entrySet()
                java.util.Iterator r0 = r0.iterator()
                r9 = r0
            L26:
                r0 = r9
                boolean r0 = r0.hasNext()
                if (r0 == 0) goto Ld6
                r0 = r9
                java.lang.Object r0 = r0.next()
                java.util.Map$Entry r0 = (java.util.Map.Entry) r0
                r10 = r0
                java.util.HashMap r0 = new java.util.HashMap
                r1 = r0
                r1.<init>()
                r11 = r0
                r0 = r10
                java.lang.Object r0 = r0.getValue()
                java.util.Map r0 = (java.util.Map) r0
                java.util.Set r0 = r0.entrySet()
                java.util.Iterator r0 = r0.iterator()
                r12 = r0
            L5b:
                r0 = r12
                boolean r0 = r0.hasNext()
                if (r0 == 0) goto L99
                r0 = r12
                java.lang.Object r0 = r0.next()
                java.util.Map$Entry r0 = (java.util.Map.Entry) r0
                r13 = r0
                r0 = r11
                r1 = r13
                java.lang.Object r1 = r1.getValue()
                r2 = r13
                java.lang.Object r2 = r2.getValue()
                r3 = r13
                java.lang.Object r3 = r3.getKey()
                java.util.Map$Entry r2 = java.time.format.DateTimeTextProvider.access$000(r2, r3)
                java.lang.Object r0 = r0.put(r1, r2)
                if (r0 == 0) goto L96
                goto L5b
            L96:
                goto L5b
            L99:
                java.util.ArrayList r0 = new java.util.ArrayList
                r1 = r0
                r2 = r11
                java.util.Collection r2 = r2.values()
                r1.<init>(r2)
                r12 = r0
                r0 = r12
                java.util.Comparator r1 = java.time.format.DateTimeTextProvider.access$100()
                java.util.Collections.sort(r0, r1)
                r0 = r7
                r1 = r10
                java.lang.Object r1 = r1.getKey()
                r2 = r12
                java.lang.Object r0 = r0.put(r1, r2)
                r0 = r8
                r1 = r12
                boolean r0 = r0.addAll(r1)
                r0 = r7
                r1 = 0
                r2 = r8
                java.lang.Object r0 = r0.put(r1, r2)
                goto L26
            Ld6:
                r0 = r8
                java.util.Comparator r1 = java.time.format.DateTimeTextProvider.access$100()
                java.util.Collections.sort(r0, r1)
                r0 = r5
                r1 = r7
                r0.parsable = r1
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeTextProvider.LocaleStore.<init>(java.util.Map):void");
        }

        String getText(long j2, TextStyle textStyle) {
            Map<Long, String> map = this.valueTextMap.get(textStyle);
            if (map != null) {
                return map.get(Long.valueOf(j2));
            }
            return null;
        }

        Iterator<Map.Entry<String, Long>> getTextIterator(TextStyle textStyle) {
            List<Map.Entry<String, Long>> list = this.parsable.get(textStyle);
            if (list != null) {
                return list.iterator();
            }
            return null;
        }
    }
}
