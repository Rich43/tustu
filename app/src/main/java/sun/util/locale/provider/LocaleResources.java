package sun.util.locale.provider;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.util.calendar.ZoneInfo;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.resources.LocaleData;
import sun.util.resources.OpenListResourceBundle;
import sun.util.resources.ParallelListResourceBundle;
import sun.util.resources.TimeZoneNamesBundle;

/* loaded from: rt.jar:sun/util/locale/provider/LocaleResources.class */
public class LocaleResources {
    private final Locale locale;
    private final LocaleData localeData;
    private final LocaleProviderAdapter.Type type;
    private ConcurrentMap<String, ResourceReference> cache = new ConcurrentHashMap();
    private ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    private static final String BREAK_ITERATOR_INFO = "BII.";
    private static final String CALENDAR_DATA = "CALD.";
    private static final String COLLATION_DATA_CACHEKEY = "COLD";
    private static final String DECIMAL_FORMAT_SYMBOLS_DATA_CACHEKEY = "DFSD";
    private static final String CURRENCY_NAMES = "CN.";
    private static final String LOCALE_NAMES = "LN.";
    private static final String TIME_ZONE_NAMES = "TZN.";
    private static final String ZONE_IDS_CACHEKEY = "ZID";
    private static final String CALENDAR_NAMES = "CALN.";
    private static final String NUMBER_PATTERNS_CACHEKEY = "NP";
    private static final String DATE_TIME_PATTERN = "DTP.";
    private static final Object NULLOBJECT;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LocaleResources.class.desiredAssertionStatus();
        NULLOBJECT = new Object();
    }

    /* JADX WARN: Multi-variable type inference failed */
    LocaleResources(ResourceBundleBasedAdapter resourceBundleBasedAdapter, Locale locale) {
        this.locale = locale;
        this.localeData = resourceBundleBasedAdapter.getLocaleData();
        this.type = ((LocaleProviderAdapter) resourceBundleBasedAdapter).getAdapterType();
    }

    private void removeEmptyReferences() {
        while (true) {
            Reference<? extends Object> referencePoll = this.referenceQueue.poll();
            if (referencePoll != null) {
                this.cache.remove(((ResourceReference) referencePoll).getCacheKey());
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.Object getBreakIteratorInfo(java.lang.String r9) {
        /*
            r8 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "BII."
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r9
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r11 = r0
            r0 = r8
            r0.removeEmptyReferences()
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            java.lang.Object r0 = r0.get(r1)
            sun.util.locale.provider.LocaleResources$ResourceReference r0 = (sun.util.locale.provider.LocaleResources.ResourceReference) r0
            r12 = r0
            r0 = r12
            if (r0 == 0) goto L36
            r0 = r12
            java.lang.Object r0 = r0.get()
            r1 = r0
            r10 = r1
            if (r0 != 0) goto L5e
        L36:
            r0 = r8
            sun.util.resources.LocaleData r0 = r0.localeData
            r1 = r8
            java.util.Locale r1 = r1.locale
            java.util.ResourceBundle r0 = r0.getBreakIteratorInfo(r1)
            r1 = r9
            java.lang.Object r0 = r0.getObject(r1)
            r10 = r0
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            sun.util.locale.provider.LocaleResources$ResourceReference r2 = new sun.util.locale.provider.LocaleResources$ResourceReference
            r3 = r2
            r4 = r11
            r5 = r10
            r6 = r8
            java.lang.ref.ReferenceQueue<java.lang.Object> r6 = r6.referenceQueue
            r3.<init>(r4, r5, r6)
            java.lang.Object r0 = r0.put(r1, r2)
        L5e:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getBreakIteratorInfo(java.lang.String):java.lang.Object");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    int getCalendarData(java.lang.String r9) {
        /*
            r8 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "CALD."
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r9
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r11 = r0
            r0 = r8
            r0.removeEmptyReferences()
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            java.lang.Object r0 = r0.get(r1)
            sun.util.locale.provider.LocaleResources$ResourceReference r0 = (sun.util.locale.provider.LocaleResources.ResourceReference) r0
            r12 = r0
            r0 = r12
            if (r0 == 0) goto L39
            r0 = r12
            java.lang.Object r0 = r0.get()
            java.lang.Integer r0 = (java.lang.Integer) r0
            r1 = r0
            r10 = r1
            if (r0 != 0) goto L7c
        L39:
            r0 = r8
            sun.util.resources.LocaleData r0 = r0.localeData
            r1 = r8
            java.util.Locale r1 = r1.locale
            java.util.ResourceBundle r0 = r0.getCalendarData(r1)
            r13 = r0
            r0 = r13
            r1 = r9
            boolean r0 = r0.containsKey(r1)
            if (r0 == 0) goto L5f
            r0 = r13
            r1 = r9
            java.lang.String r0 = r0.getString(r1)
            int r0 = java.lang.Integer.parseInt(r0)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r10 = r0
            goto L64
        L5f:
            r0 = 0
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r10 = r0
        L64:
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            sun.util.locale.provider.LocaleResources$ResourceReference r2 = new sun.util.locale.provider.LocaleResources$ResourceReference
            r3 = r2
            r4 = r11
            r5 = r10
            r6 = r8
            java.lang.ref.ReferenceQueue<java.lang.Object> r6 = r6.referenceQueue
            r3.<init>(r4, r5, r6)
            java.lang.Object r0 = r0.put(r1, r2)
        L7c:
            r0 = r10
            int r0 = r0.intValue()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getCalendarData(java.lang.String):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0029 A[PHI: r10
  0x0029: PHI (r10v1 java.lang.String) = (r10v0 java.lang.String), (r10v5 java.lang.String) binds: [B:3:0x001a, B:5:0x0026] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getCollationData() {
        /*
            r8 = this;
            java.lang.String r0 = "Rule"
            r9 = r0
            java.lang.String r0 = ""
            r10 = r0
            r0 = r8
            r0.removeEmptyReferences()
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            java.lang.String r1 = "COLD"
            java.lang.Object r0 = r0.get(r1)
            sun.util.locale.provider.LocaleResources$ResourceReference r0 = (sun.util.locale.provider.LocaleResources.ResourceReference) r0
            r11 = r0
            r0 = r11
            if (r0 == 0) goto L29
            r0 = r11
            java.lang.Object r0 = r0.get()
            java.lang.String r0 = (java.lang.String) r0
            r1 = r0
            r10 = r1
            if (r0 != 0) goto L60
        L29:
            r0 = r8
            sun.util.resources.LocaleData r0 = r0.localeData
            r1 = r8
            java.util.Locale r1 = r1.locale
            java.util.ResourceBundle r0 = r0.getCollationData(r1)
            r12 = r0
            r0 = r12
            r1 = r9
            boolean r0 = r0.containsKey(r1)
            if (r0 == 0) goto L46
            r0 = r12
            r1 = r9
            java.lang.String r0 = r0.getString(r1)
            r10 = r0
        L46:
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            java.lang.String r1 = "COLD"
            sun.util.locale.provider.LocaleResources$ResourceReference r2 = new sun.util.locale.provider.LocaleResources$ResourceReference
            r3 = r2
            java.lang.String r4 = "COLD"
            r5 = r10
            r6 = r8
            java.lang.ref.ReferenceQueue<java.lang.Object> r6 = r6.referenceQueue
            r3.<init>(r4, r5, r6)
            java.lang.Object r0 = r0.put(r1, r2)
        L60:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getCollationData():java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object[] getDecimalFormatSymbolsData() {
        /*
            Method dump skipped, instructions count: 213
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getDecimalFormatSymbolsData():java.lang.Object[]");
    }

    public String getCurrencyName(String str) {
        Object object = null;
        String str2 = CURRENCY_NAMES + str;
        removeEmptyReferences();
        ResourceReference resourceReference = this.cache.get(str2);
        if (resourceReference != null) {
            Object obj = resourceReference.get();
            object = obj;
            if (obj != null) {
                if (object.equals(NULLOBJECT)) {
                    object = null;
                }
                return (String) object;
            }
        }
        OpenListResourceBundle currencyNames = this.localeData.getCurrencyNames(this.locale);
        if (currencyNames.containsKey(str)) {
            object = currencyNames.getObject(str);
            this.cache.put(str2, new ResourceReference(str2, object, this.referenceQueue));
        }
        return (String) object;
    }

    public String getLocaleName(String str) {
        Object object = null;
        String str2 = LOCALE_NAMES + str;
        removeEmptyReferences();
        ResourceReference resourceReference = this.cache.get(str2);
        if (resourceReference != null) {
            Object obj = resourceReference.get();
            object = obj;
            if (obj != null) {
                if (object.equals(NULLOBJECT)) {
                    object = null;
                }
                return (String) object;
            }
        }
        OpenListResourceBundle localeNames = this.localeData.getLocaleNames(this.locale);
        if (localeNames.containsKey(str)) {
            object = localeNames.getObject(str);
            this.cache.put(str2, new ResourceReference(str2, object, this.referenceQueue));
        }
        return (String) object;
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0044 A[PHI: r10
  0x0044: PHI (r10v1 java.lang.String[]) = (r10v0 java.lang.String[]), (r10v4 java.lang.String[]) binds: [B:3:0x002e, B:5:0x0041] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.String[] getTimeZoneNames(java.lang.String r9) {
        /*
            r8 = this;
            r0 = 0
            r10 = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "TZN.."
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r9
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r11 = r0
            r0 = r8
            r0.removeEmptyReferences()
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            java.lang.Object r0 = r0.get(r1)
            sun.util.locale.provider.LocaleResources$ResourceReference r0 = (sun.util.locale.provider.LocaleResources.ResourceReference) r0
            r12 = r0
            r0 = r12
            boolean r0 = java.util.Objects.isNull(r0)
            if (r0 != 0) goto L44
            r0 = r12
            java.lang.Object r0 = r0.get()
            java.lang.String[] r0 = (java.lang.String[]) r0
            java.lang.String[] r0 = (java.lang.String[]) r0
            r1 = r0
            r10 = r1
            boolean r0 = java.util.Objects.isNull(r0)
            if (r0 == 0) goto L79
        L44:
            r0 = r8
            sun.util.resources.LocaleData r0 = r0.localeData
            r1 = r8
            java.util.Locale r1 = r1.locale
            sun.util.resources.TimeZoneNamesBundle r0 = r0.getTimeZoneNames(r1)
            r13 = r0
            r0 = r13
            r1 = r9
            boolean r0 = r0.containsKey(r1)
            if (r0 == 0) goto L79
            r0 = r13
            r1 = r9
            java.lang.String[] r0 = r0.getStringArray(r1)
            r10 = r0
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            sun.util.locale.provider.LocaleResources$ResourceReference r2 = new sun.util.locale.provider.LocaleResources$ResourceReference
            r3 = r2
            r4 = r11
            r5 = r10
            r6 = r8
            java.lang.ref.ReferenceQueue<java.lang.Object> r6 = r6.referenceQueue
            r3.<init>(r4, r5, r6)
            java.lang.Object r0 = r0.put(r1, r2)
        L79:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getTimeZoneNames(java.lang.String):java.lang.String[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.util.Set<java.lang.String> getZoneIDs() {
        /*
            r8 = this;
            r0 = 0
            r9 = r0
            r0 = r8
            r0.removeEmptyReferences()
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            java.lang.String r1 = "ZID"
            java.lang.Object r0 = r0.get(r1)
            sun.util.locale.provider.LocaleResources$ResourceReference r0 = (sun.util.locale.provider.LocaleResources.ResourceReference) r0
            r10 = r0
            r0 = r10
            if (r0 == 0) goto L25
            r0 = r10
            java.lang.Object r0 = r0.get()
            java.util.Set r0 = (java.util.Set) r0
            r1 = r0
            r9 = r1
            if (r0 != 0) goto L50
        L25:
            r0 = r8
            sun.util.resources.LocaleData r0 = r0.localeData
            r1 = r8
            java.util.Locale r1 = r1.locale
            sun.util.resources.TimeZoneNamesBundle r0 = r0.getTimeZoneNames(r1)
            r11 = r0
            r0 = r11
            java.util.Set r0 = r0.keySet()
            r9 = r0
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            java.lang.String r1 = "ZID"
            sun.util.locale.provider.LocaleResources$ResourceReference r2 = new sun.util.locale.provider.LocaleResources$ResourceReference
            r3 = r2
            java.lang.String r4 = "ZID"
            r5 = r9
            r6 = r8
            java.lang.ref.ReferenceQueue<java.lang.Object> r6 = r6.referenceQueue
            r3.<init>(r4, r5, r6)
            java.lang.Object r0 = r0.put(r1, r2)
        L50:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getZoneIDs():java.util.Set");
    }

    String[][] getZoneStrings() {
        TimeZoneNamesBundle timeZoneNames = this.localeData.getTimeZoneNames(this.locale);
        Set<String> zoneIDs = getZoneIDs();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Iterator<String> it = zoneIDs.iterator();
        while (it.hasNext()) {
            linkedHashSet.add(timeZoneNames.getStringArray(it.next()));
        }
        if (this.type == LocaleProviderAdapter.Type.CLDR) {
            Map<String, String> aliasTable = ZoneInfo.getAliasTable();
            for (String str : aliasTable.keySet()) {
                if (!zoneIDs.contains(str)) {
                    String str2 = aliasTable.get(str);
                    if (zoneIDs.contains(str2)) {
                        String[] stringArray = timeZoneNames.getStringArray(str2);
                        stringArray[0] = str;
                        linkedHashSet.add(stringArray);
                    }
                }
            }
        }
        return (String[][]) linkedHashSet.toArray(new String[0]);
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x003e A[PHI: r10
  0x003e: PHI (r10v1 java.lang.String[]) = (r10v0 java.lang.String[]), (r10v4 java.lang.String[]) binds: [B:3:0x002b, B:5:0x003b] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.String[] getCalendarNames(java.lang.String r9) {
        /*
            r8 = this;
            r0 = 0
            r10 = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "CALN."
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r9
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r11 = r0
            r0 = r8
            r0.removeEmptyReferences()
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            java.lang.Object r0 = r0.get(r1)
            sun.util.locale.provider.LocaleResources$ResourceReference r0 = (sun.util.locale.provider.LocaleResources.ResourceReference) r0
            r12 = r0
            r0 = r12
            if (r0 == 0) goto L3e
            r0 = r12
            java.lang.Object r0 = r0.get()
            java.lang.String[] r0 = (java.lang.String[]) r0
            java.lang.String[] r0 = (java.lang.String[]) r0
            r1 = r0
            r10 = r1
            if (r0 != 0) goto L73
        L3e:
            r0 = r8
            sun.util.resources.LocaleData r0 = r0.localeData
            r1 = r8
            java.util.Locale r1 = r1.locale
            java.util.ResourceBundle r0 = r0.getDateFormatData(r1)
            r13 = r0
            r0 = r13
            r1 = r9
            boolean r0 = r0.containsKey(r1)
            if (r0 == 0) goto L73
            r0 = r13
            r1 = r9
            java.lang.String[] r0 = r0.getStringArray(r1)
            r10 = r0
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            sun.util.locale.provider.LocaleResources$ResourceReference r2 = new sun.util.locale.provider.LocaleResources$ResourceReference
            r3 = r2
            r4 = r11
            r5 = r10
            r6 = r8
            java.lang.ref.ReferenceQueue<java.lang.Object> r6 = r6.referenceQueue
            r3.<init>(r4, r5, r6)
            java.lang.Object r0 = r0.put(r1, r2)
        L73:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getCalendarNames(java.lang.String):java.lang.String[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x003e A[PHI: r10
  0x003e: PHI (r10v1 java.lang.String[]) = (r10v0 java.lang.String[]), (r10v4 java.lang.String[]) binds: [B:3:0x002b, B:5:0x003b] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.String[] getJavaTimeNames(java.lang.String r9) {
        /*
            r8 = this;
            r0 = 0
            r10 = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "CALN."
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r9
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r11 = r0
            r0 = r8
            r0.removeEmptyReferences()
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            java.lang.Object r0 = r0.get(r1)
            sun.util.locale.provider.LocaleResources$ResourceReference r0 = (sun.util.locale.provider.LocaleResources.ResourceReference) r0
            r12 = r0
            r0 = r12
            if (r0 == 0) goto L3e
            r0 = r12
            java.lang.Object r0 = r0.get()
            java.lang.String[] r0 = (java.lang.String[]) r0
            java.lang.String[] r0 = (java.lang.String[]) r0
            r1 = r0
            r10 = r1
            if (r0 != 0) goto L6c
        L3e:
            r0 = r8
            java.util.ResourceBundle r0 = r0.getJavaTimeFormatData()
            r13 = r0
            r0 = r13
            r1 = r9
            boolean r0 = r0.containsKey(r1)
            if (r0 == 0) goto L6c
            r0 = r13
            r1 = r9
            java.lang.String[] r0 = r0.getStringArray(r1)
            r10 = r0
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            r1 = r11
            sun.util.locale.provider.LocaleResources$ResourceReference r2 = new sun.util.locale.provider.LocaleResources$ResourceReference
            r3 = r2
            r4 = r11
            r5 = r10
            r6 = r8
            java.lang.ref.ReferenceQueue<java.lang.Object> r6 = r6.referenceQueue
            r3.<init>(r4, r5, r6)
            java.lang.Object r0 = r0.put(r1, r2)
        L6c:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getJavaTimeNames(java.lang.String):java.lang.String[]");
    }

    public String getDateTimePattern(int i2, int i3, Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance(this.locale);
        }
        return getDateTimePattern((String) null, i2, i3, calendar.getCalendarType());
    }

    public String getJavaTimeDateTimePattern(int i2, int i3, String str) {
        String strNormalizeCalendarType = CalendarDataUtility.normalizeCalendarType(str);
        String dateTimePattern = getDateTimePattern("java.time.", i2, i3, strNormalizeCalendarType);
        if (dateTimePattern == null) {
            dateTimePattern = getDateTimePattern((String) null, i2, i3, strNormalizeCalendarType);
        }
        return dateTimePattern;
    }

    private String getDateTimePattern(String str, int i2, int i3, String str2) {
        String str3;
        String dateTimePattern;
        String dateTimePattern2 = null;
        String dateTimePattern3 = null;
        if (i2 >= 0) {
            if (str != null) {
                dateTimePattern2 = getDateTimePattern(str, "TimePatterns", i2, str2);
            }
            if (dateTimePattern2 == null) {
                dateTimePattern2 = getDateTimePattern((String) null, "TimePatterns", i2, str2);
            }
        }
        if (i3 >= 0) {
            if (str != null) {
                dateTimePattern3 = getDateTimePattern(str, "DatePatterns", i3, str2);
            }
            if (dateTimePattern3 == null) {
                dateTimePattern3 = getDateTimePattern((String) null, "DatePatterns", i3, str2);
            }
        }
        if (i2 >= 0) {
            if (i3 >= 0) {
                dateTimePattern = null;
                if (str != null) {
                    dateTimePattern = getDateTimePattern(str, "DateTimePatterns", 0, str2);
                }
                if (dateTimePattern == null) {
                    dateTimePattern = getDateTimePattern((String) null, "DateTimePatterns", 0, str2);
                }
                switch (dateTimePattern) {
                    case "{1} {0}":
                        str3 = dateTimePattern3 + " " + dateTimePattern2;
                        break;
                    case "{0} {1}":
                        str3 = dateTimePattern2 + " " + dateTimePattern3;
                        break;
                    default:
                        str3 = MessageFormat.format(dateTimePattern, dateTimePattern2, dateTimePattern3);
                        break;
                }
            } else {
                str3 = dateTimePattern2;
            }
        } else if (i3 >= 0) {
            str3 = dateTimePattern3;
        } else {
            throw new IllegalArgumentException("No date or time style specified");
        }
        return str3;
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0028  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String[] getNumberPatterns() {
        /*
            r8 = this;
            r0 = 0
            r9 = r0
            r0 = r8
            r0.removeEmptyReferences()
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            java.lang.String r1 = "NP"
            java.lang.Object r0 = r0.get(r1)
            sun.util.locale.provider.LocaleResources$ResourceReference r0 = (sun.util.locale.provider.LocaleResources.ResourceReference) r0
            r10 = r0
            r0 = r10
            if (r0 == 0) goto L28
            r0 = r10
            java.lang.Object r0 = r0.get()
            java.lang.String[] r0 = (java.lang.String[]) r0
            java.lang.String[] r0 = (java.lang.String[]) r0
            r1 = r0
            r9 = r1
            if (r0 != 0) goto L55
        L28:
            r0 = r8
            sun.util.resources.LocaleData r0 = r0.localeData
            r1 = r8
            java.util.Locale r1 = r1.locale
            java.util.ResourceBundle r0 = r0.getNumberFormatData(r1)
            r11 = r0
            r0 = r11
            java.lang.String r1 = "NumberPatterns"
            java.lang.String[] r0 = r0.getStringArray(r1)
            r9 = r0
            r0 = r8
            java.util.concurrent.ConcurrentMap<java.lang.String, sun.util.locale.provider.LocaleResources$ResourceReference> r0 = r0.cache
            java.lang.String r1 = "NP"
            sun.util.locale.provider.LocaleResources$ResourceReference r2 = new sun.util.locale.provider.LocaleResources$ResourceReference
            r3 = r2
            java.lang.String r4 = "NP"
            r5 = r9
            r6 = r8
            java.lang.ref.ReferenceQueue<java.lang.Object> r6 = r6.referenceQueue
            r3.<init>(r4, r5, r6)
            java.lang.Object r0 = r0.put(r1, r2)
        L55:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getNumberPatterns():java.lang.String[]");
    }

    public ResourceBundle getJavaTimeFormatData() {
        ResourceBundle dateFormatData = this.localeData.getDateFormatData(this.locale);
        if (dateFormatData instanceof ParallelListResourceBundle) {
            this.localeData.setSupplementary((ParallelListResourceBundle) dateFormatData);
        }
        return dateFormatData;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x006f A[PHI: r17
  0x006f: PHI (r17v1 java.lang.Object) = (r17v0 java.lang.Object), (r17v6 java.lang.Object) binds: [B:9:0x0061, B:11:0x006c] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String getDateTimePattern(java.lang.String r9, java.lang.String r10, int r11, java.lang.String r12) {
        /*
            Method dump skipped, instructions count: 263
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleResources.getDateTimePattern(java.lang.String, java.lang.String, int, java.lang.String):java.lang.String");
    }

    /* loaded from: rt.jar:sun/util/locale/provider/LocaleResources$ResourceReference.class */
    private static class ResourceReference extends SoftReference<Object> {
        private final String cacheKey;

        ResourceReference(String str, Object obj, ReferenceQueue<Object> referenceQueue) {
            super(obj, referenceQueue);
            this.cacheKey = str;
        }

        String getCacheKey() {
            return this.cacheKey;
        }
    }
}
