package sun.util.locale.provider;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.spi.TimeZoneNameProvider;
import sun.security.validator.Validator;
import sun.util.calendar.ZoneInfo;
import sun.util.locale.provider.LocaleServiceProviderPool;

/* loaded from: rt.jar:sun/util/locale/provider/TimeZoneNameUtility.class */
public final class TimeZoneNameUtility {
    private static ConcurrentHashMap<Locale, SoftReference<String[][]>> cachedZoneData = new ConcurrentHashMap<>();
    private static final Map<String, SoftReference<Map<Locale, String[]>>> cachedDisplayNames = new ConcurrentHashMap();

    /* JADX WARN: Removed duplicated region for block: B:6:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String[][] getZoneStrings(java.util.Locale r4) {
        /*
            java.util.concurrent.ConcurrentHashMap<java.util.Locale, java.lang.ref.SoftReference<java.lang.String[][]>> r0 = sun.util.locale.provider.TimeZoneNameUtility.cachedZoneData
            r1 = r4
            java.lang.Object r0 = r0.get(r1)
            java.lang.ref.SoftReference r0 = (java.lang.ref.SoftReference) r0
            r6 = r0
            r0 = r6
            if (r0 == 0) goto L1b
            r0 = r6
            java.lang.Object r0 = r0.get()
            java.lang.String[][] r0 = (java.lang.String[][]) r0
            r1 = r0
            r5 = r1
            if (r0 != 0) goto L32
        L1b:
            r0 = r4
            java.lang.String[][] r0 = loadZoneStrings(r0)
            r5 = r0
            java.lang.ref.SoftReference r0 = new java.lang.ref.SoftReference
            r1 = r0
            r2 = r5
            r1.<init>(r2)
            r6 = r0
            java.util.concurrent.ConcurrentHashMap<java.util.Locale, java.lang.ref.SoftReference<java.lang.String[][]>> r0 = sun.util.locale.provider.TimeZoneNameUtility.cachedZoneData
            r1 = r4
            r2 = r6
            java.lang.Object r0 = r0.put(r1, r2)
        L32:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.TimeZoneNameUtility.getZoneStrings(java.util.Locale):java.lang.String[][]");
    }

    private static String[][] loadZoneStrings(Locale locale) {
        TimeZoneNameProvider timeZoneNameProvider = LocaleProviderAdapter.getAdapter(TimeZoneNameProvider.class, locale).getTimeZoneNameProvider();
        if (timeZoneNameProvider instanceof TimeZoneNameProviderImpl) {
            return ((TimeZoneNameProviderImpl) timeZoneNameProvider).getZoneStrings(locale);
        }
        Set<String> zoneIDs = LocaleProviderAdapter.forJRE().getLocaleResources(locale).getZoneIDs();
        LinkedList linkedList = new LinkedList();
        Iterator<String> it = zoneIDs.iterator();
        while (it.hasNext()) {
            String[] strArrRetrieveDisplayNamesImpl = retrieveDisplayNamesImpl(it.next(), locale);
            if (strArrRetrieveDisplayNamesImpl != null) {
                linkedList.add(strArrRetrieveDisplayNamesImpl);
            }
        }
        return (String[][]) linkedList.toArray(new String[linkedList.size()]);
    }

    public static String[] retrieveDisplayNames(String str, Locale locale) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(locale);
        return retrieveDisplayNamesImpl(str, locale);
    }

    public static String retrieveGenericDisplayName(String str, int i2, Locale locale) {
        String[] strArrRetrieveDisplayNamesImpl = retrieveDisplayNamesImpl(str, locale);
        if (Objects.nonNull(strArrRetrieveDisplayNamesImpl)) {
            return strArrRetrieveDisplayNamesImpl[6 - i2];
        }
        return null;
    }

    public static String retrieveDisplayName(String str, boolean z2, int i2, Locale locale) {
        String[] strArrRetrieveDisplayNamesImpl = retrieveDisplayNamesImpl(str, locale);
        if (Objects.nonNull(strArrRetrieveDisplayNamesImpl)) {
            return strArrRetrieveDisplayNamesImpl[(z2 ? 4 : 2) - i2];
        }
        return null;
    }

    private static String[] retrieveDisplayNamesImpl(String str, Locale locale) {
        LocaleServiceProviderPool pool = LocaleServiceProviderPool.getPool(TimeZoneNameProvider.class);
        Map<Locale, String[]> concurrentHashMap = null;
        SoftReference<Map<Locale, String[]>> softReference = cachedDisplayNames.get(str);
        if (Objects.nonNull(softReference)) {
            concurrentHashMap = softReference.get();
            if (Objects.nonNull(concurrentHashMap)) {
                String[] strArr = concurrentHashMap.get(locale);
                if (Objects.nonNull(strArr)) {
                    return strArr;
                }
            }
        }
        String[] strArr2 = new String[7];
        strArr2[0] = str;
        int i2 = 1;
        while (i2 <= 6) {
            strArr2[i2] = (String) pool.getLocalizedObject(TimeZoneNameGetter.INSTANCE, locale, i2 < 5 ? i2 < 3 ? "std" : "dst" : Validator.VAR_GENERIC, Integer.valueOf(i2 % 2), str);
            i2++;
        }
        if (Objects.isNull(concurrentHashMap)) {
            concurrentHashMap = new ConcurrentHashMap();
        }
        concurrentHashMap.put(locale, strArr2);
        cachedDisplayNames.put(str, new SoftReference<>(concurrentHashMap));
        return strArr2;
    }

    /* loaded from: rt.jar:sun/util/locale/provider/TimeZoneNameUtility$TimeZoneNameGetter.class */
    private static class TimeZoneNameGetter implements LocaleServiceProviderPool.LocalizedObjectGetter<TimeZoneNameProvider, String> {
        private static final TimeZoneNameGetter INSTANCE;
        static final /* synthetic */ boolean $assertionsDisabled;

        private TimeZoneNameGetter() {
        }

        static {
            $assertionsDisabled = !TimeZoneNameUtility.class.desiredAssertionStatus();
            INSTANCE = new TimeZoneNameGetter();
        }

        @Override // sun.util.locale.provider.LocaleServiceProviderPool.LocalizedObjectGetter
        public String getObject(TimeZoneNameProvider timeZoneNameProvider, Locale locale, String str, Object... objArr) {
            Map<String, String> aliasTable;
            if (!$assertionsDisabled && objArr.length != 2) {
                throw new AssertionError();
            }
            int iIntValue = ((Integer) objArr[0]).intValue();
            String str2 = (String) objArr[1];
            String name = getName(timeZoneNameProvider, locale, str, iIntValue, str2);
            if (name == null && (aliasTable = ZoneInfo.getAliasTable()) != null) {
                String str3 = aliasTable.get(str2);
                if (str3 != null) {
                    name = getName(timeZoneNameProvider, locale, str, iIntValue, str3);
                }
                if (name == null) {
                    name = examineAliases(timeZoneNameProvider, locale, str, str3 != null ? str3 : str2, iIntValue, aliasTable);
                }
            }
            return name;
        }

        private static String examineAliases(TimeZoneNameProvider timeZoneNameProvider, Locale locale, String str, String str2, int i2, Map<String, String> map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue().equals(str2)) {
                    String key = entry.getKey();
                    String name = getName(timeZoneNameProvider, locale, str, i2, key);
                    if (name != null) {
                        return name;
                    }
                    String strExamineAliases = examineAliases(timeZoneNameProvider, locale, str, key, i2, map);
                    if (strExamineAliases != null) {
                        return strExamineAliases;
                    }
                }
            }
            return null;
        }

        private static String getName(TimeZoneNameProvider timeZoneNameProvider, Locale locale, String str, int i2, String str2) {
            String genericDisplayName;
            genericDisplayName = null;
            switch (str) {
                case "std":
                    genericDisplayName = timeZoneNameProvider.getDisplayName(str2, false, i2, locale);
                    break;
                case "dst":
                    genericDisplayName = timeZoneNameProvider.getDisplayName(str2, true, i2, locale);
                    break;
                case "generic":
                    genericDisplayName = timeZoneNameProvider.getGenericDisplayName(str2, i2, locale);
                    break;
            }
            return genericDisplayName;
        }
    }

    private TimeZoneNameUtility() {
    }
}
